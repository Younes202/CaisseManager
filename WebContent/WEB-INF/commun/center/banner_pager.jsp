<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.NumericUtil"%>
<%@page import="framework.model.common.constante.ProjectConstante"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.component.complex.table.RequestTableBean"%>
<%
// Ce composant construit une bannière pour la pagination. Pour son bon fonctionnement, un appel à la méthode
// "sendDataPager(request, dataCount)" qui se trouve dans "FrameworkServlet" est nécessaire afin de maintenir les paramètres
// (page, lc, start, end) dans la requête. Pour récupérer le début et la fin des pages, il suffit de faire appel aux méthodes
// "getStartPager(request)" et "getEndPager(request)" de la servlet "FrameworkServlet".
// Ce composant peut fonctionner avec ou sans formulaire. Dans le cas d'un formulaire (ce qui veut dire que l'attribut "form_name"
// est utilisé), les paramètres sont passé dans des champs cachés et à chaque action une validation du formulaire sera effectuée.
// Dans le deuxième cas (sans formulaire), toutes les actions font appel à un "link" avec en paramètre les données de la
// pagination(page et lc).
// Certaines parties de la bannière de pagination peuvent être cachées, pour cela il suffit de ne pas utiliser les attributs
// correspondants à l'affichage des libellés dans le composant. Exemple : pour ne pas afficher la partie gauche il suffit de ne
// pas utiliser l'attribut "rows_counter_text". Pour la partie droite ne pas utiliser "rows_inpage_text".

boolean isPaginate = StringUtil.isTrue(""+request.getAttribute("show_paginate"));
boolean isFiltrable = StringUtil.isTrue(""+request.getAttribute("is_filtrable"));
boolean isEnableDisable = StringUtil.isTrue(""+request.getAttribute("is_enabDis"));
String stateFilter = (String)request.getAttribute("currFilter");
String tableName = (String)request.getAttribute("pagerBean_name");
String width = (String)request.getAttribute("pagerBean_width");
String currAct = EncryptionUtil.encrypt(""+request.getAttribute("pagger_table_act"));
RequestTableBean cplxTable = ControllerUtil.getRequestTableBean(tableName, request);

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
	String onChangeRefresh = " onChange=\"" + jsFunction + "\"";
	%>
	<table cellspacing='0' cellpadding='0' class='inf' width='<%=width%>'>
		<tr>
			<td width='20%' nowrap='nowrap'>
				<%
				if(isPaginate){
					if (page_count >= 1) { %>
					<%=((start + 1) + "-" + end + " / " + element_count)%>
					&nbsp;
				<%}
				} else{%>
						&nbsp;<%= element_count%>
					<%
				}%>
				&nbsp;&nbsp;
				<a class="btn btn-default btn-xs shiny icon-only success" href="javascript:void(0);" id="refreshrow_<%=tableName%>" style="margin-right:2px;margin-top: -2px;" title="<%=StrimUtil.label("img.refresh")%>" <%=onClickRefresh %>>
					<i class="fa fa-refresh"></i>
				</a>		
						|
			<%if(isEnableDisable){ %>
				<select style="width:64px;font-size: 10px;color: #e91e63 !important;" class="flexselect" id="<%=tableName%>_pager.flt_sub" name="<%=tableName%>_pager.flt_sub" <%=onChangeRefresh %>>
					<option value='E' <%="E".equals(stateFilter) ? " selected=\"selected\" ":"" %>>Actifs</option>
					<option value='D' <%="D".equals(stateFilter) ? " selected=\"selected\" ":"" %>>Inactifs</option>
					<option value='A' <%="A".equals(stateFilter) ? " selected=\"selected\" ":"" %>>Tous</option>
				</select>
				<%} %>
			<%if(isFiltrable){ %>			
				<a class="btn btn-default btn-xs shiny icon-only success" href="javascript:void(0);" style="margin-left:2px;margin-right:2px;margin-top: -2px;" id="<%=tableName %>_img_show" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#<%=tableName%>_table_modal" title="<%=StrimUtil.label("filter.show")%>">
					<i class="fa fa-search"></i>
				</a>		
						
						 |
			<%} %> 
				<a class="btn btn-default btn-xs shiny icon-only success" href="javascript:void(0);" style="margin-right:2px;margin-top: -2px;" title="<%=StrimUtil.label("img.reinit")%>" onclick="reinitTable('<%=tableName%>', '<%=currAct%>')">
					<i class="fa fa-reply"></i>
				</a>		
						
			</td>
			<td width='60%' nowrap='nowrap' align='center'>
		<%
if (page_count >= 1 && isPaginate) {
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
<%if (page_count >= 1 && isPaginate) {
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