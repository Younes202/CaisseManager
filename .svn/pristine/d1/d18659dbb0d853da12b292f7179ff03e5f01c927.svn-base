<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#generic_modal_body{
	width: 920px;
	margin-left: -10%;
}
</style>

<c:set var="list_financement" value='<%=ControllerUtil.getMenuAttribute("PAIEMENT_DATA", request) %>' />

<script type="text/javascript">
	$(document).ready(function() {
		$(document).off('click', '#add_ctrl_finance');
		$(document).on('click', '#add_ctrl_finance', function(){
			var contentTr = $("#ctrl_gpt_finance").html();
			var cpt = $("select[id^='opc_financement_enum\\.id']").length + 1;
			contentTr = (contentTr ? contentTr.replace(new RegExp("_0", 'g'), "_"+ cpt) : null);
			
			if(contentTr != null){
				$("#ctrl_table_finance").append('<tr style="border-bottom: 1px dashed blue;" cpt="'+cpt+'">' + contentTr + '</tr>');
			}
			//
			$("select[id='opc_financement_enum\\.id_" + cpt + "']").select2({allowClear: true});
			$("select[id='opc_compte_bancaire\\.id_" + cpt + "']").select2({allowClear: true});
			$("select[id='opc_fournisseurCheque\\.id_" + cpt + "']").select2({allowClear: true});
			
			  $('#date_echeance_' + cpt).datepicker({
		    	todayBtn: true,
		    	clearBtn: true,
			    language: "fr",
			    autoclose: true,
			    todayHighlight: true
		    });
		});
		$(document).off('click', '#delete_finance');
		$(document).on('click', "#delete_finance", function() {
			$(this).closest("tr").remove();
		});
		
		$(document).off('change', 'select[id^="opc_financement_enum\\.id"]');
		$(document).on('change', "select[id^='opc_financement_enum\\.id']", function(){
			var cpt = $(this).attr("id").substring($(this).attr("id").lastIndexOf("_")+1, $(this).attr("id").length);
			$("#opc_compte_bancaire\\.id_"+cpt+", #opc_fournisseurCheque\\.id_"+cpt).val('').trigger('change');
			$("#montant_"+cpt+", #num_cheque_"+cpt+", #num_virement_"+cpt+", #date_echeance_"+cpt).val('');
			
			manageModePaiement(cpt);
		});
		// Restaurer le cheque fournisseur
		var comboFourn = $("option:selected", 'select[id$="opc_fournisseur\\.id"]').attr("hiddenkey");
		if(comboFourn && comboFourn.length > 0){
			var currFourn = comboFourn.split('|')[0];
			populateChequeFourn(currFourn);
		}
	});
	
	
	function populateChequeFourn(valFourn){
		$("select[id^='opc_fournisseurCheque\\.id']").empty();
		// Restutuer la liste
		if(valFourn == ''){
			return;
		}
		$("#num_cheque_all > option").each(function() {
			if($(this).attr("hiddenkey")){
				 var fourn = $(this).attr("hiddenkey").split('|')[0];
				 if(valFourn == fourn){
					 var option = new Option($(this).text(), $(this).val());
					 $("select[id^='opc_fournisseurCheque\\.id']").append(option);
				 }
			}
		});
		$("select[id^='opc_fournisseurCheque\\.id']").trigger('change');
	}
	
	function manageModePaiement(cpt){
		var hidden = $("option:selected", "#opc_financement_enum\\.id_"+cpt).attr("hiddenkey");
		
		if(hidden){
			var modePaiement = hidden.split('|');
			if(modePaiement[0] == 'CHEQUE'){
				$("#num_virement_"+cpt+", #div_opc_fournisseurCheque\\.id_"+cpt).css('display', 'none');
				$("#num_cheque_"+cpt).css('display', 'block');
			} else if(modePaiement[0] == 'CHEQUE_F'){ 
				$("#num_virement_"+cpt+", #num_cheque_"+cpt).css('display', 'none');
				$("#div_opc_fournisseurCheque\\.id_"+cpt).css('display', 'block');
			} else if(modePaiement[0] == 'VIREMENT' || modePaiement[0] == 'EFFET'){
				$("#num_cheque_"+cpt+", #div_opc_fournisseurCheque\\.id_"+cpt).css('display', 'none');
				$("#num_virement_"+cpt).css('display', 'block');
			} else{
				$("#num_virement_"+cpt+", #div_opc_fournisseurCheque\\.id_"+cpt).css('display', 'none');
				$("#num_cheque_"+cpt).css('display', 'none');
			}
			$("#div_opc_compte_bancaire\\.id_"+cpt).css('display', 'block');
			$("#date_echeance_"+cpt).css('display', 'block');
			$("#montant_"+cpt).css('display', 'block');
		} else{
			$("#div_opc_compte_bancaire\\.id_"+cpt+", #num_virement_"+cpt+", #div_opc_fournisseurCheque\\.id_"+cpt+", #date_echeance_"+cpt+", #montant_"+cpt).css('display', 'none');
		}
	}
	</script>
	
