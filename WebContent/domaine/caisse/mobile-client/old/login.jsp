<!DOCTYPE html>
<html>
<%@page import="org.apache.commons.lang3.ObjectUtils.Null"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="appli.model.domaine.administration.service.IUserService"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.beanContext.EtablissementPersistant"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.administration.service.IParametrageService"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%
boolean isMobileEnv = ContextAppli.SRC_CMD.MOB_ENV.toString().equals(ControllerUtil.getUserAttribute("SRC_URL", request));
%>
<head>
    <title>Caisse Manager</title>
    
    <jsp:include page="/WEB-INF/fragment/header-resources.jsp"></jsp:include>
	<script src="resources/framework/js/jquery.maskedinput-1.4.1.js"></script>
	<script src="resources/framework/js/mobile_util.js?v=1.0" type="text/javascript"></script>
    <script src="resources/framework/js/qrcode.min.js"></script>
    
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
%>

 <script type="text/javascript">
    
$(document).ready(function() {
	addTokenParams();
	
	$("#login, #password").keypress(function(e){
		if(e.which == 13) {
			$("#login_lnk").trigger("click");
	    }
	});
	$("#login_lnk").click(function(){
    	writeLocalStorage("caisse_ref", $("#caisse_ref").val());
    	writeLocalStorage("caisse_env", $("#env").val());
    	var login = $("#login").val();
    	var pw = $("#pass").val();
    	
    	sendData(login, pw);
	});

	makeCode();	
	
	<%if(ControllerUtil.getUserAttribute("MSG_CPT_CREATE", request) != null){ %>
		setTimeout(() => {
			$("#div_pt_succes").hide("1000");	
		}, 8000);
	<%}%>
	
	<%if(!isMobileEnv){%>
	if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
		setTimeout(() => {
			$("#mob_app_div").show();			
		}, 100);
	}
	<%} %>
	
});	

function makeCode() {	
	var qrCode = new QRCode(document.getElementById("qrcodeEts"), {
		width : 100,
		height : 100
	});
	
	var elText = "ETS-<%=EncryptionUtil.encrypt(StrimUtil.getGlobalConfigPropertie("instance.url")+"|"+ContextAppli.getEtablissementBean().getToken()+"|mob-client")%>";
	qrCode.makeCode(elText);
}
function setAndroidLoginPw(login, pw) {
    $("#login_lnk").attr("params", $("#login_lnk").attr("params")+"&isA=1&log="+login+"&pw="+pw);
    $("#login_lnk").trigger("click");
}

function setAndroidToken(token){
	setMobileToken(token);
}

