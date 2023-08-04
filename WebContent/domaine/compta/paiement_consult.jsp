<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
Boolean isEdit = (Boolean) request.getAttribute("editFromPaiement");
%>

<c:set var="list_financement" value='<%=ControllerUtil.getMenuAttribute("PAIEMENT_DATA", request) %>' />

<c:choose>
	<c:when test="${list_financement.size() > 0 }">
		<table style="width: 97%;margin-left: 20px;border: 1px solid #9E9E9E;">
			<tr style="border-bottom: 1px solid blue;background-color: #9e9e9e1a;">
				<th>
					<%if(ControllerUtil.isEditionWritePage(request) || BooleanUtil.isTrue(isEdit) || "mergePaiementPopup".equals(ControllerUtil.getAction(request))){ %>
					<std:linkPopup action="admin.compteBancaire.loadPaiementPopup" tooltip="Gestion des paiements" noJsValidate="true" icon="fa-pencil-square-o" style="width: 25px;padding: 3px;background-color: #03A9F4;margin-left: 2px;" />
					<%} %> 
					Mode
				</th>
				<th>Compte</th>
				<th width="110px">Num&eacute;ro</th>
				<th width="90px">Montant</th>
				<th width="110px">Ech&eacute;ance</th>
			</tr>
			<c:forEach items="${list_financement }" var="financement">
					<tr style="border-bottom: 1px dashed #FF9800;">
						<td style="padding-top: 5px; padding-right: 10px;padding-left: 5px;" valign="top">
							${financement.opc_financement_enum.libelle }
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							${financement.opc_compte_bancaire.libelle }
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<c:if test="${financement.opc_financement_enum.code=='CHEQUE' }">
								${financement.num_cheque }
							</c:if>
							<c:if test="${financement.opc_financement_enum.code=='CHEQUE_F' }">
								${financement.opc_fournisseurCheque.num_cheque }
							</c:if>
							<c:if test="${financement.opc_financement_enum.code=='VIREMENT' }">
								${financement.num_virement }
							</c:if>
						</td>
						<td style="padding-top: 5px; padding-right: 10px;text-align: right;" valign="top">
							<fmt:formatDecimal value="${financement.montant }" /> 
						</td>
						<td style="padding-top: 5px; padding-right: 10px;text-align: center;" valign="top">
							<fmt:formatDate value="${financement.date_echeance }" /> 
						</td>
					</tr>
				</c:forEach>
			</table>
		
	</c:when>
	<c:otherwise>
		<%if(ControllerUtil.isEditionWritePage(request) || BooleanUtil.isTrue(isEdit)){ %>
			<std:linkPopup classStyle="" action="admin.compteBancaire.loadPaiementPopup" tooltip="Gestion des paiements" value="Ajouter un mode de paiement" noJsValidate="true" icon="fa-plus-square-o" style="margin-left:5%;" />
		<%} %> 
	</c:otherwise>
</c:choose>

