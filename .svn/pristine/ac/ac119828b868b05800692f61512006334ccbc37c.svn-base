<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.fidelite.service.ICarteFideliteClientService"%>
<%@page import="framework.model.common.constante.ActionConstante"%>
<%@page import="framework.model.common.constante.ProjectConstante"%>
<%@page import="appli.controller.domaine.fidelite.action.CarteFideliteClientAction"%>
<%@page import="appli.controller.domaine.fidelite.bean.CarteFideliteClientBean"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
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
			<span class="widget-caption">Affectation carte</span>
		</div>

		<div class="widget-body">
				<div class="row">
					<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
				</div>
				<div class="row">
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Carte" />
						<div class="col-md-4">
							<std:select name="carteFideliteClient.opc_carte_fidelite.id" type="long" data="${liste_carte }" key="id" labels="libelle" required="true" style="width:100%;"/>
						</div>
						<std:label classStyle="control-label col-md-2" value="Client" />
						<div class="col-md-4">
							<std:select name="carteFideliteClient.opc_client.id" type="long" data="${liste_client }" key="id" labels="nom;' ';prenom" required="true" style="width:100%;"/>
						</div>	
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Date d&eacute;but validit&eacute;" />
						<div class="col-md-4">
							<std:date name="carteFideliteClient.date_debut"/>
						</div>
							<std:label classStyle="control-label col-md-2" value="Date fin validit&eacute;" />
						<div class="col-md-4">
							<std:date name="carteFideliteClient.date_fin"/>
						</div>
					</div>
					<div class="form-group">
							<std:label classStyle="control-label col-md-2" value="Code/code barre" />
						<div class="col-md-2">
							<std:text name="carteFideliteClient.code_barre" type="string" maxlength="80"></std:text>
						</div>
						<c:if test="${not empty carteFideliteClient.id }">
							<std:label classStyle="control-label col-md-2" value="Date suspension" />
							<div class="col-md-4">
								<std:date name="carteFideliteClient.date_suspension" readOnly="true"/>
							</div>							
						</c:if>
							
						</div>
					</div>
					<div class="row" style="text-align: center;">
						<std:button actionGroup="M" classStyle="btn btn-success" action="fidelite.carteFideliteClient.work_merge" workId="${carteFideliteClient.id }" icon="fa-save" value="Sauvegarder" />
						<std:button actionGroup="D" classStyle="btn btn-danger" action="fidelite.carteFideliteClient.work_delete" workId="${carteFideliteClient.id }" icon="fa-trash-o" value="Supprimer" />
						<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</div>
</std:form>	
