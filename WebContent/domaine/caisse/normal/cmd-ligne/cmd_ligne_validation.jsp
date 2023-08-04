<%@page import="java.net.InetAddress"%>
<%@page import="java.util.Date"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementOffrePersistant"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="java.util.Collections"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.TYPE_LIGNE_COMMANDE"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.Map"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<% boolean isToNotify = (boolean)request.getAttribute("isToNotify"); %>


<style>

 #div_ligne {
            margin-top: 3px;
            margin-left: 4px;
            overflow: hidden auto;
            height: 410px;
        }
        
#customers {
  font-family: Arial, Helvetica, sans-serif;
  border-collapse: collapse;
  width: 100%;
}

#customers td, #customers th {
  border: 1px solid #ddd;
  padding: 8px;
}

#customers tr:nth-child(even){background-color: #f2f2f2;}

#customers tr:hover {background-color: #ddd;}

#customers th {
  padding-top: 12px;
  padding-bottom: 12px;
  text-align: left;
  background-color: #04AA6D;
  color: white;
}
</style>

<style>
    #div-ligne {
        height: 850px; /* Set the desired height for the scrollable area */
        overflow-y: auto; /* Enable vertical scrolling */
    }
</style>

<%
    String encryptedAction = EncryptionUtil.encrypt("caisse.clientMobile.initCmdLigne");
    String ajaxUrl = "front?w_f_act=" + encryptedAction + "&isAct=1";
%>


<script type="text/javascript">


	$(document).ready(function (){

		$(document).off('click', "a[id^='lnk_det']");
		$(document).on('click', "a[id^='lnk_det']", function(){
			var idx = $(this).attr("curr");
			$("tr[id^='tr_det_']").each(function(){
				if($(this).attr("id") != "tr_det_"+idx){
					$(this).hide();
				}
			});
			$("#tr_det_"+idx).toggle(100);
		});
		
		$("#pdf_link").click(function(){
			$(this).attr("href", $(this).attr("href")+"&dateDebut="+$("#dateDebut").val()+"&dateFin="+$("#dateFin").val());
		});
		
		$("#left-div").hide();
		$("#div-ligne").css("width", "131em");
	});
	
	  function loadCommandeAjax(){
			isInProgress = true;
			
			$.ajax({
		        beforeSend: function(data) {
		        },
		        url: "<%=ajaxUrl%>",
		        type: "GET",
		        cache: false,
		        dataType: 'text',
		        error: function(data) {
		        },
		        success: function(data) {
		        	
		        	isInProgress = false;
		        	
		        	<%if(isToNotify){%>
		        	<%System.out.println("dddddddddddddd"+isToNotify);%>
	                showNotification("New Command", "A new command has appeared.");
	                
	                
	                <%}%>
	                var divRightContent = $(data).find("#div-right").html();

	                // Inject the extracted content into the div with id "id-rightt".
	                $("#id-rightt").html(divRightContent);
		        }});
			
		}
	  if(typeof CMD_LIV_INTERVAL !== "undefined") {
		    clearInterval(CMD_LIV_INTERVAL);
		}

		// Set the new interval
		CMD_LIV_INTERVAL = setInterval(function() {
		    loadCommandeAjax();
		}, 5000);
</script>


<script>

