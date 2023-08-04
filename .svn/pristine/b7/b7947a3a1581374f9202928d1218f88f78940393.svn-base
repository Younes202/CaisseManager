<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="appli.model.domaine.administration.persistant.EtatFinanceDetailPersistant"%>
<%@page import="appli.controller.domaine.administration.bean.EtatFinanceBean"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@page errorPage="/commun/error.jsp"%>

<%
EtatFinanceBean etatFinanceBean = (EtatFinanceBean) request.getAttribute("etatFinanceBean");
String devise = "&nbsp;"+StrimUtil.getGlobalConfigPropertie("devise.symbole");
BigDecimal soldeStock = null, valeurStockActuel = null, valeurStockPrecedent = null,
		soldeBanque = null, soldeBanqueActuel = null, soldeBanquePrecedent= null;
for(EtatFinanceDetailPersistant det : etatFinanceBean.getList_detail()){
	if(det.getType().equals("EMPL")){
		soldeStock = BigDecimalUtil.add(soldeStock, BigDecimalUtil.substract(det.getMtt_etat_actuel(), det.getMtt_etat_prev()));
		valeurStockActuel = BigDecimalUtil.add(valeurStockActuel, det.getMtt_etat_actuel());
		valeurStockPrecedent = BigDecimalUtil.add(valeurStockPrecedent, det.getMtt_etat_prev());
	}
	if(det.getType().equals("BANQ")){
		soldeBanque = BigDecimalUtil.add(soldeBanque, BigDecimalUtil.substract(det.getMtt_etat_actuel(), det.getMtt_etat_prev()));
		soldeBanqueActuel = BigDecimalUtil.add(soldeBanqueActuel, det.getMtt_etat_actuel());
		soldeBanquePrecedent = BigDecimalUtil.add(soldeBanquePrecedent, det.getMtt_etat_prev());
	}
}
%>
<style>
	#resume_finance tr{
		font-size: 12px;
	}
	#resume_finance td{
		padding: 4px;
	}
</style>

