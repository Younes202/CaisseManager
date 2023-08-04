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
			<span class="widget-caption">Configuration imprimante RAZ</span>
			<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="stock.emplacement.work_init_update" workId="${emplacement.id}" icon="fa fa-pencil" tooltip="Cr&eacute;er" />
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
							
				<div class="form-group">
					<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
					<div class="col-md-7">
						<c:choose>
							<c:when test="${parametre.type=='STRING'}">
								<c:choose>
									<c:when test="${parametre.code == 'PRINT_RAZ' }">
										<std:select name="param_${parametre.code}" type="string" data="${list_imprimante}" width="70%" value="${parametre.valeur}" />
									</c:when>
									<c:otherwise>
										<std:text name="param_${parametre.code}" type="string" style="width:50%;float:left;" maxlength="120" value="${parametre.valeur}" />	
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:when test="${parametre.type=='TEXT'}">
								<std:textarea name="param_${parametre.code}" style="width:50%;float:left;" rows="3" value="${parametre.valeur}" />
							</c:when>
							<c:when test="${parametre.type=='NUMERIC'}">
								<std:text name="param_${parametre.code}" type="long" style="width:120px;float:left;" maxlength="120" value="${parametre.valeur}" />
							</c:when>
							<c:when test="${parametre.type=='DECIMAL'}">
								<std:text name="param_${parametre.code}" type="decimal" style="width:120px;float:left;" maxlength="120" value="${parametre.valeur}" />
							</c:when>
							<c:when test="${parametre.type=='BOOLEAN'}">	
								<std:checkbox name="param_${parametre.code}" checked='${parametre.valeur }' />
							</c:when>
						</c:choose>
						<c:if test="${parametre.help != null && parametre.help != ''}">
							<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="${parametre.help}" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
						</c:if>
					</div>
				</div>	
							
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button actionGroup="M" classStyle="btn btn-success" targetDiv="XX" onClick="$('#close_modal').trigger('click');" action="caisse.razPrint.init_print_conf" params="isupd=1" icon="fa-save" value="Sauvegarder" />
						<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</std:form>

<script type="text/javascript">
/*Handles ToolTips*/
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script>  