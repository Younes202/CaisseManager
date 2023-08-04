package appli.model.domaine.util_srv.raz.html;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.DocumentException;

import appli.model.domaine.vente.persistant.CaisseJourneePersistant;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.StringUtil;

public class RazEmployeHTML {
	
	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */
	public String getHtml(String titre, String date, 
    		Long currentUserId,
    		String type, List<CaisseJourneePersistant> listDataShift, 
    		Map<String, Map> mapEmpl,
    		BigDecimal fraisLivraison) {
		
		StringBuilder sb = new StringBuilder();
		ajouterEntete(sb, titre);
		
		sb.append("<table class='table table-hover' style='width:80%;'>");
		ajouterTitre(sb, date);
		ajouterContenu(sb, currentUserId, type, listDataShift, mapEmpl, fraisLivraison);	
		sb.append("</table>");
		
		return sb.toString();
	}

	/**
	 * @param document
	 * @param cupP
	 * @throws DocumentException 
	 */
	private void ajouterEntete(StringBuilder sb, String titre){ 
		sb.append("<h3>"+titre+"</h3>");
	}
	
	private void ajouterTitre(StringBuilder sb, String date){
		sb.append("<thead class='bordered-darkorange'>"
				+ "<tr><th style='text-align:center;' colspan='2'><h3>Date du "+date+"</h3></th></tr>"
				+ "<tr>"
					+ "<th>DONNÉE</th>"
					+ "<th style='text-align:right;'>MONTANT</th>"
				+ "</tr>"
				+ "</thead>");
	}
	
	/**
	 * @param document
	 * @param cupP
	 */
	private void ajouterContenu(StringBuilder sb, 
    		Long currentUserId,
    		String type, List<CaisseJourneePersistant> listDataShift, 
    		Map<String, Map> mapEmpl,
    		BigDecimal fraisLivraison) {
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
 				sb.append("<tr><td colspan='2' style='background-color:#e0f7fa;font-weight:bold;'>"+data.getOpc_caisse().getReference().toUpperCase()+"</td></tr>");
 			}
 			oldCaisse = data.getOpc_caisse().getReference();
 			
 			// Detail shift
 			String shift = "SHIFT "+idxShift;
 			shift += "";
 			if("O".equals(data.getStatut_caisse())){
 				shift += " : OUVERT";
 			} else if("E".equals(data.getStatut_caisse())){
 				shift += " : EN CLOTURE";
 			} else{
 				shift += " : CLOS";
 			}
 			shift += " [";
 			
 			String dateOuv = "A "+DateUtil.dateToString(data.getDate_ouverture(), "HH:mm:ss")
 			+(data.getDate_cloture()!=null ? "à "+StringUtil.getValueOrEmpty(DateUtil.dateToString(data.getDate_cloture(), "HH:mm:ss")): "");

 			shift += dateOuv + "] ";
 			String ouvr = " Ouverture :";
 			if(data.getOpc_user() != null){
 				if(data.getOpc_user().getOpc_employe()==null){
 					ouvr += data.getOpc_user().getLogin();
 				} else{
 					ouvr += data.getOpc_user().getOpc_employe().getNom() + StringUtil.getValueOrEmpty(data.getOpc_user().getOpc_employe().getPrenom());
 				}
 			}
 			shift += "----------"+ouvr;
 			
 			if(data.getOpc_user_cloture() != null){
 				String clo = " Clôture :";
 				if(data.getOpc_user_cloture().getOpc_employe()==null){
 					clo += data.getOpc_user_cloture().getLogin();
 				} else{
 					clo += data.getOpc_user_cloture().getOpc_employe().getNom() + StringUtil.getValueOrEmpty(data.getOpc_user_cloture().getOpc_employe().getPrenom());
 				}
 				shift += "---------"+clo;
 			}
 			sb.append("<tr><td colspan='2' style='background-color:black;color:white;font-weight:bold;'>"+shift+"</td></tr>");
 			
// 			addCellData(sb, "Montant total vente brut",BigDecimalUtil.formatNumberZero(mapEmpl.get(data.getOpc_user().getLogin())));
 			addCellData(sb, "Fonds de roulement ", BigDecimalUtil.formatNumberZero(data.getMtt_ouverture()));
 			addCellData(sb, "Montant net vente", BigDecimalUtil.formatNumberZero(data.getMtt_total_net()));
 			addCellData(sb, "Nbr ventes", BigDecimalUtil.formatNumberZero(data.getNbr_vente()));
 			
 			//****** Detail ****** 
 			sb.append("<tr><td colspan='2' style='background-color:#fff5a2;color:blue;font-weight:bold;'>DÉTAILS</td></tr>");
 			
