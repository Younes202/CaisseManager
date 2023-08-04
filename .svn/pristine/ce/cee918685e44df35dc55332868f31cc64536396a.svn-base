package appli.model.domaine.util_srv.raz.html;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.itextpdf.text.DocumentException;

import appli.controller.domaine.util_erp.ContextAppli.PARAM_APPLI_ENUM;
import appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS;
import appli.controller.util_ctrl.MouvementDetailPrintBean;
import appli.model.domaine.stock.persistant.FamillePersistant;
import framework.controller.ContextGloabalAppli;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

public class RazJourHTML {
	
	public enum MODE_PDF {
		MODEL, ARCHIVE, PAIE_ALL, PAIE
	}
	
	
	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */
	public String getHtml(String titre, String dtTxt, Map<String, Object> mapData) {
		
		StringBuilder sb = new StringBuilder();
		List<Map<String, Object>> listJours = (List<Map<String, Object>>) mapData.get("list_days_mtt");
		
		ajouterEntete(sb, titre);
		
		sb.append("<table class='table table-hover' style='width:500px;'>");
//		if(listJours == null){
			ajouterTitre(sb, dtTxt);
			
			sb.append("<tr style='background-color: #00bcd4;'>"
				+ "<th>DONNÉE</th>"
				+ "<th style='text-align:right;'>QUANTITÉ</th>"
				+ "<th style='text-align:right;'>MONTANT</th>"
			+ "</tr>");
			ajouterContenu(sb, mapData);	
//		} else{// Jour par jour
			if(listJours != null){
			for(Map<String, Object> mapJours : listJours){
				String dt = (String) mapJours.get("date_raz");
				
				ajouterTitre(sb, dt);
				
//				sb.append("<tr style='background-color: #00bcd4;'>"
//						+ "<th>DONNÉE</th>"
//						+ "<th style='text-align:right;'>QUANTITÉ</th>"
//						+ "<th style='text-align:right;'>MONTANT</th>"
//					+ "</tr>");
				
				ajouterContenu(sb, mapJours);	
				sb.append("<tr><td colspan='3'><hr></td><tr>");
			}
		}
		sb.append("</table>");
		
		return sb.toString();
	}

	/**
	 * @param document
	 * @param cupP
	 * @throws DocumentException 
	 */
	private void ajouterEntete(StringBuilder sb, String titre) {
		sb.append("<h3>"+titre+"</h3>");
	}
	
	private void ajouterTitre(StringBuilder sb, String text){
		sb.append(StringUtil.isNotEmpty(text) ? "<thead class='bordered-darkorange'>"
				+ "<tr><th style='text-align:center;' colspan='3'><h3>"+text+"</h3></th></tr>"
				+ "</thead>" : "");
	}
	
	/**
	 * @param document
	 * @param cupP
	 */
	private void ajouterContenu(StringBuilder sb, Map<String, Object> mapRetour) {
		BigDecimal mttOffertGlobal = (BigDecimal) mapRetour.get("mtt_offert");
		BigDecimal[] montantsRecap = (BigDecimal[]) mapRetour.get("mtt_recap");
		BigDecimal[] montantsMode = (BigDecimal[]) mapRetour.get("mtt_mode");
//		String[][] data = (String[][]) mapRetour.get("data_recap");
		
		 BigDecimal tauxTva = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig(PARAM_APPLI_ENUM.TVA_VENTE.toString()));
		
