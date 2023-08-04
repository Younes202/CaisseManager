<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.util.audit.ReplicationGenerationEventListener"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
	#tasks_table tr{
		line-height: 57px;
		border-top: 1px solid #cacaca;
	}
</style>
<!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li class="active">Tâches super admin</li>
     </ul>
 </div>
 <!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>

	<!-- row -->
	<div class="row" style="margin-left: 3px;margin-top: 13px;">
		<std:form name="search-form">
			<table style="width: 100%;" id="tasks_table">
				<tr>
					<td>
						<std:link actionGroup="C" classStyle="btn btn-primary" value="Editeur de requête" action="admin.requeteur.work_edit" icon="fa fa-database" tooltip="Editeur de requête" /> 
					</td>
					<td>	
						<std:link actionGroup="C" classStyle="btn btn-primary" value="Logs apache" action="admin.requeteur.find_logs" icon="fa fa-database" tooltip="Surveillance des logs" /> 
					</td>
					<td>	
						<std:link actionGroup="C" classStyle="btn btn-primary" value="Logs custom" action="admin.requeteur.find_logs" params="iscust=1" icon="fa fa-database" tooltip="Surveillance des logs custom" />
					</td>
				</tr>
				<tr>	
					<td>	
						<std:link actionGroup="C" classStyle="btn btn-primary" value="Script View" action="admin.requeteur.run_script_view" icon="fa fa-database" tooltip="Exécuter script view" />
					</td>
					<td>	
						<std:link actionGroup="C" classStyle="btn btn-primary" value="Focer la conf et abonnement" action="admin.requeteur.force_conf_abn" icon="fa fa-gears" tooltip="Recharger la conf et abonnement cloud" />
					</td>
					<td>	
						<std:link actionGroup="C" classStyle="btn btn-primary" value="Recharger le paramètrage" action="admin.requeteur.recharger_params_right" icon="fa fa-gears" tooltip="Recharger paramétrage" />
					</td>
				</tr>		
				<%if(ReplicationGenerationEventListener._IS_LOCAL_SYNCHRO_INSTANCE){ %> 
				<tr>
					<td colspan="2">
						<std:select name="dataSync" type="string" data="${listEntite}" />
						<std:link actionGroup="C" classStyle="btn btn-default" value="Mettre données en attente Synchro **" action="admin.requeteur.synchroniseData" params="iscust=1" icon="fa fa-rss" tooltip="Synchro de toutes les données locales avec le distant" />
					</td>
					<td>
						<std:link actionGroup="C" classStyle="btn btn-default" value="Synchroniser données en attente avec Cloud" action="admin.requeteur.synchroniseAllWaiting" icon="fa fa-rss" tooltip="Synchro des données" />
					</td>
				</tr>		
				<%} %>
				<tr>
					<td>
						<std:link actionGroup="C" classStyle="btn btn-danger" value="Fusionner les articles (stock)" action="stock.article.fusionnerArticleDoublon" icon="fa fa-tasks" tooltip="Fusionner les articles en double" />
					</td>
					<td>	
						<std:link actionGroup="C" classStyle="btn btn-danger" action="caisse.caisse.recalculMouvementsStock" value="Reclacul mouvements" icon="fa fa-tasks" tooltip="Recalcul mouvements articles" />
					</td>	
					<td>
			        	<std:link actionGroup="C" classStyle="btn btn-danger" style="color: ##ff5722;" action="stock.article.majStockAll" value="Recalculer quantité stock"  icon="fa fa-tasks" tooltip="Recalculer les quantités articles en stock" />
			        </td>
		        </tr>
		        <tr>
					<td>
						<std:link actionGroup="C" classStyle="btn btn-danger" value="Purger doublons stock" action="stock.article.purgerDoublonStockInfo" icon="fa fa-tasks" tooltip="Purger doublon stock info" />
					</td>
					<td>
					
					</td>
					<td>
					
					</td>
				</tr>	
		        <tr>
		        	<td colspan="3">
						<std:link actionGroup="C" classStyle="btn btn-warning" action="caisse.caisse.recalculHistoriqueAnnulation" value="Reclacul annulation" icon="fa fa-tasks" tooltip="Recalcul annulation articles" />
		        	</td>
		        </tr>
		        
		        <tr>
		        	<td colspan="3">
		        		<a href="javascript:" onclick="$('#div_req').toggle(1000);">Requêtes de purge</a>
		        		<div id="div_req" style="display: none;">
		        			<span style="width: 100%;float: left;line-height: 20px;color: orange;">
		        				! Pensez à faire une sauvegarde base de données avant de lancer ces requêtes. Retour en arrière impossible.
		        			</span>
		        			
		        			<h3>Purger les mouvements caisse</h3>
		        			<span style="width: 100%;float: left;line-height: 20px;">delete from caisse_mouvement_article;</span>
							<span style="width: 100%;float: left;line-height: 20px;">delete from caisse_mouvement_offre;</span>
							<span style="width: 100%;float: left;line-height: 20px;">delete from caisse_mouvement_statut;</span>
							<span style="width: 100%;float: left;line-height: 20px;">delete from caisse_mouvement_trace;</span>
							<span style="width: 100%;float: left;line-height: 20px;">delete from carte_fidelite_conso;</span>
							<span style="width: 100%;float: left;line-height: 20px;">delete from carte_fidelite_points;</span>
							<span style="width: 100%;float: left;line-height: 20px;">delete from client_portefeuille_mvm;</span>
							<span style="width: 100%;float: left;line-height: 20px;">delete from caisse_mouvement;</span>
							<br>
							<h3>Purger les journées et shifts</h3>
							<span style="width: 100%;float: left;line-height: 20px;">delete from cuisine_journee;</span>
							<span style="width: 100%;float: left;line-height: 20px;">delete from article_balance;</span>
							<span style="width: 100%;float: left;line-height: 20px;">delete from caisse_journee;</span>
							<span style="width: 100%;float: left;line-height: 20px;">delete from journee;</span>
							<br>
							<h3>Exporter articles :</h3>
							<span style="width: 100%;float: left;line-height: 20px;">
								Exporter et importer dans l'ordre les tables : <br>
								famille | list_choix | list_choix_detail | 
								menu_composition | menu_composition_detail | 
								marque | article | aticle_detail 
							</span>
		        		</div>
		        	</td>
		        </tr>
	        </table>
		</std:form>
	</div>
	<div class="row" style="margin-top: 30px;">
          		<div class="col-lg-6 col-sm-6 col-xs-12">
          			<div class="widget">
                <div class="widget-header bg-gold">
                    <i class="widget-icon fa fa-check"></i>
                    <span class="widget-caption">Scripts et widgets</span>
                </div><!--Widget Header-->
                <div class="widget-body" style="font-size: 11px;color: #8c8c8c;">
   				<%="&#60;script type=\"text/javascript\" src=\""+StrimUtil.getGlobalConfigPropertie("instance.url")+"/domaine/caisse/mobile/web/widget_cmd_ligne.js?v=1.1\" id=\"caisse-jtn\" url=\""+StrimUtil.getGlobalConfigPropertie("instance.url")+"/mob-client\" jtn=\""+ContextAppli.getEtablissementBean().getToken()+"\"&#62;&#60;&#47;script&#62;" %>
			</div>
		</div>			
          		</div>
          	</div>	
	              	
	<!-- end widget content -->

</div>
<!-- end widget div -->
 