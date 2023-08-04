<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
List<Object[]> listData = (List<Object[]>)request.getAttribute("dataArray");
String devise = "&nbsp;"+StrimUtil.getGlobalConfigPropertie("devise.symbole");
%>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">D&eacute;tail montants</span>
		</div>
		<div class="widget-body">
			<div class="row">
				<div class="form-group">
					<table style="margin-left: 40px;margin-left: 25%;" >
						<tr><td style="width: 200px;"></td><td></td></tr>
					<%
					for(Object[] data : listData){
					%>
					<tr>
						<td style="color: #d73d32;"><%=StringUtil.isEmpty(data[1]) ? "Autres":data[1] %></td>
						<td align="right" style="color: #53a93f;font-weight: bold;font-size: 14px;"><%=BigDecimalUtil.formatNumber((BigDecimal)data[0]) %>
							<span style="color: black;font-weight: normal;"><%=devise %></span>
						</td>
					</tr>
					<%} %>
					</table>
				</div>
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</std:form>