<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>


<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des personnels</li>
		<li>Liste offres prix</li>
		<li class="active">Recherche</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:linkPopup actionGroup="C" classStyle="btn btn-default" action="pers.composantClientPrix.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
		<std:link classStyle="btn btn-default" action="pers.client.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>

	<!-- row -->
	<div class="widget">
		<div class="row">
			<div class="col-lg-12 col-sm-12 col-xs-12">
				<div class="tabbable">
					<ul class="nav nav-tabs" id="myTab">
						<% request.setAttribute("curMnu", "artPrix"); %>
						<jsp:include page="/domaine/personnel/client_header_tab.jsp" />
					</ul>
				</div>
			</div>
		</div>
		<div class="widget-body">
			<div class="row">

				<std:form name="search-form">
					<cplx:table name="list_prix_composant_find" checkable="false" transitionType="simple" width="100%" title="Liste des offres prix" initAction="pers.composantClientPrix.work_find">
						<cplx:header>
							<cplx:th type="empty" />
							<cplx:th type="string" value="Article" field="composantClientPrix.opc_article.libelle" />
							<cplx:th type="decimal" value="Prix de vente" field="composantClientPrix.mtt_prix" width="150" />
							<cplx:th type="empty" />
						</cplx:header>
						<cplx:body>
							<c:forEach items="${list_composantClientPrix }" var="composantClientPrix">
								<cplx:tr workId="${composantClientPrix.id }">
									<cplx:td>
										<work:edit-link-popup />
									</cplx:td>
									<cplx:td value="${composantClientPrix.opc_article.libelle}"></cplx:td>
									<cplx:td align="right" value="${composantClientPrix.mtt_prix}"></cplx:td>
									<cplx:td align="center">
										<work:delete-link />
									</cplx:td>
								</cplx:tr>
							</c:forEach>
						</cplx:body>
					</cplx:table>
				</std:form>

			</div>
			<!-- end widget content -->
		</div>
	</div>
</div>
<!-- end widget div -->
