<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#generic_modal_body{
	width: 920px;
	margin-left: -10%;
}
</style>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Fiche emplacement</span>
			<c:if test="${emplacement.origine_id == null}" >
				<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="stock.emplacement.work_init_update" workId="${emplacement.id}" icon="fa fa-pencil" tooltip="Cr&eacute;er" />
			</c:if>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" valueKey="emplacement.titre" />
						 <div class="col-md-9">
							<std:text name="emplacement.titre" type="string" placeholderKey="emplacement.titre" required="true" maxlength="80" />
						 </div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" valueKey="emplacement.description" />
					<div class="col-md-9">
						<std:textarea name="emplacement.description" rows="5" cols="50" maxlength="255" />
					</div>
				</div>
			</div>	
			<div class="row">	
				<div class="col-md-6">
					<fieldset><legend>Familles et articles &agrave; inclure</legend>
						<div class="form-group">
							<std:label classStyle="control-label col-md-3" value="Familles" />
							<div class="col-md-9">
								<std:select name="emplacement.familles_array" isTree="true" type="string[]" multiple="true" width="100%" data="${listFamilles }" key="id" labels="libelle" />
							</div>
						</div>
						<div class="form-group">
							<std:label classStyle="control-label col-md-3" value="Articles" />
							<div class="col-md-9">		
								<std:select name="emplacement.articles_array" type="string[]" multiple="true" width="100%" data="${listArticles }" key="id" labels="libelle" />
							</div>	
						</div>
					</fieldset>
				</div>	
				<div class="col-md-6">
					<fieldset><legend>Familles et articles &agrave; exclure</legend>
						<div class="form-group">
							<std:label classStyle="control-label col-md-3" value="Familles" />
							<div class="col-md-9">
								<std:select name="emplacement.familles_ex_array" isTree="true" type="string[]" multiple="true" width="100%" data="${listFamilles }" key="id" labels="libelle" />
							</div>
						</div>
						<div class="form-group">
							<std:label classStyle="control-label col-md-3" value="Articles" />
							<div class="col-md-9">		
								<std:select name="emplacement.articles_ex_array" type="string[]" multiple="true" width="100%" data="${listArticles }" key="id" labels="libelle" />
							</div>	
						</div>
					</fieldset>
				</div>	
					
					
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<%
							if (ControllerUtil.getMenuAttribute("IS_SUB_ADD", request) != null) {
						%>
						<std:button actionGroup="M" classStyle="btn btn-success" closeOnSubmit="true" targetDiv="xxxx" onComplete="$('#li_manager').show(1000);" action="stock.emplacement.work_merge" workId="${emplacement.id }" icon="fa-save" value="Sauvegarder" />
						<%
							} else {
						%>
						<std:button actionGroup="M" classStyle="btn btn-success" action="stock.emplacement.work_merge" workId="${emplacement.id }" icon="fa-save" value="Sauvegarder" />
						<%
							}
						%>
						<std:button actionGroup="D" classStyle="btn btn-danger" action="stock.emplacement.work_delete" workId="${emplacement.id }" icon="fa-trash-o" value="Supprimer" />
						<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</std:form>