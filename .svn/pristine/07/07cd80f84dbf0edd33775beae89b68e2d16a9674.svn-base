<%@page import="appli.controller.domaine.util_erp.ContextAppli.TYPE_CAISSE_ENUM"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="appli.controller.domaine.habilitation.bean.ProfileBean"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@page errorPage="/commun/error.jsp"%>

<%
ProfileBean profileBean = (ProfileBean)ControllerUtil.getMenuAttribute("profileBean", request);
boolean isAccessBackOff = false;
boolean isAccessCaisse = false;

if(profileBean != null){
	if(BooleanUtil.isTrue(profileBean.getIs_backoffice()) || profileBean.getEnvs().indexOf(";"+TYPE_CAISSE_ENUM.BACKOFFICE+";") != -1){
		isAccessBackOff = true;
	}
	if(BooleanUtil.isTrue(profileBean.getIs_caisse()) || profileBean.getEnvs().indexOf(";"+TYPE_CAISSE_ENUM.CAISSE+";") != -1){
		isAccessCaisse = true;
	}
}
%>
<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Modules avanc&eacute;s</li>
		<li>Fiche profile</li>
		<li class="active">Edition</li>
	</ul>
</div>

<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="U" classStyle="btn btn-default" action="hab.habilitation.work_init_update" params="isCais=1" workId="${profile_id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		<std:link classStyle="btn btn-default" action="hab.profile.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
	<!-- widget grid -->
	<div class="widget">
		<std:form name="data-form">

			<div class="row">
				<div class="col-lg-12 col-sm-6 col-xs-12">
					<div class="tabbable">
						<ul class="nav nav-tabs" id="myTab">
							<li><a data-toggle="tab" href="#descripton" wact="<%=EncryptionUtil.encrypt("hab.profile.work_edit")%>"> Fiche </a></li>
							<%if(isAccessBackOff){ %>
							<li><a data-toggle="tab" href="#mouvement" wact="<%=EncryptionUtil.encrypt("hab.habilitation.work_edit")%>"> Droits back-office</a></li>
							<%} %>
							<%if(isAccessCaisse){ %>
							<li class="active"><a data-toggle="tab" href="#mouvement" wact="<%=EncryptionUtil.encrypt("hab.habilitation.work_edit")%>" params="isCais=1"> Droits caisse</a></li>
							<%} %>
						</ul>
					</div>
				</div>
			</div>

			<div class="widget-body">
				<div class="row">
					<div class="form-group">
						<std:label classStyle="control-label col-md-5" value="Affectation des employ&eacute;s" />
						<div class="col-md-7">
							<std:checkbox name="param_CAISEMPL" checked='${param_CAISEMPL}' />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-5" value="Affectation des clients" />
						<div class="col-md-7">
							<std:checkbox name="param_CAISCLI" checked='${param_CAISCLI}' />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-5" value="Cl&ocirc;ture/ouverture journ&eacute;e" />
						<div class="col-md-7">
							<std:checkbox name="param_CLOJRN" checked='${param_CLOJRN}' />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-5" value="Impression raz" />
						<div class="col-md-7">
							<std:checkbox name="param_IMPRAZ" checked='${param_IMPRAZ}' />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-5" value="Voir historique caisse" />
						<div class="col-md-7">
							<std:checkbox name="param_HISTOCAIS" checked='${param_HISTOCAIS}' />
						</div>
					</div>
				</div>
				<div class="form-actions">
					<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
						<std:button actionGroup="M" classStyle="btn btn-success" action="hab.habilitation.work_merge" params="isCais=1" workId="${profile_id }" icon="fa-save" value="Sauvegarder" />
						<std:button actionGroup="D" classStyle="btn btn-danger" action="hab.habilitation.work_delete" workId="${profile_id }" icon="fa-trash-o" value="Supprimer" />
					</div>
				</div>
			</div>
		</std:form>
	</div>
</div>
