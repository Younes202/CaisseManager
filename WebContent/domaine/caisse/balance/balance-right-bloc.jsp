<%@page import="appli.controller.domaine.caisse.action.CaisseWebBaseAction"%>
<%@page import="appli.controller.domaine.caisse.ContextAppliCaisse"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="java.util.Map"%>
<%@page import="appli.model.domaine.stock.persistant.ArticlePersistant"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
int HEIGHT_HEADER = 250;
int WIDTH_CMD = 420;
int HEIGHT_DETAIL = 413;
boolean isJourneeOuverte = (ContextAppliCaisse.getJourneeBean() != null && ContextAppliCaisse.getJourneeBean().getStatut_journee().equals("O"));
%>

<script type="text/javascript">
function clearClac(){
	$("#calc_lnk").html('<img src="resources/caisse/img/caisse-web/calculator_blue.png">');
	$("#qte_calc").val('');
}

$(document).ready(function (){
	$("#search_tab").click(function(){
		$("#art\\.code_barre, #art\\.code").val('');
		setTimeout(function(){
			$("#art\\.code_barre").focus();
		}, 1000);
	});
	
	$("#myTab4 a").click(function(){
		resetDetailCmd();
		barcode = "";
	});
	
	// Calc event
	$(document).off('click', '.btn-calc');
	$(document).on('click', '.btn-calc', function(){
		if($(this).text() == 'C'){
			clearClac();
		} else{
			$("#calc_lnk").text($.trim($("#calc_lnk").text())+$.trim($(this).text()));
			$("#qte_calc").val($("#calc_lnk").text());
		}
		$("#calc_lnk").trigger("click");
	});
	refreshSize();
});
</script>

	<div style="float: left;">
		<!-- Familles et menus -->
		<div style="height: <%=HEIGHT_DETAIL+20%>px;float: left;" id="famille-div">
			
