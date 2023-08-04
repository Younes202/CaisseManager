<%@page import="appli.controller.domaine.util_erp.ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.STATUT_LIVREUR_COMMANDE"%>
<%@page import="appli.model.domaine.administration.persistant.UserPersistant"%>
<%@page import="framework.model.common.service.MessageService"%>
<%@page import="framework.model.common.constante.ProjectConstante"%>
<%@page import="java.net.InetAddress"%>
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
String tp = (String)request.getAttribute("tp");
if(tp == null){
	tp = "A";
}
Long livreurId = ContextAppli.getUserBean().getId();
%>

<style>
	.modal-backdrop{
		display: none;
	}
</style>

<script type="text/javascript">
var currPage = 0;
var data = "";
var isInProgress = false;
 $(document).ready(function (){
	
	for (var i = 1; i <= CMD_LIV_INTERVAL+10; i++){
	    window.clearInterval(i);
	}
	
	loadCommandeAjax('<%=tp%>');
	
	setTimeout(() => {
		sendData();	
	}, 1000);
	
	const element = document.querySelector("#div_cmd");
	var oldTopPos = 0;
	element.addEventListener("scroll", (event) => {
	    if($("#div_cmd").scrollTop() > oldTopPos && $("#div_cmd").scrollTop() >= ($("#div_cmd").height()+400)) {
	    	if(!isInProgress){
		    	currPage++;
		    	loadCommandeAjax('<%=tp%>');   
		    }
	    }
	    oldTopPos = $("#div_cmd").scrollTop();
	});
});

