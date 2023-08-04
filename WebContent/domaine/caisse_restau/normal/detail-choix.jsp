<%@page import="java.util.HashMap"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.model.domaine.vente.persistant.MenuCompositionPersistant"%>
<%@page import="appli.model.domaine.vente.persistant.ListChoixPersistant"%>
<%@page import="appli.model.domaine.vente.persistant.MenuCompositionDetailPersistant"%>
<%@page import="appli.model.domaine.vente.persistant.ListChoixDetailPersistant"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="appli.controller.domaine.caisse.action.CaisseWebBaseAction"%>
<%@page import="java.util.Map"%>
<%@page import="appli.model.domaine.stock.persistant.FamillePersistant"%>
<%@page import="appli.model.domaine.stock.persistant.ArticlePersistant"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>

<%
boolean isCtrlStock = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("SHOW_STOCK_MVM_CAISSE"));
Map<Long, String> mapStock = (Map)request.getAttribute("mapStock");
if(mapStock == null){
	mapStock = new HashMap<>();
}

List<FamillePersistant> listSousFamille = (List<FamillePersistant>)request.getAttribute("listFamille");
List<ArticlePersistant> listArticle = (List<ArticlePersistant>)request.getAttribute("listArticle");
Map<String, Object> listMenu = (Map<String, Object>)request.getAttribute("listMenu");

IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
List<MenuCompositionPersistant> listSousMenu = (List<MenuCompositionPersistant>)ControllerUtil.getUserAttribute("LIST_SOUS_MENU", request);
Integer step = (Integer)ControllerUtil.getUserAttribute("STEP_MNU", request);
%>
<%
if(listSousMenu != null && listSousMenu.size()>0){
%>
<div id="WiredWizard" class="wizard wizard-wired" data-target="#WiredWizardsteps">
   <ul class="steps">
   <%
	int i=0;
   int withLi = 100/listSousMenu.size();
	for(MenuCompositionPersistant sousMenu : listSousMenu){ 
		 String libelle = sousMenu.getLibelle().replaceAll("\\#", "");
	%>
            <li data-target="#wiredstep<%=i %>" class='<%=(step==i)?"active":((step>i)?"complete":"") %>' style="width: <%=withLi%>%;">
            	<span class="step"><%=i+1 %></span>
            	<span class="title"><%=libelle %></span><span class="chevron"></span>
            </li>
	<%
		i++;
	} %>  
 	</ul>
</div>
<%} %> 
 
       
<%
if(listSousFamille != null){
	for(FamillePersistant familleP : listSousFamille){%>
			<%
			String libelle = familleP.getLibelle().replaceAll("\\#", "");
			%>
			<std:link style='<%=CaisseWebBaseAction.GET_STYLE_CONF("BUTTON_SOUS_FAMILLE", "COULEUR_TEXT_DETAIL")%>' action="caisse-web.caisseWeb.familleEvent" targetDiv="menu-detail-div" workId="<%=familleP.getId().toString() %>" classStyle="caisse-btn detail-famille-btn" value="">
				<span class="span-img-stl">
					<img alt="" src="resourcesCtrl?elmnt=<%=EncryptionUtil.encrypt(familleP.getId().toString()) %>&path=famille&rdm=<%=(familleP.getDate_maj()!=null?familleP.getDate_maj().getTime():"1")%>" class="img-caisse-stl">
				</span>	
       			<span class="span-libelle-stl"><%=libelle %>&nbsp;&nbsp;</span>
			</std:link>
	<%}
}%> 

