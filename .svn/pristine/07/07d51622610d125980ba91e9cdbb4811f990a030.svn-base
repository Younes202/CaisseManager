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
         <li class="active">Chantier</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:link actionGroup="C" classStyle="btn btn-default" action="stock.travauxChantier.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
          |
          <std:linkPopup noJsValidate="true" actionGroup="C" style='margin-left: 10px;' action="admin.dataForm.work_find" icon="fa fa-cogs" tooltip="Données formulaire" value="Formulaire" params="tp=CHANTIER" />
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
	<cplx:table name="list_travauxChantier" checkable="false" transitionType="simple" width="100%" title="Liste des chantiers" initAction="stock.travauxChantier.work_find" autoHeight="true">
		<cplx:header>
			<cplx:th type="empty" />
			<cplx:th type="string" value="Libellé" field="travauxChantier.libelle"/>
			<cplx:th type="date" value="Date début" field="travauxChantier.date_debut" width="120"/>
			<cplx:th type="date" value="Date fin" field="travauxChantier.date_fin" width="120"/>
			<cplx:th type="string" value="Responsable" field="travauxChantier.opc_responsable.nom"/>
			<cplx:th type="string" value="Lieu" field="travauxChantier.lieu"/>
			<cplx:th type="decimal" value="Budget prévu" field="travauxChantier.budget_prevu" width="100"/>
			<cplx:th type="decimal" value="Budget consommé" field="travauxChantier.budget_consomme" width="100"/>
			<cplx:th type="empty" />
		</cplx:header>
		<cplx:body>
			<c:forEach items="${list_travauxChantier }" var="travauxChantier">
				<cplx:tr workId="${travauxChantier.id }">
					<cplx:td>
						<work:edit-link />
					</cplx:td>
					<cplx:td value="${travauxChantier.libelle}"></cplx:td>
					<cplx:td value="${travauxChantier.date_debut}" style="text-align:center;"></cplx:td>
					<cplx:td value="${travauxChantier.date_fin}" style="text-align:center;"></cplx:td>
					<cplx:td value="${travauxChantier.opc_responsable.nom} ${travauxChantier.opc_responsable.prenom}"></cplx:td>
					<cplx:td value="${travauxChantier.lieu}"></cplx:td>
					<cplx:td value="${travauxChantier.budget_prevu}" style="text-align:right;"></cplx:td>
					<cplx:td value="${travauxChantier.budget_consomme}" style="text-align:right;"></cplx:td>
					<cplx:td align="center">
						<work:delete-link />
					</cplx:td>
				</cplx:tr>

				<c:if test="${travauxChantier.list_travaux.size() > 0 }">
					<tr>
						<td colspan="9">
							<table style="width: 70%;margin-left: 15%;border: 1px solid gray;">
								<tr>
									<th>Libellé</th>
									<th>Lieu</th>
									<th style="width: 120px;text-align: center;">Budget consommé</th>
									<th style="width: 120px;text-align: center;">Date début</th>
									<th style="width: 120px;text-align: center;">Date fin</th>
								</tr>	
								<c:set var="ttlBudget" value="${0 }" />
								<c:forEach items="${travauxChantier.list_travaux }" var="trv">
									<tr>
										<td><std:link classStyle="" action="stock.travaux.work_edit" workId="${trv.id }" value="${trv.libelle }" /></td>
										<td>${trv.lieu }</td>
										<td style="text-align: right;"><fmt:formatDecimal value="${trv.budget_consomme }"/></td>
										<td style="text-align: center;"><fmt:formatDate value="${trv.date_debut }" /></td>
										<td style="text-align: center;"><fmt:formatDate value="${trv.date_fin }"/></td>
									</tr>
									<c:set var="ttlBudget" value="${ttlBudget + trv.budget_consomme }" />
								</c:forEach>
								<tr>
									<td colspan="2"></td>
									<td style="text-align: right;font-weight: bold;"><fmt:formatDecimal value="${ttlBudget }"/></td>
									<td colspan="2"></td>
								</tr>
							</table>
						</td>			
					</tr>	
				</c:if>
			</c:forEach>
		</cplx:body>
	</cplx:table>
 </std:form>
 </div>
					<!-- end widget content -->

				</div>
				<!-- end widget div -->