<%@page import="framework.model.common.util.StrimUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		init_keyboard_events();
	});
</script>
	
<style>
#generic_modal_body{
	width: 750px;
	margin-left: -25%;
}
</style>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Ajouter le nombre de couverts</span>
			
			<div class="widget-buttons buttons-bordered" style="margin-bottom: 10px;">
         		<i class="fa fa-keyboard-o" style="font-size: 20px;"></i>         
         		<label>
	                 <input class="checkbox-slider toggle colored-blue" type="checkbox" id="keyboard-activator" style="display: none;">
	                 <span class="text"></span>
	             </label>
         	</div>
         	
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal" style="margin-top: 2px;margin-right: 5px;">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row" style="margin-left: 0px;margin-right: 0px;">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Nobre de couverts" />
					<div class="col-md-8">
						<std:select forceWriten="true" type="long" name="nbr_couvert" data="${dataCouvert }" required="true" value="${nbr_couvert }" />
					</div>
				</div>
			</div>		
			<div class="row" style="text-align: center;">
				<div class="col-md-12">
					<std:button actionGroup="X" classStyle="btn btn-success" action="caisse-web.caisseWeb.manageCouverts" targetDiv="left-div" params="${params }" icon="fa-save" value="Valider" closeOnSubmit="true" />
				</div>
			</div>
		</div>
	</div>
</std:form>