		if(montantsRecap != null){
    		 BigDecimal mttTotalNetALL = montantsMode[0], 
    				 	mttTotalBrutALL = montantsMode[1], 
    				 	mttEspeceALL = montantsMode[2], 
    				 	mttCarteALL = montantsMode[3], 
    				 	mttChequeALL = montantsMode[4], 
    				 	mttDejALL = montantsMode[5],
    		 			mttPointALL = montantsMode[6],
    		 			mttReserveALL = montantsMode[7];
    		 
             BigDecimal nbrNoteAll = montantsRecap[0], 
            		 	mttMoyen = montantsRecap[1], 
            		 	mttTotalReducClientALL = montantsRecap[2], 
            		 	mttTotalReducEmployeALL = montantsRecap[3], 
            		 	mttTotalOffertClientALL = montantsRecap[4], 
            		 	mttTotalOffertEmployeALL = montantsRecap[5], 
            		 	mttTotalAnnuleALL = montantsRecap[6],
                    	mttTotalReducAutreALL = montantsRecap[7],
                    	mttTotalOffertAutreALL = montantsRecap[8],
                    	mttTotalAnnuleArtALL = montantsRecap[9];
             
             sb.append("<table style='width:500px;'>");
             
           if(!BigDecimalUtil.isZero(mttEspeceALL)){
            sb.append("<tr>");
         	sb.append("<td align='right' style='width:50%;'>ESPECES :</td>");
         	sb.append("<td align='right'>"+BigDecimalUtil.formatNumberZero(mttEspeceALL)+"</td>");
         	sb.append("</tr>");
           }
           if(!BigDecimalUtil.isZero(mttCarteALL)){
         	sb.append("<tr>");
         	sb.append("<td align='right'>CARTE :</td>");
         	sb.append("<td align='right'>"+BigDecimalUtil.formatNumberZero(mttCarteALL)+"</td>");
         	sb.append("</tr>");
           }
           if(!BigDecimalUtil.isZero(mttChequeALL)){
         	sb.append("<tr>");
         	sb.append("<td align='right'>CHÈQUE :</td>");
         	sb.append("<td align='right'>"+BigDecimalUtil.formatNumberZero(mttChequeALL)+"</td>");
         	sb.append("</tr>");
           }
           if(!BigDecimalUtil.isZero(mttDejALL)){
         	sb.append("<tr>");
         	sb.append("<td align='right'>CHÈQUE TABLE :</td>");
         	sb.append("<td align='right'>"+BigDecimalUtil.formatNumberZero(mttDejALL)+"</td>");
         	sb.append("</tr>");
           }
           
           if(!BigDecimalUtil.isZero(mttPointALL)){
            	sb.append("<tr>");
            	sb.append("<td align='right'>POINTS :</td>");
            	sb.append("<td align='right'>"+BigDecimalUtil.formatNumberZero(mttPointALL)+"</td>");
            	sb.append("</tr>");
              }
           if(!BigDecimalUtil.isZero(mttReserveALL)){
            	sb.append("<tr>");
            	sb.append("<td align='right'>RESERVE :</td>");
            	sb.append("<td align='right'>"+BigDecimalUtil.formatNumberZero(mttReserveALL)+"</td>");
            	sb.append("</tr>");
              }
           
           if(!BigDecimalUtil.isZero(nbrNoteAll)){
         	sb.append("<tr>");
         	sb.append("<td align='right'>Nombre de notes payantes :</td>");
         	sb.append("<td align='right'>"+nbrNoteAll.intValue()+"</td>");
         	sb.append("</tr>");
           }
           if(!BigDecimalUtil.isZero(mttMoyen)){
         	sb.append("<tr>");
         	sb.append("<td align='right'>Moyenne notes :</td>");
         	sb.append("<td align='right'>"+BigDecimalUtil.formatNumberZero(mttMoyen)+"</td>");
         	sb.append("</tr>");
           }
           if(!BigDecimalUtil.isZero(mttTotalReducClientALL)){
         	sb.append("<tr>");
         	sb.append("<td align='right' style='color:blue;'>Réductions clients :</td>");
         	sb.append("<td align='right' style='color:blue;'>"+BigDecimalUtil.formatNumberZero(mttTotalReducClientALL)+"</td>");
         	sb.append("</tr>");
           }
           if(!BigDecimalUtil.isZero(mttTotalReducEmployeALL)){
         	sb.append("<tr>");
         	sb.append("<td align='right' style='color:blue;'>Réductions personnels :</td>");
         	sb.append("<td align='right' style='color:blue;'>"+BigDecimalUtil.formatNumberZero(mttTotalReducEmployeALL)+"</td>");
         	sb.append("</tr>");
           }
           if(!BigDecimalUtil.isZero(mttTotalReducAutreALL)){
            	sb.append("<tr>");
            	sb.append("<td align='right' style='color:blue;'>Réductions :</td>");
            	sb.append("<td align='right' style='color:blue;'>"+BigDecimalUtil.formatNumberZero(mttTotalReducAutreALL)+"</td>");
            	sb.append("</tr>");
              }
        	
           if(!BigDecimalUtil.isZero(mttTotalOffertClientALL)){
         	sb.append("<tr>");
         	sb.append("<td align='right' style='color:blue;'>Offerts clients :</td>");
         	sb.append("<td align='right' style='color:blue;'>"+BigDecimalUtil.formatNumberZero(mttTotalOffertClientALL)+"</td>");
         	sb.append("</tr>");
           }
           if(!BigDecimalUtil.isZero(mttTotalOffertEmployeALL)){
         	sb.append("<tr>");
         	sb.append("<td align='right' style='color:blue;'>Offerts personnels :</td>");
         	sb.append("<td align='right' style='color:blue;'>"+BigDecimalUtil.formatNumberZero(mttTotalOffertEmployeALL)+"</td>");
         	sb.append("</tr>");
           }
           if(!BigDecimalUtil.isZero(mttTotalOffertAutreALL)){
            	sb.append("<tr>");
            	sb.append("<td align='right' style='color:blue;'>Offerts :</td>");
            	sb.append("<td align='right' style='color:blue;'>"+BigDecimalUtil.formatNumberZero(mttTotalOffertAutreALL)+"</td>");
            	sb.append("</tr>");
              }
           if(!BigDecimalUtil.isZero(mttTotalAnnuleALL)){
         	sb.append("<tr>");
         	sb.append("<td align='right' style='color:red;'>Annulations commandes :</td>");
         	sb.append("<td align='right' style='color:red;'>"+BigDecimalUtil.formatNumberZero(mttTotalAnnuleALL)+"</td>");
         	sb.append("</tr>");
           }
           if(!BigDecimalUtil.isZero(mttTotalAnnuleArtALL)){
            	sb.append("<tr>");
            	sb.append("<td align='right' style='color:red;'>Annulations articles :</td>");
            	sb.append("<td align='right' style='color:red;'>"+BigDecimalUtil.formatNumberZero(mttTotalAnnuleArtALL)+"</td>");
            	sb.append("</tr>");
              }
        	
            // Total
            BigDecimal mttHt = BigDecimalUtil.getMttHt(mttTotalNetALL, tauxTva);
            BigDecimal mttTva = BigDecimalUtil.substract(mttTotalNetALL, mttHt); 
            
            sb.append("<tr style='background-color: #dfdbbe;font-weight: bold;'>");
         	sb.append("<td align='right'>TOTAL BRUT :</td>");
         	sb.append("<td align='right'>"+BigDecimalUtil.formatNumberZero(mttTotalBrutALL)+"</td>");
         	sb.append("</tr>");
         	
         	sb.append("<tr style='background-color: #dfdbbe;font-weight: bold;'>");
         	sb.append("<td align='right'>TOTAL NET :</td>");
         	sb.append("<td align='right'>"+BigDecimalUtil.formatNumberZero(mttTotalNetALL)+"</td>");
         	sb.append("</tr>");
         	
         	sb.append("<tr style='background-color: #dfdbbe;'>");
         	sb.append("<td align='right'>MONTANT TVA ("+BigDecimalUtil.formatNumberZero(tauxTva)+"%) :</td>");
         	sb.append("<td align='right'>"+BigDecimalUtil.formatNumberZero(mttTva)+"</td>");
         	sb.append("</tr>");
//         	
//         	sb.append("<tr style='background-color: #dfdbbe;font-weight: bold;'>");
//         	sb.append("<td align='right'>TOTAL TTC :</td>");
//         	sb.append("<td align='right'>"+BigDecimalUtil.formatNumberZero(mttTotalNetALL)+"</td>");
//         	sb.append("</tr>");
         	
        	sb.append("</table><hr>");
         }
         
