<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.beanContext.AbonnementBean"%>
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
						request.setAttribute("tabName", "divers");
					request.setAttribute("pAction", EncryptionUtil.encrypt("caisse.wizardInstall.initTicket"));
					request.setAttribute("sAction", EncryptionUtil.encrypt("caisse.wizardInstall.initMaintenance"));
					%>
					<jsp:include page="wizard.jsp" />

					<div class="step-content" id="simplewizardinwidget-steps">
						<%
							AbonnementBean abnBean = ContextAppli.getAbonementBean();
						%>
						<c:set var="isOptCompta" value="<%=abnBean.isOptCompta()%>" />
						<c:set var="isOptOptimisation" value="<%=abnBean.isOptPlusOptimisation()%>" />

						<div class="widget-body">
							<div class="row">
								<c:if test="${isOptCompta}">
									<div class="form-title">COMPTES BANCAIRES</div>
									<c:forEach items="${listParams }" var="parametre">
										<c:if test="${parametre.groupe == 'DIVERS_COMPTE'}">
											<div class="form-group">
												<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
												<div class="col-md-7">
													<std:select name="param_${parametre.code}" type="long" style="width:50%;float:left;" key="id" labels="libelle" data="${listeBanque }" value="${parametre.valeur}" />
												</div>
											</div>
										</c:if>
									</c:forEach>
									<br>
								</c:if>

								<div class="form-title">AUTRES</div>
								<c:forEach items="${listParams }" var="parametre">
									<c:if test="${parametre.groupe == 'DIVERS'}">
										<div class="form-group">
											<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
											<div class="col-md-7">
												<c:choose>
													<c:when test="${parametre.type=='STRING'}">
														<std:text name="param_${parametre.code}" type="string" style="width:50%;float:left;" maxlength="120" value="${parametre.valeur}" />
													</c:when>
													<c:when test="${parametre.type=='TEXT'}">
														<std:textarea name="param_${parametre.code}" style="width:50%;float:left;" rows="3" value="${parametre.valeur}" />
													</c:when>
													<c:when test="${parametre.type=='NUMERIC'}">
														<std:text name="param_${parametre.code}" type="long" style="width:120px;float:left;" maxlength="120" value="${parametre.valeur}" />
													</c:when>
													<c:when test="${parametre.type=='BOOLEAN'}">
														<std:checkbox name="param_${parametre.code}" checked='${parametre.valeur }' />
													</c:when>
												</c:choose>
												<c:if test="${parametre.help != null && parametre.help != ''}">
													<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="${parametre.help}" src="resources/framework/img/info.png" style="vertical-align: bottom;" />
												</c:if>
											</div>
										</div>
									</c:if>
								</c:forEach>

								<c:if test="${isOptOptimisation }">
									<div class="form-title" style="color: red;">OPTIMISATION</div>
									<c:forEach items="${listParams }" var="parametre">
										<c:if test="${parametre.groupe == 'DIVERS_OPTIM' }">
											<div class="form-group">
												<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
												<div class="col-md-7">
													<c:choose>
														<c:when test="${parametre.code == 'PRINT_RAZ' }">
															<std:select name="param_${parametre.code}" type="string" data="${list_imprimante}" width="70%" value="${parametre.valeur}" />
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
														<c:when test="${parametre.type=='BOOLEAN'}">
															<std:checkbox name="param_${parametre.code}" checked='${parametre.valeur }' />
														</c:when>
													</c:choose>
													<c:if test="${parametre.help != null && parametre.help != ''}">
														<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="${parametre.help}" src="resources/framework/img/info.png" style="vertical-align: bottom;" />
													</c:if>
												</div>
											</div>
										</c:if>
									</c:forEach>
								</c:if>
							</div>
						</div>
					</div>
				</div>
			</div>
		</std:form>
	</div>
</div>
