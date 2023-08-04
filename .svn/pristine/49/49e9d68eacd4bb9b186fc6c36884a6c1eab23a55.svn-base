<%@page import="java.io.File"%>
<%@page import="appli.model.domaine.stock.persistant.FamillePersistant"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.controller.domaine.caisse.ContextAppliCaisse"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="java.util.List"%>
<%@page import="appli.model.domaine.stock.persistant.ArticlePersistant"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="appli.controller.domaine.caisse.action.CaisseWebBaseAction"%>
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
int HEIGHT_DETAIL = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig("HAUTEUR_BLOC_FAMILLE")).intValue();
boolean isDroitOuvrirJrn = StringUtil.isTrueOrNull(""+ControllerUtil.getUserAttribute("RIGHT_CLOJRN", request));

boolean isCaisseVerouille = ControllerUtil.getMenuAttribute("IS_CAISSE_VERROUILLE", request) != null;
boolean isCaisseNotFermee = ContextAppliCaisse.getJourneeCaisseBean() != null && ContextAppliCaisse.getJourneeCaisseBean().getStatut_caisse().equals("O");
boolean isJourneeCaisseOuverte = !isCaisseVerouille && isCaisseNotFermee;
boolean isJourneeOuverte = (ContextAppliCaisse.getJourneeBean() != null && ContextAppliCaisse.getJourneeBean().getStatut_journee().equals("O"));
CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant)ControllerUtil.getUserAttribute("CURRENT_COMMANDE", request);
IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);

boolean isServeur = "SERVEUR".equals(ContextAppli.getUserBean().getOpc_profile().getCode());
boolean isCaissier = ContextAppli.getUserBean().isInProfile("CAISSIER");
boolean isManager = ContextAppli.getUserBean().isInProfile("MANAGER");
boolean isAdmin = ContextAppli.getUserBean().isInProfile("ADMIN");

boolean isConfirmDelCmd = (StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("SECURE_DELETE_CMD"))
								&& !isAdmin && !isManager);

isConfirmDelCmd = (isConfirmDelCmd && CURRENT_COMMANDE.getId() != null && !CURRENT_COMMANDE.getLast_statut().equals(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.TEMP.toString()));
boolean isCloseSaisieQte = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CLAC_NBR_SHOW"));

List<FamillePersistant> listFamille = (List<FamillePersistant>)request.getAttribute("listFamille");
%>

