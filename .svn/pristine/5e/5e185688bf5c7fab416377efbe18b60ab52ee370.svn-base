<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.util.audit.ReplicationGenerationEventListener"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.constante.ActionConstante"%>
<%@page import="framework.model.common.constante.ProjectConstante"%>
<%@page import="appli.controller.domaine.stock.action.ArticleAction"%>
<%@page import="appli.controller.domaine.stock.bean.ArticleBean"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
boolean isRestau = ContextAppli.IS_RESTAU_ENV();
%>
<script type="text/javascript">
	$(document).ready(function() {
		$("#generer_code").off("click").on("click", function(){
			if($.trim($("#article\\.<%=isRestau ? "opc_famille_cuisine":"opc_famille_stock"%>\\.id").val()) == ''){
				alertify.error("Veuillez sélectionner une famille.");
				return;
			}
			executePartialAjax($(this), '<%=EncryptionUtil.encrypt("stock.article.generer_code")%>', 'article.code', true, true, null, true);
		});
		$("#article\\.is_noncaisse").off("click").on("click", function(){
			manageDestCaisse();
		});
	});

	function manageDestCaisse(){
		if($("#article\\.is_noncaisse").prop("checked")){
			$("#article\\.is_nonmobile").prop("checked", true);
			$("#article\\.is_nonmobile").prop("disabled", true);
		} else{
			$("#article\\.is_nonmobile").prop("disabled", null);
		}
	}
	
	function calculMtt(){
		var pvnt = getFloat($("#article\\.prix_vente").val());
		$("#tbl_totaux span").html("");
		
		var prixHt = 0;var qte = 0;var prixTtc = 0;
		
		$("input[id^='quantite_']").each(function(){
			var idx = $(this).attr("id").substring(9);
			var q = getFloat($(this).val());
			
			q = (q == null) ? 0 : q;
			qte = qte + q;
			if(getFloat($("#prix_ht_"+idx).val()) != null){
				prixHt = prixHt + (getFloat($("#prix_ht_"+idx).val()) * q);
			}
			if(getFloat($("#prix_ttc_"+idx).val()) != null){
				prixTtc = prixTtc + (getFloat($("#prix_ttc_"+idx).val()) * q);
			}
		});
		
		if(pvnt != null){
			var tauxTva = <%=ContextGloabalAppli.getGlobalConfig("TVA_VENTE")%>
			if($.trim(tauxTva) == ''){
				tauxTva = 0;
			}
			var pvntTTC = pvnt;
			var margeTTC =  prixTtc>0 ? ((pvntTTC-prixTtc)*100)/prixTtc : 0.00;
			var margeDhsTTC = prixTtc>0 ? (pvntTTC-prixTtc) : 0.00;
			
			var htmlVnt =   "| <span style='color: black;'>Prix vente TTC :</span> "+pvntTTC.toFixed(<%=ContextGloabalAppli.getNbrDecimalSaisie()%>)+" Dhs";
			var htmlMarge = "| <span style='color: black;'>Marge TTC % :</span> "+margeTTC.toFixed(<%=ContextGloabalAppli.getNbrDecimalSaisie()%>)+" %";
			var htmlMargeMtt = "| <span style='color: black;'>Marge TTC Dhs :</span> "+margeDhsTTC.toFixed(<%=ContextGloabalAppli.getNbrDecimalSaisie()%>)+" Dhs";
			
			$("#span_marge_vnt").html(htmlVnt);
			$("#span_marge_poucent").html(htmlMarge);
			$("#span_marge_mtt").html(htmlMargeMtt);
		}
		
		$("#td_qte").html(qte.toFixed(<%=ContextGloabalAppli.getNbrDecimalSaisie()%>));
		$("#tt_ht").html(prixHt.toFixed(<%=ContextGloabalAppli.getNbrDecimalSaisie()%>));
		$("#tt_ttc").html(prixTtc.toFixed(<%=ContextGloabalAppli.getNbrDecimalSaisie()%>));
	}
	function calculPrix(idx){
		var composant = $("#tva_enum_"+idx);
		var hidden = $("option:selected", composant).attr("hiddenkey");
		if(hidden){
			var dataArray = hidden.split('|');
			var taux = parseFloat(dataArray[0]);
			$("#prix_ttc_"+idx).val("");
			
			var ht = getFloat($("#prix_ht_"+idx).val());
			if(ht && ht != null){
				var ttc = ht + (ht*taux)/100;
				$("#prix_ttc_"+idx).val(ttc.toFixed(<%=ContextGloabalAppli.getNbrDecimalSaisie()%>));   
			}
		}
	}
	
	function manageInfos(idSelect, option){
		var idx = idSelect.substring(25);
		var hidden = option.attr("infos");
		var val = $("#"+getJQueryName(idSelect)).val();
		
		if(!val  || val == ''){
			 $("#lnk_comp_"+idx).hide();
		} else{
			 $("#lnk_comp_"+idx).attr("params", "art="+val).attr("noVal", "true");
			 $("#lnk_comp_"+idx).show();
		}
		
		$("#tva_enum_"+idx).val("");
	 	$("#tva_enum_"+idx).trigger("change");
	 	$("#prix_ht_"+idx).val("");
	 	
		if(hidden){
			var dataArray = hidden.split('_');
			var ht = parseFloat(dataArray[0]);
			var tva = dataArray[1];
			var taux = parseFloat(dataArray[2]);
//				var uniteQteVente = parseInt($.trim(dataArray[3])==''?1:dataArray[3]);//unite_vente_quantite
//				ht = ht / uniteQteVente;
			
			if(ht && ht != null){
				$("#prix_ht_"+idx).val(ht.toFixed(<%=ContextGloabalAppli.getNbrDecimalSaisie()%>));
			}
		 	$("#tva_enum_"+idx).val(tva);
		 	$("#tva_enum_"+idx).trigger("change");
		}
	}
	
	$(document).ready(function() {
		$(document).off('change', 'select[id^="opc_article_composant"]').on('change', "select[id^='opc_article_composant']", function() {
			manageInfos($(this).attr("id"), $("option:selected", $(this)))
		});
		
		// Calcul au d�marrage------------
		setTimeout(function(){
			manageDestCaisse();
			calculMtt();
			
			<% if(isRestau){%>
				manageBalanceInfo();
			<%}%>
		}, 1000);
		
		$("input[id^='quantite_']").each(function(){
			var idx = $(this).attr("id").substring(9);
			calculPrix(idx);
		});
		//----------------------------------
		$(document).off('change', 'select[id^="tva_enum_"]');
		$(document).on('change', "select[id^='tva_enum_']", function() {
			var idx = $(this).attr("id").substring(9);
			calculPrix(idx);
			// Rafraich marge
			calculMtt();
		});
		
		$(document).off('change', "#article\\.prix_vente, input[id^='quantite_']");
		$(document).on('change', "#article\\.prix_vente, input[id^='quantite_']", function(){
			calculMtt();
		});
		
		$("#generer_balKey").off("click").on("click", function(){
			if($.trim($("#article\\.opc_famille_stock\\.id").val()) == ''){
				alertify.error("Veuillez sélectionner une famille.");
				return;
			}
			executePartialAjax($(this), '<%=EncryptionUtil.encrypt("stock.composant.generer_balanceKey")%>', 'article.code_direct_bal', true, true, null, true);
		});
		
		$(document).off('click', '#add_ctrl');
		$(document).on('click', '#add_ctrl', function(){
			var contentTr = $("#ctrl_gpt").html();
			var cpt = $("select[id^='opc_article_composant']").length + 1;
			contentTr = contentTr.replace(new RegExp("_0", 'g'), "_"+ cpt);
			$("#ctrl_table").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
			// Ligne � la fin
			$("#ctrl_table").append($("#tr_total"));
			loadSelectAuto("opc_article_composant.id_" + cpt);
			$("select[id='opc_article_composant\\.id_" + cpt + "']").css("width", "100% !important");
			 $("#lnk_comp_"+cpt).css('display', 'none');
		});
		$(document).off('click', '#delete_cont');
		$(document).on('click', "#delete_cont", function() {
			$(this).closest("tr").remove();
			calculMtt();
			
			<% if(isRestau){%>
				manageBalanceInfo();
			<%}%>
		});
						
			<%if(ControllerUtil.isEditionWritePage(request)){%>
				loadInputFileEvents();
			<%} else{%>
				$("#sep_photo1").remove();
			<%}%>
			
			<%ArticleBean articleBean = (ArticleBean)request.getAttribute("article");
			if(articleBean != null && articleBean.getId() != null){ 
				IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
				Map<String, byte[]> dataimg = service.getDataImage(articleBean.getId(), "article");
				if(dataimg.size() > 0){
					String fileName = dataimg.entrySet().iterator().next().getKey();
					byte[] value = dataimg.entrySet().iterator().next().getValue();%>
					$("#photo1_div").css("background", "");
			 		$("#photo1_div").html("<img src='data:image/jpeg;base64,<%=FileUtil.getByte64(value) %>' width='120' height='120'/>");
			 		$("#photo1_name_span").text("<%=fileName%>");
			 		$("#photo1_name").val("<%=fileName%>");
				<%}
			}%>
			
			
			$(".selectCustom").each(function(){
				<%if(!ControllerUtil.isEditionWritePage(request)){%>
					$(this).attr("disabled", "disabled");
				<%}%>
				loadSelectAuto($(this).attr("id"));
			});
		
	});
	function loadSelectAuto(idSelect){
		 $("#"+getJQueryName(idSelect)).select2({
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
	                                value: item.prix_achat_ht+"_"+(item.opc_tva_enum!=null?item.opc_tva_enum.id+"_"+item.opc_tva_enum.libelle:""),
	                                id: item.id
	                            }
	                        })
	                    };
	                }
	            }
	        });
		 	$("#"+getJQueryName(idSelect)).off('select2:select').on('select2:select', function (e) {
		 		var data = e.params.data;
		 	    var opt = $("select option[value=" + data.id + "]");
		 	    opt.attr('infos', data.value);
			 	
		 	    cleanUpSelected($(this));
			 	//
			 	manageInfos(idSelect, opt);
			});
		}
	
	function cleanUpSelected(element){
		if(element){
			if(!$.isNumeric(element.val())){
				element.empty().trigger("change");
			 }
		} else{
			$("select[id^='opc_article_composant']").each(function(){
				if(!$.isNumeric($(this).val())){
					$(this).empty().trigger("change");
				 }
			});
		}
	}
	function getArtLib(item){
		var val = '';
		if(item.code_barre && item.code_barre != ''){
			val += item.code_barre+'-';	
		}
		if(item.code && item.code != ''){
			val += item.code+"-";	
		}
		val += item.libelle;
		if(item.composition && item.composition != ''){
			val += '('+item.composition+')';	
		}
		
		return val;
	}
	function manageBalanceInfo(){
		<% if(isRestau){%>
			var famStr = '<%=request.getAttribute("familleBalance") %>';
			if(famStr.indexOf("|"+$("#article\\.opc_famille_cuisine\\.id").val()+"|") != -1){
				$("#div_bal").show();		
			} else{
				$("#div_bal").hide();
			}
		<%}%>	
	}
