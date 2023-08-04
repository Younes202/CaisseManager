<%@page import="java.util.HashMap"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.model.domaine.vente.persistant.CaissePersistant"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.TYPE_LIGNE_COMMANDE"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementOffrePersistant"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="java.util.Date"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.util.Random"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
boolean IS_ALERT_SONORE = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig(ContextAppli.PARAM_APPLI_ENUM.ALERT_SONOR_CUISINE.toString()));
boolean IS_UPD_CMD_HISTO = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("UPD_CMD_HISTO"));
Integer DELAIS_ALERT_MINUTE = (Integer)ControllerUtil.getUserAttribute("DELAIS_ALERT_MINUTE", request);

String[] colors = {"yellow", "palegreen", "azure", "#03a9f4", "#ffc107", "#fff59d", "gray", "#fb6e52", "#f4b400", "#5db2ff", "#838422", "#6c2d28", "#c4ddbd", "#b6897f"};
boolean isAlertSonore = request.getAttribute("alert_sonore") != null;
Integer NBR_NIVEAU_CAISSE = StringUtil.isNotEmpty(ContextGloabalAppli.getGlobalConfig("NBR_NIVEAU_CAISSE"))?Integer.valueOf(ContextGloabalAppli.getGlobalConfig("NBR_NIVEAU_CAISSE")):null;
boolean isToCollapse = "on".equals(request.getAttribute("valCollapse"));
boolean isToCollapseMnu = "on".equals(request.getAttribute("valCollapseMnu"));

String tpCmd = (String)ControllerUtil.getUserAttribute("tp", request);
STATUT_CAISSE_MOUVEMENT_ENUM statut = null;
if(tpCmd == null || (tpCmd.equals("val") || tpCmd.equals("enc"))) {
	statut = STATUT_CAISSE_MOUVEMENT_ENUM.PREP;
} else if(tpCmd.equals("pre")) {
	statut = STATUT_CAISSE_MOUVEMENT_ENUM.PRETE;
} else {
	statut = STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE;
}
%>

<!-- Pager -->
<%
	List<CaissePersistant> listCaisse = (List<CaissePersistant>)request.getAttribute("listCaisse");
%>
<style>
.class-bg td{
	background-color: #DADADA;
	color :black;
	font-weight: bold;
}
.td-bg{
    background-color: white;
    color :black;
}
</style>
<script type="text/javascript">
$(document).ready(function (){
	var ratioArray = getWindowPilRatioZoom();
	
	<%if(listCaisse.size() <= 2){%>
		var rowH = $(window).height();
		rowH = parseInt(rowH+ (ratioArray[1]==1?0:(rowH/ratioArray[1])))-70;
		
		var blocH = $(window).height();
		blocH = parseInt(blocH+ (ratioArray[1]==1?0:(blocH/ratioArray[1])))-20;	
	<%} else{%>
		var rowH = parseInt($(window).height()/<%=listCaisse.size()/2%>);
		rowH = parseInt(rowH+ (ratioArray[1]==1?0:(rowH/ratioArray[1])))-70;
		
		var blocH = parseInt($(window).height()/2);
		blocH = parseInt(blocH+ (ratioArray[1]==1?0:(blocH/ratioArray[1])))-120;
	<%}%>
	$("#row_pilotage").find(".widget-body").css("height", rowH+"px");
	$(".div-scoll-cmd").css("height", blocH+"px");
});
</script>
		
<%if(request.getAttribute("alert_sonore") != null && IS_ALERT_SONORE){ %>		
	<embed src="resources/framework/sound/rington.wav" height="0px" width="0px" autoplay="true" style="position: absolute;left: -100px;top: -100px;">
<%}	
if(request.getAttribute("is_new_added") != null){ %>
	<embed src="resources/framework/sound/alert.mp3" height="0px" width="0px" autoplay="true" style="position: absolute;left: -100px;top: -100px;">
<%} %>

