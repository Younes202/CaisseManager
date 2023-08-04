<%@page import="appli.controller.domaine.util_erp.ContextAppli.TYPE_MOUVEMENT_ENUM"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="appli.controller.domaine.stock.bean.MouvementBean"%>
<%@page import="appli.controller.domaine.stock.action.ArticleAction"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script src="resources/framework/js/fuelux/wizard/wizard-custom.js"></script>
<script src="resources/framework/js/util_file_upload.js?v=1.1"></script>
<script src="resources/framework/js/toastr/toastr.js"></script>



<style>
.select2-container {
	min-width: 140px !important;
}

hr {
	margin: 15px;
}
</style>

<%
	String typeMouvement = (String) ControllerUtil.getMenuAttribute("type_mvmnt", request);	
	ControllerUtil.setMenuAttribute("tp", "A", request);
	
	if(StringUtil.isEmpty(typeMouvement)){
		typeMouvement = TYPE_MOUVEMENT_ENUM.a.toString();
	}
	TYPE_MOUVEMENT_ENUM typeMouvementEnum = TYPE_MOUVEMENT_ENUM.valueOf(typeMouvement);
%>

<script type="text/javascript">
	var idxFile = 1;
	$(document).ready(function() {
		$("#addFile").click(function(){
			var content = $("#fileLoadDiv").clone().html();
			content = content.replace(/photoX/g,"photo"+idxFile);
			$("#row_file").append(content);
			idxFile++;
		});
		// Init wizard
		$('#mvmWizard').wizard();

		
		<%if(ControllerUtil.isEditionWritePage(request)){%>
		loadInputFileEvents();
	<%} else{%>
		$("div[id^='sep_photo']").remove();
	<%}
	// Initialiser les photos ou documents
	MouvementBean mvmBean = (MouvementBean)request.getAttribute("mouvement");
	if(mvmBean != null && mvmBean.getId() != null){
		IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
		Map<String, byte[]> dataimg = service.getDataImage(mvmBean.getId(), "mvm");
		for(String key : dataimg.keySet()){%>
			var content = $("#fileLoadDiv").clone().html();
			content = content.replace(/photoX/g,"photo"+idxFile);
			$("#row_file").append(content);
			var ext = '<%=key.substring(key.lastIndexOf(".")+1)%>';
			var img = "";
	        if(ext.startsWith('pdf')){
	        	img = "resources/framework/img/filetype_pdf.png";
	        } else if(ext.startsWith('xls')){
	        	img = "resources/framework/img/xls_file.png";
	        } else if(ext.startsWith('txt')){
	        	img = "resources/framework/img/txt.png";
	        } else if(ext.startsWith('doc')){
	        	img = "resources/framework/img/document_microsoft_word_01.png";
	        } else{
	        	img = "data:image/jpeg;base64,<%=FileUtil.getByte64(dataimg.get(key))%>";
	        }
	        $("#photo"+idxFile+"_div").css("background", "");
	        $("#photo"+idxFile+"_div").html("<img src='"+img+"' width='120' height='120'/>");
			$("#photo"+idxFile+"_name_span").html('<a href="front?w_f_act=<%=EncryptionUtil.encrypt("stock.mouvement.downloadPieceJointe")%>&pj=<%=EncryptionUtil.encrypt(mvmBean.getId().toString())%>&nm=<%=key%>" target="_blank"><%=key%></a> | <a href="front?w_f_act=<%=EncryptionUtil.encrypt("stock.mouvement.downloadPieceJointe")%>&pj=<%=EncryptionUtil.encrypt(mvmBean.getId().toString())%>&nm=<%=key%>&isdown=1" target="downloadframe"><i class="fa fa-cloud-download"></i></a>');
			$("#photo"+idxFile+"_name").val('<%=key%>');
			idxFile++;
	<%}
}%>
	
<%-- 		<%if(typeMouvement.equals("t") && ControllerUtil.isEditionCreateAction(request)){%> --%>
// 			$("#div_detail").css('display', 'none');
<%-- 		<%}%> --%>

		$("#generer_bl").click(function(){
			executePartialAjax($(this), '<%=EncryptionUtil.encrypt("stock.mouvement.genererNumBL")%>', 'mouvement.num_bl', true, true, null, true);
		});
		$("#generer_fac").click(function(){
			executePartialAjax($(this), '<%=EncryptionUtil.encrypt("stock.mouvement.genererNumFac")%>', 'mouvement.num_facture', true, true, null, true);
		});
		
		$(document).off('click', '#add_ctrl');
		$(document).on('click', '#add_ctrl', function(){
			var contentTr = $("#ctrl_gpt").html();
			var cpt = $("select[id^='opc_article']").length + 1;
			contentTr = (contentTr ? contentTr.replace(new RegExp("_0", 'g'), "_"+ cpt) : null);
			
			if(contentTr != null){
				$("#ctrl_table").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
			}
			//
			//$("select[id='opc_article\\.id_" + cpt + "']").select2({allowClear: true});
			loadSelectAuto("opc_article\\.id_" + cpt);
			
			  $('#date_peremption_' + cpt).datepicker({
		    	todayBtn: true,
		    	clearBtn: true,
			    language: "fr",
			    autoclose: true,
			    todayHighlight: true
		    });
			 $("select[id='opc_article\\.id_" + cpt + "']").css("width", "100% !important");
			 $("#edit_article_"+cpt).css('display', 'none');
			 
			 if(!$("#div_detail").attr("full")){
			 	$("html, body").animate({ scrollTop: $(document).height() }, 500);
			 }
		});
		$(document).off('click', '#delete_cont');
		$(document).on('click', "#delete_cont", function() {
			$(this).closest("tr").remove();
			var idx = $(this).attr("name").substring(4);
			executePartialInputAjax("data-form", '<%=EncryptionUtil.encrypt("stock.mouvement.refreshDataMtt")%>', true, true, 'cpt='+idx);
		});
		
		$(document).off('change', 'select[id^="opc_article"]');
		$(document).on('change', "select[id^='opc_article']", function() {
			var idx = $(this).attr("id").substring($(this).attr("id").lastIndexOf("_")+1);
			<%if (typeMouvement.equals("a") || typeMouvement.equals("v") || typeMouvement.equals("av")) {%>
				executePartialInputAjax("data-form", '<%=EncryptionUtil.encrypt("stock.mouvement.refreshDataMtt")%>', true, true, 'isArt=1&cpt='+idx);
			<%}%>
			var currVal = $("option:selected", $(this)).attr("value");
			
			if(currVal){
				$('#edit_article_'+idx).attr("params", "art="+currVal);
				$('#edit_article_'+idx).css('display', 'block');
			} else{
				$('#edit_article_'+idx).css('display', 'none');
			}
		});
		
// 		$(document).off('change', '#mouvement\\.type_transfert');
// 		$(document).on('change', "#mouvement\\.type_transfert", function() {
// 			manageTypeTransfert();
// 		});
		
		<%if (typeMouvement.equals("a") || typeMouvement.equals("v") || typeMouvement.equals("av")) {%>
			$(document).off('change', 'input[id^="is_remise_"],input[id^="remise_"],input[id^="prix_ttc_"], input[id^="prix_ht_"], select[id^="opc_tva_enum"], input[id^="quantite_"], input[id^="prix_vente_"]');
			$(document).on('change', 'input[id^="is_remise_"],input[id^="remise_"],input[id^="prix_ttc_"],input[id^="prix_ht_"], select[id^="opc_tva_enum"], input[id^="quantite_"], input[id^="prix_vente_"]', function(e){
				 e.stopImmediatePropagation();
		         e.preventDefault();
		            
				var idx = $(this).attr("id").substring($(this).attr("id").lastIndexOf("_")+1);
				var isTtcField = ($(this).attr("id").indexOf('prix_ttc_') != -1 || $(this).attr("id").indexOf('prix_vente_') != -1);
				executePartialInputAjax("data-form", '<%=EncryptionUtil.encrypt("stock.mouvement.refreshDataMtt")%>', true, true, 'isTtc='+(isTtcField?'1':'0')+'&cpt='+idx);
			});
		<%}%>	
			
		// Peulper les donner cheque fourniseur
		setTimeout(function() {
// 			manageTypeTransfert();
			$("#opc_article\\.id_0").trigger("change");//Recalcul total all lines
		}, 100);
		
		<%if (request.getAttribute("is_inv_prev") != null) {%>
		$("#ctrl_table").find("input, select").attr("disabled", "disabled").css("background-color", "#eee");
		$("#ctrl_table").find("a").remove();
		<%}%>

		$("#div_detail").css('display', 'inline');
		$("#add_ctrl").css('display', 'inline-block');
		$(document).off('change', '#mouvement\\.mouvementIds').on('change', '#mouvement\\.mouvementIds', function(){
			if($('#mouvement\\.type_transfert') != ''){
				$("#reload").trigger("click");
			}
			if($('#mouvement\\.mouvementIds') != ''){
				$("#reload").show();
			}else{
				$("#reload").hide();
				if(<%=typeMouvement%>=='t' && $('#mouvement\\.type_transfert') != 'A'){
					$("#div_detail").css('display', 'none');
					$("#add_ctrl").css('display', 'none');
				}
			}
		});
		
		$('#mouvement\\.type_transfert').trigger("change");
		
		// Load barre
		manageCodeBarre('<%=EncryptionUtil.encrypt("stock.composant.getArticlesByCodeBarre")%>', "addOptionBarre(html)");
	});
	
	function addOptionBarre(html){
		if(html != ''){
			if(html.indexOf("ERREUR") != -1){
				alertify.error(html);
				return;
			}
			var artJson = jQuery.parseJSON(html);
			
			var opt = '<option value="'+artJson.id+'" selected="selected" id="'+artJson.id+'">';
			if(artJson.code_barre != null){
				opt += '['+artJson.code_barre+']'; 
			}
			opt += artJson.code+'-'+artJson.libelle;
			
			if(artJson.composition != null){
				opt += '('+artJson.composition+')';
			}
			opt += '</option>';
			
			// Ajouter ligne
			$('#add_ctrl').trigger("click");
			var cpt = $("select[id^='opc_article']").length;
			$("#opc_article\\.id_"+cpt).append(opt);
			
			$("#opc_article\\.id_"+cpt).trigger("change");
		}
	}
	
	function loadSelectAuto(idSelect){
	 $("#"+idSelect).select2({
            language: "fr",
            minimumInputLength: 3,
            tags: [],
            placeholder: "Article",
            allowClear: true,
            ajax: {
                url: 'front?w_f_act=<%=EncryptionUtil.encrypt("stock.mouvement.getListArticles")%>&isAct=1',
                dataType: 'json',
                type: "GET",
                quietMillis: 50,
                processResults: function (data) {
                    return {
                        results: $.map(data.items, function (item) {
                            return {
                                text: getArtLib(item),
                                id: item.id
                            }
                        })
                    };
                }
            }
        }); 
	 	$("#"+idSelect).off('select2:select').on('select2:select', function (e) {
		 cleanUpSelected($(this));
		});
	}
	function cleanUpSelected(element){
		if(element){
			if(!$.isNumeric(element.val())){
				element.empty().trigger("change");
			 }
		} else{
			$("select[id^='opc_article']").each(function(){
				if(!$.isNumeric($(this).val())){
					$(this).empty().trigger("change");
				 }
			});
		}
	}
	function getArtLib(item){
		var val = '';
		val += '['+item.code+']'+'-'+item.libelle;
		
		if(item.code_barre && item.code_barre != ''){
			val += '['+item.code_barre+'] ';	
		}
		
		if(item.composition && item.composition != '' && item.composition != 'undefined'){
			val += '('+item.composition+')';	
		}
		
		return val;
	}
	
