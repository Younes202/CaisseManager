<%@page import="appli.model.domaine.personnel.persistant.EmployeAppreciationPersistant"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="appli.controller.domaine.personnel.bean.EmployeBean"%>
<%@page import="framework.model.util.audit.ReplicationGenerationEventListener"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
.form-title {
	margin-left: 12px;
}
</style>

<%

 boolean isRh = ContextGloabalAppli.getAbonementBean().isOptRh();
 %>

<script type="text/javascript">
  	function manageTemps(){
  		if($('#employe\\.mode_paie').val() == "J"){
			$('#div-heureParJour').show();
		} else{
			$('#div-heureParJour').hide();
		}
  	}
  
  	var idxFile = 1;
	$(document).ready(function() {
		$("#addFile").click(function(){
			var content = $("#fileLoadDiv").clone().html();
			content = content.replace(/photoX/g,"photo"+idxFile);
			$("#row_file").append(content);
			idxFile++;
		});
		// Init wizard

		
		<%if(ControllerUtil.isEditionWritePage(request)){%>
		loadInputFileEvents();
	<%} else{%>
		$("div[id^='sep_photo']").remove();
	<%}
	// Initialiser les photos ou documents
	EmployeBean employeBean = (EmployeBean)request.getAttribute("employe");
 		List<EmployeAppreciationPersistant> existingAppreciations = null;
 		if(employeBean != null && employeBean.getId() != null){ 
 			existingAppreciations = employeBean.getList_appreciation();
 			IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
 			Map<String, byte[]> dataimg = service.getDataImage(employeBean.getId(), "employe_pic");
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
			$("#photo"+idxFile+"_name_span").html('<a href="front?w_f_act=<%=EncryptionUtil.encrypt("pers.employe.downloadPieceJointe")%>&pj=<%=EncryptionUtil.encrypt(employeBean.getId().toString())%>&nm=<%=key%>" target="_blank"><%=key%></a> | <a href="front?w_f_act=<%=EncryptionUtil.encrypt("pers.employe.downloadPieceJointe")%>&pj=<%=EncryptionUtil.encrypt(employeBean.getId().toString())%>&nm=<%=key%>&isdown=1" target="downloadframe"><i class="fa fa-cloud-download"></i></a>');
			$("#photo"+idxFile+"_name").val('<%=key%>');
			idxFile++;
	<%}
}%>
	
		
		$('#employe\\.mode_paie').change(function(){
			manageTemps();
		});
		manageTemps();
		
 		$("#generer_bl").click(function(){
			executePartialAjax($(this), '<%=EncryptionUtil.encrypt("pers.employe.genererNumBL")%>', 'employe.numero', true, true, null, true);
		});
 		
 		<%if(ControllerUtil.isEditionWritePage(request)){%>
 			loadInputFileEvents();
 		<% } else{%>
 			getTabElement("#sep_photoZ").remove();
 		<%}%>
 		
 		<%

 		if(employeBean != null && employeBean.getId() != null){ 
 			existingAppreciations = employeBean.getList_appreciation();
 			IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
 			Map<String, byte[]> dataimg = service.getDataImage(employeBean.getId(), "employe_pic");
 			if(dataimg.size() > 0){
 				String fileName = dataimg.entrySet().iterator().next().getKey();
 				byte[] value = dataimg.entrySet().iterator().next().getValue();
 			%>
 				getTabElement("#photoZ_div").css("background", "");
 				getTabElement("#photoZ_div").html("<img src='data:image/jpeg;base64,<%=FileUtil.getByte64(value) %>' width='120' height='120'/>");
 				getTabElement("#photoZ_name_span").text("<%=fileName%>");
 				getTabElement("#photoZ_name").val("<%=fileName%>");
			<%}
		}%>
	});
</script>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des personnels</li>
		<li>Employ&eacute;s</li>
		<li class="active">Edition</li>
	</ul>
</div>

