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
			<span class="widget-caption">Configuration des imprimantes</span>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-4" value="Imprimantes d'impression" />
					<div class="col-md-7">
						<std:select name="caisse.imprimante_array" required="true" forceWriten="true" multiple="true" type="string[]" data="${list_imprimante}" width="100%" value="${imprArray }" />
					</div>
				</div>
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button style="border-radius: 37px;height: 52px;font-size: 21px;" actionGroup="X" classStyle="btn btn-success" noJsValidate="true" closeOnSubmit="true" action="caisse-web.caisseWeb.merge_conf_imprimantes" workId="${caisse.id }" icon="fa-save" value="Sauvegarder" />
						<button style="border-radius: 37px;height: 52px;font-size: 21px;" type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</div>
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