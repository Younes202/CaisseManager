<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@page errorPage="/commun/error.jsp" %>

 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion des travaux</li>
         <li class="active">Travaux</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:link actionGroup="C" classStyle="btn btn-default" action="stock.travaux.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
          |
          <std:linkPopup noJsValidate="true" actionGroup="C" style='margin-left: 10px;' action="admin.dataForm.work_find" icon="fa fa-cogs" tooltip="Données formulaire" value="Formulaire" params="tp=TRAVAUX" />
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
	<!-- Liste des travauxs -->
	<cplx:table name="list_travaux" checkable="false" transitionType="simple" width="100%" title="Liste des travaux" initAction="stock.travaux.work_find" autoHeight="true">
		<cplx:header>
			<cplx:th type="empty" />
			<cplx:th type="string" value="Libellé" field="travaux.libelle"/>
			<cplx:th type="date" value="Date début" field="travaux.date_debut" width="120"/>
			<cplx:th type="date" value="Date fin" field="travaux.date_fin" width="120"/>
			<cplx:th type="string" value="Lieu" field="travaux.lieu" />
			<cplx:th type="string" value="Responsable" field="travaux.opc_responsable.nom" />
			<cplx:th type="string" value="Equipe" field="travaux.employe" filtrable="false" sortable="false"/>
			<cplx:th type="decimal" value="Budget prévu" field="travaux.budget_prevu" width="100"/>
			<cplx:th type="decimal" value="Budget consommé" field="travaux.budget_consomme" width="100"/>
			<cplx:th type="empty" />
		</cplx:header>
		<cplx:body>
			<c:forEach items="${list_travaux }" var="travaux">
				
				<c:if test="${empty oldChantier or travaux.opc_chantier.id != oldChantier}">
			    	 <tr>
						<td style="background-color: #b7e9ff;" colspan="2" noresize="true" class="separator-group">
							<span class="fa fa-fw fa-folder-open-o separator-icon"></span> ${travaux.opc_chantier.libelle }
						</td>
						<td style="background-color: #b7e9ff;color: blue;text-align: center;"><fmt:formatDate value="${travaux.opc_chantier.date_debut}"/></td>
						<td style="background-color: #b7e9ff;color: blue;text-align: center;"><fmt:formatDate value="${travaux.opc_chantier.date_fin}"/></td>
						<td style="background-color: #b7e9ff;color: blue;">${travaux.opc_chantier.lieu }</td>
						<td style="background-color: #b7e9ff;color: blue;">${travaux.opc_chantier.opc_responsable.nom }</td>
						<td style="background-color: #b7e9ff;"></td>
						<td style="background-color: #b7e9ff;text-align: right;font-weight: bold;color: blue;"><fmt:formatDecimal value="${travaux.opc_chantier.budget_prevu}"/></td>
						<td style="background-color: #b7e9ff;text-align: right;font-weight: bold;color: blue;"><fmt:formatDecimal value="${travaux.opc_chantier.budget_consomme}"/></td>
						<td style="background-color: #b7e9ff;"></td>
					</tr>
				</c:if>		
			
				<c:set var="oldChantier" value="${travaux.opc_chantier.id }"></c:set>
				
				<cplx:tr workId="${travaux.id }">
					<cplx:td>
						<work:edit-link />
					</cplx:td>
					<cplx:td value="${travaux.libelle}"></cplx:td>
					<cplx:td value="${travaux.date_debut}" style="text-align:center;"></cplx:td>
					<cplx:td value="${travaux.date_fin}" style="text-align:center;"></cplx:td>
					<cplx:td value="${travaux.lieu}"></cplx:td>
					<cplx:td value="${travaux.opc_responsable.nom} ${travaux.opc_responsable.prenom}"></cplx:td>
					<cplx:td>
						${employesStr }
					</cplx:td>
					<cplx:td value="${travaux.budget_prevu}" style="text-align:right;"></cplx:td>
					<cplx:td value="${travaux.budget_consomme}" style="text-align:right;"></cplx:td>
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