</script>

<%
	boolean isDuplic = ActionConstante.INIT_DUPLIC.equals(ControllerUtil.getAction(request));
	boolean isMargeCaissier = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("MARGE_CAISSIER"));
	boolean isOptStock = ContextGloabalAppli.getAbonementBean().isOptStock();
	boolean isErp = "erp".equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
%>

<!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Caisse enregistreuse</li>
         <li>Fiche article
		</li>
         <li class="active">Edition</li>
     </ul>
 </div>
 
	<div class="page-header position-relative">
		<div class="header-title" style="padding-top: 4px;">
	        <c:if test="${empty isEditable or isEditable }">
		        <std:link actionGroup="U" classStyle="btn btn-default" action="stock.article.work_init_update" workId="${article.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		        <std:link actionGroup="DU" classStyle="btn btn-default" action="stock.article.work_init_duplic" workId="${article.id }" icon="fa-3x fa fa-copy" tooltip="Dupliquer" />
	        </c:if>
			<std:link classStyle="btn btn-default" action="stock.article.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
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
	
	
	<std:form name="data-form">
        <input type="hidden" name="images_uploaded" id="images_uploaded" value=";">
		<input type="hidden" name="images_names" id="images_names" value=";">
		<input type="hidden" name="MAX_FILE_SIZE" value="2097152" />
	<div class="widget">
      	<div class="widget-header bordered-bottom bordered-blue">
	      	<span class="widget-caption">Fiche article</span>
			<std:link actionGroup="U" classStyle="btn btn-default" action="stock.article.work_init_update" workId="${articleId }" icon="fa-3x fa-pencil" tooltip="Modifier" targetDiv="generic_modal_body" />
     	</div>
         <div class="widget-body">
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" valueKey="article.opc_famille"/>
					<div class="col-md-4">
					<%if(isRestau){ %>
						<std:select name="article.opc_famille_cuisine.id" type="long" key="id" labels="code;'-';libelle" data="${listeFaimlle}" onChange="manageBalanceInfo();" required="true" isTree="true" width="100%" />
					<%} else{ %>
						<std:select name="article.opc_famille_stock.id" type="long" key="id" labels="code;'-';libelle" data="${listeFaimlle}" required="true" isTree="true" width="100%" />
					<%} %>
					</div>
				</div>	
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" valueKey="article.reference" />
					<div class="col-md-4">
						<std:text name="article.code" type="string" placeholderKey="article.reference" required="true" maxlength="50" iskey="true" style="width: 100px;float: left;"/>
						<% if(ControllerUtil.isEditionCreateAction(request)){ %>
							<a class="refresh-num" id="generer_code" href="javascript:" title="Générer la réféce d'article" style="right: 50%;">
				            	<i class="fa fa-refresh"></i>
				        	</a>
						<% }%>
					</div>
					<std:label classStyle="control-label col-md-2" valueKey="article.libelle"/>
					<div class="col-md-4">
						<std:text name="article.libelle" type="string" placeholderKey="article.libelle" required="true" maxlength="80"/>
					</div>
				</div>
				
				<%if(isRestau){ %>
					<div class="form-group" style="display: none;" id="div_bal">
						<std:label classStyle="control-label col-md-2" value="Raccourcis balance"/>
						<div class="col-md-4">
							<std:text name="article.code_direct_bal" type="long" placeholder="Raccourcis balance" maxlength="4" style="width: 100px;float: left;"/>
							<% if(ControllerUtil.isEditionCreateAction(request)){ %>
								<a class="refresh-num" id="generer_balKey" href="javascript:" title="Générer la clé">
					            	<i class="fa fa-refresh"></i>
					        	</a>
							<% }%>
						</div>
				</div>
				<%} %>	
					
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" valueKey="article.code_barre"/>
					<div class="col-md-4">
						<std:text name="article.code_barre" type="string" placeholderKey="article.code_barre" style="width: 40%;" maxlength="50"/>
					</div>
				</div>
				<div class="form-group">
					<%if(isRestau){ %>
					<std:label classStyle="control-label col-md-2" valueKey="article.destination"/>
					<div class="col-md-4">
						<std:select name="article.destination" type="string" data="${listeDestination}" required="true" />
					</div>
					<%} %>
					<%if(!isErp){ %>
					<std:label classStyle="control-label col-md-2" value="Ne pas afficher dans caisse"/>
					<div class="col-md-1">
						<std:checkbox name="article.is_noncaisse" />
					</div>
					<std:label classStyle="control-label col-md-2" value="Ne pas afficher dans mobile"/>
					<div class="col-md-1">	
						<std:checkbox name="article.is_nonmobile" />
					</div>
					<%} %>
				</div>
				
				<%if(isRestau){ %>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Temps préparation cuisine"/>
						<div class="col-md-4">
							<std:text name="article.temps_cuis_ref" type="long" maxlength="2" style="width:50px;" />
						</div>
					</div>	
				<%} %>
				
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Image" />
					<div class="col-md-4">
						<div class="col-sm-12">	
							<div id="photo1_div" style="width: 150px;margin-top:10px; border: 1px solid #c6d983; border-radius: 25px; text-align: center; height: 120px; background: url('resources/framework/img/picture_file.png') no-repeat center;cursor: pointer;">
								<span style="font-size: 11px;">Image</span>
							</div>
							<span id="photo1_name_span"></span>
							<input type="hidden" name="photo1_name" id="photo1_name">
						</div>	
						<div class="col-sm-12">
							<!-- Separator -->
							<div id="sep_photo1" style="width:150px;margin-bottom: 5px; height: 20px; text-align: center;">
								<a href="javascript:"><b>X</b></a>
							</div>
							<!-- End -->
							<input type="file" name="photo1" id="photo1_div_file" style="display: none;" accept="image/bmp,image/gif,image/jpg,image/png,image/jpeg">
						</div>
					</div>
				</div>	
				
			<fieldset>
				<legend style="margin-left: 10px;">Informations de vente</legend>	
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" valueKey="article.opc_unite_vente_enum"/>
					<div class="col-md-4">
						<std:select name="article.opc_unite_vente_enum.id" type="long" key="id" labels="libelle" data="${listeUnite}" width="150"/>
						<i class="fa fa-reorder" style="color:blue;" title="Liste énumérée"></i>
					</div>
					<std:label classStyle="control-label col-md-2" valueKey="article.unite_vente_quantite"/>
					<div class="col-md-4">
						<std:text name="article.unite_vente_quantite" type="decimal" placeholderKey="article.unite_vente_quantite" style="width: 40%;" maxlength="20"/>
					</div>
				</div>
				<div class="form-group">
					<%if(!ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE){ %>
						<std:label classStyle="control-label col-md-2" value="Prix vente TTC"/>
						<div class="col-md-4">
							<std:text name="article.prix_vente" type="decimal" required="true" placeholderKey="article.prix_vente" style="width: 40%;" maxlength="14"/>
						</div>
					<%} %>
					<%if(!isErp && isMargeCaissier){%>
						<std:label classStyle="control-label col-md-2" value="Commission de Caissier (%)"/>
						<div class="col-md-4">
							<std:text name="article.taux_marge_caissier" type="decimal" placeholder="Taux (%)" min="0" max="100" size="3" style="width: 40%;float:left;"/>
							<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Taux commission récupérée par le caissier sur la vente de cet article" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
						</div>
					<%}%>
				</div>	
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Marge minimale"/>
					<div class="col-md-4">
						<std:text name="article.min_marge" type="decimal" placeholder="Minimum marge" style="width: 40%;" maxlength="14"/>
					</div>
					<std:label classStyle="control-label col-md-2" value="Montant garantie"/>
					<div class="col-md-4">
						<std:text name="article.mtt_garantie" type="decimal" placeholder="Montant garantie" style="width: 40%;float:left;" maxlength="14"/>
						<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Si l'article nécessite un dépôt de garantie par le client" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
					</div>
				</div>
				
				<%if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE){ %>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Prix de vente spécifique"/>
						<div class="col-md-10">	
							<table style="background-color: #f1f1e8;width: 59%;">
								<c:set var="maprix" value="${article.getMapPrix() }" />
							
								<c:forEach var="ets" items="${listEtablissement }">
									<tr style="line-height: 30px;">
										<td>${ets.nom }</td>
										<td>
											<std:text name="prix_vente_ets_${ets.id }" type="decimal" required="true" placeholderKey="article.prix_vente" style="width: 50%;height:24px;" maxlength="14" value="${maprix.get(ets.id) }"/>
										</td>
									</tr>
								</c:forEach>
							</table>
						</div>
					</div>
				<%} %>
				
			</fieldset>		
				<hr>	
				<!-- Photos -->
			<div class="row">
				<div class="form-group">	
					<std:label classStyle="control-label col-md-2" valueKey="article.description"/>
					<div class="col-md-4">
						<std:textarea name="article.description" rows="5" cols="50" maxlength="255"/>
					</div>
				</div>
		</div>
		
		<!-- Generic Form -->
		<jsp:include page="/domaine/administration/dataValue_form.jsp" />
	
	</div>
	</div>
	
	</div>
	<div class="widget">
	<%if(isOptStock) { %>
         <div class="widget-header bordered-bottom bordered-blue">
            <span class="widget-caption">Composants de cet article</span>
         </div>
     <%} %>    
         <div class="widget-body">
			<%if(isOptStock) { %>
			<div class="row">
				<table id="ctrl_table" style="width: 97%;margin-left: 20px;">
					<tr>
						<th width="20%">Composant</th>
						<th width="14%">Quantité</th>
						<th width="14%">Prix U HT</th>
						<th width="14%">TVA</th>
						<th width="14%">Prix U TTC</th>
						<th width="14%">Destockage <i class="fa fa-info-circle" style="color: green;" title="Conditions de destockage"></i></th>
						<th></th>
					</tr>
					<tr id="ctrl_gpt" style="display: none;">
						<td style="padding-top: 5px; valign="top" nowrap="nowrap">
							<select style="width:250px;border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;" class="slct" name="opc_article_composant.id_0" id="opc_article_composant.id_0" ></select>
							<std:linkPopup id="lnk_comp_0" actionGroup="" classStyle="btn btn-default" action="stock.composant.work_edit" icon="fa-3x fa-eye" tooltip="Consulter" style="display:none;margin-right: 2px;" />
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 110px;" name="quantite_0" type="decimal" placeholderKey="article.quantite"  maxlength="14"/>
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:text name="prix_ht_0" type="decimal" style="width: 110px;" placeholderKey="article.prix_achat_ht" readOnly="true"/>
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:select classStyle="slct" hiddenkey="code" name="tva_enum_0" type="long" key="id" labels="libelle" addBlank="false" data="${listeTva}"/>
						</td>
						<td style="padding-top: 5px; padding-right: 10px;" valign="top">
							<std:text name="prix_ttc_0" type="decimal" style="width: 110px;" placeholder="Prix T.T.C" readOnly="true"/>
						</td>	
						<td valign="top" style="padding-top: 5px; padding-right: 1px;">
							<std:select name="mode_destock_0" data="${dataTypeCmd }" classStyle="slct" type="string"/>
						</td>
						<td valign="top" style="padding-top: 5px; padding-right: 1px;">
							<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
						</td>
					</tr>
					
					<c:set var="cpt" value="${1 }" />
					<c:set var="encodeService" value="<%=new EncryptionUtil()%>" />
					<c:set var="articleAction" value="<%=new ArticleAction()%>" />
					<c:forEach items="${article.list_article }" var="articleDet">
						<%if(!isDuplic){ %>
							<std:hidden name="eaiid_${cpt}" value="${encodeService.encrypt(articleDet.id) }" />
						<%} %>
						<tr>
							<td style="padding-top: 5px; valign="top" nowrap="nowrap">
								<select class="selectCustom" name="opc_article_composant.id_${cpt}" id="opc_article_composant.id_${cpt}" style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;; width: 100% !important;">
									<c:if test="${not empty articleDet.opc_article_composant }">
										<c:set var="item" value="${articleDet.opc_article_composant }"/>
										<c:set var="valSelect" value="${item.prix_achat_ht.toString().concat('_').concat(item.opc_tva_enum!=null?(item.opc_tva_enum.id.toString().concat('_').concat(item.opc_tva_enum.libelle)):'') }"/>
										
										<option value="${articleDet.opc_article_composant.id }" selected="selected" infos="${valSelect }">
											${articleDet.opc_article_composant.code }-${articleDet.opc_article_composant.libelle } 
											<c:if test="${articleDet.opc_article_composant.composition != null }">
												(${articleDet.opc_article_composant.composition })
											</c:if>
										</option>
									</c:if>
								</select>
								<std:linkPopup id="lnk_comp_${cpt}" actionGroup="" classStyle="btn btn-default" action="stock.composant.work_edit" icon="fa-3x fa-eye" params="art=${articleDet.opc_article_composant.id }" tooltip="Consulter" style="margin-right: 2px;" />
							</td>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top">
								<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 110px;" name="quantite_${cpt}" type="decimal" placeholderKey="article.quantite" value="${articleDet.quantite }"  maxlength="14"/>
							</td>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top">
								<std:text  name="prix_ht_${cpt}" type="decimal" style="width: 110px;" readOnly="true" value="${articleDet.opc_article_composant.getPrixAchatUnitaireHT() }"/>
							</td>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top">
								<std:select classStyle="slct" hiddenkey="code" name="tva_enum_${cpt}" type="long" key="id" labels="libelle" data="${listeTva}" value="${articleDet.opc_article_composant.opc_tva_enum.id }"/>
								<i class="fa fa-reorder" style="color:blue;" title="Liste énumérée"></i>
							</td>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top">
								<std:text name="prix_ttc_${cpt}" type="decimal" style="width: 110px;" maxlength="14" readOnly="true"/>
							</td>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top">
								<std:select name="mode_destock_${cpt}" data="${dataTypeCmd }" classStyle="slct" type="string" value="${articleDet.mode_destock }"/>
							</td>
							<td valign="top" style="padding-top: 5px; padding-right: 1px;">
								<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
							</td>
						</tr>

						<c:set var="cpt" value="${cpt+1 }" /> 
					</c:forEach>
				</table>
				<hr>
				<table style="width: 97%;margin-left: 20px;">
					<tr>
						<td width="20%" align="right" style="font-weight: bold;">TOTAL : </td>
						<td width="14%" align="right"><span class="badge badge-purple" id="td_qte" style="font-weight: bold;margin-right: 30px;"></span></td>
						<td width="14%" align="right"><span class="badge badge-purple" id="tt_ht" style="font-weight: bold;margin-right: 30px;"></span></td>
						<td width="14%">&nbsp;</td>
						<td width="14%" align="right" ><span class="badge badge-purple" id="tt_ttc" style="font-weight: bold;margin-right: 30px;"></span></td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td colspan="6" ><hr></td>
					</tr>		
					<tr>
						<td colspan="5" align="center">
							<span class="badge badge-success badge-square" id="span_marge_vnt" style="height: 23px;font-weight: bold;font-size: 13px !important;margin-right: 20px;"></span>
							<span class="badge badge-success badge-square" id="span_marge_poucent" style="height: 23px;font-weight: bold;font-size: 13px !important;margin-right: 20px;"></span>
							<span class="badge badge-success badge-square" id="span_marge_mtt" style="height: 23px;font-weight: bold;font-size: 13px !important;"></span>
						</td>
					</tr>
				</table>	
			</div>
			<hr>
			<%} %>
				<div class="form-actions">
					<c:set var="IS_AJAX_BACK_JOB_CALL" value="<%=ProjectConstante.IS_AJAX_BACK_JOB_CALL %>"></c:set>
					<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
					<%if(isOptStock) { %>
						<std:link actionGroup="M" id="add_ctrl" tooltipKey="article.articleDetail.ajouter" icon="fa-1x fa-plus" classStyle="btn btn-default" />
					<%} %>
						<%if(isDuplic){%>
						<std:button actionGroup="M" classStyle="btn btn-success" action="stock.article.work_merge" onClick="cleanUpSelected();" icon="fa-save" value="Sauvegarder" />
						<%} else{ %>
						<std:button actionGroup="M" classStyle="btn btn-success" action="stock.article.work_merge" onClick="cleanUpSelected();" workId="${article.id }" icon="fa-save" value="Sauvegarder" />
						<%} %>
						<std:button actionGroup="D" classStyle="btn btn-danger" action="stock.article.work_delete" workId="${article.id }" icon="fa-trash-o" value="Supprimer" />
					</div>
				</div>
			</div>
		</div>	
		
		
	</std:form>
</div>


<script type="text/javascript">
/*Handles ToolTips*/
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script>  
