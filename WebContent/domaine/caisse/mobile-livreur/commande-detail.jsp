<%@page import="appli.model.domaine.util_srv.raz.RazService"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementOffrePersistant"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.TYPE_LIGNE_COMMANDE"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="java.util.List"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="appli.controller.domaine.caisse.ContextAppliCaisse"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="framework.controller.Context"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@page errorPage="/commun/error.jsp" %>

<%
	CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant)request.getAttribute("caisseMouvement");
%>

<style>
	.modal-backdrop{
		display: none;
	}
	#map {
		width: 100%;
		height: 250px;
	}
	.gmnoprint {
		display: none;
	}
	.bloc-style{
		border: 1px solid #cccccc;
		border-radius: 7px;
		padding: 4px;
		height: 92px;
		width: 100%;
		margin-bottom: 6px;
		float: left;
		background-color: #f5f5f5;
	}
</style>

<script type="text/javascript">

$(document).ready(function (){
	var tel = '<%=CURRENT_COMMANDE.getOpc_client() != null ? CURRENT_COMMANDE.getOpc_client().getTelephone() : ""%>'
	var data = '<a href="whatsapp://send?phone='+tel+'" style="color: #262626;font-weight: bold; margin: 0px 5px;" target="_blank" class="btn btn-default btn-circle btn-lg btn-menu shiny">'
				+'<i class="fa fa-whatsapp" style="line-height: 24px; color: #53a93f; font-size: 40px; margin-left: -6px;"></i>'
				+'</a>'
				+'<a href="tel:'+tel+'" style="color: #262626;font-weight: bold; margin: 0px 5px;" class="btn btn-default btn-circle btn-lg btn-menu shiny">'
				+'<i class="fa fa-phone" style="line-height: 24px; font-size: 37px; color: #4a96da; margin-left: -2px;"></i>'
				+'</a>'
				+'<a href="sms:'+tel+'" style="color: #262626;font-weight: bold; margin: 0px 5px;" class="btn btn-default btn-circle btn-lg btn-menu shiny">'
				+'<i class="fa fa-envelope-o" style="line-height: 22px; font-size: 30px; margin-left: -3px;"></i>'
				+'</a>';
});

function navigate(lat,lng){
	//Android.navigate('1,00025','0,36987');
}

function openApp(tp,lat,lng) {
    if(navigator.userAgent.toLowerCase().indexOf("android") > -1) {
    	var destination = lat + ',' + lng;
        if(tp=="GM"){
        	window.open("https://www.google.com/maps/search/?api=1&query="+destination)
        	} else if(tp=="WZ"){
        	window.open("https://waze.com/ul?ll="+destination+"&navigate=yes&z=10");
        } 
    } else{
        if(tp=="CL"){
			window.location='https://play.google.com/store/apps/details?id=com.driverconnect1';
        } else if(tp=="WZ"){
        	window.location='https://play.google.com/store/apps/details?id=com.waze';			        
        }
    }
}

function initMap() {
	var map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 0, lng: 0},
        zoom: 15
    });
	
	var directionsService = new google.maps.DirectionsService;
    var directionsRenderer = new google.maps.DirectionsRenderer;
    
    directionsRenderer.setMap(map);
		
	if(navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
			 var pos = {
				        	lat: position.coords.latitude,
				            lng: position.coords.longitude
				        };
			 
			 //map.setCenter(pos);
			 var pos_dest = {
				lat: position.coords.latitude,
			    lng: position.coords.longitude
			 };
			 
			 <%if(CURRENT_COMMANDE.getOpc_client() != null && CURRENT_COMMANDE.getOpc_client().getPosition_lat() != null){%>
			 	pos_dest = {
			        	lat: <%=CURRENT_COMMANDE.getOpc_client().getPosition_lat()%>,
			            lng: <%=CURRENT_COMMANDE.getOpc_client().getPosition_lng()%>
			        };
			 <%}%>
			 
			 directionsService.route({
		        origin: pos,
		        destination: pos_dest,
		        travelMode: 'DRIVING'
		      }, function(response, status) {
		        if (status === 'OK') {
		          directionsRenderer.setDirections(response);
		        } else {
		          window.alert('Directions request failed due to ' + status);
		        }
		      });
		})
	}
}
</script>

