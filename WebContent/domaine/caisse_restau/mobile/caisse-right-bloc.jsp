<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="appli.controller.domaine.caisse.action.CaisseWebBaseAction"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="appli.controller.domaine.caisse.ContextAppliCaisse"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="java.util.List"%>
<%@page import="appli.model.domaine.stock.persistant.ArticlePersistant"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant)ControllerUtil.getUserAttribute("CURRENT_COMMANDE", request);

boolean isCaisseVerouille = ControllerUtil.getMenuAttribute("IS_CAISSE_VERROUILLE", request) != null;
boolean isCaisseNotFermee = ContextAppliCaisse.getJourneeCaisseBean() != null && ContextAppliCaisse.getJourneeCaisseBean().getStatut_caisse().equals("O");
boolean isJourneeCaisseOuverte = !isCaisseVerouille && isCaisseNotFermee;
boolean isJourneeOuverte = (ContextAppliCaisse.getJourneeBean() != null && ContextAppliCaisse.getJourneeBean().getStatut_journee().equals("O"));

boolean isCloseSaisieQte = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CLAC_NBR_SHOW"));
boolean isRestau = ContextAppli.IS_RESTAU_ENV();
%>

<script type="text/javascript">
function clearClac(){
	$("#calc_lnk").html('<img src="resources/caisse/img/caisse-web/calculator_blue.png">');
	$("#qte_calc").val('');
}

$(document).ready(function (){
	<%if(request.getAttribute("isLoadEvent")!=null){ %>
		$("#up_btn").hide();
	<%}%>
	$("#myTab4 a").click(function(){
		
	});
	
// 	var widowHeight = $(window).height();
// 	var widowWidth = $(window).width();
// 	$("#tab_det_fam").css("height", (widowHeight-155)+"px");
	
	// Calc event
	$(document).off('click', '.btn-calc');
	$(document).on('click', '.btn-calc', function(){
		<%if(isCloseSaisieQte){%>
			if($(this).text() == 'C'){
				clearClac();
				<%if(isRestau){%>
					$("#calc_lnk").hide();
				<%}%>
			} else{
				$("#calc_lnk").text($.trim($("#calc_lnk").text())+$.trim($(this).text()));
				$("#qte_calc").val($("#calc_lnk").text());
			}
			<%if(isRestau){%>
				$("#calc_lnk").trigger("click");
			<%}%>	
		<%} else{%>
			if($(this).text() == 'X'){
				$("#calc_lnk").trigger("click");
				return;
			} else if($(this).text() == 'C'){
				clearClac();
			} else{
				$("#calc_lnk").text($.trim($("#calc_lnk").text())+$.trim($(this).text()));
				$("#qte_calc").val($("#calc_lnk").text());
			}
		<%}%>
	});
	<%if(ContextAppliCaisse.getJourneeBean() != null){%>
	$(document).off('click', '#clore_lnk').on('click', '#clore_lnk', function(){ 
		showConfirmDeleteBox('<%=EncryptionUtil.encrypt("caisse.journee.cloturer_journee")%>', "workId=<%=EncryptionUtil.encrypt(ContextAppliCaisse.getJourneeBean().getId().toString())%>", $("#clore_lnk"), "Vous &ecirc;tes sur le point de clore la journ&eacute;e.<br>Voulez-vous confirmer ?", null, "Clore la journ&eacute;e");
	});
	<%}%>
	refreshSize();
});
</script>

	<div style="float: left;width: 100%;">
		<!-- Familles et menus -->
		<div style="float: left;width: 100%;" id="famille-div">
			
