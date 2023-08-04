<!DOCTYPE html>
<%@page import="java.util.Iterator"%>
<%@page import="appli.model.domaine.administration.service.IParametrageService"%>
<%@page import="framework.model.common.service.MessageService"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="appli.controller.domaine.caisse.ContextAppliCaisse"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.beanContext.EtablissementPersistant"%>
<%@page import="appli.model.domaine.stock.service.IMouvementService"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.net.InetAddress"%>

<html style="overflow: hidden;">
<head>
<meta charset="UTF-8">
<title>Afficheur caisse</title>

<style>
	#cmd_table {
		border-spacing: 0;
	    border-collapse: collapse;
	    text-align: left;
    	width: 100%;
    	font-size: 17px;
	}
	#cmd_table td {
	    vertical-align: middle;
	    border-bottom: 1px dashed #ddd;
	}
	#cmd_det_div{
		overflow-x: hidden;
		overflow-y: auto;
		margin-top: 20px; 
		box-shadow: -16px 16px 61px 9px #9E9E9E;
	    border-radius: 12px;
	    border: 1px solid #2196F3;
	    background-color: #fdffef;
	}
	
	.menu-style{
	
	}
	.famille-style{
 		background-color: #eadaad !important;
	}
	
   /* jssor slider loading skin spin css */
   .jssorl-009-spin img {
       animation-name: jssorl-009-spin;
       animation-duration: 1.6s;
       animation-iteration-count: infinite;
       animation-timing-function: linear;
   }

   @keyframes jssorl-009-spin {
       from {
           transform: rotate(0deg);
       }

       to {
           transform: rotate(360deg);
       }
   }


   .jssorb064 {position:absolute;}
   .jssorb064 .i {position:absolute;cursor:pointer;}
   .jssorb064 .i .b {fill:#000;fill-opacity:.5;stroke:#fff;stroke-width:400;stroke-miterlimit:10;stroke-opacity:0.5;}
   .jssorb064 .i:hover .b {fill-opacity:.8;}
   .jssorb064 .iav .b {fill:#ffe200;fill-opacity:1;stroke:#ffaa00;stroke-opacity:.7;stroke-width:2000;}
   .jssorb064 .iav:hover .b {fill-opacity:.6;}
   .jssorb064 .i.idn {opacity:.3;}

   .jssora051 {display:block;position:absolute;cursor:pointer;}
   .jssora051 .a {fill:none;stroke:#fff;stroke-width:360;stroke-miterlimit:10;}
   .jssora051:hover {opacity:.8;}
   .jssora051.jssora051dn {opacity:.5;}
   .jssora051.jssora051ds {opacity:.3;pointer-events:none;}
</style>

<jsp:include page="/WEB-INF/fragment/header-resources.jsp"></jsp:include>
<script src="resources/caisse/js/afficheur/jssor.slider.min.js" type="text/javascript"></script>

<%

IArticleService service = ServiceUtil.getBusinessBean(IArticleService.class);
IParametrageService paramSrv= ServiceUtil.getBusinessBean(IParametrageService.class);
EtablissementPersistant restauBean = paramSrv.getEtsOneOrCodeAuth();
MessageService.getGlobalMap().put("GLOBAL_ETABLISSEMENT", restauBean);

Map<String, byte[]> dataimg = service.getDataImage(restauBean.getId(), "param");
%>

<script type="text/javascript">
$(document).ready(function (){
	<%-- Plein &eacute;cran et resize --%>
	$("#fullscreen-toggler").click(function(){
		setTimeout(function(){
			refreshSize();
		}, 1000);
	});
	<%if(dataimg != null && dataimg.size() > 0){ %>	
		jssor_1_slider_init();	
	<%}%>
	refreshSize();
	$("#slider").show();
	$("#cmd_div").hide();
	
	  // Empecher session out
	  setInterval(function() {
		  var url = 'front?w_uact=<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.work_init")%>';
		  callBackJobAjaxUrl(url, false);
	  }, 60000);
	});

	function refreshSize(){
		var widowHeight = $(window).height();
		$("#cmd_det_div").css("height", (widowHeight-30)+"px");
	}
	
	<%
	Long affId = ContextAppliCaisse.getCaisseBean().getId();
	String tempoVeille = ContextGloabalAppli.getGlobalConfig(affId, "SLIDE_TEMPO_VEILLE");
	if(StringUtil.isEmpty(tempoVeille)){
		tempoVeille = "30000";
	} else{
		tempoVeille = ""+(Integer.valueOf(tempoVeille)*1000);
	}
	
	String duree = ContextGloabalAppli.getGlobalConfig(affId, "SLIDE_DURATION");
	String effet = ContextGloabalAppli.getGlobalConfig(affId, "SLIDE_EFFET");
	
	Map<String, String> mapDataEffect = (Map<String, String>)request.getAttribute("mapDataEffect");
	Iterator itMap = mapDataEffect.keySet().iterator();
	effet = (effet != null ? mapDataEffect.get(effet.trim()) : mapDataEffect.get(itMap.next()));

	if(StringUtil.isEmpty(effet)){
		effet = mapDataEffect.get(itMap.next());
	}
	
	if(StringUtil.isEmpty(duree)){ 
		duree = "1";
	}
	String durationEnd = (effet != null ? effet.substring(effet.indexOf(",")+1) : "30");
	effet = "{$Duration:"+(Integer.valueOf(duree)*1000)+"," +durationEnd;
	%>

<%if(dataimg != null && dataimg.size() > 0){ %>	
	 jssor_1_slider_init = function() {
		 var jssor_1_SlideshowTransitions = [
                <%=effet%>              
         ];
		 var jssor_1_options = {
           $AutoPlay: 1,
           $SlideshowOptions: {
             $Class: $JssorSlideshowRunner$,
             $Transitions: jssor_1_SlideshowTransitions,
             $TransitionsOrder: 1
           },
           $ArrowNavigatorOptions: {
             $Class: $JssorArrowNavigator$
           },
           $BulletNavigatorOptions: {
             $Class: $JssorBulletNavigator$
           }
         };
		 
        /* var jssor_1_options = {
           $AutoPlay: 0,
           $Idle: 2000,
           $SlideEasing: $Jease$.$InOutSine,
           $DragOrientation: 3,
           $ArrowNavigatorOptions: {
             $Class: $JssorArrowNavigator$
           },
           $BulletNavigatorOptions: {
             $Class: $JssorBulletNavigator$
           }
         };*/

         var jssor_1_slider = new $JssorSlider$("jssor_1", jssor_1_options);

         //make sure to clear margin of the slider container element
         jssor_1_slider.$Elmt.style.margin = "";

         /*#region responsive code begin*/

         /*
             parameters to scale jssor slider to fill parent container

             MAX_WIDTH
                 prevent slider from scaling too wide
             MAX_HEIGHT
                 prevent slider from scaling too high, default value is original height
             MAX_BLEEDING
                 prevent slider from bleeding outside too much, default value is 1
                 0: contain mode, allow up to 0% to bleed outside, the slider will be all inside parent container
                 1: cover mode, allow up to 100% to bleed outside, the slider will cover full area of parent container
                 0.1: flex mode, allow up to 10% to bleed outside, this is better way to make full window slider, especially for mobile devices
         */

         var MAX_WIDTH = 3000;
         var MAX_HEIGHT = 3000;
         var MAX_BLEEDING = 1;

         function ScaleSlider() {
             var containerElement = jssor_1_slider.$Elmt.parentNode;
             var containerWidth = containerElement.clientWidth;

             if (containerWidth) {
                 var originalWidth = jssor_1_slider.$OriginalWidth();
                 var originalHeight = jssor_1_slider.$OriginalHeight();

                 var containerHeight = containerElement.clientHeight || originalHeight;

                 var expectedWidth = Math.min(MAX_WIDTH || containerWidth, containerWidth);
                 var expectedHeight = Math.min(MAX_HEIGHT || containerHeight, containerHeight);

                 //scale the slider to expected size
                 jssor_1_slider.$ScaleSize(expectedWidth, expectedHeight, MAX_BLEEDING);

                 //position slider at center in vertical orientation
                 jssor_1_slider.$Elmt.style.top = ((containerHeight - expectedHeight) / 2) + "px";

                 //position slider at center in horizontal orientation
                 jssor_1_slider.$Elmt.style.left = ((containerWidth - expectedWidth) / 2) + "px";
             }
             else {
                 window.setTimeout(ScaleSlider, 30);
             }
         }

         function OnOrientationChange() {
             ScaleSlider();
             window.setTimeout(ScaleSlider, 800);
         }

         ScaleSlider();

         $Jssor$.$AddEvent(window, "load", ScaleSlider);
         $Jssor$.$AddEvent(window, "resize", ScaleSlider);
         $Jssor$.$AddEvent(window, "orientationchange", OnOrientationChange);
         /*#endregion responsive code end*/
     };
     
<%}%>
    </script>
</head>

<body style="width: 100%; height: 100%;">

	<div style="position:relative;top:0;left:0;width:100%;height:100%;overflow:hidden;" id="slide_div">
	
<%if(dataimg != null && dataimg.size() > 0){ %>
		<div id="jssor_1" style="position:relative;margin:0 auto;top:0px;left:0px;width:1366px;height:768px;overflow:hidden;visibility:hidden;">
            <!-- Loading Screen -->
            <div data-u="loading" class="jssorl-009-spin" style="position:absolute;top:0px;left:0px;width:100%;height:100%;text-align:center;background-color:rgba(0,0,0,0.7);">
                <img style="margin-top:-19px;position:relative;top:50%;width:38px;height:38px;" src="resources/<%=StrimUtil.getGlobalConfigPropertie("context.soft") %>/img/loading2.svg" />
            </div>
            <div data-u="slides" style="cursor:default;position:relative;top:0px;left:0px;width:1366px;height:768px;overflow:hidden;">
                <%	
				for(String key : dataimg.keySet()){	%>
			        
			            <div data-p="267">
			                <img data-u="image" src="data:image/jpeg;base64,<%=FileUtil.getByte64(dataimg.get(key)) %>" />
			            </div>
			        
				<%}%>
            </div>
            <!-- Bullet Navigator -->
            <div data-u="navigator" class="jssorb064" style="position:absolute;bottom:12px;right:12px;" data-autocenter="1" data-scale="0.5" data-scale-bottom="0.75">
                <div data-u="prototype" class="i" style="width:16px;height:16px;">
                    <svg viewbox="0 0 16000 16000" style="position:absolute;top:0;left:0;width:100%;height:100%;">
                        <circle class="b" cx="8000" cy="8000" r="5800"></circle>
                    </svg>
                </div>
            </div>
            <!-- Arrow Navigator -->
            <div data-u="arrowleft" class="jssora051" style="width:55px;height:55px;top:0px;left:25px;" data-autocenter="2" data-scale="0.75" data-scale-left="0.75">
                <svg viewbox="0 0 16000 16000" style="position:absolute;top:0;left:0;width:100%;height:100%;">
                    <polyline class="a" points="11040,1920 4960,8000 11040,14080 "></polyline>
                </svg>
            </div>
            <div data-u="arrowright" class="jssora051" style="width:55px;height:55px;top:0px;right:25px;" data-autocenter="2" data-scale="0.75" data-scale-right="0.75">
                <svg viewbox="0 0 16000 16000" style="position:absolute;top:0;left:0;width:100%;height:100%;">
                    <polyline class="a" points="4960,1920 11040,8000 4960,14080 "></polyline>
                </svg>
            </div>
            <div data-interaction="user-commands" class="cmd-box" style="display:none;top:2px;left:auto;bottom:auto;right:2px;width:23px;height:69px;box-sizing:border-box;" data-scale=".2" data-scale-top=".5" data-scale-right=".5">
                <div data-command="jssor-getslider" class="cmd-btn" title="get this slider">X</div>
                <div data-command="jssor-qrcode" class="cmd-btn" title="QR code">T</div>
                <div data-command="jssor-share" class="cmd-btn" title="share">Y</div>
            </div>
        </div>
<%} %>        
	</div>
	<!-- FIN -->
	<!-- Content div -->
	<div class="row" style="text-align: center;width: 100%;" id="cmd_div">
		<h1 style="margin-top: -30%;
    font-size: 6em;
    font-weight: bold !important;
    text-align: center;">Bienvenue</h1>
	</div>
	<!-- Options div -->
	<div class="btn-group dropup" style="position: absolute;left: 0px;top: 90%;opacity:0.5;">
           <a class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
               <i class="fa fa-angle-down"></i>
           </a>
           <ul class="dropdown-menu">
               <li>
                   <a id="fullscreen-toggler" href="#" style="font-size: 20px;"> <i class="fa fa-external-link"></i> Plein &eacute;cran</a>
               </li>
               <li>
                   <a href="javascript:" onclick="location.reload();" style="color: blue;font-size: 20px;"><i class="fa fa-refresh"></i> Rafraichir &eacute;cran</a>
               </li>
               <li class="divider"></li>
               <li>
                   <a href="front?lmnu=lgo" style="color: red;font-size: 20px;" onclick="wsCloseConnection();submitAjaxForm('<%=EncryptionUtil.encrypt("commun.login.disconnect")%>', null, null, null);"><i class="fa fa-lock"></i> Se d&eacute;connecter</a>
               </li>
           </ul>
     </div>

	<script type="text/javascript">
		$(document).ready(function (){
			setTimeout(function(){
				wsSendMessage();
			}, 1000);
		});
	
		<%
		String adresseIp = request.getLocalAddr();
        if (adresseIp.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {// Si on est sur la m�me machine (local ost)
            InetAddress inetAddress = InetAddress.getLocalHost();
            adresseIp = inetAddress.getHostAddress();
        }
		String path = adresseIp+":"+request.getLocalPort() + request.getContextPath();
		%>
		
		var webSocket = new WebSocket("ws://<%=path%>/cmd-track");
		webSocket.onopen = function(message) {
			console.debug('Connected ...');
		};
		
		var tempo = <%=tempoVeille%>;
		// Tempo pour effacer l'�cran
		var timer = window.setTimeout(function(){
			$("#slide_div").show();
			$("#cmd_div").hide();
		}, tempo);
		
		webSocket.onmessage = function(message) {
			var widowHeight = $(window).height();
			var dataSended = jQuery.parseJSON(message.data);
			var valReour = dataSended.val;
			var infos = "";
			// DR:Delete row, V:Validation commande, T:Maj commande, X:Annulation commande
			
			if(valReour == 'X'){// Annulation commande
				$("#slide_div").show();
				$("#cmd_div").hide();
				return;
			} else {
				// LOGO CLIENT MANAGER EN HAUT GAUCHE
				<%
            	IMouvementService mouvementService = (IMouvementService)ServiceUtil.getBusinessBean(IMouvementService.class);
            	Map<String, byte[]> imagep = mouvementService.getDataImage(ContextAppli.getEtablissementBean().getId(), "restau");
                if(imagep.size() > 0){%>
                infos = infos + '<div style="position: absolute;">' +	
                	'<img src="data:image/jpeg;base64,<%=FileUtil.getByte64(imagep.entrySet().iterator().next().getValue()) %>" alt="Caisse manager" style="height: 80px;margin-left: 20px;" />'+                     
                	'</div>';
             <% }%>
             
				if(valReour == 'V'){	// Recapitulatif commande si paiement
					// LOGO CAISSE MANAGER EN BAS DROIT
					infos = infos + '<div style="position: absolute;right: 13px;top: 96%;width:100%;text-align: right;">' +
						'<img alt="Caisse manager" src="resources/restau/img/logo_caisse.png" style="height: 30px;" />' +
					'</div>';
					
					infos = infos + '<div style="width: 100%;margin-top: 10%;">' +
					'<table align="center"><tr><td><span style="font-size: 50px;font-weight: bold;">TOTAL : </span></td>' +
					'<td><span style="font-size: 70px;font-weight: bold;padding-left: 20px;">'+dataSended.total+'</span>' +
					'<span style="font-size: 50px;color:blue;">Dhs</span></td></tr>' +
					'<tr><td colspan="2"><hr></td></tr>' +
					'<tr><td><span style="font-size: 60px;font-style: italic;">A RENDRE :</span></td>' +
					'<td style="background-color: #eeeeee;border-radius: 20px;box-shadow: 16px 13px 45px -9px #262626;"><span style="font-size: 60px;font-weight:bold;color:#53a93f;">'+dataSended.a_rendre+'</span>' +
					'<span style="font-size: 50px;color:blue;">Dhs</span></td></tr></table>' +
					
					'<span style="width: 100%;float: left;color:black;font-size: 26px;margin-top: 5%;">Commande : <b style="color:#fb6e52;font-size: 30px;">'+dataSended.ref_cmd+'</b></span>';
					
					 <%String PIED_TEXT = ContextGloabalAppli.getGlobalConfig(ContextAppli.PARAM_APPLI_ENUM.TEXT_PIED_TICKET.toString());
		                if (StringUtil.isNotEmpty(PIED_TEXT)) {%>
		               		infos = infos + '<span style="font-size: 26px;margin-top: 10%;width: 100%;float: left;color:#a0d468;font-style: italic;"><%=PIED_TEXT %></span>';
		                <%}	%>
		                infos = infos + '</div>';   
				} else{
					// LOGO CAISSE MANAGER EN BAS DROIT
					infos = infos + '<div style="position: absolute;right: 52%;top: 96%;width:100%;text-align: right;">' +
						'<img alt="Caisse manager" src="resources/restau/img/logo_caisse.png" style="height: 30px;margin-top: 0px;" />' +
					'</div>';
					
					infos = infos + '<div style="width:50%;float:left;margin-top:10%;" id="cmd_left_div">' +
							'<div style="text-align:center;width:100%;">' +
								'<span style="font-size: 4em;font-weight: bold;padding-left: 20px;transform: scale(1.5, 3.5);display: inline-block;">'+dataSended.total+
								'<span style="color:blue;"> Dhs</span></span>'+
							'</div>' +
							'<hr style="width:100%;float:left;">';
							
					if(valReour == 'T'){// Si article selectionn�
						infos = infos + '<div style="text-align:center;margin-top: 10%;width:100%;">' +
							'<span style="font-size:4em;">'+dataSended.qte+'</span>' +
							'<span style="color:blue;font-size:2em;">x</span>' +
							' <span style="font-size: 4em;"><b style="color:#fb6e52;">'+dataSended.art+'</b></span>';
							if(dataSended.prix!=''){
								infos = infos + '<br>' +
								'<span style="font-size:3em;padding-left:10px;">('+dataSended.prix+'</span>' +
								'<span style="color:blue;font-size:2em;"> Dhs</span><span style="font-size:3em;">)</span>';
							}
						infos = infos + '</div>';			
					}
					
					infos = infos + '</div>';
				
				
					// ************************** DETAIL COMMANDE ****************************************
					var dettailCmd = dataSended.detail;
					var dettailOffre = dataSended.offre;
					//
					infos = infos + '<div style="width:50%;" id="cmd_det_div">' +
								'<table id="cmd_table">'+
								'<tr style="background-color: #2dc3e8;"><th>ARTICLE</th><th style="text-align:right;">QUANTITE</th><th style="text-align:right;">PRIX</th></tr>';
					
					for(var i=0; i<dettailCmd.length; i++){
						var detail = dettailCmd[i];
						
						var type = detail.type;
						var libCmd = detail.art;
						
						
				       var isSelected = false;
				       if(dataSended.art != ''){
				    	   if(dataSended.curr_path == detail.curr_path){
				    		   isSelected = true;
				       		}
				       }
						
						 if(type == null || type == ''){
					           type = "XXX";
					       }
					       var qte = (detail.qte != null ? detail.qte : null);
					       var mttTotal = "";
					       if(detail.is_offert=='true'){
					    	   mttTotal = "<i class='fa fa-gift'style='color:green;font-size: 17px;cursor:pointer;' data-container='body' data-titleclass='bordered-blue' data-class='' data-toggle='popover' data-placement='top' data-title='Article offert' data-content='Prix : "+detail.prix+"' data-original-title='' title=''></i>";
					       } else if(detail.prix != 0){
					    	   mttTotal = detail.prix;
					       }
					       
					       var styleTd = "";
					       var classType = "";
				    	   if(type=='ART'){
				    		   classType = "ligne-fam-style";
				           } else if(type=='GROUPE_FAMILLE'){
				        	   classType = "famille-style";
				           } 
					       
					       if(detail.level != ''){
								var lev = detail.level ? detail.level : 1;					       
					    	   styleTd = styleTd + "padding-left:"+(6*parseInt(lev))+"px;";
					       }
					       var isArticle = (type=='ART');
		
					       infos = infos + '<tr class="'+classType+'" style="'+(isArticle?"color:blue;":"")+(isSelected?"background-color:#ffca06;":"")+'" '+(isSelected?" isSel=1":"")+'>'+
					       		'<td style="'+styleTd+(isArticle?"line-height: 23px;":"line-height: 15px;font-size:13px;")+'">'+(isArticle?"<i class='fa-fw fa fa-check'></i>":"")+libCmd+'</td>'+
					       		'<td align="right">'+(isArticle ? qte:"")+'</td>'+
					       		'<td align="right" style="padding-right:5px;">'+(parseFloat(mttTotal)>0 ? mttTotal:'')+'</td>';
					       '</tr>';
					   }
					   infos = infos + '</table></div>';
				}
			}
			//
			$("#cmd_div").html(infos).show();
			$("#slide_div").hide();
			
			$("#cmd_det_div").css("height", (widowHeight-30)+"px");
			
			// Selectionner la ligne
			var selectedTr = $('#cmd_table tr[isSel]');
			if(selectedTr && selectedTr.length > 0){
				selectedTr.get(0).scrollIntoView();
			}
			
			window.clearTimeout(timer);
			// Tempo pour effacer l'�cran
			timer = window.setTimeout(function(){
				$("#slide_div").show();
				$("#cmd_div").hide();
			}, tempo);
		};
		webSocket.onclose = function(message) {
			console.debug("Disconnect ..."+message);
		};
		webSocket.onerror = function(message) {
			console.debug("Error ... : "+message);
		};
		
		<%
			Long afficheurId = MessageService.getGlobalMap().get("CURRENT_AFFICHEUR") != null ? 
					(Long)MessageService.getGlobalMap().get("CURRENT_AFFICHEUR") : affId;
		%>
		
		function wsSendMessage() {
			webSocket.send('<%=afficheurId %>');
		}
		function wsCloseConnection() {
			webSocket.close();
		}
   	</script>
    	<!-- #endregion Jssor Slider End -->
	  
	  <jsp:include page="/WEB-INF/fragment/footer-resources.jsp"></jsp:include>
</body>
</html>