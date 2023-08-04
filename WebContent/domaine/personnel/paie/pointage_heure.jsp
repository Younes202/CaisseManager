<%@page import="java.util.Iterator"%>
<%@page import="framework.model.common.util.DateUtil.TIME_ENUM"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="appli.model.domaine.personnel.persistant.paie.PointageEventPersistant"%>
<%@page import="appli.model.domaine.personnel.persistant.EmployePersistant"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.util.audit.ReplicationGenerationEventListener"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="appli.model.domaine.personnel.persistant.paie.PointagePersistant"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Calendar"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="java.util.Date"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
	.ui-dialog .ui-dialog-titlebar{
	    background-color: #c2b99b !important;
	}
	.ui-dialog .ui-dialog-buttonpane{
	    background: #e6e6e6 !important; 
   }
   .th-static{
   		position: absolute;
   		width: 60px;
   }
   	.tbl-pointage{
		width: 98%;
		margin: 14px;
	}
	.tbl-pointage td{
	    border: 1px solid #a0d468;
   		text-align: center;
   	}
   	.tbl-pointage td:HOVER{
   		background-color: #f3ffff;
   		cursor: pointer;
   	}
   	.tbl-pointage tr{
   		line-height: 25px;
   	}
</style>

<%
boolean isPointeuse = (StringUtil.isNotEmpty(ContextGloabalAppli.getEtablissementBean().getPointeuse_db_path()) || 
		(StringUtil.isNotEmpty(ContextGloabalAppli.getEtablissementBean().getPointeuse_ip()) 
		&& StringUtil.isNotEmpty(ContextGloabalAppli.getEtablissementBean().getPointeuse_port())));

String devise = "&nbsp;"+StrimUtil.getGlobalConfigPropertie("devise.symbole");
Date dateDebut = (Date)ControllerUtil.getMenuAttribute("CURR_DATE_DT", request);
String dateDebutStr = (String)ControllerUtil.getMenuAttribute("CURR_DATE", request);
int maxDay = DateUtil.getCalendar(dateDebut).getActualMaximum(Calendar.DAY_OF_MONTH);
String currHour = DateUtil.dateToString(new Date(), "HH")+":00";

Map<String, PointagePersistant> mapDataPointage = (Map<String, PointagePersistant>)request.getAttribute("mapDataPointage");

Map<String, Map<String, List<PointageEventPersistant>>> mapHorairePointage = (Map<String, Map<String, List<PointageEventPersistant>>>)request.getAttribute("mapHorairePointage");
Map<String, Map<String, Long>> mapTotalHorairePointage = (Map<String, Map<String, Long>>)request.getAttribute("mapTotalHorairePointage");
%>

<script type="text/javascript">
	$(document).ready(function (){
		$(".tbl-pointage td[dt]").click(function(){
				$("#pointage-lnk").attr("params", "empl="+$(this).attr("empl")+"&dt="+$(this).attr("dt")+"&ttl="+$(this).attr("ttl")+"&tpp="+$(this).attr("tpp"));
				$("#pointage-lnk").trigger("click");
		});
		manageShowtrTime();
	});
	
	function manageShowtrTime(){
		var currIdx = "<%=currHour%>";
		// Scroll position	
		$('.tbl-pointage').closest("div").animate({
		    scrollLeft: $('.tbl-pointage td[idx="'+currIdx+'"]').last().offset().left
		   }, 'slow');
		
		var idx = $('.tbl-pointage td[idx="'+currIdx+'"]').last().index();
		// Color current day
		$('.tbl-pointage tr').find('td:eq('+idx+')').css('background-color', "#ffdead");
	}
</script>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des personnels</li>
		<li class="active">Pointage</li>
	</ul>
</div>

<std:form name="search-form">
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="top: 4px;display: inline;">
		<% request.setAttribute("mnuTop", "H"); %>
		<jsp:include page="/domaine/personnel/paie/header_btn_include.jsp" />
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

	<!-- row -->
	<div class="row" style="margin:0px;">
	
