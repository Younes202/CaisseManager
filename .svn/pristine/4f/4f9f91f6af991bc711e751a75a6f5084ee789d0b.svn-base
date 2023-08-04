<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<style>
#list_etat_detail_article_body tr{
	height: 18px;
}
</style>

 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion de stock</li>
         <li>Etat des stocks</li>
         <li class="active">D&eacute;tail des mouvements article</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
     	<div class="header-title" style="padding-top: 4px;">
			<std:link classStyle="btn btn-default" action="stock.mouvement.etat_article_work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour aux articles" />
			|
			<%if("D".equals(ControllerUtil.getMenuAttribute("mode_det", request))){ %>
				<std:link actionGroup="C" params="mode_det=G" style='font-weight:bold;margin-left: 70px;' noJsValidate="true" classStyle="btn btn-link label label-info graded" value="Vue groupée" action="stock.mouvement.etat_article_detail" icon="fa fa-reorder" tooltip="Vue groupée" />
			<%} else{ %>
				<std:link actionGroup="C" params="mode_det=D" style='font-weight:bold;margin-left: 70px;' noJsValidate="true" classStyle="btn btn-link label label-warning graded" value="Vue détaillée" action="stock.mouvement.etat_article_detail" icon="fa fa-reorder" tooltip="Vue détaillée" />
			<%} %>
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


<c:set var="bigDecimalUtil" value="<%=new BigDecimalUtil() %>" />
<c:set var="dateUtil" value="<%=new DateUtil() %>" />
	<!-- row -->
	<div class="row">
