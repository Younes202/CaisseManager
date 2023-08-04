<%@page import="framework.controller.ControllerUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.TYPE_CAISSE_ENUM"%>
<%@page import="appli.controller.domaine.caisse.bean.CaisseBean"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion de caisse</li>
		<li class="active">Configuration</li>
	</ul>
</div>

<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="U" classStyle="btn btn-default" workId="${caisseId }" action="caisse_restau.caisseConfigurationRestau.work_init_update" icon="fa-3x fa-pencil" tooltip="Modifier" />
		<std:link classStyle="btn btn-default" action="caisse.caisse.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
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
				<div class="col-lg-12 col-sm-12 col-xs-12">
					<div class="tabbable">
						<ul class="nav nav-tabs" id="myTab">
							<li><a data-toggle="tab" href="#descripton" wact="<%=EncryptionUtil.encrypt("caisse.caisse.work_edit")%>"> Fiche </a></li>
							<%
								for (TYPE_CAISSE_ENUM typeCaisse : TYPE_CAISSE_ENUM.values()) {
								if (typeCaisse.toString().equals(ControllerUtil.getMenuAttribute("typeCaisse", request))) {
							%>
							<li class="active"><a data-toggle="tab" href="#configCaisse" wact="<%=EncryptionUtil.encrypt("caisse_restau.caisseConfigurationRestau.work_edit")%>"> Configuration <%=typeCaisse.getLibelle()%>
							</a></li>
							<%
								break;
							}
							}
							%>
						</ul>
					</div>
				</div>
			</div>

			<div class="widget-body">
				<div class="row">
					<div class="form-title">IMPRIMANTE ÉTIQUETTE</div>
						<c:forEach items="${listParams }" var="parametre">
							<c:if test="${parametre.groupe == 'CAISSE_PRES_ETQ'}">
								<div class="form-group">
									<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
									<div class="col-md-7">
										<c:choose>
											<c:when test="${parametre.code=='ETIQUETTE_PRINT'}">
												<std:select name="param_${parametre.code}" type="string" data="${list_imprimante}" width="70%" value="${parametre.valeur}" />
											</c:when>
											<c:when test="${parametre.code=='ETIQUETTE_ORIENTATION'}">
												<std:select name="param_${parametre.code}" type="string" data="${orientationBalance}" width="70%" value="${parametre.valeur}" />
											</c:when>
											<c:when test="${parametre.type=='STRING'}">
												<std:text name="param_${parametre.code}" type="string" style="width:50%;float:left;" maxlength="120" value="${parametre.valeur}" />
											</c:when>
											<c:when test="${parametre.type=='TEXT'}">
												<std:textarea name="param_${parametre.code}" style="width:50%;float:left;" rows="3" value="${parametre.valeur}" />
											</c:when>
											<c:when test="${parametre.type=='NUMERIC'}">
												<std:text name="param_${parametre.code}" type="long" style="width:120px;float:left;" maxlength="120" value="${parametre.valeur}" />
											</c:when>
											<c:when test="${parametre.type=='DECIMAL'}">
												<std:text name="param_${parametre.code}" type="decimal" style="width:120px;float:left;" maxlength="120" value="${parametre.valeur}" />
											</c:when>
											<c:when test="${parametre.type=='BOOLEAN'}">	
												<std:checkbox name="param_${parametre.code}" checked='${(parametre.valeur!="" && parametre.valeur!=null)?"1":"0"}' />
											</c:when>
										</c:choose>
										<c:if test="${parametre.help != null && parametre.help != ''}">
											<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="${parametre.help}" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
										</c:if>
									</div>
								</div>
							</c:if>
						</c:forEach>
					</div>
					<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
						<std:button classStyle="btn btn-success" action="caisse_restau.caisseConfigurationRestau.work_update" icon="fa-save" value="Sauvegarder" />
					</div>
				</div>
		</std:form>
	</div>
</div>
