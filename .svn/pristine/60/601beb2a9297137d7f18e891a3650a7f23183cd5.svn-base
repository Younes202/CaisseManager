<%@page import="framework.model.util.FileUtil"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="appli.controller.domaine.stock.bean.ChargeDiversBean"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script src="resources/framework/js/util_file_upload.js?v=1.2"></script>

<% 
	String type = (String) ControllerUtil.getMenuAttribute("tpD", request);
	if(type == null){
		type = "D";
	}
%>

<script type="text/javascript">
	var idxFile = 1;
	$(document).ready(function() {
		$("#chargeDivers\\.libelle2").select2({
			 tags: true
		});
		
		// Add
		$(document).off('click', '#add_ctrl_d');
		$(document).on('click', '#add_ctrl_d', function(){
			var contentTr = $("#ctrl_gpt_d").html();
			var cpt = $("select[id^='opc_compte']").length + 1;
			contentTr = (contentTr ? contentTr.replace(new RegExp("_0", 'g'), "_"+ cpt) : null);
			
			if(contentTr != null){
				$("#ctrl_table_d").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
			}
			//
			$("select[id='opc_compte\\.id_" + cpt + "']").select2({allowClear: true});
			$("select[id='opc_compte\\.id_" + cpt + "']").css("width", "100% !important");
		});
		$(document).off('click', '#delete_cont');
		$(document).on('click', "#delete_cont", function() {
			$(this).closest("tr").remove();
		});
			
		// File	
		$("#addFile").click(function(){
			var content = $("#fileLoadDiv").clone().html();
			content = content.replace(/photoX/g,"photo"+idxFile);
			$("#row_file").append(content);
			idxFile++;
		});
		
		
		<%if(ControllerUtil.isEditionWritePage(request)){%>
		loadInputFileEvents();
	<% } else{%>
		$("div[id^='sep_photo']").remove();
	<%}
	// Initialiser les photos ou documents
	ChargeDiversBean chrgDvBean = (ChargeDiversBean)request.getAttribute("chargeDivers");
	if(chrgDvBean != null && chrgDvBean.getId() != null){
		IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
		Map<String, byte[]> dataimg = service.getDataImage(chrgDvBean.getId(), "depense");
		for(String key : dataimg.keySet()){%>
			var content = $("#fileLoadDiv").clone().html();
			content = content.replace(/photoX/g,"photo"+idxFile);
			$("#row_file").append(content);
			var ext = '<%=key.substring(key.lastIndexOf(".")+1)%>';
			var img = "";
	        if(ext.startsWith('pdf')){
	        	img = "resources/img/filetype_pdf.png";
	        } else if(ext.startsWith('xls')){
	        	img = "resources/img/xls_file.png";
	        } else if(ext.startsWith('txt')){
	        	img = "resources/img/txt.png";
	        } else if(ext.startsWith('doc')){
	        	img = "resources/img/document_microsoft_word_01.png";
	        } else{
	        	img = "data:image/jpeg;base64,<%=FileUtil.getByte64(dataimg.get(key)) %>";
	        }
	        $("#photo"+idxFile+"_div").css("background", "");
	        $("#photo"+idxFile+"_div").html("<img src='"+img+"' width='120' height='120'/>");
			$("#photo"+idxFile+"_name_span").html('<a href="front?w_f_act=<%=EncryptionUtil.encrypt("stock.chargeDivers.downloadPieceJointe")%>&pj=<%=EncryptionUtil.encrypt(chrgDvBean.getId().toString())%>&nm=<%=key%>" target="_blank"><%=key%></a> | <a href="front?w_f_act=<%=EncryptionUtil.encrypt("stock.chargeDivers.downloadPieceJointe")%>&pj=<%=EncryptionUtil.encrypt(chrgDvBean.getId().toString())%>&nm=<%=key%>&isdown=1" target="downloadframe"><i class="fa fa-cloud-download"></i></a>');
			$("#photo"+idxFile+"_name").val('<%=key%>');
			idxFile++;
	<%}
	} %>
		
		
		$("#generer_bl").click(function(){
			executePartialAjax($(this), '<%=EncryptionUtil.encrypt("stock.chargeDivers.generer_numBL")%>', 'chargeDivers.num_bl', true, true, null, true);
		});
	});