<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="U" classStyle="btn btn-default" action="pers.employe.work_init_update" workId="${employe.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		<%
		boolean isSubAdd = "pers.employe.work_init_create".equals(ControllerUtil.getMenuAttribute("IS_SUB_ADD", request));
		String act =  (!isSubAdd ? ""+ControllerUtil.getMenuAttribute("IS_SUB_ADD", request) : "pers.employe.work_find");
		%>
		
		<std:link classStyle="btn btn-default" action='<%=act %>' params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
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
		<std:form name="data-form">
			<div class="row">
				<div class="col-lg-12 col-sm-6 col-xs-12">
					<div class="tabbable">
						<ul class="nav nav-tabs" id="myTab">
							<li class="active"><a data-toggle="tab" noVal="true" href="#descripton" wact="<%=EncryptionUtil.encrypt("pers.employe.work_edit")%>"> Fiche </a></li>
							<%
								if (ControllerUtil.getMenuAttribute("employeId", request) != null) {
							%>
							<%
								if (isRh) {
							%>
							<li><a data-toggle="tab" href="#mouvement" noVal="true" wact="<%=EncryptionUtil.encrypt("pers.salaire.work_find")%>"> Salaires </a></li>
							<li><a data-toggle="tab" href="#mouvement" noVal="true" wact="<%=EncryptionUtil.encrypt("pers.pointage.work_init")%>" params="tp=emp"> Pointage </a></li>
							<%
								} else {
							%>
							<li><a data-toggle="tab" href="#mouvement" noVal="true" style="color: #e0e0e0;"> Salaires </a></li>
							<li><a data-toggle="tab" href="#mouvement" noVal="true" style="color: #e0e0e0;"> Pointage </a></li>
							<%
								}
							%>

							<li><a data-toggle="tab" href="#mouvement" noVal="true" wact="<%=EncryptionUtil.encrypt("caisse.caisse.find_mvm_employe")%>"> Commandes </a></li>
							<li><a data-toggle="tab" href="#mouvement" noVal="true" wact="<%=EncryptionUtil.encrypt("pers.employe.find_reduction")%>"> R&eacute;duction </a></li>
							<%
								}
							%>
						</ul>
					</div>
				</div>
			</div>

			<div class="widget-body">
				<div class="row">
					<%
						if (ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE) {
					%>
					<div class="form-group">
						<std:label classStyle="control-label col-md-3" value="Etablissement" />
						<div class="col-md-4">
							<std:select name="employe.ets_ids" multiple="true" labels="nom" key="id" type="long[]" data="${listEtablissement}" />
						</div>
					</div>
					<%
						}
					%>


					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="employe.numero" />
						<div class="col-md-4">
							<std:text name="employe.numero" type="string" iskey="true" style="width:120px;float: left;" maxlength="20" required="true" />
							<%
								if (ControllerUtil.isEditionCreateAction(request)) {
							%>
							<a class="refresh-num" id="generer_bl" href="javascript:" title="G&eacute;n&eacute;rer un num&eacute;ro"> <i class="fa fa-refresh"></i>
							</a>
							<%
								}
							%>
						</div>
						
						<std:label classStyle="control-label col-md-2" value="Famille"/>
						<div class="col-md-4">
							<std:select name="employe.opc_famille.id" type="long" key="id" labels="code;'-';libelle" data="${listeFamille}" isTree="true" width="100%"/>
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="employe.nom" />
						<div class="col-md-4">
							<std:text name="employe.nom" required="true" placeholderKey="employe.nom" type="string" maxlength="50" />
						</div>
						<std:label classStyle="control-label col-md-2" valueKey="employe.prenom" />
						<div class="col-md-4">
							<std:text name="employe.prenom" required="true" type="string" maxlength="50" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Gendre" />
						<div class="col-md-4">
							<std:select name="employe.civilite" type="string" data="${civiliteArray}" />
						</div>
						<std:label classStyle="control-label col-md-2" value="Situation Famillielle" />
						<div class="col-md-4">
							<std:select name="employe.situation_fam" type="string" data="${situationFamArray}" />
						</div>
						<std:label classStyle="control-label col-md-2" valueKey="employe.date_naissance" />
						<div class="col-md-4">
							<std:date name="employe.date_naissance" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="employe.cin" />
						<div class="col-md-4">
							<std:text name="employe.cin" type="string" style="width:50%;" maxlength="20" />
						</div>
						<std:label classStyle="control-label col-md-2" valueKey="employe.nbr_enfant" />
						<div class="col-md-4">
							<std:text name="employe.nbr_enfant" type="long" style="width:20%;" maxlength="2" />
						</div>
						<std:label classStyle="control-label col-md-2" value="Periode validation" />
						<div class="col-md-4">
							<std:text name="employe.periode_validation" type="long" style="width:20%;" maxlength="2" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="employe.telephone" />
						<div class="col-md-4">
							<std:text name="employe.telephone" type="string" style="width:120px;" maxlength="20" />
						</div>
						<std:label classStyle="control-label col-md-2" valueKey="employe.mail" />
						<div class="col-md-4">
							<std:text name="employe.mail" type="string" validator="email" maxlength="50" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Image" />
						<div class="col-md-4">
							<div class="col-sm-12">
								<div id="photoZ_div" style="width: 150px; margin-top: 10px; border: 1px solid #c6d983; border-radius: 25px; text-align: center; height: 120px; background: url('resources/framework/img/picture_file.png') no-repeat center; cursor: pointer;">
									<span style="font-size: 11px;">Image</span>
								</div>
								<span id="photoZ_name_span"></span> <input type="hidden" name="photoZ_name" id="photoZ_name">
							</div>
							<div class="col-sm-12">
								<!-- Separator -->
								<div id="sep_photoZ" style="width: 120px; margin-bottom: 5px; height: 20px; text-align: center;">
									<a href="javascript:"><b>X</b></a>
								</div>
								<!-- End -->
								<input type="file" name="photoZ" id="photoZ_div_file" style="display: none;" accept="image/bmp,image/gif,image/jpg,image/png,image/jpeg">
							</div>
						</div>
					</div>

					<div class="form-title">Informations du contrat</div>
					<div class="form-group">-
						<std:label classStyle="control-label col-md-2" valueKey="employe.date_entree" />
						<div class="col-md-4">
							<std:date name="employe.date_entree" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="employe.type_contrat" />
						<div class="col-sm-4">
							<div style="float: left;">
								<std:select type="long" required="true" style="float:left;" width="98%" name="employe.type_contrat_enum.id" data="${lisTypeContrat }" labels="libelle" key="id" />
							</div>
							<div style="padding-right: 4px; float: left;">
								<std:linkPopup actionGroup="C" style="line-height: 30px;" params="w_nos=1&valEnum.opc_typenum.id=${typeEmployeEnumId }" classStyle="" noJsValidate="true" action="admin.valTypeEnum.work_init_create">
									<i class="fa fa-reorder" style="color: blue;" title="Liste &eacute;num&eacute;r&eacute;e"></i>
								</std:linkPopup>
							</div>
						</div>

						<std:label classStyle="control-label col-md-2" valueKey="employe.poste" />
						<div class="col-md-4">
							<std:select name="employe.opc_poste.id" type="long" key="id" labels="intitule" data="${listPoste}" />
							<% if(isSubAdd){ %>
							<std:linkPopup action="pers.poste.work_init_create" style="float: right;position: absolute;margin-left: 2%;" icon="fa fa-plus" noJsValidate="true" actionGroup="C" params="w_nos=1" />
							<%} %>
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Modalit&eacute; de paiement" />
						<div class="col-md-4">
							<std:select name="employe.mode_paie" type="string" data="${typeTravail}" />
						</div>
						<std:label classStyle="control-label col-md-2" value="Tarif par unit&eacute" />
						<div class="col-md-4">
							<std:text name="employe.tarif" type="decimal" style="width:30%;" maxlength="15" />
						</div>
					</div>
					<div class="form-group" style="display: none;" id="div-heureParJour">
						<std:label classStyle="control-label col-md-2" value="Heure/jour" />
						<div class="col-md-4">
							<std:text name="employe.heureParJour" type="decimal" maxlength="10" style="width:20%;border-bottom: 1px solid #FF9800 !important;border-bottom-style: dashed !important;text-align:right;" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="employe.cnss" />
						<div class="col-md-4">
							<std:text name="employe.cnss" type="string" style="width:50%;" maxlength="20" />
						</div>
						<std:label classStyle="control-label col-md-2" valueKey="employe.cimr" />
						<div class="col-md-4">
							<std:text name="employe.cimr" type="string" style="width:50%;" maxlength="20" />
						</div>
					</div>

					<div class="form-title">Sortie</div>

					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="employe.type_sortie" />
						<div class="col-md-4">
							<div style="float: left;">
							<std:select name="employe.type_sortie_enum.id" type="long" key="id" labels="libelle" style="width:98%;" data="${lisTypeSortie }" />
							</div>
							<div style="padding-right: 4px; float: left;">
								<std:linkPopup actionGroup="C" style="line-height:30px;" params="w_nos=1&valEnum.opc_typenum.id=${lisTypeSortieEnumId }" classStyle="" noJsValidate="true" action="admin.valTypeEnum.work_init_create">
									<i class="fa fa-reorder" style="color: blue;" title="Liste &eacute;num&eacute;r&eacute;e"></i>
								</std:linkPopup>
							</div>
						</div>
						<std:label classStyle="control-label col-md-2" valueKey="employe.date_sortie" />
						<div class="col-md-4">
							<std:date name="employe.date_sortie" />
						</div>
					</div>

					<div class="form-title">Adresse</div>

					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="adresse.adresse" />
						<div class="col-md-4">
							<std:text name="employe.adresse_rue" type="string" placeholderKey="adresse.rue" style="width:90%;" maxlength="50" />
						</div>
						<std:label classStyle="control-label col-md-2" valueKey="adresse.complement" />
						<div class="col-md-4">
							<std:text name="employe.adresse_compl" type="string" placeholderKey="adresse.complement" style="width:90%;" maxlength="120" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="adresse.ville" />
						<div class="col-md-4">
							<std:select name="employe.opc_ville.id" type="long" data="${listVille }" key="id" labels="libelle" placeholderKey="adresse.ville" style="width:70%;" />
						</div>
					</div>
				</div>

				<!-- Generic Form -->
				<jsp:include page="/domaine/administration/dataValue_form.jsp" />

			</div>
			
			<div class="form-title">Appreciations</div>
			<table id="appreciation-table" class="table table-striped">
			    <thead>
			        <tr>
			            <th>Type</th>
			            <th>Justification</th>
			            <th>Date de Début</th>
			            <th>Action</th>
			        </tr>
			    </thead>
			    <tbody>
			        <%
					if (existingAppreciations == null) {
					%>
					<tr>
					    <td>
					    	<std:select required="true" name="type[]" type="string" classStyle="form-control" data="${appreciationsType}" />
					    </td>
					    <td>
					    	<std:textarea required="true" name="justification[]" maxlength="255" classStyle="form-control"  cols="60" rows="4" />
<!-- 					        <input type="text" name="justification[]" class="form-control"> -->
					    </td>
					    <td>
					        <input type="date" name="date_debut[]" class="form-control">
					    </td>
					    <td>
					        <button type="button" class="btn btn-danger btn-remove-row">-</button>
					    </td>
					</tr>
					<%
					} else {
					    for (EmployeAppreciationPersistant appreciation : existingAppreciations) {
					%>
					<tr>
					    <td>
					    	<std:text  readOnly="readOnly" type="text" name="typeEdit[]" value='<%= appreciation.getType() != null ? appreciation.getType() : "" %>' classStyle="form-control"   />
					    </td>
					    <td>
					      <std:textarea readOnly="true"  name="justificationEdit[]" maxlength="255" classStyle="form-control" value='<%= appreciation.getJustification() != null ? appreciation.getJustification() : "" %>' cols="60" rows="4" />
					    </td>
					    <td>
					    <std:text readOnly="readOnly" type="text" name="date_debutEdit[]" value='<%= appreciation.getDate_debut() != null ? appreciation.getDate_debut() : "" %>' />

<%-- 					        <input type="date" name="date_debut[]" class="form-control" value="<%= appreciation.getDate_debut() %>"> --%>
					    </td>
					    <td>
					        <button type="button" class="btn btn-danger btn-remove-row">-</button>
					    </td>
					</tr>
					<%
					    }
					}
					%>

			    </tbody>
			</table>
			<button type="button" class="btn btn-primary" id="btn-add-row">+</button>
			
			<hr>
			
			
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
			<div class="form-actions">
				<div class="row" style="text-align: center;" class="col-md-12">
					<std:button actionGroup="M" classStyle="btn btn-success" action="pers.employe.work_merge" workId="${employe.id }" icon="fa-save" value="Sauvegarder" />
					<std:button actionGroup="D" classStyle="btn btn-danger" action="pers.employe.work_delete" workId="${employe.id }" icon="fa-trash-o" value="Supprimer" />
				</div>

			</div>
						
			<script type="text/javascript">
    $(document).ready(function() {
        // Add new row
        $("#btn-add-row").click(function() {
            var newRow = `
                <tr>
            	<td>
		    	<std:select name="type[]" type="string" classStyle="form-control" data="${appreciationsType}" />
		    </td>
		    <td>
		    	<std:textarea name="justification[]" maxlength="255" classStyle="form-control"  cols="60" rows="4" />
<!-- 					        <input type="text" name="justification[]" class="form-control"> -->
		    </td>
		    <td>
		        <input type="date" name="date_debut[]" class="form-control">
		    </td>
		    <td>
	        <button type="button" class="btn btn-danger btn-remove-row">-</button>
	    	</td>
                </tr>
            `;
            $("#appreciation-table tbody").append(newRow);
        });

        // Remove row
        $(document).on("click", ".btn-remove-row", function() {
            $(this).closest("tr").remove();
        });
    });
</script>
						
			
		</std:form>
	</div>
</div>





