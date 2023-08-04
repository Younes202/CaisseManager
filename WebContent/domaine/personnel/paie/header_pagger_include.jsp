
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.NumericUtil"%>
<%@page import="framework.model.common.constante.ProjectConstante"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.component.complex.table.RequestTableBean"%>


<div style="border: 1px solid #2dc3e8;">
<%
String mnuTop = (String)request.getAttribute("mnuTop");
String act = "paie.salariePaie.loadVueMois";
if("J".equals(mnuTop)){
	act = "pers.pointage.loadDataJour";
}

String tableName = "paie_table_body";
RequestTableBean cplxTable = ControllerUtil.getRequestTableBean(tableName, request);
String currAct = EncryptionUtil.encrypt(act);
String jsFunction = "";
// *************************** Pager ******************************//
	int element_count = cplxTable.getDataSize();
	int line_count = cplxTable.getPageSize();
	int curent_page = cplxTable.getCurrentPage();
	int page_count = (int) Math.ceil((double) element_count / line_count);// Calculate
	int end = cplxTable.getLimitIndex();
	// page
	// count
	int start = cplxTable.getStartIndex();
	String[] rowsInpage = StringUtil.getElementsArray(ProjectConstante.ROWS_IN_PAGE, ",", true);
	// Re-ajust rows segment
	for (int i = 0; i < rowsInpage.length; i++) {
		if ((i > 0) && (rowsInpage[i - 1] == null)) {
			rowsInpage[i] = null;
		} else if ((NumericUtil.getIntOrDefault(rowsInpage[i]) > element_count)
				&& ((i > 0) && (NumericUtil.getIntOrDefault(rowsInpage[i - 1]) >= element_count))) {
			rowsInpage[i] = null;
		}
	}
	// Hidden fields
	String oldCp = (String)ControllerUtil.getParam(request, tableName+"_pager.cp");
	%>
	<input type='hidden' name="<%=tableName%>_pager.cp_old" value="<%= (StringUtil.isEmpty(oldCp) ? "1" : oldCp)%>"/>
	<input type='hidden' name="<%=tableName%>_pager.cp" id="<%=tableName%>_pager.cp"/>
	<input type='hidden' name="<%=tableName%>_pager.fie" id="<%=tableName%>_pager.fie" value="<%=StringUtil.getValueOrEmpty(ControllerUtil.getParam(request, tableName+"_pager.fie"))%>"/>

	<%
	jsFunction = "pagerAjaxTable('"+tableName+"','"+curent_page+"', '"+currAct+"');";
	String onClickRefresh = " onClick=\"" + jsFunction + "\"";
	%>
	<table cellspacing='0' cellpadding='0' class='inf' width='100%' style="height: 34px;background-color: #e0f7fa;">
		<tr>
			<td width='20%' nowrap='nowrap'>
				<%if (page_count >= 1) { %>
					<%=((start + 1) + "-" + end + " / " + element_count)%>
					&nbsp;
				<%} %>
				&nbsp;&nbsp;
			
			</td>
			<td width='60%' nowrap='nowrap' align='center'>
		<%
