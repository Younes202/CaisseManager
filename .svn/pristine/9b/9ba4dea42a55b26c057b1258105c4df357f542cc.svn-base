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
</style>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Synchronisation des données</span>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row" style="margin: 0px;">
				<div class="form-group">
					<std:label classStyle="control-label col-md-4" value="URL distante" />
					<div class="col-md-8">
						<std:select name="synchro_ets" type="long[]" data="${listEts }" key="id" labels="nom" multiple="true" required="true" width="100%" />
					</div>
				</div>
				<div class="form-group">	
					<std:label classStyle="control-label col-md-4" value="Eléments à synchroniser" />
					<div class="col-md-8">
						<std:select name="synchro_elmnts" type="string[]" data="${synchroList }" multiple="true" required="true" width="100%" />
					</div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-4" value="Désactiver données existantes" />
					<div class="col-md-8">
						<std:checkbox name="is_disabled" />
					</div>
				</div>
			</div>		
		</div>
		<div class="modal-footer">
			<std:button actionGroup="M" classStyle="btn btn-success" closeOnSubmit="true" action="stock.centraleSynchro.work_merge" params="isSub=1" icon="dot-circle-o" value="Sauvegarder" />
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>

	</div>
</std:form>

<script type="text/javascript">
/*Handles ToolTips*/
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script> 