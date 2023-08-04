<%@page import="framework.model.common.util.EncryptionEtsUtil"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%> 
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<c:set var="encryptUtil" value="<%=new EncryptionEtsUtil(EncryptionEtsUtil.getDecrypKey())%>" />

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Fiche utilisateur</span>
			<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="admin.user.work_init_update" workId="${user.id}" icon="fa fa-pencil"
				tooltip="Modifier" />
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" valueKey="user.login" />
					<div class="col-md-4">
						<std:text name="user.login" type="string" maxlength="50" required="true" />
					</div>
					<std:label classStyle="control-label col-md-2" valueKey="user.password" />
					<div class="col-md-4">
						<std:password name="user.password" type="string" maxlength="30" required="true" value="${encryptUtil.decrypt(user.password) }" />
						<%
							if(BooleanUtil.isTrue(ContextAppli.getUserBean().getIs_admin())){
						%>
							<img class="tooltip-blue" data-toggle="tooltip" data-placement="buttom" data-original-title="${encryptUtil.decrypt(user.password)}" src="resources/framework/img/exclamation.png" style="vertical-align: bottom;"/>
						<%}
						%>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" valueKey="user.badge" />
					<div class="col-md-4">
						<std:text name="user.badge" type="string" maxlength="30" />
					</div>
				</div>
			</div>		
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Profile" />
					<div class="col-md-6">
						<std:select name="user.opc_profile.id" type="long" key="id" labels="libelle" data="${list_profile }" width="300" required="true" />
					</div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Profile 2" />
					<div class="col-md-6">
						<std:select name="user.opc_profile2.id" type="long" key="id" labels="libelle" data="${list_profile }" width="300" />
					</div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Profile 3" />
					<div class="col-md-6">
						<std:select name="user.opc_profile3.id" type="long" key="id" labels="libelle" data="${list_profile }" width="300" />
					</div>
				</div>
			</div>
			<div class="row">	
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" valueKey="user.employe" />
					<div class="col-md-6">
						<std:select name="user.opc_employe.id" type="long" key="id" labels="nom;' ';prenom" data="${list_employe }" width="300" />
					</div>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<std:button actionGroup="M" classStyle="btn btn-success" action="admin.user.work_merge" workId="${user.id }" icon="fa-save" value="Sauvegarder" />
			<std:button actionGroup="D" classStyle="btn btn-danger" action="admin.user.work_delete" workId="${user.id }" icon="fa-trash-o" value="Supprimer" />
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
				<i class="fa fa-times"></i> Fermer
			</button>
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