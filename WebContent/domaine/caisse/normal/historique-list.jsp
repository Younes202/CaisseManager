<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="framework.controller.Context"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<%
boolean isManager = ContextAppli.getUserBean().isInProfile("MANAGER");
boolean isAdmin = ContextAppli.getUserBean().isInProfile("ADMIN");

boolean isReprise = "REP".equals(ControllerUtil.getUserAttribute("PLAN_MODE", request));
boolean isConfirmDelCmd = (!isAdmin && !isManager && StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("SECURE_DELETE_CMD")));  
boolean isPrintRaz = StringUtil.isTrueOrNull(""+ControllerUtil.getUserAttribute("RIGHT_IMPRAZ", request));

boolean isPrintHisto = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("PRINT_HISTO")); 
boolean isPrintCmdTmp = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("PRINT_TICKET_TEMP"));
boolean isPrintFacture = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("PRINT_FACTURE"));
boolean isAnnulCmdEncaisse = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("ANNUL_CMD_ENCAISSEE"));

boolean isMttHistoriqueShow = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("SHOW_MTT_HISTO")); 
boolean isMttRepriseShow = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("SHOW_MTT_HISTO_REPRISE"));
boolean isRestau = ContextAppli.IS_RESTAU_ENV();

boolean isMttShow = (isReprise && isMttRepriseShow) || (!isReprise && isMttHistoriqueShow);
%>
<c:set var="isPrintHisto" value="<%=isPrintHisto%>"/>
<c:set var="isPrintCmdTmp" value="<%=isPrintCmdTmp%>" />

<style>
	.modal-backdrop{
		display: none;
	}
	#list_mouvement_filter_form{
		margin-left: 10px;
	}
	#list_mouvement_filter_div{
		margin-top: 20px;
	}
	#list_mouvement_body .sortable tr {
    	height: 46px;
	}
	#list_mouvement_body tr{
		height: 30px;
	}
	.btn_act_tab{
	    height: 28px !important;
    	width: 37px !important;
    	padding-left: 7px !important;
    }
    .btn_act_tab i{
    	font-size: 23px !important;
    	line-height: 27px !important;
    }
</style>

<script type="text/javascript">
$(document).ready(function (){
	init_keyboard_events();
	
	<%// Pour fermer la popup
	if(request.getAttribute("PAGE_JS") != null){%>
	<%=request.getAttribute("PAGE_JS")%>
	<%}%>

	$(document).off('click', ".livraison");
	$(document).on('click', ".livraison", function(){
		var msg = "Marquer cette commande comme <b>non</b> livr&eacute;e.";
		showConfirmDeleteBox('<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.valider_livraison")%>', $(this).attr("params")+"&caisse.id="+$("#caisse\\.id").val(), $("#histo_targ_link"), msg, null, "Marquage des commandes");
	});
	$(document).off('click', "#annul_lnk");
	$(document).on('click', "#annul_lnk", function(){
		showConfirmDeleteBox('<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.annulerCommande")%>', $(this).attr("params")+"&caisse.id="+$("#caisse\\.id").val(), $("#annul_cmd_lnk"), "Cette commande sera annul&eacute;e.<br>Souhaitez-vous continuer ?", null, "Annulation commande");
	});
	<%if(isMttShow || isPrintRaz){%>
	$(document).off('click', "a[id^='lnk_det']");
	$(document).on('click', "a[id^='lnk_det']", function(){
		setTrContent($(this).attr("curr"), "<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.editTrHistorique")%>");
	});
	<%}%>
	// Arret du timer
	if (typeof interval_tracker_id !== 'undefined') {
		for (var i = 1; i < (interval_tracker_id+10); i++){
	        window.clearInterval(i);
		}
	}
	
	$("#div-histo").css("height", ($(window).height()-40)+"px");
	$("#left-div").hide();
	$("#right-div").css("width", "99%");
});
</script>

