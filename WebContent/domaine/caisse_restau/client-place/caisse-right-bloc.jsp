<%@page import="framework.component.complex.table.RequestTableBean"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="appli.controller.domaine.caisse.ContextAppliCaisse"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="java.util.List"%>
<%@page import="appli.model.domaine.stock.persistant.ArticlePersistant"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="appli.controller.domaine.caisse.action.CaisseWebBaseAction"%>
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
int HEIGHT_DETAIL = 370;
boolean isCaisseNotFermee = ContextAppli.getEtablissementBean() != null 
	&& ContextAppliCaisse.getJourneeCaisseBean() != null
	&& ContextAppliCaisse.getJourneeCaisseBean().getStatut_caisse().equals("O");
boolean isJourneeCaisseOuverte = isCaisseNotFermee;
boolean isJourneeOuverte = (ContextAppliCaisse.getJourneeBean() != null && ContextAppliCaisse.getJourneeBean().getStatut_journee().equals("O"));
%>

<script type="text/javascript">
$(document).ready(function (){
	refreshSize();
	$("#left-div").show();
	
	<%String imgBack = null;%>
	var tempo = 90000;
	// Tempo pour effacer l'�cran
	var timer = window.setTimeout(function(){
	}, tempo)
	// Mise en veille de l'�cran
	$(".tab-content a").click(function(){
		window.clearTimeout(timer);
		$("#img-back-cmd").remove();
		
		timer = window.setTimeout(function(){
			<%IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
			Map<String, byte[]> dataimg = service.getDataImage(ContextAppli.getEtablissementBean().getId(), "paramFE");
			if(dataimg.size() > 0){
				imgBack = "data:image/jpeg;base64,"+FileUtil.getByte64(dataimg.entrySet().iterator().next().getValue());%>
	//			$("#menu-detail-div").html("<img id='img-back-cmd' src='<%=imgBack%>' width='100%' height='100%'>");
			<%} else{%>
	//			$("#menu-detail-div").html('');
			<%}%>
		}, tempo)
	});
});

</script>


<style>
.btn-grad {
    background-image: linear-gradient(to right, #f31606 0%, #c2db56 51%, #ec1e09 100%);	margin: 10px;
	padding: 15px 45px;
	text-align: center;
	width: 95%;
	font-size: 7em;
	font-weight: bolde;
	text-transform: uppercase;
	transition: 0.5s;
	background-size: 200% auto;
	color: white;
	box-shadow: 0 0 20px #eee;
	border-radius: 10px;
	display: block;
	height: 2em;
}

.btn-grad:hover {
	background-position: right center;
	/* change the direction of the change here */
	color: #fff;
	text-decoration: none;
}

.btn-gradFocus {
	    background-image: linear-gradient(to right, #750404 0%, #480f0f 51%, #e80a39 100%);
	color: white;
}

.btn-grad:focus {
	    background-image: linear-gradient(to right, #750404 0%, #480f0f 51%, #e80a39 100%); 
	color: white;
}


            #detailsBott {
       margin-left: 58%;
    appearance: none;
    background-color: #FFFFFF;
    border-radius: 50%;
    border-style: none;
    box-shadow: #ADCFFF 0 -12px 6px inset;
    box-sizing: border-box;
    color: #000000;
    margin-top: 36px;
    cursor: pointer;
    font-size: 1.2rem;
    font-weight: 900;
    letter-spacing: -.24px;
    outline: none;
    padding: 1rem 1.3rem;
    quotes: auto;
    margin-top: -98px;
    text-decoration: none;
    width: 66%;
    height: 9.5em;
    position: absolute;
    z-index: 9999999999;
    top: -3.2em;
    margin-bottom: 6px;
    right: -12.5em;
    user-select: none;
    -webkit-user-select: none;
    touch-action: manipulation;
}

#detailsBott:hover {
  background-color: #FFC229;
  box-shadow: #FF6314 0 -6px 8px inset;
  transform: scale(1.125);
}

#detailsBott:active {
  transform: scale(1.025);
}

@media (min-width: 768px) {
  #detailsBott {
    font-size: 1.5rem;
    padding: .75rem 2rem;
  }
}

</style>

