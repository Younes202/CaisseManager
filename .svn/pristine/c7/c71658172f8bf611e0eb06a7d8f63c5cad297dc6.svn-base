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
			<span class="widget-caption">Edition facture</span>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Type" />
					<div class="col-md-9">
						<std:select type="string" name="typeMvm" placeholder="Type mouvement" required="true" data="${typeMvm }" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Date facture" />
					<div class="col-md-9">
						<std:date name="dateFacture" placeholder="Date facture" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Nom facture" />
					<div class="col-md-9">
						<std:text name="nomFacture" required="true" placeholder="Nom facture" type="string" value="${client.nom } ${client.prenom }" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Adresse facture" />
					<div class="col-md-9">
						<std:textarea name="adresseFacture" required="true" placeholder="Adresse facture" value="${client.getAdressFactureFull() }" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Date début" />
					<div class="col-md-9">
						<std:date name="dateDebut" placeholder="Date début" required="true" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Date fin" />
					<div class="col-md-9">
						<std:date name="dateFin" placeholder="Date fin" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Mode de paiement" />
					<div class="col-md-9">
						<std:select name="modePaiement" type="long" data="${listeFinancement }" key="id" labels="libelle" placeholder="Mode de paiement" style="width:70%;" />
					</div>
				</div>
			</div>
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button actionGroup="M" classStyle="btn btn-success" action="stock.emplacement.work_merge" workId="${emplacement.id }" icon="fa-save" value="Sauvegarder" />
						<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</div>
		</div>
</std:form>