<!-- Row Start -->
            <div class="row">
              <div class="col-lg-12 col-md-12">
              	<div class="widget">
                   <div class="widget-body">
                   	
			<div id="table-scroll" class="row" style="overflow: auto;">	
				<std:linkPopup action="pers.pointage.loadDataJour" id="pointage-lnk" style="display:none;" />		
		
			<table class="tbl-pointage">
				<tr>
					<td style="width: 60px;border:0px;"></td>
					<%
					String CURR_DATE = (String)ControllerUtil.getMenuAttribute("CURR_DATE", request);
					Date currDate = DateUtil.stringToDate(CURR_DATE);
					Date beforeDate = DateUtil.addSubstractDate(currDate, TIME_ENUM.HOUR, -12);
					Date afterDate = DateUtil.getEndOfDay(currDate);
					Date startDate = beforeDate;
					List<Integer> listIdx = new ArrayList<>();
					int i = 0;
					String oldDay = null;
					while(startDate.before(afterDate)){
						String day = DateUtil.dateToString(startDate, "EEE");
						String h = DateUtil.dateToString(startDate, "HH:mm");
						
						if(day.startsWith("dim")){
							listIdx.add(i);
						}
					%>
						<td idx="<%=h %>" style='background-color: <%=listIdx.contains(i) ?"red":"#e1e1e1" %>;font-size:10px;'><%=(oldDay == null || !oldDay.equals(day)) ? day : ""%></td>
					<%
					startDate = DateUtil.addSubstractDate(startDate, TIME_ENUM.HOUR, 1);
					i++;
					oldDay = day;
					} %>	
				</tr>
				<tr>
					<td style="width: 60px;border:0px;"></td>
					<%
					startDate = beforeDate;
					while(startDate.before(afterDate)){
						String h = DateUtil.dateToString(startDate, "HH:mm");
					%>
						<td idx="<%=h %>" style='background-color: <%=listIdx.contains(i) ?"red":"#c7c7c7"%>;font-size:10px;'><%=h %></td>
					<%
						startDate = DateUtil.addSubstractDate(startDate, TIME_ENUM.HOUR, 1);
					} %>	
				</tr>
				
				<%
				List<EmployePersistant> listEmployeCombo = (List<EmployePersistant>)request.getAttribute("listEmployePagger");
				for(EmployePersistant employeP : listEmployeCombo){
					String key = employeP.getNumero()+"-"+employeP.getNom()+" "+StringUtil.getValueOrEmpty(employeP.getPrenom());
					Map<String, List<PointageEventPersistant>> mapDetailHoraire = mapHorairePointage.get(key);
					Map<String, Long> mapTotalHoraire = mapTotalHorairePointage.get(key);
					
					String h1 = DateUtil.dateToString(beforeDate, "dd");
					String h2 = DateUtil.dateToString(afterDate, "dd");
					
					Long periode1 = (mapTotalHoraire!=null ? mapTotalHoraire.get(h1) : null);
					Long periode2 = (mapTotalHoraire != null ? mapTotalHoraire.get(h2) : null);
					Long periode = (periode1 != null ? periode1 : periode2);

					String valDuree = "";
					if(periode != null && periode>0){
						long h = periode / 60 % 24;
						long min = h > 0 ? (periode % 60) : periode;
						valDuree = (h>0 ? h+"h ":"")+(min>0?min+"min":"");
					}

				%> 
					<tr>
						<td style="background-color: black;font-size: 10px;text-align: left;padding-left: 2px;color: white;font-weight: normal;min-width: 150px;">
							<span style="position: sticky;">
								<%=employeP.getNumero()+"-"+employeP.getNomPrenom() %>
								<%if(employeP.getOpc_poste() != null){ %>
									[<%=employeP.getOpc_poste().getIntitule() %>]								
								<%} 
								if(StringUtil.isNotEmpty(valDuree)){ %>
									[<%=valDuree %>]
								<%} %>
							</span>
						</td>
						<%
						List<PointageEventPersistant> listDetail = null;

						if(mapDetailHoraire!=null){
							listDetail = mapDetailHoraire.get(h1);
							if(listDetail == null){
								listDetail = new ArrayList<>();
							}
							if(mapDetailHoraire.get(h2) != null){
								listDetail.addAll(mapDetailHoraire.get(h2));
							}
						}
												
						Date oldStart = null;
						int col = 0;
						startDate = beforeDate;
						while(startDate.before(afterDate)){
							col++;
							boolean isInInterval = false;
							String style = "";
							String lib = "";
							//
							if(listDetail != null){
								for (Iterator<PointageEventPersistant> iterator = listDetail.iterator(); iterator.hasNext();) {
									PointageEventPersistant detailP = iterator.next();	
									Date datePStart = detailP.getDate_pointage();
									Date datePEnd = (iterator.hasNext() ? iterator.next().getDate_pointage() : detailP.getDate_pointage());
									
									valDuree = "";
									int duree = DateUtil.getDiffMinuts(datePStart, datePEnd);
									if(duree > 0){
										long h = duree / 60 % 24;
										long min = h > 0 ? (duree % 60) : duree;
										valDuree = (h>0 ? h+"h ":"")+(min>0?min+"min":"");
									}
									
									if((startDate.compareTo(datePStart)==0 || startDate.after(datePStart)) 
												&& (/*startDate.compareTo(datePEnd)==0 || */startDate.before(datePEnd))){
										isInInterval = true;
										if(oldStart == null || oldStart.compareTo(datePStart) != 0){
											lib = DateUtil.dateToString(datePStart, "HH:mm")+" à "+DateUtil.dateToString(datePEnd, "HH:mm")+" ["+valDuree+"]";
										} else{
											lib = "";
										}
										
										oldStart = datePStart;
										
										break;
									}
								}
							}
							style = (isInInterval ? "background-color: #4caf50;" : "");
							
							%>
							<td style='min-width:30px;<%=style%>'>
								<%if(StringUtil.isNotEmpty(lib)){ %>
									<i class="fa fa-clock-o" data-toggle="tooltip" data-placement="top" data-original-title="<%=lib %>"></i>
								<%} %>
							</td>
							<%
							startDate = DateUtil.addSubstractDate(startDate, TIME_ENUM.HOUR, 1);
						} %>
						
					</tr>
					<tr style="height: 10px;"><td colspan="<%=col+1%>"></td></tr>
				<%} %>				
			</table>
		</div>
		</div>
		</div>
		</div>
			</div>
		</div>
	</div>
</std:form>


<script type="text/javascript">
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script> 