</script>


<img class="imgBarCode" src="resources/framework/img/barcode_scanner.png" style="width: 20px; position: absolute; right: 17px; top: -27px;" title="Lecteur code barre utilisable sur cet écran">

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion de stock</li>
		<li>Fiche <%=typeMouvementEnum.getLibelle()%></li>
		<li class="active">Edition</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<c:if test="${!is_mois_clos && mouvement.origine_id == null}">
			<std:link actionGroup="U" classStyle="btn btn-default" action="stock.mouvement.work_init_update" workId="${mouvement.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		</c:if>

		<%
			if (ControllerUtil.getMenuAttribute("IS_TRV_MNU", request) != null) {
		%>
		<std:link classStyle="btn btn-default" action="stock.travaux.work_edit" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour aux travaux" />
		<%
			} else {
		%>
		<std:link classStyle="btn btn-default" action='<%=StringUtil.isTrue("" + ControllerUtil.getMenuAttribute("IS_GRP_VIEW", request))
		? "stock.mouvement.find_mvm_groupe"
		: "stock.mouvement.work_find"%>' params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
		<%
			}
		%>

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

	<c:set var="encryptionUtil" value="<%=new EncryptionUtil()%>" />


	<div class="row">
		<div class="col-lg-12 col-sm-12 col-xs-12">
			<div class="widget flat radius-bordered">

				<div class="widget-body">
					<div id="mvmWizard" class="wizard" data-target="#mvmWizard-steps">
						<ul class="steps">
							<li data-target="#mvmWizardstep1" onclick="$('#newtWizardBtn').show();" class="active"><span class="step">1</span>Description<span class="chevron"></span></li>
							<li data-target="#mvmWizardstep2"><span class="step">2</span>Détail<span class="chevron"></span></li>
						</ul>
						<div class="actions" id="mvmWizard-actions">
							<button onclick="$('#newtWizardBtn').show();" type="button" class="btn btn-default btn-sm shiny btn-prev">
								<i class="fa fa-angle-left"></i>Préc
							</button>
							<button onclick="$('#newtWizardBtn').hide();" id="newtWizardBtn" type="button" class="btn btn-default btn-sm shiny btn-next" data-last="finish">
								Suiv<i class="fa fa-angle-right"></i>
							</button>
						</div>
					</div>

					<std:form name="data-form">
						<div class="step-content" id="mvmWizard-steps">
							<div class="step-pane active" id="mvmWizardstep1">
								<input type="hidden" name="MAX_FILE_SIZE" value="2097152" /> <input type="hidden" name="is_inv_prev" value="${is_inv_prev?'true':'false' }"> <input type="hidden" name="mouvement.opc_mouvement.id" value="${encryptionUtil.encrypt(mouvement.opc_mouvement.id.toString()) }"> <input type="hidden" name="mouvement.opc_demande.id" value="${encryptionUtil.encrypt(mouvement.opc_demande.id.toString()) }">

								<div style="display: none;">
									<std:select name="num_cheque_all" type="long" key="id" labels="num_cheque" hiddenkey="opc_fournisseur.id" data="${listChequeFournisseur }" style="display:none;">
										<std:ajax action="stock.mouvement.work_init_update" skipInit="false" event="change" target="type_tranfert_div;div_detail" params="reload=1" />
									</std:select>
								</div>

								<!-- widget grid -->
								<div class="widget">

									<div class="widget-body" id="mvm_body">
										<div class="row">

											<input type="hidden" name="mouvement.type_mvmnt" value="<%=typeMouvement%>">

											<div class="form-group" style="padding-left: 30px; font-size: 15px;">
												<b>Informations</b>
											</div>
											<div class="form-group">
												<std:label classStyle="control-label col-md-2" valueKey="mouvement.date_mouvement" />
												<div class="col-md-2">
													<std:date name="mouvement.date_mouvement" required="true" readOnly="${is_inv_prev?'true':'false' }" />
												</div>
											</div>
											<div class="form-group">
												<%
													String libNumero = "Numéro ";
												if (typeMouvement.equals(TYPE_MOUVEMENT_ENUM.dv.toString())) {
													libNumero += "de devis";
												} else if (typeMouvement.equals(TYPE_MOUVEMENT_ENUM.cm.toString())) {
													libNumero += "de commande";
												} else if (typeMouvement.equals(TYPE_MOUVEMENT_ENUM.av.toString())) {
													libNumero += "d'avoir";
												} else if (typeMouvement.equals(TYPE_MOUVEMENT_ENUM.t.toString())) {
													libNumero += "transfert";
												} else if (typeMouvement.equals(TYPE_MOUVEMENT_ENUM.tr.toString())) {
													libNumero += "transformation";
												}
												%>

												<std:label classStyle="control-label col-md-2" value="<%=libNumero%>" />
												<div class="col-md-2">
													<std:text name="mouvement.num_bl" iskey="true" type="string" placeholder="<%=libNumero%>" maxlength="50" style="width: 90%;float: left;" />
													<%
														if (ControllerUtil.isEditionCreateAction(request)) {
													%>
													<a class="refresh-num" id="generer_bl" href="javascript:" title="G&eacute;n&eacute;rer un num&eacute;ro de bon de transfert"> <i class="fa fa-refresh"></i>
													</a>
													<%
														}
													%>
												</div>

												<%
													if (typeMouvement.equals(TYPE_MOUVEMENT_ENUM.a.toString())) {
												%>

												<std:label classStyle="control-label col-md-2" value="Numéro de reçu" />
												<div class="col-md-2">
													<std:text name="mouvement.num_recu" type="string" placeholder="Numéro de reçu" maxlength="50" style="width: 90%;" />
												</div>

												<std:label classStyle="control-label col-md-2" valueKey="mouvement.num_facture" />
												<div class="col-md-2">
													<std:text name="mouvement.num_facture" type="string" placeholderKey="mouvement.num_facture" maxlength="50" style="width: 90%;float: left;" />
													<%
														if (ControllerUtil.isEditionCreateAction(request)) {
													%>
													<a class="refresh-num" id="generer_fac" href="javascript:" title="G&eacute;n&eacute;rer un num&eacute;ro de facture"> <i class="fa fa-refresh"></i>
													</a>
													<%
														}
													%>
												</div>
												<%
													}
												%>
											</div>

											<%
												if ("a".equals(typeMouvement) || "v".equals(typeMouvement) || "av".equals(typeMouvement)
													|| typeMouvement.equals(TYPE_MOUVEMENT_ENUM.dv.toString())
													|| typeMouvement.equals(TYPE_MOUVEMENT_ENUM.cm.toString())) {
											%>
											<div class="form-group">
												<%
													if (TYPE_MOUVEMENT_ENUM.a.toString().equals(typeMouvement) || TYPE_MOUVEMENT_ENUM.av.toString().equals(typeMouvement)
														|| typeMouvement.equals(TYPE_MOUVEMENT_ENUM.cm.toString())) {
												%>
												<std:label classStyle="control-label col-md-2" valueKey="mouvement.opc_fournisseur" />
												<div class="col-md-4">
													<std:select name="mouvement.opc_fournisseur.id" type="long" key="id" hiddenkey="id" style="width:80%" labels="libelle;' ';marque" data="${listeFournisseur}" groupKey="opc_famille.id" groupLabels="opc_famille.code;'-';opc_famille.libelle" required="true" width="100%" />
													<std:link action="stock.fournisseur.work_init_create" style="float: right;position: absolute;margin-left: 2%;" icon="fa fa-plus" noJsValidate="true" actionGroup="C" params="w_nos=1" />
												</div>
												<%
													} else {
												%>
												<std:label classStyle="control-label col-md-2" value="Client" />
												<div class="col-md-4">
													<std:select name="mouvement.opc_client.id" type="long" key="id" hiddenkey="id" labels="nom;' ';prenom" data="${listClient}" width="80%" />
													<std:link action="pers.client.work_init_create" style="float: right;position: absolute;margin-left: 2%;" icon="fa fa-plus" noJsValidate="true" actionGroup="C" params="w_nos=1" />

												</div>

												<%
													}
												%>
												<%
													if ("a".equals(typeMouvement) || "v".equals(typeMouvement)) {
												%>
												<std:label classStyle="control-label col-md-2" valueKey="mouvement.is_facture_comptable" />
												<div class="col-md-4">
													<std:checkbox name="mouvement.is_facture_comptable" />
												</div>
												<%
													}
												%>
												<%
													if ("cm".equals(typeMouvement)) {
												%>
												<std:label classStyle="control-label col-md-2" value="Chiffrer ce bon" />
												<div class="col-md-4">
													<std:checkbox name="mouvement.is_chiffre" />
												</div>
												<%
													}
												%>
											</div>
											<hr>
											<!-- **************************** FINANCEMENT BLOC ********************** -->
											<%
												if (!typeMouvement.equals(TYPE_MOUVEMENT_ENUM.dv.toString())
													&& !typeMouvement.equals(TYPE_MOUVEMENT_ENUM.cm.toString())) {
											%>
											<div class="form-group" style="padding-left: 30px; font-size: 15px;">
												<b>Modes de paiement</b>
											</div>
											<c:set var="menu_scope.PAIEMENT_DATA" value="${mouvement.getList_paiement() }" scope="session" />
											<div class="form-group" id="finance_bloc">
												<jsp:include page="/domaine/compta/paiement_consult.jsp"></jsp:include>
											</div>
											<%
												}
											%>
											<!-- **************************** FIN FINANCEMENT BLOC ********************** -->
											<%
												}
											%>

											<hr>

										<%if (!typeMouvement.equals(TYPE_MOUVEMENT_ENUM.cm.toString())) { %>
											<div class="form-group">
												<%if (!typeMouvement.equals(TYPE_MOUVEMENT_ENUM.rt.toString()) && !typeMouvement.equals(TYPE_MOUVEMENT_ENUM.a.toString())
														&& !typeMouvement.equals(TYPE_MOUVEMENT_ENUM.dv.toString())) { %>
													<std:label classStyle="control-label col-md-2" valueKey="mouvement.opc_emplacement" />
													<div class="col-md-4">
													<%if (typeMouvement.equals(TYPE_MOUVEMENT_ENUM.t.toString()) || typeMouvement.equals(TYPE_MOUVEMENT_ENUM.av.toString())) { %>
														<std:select name="mouvement.opc_emplacement.id" disable="${is_inv_prev?'true':'false' }" type="long" key="id" labels="titre" data="${listeOrigine}" required="true" width="80%" />
														<std:linkPopup action="stock.emplacement.work_init_create" style="float: right;position: absolute;margin-left: 2%;" icon="fa fa-plus" noJsValidate="true" actionGroup="C" params="w_nos=1" />
													<%} else {%>
														<std:select name="mouvement.opc_destination.id" disable="${is_inv_prev?'true':'false' }" type="long" key="id" labels="titre" data="${listeDestination}" required="true" width="80%" />
														<std:linkPopup action="stock.emplacement.work_init_create" style="float: right;position: absolute;margin-left: 2%;" icon="fa fa-plus" noJsValidate="true" actionGroup="C" params="w_nos=1" />
													<%} %>
												</div>
												<% } %>
												<% if (TYPE_MOUVEMENT_ENUM.t.toString().equals(typeMouvement) || TYPE_MOUVEMENT_ENUM.a.toString().equals(typeMouvement)
														|| TYPE_MOUVEMENT_ENUM.rt.toString().equals(typeMouvement)) { %>
													<std:label classStyle="control-label col-md-2" valueKey="mouvement.opc_destination" />
													<div class="col-md-4">
														<std:select name="mouvement.opc_destination.id" disable="${is_inv_prev?'true':'false' }" type="long" key="id" labels="titre" data="${listeDestination}" required="true" width="80%" />
														<std:linkPopup action="stock.emplacement.work_init_create" style="float: right;position: absolute;margin-left: 2%;" icon="fa fa-plus" noJsValidate="true" actionGroup="C" params="w_nos=1" />
													</div>
												<%} %>
												<% if (typeMouvement.equals("p")) { %>
												<std:label classStyle="control-label col-md-2" valueKey="mouvement.opc_type_perte_enum" />
												<div class="col-sm-4">
													<div style="float: left;">
														<std:select type="long" required="true" style="float:left;" width="95%" name="mouvement.opc_type_perte_enum.id" data="${typePerte }" labels="libelle" key="id" />
													</div>
													<div style="padding-right: 4px; float: left;">
														<std:linkPopup actionGroup="C" params="w_nos=1&valEnum.opc_typenum.id=${typePerteEnumId }" classStyle="" noJsValidate="true" action="admin.valTypeEnum.work_init_create">
															<i class="fa fa-reorder" style="color: blue;" title="Liste &eacute;num&eacute;r&eacute;e"></i>
														</std:linkPopup>
													</div>
												</div>
												<% } %>
												<% if (typeMouvement.equals("c")) { %>
												<std:label classStyle="control-label col-md-2" valueKey="mouvement.opc_famille_consommation" />
												<div class="col-md-4">
													<std:select name="mouvement.opc_famille_consommation.id" type="long" key="id" labels="code;'-';libelle" data="${familleConsommation}" isTree="true" width="80%" />
													<std:linkPopup action="stock.famille.work_init_create" style="float: right;position: absolute;margin-left: 2%;" icon="fa fa-plus" noJsValidate="true" actionGroup="C" params="w_nos=1&tp=CO" />


												</div>
												<% } %>
											</div>
											<% } %>

											<% if (typeMouvement.equals("a")) { %>
											<div class="form-group" style="padding-left: 30px; font-size: 15px;">
												<b>Immobilisation</b>
											</div>
											<div class="form-group">
												<std:label classStyle="control-label col-md-3" value="Date début amortissement" />
												<div class="col-md-3">
													<std:date name="chargeDivers.date_debut_amo" />
												</div>
												<std:label classStyle="control-label col-md-3" value="Nombre d'année" />
												<div class="col-md-3">
													<std:text name="chargeDivers.nbr_annee_amo" type="long" maxlength="3" style="width:50px;" />
												</div>
											</div>
											<hr>
											<%
												}
											%>
											<div class="form-group">
												<std:label classStyle="control-label col-md-2" valueKey="mouvement.commentaire" />
												<div class="col-md-4">
													<std:textarea name="mouvement.commentaire" rows="3" cols="80" />
												</div>
											</div>

											<div class="form-group" id="type_div">
												<%
													if (TYPE_MOUVEMENT_ENUM.t.toString().equals(typeMouvement) || TYPE_MOUVEMENT_ENUM.av.toString().equals(typeMouvement)
														|| TYPE_MOUVEMENT_ENUM.rt.toString().equals(typeMouvement)) {
												%>
												<std:label classStyle="control-label col-md-2" value='<%="av".equals(typeMouvement) ? "Type d\'avoir" : "Type de transfert"%>' />
												<div class="col-md-4">
													<std:select mode="std" name="mouvement.type_transfert" type="String" data="${listType }" addBlank="false" required="true" width="250px">
														<std:ajax action="stock.mouvement.work_init_update" skipInit="false" event="change" target="type_tranfert_div;div_detail" params="reload=1" />
													</std:select>
												</div>
												<%
													}
												%>
											</div>

											<!-- Source transfrt -->
											<div id="type_tranfert_div">
												<c:if test="${not empty mouvement.type_transfert and mouvement.type_transfert != 'A'}">
													<div class="form-group">
														<std:label classStyle="control-label col-md-2" value="${libelleSource }" />
														<div class="col-md-4">
															<c:choose>
																<c:when test="${mouvement.type_transfert=='F'}">
																	<std:select classStyle="select2" name="mouvement.mouvementIds" type="long[]" key="id" labels="num_facture" data="${listData }" multiple="true" required="true" width="80%" />
																</c:when>
																<c:when test="${mouvement.type_transfert=='B'}">
																	<std:select classStyle="select2" name="mouvement.mouvementIds" type="long[]" key="id" labels="num_bl" data="${listData }" multiple="true" required="true" width="80%" />
																</c:when>
																<c:when test="${mouvement.type_transfert=='R'}">
																	<std:select classStyle="select2" name="mouvement.mouvementIds" type="long[]" key="id" labels="num_bl" data="${listData }" multiple="true" required="true" width="80%" />
																</c:when>
																<c:when test="${mouvement.type_transfert=='P'}">
																	<std:select classStyle="select2" name="mouvement.mouvementIds" type="long[]" key="id" labels="code;' ';libelle" data="${listData }" multiple="true" required="true" width="80%" />
																</c:when>
															</c:choose>
															<%
																if (ControllerUtil.isEditionWritePage(request)) {
															%>
															<std:link action="stock.mouvement.refreshTranfertArticles" id="reload" params="skipI=1&skipP=1" noJsValidate="true" targetDiv="div_detail" tooltip="Charger les composants pour modification" classStyle="btn btn-success" icon="fa fa-refresh" />
															<%
																}
															%>
														</div>
													</div>
												</c:if>
											</div>
										</div>

										<!-- Generic Form -->
										<jsp:include page="/domaine/administration/dataValue_form.jsp" />

									</div>
								</div>



								<!-- Pieces -->
								<div class="widget">
									<div class="widget-header bordered-bottom bordered-blue">
										<span class="widget-caption"> <%
										 	if (ControllerUtil.isEditionWritePage(request)) {
										 %> <a href="javascript:void(0);" id="addFile" targetBtn="C" class="btn btn-default" title="Ajouter pi&egrave;ce jointe" style="margin-top: -2px;"> <i class="fa fa-3x fa-plus"> </i> Ajouter pi&egrave;ces jointes
																				</a> <%
										 	}
										 %>
										</span>
									</div>
									<div class="widget-body">
										<!-- Photos -->
										<div class="row" id="row_file">
											<div id="fileLoadDiv" style="display: none;">
												<div class="col-md-2">
													<div class="col-sm-12">
														<div id="photoX_div" style="border: 1px solid #c6d983; border-radius: 25px; text-align: center; height: 120px; background: url('resources/framework/img/picture_file.png') no-repeat center; cursor: pointer;">
															<span style="font-size: 11px;">Fichier</span>
														</div>
													</div>
													<div class="col-sm-12" style="text-align: center; color: olive;">
														<span id="photoX_name_span"></span> <input type="hidden" name="photoX_name" id="photoX_name">
													</div>
													<div class="col-sm-12">
														<!-- Separator -->
														<div id="sep_photoX" style="margin-bottom: 5px; height: 20px; text-align: center;">
															<a href="javascript:"><b>X</b></a>
														</div>
														<!-- End -->
														<input type="file" name="photoX" id="photoX_div_file" style="display: none;" accept="image/*,.doc,.pdf,.xls,.xlsx,.doc,.docx,.txt">
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>

							</div>
							<div class="step-pane" id="mvmWizardstep2">

								<div class="widget" id="div_detail">
									<jsp:include page="/domaine/stock/mouvement_detail_edit.jsp"></jsp:include>
								</div>

								<hr>
								<div class="form-actions">
									<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
										<std:button actionGroup="M" classStyle="btn btn-success" onClick="cleanUpSelected();" action="stock.mouvement.work_merge" workId="${mouvement.id }" icon="fa-save" value="Sauvegarder" />
										<%
											if (request.getAttribute("is_inv_prev") == null) {
										%>
										<std:button actionGroup="D" classStyle="btn btn-danger" action="stock.mouvement.work_delete" workId="${mouvement.id }" icon="fa-trash-o" value="Supprimer" />
										<%
											}
										%>
									</div>
								</div>

							</div>
						</div>
					</std:form>

				</div>
			</div>
		</div>
	</div>
</div>