<!-- Articles -->
<%
if(listArticle != null){
	for(ArticlePersistant articleP : listArticle){
		if(BooleanUtil.isTrue(articleP.getIs_disable())){
			continue;
		}
		boolean isDisStock = (isCtrlStock && mapStock != null && mapStock.get(articleP.getId())!=null && mapStock.get(articleP.getId()).startsWith("*-*"));
		String libStck = ((isCtrlStock && mapStock != null && mapStock.get(articleP.getId())!=null) ? mapStock.get(articleP.getId()).replaceAll("\\*-\\*", "") : "");
		String libelle = articleP.getLibelle().replaceAll("\\#", "");
		String prix = BigDecimalUtil.formatNumber(articleP.getPrix_vente());
		%>
		<std:link style='<%=CaisseWebBaseAction.GET_STYLE_CONF("BUTTON_ARTICLE", "COULEUR_TEXT_DETAIL")%>' params='<%=isDisStock?"disStck=1":"" %>' action="caisse-web.caisseWeb.addArticleFamilleCmd" targetDiv="left-div" workId="<%=articleP.getId().toString() %>" classStyle="caisse-btn detail-article-btn" value="">
			<span class="span-img-stl">
				<img alt="" src="resourcesCtrl?elmnt=<%=EncryptionUtil.encrypt(articleP.getId().toString()) %>&path=article&rdm=<%=(articleP.getDate_maj()!=null?articleP.getDate_maj().getTime():"1")%>" class="img-caisse-det-stl">
			</span>	
       		<span class="span-libelle-stl"><i class="fa fa-tag" style='color:red;'></i><%=libelle %>&nbsp;&nbsp;</span>
       		<%if(isDisStock){ %>
       			<span class="span-repture-stock">RUPTURE STOCK</span>
       		<%} %>
       		<span class="span-prix-stl">[<%=prix %>]
       		<%if(isCtrlStock){ %>
       			<span class="span-stock"><%=StringUtil.getValueOrEmpty(libStck) %></span>
       		<%} %>
       		</span>
		</std:link>
<%	}
}%>

