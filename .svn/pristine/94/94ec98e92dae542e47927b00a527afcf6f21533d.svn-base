package appli.model.domaine.util_srv.raz.html;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.itextpdf.text.DocumentException;

import appli.model.domaine.util_srv.raz.RazDetail;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.StringUtil;

public class RazLivraisonHTML {
	
	public enum MODE_PDF {
		MODEL, ARCHIVE, PAIE_ALL, PAIE
	}
	
	
	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */
	public String getHtml(String titre, String text, List<RazDetail> data) {
		
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
					+ "<th>MODE DE PAIEMENT</th>"
					+ "<th style='text-align:right;'>QUANTITÉ</th>"
					+ "<th style='text-align:right;'>MONTANT</th>"
				+ "</tr>"
				+ "</thead>");
	}
	
	/**
	 * @param document
	 * @param cupP
	 */
	private void ajouterContenu(StringBuilder sb, List<RazDetail> data) {
		if(data.size() == 0){
			sb.append("<tr>");
          	sb.append("<td align='center' style='color:blue;' colspan='3'>Aucune donnée</td>");
          	sb.append("</tr>");
          	return;
		}
		
		int ttlQte = 0;
		BigDecimal ttlMtt = null, ttlMtt2 = null;
         for (RazDetail razDetail : data) {
        	 sb.append("<tr>");
          	sb.append("<td align='right' style='width:50%;'>"+razDetail.getLibelle()+"</td>");
          	sb.append("<td align='right' style='color:blue;'>"+razDetail.getQuantite()+"</td>");
          	sb.append("<td align='right' style='font-weight:bold;'>"+BigDecimalUtil.formatNumberZero(razDetail.getMontant())+"</td>");
          	sb.append("<td align='right' style='font-weight:bold;'>"+BigDecimalUtil.formatNumberZero(razDetail.getMontant2())+"</td>");
          	sb.append("</tr>");
          	
          	ttlQte = ttlQte + razDetail.getQuantite();
	      	ttlMtt = BigDecimalUtil.add(ttlMtt, razDetail.getMontant());
	      	ttlMtt2 = BigDecimalUtil.add(ttlMtt2, razDetail.getMontant2());
		}
       	sb.append("<tr style='background-color: #e0f7fa;'>");
       	sb.append("<td align='right'>TOTAL</td>");
       	sb.append("<td align='right' style='color:blue;'>"+ttlQte+"</td>");
       	sb.append("<td align='right' style='font-weight:bold;'>"+BigDecimalUtil.formatNumberZero(ttlMtt)+"</td>");
       	sb.append("<td align='right' style='font-weight:bold;'>"+BigDecimalUtil.formatNumberZero(ttlMtt2)+"</td>");
       	sb.append("</tr>");
	}
}
