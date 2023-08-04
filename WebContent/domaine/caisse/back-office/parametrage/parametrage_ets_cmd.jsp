<%@page import="appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS"%>
<%@page import="framework.model.beanContext.EtablissementPersistant"%>
<%@page import="java.util.HashMap"%>
<%@page import="framework.model.beanContext.EtablissementOuverturePersistant"%>
<%@page import="framework.model.beanContext.AbonnementBean"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="appli.model.domaine.administration.persistant.ParametragePersistant"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="fn"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
   .form-title{ 
    	margin-left: 12px;
	}
</style>

<%
EtablissementPersistant restauBean = (EtablissementPersistant)request.getAttribute("etablissement");
%>

<script type="text/javascript">
$(document).ready(function() {
   $("a[id='copie-cal']").click(function(){
	    var tarKey = parseInt($(this).attr("key"));
	    var srcKey = parseInt($(this).attr("key"))-1;
	    //
	    $("#heure_debut_matin_"+tarKey).val($("#heure_debut_matin_"+srcKey).val());
		$("#heure_fin_matin_"+tarKey).val($("#heure_fin_matin_"+srcKey).val());
		$("#heure_debut_midi_"+tarKey).val($("#heure_debut_midi_"+srcKey).val());
		$("#heure_fin_midi_"+tarKey).val($("#heure_fin_midi_"+srcKey).val());
   });
});
</script>

<%
	Map<String, String> mapJours = new HashMap<>();
	mapJours.put("1", "Lundi");
	mapJours.put("2", "Mardi");
	mapJours.put("3", "Mercredi");
	mapJours.put("4", "Jeudi");
	mapJours.put("5", "Vendredi");
	mapJours.put("6", "Samedi");
	mapJours.put("7", "Dimanche");
%>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Paramétrage</li>
		<li class="active">Etablissement/Commande en ligne</li>
	</ul>
</div>