<%
	String adresseIp = request.getLocalAddr();
	if (adresseIp.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {// Si on est sur la m�me machine (local ost)
	    InetAddress inetAddress = InetAddress.getLocalHost();
	    adresseIp = inetAddress.getHostAddress();
	}
	String path = adresseIp+":"+request.getLocalPort() + request.getContextPath();
%>

var webSocket = new WebSocket("ws://<%=path%>/mobile-track");
startWebsocket();

function getValOrEmpty(val){
	return (!val || val == null || val == 'null') ? '' : val;
}

function startWebsocket(){
	webSocket.onopen = function(message) {
		console.debug('Connected ...');
		wsSendMessage();
	};

	webSocket.onmessage = function(message) {
		<%if(!"P".equals(tp)){%>
			if(message.data != ""){
				var dataSended = jQuery.parseJSON(message.data);
				populateData(dataSended);
				restaureDate(dataSended);
			}
		<%}%>
	}

	webSocket.onclose = function(message) {
		console.debug("Disconnect ..."+message);
		startWebsocket();
	};
	webSocket.onerror = function(message) {
		console.debug("Error ... : "+message);
		
		CMD_LIV_INTERVAL = setInterval(function(){ 
			loadCommandeAjax('<%=tp%>'); 
		}, 10000);
	};
}

function wsSendMessage() {
	webSocket.send("<%="LIV"+livreurId %>");
}
function wsCloseConnection() {
	webSocket.close();
}

function loadCommandeAjax(tp, cmd){
	var params = (cmd ? "&cmd="+cmd : "")+"&pg="+currPage;
	showSpinner();
	isInProgress = true;
	
	$.ajax({
        beforeSend: function(data) {
        },
        url: 'front?w_f_act=<%=EncryptionUtil.encrypt("caisse.livreurMobile.loadCommandes")%>&tp=' + tp + params,
        type: "POST",
        cache: false,
        dataType: 'text',
        error: function(data) {
        	hideSpinner();
        },
        success: function(data) {
        	var jsonResponse = jQuery.parseJSON(data);
        	if (Array.isArray(jsonResponse) && jsonResponse.length) {
        		$(jsonResponse).each(function (i, val) {
        			populateData(tp, val);
        			restaureDate(val);
        		});
        	} else{
        		$("#div_cmd").html("<h3 style='text-align:center;color:blue;'>Aucune commande disponible.</h3>");	
        	}
        	hideSpinner();
        	isInProgress = false;
        }
	});
}
function populateData(tp, val){	
	if($('#card_'+val.id).length > 0){
		if(tp == 'A' && (val.statut_livreur == '<%=STATUT_LIVREUR_COMMANDE.LIVRE%>'
				|| val.statut_livreur == '<%=STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL%>')){
			$('#card_'+val.id).remove();
			return;
		}
	}
	
	data = "";
	var libStatut = '';
	var params = '';
	var style = '';
	
	if(tp == 'H'){
		if(val.statut_livreur == '<%=STATUT_LIVREUR_COMMANDE.EN_ROUTE%>'){
			libStatut = '<%=STATUT_LIVREUR_COMMANDE.EN_ROUTE.getLibelle() %>';
			style = "font-weight: bold;text-transform: uppercase;font-size: 15px;color:blue;";
		} else if(val.statut_livreur == '<%=STATUT_LIVREUR_COMMANDE.LIVRE%>'){
			libStatut = '<%=STATUT_LIVREUR_COMMANDE.LIVRE.getLibelle() %>';
			style = "font-weight: bold;text-transform: uppercase;font-size: 15px;color:green;";
		} else{
			libStatut = '<%=STATUT_LIVREUR_COMMANDE.EN_ATTENTE.getLibelle() %>';
			style = "font-weight: bold;text-transform: uppercase;font-size: 15px;color:orange;";
		}
	} else{
		if(val.statut_livreur == '<%=STATUT_LIVREUR_COMMANDE.EN_ROUTE%>'){
			libStatut = 'Marquer <%=STATUT_LIVREUR_COMMANDE.LIVRE.getLibelle() %>';
			params = 'statut=<%=STATUT_LIVREUR_COMMANDE.LIVRE%>';
			style = "width: 128px;height: 28px;font-size: 12px;text-transform: uppercase;background-color:green !important;border:1px solid red;";
		} else{
			libStatut = 'Marquer <%=STATUT_LIVREUR_COMMANDE.EN_ROUTE.getLibelle()%>';
			params = 'statut=<%=STATUT_LIVREUR_COMMANDE.EN_ROUTE %>';
			style = "width: 128px;height: 28px;font-size: 12px;text-transform: uppercase;background-color:orange !important;border:1px solid black;";
		}	
	}
		
	params = params + "&cmd="+val.id;
	
	data += `<div class="col-md-12 col-sm-12 col-xs-12" style="margin-bottom: -24px;" id="card_`+val.id+`">`+
				`<div class="widget radius-bordered">`+
					`<div class="widget-header">`+
					`<span class="widget-caption" style="font-size: 17px;font-weight: bold !important;">`+val.ref_commande+`</span>`+
					`<div class="widget-buttons buttons-bordered" style="width: 45%;text-align:right;">`;
					
			if(tp == 'A'){		
				data +=
					`<a href="javascript:" class="btn btn-blue btn-xs" onComplete="loadCommandeAjax('`+tp+`', `+val.id+`);" wact="<%=EncryptionUtil.encrypt("caisse.livreurMobile.changeStatutCommande") %>" params="`+params+`" targetDiv="XXXX" style="`+style+`">`+libStatut+`</a>`
				;
			} else{
				data += `<span style="`+style+`">`+libStatut+`</span>`;
			}
			
			data += `</div>`+
			`</div>`+
			`<div class="widget-body">`+
				`<a href="javascript:" wact="<%=EncryptionUtil.encrypt("caisse.livreurMobile.getDetailCmd") %>" params="cmd=`+val.id+`" targetdiv="generic_modal_body" data-toggle="modal" data-target="#generic_modal">`+
				`<div style="font-size: 15px;color: #673ab7;">`+formatDate(val.date_vente)+`</div>`;
	if(val.opc_client != null){
		data += `<div class="form-group" style="margin-bottom:0px; margin-left: 0px">`+
					`<div class="col" style="font-weight: 700">`+
					`<i style="color:#b37c8f;margin-right: 6px;" class="fa fa-user"></i>`+
					getValOrEmpty(val.opc_client.nom)+' '+getValOrEmpty(val.opc_client.prenom) +
					`</div>`+
				`</div>`+
				`<div class="form-group" style="margin-bottom:0px; margin-left: 0px">`+	
					`<div class="col" style="font-weight: 700"><i style="color:#b37c8f;margin-right: 5px;" class="fa fa-phone-square"></i><a href="tel:`+ getValOrEmpty(val.opc_client.telephone) + `">`+
					getValOrEmpty(val.opc_client.telephone) +
					`</a></div>`+
				`</div>`+
				`<div class="form-group" style="margin-bottom:0px; margin-left: 0px">`+	
				`<div class="col" style="font-weight: 700"><i style="color:#b37c8f;margin-right: 5px;" class="fa fa-whatsapp"></i><a href="https://wa.me/212`+ getValOrEmpty(val.opc_client.telephone) + `?text=Send20%a20%quote">`+
					getValOrEmpty(val.opc_client.telephone) +
					`</a></div>`+
				`</div>`+
				
				`<div class="form-group" style="margin-bottom:0px; margin-left: 0px">`+	
				`<div class="col" style="font-weight: 700">`+
				`<i style="color:#b37c8f;margin-right: 7px;" class="fa fa fa-map-marker"></i>`+
				`<a href="http://maps.google.com/?q=`+ getValOrEmpty(val.opc_client.adresse_rue) + `">`+
				getValOrEmpty(val.opc_client.adresse_rue) +
					`</a></div>`+
				`</div>`
				
				/* `<div class="form-group" style="margin-bottom:0px; margin-left: 0px">`+
					`<i style="color:#b37c8f;float:left;" class="fa fa-map-marker"></i>`+
					`<div class="col-xs-9" style="font-weight: 700">`+
					getValOrEmpty(val.opc_client.adresse_rue) + ' - ' + getValOrEmpty(val.opc_client.adresse_compl) +
					`</div>`+
				`</div>` */
				
				;
			}
	
		data += `<div style="display:flex; margin-bottom: 5px">`+
					`<div style="width: 50%;font-weight: bold;">`+getValOrEmpty(val.mode_paiement)+`</div>`+
					`<div style="position: absolute;font-weight: bold;color: black;font-size: 21px;right: 19px;top: 60px;">`+val.mtt_commande+`Dhs</div>`+
				`</div>`+
			`</a>`+
		`</div>`+
	`</div>`+
`</div>`;

	if($("#card_"+val.id).length == 0){
		$("#div_cmd").append(data);	
	} else{
		$("#card_"+val.id).replaceWith(data);
	}
}

function restaureDate(val){
	var key = "cmd_"+val.id;

	var dataArray = "\{\"cmd_ref\""+" :"+ "\""+getValOrEmpty(val.ref_commande)+"\","  
	+"\"client_nom\"" +" :"+ "\""+getValOrEmpty(val.opc_client.nom)+"\","
	+"\"client_address\"" +" :"+ "\""+getValOrEmpty(val.opc_client.adresse_rue)+"\","
	+"\"cmd_type\"" +" :"+ "\""+getValOrEmpty(val.mode_paiement)+"\","
	+"\"cmd_date_vente\"" +" :"+ "\""+getValOrEmpty(formatDate(val.date_vente))+"\","
	+"\"client_telephone\"" +" :"+ "\""+getValOrEmpty(val.opc_client.telephone)+"\","
	+"\"cmd_montant\"" +" :"+ "\""+getValOrEmpty(val.mtt_commande)+"\""
	+"}";
	
	saveOffLineData(key, dataArray);
}

function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear(),
    	 hour = d.getHours();
   	 minute = d.getMinutes();
   	 
    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;
    if (hour < 10) hour = '0' + hour;
    if (minute < 10) minute = '0' + minute;
    
    var dt = [day, month, year].join('/')+' '+[hour, minute].join(':');
    
    return dt;
}

var isAndroid = /(android)/i.test(navigator.userAgent);

function saveOffLineData(key,val){
	if(isAndroid){
		Android.saveInSharedPref(key,val);
	}
}
 	
function sendData(){
	var userAgent = navigator.userAgent || navigator.vendor || window.opera;
 	if (/android/i.test(userAgent)) {
		Android.sendData('<%="livreur_"+livreurId+"|true"%>');
    }  
}
</script>

<div class="row" style="padding: 7px 0px 0px 0px; overflow-y: initial;overflow-x: hidden;height: 130vh;background-color: white;" id="div_cmd">

</div>
 <div id="demo"></div> 

