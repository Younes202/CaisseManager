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
			<span class="widget-caption">Fiche r&eacute;p&eacute;rtoire</span>
			<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="admin.ged.work_init_update" workId="${ged.id}" params="ged_cur=${ged.id}" icon="fa fa-pencil"
				tooltip="Modifier" />
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Parent" />
					<div class="col-md-10">
						<std:select name="ged_parent" type="long" key="id" disable="${empty ged.id ? true:false}" labels="libelle" data="${listGedParent}" required="true" isTree="true" width="100%" value="${ged_parent }"/>
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Libell&eacute;" />
					<div class="col-md-6">
						<std:text name="ged.libelle" type="string" placeholder="Libell&eacute;" required="true" />
					</div>
				</div>
			</div>
		<hr>
		<div class="modal-footer">
			<std:button actionGroup="M" classStyle="btn btn-success" closeOnSubmit="true" action="admin.ged.work_merge" workId="${ged.id }" icon="fa-save" value="Sauvegarder" />
			<std:button actionGroup="D" classStyle="btn btn-danger" closeOnSubmit="true" action="admin.ged.work_delete" workId="${ged.id }" icon="fa-trash-o" value="Supprimer" />
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>

	</div>
</div>
</std:form>
