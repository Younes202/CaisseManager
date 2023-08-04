package appli.model.domaine.stock.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import appli.controller.domaine.administration.bean.EtablissementBean;
import appli.controller.domaine.administration.bean.SocieteBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS;
import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import appli.model.domaine.administration.service.IEtablissementService;
import appli.model.domaine.compta.persistant.PaiementPersistant;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.stock.persistant.MouvementArticlePersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.service.IMouvementService;
import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.controller.ContextGloabalAppli;
import framework.model.beanContext.SocietePersistant;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.NumberToLetter;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.PdfBean;
import framework.model.util.PdfUtil;

public class FactureVentePDF {
	private FactureBean factureB;
	private CaisseMouvementPersistant currCaisseMvm;
	private MouvementPersistant currMvm;
	private boolean showDetail;
	
	public FactureVentePDF(CaisseMouvementPersistant caisseMvm, boolean isDetail){
		this.currCaisseMvm = caisseMvm;
		this.showDetail = isDetail;
		
		this.factureB = new FactureBean();
		this.factureB.setOpc_client(caisseMvm.getOpc_client());

		BigDecimal totalHT = caisseMvm.getMtt_commande_net();
		String tauxTva = ContextGloabalAppli.getGlobalConfig(ContextAppli.PARAM_APPLI_ENUM.TVA_VENTE.toString());
		BigDecimal tauxTvaB = StringUtil.isEmpty(tauxTva) ? BigDecimalUtil.ZERO : BigDecimalUtil.get(tauxTva);
		//
		if(!BigDecimalUtil.isZero(tauxTvaB)) {
			//Exemple : 29,9 euro TTC avec une TVA à 19,6% donnera le prix HT suivant : 29,9 / 1,196 = 25 euros HT
			totalHT = BigDecimalUtil.divide(caisseMvm.getMtt_commande_net(), BigDecimalUtil.divide(BigDecimalUtil.get(tauxTva), BigDecimalUtil.get(100)));
		}
		
		if(ContextAppli.getSocieteBean() == null) {
			this.factureB.setOpc_societe(new SocieteBean());
		} else {
			this.factureB.setOpc_societe(ContextAppli.getSocieteBean());	
		}
		
		this.factureB.setMode_paiement(caisseMvm.getMode_paiement());
		this.factureB.setMtt_net_ht(totalHT);
		this.factureB.setMtt_net_ttc(caisseMvm.getMtt_commande_net());
		this.factureB.setMtt_tva(BigDecimalUtil.substract(caisseMvm.getMtt_commande_net(), totalHT));
		this.factureB.setTaux_tva(tauxTvaB);
	}
	
	public FactureVentePDF(MouvementPersistant mvm, boolean isDetail){
		this.currMvm = mvm;
		this.showDetail = isDetail;
		
		this.factureB = new FactureBean();
		this.factureB.setOpc_client(mvm.getOpc_client());
		
		if(ContextAppli.getSocieteBean() == null) {
			this.factureB.setOpc_societe(new SocieteBean());
		} else {
			this.factureB.setOpc_societe(ContextAppli.getSocieteBean());	
		}
		
		String modePaiement = "ESPECES";
		List<PaiementPersistant> listPaiement = mvm.getList_paiement();
		for (PaiementPersistant paiementP : listPaiement) {
			modePaiement = paiementP.getLibelle();
		}
		
		this.factureB.setMode_paiement(modePaiement);
		this.factureB.setMtt_net_ht(mvm.getMontant_ht());
		this.factureB.setMtt_net_ttc(mvm.getMontant_ttc());
		this.factureB.setMtt_tva(mvm.getMontant_tva());
		
		ValTypeEnumPersistant opc_tva_enum = mvm.getList_article().get(0).getOpc_tva_enum();
		if(opc_tva_enum != null) {
			this.factureB.setTaux_tva(BigDecimalUtil.get(opc_tva_enum.getCode()));
		}
	}
	public FactureBean getFactureBean(){
		return factureB;
	}

	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */

