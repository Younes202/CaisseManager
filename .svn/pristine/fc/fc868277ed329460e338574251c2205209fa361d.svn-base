<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<script type="text/javascript">
$(document).ready(function (){
	$(document).off('click', '#add_ctrl').on('click', '#add_ctrl', function() {
		var contentTr = $("#ctrl_gpt").html();
		var cpt = $("input[id^='data_label_']").length + 1;
		contentTr = contentTr.replace(new RegExp("_0", 'g'), "_" + cpt);

		$("#ctrl_table").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
		$("#maxCtrl").val(cpt);
	});
	$(document).on('click', "#delete_cont", function() {
		$(this).closest("tr").remove();
	});
	
	$(document).on('change', "select[id^='data_type_']", function() {
    	manageFields($(this));
    });

	setTimeout(function(){
    	$("select[id^='data_type_']").each(function(){
    		manageFields($(this));
        });
    }, 1000);
});

function manageFields(field){
	var idx = field.attr("id").substring(field.attr("id").lastIndexOf("_")+1);
	$('#data_enum_'+idx).hide();
	$('#is_required_'+idx).show();
	$('#max_length_'+idx).show();
	//
	if(field.val() == 'ENUM'){
		$('#data_enum_'+idx).show();
		$('#max_length_'+idx).hide();
	} else if(field.val() == 'BOOLEAN'){
		$('#is_required_'+idx).hide();
		$('#max_length_'+idx).hide();
	} else if(field.val() == 'TITRE'){
		$('#is_required_'+idx).hide();
		$('#max_length_'+idx).hide();
	}
}
</script>

 <style>
#generic_modal_body{
	width: 920px;
	margin-left: -10%;
}
#ctrl_table input, #ctrl_table select {
	font-size: 10px !important;
} 
</style>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Fiche formulaire</span>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row" style="margin-left: 0px;">
				<table style="width: 97%;">
					<tr style="font-weight: normal;">
						<th>Libellé</th>
						<th style="width: 120px;">Type</th>
						<th style="width: 120px;">Obligatoire</th>
						<th>Style</th>
						<th style="width: 120px;">Taille maximale</th>
					</tr>
				</table>
				<hr>
			<!-- Liste des employes -->
			<table id="ctrl_table" style="width: 97%;">
				<tbody>
					<tr id="ctrl_gpt" style="display: none;">
						<std:hidden name="data_group_0" value="${dataGroup }" />
						<td style="padding-top: 5px; padding-right: 30px;" valign="top">
							<std:text name="data_label_0" type="string" style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;" placeholder="Libellé"
								maxlength="120" />
						</td>
						<td style="padding-top: 5px; padding-right: 30px;" valign="top">
							<std:select mode="std" name="data_type_0" type="string" data="${typeArray}" placeholder ="Type" width="120"/>
							<std:select mode="std" name="data_enum_0" type="long" data="${listEnum}" key="id" labels="libelle" placeholder ="Enumération"/>
						</td>
						<td style="padding-top: 5px; padding-right: 30px;" valign="top">
							<std:checkbox name="is_required_0" /> 
						</td>
						<td style="padding-top: 5px; padding-right: 30px;" valign="top">
							<std:text name="data_style_0" type="string" placeholder="Style css" maxlength="250" />
						</td>
						<td style="padding-top: 5px; padding-right: 30px;" valign="top">
							<std:text name="max_length_0" type="long" placeholder="Max caractères" maxlength="5" style="width:77px;" />
						</td>
						<td valign="middle" style="padding-top: 5px; padding-right: 1px;">
							<std:link id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
						</td>
					</tr>
					
					<c:set var="cpt" value="${1 }" />
					<c:set var="encodeService" value="<%=new EncryptionUtil()%>" />
						
					<c:forEach items="${list_data }" var="data">
						<tr>
							<std:hidden name="eaiid_${cpt}" value="${encodeService.encrypt(data.id.toString()) }" />
							<std:hidden name="data_group_${cpt}" value="${dataGroup }" />
							<std:hidden name="data_code_${cpt}" value="${data.data_code }" />
							<td>
								<std:text name="data_label_${cpt}" type="string" maxlength="120" value="${data.data_label}" />
							</td>
							<td>
								<std:select mode="std" name="data_type_${cpt}" type="string" data="${typeArray}" placeholder ="Type" value="${data.data_type}" width="120"/>
								<std:select mode="std" name="data_enum_${cpt}" type="long" style="display:none;" data="${listEnum}" key="id" labels="libelle" placeholder ="Enumération" value="${data.data_enum}"/>
							</td>
							<td>
								<std:checkbox name="is_required_${cpt}" checked="${ data.is_required?'true':'false'}" />
							</td>
							<td>
								<std:text name="data_style_${cpt}" type="string" maxlength="120"  placeholder="Style css" value="${data.data_style}" />
							</td>
							<td>
								<std:text name="max_length_${cpt}" type="long" maxlength="5" placeholder="Max caractères" value="${data.max_length}" style="width:77px;" />
							</td>
							<td align="center">
								<std:link id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
							</td>
						</tr>
						<c:set var="cpt" value="${cpt+1 }" />
					</c:forEach>
				</body>
			</table>
		</div>
		<hr>
		<div class="row" style="text-align: center;">
			<div class="col-md-12">
				<std:link actionGroup="X" id="add_ctrl" tooltip="Ajouter" icon="fa-1x fa-plus" classStyle="btn btn-default"></std:link>
				<std:button actionGroup="M" classStyle="btn btn-success" closeOnSubmit="true" action="admin.dataForm.work_merge" icon="fa-save" value="Sauvegarder" />
				<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
					<i class="fa fa-times"></i> Fermer
				</button>
			</div>
		</div>	
	</div>
</div>	
</std:form>

