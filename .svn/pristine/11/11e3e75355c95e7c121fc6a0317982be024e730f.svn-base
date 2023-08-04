<%@page import="appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.controller.Context"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#scroll_list_article_alert {
	max-height: 325px !important;
	font-size: 12px;
}

#scroll_list_article_alert {
	max-height: 290px;
}

.morris-hover {
	position: absolute;
	z-index: 1000
}

.morris-hover.morris-default-style {
	border-radius: 10px;
	padding: 6px;
	color: #666;
	background: rgba(255, 255, 255, 0.8);
	border: solid 2px rgba(230, 230, 230, 0.8);
	font-family: sans-serif;
	font-size: 12px;
	text-align: center
}

.morris-hover.morris-default-style .morris-hover-row-label {
	font-weight: bold;
	margin: 0.25em 0
}

.morris-hover.morris-default-style .morris-hover-point {
	white-space: nowrap;
	margin: 0.1em 0
}

.sortable tr {
	height: 10px;
}

.databox .databox-row.row-2 {
	height: 13.66%;
}
</style>

<!-- /Page Breadcrumb -->
<!-- Page Body -->
<div class="page-body">
	<std:form name="search-form">
		<!-- ******************************* the Flash Info chart row ******************************* -->
		<div class="row">
			<div class="col-lg-12 col-sm-12 col-xs-12">
				<div class="widget-main ">
					<div class="tabbable">
						<%
							request.setAttribute("tab", "venteBO");
						%>
						<jsp:include page="tabs_header.jsp" />

						<div class="tab-content">
							<div class="row">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="well with-header" style="height: 575px;">
										<div class="header bordered-darkorange">Répartition des ventes</div>
										<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 510px; font-size: 12px;">
											<div class="row" style="margin-left: 0px; margin-right: 0px;">
												<div class="col-lg-3 col-md-3">
													<span style="float: left; margin-top: 5px;">De &nbsp;</span>
													<std:date name="rep_dt_debut" value="${curr_dtDebut }" />
												</div>
												<div class="col-lg-3 col-md-3">
													<span style="float: left; margin-top: 5px;">A &nbsp;</span>
													<std:date name="rep_dt_fin" value="${curr_dtFin }" />
												</div>
												<div class="col-lg-4 col-md-4">
													<span style="float: left; margin-top: 5px;">Type &nbsp;</span>
													<std:select type="string" name="curr_famille" data="${list_familleStock }" key="id" labels="libelle" width="70%;" value="${curr_famille }" isTree="true" />
												</div>
												<div class="col-lg-2 col-md-2">
													<a href="javascript:" wact="<%=EncryptionUtil.encrypt("dash.dashBoard.init_ventes_bo")%>" targetDiv="repartitionCaisse_div"> 
														<img src="resources/framework/img/refresh.png" />
													</a>
												</div>
											</div>
											<div class="row" id="repartitionCaisse_div" style="margin-left: 0px; margin-right: 0px;margin-top: 4px;">
												<jsp:include page="/domaine/dashboard_erp/dashboard_ventes_bo_include.jsp" />
											</div>
										</div>
									</div>
								</div>
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="well with-header" style="height: 512px;">
										<div class="header bordered-darkorange">Ventes par caissier</div>
										<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 45px; font-size: 12px;">
											<div class="row" style="margin-left: 0px; margin-right: 0px;">
												<div class="col-lg-2 col-md-2">
													<span style="float: left; margin-top: 5px;">De &nbsp;</span>
												</div>
												<div class="col-lg-4 col-md-4">
													<std:date name="rep_dt_debut" value="${curr_dtDebut }" />
												</div>
												<div class="col-lg-2 col-md-2">
													<span style="float: left; margin-top: 5px;">A &nbsp;</span>
												</div>
												<div class="col-lg-4 col-md-4">
													<std:date name="rep_dt_fin" value="${curr_dtFin }" />
													<div class="col-lg-2 col-md-2">
														<a href="javascript:" wact="<%=EncryptionUtil.encrypt("dash.dashBoard.getChiffreVenteBOParCaissier")%>" targetDiv="repartitionCaisseBO_div" style="margin-top: -28px; position: absolute; margin-left: 215%"> <img src="resources/framework/img/refresh.png" />
														</a>
													</div>
												</div>
											</div>
										</div>
										<div class="row" id="repartitionCaisseBO_div" style="margin-left: 0px; margin-right: 0px;margin-top: -24px;">
											<jsp:include page="/domaine/dashboard_erp/dashboard_repartition_venteBO_include.jsp" />
										</div>
									</div>
								</div>

							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</std:form>
</div>
<!-- /Page Body -->

