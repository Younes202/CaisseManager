<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#generic_modal_body{
	width: 920px;
	margin-left: -10%;
}
</style>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Historique des prix</span>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="row">
			        <div class="form-group">
			        	<std:label classStyle="control-label col-md-2" value="Client" />
			            <div class="col-md-4">
			                 <std:select onChange="$('#fourn').val('');" name="cli" type="long" data="${listClient }" key="id" labels="nom;' ';prenom" />
			            </div>
			            <std:label classStyle="control-label col-md-2" value="Fournisseur" />
			            <div class="col-md-2">
			                 <std:select onChange="$('#cli').val('');" name="fourn" type="long" key="id" hiddenkey="id" labels="libelle;' ';marque" data="${listeFournisseur}" groupKey="opc_famille.id" groupLabels="opc_famille.code;'-';opc_famille.libelle" required="true" width="100%" />
			            </div>
			            <div class="col-md-2">
			           	 	<std:button action="stock.article.init_histo_prix" value="Filtrer" classStyle="btn btn-primary" />
			           	 </div>	
			       </div>
			   </div>
			
			
				<div class="row">
					
	<!-- Liste des prix -->
	<cplx:table name="list_prix_fournisseur" checkable="false" transitionType="simple" width="100%" title="Historique des prix" initAction="stock.article.init_histo_prix" autoHeight="true">
		<cplx:header>
			<cplx:th type="date" value="Date" field="mouvementArticle.opc_mouvement.date_mouvement" width="100"/>
			<cplx:th type="decimal" value="Prix" field="mouvementArticle.prix_ht"/>
			<cplx:th type="decimal" value="Prix" field="mouvementArticle.prix_ttc"/>
			
			<c:if test="${src == 'client' }">
				<cplx:th type="long[]" value="Client" field="mouvementArticle.opc_mouvement.opc_client.id" groupValues="${lisClient }" groupKey="id" groupLabel="nom"/>
			</c:if>
			<c:if test="${src == 'fourn' }">
				<cplx:th type="long[]" value="Fournisseur" field="mouvementArticle.opc_mouvement.opc_fournisseur.id" groupValues="${lisClient }" groupKey="id" groupLabel="libelle"/>
			</c:if>
		</cplx:header>
		<cplx:body>
			<c:forEach items="${list_prixHisto }" var="mvmDet">
				<cplx:tr workId="${prixFourn.id }">
					<cplx:td align="center" value="${mvmDet.opc_mouvement.date_mouvement}"></cplx:td>
					<cplx:td align="right" value="${mvmDet.prix_ht}"></cplx:td>
					<cplx:td align="right" value="${mvmDet.prix_ttc}"></cplx:td>
					
					<c:if test="${src == 'client' }">
						<cplx:td align="right">
							${mvmDet.opc_mouvement.opc_client.nom} ${mvmDet.opc_mouvement.opc_client.libelle}
						</cplx:td>
					</c:if>
					<c:if test="${src == 'fourn' }">
						<cplx:td align="right">
							${mvmDet.opc_mouvement.opc_fournisseur.nom} ${mvmDet.opc_mouvement.opc_fournisseur.libelle}
						</cplx:td>
					</c:if>	
				</cplx:tr>
			</c:forEach>
		</cplx:body>
	</cplx:table>

 </div>
 </div>

 </div>
 </div>
  </std:form>