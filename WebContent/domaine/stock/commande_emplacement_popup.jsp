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
			<span class="widget-caption">Emplacement commande</span>
			<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="stock.emplacement.work_init_update" workId="${emplacement.id}" icon="fa fa-pencil" tooltip="Cr&eacute;er" />
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Emplacement" />
					<div class="col-md-9">
						<std:select name="emplacements" type="string[]" required="true" multiple="true" width="100%" data="${listEmplacement }" key="id" labels="titre" />
					</div>
				</div>
			</div>	
			<div class="row" style="text-align: center;">
				<div class="col-md-12">
					<std:button actionGroup="M" classStyle="btn btn-success" action="stock.mouvement.genererBonCommandeSeuilStock" params="isSub=1" icon="fa-save" value="Générer" />
					<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
						<i class="fa fa-times"></i> Fermer
					</button>
				</div>
			</div>
		</div>
	</div>
</std:form>