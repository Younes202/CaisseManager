<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page import="framework.model.common.util.StrimUtil"%>
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
						<%
							request.setAttribute("tab", "offre");
						%>
						<jsp:include page="../tabs_header.jsp" />

						<div class="tab-content">
							<!-- ******************************* the Flash Info chart row ******************************* -->

							<div class="row" id="dash_CMP"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</std:form>
</div>

<script type="text/javascript">
   $(window).ready(function () {
   	  // Charger les donn�es
      loadWidgetsAsynch();
	});
        
     function loadWidgetsAsynch(){
      	var divLoading = "<div style='text-align:center;width:100%;height:100%;position:absolute;margin-top:30%;'><span style='color: #777;font-size: 12px;'>Chargement ...</span><img src='resources/<%=StrimUtil.getGlobalConfigPropertie("context.soft") %>/img/loading3.svg'></div>";
     	$("#dash_CMP").html(divLoading);
     	
     	submitAjaxForm('<%=EncryptionUtil.encrypt("dash.dashOffre.init_data_employe")%>', null, $("#search-form"), null, "dash_CMP");
	}
</script>
<!-- /Page Body -->