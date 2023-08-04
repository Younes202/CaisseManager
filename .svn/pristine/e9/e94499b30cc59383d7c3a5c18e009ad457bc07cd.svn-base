<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script type="text/javascript">
$(document).ready(function (){
	$("#nbr_ch").change(function(){
		
		$("div[id^='nbr_ch_']").remove();
		
		for(var i=0; i<parseInt($(this).val())-1; i++){
			var clonedDiv = $("#div_nbr").clone();
			$(clonedDiv).css("margin-top", "5px");
			$(clonedDiv).attr("id", $(this).attr("id")+"_"+i);
			$("#row_cheq").append(clonedDiv);
		}
	});
});
</script>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Fiche ch&egrave;que fournisseur</span>
			<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="stock.fournisseurCheque.work_init_update" workId="${fournisseurCheque.id}" icon="fa fa-pencil" tooltip="Cr&eacute;er" />
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-4" value="Fournisseur" />
					<div class="col-md-8">
						<std:select name="fournisseurCheque.opc_fournisseur.id" data="${listeFournisseur }" type="long" key="id" labels="libelle;' ';marque" isTree="true" groupKey="opc_famille.id" groupLabels="opc_famille.code;'-';opc_famille.libelle" width="100%" required="true" />
					</div>
				</div>	
			<c:if test="${fournisseurCheque.id == null }">
				<div class="form-group">	
					<std:label classStyle="control-label col-md-4" value="Nombre" />
					<div class="col-md-2">
						<std:text name="nbr_ch" type="long" placeholder="Nombre" maxlength="2" />
					</div>
				</div>	
			</c:if>	
			</div>
			
	<c:choose>
		<c:when test="${fournisseurCheque.id == null }">
			<div class="row">
				<div class="form-group" style="margin-top: 5px;" id="row_cheq">
					<std:label classStyle="control-label col-md-4" value="Num&eacute;ro de ch&egrave;que" />
					<div class="col-md-4" id='div_nbr'>
						<std:text name="fournisseurCheque.num_cheque" type="string" placeholder="Num&eacute;ros de ch&egrave;que" maxlength="30" />
					</div>
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<div class="row">
				<div class="form-group" style="margin-top: 5px;">
					<std:label classStyle="control-label col-md-4" value="Num&eacute;ros de ch&egrave;que" />
					<div class="col-md-8">
						<std:text name="fournisseurCheque.num_cheque" type="string" placeholder="Num&eacute;ros" maxlength="30" />
					</div>
				</div>
			</div>		
		</c:otherwise>
	</c:choose>		

			<hr>
			<div class="row" style="text-align: center;">
				<div class="col-md-12">
					<std:button actionGroup="M" classStyle="btn btn-success" action="stock.fournisseurCheque.work_merge" workId="${fournisseurCheque.id }" icon="fa-save" value="Sauvegarder" />
					<std:button actionGroup="D" classStyle="btn btn-danger" action="stock.fournisseurCheque.work_delete" workId="${fournisseurCheque.id }" icon="fa-trash-o" value="Supprimer" />
					<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
						<i class="fa fa-times"></i> Fermer
					</button>
				</div>
			</div>
		</div>
</std:form>