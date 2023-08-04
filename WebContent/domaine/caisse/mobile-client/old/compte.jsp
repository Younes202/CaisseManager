<%@page import="appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS"%>
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

<div style="margin-top: -1px;
    overflow-x: hidden;
    overflow-y: auto;">
<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Mon compte</span>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row" style="margin-left: 10px;margin-right: 0px;">
				<jsp:include page="compte_include.jsp" />
			</div>	

		<div class="row" style="text-align: center;">
			<div class="col-md-12">
				<std:button actionGroup="M" classStyle="btn btn-success" action="caisse.clientMobile.merge_compte" workId="${client.id }" params="isSub=1" icon="fa-lock" style="border-radius: 37px;height: 42px;font-size: 18px;" value="Sauvegarder" />
			</div>
		</div>
	</div>
</div>
</std:form>
</div>