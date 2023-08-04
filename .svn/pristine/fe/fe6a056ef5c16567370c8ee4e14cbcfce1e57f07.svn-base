<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		$('#pdf_link').click(function(){
			$("#w_f_act").val("<%=EncryptionUtil.encrypt("stock.composant.exporterComposants")%>");
			document.getElementById("data-form-export").submit();
			$('#close_modal').trigger('click');
		});
	});
</script>	
		
<std:form name="data-form-export">
	<input type="hidden" name="isSub" value="1">	
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Fiche export articles</span>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="  Familles à exporter" />
					<div class="col-md-9">
						<std:select name="familles_array" isTree="true" type="string[]" multiple="true" width="100%" data="${listeFaimlle }" key="id" labels="libelle" />
					</div>
				</div>
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button actionGroup="M" id="pdf_link" classStyle="btn btn-success" icon="fa-save" value="Exporter le fichier" />
						<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>	
				</div>
			</div>
		</div>
	</div>
</std:form>