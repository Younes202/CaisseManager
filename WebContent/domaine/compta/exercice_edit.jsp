<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
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
			<span class="widget-caption">Fiche exercice</span>
			<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="compta.exercice.work_init_update" workId="${exercice.id}" icon="fa fa-pencil" tooltip="Cr&eacute;er" />
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Nom" />
					<div class="col-md-9">
						<std:text name="exercice.libelle" type="string" placeholder="Libell&eacute;" required="true" maxlength="120" />
					</div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="D&eacute;but" />
					<div class="col-md-4">
						<std:date name="exercice.date_debut" />
					</div>
					<std:label classStyle="control-label col-md-2" value="Fin" />
					<div class="col-md-4">
						<std:date name="exercice.date_fin" />
					</div>
				</div>
			</div>	
			<div class="row" style="text-align: center;">
				<div class="col-md-12">
					<std:button actionGroup="M" classStyle="btn btn-success" action="compta.exercice.work_merge" workId="${exercice.id }" icon="fa-save" value="Sauvegarder" />

					<std:button actionGroup="D" classStyle="btn btn-danger" action="compta.exercice.work_delete" workId="${exercice.id }" icon="fa-trash-o" value="Supprimer" />
					<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
						<i class="fa fa-times"></i> Fermer
					</button>
				</div>
			</div>
		</div>
	</div>
</std:form>