<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<style>
#generic_modal_body{
	width: 570px;
	margin-left: -10%;
}
.btn_code_bar{
	font-size: 26px;
    font-weight: bold;
    color: black;
    margin-bottom: 14px;
}
.btn.btn-circle {
    width: 47px;
    height: 47px;
    padding: 1px 10px;
}
</style>

<script type="text/javascript">
var poids_input_focus = $("#art\\.poids");
$(document).ready(function (){
	setTimeout(function(){
		poids_input_focus.focus();
	}, 1000);
	
	$("#input_poids input").focusin(function(){
		poids_input_focus = $(this);
	});
	
	//
	$("#code_keys a").click(function(){
		if($(this).attr("id") == 'reset'){
			poids_input_focus.val('');
		} else if ($(this).attr("id") == 'back'){
			poids_input_focus.val(poids_input_focus.val().substring(0, poids_input_focus.val().length-1));
		} else{
			poids_input_focus.val(poids_input_focus.val()+$(this).text());
		}
		poids_input_focus.focus();
	});
	$(document).keydown(function(e) {
        var code = (e.keyCode ? e.keyCode : e.which);
        //
        if(code==13){
        	e.preventDefault();
        	$("#load_lnk").trigger("click");
        	return;
        }
    });
});
</script>
 
 <%
 boolean isCodeBarre =  "C".equals(ContextGloabalAppli.getGlobalConfig("BALANCE_MODE"));
 %>
 
 
<std:form name="search-form">
	<div class="widget-header bordered-bottom bordered-blue">
		<span class="widget-caption">Article [<b>${article.libelle }</b>]</span>
		<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal" style="margin-top: 2px;margin-right: 5px;">
			<i class="fa fa-times"></i> Fermer
		</button>
	</div>
	<div class="widget-body" id="poids_div">
		<div class="row">
			<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
		</div>
		<div class="row">
			<div class="col-md-4" id="input_poids">
				<div class="col-md-12">
					<std:text name="art.poids" placeholder="Grm ou Mil" type="string" style="text-align:right;font-weight: bold;height:50px;width:95%;font-size: 25px;float:left;margin-left: 5px;" maxlength="15" />
				</div>
				<div class="col-md-12" style="text-align: center;">
				
				<%if(isCodeBarre){ %>
				<std:button value="Valider" params="art=${article.id }" action="caisse-web.balance.addArticlePoids" style="margin-top: 15px;" classStyle="btn btn-lg btn-success"/>
				<%} else{ %>
					<std:button value="Valider" targetDiv="poids_div" params="art=${article.id }" action="caisse-web.balance.addArticlePoids" style="margin-top: 15px;" classStyle="btn btn-lg btn-success"/>
				<%} %>	
				</div>
			</div>
			<div class="col-md-8" id="code_keys">
				<%for(int i=0; i<10; i++){ %>
					<a href="javascript:void(0);" class="btn btn-info btn-circle num_auth_stl btn_code_bar"><%=i %></a>
				<%} %>
				<a href="javascript:void(0);" class="btn btn-warning btn-circle num_auth_stl btn_code_bar">.</a>
				<a href="javascript:void(0);" id="back" class="btn btn-warning btn-circle num_auth_stl btn_code_bar" style="font-size: 12px;"><i class="fa fa-mail-reply"></i></a>
				<a href="javascript:void(0);" id="reset" class="btn btn-warning btn-circle num_auth_stl btn_code_bar" style="font-size: 12px;"><i class="fa fa-magic"></i></a>
			</div>
		</div>
	</div>	
 </std:form>	

