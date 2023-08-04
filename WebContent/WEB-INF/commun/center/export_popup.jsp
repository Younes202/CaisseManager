<%@page import="framework.controller.bean.ColumnsExportBean"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.constante.ProjectConstante"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.component.complex.table.export.ExportTableBean"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%
ExportTableBean exportTable = (ExportTableBean)request.getAttribute("export_bean");
String tableName = exportTable.getTableName();
String formattedName = StringUtil.replaceAll(tableName, '.', "\\\\.");
int idxTr = 0;
%>

<style>
.ui-dialog .ui-dialog-titlebar{
    background-color: #c2b99b !important;
}
.ui-dialog .ui-dialog-buttonpane{
    background: #e6e6e6 !important;
   }
</style>

<a href="javascript:" id="<%=tableName %>_modal_export_link" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#<%=tableName %>_export_form" style="display: none;"></a>

 <div id="<%=tableName %>_export_form" class="modal modal-message modal-warning fade" style="display: none;" aria-hidden="true">
 <div class="modal-dialog" style="min-width: 700px;">  
	<div class="modal-content" style="min-width: 700px;">
 		<!-- widget grid -->
		<div class="widget">
         <div class="widget-header bordered-bottom bordered-blue">
            <span class="widget-caption">Exporter les donn&eacute;es</span>
         </div>
         <div class="widget-body">
<script type="text/javascript">
	//
	function manageSwitchTr(idImg){
		var currImg = $("#"+idImg);
		var trEl = currImg.closest("tr");
		var typeImg = currImg.attr("src");
		
		if(typeImg.indexOf('add') != -1){
			$('#<%=formattedName %>_selected > tbody:last').append(trEl);
			$("#<%=formattedName %>_export").find(trEl).remove();
			currImg.attr("src", "resources/framework/img/table/action/remove.png");
		} else{
			$('#<%=formattedName %>_export > tbody:last').append(trEl);
			$("#<%=formattedName %>_selected").find(trEl).remove();
			currImg.attr("src", "<%=ProjectConstante.IMG_ADD_PATH%>");
		}
		//
		$("#<%=formattedName %>_selected").tableDnD();
	}

	$(document).ready(function() {
		// Supprimer les autres
		$("div[id$='_export_form']").each(function(){
			if($(this).attr("id") != '<%=tableName %>_export_form'){
				$(this).remove();
			}
		});
		//
		$("#<%=formattedName %>_selected").tableDnD();
	});

