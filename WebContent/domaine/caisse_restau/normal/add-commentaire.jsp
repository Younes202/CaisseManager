<%@page import="framework.model.common.util.StrimUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#generic_modal_body{
	width: 750px;
	margin-left: -25%;
}
</style>

<script type="text/javascript">
	$(document).ready(function (){
		init_keyboard_events();
		
		$("#comm_enum").change(function(){
			$("#commentaire").val($("#comm_enum option:selected").text());
		});
	});
</script>
	
<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Ajouter un commentaire</span>
			
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
					<std:label classStyle="control-label col-md-3" value="Commentaire type" />
					<div class="col-md-8">
						<std:select type="long" name="comm_enum" data="${listComments }" key="id" labels="libelle" width="100%" forceWriten="true" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Commentaire" />
					<div class="col-md-8">
						<std:textarea name="commentaire" rows="5" cols="50" style="width:100%;" value="${commentaire }" forceWriten="true" />
					</div>
				</div>
			</div>		
			<div class="row" style="text-align: center;">
				<div class="col-md-12">
					<std:button actionGroup="X" style="border-radius: 37px;height: 52px;font-size: 21px;" classStyle="btn btn-success" action="caisse-web.caisseWeb.addCommentaireCommande" targetDiv="left-div" params="${params }" icon="fa-save" value="Valider" closeOnSubmit="true" />
				</div>
			</div>
		</div>
	</div>
</std:form>