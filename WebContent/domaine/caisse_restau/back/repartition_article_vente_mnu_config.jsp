<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="java.math.BigInteger"%>
<%@page import="java.util.Arrays"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.math.BigDecimal"%>
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
		<li>Suivi</li>
		<li class="active">Configuration des ventes</li>
	</ul> 
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
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

	<div class="widget">
	<%if(ContextAppli.IS_RESTAU_ENV()){ %>
		<div class="tabbable">
			<ul class="nav nav-tabs" id="myTab9">
				<li><a data-toggle="tab" href="#repartitionVente" wact="<%=EncryptionUtil.encrypt("caisse.journee.find_repartition")%>"> R&eacute;partition des ventes </a></li>
				<li><a data-toggle="tab" href="#repartitionVente" params="is_poste=1" wact="<%=EncryptionUtil.encrypt("caisse.journee.find_repartition")%>"> R&eacute;partition par poste </a></li>
				<li class="active"><a data-toggle="tab" href="#conf" wact="<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.manage_reparition_conf")%>"> Configuration </a></li>
			</ul>
		</div>	
	<%} %>
		<std:form name="search-form">
	         <div class="widget-body">
					<div class="row">
						<div class="col-lg-6 col-sm-6 col-xs-12">
							<div class="widget">
								<div class="widget-header bg-palegreen">
								  <i class="widget-icon fa fa-arrow-down"></i>
								  <span class="widget-caption">Articles &eacute;quivalents aux menus</span>
								 </div>
								<div class="widget-body">
									<table class="table table-hover table-striped table-bordered table-condensed">
										<tr>
											<th>Menu</th>
											<th>Articles &eacute;quivalents</th>
										</tr>
										<c:set var="idx" value="${0 }" />
										<c:forEach items="${listMenus }" var="menu">
											<c:if test="${menu.is_menu }">
												<tr>
													<td>
														
														${menu.libelle }
														<std:hidden name="menu_${idx }" basic="true" value="${menu.id }"/>
													</td>
													<td>
														<std:select name="article_${idx }" type="long[]" width="90%" data="${listArticles }" multiple="true" key="id" labels="libelle" value="${dataMenu.get(menu.id) }" />
													</td>
												</tr>
												<c:set var="idx" value="${idx+1 }" />
											</c:if>
										</c:forEach>
									</table>
								</div>
							</div>	
		        		</div>
			         	<div class="col-lg-6 col-sm-6 col-xs-12">
							<div class="widget">
								<div class="widget-header bg-blue">
								  <i class="widget-icon fa fa-arrow-down"></i>
								  <span class="widget-caption">Familles &agrave; exclure </span>
								 </div>
								<div class="widget-body">
									<std:select name="familles_array" type="long[]" multiple="true" width="100%" data="${listFamilles }" key="id" labels="libelle" isTree="true" value="${dataFamille }" />
			         			</div>
			        		</div>
			         	</div>
		        	 </div>
		        	 
		        	 <div class="form-actions">
						<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
								<std:button classStyle="btn btn-success" action="caisse-web.caisseWeb.manage_reparition_conf" params="is_save=true" icon="fa-save" value="Sauvegarder" />
						</div>
					</div>
				</div>		
			</std:form>
	</div>
</div>