<div id="div-histo" style="margin-top: 3px;margin-left: 4px;overflow-x: hidden;overflow-y: auto;">
<std:form name="search-form">
	<a href="javascript:" id="histo_targ_link" targetDiv="right-div"></a>
	<std:link action="caisse-web.caisseWeb.initHistorique" targetDiv="right-div" style="display:none;" id="histo-lnk" />
	<std:link id="annul_cmd_lnk" style="display:none;" targetDiv="right-div" />
	
	<div class="widget-header bordered-bottom bordered-blue">
		<span class="widget-caption"><%=isReprise ? "Reprise des commandes":"Historique des commandes"%></span>
			<div class="widget-buttons buttons-bordered" style="margin-bottom: 10px;">
         		<i class="fa fa-keyboard-o" style="font-size: 23px;"></i>         
         		<label>
	                 <input class="checkbox-slider toggle colored-blue" type="checkbox" id="keyboard-activator" style="display: none;">
	                 <span class="text"></span>
	             </label>
         	</div>
	</div>
	<div class="widget-body">
		<div class="row">
			<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
		</div>
		
	<div class="row" style="margin-bottom: 10px;">
		<%
			if(!ContextAppli.getUserBean().isInProfile("SERVEUR")){
		%>
		<std:label classStyle="control-label col-md-1" value="Caisse" />
		<div class="col-md-2">
			<std:select name="caisse.id" mode="std" type="long" data="${listCaisse }" key="id" labels="reference" value="${currCaisseId }" onChange="$('#histo-lnk').trigger('click');" width="100%" />
		</div>
		
		<std:label classStyle="control-label col-md-1" value="Serveur" />
		<div class="col-md-2">
			<std:select name="serveur.id" mode="std" type="long" data="${listServeur }" key="id" labels="login" value="${currServeurId }" onChange="$('#histo-lnk').trigger('click');" width="100%" />
		</div>
		
		<%
			}
		%>
		
		<c:if test="${listTypeCmd != null }">
			<std:label classStyle="control-label col-md-1" value="Type" />
			<div class="col-md-2">
				<std:select name="type_cmd" mode="std" type="string" data="${listTypeCmd }" addBlank="true" value="${curr_typeCmd }" onChange="$('#histo-lnk').trigger('click');" width="100%" />
			</div>	
		</c:if>
		<std:label classStyle="control-label col-md-1" value="Statut" />
		<div class="col-md-2">
			<std:select name="statut_cmd" mode="std" type="string" data="${listStatut }" addBlank="true" value="${curr_statut }" onChange="$('#histo-lnk').trigger('click');" width="100%" />
		</div>	
		
		<std:label classStyle="control-label col-md-1" value="Référence" />
		<div class="col-md-2">
			<std:text name="ref_rep_cmd" type="string" value="${curr_ref_rep }" onChange="$('#histo-lnk').trigger('click');" style="width:100%;" />
		</div>	
	</div>
	
	<c:set var="encryptionUtil" value="<%=new EncryptionUtil()%>" />
	<c:set var="contextRestau" value="<%=new ContextAppli()%>" />
	
	<!-- Liste des mouvements -->
	<cplx:table name="list_mouvement" transitionType="simple" width="100%" autoHeight="true" title="Historique des commandes" initAction="caisse-web.caisseWeb.initHistorique" exportable="false" checkable="false" paginate="false">
		<cplx:header>
		<%if(isMttShow || isPrintRaz){%>
			<cplx:th type="empty" width="50"/>
		<%} %>
			<cplx:th type="string" value="Commande" field="mouvement.ref_commande" width="180" />
			<%if(isMttShow || isPrintRaz){%>
			<cplx:th type="decimal" value="Montant" field="mouvement.mtt_commande" width="90"/>
			<cplx:th type="decimal" value="Montant NET" field="mouvement.mtt_commande_net" width="90"/>
			<%} %>
			<cplx:th type="string" value="Statut" field="mouvement.last_statut"/>
			<cplx:th type="string" value="Caissier" field="mouvement.opc_user.login"/>
		<%if(isRestau){ %>
			<cplx:th type="string" value="Call" field="mouvement.num_token_cmd"/>
			<cplx:th type="string" value="Table" field="mouvement.ref_table" filtrable="false" sortable="false"/>
			<cplx:th type="string" value="Serveur" leftJoin="true" sortable="false" field="mouvement.opc_serveur.login"/>
		<%} %>	
			
			<cplx:th type="string" value="Type" field="mouvement.type_commande"/>
			<cplx:th type="empty" width="210"></cplx:th>
		</cplx:header>
		<cplx:body>
			<c:forEach items="${listMouvement }" var="mouvement">
				<cplx:tr workId="${mouvement.id }" style="height: 44px;">
				<%if(isMttShow || isPrintRaz){%>
					<cplx:td>
						<std:linkPopup classStyle="btn btn-primary btn_act_tab" workId="${mouvement.id}" action="caisse-web.caisseWeb.selectHistorique" tooltip="Consulter">
							<i class="fa fa-eye"></i>
						</std:linkPopup>
					</cplx:td>
				<%} %>
					<cplx:td>
						<%if(isMttShow || isPrintRaz){%>
						<a style="transform: scale(1, 1.8);display: inline-block;font-weight:bold;${mouvement.is_annule?'text-decoration: line-through;color: gray;':'' }" href="javascript:" id="lnk_det" curr="${mouvement.id}">
							<span class="fa fa-plus" style="color:green;"></span> ${mouvement.ref_commande}
						</a>
						<%} else{ %>
							<span style="font-weight:bold;${mouvement.is_annule?'text-decoration: line-through;color: gray;':'' }">${mouvement.ref_commande}</span>
						<%} %>
						<c:if test="${not empty mouvement.mtt_reduction and mouvement.mtt_reduction > 0}">
							<i class="fa fa-gift" style="color: green;" title="Avec r&eacute;duction"></i>
						</c:if>
						<c:if test="${empty mouvement.mode_paiement || mouvement.last_statut=='TEMP'}">
							<i class="fa fa-spinner" style="color: #673ab7;font-size: 14px;font-weight: bold;" title="Commande en attente d'encaissement"></i>
						</c:if>
					</cplx:td>
					<%if(isMttShow || isPrintRaz){%>
					<cplx:td align="right" style="font-weight:bold;">
						<fmt:formatDecimal value="${mouvement.mtt_commande}"/>
					</cplx:td>
					<cplx:td align="right" style="font-weight:bold;" value="${mouvement.mtt_commande_net}"></cplx:td>
					<%} %>
					<cplx:td style="color:blue;font-weight:bold;padding-left:7px;">
						<c:set var="colEtat" value="black" />
						<c:choose>
							<c:when test="${mouvement.last_statut=='ANNUL' }">
								<i class="fa fa-user-times" style="color: red;font-size: 22px;font-weight: bold;"></i>
								<c:set var="colEtat" value="red" />
							</c:when>
							<c:when test="${mouvement.last_statut=='TEMP'}">
								<i class="fa fa-spinner" style="color: #673ab7;font-size: 22px;font-weight: bold;" title="Commande en attente d'encaissement"></i>
								<c:set var="colEtat" value="#673ab7" />
							</c:when>
							<c:when test="${mouvement.last_statut=='PREP' }">
								<img src="resources/restau/img/cuisine-image-animee-0008.gif" style="width: 25px;" />
								<c:set var="colEtat" value="#795548" />	
							</c:when>
							<c:when test="${mouvement.last_statut=='PRETE' }">
								<i class="fa fa-check-square-o" style="color: green;font-size: 22px;font-weight: bold;"></i>
								<c:set var="colEtat" value="green" />
							</c:when>
							<c:when test="${mouvement.last_statut=='VALIDE' }">
								<i class="fa fa-save" style="color: blue;font-size: 22px;font-weight: bold;"></i>
								<c:set var="colEtat" value="blue" />
							</c:when>
							<c:when test="${mouvement.last_statut=='LIVRE' }">
								<i class="fa fa-motorcycle" style="color: black;font-size: 22px;font-weight: bold;"></i>
								<c:set var="colEtat" value="black" />
							</c:when>
						</c:choose>
						<span style="padding-left: 5px;color:${colEtat}">${contextRestau.getLibelleStatut(mouvement.last_statut) }</span>
					</cplx:td>
					<cplx:td value="${mouvement.opc_user.login}"></cplx:td>
					<%if(isRestau){ %>
					<cplx:td value="${mouvement.num_token_cmd}" align="right"></cplx:td>
					<cplx:td style="font-size: 17px;font-weight: bold;" align="center">
						<span style="text-transform: uppercase;transform: scale(1, 1.8);display: inline-block;">${mouvement.getRefTablesDetail()}</span>
					</cplx:td>
					<cplx:td style="font-weight:bold;color:#ff5722;padding-left: 15px;">
						<span style="text-transform: uppercase;transform: scale(1, 1.8);display: inline-block;">${mouvement.opc_serveur.login}</span>
					</cplx:td>
					<%} %>
					<cplx:td style="color:blue;">
						<c:choose>
							<c:when test="${mouvement.type_commande == 'P' }">
								Sur place
							</c:when>
							<c:when test="${mouvement.type_commande == 'E' }">
								A emporter
							</c:when>
							<c:when test="${mouvement.type_commande == 'L' }">
								Livraison
							</c:when>
						</c:choose>
						<c:if test="${not empty mouvement.opc_livreurU }"> 
							&nbsp;[<span style="color:blue;">${mouvement.opc_livreurU.login }</span>]
						</c:if>
					</cplx:td> 
					<cplx:td>
						<c:if test="${isPrintHisto or empty mouvement.mode_paiement}">
							<c:if test="${isPrintCmdTmp or not empty mouvement.mode_paiement}">
								<std:link classStyle="btn btn-success shiny btn_act_tab" style="color:black;" params="mvm=${mouvement.id}" targetDiv="div_gen_printer" action="caisse-web.caisseWeb.print" tooltip="Imprimer le ticket">
									<i class="fa fa-print"></i>
								</std:link>
							</c:if>
						</c:if>
						<c:if test="${mouvement.last_statut != 'ANNUL'}">
							<%if(isReprise){ %>
								<std:link classStyle="btn btn-default shiny btn_act_tab" id="restit_lnk"  style="color:blue;" workId="${mouvement.id}" action="caisse-web.caisseWeb.restituerInfosCommande" targetDiv="left-div" tooltip="Reprendre la commande">
									<i class="fa fa-reply-all"></i>
								</std:link>
							<%} %>
							<c:set var="isAnnulCmdEncaisse" value="<%=isAnnulCmdEncaisse%>" />
							<c:set var="isConfirmDelCmd" value="<%=isConfirmDelCmd%>" />
							<c:set var="isConfirmDelCmdConf" value="${mouvement.getId() != null and !mouvement.getLast_statut().equals('TEMP')}" />
							
							<c:if test="${isAnnulCmdEncaisse or empty mouvement.mode_paiement }">
								<c:choose>
									<c:when test="${isConfirmDelCmd and isConfirmDelCmdConf}">
										<std:linkPopup classStyle="btn btn-default shiny btn_act_tab" action="caisse-web.caisseWeb.loadConfirmAnnule" params="tp=histo&workId=${encryptionUtil.encrypt(mouvement.id)}" tooltip="Annuler la commande">
											<i class="fa fa-ban"></i>
										</std:linkPopup>
									</c:when>
									<c:otherwise>
										<a id="annul_lnk" class="btn btn-default shiny btn_act_tab" style="color:orange;" params="tp=histo&workId=${encryptionUtil.encrypt(mouvement.id)}" title="Annuler la commande">
											<i class="fa fa-ban" style="color: green;"></i>
										</a>
									</c:otherwise>
								</c:choose>
							</c:if>
							
							<c:if test="${mouvement.type_commande == 'L'}">
								<c:if test="${empty mouvement.opc_livreurU }">
									<std:linkPopup classStyle="btn btn-default shiny btn_act_tab" action="caisse-web.caisseWeb.initLivraison" style="color:#d73d32;" workId="${mouvement.id }" tooltip="Marquer cette commande comme livr&eacute;e">
										<i class="fa fa-truck"></i>
									</std:linkPopup>
								</c:if>
								<c:if test="${not empty mouvement.opc_livreurU }">
									<a href="javascript:" class="btn btn-default shiny livraison btn_act_tab" style="color:green;" params="workId=${encryptionUtil.encrypt(mouvement.id)}" title="Marquer cette commande comme non livr&eacute;e">
										<i class="fa fa-truck" style="color: green"></i>
									</a>
								</c:if>
							</c:if>
							
							<%if(isPrintFacture){ %>
								<std:linkPopup actionGroup="S" classStyle="btn btn-default shiny btn_act_tab" style="color:#d73d32;" action="stock-caisse.mouvementStock.init_pdf_facture" params="src=cai" workId="${mouvement.id}" tooltip="Imprimer la facture">
									<i class="fa fa-file-pdf-o"></i>
								</std:linkPopup>
							<%} %>
							
						</c:if>	
					</cplx:td>
				</cplx:tr>
				<tr style="display: none;" id="tr_det_${mouvement.id}" class="sub">
					<td colspan="9" noresize="true" style="background-color: #fff4d3;cursor: default;" align="center" id="tr_consult_${mouvement.id}">
						
					</td>
				</tr>
			</c:forEach>
		</cplx:body>
	</cplx:table>
</div>	
 </std:form>	
</div>