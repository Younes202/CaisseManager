<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
Map<String, BigDecimal[]> mapArticle = (Map<String, BigDecimal[]>)request.getAttribute("mapArticle");
%>

<!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Stock</li>
         <li>Marge articles
		</li>
         <li class="active">Edition</li>
     </ul>
 </div>
 
  <div class="page-header position-relative">
		<div class="header-title" style="padding-top: 4px;">
			<std:link classStyle="btn btn-default" action="stock.composant.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
      	</div>
      <!--Header Buttons-->
      <jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
      <!--Header Buttons End-->
  </div>
  <!-- /Page Header -->

<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
	<!-- widget grid -->

<std:form name="data-form">	
	<div class="widget">
      	<div class="widget-header bordered-bottom bordered-blue">
	      	<span class="widget-caption">Marge articles</span>
     	</div>
         <div class="widget-body">
			<div class="row">
				<table class="table table-hover" style="width: 80%;margin-left: 10%;font-size: 11px;">
                     <thead class="bordered-darkorange">
                         <tr>
                             <th>Article</th>
                             <th>Prix vente</th>
                             <th>Prix achat</th>
                             <th>Marge</th>
                             <th>Porcentage</th>
                         </tr>
                     </thead>
                     <tbody>
                     <%for(String art : mapArticle.keySet()){ %>
                     	<%
                     	String style = mapArticle.get(art)[3].compareTo(BigDecimalUtil.ZERO) < 0 ? "color:red;" : "";
                     	%>
                         <tr>
                             <td><%=art %></td>
                             <td><%=BigDecimalUtil.formatNumber(mapArticle.get(art)[0]) %></td>
                             <td style="color: blue;"><%=BigDecimalUtil.formatNumber(mapArticle.get(art)[1]) %></td>
                             <td style="<%=style%>"><%=BigDecimalUtil.formatNumber(mapArticle.get(art)[2]) %></td>
                             <td style="<%=style%>"><%=BigDecimalUtil.formatNumber(mapArticle.get(art)[3]) %>%</td>
                         </tr>
                        <%} %> 
                 </tbody>
             </table>
			</div>
		</div>	
		</div>
	</std:form>
</div>

