<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>


<std:form name="data-form">
	<input type="hidden" name="images_uploaded" id="images_uploaded" value=";">
	<input type="hidden" name="images_names" id="images_names" value=";">
	<input type="hidden" name="MAX_FILE_SIZE" value="2097152" />

	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Fiche valeur</span>
			<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="admin.valTypeEnum.work_init_update" workId="${valTypeEnum.id}" icon="fa fa-pencil" tooltip="Cr&eacute;er" />
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="col-lg-12 col-md-12">
					<std:hidden name="valTypeEnum.code" />
					<br>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="valTypeEnum.libelle" />
						<div class="col-md-8">
							<std:text name="valTypeEnum.libelle" type="string" maxlength="50" required="true" />
						</div>
					</div>
				</div>
			</div>
			<hr>
			<div class="modal-footer">
				<%if (ControllerUtil.getMenuAttribute("IS_SUB_ADD", request) != null) {%>
				<std:button actionGroup="M" closeOnSubmit="true" targetDiv="xxxx" params="w_nos=1" onComplete="$('#refresh-ajx').trigger('click');" classStyle="btn btn-success" action="admin.valTypeEnum.work_merge" workId="${valTypeEnum.id }" icon="fa-save" value="Sauvegarder" />
				<% } else {%>
				<std:button actionGroup="M" classStyle="btn btn-success" action="admin.valTypeEnum.work_merge" workId="${valTypeEnum.id }" icon="fa-save" value="Sauvegarder" />
				<%}%>
				<std:button actionGroup="D" classStyle="btn btn-danger" action="admin.valTypeEnum.work_delete" workId="${valTypeEnum.id }" icon="fa-trash-o" value="Supprimer" />
				<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
					<i class="fa fa-times"></i> Fermer
				</button>
			</div>

		</div>
	</div>
</std:form>