</script>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion de stock</li>
		<% if(type.equals("D")){ %>
			<li>Fiche de d&eacute;pense</li>
		<%} else{%>
			<li>Fiche de recette</li>
		<%} %>
		<li class="active">Edition</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="U" classStyle="btn btn-default" action="stock.chargeDivers.work_init_update" workId="${chargeDivers.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		
		<%if(ControllerUtil.getMenuAttribute("IS_TRV_MNU", request) != null){%>
		   <std:link classStyle="btn btn-default" action="stock.travaux.work_edit" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour aux travaux" />
	   <%} else{ %>
			<std:link classStyle="btn btn-default" action='<%=StringUtil.isTrue(""+ControllerUtil.getMenuAttribute("IS_GRP_VIEW", request))?"stock.chargeDivers.find_charge_groupe":"stock.chargeDivers.work_find" %>' params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
		<%} %>	
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
	<div style="display: none;">
	<std:select name="num_cheque_all" type="long" key="id" labels="num_cheque" hiddenkey="opc_fournisseur.id" data="${listChequeFournisseur }" style="display:none;" />
	</div>
	
		<!-- widget grid -->
		<div class="widget">
			<div class="widget-header bordered-bottom bordered-blue">
			<% if(type.equals("D")){ %>
				<span class="widget-caption">Fiche de d&eacute;pense</span>
			<%} else{%>
				<span class="widget-caption">Fiche de recette</span>
			<%} %>
			</div>
			<div class="widget-body">
				<div class="row">
					<div class="form-group" style="padding-left: 30px;font-size: 15px;">
						<b>Informations</b>					
					</div>
					<div class="form-group">
						<c:if test="${list_charge_lib.size() > 0 }">
							<std:label classStyle="control-label col-md-2" value="Libellé" />
							<div class="col-md-4">
							
								<select <%=ControllerUtil.isEditionWritePage(request) ? "":"disabled " %> name="chargeDivers.libelle2" 
										style='<%=ControllerUtil.isEditionWritePage(request)?"":"background-color:#eeeeee;"%>width: 90%;' id="chargeDivers.libelle2" class="select2">
									<option value="">&nbsp;</option>
									<c:forEach items="${list_charge_lib }" var="cd">
										<c:if test="${cd.libelle==chargeDivers.libelle}">
											<c:set var="isPassedEq" value="${true}" />
										</c:if>
										<c:choose>
											<c:when test="${cd.is_groupe }">
												<c:if test="${isPassedCD}">
													</optgroup>	
												</c:if>
												<optgroup label="${cd.libelle }">
												<c:set var="isPassedCD" value="${true}" />
											</c:when>
											<c:otherwise>
												<option ${isPassedEq?" selected='selected'":"" }>${cd.libelle }</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<c:if test="${isPassedCD}">
										</optgroup>	
									</c:if>
									<c:if test="${not isPassedEq }">
										<option selected='selected'>${chargeDivers.libelle }</option>
									</c:if>
								</select>
							</div>
						</c:if>
						<c:if test="${list_charge_lib.size() == 0 }">
							<std:label classStyle="control-label col-md-2" value="Libellé" />
							<div class="col-md-4">
								<std:text name="chargeDivers.libelle" type="string" placeholderKey="chargeDivers.libelle" maxlength="120"/>
							</div>
						</c:if>
					</div>	
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="chargeDivers.num_bl" />
						<div class="col-md-4">
							<std:text name="chargeDivers.num_bl" type="string" placeholderKey="chargeDivers.num_bl" iskey="true" required="true" style="width:50%;width: 200px;float: left;" maxlength="20"/>
							<% if(ControllerUtil.isEditionCreateAction(request)){ %>
								<a class="refresh-num" id="generer_bl" href="javascript:" title="G&eacute;n&eacute;rer un num&eacute;ro de bon" style="right: 42%">
				            		<i class="fa fa-refresh"></i>
				        		</a>
			        		<% }%>
						</div>

						<std:label classStyle="control-label col-md-2" valueKey="chargeDivers.date_mouvement" />
						<div class="col-md-4">
							<std:date name="chargeDivers.date_mouvement" required="true"/>
						</div>
					</div>
					<div class="form-group">
					<% if(type.equals("D")){ %>
						<std:label classStyle="control-label col-md-2" valueKey="chargeDivers.type_depense" />
					<%} else{%>
						<std:label classStyle="control-label col-md-2" valueKey="chargeDivers.type_recette" />
					<%} %>
						<div class="col-md-4">
							<std:select name="chargeDivers.opc_famille_consommation.id" type="long" key="id" labels="code;'-';libelle" data="${familleConsommation }" isTree="true" required="true" width="100%"/>
						</div>
						<std:label classStyle="control-label col-md-2" valueKey="chargeDivers.opc_fournisseur" />
						<div class="col-md-4">
							<std:select name="chargeDivers.opc_fournisseur.id" required="true" type="long" key="id" labels="libelle;' ';marque" hiddenkey="id" data="${listeFournisseur }" isTree="true" groupKey="opc_famille.id" groupLabels="opc_famille.code;'-';opc_famille.libelle" width="100%" />
						</div>
					</div>
					<hr>
					
					<div class="form-group" style="padding-left: 30px;font-size: 15px;">
						<b>Modes de paiement</b>					
					</div>
					<c:set var="menu_scope.PAIEMENT_DATA" value="${chargeDivers.getList_paiement() }" scope="session" />
					<div class="form-group" id="finance_bloc">
						<jsp:include page="/domaine/compta/paiement_consult.jsp"></jsp:include>
					</div>
					<hr>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="chargeDivers.montant" />
						<div class="col-md-4">
							<std:text name="chargeDivers.montant" type="decimal" placeholderKey="chargeDivers.montant" maxlength="14" style="width: 150px;" required="true"/>
						</div>
						<std:label classStyle="control-label col-md-2" valueKey="chargeDivers.tva" />
						<div class="col-md-4">
							<std:select hiddenkey="code" name="chargeDivers.opc_tva_enum.id" type="long" key="id" labels="libelle" data="${listeTva}"/>
							<i class="fa fa-reorder" style="color:blue;" title="Liste &eacute;num&eacute;r&eacute;e"></i>
						</div>
					</div>
					<hr>
					<div class="form-group" style="padding-left: 30px;font-size: 15px;">
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
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="chargeDivers.commentaire" />
						<div class="col-md-4">
							<std:textarea name="chargeDivers.commentaire" rows="5" cols="80" />
						</div>
					</div>
				</div>
				<!-- Generic Form -->
				<jsp:include page="/domaine/administration/dataValue_form.jsp" />
			</div>
		</div>

		<!-- Liste des comptes -->