<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="C" classStyle="btn btn-default" action="admin.parametrage.work_init_update" icon="fa-3x fa-pencil" tooltip="Modifier" />
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
		
		<%if(ControllerUtil.getMenuAttribute("isMnuCaiBo", request) == null){ %>
			<div class="row">
		        <div class="col-lg-12 col-sm-12 col-xs-12">
		              <div class="tabbable">
		                    <ul class="nav nav-tabs" id="myTab">
		                          <%request.setAttribute("mnu_param", "cmdline"); %> 
			 					  <jsp:include page="parametrage_headers.jsp" />
		                     </ul>
		                </div>
		          </div>
		      </div>
		   <%} %>
			<div class="widget-body">
				<div class="row">
					<div class="col-md-6">
						<fieldset>
							<h3 style="color: #2dc3e8;padding-left: 20px;border-bottom: 1px dashed rgba(0, 0, 0, .2);">INFORMATIONS D'OUVERTURE</h3>
							<table id="cal-table">
								<tr style="background-color: #ebf3dd;">
									<td style="width: 50px;"></td>
									<th style="width: 140px;" valign="middle">Jour</th>
									<th colspan="2" style="width: 180px;text-align: center;">Matin</th>
									<th colspan="2" style="width: 180px;text-align: center;">Apr&egrave;s-midi</th>
								</tr>
								<tr style="background-color: #ebf3dd;">
									<th></th>
									<th></th>
									<th style="font-weight: normal;">Heure début</th>
									<th style="font-weight: normal;">Heure fin</th>
									<th style="font-weight: normal;">Heure début</th>
									<th style="font-weight: normal;">Heure fin</th>
								</tr>
								<c:set var="encodeService" value="<%=new EncryptionUtil()%>" />
						<%
						boolean isEditionPage = ControllerUtil.isEditionWritePage(request);
						for (String key : mapJours.keySet()) {%>
							<c:set var="currOuv" value="${null }"/>
							<c:set var="jour" value="<%=key %>"/>
							
							<%
							EtablissementOuverturePersistant restauOuvertureP = null;
							if(restauBean != null){
								for(EtablissementOuverturePersistant roP : restauBean.getList_ouverture()){
									if(key.equals(roP.getJour_ouverture())){%>
										<c:set var="currOuv" value="<%=roP %>"/> 
									<%
									break;
									}
								}
							}
						
						%>
								<tr style="height: 30px; border-bottom: 1px solid #FFC107;">
									<td align="center">
										<std:hidden name="eaiid_${jour}" value="${encodeService.encrypt(currOuv.id) }" />
										<std:hidden name="jour_ouverture_${jour}" value="${jour}" />
										<std:checkbox name="jour_${jour}" checked="${currOuv!=null ? true:false }" />
									</td>
									<td style="width: 120px;">
										<%=mapJours.get(key)%>
									</td>
									<td>
										<std:text name="heure_debut_matin_${jour}" mask="99:99" type="string" maxlength="5" style="width: 50px;height: 26px;padding: 5px;float: left;" value="${currOuv.heure_debut_matin}"/>
									</td>
									<td>
										<std:text name="heure_fin_matin_${jour}" mask="99:99" type="string" maxlength="5" style="width: 50px;height: 26px;padding: 5px;float: left;" value="${currOuv.heure_fin_matin}"/>
									</td>
									<td>
										<std:text name="heure_debut_midi_${jour}" mask="99:99" type="string" maxlength="5" style="width: 50px;height: 26px;padding: 5px;float: left;" value="${currOuv.heure_debut_midi}"/>
									</td>
									<td>
										<std:text name="heure_fin_midi_${jour}" mask="99:99" type="string" maxlength="5" style="width: 50px;height: 26px;padding: 5px;float: left;" value="${currOuv.heure_fin_midi}"/>
										<%if(isEditionPage && !key.equals("1")){ %>
										<a href="javascript:" id="copie-cal" key="<%=key%>">Copier</a> 
										<%} %>
									</td>
								</tr>
						<% } %>
							</table>
						</fieldset>	
					</div>
					<div class="col-md-6">
					<%if(ContextAppli.getAbonementBean().isOptPlusCmdVitrine() 
							&& (ContextAppli.IS_MARKET_ENV() || ContextAppli.IS_RESTAU_ENV())){ %>
						<fieldset>
							<h3 style="color: #2dc3e8;padding-left: 20px;border-bottom: 1px dashed rgba(0, 0, 0, .2);">INFORMATIONS COMMANDE</h3>
							<div class="form-group">
								<std:label classStyle="control-label col-md-8" value="Validation auto compte client" />
								<div class="col-md-4">
									<std:checkbox name="etablissement.is_valid_cpt" checked="${etablissement.is_valid_cpt }" />
									<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Valider les " src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
								</div>
							</div>
							<div class="form-group">	
								<std:label classStyle="control-label col-md-8" value="Autoriser commandes hos ouverture" />
								<div class="col-md-4">
									<std:checkbox name="etablissement.is_cmd_ets_ferme" checked="${etablissement.is_cmd_ets_ferme }" />
									<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Autoriser les commandes si l'établissement est fermé" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
								</div>
							</div>
							<div class="form-group">
								<std:label classStyle="control-label col-md-8" value="Validation auto commande espèces" />
								<div class="col-md-4">
									<std:checkbox name="etablissement.is_valid_auto_esp" checked="${etablissement.is_valid_auto_esp }" />
									<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Valider les commandes depuis le mobile par l'établissement avant leur prise en compte" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
								</div>
							</div>
<!-- 							<div class="form-group">	 -->
<%-- 								<std:label classStyle="control-label col-md-8" value="Validation auto commande carte" /> --%>
<!-- 								<div class="col-md-4"> -->
<%-- 									<std:checkbox name="etablissement.is_valid_auto_cb" checked="${etablissement.is_valid_auto_cb }" /> --%>
<!-- 									<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Valider les commandes depuis le mobile par l'établissement avant leur prise en compte" src="resources/framework/img/info.png" style="vertical-align: bottom;"/> -->
<!-- 								</div> -->
<!-- 							</div> -->
							<div class="form-group">
								<std:label classStyle="control-label col-md-8" value="Durée moyenne commande" />
								<div class="col-md-4">
									<std:text name="etablissement.duree_cmd" type="long" placeholder="Durée" maxlength="5" style="width: 120px;" value="${etablissement.duree_cmd }" /> Minutes
									<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Temps moyen en minute pour préparer une commande. Si saisi alors les commandes seront contrôlées pour vérifier la cohérence"/>
								</div>
							</div>
							<div class="form-group">	
								<std:label classStyle="control-label col-md-8" value="Maximum écart heures commande" />
								<div class="col-md-4">
									<std:text name="etablissement.max_heure_cmd" type="long" placeholder="Heures" maxlength="5" style="width: 120px;" value="${etablissement.max_heure_cmd }" /> Heures
									<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Nombre d'heures futures maximum au delà duquel on ne peut pas commander"/>
								</div>
							</div>
							<div class="form-group">	
								<std:label classStyle="control-label col-md-8" value="Distance maximale client" />
								<div class="col-md-4">
									<std:text name="etablissement.max_dist" type="long" placeholder="Km" maxlength="5" style="width: 120px;" value="${etablissement.max_dist }" /> Km
									<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="DDistance maximale entre le client et l'établissement autorisée pour les commande livraison"/>
								</div>
							</div>
						</fieldset>
					<%} %>	
					</div>
				</div>
				<hr>
				<div class="row">
					<div class="col-md-6">
						<h3 style="color: #2dc3e8;padding-left: 20px;border-bottom: 1px dashed rgba(0, 0, 0, .2);">ETABLISSEMENT</h3>
						<c:forEach items="${listParams }" var="parametre">
							<c:if test="${parametre.groupe == 'CAISSE_ETS_CMD'}">
								<div class="form-group">
									<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
									<div class="col-md-7">
										<c:choose>
											<c:when test="${parametre.type=='STRING'}">
												<std:text name="param_${parametre.code}" type="string" style="width:50%;float:left;" maxlength="120" value="${parametre.valeur}" />
											</c:when>
											<c:when test="${parametre.type=='NUMERIC'}">
												<std:text name="param_${parametre.code}" type="long" style="width:120px;float:left;" maxlength="120" value="${parametre.valeur}" />
											</c:when>
											<c:when test="${parametre.type=='DECIMAL'}">
												<std:text name="param_${parametre.code}" type="decimal" style="width:120px;float:left;" maxlength="120" value="${parametre.valeur}" />
											</c:when>
											<c:when test="${parametre.type=='BOOLEAN'}">	
												<std:checkbox name="param_${parametre.code}" checked='${parametre.valeur }' />
											</c:when>
										</c:choose>
										<c:if test="${parametre.help != null && parametre.help != ''}">
											<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="${parametre.help}" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
										</c:if>
									</div>
								</div>
							</c:if>
						</c:forEach>	
					</div>
				</div>

				<%if(ContextAppli.IS_ERP_ENV()) { %>				
					<hr>
					<div class="row">
						<div class="col-md-6">
							<h3 style="color: #2dc3e8;padding-left: 20px;border-bottom: 1px dashed rgba(0, 0, 0, .2);">LIEUX ET REMPLAÇANTS</h3>
							<c:forEach items="${listParams }" var="parametre">
								<c:if test="${parametre.groupe == 'AGENDA_ETS'}">
									<c:if test="${parametre.code == 'LIEU_TRAVAIL'}">
										<div class="form-group">
											<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
											<div class="col-md-7">
												<std:select name="param_${parametre.code}" type="long" style="width:50%;float:left;" key="id" labels="libelle" data="${listeLieux }" value="${parametre.valeur}" />
											</div>
										</div>
									</c:if>	
									<c:if test="${parametre.code == 'SPECIALITE'}">
										<div class="form-group">
											<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
											<div class="col-md-7">
												<std:select name="param_${parametre.code}" type="long" style="width:50%;float:left;" key="id" labels="libelle" multiple="true" data="${listeSpecialite }" value="${parametre.valeur}" />
											</div>
										</div>
									</c:if>	
								</c:if>
							</c:forEach>	
						</div>
					</div>
				<%} %>
			</div>
			
		<hr>
		<div class="form-actions">
			<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
				<std:button actionGroup="M" classStyle="btn btn-success" action="admin.parametrage.work_update" params="tp=cmdl" icon="fa-save" value="Sauvegarder" />
			</div>
		</div>
		</std:form>
	</div>
</div>

<script type="text/javascript">
/*Handles ToolTips*/
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script>    