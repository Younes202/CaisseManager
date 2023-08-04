<%@page import="framework.controller.ControllerUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@page errorPage="/commun/error.jsp"%>

<%
String targetDiv = "body-content";
if(ContextAppli.APPLI_ENV.pil.toString().equals(ControllerUtil.getUserAttribute("CURRENT_ENV", request))) {
	targetDiv = "corp-div";
} else if(ContextAppli.APPLI_ENV.cuis.toString().equals(ControllerUtil.getUserAttribute("CURRENT_ENV", request))) {
	targetDiv = "corp-div";
} else if(ContextAppli.APPLI_ENV.pres.toString().equals(ControllerUtil.getUserAttribute("CURRENT_ENV", request))) {
	targetDiv = "corp-div";
} else if(ContextAppli.APPLI_ENV.cais.toString().equals(ControllerUtil.getUserAttribute("CURRENT_ENV", request))){
	targetDiv = "right-div";
}
%>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Vente : <b>${caisseMouvement.ref_commande }</b> du <b><fmt:formatDate value="${caisseMouvement.date_creation }" pattern="dd/MM/yyyy HH:mm:ss" /></b>
						${caisseMouvement.is_annule ? ' (<b style="color:red;">Annul&eacute;e</b>)' : '' }
			</span>
<%-- 			<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="stock.caisseMouvement.work_init_update" workId="${caisseMouvement.id}" icon="fa fa-pencil" tooltip="Cr&eacute;er" /> --%>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Type" />
					<div class="col-md-9">
						<std:select name="caisseMouvement.type_commande" type="string" data="${typeCmds }" required="true"/>
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Mode de paie" />
					<div class="col-md-9">
						<std:select name="caisseMouvement.mode_paiement" type="string" data="${modePaiement }"/>
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Client" />
					<div class="col-md-9">
						<std:select name="caisseMouvement.opc_client.id" type="long" data="${listClient }" key="id" labels="nom;' ';prenom" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Livreur" />
					<div class="col-md-9">
						<std:select name="caisseMouvement.opc_livreurU.id" type="long" data="${listLivreur }" key="id" labels="login" />
					</div>
				</div>
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button actionGroup="M" classStyle="btn btn-success" closeOnSubmit="true" action="caisse.journee.init_update_journee" targetDiv="<%=targetDiv %>" params="isUp=1&cmvm=${caisseMouvement.id }" icon="fa-save" value="Sauvegarder" />
						<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</std:form>