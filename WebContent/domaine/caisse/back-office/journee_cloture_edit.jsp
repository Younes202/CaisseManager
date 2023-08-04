<%@page import="appli.model.domaine.vente.persistant.JourneePersistant"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
JourneePersistant lastJournee = (JourneePersistant)request.getAttribute("lastJournee");
%>
<std:form name="data-form">
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Gestion journ&eacute;e</span>
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal" style="margin-top: 2px;margin-right: 2px;">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>
		<div class="widget-body">
		<div class="row">
			<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
		</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Solde coffre fort"/>
					<div class="col-md-4">
						<std:text autocomplete="false" name="journee.solde_coffre" type="decimal" maxlength="15" style="width:120px;" value="${oldSoldeCoffre }" />
					</div>
				</div>
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button actionGroup="M" classStyle="btn btn-danger" action="caisse.journee.cloturer_journee" workId="${currJournee }" icon="fa-save" value="Cl&ocirc;turer la journ&eacute;e" />
				</div>
			</div>
		</div>
	</div>
</div>
</std:form>

<script type="text/javascript">
/*Handles ToolTips*/
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script>  