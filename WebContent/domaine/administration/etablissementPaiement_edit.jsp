<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
	#generic_modal_body{
		width: 720px;
		margin-left: -10%;
	}
	.widget-body .row{
		margin-left: 0px;
	}
</style>
<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Fiche paiement</span>
			<std:link actionGroup="U" classStyle="btn btn-default" targetDiv="generic_modal_body" action="admin.etablissementPaiement.work_init_update" workId="${etablissementPaiement.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Paiement" />
					<div class="col-md-4">
						<std:date name="etablissementPaiement.date_paiement" required="true" />
					</div>
					<std:label classStyle="control-label col-md-2" value="Ech&eacute;ance" />
					<div class="col-md-4">
						<std:date name="etablissementPaiement.date_echeance" required="true" />
					</div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Etablissement" />
					<div class="col-md-4">
						<std:select name="etablissementPaiement.opc_etablissement.id" type="long" data="${listEtablissement }" key="id" labels="nom" required="true" width="100%" />
					</div>
					<std:label classStyle="control-label col-md-2" value="Montant" />
					<div class="col-md-4">
						<std:text name="etablissementPaiement.mtt_abonnement" type="decimal" placeholder="Montant" style="width: 60%;" maxlength="20"/>
					</div>
				</div>
			</div>	
			<div class="row">		
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Commentaire" />
					<div class="col-md-4">
						<std:textarea name="etablissementPaiement.commentaire" maxlength="255" cols="60" rows="4" />
					</div>
				</div>
			</div>	
			<hr>
			<div class="modal-footer">
				<div class="row" style="text-align: center;" class="col-md-12">
					<std:button actionGroup="M" classStyle="btn btn-success" action="admin.etablissementPaiement.work_merge" workId="${etablissementPaiement.id }" icon="fa-save" value="Sauvegarder" />
					<std:button actionGroup="D" classStyle="btn btn-danger" action="admin.etablissementPaiement.work_delete" workId="${etablissementPaiement.id }" icon="fa-trash-o" value="Supprimer" />
					<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
						<i class="fa fa-times"></i> Fermer
					</button>
				</div>
			</div>
		</div>
	</div>
</std:form>
