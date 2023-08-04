<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%> 
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#generic_modal_body{
	width: 700px;
}
</style>

<script type="text/javascript">
	$(document).ready(function() {
		var barcode="";
	    $(document).keydown(function(e) {
	        var code = (e.keyCode ? e.keyCode : e.which);
	        var sourceEvent = $(e.target).prop('nodeName');
	        var isInput = (sourceEvent == 'INPUT') ? true : false;
	        //
	        if(!isInput && code==13 && $.trim(barcode) != ''){
	        	barcode = barcode.substring(barcode.length-10);
	        	if(barcode.length==10){
	        		$(document).off('keydown');
	        		e.preventDefault();
	        		submitAjaxForm('<%=EncryptionUtil.encrypt("admin.user.confirmActCmd")%>', 'tkn='+barcode+'&is_del=<%=request.getAttribute("is_del")%>&tp=<%=request.getAttribute("tp")%>&workId=<%=request.getAttribute("mvm")%>&caisse.id=<%=request.getAttribute("caisse")%>', $("#data-form"), $("#trg-lnk"));
	        	}
	        	barcode="";
	        } else{
	  			 barcode = barcode + String.fromCharCode(code);
	        }
	    });
	    
		$("#cmd\\.user\\.id, #cmd\\.password").keypress(function(e){
			if(e.which == 13) {
				$("#auth_act_lnk").trigger("click");
				e.stopPropagation();
				return false;
		    }
		});
	});
</script>
	
<std:form name="data-top-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Authentification mot de passe OU badge</span>
			<img src="resources/framework/img/badge_scanner.png" style="width: 20px" title="Lecteur badge utilisable sur cet écran">
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<div class="col-md-6">
						<std:label classStyle="control-label col-md-3" value="Login" style="font-weight:bold;"/>&nbsp;
						<div class="col-md-9" style="margin-top: -15px;">
							<std:select name="cmd.user.id" type="long" style="width:100%;" data="${listUser }" key="id" labels="login" />
						</div>
					</div>
					<div class="col-md-6">
						<std:label classStyle="control-label col-md-5" value="Mot de passe" style="font-weight:bold;"/>&nbsp;
						<div class="col-md-7">
							<std:password name="cmd.password" placeholder="Mot de passe" type="string" style="width:140px;margin-top: -15px;" maxlength="80" />
						</div>
					</div>
				</div>
			</div>
			<div class="row" style="text-align: center;">
				<div class="col-md-12">
					<std:button id="auth_act_lnk" actionGroup="M" classStyle="btn btn-lg btn-success" closeOnSubmit="true" action="admin.user.confirmActCmd" targetDiv="xxxx" onComplete="$('#li_manager').show(1000);" icon="fa-save" value="S'authentifier" />
					<button type="button" id="close_modal" class="btn btn-lg btn-primary" data-dismiss="modal">
						<i class="fa fa-times"></i> Fermer
					</button>
				</div>
			</div>
		</div>
	</div>
</std:form>