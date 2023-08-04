<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="fn"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>


<%
boolean isMasterCloud = "cloudMaster".equals(StrimUtil.getGlobalConfigPropertieIgnoreErreur("context.install"));
%>
<style>
   .form-title{ 
    	margin-left: 12px;
	}
</style>

<script type="text/javascript">
	$(document).ready(function (){
		$("#purge_lnk").click(function(){
			if($("#calcul_span").css("display") != "none"){
				alertify.error("Un traitement de purge est déj&agrave; en cours veuillez patienter.");
				return;
			}
			showConfirmDeleteBox('<%=EncryptionUtil.encrypt("admin.parametrage.purger_base")%>', 'etat='+$(this).attr('curr'), $(this), "Vous &ecirc;tes sur le point de purger les données.<br>Voulez-vous confirmer ?", "startAnimPurge()", "Purge des données");
		});
	});
	
	function startAnimPurge(){
		$("#calcul_span").show();
	}
</script>	
	
<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Paramétrage</li>
		<li class="active">Maintenance</li>
	</ul>
</div>

<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
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
		        <div class="col-lg-12 col-sm-12 col-xs-12">
		              <div class="tabbable">
		                    <ul class="nav nav-tabs" id="myTab">
		                          <%request.setAttribute("mnu_param", "maintenance"); %> 
			 					  <jsp:include page="parametrage_headers.jsp" />
		                     </ul>
		                </div>
		          </div>
		      </div>
		
			<div class="widget-body">
			<%if(!isMasterCloud){ %> 
				<div class="row">
					<div class="alert alert-warning fade in">
	                    <button class="close" data-dismiss="alert">
	                        x
	                    </button>
	                    <i class="fa-fw fa fa-warning"></i>
	                    <strong style="color: orange;text-align: center;">Attention :  Utiliser ce module avec précaution car il impacte la base de données.</strong><br>
	                    <b>Purge des données</b><br>
	                    <i class="fa fa-info-circle" style="color: red;"></i> La purge des données est irréversible.
	                    <i class="fa fa-info-circle" style="color: red;"></i> Les données purgées sont celles qui ne sont plus utiles pour les calculs car une fois le mois cl&ocirc;turé car les chiffres calculés sont stockés.
	                    <i class="fa fa-info-circle" style="color: red;"></i> Un mois purgé ne peut plus &ecirc;tre ré-ouvert.
	                    <i class="fa fa-info-circle" style="color: red;"></i> La purge doit se faire de préférence, quand aucune opération n'est en cours dans l'application.
	                    <br><b>Sauvegarde de la base</b><br>
	                    <i class="fa fa-info-circle" style="color: red;"></i> La sauvegarde de la base permet sa restitution en cas d'erreur de purge et avant la réutilisation du systeme.
	                    <i class="fa fa-info-circle" style="color: red;"></i> La base sauvegardée peut aussi servir pour consulter l'historique des données purgées.
	                    <i class="fa fa-info-circle" style="color: red;"></i> La base sauvegardée doit &ecirc;tre montée avant son utilisation via une retauration technique.
	                </div>
	              </div>
	             <%} %>
	              <div class="row">
	              	<div class="col-lg-6 col-sm-6 col-xs-12">
		              	<div class="widget">
			                  <div class="widget-header bg-palegreen">
			                      <i class="widget-icon fa fa-check"></i>
			                      <span class="widget-caption">Informations application</span>
			                  </div><!--Widget Header-->
			                  <div class="widget-body">
			                   <%
			                  Map<String, String> systemInfos = (Map<String, String>)request.getAttribute("systemInfos");
			                  %>
			                  <table><tr><td>
			                  	Taille de la base de données :  
			                  	</td>
			                  	<td style="font-weight: bold;text-align: right;">&nbsp; <%=systemInfos.get("DB_SIZE")%> Mo</td>
			                  	</tr>
			                  	<tr><td align="right">
			                    Taille des images et des fichiers : </td>
			                    <td style="font-weight: bold;text-align: right;">&nbsp; <%=systemInfos.get("FILES_SIZE")%> Mo</td>
			                    </tr>
			                     <tr><td>
			                    Mémoire RAM application : </td>
			                    <td style="font-weight: bold;text-align: right;">&nbsp; <%=systemInfos.get("RAM_SYSTEM")%> Go</td>
			                    </tr>
			                    </table>
			                  </div><!--Widget Body-->
			              </div>
	              		</div>
	              		<div class="col-lg-6 col-sm-6 col-xs-12">
		              	<div class="widget">
			                  <div class="widget-header bg-palegreen">
			                      <i class="widget-icon fa fa-check"></i>
			                      <span class="widget-caption">Informations syst&egrave;me</span>
			                  </div><!--Widget Header-->
			                  <div class="widget-body">
			                  <table>
			                    <tr><td>
			                    Disque dur : </td>
			                    <td style="font-weight: bold;text-align: right;">&nbsp; <%=systemInfos.get("HDD_SIZE")%> Go</td>
			                    </tr>
			                    <tr><td>
			                    Mémoire RAM : </td>
			                    <td style="font-weight: bold;text-align: right;">&nbsp; <%=systemInfos.get("RAM_SERVEUR_SIZE")%> Go</td>
			                    </tr>
			                    <tr><td>
			                    Syst&egrave;me : </td>
			                    <td style="font-weight: bold;text-align: right;">&nbsp; <%=systemInfos.get("OS_NAME")%></td>
			                    </tr>
			                    </table>
			                  </div><!--Widget Body-->
			              </div>
	              		</div>
	              </div>
	              <div class="row">
	              	<%if(!isMasterCloud){ %> 
	              	<div class="col-lg-6 col-sm-6 col-xs-12">
		              	<div class="widget">
			                  <div class="widget-header bg-lightred">
			                      <i class="widget-icon fa fa-check"></i>
			                      <span class="widget-caption">Sauvegarde des données</span>
			                  </div><!--Widget Header-->
			                  <div class="widget-body" style="float: left;">
			                  	 <div style="width: 100%;float: left;">
			                  	 	<span style="line-height: 30px;float: left;">Chemin de sauvegarde (local ou réseau) : </span> 
			                  	 	<std:text name="cheminBckDb" type="string" forceWriten="true" />
			                  	 </div>
			                     <div style="width: 100%;float: left;margin-top: 10px;">
			                     	<std:link params="mode=M" action="admin.parametrage.dumper_base" style="width: 320px;text-align: left;float:left;" classStyle="btn btn-primary" icon="fa-check" value="Sauvegarder manuellement la base de données" />
			                     	<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Réaliserune sauvegarde manuelle de la base de données." src="resources/framework/img/info.png" style="vertical-align: bottom;float:left;"/>
			                     </div>
			                     <div style="width: 100%;margin-top: 10px;float: left;">
				                     <span style="line-height: 30px;float: left;">Heure sauvegarde 1 : </span> 
				                     <std:text name="startDt1" type="string" mask="99:99" forceWriten="true" style="width:60px;float:left;" />
				                     
				                     <span style="line-height: 30px;margin-left:5px;float: left;">Heure suvegarde 2 : </span> 
				                     <std:text name="startDt2" type="string" mask="99:99" forceWriten="true" style="width:60px;float:left;" />
				                     
				                     <std:link params="mode=A" action="admin.parametrage.dumper_base" style="width: 120px;margin-left:5px;text-align: left;" classStyle="btn btn-primary" icon="fa-check" value="Sauvegarder" />
				                     <img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Saisir les horaires ou l'activité est au minimum pour réaliser la sauvegarde de la base." src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
			                     </div>
			                  </div><!--Widget Body-->
			              </div>
	              		</div>
	              <%} %>		
	             <%if(!isMasterCloud){ %> 		
	              		<div class="col-lg-6 col-sm-6 col-xs-12">
	              			<div class="widget">
			                  <div class="widget-header bg-gold">
			                      <i class="widget-icon fa fa-check"></i>
			                      <span class="widget-caption">Purge des données</span>
			                  </div><!--Widget Header-->
			                  <div class="widget-body">
			                  	<c:choose>
			                  		<c:when test="${empty moisPurge }">
			                  			Aucun mois n'est disponible pour la purge des données.
			                  		</c:when>
			                  		<c:otherwise>
			                  			<table style="width: 100%;">
			                  				<tr style="border-bottom: 1px dashed #427fed;">
			                  					<td>Ventes caisses</td>
			                  					<td><std:checkbox name="purge_VEC" checked="true" forceWriten="true" /></td>
			                  					<td>Ventes hors caisses</td>
			                  					<td><std:checkbox name="purge_VEA" checked="true" forceWriten="true"/></td>
			                  				</tr>
			                  				<tr style="border-bottom: 1px dashed #427fed;">	
			                  					<td>Achats</td>
			                  					<td><std:checkbox name="purge_ACH" checked="true" forceWriten="true"  /></td>
			                  					<td>Dépenses</td>
			                  					<td><std:checkbox name="purge_DEP" checked="true" forceWriten="true"  /></td>
			                  				</tr>
			                  				<tr style="border-bottom: 1px dashed #427fed;">	
			                  					<td>Recettes</td>
			                  					<td><std:checkbox name="purge_REC" checked="true" forceWriten="true"  /></td>
			                  					<td>Mouvements stock
			                  					<i class="fa fa-info-circle" data-toggle="tooltip" data-placement="top" data-original-title="Cette purge concerne : avoirs, préparations, transferts, consommations, ch&egrave;ques fournisseurs"></i>
			                  					</td>
			                  					<td><std:checkbox name="purge_MVM" checked="true" forceWriten="true"  /></td>
			                  				</tr>
			                  				<tr style="border-bottom: 1px dashed #427fed;">	
			                  					<td>Inventaires</td>
			                  					<td><std:checkbox name="purge_INV" checked="true" forceWriten="true"  /></td>
			                  					<td>Ecritures comptables</td>
			                  					<td><std:checkbox name="purge_ECR" checked="true" forceWriten="true"  /></td>
			                  				</tr>
			                  				<tr style="border-bottom: 1px dashed #427fed;">	
			                  					<td>Journées</td>
			                  					<td><std:checkbox name="purge_JOU" checked="true" forceWriten="true"  /></td>
			                  				</tr>	
			                  			</table>
			                  		
			                  		
			                  			<c:set var="encryptUtil" value="<%=new EncryptionUtil() %>" />
			                     		<a id="purge_lnk" class="btn btn-danger" href="javascript:void(0);" style="margin-left: 25%;margin-top:12px;" curr="${encryptUtil.encrypt(moisPurge.id) }"><i class="fa fa-times"></i> 
			                     			Purger les données du mois [<span style="color:yellow;font-size: 13px !important;height: 22px;font-weight: bold;"><fmt:formatDate value="${moisPurge.date_etat }" pattern="MM/yyyy"/></span>]
			                     		</a>
			                     		<span id="calcul_span" style="width: 100%;float: left;margin-bottom: 17px;color: #F44336;font-style: italic;display:none;"><img src='resources/framework/img/select2-spinner.gif' /> Patientez, cette opération prendra plusieurs minutes ... </span>
			                     	<br>
			                     	<span style="font-size: 11px;color: fuchsia;">* Par précaution, pensez &agrave; réaliser un inventaire et &agrave; faire une sauvegarde des données</span>
			                  		</c:otherwise>
			                  	</c:choose>
			                  
			                  </div><!--Widget Body-->
			              </div>
	              		</div>
	              <%} %>		
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