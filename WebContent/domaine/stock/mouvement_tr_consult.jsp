<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="appli.controller.domaine.stock.bean.MouvementBean"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
	MouvementBean mvmBean = (MouvementBean)request.getAttribute("mouvementBean");
	String typeMouvement = mvmBean.getType_mvmnt();	
%>
<style>
	.control-label{
	margin-top: -4px;
	}
</style>
	<!-- widget grid -->
	<div class="widget" style="width: 95%;margin: 5px;">
         <div class="widget-body" style="border-radius: 15px;">
				<div class="row">
			<div class="form-group">

				<std:label classStyle="control-label col-md-2" valueKey="mouvement.date_mouvement" />
				<div class="col-md-2">
					<fmt:formatDate value="${mouvementBean.date_mouvement}" />
				</div>
				<%
					if ("t".equals(typeMouvement)) {
				%>
				<std:label classStyle="control-label col-md-2" valueKey="mouvement.transfert.num_bl" />
				<div class="col-md-2">
					${mouvementBean.num_bl}
				</div>
				<%} else if (!"p".equals(typeMouvement) && !"c".equals(typeMouvement)) { %>
				<std:label classStyle="control-label col-md-2" valueKey="mouvement.num_bl" />
				<div class="col-md-2">
					${mouvementBean.num_bl}
				</div>
				<%} %>
				<%
					if ("a".equals(typeMouvement)) {
				%>
				<std:label classStyle="control-label col-md-2" valueKey="mouvement.num_facture" />
				<div class="col-md-2">
					${mouvementBean.num_facture}
				</div>
				<%
					}
				%>
			</div>

			<%
				if ("a".equals(typeMouvement)) {
			%>
			<div class="form-group">
				<std:label classStyle="control-label col-md-2" valueKey="mouvement.opc_fournisseur" />
				<div class="col-md-4">
					${mouvementBean.opc_fournisseur.libelle}
				</div>
				<std:label classStyle="control-label col-md-2" valueKey="mouvement.is_facture_comptable" />
				<div class="col-md-4">
					${mouvementBean.is_facture_comptable ? 'Oui' : 'Non' }
				</div>			
			</div>
			
					<!-- **************************** FINANCEMENT BLOC ********************** -->
					<c:set var="menu_scope.PAIEMENT_DATA" value="${mouvementBean.getList_paiement() }" scope="session" />
					<div class="form-group" id="finance_bloc">
						<jsp:include page="/domaine/compta/paiement_consult.jsp"></jsp:include>
					</div>
					<!-- **************************** FIN FINANCEMENT BLOC ********************** -->
                          			
			<%
				}
			%>
			<hr>
			<div class="form-group">
				<%
					if (!typeMouvement.equals("a")) {
				%>
				<std:label classStyle="control-label col-md-2" valueKey="mouvement.opc_emplacement" />
				<div class="col-md-4">
					${mouvementBean.opc_emplacement.titre }
				</div>
				<%
					}
				%>
				<% if("t".equals(typeMouvement) || "a".equals(typeMouvement)){ %>
				<std:label classStyle="control-label col-md-2" valueKey="mouvement.opc_destination" />
				<div class="col-md-4">
					${mouvementBean.opc_destination.titre}
				</div>
				<%} %>
				<%  if (typeMouvement.equals("p")) { %>
				<std:label classStyle="control-label col-md-2" valueKey="mouvement.opc_type_perte_enum" />
				<div class="col-md-4">
					${mouvementBean.opc_type_perte_enum.libelle}
				</div>
				<%} %>
				<%  if (typeMouvement.equals("c")) { %>
				<std:label classStyle="control-label col-md-2" valueKey="mouvement.opc_famille_consommation" />
				<div class="col-md-4">
					${mouvementBean.opc_famille_consommation.libelle}
				</div>
				<%} %>
			</div>

			<div class="form-group">

				<std:label classStyle="control-label col-md-2" valueKey="mouvement.commentaire" />
				<div class="col-md-4">
					${mouvementBean.commentaire}
				</div>
			</div>
		</div>	
		<b style="color: blue;">D&eacute;tail des composants</b>
		<hr>
				<div class="row">
				<table style="width: 97%;margin-left: 20px;">
					<tr>
						<th>Composant</th>
						<th width="100px;" style="text-align: center;">Quantit&eacute;</th>
					
					<%if(typeMouvement.equals("t")){ %>
						<th width="140px" style="text-align: center;">P.U achat HT</th>
						<th width="140px" style="text-align: center;">Total H.T</th>
						<th width="90px" style="text-align: center;">TVA</th>
						<th width="140px" style="text-align: center;">Total T.T.C</th>
					<%} else if(typeMouvement.equals("a")){ %>
						<th width="140px" style="text-align: center;">P.U achat HT</th>
						<th width="140px" style="text-align: center;">Total H.T</th>
						<th width="90px" style="text-align: center;">TVA</th>
						<th width="140px" style="text-align: center;">Total T.T.C</th>
						<th width="140px" style="text-align: center;">Remise</th>
						<th width="140px">Date de p&eacute;remption</th>
					<%} else if(typeMouvement.equals("v")){ %>
						<th width="140px" style="text-align: center;">P.U vente HT</th>
						<th width="90px" style="text-align: center;">TVA</th>
						<th width="140px" style="text-align: center;">P.U vente TTC</th>
						<th width="140px" style="text-align: center;">Total H.T</th>
						<th width="140px">Total T.T.C</th>
					<% }%>
					</tr>
					
					<c:set var="totalHt" />
					<c:set var="totalTTC" />
					
					<c:set var="bigDecimalUtil" value="<%=new BigDecimalUtil() %>" />
					
					<c:forEach items="${mouvementBean.list_article }" var="articleMvm">
						<c:set var="totalHt" value="${bigDecimalUtil.add(totalHt, articleMvm.prix_ht_total) }" />
						<c:set var="totalTTC" value="${bigDecimalUtil.add(totalTTC, articleMvm.prix_ttc_total) }" />
						
						<tr style="height: 10px;">
							<td style="padding-top: 5px; padding-right: 10px;" valign="top">
								${articleMvm.opc_article.code}-${articleMvm.opc_article.getLibelleDataVal()}
							</td>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top" align="right">
								<fmt:formatDecimal value="${articleMvm.quantite}" />
							</td>
							
					<%if(typeMouvement.equals("t")){ %>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top" align="right">
							<fmt:formatDecimal value="${articleMvm.prix_ht }"/>
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top" align="right">
							<fmt:formatDecimal value="${articleMvm.prix_ht_total }"/>
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top;" align="right">
							${articleMvm.opc_tva_enum.libelle}
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top" align="right">
							<fmt:formatDecimal value="${articleMvm.prix_ttc_total}"/>
						</td>
					<%} else if(typeMouvement.equals("a")){ %>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top" align="right">
								<fmt:formatDecimal value="${articleMvm.prix_ht }"/>
							</td>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top" align="right">
								<fmt:formatDecimal value="${articleMvm.prix_ht_total }"/>
							</td>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top;" align="right">
								${articleMvm.opc_tva_enum.libelle}
							</td>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top" align="right">
								<fmt:formatDecimal value="${articleMvm.prix_ttc_total}"/>
							</td>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top" align="right">
								<fmt:formatDecimal value="${articleMvm.remise }"/> ${articleMvm.is_remise_ratio?'%':'' } 
							</td>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top">
								<fmt:formatDate value="${articleMvm.date_peremption }"/>
							</td>
						<%} else if(typeMouvement.equals("v")){ %>	
							<td style="padding-top: 5px; padding-right: 10px;" valign="top" align="right">
								<fmt:formatDecimal value="${articleMvm.prix_ht }"/>
							</td>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top;" align="right">
								${articleMvm.opc_tva_enum.libelle}
							</td>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top" align="right">
								<fmt:formatDecimal value="${articleMvm.prix_vente }"/>
							</td>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top" align="right">
								<fmt:formatDecimal value="${articleMvm.prix_ht_total }"/>
							</td>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top" align="right">
								<fmt:formatDecimal value="${articleMvm.prix_ttc_total}"/>
							</td>
						<%} %>
						</tr>
					</c:forEach>
						<tr>
						<%if(typeMouvement.equals("t")){ %>
							<td align="right" style="font-weight: bold;">Total</td>
							<td></td>
							<td></td>
							<td align="right" style="font-weight: bold;padding-top: 5px; padding-right: 10px;"><fmt:formatDecimal value="${totalHt }"/></td>
							<td></td>
							<td align="right" style="font-weight: bold;padding-top: 5px; padding-right: 10px;"><fmt:formatDecimal value="${totalTTC }"/></td>
						<%} else if(typeMouvement.equals("a")){ %>	
							<td align="right" style="font-weight: bold;">Total</td>
							<td></td>
							<td></td>
							<td align="right" style="font-weight: bold;padding-top: 5px; padding-right: 10px;"><fmt:formatDecimal value="${totalHt }"/></td>
							<td></td>
							<td align="right" style="font-weight: bold;padding-top: 5px; padding-right: 10px;"><fmt:formatDecimal value="${totalTTC }"/></td>
							<td></td>
							<td></td>
						<%} else if(typeMouvement.equals("v")){ %>	
							<td align="right" style="font-weight: bold;">Total</td>
							<td></td>
							<td></td>
							<td></td>
							<td align="right" style="font-weight: bold;padding-top: 5px; padding-right: 10px;"><fmt:formatDecimal value="${totalHt }"/></td>
							<td></td>
							<td align="right" style="font-weight: bold;padding-top: 5px; padding-right: 10px;"><fmt:formatDecimal value="${totalTTC }"/></td>
						<%} %>
						</tr>
				</table>
			</div>
		</div>
	</div>
<br>