<std:linkPopup id="zoomLnk" action="caisse-web.cuisine.zoomCommande" style="display:none;" classStyle="" />
<div class="row" id="row_pilotage " style="background-color: #424040;">
<%
	for(int z=0; z<listCaisse.size(); z++){
		CaissePersistant caissePers = listCaisse.get(z); 
	
		String tp = (String)ControllerUtil.getUserAttribute(caissePers.getId()+"_tp", request);
		boolean isStatutConfEcran = !tp.equals("pre") && ((tp.equals("val") && StringUtil.isTrue(""+ControllerUtil.getUserAttribute("isValidationEcran", request)))
								|| (tp.equals("enc") && StringUtil.isTrue(""+ControllerUtil.getUserAttribute("isPreparationEcran", request))));
	
	
		List<CaisseMouvementPersistant> listCaisseMouvement = (List<CaisseMouvementPersistant>)request.getAttribute(caissePers.getId()+"_listCaisseMouvement");
		Integer nbrCols = (Integer)request.getAttribute(caissePers.getId()+"_nbrCols"); 
		int nbrTotal = (Integer)request.getAttribute(caissePers.getId()+"_nbrTotal");
		int page_count = (int) Math.ceil((double) nbrTotal / nbrCols);// Calculate
	
		String currPageSt = (String)ControllerUtil.getUserAttribute(caissePers.getId()+"_sidx", request);
		int currPage = Integer.valueOf(currPageSt==null?"0":currPageSt);
	
		int previousPage = currPage==0 ? 0 : currPage-1;
		int nextPage = (currPage+1)>=page_count ? currPage : currPage+1;
		int currNbrElmnt = (currPage+1)*nbrCols;
		currNbrElmnt = (currNbrElmnt > nbrTotal) ? nbrTotal : currNbrElmnt;
	
		String stlPrev = "margin-left: 70px;"+(currPage==0 ? "background-color:gray !important;":"");
		String stlNext = ((currPage+1)>=page_count ? "background-color:gray !important;":"");
	
		String[] checks = (String[])request.getAttribute(caissePers.getId()+"_checks");
%>
	<div class='<%=listCaisse.size()==1?"col-lg-12 col-sm-12 col-xs-12":"col-lg-6 col-sm-6 col-xs-6" %>' style='border-right: 2px dashed #090301;<%=z%2==0?"border-left: 2px dashed #090301;":""%>;margin-top: -15px;'>
	<div class="widget" style="margin-bottom: 7px;">
		<div class="widget-header bg-lightred" style="background: #c9c3c3 !important;">
		 	 <span class="widget-caption" style="font-weight: bold !important;
		 	 			font-size:18px;
		 	 			margin-left: 10px;
		 	 			text-transform: uppercase;
		 	 			color: black;">
		  		<%=caissePers.getReference()%>
		  	 </span>
		  	
		  	<std:link classStyle="btn btn-yellow shiny" style="color:white;" targetDiv="corp-div" action="caisse-web.cuisinePilotage.loadCommande" params='<%="tp=val&cais="+caissePers.getId()%>' id="cmd-valide" icon="fa-3x fa-cutlery" value="Valid&eacute;es" tooltip="Valid&eacute;es">
		  		<%if(tp.equals("val")){ %>
		  			<i id='curr-btn' style='color: red;' class='fa fa-hand-o-left'></i>
		  		<%} %>
		  		 (<span style="font-weight: bold;color: red;font-size: 14px;"><%=StringUtil.getValueOrEmpty(request.getAttribute(caissePers.getId()+"_NBR_VALIDEE"))%></span> )
		  	</std:link>
	        |
	        <std:link classStyle="btn btn-magenta shiny" style="color:white;" targetDiv="corp-div" action="caisse-web.cuisinePilotage.loadCommande" params='<%="tp=enc&cais="+caissePers.getId()%>' icon="fa-3x fa-history" value="En cours" tooltip="En cours">
	        	<%if(tp.equals("enc")){ %>
		  			<i id='curr-btn' style='color: red;' class='fa fa-hand-o-left'></i>
		  		<%} %>
	        </std:link>
	        |
	        <std:link classStyle="btn btn-success shiny" style="color:white;" targetDiv="corp-div" action="caisse-web.cuisinePilotage.loadCommande" params='<%="tp=pre&cais="+caissePers.getId()%>' icon="fa-3x fa-check-square-o" value="Pr&ecirc;tes" tooltip="Pr&ecirc;tes">
	        	<%if(tp.equals("pre")){ %>
		  		<i id='curr-btn' style='color: red;' class='fa fa-hand-o-left'></i>
		  		<%} %>
	        </std:link>
		  	<%if (page_count >= 1){
		  			String prev = "sidx="+previousPage;
		  			String next = "sidx="+nextPage;
		  	%>
			  <std:link classStyle="btn btn-blue btn-sm icon-only white" targetDiv="corp-div" action="caisse-web.cuisinePilotage.loadCommande" params='<%=prev+"&cais="+caissePers.getId()%>' style='<%=stlPrev%>' icon="fa fa-arrow-circle-left" />
				<span style="color: white;"><%=currNbrElmnt%>/<%=nbrTotal%></span>
			  <std:link classStyle="btn btn-blue btn-sm icon-only white" targetDiv="corp-div" action="caisse-web.cuisinePilotage.loadCommande" params='<%=next+"&cais="+caissePers.getId()%>' style='<%=stlNext%>' icon="fa fa-arrow-circle-right" />
			<%
				}
			%>
		 </div>
		 <div class="widget-body" style="overflow-y: hidden;overflow-x: auto;background-color: #424040;"> 
           <%if(listCaisseMouvement.size() == 0){ %>
			<h2 style="text-align: center;color:white;margin-top: 10%;">Aucune commande disponible.</h2> 
		<% }
 		 Map<Long, List<Long>> mapDetail = (Map<Long, List<Long>>)request.getAttribute(caissePers.getId()+"_mapDetailDest");               
 	     int idx = 0;
 	     Date now = new Date();
 	     for(CaisseMouvementPersistant mvm : listCaisseMouvement){ 
 	    	 boolean isTempsDepasse = false;
 	    	 String clock = "";
 			// Rcenser les menus
 			Map<String, String> mapMnu = new HashMap<>();	
 			for(CaisseMouvementArticlePersistant caisseMvmP : mvm.getList_article()){
 				if(BooleanUtil.isTrue(caisseMvmP.getIs_menu())){
 					mapMnu.put(caisseMvmP.getMenu_idx(), caisseMvmP.getLibelle());
 				}
 			}
 	    	 if(DELAIS_ALERT_MINUTE != null){
 		         Date dateCmd = mvm.getDate_creation();
 		         if(dateCmd == null){
 		             continue;    
 		         }
 		         
 		         int minutes = DateUtil.getDiffMinuts(dateCmd, now);
 		        int secondes = DateUtil.getDiffSeconds(dateCmd, now);
 		
 		         if (minutes > DELAIS_ALERT_MINUTE) {
 		             isTempsDepasse = true;
 		         }
 			    if (secondes < 60) {
 			        clock += (secondes < 10 ? "0" + secondes : "" + secondes)+"s";
 			    } else {
 			        int mints = secondes / 60;
 			        secondes = secondes % 60;
 			        clock += (mints < 10 ? "0" + mints : "" + mints)+"min";
 			        clock += secondes < 10 ? "0" + secondes : "" + secondes+"s";
 			    }
 	   		}
 	        String typeCmd = null;
 	        String iconTP = null;
 	        if(ContextAppli.TYPE_COMMANDE.E.toString().equals(mvm.getType_commande())){
 	            typeCmd = "A emporter";
 	           iconTP = "class=\"fa fa-street-view\"";
 	        } else if(ContextAppli.TYPE_COMMANDE.P.toString().equals(mvm.getType_commande())){
 	             typeCmd = "Sur place";
 	            iconTP = " class=\"fa fa-dropbox\"";
 	        } else{
 	            typeCmd = "Livraison";
 	           iconTP = "class=\"fa fa-motorcycle\"";
 	        }
 	        
 			List<Long> listConfigCuisine = mapDetail.get(mvm.getId());
 	        String parMvm = "cai="+caissePers.getId()+"&tp=C&mvm="+EncryptionUtil.encrypt(mvm.getId().toString());
 	        
 	        boolean isNotCurrStatut = tp.equals("val") && !mvm.getLast_statut().equals(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString())
 				|| tp.equals("enc") && !mvm.getLast_statut().equals(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PREP.toString())
 				|| tp.equals("pre") && !mvm.getLast_statut().equals(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PRETE.toString());
 		%> 
            <div class="plan" style="width: <%=Integer.valueOf(100/nbrCols)-2%>%;float: left;margin-right: 10px;margin-top: -10px;background-color: white;border-radius: 17px;">
                <table style="width: 100%;background-color: #a7102c;border-bottom: 0px;border:none; border-top-right-radius: 10px;border-top-left-radius: 10px;border-collapse: separate; " class="entete-cmd">
                	 <tr style="border-bottom: 0px dashed white;height: 30px;">
                	 	<td style="width: 15px;margin-left: 6px;">
 	                		<%if(!isNotCurrStatut && isStatutConfEcran){ %>
	                			<input type="checkbox" name="<%=caissePers.getId()%>_mvm-check" value="<%=mvm.getId()%>" <%=StringUtil.contains(mvm.getId().toString(), checks)?" checked='checked'":""%> style="height: 24px;width: 24px;">
 		                	<%} %> 
                		</td>
                		<td align="right" style="padding-right: 2px;">
                 			<%if(IS_UPD_CMD_HISTO){ %>
	                			<std:linkPopup classStyle="btn btn-xs btn-default shiny" action="caisse.journee.edit_mouvement" workId="<%=mvm.getId().toString()%>">
									<span class="fa fa-eye"></span>
								</std:linkPopup>
								| 
 							<%} %>
                			<%if(!isNotCurrStatut && isStatutConfEcran){ %>
                				<std:link classStyle="btn btn-palegreen shiny btn-xs" style="color:black;" targetDiv="corp-div" params='<%="mnu="+mvm.getId()+"&cais="+caissePers.getId()%>' action="caisse-web.cuisinePilotage.changerStatut" icon="fa-3x fa-check" value="Statut" tooltip="Changer le statut de la commande" />
                			<% if(mvm.getLast_statut().equals(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString())){ %>
                				<std:linkPopup classStyle="btn btn-success shiny btn-xs" params='<%="mnu="+mvm.getId()+"&cais="+caissePers.getId()%>' value="Tranf&eacute;rer" action="caisse-web.cuisinePilotage.initTransfererCmdCuisine" icon="fa-3x fa-mail-reply-all" tooltip="Transf&eacute;rer la cammande"/>
                			<%}
                			}%>
                			<a href="javascript:" class="btn btn-yellow shiny btn-xs zoomDiv" params='<%=parMvm%>' style="width: 30px;color:black;"><i class="fa fa-search-plus"></i></a>
                		</td>
                	</tr>
                	
                	<%String refTables = mvm.getRefTablesDetail();%>
                	<tr style="border-radius: 4px;cursor:pointer;border-bottom: 0px dashed blue;">
                		<td colspan="2" style="text-align: center;">
                			<div style="width: calc(100% - 200px);float: left;border-bottom: 0px dashed white;height: 26px;line-height: 24px;">
	                			<%if(mvm.getOpc_serveur() != null){ %>
		                			<span style="color: white;font-weight: bold;font-size: 13px;"> <i class="fa fa-user"></i> [<%=mvm.getOpc_serveur().getLogin().toUpperCase()%>]</span>
		                		<%} %>
	                		</div>
	                		<div style="width: 80px;white-space: nowrap;float: left;border-bottom: 0px dashed white;height: 26px;line-height: 24px;">	
                				<%if(isTempsDepasse){ %>
	                				<i class="fa fa-warning" style="font-size: 18px;color: red;"></i>
	                			<%} %>
	                			<span style="font-size: 18px;font-weight: bold;color: white;
	                					position: absolute;margin-top: -31px;margin-left: -101px;"><%=(mvm.getRef_commande().length()>12?mvm.getRef_commande().substring(12):mvm.getRef_commande())%></span><br>
                			</div>
                			
                			<div style="padding: 0px 10px 0px 7px;
								    float: left;
								    height: 20px;
								    line-height: 18px;
								    background-color: #ba8c15;
								    border-radius: 10px;
								    width:120px;">
                				| <span style="color: white;font-weight: bold;font-size: 12px;"><%=typeCmd.toUpperCase()%></span>
		                		<i style="color: black;" <%=iconTP %>></i>
		                	</div>
		                	<div style="width: 50%;float: left;border-bottom: 0px dashed white;height: 26px;line-height: 24px;">	
                				<span style="font-size: 20px;color: orange;margin-left: 5px;"><%=clock%></span>
                			</div>	
	                		<%if(StringUtil.isNotEmpty(mvm.getNum_token_cmd())){ %>
	                		<div style="width: 25%;float: left;border-bottom: 0px dashed white;height: 26px;line-height: 24px;">
	                			<span style="color: #3540fd;font-weight: bold;font-size: 12px;">
				                	<%=StringUtil.isNotEmpty(mvm.getNum_token_cmd()) ?"<i class='fa fa-tag'></i> "+mvm.getNum_token_cmd() : ""%><i class='fa fa-tag'></i>
				                </span>	
				            </div>
				            <%} %>
				            <%if(StringUtil.isNotEmpty(refTables)){%>
				            <div style="width: 25%;float: left;border-bottom: 0px dashed white;height: 26px;line-height: 24px;">
			             		<span style="color: #3540fd;font-weight: bold;font-size: 14px;text-transform: uppercase;">  
			                		<%=StringUtil.isNotEmpty(refTables) ? "<i class='fa fa-cutlery'></i> "+refTables : ""%><i class='fa fa-cutlery'></i>
			                	</span>	
	                		</div>
	                		<%} %>
                		</td>
                	 </tr>
                </table>
            <div style="overflow: auto;padding-bottom: 10px;w" class="div-scoll-cmd">
                <table style="width: 100%;border-bottom: 0px;color: black;text-align: left;border-collapse: separate;" id="cmd-table" >
					<%
								List<Integer> listIdxClient = new ArrayList<>();
								for(CaisseMouvementArticlePersistant caisseMvmP : mvm.getList_article()){
									if(BooleanUtil.isTrue(caisseMvmP.getIs_annule())){
										continue;
									}
									if(!listIdxClient.contains(caisseMvmP.getIdx_client()) && caisseMvmP.getIdx_client() != null){
										listIdxClient.add(caisseMvmP.getIdx_client()); 
									}
								}
								
								Integer idxArticle = 0;
								int nbrNiveau = 0;
								int nbrClient = (mvm.getMax_idx_client() == null ? 1 : mvm.getMax_idx_client());
							    //
								for(int i=1; i<=nbrClient; i++){// Les clients
									boolean isFamillePassed = false;  
									
									if(!listIdxClient.contains(i)){
										continue;
									}
									
									if(nbrClient > 1){ %>
							    	   <tr style="color: black;background-color: #b9b9b9;" class="client-root-style">
								       		<td colspan="2" align="center">CLIENT <%=i%></td>
								       </tr>
				      				<% }
				      					String mnuIdxStatut = null;
				      					CaisseMouvementArticlePersistant lastDetailAvecStatut = null;
				      					//
				      					for(CaisseMouvementArticlePersistant caisseMvmP : mvm.getList_article()){
				      				       if(caisseMvmP.getIdx_client()!=null && caisseMvmP.getIdx_client()!=i){
				      				           continue;
				      				       }
				      				       
				      					   boolean isPrete = ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PRETE.toString().equals(caisseMvmP.getLast_statut());
				      				       
				      					   if(BooleanUtil.isTrue(caisseMvmP.getIs_menu())){
				      			    	   		mnuIdxStatut = caisseMvmP.getMenu_idx();
				      			    	   		lastDetailAvecStatut = caisseMvmP;
				      			    	   } else if(ContextAppli.TYPE_LIGNE_COMMANDE.ART.toString().equals(caisseMvmP.getType_ligne())
				      			    			   || ContextAppli.TYPE_LIGNE_COMMANDE.ART_MENU.toString().equals(caisseMvmP.getType_ligne())){
				      			    		   lastDetailAvecStatut = caisseMvmP;
				      			    	   }
				      					   
				      					   //---------------------------------------
				      					 	int tempsRef = 1000;
				      					 	String tooltip = "";
				      					 	if(caisseMvmP.getDate_debut_stat() != null
				      					 			&& caisseMvmP.getDate_fin_stat() != null
				      					 			&& caisseMvmP.getOpc_article() != null 
				      					 			&& caisseMvmP.getOpc_article().getTemps_cuis_ref() != null){
				      					 		
				      					 		int tempsPrep = DateUtil.getDiffMinuts(caisseMvmP.getDate_debut_stat(), caisseMvmP.getDate_fin_stat());
	 											tempsRef =  caisseMvmP.getOpc_article().getTemps_cuis_ref();
	 											
	 											if(tempsPrep > tempsRef){
	 												tooltip = "<i class='fa fa-warning' style='color:red;font-size: 17px;'>"+(tempsRef-tempsPrep)+"</i>";
	 											}
	 										}
				      					   
				      					   int tempsPrep = -1;
				      					   if(caisseMvmP.getDate_debut_stat() != null && caisseMvmP.getDate_fin_stat() != null){
				      					   		tempsPrep = DateUtil.getDiffMinuts(caisseMvmP.getDate_debut_stat(), caisseMvmP.getDate_fin_stat());
				      					   }
				      					   //----------------------------------------
				      					   
				      					   String styleConfig = "";
				      				       boolean isInCaisseConf = false;
				      				       // V�rifier si l'�l�ment fait partie de la conf
				      				       if(lastDetailAvecStatut != null && listConfigCuisine.contains(lastDetailAvecStatut.getId())){
				      				    		isInCaisseConf = true;
				      				       }
				      				       // V�rifier les statuts
				      				      if(isInCaisseConf	
				      				    		  && !BooleanUtil.isTrue(caisseMvmP.getIs_annule()) 
				      				    		  && !BooleanUtil.isTrue(caisseMvmP.getIs_suite_lock())
				      				    		  && (
				      			    		   		tp.equals("val") && (lastDetailAvecStatut.getLast_statut()==null || ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString().equals(lastDetailAvecStatut.getLast_statut()))
				      			    		   		|| tp.equals("enc") && ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PREP.toString().equals(lastDetailAvecStatut.getLast_statut())
				      			    		   		|| tp.equals("pre") && ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PRETE.toString().equals(lastDetailAvecStatut.getLast_statut())
				      			    		   	  )
				      				    	){
				      				    	  // continue;
				      				    	  styleConfig = "X";
				      				       }
				      				       
				      				      if(!"X".equals(styleConfig)){
				      				    	   if(isInCaisseConf){
				      				    		   styleConfig = "font-size:12px;font-weight: normal;color:#66d75d !important;"; 
				      				    	   } else{
				      				    	   	   styleConfig = "font-size:12px;font-weight: normal;color:#c3c3c3 !important;";
				      				    	   }
				      				       } else{
				      				    	   styleConfig = "";
				      				       }
				      						String styleAnnul = "";
				      				       if(BooleanUtil.isTrue(caisseMvmP.getIs_annule())){
				      				    		styleAnnul = "color:#ffbebe !important;font-style: italic !important;;text-decoration: line-through !important;;";
				      				       }
				      				       /*boolean isValideStatut = (
				      				    		   		StringUtil.isEmpty(caisseMvmP.getLast_statut()) 
				      				    		   		&& ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString().equals(mvm.getLast_statut())
				      				    		   	)
				      				    		   	|| ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString().equals(caisseMvmP.getLast_statut());*/

				      				       boolean isArticle = false;
				      				       
				      				       // Ajout du num&eacute;ro dans le tableau
				      				       String type = caisseMvmP.getType_ligne();
				      				       String libCmd = caisseMvmP.getLibelle();
				      				       if(type == null){
				      				           type = "XXX";
				      				       }
				      				       
				      				       if(type.equals(TYPE_LIGNE_COMMANDE.MENU.toString()) && (caisseMvmP.getLevel() == null || caisseMvmP.getLevel() > 1)) {
				      				           idxArticle++;
				      				           libCmd = idxArticle + "-" + libCmd;
				      				       } else if(type.equals(TYPE_LIGNE_COMMANDE.ART.toString()) || type.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString())){
				      				           idxArticle++;
				      				           libCmd = idxArticle + "-" + libCmd;
				      				       }
				      				       
				      				  		// Retard CMD
				      				     	libCmd = libCmd + tooltip;
				      				     	if(BooleanUtil.isTrue(caisseMvmP.getIs_suite_lock())){
				      				    		libCmd = libCmd + " <i class='fa fa-clock-o' style='color:#4caf50;'></i>";
				      				     	}
				      				     	
				      				       if(StringUtil.isNotEmpty(caisseMvmP.getCommentaire())){
				      				           libCmd = libCmd + " <i class='fa fa-comments-o' style='color:orange;font-size: 17px;cursor:pointer;' data-container='body' data-titleclass='bordered-blue' data-class='' data-toggle='popover' data-placement='top' data-title='Commentaire' data-content='"+caisseMvmP.getCommentaire()+"' data-original-title='' title=''></i>";
				      				       }
				      				       
				      				       Integer qte = (caisseMvmP.getQuantite() != null 
				      				    		   					? caisseMvmP.getQuantite().intValue() : null);
				      	// 					       String mttTotal = "";
				      	// 					       if( BooleanUtil.isTrue(caisseMvmP.getIs_offert())){
				      	// 					    	   mttTotal = "<i class='fa fa-gift'style='color:green;font-size: 17px;cursor:pointer;' data-container='body' data-titleclass='bordered-blue' data-class='' data-toggle='popover' data-placement='top' data-title='Article offert' data-content='Prix : "+BigDecimalUtil.formatNumber(caisseMvmP.getMtt_total())+"' data-original-title='' title=''></i>";
				      	// 					       } else if(!BigDecimalUtil.isZero(caisseMvmP.getMtt_total())){
				      	// 					    	   mttTotal = BigDecimalUtil.formatNumber(caisseMvmP.getMtt_total());
				      	// 					       }
				      				       
				      				       String styleTd = "";
				      				       String classType = "";
				      				       String iconStl = "";
				      				      if(caisseMvmP.getMenu_idx() != null || type.equals(TYPE_LIGNE_COMMANDE.MENU.toString())){
				      				    	  if(type.equals(TYPE_LIGNE_COMMANDE.MENU.toString())) {
				      				    		  if(caisseMvmP.getLevel() != null && caisseMvmP.getLevel() == 1){
				      				    			  classType = "menu-cat-style class-bg";
				      				    		  } else{
				      				        	   	classType = "menu-style class-bg";
				      				    		  }
  				      				           } else if(type.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString())) {
				      				        	   isArticle = true;
				      				        	   classType = "ligne-style td-bg";
				      				        	   nbrNiveau = 0;
				      				           } else if(type.equals(TYPE_LIGNE_COMMANDE.GROUPE_MENU.toString())){
				      				        	   classType = "group-style td-bg";
				      				        	   nbrNiveau++;
				      				           } else if(type.equals(TYPE_LIGNE_COMMANDE.GROUPE_FAMILLE.toString())){
				      				        	   classType = "group-style class-bg";
				      				        	   nbrNiveau++;
					      				    		iconStl = "<i id='curr-btn' style='color: red;' class='fa fa-fire'></i>";
				      				           }
				      				       } else{
				      				    	   if(type.equals(TYPE_LIGNE_COMMANDE.ART.toString())){
				      				    		   classType = "ligne-fam-style td-bg";
				      				    		   isArticle = true;
				      				    		   nbrNiveau = 0;
				      				           } else if(type.equals(TYPE_LIGNE_COMMANDE.GROUPE_FAMILLE.toString())){
				      				        	   // Ajouter une ligne e s&eacute;paration
				      				        	   if(!isFamillePassed){
				      	%>
					        	       <tr style="height:3px;color: black;background-color:#d7ccc8;" class="menu-root-style">
					        	       		<td colspan="2" align="center"></td>
					        	       </tr>
					        	       <%
					        	       	isFamillePassed = true;
      	       			        	   }
      	       			        	   classType = "famille-style class-bg";
      	       			        	   nbrNiveau++;
	      				    			iconStl = "<i id='curr-btn' style='color: red;' class='fa fa-fire'></i>";
      	       			           } 
      	       			       }
					        	       			       
       	       			       String params = "cd="+caisseMvmP.getCode()+"&elm="+caisseMvmP.getElementId()+"&par="+StringUtil.getValueOrEmpty(caisseMvmP.getParent_code())+"&mnu="+caisseMvmP.getMenu_idx()+"&tp="+caisseMvmP.getType_ligne();
       	       			       if(caisseMvmP.getLevel() != null){
       	       			    	   if(caisseMvmP.getLevel() != null && caisseMvmP.getLevel() > 1){
       	       				    	   styleTd = styleTd + "padding-left:"+(7*caisseMvmP.getLevel())+"px;";
       	       				       } else if(isArticle){
       	       				    	   styleTd = styleTd + "padding-left:40px;";
       	       				       }
       	       			       }
       	       			      
       	       			       boolean isToAdd = (isArticle || NBR_NIVEAU_CAISSE == null || nbrNiveau <= NBR_NIVEAU_CAISSE) && !caisseMvmP.getLibelle().startsWith("#") && !type.equals(TYPE_LIGNE_COMMANDE.LIVRAISON.toString());
       	       			       //
       	       			       if (isToAdd && (!isArticle || (isArticle && "C".equals(caisseMvmP.getOpc_article().getDestination())))) {
       	       			    	   boolean isNotArt = (!type.equals(TYPE_LIGNE_COMMANDE.ART.toString()) && !type.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString()));
       	       			    	   boolean toCollapse = StringUtil.isNotEmpty(styleConfig);
       	       			    	   
       	       			    	   boolean isMnuCollapse = (caisseMvmP.getMenu_idx()!= null && !type.equals(TYPE_LIGNE_COMMANDE.MENU.toString()) && isToCollapseMnu)/*Uniquement si cookies*/;
         					        if(BooleanUtil.isTrue(caisseMvmP.getIs_menu())){ %>
					         	<tr class="<%=classType%>" style="<%=styleConfig+styleAnnul%>;cursor:pointer;height:30px;background-color:#e9eaea;<%=toCollapse && isToCollapse?"display:none;":""%>" par="<%=params%>">
						       		<td nowrap="nowrap" colspan="2" style="<%=styleTd%>;vertical-align: middle;">
											<%if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PRETE.toString().equals(caisseMvmP.getLast_statut())){ %>
							       				<i class="fa fa-check-square-o" style="color: green;font-size: 17px;"></i>
											<% } else if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PREP.toString().equals(caisseMvmP.getLast_statut())){ %>
							       				<img style="margin-top:-5px;" src="resources/caisse/img/caisse-web/inprogress.gif" width="24" />
							       			<% } else if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE.toString().equals(caisseMvmP.getLast_statut())){ %>
							       				<i class="fa fa-user" title="Livr&eacute;e" style="color: blue;font-size: 17px;"></i>
							       			<% } else if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString().equals(caisseMvmP.getLast_statut())){
							       					if(!type.equals(ContextAppli.TYPE_LIGNE_COMMANDE.ART_MENU.toString()) 
									       							&& !type.equals(ContextAppli.TYPE_LIGNE_COMMANDE.ART.toString())){%>
							       				<i class="fa fa-bars" title="Valid&eacute;e" style="color: green;font-size: 17px;"></i>
							       			<% }
							       			} %>
											
											<span mnu="<%=caisseMvmP.getMenu_idx()+"_"+caissePers.getId()%>" class="lnk_mnu">	
											<%=(!isNotArt ? "<span class='fa fa-circle' style='color:#9dc2ff;'></span>&nbsp;":"&nbsp;")+iconStl+libCmd%>
											<i class="icon_exp fa fa-plus-square-o"></i>
											</span>
										<div style="margin-top: 4px;float: right;">
											<% if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PREP.toString().equals(caisseMvmP.getLast_statut())){ 
												if("".equals(styleConfig)){ %>
													<std:link action="caisse-web.cuisinePilotage.changerStatut" targetDiv="corp-div" params='<%="cmd="+mvm.getId()+"&mnu="+caisseMvmP.getId()+"&cais="+caissePers.getId()%>' classStyle="btn btn-palegreen shiny btn-xs" icon="fa-3x fa-check" value="" tooltip="Changer statut" />
												<% }  
												} else if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString().equals(caisseMvmP.getLast_statut())){ 
													if("".equals(styleConfig)){ %>
													<std:link action="caisse-web.cuisinePilotage.changerStatut" targetDiv="corp-div" params='<%="cmd="+mvm.getId()+"&mnu="+caisseMvmP.getId()+"&cais="+caissePers.getId()%>' classStyle="btn btn-palegreen shiny btn-xs" icon="fa-3x fa-check" value="" tooltip="Changer statut" />
													<std:linkPopup classStyle="btn btn-success shiny btn-xs" params='<%="mnu="+mvm.getId()+"&det="+caisseMvmP.getId()+"&cais="+caissePers.getId()%>' value="" action="caisse-web.cuisinePilotage.initTransfererCmdCuisine" icon="fa-3x fa-mail-reply-all" tooltip="Transf&eacute;rer la commande"/>
												<%} 
											} else{ %>
							       				<std:link action="" targetDiv="XXX" classStyle="btn btn-palegreen btn-xs" icon="fa fa-fire" style="visibility:hidden;position:absolute;top:-100px;" />
							       			<%} %>

											<std:link action="caisse-web.cuisine.print_etiquette_menu" targetDiv="div_gen_printer" params='<%="cmd="+mvm.getId()+"&mnu="+caisseMvmP.getId()%>' classStyle="btn btn-info shiny btn-xs" icon="fa-3x fa-print" value="" tooltip="Imprimer &eacute;tiquette" />							       			

						       			</div>
									</td>
						       </tr>
					        <%
					        	} else{
					        %>
					         	<tr id="<%=isMnuCollapse ? "tr_"+(caisseMvmP.getMenu_idx()+"_"+caissePers.getId()):""%>" class="<%=classType%>" style="<%=isMnuCollapse?"display:none;":""%><%=styleConfig%>;<%=toCollapse && isToCollapse?"display:none;":""%><%=caisseMvmP.getMenu_idx() == null && isInCaisseConf && isStatutConfEcran?"height:25px;":""%>" par="<%=params%>">
						       		<td <%=isNotArt?" colspan='2' ":""%> style="<%=styleTd%>;vertical-align: middle;">
						       			<% if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PRETE.toString().equals(caisseMvmP.getLast_statut())){ %>
						       				<i class="fa fa-check-square-o" style="color: green;font-size: 17px;"></i>
										<% } else if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PREP.toString().equals(caisseMvmP.getLast_statut())){ %>
						       				<img style="margin-top:-5px;" src="resources/caisse/img/caisse-web/inprogress.gif" width="24" />
						       			<% } else if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE.toString().equals(caisseMvmP.getLast_statut())){ %>
						       				<i class="fa fa-user" title="Livr&eacute;e" style="color: blue;font-size: 17px;"></i>
						       			<% } else if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString().equals(caisseMvmP.getLast_statut())){ %>
						       				<i class="fa fa-bars" title="Valid&eacute;e" style="color: green;font-size: 17px;"></i>
						       			<% } %>
						       			
						       			<%if(caisseMvmP.getMenu_idx() != null && isArticle && isToCollapse){ %>
							       				<span style="margin-left: -20px;color: #bc5679;"><i style='color: green;font-size: 17px;' class='fa fa-bars'></i>
							       				<%=mapMnu.get(caisseMvmP.getMenu_idx()) %></span><br>
							       			<%} %>
							       			
						       			<%=(!isNotArt ? "<span class='fa fa-circle' style='color:#9dc2ff;'></span>&nbsp;":"&nbsp;")+iconStl+libCmd%>
						       		<% if(!isNotArt){ %>
						       		</td>
						       		<td align="right" nowrap="nowrap" style="padding-right: 5px;">
						       		<% } %>
						       			<div style="vertical-align: middle;">
						       			<%if(!isNotArt){ %>
						       			<%if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PREP.toString().equals(caisseMvmP.getLast_statut())){ %>
						       				<%if("".equals(styleConfig)){ %>
							       				<std:link action="caisse-web.cuisinePilotage.changerStatut" targetDiv="corp-div" params='<%="cmd="+mvm.getId()+"&mnu="+caisseMvmP.getId()+"&cais="+caissePers.getId()%>' classStyle="btn btn-palegreen shiny btn-xs" icon="fa-3x fa-check" value="" tooltip="Changer statut" />
						       				<%} %>
						       			<%} else if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString().equals(caisseMvmP.getLast_statut())){ %>
						       				<%if("".equals(styleConfig)){ %>
							       				<std:link action="caisse-web.cuisinePilotage.changerStatut" targetDiv="corp-div" params='<%="cmd="+mvm.getId()+"&mnu="+caisseMvmP.getId()+"&cais="+caissePers.getId() %>' classStyle="btn btn-palegreen shiny btn-xs" icon="fa-3x fa-check" value="" tooltip="Changer statut" />
						       					<%if(caisseMvmP.getMenu_idx() == null){ %>
						       						<std:linkPopup classStyle="btn btn-success shiny btn-xs" params='<%="mnu="+mvm.getId()+"&det="+caisseMvmP.getId()+"&cais="+caissePers.getId() %>' value="" action="caisse-web.cuisinePilotage.initTransfererCmdCuisine" icon="fa-3x fa-mail-reply-all" tooltip="Transf&eacute;rer la commande"/>
						       					<%} %>
						       				<%} %>
						       			<%} else{ %>
							       			<std:link action="" targetDiv="XXX" classStyle="btn btn-palegreen btn-xs" icon="fa fa-fire" style="visibility:hidden;position:absolute;top:-100px;" />
							       		<%}%>
						       			
						       			<%if(caisseMvmP.getMenu_idx() == null){ %>
						       				<std:link action="caisse-web.cuisine.print_etiquette_menu" targetDiv="div_gen_printer" params='<%="cmd="+mvm.getId()+"&mnu="+caisseMvmP.getId()%>' classStyle="btn btn-info shiny btn-xs" icon="fa-3x fa-print" value="" tooltip="Imprimer &eacute;tiquette" />
						       			<%} %>
						       			
						       				<%
						       				int oldQte = caisseMvmP.getNbrByStatut(statut);
						       				String qteStr = (oldQte!=qte && oldQte>0) ? oldQte+"("+qte+")" : ""+qte;
						       				%>
                                        <span class="badge badge-darkorange " style="font-weight: bold;padding-right: 3px;font-weight: bold;
									    padding: 1px 8px 6px 6px;
									    font-size: 19px !important;
									    height: 24px;"><%=qteStr %></span>
						       			<%}%>
						       			</div>
						       		</td>
						       </tr>
					        <%} %>
					            
						       <%if(StringUtil.isNotEmpty(caisseMvmP.getCommentaire())){ %>
						       		<tr style="background-color: yellow;">
						       			<td colspan="2" style="font-size: 12px;"><%=caisseMvmP.getCommentaire() %></td>
						       		</tr>
						       <%} %>
					       <%
					       }
					   }
					}
					%>     
					</table>
				</div>	   
        </div>
  <%
  	idx++;
	  if(idx > colors.length){
		  idx = 0;
	  }
     } %>      
    </div>
    </div>
    </div>
<%} %>
</div>