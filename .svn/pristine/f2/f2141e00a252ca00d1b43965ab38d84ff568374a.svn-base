<%@page import="org.hibernate.query.criteria.internal.predicate.IsEmptyPredicate"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="appli.model.domaine.habilitation.persistant.ProfilePersistant"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.TYPE_CAISSE_ENUM"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.APPLI_ENV"%>
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

boolean isCaissier = (profileBean != null ? "CAISSIER".equals(profileBean.getCode()) : false);
boolean isLivreur = (profileBean != null ? "LIVREUR".equals(profileBean.getCode()) : false);
boolean isServeur = (profileBean != null ? "SERVEUR".equals(profileBean.getCode()) : false);
boolean isClient = (profileBean != null ? "CLIENT".equals(profileBean.getCode()) : false);

if(profileBean != null){
	if(BooleanUtil.isTrue(profileBean.getIs_backoffice()) || profileBean.getEnvs().indexOf(";"+TYPE_CAISSE_ENUM.BACKOFFICE+";") != -1){
		isAccessBackOff = true;
	}
	if(BooleanUtil.isTrue(profileBean.getIs_caisse()) || profileBean.getEnvs().indexOf(";"+TYPE_CAISSE_ENUM.CAISSE+";") != -1){
		isAccessCaisse = true;
	}
}
%>

<script type="text/javascript">
$(document).ready(function() {
	$(".inputROnly").each(function(){
	    if($(this).text()=='Oui'){
	        $(this).replaceWith('<i style="font-size:16px;font-weight:bold;color:green;" class="fa fa-check-square-o"></i>');
	    } else{
	        $(this).replaceWith('<i style="font-size:16px;font-weight:bold;color:red;" class="fa fa-times"></i>');
	    }
	});
});
	
function checkAllProfile(){
	$("input[id^='profile_']").prop('checked', true);	
}
</script>

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
		<std:link actionGroup="U" classStyle="btn btn-default" action="hab.profile.work_init_update" workId="${profile.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
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
							<li class="active"><a data-toggle="tab" href="#descripton" wact="<%=EncryptionUtil.encrypt("hab.profile.work_edit")%>"> Fiche </a></li>
							<% if (ControllerUtil.getMenuAttribute("profileId", request) != null && request.getAttribute("IS_ADMIN") == null) {%>
							<%if(isAccessBackOff && !isCaissier && !isClient && !isServeur && !isLivreur){ %>
								<li><a data-toggle="tab" href="#mouvement" wact="<%=EncryptionUtil.encrypt("hab.habilitation.work_edit")%>"> Droits back-office</a></li>
							<%} %>
							<%if(isAccessCaisse && !isClient){ %> 
								<li><a data-toggle="tab" href="#mouvement" wact="<%=EncryptionUtil.encrypt("hab.habilitation.work_edit")%>" params="isCais=1"> Droits caisse</a></li>
							<%}
							}
						%>
						</ul>
					</div>
				</div>
			</div>

			<div class="widget-body">
				<div class="row">
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="user.profile" />
						<div class="col-md-4">
							<std:text name="profile.libelle" validator="val=alphanum;fmt=upper" required="true" size="30" maxlength="50" type="string" />
						</div>
					</div>
					
					<c:if test="${profile.code != 'ADMIN' and profile.code != 'CLIENT'}">
						<h2 style="margin-left: 10px;">Environnements d'accès <a href="javascript:" onclick="checkAllProfile();">(tous)</a></h2>
							
							<div style="width: 49%;padding-left: 29px;float: left;">
							<table style="width: 100%;">
							<%
							Map<String, String> boEnvs = (Map)request.getAttribute("envs");
							Map<String, String> mobileEnvs = (Map)request.getAttribute("envsMobile");
							
							if(!isCaissier && !isServeur && !isLivreur){
								for(String env : boEnvs.keySet()){ %> 
									<tr>	
										<td style="width: 200px;">
											<std:label classStyle="" style="line-height: 25px;font-weight: bold;font-size: 16px;" value="<%=boEnvs.get(env) %>" />
										</td>
										<td>	
											<std:checkbox checked='<%=(profileBean!=null && profileBean.getEnvs()!=null && profileBean.getEnvs().indexOf(";"+env+";") != -1)?"true":"false" %>' name='<%="profile_"+env %>' />
										</td>
									</tr>		
								<%}
							} else{
								mobileEnvs.remove("synthese");
								
								if(!isCaissier){
									mobileEnvs.remove("utilitaire");
								}
							}%>
							</table>
						</div>
						<div style="width: 49%;padding-left: 29px;float: left;">	
							<table style="width: 100%;">
							<%for(String env : mobileEnvs.keySet()){ %>
								<tr>
									<td style="width: 250px;">
										<std:label classStyle="" style="line-height: 25px;font-weight: bold;font-size: 16px;" value="<%=mobileEnvs.get(env) %>" />
									</td>
									<td>	
										<std:checkbox checked='<%=(profileBean!=null && profileBean.getEnvs()!=null && profileBean.getEnvs().indexOf(";"+env+";") != -1)?"true":"false" %>' name='<%="profile_"+env %>' />
									</td>
								</tr>	
							<%} %>
							</table>
						</div>
					</c:if>	
				</div>
				<hr>
				<div class="form-actions">
					<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
						<std:button actionGroup="M" classStyle="btn btn-success" action="hab.profile.work_merge" workId="${profile.id }" icon="fa-save" value="Sauvegarder" />
						<std:button actionGroup="D" classStyle="btn btn-danger" action="hab.profile.work_delete" workId="${profile.id }" icon="fa-trash-o" value="Supprimer" />
					</div>
				</div>
			</div>
		</std:form>
	</div>
</div>
