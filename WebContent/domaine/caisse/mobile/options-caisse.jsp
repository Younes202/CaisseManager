<%@page import="appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
%>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget" id="shift-div">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Options caisse</span>
			
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row" style="margin-left: 10px;margin-right: 0px;">
				<%if(isRestau){ %>
				<div class="form-group">
					<std:label classStyle="control-label col-md-4" value="Code wifi" />
					<div class="col-md-8">
						<std:text name="code_wifi" type="string" classStyle="form-control" maxlength="150" style="border-radius: 21px !important;float: left;width:80%;" value="${paramWifi.valeur }" />
						<std:button action="caisse-web.caisseWeb.printCodeWifi" targetDiv="div_gen_printer" closeOnSubmit="true" icon="fa-print" />
					</div>
				</div>
				<%} %>
				<div class="form-group">
					<std:label classStyle="control-label col-md-4" value="Afficher image mise en veille" />
					<div class="col-md-8">
						<std:checkbox name="param_veille" checked="${paramVeille.valeur }" />
					</div>
				</div>
				
				<div class="form-group">
					<div class="col-md-12">
						<%
						boolean isEmbedPrint = ControllerUtil.getUserAttribute("IS_EMBDED_MOBILE_PRINTER", request) != null;
						%>
						<std:link action="caisse-web.caisseWeb.managerEmbededPrinter" closeOnSubmit="true" style='<%=isEmbedPrint ? "font-weight:bold;color:red;":"font-weight:bold;color:blue;" %>' value='<%=isEmbedPrint ? "Désactiver impression mobile" : "Activer impression mobile" %>' />
					</div>
				</div>
			</div>	

		<div class="row" style="text-align: center;">
			<div class="col-md-12">
				<std:button actionGroup="M" classStyle="btn btn-success" action="caisse-web.caisseWeb.init_opts" closeOnSubmit="true" params="isSub=1" icon="fa-lock" style="border-radius: 37px;height: 42px;font-size: 18px;" value="Sauvegarder" />
			</div>
		</div>
	</div>
</div>
</std:form>