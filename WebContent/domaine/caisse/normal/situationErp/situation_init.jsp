<%@page import="appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#generic_modal_body{
	width: 920px;
	margin-left: -10%;
}
</style>

<%
boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
%>
<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Situation</span>
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row" style="margin-left: 10px;margin-right: 10px;">
				<div class="form-group">
					<%if(StringUtil.isTrueOrNull(ContextGloabalAppli.getGlobalConfig("RIGHT_SITUATION_CLIENT"))){ %>
					<div class="col-md-4">
						<std:button style="margin-left:15px;border-radius: 37px;height: 52px;font-size: 21px;background-color: #9b5a05;color: white;" closeOnSubmit="true"   action="caisse-web.caisseWeb.init_situation" params="curMnu=cli" targetDiv="right-div" icon="fa-print" value="Situation CLIENT"/>
					</div>
					<%} %>
					<%if(StringUtil.isTrueOrNull(ContextGloabalAppli.getGlobalConfig("RIGHT_SITUATION_SOC_LIVR"))){%>
					<div class="col-md-4">
						<std:button style="margin-left:15px;border-radius: 37px;height: 52px;font-size: 21px;background-color: #06977f;color: white;" closeOnSubmit="true"   action="caisse-web.caisseWeb.init_situation" params="curMnu=socLivr" targetDiv="right-div" icon="fa-print" value="SOCIÉTÉ LIVRAISON"/>
					</div>
					<%} %>
					<%if(StringUtil.isTrueOrNull(ContextGloabalAppli.getGlobalConfig("RIGHT_SITUATION_LIVREUR"))){%>
					<div class="col-md-4">
						<std:button style="margin-left:15px;border-radius: 37px;height: 52px;font-size: 21px;background-color: #a98d04;color: white;" closeOnSubmit="true"   action="caisse-web.caisseWeb.init_situation" params="curMnu=livr" targetDiv="right-div" icon="fa-print" value="Situation LIVREUR"/>
					</div>
					<%} %>
				</div>
			</div>	
		</div>
	</div>
</std:form>