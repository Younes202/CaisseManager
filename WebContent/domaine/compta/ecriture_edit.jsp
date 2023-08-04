<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
			calculMontantTotal();
			
			$(document).off('change', 'input[id^="montant"]').on('change', 'input[id^="montant"]', function(){
				calculMontantTotal();
			});
			
		$(document).off('click', '#add_ctrl_d');
		$(document).on('click', '#add_ctrl_d', function(){
			var contentTr = $("#ctrl_gpt_d").html();
			var cpt = $("select[id^='opc_compte\\.id']").length + 1;
			contentTr = contentTr.replace(new RegExp("_0", 'g'), "_"+ cpt);
			$("#ctrl_table_d").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
			$("select[id='opc_compte\\.id_" + cpt + "']").select2({allowClear: true});
			$(".select2-container").css("width", "400px");
		});
		$(document).off('click', '#add_ctrl_c');
		$(document).on('click', '#add_ctrl_c', function(){
			var contentTr = $("#ctrl_gpt_c").html();
			var cpt = $("select[id^='opc_compte\\.id']").length + 1;
			contentTr = contentTr.replace(new RegExp("_0", 'g'), "_"+ cpt);
			$("#ctrl_table_c").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
			$("select[id='opc_compte\\.id_" + cpt + "']").select2({allowClear: true});
			$(".select2-container").css("width", "400px");
		});
		$(document).off('click', '#delete_cont');
		$(document).on('click', "#delete_cont", function() {
			$(this).closest("tr").remove();
			calculMtt();
		});
	});
	
	function calculMontantTotal(){
		var totalMttD = 0;
		var totalMttC = 0;
		
		$("#ctrl_table_d").find("input[id^='montant']").each(function(){
			var valTrim = $(this).val().replace(/ /g,"");
			var val = parseFloat(valTrim);
			if(val){
				totalMttD = totalMttD + val;
			}
		});
		$("#total_d").html(totalMttD.toFixed(2));
		// -----------------------------------------------
		$("#ctrl_table_c").find("input[id^='montant']").each(function(){
			var valTrim = $(this).val().replace(/ /g,"");
			var val = parseFloat(valTrim);
			if(val){
				totalMttC = totalMttC + val;
			}
		});
		$("#total_c").html(totalMttC.toFixed(2));
	}
</script>
	
