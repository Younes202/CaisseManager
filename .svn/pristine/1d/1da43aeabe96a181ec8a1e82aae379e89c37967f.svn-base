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


<%
boolean isCompta = ContextGloabalAppli.getAbonementBean().isOptCompta(); 
boolean isPromo = ContextGloabalAppli.getAbonementBean().isOptCommercial();
boolean isRh = ContextGloabalAppli.getAbonementBean().isOptRh();
boolean isStock = ContextGloabalAppli.getAbonementBean().isOptStock();
%>

<style>
	#scroll_list_article_alert{
		max-height: 325px !important;
		font-size: 12px;
	}
	#scroll_list_article_alert{
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
					
						<% request.setAttribute("tab", "journee"); %>
						<jsp:include page="tabs_header.jsp" />
					
					<div class="tab-content">
						<div class="row">
							<%
							boolean isShiftRight = Context.isOperationAvailable("SHIFT");
							if(isShiftRight){
							%>
								<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
									<div class="well with-header" style="height: 622px;" id="chiffre_dash_div">
									
									</div>
								</div>
								
								<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
									<div class="well with-header">
										<div class="header bordered-darkorange">Situation en chiffre</div>
										<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height:511px;max-height: 511px;<%=(isStock && isCompta && isRh)?"":"background-image: linear-gradient(to top, rgba(0,0,0,0), rgb(157, 151, 151));"%>">
											<div class="databox-top">
												<div class="databox-row row-12" style="border-bottom: 1px solid gray;height: 122%;">
													<div class="databox-cell cell-5 text-center">
														<div class="databox-number number-xxlg sonic-silver" style="font-size: 12px;">
															<%if(isStock && isCompta && isRh){ %>
															<span style="float: left;margin-top: 5px;">Début &nbsp;</span>
														  	<div class="input-group date">
														  		<input type="text" class="form-control" targetDiv="situation-div" name="situation_dt_debut" id="situation_dt_debut" style="font-size: 14px;color:green !important;font-weight: bold;border: 0px;" value="${curr_sitDebut }">
														  			<span class="input-group-addon" targetDiv="situation-div"  style="border: 1px solid #f3f3f3;padding-top: 4px;">
														  				<i class="fa fa-calendar" style="font-size: 22px;color: #9C27B0;"></i>
														  			</span>
															</div>
															<%} %>
														</div>
													</div>
												</div>
											</div>
											<div class="databox-bottom" style="margin-top: 70px;" id="situation-div">
											<%if(isStock && isCompta && isRh){ %>
												<jsp:include page="/domaine/dashboard_erp/dashboard_situation_include.jsp"></jsp:include>
												<%} %>
											</div>
										</div>
									</div>
								</div>	
							<%} %>	
						</div>	
						<div class="row">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div class="well with-header" style="height: 575px;">
				<div class="header bordered-darkorange">
					Répartition des ventes
				</div>
				<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 510px;font-size: 12px;">
					<div class="row" style="margin-left: 0px;margin-right: 0px;">
						
						<div class="col-lg-2 col-md-2">
							<span style="float: left;margin-top: 5px;">De &nbsp;</span>
						</div>
						<div class="col-lg-4 col-md-4">	
							<std:date name="rep_dt_debut" value="${curr_dtDebut }"/>
						</div>
						<div class="col-lg-2 col-md-2">
							<span style="float: left;margin-top: 5px;">A &nbsp;</span>
						</div>
						<div class="col-lg-4 col-md-4">
							<std:date name="rep_dt_fin" value="${curr_dtFin }"/>
						</div>
					</div>
					<div class="row" style="border-bottom: 1px solid gray;height: 50px;margin-top: 7px;margin-left: 0px;margin-right: 0px;">
						<div class="col-lg-2 col-md-2">
							<span style="float: left;margin-top: 5px;">Type &nbsp;</span>
						</div>
						<div class="col-lg-9 col-md-9">	 
							<std:select type="string" name="curr_famille" data="${list_famille }" key="id" labels="libelle" width="100%;" value="${curr_famille }" isTree="true" />
						</div>
						<div class="col-lg-1 col-md-1">
							<a href="javascript:" wact="<%=EncryptionUtil.encrypt("dash.dashBoard.init_indicateur_repartition")%>" targetDiv="repartitionCaisse_div" style="margin-top: 4px;position: absolute;left: 0px;">
								<img src="resources/framework/img/refresh.png"/>
							</a>
						</div>	
					</div>
					<div class="row" id="repartitionCaisse_div" style="margin-left: 0px;margin-right: 0px;">
						<jsp:include page="/domaine/dashboard_erp/dashboard_repartition_include.jsp"></jsp:include>
					</div>
				</div>
			</div>
		</div>	
	
		
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div class="well with-header" style="height: 575px;">
				<div class="header bordered-darkorange">
					Chiffres par employé
				</div>
				<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 510px;font-size: 12px;">
					<div class="row" style="margin-left: 0px;margin-right: 0px;">
						
						<div class="col-lg-1 col-md-2">
							<span style="float: left;margin-top: 5px;">De &nbsp;</span>
						</div>
						<div class="col-lg-4 col-md-4">	
							<std:date name="rep_dt_debut2" value="${curr_dtDebut }"/>
						</div>
						<div class="col-lg-1 col-md-2">
							<span style="float: left;margin-top: 5px;">A &nbsp;</span>
						</div>
						<div class="col-lg-4 col-md-4">
							<std:date name="rep_dt_fin2" value="${curr_dtFin }"/>
							
						</div>
						<div class="col-lg-2 col-md-2">
						    <a href="javascript:" wact="<%=EncryptionUtil.encrypt("dash.dashBoard.init_data_vente")%>" targetDiv="repartition_div" style="margin-top: 4px;position: absolute;left: 0px;">
								<img src="resources/framework/img/refresh.png"/>
							</a>
						</div>
					</div>
					<div class="row" id="repartition_div" style="margin-left: 0px;margin-right: 0px;">
						<jsp:include page="/domaine/dashboard_erp/dashboard_repartition_vente_caisse_include.jsp" />
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


<script>
 $(window).ready(function () {
      loadWidgetsAsynch();
  });

        
        function loadWidgetsAsynch(){
        	var divLoading = "<div style='text-align:center;width:100%;height:100%;position:absolute;margin-top:30%;'><span style='color: #777;font-size: 12px;'>Chargement ...</span><img src='resources/<%=StrimUtil.getGlobalConfigPropertie("context.soft") %>/img/loading3.svg'></div>";
        	<%if(isStock && isCompta && isRh){ %>
        		$("#situation-div").html(divLoading);
        	<%} else{%>
        		$("#situation-div").html("<i style='text-align: center;width: 100%;position: absolute;margin-top: 15%;font-size: 41px;color: #fbfbfb;' class='fa fa-lock'></i>");
        	<%} %>
        	$("#chiffre_dash_div").html(divLoading);
        	
        	<%if(isStock && isCompta && isRh){ %>	
        		submitAjaxForm('<%=EncryptionUtil.encrypt("dash.dashBoard.init_situation_chiffre")%>', null, $("#search-form"), null, "situation-div");
        	<%}%>
        	submitAjaxForm('<%=EncryptionUtil.encrypt("dash.dashBoard2.init_chiffres_journee")%>', null, $("#search-form"), null, "chiffre_dash_div");
        }
        
    </script>