		// Détail -----------------------------------------------------------------------------------------------
		List<MouvementDetailPrintBean> listDet = getListDetail(mapRetour, "MENU");;
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		BigDecimal mttTotal = BigDecimalUtil.get(0), qteTotal = BigDecimalUtil.get(0);
        BigDecimal mttTotalAnnul = BigDecimalUtil.get(0), qteTotalAnnul = BigDecimalUtil.get(0);
        BigDecimal mttTotalOffert = BigDecimalUtil.get(0), qteTotalOffert = BigDecimalUtil.get(0);
        BigDecimal[] mttQteArray = null;
        
		sb.append("<table style='width:500px;'>");
		
		if(isRestau && listDet.size() > 0){
			 // Menu ---------------------------------------------------------
			ajouterTitre(sb, "LES VENTES EN MENUS");
	         //---------- On collecte les taux de TVA -----------------------
	         //
	         if (listDet.size() > 0) {
	             mttQteArray = addDetail(sb, listDet, "Menus");
	             
	             mttTotal = BigDecimalUtil.add(mttTotal, mttQteArray[0]);
	             qteTotal = BigDecimalUtil.add(qteTotal, mttQteArray[1]);
	         }
	         listDet = getListDetail(mapRetour, "ANNUL_MENU");
	         if (listDet.size() > 0) {
	             mttQteArray = addDetail(sb, listDet, "Menus annulés");
	             mttTotalAnnul = BigDecimalUtil.add(mttTotalAnnul, mttQteArray[0]);
	             qteTotalAnnul = BigDecimalUtil.add(qteTotalAnnul, mttQteArray[1]);
	         }
	         listDet = getListDetail(mapRetour, "OFFR_MENU");
	         if (listDet.size() > 0) {
	             mttQteArray = addDetail(sb, listDet, "Menus offerts");
	             mttTotalOffert = BigDecimalUtil.add(mttTotalOffert, mttQteArray[0]);
	             qteTotalOffert = BigDecimalUtil.add(qteTotalOffert, mttQteArray[1]);
	             
	         }
	         // Article
	         listDet = getListDetail(mapRetour, "ART_MENU");
	         if (listDet.size() > 0) {
	             mttQteArray = addDetail(sb, listDet, "Articles vendus");
	             mttTotal = BigDecimalUtil.add(mttTotal, mttQteArray[0]);
	             qteTotal = BigDecimalUtil.add(qteTotal, mttQteArray[1]);
	             
	         }
	         listDet = getListDetail(mapRetour, "ANNUL_ART_MENU");
	         if (listDet.size() > 0) {
	             mttQteArray = addDetail(sb, listDet, "Articles annulés");
	             mttTotalAnnul = BigDecimalUtil.add(mttTotalAnnul, mttQteArray[0]);
	             qteTotalAnnul = BigDecimalUtil.add(qteTotalAnnul, mttQteArray[1]);
	             
	         }
	         listDet = getListDetail(mapRetour, "OFFR_ART_MENU");
	         if (listDet.size() > 0) {
	             mttQteArray = addDetail(sb, listDet, "Articles offerts");
	             mttTotalOffert = BigDecimalUtil.add(mttTotalOffert, mttQteArray[0]);
	             qteTotalOffert = BigDecimalUtil.add(qteTotalOffert, mttQteArray[1]);
	             
	         }
	         
	         ajouterTitre(sb, "LES VENTES HORS MENUS");
		}
         //
         listDet = getListDetail(mapRetour, "GROUPE_FAMILLE");
         if (listDet.size() > 0) {
             mttQteArray = addDetail(sb, listDet, "Familles");
             mttTotal = BigDecimalUtil.add(mttTotal, mttQteArray[0]);
             qteTotal = BigDecimalUtil.add(qteTotal, mttQteArray[1]);
             
         }
         listDet = getListDetail(mapRetour, "ANNUL_GROUPE_FAMILLE");
         if (listDet.size() > 0) {
             mttQteArray = addDetail(sb, listDet, "Familles annulées");
             mttTotalAnnul = BigDecimalUtil.add(mttTotalAnnul, mttQteArray[0]);
             qteTotalAnnul = BigDecimalUtil.add(qteTotalAnnul, mttQteArray[1]);
             
         }
         listDet = getListDetail(mapRetour, "OFFR_GROUPE_FAMILLE");
         if (listDet.size() > 0) {
             mttQteArray = addDetail(sb, listDet, "Familles offertes");
             mttTotalOffert = BigDecimalUtil.add(mttTotalOffert, mttQteArray[0]);
             qteTotalOffert = BigDecimalUtil.add(qteTotalOffert, mttQteArray[1]);
             
         }
         // Article
         listDet = getListDetail(mapRetour, "ART");
         if (listDet.size() > 0) {
             mttQteArray = addDetail(sb, listDet, "Articles vendus");
             mttTotal = BigDecimalUtil.add(mttTotal, mttQteArray[0]);
             qteTotal = BigDecimalUtil.add(qteTotal, mttQteArray[1]);
             
         }
         listDet = getListDetail(mapRetour, "ANNUL_ART");
         if (listDet.size() > 0) {
             mttQteArray = addDetail(sb, listDet, "Articles annulés");
             mttTotalAnnul = BigDecimalUtil.add(mttTotalAnnul, mttQteArray[0]);
             qteTotalAnnul = BigDecimalUtil.add(qteTotalAnnul, mttQteArray[1]);
             
         }
         listDet = getListDetail(mapRetour, "OFFR_ART");
         if (listDet.size() > 0) {
             mttQteArray = addDetail(sb, listDet, "Articles offerts");
             mttTotalOffert = BigDecimalUtil.add(mttTotalOffert, mttQteArray[0]);
             qteTotalOffert = BigDecimalUtil.add(qteTotalOffert, mttQteArray[1]);
             
         }
         if (!BigDecimalUtil.isZero(mttTotal)) {
        	sb.append("<tr style='background-color: #dfdbbe;font-weight: bold;'>");
         	sb.append("<td align='right' style='width:50%;'>TOTAL COMMANDES</td>");
         	sb.append("<td align='right'></td>");
         	sb.append("<td align='right'>"+BigDecimalUtil.formatNumberZero(mttTotal)+"</td>");
         	sb.append("</tr>");
         }