</script>


	<form id="export_form" name="export_form" target="downloadframe">
		<input type="hidden" id="work_export_action" name="work_export_action" value="true" />
	
		<std:hidden basic="true" name="work_export_order" id="work_export_order"/> 
		<std:hidden basic="true" name="tableExportName" id="tableExportName" value="<%=tableName %>"/>
		<input type="hidden" id="w_f_act" name="w_f_act">
	
		<div class="row">
			<div class="form-group">
				<label class="control-label col-md-2">Titre</label>
				<div class="col-md-4">
					<input type="text" name="work_export_title" class="form-control" size="50" value="<%=exportTable.getTitle() %>">
				</div>
				<label class="control-label col-md-2">Format</label>
				<div class="col-md-4">
					<select name="work_export_format">
						<option value="pdf">PDF</option>
						<option value="xls">EXCEL</option>
						<option value="csv">CSV</option>
						<option value="txt">TEXTE</option>
					</select>
				</div>
			</div>
		</div>	
		<hr>
		<div class="row">
			<div class="col-md-6">
				<table cellspacing=0 cellpadding=0 class="sortable" width="320px;" id="<%=tableName %>_export" style="height: 30px;">
					<thead>
						<tr>
							<th width="140"><div style="float: left; display: block; -moz-user-select: none;"><h3><%=StrimUtil.label("work.col.dispo") %></h3></div></th>
							<th width="40"><div style="float: left; display: block; -moz-user-select: none;"><h3><%=StrimUtil.label("work.taille") %></h3></div></th>
							<th width="15"><div style="float: left; display: block; -moz-user-select: none;"><h3></h3></div></th>
						</tr>
					</thead>
					<tbody>
					<%
					List<ColumnsExportBean> listColumnExport = exportTable.getListColumnAdded();
					if((listColumnExport != null) && (listColumnExport.size() > 0)) {
						for(ColumnsExportBean column : listColumnExport){
							String label = column.getLabel();
							if(StringUtil.isNotEmpty(label)){
								String width = StringUtil.isEmpty(column.getWidth()) ? "150" : column.getWidth();
							%>
							<tr id="<%=idxTr %>">
							<%
							String expField = idxTr +"_work_export_field";
							String expType = idxTr +"_work_export_type";
							String expLabel = idxTr +"_work_export_label";
							String expWidth = idxTr +"_work_export_width";
							%>
								<std:hidden basic="true" name="<%=expField %>" value="<%=column.getField() %>"/>
								<std:hidden basic="true" name="<%=expType %>" value="<%=column.getType() %>"/>
								
								<td style="border: 0px;"><input type="text" name="<%=expLabel %>" size="20" class="form-control" value="<%=label %>"></td>
								<td style="border: 0px;"><input type="text" name="<%=expWidth %>" size="2" class="form-control" value="<%=width %>"></td>
								<td style="border: 0px;"><a href="javascript:void(0);" >
									<img src="<%=ProjectConstante.IMG_ADD_PATH%>" class="export_table" border="0" id="img_l_<%=idxTr %>" onclick="manageSwitchTr('img_l_<%=idxTr %>')" />
								</a></td>
							</tr>
							<%idxTr++;
							}
						  }
						}%>
					</tbody>
				</table>
			</div>
			<div class="col-md-6">
				<table cellspacing=0 cellpadding=0 class="sortable" width="320px;" id="<%=tableName %>_selected" style="height: 30px;">
					<thead>
						<tr>
							<th width="140"><div style="float: left; display: block; -moz-user-select: none;"><h3><%=StrimUtil.label("work.col.select") %></h3></div></th>
							<th width="40"><div style="float: left; display: block; -moz-user-select: none;"><h3><%=StrimUtil.label("work.taille") %></h3></div></th>
							<th width="15"><div style="float: left; display: block; -moz-user-select: none;"><h3></h3></div></th>
						</tr>
					</thead>
					<tbody>
					<%
					List<ColumnsExportBean> listColumn = exportTable.getListColumn();
					if(listColumn != null){
						for(ColumnsExportBean column : listColumn){
							String label = column.getLabel();
							if(StringUtil.isNotEmpty(label)){
								String width = StringUtil.isEmpty(column.getWidth()) ? "150" : column.getWidth();
							%>
							<tr id="<%=idxTr %>">
														<%
							String expField = idxTr +"_work_export_field";
							String expType = idxTr +"_work_export_type";
							String expLabel = idxTr +"_work_export_label";
							String expWidth = idxTr +"_work_export_width";
							%>
								<std:hidden basic="true" name="<%=expField %>" value="<%=column.getField() %>"/>
								<std:hidden basic="true" name="<%=expType %>" value="<%=column.getType() %>"/>
								
								<td style="border: 0px;"><input type="text" name="<%=expLabel %>" size="20" class="form-control" value="<%=label %>"></td>
								<td style="border: 0px;"><input type="text" name="<%=expWidth %>" size="2" maxlength="4" class="form-control" value="<%=width %>"></td>
								<td style="border: 0px;"><a href="javascript:void(0);" >
									<img src="resources/framework/img/table/action/remove.png" class="export_table" border="0" id="img_r_<%=idxTr %>" onclick="return manageSwitchTr('img_r_<%=idxTr %>');"/></a>
								</td>
							</tr>
							<%idxTr++;
							}
						   }
						}%>
					</tbody>
				</table>
			</div>
		</div>	
		<div class="row">
			<div class="col-md-6"></div>
			<div class="col-md-6" style="color: blue;font-size: 10px;">
			<%=StrimUtil.label("work.col.ordremsg") %>
			</div>
		</div>
					
</form>
	</div>
	<div class="modal-footer">
         <button type="button" class="btn btn-info" id="<%=tableName %>_export_btn"> Exporter les donn&eacute;es</button>
         <button type="button" id="<%=tableName %>_export_close" class="btn btn-danger" data-dismiss="modal"><i class="fa fa-times"></i> Fermer</button>
     </div>
</div>
</div>
</div>
</div>