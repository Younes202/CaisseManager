<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.fidelite.service.ICarteFideliteClientService"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.constante.ActionConstante"%>
<%@page import="framework.model.common.constante.ProjectConstante"%>
<%@page import="appli.controller.domaine.fidelite.action.CarteFideliteClientAction"%>
<%@page import="appli.controller.domaine.fidelite.bean.CarteFideliteClientBean"%>
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
		<li>Portefeuille client</li>
		<li class="active">Historique des utilisations</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
	<%
	boolean isPortefeuilleMnu = ("PORTEFEUILLE".equals(ControllerUtil.getUserAttribute("MNU_FIDELITE", request)) && ControllerUtil.getMenuAttribute("IS_MENU_CMLIENT", request) == null); 
	String act = (isPortefeuilleMnu ? "fidelite.portefeuille.work_find" : "pers.client.work_find");
	%>
		<std:link classStyle="btn btn-default" action="<%=act %>" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
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
	<std:form name="data-form">
		<!-- widget grid -->
		<div class="widget">
		
	<div class="row">
        <div class="col-lg-12 col-sm-12 col-xs-12">
			<div class="row">
				<div class="col-lg-12 col-sm-12 col-xs-12">
					<% request.setAttribute("curMnu", "porteCmd"); %>
					<jsp:include page="/domaine/personnel/client_header_tab.jsp" />
				</div>
			</div>		
			<div class="widget" >
				<div class="widget-body">
					<div class="row">
						<cplx:table name="list_portefeuilleMvm_utilisation" transitionType="simple" width="100%" title="Historique des utilisations" initAction="fidelite.portefeuille.find_utilisation" autoHeight="true" checkable="false">
							<cplx:header>
								<cplx:th type="date" value="Date" field="portefeuilleMvm.date_mvm" />
								<cplx:th type="decimal" value="Montant" field="portefeuilleMvm.montant" width="120"/>
							</cplx:header>
							<cplx:body>
								<c:forEach items="${list_portefeuilleMvm }" var="portefeuilleMvm">
									<cplx:tr workId="${portefeuilleMvm.id }">
										<cplx:td value="${portefeuilleMvm.date_mvm}" />
										<cplx:td style="font-weight:bold;" align="right" value="${portefeuilleMvm.montant }"></cplx:td>
									</cplx:tr>	
								</c:forEach>
							</cplx:body>
						</cplx:table>
					</div>
				</div>
			</div>
		</div>
		</div>
	</div>
	</std:form>	
</div>