<%
if(listMenu != null){
	for(String cle : listMenu.keySet()){
		Object elementP = listMenu.get(cle);
		
		if(elementP instanceof MenuCompositionPersistant){
			MenuCompositionPersistant menuP = ((MenuCompositionPersistant)elementP);
			if(BooleanUtil.isTrue(menuP.getIs_desactive())){
				continue;
			}
			String libelle = menuP.getLibelle().replaceAll("\\#", "");
			String prix = BigDecimalUtil.isZero(menuP.getMtt_prix()) ? "":BigDecimalUtil.formatNumber(menuP.getMtt_prix());
			%>
			<std:link style='<%=CaisseWebBaseAction.GET_STYLE_CONF("BUTTON_SOUS_MENU", "COULEUR_TEXT_DETAIL")%>' action="caisse-web.caisseWeb.menuCompoEvent" targetDiv="menu-detail-div" workId="<%=menuP.getId().toString() %>" classStyle="caisse-btn detail-menu-btn" value="">
				<span class="span-img-stl">
					<img alt="" src="resourcesCtrl?elmnt=<%=EncryptionUtil.encrypt(menuP.getId().toString()) %>&path=menu&rdm=<%=(menuP.getDate_maj()!=null?menuP.getDate_maj().getTime():"1")%>" class="<%=StringUtil.isNotEmpty(prix) ? "img-caisse-det-stl":"img-caisse-stl" %>">
				</span>	
        			<span class="span-libelle-stl"><%=libelle %>&nbsp;&nbsp;</span>
        			<%if(StringUtil.isNotEmpty(prix)){ %>
				<span class="span-prix-stl">[<%=prix %>]</span>
				<%} %>
			</std:link>
	  	<%} 
		else if(elementP instanceof ArticlePersistant){
			String params = "mnu="+cle;
			ArticlePersistant artP = (ArticlePersistant)elementP;
			
			if(BooleanUtil.isTrue(artP.getIs_disable())){
				continue;
			}
			boolean isDisStock = (isCtrlStock && mapStock.get(artP.getId())!=null && mapStock.get(artP.getId()).startsWith("*-*"));
			String libelle = artP.getLibelle().replaceAll("\\#", "");
			%>
			<std:link style='<%=CaisseWebBaseAction.GET_STYLE_CONF("BUTTON_ARTICLE", "COULEUR_TEXT_DETAIL")%>' params='<%=params+(isDisStock?"&disStck=1":"") %>' action="caisse-web.caisseWeb.addArticleMenuCmd" targetDiv="left-div" workId="<%=artP.getId().toString() %>" classStyle="caisse-btn detail-article-btn" value="">
				<span class="span-img-stl">
					<img alt="" src="resourcesCtrl?elmnt=<%=EncryptionUtil.encrypt(artP.getId().toString()) %>&path=article&rdm=<%=(artP.getDate_maj()!=null?artP.getDate_maj().getTime():"1")%>" class="img-caisse-stl">
				</span>	
        		<span class="span-libelle-stl"><i class="fa fa-tag" style='color:red;'></i><%=libelle %>
        		<%if(isDisStock){ %>
       				<span class="span-repture-stock">RUPTURE STOCK</span>
       			<%} %>
	        		<%if(isCtrlStock){ %>
		       			<span class="span-stock"><%=StringUtil.getValueOrEmpty(mapStock.get(artP.getId())) %></span>
		       		<%} %>
        		</span>
			</std:link>
		<%} 
		else if(elementP instanceof MenuCompositionDetailPersistant){
			String params = "det="+((MenuCompositionDetailPersistant)elementP).getId()+"&tp=MC&mnu="+cle;
			MenuCompositionDetailPersistant mnuCompo = (MenuCompositionDetailPersistant)elementP;
			String prix = BigDecimalUtil.isZero(mnuCompo.getPrix()) ? "":BigDecimalUtil.formatNumber(mnuCompo.getPrix());
			
			if(mnuCompo.getOpc_article() != null){
				ArticlePersistant artP = mnuCompo.getOpc_article();
				if(BooleanUtil.isTrue(artP.getIs_disable())){
					continue;
				}
				boolean isDisStock = (isCtrlStock && mapStock.get(artP.getId())!=null && mapStock.get(artP.getId()).startsWith("*-*"));
				String libelle = artP.getLibelle().replaceAll("\\#", "");
				%>
				<std:link style='<%=CaisseWebBaseAction.GET_STYLE_CONF("BUTTON_ARTICLE", "COULEUR_TEXT_DETAIL")%>' params='<%=params+(isDisStock?"&disStck=1":"") %>' action="caisse-web.caisseWeb.addArticleMenuCmd" targetDiv="left-div" workId="<%=artP.getId().toString() %>" classStyle="caisse-btn detail-article-btn" value="">
					<span class="span-img-stl">
						<img alt="" src="resourcesCtrl?elmnt=<%=EncryptionUtil.encrypt(artP.getId().toString()) %>&path=article&rdm=<%=(artP.getDate_maj()!=null?artP.getDate_maj().getTime():"1")%>" class="<%=StringUtil.isNotEmpty(prix) ? "img-caisse-det-stl":"img-caisse-stl" %>">
					</span>	
	        		<span class="span-libelle-stl"><i class="fa fa-tag" style='color:red;'></i><%=libelle %>&nbsp;&nbsp;</span>
	        		<%if(isDisStock){ %>
       					<span class="span-repture-stock">RUPTURE STOCK</span>
       				<%} %>
	        		<%if(StringUtil.isNotEmpty(prix)){ %>
	        			<span class="span-prix-stl">[<%=prix %>]
	        				<%if(isCtrlStock){ %>
				       			<span class="span-stock"><%=StringUtil.getValueOrEmpty(mapStock.get(artP.getId())) %></span>
				       		<%} %>
	        			</span>
	        		<%} %>	
				</std:link>
			<%	} else if(((MenuCompositionDetailPersistant)elementP).getOpc_famille() != null){
					FamillePersistant famP = ((MenuCompositionDetailPersistant)elementP).getOpc_famille();
					if(BooleanUtil.isTrue(famP.getIs_disable())){
						continue;
					}
					String libelle = famP.getLibelle().replaceAll("\\#", "");
					%>
					<std:link style='<%=CaisseWebBaseAction.GET_STYLE_CONF("BUTTON_SOUS_FAMILLE", "COULEUR_TEXT_DETAIL")%>' action="caisse-web.caisseWeb.menuCompoDetailEvent" targetDiv="menu-detail-div" workId="<%=((MenuCompositionDetailPersistant)elementP).getId().toString() %>" classStyle="caisse-btn detail-famille-btn" value="">
						<span class="span-img-stl">
							<img alt="" src="resourcesCtrl?elmnt=<%=EncryptionUtil.encrypt(famP.getId().toString()) %>&path=famille&rdm=<%=(famP.getDate_maj()!=null?famP.getDate_maj().getTime():"1")%>" class="<%=StringUtil.isNotEmpty(prix) ? "img-caisse-det-stl":"img-caisse-stl" %>">
						</span>	
		        		<span class="span-libelle-stl"><%=libelle %>&nbsp;&nbsp;</span>
		        		<%if(StringUtil.isNotEmpty(prix)){ %>
		        			<span class="span-prix-stl">[<%=prix %>]</span>
		        		<%} %>
					</std:link>
			<%	} else if(((MenuCompositionDetailPersistant)elementP).getOpc_list_choix() != null){
				ListChoixPersistant listChoix = ((MenuCompositionDetailPersistant)elementP).getOpc_list_choix();
				if(BooleanUtil.isTrue(listChoix.getIs_disable())){
					continue;
				}
				String libelle = listChoix.getLibelle().replaceAll("\\#", "");
				%>
					<std:link style='<%=CaisseWebBaseAction.GET_STYLE_CONF("BUTTON_LIST_CHOIX", "COULEUR_TEXT_DETAIL")%>' action="caisse-web.caisseWeb.menuCompoDetailEvent" targetDiv="menu-detail-div" workId="<%=((MenuCompositionDetailPersistant)elementP).getId().toString() %>" classStyle="caisse-btn detail-choix-btn" value="">
		        		<span class="span-img-stl">&nbsp;</span>
		        		<span class="span-libelle-stl"><%=libelle %>&nbsp;&nbsp;</span>
		        		<%if(StringUtil.isNotEmpty(prix)){ %>
		        			<span class="span-prix-stl">[<%=prix %>]</span>
		        		<%} %>	
					</std:link>
		<%		}
		} 
		else if(elementP instanceof ListChoixDetailPersistant){
			String params = "det="+((ListChoixDetailPersistant)elementP).getId()+"&tp=LC&mnu="+cle;
			
			if(((ListChoixDetailPersistant)elementP).getOpc_article() != null){
				ArticlePersistant articleP = ((ListChoixDetailPersistant)elementP).getOpc_article();
				if(BooleanUtil.isTrue(articleP.getIs_disable())){
					continue;
				}
				boolean isDisStock = (isCtrlStock && mapStock.get(articleP.getId())!=null && mapStock.get(articleP.getId()).startsWith("*-*"));
				String libStck = ((isCtrlStock && mapStock != null && mapStock.get(articleP.getId())!=null) ? mapStock.get(articleP.getId()).replaceAll("\\*-\\*", "") : "");
				
				String libelle = articleP.getLibelle().replaceAll("\\#", "");
				%>
				<std:link style='<%=CaisseWebBaseAction.GET_STYLE_CONF("BUTTON_ARTICLE", "COULEUR_TEXT_DETAIL")%>' params='<%=params+(isDisStock?"&disStck=1":"") %>' action="caisse-web.caisseWeb.addArticleMenuCmd" targetDiv="left-div" workId="<%=articleP.getId().toString() %>" classStyle="caisse-btn detail-article-btn" value="">
					<span class="span-img-stl">
						<img alt="" src="resourcesCtrl?elmnt=<%=EncryptionUtil.encrypt(articleP.getId().toString()) %>&path=article&rdm=<%=(articleP.getDate_maj()!=null?articleP.getDate_maj().getTime():"1")%>" class="img-caisse-stl">
					</span>	
	        		<span class="span-libelle-stl"><i class="fa fa-tag" style='color:red;'></i><%=libelle %>&nbsp;&nbsp;
	        		<%if(isDisStock){ %>
       					<span class="span-repture-stock">RUPTURE STOCK</span>
       				<%} %>
		        		<%if(isCtrlStock){ %>
			       			<span class="span-stock"><%=StringUtil.getValueOrEmpty(libStck) %></span>
			       		<%} %>
	        		</span>
				</std:link>
		<%	} else if(((ListChoixDetailPersistant)elementP).getOpc_famille() != null){
				FamillePersistant familleP = ((ListChoixDetailPersistant)elementP).getOpc_famille();
				if(BooleanUtil.isTrue(familleP.getIs_disable())){
					continue;
				}
				String libelle = familleP.getLibelle().replaceAll("\\#", "");
				%>
				<std:link style='<%=CaisseWebBaseAction.GET_STYLE_CONF("BUTTON_SOUS_FAMILLE", "COULEUR_TEXT_DETAIL")%>' action="caisse-web.caisseWeb.menuCompoChoixEvent" targetDiv="menu-detail-div" workId="<%=((ListChoixDetailPersistant)elementP).getId().toString() %>" classStyle="caisse-btn detail-famille-btn" value="">
					<span class="span-img-stl">
						<img alt="" src="resourcesCtrl?elmnt=<%=EncryptionUtil.encrypt(familleP.getId().toString()) %>&path=famille&rdm=<%=(familleP.getDate_maj()!=null?familleP.getDate_maj().getTime():"1")%>" class="img-caisse-stl">
					</span>	
	        		<span class="span-libelle-stl"><%=libelle %>&nbsp;&nbsp;</span>
				</std:link>
		<%	} else if(((ListChoixDetailPersistant)elementP).getOpc_list_choix() != null){
				 ListChoixPersistant lisChoixP = ((ListChoixDetailPersistant)elementP).getOpc_list_choix();
				 if(BooleanUtil.isTrue(lisChoixP.getIs_disable())){
					continue;
				 }
				 String libelle = lisChoixP.getLibelle().replaceAll("\\#", "");
				 %>
				 <std:link style='<%=CaisseWebBaseAction.GET_STYLE_CONF("BUTTON_LIST_CHOIX", "COULEUR_TEXT_DETAIL")%>' action="caisse-web.caisseWeb.menuCompoChoixEvent" targetDiv="menu-detail-div" workId="<%=((ListChoixDetailPersistant)elementP).getId().toString() %>" classStyle="caisse-btn detail-choix-btn" value="">
	        		<span class="span-img-stl">&nbsp;</span>
	        		<span class="span-libelle-stl"><%=libelle %>&nbsp;&nbsp;</span>
				</std:link>
	  <%	}
		}
	}
}%>

