<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#generic_modal_body {
	width: 920px;
	margin-left: -10%;
}
</style>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Fiche lieux</span>
			<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="admin.lieux.work_init_update" workId="${lieux.id}" icon="fa fa-pencil"
				tooltip="Cr&eacute;er" />
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Libellé" />
					<div class="col-md-9">
						<std:text name="lieux.libelle" type="string" placeholder="Libellé" required="true" maxlength="80" />
					</div>
				</div>
			</div>
			<div class="row">
				<b style="padding-left: 10px;line-height: 50px;">DÉTAIL ADRESSE</b>	
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Adresse" />
					<div class="col-md-4">
						<std:text name="lieux.adresse_rue" type="string" placeholder="Adresse" maxlength="250" style="width:100%;" />
					</div>

					<std:label classStyle="control-label col-md-2" value="Complément" />
					<div class="col-md-4">
						<std:text name="lieux.adresse_compl" type="string" placeholder="Complément" style="width: 100%;" maxlength="250" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Ville" />
					<div class="col-md-4">
						<std:select name="lieux.opc_ville.id" groupLabels="opc_region.libelle" groupKey="opc_region.id" type="long" data="${listVille }" key="id" labels="libelle" placeholder="Ville" />
					</div>
					<std:label classStyle="control-label col-md-2" value="Quartier" />
					<div class="col-md-4">
						<std:select name="lieux.opc_villeQuartier.id" type="long" data="${listVilleQuartier }" key="id" labels="libelle" placeholder="Quartier" />
					</div>
				</div>
				
				<b style="padding-left: 10px;line-height: 50px;">INFORMATIONS CONTACT</b>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Téléphone" />
					<div class="col-md-4">
						<std:text name="lieux.telephone" type="string" placeholder="Téléphone" style="width: 60%;" maxlength="20" />
					</div>

					<std:label classStyle="control-label col-md-2" value="Portable" />
					<div class="col-md-4">
						<std:text name="lieux.portable" type="string" placeholder="Portable" style="width: 60%;" maxlength="20" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Mail" />
					<div class="col-md-4">
						<std:text name="lieux.mail" type="string" placeholder="Mail" maxlength="50" />
					</div>

					<std:label classStyle="control-label col-md-2" value="Site" />
					<div class="col-md-4">
						<std:text name="lieux.site" type="string" placeholder="Site" maxlength="50" />
					</div>
				</div>
			</div>
			<div class="row" style="text-align: center;margin-top: 15px;">
				<div class="col-md-12">
					<std:button actionGroup="M" classStyle="btn btn-success" action="admin.lieux.work_merge" workId="${lieux.id }" icon="fa-save" value="Sauvegarder" />
					<std:button actionGroup="D" classStyle="btn btn-danger" action="admin.lieux.work_delete" workId="${lieux.id }" icon="fa-trash-o" value="Supprimer" />
					<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
						<i class="fa fa-times"></i> Fermer
					</button>
				</div>
			</div>
			</div>
		</div>
</std:form>