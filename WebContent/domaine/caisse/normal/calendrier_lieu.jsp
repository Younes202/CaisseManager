
<%@page import="java.util.ArrayList"%>
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
	  #cal-tbl th{
	    background: white;
  		position: sticky;
  		top: 0; /* Don't forget this, required for the stickiness */
  		box-shadow: 0 2px 2px -1px rgba(0, 0, 0, 0.4);
  	}
</style>
<script type="text/javascript">
$(document).ready(function () {
	var ratioW = -38;
	var vH = 110;
	var vW = 0;
	var dataZoom = readLocalStorage('zoom_cai_cock');
	
	if(dataZoom && dataZoom!=null && dataZoom!='' && dataZoom!='1'){
		if(dataZoom == '0.9'){
			ratioW = 125;
			vH = 30;
			vW = 160;
		} else if(dataZoom == '0.85'){
			ratioW = 325;
			vH = -30;
			vW = 260;
		} else if(dataZoom == '0.8'){
			ratioW = 325;
			vH = -70;
			vW = 365;
		} else if(dataZoom == '0.7' || dataZoom == '0.75'){
			ratioW = 585;
			vH = -100;
			vW = 480;
		} else if(dataZoom == '0.6'){
			ratioW = 750;
			vH = -300;
			vW = 1000;
		} else if(dataZoom == '0.5'){
			ratioW = 800;
			vH = -400;
			vW = 1500;
		}
	}
		
	$("#row_dt_dates").css("height", "calc(100vh - "+vH+"px").css("width", "calc(100vw + "+vW+"px");
	
	$("#left-div").hide();
	$("#right-div").css("width", (window.innerWidth+ratioW)+"px");
	
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

<div class="row" style="margin-left: 0px;margin-right: 0px;">
   <div class="widget flat">
       <div class="widget-body">
       		<div class="row" style="margin-left: 0px;margin-bottom: 10px;">
       		
       			<div class="col-md-3">
		       		<std:linkPopup actionGroup="C" id="add-button" classStyle="btn btn-default" params="src=lieu" action="caisse-web.calendrier.work_init_create" icon="fa-3x fa-plus" value="Réservation" tooltip="Ajouter réservation" />
		       		<std:link actionGroup="C" classStyle="btn btn-primary" action="caisse-web.calendrier.work_find" icon="fa-3x fa-map" value="Vue date" tooltip="Vue date" targetDiv="right-div" />
		       		<std:linkPopup style="display:none;" id="link-popup" action="caisse-web.calendrier.work_edit" />
	        	</div>
	        	<div class="col-md-9">
		        	<std:label classStyle="control-label col-md-2" value="Date début" />
		            <div class="col-md-2">
		                 <std:date name="dateDebut" value="${dateDebut }"/>
		            </div>
		            <div class="col-md-2" style="text-align: center;">
		            	<std:link action="caisse-web.calendrier.vue_lieu" targetDiv="right-div" params="prev=1" icon="fa fa-arrow-circle-left" tooltip="Mois précédent" />
		            	<std:link action="caisse-web.calendrier.vue_lieu" targetDiv="right-div" params="next=1" icon="fa fa-arrow-circle-right" tooltip="Mois suivant" />
		            </div>
		            
		            <std:label classStyle="control-label col-md-1" value="Date fin" />
		            <div class="col-md-2">
		                 <std:date name="dateFin" value="${dateFin }"/>
		            </div>
		            <div class="col-md-2">
		           	 	<std:button action="caisse-web.calendrier.vue_lieu" value="Filtrer" classStyle="btn btn-primary" targetDiv="right-div" />
		           	 </div>	
	       		</div>
	  	 </div>
           
           <div class="row" id="row_dt_dates" style="overflow-y: auto;">
           <table id="cal-tbl" class="table table-bordered table-striped table-condensed flip-content" style="width: 98%;margin-left: 1%;">
	           
	           <%
	           List<String> listTdDay = new ArrayList<>();
	           Date dtDeb = dateDebut;
	           while(dtDeb.before(dateFin)){
	        	   String dtDay = DateUtil.dateToString(dtDeb, "EEE dd/MM");
	        	   listTdDay.add("<span style='color:#3f51b5;'>"+dtDay+"</span>");
	        	   dtDeb = DateUtil.addSubstractDate(dtDeb, TIME_ENUM.DAY, 1);
	           }
	           %>
	           
	           <tr>
	           		<th></th>
		           <% 
		           String[] colors = {"#141314", "#f57c00", "#1976d2", "#c2185b", "gray", "yellow", "green"};
		           String[] boColors = {"#ffffff", "#fff7ec", "#d7fbff", "#fdffea", "#fff1f6", "#e4e4e4", "#fffbd6"};
		           int i = 0;
		           for(String plan : mapLieu.keySet()){ 
		        	   List<String> listDet = mapLieu.get(plan);
		        	   if(i == boColors.length-1){
		        		   i = 0;
		        	   }
		           %>
		           		<th style="font-weight: bold;text-align: center;background-color: <%=boColors[i]%>;" colspan="<%=listDet.size()%>"><%=plan %></th>
		           		<th></th>
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
	           <%}%>

          		<th></th>
          	
          		<%	
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
	           		String dtDay = DateUtil.dateToString(dateDebut, "EEE dd/MM");
	           		
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
	           			int idxJ = 0;
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
	           			
	           			if(listTdDay.size()-1 > idxJ){
	           			%>
	           				<%="<td>"+listTdDay.get(idxJ)+"</td>" %>
	           			<%
	           			}
	           			idxJ++;
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
