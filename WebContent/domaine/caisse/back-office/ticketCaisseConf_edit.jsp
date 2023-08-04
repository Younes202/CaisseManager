<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script type="text/javascript">
$(document).ready(function() {
	$(document).off('click', '#add_ctrl');
	$(document).on('click', '#add_ctrl', function(){
		var contentTr = $("#ctrl_gpt").html();
		var cpt = $("select[id^='correspondance']").length + 1;
		contentTr = (contentTr ? contentTr.replace(new RegExp("_0", 'g'), "_"+ cpt) : null);
		
		if(contentTr != null){
			$("#ctrl_table").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
		}
		//
		 $("select[id='correspondance_" + cpt + "']").select2({allowClear: true});
		 $("select[id='correspondance_" + cpt + "']").css("width", "100% !important");
		 
		 $("select[id='famille_" + cpt + "']").select2({allowClear: true});
	});
	$(document).off('click', '#delete_cont');
	$(document).on('click', "#delete_cont", function() { 
		$(this).closest("tr").remove();
	});
	
	$(document).off('change', 'select[id^="correspondance_"]');
	$(document).on('change', 'select[id^="correspondance_"]', function(){
		var curr = $(this).attr("id").substring(15);
		$('#valeur_defaut_'+curr).hide(100);
		$('#famille_div_'+curr).hide(100);
		//
		if($(this).val() == 'libre'){
			$("#valeur_defaut_"+curr).show(100);
		} else if($(this).val() == 'famille'){
			$("#famille_div_"+curr).show(100);
		}
	});
	
});
</script>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Caisse</li>
		<li>Configuration impression</li>
		<li class="active">Edition</li>
	</ul>
</div>

<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="U" classStyle="btn btn-default" action="caisse.ticketCaisseConf.work_init_update" workId="${ticketCaisseConf.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		<std:link classStyle="btn btn-default" action="caisse.ticketCaisseConf.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
        	<span class="widget-caption">Configuration ticket de caisse personnalis&eacute;</span>
        </div>
		<std:form name="data-form">
			<div class="widget-body">
				<div class="row">
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Libell&eacute;" />
						<div class="col-md-4">
							<std:text name="ticketCaisseConf.libelle" type="string" placeholder="Libell&eacute;" required="true" maxlength="150" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-3" value="Taille police" />
						<div class="col-md-2">
							<std:text name="ticketCaisseConf.font_size" type="long" placeholder="Taille police" style="width:50px;" maxlength="2" />
						</div>
						<std:label classStyle="control-label col-md-3" value="Type police"/>
						<div class="col-md-2">
							<std:select type="string" name="ticketCaisseConf.font_weight" data="${fontWeightArray }" width="50" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-3" value="Espace Verticale" />
						<div class="col-md-2">
							<std:text name="ticketCaisseConf.vertical_space" type="long" required="true" placeholder="Espace Verticale" style="width:60px;float: left;" maxlength="4" />
							<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Espace verticale automatique en cas d'absence de 'position Y' (y=0)" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
						</div>
						<std:label classStyle="control-label col-md-3" value="Position retour" />
						<div class="col-md-2">
							<std:text name="ticketCaisseConf.back_pos" type="long" placeholder="Position retour" style="width:60px;" maxlength="4" />
						</div>
					</div>
				</div>
				<hr>
				<div class="row">
					<table id="ctrl_table" style="width: 97%;margin-left: 20px;">
					<tr>
						<th width="300px">Correspondance</th>
						<th width="100px">Position X (mm)</th>
						<th width="100px">Position Y (mm)</th>
						<th width="50px"></th>
					</tr>
					<tr id="ctrl_gpt" style="display: none;">
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<div style="width: 200px;float: left;margin-left: 5px;">
								<std:select mode="std" style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important; width: 100% !important;" classStyle="slct" name="correspondance_0" type="string" data="${correspandanceFieldsList }" key="code" labels="libelle" />
							</div>
							<div style="width: 200px;float: left;margin-left: 5px;">	
								<std:text name="valeur_defaut_0" type="string" placeholder="Valeur par d&egrave;faut" style="display:none;"/>
								<div style="display:none;" id="famille_div_0">
									<std:select mode="std" name="famille_0" type="long" key="id" labels="code;'-';libelle" data="${listFamille }" isTree="true" width="200px" placeholder="Famille"/>								
								</div>
							</div>
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="posX_0" type="long" placeholder="Position X"/>
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="posY_0" type="long" placeholder="Position Y"/>
						</td>
						<td valign="top" style="padding-top: 5px; padding-right: 1px;">
							<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
						</td>
					</tr>

					<c:set var="cpt" value="${1 }" />
					<c:set var="encodeService" value="<%=new EncryptionUtil() %>" />
					
					<c:forEach items="${ticketCaisseConf.list_detail }" var="ticketCaisseConDetail">
						<std:hidden name="eaiid_${cpt}" value="${encodeService.encrypt(ticketCaisseConDetail.id) }" />
						<tr>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top">
								<div style="width: 200px;float: left;margin-left: 5px;">
									<std:select name="correspondance_${cpt}" type="string" data="${correspandanceFieldsList }" key="code" labels="libelle" value="${ticketCaisseConDetail.correspondance }"/>
								</div>
								<div style="width: 200px;float: left;margin-left: 5px;">
									<std:text style="display:${ticketCaisseConDetail.correspondance!='libre'?'none':'' };" name="valeur_defaut_${cpt}" type="string" value="${ticketCaisseConDetail.valeur_defaut }" />
									<div style="display:${ticketCaisseConDetail.correspondance!='famille'?'none':'' };" id="famille_div_${cpt}">
										<std:select mode="std" name="famille_${cpt}" type="long" key="id" labels="code;'-';libelle" placeholder="Famille" data="${listFamille }" isTree="true" width="200px" value="${ticketCaisseConDetail.famille }" />
									</div>
								</div>
							</td>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top">
								<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="posX_${cpt}" type="long" placeholder="Position X" value="${ticketCaisseConDetail.posX }" />
							</td>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top">
								<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="posY_${cpt}" type="long" placeholder="Position Y" value="${ticketCaisseConDetail.posY }" />
							</td>
							<td valign="top" style="padding-top: 5px; padding-right: 1px;">
								<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
							</td>
						</tr>
						<c:set var="cpt" value="${cpt+1 }" /> 
					</c:forEach>
				</table>
				
				</div>
			</div>
			<hr>
			<div class="form-actions">
				<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
					<std:link actionGroup="M" id="add_ctrl" tooltip="Ajouter" icon="fa-1x fa-plus" classStyle="btn btn-default" />
					<std:button actionGroup="M" classStyle="btn btn-success" action="caisse.ticketCaisseConf.work_merge" workId="${ticketCaisseConf.id }" icon="fa-save" value="Sauvegarder" />
					<std:button actionGroup="D" classStyle="btn btn-danger" action="caisse.ticketCaisseConf.work_delete" workId="${ticketCaisseConf.id }" icon="fa-trash-o" value="Supprimer" />
				</div>
			</div>
		</std:form>
	</div>
</div>

<script type="text/javascript">
/*Handles ToolTips*/
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script>    