function sendData(login, pw){
	if(typeof Android !== 'undefined' && Android){
		Android.sendData("login_"+login+"|"+pw);
	}
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

 <div class="login-container animated fadeInDown">
    <std:form name="login-form">
    	<input type="hidden" name="env" id="env">
        <div class="loginbox bg-white" style="width: 96% !important;margin-left:2%;border-radius: 20px;box-shadow: 15px 6px 42px 0px #6e5240;opacity: 95%;">
	     	<div class="header">
                <div class="row">
                  <div class="col-md-12 col-lg-12" style="text-align: center;">
                    <%if(ContextAppli.getEtablissementBean() != null){ %>
					        	<div style="text-align: center;">
				            <%
				            EtablissementPersistant etsBean = ContextGloabalAppli.getEtablissementBean();
				            if(etsBean == null){
				            	etsBean = new EtablissementPersistant();
				            }
				            if(etsBean.getId() != null){
				                 Map<String, byte[]> imagep = ServiceUtil.getBusinessBean(IUserService.class).getDataImage(etsBean.getId(), "restau");
				                 if(imagep.size() > 0){
				                %>
										<img src="data:image/jpeg;base64,<%=FileUtil.getByte64(imagep.entrySet().iterator().next().getValue())%>" 
											alt="Caisse manager" style="height: 120px;margin-top: -12px;border-radius: 22px;" />                        
				                 <%
				                  } 
				            }
				            %>
				            <span style="font-size: 20px;float: left;width: 100%;">
				    			<%=ContextAppli.getEtablissementBean().getRaison_sociale().toUpperCase() %>
				    		</span>
                    <%}
				    %>
				    
				    <p style="position: fixed;bottom: 6px;margin-right: 34%;
				        position: absolute;
					    top: 6px;
					    left: 17px;
					    margin-right: 34%;">
				    	<img src="resources/<%=StrimUtil.getGlobalConfigPropertie("context.soft") %>/img/logo_caisse_red.png?v=1.7" alt="Logo" style="width: 110px;">
				    </p>
				    <span style="position: absolute;
    top: 47px;
    left: 22px;
    font-size: 8px;">[V <%=StringUtil.getValueOrEmpty(restauP.getVersion_soft()) %>]</span>
                  </div>
                  </div>
                </div>
            </div>
            
            <div class="loginbox-or" style="margin-top: 20px;">
                <div class="or-line" style="left: 7px;right: 7px"></div>
                <div class="or" style="width: 50%;margin-top: -10px;left: 26%;" id="div_env">AUTHENTIFICATION</div>
            </div>
            
            
            <%if(ControllerUtil.getUserAttribute("MSG_CPT_CREATE", request) != null){ %>
            	<div id="div_pt_succes" class="alert alert-success fade in">
	                   <button class="close" data-dismiss="alert">
	                       ×
	                   </button>
	                   <i class="fa-fw fa fa-check"></i>
	                   <strong>Compte crée avec succès</strong> <%=ControllerUtil.getUserAttribute("MSG_CPT_CREATE", request) %>.
	               </div>
            <%} %>
            
            <div id="login_div">  
	            <div class="loginbox-textbox" style="margin-top: -16px;">
	            	<span class="input-icon icon-right">
		                <std:text  name="log.login" id="login" classStyle="form-control auth" type="string" mask="99-99-99-99-99" placeholder="Téléphone" />
		                <i class="fa fa-user darkorange"></i>
		            </span>    
	            </div>
	            <div class="loginbox-textbox">
	            	<span class="input-icon icon-right">
		                <input type="password" class="form-control auth" name="log.password" id="pass" placeholder="Mot de passe">
		                 <i class="glyphicon glyphicon-lock maroon"></i>
	                </span>
	                <a href="javascript:" onclick="$('#account_lose_div').toggle('1000');" style="font-size: 12px;text-decoration: underline;color: #d73d32;">Mot de passe oublié?</a>  
	            </div>
	            <div id="account_lose_div" class="row" style="margin-left: 40px;margin-right: 15px;display: none;">
					<div class="row">
						<div class="form-group">
							<std:link style="background-color: #d2d2d2;" value="Renvoyer par mail" icon="menu-icon fa fa-envelope-o" />
							<span style="font-size: 10px;color: fuchsia;width: 100%;float: left;">Vous recevrez dans le mail saisi lors de la création du compte.</span>
						</div>
					</div>
				</div>
	            <br>
	            <div class="loginbox-submit">
	                <std:link id="login_lnk" params="isSub=1&mnt=log" action="caisse.clientMobile.login" classStyle="btn btn-primary btn-block" value="CONNEXION" />
	            </div>
	            
	            <div class="loginbox-or" style="margin-top: 10px;">
                	<div class="or-line" style="left: 7px;right: 7px"></div>
                	<div class="or" style="width: 50%;left: 26%;" id="div_env">OU</div>
            	</div>
	            <div class="form-group" style="text-align: center;">
					<a href="javascript:" style="font-size: 15px;text-decoration: underline;color: #d73d32;" onclick="$('#login_div').hide(100);$('#account_div').show(100);">Créer mon compte</a>
				</div>
				
	<hr>			
	<div class="row">
    	<div class="col-md-6 col-sm-6 col-xs-6"   id="mob_app_div" style="display: none;text-align: center;">
				<a title="Télécharger l'application" 
							style="color: black;width: 100%;float: left;line-height: 54px;font-size: 15px;" 
							href="front?w_f_act=<%=EncryptionUtil.encrypt("caisse.clientMobile.setEtsIpToken")%>&skipF=1&store=AND&jtn=<%=ContextAppli.getEtablissementBean().getToken()%>"> 
							<img style="    height: 41px;
							    width: 150px;
							    margin-bottom: 1px;
							    padding-left: 16px;" src="resources/framework/img/android-ps.png" alt="Android" />
					</a>		    
		         <a title="Télécharger l'application" 
		         	style="color: black;width: 100%;float: left;line-height: 54px;" 
		         	href="front?w_f_act=<%=EncryptionUtil.encrypt("caisse.clientMobile.setEtsIpToken")%>&skipF=1&store=APP&jtn=<%=ContextAppli.getEtablissementBean().getToken()%>"> 
					<img style="height: 41px;
							    width: 150px;
							    margin-bottom: 1px;
							    padding-left: 16px;" src="resources/framework/img/ios-ps.jpg" alt="Apple" />
				</a>	
    	</div>
		<div class="col-md-6 col-sm-6 col-xs-6" style="text-align: center;">
			    <div id="qrcodeEts" style="width: 110px;
				    height: 110px;padding-top: 5px;
				    margin-left: 30px;float:left;">
			    </div>
  		</div>
  	</div>	
  </div>
	         
	         <!-- *************************************** CPT ***************************************** -->
			<div id="account_div" class="row" style="margin-left: 15px;margin-right: 15px;display: none;">
				<div class="form-group" style="margin-left: 13px;">
					<a href="javascript:" style="font-size: 15px;
						font-weight: bold;
						text-decoration: underline;
						color: #d73d32;
						position: absolute;
						right: 19px;
    					top: 19px;" onclick="$('#account_div').hide(100);$('#login_div').show(100);">
    						<i class="fa fa-angle-left"></i> RETOUR
    				</a>
				</div>
				<div class="row">
					<div class="col-md-12">
						<jsp:include page="compte_include.jsp" />
					</div>
				</div>	
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<div class="form-group" style="text-align: center;margin-top: 10px;">
							<std:button actionGroup="C" style="width: 200px;" classStyle="btn btn-primary btn-block" params="isSub=1&mnt=acc&skipF=1" action="caisse.clientMobile.merge_compte" icon="fa-save" value="CRÉER MON COMPTE" />
						</div>
					</div>
				</div>			
			</div>	   
        
        </div>
      </std:form>  
    </div>

<div id="spinner" class="spinner" style="display: none;"> 
	<img id="img-spinner" src="resources/framework/img/ajax-loader2.svg" alt="Loading" />
</div>

    <!--Basic Scripts-->
    <script src="resources/framework/js/jquery-2.1.1.min.js"></script>
	<script src="resources/framework/js/jquery.cookie.js"></script> 
    <script src="resources/framework/js/util.js?v=1.4<%=StrimUtil.getGlobalConfigPropertie("version.resources")%>"></script>
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