function showNotification(title, message) {
	  // Check if the browser supports Web Notifications
	  if (!("Notification" in window)) {
	    console.log("This browser does not support Web Notifications.");
	    return;
	  }

	  // Check the notification permission
	  if (Notification.permission === "granted") {
	    // If permission is granted, show the notification
	    const notification = new Notification(title, { body: message });
	  } else if (Notification.permission !== "denied") {
	    // If permission is not denied, request permission from the user
	    Notification.requestPermission().then(permission => {
	      if (permission === "granted") {
	        // If permission is granted after the request, show the notification
	        const notification = new Notification(title, { body: message });
	      }
	    });
	  }
	}
    // Function to display the notification
    // WebSocket connection
      <%--   <%
		String adresseIp = request.getLocalAddr();
        if (adresseIp.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {// Si on est sur la m�me machine (local ost)
            InetAddress inetAddress = InetAddress.getLocalHost();
            adresseIp = inetAddress.getHostAddress();
        }
		String path = adresseIp+":"+request.getLocalPort() + request.getContextPath();
		%>
		
		var webSocket = new WebSocket("ws://<%=path%>/cmd-ligne");

		webSocket.onopen = function(message) {
        console.log('WebSocket connection established'+ message);
    };

    webSocket.onmessage = function(message) {
        var newCommandCount = parseInt(event.data);
        if (newCommandCount > 0) {
            // If a new command notification is received, display the notification
            showNotification();
        }
    };

    webSocket.onclose = function(message) {
        console.log('WebSocket connection closed'+ message);
    };

    webSocket.onerror = function(message) {
        console.error('WebSocket error:'+ message);
    }; --%>
</script>

	
<script>
    // Function to display the alert and request notification permission
    function requestNotificationPermission() {
      if ('Notification' in window) {
        // If the browser supports notifications
        Notification.requestPermission().then((permission) => {
          if (permission === 'granted') {
            // Permission has been granted
            alert('Notification permission granted!');
          } else if (permission === 'denied') {

            // Permission has been denied
            alert('Notification permission denied.');
          } else {
            // The user closed the permission prompt without making a choice
            alert('Notification permission not granted.');
          }
        });
      } else {
        // If the browser does not support notifications
        alert('Your browser does not support notifications.');
      }
    }

    // Event listener for the button click
    document.getElementById('getPermissionBtn').addEventListener('click', function() {
      requestNotificationPermission();
    });
  </script>


 
<div id="div-ligne" style="margin-top: 3px;margin-left: 4px;overflow-x: hidden;overflow-y: auto;">
 <std:button id="getPermissionBtn" classStyle=" btn-sm shiny btn btn-danger" value="Be Notified" ></std:button>

<std:form name="search-form">
			<div class="col-lg-12 col-sm-12 col-xs-12">
				<div class="widget">
					<div class="widget-header bordered-bottom bordered-themeprimary">
                         <i class="widget-icon fa fa-tasks themeprimary"></i>
                         <span class="widget-caption themeprimary">COMMANDES EN ATTENTE DE VALIDATION</span>
                     </div>
                     <div class="widget-body no-padding">
			
				<%
				IArticleService service = ServiceUtil.getBusinessBean(IArticleService.class);
				Integer NBR_NIVEAU_CAISSE = StringUtil.isNotEmpty(ContextGloabalAppli.getGlobalConfig("NBR_NIVEAU_CAISSE"))?Integer.valueOf(ContextGloabalAppli.getGlobalConfig("NBR_NIVEAU_CAISSE")):null;
				List<CaisseMouvementPersistant> list_commande = (List<CaisseMouvementPersistant>)request.getAttribute("list_commande");
				List<CaisseMouvementPersistant> list_commandeAvenir = (List<CaisseMouvementPersistant>)request.getAttribute("list_commandeAvenir");
				List[] listCmds = {list_commande, list_commandeAvenir};
				
			int idx = 0;
			for(List<CaisseMouvementPersistant> list : listCmds){
				
				if(idx == 1 && list_commandeAvenir.size() > 0){ %>
					<span style="color: red;font-size: 12px;">
						** Les commandes à venir seront validées par l'AUTOMATE.<br>
						Si vous valider manuellement, alors elle sera envoyée immédiatement.
					</span>
					<hr>
				<%}
				
				for(CaisseMouvementPersistant caisseMvm : list){%>
					<c:set var="caisseMouvement" value="<%=caisseMvm %>" />
					
					<div id="div_bloc_${caisseMouvement.id }" style="border: 1px solid gray;width: 96%;margin-bottom: 5px;">
							<!-- widget grid -->
								<div class="widget-header bordered-bottom bordered-blue" style='background-color: <%=idx==1 ? "#ff9800":"#dcdcdc"%>;'>
									<span style="width: 100%;line-height: 16px;" class="widget-caption">Vente : <b>${caisseMouvement.ref_commande }</b> du <b><fmt:formatDate value="${caisseMouvement.date_creation }" pattern="dd/MM/yyyy HH:mm" /></b>
										${(caisseMouvement.is_annule and !caisseMouvement.is_tovalidate) ? ' (<b style="color:red;">Annul&eacute;e</b>)' : '' }
									</span>
									<%if(idx==1){ %>
									<%
										int duree = DateUtil.getDiffMinuts(new Date(), caisseMvm.getDate_souhaite());
										long h = (duree/60);
										long min = h > 0 ? (duree%60) : duree;
										String str = (h>0 ? h+"h ":"")+(min>0?min+"min":"");		
									%>
										<span style="color: black;color: #f7ffa4;font-weight: bold;">
											[DATE SOUHAITÉE : <%=DateUtil.dateToString(caisseMvm.getDate_souhaite(), "dd/MM/yyyy HH:mm") %>]
											(<%=str %>)
										</span>
									<%} %>
								</div>
								<div class="widget-body">
										<div class="row">
											<div class="form-group">
												<std:label style="padding-top: 0px;" classStyle="control-label col-md-3" value="Date encaissement" />
												<div class="col-md-9">
													<span class="widget-caption">:<b><fmt:formatDate value="${caisseMouvement.date_encais }" pattern="dd/MM/yyyy HH:mm" /></b></span>
												</div>
											</div>	
											<div class="form-group" style="margin-bottom: 0px;">
												<std:label style="padding-top: 0px;" classStyle="control-label col-md-3" value="Type" />
												<div class="col-md-3"><b>
													<c:choose>
														<c:when test="${caisseMouvement.type_commande=='P' }">
															SUR PLACE
														</c:when>
														<c:when test="${caisseMouvement.type_commande=='E' }">
															A EMPORTER
														</c:when>
														<c:when test="${caisseMouvement.type_commande=='L' }">
															LIVRAISON
														</c:when>
													</c:choose></b>
												</div>
												<std:label style="padding-top: 0px;" classStyle="control-label col-md-3" valueKey="caisseMouvement.mode_paiement" />
												<div class="col-md-3">
													<b>
														<c:choose>
															<c:when test="${caisseMouvement.mode_paiement == 'es'}">
																CARTE BANCAIRE
															</c:when>
															<c:when test="${caisseMouvement.mode_paiement == 'cb' }">
																ESPECES
															</c:when>
															<c:otherwise>
																ESPECES
															</c:otherwise>
														</c:choose>
													</b>
												</div>
											</div>	
											<hr style="margin-top: 5px;margin-bottom: 0px;">
											<div class="form-group" style="margin-bottom: 0px;">
												<c:if test="${caisseMouvement.mtt_donne > 0 }">
													<std:label style="padding-top: 0px;" classStyle="control-label col-md-3" value="Esp&egrave;ces" />
													<div class="col-md-3">
														<fmt:formatDecimal value="${caisseMouvement.mtt_donne }"/>
													</div>
												</c:if>
												<c:if test="${caisseMouvement.mtt_donne_cb > 0 }">
													<std:label style="padding-top: 0px;" classStyle="control-label col-md-3" value="Carte" />
													<div class="col-md-3">
														<fmt:formatDecimal value="${caisseMouvement.mtt_donne_cb }"/>
													</div>
												</c:if>
												<c:if test="${caisseMouvement.mtt_portefeuille > 0 }">
													<std:label style="padding-top: 0px;line-height: 5px;" classStyle="control-label col-md-3" value="Portefeuille"/>											<div class="col-md-3">
														<fmt:formatDecimal value="${caisseMouvement.mtt_portefeuille }"/>
													</div>
												</c:if>
												<c:if test="${caisseMouvement.mtt_donne_point > 0 }">
													<std:label style="padding-top: 0px;line-height: 5px;" classStyle="control-label col-md-3" value="Point" />
													<div class="col-md-3">
														<fmt:formatDecimal value="${caisseMouvement.mtt_donne_point }"/>
													</div>
												</c:if>
												<c:if test="${caisseMouvement.mtt_art_offert > 0 }">
													<std:label style="padding-top: 0px;" classStyle="control-label col-md-3" value="Offert" /> 
													<div class="col-md-3">
														<fmt:formatDecimal value="${caisseMouvement.mtt_art_offert }" /> 
													</div>	
												</c:if>
												<c:if test="${caisseMouvement.mtt_reduction > 0 }">	
													<std:label style="padding-top: 0px;" classStyle="control-label col-md-3" value="R&eacute;duction Cmd" />
													<div class="col-md-3">
														<fmt:formatDecimal value="${caisseMouvement.mtt_reduction }"/>
													</div>
												</c:if>	
												<c:if test="${caisseMouvement.mtt_art_reduction > 0 }">	
													<std:label style="padding-top: 0px;" classStyle="control-label col-md-3" value="R&eacute;duction Art" />
													<div class="col-md-3">
														<fmt:formatDecimal value="${caisseMouvement.mtt_art_reduction }"/>
													</div>
												</c:if>	
											</div>
											<div class="form-group" style="margin-bottom:0px;">	
												<std:label style="padding-top: 0px;" classStyle="control-label col-md-3" valueKey="caisseMouvement.opc_client" />
												<div class="col-md-9" style="color: blue;padding-top: 6px;">
													${caisseMouvement.opc_client.nom } ${caisseMouvement.opc_client.prenom } ${caisseMouvement.opc_client.getAdressFull() }
													<c:if test="${not empty caisseMouvement.opc_client.telephone}">
														[${caisseMouvement.opc_client.telephone}]
													</c:if>
												</div>
											</div>	
											</div>
											<hr style="margin-top: 5px;margin-bottom: 0px;">
											<div class="form-group" style="margin-bottom: 0px;">
												<std:label style="padding-top: 0px;" classStyle="control-label col-md-3" value="Montant cmd" />
												<div class="col-md-3" style="color: blue;font-weight: bold;">
													<fmt:formatDecimal value="${caisseMouvement.mtt_commande }" /> 
												</div>
												<std:label style="padding-top: 0px;" classStyle="control-label col-md-3" value="Montant net" />
												<div class="col-md-3" style="font-weight: bold;font-size:16px;color: #630767;">
													<fmt:formatDecimal value="${caisseMouvement.mtt_commande_net }" /> 
												</div>	
											</div>
											<div class="form-group" style="margin-top: 10px;margin-left: 0px;">
												<a style="color: blue;font-size: 16px;text-decoration: underline;" href="javascipt:" onclick="$('#det-<%=caisseMvm.getId() %>').toggle(100);">Détail commande</a>
												<div id="det-<%=caisseMvm.getId() %>" class="col-md-12" style="display:none;margin-left: -2px;max-height: 400px;overflow-y: auto;overflow-x:hidden;">
													<table style="width: 98%;color: black;border: 1px solid #2dc3e8;" align="center">
						
						<%
							// Total par menu
						Map<String, BigDecimal> mttMenuMap = new HashMap<>();
						List<String> mttTableClient = new ArrayList<>();
						List<String> listTables = new ArrayList<>();
						List<Integer> listIdxClient = new ArrayList<>();
						Map<String, Integer> nbrCouvertTable = new HashMap<>();
						
						for(CaisseMouvementArticlePersistant caisseMvmP : caisseMvm.getList_article()){
							if(!listIdxClient.contains(caisseMvmP.getIdx_client()) && caisseMvmP.getIdx_client() != null){
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
						
							if(listIdxClient != null && listIdxClient.size()>0 && listIdxClient.get(listIdxClient.size()-1) > caisseMvm.getMax_idx_client()){
								caisseMvm.setMax_idx_client(listIdxClient.get(listIdxClient.size()-1));
							}
							
								if(listTables.size() == 0){
									listTables.add("XX");
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
								for(int i=1; i<=caisseMvm.getMax_idx_client(); i++){
							if(!listIdxClient.contains(i)){
								continue;
							}
						
							// Si client de la table
							if(!"XX".equals(refTable) 
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
									       			<td></td>
									      	   </tr>
								    	 <%}
								    	 
								    	 if(isTablePassed && !"XX".equals(refTable)){
									    	   if(idxTable != 1){%>
									    		   <tr style="color: black;font-weight: normal;background-color: #ccc;" class="client-root-style">
										       			<td colspan="2" align="right" style="font-weight: bold;">SOUS TOTAL TABLE</td>
										       			<td align="right" style="font-weight: bold;"><%=BigDecimalUtil.formatNumber(sousTotalTable) %></td>
										       			<td></td>
										      	   </tr>
									    	 <%
									    	 sousTotalTable = null;
									    	   }
									      }
									  }%>
									  
									  <%
									 	 if(isTablePassed && !"XX".equals(refTable)){ %>
								    	 <tr style="color: black;background-color: #262626;" class="client-root-style">
								       		<td colspan="3" style="border-radius: 15px;font-weight: bold;color: #fbfbfb;" align="center">
								       			<%=refTable %> <%=nbrCouvertTable.get(refTable)!=null?" ("+nbrCouvertTable.get(refTable)+" couverts)":"" %>
								       			&nbsp;<input style="display: none;" type="checkbox" name="checktab_" id="checktab_" value="<%=refTable%>">
								       		</td>
								       </tr>
								    	 <%
								    	} %> 
								    	
								    	<%if(listIdxClient.size() > 1){  %>
								    	   <tr style="color: black;background-color: #f44336;height: 32px;" class="client-root-style">
									       		<td colspan="3" style="border-radius: 15px;padding-left: 10px;"> 
									       			<i class="fa fa-street-view" style="color: #a0d468"></i>
									       			CLIENT <%=i %>
									       			&nbsp;<input style="display: none;" type="checkbox" name="checkcli_" id="checkcli_" value="<%=i%>">
									       			
									       			<span>
									       				<std:link action="caisse-web.caisseWeb.print" targetDiv="div_gen_printer" params='<%="mvm="+caisseMvm.getId()+"&cli="+i %>' classStyle="btn btn-xs btn-yellow shiny" icon="fa-print" style="position: absolute;right: 23px;" />
									       			</span>
									       		</td>
									       </tr>
								      <%}
								   	 
								    sousTotal = null;
								    isTablePassed = false;
								    //
									for(CaisseMouvementArticlePersistant caisseMvmP : caisseMvm.getList_article()){
								       if((caisseMvmP.getIdx_client()!=null && caisseMvmP.getIdx_client()!=i) || (caisseMvmP.getRef_table()!=null && !caisseMvmP.getRef_table().equals(refTable))){
								           continue;
								       }
								       
								       if(!BooleanUtil.isTrue(caisseMvmP.getIs_annule()) && !BooleanUtil.isTrue(caisseMvmP.getIs_offert())){
								      		sousTotal = BigDecimalUtil.add(sousTotal, caisseMvmP.getMtt_total());
								      		sousTotalTable = BigDecimalUtil.add(sousTotalTable, caisseMvmP.getMtt_total());
								       }
								       
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
								       
								       Integer qte = (caisseMvmP.getQuantite() != null ? caisseMvmP.getQuantite().intValue() : null);
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
								       String params = (BooleanUtil.isTrue(caisseMvmP.getIs_offert())?"isaf=1&":"") +"cli="+i+"&cd="+caisseMvmP.getCode()+"&elm="+caisseMvmP.getElementId()+"&par="+StringUtil.getValueOrEmpty(caisseMvmP.getParent_code())+"&mnu="+caisseMvmP.getMenu_idx()+"&tp="+caisseMvmP.getType_ligne();
								       
								       if(caisseMvmP.getLevel() != null && caisseMvmP.getLevel() > 1){
								    	   styleTd = styleTd + "padding-left:"+(7*caisseMvmP.getLevel())+"px;";
								       } else if(isArticle){
								    	   styleTd = styleTd + "padding-left:30px;";
								       }
								      
								       if(isToAdd){
								    	   boolean isNotArt = (!type.equals(TYPE_LIGNE_COMMANDE.ART.toString()) && !type.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString()));
								       
								       		boolean isToClolapse = (caisseMvmP.getMenu_idx()!= null 
								       										&& !type.equals(TYPE_LIGNE_COMMANDE.MENU.toString()));
								       %>
								       
								       <%if(isRootMenu){ %>
								       	<tr id="<%=isToClolapse ? "tr_"+caisseMvmP.getMenu_idx():"" %>" class="<%=classType%>" style="color:#ebbd08;background-color: #fff8c6;font-size: 19px;height: <%=height %>;" par="<%="isroot=1&"+params%>">
								       		<td colspan="4" style="<%=styleTd %>;<%="5px".equals(height)?"":"line-height:"+height %>;<%=BooleanUtil.isTrue(caisseMvmP.getIs_annule())?"text-decoration: line-through;color: red;":""%>">
								       			<%=libCmd %>
								       			<%if(BooleanUtil.isTrue(caisseMvmP.getIs_annule()) && caisseMvmP.getDate_annul() != null){ %>
								       				<span style="color: black;">(<%=(caisseMvmP.getOpc_user_annul()!=null?caisseMvmP.getOpc_user_annul().getLogin():"")%> - <%=DateUtil.dateTimeToString(caisseMvmP.getDate_annul()) %>)</span>
								       			<%} %>
								       		</td>
								       	</tr>
								       <%} else{ 
								       String mttTotalMenu = BigDecimalUtil.formatNumber(mttMenuMap.get(caisseMvmP.getMenu_idx()));
								       boolean isMtt = (!BigDecimalUtil.isZero(caisseMvmP.getMtt_total()) && !BooleanUtil.isTrue(caisseMvmP.getIs_offert()));
								       %>
								         <tr id="<%=isToClolapse ? "tr_"+caisseMvmP.getMenu_idx():"" %>" class="<%=classType%> <%=isMenu?(" mnu_"+caisseMvmP.getMenu_idx()):"" %>" style="height: <%=height %>;<%=isToClolapse?"display:none;":"" %> <%=isArticle?"font-weight:bold;":""%>" par="<%=(isMtt?"isart=1&":"")+params%>">
								       		<td style="<%=styleTd %>;<%="5px".equals(height)?"":"line-height:"+height %>;<%=BooleanUtil.isTrue(caisseMvmP.getIs_annule())?"text-decoration: line-through;color: red;":""%>">
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
								       		
								       			<%=(!isNotArt ? "<span class='fa fa-circle' style='color:#4caf50;'></span>&nbsp;":"")+libCmd %>
								       			<%if(BooleanUtil.isTrue(caisseMvmP.getIs_annule()) && caisseMvmP.getDate_annul() != null){ %>
								       				<span style="color: black;">(<%=(caisseMvmP.getOpc_user_annul()!=null?caisseMvmP.getOpc_user_annul().getLogin():"")%> - <%=DateUtil.dateTimeToString(caisseMvmP.getDate_annul()) %>)</span>
								       			<%} %>
								       			
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
							     	<td></td>
							     </tr>
						   <%}
							
							if(listTables.size()>0 && !listTables.get(0).equals("XX")){%>
							 <tr style="color: black;background-color: #ccc;font-weight: normal;" class="menu-root-style">
						    	<td colspan="2" align="right" style="font-weight: bold;">SOUS TOTAL TABLE</td>
						    	<td align="right" style="font-weight: bold;"><%=BigDecimalUtil.formatNumber(sousTotalTable) %></td>
						    	<td></td>
						    </tr>
							<%
							}
							
						   // Les offres
						   if(caisseMvm.getList_offre() != null && caisseMvm.getList_offre().size() > 0){
							   %>
						       <tr style="color: green;" class="menu-root-style">
						       		<td colspan="4" align="center">Offres</td>
						       </tr>
						       <%
							   
							   for (CaisseMouvementOffrePersistant offreDet : caisseMvm.getList_offre()) {
							       if(offreDet.getIs_annule() != null && offreDet.getIs_annule()){
							           continue;
							       }
							       Long idOffre = offreDet.getOpc_offre().getId();
							       String libelleOffre = offreDet.getOpc_offre().getLibelle();
							       
						      		String params = "cd="+idOffre+"&elm="+offreDet.getOpc_offre().getId()+"&tp=OFFRE";
							       %>
							       <tr style="color: red;" class="ligne-style" par="<%=params%>">
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
					<div style="width: 100%;text-align: center;margin-bottom: 7px;margin-top: 6px;">
						<std:link onClick="$('#div_bloc_${caisseMouvement.id }').hide();" actionGroup="C" classStyle="btn btn-success" workId="<%=caisseMvm.getId().toString() %>" action="caisse.clientMobile.statCmd" params="stat=A" icon="fa-3x fa-check" value="Accepter" tooltip="Accepter" />
						<std:link onClick="$('#div_bloc_${caisseMouvement.id }').hide();" actionGroup="C" classStyle="btn btn-danger" workId="<%=caisseMvm.getId().toString() %>" action="caisse.clientMobile.statCmd" params="stat=R" icon="fa-3x fa-time" value="Rejeter" tooltip="Rejeter" />
					</div>
				</div>
				<%} 
				idx++;
			}%>
			
			<%if(list_commande.size() == 0 && list_commandeAvenir.size() == 0){ %>
				<span style="line-height: 43px;padding-left: 5px;">Aucune mouvement à valider.</span>
			<%} %>
				
				</div>
			</div>
		  </div>	
		</std:form>
	</div><!-- 
</div>
 -->