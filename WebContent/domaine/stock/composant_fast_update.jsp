<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script type="text/javascript">
$(document).ready(function (){
	$(document).off('click', '#add_ctrl');
	$(document).on('click', '#add_ctrl', function() {
		var contentTr = $("#ctrl_gpt").html();
		var cpt = $("input[id^='contact_']").length + 1;
		contentTr = contentTr.replace(new RegExp("_0", 'g'), "_" + cpt);

		$("#ctrl_table").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
		$("#maxCtrl").val(cpt);
	});
	$(document).on('click', "#delete_cont", function() {
		$(this).closest("tr").remove();
	});
	
	$('.pdf_link').click(function(){
		$("#pdf_load_trig").attr("href", "front?w_f_act=<%=EncryptionUtil.encrypt("stock.composant.telechargerModel")%>&"+$(this).attr("params"));
		document.getElementById("pdf_load_trig").click();
	});
});
</script>

<a href="" id="pdf_load_trig" target="downloadframe" style="display:none;"></a> 

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion de stock</li>
		<li>Fiche articles</li>
		<li class="active">Edition</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link classStyle="btn btn-default" action="stock.composant.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
		
		<c:if test="${fam != null && fam != ''}">
			<std:link tooltip="Télécharger le modèle"  params="skipI=true&skipP=true" icon="fa-file-excel-o" value="Modèle Excel" classStyle="btn btn-default shiny primary pdf_link" />
		</c:if>
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
		<div class="widget">
			<div class="widget-header bordered-bottom bordered-blue">
				<span class="widget-caption">Saisie rapide des articles</span>
			</div>
			<div class="widget-body">
				<div class="row">
					<fieldset>
						<legend style="margin-left: 10px;">Famille</legend>
						<div class="form-group">
							<std:label classStyle="control-label col-md-2" value="Famille"/>
							<div class="col-md-4">
								<std:select name="famille_art" type="long" key="id" labels="code;'-';libelle" value="${famId }" data="${listeFaimlle}" required="true" isTree="true" width="100%" />
							</div>	
							<div class="col-md-1">
								<std:link classStyle="btn btn-primary" action="stock.composant.loadArticleFastSaisie" params="isRef=1" icon="fa-refresh" value="Charger articles" />
							</div>
						</div>
					</fieldset>
				</div>	
				<div class="row" id="div_det">
					<table id="ctrl_table" style="width: 97%; margin-left: 20px;">
						<tr>
							<th>Article</th>
							<th>Code barre</th>
							<c:if test="${isBalance }">
								<th>Raccourci</th>
							</c:if>
							<th>Prix d'achat</th>
							<th>Prix de vente</th>
						</tr>
						<tr id="ctrl_gpt" style="display: none;">
							<td style="padding-top: 5px; padding-right: 30px;" valign="top">
								<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;" name="libelle_0" type="string" placeholder="Libellé" maxlength="120" />
							</td>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top">
								<std:text name="code_barre_0" type="string" placeholder="Code barre" maxlength="80" />
							</td>
							<c:if test="${isBalance }">
								<td style="padding-top: 5px; padding-right: 30px;" valign="top">
									<std:text name="code_direct_bal_0" type="long" placeholder="Raccourci balance" maxlength="10" />
								</td>
							</c:if>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top">
								<std:text name="prix_achat_ttc_0" type="decimal" placeholder="prix d'achat" maxlength="20" />
							</td>
							<td style="padding-top: 5px; padding-right: 30px;" valign="top">
								<std:text name="prix_vente_0" type="decimal" placeholder="Prix de vente" maxlength="20" />
							</td>
							<td valign="middle" style="padding-top: 5px; padding-right: 1px;">
								<std:link id="delete_cont" icon="fa fa-times" style="color: red;"></std:link>
							</td>
						</tr>

						<c:set var="cpt" value="${1 }" />
						<c:set var="encodeService" value="<%=new EncryptionUtil()%>" />

						<c:forEach items="${list_article }" var="article">
							<std:hidden name="eaiid_${cpt}" value="${encodeService.encrypt(article.id) }" />
							<tr>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top">
									<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;" name="libelle_${cpt}" type="string" placeholder="Libellé" value="${article.libelle }" maxlength="120" />
								</td>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top">
									<std:text name="code_barre_${cpt}" type="string" placeholder="Code barre" value="${article.code_barre }" maxlength="30" />
								</td>
								 <c:if test="${isBalance }">
									 <td style="padding-top: 5px; padding-right: 30px;" valign="top">
										<std:text name="code_direct_bal_${cpt}" type="long" placeholder="Raccourci balance" value="${article.code_direct_bal }" maxlength="10" />
									</td>
								 </c:if>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top">
									<std:text name="prix_achat_ttc_${cpt}" type="decimal" placeholder="Prix d'achat" value="${article.prix_achat_ttc }" maxlength="20" />
								</td>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top">
									<std:text name="prix_vente_${cpt}" type="decimal" placeholder="Prix de vente" value="${article.prix_vente }" maxlength="20" />
								</td>
								<td valign="top" style="padding-top: 5px; padding-right: 1px;">
									<std:link actionGroup="X" id="delete_cont" icon="fa fa-times" style="color: red;" />
								</td>
							</tr>

							<c:set var="cpt" value="${cpt+1 }" />
						</c:forEach>
					</table>
				</div>
			</div>
		</div>

		<hr>
		<div class="row" style="text-align: center;">
			<div class="col-md-12">
				<std:link actionGroup="X" id="add_ctrl" tooltip="Ajouter" icon="fa-1x fa-plus" classStyle="btn btn-default"></std:link>
				<std:button actionGroup="X" classStyle="btn btn-success" action="stock.composant.loadArticleFastSaisie" workId="${fournisseur.id }" icon="fa-save" value="Sauvegarder" />
			</div>
		</div>
	</std:form>
</div>

