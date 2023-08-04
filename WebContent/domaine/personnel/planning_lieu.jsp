<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.DateUtil.TIME_ENUM"%>
<%@page import="java.util.Date"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.util.Calendar"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="java.awt.Color"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="appli.model.domaine.personnel.persistant.PlanningPersistant"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
	.fc table {
		z-index: 0 !important;
	}
	.dt-cal-bloc{
	    cursor: pointer;
	    border-radius: 10px;
	    padding: 5px;
	    box-shadow: 2px 9px 7px -10px #000000;
	    border: 2px solid #c6c3c3;
	    font-size: 11px;
	    min-width: 157px;
	   }
</style>
<script type="text/javascript">
$(document).ready(function () {	
	$(".td-cal-bloc").click(function(){
		if($(this).find(".dt-cal-bloc").length == 0){
			$("#add-button").attr("params", "src=lieu&dt="+$(this).attr("dt")+"&lieu="+$(this).attr("lieu")).trigger("click");
			$("#add-button").attr("params", "src=lieu");
		}
	});
});
</script>

<% 
List<PlanningPersistant> list_planning = (List<PlanningPersistant>) request.getAttribute("list_planning");
Map<String, List<String>> mapLieu = (Map<String, List<String>>)request.getAttribute("mapLieu");
Date dateDebut = (Date)request.getAttribute("dateDebut");
Date dateFin = (Date)request.getAttribute("dateFin");
%>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Calendrier</li>
		<li>Fiche calendrier</li>
		<li class="active">Recherche</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative page-header-fixed">
	<div class="header-title" style="padding-top: 4px;">
		<std:linkPopup actionGroup="C" id="add-button" classStyle="btn btn-default" params="src=lieu" action="pers.planning.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
		<std:link actionGroup="C" classStyle="btn btn-primary" action="pers.planning.work_find" icon="fa-3x fa-map" value="Vue date" tooltip="Vue date" />
		<std:linkPopup style="display:none;" id="link-popup" action="pers.planning.work_edit" />
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<!-- Page Body -->
<div class="page-body">
    <div class="row">
       <div class="widget flat">
           <div class="widget-body">
				<div class="row" style="margin-left: 0px;margin-right: 0px;">
				   <div class="widget flat">
				       <div class="widget-body">
				       		<div class="row" style="margin-left: 0px;margin-bottom: 10px;">
					        	<div class="col-md-12">
						        	<std:label classStyle="control-label col-md-2" value="Date début" />
						            <div class="col-md-2">
						                 <std:date name="dateDebut" value="${dateDebut }"/>
						            </div>
						            <div class="col-md-2" style="text-align: center;">
						            	<std:link action="pers.planning.vue_lieu" params="prev=1" icon="fa fa-arrow-circle-left" tooltip="Mois précédent" />
						            	<std:link action="pers.planning.vue_lieu" params="next=1" icon="fa fa-arrow-circle-right" tooltip="Mois suivant" />
						            </div>
						            
						            <std:label classStyle="control-label col-md-1" value="Date fin" />
						            <div class="col-md-2">
						                 <std:date name="dateFin" value="${dateFin }"/>
						            </div>
						            <div class="col-md-2">
						           	 	<std:button action="pers.planning.vue_lieu" value="Filtrer" classStyle="btn btn-primary" />
						           	 </div>	
					       		</div>
					  	 </div>
				           
				           <div class="row" id="row_dt_dates" style="overflow-y: auto;">
				           <table class="table table-bordered table-striped table-condensed flip-content" style="width: 98%;margin-left: 1%;">
	           <tr>
	           		<th></th>
		           <% 
		           String[] colors = {"#141314", "#f57c00", "#1976d2", "#c2185b", "gray", "yellow", "green"};
		           String[] boColors = {"#ececec", "#ffe6c0", "#dcffb4", "#e0f7fa", "#f8bbd0", "#ffee58", "#d1c4e9"};
		           int i = 0;
		           for(String plan : mapLieu.keySet()){ 
		        	   List<String> listDet = mapLieu.get(plan);
		        	   if(i == boColors.length-1){
		        		   i = 0;
		        	   }
		           %>
		           		<th style="font-weight: bold;text-align: center;background-color: <%=boColors[i]%>;" colspan="<%=listDet.size()%>"><%=plan %></th>
		           <% 
		           i++;
		           }%>
	           </tr>
	           <tr>
	           		<th></th>
	           <% 
	           i = 0;
	           for(String plan : mapLieu.keySet()){ 
	        	   List<String> listDet = mapLieu.get(plan);
	        	   if(i == boColors.length-1){
	        		   i = 0;
	        	   }
	           %>
	           <% for(String lieu : listDet){ %>
	           		<th style="background-color: <%=boColors[i]%>;"><%=lieu %></th>
	           <%}
	           i++;
	           }%>
	           </tr>
	           
	           	<% 
	           	
	           	String[] borderColors = {"blue", "orange", "red", "green", "#c2185b", "#544901"};
	           	String oldM = null;
	           	int idxColeur = 0;
	           	while(dateDebut.before(dateFin)){
	           		String currM = DateUtil.dateToString(dateDebut, "MM");
	           		Date startDate = DateUtil.getStartOfDay(dateDebut);
	           		Date endDate = DateUtil.getEndOfDay(dateDebut);
	           		String dtDay = DateUtil.dateToString(dateDebut, "dd/MM");
	           		
	           		if(oldM!=null && !oldM.equals(currM)){
	           			idxColeur++;
	           			
	           			if(idxColeur == colors.length-1){
	           				idxColeur = 0;
	           			}
	           		}
	           	%>
	           		<tr>
	           		  <td style='color:<%=colors[idxColeur] %>;font-weight:bold;'>
	           			<%=dtDay %>
	           		  </td>
	           		
	           		<% 
	           		i = 0;
	           		for(String plan : mapLieu.keySet()){
	           			List<String> listDet = mapLieu.get(plan);
	           			
	           			if(i == boColors.length-1){
	     	        		i = 0;
	     	        	}
	           			
	           			for(String lieu : listDet){
	           		%>
	           				<td class="td-cal-bloc" dt="<%=DateUtil.dateToString(dateDebut, "yyyy-MM-dd") %>" lieu="<%=lieu %>" style="background-color: <%=boColors[i]%>;cursor:pointer;">
	           				<%
	           				int idxBorCol = 0;
	           				for(PlanningPersistant planP : list_planning){
	           					String borderColor = borderColors[idxBorCol];
	           					idxBorCol++;
	           					
	           					if(idxBorCol > borderColors.length-1){
	           						idxBorCol = 0;
	           					}
		           				if(planP.getLieu_str().indexOf(";"+lieu+";") != -1 
		           						
		           						&& endDate.after(planP.getDate_debut())
		           						&& planP.getDate_fin().after(startDate)){%>
		           					
		           					<div class="dt-cal-bloc" style="border-left:3px solid <%=borderColor%>;background-color:<%=planP.getOpc_type_planning().getColor() %>;" onclick="$('#link-popup').attr('params', 'src=lieu&id=<%=planP.getId()%>').trigger('click');">
		           						<span style="color:blue;">
		           							<%=DateUtil.dateToString(planP.getDate_debut(), "dd/MM HH:mm") %>
		           							=> <%=DateUtil.dateToString(planP.getDate_fin(), "dd/MM HH:mm") %>
		           						</span>
		           						<br>
		           						<span style="color:gray;">
		           							<%=planP.getTitre() %>
		           						</span>
		           						<span style="color:orange;">
		           							<%=StringUtil.getValueOrEmpty(planP.getDescription()) %>
		           						</span>
	           						</div>	
		           				<%}
	           				}%>
	           			</td>
	           			<%
	           			}
	           			i++;
	           		}%>
	           		
	           		<%
	           		oldM = currM;
	           		dateDebut = DateUtil.addSubstractDate(dateDebut, TIME_ENUM.DAY, 1);	
	           	} %>
	           </tr>
           </table>
				           </div>
				       </div>
				   </div>
				</div>
           </div>
       </div>
    </div>
</div>

<!-- end widget div -->
