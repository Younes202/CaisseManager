<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#journee_body .databox .databox-text {
	font-size: 12px;
}
</style>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des journées</li>
		<li class="active">Fiche journée</li>
	</ul>
</div>

<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;"></div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<div class="page-body" id="journee_body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>

	<!-- widget grid -->
	<div class="widget">
		<std:form name="data-form">
			<div class="row">
				<div class="col-lg-12 col-sm-12 col-xs-12">
					<%
					request.setAttribute("tabName", "maintenance");
					request.setAttribute("pAction", EncryptionUtil.encrypt("caisse.wizardInstall.initDivers"));
					request.setAttribute("sAction", EncryptionUtil.encrypt("stock.emplacement.work_find"));

					%>
					<jsp:include page="wizard.jsp" />

					<div class="step-content" id="simplewizardinwidget-steps">
						<div class="widget">
							<div class="widget-header bg-lightred">
								<i class="widget-icon fa fa-check"></i> <span class="widget-caption">Sauvegarde des donn&eacute;es</span>
							</div>
							<!--Widget Header-->
							<div class="widget-body">
								<a id="dump_lnk" style="width: 250px; text-align: left;" class="btn btn-primary" href="javascript:void(0);"><i class="fa fa-shopping-cart"></i> Sauvegarder la base de donn&eacute;es</a> <img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Exporter un dump de la base de donn&eacute;es." src="resources/framework/img/info.png" style="vertical-align: bottom;" />
							</div>
							<!--Widget Body-->
						</div>
					</div>
					<div class="row" style="text-align: center;">
						<div class="col-md-12">
							<std:button actionGroup="M" classStyle="btn btn-success" action="caisse.wizardInstall.saveMaitenance" icon="fa-save" value="Sauvegarder" />

						</div>
					</div>
				</div>
			</div>
		</std:form>
	</div>
</div>
