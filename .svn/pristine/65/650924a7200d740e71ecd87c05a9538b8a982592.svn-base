<%@page import="framework.model.util.audit.ReplicationGenerationEventListener"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
.form-title {
	margin-left: 12px;
}
</style>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des personnels</li>
		<li>Clients</li>
		<li class="active">Historique</li>
	</ul>
</div>

<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
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
			<%if(ControllerUtil.getMenuAttribute("IS_MNU", request) == null){ %>	
			<div class="row">
				<div class="col-lg-12 col-sm-12 col-xs-12">
					<div class="tabbable">
						<ul class="nav nav-tabs" id="myTab">
							<li><a data-toggle="tab" href="#descripton" wact="<%=EncryptionUtil.encrypt("pers.client.work_edit")%>"> Fiche </a></li>
							<li><a data-toggle="tab" href="#mouvement" wact="<%=EncryptionUtil.encrypt("pers.client.find_mvm_client")%>"> Commandes </a></li>
							<li><a data-toggle="tab" href="#mouvement" wact="<%=EncryptionUtil.encrypt("pers.client.find_reduction")%>"> R&eacute;ductions </a></li>
							<li><a data-toggle="tab" href="#mouvement" wact="<%=EncryptionUtil.encrypt("pers.composantClientPrix.work_find")%>"> Offres articles </a></li>
							<li class="active">
								<a data-toggle="tab" href="#mouvement" wact="<%=EncryptionUtil.encrypt("pers.client.init_situation")%>"> Situation </a>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<%} %>
			<div class="widget-body">
				<div class="row">
					<h1>HISTORIQUE EN COURS</h1>				
				</div>
			</div>
		</std:form>
	</div>
</div>
