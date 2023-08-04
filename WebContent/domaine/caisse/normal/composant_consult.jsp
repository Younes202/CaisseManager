<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="appli.controller.domaine.stock.bean.ArticleBean"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="appli.controller.domaine.stock.action.ArticleAction"%>
<%@page import="framework.model.common.constante.ActionConstante"%>
<%@page import="framework.model.common.constante.ProjectConstante"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
	#generic_modal_body{
		width: 920px;
		margin-left: -16%;
	}
	.select2-container{
		width: 90% !important;
	}
</style>

<%
Boolean isCaisse = (Boolean)request.getAttribute("isCaisse");
Boolean isGenerique = (Boolean)request.getAttribute("isGenerique");
%>

<script type="text/javascript">
function getFloat(val){
	if(val && $.trim(val) != '' && val != 'null'){
		val = $.trim(val).replace(/ /g, '');
		return  parseFloat(val);
	}
	return null;
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
		var margeHT =  prixHt>0 ? ((pvnt-prixHt)*100)/prixHt : 0.00;
		var margeDhs = prixHt>0 ? (pvnt-prixHt) : 0.00;
		
		$("#span_marge_vnt").html("<span style='color: black;'>Prix de vente :</span> "+$("#article\\.prix_vente").val()+" Dhs");
		$("#span_marge_ht").html("<span style='color: black;'>Marge HT % :</span> "+margeHT.toFixed(<%=ContextGloabalAppli.getNbrDecimal()%>)+" %");
		$("#span_marge_ttc").html("<span style='color: black;'>Marge HT Dhs :</span> "+margeDhs.toFixed(<%=ContextGloabalAppli.getNbrDecimal()%>)+" Dhs");
	}
	
	$("#td_qte").html(qte.toFixed(<%=ContextGloabalAppli.getNbrDecimal()%>));
	$("#tt_ht").html(prixHt.toFixed(<%=ContextGloabalAppli.getNbrDecimal()%>));
	$("#tt_ttc").html(prixTtc.toFixed(<%=ContextGloabalAppli.getNbrDecimal()%>));
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
			$("#prix_ttc_"+idx).val(ttc.toFixed(<%=ContextGloabalAppli.getNbrDecimal()%>));   
		}
	}
}

	$(document).ready(function() {
		<% if(BooleanUtil.isTrue(isCaisse)){%>
			$('#vente-div').show();
		<%}%>
		<% if(BooleanUtil.isTrue(isGenerique)){%>
			$('#div-type').show();
		<%}%>
		
		$('#article\\.used_in').change(function(){
			if($(this).val() == 'S'){
				$('#vente-div').hide();
			} else{
				$('#vente-div').show();
			}
		});
		
		// Calcul au d&eacute;marrage------------
		setTimeout(function(){
			calculMtt();
		}, 1000);
		
		$("input[id^='quantite_']").each(function(){
			var idx = $(this).attr("id").substring(9);
			calculPrix(idx);
		});
		//----------------------------------
		
		<%if(ControllerUtil.isEditionWritePage(request)){%>
			loadInputFileEvents();
		<% } else{%>
			$("#sep_photo1").remove();
		<%}%>
		
		<%
		ArticleBean articleBean = (ArticleBean)request.getAttribute("article");
		if(articleBean != null && articleBean.getId() != null){ 
			IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
			Map<String, byte[]> dataimg = service.getDataImage(articleBean.getId(), "article");
			if(dataimg.size() > 0){
				String fileName = dataimg.entrySet().iterator().next().getKey();
				byte[] value = dataimg.entrySet().iterator().next().getValue();
			%>
				$("#photo1_div").css("background", "");
		 		$("#photo1_div").html("<img src='data:image/jpeg;base64,<%=FileUtil.getByte64(value) %>' width='120' height='120'/>");
		 		$("#photo1_name_span").text("<%=fileName%>");
		 		$("#photo1_name").val("<%=fileName%>");
			<%}
		} %>
	});
</script>

