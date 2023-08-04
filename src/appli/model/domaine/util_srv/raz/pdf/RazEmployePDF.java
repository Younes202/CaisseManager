package appli.model.domaine.util_srv.raz.pdf;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import appli.model.domaine.vente.persistant.CaisseJourneePersistant;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.PdfBean;
import framework.model.util.PdfUtil;
import framework.model.util.PdfUtil.EnumBorder;

public class RazEmployePDF {
	
	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */
	public File getPdf(String titre, String date, 
    		Long currentUserId,
    		String type, List<CaisseJourneePersistant> listDataShift, 
    		Map<String, Map> mapEmpl,
    		BigDecimal fraisLivraison) {
		PdfBean pdfBean = null;
		Document document = null;
		
		try{
			pdfBean = PdfUtil.creerDocument("raz_employe_"+DateUtil.dateToString(new Date(), "dd-MM-yyyy"));
			document = pdfBean.getDocument();
			
			ajouterEntete(document, titre, date);
			ajouterContenu(document, currentUserId, type, listDataShift, mapEmpl, fraisLivraison);	
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally{			
			document.close();
		}
		
		return pdfBean.getPdfFile();
	}

	/**
	 * @param document
	 * @param cupP
	 * @throws DocumentException 
	 */
	private void ajouterEntete(Document document, String titre, String date) throws DocumentException{
		PdfPTable table = new PdfPTable(1);
	    table.setWidthPercentage(100);
	    float[] widths = {100f};
		table.setWidths(widths);// largeur par cellule
		
		PdfPCell cell = PdfUtil.getCellVide(document);
		PdfUtil.effacerBordure(cell, EnumBorder.NO_BORDER);
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, titre, Element.ALIGN_CENTER, PdfUtil.FONT_14_BOLD_BLACK);
		cell.setBackgroundColor(PdfUtil.GRIS_CLAIR);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setFixedHeight(25);
		table.addCell(cell);

		cell = PdfUtil.getCell(document, date, Element.ALIGN_CENTER, PdfUtil.FONT_12_NORMAL_BLACK);
		cell.setBackgroundColor(PdfUtil.GRIS_LEGER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);
		
		cell = PdfUtil.getCellVide(document);
		PdfUtil.effacerBordure(cell, EnumBorder.NO_BORDER);
		table.addCell(cell);
		
		document.add(table);
		PdfUtil.ajouterLigneVide(document, 1);
	}
	