<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Comptabilit&eacute;</li>
		<li>Op&eacute;rations diverses</li>
		<li class="active">Edition</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="U" classStyle="btn btn-default" action="compta.ecriture.work_init_update" workId="${ecriture.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		<std:link classStyle="btn btn-default" action="compta.ecriture.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
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
	<std:form name="data-form">
		<!-- widget grid -->
		<div class="widget" >
			<div class="widget-header bordered-bottom bordered-blue">
				<span class="widget-caption">Fiche op&eacute;ration</span>
			</div>
			<div class="widget-body">
				<fieldset>
               		<legend>Op&eacute;ration</legend>          
                      <div class="form-group">
                        <std:label classStyle="col-sm-2 control-label" value="Libell&eacute;"/>
                        <div class="col-sm-4">
                        	<std:text type="string" required="true" name="ecriture.libelle" placeholder="Libell&eacute;" maxlength="80" />
                        </div>
                        <std:label classStyle="col-sm-2 control-label" value="Date"/>
                        <div class="col-sm-4">
                          <std:date required="true" name="ecriture.date_mouvement" />
                        </div>
                      </div>
                      <div class="form-group">
                        <std:label classStyle="col-sm-2 control-label" value="Soci&eacute;t&eacute;"/>
                        <div class="col-sm-4">
                        	<std:select type="long" required="true" name="ecriture.opc_societe.id" placeholder="Soci&eacute;t&eacute;" data="${listSociete }" key="id" labels="raison_sociale" width="400" />
                        </div>
                        <std:label classStyle="col-sm-2 control-label" value="Non comptabilis&eacute;"/>
                        <div class="col-sm-4">
                        	<std:checkbox name="ecriture.is_compta" />
                        	<i class="fa fa-info-circle" style="cursor: help;cursor: help;color: #009688;font-size: 14px;" title="Si coch&eacute;e, alors cette op&eacute;ration ne sera pas comptabilis&eacute;e dans la compta officielle"></i>
                        </div>
                      </div> 
               </fieldset>
               <br><br> 
               <fieldset>
               		<legend>Ventilation</legend>
            <div class="row">
            	 <div class="col-sm-6" style="border-right: 1px solid blue;">
					<table id="ctrl_table_d" style="width: 97%;margin-left: 20px;">
						<tr>
							<td style="text-align: center;background-color: #B3E5FC;font-size: 15px;" colspan="3">
								Compte d&eacute;bits
								<std:link actionGroup="M" id="add_ctrl_d" tooltip="Ajouter un compte" value="Ajouter" icon="fa-3x fa-plus" classStyle="btn btn-xs btn-primary" />
							</td>
						</tr>
						<tr>
							<th width="80%">Compte</th>
							<th width="14%">Montant</th>
							<th></th>
						</tr>
						<tr id="ctrl_gpt_d" style="display: none;">
							<std:hidden name="sens_0" value="D" />
							<td style="padding-top: 5px; padding-right: 10px;" valign="top">
								<std:select mode="std" style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;" name="opc_compte.id_0" type="long" data="${listCompte }" width="400" key="id" labels="code;'-';libelle" placeholder="Compte" isTree="true" />
							</td>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top">
								<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="montant_0" type="decimal" placeholder="Montant" maxlength="20" />
							</td>
							<td valign="top" style="padding-top: 5px; padding-right: 1px;">
								<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
							</td>
						</tr>
						
						<c:set var="cpt" value="${1 }" />
						<c:forEach items="${listEcritureGroupee }" var="ecritP">
							<c:if test="${ecritP.sens == 'D' }">
								<std:hidden name="sens_${cpt }" value="${ecritP.sens }" />
								<tr>
									<td style="padding-top: 5px; padding-right: 10px;" valign="top">
										<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;" name="opc_compte.id_${cpt }" type="long" data="${listCompte }" width="400" value="${ecritP.opc_compte.id }" key="id" labels="code;'-';libelle" placeholder="Compte" isTree="true" />
									</td>
									<td style="padding-top: 5px; padding-right: 10px;" valign="top">
										<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="montant_${cpt }" type="decimal" placeholder="Montant" value="${ecritP.montant }" maxlength="20" />
									</td>
									<td valign="top" style="padding-top: 5px; padding-right: 1px;">
										<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
									</td>
								</tr>
								<c:set var="cpt" value="${cpt+1 }" /> 
							</c:if>
						</c:forEach>
					</table>
				</div>	
				<div class="col-sm-6">
					<table id="ctrl_table_c" style="width: 97%;margin-left: 20px;">
						<tr>
							<td style="text-align: center;background-color: #B3E5FC;font-size: 15px;" colspan="3">
								Compte cr&eacute;dits
								<std:link actionGroup="M" id="add_ctrl_c" tooltip="Ajouter un compte" value="Ajouter" icon="fa-3x fa-plus" classStyle="btn btn-xs btn-primary" />
							</td>
						</tr>
						<tr>
							<th width="80%">Compte</th>
							<th width="14%">Montant</th>
							<th></th>
						</tr>
						<tr id="ctrl_gpt_c" style="display: none;">
							<std:hidden name="sens_0" value="C" />
							<td style="padding-top: 5px; padding-right: 10px;" valign="top">
								<std:select mode="std" style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;" name="opc_compte.id_0" type="long" data="${listCompte }" width="400" key="id" labels="libelle" placeholder="Compte" isTree="true" />
							</td>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top">
								<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="montant_0" type="decimal" placeholder="Montant" maxlength="20" />
							</td>
							<td valign="top" style="padding-top: 5px; padding-right: 1px;">
								<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
							</td>
						</tr>
						
						<c:forEach items="${listEcritureGroupee }" var="ecritP">
							<c:if test="${ecritP.sens == 'C' }">
								<std:hidden name="sens_${cpt }" value="${ecritP.sens }" />
								<tr>
									<td style="padding-top: 5px; padding-right: 10px;" valign="top">
										<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;" name="opc_compte.id_${cpt }" type="long" data="${listCompte }" width="400" value="${ecritP.opc_compte.id }" key="id" labels="libelle" placeholder="Compte" isTree="true" />
									</td>
									<td style="padding-top: 5px; padding-right: 10px;" valign="top">
										<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="montant_${cpt }" type="decimal" placeholder="Montant" value="${ecritP.montant }" maxlength="20" />
									</td>
									<td valign="top" style="padding-top: 5px; padding-right: 1px;">
										<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
									</td>
								</tr>
								<c:set var="cpt" value="${cpt+1 }" /> 
							</c:if>	
						</c:forEach>
					</table>
				</div>	
			</div>
			<hr>
			<div class="row">	
			 	<div class="col-sm-6" style="text-align: right;">
					<span class="label label-danger" style="font-size: 14px;margin-right: 8%;" id="total_d"></span>
				</div>	
				<div class="col-sm-6" style="text-align: right;">
					<span class="label label-success" style="font-size: 14px;margin-right: 8%;" id="total_c"></span>
				</div>
			</div>		
             </fieldset>         
                     <div class="form-group">
                      	<hr>
                      </div>            
				<div class="row" style="text-align: center;">
					<std:button actionGroup="M" classStyle="btn btn-success" action="compta.ecriture.work_merge" workId="${ecriture.id }" icon="fa-save" value="Sauvegarder" />
					<std:button actionGroup="D" classStyle="btn btn-danger" action="compta.ecriture.work_delete" workId="${ecriture.id }" icon="fa-trash-o" value="Supprimer" />
				</div>
			</div>
			</div>
			</std:form>	
		</div>