<%-- 		<%if("D".equals(type)){ %> --%>
<!-- 		<div class="widget"> -->
<!-- 			<div class="widget-header bordered-bottom bordered-blue"> -->
<!-- 				<span class="widget-caption"> -->
<%-- 				<%if(ControllerUtil.isEditionWritePage(request)){ %> --%>
<!-- 					<a href="javascript:void(0);" id="add_ctrl_d" targetBtn="C" class="btn btn-default" title="Ajouter un compte" style="margin-top: -2px;"> -->
<!-- 						<i class="fa fa-3x fa-plus"> </i> Ajouter un compte -->
<!-- 					</a> -->
<%-- 					<%} %> --%>
<!-- 				</span> -->
<!-- 			</div> -->
<!-- 			 <div class="widget-body"> -->
<!-- 				<div class="row"> -->
<!-- 					<table id="ctrl_table_d" style="width: 97%;margin-left: 20px;"> -->
<!-- 						<tr> -->
<!-- 							<th>Compte</th> -->
<!-- 							<th width="100px">Montant TTC</th> -->
<!-- 							<th width="100px">Montant TVA</th> -->
<!-- 							<th width="70px"></th> -->
<!-- 						</tr> -->
<!-- 						<tr id="ctrl_gpt_d" style="display: none;"> -->
<%-- 							<std:hidden name="sens_0" value="D" /> --%>
<!-- 							<td style="padding-top: 5px; padding-right: 10px;" valign="top"> -->
<%-- 								<std:select mode="std" style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;" name="opc_compte.id_0" type="long" data="${listCompte }" width="400" key="id" labels="code;'-';libelle" placeholder="Compte" isTree="true" /> --%>
<!-- 							</td> -->
<!-- 							<td style="padding-top: 5px; padding-right: 10px;" valign="top"> -->
<%-- 								<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="montant_0" type="decimal" placeholder="Montant TTC" maxlength="20" /> --%>
<!-- 							</td> -->
<!-- 							<td style="padding-top: 5px; padding-right: 10px;" valign="top"> -->
<%-- 								<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="montant_tva_0" type="decimal" placeholder="Montant" maxlength="20" /> --%>
<!-- 							</td> -->
<!-- 							<td valign="top" style="padding-top: 5px; padding-right: 1px;"> -->
<%-- 								<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link> --%>
<!-- 							</td> -->
<!-- 						</tr> -->
						
<%-- 						<c:set var="cpt" value="${1 }" /> --%>
<%-- 						<c:forEach items="${depense.list_ventilation }" var="ecritP"> --%>
<!-- 								<tr> -->
<!-- 									<td style="padding-top: 5px; padding-right: 10px;" valign="top"> -->
<%-- 										<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;" name="opc_compte.id_${cpt }" type="long" data="${listCompte }" width="400" value="${ecritP.opc_compte.id }" key="id" labels="code;'-';libelle" placeholder="Compte" isTree="true" /> --%>
<!-- 									</td> -->
<!-- 									<td style="padding-top: 5px; padding-right: 10px;" valign="top"> -->
<%-- 										<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="montant_${cpt }" type="decimal" placeholder="Montant" value="${ecritP.montant }" maxlength="20" /> --%>
<!-- 									</td> -->
<!-- 									<td style="padding-top: 5px; padding-right: 10px;" valign="top"> -->
<%-- 										<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="montant_tva_${cpt }" type="decimal" placeholder="Montant" value="${ecritP.montant_tva }" maxlength="20" /> --%>
<!-- 									</td> -->
<!-- 									<td valign="top" style="padding-top: 5px; padding-right: 1px;"> -->
<%-- 										<std:link actionGroup="M" id="delete_cont" icon="fa fa-times" style="color: red;"></std:link> --%>
<!-- 									</td> -->
<!-- 								</tr> -->
<%-- 								<c:set var="cpt" value="${cpt+1 }" />  --%>
<%-- 						</c:forEach> --%>
<!-- 					</table> -->
<!-- 				</div> -->
<!-- 			</div>	 -->
<!-- 		</div>	 -->
<%-- 		<%} %> --%>
		
			
		<!-- Pieces -->
		<% if(type.equals("D")){ %>
			<div class="widget">
				<div class="widget-header bordered-bottom bordered-blue">
					<span class="widget-caption">
					<%if(ControllerUtil.isEditionWritePage(request)){ %>
						<a href="javascript:void(0);" id="addFile" targetBtn="C" class="btn btn-default" title="Ajouter pi&egrave;ce jointe" style="margin-top: -2px;">
							<i class="fa fa-3x fa-plus"> </i> Ajouter pi&egrave;ces jointes
						</a>
						<%} %>
					</span>
				</div>
				 <div class="widget-body">
						<!-- Photos -->
						<div class="row" id="row_file">
							<div id="fileLoadDiv" style="display: none;">
								<div class="col-md-2">
									<div class="col-sm-12">	
										<div id="photoX_div" style="border: 1px solid #c6d983; border-radius: 25px; text-align: center; height: 120px; background: url('resources/framework/img/picture_file.png') no-repeat center;cursor: pointer;">
											<span style="font-size: 11px;">Fichier</span>
										</div>
									</div>
									<div class="col-sm-12" style="text-align: center;color: olive;">
										<span id="photoX_name_span"></span>
										<input type="hidden" name="photoX_name" id="photoX_name">
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
		<%} %>

		<div class="row" style="text-align: center;">
			<div class="col-md-12">
				<std:button actionGroup="M" classStyle="btn btn-success" action="stock.chargeDivers.work_merge" workId="${chargeDivers.id }" icon="fa-save" value="Sauvegarder" />
				<std:button actionGroup="D" classStyle="btn btn-danger" action="stock.chargeDivers.work_delete" workId="${chargeDivers.id }" icon="fa-trash-o" value="Supprimer" />
			</div>
		</div>
	</std:form>
</div>