	/**
	 * @param document
	 * @param cupP
	 */
	private void ajouterContenu(Document document, Long currentUserId,
    		String type, List<CaisseJourneePersistant> listDataShift, 
    		Map<String, Map> mapEmpl,
    		BigDecimal fraisLivraison) throws DocumentException {
	    PdfPTable table = new PdfPTable(2);
	    table.setWidthPercentage(100f);
	    float[] widths2 = {80f, 20f};
		table.setWidths(widths2);// largeur par cellule
		
		PdfUtil.ajouterligneSeparateur(document);
		table = new PdfPTable(2);
	    table.setWidthPercentage(100f);
	    float[] widths3 = {70f, 30f};
		table.setWidths(widths3);// largeur par cellule
		
        String oldCaisse = null;
		int idxShift = 0;
		for(CaisseJourneePersistant data : listDataShift){
			if(type.equals("RE") && !data.getOpc_user().getId().equals(currentUserId)){
				continue;
			}
			
 			boolean isDblCloture = (data.getMtt_cloture_old_espece() != null);
 			BigDecimal mttClotureCaisserEsp = isDblCloture ? data.getMtt_cloture_old_espece() : data.getMtt_cloture_caissier_espece();
 			BigDecimal mttClotureCaisserCheq = isDblCloture ? data.getMtt_cloture_old_cheque() : data.getMtt_cloture_caissier_cheque();
 			BigDecimal mttClotureCaisserDej = isDblCloture ? data.getMtt_cloture_old_dej() : data.getMtt_cloture_caissier_dej();
 			BigDecimal mttClotureCaisserCb = isDblCloture ? data.getMtt_cloture_old_cb() : data.getMtt_cloture_caissier_cb();
 			BigDecimal mttClotureCaissier = BigDecimalUtil.add(mttClotureCaisserEsp, mttClotureCaisserCheq, mttClotureCaisserDej, mttClotureCaisserCb);
 			
 			BigDecimal mttClotureCaissierNet = (mttClotureCaisserEsp==null?null:BigDecimalUtil.substract(mttClotureCaissier, data.getMtt_ouverture()));
 			BigDecimal mttClotureCaissierEspNet = (mttClotureCaisserEsp==null?null:BigDecimalUtil.substract(mttClotureCaisserEsp, data.getMtt_ouverture()));
 			// On retire les montants non calculable
 			BigDecimal mttRef = BigDecimalUtil.substract(data.getMtt_total_net(), data.getMtt_portefeuille(), data.getMtt_donne_point());
 			BigDecimal ecartCaissier = BigDecimalUtil.substract(mttClotureCaissierNet, mttRef);
 			
			idxShift++;
			
			if(oldCaisse == null || !oldCaisse.equals(data.getOpc_caisse().getReference())){
				idxShift = 1;
				addCellTitre(document, table, data.getOpc_caisse().getReference());
			}
			oldCaisse = data.getOpc_caisse().getReference();
			
			// Detail shift
			String shift = " [Shift "+idxShift;
			shift += " : ";
			if("O".equals(data.getStatut_caisse())){
				shift += "Ouvert";
			} else if("E".equals(data.getStatut_caisse())){
				shift += "En Cloture";
			} else{
				shift += "Clos";
			}
			shift += "]";
			String dateOuv = " ---- "+DateUtil.dateToString(data.getDate_ouverture(), "HH:mm:ss")
			+(data.getDate_cloture()!=null ? "à "+StringUtil.getValueOrEmpty(DateUtil.dateToString(data.getDate_cloture(), "HH:mm:ss")): "");

			shift += dateOuv;			
			
			String ouvr = " ----- Ouverture :";
			if(data.getOpc_user() != null){
				if(data.getOpc_user().getOpc_employe()==null){
					ouvr += data.getOpc_user().getLogin();
				} else{
					ouvr += data.getOpc_user().getOpc_employe().getNom() + StringUtil.getValueOrEmpty(data.getOpc_user().getOpc_employe().getPrenom());
				}
			}
			shift += ouvr;
			
			if(data.getOpc_user_cloture() != null){
				String clo = "Clôture :";
				if(data.getOpc_user_cloture().getOpc_employe()==null){
					clo += data.getOpc_user_cloture().getLogin();
				} else{
					clo += data.getOpc_user_cloture().getOpc_employe().getNom() + StringUtil.getValueOrEmpty(data.getOpc_user_cloture().getOpc_employe().getPrenom());
				}
				shift += clo;
			}
			addCellTitre(document, table, shift);
			
			// Mtt total
			addCellTitre(document, table, "DÉTAIL CHIFFRES");
//			addCellData(document, table, "Montant total vente brut", BigDecimalUtil.formatNumberZero(mapEmpl.get(data.getOpc_user().getLogin())));
			addCellData(document, table, "Fonds de roulement ", BigDecimalUtil.formatNumberZero(data.getMtt_ouverture()));
			addCellData(document, table, "Montant net vente", BigDecimalUtil.formatNumberZero(data.getMtt_total_net()));
			addCellData(document, table, "Nbr ventes", BigDecimalUtil.formatNumberZero(data.getNbr_vente()));
			
			if(!BigDecimalUtil.isZero(data.getMtt_annule())){
				addCellData(document, table, "Annulations CMD ", BigDecimalUtil.formatNumberZero(data.getMtt_annule()));
			}
			if(!BigDecimalUtil.isZero(data.getMtt_annule_ligne())){
				addCellData(document, table, "Annulations Ligne ", BigDecimalUtil.formatNumberZero(data.getMtt_annule_ligne()));
			}
			if(fraisLivraison != null && data.getNbr_livraison() != null && data.getNbr_livraison() > 0){
				String val = BigDecimalUtil.formatNumber(BigDecimalUtil.multiply(fraisLivraison, BigDecimalUtil.get((data.getNbr_livraison()==null?0:data.getNbr_livraison()))));
				addCellData(document, table, "Livraisons ", val);
			} 
			if(!BigDecimalUtil.isZero(data.getMtt_reduction())){
				addCellData(document, table, "Réductions ", BigDecimalUtil.formatNumberZero(data.getMtt_reduction()));
			}
			if(!BigDecimalUtil.isZero(data.getMtt_art_offert())){
				addCellData(document, table, "Offerts ", BigDecimalUtil.formatNumberZero(data.getMtt_art_offert()));
			}
			
			// Detail
			addCellTitre(document, table, "RÉPARTITION DES MONTANTS");
			if(!BigDecimalUtil.isZero(data.getMtt_espece())){
				addCellData(document, table, "Especes ", BigDecimalUtil.formatNumberZero(data.getMtt_espece()));
			}
			if(!BigDecimalUtil.isZero(data.getMtt_cheque())){
				addCellData(document, table, "Chèques ", BigDecimalUtil.formatNumberZero(data.getMtt_cheque()));
			}
			if(!BigDecimalUtil.isZero(data.getMtt_dej())){
				addCellData(document, table, "Chèques déj.", BigDecimalUtil.formatNumberZero(data.getMtt_dej()));
			}
			if(!BigDecimalUtil.isZero(data.getMtt_cb())){
				addCellData(document, table, "Carte bancaire ", BigDecimalUtil.formatNumberZero(data.getMtt_cb()));
			}
			if(!BigDecimalUtil.isZero(data.getMtt_portefeuille())){
				addCellData(document, table, "Portefeuille ", BigDecimalUtil.formatNumberZero(data.getMtt_portefeuille()));
			} 
			if(!BigDecimalUtil.isZero(data.getMtt_donne_point())){ 
				addCellData(document, table, "Points ", BigDecimalUtil.formatNumberZero(data.getMtt_donne_point()));
			} 
			
			addCellTitre(document, table, "CLÔTURE CAISSE");
			if(!BigDecimalUtil.isZero(mttClotureCaissierEspNet)){ 
				addCellData(document, table, "Clôture espèces", BigDecimalUtil.formatNumberZero(mttClotureCaissierEspNet));
			}
			if(!BigDecimalUtil.isZero(mttClotureCaisserCheq)){ 
				addCellData(document, table, "Clôture chèque", BigDecimalUtil.formatNumberZero(mttClotureCaisserCheq));
			}
			if(!BigDecimalUtil.isZero(mttClotureCaisserDej)){ 
				addCellData(document, table, "Clôture déj", BigDecimalUtil.formatNumberZero(mttClotureCaisserDej));
			}
			if(!BigDecimalUtil.isZero(mttClotureCaisserCb)){ 
				addCellData(document, table, "Clôture carte", BigDecimalUtil.formatNumberZero(mttClotureCaisserCb));
			}
			
			if("C".equals(data.getStatut_caisse())){
				addCellTitre(document, table, "ÉCARTS DE VETE");
				BigDecimal ecartEspeses = BigDecimalUtil.substract(mttClotureCaissierEspNet, data.getMtt_espece());
				BigDecimal ecartCb = BigDecimalUtil.substract(mttClotureCaisserCb, data.getMtt_cb());
				BigDecimal ecartCheque = BigDecimalUtil.substract(mttClotureCaisserCheq, data.getMtt_cheque());
				BigDecimal ecartDej = BigDecimalUtil.substract(mttClotureCaisserDej, data.getMtt_dej());
				
				if(!BigDecimalUtil.isZero(ecartEspeses)){ 
					addCellData(document, table, "Ecart espèces",  BigDecimalUtil.formatNumberZero(ecartEspeses));
				}
				if(!BigDecimalUtil.isZero(ecartCheque)){ 
					addCellData(document, table, "Ecart chèque", BigDecimalUtil.formatNumberZero(ecartCheque));
				}
				if(!BigDecimalUtil.isZero(ecartDej)){ 
					addCellData(document, table, "Ecart déj", BigDecimalUtil.formatNumberZero(ecartDej));
				}
				if(!BigDecimalUtil.isZero(ecartCb)){ 
					addCellData(document, table, "Ecart carte", BigDecimalUtil.formatNumberZero(ecartCb));
				}
				if(!BigDecimalUtil.isZero(ecartCaissier)){ 
					addCellData(document, table, "** TOTAL ECART",BigDecimalUtil.formatNumberZero(ecartCaissier));
				}
			} 
		}
		
         Map<String, BigDecimal> mapMvm = mapEmpl.get("MVM");
  		 Map<String, BigDecimal> mapAnnul = mapEmpl.get("ANNUL");
  		 Map<String, BigDecimal> mapOffre = mapEmpl.get("OFFR");
  		
  		 PdfUtil.ajouterLigneVide(document, 2);
  		
  		 for(String key : mapMvm.keySet()) {
  			addCellTitre(document, table, key);
  			addCellData(document, table, "TOTAL MOUVEMENTS",BigDecimalUtil.formatNumberZero(mapMvm.get(key)));
  			addCellData(document, table, "TOTAL ANNULATION",BigDecimalUtil.formatNumberZero(mapAnnul.get(key)));
  			addCellData(document, table, "TOTAL OFFRES",BigDecimalUtil.formatNumberZero(mapOffre.get(key)));
		}
         
         document.add(table);
	}
	private void addCellTitre(Document document, PdfPTable table, String val){
		PdfPCell cell = PdfUtil.getCell(document, val, Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
   	 	cell.setBackgroundColor(PdfUtil.JAUNE_LEGER);
   	 	cell.setColspan(2);
        table.addCell(cell);
	}
	
	private void addCellData(Document document, PdfPTable table, String val, String val2){
		PdfPCell cell = PdfUtil.getCell(document, val, Element.ALIGN_RIGHT, PdfUtil.FONT_10_NORMAL_BLACK);
        table.addCell(cell);
        cell = PdfUtil.getCell(document, val2, Element.ALIGN_RIGHT, PdfUtil.FONT_10_BOLD_BLACK);
        table.addCell(cell);
	}
}
