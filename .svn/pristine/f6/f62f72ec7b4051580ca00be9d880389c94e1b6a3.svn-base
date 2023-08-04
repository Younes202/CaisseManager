<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
	#resume_finance tr{
		font-size: 12px;
	}
	#resume_finance td{
		padding: 4px;
	}
</style>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Fiche chiffres ${exercice.libelle }</span>
			<div class="widget-buttons buttons-bordered" style="margin-bottom: 10px;">
				<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
					<i class="fa fa-times"></i>
				</button> 
			</div>
		</div>
		<div class="widget-body">
			<div class="row" style="padding: 10px;">
				<table class="table table-hover" id="resume_finance">
					<tbody>
						<tr>
							<td colspan="2" style="background-color: #80deea;">ENTREES</td>
						</tr>
						<tr>	
							<td>Ventes caisse</td>
							<td style="text-align: right;color: green;"><fmt:formatDecimal value="${exercice.mtt_vente_caisse }"/></td>
						<tr>
						<tr>	
							<td>Ventes hors caisse</td>
							<td style="text-align: right;color: green;"><fmt:formatDecimal value="${exercice.mtt_vente_hors_caisse }"/></td>
						<tr>
						<tr>	
							<td>Recette divers</td>
							<td style="text-align: right;color: green;"><fmt:formatDecimal value="${exercice.mtt_recette_divers }"/></td>
						<tr>
						<tr>
							<td style="text-align:right">TOTAL</td>
							<td style="text-align: right;font-weight: bold;">
								<span class="badge badge-success badge-square">
								<fmt:formatDecimal value="${exercice.mtt_vente_caisse 
				                     			+ exercice.mtt_vente_hors_caisse 
				                     			+ exercice.mtt_recette_divers }"/>
				            	</span>
				            </td>
						</tr>
						
						<!-- ****************************************** -->
						<tr>
							<td colspan="2" style="background-color: #80deea;">SORTIES</td>
						</tr>
						
						<tr>
							<td>D&eacute;penses divers</td>
							<td style="text-align: right;color: red;"><fmt:formatDecimal value="${exercice.mtt_depense_divers }"/></td>
						</tr>
						<tr>
							<td>Achats</td>
							<td style="text-align: right;color: red;"><fmt:formatDecimal value="${exercice.mtt_achat }"/></td>
						</tr>
						<tr>	
							<td>Salaires</td>
							<td style="text-align: right;color: red;"><fmt:formatDecimal value="${exercice.mtt_salaire }"/></td>
						<tr>
						<tr>
							<td style="text-align:right">TOTAL</td>
							<td style="text-align: right;font-weight: bold;">
								<span class="badge badge-danger badge-square">
										<fmt:formatDecimal value="${exercice.mtt_depense_divers
				                     			+ exercice.mtt_achat
				                     			+ exercice.mtt_salaire 
				                     		  }"/>
				                  </span>   			
				             </td>
						</tr>
						
						<tr style="background-color: #FFF59D;">
							<td style="text-align:right">RESULTAT</td>
							<td style="text-align: right;font-weight: bold;">
								<span class="badge badge-magenta badge-square">
				                     <fmt:formatDecimal value="${
				                     			exercice.mtt_vente_caisse 
				                     			+ exercice.mtt_vente_hors_caisse 
				                     			+ exercice.mtt_recette_divers 
				                     			- exercice.mtt_depense_divers
				                     			- exercice.mtt_achat
				                     			- exercice.mtt_salaire 
				                     			}" />
				                </span>
							</td>
						</tr>
					</tbody>
				</table>
			</div>	
		</div>
	</div>
</std:form>