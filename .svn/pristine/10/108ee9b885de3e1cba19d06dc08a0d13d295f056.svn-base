<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li>
			<i class="fa fa-home"></i> 
			<a href="#">Accueil</a>
		</li>
		<li class="active">Synchroniser données</li>
	</ul>
</div>
<std:form name="search-form"> 
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:linkPopup actionGroup="C" classStyle="btn btn-success" action="stock.centraleSynchro.work_init_create" value="Ajouter" icon="fa-3x fa-plus" tooltip="Ajouter" />
		<std:link actionGroup="C" classStyle="btn btn-info" action="stock.centraleSynchro.synchroEts" icon="fa-3x fa-cogs" value="Synchroniser établissements" />
		<std:link actionGroup="C" classStyle="btn btn-info" action="stock.centraleSynchro.synchroAll" style="color: white;" value="Synchroniser données" icon="fa-wifi" tooltip="Synchroniser avec Etablissement" />
		
		<std:link actionGroup="C" classStyle="btn btn-primary" action="stock.centraleSynchro.loadChiffresEts" style="color: #000000;" value="Chiffres établissements" icon="fa-pie-chart" tooltip="Chiffres établissements" />
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<div class="alert alert-warning fade in">
            <button class="close" data-dismiss="alert">
                x
            </button>
            <i class="fa-fw fa fa-warning"></i>
            <strong style="color: orange;text-align: center;">Attention : Si des éléments existent avec la même clé (code, nom, numéro) alors ils seront mises à jour.<br>
            Il est recommandé d'utiliser la synchnisation pour contrôler la totalité des données synchronisables et donc de désactiver la possibilité de manipulation de ces données
            dans les établissements cibles.</strong>
        </div>
      </div>


	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>

	<!-- row -->
	<div class="row">
		
			<!-- Liste des users -->
			<cplx:table name="list_centraleSynchro" autoHeight="true" checkable="false" transitionType="simple" width="100%" title="Liste des synchronisations" initAction="stock.centraleSynchro.work_find">
				<cplx:header>
					<cplx:th type="string" value="Type opération" field="centraleSynchro.type_opr" width="350"/>
					<cplx:th type="string" value="Etablissement destination" field="centraleSynchro.opc_centrale_ets.nom" width="350"/>
					<cplx:th type="date" value="Date synchronisation" field="centraleSynchro.date_synchro" width="350"/>
					<cplx:th type="empty" width="180" />
				</cplx:header>
				<cplx:body>
					<c:set var="oldType" value="${null }"></c:set>
					
					<c:forEach items="${list_centraleSynchro }" var="sync">
						<cplx:tr workId="${sync.id }">
							<cplx:td style="font-weight:bold;">
								<c:choose>
									<c:when test="${sync.type_opr == 'FAM'}">
									FAMILLE
									</c:when>
									<c:when test="${sync.type_opr == 'LCHOIX'}">
									LISTE DE CHOIX
									</c:when>
									<c:when test="${sync.type_opr == 'ART'}">
									ARTICLE
									</c:when>
									<c:when test="${sync.type_opr == 'MNU'}">
									MENU
									</c:when>
									<c:when test="${sync.type_opr == 'FOURN'}">
									FOURNISSEUR
									</c:when>
									<c:when test="${sync.type_opr == 'TRANSF'}">
									TRANSFERT
									</c:when>
									<c:when test="${sync.type_opr == 'VENTE'}">
									VENTE
									</c:when>
								</c:choose>
							</cplx:td>
							<cplx:td value="${sync.opc_centrale_ets.nom}"></cplx:td>
							<cplx:td align="center" style="color:green;" value="${sync.date_synchro}">
								<c:if test="${sync.date_synchro == null }">
									<span style="color: red;">Non synchronisé</span>
								</c:if>
							</cplx:td>
							<cplx:td align="left">
								<std:link action="stock.centraleSynchro.synchroAll" workId="${sync.id }" value="Synchroniser">
									<i class="fa fa-wifi" style="color: green;"></i>
								</std:link>
								<c:if test="${sync.date_synchro == null }">
									<work:delete-link />
								</c:if>
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