         if (!BigDecimalUtil.isZero(mttTotalOffert)) {
        	 sb.append("<tr style='background-color: #dfdbbe;font-weight: bold;'>");
          	 sb.append("<td align='right'>TOTAL ARTICLES OFFERS</td>");
          	 sb.append("<td align='right'>"+qteTotalOffert.intValue()+"</td>");
          	 sb.append("<td align='right'>"+BigDecimalUtil.formatNumberZero(mttTotalOffert)+"</td>");
          	 sb.append("</tr>");
         }

         if (!BigDecimalUtil.isZero(mttOffertGlobal)) {
        	 sb.append("<tr style='background-color: #dfdbbe;font-weight: bold;'>");
          	sb.append("<td align='right'>TOTAL OFFERS COMMANDES</td>");
          	sb.append("<td align='right'></td>");
          	sb.append("<td align='right'>"+BigDecimalUtil.formatNumberZero(mttOffertGlobal)+"</td>");
          	sb.append("</tr>");
         }

         if (!BigDecimalUtil.isZero(mttTotalAnnul)) {
        	 sb.append("<tr style='background-color: #dfdbbe;font-weight: bold;'>");
          	sb.append("<td align='right'>TOTAL ANNULÉS ARTICLES</td>");
          	sb.append("<td align='right'>"+qteTotalAnnul.intValue()+"</td>");
          	sb.append("<td align='right'>"+BigDecimalUtil.formatNumberZero(mttTotalAnnul)+"</td>");
          	sb.append("</tr>");
         }
         // Ligne TVA
         