<table class="table table-hover" id="resume_finance">
	<tbody>
		<tr>
			<td colspan="2" style="background-color: #80deea;">ENTREES</td>
		</tr>
		<tr>	
			<td>Ventes caisse</td>
			<td style="text-align: right;color: green;"><fmt:formatDecimal value="${etatFinanceBean.mtt_vente_caisse }"/></td>
		<tr>
		<tr>	
			<td>Livraison</td>
			<td style="text-align: right;color: green;"><fmt:formatDecimal value="${etatFinanceBean.mtt_livraison_part }"/></td>
		<tr>
		<tr>	
			<td>Ventes hors caisse</td>
			<td style="text-align: right;color: green;"><fmt:formatDecimal value="${etatFinanceBean.mtt_vente_hors_caisse }"/></td>
		<tr>
		<tr>	
			<td>Recette divers</td>
			<td style="text-align: right;color: green;"><fmt:formatDecimal value="${etatFinanceBean.mtt_recette_divers }"/></td>
		<tr>
		<tr>
			<td style="text-align:right">TOTAL</td>
			<td style="text-align: right;font-weight: bold;">
				<span class="badge badge-success badge-square">
				<fmt:formatDecimal value="${etatFinanceBean.mtt_vente_caisse 
                     			+ etatFinanceBean.mtt_livraison_part 
                     			+ etatFinanceBean.mtt_vente_hors_caisse 
                     			+ etatFinanceBean.mtt_recette_divers }"/>
            	</span>
            </td>
		</tr>
		
		<!-- ****************************************** -->
		<tr>
			<td colspan="2" style="background-color: #80deea;">SORTIES</td>
		</tr>
		
		<tr>
			<td>D&eacute;penses divers</td>
			<td style="text-align: right;color: red;"><fmt:formatDecimal value="${etatFinanceBean.mtt_depense_divers }"/></td>
		</tr>
		<tr>
			<td>Achats</td>
			<td style="text-align: right;color: red;"><fmt:formatDecimal value="${etatFinanceBean.mtt_achat }"/></td>
		</tr>
		<tr>	
			<td>Salaires</td>
			<td style="text-align: right;color: red;"><fmt:formatDecimal value="${etatFinanceBean.mtt_salaire }"/></td>
		<tr>
		<tr>	
			<td>Frais livreurs</td>
			<td style="text-align: right;color: red;"><fmt:formatDecimal value="${etatFinanceBean.mtt_livraison-etatFinanceBean.mtt_livraison_part }"/></td>
		<tr>
		<tr>
			<td style="text-align:right">TOTAL</td>
			<td style="text-align: right;font-weight: bold;">
				<span class="badge badge-danger badge-square">
						<fmt:formatDecimal value="${etatFinanceBean.mtt_depense_divers
                     			+ etatFinanceBean.mtt_achat
                     			+ etatFinanceBean.mtt_salaire 
                     			+ (etatFinanceBean.mtt_livraison-etatFinanceBean.mtt_livraison_part)  }"/>
                  </span>   			
             </td>
		</tr>
		
		<tr style="background-color: #FFF59D;">
			<td style="text-align:right">RESULTAT</td>
			<td style="text-align: right;font-weight: bold;">
				<span class="badge badge-magenta badge-square">
                     <fmt:formatDecimal value="${
                     			etatFinanceBean.mtt_vente_caisse 
                     			+ etatFinanceBean.mtt_livraison_part 
                     			+ etatFinanceBean.mtt_vente_hors_caisse 
                     			+ etatFinanceBean.mtt_recette_divers 
                     			- etatFinanceBean.mtt_depense_divers
                     			- etatFinanceBean.mtt_achat
                     			- etatFinanceBean.mtt_salaire 
                     			- (etatFinanceBean.mtt_livraison-etatFinanceBean.mtt_livraison_part) 
                     			}" />
                </span>
			</td>
		</tr>
		
		
		<!-- ****************************************** -->
		<tr>
			<td colspan="2" style="background-color: #80deea;">ETATS CHEQUE</td>
		</tr>
		<tr>	
			<td>Ch&egrave;ques d&eacute;penses encaiss&eacute;s</td>
			<td style="text-align: right;"><fmt:formatDecimal value="${etatFinanceBean.mtt_depense_cheque_encais+etatFinanceBean.mtt_achat_cheque_encais  }"/></td>
		<tr>
		<tr>	
			<td>Ch&egrave;ques recettes encaiss&eacute;s</td>				
			<td style="text-align: right;"><fmt:formatDecimal value="${etatFinanceBean.mtt_recette_cheque_encais + metatFinanceBean.mtt_vente_cheque_encais}"/></td>
		</tr>
		<tr>	
			<td>Ch&egrave;ques d&eacute;penses non encaiss&eacute;s</td>
			<td style="text-align: right;"><fmt:formatDecimal  value="${etatFinanceBean.mtt_depense_cheque_non_encais+etatFinanceBean.mtt_achat_cheque_non_encais  }"/></td>
		<tr>
		<tr>	
			<td>Ch&egrave;ques recettes non encaiss&eacute;s</td>				
			<td style="text-align: right;"><fmt:formatDecimal value="${etatFinanceBean.mtt_recette_cheque_non_encais + metatFinanceBean.tt_vente_cheque_non_encais}"/></td>
		</tr>
		
		<tr style="background-color: #FFF59D;">
        	<td style="text-align: right;font-weight: bold;">SOLDE</td>
        	<td style="text-align: right;font-weight: bold;"><span class="badge badge-magenta badge-square">
        		<%=BigDecimalUtil.formatNumberZero(soldeBanqueActuel) %><%=devise%></span></td>
        </tr>
		
		<!-- ****************************************** -->
		<tr>
			<td colspan="2" style="background-color: #80deea;">ETATS STOCKS</td>
		</tr>
		<tr>
			<td colspan="2">
				<table class="table table-hover" style="width: 100%;">
                   <thead class="bordered-darkorange">
                       <tr>
                           <th>
                              Stock
                           </th>
                           <th style="text-align: right;">
                               Pr&eacute;c&eacute;dent
                           </th>
                           <th style="text-align: right;">
                               Actuel
                           </th>
                           <th style="text-align: right;font-weight: bold;">
                               Solde
                           </th>
                       </tr>
                        </thead>
                     <c:forEach var="empl" items="${etatFinanceBean.list_detail}"> 
						<c:if test="${empl.type == 'EMPL' }">
							<tr>
								<td>${empl.libelle}</td>
								<td align="right"><fmt:formatDecimal value="${empl.getMtt_etat_prev() }" /></td>
								<td align="right"><fmt:formatDecimal value="${empl.getMtt_etat_actuel() }" /></td>
								<td align="right"><fmt:formatDecimal value="${soldeStock }" /></td>
							</tr>	
						</c:if>
					</c:forEach>
                       <tr style="background-color: #FFF59D;">
                       	<td  colspan="3" style="text-align: right;font-weight: bold;">SOLDE</td>
                       	<td style="text-align: right;font-weight: bold;">
                       	<span class="badge badge-magenta badge-square"><%=BigDecimalUtil.formatNumberZero(valeurStockActuel) %><%=devise%></span></td>
                       </tr>
                     
                   </table>    
			</td>
		</tr>
	</tbody>
</table>
