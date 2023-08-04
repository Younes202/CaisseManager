<%@page import="framework.model.util.FileUtil"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="appli.controller.domaine.stock.bean.TravauxBean"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script type="text/javascript">
var idxFile = 1;

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
	$("#generer_code").click(function(){
		executePartialAjax($(this), '<%=EncryptionUtil.encrypt("stock.travaux.genererCode")%>', 'travaux.code', true, true, null, true);
	});
	
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
	TravauxBean fraisBean = (TravauxBean)request.getAttribute("travaux");
	if(fraisBean != null && fraisBean.getId() != null){
		IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
		Map<String, byte[]> dataimg = service.getDataImage(fraisBean.getId(), "travaux");
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
			$("#photo"+idxFile+"_name_span").html('<a href="front?w_f_act=<%=EncryptionUtil.encrypt("stock.travaux.downloadPieceJointe")%>&pj=<%=EncryptionUtil.encrypt(fraisBean.getId().toString())%>&nm=<%=key%>" target="_blank"><%=key%></a> | <a href="front?w_f_act=<%=EncryptionUtil.encrypt("stock.travaux.downloadPieceJointe")%>&pj=<%=EncryptionUtil.encrypt(fraisBean.getId().toString())%>&nm=<%=key%>&isdown=1" target="downloadframe"><i class="fa fa-cloud-download"></i></a>');
			$("#photo"+idxFile+"_name").val('<%=key%>');
			idxFile++;
	<%}
	} %>
});
</script>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des travaux</li>
		<li>Fiche travaux</li>
		<li class="active">Edition</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="U" classStyle="btn btn-default" action="stock.travaux.work_init_update" workId="${travaux.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		<std:link classStyle="btn btn-default" action="stock.travaux.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
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
		
			<div class="widget-body">
				<div class="row">
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Projet" />
						<div class="col-md-6">
							<std:select name="travaux.opc_chantier.id" width="80%" type="long" key="id" labels="libelle" data="${listeChantier }" />
							<std:link action="stock.travauxChantier.work_init_create" style="float: right;position: absolute;margin-left: 2%;" icon="fa fa-plus" noJsValidate="true" actionGroup="C" params="w_nos=1" />
							
						</div>
					</div>	
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Libellé" />
						<div class="col-md-4">
							<std:text name="travaux.libelle" type="string" placeholder="Libellé" required="true" maxlength="80" />
						</div>
						<std:label classStyle="control-label col-md-2" value="Lieu" />
						<div class="col-md-4">
							<std:text name="travaux.lieu" type="string" placeholder="Lieu" maxlength="80" />
						</div>
					</div>
					<div class="form-group">					
						<std:label classStyle="control-label col-md-2" value="Date début" />
						<div class="col-md-4">
							<std:date name="travaux.date_debut" />
						</div>
						<std:label classStyle="control-label col-md-2" value="Date fin" />
						<div class="col-md-4">
							<std:date name="travaux.date_fin" />
						</div>
					</div>	
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Budget prévu" />
						<div class="col-md-4">
							<std:text name="travaux.budget_prevu" type="decimal" style="width:150px;" placeholder="Budget prévu" maxlength="10" />
						</div>
						<std:label classStyle="control-label col-md-2" value="Budget consommé" />
						<div class="col-md-4">
							<std:text name="travaux.budget_consomme" readOnly="true" type="decimal" style="width:150px;" placeholder="Budget consommé" maxlength="10" />
						</div>
					</div>	
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Responsable" />
						<div class="col-md-4">
							<std:select name="travaux.opc_responsable.id" type="long" key="id" labels="nom;' ';prenom" data="${listeEmploye }" />
						</div>
						<std:label classStyle="control-label col-md-2" value="Employés" />
						<div class="col-md-4">
							<std:select name="travaux.employe_array" width="100%" multiple="true" type="string[]" key="id" labels="nom;' ';prenom" data="${listeEmploye }" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Description" />
						<div class="col-md-10">
							<std:textarea-rich name="travaux.description"/>
						</div>
					</div>
				</div>
				
				<jsp:include page="/domaine/administration/dataValue_form.jsp" />
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
		