<%if(isJourneeOuverte){ %>			
			<div class="tabbable tabs-right">
				<ul class="nav nav-tabs" id="myTab4" style="text-align: right;">
                     <li class="active tab-green" style="height: 80px;<%=CaisseWebBaseAction.GET_STYLE_CONF("PANEL_MENU_FAMILLE", null)%>">
                         <a data-toggle="tab" href="#menu2" style="height: 80px;padding-top: 31px;">
                         	<span class="tab-title-cai">FAMILLES</span>
                         </a>
                     </li>
                     <li class="tab-green" style="height: 80px;<%=CaisseWebBaseAction.GET_STYLE_CONF("PANEL_MENU_FAMILLE", null)%>">
                         <a data-toggle="tab" href="#menu3" style="height: 80px;padding-top: 31px;">
                         	<span class="tab-title-cai">FAVORIS</span>
                         </a>
                     </li>
                     <li class="tab-green" style="height: 80px;<%=CaisseWebBaseAction.GET_STYLE_CONF("PANEL_MENU_FAMILLE", null)%>">
                         <a data-toggle="tab" id="search_tab" href="#menu4" style="height: 80px;padding-top: 31px;">
                         	<span class="tab-title-cai">RECHERCHE</span>
                         </a>
                     </li>
                 </ul>
                 
                 <c:set var="genericService" value="<%=ServiceUtil.getBusinessBean(IArticleService.class) %>" />
                 <c:set var="fileUtil" value="<%=new FileUtil() %>" />
                 <c:set var="caisseWeb" value="<%=new CaisseWebBaseAction() %>" />
                 
                 <div class="tab-content" style="height: <%=HEIGHT_DETAIL%>px;overflow: hidden;<%=CaisseWebBaseAction.GET_STYLE_CONF("PANEL_MENU_FAMILLE", null)%>">
                      <div id="menu2" class="tab-pane in active">
	                   	<div id="menu2" class="tab-pane in active">
	                   		<jsp:include page="/domaine/caisse/balance/pager-famille-include.jsp" />
                     	</div>
                     </div>
                     <div id="menu3" class="tab-pane">
				         <jsp:include page="/domaine/caisse/balance/pager-favoris-include.jsp" />
                   	 </div>
                   	 <div id="menu4" class="tab-pane">
				         <div class="row" style="margin-left: 5px;">
				         	<div class="col-md-4" id="input_barre">
								<div class="col-md-12">
									<std:text name="art.code_barre" placeholder="Code barre" type="string" style="border-radius: 25px !important;font-weight: bold;height:50px;width:50%;font-size: 25px;float:left;" maxlength="15" />
								</div>
								<div class="col-md-12" style="margin-top: 3%;">	
									<std:text name="art.code" placeholder="Code/Libell&eacute; article" type="string" style="border-radius: 25px !important;font-weight: bold;height:50px;width:100%;font-size: 25px;float:left;" maxlength="120" />
								</div>
								<div class="col-md-12" style="text-align: center;margin-top: 20px;">	
									<std:button action="caisse-web.balance.loadArtCodeBarre" classStyle="btn btn-lg shiny btn-primary" targetDiv="menu-detail-div" value="Rechercher" />
								</div>	
							</div>	
							<div class="col-md-8" id="code_keys" style="border-left: 1px solid #e1e1e1;">
								<div class="col-md-12">
									<%for(int i=0; i<10; i++){ %>
										<a href="javascript:void(0);" class="btn btn-info btn-circle num_auth_stl btn_code_bar"><%=i %></a>
									<%} %>
									<a href="javascript:void(0);" class="btn btn-info btn-circle num_auth_stl btn_code_bar">.</a>
								</div>
								<hr style="width: 100%;float: left;">
								<div class="col-md-12">
									<%for(int i=0; i<ContextAppli.ALPHABET.length; i++){ %>
									<a href="javascript:void(0);" class="btn btn-success btn-circle num_auth_stl btn_code_bar"><%=ContextAppli.ALPHABET[i] %></a>
									<%} %>
									<a href="javascript:void(0);" class="btn btn-info btn-circle num_auth_stl btn_code_bar"> </a>
									<a href="javascript:void(0);" class="btn btn-info btn-circle num_auth_stl btn_code_bar">-</a>
									<a href="javascript:void(0);" class="btn btn-info btn-circle num_auth_stl btn_code_bar">_</a>
									
									<a href="javascript:void(0);" id="back" class="btn btn-warning btn-circle num_auth_stl btn_code_bar" style="font-size: 12px;margin-left: 20px;"><i class="fa fa-mail-reply"></i></a>
									<a href="javascript:void(0);" id="reset" class="btn btn-warning btn-circle num_auth_stl btn_code_bar" style="font-size: 12px;"><i class="fa fa-times"></i></a>
								</div>
							</div>	
						</div>
                   	 </div>
                 </div>
              </div>
      <%} else{%>
        		<h2 style="text-align: center;color: orange;margin-top: 17%;font-size: 67px;margin-left: 36%;font-weight: bold !important;border: 2px solid blue;border-radius: 79px;height: 150px;padding-top: 28px;">
         			<i class="fa fa-lock" style="color: red !important;"></i>
         			Journ&eacute;e Ferm&eacute;e
         		</h2>
        <%} %>
		</div>
	</div>
	<!-- Commande d&eacute;tail -->
	<div style="float: left;width: 100%;">
		<div id="menu-left1-div" style="width: 80px;float:left;padding-left: 5px;">
		<%if(isJourneeOuverte){ %>		
			<std:link id="up_btn" action="caisse-web.balance.upButtonEvent" targetDiv="menu-detail-div" classStyle="btn btn-default btn-circle btn-lg btn-menu shiny" tooltip="Monter d'un niveau">
				<img src="resources/caisse/img/caisse-web/upload.png" />
			</std:link>
			<% 
		  } %>
		</div>
		<div id="menu-detail-div" style='overflow-y: auto;overflow-x: hidden;padding: 5px;float: left;${caisseWeb.GET_STYLE_CONF("PANEL_DETAIL", null)}'>
		
		</div>
	</div>
<script type="text/javascript">
$(document).ready(function (){
	/*Handles Popovers*/
	var popovers = $('[data-toggle=popover]');
	$.each(popovers, function () {
	    $(this)
	        .popover({
	            html: true,
	            template: '<div class="popover ' + $(this)
	                .data("class") +
	                '"><div class="arrow"></div><h3 class="popover-title ' +
	                $(this)
	                .data("titleclass") + '">Popover right</h3><div class="popover-content"></div></div>'
	        });
	});

	var hoverpopovers = $('[data-toggle=popover-hover]');
	$.each(hoverpopovers, function () {
	    $(this)
	        .popover({
	            html: true,
	            template: '<div class="popover ' + $(this)
	                .data("class") +
	                '"><div class="arrow"></div><h3 class="popover-title ' +
	                $(this)
	                .data("titleclass") + '">Popover right</h3><div class="popover-content"></div></div>',
	            trigger: "hover"
	        });
	});
    });
 </script>
 
 <jsp:include page="/commun/print-local.jsp" />   