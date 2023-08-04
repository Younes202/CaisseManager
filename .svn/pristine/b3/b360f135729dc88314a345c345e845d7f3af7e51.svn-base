<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>


<std:form name="data-form">
<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption"> Programmation de l'automate
			</span>
		</div>
		
		<div class="widget-body">
		<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
				<div class="row">
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="chargeDivers.date_debut_auto" style="width: 100px;"/>
						<div class="col-md-4">
							<std:date name="chargeDivers.date_debut_auto" required="true" style="width: 180px;"/>
						</div>

						<std:label classStyle="control-label col-md-2" valueKey="chargeDivers.date_fin_auto" />
						<div class="col-md-4">
							<std:date name="chargeDivers.date_fin_auto"/>
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Tous les" style="width: 100px;"/>
						<div class="col-md-4">
							<std:text name="chargeDivers.frequence" type="long" required="true"/> 
						</div>
						<div class="col-md-4">
							<std:select name="chargeDivers.frequence_type" type="string" data="${frequenceTypeArray }" />
							<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Si la fr&eacute;quence est mensuelle, alors il sera ex&eacute;cut&eacute; &agrave; le m&ecirc;me jour que celui de la date du d&eacute;but de l'automate." src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
						</div>
					</div>
				</div>
			</div>
		</div>


		<div class="modal-footer">
				<std:button actionGroup="U" classStyle="btn btn-success" action="stock.chargeDivers.update_automate" workId="${chargeDivers.id}" icon="fa-save" value="Sauvegarder"/>
				<c:if test="${not empty chargeDivers.date_debut_auto }">
			 		<std:button actionGroup="D" classStyle="btn btn-danger" action="stock.chargeDivers.delete_automate" workId="${chargeDivers.id}" icon="fa-trash-o" value="Supprimer"/>
			 	</c:if> 
				<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
					<i class="fa fa-times"></i> Fermer
				</button>
			</div>
		
	</std:form>
	
<script type="text/javascript">
/*Handles ToolTips*/
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script>	