<c:if test="${not empty travaux.id }">
		<div class="widget">
			<div class="widget-header bordered-bottom bordered-blue">
				<span class="widget-caption">Charges liées</span>
			</div>
			<div class="widget-body">
				<div class="row">
					<div class="tabbable tabs-left">
                       <ul class="nav nav-tabs" id="myTab3">
	                        <li class="active tab-sky">
	                            <a data-toggle="tab" href="#home1">
	                                Dépenses
	                            </a>
	                        </li>
							<li class="tab-red">
	                            <a data-toggle="tab" href="#home4">
	                                Consommations
	                            </a>
	                        </li>
	                        <li class="tab-red">
	                            <a data-toggle="tab" href="#home2">
	                                Achats
	                            </a>
	                        </li>
	                        <li class="tab-orange">
	                            <a data-toggle="tab" href="#home3">
	                                Salaires
	                            </a>
	                        </li>
	                    </ul>
                        <div class="tab-content" style="padding: 5px;">
                           <div id="home1" class="tab-pane in active">                    
					<!-- Liste des dépenses -->
					<div class="col-md-12">
						<std:link actionGroup="C" classStyle="btn btn-default" action="stock.chargeDivers.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
						
						<cplx:table name="list_chargeDivers" transitionType="simple" forceFilter="true" width="100%" title="Liste des dépenses" checkable="false" initAction="stock.chargeDivers.work_find" autoHeight="true">
							<cplx:header>
								<cplx:th type="empty"></cplx:th>
								<cplx:th type="date" valueKey="chargeDivers.date_mouvement" field="chargeDivers.date_mouvement" width="100" filterOnly="true"/>
								<cplx:th type="string" valueKey="chargeDivers.num_bl" field="chargeDivers.num_bl" width="150"/>
								<cplx:th type="string" valueKey="chargeDivers.libelle" field="chargeDivers.libelle"/>
								<cplx:th type="long[]" valueKey="chargeDivers.type_depense" field="chargeDivers.opc_famille_consommation.id" groupValues="${familleConsommation }" groupKey="id" groupLabel="code;'-';libelle" width="170"/>
								<cplx:th type="long[]" valueKey="chargeDivers.opc_fournisseur" field="chargeDivers.opc_fournisseur.id" groupValues="${listeFournisseur }" groupKey="id" groupLabel="libelle"/>
								<cplx:th type="string" valueKey="chargeDivers.opc_financement_enum" field="chargeDivers.opc_financement_enum.libelle" sortable="false" filtrable="false" width="100"/>
								<cplx:th type="string" value="TVA" field="chargeDivers.opc_tva_enum.id" groupValues="${listeTva }" groupKey="id" groupLabel="libelle" width="80"/>
								<cplx:th type="decimal" valueKey="chargeDivers.montant" field="chargeDivers.montant" width="100"/>
							</cplx:header>
							<cplx:body>
								<c:set var="oldDate" value="${null }"></c:set>
								<c:forEach items="${list_chargeDivers }" var="chargeDivers">
									<c:if test="${oldDate == null  or oldDate != chargeDivers.date_mouvement }">
										<tr>
											<td colspan="9" noresize="true" class="separator-group">
												<span class="fa fa-fw fa-folder-open-o separator-icon"></span> <fmt:formatDate value="${chargeDivers.date_mouvement}"/>
											</td>
										</tr>	
									</c:if>
									<c:set var="oldDate" value="${chargeDivers.date_mouvement }"></c:set>
									
									<cplx:tr workId="${chargeDivers.id }">
										<cplx:td>
											<work:edit-link/>
										</cplx:td>
										<cplx:td>
											<c:if test="${chargeDivers.is_automatique}">
												<i class="fa fa-clock-o" style="color: green;"></i>
											</c:if>	
											<a href="javascript:" id="lnk_det" curr="${chargeDivers.id}"><span class="fa fa-plus" style="color:green;"></span> 
												${empty chargeDivers.num_bl ? '------------' : chargeDivers.num_bl }
											</a>
										</cplx:td>
										<cplx:td value="${chargeDivers.libelle}"></cplx:td>
										<cplx:td value="${chargeDivers.opc_famille_consommation.code}-${chargeDivers.opc_famille_consommation.libelle}"></cplx:td>
										<cplx:td value="${chargeDivers.opc_fournisseur.libelle}"></cplx:td>
										<cplx:td value="${chargeDivers.getPaiementsStr()}"></cplx:td>
										<cplx:td align="right" style="font-weight:bold;" value="${chargeDivers.opc_tva_enum.libelle}"></cplx:td>
										<cplx:td align="right" style="font-weight:bold;" value="${chargeDivers.montant}"></cplx:td>
									</cplx:tr>
									<tr style="display: none;" id="tr_det_${chargeDivers.id}" class="sub">
										<td colspan="9" noresize="true" style="background-color: #fff4d3;cursor: default;" align="center" id="tr_consult_${chargeDivers.id}">
											
										</td>
									</tr>
								</c:forEach>
								<c:if test="${list_chargeDivers.size() > 0 }">
									<tr class="sub">
										<td colspan="8"></td>
										<td align="right">
											<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
												<fmt:formatDecimal value="${totalTtc }"/>
											</span>
										</td>
									</tr>
								</c:if>		
							</cplx:body>
						</cplx:table>
					</div>
				</div>
				<div id="home4" class="tab-pane">
					<!-- Liste des consommations -->
					<div class="col-md-12">
						<std:link actionGroup="C" classStyle="btn btn-default" params="tp=c" action="stock.mouvement.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
						
						<cplx:table name="list_mouvementC" transitionType="simple" width="100%" forceFilter="true" title="Liste des consommations" initAction="stock.mouvement.work_find" autoHeight="true" checkable="false">
							<cplx:header>
								<cplx:th type="empty" />
								<cplx:th type="string" value="BL" field="mouvement.num_bl" width="150" filterOnly="true"/>
								<cplx:th type="string" value="Numéro" field="mouvement.num_bl" width="170" sortable="false" filtrable="false"/>
								<cplx:th type="long[]" value="Stock" field="mouvement.opc_emplacement.id" fieldExport="mouvement.opc_emplacement.titre"  groupValues="${listeDestination }" groupKey="id" groupLabel="titre"/>
								<cplx:th type="decimal" value="Montant H.T" field="mouvement.montant_ht" width="110"/>
								<cplx:th type="decimal" value="Montant TVA" field="mouvement.montant_tva" width="110"/>
								<cplx:th type="decimal" value="Montant T.T.C" field="mouvement.montant_ttc" width="110"/>
							</cplx:header>
							
							<c:set var="bigDecimalUtil" value="<%=new BigDecimalUtil() %>" />
							
							<cplx:body>
								<c:set var="dateUtil" value="<%=new DateUtil() %>" />
								<c:set var="oldDate" value="${null }"></c:set>
								<c:forEach items="${list_mouvementC }" var="mouvement">
									<c:if test="${oldDate == null  or dateUtil.dateToString(oldDate) != dateUtil.dateToString(mouvement.date_mouvement) }">
										<tr>
											<td colspan="7" noresize="true" class="separator-group">
												<span class="fa fa-fw fa-folder-open-o separator-icon"></span>  <fmt:formatDate value="${mouvement.date_mouvement}"/>
											</td>
										</tr>	
									</c:if>
									<c:set var="oldDate" value="${mouvement.date_mouvement }"></c:set>
								
									<cplx:tr workId="${mouvement.id }">
										<cplx:td>
											<work:edit-link/>
										</cplx:td>
										<cplx:td>
											<c:choose>
												<c:when test="${not empty mouvement.num_bl}">
													<c:set var="blFac" value="${mouvement.num_bl}"/>
												</c:when>
												<c:otherwise>
													<c:set var="blFac" value="${mouvement.num_facture}" />
												</c:otherwise>	
											</c:choose>
											
											<a href="javascript:" id="lnk_det" curr="${mouvement.id}">
												<span class="fa fa-plus" style="color:green;"></span>
												${blFac }
											</a>
											
											<c:if test="${not empty mouvement.mouvement_group_id }">
												<i class="fa fa-folder-open" style="color: blue;"  title="Mouvement groupé en date du ${mapDateGroupement.get(mouvement.mouvement_group_id) }"></i>
											</c:if>
										</cplx:td>
										<cplx:td value="${mouvement.opc_emplacement.titre}"></cplx:td>
										<cplx:td align="right" style="font-weight:bold;" value="${mouvement.montant_ht }" />
										<cplx:td align="right" style="font-weight:bold;" value="${mouvement.montant_tva }" />
										<cplx:td align="right" style="font-weight:bold;" value="${mouvement.montant_ttc }" />
									</cplx:tr>
									
									<tr style="display: none;" id="tr_det_${mouvement.id}" class="sub">
										<td colspan="7" noresize="true" style="background-color: #fff4d3;cursor: default;" align="center" id="tr_consult_${mouvement.id}">
											
										</td>
									</tr>
								</c:forEach>	
							</cplx:body>
						</cplx:table>
					</div>
				</div>	
				<div id="home2" class="tab-pane">
					<!-- Liste des achats -->
					<div class="col-md-12">
						<std:link actionGroup="C" classStyle="btn btn-default" params="tp=a" action="stock.mouvement.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
					
						<cplx:table name="list_mouvementA" transitionType="simple" width="100%" forceFilter="true" title="Liste des achats" initAction="stock.mouvement.work_find" autoHeight="true" checkable="false">
							<cplx:header>
								<cplx:th type="empty"></cplx:th>
								<cplx:th type="string" value="Facture" field="mouvement.num_facture" width="150" filterOnly="true"/>
								<cplx:th type="string" value="BL" field="mouvement.num_bl" width="150" filterOnly="true"/>
								<cplx:th type="string" value="Reçu" field="mouvement.num_recu" width="150" filterOnly="true"/>
								<cplx:th type="string" value="Numéro" field="mouvement.num_bl" width="170" sortable="false" filtrable="false"/>
								<cplx:th type="long[]" valueKey="mouvement.opc_fournisseur" field="mouvement.opc_fournisseur.id" groupValues="${listeFournisseur }" groupKey="id" groupLabel="libelle"/>
								<cplx:th type="string" value="Paiement" field="mouvement.opc_financement_enum.code" filtrable="false" sortable="false" width="120"/>
								<cplx:th type="long[]" value="Stock" field="mouvement.opc_emplacement.id" fieldExport="mouvement.opc_emplacement.titre"  groupValues="${listeDestination }" groupKey="id" groupLabel="titre"/>
								<cplx:th type="long[]" valueKey="mouvement.opc_destination" field="mouvement.opc_destination.id" fieldExport="mouvement.opc_destination.titre" groupValues="${listeDestination }" groupKey="id" groupLabel="titre"/>
								<cplx:th type="decimal" value="Montant H.T" field="mouvement.montant_ht" width="110"/>
								<cplx:th type="decimal" value="Montant TVA" field="mouvement.montant_tva" width="110"/>
								<cplx:th type="decimal" value="Remise TTC" field="mouvement.montant_ttc_rem" width="110"/>
								<cplx:th type="decimal" value="Montant T.T.C" field="mouvement.montant_ttc" width="110"/>
							</cplx:header>
							<c:set var="bigDecimalUtil" value="<%=new BigDecimalUtil() %>" />
							
							<cplx:body>
								<c:set var="dateUtil" value="<%=new DateUtil() %>" />
								<c:set var="oldDate" value="${null }"></c:set>
								
								<c:forEach items="${list_mouvementA }" var="mouvement">
									<c:if test="${oldDate == null  or dateUtil.dateToString(oldDate) != dateUtil.dateToString(mouvement.date_mouvement) }">
										<tr>
											<td colspan="13" noresize="true" class="separator-group">
												<span class="fa fa-fw fa-folder-open-o separator-icon"></span>  <fmt:formatDate value="${mouvement.date_mouvement}"/>
											</td>
										</tr>	
									</c:if>
									<c:set var="oldDate" value="${mouvement.date_mouvement }"></c:set>
								
									<cplx:tr workId="${mouvement.id }">
										<cplx:td>
											<work:edit-link/>
										</cplx:td>
										<cplx:td>
											<c:choose>
												<c:when test="${not empty mouvement.num_bl}">
													<c:set var="blFac" value="${mouvement.num_bl}"/>
												</c:when>
												<c:otherwise>
													<c:set var="blFac" value="${mouvement.num_facture}" />
												</c:otherwise>	
											</c:choose>
											
											<a href="javascript:" id="lnk_det" curr="${mouvement.id}">
												<span class="fa fa-plus" style="color:green;"></span>
												${blFac }
											</a>
											
											<c:if test="${not empty mouvement.mouvement_group_id }">
												<i class="fa fa-folder-open" style="color: blue;"  title="Mouvement groupé en date du ${mapDateGroupement.get(mouvement.mouvement_group_id) }"></i>
											</c:if>											
											<c:if test="${mouvement.opc_mouvement.num_bl != null }">
												<i class="fa fa-tty" style="color: green;"  title="Achat généré depuis la commande ${mouvement.opc_mouvement.num_bl }"></i>
											</c:if>
										</cplx:td>
										<cplx:td value="${mouvement.opc_fournisseur.libelle}"></cplx:td>
										<cplx:td value="${mouvement.getPaiementsStr() }"></cplx:td>
										<cplx:td value="${mouvement.opc_emplacement.titre}"></cplx:td>
										<cplx:td value="${mouvement.opc_destination.titre}"></cplx:td>
										<cplx:td align="right" style="font-weight:bold;" value="${mouvement.montant_ht }" />
										<cplx:td align="right" style="font-weight:bold;" value="${mouvement.montant_tva }" />
										
										<cplx:td align="right" style="font-weight:bold;">
											<fmt:formatDecimal value="${mouvement.montant_ttc_rem }" />
											<c:if test="${mouvement.montant_ht_rem!=null and mouvement.montant_ht_rem!=0 }">
												<i class="fa fa-fw fa-gift" style="color: red;font-size: 14px;" data-toggle="tooltip" data-placement="buttom" 
													data-original-title="
														HT avant rem :<br>${bigDecimalUtil.formatNumberZeroBd(bigDecimalUtil.add(mouvement.montant_ht, mouvement.montant_ht_rem))}
														<br>
														Remise HT :<br>${bigDecimalUtil.formatNumberZeroBd(mouvement.montant_ht_rem)}
														<br>
														Remise TTC :<br>${bigDecimalUtil.formatNumberZeroBd(mouvement.montant_ttc_rem)}
														<br>
														TTC avant rem :<br>${bigDecimalUtil.formatNumberZeroBd(bigDecimalUtil.add(mouvement.montant_ttc, mouvement.montant_ttc_rem))}
													"
												></i>
											</c:if>
										</cplx:td>
										<cplx:td align="right" style="font-weight:bold;" value="${mouvement.montant_ttc }" />
									</cplx:tr>
									
									<tr style="display: none;" id="tr_det_${mouvement.id}" class="sub">
										<td colspan="13" noresize="true" style="background-color: #fff4d3;cursor: default;" align="center" id="tr_consult_${mouvement.id}">
											
										</td>
									</tr>
							</c:forEach>	
								<!-- Total -->
								<c:if test="${list_mouvementA.size() > 0 }">
									<tr class="sub">
										<td colspan="9"></td>
										<td align="right">
											<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
												<fmt:formatDecimal value="${totalHt }"/>
											</span>
										</td>
										<td align="right">
											<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
												<fmt:formatDecimal value="${totalTva }"/>
											</span>
										</td>
										<td align="right">
											<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
												<fmt:formatDecimal value="${totalRemise }"/>
											</span>
										</td>
										<td align="right">
											<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
												<fmt:formatDecimal value="${totalTtc }"/>
											</span>
										</td>
									</tr>
								</c:if>
							</cplx:body>
						</cplx:table>
					</div>
				</div>
				<div id="home3" class="tab-pane">	
					<!-- Liste des salaires -->
					<div class="col-md-12">
						<std:link actionGroup="C" classStyle="btn btn-default" action="paie.salariePaie.loadVueMois" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
						
						<cplx:table name="list_salaire" width="100%" checkable="false" title="Liste des salaires" initAction="pers.salaire.work_find" transitionType="simple" autoHeight="true">
							<cplx:header>
								<cplx:th type="empty"></cplx:th>
								<cplx:th type="string" value="Employ&eacute;" field="salaire.opc_employe.id" groupValues="${listEmploye }" groupKey="id" groupLabel="nom;' ';prenom" filterOnly="true"/>
								<cplx:th type="date" valueKey="salaire.date_debut" field="salaire.date_debut" width="120"/>
								<cplx:th type="date" valueKey="salaire.date_fin" field="salaire.date_fin" />
								<cplx:th type="decimal" valueKey="salaire.montant_brut" field="salaire.montant_brut" width="150" />
								<cplx:th type="decimal" valueKey="salaire.montant_net" field="salaire.montant_net" width="150" />
								<cplx:th type="decimal" valueKey="salaire.nbr_conge" field="salaire.nbr_conge" width="150" />
							</cplx:header>
							<cplx:body>
								<c:set var="oldEmpl" value="${null }"></c:set>
								<c:forEach items="${list_salaire }" var="salaire">
									<c:if test="${oldEmpl == null || oldEmpl != salaire.opc_employe.id }">
									     <tr>
											<td colspan="8" noresize="true" style="font-size: 13px;font-weight: bold;color:#e75b8d;">${salaire.opc_employe.nom } ${salaire.opc_employe.prenom }</td>
										</tr>	
									</c:if>
									<c:set var="oldEmpl" value="${salaire.opc_employe.id }"></c:set>
								
									<cplx:tr workId="${salaire.id }">
										<cplx:td>
											<work:edit-link/>
										</cplx:td>	
										<cplx:td value="${salaire.opc_employe.nom} ${salaire.opc_employe.prenom}"></cplx:td>
										<cplx:td value="${salaire.date_debut}"></cplx:td>
										<cplx:td value="${salaire.date_fin}">
											<c:if test="${salaire.list_paiement.size()==0}">  
											    <i class="fa fa-info-circle danger" title="Aucun mode de paiement n'est associ&eacute;"></i>
											</c:if>
										</cplx:td>
										<cplx:td align="right" value="${salaire.montant_brut}"></cplx:td>
										<cplx:td align="right" style="font-weight:bold;" value="${salaire.montant_net}"></cplx:td>
										<cplx:td align="right" value="${salaire.nbr_conge}"></cplx:td>
									</cplx:tr>
								</c:forEach>
							</cplx:body>
						</cplx:table>
					</div>				
				</div>
			</div>
		</div>
	</div>	
				
			</div>
		</div>
</c:if>
		<hr>
		<div class="row" style="text-align: center;">
			<div class="col-md-12">
				<std:button actionGroup="M" classStyle="btn btn-success" action="stock.travaux.work_merge" workId="${travaux.id }" icon="fa-save" value="Sauvegarder" />
				<std:button actionGroup="D" classStyle="btn btn-danger" action="stock.travaux.work_delete" workId="${travaux.id }" icon="fa-trash-o" value="Supprimer" />
			</div>
		</div>
	</std:form>
</div>

