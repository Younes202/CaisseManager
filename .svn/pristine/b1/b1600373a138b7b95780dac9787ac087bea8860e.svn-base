<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
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
						<% request.setAttribute("tab", "rh"); %>
						<jsp:include page="../tabs_header.jsp" />

						<div class="tab-content">
							<!-- ******************************* the Flash Info chart row ******************************* -->
							<!-- <div class="row">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="well with-header" style="height: 5px;">
										<div class="header bordered-darkorange">Retards pointage par employ&eacute;</div>
										<div style="height: 510px;" id="dash_pointage">
										
										</div>
									</div>
								</div>
							</div> -->

							<div class="row" id="dash_travail">
							
							</div>
							<div class="row" id="dash_travail2">
							
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</std:form>
</div>
<!-- /Page Body -->

<script type="text/javascript">
	$(window).ready(function () {
   	  // Charger les donn�es
      loadWidgetsAsynch();
	});
        
     function loadWidgetsAsynch(){
     	var divLoading = "<div style='text-align:center;width:100%;height:100%;position:absolute;margin-top:30%;'><span style='color: #777;font-size: 12px;'>Chargement ...</span><img src='resources/<%=StrimUtil.getGlobalConfigPropertie("context.soft") %>/img/loading3.svg'></div>";
     /* 	$("#dash_pointage").html(divLoading);
     	$("#dash_abscence").html(divLoading); */
     	$("#dash_travail").html(divLoading);
     	$("#dash_travail2").html(divLoading);
     	
<%--      	submitAjaxForm('<%=EncryptionUtil.encrypt("dash.dashRH.init_abscence")%>', null, $("#search-form"), null, "dash_abscence"); --%>
<%--      	submitAjaxForm('<%=EncryptionUtil.encrypt("dash.dashRH.init_pointage")%>', null, $("#search-form"), null, "dash_pointage"); --%>
     	submitAjaxForm('<%=EncryptionUtil.encrypt("dash.dashRH.init_data_employe")%>', null, $("#search-form"), null, "dash_travail");
     	submitAjaxForm('<%=EncryptionUtil.encrypt("dash.dashRH.repartition_frais_type_employe")%>', null, $("#search-form"), null, "dash_travail2");

	}
</script>