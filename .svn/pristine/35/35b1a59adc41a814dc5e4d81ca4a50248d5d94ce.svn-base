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
         <li>Caisse</li>
         <li class="active">Liste des configurations</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:link actionGroup="C" classStyle="btn btn-default" action="caisse.ticketCaisseConf.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
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
			<cplx:table name="list_ticketCaisseConf" checkable="false" transitionType="simple" width="100%" title="Liste des configurations" initAction="caisse.ticketCaisseConf.work_find" autoHeight="true">
				<cplx:header>
					<cplx:th type="empty" />
					<cplx:th type="string" value="Libell&eacute;" field="ticketCaisseConf.libelle" />
<%-- 					<cplx:th type="string" value="Type" field="ticketCaisseConf.opc_enum_type.libelle" /> --%>
					<cplx:th type="empty" />
				</cplx:header>
				<cplx:body>
					<c:forEach items="${list_ticketCaisseConf }" var="ticketCaisseConf">
						<cplx:tr workId="${ticketCaisseConf.id }">
							<cplx:td>
								<work:edit-link />
							</cplx:td>
							<cplx:td value="${ticketCaisseConf.libelle}"></cplx:td>
<%-- 							<cplx:td value="${ticketCaisseConf.opc_enum_type.libelle}"></cplx:td> --%>
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
