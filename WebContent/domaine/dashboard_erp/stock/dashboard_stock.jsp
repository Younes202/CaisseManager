<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>

<%@page errorPage="/commun/error.jsp"%>

<style>
.sortable td{
	font-size: 12px;
	}
	#scroll_list_article_alert{
		max-height: 325px !important;
		font-size: 12px;
	}
	#scroll_list_article_alert{
		max-height: 290px;
	}
	.sortable tr {
    height: 10px;
}
</style>

<!-- Page Body -->
<div class="page-body">
	<std:form name="search-form">
	<div class="row">
		<div class="col-lg-12 col-sm-12 col-xs-12">
			<div class="widget-main ">
				<div class="tabbable">
						<% request.setAttribute("tab", "stock"); %>
						<jsp:include page="../tabs_header.jsp" />
					
					<div class="tab-content">
		<div class="row" id="dash_article_stock">
			<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
				<div class="well with-header" style="height: 575px;" id="dash_art_1">
				</div>
			</div>
			<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
				<div class="well with-header" style="height: 575px;" id="dash_art_2">
				</div>
			</div>
		</div>	
		<div class="row">
			<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12" id="dash_inv">

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

<script type="text/javascript">
	$(window).ready(function () {
   	  // Charger les donn�es
      loadWidgetsAsynch();
	});
        
     function loadWidgetsAsynch(){
     	var divLoading = "<div style='text-align:center;width:100%;height:100%;position:absolute;margin-top:30%;'><span style='color: #777;font-size: 12px;'>Chargement ...</span><img src='resources/<%=StrimUtil.getGlobalConfigPropertie("context.soft") %>/img/loading3.svg'></div>";
     	$("#dash_art_1").html(divLoading);
     	$("#dash_art_2").html(divLoading);
     	
     	submitAjaxForm('<%=EncryptionUtil.encrypt("dash.dashStock.init_article_stock")%>', null, $("#search-form"), null, "dash_article_stock");
     
     }

</script>