<std:form name="data-form">
		
	<div style="display: none;">
		<std:select name="num_cheque_all" type="long" key="id" labels="num_cheque" hiddenkey="opc_fournisseur.id" data="${listChequeFournisseur }" style="display:none;" />
	</div>
	
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Gestion des paiements</span>
			<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="stock.emplacement.work_init_update" workId="${emplacement.id}" icon="fa fa-pencil" tooltip="Cr&eacute;er" />
			
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<table id="ctrl_table_finance" style="width: 97%;margin-left: 20px;">
					<tr style="border-bottom: 1px solid blue;">
						<th>Mode de paiement</th>
						<th width="100px">Compte</th>
						<th width="110px">Num&eacute;ro</th>
						<th width="90px">Montant</th>
						<th width="110px">Date &eacute;ch&eacute;ance</th>
						<th width="50px"></th>
					</tr>
					<tr id="ctrl_gpt_finance" style="display: none;">
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:select classStyle="slct" name="opc_financement_enum.id_0" hiddenkey="code" type="long" key="id" labels="libelle" data="${listeFinancement}" />
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<div style="display:none;" id="div_opc_compte_bancaire.id_0">
								<std:select classStyle="slct" name="opc_compte_bancaire.id_0" width="200" type="long" key="id" labels="libelle" data="${listeBanque}" />
							</div>	
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:text name="num_cheque_0" style="display:none;width:100px;" type="string" placeholder="Ch&egrave;que" maxlength="40"/>

							<div  style="display:none;" id="div_opc_fournisseurCheque.id_0">
								<std:select classStyle="slct" name="opc_fournisseurCheque.id_0" width="200" type="long" key="id" labels="num_cheque;' (';opc_fournisseur.libelle;')'" hiddenkey="opc_fournisseur.id" data="${listChequeFournisseur}" />
							</div>	
 
 							<std:text name="num_virement_0" style="display:none;width:100px;" type="string" placeholder="Num&eacute;ro" maxlength="40"/>
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:text name="montant_0" type="decimal" style="display:none;width:100px;" placeholder="Montant" maxlength="14" />
						</td> 
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:date name="date_echeance_0" style="display:none;" placeholder="Ech&eacute;ance" classStyle="form-control" />
						</td>
						<td valign="top" style="padding-top: 5px; padding-right: 1px;">
							<std:link actionGroup="M" id="delete_finance" icon="fa fa-times" style="color: red;"></std:link>
						</td>
					</tr>

					<c:set var="cpt" value="${1}" />
					<c:set var="totalLigne" />
					
					<c:forEach items="${list_financement }" var="financement">
						<tr>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top">
								<std:select classStyle="slct" name="opc_financement_enum.id_${cpt}" hiddenkey="code" type="long" key="id" labels="libelle" data="${listeFinancement}" value="${financement.opc_financement_enum.id }" />
							</td>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top">
								<div style="display:block;" id="div_opc_compte_bancaire.id_${cpt}">
									<std:select classStyle="slct" name="opc_compte_bancaire.id_${cpt}" width="200" type="long" key="id" labels="libelle" data="${listeBanque}" value="${financement.opc_compte_bancaire.id }" />
								</div>
							</td>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top">
								<std:text name="num_cheque_${cpt}" type="string" style="width:100px;display:${financement.opc_financement_enum.code=='CHEQUE'?'':'none' };" placeholder="Ch&egrave;que" maxlength="40" value="${financement.num_cheque }" />

								<div  style="display:${financement.opc_financement_enum.code=='CHEQUE_F'?'':'none' };" id="div_opc_fournisseurCheque.id_${cpt}">
									<std:select classStyle="slct" name="opc_fournisseurCheque.id_${cpt}" width="200" type="long" key="id" labels="num_cheque;' (';opc_fournisseur.libelle;')'" data="${listChequeFournisseur}" hiddenkey="opc_fournisseur.id" value="${financement.montant }" />
								</div>	
 
								<std:text name="num_virement_${cpt}" type="string" style="width:100px;display:${(financement.opc_financement_enum.code=='VIREMENT' or financement.opc_financement_enum.code=='EFFET')?'':'none' };" placeholder="Num&eacute;ro" maxlength="40" value="${financement.num_virement }" />
							</td>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top">
								<std:text name="montant_${cpt}" type="decimal" style="width:100px;" placeholder="Montant" maxlength="14" value="${financement.montant }" />
							</td>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top">
								<std:date name="date_echeance_${cpt}" placeholder="Ech&eacute;ance" value="${financement.date_echeance }"/>
							</td>
							<td valign="top" style="padding-top: 5px; padding-right: 1px;">
								<std:link actionGroup="M" id="delete_finance" icon="fa fa-times" style="color: red;"></std:link>
							</td>
						</tr>

						<c:set var="cpt" value="${cpt+1 }" /> 
					</c:forEach>
				</table>
			</div>
			
			<div class="row" style="text-align: center;margin-top: 10px;">
				<div class="col-md-12">
					<std:link actionGroup="X" id="add_ctrl_finance" tooltip="Ajouter un mode de paiement" icon="fa-1x fa-plus" classStyle="btn btn-default" />
					<std:button actionGroup="X" classStyle="btn btn-success" closeOnSubmit="true" targetDiv="finance_bloc" action="admin.compteBancaire.mergePaiementPopup" params="elmnt=${elementId }" icon="fa-save" value="Sauvegarder" />
				</div>
			</div>
		</div>
	</div>
</std:form>			
					