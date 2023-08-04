<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#generic_modal_body{
	width: 620px;
	margin-left: -5%;
}
#ctrl_table tr{
    border-top: 1px solid #f8f1f1;
}
#ctrl_table input{
	height: 21px;
    font-size: 10px;
}
#ctrl_table input[type='checkbox']{
	height: 20px !important;
    font-size: 10px;
}
#ctrl_table a{
    padding: 0px 4px 0px 7px;
}
</style>

<script type="text/javascript">
$(document).ready(function() {
	$(document).off('click', '#add_ctrl').on('click', '#add_ctrl', function(){
		var contentTr = $("#ctrl_gpt").html();
		var cpt = $("input[id^='libelle_']").length + 1;
		contentTr = contentTr.replace(new RegExp("_0", 'g'), "_"+ cpt);
		$("#ctrl_table").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
		
		$("#ctrl_table").tableDnD();
	});
	$(document).off('click', '#delete_cont').on('click', '#delete_cont', function() {
		$(this).closest("tr").remove();
		
		$("#ctrl_table").tableDnD();
	});
	$("#ctrl_table").tableDnD();
});
function manageLabelsOrder(){
	var cpt = 1;
	$("#ctrl_table tr").each(function(){
		$(this).find("input[type='checkbox']").each(function(){
			if($(this).attr('id') != 'is_groupe_0'){
				$(this).attr('id', "is_groupe_"+cpt);
				$(this).attr('name', "is_groupe_"+cpt);
			}
		});
		$(this).find("input[type='text']").each(function(){
			if($(this).attr('id') != 'libelle_0'){
				$(this).attr('id', "libelle_"+cpt);
				$(this).attr('name', "libelle_"+cpt);
			}
		});
		cpt++;
	});
}
</script>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Libellés paramétrés</span>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group" style="margin-left: 13px;">
					<span style="font-size: 10px;color: blue;">Glisser les lignes pour trier</span>
				</div>	
				<div class="form-group" style="margin-left: 13px;">
					| <std:link actionGroup="C" id="add_ctrl" tooltip="Ajouter libellé" value="Ajouter" icon="fa-1x fa-plus" classStyle="btn btn-default" />
				</div>
				<div class="form-group" style="margin-left: 13px;
						overflow-y: auto;
    					overflow-x: hidden;
    					max-height: 517px;">
					<table id="ctrl_table" style="width: 97%;margin-left: 20px;">
						<tr>
							<th width="60%">Libellé</th>
							<th width="10%">Est un titre ?</th>
							<th></th>
						</tr>
						<tr id="ctrl_gpt" style="display: none;">
							<td style="padding-top: 5px; padding-right: 10px;" valign="top">
								<std:text name="libelle_0" type="string" placeholder="Libellé" maxlength="120"/>
							</td>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top">
								<std:checkbox name="is_groupe_0" />
							</td>
							<td valign="top" style="padding-top: 5px; padding-right: 1px;">
								<std:link actionGroup="C" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
							</td>
						</tr>
	
						<c:set var="cpt" value="${1 }" />
						<c:forEach items="${list_charge_lib }" var="cdLib">
							<tr>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top">
									<std:text name="libelle_${cpt}" type="string" placeholder="Libellé" value="${cdLib.libelle }" maxlength="120"/>	
								</td>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top">	
									<std:checkbox name="is_groupe_${cpt}" checked="${cdLib.is_groupe }"/>
								</td>
								<td valign="top" style="padding-top: 5px; padding-right: 1px;">
									<std:link actionGroup="C" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
								</td>
							</tr>
							<c:set var="cpt" value="${cpt+1 }" /> 
						</c:forEach>
					</table>				
				</div>	
				<hr>
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button actionGroup="C" targetDiv="xxxxx" onClick="manageLabelsOrder();" classStyle="btn btn-success" params="isSub=1" closeOnSubmit="true" action="stock.chargeDivers.load_labels" icon="fa-save" value="Sauvegarder" />
						<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</std:form>