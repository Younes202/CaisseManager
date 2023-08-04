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
			<span class="widget-caption">Fiche vacance</span>
			<std:link actionGroup="U" classStyle="btn btn-default" action="admin.vacance.work_init_update" workId="${vacance.id}" icon="fa fa-pencil"
				tooltip="Cr&eacute;er" />
				</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Date début" />
					<div class="col-md-4">
						<std:date name="vacance.date_debut" placeholder="Date début" />
					</div>
					<std:label classStyle="control-label col-md-2" value="Date Fin" />
						<div class="col-md-4">
							<std:date name="vacance.date_fin" placeholder="Date fin"/>
						</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Vacance permanant" />
						<div class="col-md-2">
							<std:checkbox name="vacance.is_permanant"  />
						</div>
					</div>
					
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Remplaçant" />
						<div class="col-md-4">
							<std:select name="vacance.opc_remplacant.id" data="${listProfessionnel }" key="id" labels="nom;' ';prenom" type="long" placeholder="Remplaçant" style="width:50%" />
						</div>
					</div>
			</div>	
				
		
					
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button actionGroup="M" classStyle="btn btn-success" action="admin.vacance.work_merge" workId="${vacance.id }" icon="fa-save" value="Sauvegarder" />
						<std:button actionGroup="D" classStyle="btn btn-danger" action="admin.vacance.work_delete" workId="${vacance.id }" icon="fa-trash-o" value="Supprimer" />
						<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
		</div>
	</div>
	
</std:form>
