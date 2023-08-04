<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>

<%@page errorPage="/commun/error.jsp"%>

<!-- Page Body -->
<div class="page-body">
<std:form name="search-form">
<div class="row">
		<div class="col-lg-12 col-sm-12 col-xs-12">
			<div class="widget-main ">
				<div class="tabbable">
						<% request.setAttribute("tab", "caisse"); %>
						<jsp:include page="/domaine/dashboard_erp/tabs_header.jsp" />
					
					<div class="tab-content">
					
	<div class="row" id="ecart_div">
		<jsp:include page="dashboard_caisse_ecarts.jsp" />
	</div>
	<div class="row">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div class="well with-header" style="height: 575px;">
				<div class="header bordered-darkorange">Chiffre/Ecart/Annulation par employ&eacute;</div>
				<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 510px;">
					<div class="row" style="margin-left: 0px;margin-right: 0px;">
						<div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
							<span style="float: left;margin-top: 5px;">De &nbsp;</span>
							<std:date name="empl_dt_debut" value="${curr_empl_dtDebut }"/>
						</div>
						<div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
							<span style="float: left;margin-top: 5px;">A &nbsp;</span>
							<std:date name="empl_dt_fin" value="${curr_empl_dtFin }"/>

							<a href="javascript:" wact="<%=EncryptionUtil.encrypt("dash.dashCaisse.init_data_employe")%>" targetDiv="empl_mtt_div" style="margin-top: -28px;position: absolute;left: 169px;">
								<img src="resources/framework/img/refresh.png"/>
							</a>
						</div>
					</div>
					<div class="row" id="empl_mtt_div" style="margin-left: 0px;margin-right: 0px;height: 150px;">
						<jsp:include page="dashboard_caisse_employe.jsp" />
					</div>
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
    