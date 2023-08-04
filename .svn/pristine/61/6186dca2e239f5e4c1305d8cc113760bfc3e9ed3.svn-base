<%@page import="framework.model.util.FileUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="appli.controller.domaine.personnel.bean.FraisBean"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
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

<script type="text/javascript">
	var idxFile = 1;
	$(document).ready(function() {
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
		FraisBean fraisBean = (FraisBean)request.getAttribute("frais");
		if(fraisBean != null && fraisBean.getId() != null){
			IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
			Map<String, byte[]> dataimg = service.getDataImage(fraisBean.getId(), "frais");
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
				$("#photo"+idxFile+"_name_span").html('<a href="front?w_f_act=<%=EncryptionUtil.encrypt("pers.frais.downloadPieceJointe")%>&pj=<%=EncryptionUtil.encrypt(fraisBean.getId().toString())%>&nm=<%=key%>" target="_blank"><%=key%></a> | <a href="front?w_f_act=<%=EncryptionUtil.encrypt("pers.frais.downloadPieceJointe")%>&pj=<%=EncryptionUtil.encrypt(fraisBean.getId().toString())%>&nm=<%=key%>&isdown=1" target="downloadframe"><i class="fa fa-cloud-download"></i></a>');
				$("#photo"+idxFile+"_name").val('<%=key%>');
				idxFile++;
		<%}
		} %>
		
		$(document).off('click', '#add_ctrl');
		$(document).on('click', '#add_ctrl', function(){
			var contentTr = $("#ctrl_gpt").html();
			var cpt = $("select[id^='opc_type_frais']").length + 1;
			contentTr = (contentTr ? contentTr.replace(new RegExp("_0", 'g'), "_"+ cpt) : null);
			
			if(contentTr != null){
				$("#ctrl_table").append('<tr cpt="'+cpt+'">' + contentTr + '</tr>');
			}
			//
			$("select[id='opc_type_frais\\.id_" + cpt + "']").select2({allowClear: true});
			
			  $('#date_depense_' + cpt).datepicker({
		    	todayBtn: true,
		    	clearBtn: true,
			    language: "fr",
			    autoclose: true,
			    todayHighlight: true
		    });
			 $("select[id='opc_type_frais\\.id_" + cpt + "']").css("width", "100% !important");
		});
		$(document).off('click', '#delete_dep');
		$(document).on('click', "#delete_dep", function() {
			$(this).closest("tr").remove();
		});
	});
</script>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des frais</li>
			<li>Fiche de frais RH</li>
		<li class="active">Edition</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<c:if test="${frais.statut == null }">
		<std:link actionGroup="U" classStyle="btn btn-default" action="pers.frais.work_init_update" workId="${frais.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		</c:if>
		<std:link classStyle="btn btn-default" action='pers.frais.work_find' params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
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
				<span class="widget-caption">Fiche de frais</span>
			</div>
			<div class="widget-body">
				<div class="row">
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Libellé" />
						<div class="col-md-5">
							<std:text name="frais.libelle" type="string" placeholder="Libellé" required="true" maxlength="120"/>
						</div>
						<std:label classStyle="control-label col-md-2" value="Montant total" />
						<div class="col-md-2">
							<std:text name="frais.mtt_total" type="decimal" placeholder="Montant total" required="true" maxlength="14"/>
						</div>
					</div>	
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Employé" />
						<div class="col-md-4">
							<std:select name="frais.opc_employe.id" required="true" type="long" key="id" labels="nom;' ';prenom" data="${listeEmploye }" width="100%" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Commentaire" />
						<div class="col-md-6">
							<std:textarea name="frais.commentaire" rows="5" cols="80" />
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="widget" id="div_detail">
	         <div class="widget-header bordered-bottom bordered-blue">
	         	<span class="widget-caption">
	            	<std:link actionGroup="M" id="add_ctrl" value="Ajouter une ligne" icon="fa-1x fa-plus" classStyle="btn btn-default" style="margin-top: -2px;"/>
	            </span>
	         </div>
	         <div class="widget-body">
					<div class="row">
					<table id="ctrl_table" style="width: 97%;margin-left: 20px;">
						<tr>
							<th width="120px">Date frais</th>
							<th>Type de frais</th>
							<th width="150px">Montant</th>
							<th width="50px"></th>
						</tr>
						<tr id="ctrl_gpt" style="display: none;">
							<td style="padding-top: 5px; padding-right: 10px;" valign="top">
								<std:date name="date_depense_0" placeholder="Date frais" classStyle="form-control"/>
							</td>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top">
								<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important; width: 100% !important;" classStyle="slct" name="opc_type_frais.id_0" type="long" key="id" labels="libelle" data="${listTypeFrais}" />
							</td>
							<td style="padding-top: 5px; padding-right: 10px;" valign="top">
								<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="montant_0" type="decimal" placeholder="Montant" />
							</td>
							<td valign="top" style="padding-top: 5px; padding-right: 1px;">
								<std:link actionGroup="M" id="delete_dep" icon="fa fa-times" style="color: red;"></std:link>
							</td>
						</tr>
	
						<c:set var="cpt" value="${1 }" />
						<c:set var="encodeService" value="<%=new EncryptionUtil()%>" />
						
						<c:forEach items="${frais.list_detail }" var="detail">
							<std:hidden name="eaiid_${cpt}" value="${encodeService.encrypt(detail.id) }" />
							<tr>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top">
									<std:date name="date_depense_${cpt}" placeholder="Date frais" value="${detail.date_depense }" />
								</td>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top">
									<std:select style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;; width: 100% !important;" name="opc_type_frais.id_${cpt}" type="long" key="id" labels="libelle" data="${listTypeFrais }" value="${detail.opc_type_frais.id }" />
								</td>
								<td style="padding-top: 5px; padding-right: 30px;" valign="top">
									<std:text style="border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;width: 100px;" name="montant_${cpt}" type="decimal" placeholder="Montant" value="${detail.montant }" />
								</td>
								<td valign="top" style="padding-top: 5px; padding-right: 1px;">
									<std:link actionGroup="M" id="delete_dep" icon="fa fa-times" style="color: red;"></std:link>
								</td>
							</tr>
	
							<c:set var="cpt" value="${cpt+1 }" /> 
						</c:forEach>
					</table>
				</div>
			</div>
		</div>

		<!-- Pieces -->
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

		<div class="row" style="text-align: center;">
			<div class="col-md-12">
				<std:button actionGroup="M" classStyle="btn btn-success" action="pers.frais.work_merge" workId="${frais.id }" icon="fa-save" value="Sauvegarder" />
				<std:button actionGroup="D" classStyle="btn btn-danger" action="pers.frais.work_delete" workId="${frais.id }" icon="fa-trash-o" value="Supprimer" />
			</div>
		</div>
	</std:form>
</div>

