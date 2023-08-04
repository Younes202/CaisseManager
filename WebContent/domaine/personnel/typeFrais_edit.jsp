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
			<span class="widget-caption">Fiche type de frais</span>
			<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="pers.typeFrais.work_init_update" workId="${typeFrais.id}" icon="fa fa-pencil"
				tooltip="Cr&eacute;er" />
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="col-lg-12 col-md-12">
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Libell&eacute;" />
						<div class="col-md-5">
							<std:text name="typeFrais.libelle" type="string" maxlength="50" required="true" />
						</div>
						<std:label classStyle="control-label col-md-3" value="Montant max" />
						<div class="col-md-2">
							<std:text name="typeFrais.montant_max" type="decimal" required="true" />
						</div>
					</div>
				</div>	
			</div>		
			<div class="modal-footer">
				<std:button actionGroup="M" classStyle="btn btn-success" action="pers.typeFrais.work_merge" workId="${typeFrais.id }" icon="fa-save" value="Sauvegarder" />
				<std:button actionGroup="D" classStyle="btn btn-danger" action="pers.typeFrais.work_delete" workId="${typeFrais.id }" icon="fa-trash-o" value="Supprimer" />
				<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
					<i class="fa fa-times"></i> Fermer
				</button>
			</div>
	</div>
</div>
</std:form>
