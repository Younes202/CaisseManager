<%@page import="appli.controller.domaine.administration.bean.AgencementBean"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

	<script type="text/javascript" src="resources/restau/js/agencement/jquery-ui.js"></script>
	<script type="text/javascript" src="resources/restau/js/agencement/jquery.canvasAreaDraw.js"></script>
	
<style> 
.lib_table {
	width: 70px;
	padding: 0px !important;
	margin-top: 76% !important;
	font-size: 11px;
	text-align: center;
	color: #FF5722 !important;
}
</style>

<%
boolean isWriteForm = ControllerUtil.isEditionWritePage(request);
AgencementBean agencementBean = (AgencementBean)request.getAttribute("agencement");
if(agencementBean == null){
	agencementBean = new AgencementBean();
}
%>

<script type="text/javascript">
	var oldDataTable = "<%=StringUtil.getValueOrEmpty(agencementBean.getTable_coords())%>";
	var oldDataCoin = "<%=StringUtil.getValueOrEmpty(agencementBean.getLimite_coords())%>";
	var tables = {};
	
	function restaureCoords() {
		$("#limite_coords").val(oldDataCoin);
		$('.canvas-area[data-image-url]').canvasAreaDraw({});

		if(oldDataTable != ''){
			var data = oldDataTable.split(";");
			for (var i = 0; i < data.length; i++) {
				var coord = data[i].split(":");
				if(coord[1] && coord[1] != null && coord[1] != ''){
					var newImg = cloneImage(coord[0], parseFloat(coord[1])-200, parseFloat(coord[2])+50, true);
					updateDataTable(newImg);
				}
			}
		}
		return false;
	}

	function cloneImage(lib, left, top, isRestaure){
		var newImg = $("#img_zone_ori").clone(false);
		var d = new Date();
		var n = d.getMilliseconds();
		newImg.attr("id", "img_zone_" + n);
		newImg.show();

		if (isRestaure) {
			var txtField = newImg.find(".lib_table");
			txtField.val(lib);
			<%if(!isWriteForm){ %>
				txtField.attr("readOnly", "readOnly").css("background", "transparent");
			<%}%>
			txtField.show();
			
			left = left - 30;
			top = top - 120;
		}
		newImg.css("position", "absolute").css("left", left + "px").css("top",top + "px");
		
		//
		$("#img_div").append(newImg);
		
	<%if(isWriteForm){ %>	
		//
		newImg.draggable({
			stop : function(event, ui) {
				updateDataTable($(event.target));
				//
				if(newImg.find(".lib_table").css("display") == 'none'){
					var offset = $("#img_div").offset();
					cloneImage("", (offset.left-70), (offset.top-70), false);
				}
				newImg.find(".lib_table").show();
			}
		});
		newImg.mousedown(function(event) {
			switch (event.which) {
				case 3: {
					if(newImg.find(".lib_table").css("display") != 'none'){
						tables[$(this).attr("id")] = null;
						$(this).remove();
	
						manageTableCoords();
					}
					break;
				}
			}
		});
	<%}%>	
		
		return newImg;
	}
	
	function updateDataTable(comp) {
		var offset = comp.offset();
		var txt = comp.find(".lib_table").val() + ":" + offset.left + ":" + offset.top;

		tables[comp.attr("id")] = txt + ";";
		//
		manageTableCoords();
	}

	function manageTableCoords() {
		var tblAll = "";
		for ( var name in tables) {
			if (name != null && tables[name] != null) {
				tblAll += tables[name];
			}
		}
		$("#table_coords").text(tblAll);
	}
	
	$(document).ready(function() {
		$(document).on('change', '.lib_table', function(){
			updateDataTable($(this).closest("div"));
		});
		//
		restaureCoords();
		//		
		var offset = $("#img_div").offset();
		cloneImage("", (offset.left-70), (offset.top-70), false);
	});
</script>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li class="active">Modules avanc&eacute;s</li>
		<li class="active">Agencement</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="U" classStyle="btn btn-default" action="admin.agencement.work_init_update" workId="${agencement.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		<std:link classStyle="btn btn-default" action="admin.agencement.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
	
	<div class="widget">
	
	<div id="img_zone_ori" style="background: url(resources/restau/js/agencement/table_64.png) no-repeat; height: 93px; width: 70px;display: none;">
		<input type="text" class="lib_table" style="display: none;">
	</div>
	
	<div class="widget-body">
		<std:form name="data-form">
		<div class="row" style="margin-left: 20px;">
			<!--   *********************************la zone de dessin********************************************* -->
			<div class="row">
				<div id="img_div" style="border: 1px solid #03A9F4;padding-left: 50px;width: 30%;float: left;height: 95px;"></div>
				<div style="border: 1px solid #03A9F4;padding-left: 5px;margin-left:5px;width: 60%;height: 95px;float: left;margin-bottom: 20px;padding-top: 30px;">
					<std:label classStyle="control-label col-md-3" value="Emplacement" />
					<div class="col-md-9">
						<std:text name="agencement.emplacement" type="string" placeholder="Emplacement" required="true" maxlength="120" />
					</div>
				</div>		
				<textarea rows=3 name="table_coords" id="table_coords" style="display: none;"></textarea>
				<textarea rows=3 name="limite_coords" id="limite_coords" class="canvas-area input-xxlarge" data-image-url="resources/restau/js/agencement/zone-dessin.png" style="display: none;"></textarea>
			</div>
		</div>
		<hr>
		<div class="form-actions">
			<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
				<std:button actionGroup="M" classStyle="btn btn-primary" icon="fa-save" value="Effacer" onClick="$('#btn_clear').trigger('click');" />
				<std:button actionGroup="M" classStyle="btn btn-success" action="admin.agencement.work_merge" workId="${agencement.id }" icon="fa-save" value="Sauvegarder" />
			</div>
		</div>
	</std:form>
		</div>	
		
	</div>
	<!-- row   -->
</div>
<!-- /Page Body -->