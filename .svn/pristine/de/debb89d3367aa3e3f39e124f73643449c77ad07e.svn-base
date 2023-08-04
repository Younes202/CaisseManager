package appli.model.domaine.util_srv.raz.html;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import com.itextpdf.text.DocumentException;

import appli.model.domaine.util_srv.raz.RazDetail;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.StringUtil;

public class RazGlobaleHTML {
	
	public enum MODE_PDF {
		MODEL, ARCHIVE, PAIE_ALL, PAIE
	}
	
	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */
	public String getHtml(String titre, String text, Map<String, Map<String, RazDetail>> data) {
		
		StringBuilder sb = new StringBuilder();
		ajouterEntete(sb, titre);
		
		sb.append("<table class='table table-hover' style='width:80%;'>");
		ajouterTitre(sb, text);
		ajouterContenu(sb, data);	
		sb.append("</table>");
		
		return sb.toString();
	}

	/**
	 * @param document
	 * @param cupP
	 * @throws DocumentException 
	 */
	private void ajouterEntete(StringBuilder sb, String data) {
		sb.append("<h3>"+data+"</h3>");
	}
	
	private void ajouterTitre(StringBuilder sb, String date){
		sb.append("<thead class='bordered-darkorange'>"
				+ (StringUtil.isNotEmpty(date) ? "<tr><th style='text-align:center;' colspan='3'><h3>Date du "+date+"</h3></th></tr>" : "")
				+ "<tr>"
					+ "<th>#</th>"
					+ "<th style='text-align:right;'>QUANTITÉ</th>"
					+ "<th style='text-align:right;'>MONTANT</th>"
				+ "</tr>"
				+ "</thead>");
	}
	
	/**
	 * @param document
	 * @param cupP
	 */
	private void ajouterContenu(StringBuilder sb, Map<String, Map<String, RazDetail>> data) {
		if(data.size() == 0){
			sb.append("<tr>");
          	sb.append("<td align='center' style='color:blue;' colspan='3'>Aucune donnée</td>");
          	sb.append("</tr>");
          	return;
		}
		
		for(String key : data.keySet()){
			BigDecimal ttlMtt = null;
			Map<String, RazDetail> recapMap = data.get(key);
			
			if(recapMap.size() == 0){
				continue;
			}
			
			sb.append("<tr style='background-color: black;'>");
          	sb.append("<td colspan='3' style='color:white;' colspan='3'>"+key+"</td>");
          	sb.append("</tr>");
            
	       	 for(String key2 : recapMap.keySet()){	
	       		RazDetail razDetail = recapMap.get(key2);
	        	sb.append("<tr>");
	          	sb.append("<td align='right' style='width:50%;'>"+razDetail.getLibelle()+"</td>");
	          	sb.append("<td align='right' style='color:blue;'>"+(razDetail.getQuantite()!=null && razDetail.getQuantite()>0?razDetail.getQuantite():"")+"</td>");
	          	sb.append("<td align='right' style='font-weight:bold;'>"+BigDecimalUtil.formatNumberZero(razDetail.getMontant())+"</td>");
	          	sb.append("</tr>");
	          	
		      	ttlMtt = BigDecimalUtil.add(ttlMtt, razDetail.getMontant());
			}
	       	sb.append("<tr style='background-color: #e0f7fa;'>");
	       	sb.append("<td align='right' style='color: #e91e63;font-weight: bold;'>TOTAL</td>");
	       	sb.append("<td align='right' style='color:blue;'></td>");
	       	sb.append("<td align='right' style='font-weight:bold;color: #e91e63;font-size: 14px;'>"+BigDecimalUtil.formatNumberZero(ttlMtt)+"</td>");
	       	sb.append("</tr>");
		}
	}
}