<div class="row" style="overflow-y: scroll;height: calc(100vh - 18px);">
	<div class="col-lg-6 col-sm-6 col-xs-12">
        <div class="widget flat radius-bordered">
            <div class="widget-header bg-blue" style="display: flex">
            	<span class="widget-caption" style="font-size: 17px;">Détail de la commande</span>
                <button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal" style="position: absolute;right: 5px;top: 5px;">
					<i class="fa fa-times"></i> Fermer
				</button>
            </div>

            <div class="widget-body">
            	<div id="map" style="margin-bottom: 10px;border: 1px solid gray;"></div>
            	
            	<div style="margin-bottom: 6px;text-align: center;float: left;width: 100%;">
            		<a style="color: #9c27b0;" href="comgooglemaps://?center=40.765819,-73.975866&zoom=14">Google maps</a> | 
            		<a style="color: #9c27b0;" href="http://maps.apple.com/?sll=38.897517,-77.036542">Apple maps</a>
            	</div>
            	
                <%if(CURRENT_COMMANDE.getOpc_client() != null){ %>
	                <div class="bloc-style">
	                	<div class="form-group" style="margin-bottom: 0px;font-weight: 700;color: #4590d4;">
							INFORMATIONS CLIENT
						</div>	
						<hr style="margin-top: 5px;margin-bottom: 0px;">
						<div class="form-group" style="margin-bottom: 0px;">
							<div class="col-xs-3" style="margin-left: -15px;">
							<i style="color:#b37c8f; " class="fa fa-user"></i>
								 &nbsp;${caisseMouvement.opc_client.nom } ${caisseMouvement.opc_client.prenom }
							</div>
							<div class="col-md-3">
							<i style="color:#b37c8f;" class="fa fa-phone-square"></i>
							<a href="tel:${caisseMouvement.opc_client.telephone }" >
							&nbsp;${caisseMouvement.opc_client.telephone }
							</a>
							<i style="color:#b37c8f;margin-left: 7px;" class="fa fa-whatsapp"></i>
							<a href="https://wa.me/212${caisseMouvement.opc_client.telephone }?text=Send20%a20%quote" >
							&nbsp;${caisseMouvement.opc_client.telephone }
							</a>
								 
							</div>
						</div>
						<div class="form-group" style="margin-bottom: 0px;">
							<div class="col">
							<i style="color:#b37c8f;" class="fa fa-map-marker"></i>
							

							<a href="https://www.google.com/maps/dir/${caisseMouvement.opc_livreurU.getPosition_lat()},
							${caisseMouvement.opc_livreurU.getPosition_lng() }/${caisseMouvement.opc_client.getPosition_lat() },
							${caisseMouvement.opc_client.getPosition_lng() }" >
								 &nbsp;${caisseMouvement.opc_client.adresse_rue } - ${caisseMouvement.opc_client.adresse_compl }
								[<b>${distance }</b>Km] [<b>${duration }</b>min]</a>
							</div>
								
							<a style="padding-left: 10%;font-weight: bold;color: #953434;" href="geo:GM${caisseMouvement.opc_client.getPosition_lat()}|${caisseMouvement.opc_client.getPosition_lng()}" >
								 <i class="fa fa-times"></i> GOOGLE MAPS
							|</a>	
							<a  style="padding-left: 10%;font-weight: bold;color: #953434;" href="geo:WZ${caisseMouvement.opc_client.getPosition_lat()}|${caisseMouvement.opc_client.getPosition_lng()}" onclick="openApp('GM','${caisseMouvement.opc_client.getPosition_lat()}','${caisseMouvement.opc_client.getPosition_lng()}')">
								 <i class="fa fa-times"></i> WAZE
							</a>	 

								<%-- <button href="geo:WZ${caisseMouvement.opc_client.getPosition_lat()}|${caisseMouvement.opc_client.getPosition_lng()}" type="button" style="right: 5px;top: 5px;">
													<i class="fa fa-times"></i> WAZE
												</button>
								<button href="geo:GM${caisseMouvement.opc_client.getPosition_lat()}|${caisseMouvement.opc_client.getPosition_lng()}" type="button" style="right: 5px;top: 5px;">
													<i class="fa fa-times"></i> Google Maps
												</button> --%>
								
							</div>
						</div>
	            <%} %>
                <div class="bloc-style">
						<div class="form-group" style="margin-bottom: 0px;font-weight: 700;color: #4590d4;">
							RÉSUMÉ
						</div>	
						<hr style="margin-top: 5px;margin-bottom: 0px;">
					<div class="form-group" style="    
					    height: 100px;">
							<std:label classStyle="control-label col-xs-3" style="padding: 0px;" value="Paiement" />
							<div class="col-xs-3" style="padding: 0px;">
								<b>${caisseMouvement.mode_paiement }</b>
							</div>
							<std:label classStyle="control-label col-xs-3" style="padding: 0px;" value="Montant" />
							<div class="col-md-3" style="padding: 0px;font-weight: bold;font-size:13px;color: #630767;">
								<fmt:formatDecimal value="${caisseMouvement.mtt_commande_net }"/>Dhs
							</div>
					</div>
				</div>	
				
				<div class="bloc-style" style="height: auto;">	
					
						<div class="form-group" style="margin-bottom: 0px;font-weight: 700;color: #4590d4;">
							DETAIL COMMANDE
						</div>	
						<hr style="margin-top: 5px;margin-bottom: 4px;">
				
				<%
				List<CaisseMouvementArticlePersistant> listSortedArticle = CURRENT_COMMANDE.getList_article();
				if(listSortedArticle == null){
					listSortedArticle = new ArrayList<>();
				}
				IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
				%>
					
					<div class="form-group">
						<div class="col-md-12" style="margin-left: -20px;margin-right: 0px;padding-right: 3px;padding-left: 24px;">
							<table style="width: 100%;color: black;" id="cmd-table" align="center">
				
				<%
					// Total par menu
				String currentTableRef = (String)ControllerUtil.getUserAttribute("CURRENT_TABLE_REF", request);
				Integer NBR_NIVEAU_CAISSE = StringUtil.isNotEmpty(ContextGloabalAppli.getGlobalConfig("NBR_NIVEAU_CAISSE"))?Integer.valueOf(ContextGloabalAppli.getGlobalConfig("NBR_NIVEAU_CAISSE")):null;
				Map<String, BigDecimal> mttMenuMap = new HashMap<>();
				List<String> mttTableClient = new ArrayList<>();
				List<String> listTables = new ArrayList<>();
				Map<String, Integer> nbrCouvertTable = new HashMap<>();
				List<Integer> listIdxClient = new ArrayList<>();
				
				for(CaisseMouvementArticlePersistant caisseMvmP : listSortedArticle){
					if(BooleanUtil.isTrue(caisseMvmP.getIs_annule())){
						continue;
					}
					if(caisseMvmP.getIdx_client() != null && !listIdxClient.contains(caisseMvmP.getIdx_client())){
						listIdxClient.add(caisseMvmP.getIdx_client());
					}
					// Clients par table
					if(!mttTableClient.contains(caisseMvmP.getIdx_client()+"_"+caisseMvmP.getRef_table())){
						mttTableClient.add(caisseMvmP.getIdx_client()+"_"+caisseMvmP.getRef_table());
					}
					
					// Ajout tables
					 if(caisseMvmP.getRef_table() != null && !listTables.contains(caisseMvmP.getRef_table())){
						 listTables.add(caisseMvmP.getRef_table());
					 }
					 // Nombre de couverts
					 if(caisseMvmP.getNbr_couvert() != null){
						nbrCouvertTable.put(caisseMvmP.getRef_table(), caisseMvmP.getNbr_couvert());
					 }
					 if(caisseMvmP.getMenu_idx() == null){
					     continue;
					 }
					 mttMenuMap.put(caisseMvmP.getMenu_idx(),  BigDecimalUtil.add( mttMenuMap.get(caisseMvmP.getMenu_idx()), caisseMvmP.getMtt_total()));
				}
					// Trier les tables
					Collections.sort(listTables);
					Collections.sort(listIdxClient);
				
					if(listIdxClient != null && listIdxClient.size()>0 && listIdxClient.get(listIdxClient.size()-1) > CURRENT_COMMANDE.getMax_idx_client()){
						CURRENT_COMMANDE.setMax_idx_client(listIdxClient.get(listIdxClient.size()-1));
					}
					
					if(currentTableRef == null){
						if(listTables.size() == 0){
					listTables.add("XX");
						}
					} else if(!listTables.contains(currentTableRef)){
						listTables.add(currentTableRef);
					}
				
					Integer idxArticle = 0;
					int nbrNiveau = 0;
					BigDecimal sousTotal = null;
					BigDecimal sousTotalTable = null;
				
					// Tables -------------
					int idxTable = 0;
					for(String refTable : listTables){
						idxTable++;
						boolean isTablePassed = true;
						
						// Clients
						for(int i=1; i<=CURRENT_COMMANDE.getMax_idx_client(); i++){
					if(!listIdxClient.contains(i)){
						continue;
					}
				
					// Si client de la table
					if(!"XX".equals(refTable) 
							&& !refTable.equals(currentTableRef) 
							&& !mttTableClient.contains(i+"_"+refTable)
						){
						continue;
					}
					
					boolean isFamillePassed = false;
					
					if(listIdxClient.size() > 1){
						    	   if(i != listIdxClient.get(0)){
				%>
						    		   <tr style="color: #2196f3;font-weight: normal;background-color: #eeeeee;" class="client-root-style">
							       			<td colspan="2" align="right" style="font-weight: bold;">SOUS TOTAL CLIENT</td>
							       			<td align="right" style="font-weight: bold;"><%=BigDecimalUtil.formatNumber(sousTotal) %></td>
							      	   </tr>
						    	 <%}
						    	 
						    	 if(isTablePassed && !"XX".equals(refTable)){
							    	   if(idxTable != 1){%>
							    		   <tr style="color: black;font-weight: normal;background-color: #ccc;" class="client-root-style">
								       			<td colspan="2" align="right" style="font-weight: bold;">SOUS TOTAL TABLE</td>
								       			<td align="right" style="font-weight: bold;"><%=BigDecimalUtil.formatNumber(sousTotalTable) %></td>
								      	   </tr>
							    	 <%
							    	 sousTotalTable = null;
							    	   }
							      }
							  }%>
							  
							  <%
				// 			  if(listIdxClient.size() > 1 || ){ 
							 	 if(isTablePassed && !"XX".equals(refTable)){ %>
						    	 <tr style="color: black;background-color: #262626;" class="client-root-style">
						       		<td colspan="3" style="border-radius: 15px;font-weight: bold;color: #fbfbfb;" align="center">
						       			<%=(currentTableRef!=null && currentTableRef.equals(refTable) ? "<i style='color:#e3f2fd;' class='fa fa-arrow-circle-right'></i>":"") %> <%=refTable %> <%=nbrCouvertTable.get(refTable)!=null?" ("+nbrCouvertTable.get(refTable)+" couverts)":"" %>
						       			&nbsp;<input style="display: none;" type="checkbox" name="checktab_" id="checktab_" value="<%=refTable%>">
						       		</td>
						       </tr>
						    	 <%//}
						    	} %> 
						    	
						    	<%if(listIdxClient.size() > 1){  %>
						    	   <tr style="color: black;background-color: #f44336;height: 32px;" class="client-root-style">
							       		<td colspan="3" style="border-radius: 15px;padding-left: 10px;"> 
							       			<i class="fa fa-street-view" style="color: #a0d468"></i>
							       			CLIENT <%=i %>
							       			&nbsp;<input style="display: none;" type="checkbox" name="checkcli_" id="checkcli_" value="<%=i%>">
							       			
							       			<span>
							       				<std:link action="caisse-web.caisseWeb.print" targetDiv="generic_modal_body" params='<%="mvm="+CURRENT_COMMANDE.getId()+"&cli="+i %>' classStyle="btn btn-xs btn-yellow shiny" icon="fa-print" style="position: absolute;right: 23px;" />
							       			</span>
							       		</td>
							       </tr>
						      <%}
						   	 
						    sousTotal = null;
						    isTablePassed = false;
						    //
							for(CaisseMouvementArticlePersistant caisseMvmP : listSortedArticle){
						       if(BooleanUtil.isTrue(caisseMvmP.getIs_annule()) || caisseMvmP.getIdx_client()==null || caisseMvmP.getIdx_client()!=i || (caisseMvmP.getRef_table()!=null && !caisseMvmP.getRef_table().equals(refTable))){
						           continue;
						       }
						      sousTotal = BigDecimalUtil.add(sousTotal, (BooleanUtil.isTrue(caisseMvmP.getIs_offert()) ? null:caisseMvmP.getMtt_total()));
						      sousTotalTable = BigDecimalUtil.add(sousTotalTable, (BooleanUtil.isTrue(caisseMvmP.getIs_offert()) ? null:caisseMvmP.getMtt_total()));
						       
						       // Ajout du num�ro dans le tableau
						       String type = caisseMvmP.getType_ligne();
						       String libCmd = caisseMvmP.getLibelle();
						       if(type == null){
						           type = "XXX";
						       }
						       
						       if(type.equals(TYPE_LIGNE_COMMANDE.MENU.toString()) && (caisseMvmP.getLevel() == null || caisseMvmP.getLevel() > 1)) {
						           idxArticle++;
						           libCmd = idxArticle + " - " + libCmd;
						       } else if(type.equals(TYPE_LIGNE_COMMANDE.ART.toString())){
						           idxArticle++;
						           libCmd = idxArticle + " - " + libCmd;
						       }
						       
						       if(StringUtil.isNotEmpty(caisseMvmP.getCommentaire())){
						           libCmd = libCmd + " <i class='fa fa-comments-o' style='color:orange;font-size: 17px;cursor:pointer;' data-container='body' data-titleclass='bordered-blue' data-class='' data-toggle='popover' data-placement='top' data-title='Commentaire' data-content='"+caisseMvmP.getCommentaire()+"' data-original-title='' title=''></i>";
						       }
						       
						       String qte = RazService.getQteFormatted(caisseMvmP.getQuantite());
						       String mttTotal = "";
						       if( BooleanUtil.isTrue(caisseMvmP.getIs_offert())){
						    	   mttTotal = "<i class='fa fa-gift'style='color:green;font-size: 17px;cursor:pointer;' data-container='body' data-titleclass='bordered-blue' data-class='' data-toggle='popover' data-placement='top' data-title='Article offert' data-content='Prix : "+BigDecimalUtil.formatNumber(caisseMvmP.getMtt_total())+"' data-original-title='' title=''></i>";
						       } else if(!BigDecimalUtil.isZero(caisseMvmP.getMtt_total())){
						    	   mttTotal = BigDecimalUtil.formatNumber(caisseMvmP.getMtt_total());
						       }
						       
						       String styleTd = "";
						       String classType = "";
						       String height = "35px";
						       boolean isRootMenu = false;
						       boolean isMenu = false;
						      if(caisseMvmP.getMenu_idx() != null || type.equals(TYPE_LIGNE_COMMANDE.MENU.toString())){
						    	  if(type.equals(TYPE_LIGNE_COMMANDE.MENU.toString())) {
						    		  if(caisseMvmP.getLevel() != null && caisseMvmP.getLevel() == 1){
						    			  isRootMenu = true;
						    			  classType = "menu-cat-style cat-cmd-detail";
						    		  } else{
						    			  if(BooleanUtil.isTrue(caisseMvmP.getIs_menu())){
						    			 	 isMenu = true;
						    			  }
						        	   	  classType = "menu-style cat-cmd-detail";
						    		  }
						           } else if(type.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString())) {
						        	   classType = "ligne-style";
						        	   height = "23px";
						        	   nbrNiveau = 0;
						           } else if(type.equals(TYPE_LIGNE_COMMANDE.GROUPE_MENU.toString())){
						        	   classType = "group-style-fam";//"group-style cat-cmd-detail";
						        	   height = "5px";
						        	   nbrNiveau++;
						           } else if(type.equals(TYPE_LIGNE_COMMANDE.GROUPE_FAMILLE.toString())){
						        	   classType = "group-style-fam";//"group-style cat-cmd-detail";
						        	   height = "5px";
						        	   nbrNiveau++;
						           }
						       } else{//---------------------------------------------------------
						    	   if(type.equals(TYPE_LIGNE_COMMANDE.ART.toString())){
						    		   classType = "ligne-fam-style";
						    		   height = "23px";
						    		   nbrNiveau = 0;
						           } else if(type.equals(TYPE_LIGNE_COMMANDE.GROUPE_FAMILLE.toString())){
						        	   // Ajouter une ligne e s�paration
						        	   if(!isFamillePassed){
						        		   %>
						        	       <tr style="height:3px;color: black;background-color:#f35318;" class="menu-root-style">
						        	       		<td colspan="3" align="center"></td>
						        	       </tr>
						        	       <%
						        	       isFamillePassed = true;
						        	   }
						        	   classType = "group-style-fam";//"famille-style cat-cmd-famille";
						        	   height = "5px";
						        	   nbrNiveau++;
						           } 
						       }
						       
						       boolean isArticle = (type.equals(TYPE_LIGNE_COMMANDE.ART.toString()) || type.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString()));
						       boolean isToAdd = (isArticle || NBR_NIVEAU_CAISSE == null || nbrNiveau <= NBR_NIVEAU_CAISSE) && !caisseMvmP.getLibelle().startsWith("#");
						       if(caisseMvmP.getLevel() != null && caisseMvmP.getLevel() > 1){
						    	   styleTd = styleTd + "padding-left:"+(7*caisseMvmP.getLevel())+"px;";
						       } else if(isArticle){
						    	   styleTd = styleTd + "padding-left:15px;";
						       }
						      
						       if(isToAdd){
						    	   boolean isNotArt = (!type.equals(TYPE_LIGNE_COMMANDE.ART.toString()) && !type.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString()));
						       
						       		boolean isToClolapse = (caisseMvmP.getMenu_idx()!= null 
						       										&& !type.equals(TYPE_LIGNE_COMMANDE.MENU.toString()));
						       %>
						       
						       <%if(isRootMenu){ %>
						       	<tr id="<%=isToClolapse ? "tr_"+caisseMvmP.getMenu_idx():"" %>" class="<%=classType%>" style="color:#ebbd08;background-color: #fff8c6;font-size: 19px;height: <%=height %>;" >
						       		<td colspan="3" style="<%=styleTd %>;<%="5px".equals(height)?"":"line-height:"+height %>;">
						       			<%=libCmd %>
						       		</td>
						       	</tr>
						       <%} else{ 
						       String mttTotalMenu = BigDecimalUtil.formatNumber(mttMenuMap.get(caisseMvmP.getMenu_idx()));
						       %>
						         <tr id="<%=isToClolapse ? "tr_"+caisseMvmP.getMenu_idx():"" %>" class="<%=classType%> <%=isMenu?(" mnu_"+caisseMvmP.getMenu_idx()):"" %>" style="height: <%=height %>;<%=isToClolapse?"display:none;":"" %>">
						       		<td style="<%=styleTd %>;<%="5px".equals(height)?"":"line-height:"+height %>;">
						       			<%
						       			if(type.equals(TYPE_LIGNE_COMMANDE.MENU.toString()) && caisseMvmP.getLevel()>1 ){
						       				%>
						       				<a class="btn btn-default btn-xs icon-only white lnk_mnu" style="padding: 4px 1px;" href="javascript:void(0);" mnu="<%=caisseMvmP.getMenu_idx() %>">
						       					<i class="fa fa-plus" style="font-size: 16px;color: #03A9F4;"></i>
							       			</a>
							       			<%
						       				Map<String, byte[]> dataimg = service.getDataImage(caisseMvmP.getElementId(), "menu");
						       				String stl = "";
						       				if(dataimg.size() > 0){
						       					stl = "width: 33px;height:33px;float: right;background-size: 33px 33px !important;background: url(data:image/jpeg;base64,"+FileUtil.getByte64(dataimg.entrySet().iterator().next().getValue())+") no-repeat;";
						       					%>
						       					<span style="<%=stl%>"></span>
						       					<%
						       				}
						       			} else if(isArticle){
						       				Map<String, byte[]> dataimg = service.getDataImage(caisseMvmP.getElementId(), "article");
						       				String stl = "";
						       				if(dataimg.size() > 0){
						       					stl = "width: 23px;height:23px;float: right;background-size: 23px 23px !important;background: url(data:image/jpeg;base64,"+FileUtil.getByte64(dataimg.entrySet().iterator().next().getValue())+") no-repeat;";
						       					%>
						       					<span style="<%=stl%>"></span>
						       					<%
						       				}
						       			}
						       			%>
						       		
						       			<%=(!isNotArt ? "<span class='fa fa-circle' style='color:#4caf50;font-size:9px;'></span>&nbsp;":"")+libCmd %>
						       		</td>
						       		<td align="right">
						       			<%if(BooleanUtil.isTrue(caisseMvmP.getIs_encaisse())){ %>
						       					<i class="fa fa-check-circle-o" style="font-size: 17px;color: #53a93f;"></i>
						       			<%} %>
						       			<%=isNotArt ? "":qte %>
						       		</td>
						       		<td align="right" style="font-weight: bold;">
						       			<%if(caisseMvmP.getMenu_idx() == null){ %>
						       				<%=mttTotal %>
						       			<%} else{ 
						       			%>
						       				<span class="span_mtt_art" style='display:<%=isToClolapse ? "":"none"%>;'><%=mttTotal %></span>
						       				
						       			<%
						       			if(BooleanUtil.isTrue(caisseMvmP.getIs_offert())){
						       				mttTotalMenu = "<i class='fa fa-gift'style='color:green;font-size: 17px;cursor:pointer;' data-container='body' data-titleclass='bordered-blue' data-class='' data-toggle='popover' data-placement='top' data-title='Article offert' data-content='Prix : "+mttTotalMenu+"' data-original-title='' title=''></i>";
									       }
						       			%>	
						       				<span class="span_mtt_mnu" style='display:<%=isToClolapse ? "none":""%>;'><%=isMenu ? mttTotalMenu : "" %></span>
						       			<%} %>
						       		</td>
						       </tr>
						       <%
						         }
						       }
							}
					   }
					}
					
					if(listIdxClient.size() > 1){%>
				 		 <tr style="color: #2196f3;background-color: #eeeeee;font-weight: normal;" class="menu-root-style">
					     	<td colspan="2" align="right" style="font-weight: bold;">SOUS TOTAL CLIENT</td>
					     	<td align="right" style="font-weight: bold;"><%=BigDecimalUtil.formatNumber(sousTotal) %></td>
					     </tr>
				   <%
					}
					
					if(listTables.size()>0 && !listTables.get(0).equals("XX")){%>
					 <tr style="color: black;background-color: #ccc;font-weight: normal;" class="menu-root-style">
				    	<td colspan="2" align="right" style="font-weight: bold;">SOUS TOTAL TABLE</td>
				    	<td align="right" style="font-weight: bold;"><%=BigDecimalUtil.formatNumber(sousTotalTable) %></td>
				    </tr>
					<%
					}
					
					List<CaisseMouvementOffrePersistant> listOffres = RazService.getListOffre(CURRENT_COMMANDE);
					
				   // Les offres
				   if(listOffres != null && listOffres.size() > 0){
					   %>
				       <tr style="color: green;" class="menu-root-style">
				       		<td colspan="3" align="center">Offres</td>
				       </tr>
				       <%
					   
					   for (CaisseMouvementOffrePersistant offreDet : listOffres) {
					       if(offreDet.getIs_annule() != null && offreDet.getIs_annule()){
					           continue;
					       }
					       Long idOffre = offreDet.getOpc_offre().getId();
					       String libelleOffre = offreDet.getOpc_offre().getLibelle();
					       
				      		String params = "cd="+idOffre+"&elm="+offreDet.getOpc_offre().getId()+"&tp=OFFRE";
					       %>
					       <tr style="color: red;" class="ligne-style">
					       		<td><%="**"+libelleOffre %></td>
					       		<td></td>
					       		<td align="right">-<%=BigDecimalUtil.formatNumber(offreDet.getMtt_reduction()) %></td>
					       </tr>
					       <%
					   }
				   }
				%>     
				</table>  
							
						</div>	
					</div>
				</div>
                
            </div>
        </div>
    </div>
</div>

<script src="https://maps.googleapis.com/maps/api/js?key=<%=StrimUtil.getGlobalConfigPropertie("api.google.key") %>&libraries=places&callback=initMap" async defer></script>