<c:set var="encryptionUtil" value="<%=new EncryptionUtil()%>" />

	<div style="float: left;    width: 101%;">
		<!-- Familles et menus -->
		
		
		<%
		String tableName = "list_article";
		RequestTableBean cplxTable = ControllerUtil.getRequestTableBean(tableName, request);
		String currAct = EncryptionUtil.encrypt("stock.etatStock.work_find");
		String jsFunction = "";
		// *************************** Pager ******************************//
			int element_count = cplxTable.getDataSize();
			int line_count = cplxTable.getPageSize();
			int curent_page = cplxTable.getCurrentPage();
			int page_count = (int) Math.ceil((double) element_count / line_count);// Calculate
			int end = cplxTable.getLimitIndex();
		
		
		
		%>
		<div style="float: left;width: 100%;" id="famille-div" >
			
<%if(isJourneeCaisseOuverte){ %>			
                 <c:set var="genericService" value="<%=ServiceUtil.getBusinessBean(IArticleService.class) %>" />
                 <c:set var="caisseWeb" value="<%=new CaisseWebBaseAction() %>" />
                
                <div style="<%=CaisseWebBaseAction.GET_STYLE_CONF("PANEL_MENU_FAMILLE", null)%>">
					<div class="col-md-6">
					    <button type="button" id="menusB" onclick="openType('menus')" class="btn-grad" >MENUS</button>
					    
				        <div id="menus" class="type-cmd" style="width: 203%;">
				         <c:forEach var="menu" items="${listMenu }">
				         	<std:link style='${caisseWeb.GET_STYLE_CONF("BUTTON_MENU", "COULEUR_TEXT_MENU")};' action="caisse-web.caisseWeb.loadMenu" targetDiv="menu-detail-div" workId="${menu.id }" classStyle="caisse-top-btn menu-btn" value="">
				         		<span class="span-img-stl" style="width: 21%;height: 100%;">
				         			<img alt="" onerror="this.onerror=null;this.remove();" src="resourcesCtrl?elmnt=${encryptionUtil.encrypt(menu.getId().toString())}&path=menu&rdm=${menu.date_maj.getTime()}" class="img-caisse-stl">
				         		</span>
				         		<span class="span-libelle-stl" style="font-size: 3em;float: left;margin-top: 2%;width: 79%;text-align: left;">${menu.libelle }</span>
				         	</std:link>
						</c:forEach>
						</div>
                   </div>
					<div class="col-md-6" >
						<button type="button" id="articlesB" onclick="openType('articles')" class="btn-grad" >ARTICLES</button>
                   		<div id="articles" class="type-cmd" style="    width: 203%;float: right;margin-right: -41px;margin-right: -14px;">
                   		<c:forEach var="famille" items="${listFamille }">
                   			<std:link style='${caisseWeb.GET_STYLE_CONF("BUTTON_FAMILLE", "COULEUR_TEXT_FAMILLE")};' action="caisse-web.caisseWeb.familleEvent" targetDiv="menu-detail-div" workId="${famille.id }" classStyle="caisse-top-btn famille-btn" value="">
	         					<span class="span-img-stl" style="width: 20%;
    							margin-top: 10px;"> 
    							<img alt="" onerror="this.onerror=null;this.remove();" style="border-radius: 40%; transform: scale(1.3);"
								src="resourcesCtrl?elmnt=${encryptionUtil.encrypt(famille.getId().toString())}&path=famille&rdm=${famille.date_maj.getTime()}"
								class="img-caisse-stl">
							</span>
	         					<span class="span-libelle-stl" style="font-size: 3em;
							    margin-right: 17px;
							    text-align: -webkit-auto;
							    width: 71%;
							    height: 91%;
							    margin-top: 9px;">${famille.libelle }</span>
	                   		</std:link>
						</c:forEach>
						</div>	
						
						<!-- 	page suivant et precedent  -->

              <%--    <div class="row">
				<div class="col-md-3">

                 <%jsFunction = "pagerAjaxTable('"+tableName+"','" + (curent_page - 1) + "', '"+currAct+"');";
					String onClickPrev = ((curent_page != 1) ? " onClick=\"" + jsFunction + "\"" : "");
					%>
						<!-- Precedent --> 
						<a
						class="btn btn-default btn-xs shiny icon-only success"
						href="javascript:void(0);"
						style="margin-right:2px;margin-top: -2px;<%=(curent_page != 1) ? "" : "background: #ccc;"%>"
						title="<%=StrimUtil.label("back.page")%>"
						<%=(curent_page != 1) ? onClickPrev : ""%>> <i
							class="fa fa-chevron-left"></i>
					</a>
				</div>
				<div class="col-md-3">
				<%=page_count%>
				</div>
				<div class="col-md-3">
                <%jsFunction = "pagerAjaxTable('"+tableName+"', '" + (curent_page + 1) + "', '"+currAct+"');";
					String onClickNext = ((curent_page < page_count) ? (" onClick=\"" + jsFunction) + "\"" : "");
					%>
					<!-- Next --> 
					<a
					class="btn btn-default btn-xs shiny icon-only success"
					href="javascript:void(0);"
					style="margin-left:2px;margin-right:2px;margin-top: -2px;<%=(curent_page < page_count) ? "" : "background: #ccc;"%>"
					title="<%=StrimUtil.label("next.page")%>"
					<%=(curent_page < page_count) ? onClickNext : ""%>> <i
						class="fa fa-chevron-right"></i>
				</a>
				</div>
			</div> --%>
			
               </div>
                        <%} else if (!isJourneeOuverte){%>
                       			<h2 style="text-align: center;color: orange;top: 17%;left:30%;position:absolute;font-size: 67px;font-weight: bold !important;padding-top: 27px;">
                        			<i class="fa fa-lock" style="color: red !important;"></i>
                        			Journ&eacute;e Ferm&eacute;e
                        		</h2>
                        <%} else if(!isCaisseNotFermee){ %>
                        	<h2 style="text-align: center;color: red;top: 17%;left:30%;position:absolute;font-size: 67px;font-weight: bold !important;padding-top: 27px;">
                        		<i class="fa fa-lock" style="color: red !important;"></i>
                        		Caisse Ferm&eacute;e
                        	</h2>
                        <%} %>
		</div>
	</div>
	

	
	
	
	<!-- Commande d&eacute;tail -->
	<div style="float: left;width: 100%;margin-top: 30px;">
	<%if(isJourneeCaisseOuverte){ %>	
		<hr     style=" border: 10px solid #B46060;
					    border-image: linear-gradient(to right, #750404 0%, #480f0f 51%, #e80a39 100%);
					    border-image-slice: 1;
					    border-radius: 10px;">
		<%} %>
		<div id="menu-detail-div" style='overflow-y: auto;overflow-x: hidden;padding: 5px;float: left;width: 102%;    background-image: linear-gradient(to right, #ab2525 0%, #a92222 51%, #6c2a38 100%);;height: 100vh;'>
			
		</div>
		
		<div id="menu-left-div" style="    width: 70%;
    float: left;
    position: fixed;
    margin-bottom: px;
    padding-left: 9px;
    /* margin-top: 40%; */
    bottom: 5px;
    margin-left: 19px;">
		<%if(isJourneeCaisseOuverte){ %>

		<div class="row" style="margin-right: -12%;">
		<div class="col-12 col-md-3">


			</div>
			<div class="col-12 col-md-3">
				<std:link style="width: 140px;float: right;
    height: 140px;
    padding-top: 42px;" id="up_btn" action="caisse-web.caisseWeb.upButtonEvent" targetDiv="menu-detail-div" classStyle="btn btn-default btn-circle btn-lg btn-menu shiny" tooltip="Monter d'un niveau">
<!-- 					<img  src="resources/caisse/img/caisse-web/upload.png" /> -->
<i class="fa fa-arrow-circle-up" style="font-size: 9em;"></i>
				</std:link>


			</div>
			<div class="col-12 col-md-3">
				<std:linkPopup style="width: 140px;float: right;
    height: 140px;
    padding-top: 43px;" action="caisse-web.caisseWebClient.initPaiement" classStyle="btn btn-info btn-circle btn-lg btn-menu shiny" tooltip="Encaisser la commande">
				<i class="fa fa-calculator" style="font-size: 6em;"></i>
<!-- 					<img src="resources/caisse/img/caisse-web/cash_register_sh.png" /> -->
				</std:linkPopup>


			</div>
			<div class="col-12 col-md-3" style="margin-top: -43px;">
				<%
					if (ControllerUtil.getUserAttribute("CURRENT_COMMANDE", request) != null) {
				%>
				<a id="annul_cmd_main" act="<%=EncryptionUtil.encrypt("caisse-web.caisseWebClient.annulerCommande")%>" class="btn btn-default btn-circle btn-lg btn-menu shiny" style="width: 140px;height: 140px;padding-top: 38px;float: right;margin-top: 18%;" title="Annuler la commande"> <i class="fa fa-remove" style="color: red !important;font-size: 9em;"></i></a>




			</div>
			
<div class="col-12 col-md-3">
<button type="button" id="detailsBott" role="button">
    <i style="font-size: 7em;" id="font_ow" class="fa fa-shopping-cart"></i>
	</button>
</div>

		</div>


		<%} 
		} %>	
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