<std:form name="search-form">
	<!-- Liste des mouvements -->
	<cplx:table name="list_etat_detail_article" transitionType="simple" width="100%" forceFilter="true" title="Mouvements article [${articleLib }]" initAction="stock.mouvement.etat_article_detail" autoHeight="true" checkable="false">
		<cplx:header>
			<cplx:th type="string" value="Type" field="mouvementArticle.opc_mouvement.type_mvmnt" groupValues="${typeMvm }"/>
			<cplx:th type="string" value="BL/Facture" field="mouvementArticle.opc_mouvement.num_bl" sortable="false"/>
			<cplx:th type="string" value="Facture" field="mouvementArticle.opc_mouvement.num_facture" filterOnly="true"/>
			<cplx:th type="string" value="Fournisseur" field="mouvementArticle.opc_mouvement.opc_fournisseur.libelle"/>
			<cplx:th type="string" value="Paiement" field="mouvementArticle.opc_mouvement.opc_financement_enum.code" sortable="false" filtrable="false" width="120"/>
			<cplx:th type="long[]" value="Stock" field="mouvementArticle.opc_mouvement.opc_emplacement.id" groupValues="${listEmplacement }" groupKey="id" groupLabel="titre"/>
			<cplx:th type="long[]" value="Destination" field="mouvementArticle.opc_mouvement.opc_destination.id" groupValues="${listEmplacement }" groupKey="id" groupLabel="titre"/>
			<cplx:th type="decimal" value="Quantit&eacute;" field="mouvementArticle.quantite" width="150"/>
		</cplx:header>
		<cplx:body>
			<c:set var="oldDate" value="${null }"></c:set>
			
			<c:forEach items="${listMvmArt }" var="mouvementArticle">
				<c:if test="${oldDate == null  or oldDate != mouvementArticle.opc_mouvement.date_mouvement }">
					<tr>
						<td colspan="8" noresize="true" style="font-size: 13px;color:green;background-color: #fff9e0;">
							<fmt:formatDate value="${mouvementArticle.opc_mouvement.date_mouvement}"/>
						</td>
					</tr>	
				</c:if>
				<c:set var="oldDate" value="${mouvementArticle.opc_mouvement.date_mouvement }"></c:set>
			
				<cplx:tr workId="${mouvementArticle.opc_mouvement.id }" style="${mouvementArticle.opc_mouvement.type_mvmnt=='i'?'background-color:#fbb29b;':'' }">
					<cplx:td style="font-weight:bold;padding-left:25px;text-transform: uppercase;">
						<c:choose>
							<c:when test="${mouvementArticle.opc_mouvement.type_mvmnt=='v' }">
								Vente
								<c:set var="symbole" value="-" />
							</c:when>
							<c:when test="${mouvementArticle.opc_mouvement.type_mvmnt=='vc' }">
								Vente caisse
								<c:set var="symbole" value="-" /> 
							</c:when>
							<c:when test="${mouvementArticle.opc_mouvement.type_mvmnt=='a' }">
								Achat
								<c:set var="symbole" value="+" />
							</c:when>
							<c:when test="${mouvementArticle.opc_mouvement.type_mvmnt=='av' }">
								Avoir
								<c:set var="symbole" value="-" />
							</c:when>
							<c:when test="${mouvementArticle.opc_mouvement.type_mvmnt=='p' }">
								Perte
								<c:set var="symbole" value="-" />
							</c:when>
							<c:when test="${mouvementArticle.opc_mouvement.type_mvmnt=='rt' }">
								Retour client
								<c:set var="symbole" value="+" />
							</c:when>
							<c:when test="${mouvementArticle.opc_mouvement.type_mvmnt=='c' }">
								Consommation
								<c:set var="symbole" value="-" />
							</c:when>
							<c:when test="${mouvementArticle.opc_mouvement.type_mvmnt=='t' }">
								Transfert
								<c:set var="symbole" value="-" />
							</c:when>
							<c:when test="${mouvementArticle.opc_mouvement.type_mvmnt=='tr' }">
								Transformation : 
								<c:choose>
									<c:when test="${mouvementArticle.opc_mouvement.opc_destination !=null }">
										<c:set var="symbole" value="+" />
									</c:when>
									<c:otherwise>
										<c:set var="symbole" value="-" />
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:when test="${mouvementArticle.opc_mouvement.type_mvmnt=='i' }">
								<i class="fa fa-bullseye"></i><span style="color: blue;font-style: italic;font-weight: bold;"> Inventaire</span>
								<c:set var="symbole" value="x" />
							</c:when>
							<c:otherwise>
								Autre
								<c:set var="symbole" value="x" />
							</c:otherwise>
						</c:choose>
					</cplx:td>
					<cplx:td>
						<c:choose>
							<c:when test="${not empty mouvementArticle.opc_mouvement.num_bl}">
								<c:set var="blFac" value="${mouvementArticle.opc_mouvement.num_bl}"/>
							</c:when>
							<c:otherwise>
								<c:set var="blFac" value="${mouvementArticle.opc_mouvement.num_facture}" />
							</c:otherwise>	
						</c:choose>
					
						${blFac }
					</cplx:td>
					<cplx:td value="${mouvementArticle.opc_mouvement.opc_fournisseur.libelle}"></cplx:td>
					<cplx:td value="${mouvementArticle.opc_mouvement.getPaiementsStr() }"></cplx:td>
					<cplx:td value="${mouvementArticle.opc_mouvement.opc_emplacement.titre}"></cplx:td>
					<cplx:td value="${mouvementArticle.opc_mouvement.opc_destination.titre}"></cplx:td>
					
					<c:set var="unite" value="${mouvementArticle.opc_article.opc_unite_achat_enum.libelle }" />					
					<c:set var="keyMap" value='${dateUtil.dateToString(mouvementArticle.opc_mouvement.date_mouvement, "yyyy-MM-dd")}_${mouvementArticle.opc_mouvement.type_mvmnt }'/>
					<c:set var="qte" value="${empty mapQteTot ? mouvementArticle.quantite : mapQteTot.get(keyMap) }" />
					
					<cplx:td align="right" style="font-weight:bold;">
						<c:choose>
							<c:when test="${symbole=='-' }">
								<span style="color:red;">-<fmt:formatDecimal value="${qte }" /> ${unite }</span>
							</c:when>
							<c:when test="${symbole=='+' }">
								<span style="color:green;">+<fmt:formatDecimal value="${qte }" /> ${unite }</span>
							</c:when>
							<c:otherwise>
								<span style="color:blue;"><fmt:formatDecimal value="${qte }" /> ${unite }</span>
							</c:otherwise>
						</c:choose>
					</cplx:td>
				</cplx:tr>
			</c:forEach>	
		</cplx:body>
	</cplx:table>
 </std:form>			
 
 <html:ajaxExcludBlock>
 <%
 Map<String, BigDecimal> mapStock = (Map<String, BigDecimal>)request.getAttribute("mapStock");
 if(mapStock != null){ %>
 <table style="width: 50%;">
 <%	
 	BigDecimal total = null;
 	for(String titre : mapStock.keySet()){
 %>
 	<tr>
 		<td><span style="font-size: 16px;margin-left: 50%;font-weight: bold;color:#777;"><%=titre%></span></td>
 		<td align="right"><span style="font-size: 16px;margin-left: 50%;font-weight: bold;color:#d73d32;"><%=BigDecimalUtil.formatNumber(mapStock.get(titre)) %> ${unite }</span></td>
 	</tr>
 <%
 total = BigDecimalUtil.add(total, mapStock.get(titre));
 	}%>
 	<tr>
 		<td align="right"><span style="font-size: 16px;margin-left: 50%;font-weight: bold;">TOTAL</span></td>
 		<td align="right" style="border-top: 1px solid #999;"><span style="font-size: 19px;margin-left: 50%;font-weight: bold;"><%=BigDecimalUtil.formatNumber(total) %> ${unite }</span></td>
 	</tr>
 	</table>
 <%} %>
 </html:ajaxExcludBlock>
 </div>
					<!-- end widget content -->

				</div>
				<!-- end widget div -->