	public File exportPdf(){
		PdfBean pdfBean = null;
		Document document = null;
		
		try{
			IEtablissementService etsService = ServiceUtil.getBusinessBean(IEtablissementService.class);
			//
			EtablissementBean etsB = etsService.findById(ContextAppli.getEtablissementBean().getId());
			Integer lastNumFacture = etsB.getNum_facture();
			
			if(lastNumFacture == null){
				lastNumFacture = 1;
			} else{
				lastNumFacture++;
			}
			
			String numFacture = null;
			if(StringUtil.isEmpty(this.factureB.getNumero_facture())){
				numFacture = DateUtil.dateToString(new Date(), "yyyy")+"-"+lastNumFacture;
				// On met à jour si généré automatiquement
				etsB.setNum_facture(lastNumFacture);
				etsService.merge(etsB);
			} else{
				numFacture = this.factureB.getNumero_facture();	
			}
			
			this.factureB.setNumero_facture(numFacture);
			
			pdfBean = PdfUtil.creerDocument("facture-"+numFacture+"-"+DateUtil.dateToString(this.factureB.getDate_facture(), "dd-MM-yyyy"), true, true);
			document = pdfBean.getDocument();
			ajouterContenu(document);
			
			document.newPage();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally{
			document.close();
		}
		
		return pdfBean.getPdfFile();
	}

	/**
	 * @param document
	 * @param cupP
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	private void ajouterContenu(Document document) throws DocumentException, MalformedURLException, IOException {
		IMouvementService mouvementService = (IMouvementService)ServiceUtil.getBusinessBean(IMouvementService.class);
		String soft = StrimUtil.getGlobalConfigPropertie("context.soft");
		boolean isRestau = SOFT_ENVS.restau.toString().equals(soft);
		
		Font fontFact = new Font(Font.getFamily("TIMES_ROMAN"), 20, Font.BOLD);
		fontFact.setColor(new BaseColor(0,139,139));

		Font fontAbn = new Font(Font.getFamily("TIMES_ROMAN"), 13, Font.BOLD);
		fontAbn.setColor(new BaseColor(82, 50, 83));

//		PdfPTable tabImage = new PdfPTable(1);
//		
//		tabImage.setWidthPercentage(100f);
//		tabImage.getDefaultCell().setBorder(0);
//		tabImage.setHorizontalAlignment(Element.ALIGN_LEFT);
		
		// Logo de la société ------------------------------------------
		Image image = null;
		SocietePersistant societeB = mouvementService.findById(SocietePersistant.class, factureB.getOpc_societe().getId());
		ClientPersistant clientB = factureB.getOpc_client();
		
		Map<String, byte[]> imagep = mouvementService.getDataImage(societeB.getId(), "societe");
		if(imagep.size() > 0){
			try{
				image = Image.getInstance(imagep.entrySet().iterator().next().getValue());
				image.scaleAbsolute(Float.valueOf(150), Float.valueOf(85));
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		
		PdfPCell cellImg = null;
		if(image != null){
			cellImg = new PdfPCell(image, true);
	        cellImg.setFixedHeight(70f);
	        cellImg.setBorderWidth(0);
	        cellImg.setHorizontalAlignment(Element.ALIGN_RIGHT);
	       // tabImage.addCell(cellImg);
		}
		
        // Informations de la société
        PdfPTable tabInfoSociete = new PdfPTable(1);
        tabInfoSociete.setWidthPercentage(100f);
		tabInfoSociete.getDefaultCell().setBorder(0);
		tabInfoSociete.setHorizontalAlignment(Element.ALIGN_LEFT);
		
		tabInfoSociete.addCell(PdfUtil.getCellNoBorder(document, StringUtil.getValueOrEmpty(societeB.getRaison_sociale()), fontAbn));
		tabInfoSociete.addCell(PdfUtil.getCellNoBorder(document, StringUtil.getValueOrEmpty(societeB.getAdresse_rue()), PdfUtil.FONT_10_NORMAL_BLACK));
		
		if(societeB.getOpc_ville() != null) {
			tabInfoSociete.addCell(PdfUtil.getCellNoBorder(document, StringUtil.getValueOrEmpty(societeB.getOpc_ville().getCode_postal())
						+ " "
						+ StringUtil.getValueOrEmpty(societeB.getOpc_ville().getLibelle()), PdfUtil.FONT_10_NORMAL_BLACK));
		}
		
		if(StringUtil.isNotEmpty(societeB.getNumero_rcs())){
			tabInfoSociete.addCell(PdfUtil.getCellNoBorder(document, "RC : "+StringUtil.getValueOrEmpty(societeB.getNumero_rcs()), PdfUtil.FONT_10_NORMAL_BLACK));
		}
		if(StringUtil.isNotEmpty(societeB.getNumero_ice())){
			tabInfoSociete.addCell(PdfUtil.getCellNoBorder(document, "ICE : "+StringUtil.getValueOrEmpty(societeB.getNumero_ice()), PdfUtil.FONT_10_NORMAL_BLACK));
		}
		if(StringUtil.isNotEmpty(societeB.getNumero_tva())){
			tabInfoSociete.addCell(PdfUtil.getCellNoBorder(document, "TVA : "+StringUtil.getValueOrEmpty(societeB.getNumero_tva()), PdfUtil.FONT_10_NORMAL_BLACK));
		}
		if(StringUtil.isNotEmpty(societeB.getIdentifiant_fiscal())){
			tabInfoSociete.addCell(PdfUtil.getCellNoBorder(document, "IF : "+StringUtil.getValueOrEmpty(societeB.getIdentifiant_fiscal()), PdfUtil.FONT_10_NORMAL_BLACK));
		}
		
		// Informations de client
		PdfPTable tabInfoFacture = new PdfPTable(1);
		tabInfoFacture.setWidthPercentage(100f);
		tabInfoFacture.getDefaultCell().setBorder(0);
		tabInfoFacture.setHorizontalAlignment(Element.ALIGN_RIGHT);
		
		tabInfoFacture.addCell(cellImg);
		tabInfoFacture.addCell(PdfUtil.getCellNoBorder(document, "N° facture :  "+factureB.getNumero_facture(), PdfUtil.FONT_10_NORMAL_BLACK, Element.ALIGN_RIGHT));
		tabInfoFacture.addCell(PdfUtil.getCellNoBorder(document, "Date facture:  "+DateUtil.dateToString(factureB.getDate_facture(), "dd/MM/yyyy"), PdfUtil.FONT_10_NORMAL_BLACK, Element.ALIGN_RIGHT));
		
        PdfPTable entete = new PdfPTable(3);
		entete.setWidthPercentage(100f);
	    float[] widthsCell = {40f, 20f, 40f};
	    entete.setWidths(widthsCell);
	    entete.getDefaultCell().setBorder(0);
	    
	    entete.addCell(PdfUtil.getCellVideNoBorder(document));
	    entete.addCell(PdfUtil.getCellNoBorder(document, "FACTURE", fontFact, Element.ALIGN_RIGHT));
	    entete.addCell(PdfUtil.getCellVideNoBorder(document));
	    
	    entete.addCell(tabInfoSociete);
	    entete.addCell(PdfUtil.getCellVideNoBorder(document));
	    entete.addCell(tabInfoFacture);
	    
		document.add(entete);
		
        PdfUtil.ajouterLigneVide(document, 1);
        
        PdfPTable tab4 = new PdfPTable(1);
        tab4.setWidthPercentage(100f);
        tab4.getDefaultCell().setBorder(0);
        tab4.setHorizontalAlignment(Element.ALIGN_LEFT);
		
        if(StringUtil.isNotEmpty(societeB.getTelephone1())){
        	tab4.addCell(PdfUtil.getCellNoBorder(document, "Tél. : "+societeB.getTelephone1(), PdfUtil.FONT_10_NORMAL_BLACK));
        }
        if(StringUtil.isNotEmpty(societeB.getMail())){
        	tab4.addCell(PdfUtil.getCellNoBorder(document, "Email : "+societeB.getMail(), PdfUtil.FONT_10_NORMAL_BLACK));
        }
        if(StringUtil.isNotEmpty(societeB.getSite())){
        	tab4.addCell(PdfUtil.getCellNoBorder(document, "Site web : "+societeB.getSite(), PdfUtil.FONT_10_NORMAL_BLACK));
        }
		
		PdfPTable tab5 = new PdfPTable(1);
		tab5.setWidthPercentage(100f);
		tab5.getDefaultCell().setBorder(0);
		tab5.setHorizontalAlignment(Element.ALIGN_RIGHT);
		
		tab5.addCell(PdfUtil.getCellNoBorder(document, clientB.getNom()+" "+StringUtil.getValueOrEmpty(clientB.getPrenom()), fontAbn, Element.ALIGN_RIGHT));
		
		if(StringUtil.isNotEmpty(clientB.getIce())){
			tab5.addCell(PdfUtil.getCellNoBorder(document, "ICE :  "+clientB.getIce(), PdfUtil.FONT_10_NORMAL_BLACK, Element.ALIGN_RIGHT));
		}
		if(StringUtil.isNotEmpty(clientB.getAdresse_compl())){
			tab5.addCell(PdfUtil.getCellNoBorder(document, clientB.getAdresse_compl(), PdfUtil.FONT_10_NORMAL_BLACK, Element.ALIGN_RIGHT));
		} else if(StringUtil.isNotEmpty(clientB.getAdressFull())){
			tab5.addCell(PdfUtil.getCellNoBorder(document, clientB.getAdressFull(), PdfUtil.FONT_10_NORMAL_BLACK, Element.ALIGN_RIGHT));
		}
		if(StringUtil.isNotEmpty(clientB.getTelephone())){
			tab5.addCell(PdfUtil.getCellNoBorder(document, "Tél. : "+clientB.getTelephone(), PdfUtil.FONT_10_NORMAL_BLACK, Element.ALIGN_RIGHT));
		}
		if(StringUtil.isNotEmpty(clientB.getMail())){
			tab5.addCell(PdfUtil.getCellNoBorder(document, "Email : "+clientB.getMail(), PdfUtil.FONT_10_NORMAL_BLACK, Element.ALIGN_RIGHT));
		}
		
		PdfPTable entete2 = new PdfPTable(2);
		entete2.setWidthPercentage(100f);
	    float[] widthsCell2 = {50f, 50f};
	    entete2.setWidths(widthsCell2);
	    entete2.getDefaultCell().setBorder(0);
	    
	    entete2.addCell(tab4);
	    entete2.addCell(tab5);
		
	    document.add(entete2);
		
		PdfUtil.ajouterLigneVide(document, 2);
		
		// Tableau détaillé -------------------------------
		Font fontTilte = new Font(Font.getFamily("TIMES_ROMAN"), 10, Font.BOLD);
		fontTilte.setColor(new BaseColor(255, 255, 255));
        
	    PdfPTable table = new PdfPTable(6);
	    table.setWidthPercentage(100f);
	    table.getDefaultCell().setBorder(0);
	    float[] widths1 = {45f, 8f, 13f, 12f, 10f, 12f};
		table.setWidths(widths1);// largeur par cellule
		
		PdfPCell cell = PdfUtil.getCell(document, "Libellé", Element.ALIGN_LEFT, fontTilte);
		cell.setPadding(5f);
		cell.setBackgroundColor(new BaseColor(0,139,139));
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Qte", Element.ALIGN_LEFT, fontTilte);
		cell.setPadding(5f);
		cell.setBackgroundColor(new BaseColor(0,139,139));
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Unité", Element.ALIGN_LEFT, fontTilte);
		cell.setPadding(5f);
		cell.setBackgroundColor(new BaseColor(0,139,139));
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Tarif HT", Element.ALIGN_CENTER, fontTilte);
		cell.setPadding(5f);
		cell.setBackgroundColor(new BaseColor(0,139,139));
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "TVA(%)", Element.ALIGN_CENTER, fontTilte);
		cell.setPadding(5f);
		cell.setBackgroundColor(new BaseColor(0,139,139));
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, "Tarif TTC", Element.ALIGN_CENTER, fontTilte);
		cell.setPadding(5f);
		cell.setBackgroundColor(new BaseColor(0,139,139));
		cell.setBorder(0);
		table.addCell(cell);

		BigDecimal ttlHt = null, ttlTtc = null, ttlTva = null;
		
		BigDecimal tauxTvaFacture = factureB.getTaux_tva();
		if(showDetail) {
			if(this.currCaisseMvm != null) {
				for( CaisseMouvementArticlePersistant caiMvmDet : this.currCaisseMvm.getList_article()) {
					if(BigDecimalUtil.isZero(caiMvmDet.getMtt_total()) || BooleanUtil.isTrue(caiMvmDet.getIs_annule())) {
						continue;
					}
					
					BigDecimal mtt_ht = null;
					BigDecimal mtt_tva = null;
					
					if(BigDecimalUtil.isZero(tauxTvaFacture)) {
						mtt_ht = caiMvmDet.getMtt_total();
						mtt_tva = BigDecimalUtil.ZERO;
					} else {
						BigDecimal[] data = getHtFromTTC(caiMvmDet.getMtt_total(), tauxTvaFacture);
						mtt_ht = data[0];
						mtt_tva = data[1];
					}
					
					String unite = "Pièce";
					if(caiMvmDet.getOpc_article() != null && caiMvmDet.getOpc_article().getOpc_unite_vente_enum() != null) {
						unite = caiMvmDet.getOpc_article().getOpc_unite_vente_enum().getLibelle();
					}
					
					addTrFacture(document, table, 
							caiMvmDet.getLibelle(), 
							caiMvmDet.getQuantite(), 
							mtt_ht, tauxTvaFacture, caiMvmDet.getMtt_total(), unite);
					
					ttlHt = BigDecimalUtil.add(ttlHt, mtt_ht);
					ttlTtc = BigDecimalUtil.add(ttlTtc, caiMvmDet.getMtt_total());
					ttlTva = BigDecimalUtil.add(ttlTva, mtt_tva);
				}
			} else {
				for( MouvementArticlePersistant caiMvmDet : this.currMvm.getList_article()) {
					if(BigDecimalUtil.isZero(caiMvmDet.getPrix_vente())) {
						continue;
					}
					BigDecimal tauxTva = BigDecimalUtil.get((caiMvmDet.getOpc_tva_enum() != null ? caiMvmDet.getOpc_tva_enum().getCode() : "0"));
					
					BigDecimal mtt_ht = null;
					BigDecimal mtt_tva = null;
					if(BigDecimalUtil.isZero(tauxTva)) {
						mtt_ht = caiMvmDet.getPrix_vente();
						mtt_tva = BigDecimalUtil.ZERO;
					} else {
						mtt_ht = caiMvmDet.getPrix_vente_ht();
						mtt_tva = BigDecimalUtil.substract(caiMvmDet.getPrix_vente(), caiMvmDet.getPrix_vente_ht());
					}
					
					String unite = "";
					if(caiMvmDet.getOpc_article().getOpc_unite_vente_enum() != null) {
						unite = caiMvmDet.getOpc_article().getOpc_unite_vente_enum().getLibelle();
					}
					
					addTrFacture(document, table, 
							caiMvmDet.getOpc_article().getLibelle(), 
							caiMvmDet.getQuantite(), 
							mtt_ht, 
							tauxTva, 
							caiMvmDet.getPrix_vente(), unite);
					
					ttlHt = BigDecimalUtil.add(ttlHt, mtt_ht);
					ttlTtc = BigDecimalUtil.add(ttlTtc, caiMvmDet.getPrix_vente());
					ttlTva = BigDecimalUtil.add(ttlTva, mtt_tva);
				}
			}
		} else {
			BigDecimal[] data = getHtFromTTC(factureB.getMtt_net_ttc(), tauxTvaFacture);
			BigDecimal mtt_ht = data[0];
			BigDecimal mtt_tva = data[1];
			
			ttlHt = BigDecimalUtil.add(ttlHt, mtt_ht);
			ttlTtc = BigDecimalUtil.add(ttlTtc, factureB.getMtt_net_ttc());
			ttlTva = BigDecimalUtil.add(ttlTva, mtt_tva);
			
			String designation = isRestau ? "Facture consommation restaurant" : 
										"Facture achat produits vente en détail";
			
			addTrFacture(document, table, designation, BigDecimalUtil.get(1), mtt_ht, tauxTvaFacture, factureB.getMtt_net_ttc(), "Pièce");
		}
				
		document.add(table);
		
		PdfUtil.ajouterLigneVide(document, 3);
		
		//Détail
		PdfPTable tabInfo = new PdfPTable(1);
		tabInfo.setWidthPercentage(55f);
		tabInfo.getDefaultCell().setBorder(0);

		tabInfo.setHorizontalAlignment(Element.ALIGN_LEFT);
		
		cell = PdfUtil.getCell(document, "Information complémentaires", fontTilte);
		cell.setFixedHeight(24f);
		cell.setPadding(5f);
		cell.setBackgroundColor(new BaseColor(0,139,139));
		cell.setBorder(0);
		tabInfo.addCell(cell);
		
		String modeP = factureB.getMode_paiement();
		String information="Moyen de paiement : "+(StringUtil.isEmpty(modeP) ? "ESPECES" : modeP);
		
		cell = PdfUtil.getCell(document, information, PdfUtil.FONT_10_NORMAL_BLACK);
		cell.setPadding(8f);
		cell.setBorder(0);
		tabInfo.addCell(cell);
		
		// Tableau des totaux -------------------------------
		PdfPTable tabTotaux = new PdfPTable(2);
		tabTotaux.setWidthPercentage(40f);
		tabTotaux.getDefaultCell().setBorder(0);
	    float[] widthsT = {20f, 20f};
	    tabTotaux.setWidths(widthsT);// largeur par cellule
	    tabTotaux.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    tabTotaux.getDefaultCell().setBorderColor(new BaseColor(128,128,128));
		// Total	    
	    cell = PdfUtil.getCell(document, "Total H.T", PdfUtil.FONT_10_NORMAL_BLACK);
		cell.setPadding(5f);
		cell.setBorder(0);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		tabTotaux.addCell(cell);
		
		cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(ttlHt)+" dhs", Element.ALIGN_RIGHT, PdfUtil.FONT_10_NORMAL_BLACK);
		cell.setPadding(5f);
		cell.setBorder(0);
		cell.setBackgroundColor(new BaseColor(224, 224, 224));
		tabTotaux.addCell(cell);
		// TVA
		cell = PdfUtil.getCell(document, "TVA", PdfUtil.FONT_10_NORMAL_BLACK);
		cell.setPadding(5f);
		cell.setBorder(0);
		tabTotaux.addCell(cell);
		
		cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(ttlTva)+" dhs", Element.ALIGN_RIGHT, PdfUtil.FONT_10_NORMAL_BLACK);
		cell.setPadding(5f);
		cell.setBorder(0);
		tabTotaux.addCell(cell);
		
		// Total TTC 
		Font fontTTC = new Font(Font.getFamily("TIMES_ROMAN"), 10, Font.BOLD);
		fontTTC.setColor(new BaseColor(0,139,139));
		
		cell = PdfUtil.getCell(document, "Total TTC", fontTTC);
		cell.setPadding(5f);
		cell.setBorder(0);
		tabTotaux.addCell(cell);
		
		cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(ttlTtc)+" dhs", Element.ALIGN_RIGHT, fontTTC);
		cell.setPadding(5f);
		cell.setBorder(0);
		tabTotaux.addCell(cell);
		
		PdfPTable outer = new PdfPTable(3);
		outer.setWidthPercentage(100f);
	    float[] widths2 = {55f, 10f ,35f};
	    outer.setWidths(widths2);
	    outer.getDefaultCell().setBorder(0);
		
	    outer.addCell(tabInfo);
	    outer.addCell(PdfUtil.getCellVideNoBorder(document));
		outer.addCell(tabTotaux);
		document.add(outer);
		
		PdfUtil.ajouterLigneVide(document, 1);
		Paragraph p2 = new Paragraph("Total TTC : "+NumberToLetter.convert(ttlTtc), PdfUtil.FONT_10_BOLD_BLACK);
		document.add(p2);
		
		// Information complémentaire -------------------------------
		PdfUtil.ajouterLigneVide(document, 2);
		
		//-----------------------------------
		
	/*	Paragraph p1 = new Paragraph("Le montant total s'élève à "+getCharForNumber(reservationP.getMtt_net().intValue())+" euros", PdfUtil.FONT_9_NORMAL_BLACK);
		document.add(p1);
		
		PdfUtil.ajouterLigneVide(document, 1);*/
		
		String text = "Facture réglée par : "+(StringUtil.isEmpty(this.factureB.getMode_paiement()) ? "ESPECES" : this.factureB.getMode_paiement());
		p2 = new Paragraph(text, PdfUtil.FONT_8_NORMAL_BLACK);
		
		document.add(p2);
	}
	
