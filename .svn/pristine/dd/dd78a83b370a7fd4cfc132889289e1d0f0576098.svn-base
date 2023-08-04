<%@page import="java.time.ZoneId"%>
<%@page import="java.time.LocalDateTime"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="java.util.Date"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#table_result tr{
	
}
#table_result td{
	padding-left: 1px;
	padding-right: 2px;
	border: 1px solid blue;
}
</style>

<%
Map<String, Object> mapData = (Map<String, Object>)request.getAttribute("result_data");
Map<String, String> mapCols = null;
List<Object[]> listData = null;
//
if(mapData != null){
	mapCols = (Map<String, String>)mapData.get("cols");
	listData = (List<Object[]>)mapData.get("data");
}
String errorData = (String)request.getAttribute("result_error");
String succesData = (String)request.getAttribute("result_succes");
%>


<div class="row DTTTFooter">
	<div class="col-sm-6">
		<div class="dataTables_info" id="editabledatatable_info" role="status" aria-live="polite">
			<b><%=listData != null ? listData.size() : 0 %> &eacute;l&eacute;ments</b>
		</div>
	</div>
	<div class="col-sm-6">
		<div class="dataTables_paginate paging_bootstrap" id="editabledatatable_paginate">
			<ul class="pagination">
				<li class="prev">
					<std:link actionGroup="C" targetDiv="data_div" action="admin.requeteur.executeRequest" params="way=prev" value="Pr&eacute;c&eacute;dent" />
				</li>
				<li class="next" style="margin-left: 10px;">
					<std:link actionGroup="C" targetDiv="data_div" action="admin.requeteur.executeRequest" params="way=nxt" value="Suivant" />
				</li>
			</ul>
		</div>
	</div>
</div>

<table id="table_result" style="width: 100%;">

 <%
 if(succesData != null){	%> 
	<tr><td style="font-weight: bold;color: green;"><%=succesData %></td></tr>
<%} else if(errorData != null){ %>
	<tr><td style="font-weight: bold;color: red;"><%=errorData %></td></tr>
<%} else if(mapData != null){%>

	<tr style="background-color: orange;">
		<td></td>
<%	 for(String colName : mapCols.keySet()){
		String colType = mapCols.get(colName);
		%>
			<td><%=colName %>-<%=colType %></td>
<%	}%>
	 </tr> 
<%	
	Object[] typeArray = mapCols.values().toArray();
int j = (ControllerUtil.getMenuAttribute("startPagger", request)!=null?(Integer)ControllerUtil.getMenuAttribute("startPagger", request) : 0);
j = j + 1;

for(Object[] dataArray : listData){
	int idx = 0;
%>
		<tr>
			<td style="font-weight: bold;color: blue;"><%=j %></td>
		<%for(Object data : dataArray){
			String typeData = ""+typeArray[idx];
			String style = "";
			String dataFormat = ""+data;
			//
			if(StringUtil.isNotEmpty(dataFormat)){
				if(typeData.equals("DECIMAL")){
					style = "text-align:right;color:green;";
					dataFormat = BigDecimalUtil.formatNumber(BigDecimalUtil.get(dataFormat));
				} else if(typeData.equals("BIT")){
					style = "text-align:right;color:orange;";
				} else if(typeData.equals("INT")){
					style = "text-align:right;";
				} else if(typeData.equals("DATETIME")){
					style = "text-align:center;color:blue;";
					if(data instanceof LocalDateTime){
						Date dt = Date.from(((LocalDateTime) data).atZone(ZoneId.systemDefault()).toInstant());
						dataFormat = DateUtil.dateTimeToString(dt);
					} else{
						dataFormat = DateUtil.dateTimeToString((Date) data);						
					}
				}
			}
		%>
		<td style="<%=style%>"><%=StringUtil.getValueOrEmpty(dataFormat) %></td>
		<%
		idx++;
		} %>
	</tr>
<%	
		j++;
	}
}%>
</table>
