<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
Boolean isUpdateDate = (Boolean)request.getAttribute("isUpdateDate");
boolean isUseCoasterCall = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CUSTOM_CALL"));
%>

<std:form name="data-form">
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Gestion journ&eacute;e</span>
			<c:if test="${journee.id == null }">
            	<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="caisse.journee.work_init_update" workId="${journee.id}"
				icon="fa fa-pencil" tooltip="Cr&eacute;er" />
			</c:if>
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal" style="margin-top: 2px;margin-right: 2px;">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>
		<div class="widget-body">
		<div class="row">
			<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
		</div>
			<div class="row" style="margin-left: 7px;">
				<div class="form-group">
					<std:label classStyle="control-label col-md-4" valueKey="journee.date_journee"/>
					<div class="col-md-4">
						<std:date name="journee.date_journee" value="${currentDate }" required="true" />
					</div>
				</div>
				<%if(isUseCoasterCall && !BooleanUtil.isTrue(isUpdateDate)){ %>
				<div class="form-group">
					<std:label classStyle="control-label col-md-4" value="Coaster call indisponibles"/>
					<div class="col-md-4">
						<std:text autocomplete="false" name="journee.customcall_out" type="string" value="${currentCustomcall }" />
						<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Num&eacute;ro des coaster call indisponibles s&eacute;par&eacute;s par des ;" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
					</div>
				</div>
				<%} %>
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button actionGroup="M" classStyle="btn btn-success" style="border-radius: 37px;height: 52px;font-size: 21px;" action='<%=BooleanUtil.isTrue(isUpdateDate) ? "caisse.journee.edit_date_journee" : "caisse.journee.ouvrir_journee" %>' workId="${journee.id }" icon="fa-save" value="Ouvrir" />
					</div>
				</div>
			</div>
		</div>
	</div>
</std:form>

<script type="text/javascript">
/*Handles ToolTips*/
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script>  