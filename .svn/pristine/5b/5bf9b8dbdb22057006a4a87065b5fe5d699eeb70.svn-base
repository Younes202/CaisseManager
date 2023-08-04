<%@page import="java.util.List"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="appli.model.domaine.stock.persistant.ArticlePersistant"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fn" prefix="fn"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@page errorPage="/commun/error.jsp" %>

<%
List<ArticlePersistant> list_article = (List<ArticlePersistant>) request.getAttribute("list_article");
Map<ArticlePersistant, BigDecimal[]> mapData = (Map<ArticlePersistant, BigDecimal[]>) request.getAttribute("mapData");
%>

 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion de stock</li>
         <li>Fiche des marges vente</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
 
 <std:form name="search-form">
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
      </div>
      <!--Header Buttons-->
      <jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
      <!--Header Buttons End-->
  </div>
  <!-- /Page Header -->

<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
	<!-- row -->
	<div class="row">
	<!-- Liste des articles -->
	<cplx:table name="list_article" transitionType="simple" width="100%" title="Liste de marges des ventes" initAction="caisse.caisse.find_marge_vente" autoHeight="true" checkable="false">
		<cplx:header>
			<cplx:th type="long[]" value="Famille" field="article.opc_famille_stock.id" groupValues="${listeFaimlle }" groupKey="id" groupLabel="libelle" width="0" filterOnly="true"/><!-- Filter only -->
			<cplx:th type="long[]" value="Article" field="article.id" groupValues="${list_article }" groupKey="id" groupLabel="libelle"/>
			<cplx:th type="decimal" value="Prix achat TTC" field="article.prix_achat_ttc" width="100"/>
			<cplx:th type="decimal" value="Prix vente TTC" field="article.prix_vente_ttc" width="100"/>
			<cplx:th type="decimal" value="Marge" filtrable="false" sortable="false" width="100"/>
			<cplx:th type="decimal" value="Pourcentage" filtrable="false" sortable="false" width="100"/>
		</cplx:header>
		<cplx:body>
			<c:set var="oldfam" value="${null }"></c:set>
			<c:set var="bigDecimalUtil" value="<%=new BigDecimalUtil() %>" />
		
			<%
			for(ArticlePersistant articleP : list_article){ 
				BigDecimal[] values = mapData.get(articleP);
				if(values != null){
			%>
				<c:set var="article" value="<%=articleP %>" />
				<c:if test="${article.familleStr.size() > 0}">
					<c:forEach var="i" begin="0" end="${article.familleStr.size()-1}">
						<c:if test="${empty oldfam or i>(oldfam.size()-1) or article.familleStr.get(i).code != oldfam.get(i).code}">
						     <tr>
								<td colspan="5" noresize="true" style="font-size: 13px;font-weight: bold;color:green;padding-left: ${article.familleStr.get(i).level*10}px;background-color:#fcffdc;">${article.familleStr.get(i).code}-${article.familleStr.get(i).libelle}</td>
							</tr>
						</c:if>		
					</c:forEach>
				</c:if>
				<c:set var="oldfam" value="${article.familleStr }"></c:set>
				
				<cplx:tr>
					<cplx:td value="<%=articleP.getLibelle() %>"></cplx:td>
					<cplx:td align="right" value="<%=values[0] %>"></cplx:td>
					<cplx:td align="right" value="<%=values[1] %>"></cplx:td>
					
					<c:set var="stl" value='<%=values[2].compareTo(BigDecimalUtil.ZERO) == 1 ?"green":"red" %>' />
					
					<cplx:td align="right" style="font-weight: bold;color:${stl } " value="<%=values[2] %>"></cplx:td>
					<cplx:td align="right" style="font-weight: bold;color:${stl }" value='<%= BigDecimalUtil.formatNumberZero(values[3])+" %" %>'></cplx:td>
				</cplx:tr>
				<%}
			}%>
		
			
		</cplx:body>
	</cplx:table>
  </div>
					<!-- end widget content -->

				</div>
				<!-- end widget div -->
</std:form>	