 			if(!BigDecimalUtil.isZero(data.getMtt_annule())){
 				addCellData(sb, "Annulations CMD", BigDecimalUtil.formatNumberZero(data.getMtt_annule()));
 			}
 			if(!BigDecimalUtil.isZero(data.getMtt_annule_ligne())){
 				addCellData(sb, "Annulations Ligne", BigDecimalUtil.formatNumberZero(data.getMtt_annule_ligne()));
 			}
 			if(fraisLivraison != null){
 				String val = BigDecimalUtil.formatNumber(BigDecimalUtil.multiply(fraisLivraison, BigDecimalUtil.get((data.getNbr_livraison()==null?0:data.getNbr_livraison()))));
 				addCellData(sb, "Livraisons ", val);
 			} 
 			 if(!BigDecimalUtil.isZero(data.getMtt_reduction())){
 				 addCellData(sb, "Réductions ", BigDecimalUtil.formatNumberZero(data.getMtt_reduction()));
 			 }
 			 if(!BigDecimalUtil.isZero(data.getMtt_art_offert())){
 				 addCellData(sb, "Offerts ", BigDecimalUtil.formatNumberZero(data.getMtt_art_offert()));
 			 }
 			sb.append("<tr><td colspan='2' style='background-color:#fff5a2;color:blue;font-weight:bold;'>RÉPARTITION DES MONTANTS</td></tr>");
 			 if(!BigDecimalUtil.isZero(data.getMtt_espece())){
 			addCellData(sb, "Especes ", BigDecimalUtil.formatNumberZero(data.getMtt_espece()));
 			 }
 			 if(!BigDecimalUtil.isZero(data.getMtt_cheque())){
 			addCellData(sb, "Chèques ", BigDecimalUtil.formatNumberZero(data.getMtt_cheque()));
 			 }
 			 if(!BigDecimalUtil.isZero(data.getMtt_dej())){
 			addCellData(sb, "Chèques déj.", BigDecimalUtil.formatNumberZero(data.getMtt_dej()));
 			 }
 			 if(!BigDecimalUtil.isZero(data.getMtt_cb())){
 			addCellData(sb, "Carte bancaire ", BigDecimalUtil.formatNumberZero(data.getMtt_cb()));
 			 }
 			if(!BigDecimalUtil.isZero(data.getMtt_portefeuille())){
 				addCellData(sb, "Portefeuille ", BigDecimalUtil.formatNumberZero(data.getMtt_portefeuille()));
 			} 
 			if(!BigDecimalUtil.isZero(data.getMtt_donne_point())){ 
 				addCellData(sb, "Points ", BigDecimalUtil.formatNumberZero(data.getMtt_donne_point()));
 			} 
 			
 			sb.append("<tr><td colspan='2' style='background-color:#fff5a2;color:blue;font-weight:bold;'>CLÔTURE</td></tr>");
 			 if(!BigDecimalUtil.isZero(mttClotureCaissierEspNet)){
 			addCellData(sb, "Clôture espèces", BigDecimalUtil.formatNumberZero(mttClotureCaissierEspNet));
 			 }
 			 if(!BigDecimalUtil.isZero(mttClotureCaisserCheq)){
 			addCellData(sb, "Clôture chèque", BigDecimalUtil.formatNumberZero(mttClotureCaisserCheq));
 			 }
 			 if(!BigDecimalUtil.isZero(mttClotureCaisserDej)){
 			addCellData(sb, "Clôture déj", BigDecimalUtil.formatNumberZero(mttClotureCaisserDej));
 			 }
 			 if(!BigDecimalUtil.isZero(mttClotureCaisserCb)){
 			addCellData(sb, "Clôture carte", BigDecimalUtil.formatNumberZero(mttClotureCaisserCb));
 			 }
 			 
 			if("C".equals(data.getStatut_caisse())){
 				sb.append("<tr><td colspan='2' style='background-color:#fff5a2;color:blue;font-weight:bold;'>ECARTS</td></tr>");
 				BigDecimal ecartEspeses = BigDecimalUtil.substract(mttClotureCaissierEspNet, data.getMtt_espece());
 				BigDecimal ecartCb = BigDecimalUtil.substract(mttClotureCaisserCb, data.getMtt_cb());
 				BigDecimal ecartCheque = BigDecimalUtil.substract(mttClotureCaisserCheq, data.getMtt_cheque());
 				BigDecimal ecartDej = BigDecimalUtil.substract(mttClotureCaisserDej, data.getMtt_dej());
 			
 				 if(!BigDecimalUtil.isZero(ecartEspeses)){
 				addCellData(sb, "Ecart espèces",  BigDecimalUtil.formatNumberZero(ecartEspeses));
 				 }
 				 if(!BigDecimalUtil.isZero(ecartCheque)){
 				addCellData(sb, "Ecart chèque", BigDecimalUtil.formatNumberZero(ecartCheque));
 				 }
 				 if(!BigDecimalUtil.isZero(ecartDej)){
 				addCellData(sb, "Ecart déj", BigDecimalUtil.formatNumberZero(ecartDej));
 				 }
 				 if(!BigDecimalUtil.isZero(ecartCb)){
 				addCellData(sb, "Ecart carte", BigDecimalUtil.formatNumberZero(ecartCb));
 				 }
 				 
 				addCellData(sb, "** TOTAL ECART",BigDecimalUtil.formatNumberZero(ecartCaissier));
 			} 
  		}
 
 		sb.append("<tr><td colspan='2'>&nbsp;</td></tr>");
 		sb.append("<tr><td colspan='2' style='background-color:#fff5a2;color:blue;font-weight:bold;'>CUMULS CAISSIERS</td></tr>");
		
 		Map<String, BigDecimal> mapMvm = mapEmpl.get("MVM");
 		Map<String, BigDecimal> mapAnnul = mapEmpl.get("ANNUL");
 		Map<String, BigDecimal> mapOffre = mapEmpl.get("OFFR");
 		
 		for(String key : mapMvm.keySet()) {
 			sb.append("<tr><td colspan='2' style='color:orange;font-weight:bold;'>"+key+"</td></tr>");
 			
			addCellData(sb, "TOTAL MOUVEMENTS : "+key, BigDecimalUtil.formatNumberZero(mapMvm.get(key)));
			addCellData(sb, "TOTAL ANNULATION : "+key, BigDecimalUtil.formatNumberZero(mapAnnul.get(key)));
			addCellData(sb, "TOTAL OFFRES : "+key, BigDecimalUtil.formatNumberZero(mapOffre.get(key)));
		}

	}
	
	private void addCellData(StringBuilder sb, String val, String val2){
		 sb.append("<tr style='font-weight: bold;'>");
      	 sb.append("<td>"+val+"</td>");
      	 sb.append("<td align='right'>"+val2+"</td>");
      	 sb.append("</tr>");
	}
}
