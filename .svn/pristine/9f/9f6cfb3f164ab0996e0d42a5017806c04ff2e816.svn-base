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

<c:set var="encryptionUtil" value="<%=new EncryptionUtil() %>" />

<div style="float: left;width: 100%;overflow: hidden;">
 <c:forEach var="menu" items="${listMenu }">
       	<std:link style='${caisseWeb.GET_STYLE_CONF("BUTTON_MENU", "COULEUR_TEXT_MENU")};' action="caisse-web.caisseWeb.loadMenu" targetDiv="menu-detail-div" workId="${menu.id }" classStyle="caisse-top-btn menu-btn" value="">
       		<span class="span-img-stl">
       			<img alt="" src="resourcesCtrl?elmnt=${encryptionUtil.encrypt(menu.getId().toString())}&path=menu&rdm=${menu.date_maj.getTime()}" class="img-caisse-stl">
       		</span>
       		<span class="span-libelle-stl">${menu.libelle }</span>
       	</std:link>
</c:forEach>
 </div>

<%request.setAttribute("TP_P", "MNU"); %> 
 <jsp:include page="/domaine/caisse/normal/pagger_include.jsp" />