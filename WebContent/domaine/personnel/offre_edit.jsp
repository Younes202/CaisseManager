<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script type="text/javascript">
$(document).ready(function() {
	$("#offre\\.type_offre").change(function(){
		manageType();
	});
	manageType();
});
function manageType(){
	if($("#offre\\.type_offre").val() == 'R'){
		$("#offre_div").show();
	} else{
		$("#offre_div input").val('');
		$("#offre_div").hide();
	}
}
</script>

<style>
.form-title {
	margin-left: 12px;
}
</style>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des personnels</li>
		<li>Offres</li>
		<li class="active">Edition</li>
	</ul>
</div>

<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="U" classStyle="btn btn-default" action="pers.offre.work_init_update" workId="${offre.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		<std:link classStyle="btn btn-default" action="pers.offre.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<div class="page-body" style="margin-top: 34px;">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
        	<span class="widget-caption">Fiche offre</span>
        </div>
		<std:form name="data-form">
			<div class="widget-body">
				<div class="row">
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Nature de l'offre" />
						<div class="col-md-4">
			               	  <std:select name="offre.destination" type="string" data="${types_offre }" required="true" addBlank="false" style="width:100%;" value='<%=ControllerUtil.getMenuAttribute("typeOffre", request) %>' />
			            </div>
			        </div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="offre.libelle" />
						<div class="col-md-4">
							<std:text name="offre.libelle" type="string" maxlength="120" required="true" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="offre.date_debut" />
						<div class="col-md-4">
							<std:date name="offre.date_debut" required="true" />
						</div>
						<std:label classStyle="control-label col-md-2" valueKey="offre.date_fin" />
						<div class="col-md-4">
							<std:date name="offre.date_fin" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Type d'offre" />
						<div class="col-md-6">
							<std:select name="offre.type_offre" type="string" data="${typeOffreArray }" required="true" />
						</div>
					</div>
					<div class="form-group" id="offre_div" style="display: none;">
						<div class="form-group">
							<std:label classStyle="control-label col-md-2" value="Taux" />
							<div class="col-md-2">
								<std:text name="offre.taux_reduction" type="decimal" placeholderKey="offre.taux_reduction" maxlength="12" style="float: left;width: 80%;" />
								<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Taux de réduction/majoration. Il faut le saisir en négatif pour les majorations" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
							</div>
							<std:label classStyle="control-label col-md-2" valueKey="offre.mtt_seuil" />
							<div class="col-md-2">
								<std:text name="offre.mtt_seuil" type="decimal" placeholderKey="offre.mtt_seuil" maxlength="12" />
							</div>
							<std:label classStyle="control-label col-md-2" valueKey="offre.mtt_plafond" />
							<div class="col-md-2">
								<std:text name="offre.mtt_plafond" type="decimal" placeholderKey="offre.mtt_plafond" maxlength="12" />
							</div>
						</div>
						<div class="form-group">	
							<std:label classStyle="control-label col-md-2" value="Ventiler" />
							<div class="col-md-2">
								<std:checkbox name="offre.is_ventil" />
								<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Si montant ventilé, alors la réduction ou la majouration sera ventilée sur les articles achetés et ne sera pas affichée" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
							</div>
						</div>	
					</div>
				</div>
			</div>
			<hr>
			<div class="form-actions">
				<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
					<std:button actionGroup="M" classStyle="btn btn-success" action="pers.offre.work_merge" workId="${offre.id }" icon="fa-save" value="Sauvegarder" />
					<std:button actionGroup="D" classStyle="btn btn-danger" action="pers.offre.work_delete" workId="${offre.id }" icon="fa-trash-o" value="Supprimer" />
				</div>

			</div>
		</std:form>
	</div>
</div>

<script type="text/javascript">
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script>    