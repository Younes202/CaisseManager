<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="appli.controller.domaine.caisse.action.CaisseWebBaseAction"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.util.FileUtil"%>
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
List<FamillePersistant> listSousFamille = (List<FamillePersistant>)request.getAttribute("listFamille");
List<ArticlePersistant> listArticle = (List<ArticlePersistant>)request.getAttribute("listArticle");

IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);

if(StringUtil.isFalseOrNull(""+ControllerUtil.getMenuAttribute("IS_FROM_ART", request))){
// Pager
int nbrTotal = (request.getAttribute("nbrTotal")!=null?(Integer)request.getAttribute("nbrTotal"):0);
int page_count = (int) Math.ceil((double) nbrTotal / CaisseWebBaseAction.NBR_ELEMENTS);// Calculate 

String currPageSt = (String)ControllerUtil.getUserAttribute("sidx", request);
int currPage = Integer.valueOf(currPageSt==null?"0":currPageSt);

int previousPage = currPage==0 ? 0 : currPage-1;
int nextPage = (currPage+1)>=page_count ? currPage : currPage+1;
int currNbrElmnt = (currPage+1)*CaisseWebBaseAction.NBR_ELEMENTS; 
currNbrElmnt = (currNbrElmnt > nbrTotal) ? nbrTotal : currNbrElmnt;

String stlPrev = (currPage==0 ? "background:gray !important;":"");
String stlNext = ((currPage+1)>=page_count ? "background:gray !important;":"");

if (page_count >= 1){
	String fam = "&fam="+request.getAttribute("currFamId");
	String prev = "sidx="+previousPage + fam;
	String next = "sidx="+nextPage + fam;
%>
<std:link classStyle="btn btn-default btn-sm btn-circle shiny" targetDiv="menu-detail-div" style='<%="position: absolute;top: 410px;right: 1%;"+stlNext %>' action='<%=(currPage+1)>=page_count?"":"caisse-web.balance.familleEvent"%>' params="<%=next %>" icon="fa fa-arrow-circle-o-right" />
<span style="color: black;position: absolute;top: 470px;right: 1%;font-weight: bold;"><%=currNbrElmnt%>/<%=nbrTotal%></span>
<std:link classStyle="btn btn-default btn-sm btn-circle shiny" targetDiv="menu-detail-div" style='<%="position: absolute;top: 500px;right: 1%;"+stlPrev %>' action='<%=currPage==0?"":"caisse-web.balance.familleEvent" %>' params="<%=prev %>" icon="fa fa-arrow-circle-o-left" />

<%}
}

if(listSousFamille != null){
	for(FamillePersistant familleP : listSousFamille){
		String libelle = familleP.getLibelle().replaceAll("\\#", "");
		%>
		<std:link style='<%=CaisseWebBaseAction.GET_STYLE_CONF("BUTTON_SOUS_FAMILLE", "COULEUR_TEXT_DETAIL")%>' action="caisse-web.balance.familleEvent" targetDiv="menu-detail-div" workId="<%=familleP.getId().toString() %>" classStyle="caisse-btn detail-famille-btn" value="">
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
		String prix = BigDecimalUtil.formatNumber(articleP.getPrix_vente());
		%>
		<std:linkPopup style='<%=CaisseWebBaseAction.GET_STYLE_CONF("BUTTON_ARTICLE", "COULEUR_TEXT_DETAIL")%>' action="caisse-web.balance.addArticleFamilleCmd" workId="<%=articleP.getId().toString() %>" classStyle="caisse-btn detail-article-btn" value="">
			<span class="span-img-stl">
				<img alt="" src="resourcesCtrl?elmnt=<%=EncryptionUtil.encrypt(articleP.getId().toString()) %>&path=article&rdm=<%=(articleP.getDate_maj()!=null?articleP.getDate_maj().getTime():"1")%>" class="img-caisse-det-stl">
			</span>	
       		<span class="span-libelle-stl"><i class="fa fa-tag" style='color:red;'></i><%=articleP.getLibelle() %>&nbsp;&nbsp;</span>
       		<span class="span-prix-stl">[<%=prix %>]</span>
		</std:linkPopup>
	<%}
}%>