<script type="text/javascript">
$(document).ready(function (){
	
	$("[data-toggle=popover]").popover({
        html : true,
        content: function() {
          var content = $(this).attr("data-popover-content");
          return $(content).children(".popover-body").html();
        },
        title: function() {
          var title = $(this).attr("data-popover-content");
          return $(title).children(".popover-heading").html();
        }
    });
	
	
	// Gestion mise en attente (aussi dans commande d�tail)
	<%if(CURRENT_COMMANDE == null || StringUtil.isEmpty(CURRENT_COMMANDE.getType_commande())){%>
		$("#att_pop_lnk").show();
		$("#att_std_lnk").hide();
	<%} else{%>
		$("#att_pop_lnk").hide();
		$("#att_std_lnk").show();
	<%} %>
	
	refreshSize();
	$("#left-div").show();
	
	<%-- Sauthentifier pour sortir du lock --%>
	$(document).off('click', '#delock_lnk').on('click', '#delock_lnk', function(){
		$("#del-cmd-lnk").attr("params", "tp=delock").trigger("click");
	});
	<%if(ContextAppliCaisse.getJourneeBean() != null){%>
	$(document).off('click', '#clore_lnk').on('click', '#clore_lnk', function(){ 
		showConfirmDeleteBox('<%=EncryptionUtil.encrypt("caisse.journee.cloturer_journee")%>', "workId=<%=EncryptionUtil.encrypt(ContextAppliCaisse.getJourneeBean().getId().toString())%>", $("#clore_lnk"), "Vous &ecirc;tes sur le point de clore la journ&eacute;e.<br>Voulez-vous confirmer ?", null, "Clore la journ&eacute;e");
	});
	<%}%>
});
</script>
	
	<div style="float: left;">
		<!-- Familles et menus -->
		<div style="height: <%=HEIGHT_DETAIL+20%>px;float: left;" id="famille-div">
				
		<%
			if(isJourneeCaisseOuverte){
		%>			
			<div class="tabbable tabs-right">
                 <ul class="nav nav-tabs" id="myTab4" style="text-align: right;">
                    <li class="active tab-purple" style="height: 80px;<%=CaisseWebBaseAction.GET_STYLE_CONF("PANEL_MENU_FAMILLE", null)%>">
                         <a data-toggle="tab" href="#menu1" style="height: 80px;padding-top: 31px;">
	                         <span class="tab-title-cai">ARTICLES</span>
                         </a>
                     </li>
                     <li class="tab-green" style="height: 80px;<%=CaisseWebBaseAction.GET_STYLE_CONF("PANEL_MENU_FAMILLE", null)%>">
                         <a data-toggle="tab" href="#menu2" style="height: 80px;padding-top: 31px;">
                         	<span class="tab-title-cai">MENUS</span>
                         </a>
                     </li>
                     <li class="tab-green" style="height: 80px;<%=CaisseWebBaseAction.GET_STYLE_CONF("PANEL_MENU_FAMILLE", null)%>">
                         <a data-toggle="tab" href="#menu3" style="height: 80px;padding-top: 31px;">
                         	<span class="tab-title-cai">FAVORIS</span>
                         </a>
                     </li>
                     <li class="tab-green" style="height: 80px;<%=CaisseWebBaseAction.GET_STYLE_CONF("PANEL_MENU_FAMILLE", null)%>">
                         <a data-toggle="tab" id="search_tab" href="#menu4" style="height: 80px;padding-top: 31px;">
                         	<span class="tab-title-cai">RECH.</span>
                         </a>
                     </li>
                 </ul>
                 
                 <c:set var="genericService" value="<%=ServiceUtil.getBusinessBean(IArticleService.class)%>" />
                 <c:set var="fileUtil" value="<%=new FileUtil()%>" />
                 <c:set var="caisseWeb" value="<%=new CaisseWebBaseAction()%>" />
                 <c:set var="encryptionUtil" value="<%=new EncryptionUtil()%>" />
                 
                 <div class="tab-content" style="height: <%=HEIGHT_DETAIL%>px;overflow: hidden;<%=CaisseWebBaseAction.GET_STYLE_CONF("PANEL_MENU_FAMILLE", null)%>">
                     <div id="menu1" class="tab-pane in active">
                   		<jsp:include page='<%=ControllerUtil.getUserAttribute("PATH_JSP_CAISSE", request)+"/pager-famille-include.jsp"%>' />
                 	</div>
                     <div id="menu2" class="tab-pane">
				         <jsp:include page='<%=ControllerUtil.getUserAttribute("PATH_JSP_CAISSE", request)+"/pager-menu-include.jsp"%>' />
                   </div>
                   <div id="menu3" class="tab-pane">
                     	<jsp:include page='<%=ControllerUtil.getUserAttribute("PATH_JSP_CAISSE", request)+"/pager-favoris-include.jsp"%>' />
                   </div>
                   <div id="menu4" class="tab-pane">
				         <div class="row" style="margin-left: 5px;">
				         	<div class="col-md-12" id="input_barre">
								<div class="col-md-3">
									<std:text name="art.code_barre" forceWriten="true" placeholder="Code barre" type="string" style="border-radius: 25px !important;font-weight: bold;height:50px;font-size: 25px;float:left;text-align:right;" maxlength="15" />
								</div>
								<div class="col-md-5">	
									<std:text name="art.code" forceWriten="true" placeholder="Code/Libellé" type="string" style="border-radius: 25px !important;font-weight: bold;height:50px;font-size: 25px;float:left;" maxlength="120" />
								</div>
									<div class="col-md-2">	
									<std:button action="caisse-web.caisseWeb.loadArtCodeBarre" classStyle="btn btn-lg shiny btn-primary" targetDiv="menu-detail-div" icon="fa fa-search" style="margin-top: 6px;"/>
								</div>
							</div>
						</div>
					</div>	
                 </div>
             </div>
          <%} else { %>
          		<jsp:include page="/domaine/caisse/normal/fragment/button_centre_caisse_web.jsp" />
          <% } %>
		</div>
	</div>
	<!-- Commande détail -->
	<div style="float: left;width: 100%;">
		<div id="menu-left1-div" style="width: 80px;float:left;padding-left: 5px;">
		<% if(isJourneeCaisseOuverte){ %>
			<div style="width: <%=WIDTH_CMD%>px;position:fixed;left:0px;bottom:2px;">
				<% for(int i=0; i<10; i++){%>
					<a class='btn btn-blue btn-lg icon-only white btn-circle btn-calc ' href='javascript:void(0);'><%=i%></a>
				<%}%>
				<a class='btn btn-blue btn-lg icon-only white btn-circle btn-calc' href='javascript:void(0);'>C</a>
				<%=!isCloseSaisieQte?"<a class='btn btn-blue btn-lg icon-only white btn-circle btn-calc' href='javascript:void(0);'>.</a>":""%>
				<%=!isCloseSaisieQte?"<a class='btn btn-blue btn-lg icon-only white btn-circle btn-calc' style='color: red !important;' href='javascript:void(0);'>X</a>":""%>
				<a class='btn btn-blue btn-lg icon-only white btn-circle btn-calc' style='color: #000000 !important;' href='javascript:void(0);'>Tbl</a>
			</div>
			<a href="javascript:void(0);" id="calc_lnk" style="color: #262626;font-size: 32px;font-weight: bold;min-width: 70px;width:auto;" class="btn btn-default btn-circle btn-lg btn-menu shiny">
				<img src="resources/caisse/img/caisse-web/calculator_blue.png" />
			</a>

<!-- 			<a href="javascript:void(0);" id="calc_lnk" style="color: #262626;font-size: 32px;font-weight: bold;min-width: 70px;width:auto;" class="btn btn-default btn-circle btn-lg btn-menu shiny" data-container="body" data-titleclass="bordered-blue" data-class="" data-toggle="popover" data-placement="bottom" data-title=""  -->
<%-- 				data-content="<%for(int i=0; i<10; i++){%><a class='btn btn-blue btn-lg icon-only white btn-circle btn-calc ' href='javascript:void(0);'><%=i%></a><%}%><a class='btn btn-blue btn-lg icon-only white btn-circle btn-calc' href='javascript:void(0);'>C</a><%=!isCloseSaisieQte?"<a class='btn btn-blue btn-lg icon-only white btn-circle btn-calc' href='javascript:void(0);'>.</a><a class='btn btn-blue btn-lg icon-only white btn-circle btn-calc' style='color: red !important;' href='javascript:void(0);'>X</a>":""%>" data-original-title="" title=""> --%>
<!-- 				<img src="resources/caisse/img/caisse-web/calculator_blue.png" /> -->
<!-- 			</a> -->
			<std:link id="up_btn" action="caisse-web.caisseWeb.upButtonEvent" targetDiv="menu-detail-div" classStyle="btn btn-default btn-circle btn-lg btn-menu shiny" tooltip="Monter d'un niveau">
				<img src="resources/caisse/img/caisse-web/upload.png" />
			</std:link>
			
			<%if(ControllerUtil.getUserAttribute("CURRENT_COMMANDE", request) != null){%>
				<hr>
				<std:linkPopup id="att_pop_lnk" action="caisse-web.caisseWeb.miseEnAttente" classStyle="btn btn-default btn-circle btn-lg btn-menu shiny" tooltip="Mettre la commande en attente">
					<img src="resources/caisse/img/caisse-web/hourglass.png" />
				</std:linkPopup>
				<std:link id="att_std_lnk" style="display:none;" action="caisse-web.caisseWeb.miseEnAttente" targetDiv="left-div" classStyle="btn btn-default btn-circle btn-lg btn-menu shiny" tooltip="Mettre la commande en attente">
					<img src="resources/caisse/img/caisse-web/hourglass.png" />
				</std:link>
			
				<%if(isConfirmDelCmd){ %>
					<std:linkPopup classStyle="btn btn-default btn-circle btn-lg btn-menu shiny" action="caisse-web.caisseWeb.loadConfirmAnnule" params="tp=cmd" tooltip="Annuler la commande"> 
						<img src="resources/caisse/img/caisse-web/delete2.png" />
					</std:linkPopup>
				<%} else{ %>
					<a id="annul_cmd_main" act="<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.annulerCommande")%>" targetDiv="left-div" class="btn btn-default btn-circle btn-lg btn-menu shiny" title="Annuler la commande">
						<img src="resources/caisse/img/caisse-web/delete2.png" />
					</a>
				<%} %>
			<%} 
			} %>
		</div>
		<div id="menu-detail-div" style='overflow-y: auto;overflow-x: hidden;padding: 5px;float: left;${caisseWeb.GET_STYLE_CONF("PANEL_DETAIL", null)}'>
			
		</div>
		
		<div id="menu-left-div" style="width: 80px;float:left;padding-left: 9px;">
		<%if(isJourneeCaisseOuverte){ %>
			<%if((StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_EMPLOYE"))) 
							|| 
						(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_CLIENT"))) 
					){ %>		
				<std:linkPopup action="caisse-web.caisseWeb.initPersonne" classStyle="btn btn-default btn-circle btn-lg btn-menu shiny" tooltip="Sélectionner un client ou un employé">
					<img src="resources/caisse/img/caisse-web/user.png" />
				</std:linkPopup>
			<%} %>
			<%if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_TABLE"))){ %>
				<std:linkPopup action="caisse-web.caisseWeb.initPlan" params="isrp=0" classStyle="btn btn-default btn-circle btn-lg btn-menu shiny" tooltip="Affecter une table">
					<img src="resources/caisse/img/caisse-web/table2.png" style="width: 52px;" />
				</std:linkPopup>
			<%} %>
			<%if((!isServeur || isCaissier || isManager || isAdmin) && StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_OFFRE"))){%>
				<std:linkPopup action="caisse-web.caisseWeb.initOffre" classStyle="btn btn-default btn-circle btn-lg btn-menu shiny" tooltip="Appliquer une réduction">
					<img src="resources/caisse/img/caisse-web/free_gift.png" />
				</std:linkPopup>
			<%} %>
			<%if(!isServeur || isCaissier || isManager || isAdmin){%>
				<hr>
				<std:linkPopup action="caisse-web.caisseWeb.initPaiement" classStyle="btn btn-info btn-circle btn-lg btn-menu shiny" tooltip="Encaisser la commande">
					<img src="resources/caisse/img/caisse-web/cash_register_sh.png" />
				</std:linkPopup>
			<%} %>
		<%} %>	
		</div>
	</div>
