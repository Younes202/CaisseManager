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
            <span class="widget-caption">Fiche type de famille</span>
            <std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="stock.typeFamille.work_init_update" workId="${typeFamille.id}"
		icon="fa fa-pencil" tooltip="Cr&eacute;er" />
         </div>
         <div class="widget-body">
		<div class="row">
		   <jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	    </div>
		<div class="row">
				<!-- Formulaire de saisie de type famille -->	
		 		<input type="hidden" name="fam_worksys" value="${fam }"> 

				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Code" />
					<div class="col-md-2">
						<std:text name="typeFamille.code" type="string" placeholder="Code" required="true" />
					</div>
					<std:label classStyle="control-label col-md-2" valueKey="typeFamille.libelle" />
					<div class="col-md-6">
						<std:text name="typeFamille.libelle" type="string" placeholderKey="typeFamille.libelle" required="true" />
					</div>
				</div>

					<div class="row" style="text-align: center;">
						<div class="col-md-12">
							
						</div>
				</div>
		</div>
	</div>
	<div class="modal-footer">
	      <std:button actionGroup="C" classStyle="btn btn-success" action="stock.typeFamille.work_merge" workId="${typeFamille.id }" icon="fa-save" value="Sauvegarder" />
		  <std:button actionGroup="D" classStyle="btn btn-danger" action="stock.typeFamille.work_delete" workId="${typeFamille.id }" icon="fa-trash-o" value="Supprimer" />
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
								<i class="fa fa-times"></i> Fermer
							</button>
	</div>

</div>

</std:form>
