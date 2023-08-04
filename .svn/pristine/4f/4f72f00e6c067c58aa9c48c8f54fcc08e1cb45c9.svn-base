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
			<span class="widget-caption">Fiche message</span>
			<std:link actionGroup="U" classStyle="btn btn-default" targetDiv="generic_modal_body" action="admin.message.work_init_update" workId="${message.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Sujet" />
					<div class="col-md-10">
						<std:text name="message.sujet" type="string" maxlength="10" required="true" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Message" />
					<div class="col-md-4">
						<std:textarea name="message.text" maxlength="255" cols="60" rows="4" />
					</div>
				</div>
				<hr>
				<div class="modal-footer">
					<div class="row" style="text-align: center;" class="col-md-12">
						<std:button actionGroup="M" classStyle="btn btn-success" action="admin.message.work_merge" workId="${message.id }" icon="fa-save" value="Sauvegarder" />
						<std:button actionGroup="D" classStyle="btn btn-danger" action="admin.message.work_delete" workId="${message.id }" icon="fa-trash-o" value="Supprimer" />
						<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</std:form>
