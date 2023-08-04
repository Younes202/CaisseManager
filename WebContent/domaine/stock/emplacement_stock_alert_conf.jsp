<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Configuration alertes stocks</span>
			<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="stock.emplacement.work_init_update" workId="${emplacement.id}" icon="fa fa-pencil" tooltip="Cr&eacute;er" />
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
					<c:set var="encodeService" value="<%=new EncryptionUtil()%>" />
			
					<table class="table table-hover" style="width: 98%;margin-left: 8px;">
						<thead class="bordered-darkorange">
							<tr>
								<th>Emplacement</th>
								<th style="width: 120px">Seuil</th>
							</tr>
						</thead> 
						<tbody>					
							<c:set var="cpt" value="${1 }" />
							<c:forEach var="emplacement" items="${listEmplacement }">
								<c:set var="oldData" value="${mapData.get(emplacement.id) }" />
								<std:hidden name="opc_emplacement.id_${cpt}" value="${emplacement.id }" />
								<std:hidden name="opc_composant.id_${cpt}" value="${composantId }" />
								<tr>
									<td>${emplacement.titre }</td>
									<td><std:text name="qte_seuil_${cpt}" type="decimal" placeholder="Seuil" required="true" maxlength="15" style="width:150px;" value="${oldData }" /></td>
								</tr>
								
								<c:set var="cpt" value="${cpt+1 }" /> 
							</c:forEach>	
						</tbody>	
					</table>	
			</div>	
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button actionGroup="M" classStyle="btn btn-success" action="stock.composant.save_conf_stock" workId="${composantId }" icon="fa-save" value="Sauvegarder" />
						<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</std:form>