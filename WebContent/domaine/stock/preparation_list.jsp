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
         <li>Gestion de stock</li>
         <li>Fiche des preparations</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:link actionGroup="C" classStyle="btn btn-default" action="stock.preparation.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
          <std:link actionGroup="C" classStyle="btn btn-warning" action="stock.mouvement.work_find" icon="fa-mail-reply-all" tooltip="Retour vers transformations" value="Retour" />
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

<std:form name="search-form">
	<!-- Liste des preparations -->
	<cplx:table name="list_preparation" transitionType="simple" width="100%" titleKey="preparation.list" initAction="stock.preparation.work_find">
		<cplx:header>
			<cplx:th type="empty" />
			<cplx:th type="string" valueKey="preparation.code" field="preparation.code" width="250"/>
			<cplx:th type="string" valueKey="preparation.libelle" field="preparation.libelle"/>
			<cplx:th type="decimal" value="Valeur HT" field="preparation.montant_ht" width="150"/>
			<cplx:th type="decimal" value="Valeur TTC" field="preparation.montant_ttc" width="150"/>
			<cplx:th type="empty" />
		</cplx:header>
		<cplx:body>
			<c:forEach items="${list_preparation }" var="preparation">
				<cplx:tr workId="${preparation.id }">
					<cplx:td>
						<work:edit-link/>
					</cplx:td>
					<cplx:td value="${preparation.code}"></cplx:td>
					<cplx:td value="${preparation.libelle}"></cplx:td>
					<cplx:td value="${preparation.montant_ht}"></cplx:td>
					<cplx:td value="${preparation.montant_ttc}"></cplx:td>
					<cplx:td align="center">
						<work:delete-link />
					</cplx:td>
				</cplx:tr>
			</c:forEach>
		</cplx:body>
	</cplx:table>
 </std:form>			

 </div>
					<!-- end widget content -->

				</div>
				<!-- end widget div -->