	private void addTrFacture(Document document, PdfPTable table, 
			String designation, BigDecimal qte,
			BigDecimal mttHt,
			BigDecimal tauxTva,
			BigDecimal mttTtc, String unite) {
		PdfPCell cell = PdfUtil.getCell(document, designation , PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setPadding(5f);
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(qte), Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setPadding(5f);
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, StringUtil.getValueOrEmpty(unite), Element.ALIGN_LEFT, PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setPadding(5f);
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(mttHt)+" dhs", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setPadding(5f);
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(tauxTva)+"%", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setPadding(5f);
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = PdfUtil.getCell(document, BigDecimalUtil.formatNumber(mttTtc)+" dhs", Element.ALIGN_RIGHT, PdfUtil.FONT_9_NORMAL_BLACK);
		cell.setPadding(5f);
		cell.setBorder(0);
		table.addCell(cell);
	}
	
	private BigDecimal[] getHtFromTTC(BigDecimal mttTtc, BigDecimal tauxTva) {
		//Calcul Mtt Ht
		BigDecimal mtt_tva = BigDecimalUtil.ZERO;
		BigDecimal mtt_ht = mttTtc;
		if(!BigDecimalUtil.isZero(tauxTva)) {
			mtt_ht = BigDecimalUtil.divide(mttTtc, BigDecimalUtil.add(BigDecimalUtil.get(1), BigDecimalUtil.divide(tauxTva, BigDecimalUtil.get(100))));
			String[] div = mtt_ht.toString().split("\\.");
			int nbr_places = div[0].length();
			int precision = nbr_places+2;
			
			mtt_ht = mtt_ht.round(new MathContext(precision, RoundingMode.HALF_UP));
			mtt_tva = BigDecimalUtil.substract(mttTtc, mtt_ht);
		}
		return new BigDecimal[] {mtt_ht, mtt_tva};
	}
}