<%if(listSousMenu != null && listSousMenu.size()>0){ %>
<div class="row" id="footer_cmd_div" style="width: 75%;height: 70px;text-align: center;position: fixed;top: 92%;margin-top: 15px;">
 		<div class="btn-group" style='<%=step==0?"display: none;":""%>'>
              <button id="btn-wizard-prev"  type="button" class="btn btn-danger btn-sm btn-prev" targetDiv="menu-detail-div" wact='<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.loadNextStep")%>' params="stepr=<%=step %>" style="height: 44px;width: 190px;font-size: 23px;font-weight: bold;margin-right: 9px;border-radius: 40px;">
              	<i style="font-size: 30px;" class="fa fa-angle-left"></i>Pr&eacute;c&eacute;dent
              </button>
          </div>
          <div class="btn-group" style='<%=step>=listSousMenu.size()-1?"display: none;":""%>'>
              <button id="btn-wizard-next" type="button" class="btn btn-danger btn-sm btn-next" data-last="Finish" targetDiv="menu-detail-div" wact='<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.loadNextStep")%>' params="step=<%=step %>" style="margin-left: 10%;height: 44px;width: 150px;font-weight: bold;font-size: 23px;border-radius: 40px;">
              	Suivant
              	<i style="font-size: 30px;" class="fa fa-angle-right"></i>
              </button>
          </div>
         <%
        // if (step >= listSousMenu.size()-1) { %>
              <button class="btn btn-danger" type="button" onclick="$('#menu-detail-div').html('');" targetDiv="left-div" wact='<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.finaliserMenuCmdStep")%>' params="step=<%=step %>" style="margin-left: 20%;height: 44px;width: 170px;font-weight: bold;font-size: 23px;border-radius: 40px;background-color: #529609 !important;">Terminer</i></button>
       <%//} %>
 		
 	</div>

	 <!--Page Related Scripts-->
    <script src="resources/framework/js/fuelux/wizard/wizard-custom.js"></script>
    <script src="resources/framework/js/toastr/toastr.js?v=1.0"></script>

    <script type="text/javascript">
        jQuery(function ($) {
            $('#WiredWizard').wizard();
        });
    </script>	
<%} %>    