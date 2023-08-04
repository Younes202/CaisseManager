<%@page import="framework.controller.ControllerUtil"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
boolean isConfirmReduce = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("SECURE_REDUCTION_CMD"));
%>

<script type="text/javascript">
	$(document).ready(function (){
		init_keyboard_events();
		
		var barcodeLockQte="";
		$(document).off('keydown').on('keydown', function (e) {
	        var code = (e.keyCode ? e.keyCode : e.which);
	        var sourceEvent = $(e.target).prop('nodeName');
	        var isInput = (sourceEvent == 'INPUT') ? true : false;
	        
	        //
	        if(!isInput && code==13 && $.trim(barcodeLockQte) != ''){
	        	barcodeLockQte = barcodeLockQte.substring(barcodeLockQte.length-10);
	        	
	        	if(barcodeLockQte.length==10){
	        		submitAjaxForm('<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.offrirLigneCommandeRed")%>', '<%=request.getAttribute("params")%>&qte.tkn='+barcodeLockQte, $("#data-formOffre"), $("#trigSubQteBtn"));
	        		return false;
	        	}
	        	barcodeLockQte="";
	        } else{
	        	barcodeLockQte = barcodeLockQte + String.fromCharCode(code);
	        }
	    });
	});
</script>	
	
<std:form name="data-formOffre">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Réduction</span>
			<img src="resources/framework/img/badge_scanner.png" style="width: 20px;margin-top: 8px;margin-right: 5px;" title="Lecteur badge utilisable sur cet écran">
			
			<div class="widget-buttons buttons-bordered" style="margin-bottom: 10px;">
        		<i class="fa fa-keyboard-o" style="font-size: 20px;"></i>         
        		<label>
                 <input class="checkbox-slider toggle colored-blue" type="checkbox" id="keyboard-activator" style="display: none;">
                 <span class="text"></span>
             </label>
        	</div>
        	
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal" style="margin-top: 2px;margin-right: 5px;">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row" style="margin-left: 0px;margin-right: 0px;">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Montant" />
					<div class="col-md-3">
						<std:text name="mtt_reduce" onChange="$('#taux_reduce').val('');" forceWriten="true" type="decimal" placeholder="Montant" maxlength="15" style="width:80%;float:left;" />
						<span style="line-height: 33px;padding-left: 3px;">dhs</span>
					</div>
					<std:label classStyle="control-label col-md-3" value="OU taux" />
					<div class="col-md-3">
						<std:text name="taux_reduce" onChange="$('#mtt_reduce').val('');" forceWriten="true" type="decimal" placeholder="Taux" maxlength="15" style="width:80%;float:left;" /> 
						<span style="line-height: 33px;padding-left: 3px;">%</span>
					</div>
				</div>
			</div>	
			
			<%if(isConfirmReduce){ %>
				<div class="row" style="margin-left: 0px;margin-right: 0px;display: none;" id="div_auth">
					<div class="col-md-12">
						<h3>Autorisation manager</h3>
					</div>
					<div class="col-md-12">
						<std:label classStyle="control-label col-md-4" value="Login" style="font-size: 19px;"/>&nbsp;
						<div class="col-md-8" style="margin-top: -15px;">
							<std:select forceWriten="true" name="unlockQte.login" type="long" style="width:100%;font-size: 25px;" data="${listUser }" key="id" labels="login" />
						</div>
					</div>
					<div class="col-md-12">
						<std:label classStyle="control-label col-md-4" value="Mot de passe" style="font-size: 19px;"/>&nbsp;
						<div class="col-md-8">
							<std:password forceWriten="true" name="unlockQte.password" placeholder="Mot de passe" type="string" style="width:140px;font-size: 18px;margin-top: -15px;" maxlength="80" />
						</div>
					</div>
				</div>
			<%} %>
			
			<hr>	
			<div class="row" style="text-align: center;">
				<div class="col-md-12">
					<std:button actionGroup="X" id="trigSubQteBtn" classStyle="btn btn-success" style="border-radius: 37px;height: 52px;font-size: 21px;" action="caisse-web.caisseWeb.offrirLigneCommandeRed" targetDiv="left-div" params="${params }" icon="fa-save" value="Valider" closeOnSubmit="true" />
				</div>
			</div>
		</div>
	</div>
</std:form>