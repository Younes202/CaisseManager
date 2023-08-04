package appli.model.domaine.util_srv.raz.html;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.itextpdf.text.DocumentException;

import appli.model.domaine.util_srv.raz.RazDetail;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.StringUtil;

public class RazSocieteLivraisonHTML {
	
	public enum MODE_PDF {
		MODEL, ARCHIVE, PAIE_ALL, PAIE
	}
	
	
	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */
	public String getHtml(String titre, String date, List<RazDetail> listData, List<RazDetail> listPaie) {
		
		StringBuilder sb = new StringBuilder();
		
		ajouterEntete(sb, titre);
		
		sb.append("<table class='table table-hover' style='width:80%;'>");
		ajouterTitre(sb, date);
		ajouterContenu(sb, listData, listPaie);	
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
					+ "<th>SOCIÉTÉ</th>"
					+ "<th style='text-align:right;'>MONTANT VENTES</th>"
					+ "<th style='text-align:right;'>MONTANT PART</th>"
				+ "</tr>"
				+ "</thead>");
	}
	
	/**
	 * @param document
	 * @param cupP
	 */
	private void ajouterContenu(StringBuilder sb, List<RazDetail> listData, List<RazDetail> listPaie) {
		
		if(listData.size() == 0 && listPaie.size() == 0){
			sb.append("<tr>");
          	sb.append("<td align='center' style='color:blue;' colspan='3'>Aucune donnée</td>");
          	sb.append("</tr>");
          	return;
		}
		
		int ttlQte = 0;
		BigDecimal ttlMtt = null; 
		for (RazDetail razDetail : listData) {
        	sb.append("<tr>");
          	sb.append("<td style='width:50%;'>"+razDetail.getLibelle()+"</td>");
          	sb.append("<td align='right' style='color:blue;'>"+BigDecimalUtil.formatNumberZero(razDetail.getMontant())+"</td>");
          	sb.append("<td align='right' style='font-weight:bold;'>"+BigDecimalUtil.formatNumberZero(razDetail.getMontant2())+"</td>");
          	sb.append("</tr>");
          	
          	ttlQte = ttlQte + razDetail.getQuantite();
	      	ttlMtt = BigDecimalUtil.add(ttlMtt, razDetail.getMontant());

		}
         sb.append("<tr style='background-color: #e0f7fa;'>");
       	sb.append("<td align='right'>TOTAL</td>");
       	sb.append("<td align='right' style='color:blue;'>"+ttlQte+"</td>");
       	sb.append("<td align='right' style='font-weight:bold;'>"+BigDecimalUtil.formatNumberZero(ttlMtt)+"</td>");
       	sb.append("</tr>");
       	
       	ttlQte = 0;
		ttlMtt = null;
         for (RazDetail razDetail : listPaie) {
        	sb.append("<tr>");
           	sb.append("<td style='width:50%;'>"+razDetail.getLibelle()+"</td>");
           	sb.append("<td align='right' style='color:blue;'>"+BigDecimalUtil.formatNumberZero(razDetail.getMontant())+"</td>");
           	sb.append("<td align='right' style='font-weight:bold;'>"+BigDecimalUtil.formatNumberZero(razDetail.getMontant2())+"</td>");
           	sb.append("</tr>");
           	
           	ttlQte = ttlQte + razDetail.getQuantite();
	      	ttlMtt = BigDecimalUtil.add(ttlMtt, razDetail.getMontant());

		}
         sb.append("<tr style='background-color: #e0f7fa;'>");
       	sb.append("<td align='right'>TOTAL</td>");
       	sb.append("<td align='right' style='color:blue;'>"+ttlQte+"</td>");
       	sb.append("<td align='right' style='font-weight:bold;'>"+BigDecimalUtil.formatNumberZero(ttlMtt)+"</td>");
       	sb.append("</tr>");
	}
}