<%if(isJourneeCaisseOuverte){ %>			
			<div class="tabbable">
                 <ul class="nav nav-tabs" id="myTab4">
                    <li class="active tab-purple" style="height: 50px;<%=CaisseWebBaseAction.GET_STYLE_CONF("PANEL_MENU_FAMILLE", null)%>">
                         <a data-toggle="tab" id="main_menu_tab" href="#profile4" style="height: 50px;padding-top: 18px;">ARTICLES</a>
                     </li>
                     <li class="tab-green" style="height: 50px;<%=CaisseWebBaseAction.GET_STYLE_CONF("PANEL_MENU_FAMILLE", null)%>">
                         <a data-toggle="tab" href="#home4" style="height: 50px;padding-top: 18px;">MENUS</a>
                     </li>
                     <li class="tab-green" style="height: 50px;<%=CaisseWebBaseAction.GET_STYLE_CONF("PANEL_MENU_FAMILLE", null)%>">
                         <a data-toggle="tab" href="#menu3" style="height: 50px;padding-top: 18px;">FAVORIS</a>
                     </li>
                 </ul> 
				
                 <c:set var="genericService" value="<%=ServiceUtil.getBusinessBean(IArticleService.class) %>" />
                 <c:set var="fileUtil" value="<%=new FileUtil() %>" />
                 <c:set var="caisseWeb" value="<%=new CaisseWebBaseAction() %>" />
                 <c:set var="encryptionUtil" value="<%=new EncryptionUtil() %>" />
                 
                 <div id="tab_det_fam" class="tab-content" style="overflow-y: auto;overflow-x: hidden;<%=CaisseWebBaseAction.GET_STYLE_CONF("PANEL_MENU_FAMILLE", null)%>">
                     <div id="profile4" class="tab-pane in active">
                   	<c:forEach var="famille" items="${listFamille }">
                   		<div class="col-xs-6 col-md-6 col-lg-6">
                   			<std:link style='${caisseWeb.GET_STYLE_CONF("BUTTON_FAMILLE", "COULEUR_TEXT_FAMILLE")};' action="caisse-web.caisseWeb.familleEvent" targetDiv="right-div" workId="${famille.id }" classStyle="caisse-top-mobile-btn famille-btn" value="">
	         					<span class="span-img-stl">
	         						<img alt="" src="resourcesCtrl?elmnt=${encryptionUtil.encrypt(famille.getId().toString())}&path=famille&rdm=${famille.date_maj.getTime()}" class="img-caisse-stl">
	         					</span>
	         					<span class="span-libelle-stl">${famille.libelle }</span>
	                   		</std:link>
	                   	</div>
					</c:forEach>
                </div>
                     
                     <div id="home4" class="tab-pane">
         <c:forEach var="menu" items="${listMenu }">
         	<div class="col-xs-6 col-md-6 col-lg-6">
	         	<std:link style='${caisseWeb.GET_STYLE_CONF("BUTTON_MENU", "COULEUR_TEXT_MENU")};' action="caisse-web.caisseWeb.loadMenu" targetDiv="right-div" workId="${menu.id }" classStyle="caisse-top-mobile-btn menu-btn" value="">
	         		<span class="span-img-stl">
	         			<img alt="" src="resourcesCtrl?elmnt=${encryptionUtil.encrypt(menu.getId().toString())}&path=menu&rdm=${menu.date_maj.getTime()}" class="img-caisse-stl">
	         		</span>
	         		<span class="span-libelle-stl">${menu.libelle }</span>
	         	</std:link>
	         </div>	
		</c:forEach>
                   </div>
                   <div id="menu3" class="tab-pane">
                     <%
                     List<ArticlePersistant> listArticleFavoris = (List<ArticlePersistant>)request.getAttribute("listArticleFavoris");
						if(listArticleFavoris != null){
							for(ArticlePersistant artP : listArticleFavoris){
								if(BooleanUtil.isTrue(artP.getIs_disable())){
									continue;
								}
								String libelle = artP.getLibelle().replaceAll("\\#", "");
								String prix = BigDecimalUtil.formatNumber(artP.getPrix_vente());
							%>
								<div class="col-xs-6 col-md-6 col-lg-6">	
						         	<std:link style='<%=CaisseWebBaseAction.GET_STYLE_CONF("BUTTON_ARTICLE", "COULEUR_TEXT_DETAIL")%>' action="caisse-web.caisseWeb.addArticleFamilleCmd" targetDiv="left-div" workId="<%=artP.getId().toString() %>" classStyle="caisse-mobile-btn detail-article-btn" value="">
										<span class="span-img-stl">
											<img alt="" src="resourcesCtrl?elmnt=<%=EncryptionUtil.encrypt(artP.getId().toString()) %>&path=article&rdm=<%=(artP.getDate_maj()!=null?artP.getDate_maj().getTime():"1")%>" class="<%=StringUtil.isNotEmpty(prix) ? "img-caisse-det-stl":"img-caisse-stl" %>">
										</span>	
						        		<span class="span-libelle-stl"><i class="fa fa-tag" style='color:red;'></i><%=libelle %>&nbsp;&nbsp;</span>
						        		<%if(StringUtil.isNotEmpty(prix)){ %>
						        			<span class="span-prix-stl">[<%=prix %>]</span>
						        		<%} %>	
									</std:link>	
								</div>
						<%	}
						}%>
                   </div>
                                  </div>
                              </div>
                        <%} else{%>
                        	<jsp:include page="/domaine/caisse/normal/fragment/button_centre_caisse_mobile.jsp" />
                        <%} %>	
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