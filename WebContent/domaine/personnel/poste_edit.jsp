<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>


  <script type="text/javascript">
  	function manageTemps(){
  		if($('#employe\\.mode_paie').val() == "J"){
			$('#div-heureParJour').show();
		} else{
			$('#div-heureParJour').hide();
		}
  	}
  
	$(document).ready(function() {		
		$('#employe\\.mode_paie').change(function(){
			manageTemps();
		});
		manageTemps();
	});
</script>
	
<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Fiche poste</span>
			<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="pers.poste.work_init_update" workId="${poste.id}" icon="fa fa-pencil" tooltip="Cr&eacute;er" />
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Intitul&eacute;" />
					<div class="col-md-9">
						<std:text name="poste.intitule" type="string" placeholder="Intitul&eacute;" required="true" maxlength="80" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Modalit&eacute; de paiement" />
					<div class="col-md-4">
						<std:select name="employe.mode_paie" type="string" data="${typeTravail}"/>
					</div>
					<std:label classStyle="control-label col-md-2" value="Tarif par unit&eacute" />
					<div class="col-md-4">
						<std:text name="employe.tarif" type="decimal" style="width:30%;" maxlength="15"/>
					</div>
				</div>
				<div class="form-group" style="display:none;" id="div-heureParJour">
					<std:label classStyle="control-label col-md-2" value="Heure/jour" />
					<div class="col-md-4">
						<std:text name="employe.heureParJour" type="decimal" maxlength="10" style="width:20%;border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;text-align:right;"/>
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Description" />
					<div class="col-md-9">
						<std:textarea name="poste.description" rows="5" cols="50" maxlength="255" />
					</div>
				</div>
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<%if (ControllerUtil.getMenuAttribute("IS_SUB_ADD", request) != null) {%>
						<std:button actionGroup="M" closeOnSubmit="true" targetDiv="xxxx" params="w_nos=1" onComplete="$('#refresh-ajx').trigger('click');" classStyle="btn btn-success" action="pers.poste.work_merge" workId="${poste.id }" icon="fa-save" value="Sauvegarder" />
						<%
							} else {
						%>
						<std:button actionGroup="M" classStyle="btn btn-success" action="pers.poste.work_merge" workId="${poste.id }" icon="fa-save" value="Sauvegarder" />
						<%}%>
						<std:button actionGroup="D" classStyle="btn btn-danger" action="pers.poste.work_delete" workId="${poste.id }" icon="fa-trash-o" value="Supprimer" />
						<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</std:form>
