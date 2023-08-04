<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%> 
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>

<style>
	.btn-sm {
    padding: 0px 4px !important;
    }
    .btn-sm>.fa {
    margin-right: 0px !important;
    }
</style>

<%if(request.getAttribute("is_new_added") != null){%>
	<embed src="resources/sound/alert.mp3" height="0px" width="0px" autoplay="true" style="position: absolute;left: -100px;top: -100px;">
<%} %>

<cplx:table name="list_demandeTransfert2" transitionType="simple" width="100%" showTitleBanner="false" initAction="dash.dashBoard.init_demandeTrans" checkable="false" exportable="false" autoHeight="true">
	<cplx:header>
		<cplx:th type="date" value="Date souhait&eacute;e" field="demandeTransfert.date_souhaitee"  width="120"/>
		<cplx:th type="date" value="Date r&eacute;ception" field="demandeTransfert.date_reception" />
		<cplx:th type="string" value="Restaurant" field="demandeTransfert.opc_restaurant.nom" />
		<cplx:th type="empty" width="40" />
	</cplx:header>
	<cplx:body>
	<c:set var="oldDate" value="${null }"></c:set>
			
	<c:forEach items="${list_demandeTransfert }" var="demandeTransfert">
		<c:if test="${oldDate == null || oldDate != demandeTransfert.date_souhaitee }">
		     <tr> 
				<td colspan="4" noresize="true" style="font-size: 13px;font-weight: bold;color:#e75b8d;">
					<fmt:formatDate value="${demandeTransfert.date_souhaitee }" /> 
				</td>
			</tr>	
		</c:if>
		<c:set var="oldDate" value="${demandeTransfert.date_souhaitee }"></c:set> 
		
			<cplx:tr workId="${demandeTransfert.id }">
				<cplx:td>
					<fmt:formatDate value="${demandeTransfert.date_souhaitee }" /> 
				</cplx:td>
				<cplx:td>
					<fmt:formatDate value="${demandeTransfert.date_reception }" pattern="dd/MM/yyyy HH:mm:ss" /> 
				</cplx:td>
				<cplx:td value="${demandeTransfert.opc_restaurant.nom }" />
				<cplx:td>
					<std:link action="stock.demandeTransfert.init_transfert" workId="${demandeTransfert.id }" params="restau=${demandeTransfert.opc_restaurant.code_authentification }&isDemandeTransfert=1&isFromDash=1" actionGroup="C" icon="fa fa-share" classStyle="btn btn-sm btn-success" tooltip="Transf&eacute;rer" />
				</cplx:td>
			</cplx:tr>
		</c:forEach>
	</cplx:body>
</cplx:table>
