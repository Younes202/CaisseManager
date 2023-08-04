<%@page import="java.util.Date"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Livraison</li>
         <li>Sociétés de livraison</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
<std:form name="search-form">
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:link actionGroup="C" classStyle="btn btn-default" style="float:left;" action="pers.societeLivr.work_init_create" icon="fa-3x fa-plus" tooltip="Créer" />
          |
          <std:linkPopup noJsValidate="true" actionGroup="C" style='margin-left: 10px;' action="admin.dataForm.work_find" icon="fa fa-cogs" tooltip="Données formulaire" value="Formulaire" params="tp=EMPLOYE" />
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
		
		<!-- Liste des societeLivrs -->
		<cplx:table name="list_societeLivr" checkable="false" showDataState="true" transitionType="simple" width="100%" title="Sociétés de livraison" initAction="pers.societeLivr.work_find">
			<cplx:header>
				<cplx:th type="empty" />
				<cplx:th type="string" value="Numéro" field="societeLivr.numero" width="120" />
				<cplx:th type="string" value="Nom" field="societeLivr.nom" />
				<cplx:th type="decimal" value="Taux marge" field="societeLivr.taux_marge" width="120" />
				<cplx:th type="string" value="Adresse" field="societeLivr.opc_adresse" sortable="false" filtrable="false" />
				<cplx:th type="empty" />
			</cplx:header>
			<cplx:body>
					<c:set var="currDate" value="<%=new Date() %>"/>
					
				<c:forEach items="${list_societeLivr }" var="societeLivr">
					<cplx:tr workId="${societeLivr.id }" style="${is_disable?'text-decoration: line-through;':'' }">
						<cplx:td>
							<work:edit-link />
						</cplx:td>
						<cplx:td value="${societeLivr.numero}"></cplx:td>
						<cplx:td value="${societeLivr.nom}"></cplx:td>
						<cplx:td align="right" style="font-weight:bold;" value="${societeLivr.taux_marge }">%</cplx:td>
						<cplx:td value="${societeLivr.getAdressFull()}"></cplx:td>
						<cplx:td align="center">
							<work:delete-link />
						</cplx:td>
					</cplx:tr>
				</c:forEach>
			</cplx:body>
		</cplx:table>
	</div>
	<!-- end widget content -->

</div>
<!-- end widget div -->
</std:form>