<std:form name="data-form">

	<div class="widget">
         <div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Fiche composant</span>
			
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal" style="margin-top: 2px;margin-right: 5px;">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>

         <div class="widget-body">
         	<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row" style="padding: 0 25px;">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" valueKey="article.opc_famille"/>
					<div class="col-md-4">
						<std:select name="article.opc_famille_stock.id" type="long" key="id" labels="code;'-';libelle" data="${listeFaimlle}" required="true" isTree="true"  width="100%"/>
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" valueKey="article.code_barre"/>
					<div class="col-md-4">
						<std:text name="article.code_barre" type="string" placeholderKey="article.code_barre" style="width: 60%;" maxlength="50" required="true"/>
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Composition" />
					<div class="col-md-4">
						<std:text name="article.composition" type="string" placeholder="Composition" required="true" maxlength="120"/>
					</div>
					<std:label classStyle="control-label col-md-2" value="Conditionnement"/>
					<div class="col-md-4">
						<std:text name="article.conditionnement" type="string" placeholder="Conditionnement" required="true" maxlength="120"/>
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Forme" />
					<div class="col-md-4">
						<std:select name="article.opc_famille_stock.id" type="long" key="id" labels="libelle" data="${listeFamille}" required="true" />
						<i class="fa fa-reorder" style="color:blue;" title="Liste &eacute;num&eacute;r&eacute;e"></i>
					</div>
					<std:label classStyle="control-label col-md-2" valueKey="article.libelle"/>
					<div class="col-md-4">
						<std:text name="article.libelle" type="string" placeholderKey="article.libelle" required="true" maxlength="80"/>
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Type"/>
					<div class="col-md-4">
						<std:select name="article.type" type="string" data="${typeArticle }" required="true" />
					</div>
					<div style="display:none;" id="div-type">
						<std:label classStyle="control-label col-md-2" value="G&eacute;n&eacute;rique de" />
						<div class="col-md-4">
							<std:select name="article.opc_article_origine.id" type="long" key="id" labels="marque;' - ';libelle" data="${listArticle}" width="100" />
						</div>
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" valueKey="article.is_ctrl_quantite"/>
					<div class="col-md-4">
						<std:checkbox name="article.is_ctrl_quantite" />
						<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Contr&ocirc;ler la quantit&eacute; restante de cet article dans la caisse" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
					</div>
					
					<c:if test="${article.id == null }">
						<std:label classStyle="control-label col-md-2" value="Vendu en caisse"/>
						<div class="col-md-4">
							<std:checkbox name="article.is_article" />
						</div>
					</c:if>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Unit&eacute; de dosage"/>
					<div class="col-md-4">
						<std:select name="article.opc_unite_dosage_enum.id" type="long" key="id" labels="libelle" data="${listeDosage}" required="true" />
						<i class="fa fa-reorder" style="color:blue;" title="Liste &eacute;num&eacute;r&eacute;e"></i>
					</div>
					<std:label classStyle="control-label col-md-2" value="Dosage"/>
					<div class="col-md-4">
						<std:text name="article.dosage" type="string" placeholder="Dosage" required="true" maxlength="30" style="width: 50%;"/>
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Taux de remboursement"/>
					<div class="col-md-4">
						<std:select name="article.opc_taux_rembours_enum.id" type="long" key="id" labels="libelle" data="${listeTauxRembours}" required="true"/>
						<i class="fa fa-reorder" style="color:blue;" title="Liste &eacute;num&eacute;r&eacute;e"></i>
					</div>
				</div>	
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
				
				<fieldset id="vente-div" style="display:none;">
					<legend style="margin-left: 10px;">Informations de vente <i class="fa fa-info-circle" title="Informations utiles lors de la g&eacute;n&eacute;ration de la fiche artcile depuis ce composant"></i></legend>
						<div id="vente-inputs">
							<div class="form-group">
								<std:label classStyle="control-label col-md-2" value="TVA"/>
								<div class="col-md-4">
									<std:select name="article.opc_tva_vente_enum.id" type="long" key="id" labels="libelle" data="${listeTva}" required="true">
										<std:ajax event="change" target="vente-inputs" action="stock.article.refreshInputs" params="pt=tvav" />
									</std:select>
									<i class="fa fa-reorder" style="color:blue;" title="Liste &eacute;num&eacute;r&eacute;e"></i>
								</div>
								<std:label classStyle="control-label col-md-2" value="Prix vente TTC"/>
								<div class="col-md-4">
									<std:text name="article.prix_vente_ttc" type="decimal" placeholder="Prix de vente TTC" style="width: 80%;border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;text-align:right;" maxlength="14">
										<std:ajax event="change" target="vente-inputs" action="stock.article.refreshInputs" params="pt=ttcv" />
									</std:text>
								</div>
							</div>
						</div>
				</fieldset>
				</div>
				<hr>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" valueKey="article.description"/>
					<div class="col-md-10">
						<std:textarea name="article.description" rows="5" cols="50" maxlength="255"/>
					</div>
				</div>
		</div>
	</div>

	<div class="widget">
		<c:if test="${article.is_fiche }">
			<div class="widget-header bordered-bottom bordered-blue">
	            <span class="widget-caption">Composants</span>
	         </div>
	         <div class="widget-body">
				<div class="row">
					<table id="ctrl_table" style="width: 97%;margin-left: 20px;">
						<tr>
							<th width="20%">Composant</th>
							<th width="14%">Quantit&eacute;</th>
							<th width="14%">Prix U HT</th>
							<th width="14%">TVA</th>
							<th width="14%">Prix U TTC</th>
							<th></th>
						</tr>
	
						<c:set var="cpt" value="${1 }" />
						<c:set var="encodeService" value="<%=new EncryptionUtil()%>" />
						<c:set var="articleAction" value="<%=new ArticleAction()%>" />
						<c:forEach items="${article.list_article }" var="articleDet">
							<std:hidden name="eaiid_${cpt}" value="${encodeService.encrypt(articleDet.id) }" />
							<tr>
								<td style="padding-top: 5px; valign="top" nowrap="nowrap">
									<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;" name="opc_article_composant.id_${cpt}" hiddenkey="prix_achat_ht;opc_tva_enum.id;opc_tva_enum.libelle" type="long" key="id" labels="code;'-';libelle" data="${listArticle }" value="${articleDet.opc_article_composant.id }" groupKey="opc_famille_stock.id" groupLabels="opc_famille_stock.code;'-';opc_famille_stock.libelle" width="250" />
									<i class="fa fa-reorder" style="color:blue;" title="Liste &eacute;num&eacute;r&eacute;e"></i>
									<std:linkPopup id="lnk_comp_${cpt}" actionGroup="" classStyle="btn btn-default" action="stock.article.work_edit" icon="btn btn-sm btn-primary" params="art=${articleDet.opc_article_composant.id }&isCmp=1" tooltip="Consulter" />
								</td>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top">
									<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="quantite_${cpt}" type="decimal" placeholderKey="article.quantite" value="${articleDet.quantite }"  maxlength="14"/>
								</td>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top">
									<std:text  name="prix_ht_${cpt}" type="decimal" readOnly="true" value="${articleDet.opc_article_composant.prix_achat_ht }"/>
								</td>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top">
									<std:select classStyle="slct" hiddenkey="code" name="tva_enum_${cpt}" disable="true" type="long" key="id" labels="libelle" data="${listeTva}" value="${articleDet.opc_article_composant.opc_tva_enum.id }"/>
									<i class="fa fa-reorder" style="color:blue;" title="Liste &eacute;num&eacute;r&eacute;e"></i>
								</td>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top">
									<std:text name="prix_ttc_${cpt}" type="string"  maxlength="14" readOnly="true"/>
								</td>
								<td valign="top" style="padding-top: 5px; padding-right: 1px;">
									<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
								</td>
							</tr>
	
							<c:set var="cpt" value="${cpt+1 }" /> 
						</c:forEach>
						
						<tr><td colspan="6"><hr></td></tr>
						<tr id="tr_total" style="line-height: 27px;background-color: #f3eec2;border: 1px solid #b1c0d2;">
							<td align="right" style="font-weight: bold;">TOTAL : </td>
							<td align="right"><span class="badge badge-purple" id="td_qte" style="font-weight: bold;margin-right: 30px;"></span></td>
							<td align="right"><span class="badge badge-purple" id="tt_ht" style="font-weight: bold;margin-right: 30px;"></span></td>
							<td>&nbsp;</td>
							<td align="right" ><span class="badge badge-purple" id="tt_ttc" style="font-weight: bold;margin-right: 30px;"></span></td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td colspan="5" align="center">
								<span class="badge badge-success badge-square" id="span_marge_vnt" style="font-weight: bold;font-size: 13px !important;margin-right: 20px;"></span>
								<span class="badge badge-success badge-square" id="span_marge_ht" style="font-weight: bold;font-size: 13px !important;margin-right: 20px;"></span>
								<span class="badge badge-success badge-square" id="span_marge_ttc" style="font-weight: bold;font-size: 13px !important;"></span>
							</td>
						</tr>
						
					</table>
				</div>
			<hr>
			</div>
		</c:if>
		</div>	
	
</std:form>



<script type="text/javascript">
/*Handles ToolTips*/
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script>  
