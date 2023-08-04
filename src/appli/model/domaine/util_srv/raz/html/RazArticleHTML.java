package appli.model.domaine.util_srv.raz.html;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import com.itextpdf.text.DocumentException;

import appli.model.domaine.stock.service.impl.RepartitionBean;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.StringUtil;

public class RazArticleHTML {
	
	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */
	public String getHtml(String titre, String dtTxt, 
			Map menuData, boolean isQteOnly) {
		
		StringBuilder sb = new StringBuilder();
		ajouterEntete(sb, titre);
		
		sb.append("<table class='table table-hover' style='width:80%;'>");
		ajouterTitre(sb, dtTxt, isQteOnly);
		ajouterContenu(sb, menuData, isQteOnly);	
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
	
	private void ajouterTitre(StringBuilder sb, String date, boolean isQteOnly){
		sb.append("<thead class='bordered-darkorange'>"
				+ (StringUtil.isNotEmpty(date) ? "<tr>"
						+ "<th style='text-align:center;' colspan='"+(isQteOnly?2:3)+"'>"
						+ "<h3>Date du "+date+"</h3>"
						+ "</th>"
						+ "</tr>" : "")
				+ "<tr>"
					+ "<th>ARTICLE</th>"
					+ "<th style='text-align:right;'>QUANTITÉ</th>"
					+ (isQteOnly ? "":"<th style='text-align:right;'>MONTANT</th> ")
				+ "</tr>"
				+ "</thead>");
	}
	
	/**
	 * @param document
	 * @param cupP
	 */
	private void ajouterContenu(StringBuilder sb, Map data, boolean isQteOnly) {
		Map<Long, RepartitionBean> menuMap = (Map<Long, RepartitionBean>) data.get("MENU");
		Map<Long, RepartitionBean> menuArtsMap = (Map<Long, RepartitionBean>) data.get("MENU_ARTS");
		Map<Long, RepartitionBean> artsMap = (Map<Long, RepartitionBean>) data.get("ARTS");
		BigDecimal offreMtt = (BigDecimal) data.get("OFFRE");
		BigDecimal livraisonMtt = (BigDecimal) data.get("LIVRAISON");
		BigDecimal mttTotalNet = (BigDecimal) data.get("VENTE_NET");
		BigDecimal mttTotal = (BigDecimal) data.get("VENTE");
		
         //---------- On collecte les taux de TVA -----------------------
//         BigDecimal mttTotal = BigDecimalUtil.get(0);
         
         // Menu
         if(!isQteOnly){
	         if(menuMap != null && menuMap.size() > 0){
	        	 sb.append("<tr><td colspan='3' style='background-color: #ffeb3b;'><b>VENTE MENUS</b></td></tr>");
		         for(Long key : menuMap.keySet()){
					 RepartitionBean repB = menuMap.get(key);
					 
				       String qte = "";
			  	       if(repB.getQuantite() != null){
			  	    	   if(repB.getQuantite().doubleValue() % 1 != 0){
			  	    		 qte = BigDecimalUtil.formatNumber(repB.getQuantite());
			  	    	   } else{
			  	    		 qte = ""+repB.getQuantite().intValue();
			  	    	   }
			  	       }
			  	       
		        	 sb.append("<tr>");
		          	 sb.append("<td align='left' style='padding-left: 50px;'>"+repB.getLibelle()+"</td>");
		          	sb.append("<td align='right'>"+qte+"</td>");
		          	 sb.append("<td align='right' style='font-weight:bold;'>"+BigDecimalUtil.formatNumber(repB.getMontant())+"</td>");
		          	 sb.append("</tr>");
		          	 
//		          	mttTotal = BigDecimalUtil.add(mttTotal, repB.getMontant());
		         }
	         }
	         // Article
	         if(artsMap.size() > 0){
		         sb.append("<tr><td colspan='3' style='background-color: #ffeb3b;'><b>VENTE ARTICLES</b></td></tr>");
		         String oldFam = null;
		         BigDecimal subTotal = null;
		         for(Long key : artsMap.keySet()){
					 RepartitionBean repB = artsMap.get(key);
					 if(oldFam == null || !oldFam.equals(repB.getFamille())){
						// Sous total -------------------------------------------
						 if(oldFam != null) {
							 sb.append("<tr>");
				          	 sb.append("<td colspan='3' align='right' style='font-weight:bold;background-color: #f7f7f7;font-size: 17px;color: #9c27b0;'>"+BigDecimalUtil.formatNumber(subTotal)+"</td>");
				          	 sb.append("</tr>");
							 subTotal = null;
						 }
						 // -------------------------------------------------------
						 
						 sb.append("<tr><td colspan='3'><b>"+repB.getFamille()+"</b></td></tr>");
					 }
					 
					 subTotal = BigDecimalUtil.add(subTotal, repB.getMontant());
					 
				       String qte = "";
			  	       if(repB.getQuantite() != null){
			  	    	   if(repB.getQuantite().doubleValue() % 1 != 0){
			  	    		 qte = BigDecimalUtil.formatNumber(repB.getQuantite());
			  	    	   } else{
			  	    		 qte = ""+repB.getQuantite().intValue();
			  	    	   }
			  	       }
			  	       
		        	 sb.append("<tr>");
		          	 sb.append("<td align='left' style='padding-left: 50px;'>"+repB.getLibelle()+"</td>");
		          	sb.append("<td align='right'>"+qte+"</td>");
		          	 sb.append("<td align='right' style='font-weight:bold;'>"+BigDecimalUtil.formatNumber(repB.getMontant())+"</td>");
		          	 sb.append("</tr>");
		          	
		          	oldFam = repB.getFamille();
//		          	mttTotal = BigDecimalUtil.add(mttTotal, repB.getMontant());
		         }
		         
		      // Sous total -------------------------------------------
				 if(oldFam != null) {
					 sb.append("<tr>");
		          	 sb.append("<td colspan='3' align='right' style='font-weight:bold;background-color: #f7f7f7;font-size: 17px;color: #9c27b0;'>"+BigDecimalUtil.formatNumber(subTotal)+"</td>");
		          	 sb.append("</tr>");
					 subTotal = null;
				 }
				 // -------------------------------------------------------
	         }
	         if(!isQteOnly){
		         if (!BigDecimalUtil.isZero(mttTotal)) {
		          	sb.append("<tr style='background-color: #dfdbbe;font-weight: bold;'>");
		           	sb.append("<td align='right'>TOTAL</td>");
		           	sb.append("<td align='right'></td>");
		           	sb.append("<td align='right' style='font-weight:bold;'>"+BigDecimalUtil.formatNumberZero(mttTotal)+"</td>");
		           	sb.append("</tr>");
		           }
		          
		          if(!BigDecimalUtil.isZero(offreMtt)){
		 	         sb.append("<tr>");
		 	      	 sb.append("<td align='left'>OFFRES</td>");
		 	      	 sb.append("<td align='right'></td>");
		 	      	 sb.append("<td align='right' style='font-weight:bold;'>"+BigDecimalUtil.formatNumber(offreMtt)+"</td>");
		 	      	 sb.append("</tr>");
		          }
		       	 
		          if(!BigDecimalUtil.isZero(livraisonMtt)){
		 	         sb.append("<tr>");
		 	      	 sb.append("<td align='left'>LIVRAISONS</td>");
		 	      	 sb.append("<td align='right'></td>");
		 	      	 sb.append("<td align='right' style='font-weight:bold;'>"+BigDecimalUtil.formatNumber(livraisonMtt)+"</td>");
		 	      	 sb.append("</tr>");
		          }
		       	 
		          if (!BigDecimalUtil.isZero(mttTotalNet)) {
		         	sb.append("<tr style='background-color: #dfdbbe;font-weight: bold;'>");
		          	sb.append("<td align='right'>TOTAL NET</td>");
		          	sb.append("<td align='right'></td>");
		          	sb.append("<td align='right' style='font-weight:bold;'>"+BigDecimalUtil.formatNumberZero(mttTotalNet)+"</td>");
		          	sb.append("</tr>");
		          }
		          
		          sb.append("<tr><td colspan='3'><hr></td></tr>");
	         }
         } else{
	         // Ajout articles menus manquants
        	 if(menuArtsMap != null) {
		 		for(Long artId : menuArtsMap.keySet()){
		 			if(artsMap.get(artId) == null){
		 				artsMap.put(artId, menuArtsMap.get(artId));
		 			}
		 		}
        	 }
	 		// Article
	         if(artsMap.size() > 0){
		         sb.append("<tr><td colspan='2' style='background-color: #ffeb3b;'><b>DETAIL DES ARTICLES</b></td></tr>");
		         String oldFam = null;
		         for(Long key : artsMap.keySet()){
					 RepartitionBean repB = artsMap.get(key);
					 if(oldFam == null || !oldFam.equals(repB.getFamille())){
						 sb.append("<tr><td colspan='3'><b>"+repB.getFamille()+"</b></td></tr>");
					 }
					 
				       String qte = "";
			  	       if(repB.getQuantite() != null){
			  	    	   if(repB.getQuantite().doubleValue() % 1 != 0){
			  	    		 qte = BigDecimalUtil.formatNumber(repB.getQuantite());
			  	    	   } else{
			  	    		 qte = ""+repB.getQuantite().intValue();
			  	    	   }
			  	       }
					 
		        	 sb.append("<tr>");
		          	 sb.append("<td align='left' style='padding-left: 50px;'>"+repB.getLibelle()+"</td>");
		          	sb.append("<td align='right'>"+qte+"</td>");
		          	 sb.append("</tr>");
		          	
		          	oldFam = repB.getFamille();
		         }
	         }
         }
	}
}