if (page_count >= 1) {
		jsFunction = "pagerAjaxTable('"+tableName+"','1', '"+currAct+"');";
		String onClickDeb = ((curent_page != 1) ? (" onClick=\"" + jsFunction) + "\"" : "");
		%>
			<div id='navigation'>
				<table>
					<tr>
						<td><!--  Debut -->
						   <a class="btn btn-default btn-xs shiny icon-only success" href="javascript:void(0);" style="margin-right:2px;margin-top: -2px;<%=(curent_page != 1) ?"":"background: #ccc;" %>" title="<%=StrimUtil.label("first.page")%>" <%=(curent_page != 1) ? onClickDeb:"" %>>
								<i class="fa fa-fast-backward"></i>
							</a>
						</td>

		<%jsFunction = "pagerAjaxTable('"+tableName+"','" + (curent_page - 1) + "', '"+currAct+"');";
		String onClickPrev = ((curent_page != 1) ? " onClick=\"" + jsFunction + "\"" : "");
		%>
				<td><!-- Precedent -->
					<a class="btn btn-default btn-xs shiny icon-only success" href="javascript:void(0);" style="margin-right:2px;margin-top: -2px;<%=(curent_page != 1) ?"":"background: #ccc;" %>" title="<%=StrimUtil.label("back.page")%>" <%=(curent_page != 1) ? onClickPrev:""%>>
						<i class="fa fa-chevron-left"></i>
					</a>
					
				</td>
				<td>Page</td>

		<%jsFunction = "pagerAjaxTable('"+tableName+"',this.value, '"+currAct+"');";
		String onChange = ((page_count > 1) ? " onChange=\"" + jsFunction + "\"" : " disabled='disabled'");
		%>
		<!-- Pages -->
		<td>
			<select class="flexselect" name="<%=tableName%>_pager.cp_sub" <%=onChange%>>
			<%for (int i = 1; i < (page_count + 1); i++) {%>
				<option value='<%=i %>' <%=((curent_page == i) ? " selected=selected" : "")%>><%=i %></option>
			<%}%>
			</select>
		</td>
		<td> / <%=page_count%></td>

		<%jsFunction = "pagerAjaxTable('"+tableName+"', '" + (curent_page + 1) + "', '"+currAct+"');";
		String onClickNext = ((curent_page < page_count) ? (" onClick=\"" + jsFunction) + "\"" : "");
		%>

			<td><!-- Next -->
				<a class="btn btn-default btn-xs shiny icon-only success" href="javascript:void(0);" style="margin-left:2px;margin-right:2px;margin-top: -2px;<%=(curent_page < page_count) ?"":"background: #ccc;" %>" title="<%=StrimUtil.label("next.page")%>" <%=(curent_page < page_count) ? onClickNext:"" %>>
					<i class="fa fa-chevron-right"></i>
				</a>	
			</td>

		<%jsFunction = "pagerAjaxTable('"+tableName+"', '" + (page_count) + "', '"+currAct+"');";
		String onClickLast = ((curent_page != page_count) ? (" onClick=\"" + jsFunction) + "\"" : "");
		%>

			<td><!-- Dernier -->
					<a class="btn btn-default btn-xs shiny icon-only success" href="javascript:void(0);" style="margin-top: -2px;<%=(curent_page != page_count) ?"":"background: #ccc;" %>" title="<%=StrimUtil.label("last.page")%>" <%=(curent_page != page_count) ? onClickLast:"" %>>
						<i class="fa fa-fast-forward"></i>
					</a>
				</td>
		</tr>
	</table>
	</div>
<%} %>	
	</td>

		<td width='20%' nowrap='nowrap' align='right'>
<%if (page_count >= 1) {
		jsFunction = "pagerAjaxTable('"+tableName+"', '"+curent_page+"', '"+currAct+"');";
		String state = (element_count < NumericUtil.toInteger(ProjectConstante.DEFAULT_LINE_COUNT)) ? "disabled='disabled'" : " onChange=\"" + jsFunction	+ "\"";
	%>		
		<%=StrimUtil.label("work.elmnt.page") %>
			<select class="flexselect" name="<%=tableName%>_pager.lc" <%=state%>>
		<!-- Nombre d'element par page -->
		<%for (String nbr : rowsInpage) {
			if (nbr != null) {%>
				<option value="<%=nbr %>" <%=((nbr.equals("" + line_count)) ? "selected=selected" : "")%>><%=nbr %></option>
			<%}
			} %>
			</select>

<%} %>
		</td>
</tr>
</table>
</div>
		
		<input type="hidden" name="<%=tableName %>_pager.cp_old" value="1">
		<input type="hidden" name="<%=tableName %>_pager.cp" id="<%=tableName %>_pager.cp">
		<input type="hidden" name="<%=tableName %>_pager.fie" id="<%=tableName %>_pager.fie" value="">
<!-- *************************************************** PAGGINATION ************************************************* -->		
   