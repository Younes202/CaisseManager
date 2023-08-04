<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Mouvement de fonds</span>
			<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="admin.compteBancaireFonds.work_init_update" workId="${compteBancaireFonds.id}" icon="fa fa-pencil" tooltip="Cr&eacute;er" />
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Libell&eacute;" />
					<div class="col-md-9">
						<std:text name="compteBancaireFonds.libelle" type="string" placeholder="Libell&eacute;" required="true" maxlength="80" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Num. versement" />
					<div class="col-md-9">
						<std:text name="compteBancaireFonds.num_virsement" type="string" placeholder="Num. versement" maxlength="30" style="width:150px;" />
					</div>
				</div>
				<div class="form-group">	
					<std:label classStyle="control-label col-md-3" value="Montant"/>
					<div class="col-md-3">
						<std:text name="compteBancaireFonds.montant" type="decimal" placeholder="Montant" required="true" maxlength="16" style="width:150px;" />
					</div>
					<std:label classStyle="control-label col-md-2" value="Date"/>
					<div class="col-md-3">
						<std:date name="compteBancaireFonds.date_mouvement" required="true" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="De"/>
					<div class="col-md-9">
						<std:select name="compteBancaireFonds.opc_banque_source.id" type="long" key="id" labels="libelle" data="${listeCompteBancaire}" required="true" width="100%" />
					</div>
				</div>	
				<div class="form-group">	
					<std:label classStyle="control-label col-md-3" value="Vers"/>
					<div class="col-md-9">
						<std:select name="compteBancaireFonds.opc_banque_dest.id" type="long" key="id" labels="libelle" data="${listeCompteBancaire}" width="100%" required="true" />
					</div>
				</div>	
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Description" />
					<div class="col-md-9">
						<std:textarea name="compteBancaireFonds.description" rows="5" cols="50" maxlength="255" />
					</div>
				</div>
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button actionGroup="M" classStyle="btn btn-success" action="admin.compteBancaireFonds.work_merge" workId="${compteBancaireFonds.id }" icon="fa-save" value="Sauvegarder" />
						<std:button actionGroup="D" classStyle="btn btn-danger" action="admin.compteBancaireFonds.work_delete" workId="${compteBancaireFonds.id }" icon="fa-trash-o" value="Supprimer" />
						<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</std:form>