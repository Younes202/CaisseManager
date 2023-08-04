<%@page import="framework.controller.ControllerUtil"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="appli.model.domaine.fidelite.persistant.CarteFideliteClientPersistant"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script type="text/javascript">
$(document).ready(function() {
	$("#login, #password").keypress(function(e){
		if(e.which == 13) {
			$("#log_cmd_cli").trigger("click");
	    }
	});
});

</script>


<!-- Page Header -->
<div>
	<div style="width:50%;float: right;">
	   <img src="resources/caisse/img/caisse-web-client/bar-code-scanner-155766_640.png" alt="terminal view" style="width: 281px;margin-top: 15px;margin-left: 12px;">
	</div>
	<div style="width:50%;margin-left: 9px;">
		<div class="login-container animated fadeInDown">
	        <div class="loginbox bg-white" style="width: 289px !important;">
	            <div class="loginbox-title">CONNEXION</div>
	            <div class="loginbox-textbox">
	            	<span class="input-icon icon-right">
		                <std:text name="login" classStyle="form-control" forceWriten="true" type="string" required="true" />
		                <i class="fa fa-user darkorange"></i>
		            </span>    
	            </div>
	            <div class="loginbox-textbox">
	            	<span class="input-icon icon-right">
		                <input type="password" class="form-control" name="password" id="password">
		                 <i class="glyphicon glyphicon-lock maroon"></i>
	                </span>  
	            </div>
	            <br>
	            <div class="loginbox-submit">
	                <a href="javascript:void(0)" targetDiv="paie-div" id="log_cmd_cli" wact="<%=EncryptionUtil.encrypt("caisse-web.caisseWebClient.loginCmd")%>" class="btn btn-primary btn-block">Connexion</a>
	            </div>
	        </div>
	      </div> 
	</div>
</div>
<div>
	<div style="width:50%;float: left;">
		<h1 style="font-weight: bold !important;font-size: 20px !important;color: black;text-align: center;">Authentifiez vous </h1>
	</div>
	<div style="width:50%;float: left;">
		<h1 style="font-weight: bold !important;font-size: 20px !important;color: black;text-align: center;">OU Scannez votre carte</h1>
	</div>	
</div>
<div style="height: 134px;">
	<button noVal='true' wact='<%=EncryptionUtil.encrypt("caisse-web.caisseWebClient.initPaiement") %>' style="margin-left:15px;margin-top: 20px;height: 50px;font-size: 20px;font-weight: bold;border-radius: 47px;" class="btn btn-danger" type="button" targetDiv="paie-div">IGNORER CETTE ETAPE</button>
	<button data-dismiss="modal" style="margin-left:170px;margin-top: 20px;height: 50px;font-size: 20px;font-weight: bold;border-radius: 47px;background-color: black !important;border-color: black !important;" class="btn btn-danger" type="button">ANNULER<i class="fa fa-close" style="margin-left: 9px;font-size: 22px"></i></button>
</div>	