         if(!BigDecimalUtil.isZero(mttTotal)){
	         BigDecimal mttTva = BigDecimalUtil.divide(BigDecimalUtil.multiply(mttTotal, tauxTva), BigDecimalUtil.get(100));
	
	         sb.append("<tr style='background-color: #dfdbbe;font-weight: bold;'>");
	      	sb.append("<td align='right'>TVA (" + tauxTva + "%)</td>");
	      	sb.append("<td align='right'></td>");
	      	sb.append("<td align='right'>"+BigDecimalUtil.formatNumberZero(mttTva)+"</td>");
	      	sb.append("</tr>");
         }
         
         if(!BigDecimalUtil.isZero(mttTotal)){
	         sb.append("<tr style='background-color: #dfdbbe;font-weight: bold;'>");
	      	sb.append("<td align='right'>TOTAL NET</td>");
	      	sb.append("<td align='right'></td>");
	      	sb.append("<td align='right'>"+BigDecimalUtil.formatNumberZero(BigDecimalUtil.substract(mttTotal, mttTotalOffert, mttOffertGlobal))+"</td>");
	      	sb.append("</tr>");
         }
      	
      	sb.append("</table>");
	}

	private BigDecimal[] addDetail(StringBuilder sb, List<MouvementDetailPrintBean> listDetail, String titre){
    	sb.append("<tr style='background-color: #cddc39;line-height: 40px;'>");
     	sb.append("<td colspan='3' align='center'><b>"+titre.toUpperCase()+"</b></td>");
     	sb.append("</tr>");
     	
		//
		BigDecimal mttTotal = null;
	    BigDecimal qteTotal = BigDecimalUtil.get(0);
	    //
	    String oldElementId     = null;
	    BigDecimal sousTotal    = null;
        
	    try{
	   	    Collections.sort(listDetail, new Comparator<MouvementDetailPrintBean>() {
	           	public int compare(final MouvementDetailPrintBean object1, final MouvementDetailPrintBean object2) {
	               	if(object1.getFamille() != null && object2.getFamille() != null) {
	               		return object1.getFamille().getB_left().compareTo(object2.getFamille().getB_left());
	               	} else if(object1.getMenu() != null && object2.getMenu() != null) {
	               		return object1.getMenu().getCode().compareTo(object2.getMenu().getCode());
	               	} else {
	               		return object1.getLibelle().compareTo(object2.getLibelle());
	               	}
	           	}
	           });
	    } catch(Exception e){
	    	e.printStackTrace();// Ne pas bloquer cas d'exception "Comparison method violates its general contract!"
	    }
	    //
   	    List<FamillePersistant> oldFamilles = null;
   	    FamillePersistant oldFamille = null;
   	    Map<Long, BigDecimal> mapParentSousTotals = new HashMap<Long, BigDecimal>();
   	    //
	    for (MouvementDetailPrintBean detail : listDetail) {
	    	if(BigDecimalUtil.isZero(detail.getMtt_total())){
	    		continue;
	    	}
	    	String currElement = null;
	    	// Gestion par famille
	    	if(detail.getFamille() != null){
	    		List<FamillePersistant> listfamilleParent = detail.getListfamilleParent();
	    		currElement = detail.getFamille().getCode();	    		
	    		int ecartLevel = (oldFamille!=null ? oldFamille.getLevel()-detail.getFamille().getLevel() : 0);
	    		
	    		if(oldElementId != null && !oldElementId.equals(currElement)){
	    			
    				sb.append("<tr style='font-weight:bold;background-color: #ffe8ad;'>"
    							+ "<td class='text-right'>SOUS TOTAL</td><td></td><td class='text-right'>"+BigDecimalUtil.formatNumber(sousTotal)+"</td>"
    						+ "</tr>");
	    			
    				boolean isPrentChanged = false;
    				if(listfamilleParent != null && listfamilleParent.size() > 0 ) {	
    					for(int i=listfamilleParent.size()-2; i>=0; i--) {
    	    				FamillePersistant familleParent = listfamilleParent.get(i);
    	    				if(oldFamilles != null && i<oldFamilles.size()-1 && !familleParent.getCode().equals(oldFamilles.get(i).getCode())) {
    	    					isPrentChanged = true;
    	    					break; 
    	    				} else if( oldFamilles != null && i<oldFamilles.size()-1 && ecartLevel > 0&& familleParent.getCode().equals(oldFamilles.get(i).getCode()) ){
    	    					isPrentChanged = true;
    	    					break;
    	    				}
    					}
    				}
    				
    				if(isPrentChanged || (listfamilleParent != null && listfamilleParent.size() == 1)) {
	    				for(int i=ecartLevel; i>=0; i--){
	    					
	    					int ecart = (oldFamilles != null ? oldFamilles.get(i).getLevel().intValue() : 0);
	    					String space = "";
	    					while(ecart>=0) {
		    					space += "&nbsp;&nbsp;&nbsp;";
		    					ecart--;
		    				}
	    					if(oldFamilles != null && oldFamilles.get(i) != null) {
	    						sb.append("<tr style='font-weight:bold;background-color: #ffe8ad;'>"
		    							+ "<td>"+space+"SOUS TOTAL</td><td></td><td class='text-right'>"+BigDecimalUtil.formatNumber(mapParentSousTotals.get(oldFamilles.get(i).getId()))+"</td>"
		    						+ "</tr>");
	    					}
		    			}
    				}
	    			sousTotal = null;
	    		}
	    		
	    		 if(listfamilleParent != null && listfamilleParent.size() > 0 ) {
    	    		sb.append("<tr style='font-weight:bold; background-color: #eae8e8;'><td colspan='3'>");
	    			for(int i=0; i<listfamilleParent.size(); i++) {
	    				FamillePersistant familleParent = listfamilleParent.get(i);
	    				
	    				String space = "";
	    				int decal = listfamilleParent.get(i).getLevel() <=1 ? 0 : listfamilleParent.get(i).getLevel();
	    				
	    				while(decal>0) {
	    					space += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	    					decal--;
	    				}
	    				if(oldFamilles == null || i>oldFamilles.size()-1 || !familleParent.getCode().equals(oldFamilles.get(i).getCode())) {	
	    	    			sb.append(space + familleParent.getLibelle().replace("#", "")+"<br>");
	    				}
    	    		}
    	    		sb.append("</tr></td>");
    	    	}
	    	    	
				oldFamilles = listfamilleParent;
				oldFamille = detail.getFamille();
	    		oldElementId = detail.getFamille().getCode();
	    		
	    		if(listfamilleParent != null) {
		    		for(FamillePersistant famP : listfamilleParent) {
		    			mapParentSousTotals.put(famP.getId(),  BigDecimalUtil.add(mapParentSousTotals.get(famP.getId()), detail.getMtt_total()));
		    		}
	    		}
	    	} 
	    	// Gestion par menu
	    	else if(detail.getMenu() != null){
	    		currElement = detail.getMenu().getCode();
	    		
	    		if(oldElementId == null || !oldElementId.equals(currElement)) {
		    		if(oldElementId != null) { 
		    			sb.append("<tr style='font-weight:bold;background-color: #ffe8ad;'>"
		    						+ "<td class='text-right'>SOUS TOTAL</td><td></td><td class='text-right'>"+BigDecimalUtil.formatNumber(sousTotal)+"</td>"
		    					+ "</tr>");
		    			sousTotal = null;
		    		}
		    		String lib = detail.getMenu().getLibelle();
		    		sb.append("<tr style='font-weight:bold; background-color: #eae8e8;'><td colspan='3'>"+lib+"</td></tr>");
		    	}	    		
	    		
	    		oldElementId = detail.getMenu().getCode();
	    	}
	    	
            mttTotal = BigDecimalUtil.add(mttTotal, detail.getMtt_total());
            sousTotal = BigDecimalUtil.add(sousTotal, detail.getMtt_total());
            
            qteTotal = BigDecimalUtil.add(qteTotal, detail.getQuantite());
            
            BigDecimal quantite = detail.getQuantite();
            BigDecimal prix = detail.getMtt_total();
            String prixStr = BigDecimalUtil.formatNumberZero(prix);
            String qte = quantite != null ? "" + quantite.intValue() : "";
	
            sb.append("<tr>");
        	sb.append("<td style='width:50%;'>"+ detail.getLibelle().toUpperCase()+"</td>");
        	sb.append("<td align='right'>"+qte+"</td>");
        	sb.append("<td align='right'>"+prixStr+"</td>");
        	sb.append("</tr>");
	    }
	    
	    if(oldFamille != null){
			sb.append("<tr style='font-weight:bold;background-color: #ffe8ad;'><td class='text-right'>SOUS TOTAL</td><td></td><td class='text-right'>"+BigDecimalUtil.formatNumber(sousTotal)+"</td></tr>");
    		sb.append("<tr style='font-weight:bold;background-color: #ffe8ad;'><td class='text-right'>SOUS TOTAL FAMILLE</td><td></td><td class='text-right'>"+BigDecimalUtil.formatNumber(mapParentSousTotals.get(oldFamilles.get(0).getId()))+"</td></tr>");
		} else if(oldElementId != null) {   
			sb.append("<tr style='font-weight:bold;background-color: #ffe8ad;'><td class='text-right'>SOUS TOTAL</td><td></td><td class='text-right'>"+BigDecimalUtil.formatNumber(sousTotal)+"</td></tr>");
		}
	    
//	    sb.append("<tr style='background-color: #dfdbbe;font-weight: bold;'>");
//    	sb.append("<td align='right' style='width:50%;'>TOTAL</td>");
//    	sb.append("<td align='right'></td>");
//    	sb.append("<td align='right'>"+BigDecimalUtil.formatNumberZero(mttTotal)+"</td>");
//    	sb.append("</tr>");
    	
	    BigDecimal[] data = {mttTotal, qteTotal};

        return data;
	}
	
	/**
	 * @param type
	 * @return
	 */
	private List<MouvementDetailPrintBean> getListDetail(Map<String, Object> mapRetour, String type) {
		List<MouvementDetailPrintBean> listDetail = (List<MouvementDetailPrintBean>) mapRetour.get("list_cmd");
	    Map<String, MouvementDetailPrintBean> mapDet = new LinkedHashMap<>();
	    
	    if(listDetail == null){
	    	return new ArrayList<>();
	    }
	    for (MouvementDetailPrintBean det : listDetail) {
	        if (det.getType().equals(type)) {
	            final String key = det.getCode().trim().toUpperCase();
	            MouvementDetailPrintBean mvm = mapDet.get(key);
	            if (mvm == null) {
	                try {
						mvm = (MouvementDetailPrintBean) BeanUtils.cloneBean(det);
					} catch (Exception e) {
						e.printStackTrace();
					}
	                mapDet.put(key, mvm);
	            } else {
	                mvm.setMtt_total(BigDecimalUtil.add(mvm.getMtt_total(), det.getMtt_total()));
	                mvm.setQuantite(BigDecimalUtil.add(mvm.getQuantite(), det.getQuantite()));
	            }
	        }
	    }
	    
	    List<MouvementDetailPrintBean> listData = new ArrayList<>();
	    for(String key : mapDet.keySet()){
	    	listData.add(mapDet.get(key));
	    }
	    
	    return listData;
	}
}
