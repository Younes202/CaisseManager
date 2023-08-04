<%@page import="appli.controller.domaine.caisse.action.CaisseWebBaseAction"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="appli.model.domaine.stock.persistant.ArticlePersistant"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.model.domaine.personnel.persistant.EmployePersistant"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="java.io.File"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.controller.bean.PagerBean"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="appli.model.domaine.stock.persistant.FamillePersistant"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>

<%
List<ArticlePersistant> listArticleFavoris = (List<ArticlePersistant>)request.getAttribute("listArticleFavoris");
	if(listArticleFavoris != null && listArticleFavoris.size()>0){%>
	<div style="float: left;width: 100%;overflow: hidden;">
	<%
		for(ArticlePersistant articleP : listArticleFavoris){
			String params = "is_fav=1";
			String prix = BigDecimalUtil.formatNumber(articleP.getPrix_vente());
			%>
			<std:link style='<%=CaisseWebBaseAction.GET_STYLE_CONF("BUTTON_ARTICLE", "COULEUR_TEXT_DETAIL")%>' action="caisse-web.caisseWeb.addArticleFamilleCmd" params='<%=params%>' targetDiv="left-div" workId="<%=articleP.getId().toString()%>" classStyle="caisse-btn detail-article-btn" value="">
         		<span class="span-img-stl">
         			<img alt="" src="resourcesCtrl?elmnt=<%=EncryptionUtil.encrypt(articleP.getId().toString()) %>&path=article&rdm=<%=(articleP.getDate_maj()!=null?articleP.getDate_maj().getTime():"") %>" class="img-caisse-stl">
         		</span>	
         		<span class="span-libelle-stl"><i class="fa fa-tag" style='color:red;'></i><%=articleP.getLibelle() %>&nbsp;&nbsp;</span>
         		<span class="span-prix-stl">[<%=prix %>]</span>
         	</std:link>
	<%}%>
	</div>
<%}%>

<%request.setAttribute("TP_P", "FAV"); %> 
<jsp:include page="/domaine/caisse/normal/pagger_include.jsp" />
