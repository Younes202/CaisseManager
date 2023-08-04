"http://www.customtaglib.com/c"<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="appli.model.domaine.stock.persistant.ArticlePersistant"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<%
ArticlePersistant articleP = (ArticlePersistant)request.getAttribute("articlePrd");
%>

<%if(articleP == null){ %>
		Aucun article trouv&eacute;.
<%} else{
	IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
	String stl = null;
	Map<String, byte[]> dataimg = service.getDataImage(articleP.getId(), "article");
	if(dataimg.size() > 0){
		stl = "data:image/jpeg;base64,"+FileUtil.getByte64(dataimg.entrySet().iterator().next().getValue());
	}
%>

<%if(stl != null){ %>
<div class="col-md-4">
	<img src="<%=stl %>" alt="" style="height: 450px;">
</div>
<%} %>
<div class="col-md-<%=(stl==null?"12":"7") %>" style="<%=(stl!=null?"margin-left: 20px;":"") %>">
    <div style="width: 100%;text-align: center;">	
    	<h1 style="font-size: 40px;font-weight: bold !important;color:#4caf50;"><%=articleP.getLibelle() %></h1>
     	<h2 style="color: #3e4340;font-size: 40px;"><%=BigDecimalUtil.formatNumber(articleP.getPrix_vente()) %> Dhs</h2> 
     </div>	
     <br>
     <div style="width: 100%;">
     	<%=StringUtil.getValueOrEmpty(articleP.getDescription()) %> 
     </div>
</div>
<%}%>