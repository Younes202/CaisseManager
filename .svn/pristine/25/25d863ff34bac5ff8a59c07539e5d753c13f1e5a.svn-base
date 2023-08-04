package appli.model.domaine.util_srv.raz.html;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.DocumentException;

import appli.model.domaine.stock.service.impl.RepartitionBean;
import appli.model.domaine.util_srv.raz.RazDetail;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.StringUtil;

public class RazPosteHTML {
	
	public enum MODE_PDF {
		MODEL, ARCHIVE, PAIE_ALL, PAIE
	}
	
	
	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */
	public String getHtml(String titre, String date, Map<String, List<RazDetail>> mapModePaiement, Map<String, Map<Long, RepartitionBean>> mapRep) {
		
		StringBuilder sb = new StringBuilder();
		ajouterEntete(sb, titre);
		
		 sb.append("<table class='table table-hover' style='width:80%;'>");
		 ajouterTitre(sb, date);
		 ajouterContenu(sb, mapModePaiement, mapRep);	
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
	
	private void ajouterTitre(StringBuilder sb, String date){
		sb.append("<thead class='bordered-darkorange'>"
				+ (StringUtil.isNotEmpty(date) ? "<tr><th style='text-align:center;' colspan='3'><h3>Date du "+date+"</h3></th></tr>" : "")
				+ "<tr>"
					+ "<th>MODE DE PAIEMENT</th>"
					+ "<th style='text-align:right;'>NBR. COMMANDES</th>"
					+ "<th style='text-align:right;'>MONTANT</th>"
				+ "</tr>"
				+ "</thead>");
	}
	
	/**
	 * @param document
	 * @param cupP
	 */
	private void ajouterContenu(StringBuilder sb, Map<String, List<RazDetail>> mapModePaiement, Map<String, Map<Long, RepartitionBean>> mapRep) {
		if(mapModePaiement.size() == 0){
			sb.append("<tr>");
          	sb.append("<td align='center' style='color:blue;' colspan='3'>Aucune donnée</td>");
          	sb.append("</tr>");
		} else {
			int ttlQte = 0;
			BigDecimal ttlMtt = null;
		    
			for (String caisse : mapModePaiement.keySet()) {
				sb.append("<tr style='background-color:#fff3d3;'>");
		      	sb.append("<td colspan='3' style='font-weight:bold;'>"+caisse+"</td>");
		      	sb.append("</tr>");
		      	
				List<RazDetail> listDet = mapModePaiement.get(caisse);
				for (RazDetail razDetail : listDet) {
			    	sb.append("<tr>");
			      	sb.append("<td style='width:50%;'>"+razDetail.getLibelle()+"</td>");
			      	sb.append("<td align='right' style='color:blue;'>"+razDetail.getQuantite()+"</td>");
			      	sb.append("<td align='right' style='font-weight:bold;'>"+BigDecimalUtil.formatNumberZero(razDetail.getMontant())+"</td>");
			      	sb.append("</tr>");
			      	
			      	ttlQte = ttlQte + razDetail.getQuantite();
			      	ttlMtt = BigDecimalUtil.add(ttlMtt, razDetail.getMontant());
			     }  	
			}
	      	sb.append("<tr style='background-color: #e0f7fa;'>");
	      	sb.append("<td align='right'>TOTAL</td>");
	      	sb.append("<td align='right' style='color:blue;'>"+ttlQte+"</td>");
	      	sb.append("<td align='right' style='font-weight:bold;'>"+BigDecimalUtil.formatNumberZero(ttlMtt)+"</td>");
	      	sb.append("</tr>");
	      	sb.append("</table>");
	      	
	      	
	      	sb.append("<br>"
	      			+ "<br>"
	      			
	      			+ "<table class='table table-hover' style='width:80%;'>"
	      			+ "<thead class='bordered-darkorange'>"
					+ "<tr>"
						+ "<th>ARTICLE</th>"
						+ "<th style='text-align:right;'>QUANTITE</th>"
					+ "</tr>"
					+ "</thead>");
	      	
	      	// Détail Article
	      	for (String caisse : mapRep.keySet()) {
			     Map<Long, RepartitionBean> artsMap = mapRep.get(caisse);
			     sb.append("<tr><td colspan='2' style='background-color: #e0f7fa;'><b>"+caisse+"</b></td></tr>");
			     
		         if(artsMap.size() > 0){
			         String oldFam = null;
			         for(Long key : artsMap.keySet()){
						 RepartitionBean repB = artsMap.get(key);
						 if(oldFam == null || !oldFam.equals(repB.getFamille())){
							 sb.append("<tr><td colspan='2'><b>"+repB.getFamille()+"</b></td></tr>");
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
}
