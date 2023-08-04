<%@page import="framework.model.common.util.StringUtil"%>
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
	#scroll_list_paiement_echeance{
		max-height: 325px !important;
		font-size: 12px;
	}
	#scroll_list_cheque_nonpointe{
		max-height: 325px !important;
		font-size: 12px;
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
						<% request.setAttribute("tab", "compta"); %>
						<jsp:include page="../tabs_header.jsp" />
					
					<div class="tab-content">
					
	<!-- ******************************* the Flash Info chart row ******************************* -->
	<div class="row">
		<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
			<div class="well with-header" style="height: 675px;">
				<div class="header bordered-darkorange">
					Bilan financier
				</div>
				<div class="input-group date" style="width: 260px;position: absolute;right: 90px;top: 0px;">
	  			<span style="font-size: 16px;float: left;margin-top: 8px;margin-right: 5px;">date debut </span>
	  		
	  			<input type="text" class="form-control" name="dateDebut" id="dateDebut" style="font-size: 16px;color:green !important;font-weight: bold;border: 0px;width: 90px;" value="<%=StringUtil.getValueOrEmpty(request.getAttribute("dateDebut"))%>">
	  			<span class="input-group-addon" style="border: 1px solid #f3f3f3;padding-top: 9px;">
	  				<i class="fa fa-calendar" style="font-size: 22px;color: #9C27B0;"></i>
	  			</span>
				</div>		
				<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 610px;" id="dash-etat">

				</div>
			</div>
		</div>		
		<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
			<div class="well with-header" style="height: 575px;">
				<div class="header bordered-darkorange">Situation des ch&egrave;ques</div>
				<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 510px;" id="dash-cheque">
					
				</div>
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
			<div class="well with-header" style="height: 575px;">
				<div class="header bordered-darkorange">
					Etat des banques
				</div>
				<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 510px;" id="dash-compte">
					
				</div>
			</div>
		</div>		
		<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
			<div class="well with-header" style="height: 575px;">
				<div class="header bordered-darkorange">Ech&eacute;ances paiements non r&eacute;gl&eacute;es</div>
				<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 350px;" id="dash-echeance">
					
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
      
      $('.input-group.date, #dateDebut').datepicker({
      	clearBtn: true,
  	    language: "fr",
  	    autoclose: true,
  	    format: "mm/yyyy",
  	    startView: 1,
  	    endDate : '<%=request.getAttribute("maxDate")%>m',
  	    minViewMode: 1
      });
  	$('.input-group.date').datepicker().on("changeDate", function(e) {
          var currDate = $('#dateDebut').datepicker('getFormattedDate');
          submitAjaxForm('<%=EncryptionUtil.encrypt("dash.dashCompta.initEtatFinance")%>', "is_dash=1&dateDebut='+currDate", $("#search-form"), null, "dash-etat");
      });
      
  	loadWidgetsAsynch();
      
  });

        
   function loadWidgetsAsynch(){
	   	var divLoading = "<div style='text-align:center;width:100%;height:100%;position:absolute;margin-top:30%;'><span style='color: #777;font-size: 12px;'>Chargement ...</span><img src='resources/<%=StrimUtil.getGlobalConfigPropertie("context.soft") %>/img/loading3.svg'></div>";
	   	$("#dash-echeance").html(divLoading);
	   	$("#dash-cheque").html(divLoading);
	   	$("#dash-compte").html(divLoading);
	   	//
	   	submitAjaxForm('<%=EncryptionUtil.encrypt("dash.dashCompta.initEtatFinance")%>', "is_dash=1", $("#search-form"), null, "dash-etat");
	   	submitAjaxForm('<%=EncryptionUtil.encrypt("dash.dashCompta.gestionPaiementEcheance")%>', null, $("#search-form"), null, "dash-echeance");
	  	submitAjaxForm('<%=EncryptionUtil.encrypt("dash.dashCompta.gestionChequeNonPointe")%>', null, $("#search-form"), null, "dash-cheque");
	  	submitAjaxForm('<%=EncryptionUtil.encrypt("dash.dashCompta.evolution_banque")%>', null, $("#search-form"), null, "dash-compte");
   }
        
    </script>