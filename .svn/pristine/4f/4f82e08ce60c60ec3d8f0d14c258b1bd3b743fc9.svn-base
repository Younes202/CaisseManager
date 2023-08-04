<html>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

<head>
    <title>Caisse Manager</title>
    
    <jsp:include page="/WEB-INF/fragment/header-resources.jsp"></jsp:include>
	<script src="resources/framework/js/mobile_util.js?v=1.0" type="text/javascript"></script>
	<script src="resources/framework/js/html5-qrcode.min.js"></script>
		
	<style>
		#fullscreen-toggler{
			position: absolute;
		    right: 0px;
		    top: 0px;
		    z-index: 10000;
		    box-shadow: -1px 2px 10px rgba(7, 14, 8, 6.5) ;
		}
		.notyet{
			color: #acacac !important;
		    background-color: gray !important;
		}
		#qr-canvas {
		  margin: auto;
		  width: calc(100% - 20px);
		  max-width: 400px;
		}
		#btn-scan-qr {
		  cursor: pointer;
		}
		
		#btn-scan-qr img {
		  height: 10em;
		  padding: 15px;
		  margin: 15px;
		  background: white;
		}
		
		#qr-result {
		  font-size: 1.2em;
		  margin: 20px auto;
		  padding: 20px;
		  max-width: 700px;
		  background-color: white;
		}
		.html5-qrcode-element{
		    border-radius: 7px;
		    background-color: #a6edff;
		    padding: 6px;
		    float: inherit;
		    margin: 12px;
		    border: 1px solid black;
		}
    </style>
    
    
 <script type="text/javascript">
	function setAndroidToken(token){
		setMobileToken(token);
	}
</script>
</head>
<!--Head Ends-->


<!--Body-->
<body>
<div style="background-image: url(resources/restau/img/restaurant-691377_1920.jpg);position: absolute;width: 100%;height: 100%;top: 0px;background-size: 100%;"></div>
		
 <div class="login-container animated fadeInDown" style="border: 1px solid black;
    background-color: #727272;
    padding: 16px;
    border-radius: 16px;">
    <std:form name="login-form">
    	<input type="hidden" name="env" id="env">
        <div class="loginbox bg-white" style="width: 96% !important;margin-left:2%;border-radius: 20px;box-shadow: 15px 6px 42px 0px #6e5240;opacity: 95%;">
	     	<div class="header">
                <div class="row">
                  <div class="col-md-12 col-lg-12" style="text-align: center;opacity: 0.9;">
					    <img src="resources/<%=StrimUtil.getGlobalConfigPropertie("context.soft") %>/img/logo_caisse_red.png?v=1.7" alt="Logo" style="width: 200px;">
                  </div>
                  </div>
                </div>
            </div>
            <br>
            <!-- Scanner -->
             <div id="container" style="text-align: center;">
             	<%
             	boolean isMobCli = "mobile-client".equals(request.getSession(true).getAttribute("ENV_MOBILE"));
             	if(isMobCli){ %>
			    	<h2 style="color: white;">Scannez le QR code de l'établissement</h2>
			    <%} else{ %>
			    	<h2 style="color: white;">Scannez le QR code de l'application</h2>
			    <%} %>
					<div id="qr-reader" style="width:350px"></div>
					
				<%if(isMobCli){ %>
					<h2 style="color: white;">Ou saisissez le code l'établissement</h2>
					<input type="text" name="code_ets_mob" style="width: 172px;height: 35px;border-radius: 5px !important;" />
					<std:button style="background-color: #000;color: white;height: 34px;margin-top: -6px;" params="isSub=1&skipF=1" action="caisse.clientMobile.addEtsClient" value="Ajouter"/>
				<%} %>	
			</div>
				
			<std:link params="skipF=1" action="caisse.clientMobile.addEtsClient" id="qr_sub_lnk" style="display:none;"/>
			<std:hidden id="qr" name="qr" />
      </std:form>  
    </div>

	<div id="spinner" class="spinner" style="display: none;"> 
		<img id="img-spinner" src="resources/framework/img/ajax-loader2.svg" alt="Loading" />
	</div>
     
     <script>
		var lastResult, countResults = 0;
		function onScanSuccess(decodedText, decodedResult) {
		    if (decodedText !== lastResult) {
		        ++countResults;
		        lastResult = decodedText;
		        if(!decodedText.startsWith('APP') && !decodedText.startsWith('ETS')){
		    		alertify.error("Qr code non autorisé.");
		    		return;
		    	}
		        $("#qr").val(decodedText);
		        $("#qr_sub_lnk").trigger("click");
		    }
		}

		var html5QrcodeScanner = new Html5QrcodeScanner("qr-reader", { fps: 10, qrbox: 250 });
		html5QrcodeScanner.render(onScanSuccess);
	</script>
		
    <!--Basic Scripts-->
    <script src="resources/framework/js/jquery-2.1.1.min.js"></script>
	<script src="resources/framework/js/jquery.cookie.js"></script> 
    <script src="resources/framework/js/util.js?v=1.3<%=StrimUtil.getGlobalConfigPropertie("version.resources")%>"></script>
	<script src="resources/framework/js/jquery.validate.min.js"></script>
    
    <script src="resources/framework/js/bootstrap.min.js"></script>
    <script src="resources/framework/js/slimscroll/jquery.slimscroll.min.js"></script>

    <!--Beyond Scripts-->
    <script src="resources/framework/js/beyond.js"></script>
	<script type="text/javascript" src="resources/framework/js/alertify.min.js"></script>
	<script src="resources/framework/js/bootbox/bootbox.js"></script>
	<script src="resources/framework/js/select2/select2.full.min.js"></script>
		
</body>
</html>