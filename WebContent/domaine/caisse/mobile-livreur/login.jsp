<!DOCTYPE html>
<%@page import="appli.controller.domaine.administration.bean.UserBean"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.util.audit.ReplicationGenerationEventListener"%>
<%@page import="java.util.HashMap"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="java.net.InetAddress"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.beanContext.EtablissementPersistant"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.administration.service.IParametrageService"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

<head>
    <title>Caisse Manager</title>
    
    <jsp:include page="/WEB-INF/fragment/header-resources.jsp"></jsp:include>
	<script src="resources/framework/js/jquery.maskedinput-1.4.1.js"></script>
	 	<script src="resources/framework/js/mobile_util.js?v=1.0" type="text/javascript"></script>

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
		
    </style>
</head>
<!--Head Ends-->


<!--Body-->
<body>

<%
IParametrageService paramsService = ServiceUtil.getBusinessBean(IParametrageService.class);
EtablissementPersistant restauP = paramsService.getEtsOneOrCodeAuth();
String login = (String) request.getAttribute("login");
String pw = (String) request.getAttribute("password");
%>

 <script type="text/javascript">
    
$(document).ready(function() {
	addTokenParams();
	
	<%if(StringUtil.isNotEmpty(login) && StringUtil.isNotEmpty(pw)){%>
		setTimeout(function() {
			$('#div_attente').css("display", "flex");
			$('#login').val("<%=login%>");
			$('#password').val("<%=pw%>");
			
			$("#login_lnk").trigger("click");
		}, 10);
	<%}%>
	
	$("#login, #password").keypress(function(e){
		if(e.which == 13) {
			$("#login_lnk").trigger("click");
	    }
	});
	$("#login_lnk").click(function(){
    	writeLocalStorage("caisse_ref", $("#caisse_ref").val());
    	writeLocalStorage("caisse_env", $("#env").val());
	});
});	

// Appel depuis WebView
function setAndroidLoginPw(login, pw) {
    $("#login_lnk").attr("params", "login="+login+"&password="+pw);
}

function setAndroidToken(token){
	setMobileToken(token);
}

</script>
	
<%
	  boolean isImg = false;
	  if(restauP.getId() != null){
		  String path = "restau/fond/"+restauP.getId();
		  Map<String, byte[]> imagep = FileUtil.getListFilesByte(path);
	      if(imagep.size() > 0){ %>
			<div style="background-image: url(data:image/jpeg;base64,<%=FileUtil.getByte64(imagep.entrySet().iterator().next().getValue())%>);position: absolute;width: 100%;height: 100%;top: 0px;background-size: 100%;"></div>                        
	    <%
	    	isImg = true;
	     } 
	  }
	  if(!isImg){ %>
		<div style="background-image: url(resources/restau/img/restaurant-691377_1920.jpg);position: absolute;width: 100%;height: 100%;top: 0px;background-size: 100%;"></div>
	<%} %>

<div id="div_attente" style="display: none; position: absolute;
    						width: 100%; height: 100%; top: 0px; 
    						background: #fff; z-index: 9;">
	<div style="margin: auto;">
		<b style="font-size: 15px;">Veuillez patienter ... </b>
		<div style="text-align: center"><img id="img-spinner" src="resources/restau/img/loading2.svg" alt="Loading" width="100px" /></div>
	</div>
</div>
 <div class="login-container animated fadeInDown">
    <std:form name="login-form">
    	<input type="hidden" name="env" id="env">
        <div class="loginbox bg-white" style="width: 96% !important;margin-left:2%;border-radius: 20px;box-shadow: 15px 6px 42px 0px #6e5240;opacity: 88%;">
	     	<div class="header">
                <div class="row">
                  <div class="col-md-12 col-lg-12" style="text-align: center;">
                     <p><img src="resources/<%=StrimUtil.getGlobalConfigPropertie("context.soft") %>/img/logo_caisse_red.png?v=1.7" alt="Logo" style="width: 148px;margin-top: 15px;"></p>
                  		[V <%=StringUtil.getValueOrEmpty(restauP.getVersion_soft()) %>]
                  </div>
                </div>
            </div>
            <div class="loginbox-or" style="margin-top: 10px;">
                <div class="or-line" style="left: 7px;right: 7px"></div>
                <div class="or" style="width: 50%;left: 26%;" id="div_env">AUTHENTIFICATION</div>
            </div>
            
            <hr style="width: 100%;">            
            <div class="loginbox-textbox" style="margin-top: -16px;">
            	<span class="input-icon icon-right">
	                <std:text name="log.login" classStyle="form-control auth" type="string" required="true" />
	                <i class="fa fa-user darkorange"></i>
	            </span>    
            </div>
            <div class="loginbox-textbox">
            	<span class="input-icon icon-right">
	                <input type="password" class="form-control auth" name="log.password" id="log.password">
	                 <i class="glyphicon glyphicon-lock maroon"></i>
                </span>  
            </div>
            
            <br>
            
            <div class="loginbox-submit">
                <std:link id="login_lnk" action="caisse.livreurMobile.login" classStyle="btn btn-primary btn-block" value="Connexion" />
            </div>
            
        
      </std:form>  
    </div>


<div id="spinner" class="spinner" style="display: none;"> 
	<img id="img-spinner" src="resources/framework/img/ajax-loader2.svg" alt="Loading" />
</div>

    <!--Basic Scripts-->
    <script src="resources/framework/js/jquery-2.1.1.min.js"></script>
	<script src="resources/framework/js/jquery.cookie.js"></script> 
    <script src="resources/framework/js/util.js?v=1.2<%=StrimUtil.getGlobalConfigPropertie("version.resources")%>"></script>
	<script src="resources/framework/js/jquery.validate.min.js"></script>
    
    <script src="resources/framework/js/bootstrap.min.js"></script>
    <script src="resources/framework/js/slimscroll/jquery.slimscroll.min.js"></script>

    <!--Beyond Scripts-->
    <script src="resources/framework/js/beyond.js"></script>
	<script type="text/javascript" src="resources/framework/js/alertify.min.js"></script>
	<script src="resources/framework/js/bootbox/bootbox.js"></script>
	<script src="resources/framework/js/select2/select2.full.min.js"></script>
</body>
