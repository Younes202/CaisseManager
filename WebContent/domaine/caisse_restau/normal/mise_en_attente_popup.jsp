<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
boolean isShowModeCmd = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("SHOW_MODE_CMD"));
boolean isShowAttBtn = !StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("SHOW_MISE_ATT"));
%>

<style>
#generic_modal_body{
	width: 720px;
	margin-left: -10%;
}
</style>

<script type="text/javascript">
$(document).ready(function (){
	$("#mise_att_cuis").click(function(){
		$("#div_att_mode a").attr("class", "btn btn-palegreen shiny");
		$("#div_att_mode, #div_att_act").show(1000);
		$("#div_att_at").hide(1000);
	});
	$("#mise_att_back").click(function(){
		$("#div_att_mode, #div_att_act").hide(1000);
		$("#div_att_at").show(1000);
	});
});
</script>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Mise en attente de la commande</span>
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal" style="margin-top: 2px;">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row" style="text-align: center;">
			
				<div class="form-group" id="div_att_at">
				
					<%if(isShowAttBtn){ %>
						<std:button action="caisse-web.caisseWeb.miseEnAttente" targetDiv="left-div" params="cuis=0" classStyle="btn btn-success shiny" style="box-shadow: 5px 10px 12px grey;height: 55px;font-size: 24px;font-weight: bold;background:url('resources/caisse/img/caisse-web/hourglass.png') no-repeat;background-size: 50px;padding-left: 70px;" value="Mise en attente" />
					<%} %>
					
					<%if(isShowModeCmd){ %>
						<button id="mise_att_cuis" type="button" class="btn btn-primary shiny" style="box-shadow: 5px 10px 12px grey;height: 55px;font-size: 24px;font-weight: bold;background:url('resources/caisse/img/caisse-web/food.png') no-repeat;background-size: 46px;padding-left: 70px;margin-left: 20px;">Envoyer</button>
					<%} else{ %>
						<std:button action="caisse-web.caisseWeb.miseEnAttente" targetDiv="left-div"  params="type_cmd=P&cuis=1" classStyle="btn btn-primary shiny" style="box-shadow: 5px 10px 12px grey;height: 55px;font-size: 24px;font-weight: bold;background:url('resources/caisse/img/caisse-web/food.png') no-repeat;background-size: 46px;padding-left: 70px;margin-left: 20px;" value="Envoyer" />
					<%} %>
				</div>
			
				<div class="form-group" id="div_att_mode" style="display: none;">
					<std:link action="caisse-web.caisseWeb.miseEnAttente" targetDiv="left-div" classStyle="btn btn-palegreen shiny" style="box-shadow: 5px 10px 12px grey;height: 55px;font-size: 24px;color: black;font-weight: bold;" params="type_cmd=P&cuis=1" value="Sur place" />
					<std:link action="caisse-web.caisseWeb.miseEnAttente" targetDiv="left-div" classStyle="btn btn-palegreen shiny" style="box-shadow: 5px 10px 12px grey;height: 55px;font-size: 24px;color: black;font-weight: bold;margin-left: 15px;" params="type_cmd=E&cuis=1" value="A emporter" />
					<std:link action="caisse-web.caisseWeb.miseEnAttente" targetDiv="left-div" classStyle="btn btn-palegreen shiny" style="box-shadow: 5px 10px 12px grey;height: 55px;font-size: 24px;color: black;font-weight: bold;margin-left: 15px;" params="type_cmd=L&cuis=1" value="Livraison" />
				</div>
				
				<div class="row" id="div_att_act" style="display: none;">
					<div class="col-md-12">
						<button id="mise_att_back" style="margin-top: 20px;height: 50px;width: 144px;font-size: 20px;font-weight: bold;border-radius: 47px;background-color: black !important;border-color: black !important;" class="btn btn-danger shiny" type="button"><i class="fa fa-mail-reply-all" style="margin-left: 9px;font-size: 22px"></i> RETOUR</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</std:form>