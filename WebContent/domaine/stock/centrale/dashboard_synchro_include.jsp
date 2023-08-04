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

<cplx:table name="list_synchroElement" transitionType="simple" width="100%" showTitleBanner="false" initAction="dash.dashBoard.init_synchro" checkable="false" exportable="false" autoHeight="true">
	<cplx:header>
		<cplx:th type="string" value="&Eacute;l&eacute;ment" field="synchroElement.libelle"  width="220"/>
		<cplx:th type="string" value="&Eacute;tat de synchronisation" sortable="false" filtrable="false" />
		<cplx:th type="empty" width="40" />
	</cplx:header>
	<cplx:body>
	<c:set var="oldType" value="${null }"></c:set>
			
	<c:forEach items="${list_synchroElement }" var="synchro">
		<c:if test="${oldType == null || oldType != opc_centrale_ets.nom }">
		     <tr> 
				<td colspan="3" noresize="true" style="font-size: 13px;font-weight: bold;color:#e75b8d;">
					${synchro.opc_centrale_ets.nom }
				</td>
			</tr>	
		</c:if>
		<c:set var="oldType" value="${synchro.opc_centrale_ets.nom }"></c:set> 
		
			<cplx:tr workId="${synchro.id }">
				<cplx:td value="${synchro.type_opr }" />
				<cplx:td>
					<std:linkPopup action="admin.synchroElement.synchro_edit" workId="${synchro.id }" params="isFromDash=1" actionGroup="C" icon="fa fa-share" classStyle="btn btn-sm btn-success" tooltip="Synchroniser" />
				</cplx:td>
			</cplx:tr>
		</c:forEach>
	</cplx:body>
</cplx:table>
