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
         <li>Comptabilit&eacute;</li>
         <li class="active">Op&eacute;rations diverses</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:link actionGroup="C" classStyle="btn btn-default" action="compta.ecriture.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
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
	<!-- Liste des ecritures -->
	<cplx:table name="list_ecriture" transitionType="simple" width="100%" title="Op&eacute;ration diverses" checkable="false" initAction="compta.ecriture.work_find">
		<cplx:header>
			<cplx:th type="empty" />
			<cplx:th type="date" value="Date" field="ecriture.date" width="120"/>
			<cplx:th type="string" value="Libell&eacute;" field="ecriture.libelle"/>
			<cplx:th type="boolean" value="Comptabilis&eacute;" field="ecriture.is_compta" width="90"/>
			<cplx:th type="decimal" value="Montant" field="ecriture.montant" width="120"/>
			<cplx:th type="empty" />
		</cplx:header>
		<cplx:body>
			<c:forEach items="${listEcriture }" var="ecriture">
				<cplx:tr workId="${ecriture.id }">
					<cplx:td>
						<work:edit-link />
					</cplx:td>
					<cplx:td align="center" value="${ecriture.date_mouvement}"></cplx:td>
					<cplx:td value="${ecriture.libelle}"></cplx:td>
					<cplx:td align="center" value="${ecriture.is_compta}"></cplx:td>
					<cplx:td style="font-weight:bold;" value="${ecriture.montant}" align="right"></cplx:td>
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

