package appli.model.domaine.util_srv.raz.html;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import com.itextpdf.text.DocumentException;

import appli.controller.domaine.util_erp.ContextAppli.PARAM_APPLI_ENUM;
import appli.controller.util_ctrl.MouvementDetailPrintBean;
import framework.controller.ContextGloabalAppli;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.StringUtil;

public class RazBoissonHTML {
	
	public enum MODE_PDF {
		MODEL, ARCHIVE, PAIE_ALL, PAIE
	}
	
	private BigDecimal mttTotalAll = BigDecimalUtil.get(0);
	private BigDecimal qteTotalAll = BigDecimalUtil.get(0);
	
	
	/**
	 * @param dest
	 * @throws IOException
	 * @throws DocumentException
	 */
	public String getHtml(String titre, String date, Map<String, Object> mapData) {
		StringBuilder sb = new StringBuilder();
		Map<String, MouvementDetailPrintBean> mapCmdBoissonsFroide = (Map<String, MouvementDetailPrintBean>) mapData.get("data_froid");
		Map<String, MouvementDetailPrintBean> mapCmdBoissonsChaude = (Map<String, MouvementDetailPrintBean>) mapData.get("data_chaud");
		
		 ajouterEntete(sb, titre);
		
		 sb.append("<table class='table table-hover' style='width:80%;'>");
		 ajouterTitre(sb, date);
		 ajouterContenu(sb, mapCmdBoissonsFroide, "Boissons froides");
		 ajouterContenu(sb, mapCmdBoissonsChaude, "Boissons chaudes");
		 
		 BigDecimal tauxTva = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig(PARAM_APPLI_ENUM.TVA_VENTE.toString()));
         BigDecimal mttHt = BigDecimalUtil.getMttHt(mttTotalAll, tauxTva);
         BigDecimal mttTva = BigDecimalUtil.substract(mttTotalAll, mttHt); 
        
        sb.append("<tr><td colspan='3'><hr></td></tr>");
        sb.append("<tr style='background-color: #dfdbbe;font-weight: bold;'>");
    	sb.append("<td align='right'>TOTAL GLOBAL</td>");
    	sb.append("<td align='right' style='color:blue;'>"+""+qteTotalAll.intValue()+"</td>");
    	sb.append("<td align='right' style='font-weight:bold;'>"+BigDecimalUtil.formatNumberZero(mttTotalAll)+"</td>");
    	sb.append("</tr>");
    	
        sb.append("<tr style='background-color: #dfdbbe;font-weight: bold;'>");
    	sb.append("<td align='right'>"+"TVA (" + tauxTva + "%)"+"</td>");
    	sb.append("<td align='right'></td>");
    	sb.append("<td align='right'>"+BigDecimalUtil.formatNumberZero(mttTva)+"%</td>");
    	sb.append("</tr>");
	    	
		 sb.append("</table>");
		
		return sb.toString();
	}

	private void ajouterTitre(StringBuilder sb, String date){
		sb.append("<thead class='bordered-darkorange'>"
				+ (StringUtil.isNotEmpty(date) ? "<tr><th style='text-align:center;' colspan='3'><h3>Date du "+date+"</h3></th></tr>" : "")
				+ "<tr>"
					+ "<th>ARTICLE</th>"
					+ "<th style='text-align:right;'>QUANTITÉ</th>"
					+ "<th style='text-align:right;'>MONTANT</th>"
				+ "</tr>"
				+ "</thead>");
	}
	
	/**
	 * @param document
	 * @param cupP
	 * @throws DocumentException 
	 */
	private void ajouterEntete(StringBuilder sb, String titre) {
		sb.append("<h3>"+titre+"</h3>");
	}
	
	/**
	 * @param document
	 * @param cupP
	 */
	private void ajouterContenu(StringBuilder sb, Map<String, MouvementDetailPrintBean> mapCmdBoissons, String titre) {
		if(mapCmdBoissons == null || mapCmdBoissons.size() == 0){
			sb.append("<tr>");
          	sb.append("<td align='center' style='color:blue;' colspan='3'>Aucune donnée</td>");
          	sb.append("</tr>");
          	return;
		}
        if (mapCmdBoissons != null && mapCmdBoissons.size() > 0) {
    		sb.append("<tr style='background-color: #cddc39;line-height: 20px;'><td colspan='3'><b>"+titre+"</b></td></tr>");
        	//
        	BigDecimal mttTotal = null;
            BigDecimal qteTotal = BigDecimalUtil.get(0);
            //
            for (String key : mapCmdBoissons.keySet()) {
                MouvementDetailPrintBean detail = mapCmdBoissons.get(key);
                
                if(!key.startsWith("")){
                    continue;
                }
                
                mttTotal = BigDecimalUtil.add(mttTotal, detail.getMtt_total());
                qteTotal = BigDecimalUtil.add(qteTotal, detail.getQuantite());

                BigDecimal quantite = detail.getQuantite();
                BigDecimal prix = detail.getMtt_total();
                String prixStr = BigDecimalUtil.formatNumberZero(prix);
                String qte = quantite != null ? "" + quantite.intValue() : "";

                sb.append("<tr>");
            	sb.append("<td style='width:50%;'>"+detail.getLibelle().toUpperCase()+"</td>");
            	sb.append("<td align='right' style='color:blue;'>"+qte+"</td>");
            	sb.append("<td align='right' style='font-weight:bold;'>"+prixStr+"</td>");
            	sb.append("</tr>");
            }
            
            sb.append("<tr style='background-color: #dfdbbe;font-weight: bold;'>");
        	sb.append("<td align='right'>TOTAL</td>");
        	sb.append("<td align='right' style='color:blue;'>"+""+qteTotal.intValue()+"</td>");
        	sb.append("<td align='right' style='font-weight:bold;'>"+BigDecimalUtil.formatNumberZero(mttTotal)+"</td>");
        	sb.append("</tr>");
        	
        	qteTotalAll = BigDecimalUtil.add(qteTotalAll, qteTotal);
        	mttTotalAll = BigDecimalUtil.add(mttTotalAll, mttTotal);
        }
	}
}
