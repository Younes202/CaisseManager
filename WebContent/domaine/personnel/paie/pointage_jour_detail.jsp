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
	.tbl-pointage{
		display: block;
		width: 98%;
		margin: 14px;
      	overflow-y: auto;
	}
	.tbl-pointage tr{
   		line-height: 35px;
   	}
	.tbl-pointage td{
	    border: 1px solid #a0d468;
   		text-align: center;
   	}
   	.tbl-pointage td:HOVER{
   		background-color: #f3ffff;
   		cursor: pointer;
   	}
    .tbl-pointage thead th {
      position: sticky;
      top: 0;
    }
    .span-td-2{
	    font-weight: bold;
	    color: gray;
	    text-align: center;
	    line-height: 13px;
	    background-color: #efefef;
	    width: 100%;
	    float: left;
	    padding: 0px 0px 3px 0px;
	    margin-top: 3px;
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
int currDay = DateUtil.getCalendar(new Date()).get(Calendar.DAY_OF_MONTH)+1;

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
		var currIdx = <%=currDay%>;
   		
		// Scroll position
		var det = $('.tbl-pointage td[idx="'+(currIdx>=5 ? currIdx-5 : currIdx)+'"]');
		$('.tbl-pointage').closest("div").animate({
		    scrollLeft: (det && det.offset()) ? det.offset().left : 0
		   }, 'slow');
		// Color current day
		$('.tbl-pointage tr').find('td:eq(' + currIdx + ')').css('background-color', "#ffdead");
		
		setTimeout(() => {
			$(".tbl-pointage").css("height", (window.innerHeight-170)+"px");
		}, 500);
		
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
<!-- /Page Breadcrumb -->
<!-- Page Header -->

<std:form name="search-form">

<div class="page-header position-relative">
	<div class="header-title" style="top: 4px;display: inline;">
		<% request.setAttribute("mnuTop", "JD"); %>
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
            <div class="row">
              <div class="col-lg-12 col-md-12">
              	<div class="widget">
                   <div class="widget-body">
			<div id="table-scroll" class="row" style="overflow: auto;">	
				<std:linkPopup action="pers.pointage.loadDataJour" id="pointage-lnk" style="display:none;" />		
		
			<table class="tbl-pointage">
			<thead>
				<tr>
					<th style="border:0px;"></th>
					<%
					String CURR_DATE = (String)ControllerUtil.getMenuAttribute("CURR_DATE", request);
					List<Integer> listIdx = new ArrayList<>();
					for(int i=1; i<=maxDay; i++){ 
						Date dateD = DateUtil.stringToDate(((i<10) ? "0"+i : i)+"/"+CURR_DATE);
						String day = DateUtil.dateToString(dateD, "EEE");
						
						if(day.startsWith("dim")){
							listIdx.add(i);
						}
					%>
						<th idx="<%=i %>" style='border: 1px solid #03a9f4;text-align:center;background-color: <%=listIdx.contains(i) ?"red":"#e1e1e1;" %>'><%=day %></th>
					<%} %>	
				</tr>
				<tr>
					<td style="border:0px;"></td>
					<%for(int i=1; i<=maxDay; i++){ %>
						<th idx="<%=i %>" style='border: 1px solid #03a9f4;text-align:center;background-color: <%=listIdx.contains(i) ?"red":"#c7c7c7;"%>'><%=(i<10) ? "0"+i : i %></th>
					<%} %>	
				</tr>
			</thead>
			<tbody>	
				<%
				List<EmployePersistant> listEmployeCombo = (List<EmployePersistant>)request.getAttribute("listEmployePagger");
				for(EmployePersistant employeP : listEmployeCombo){
					String key = employeP.getNumero()+"-"+employeP.getNom()+" "+StringUtil.getValueOrEmpty(employeP.getPrenom());
					Map<String, List<PointageEventPersistant>> mapDetailHoraire = mapHorairePointage.get(key);
					Map<String, Long> mapTotalHoraire = mapTotalHorairePointage.get(key);
					%> 
					<tr>
						<td style="background-color: black;font-size: 11px;text-align: left;color: white;min-width: 170px;">
							<span style="position: sticky;padding-left: 2px;">
								<%=employeP.getNumero()+"-"+employeP.getNomPrenom() %>
								<%if(employeP.getOpc_poste() != null){ %>
									[<%=employeP.getOpc_poste().getIntitule() %>]								
								<%} %>
							</span>
						</td>
					<%
					/*Map<Integer, StringBuilder> mapResume = new LinkedHashMap<>();
					for(int i=1; i<=maxDay; i++){
						mapResume.put(i, new StringBuilder());
					}*/
									
					for(int i=1; i<=maxDay; i++){
						String stIdx = (i<10 ? "0"+i : ""+i);
						String dt = stIdx+"/"+dateDebutStr;
						List<PointageEventPersistant> listDetail = mapDetailHoraire.get(stIdx);
						Long periode = mapTotalHoraire.get(stIdx);
						%>
						<td style='vertical-align: bottom;<%=(listDetail != null ? "width: 100px;":"")%>'>	
						<%
						if(listDetail != null){
							int x = 0;
							for(PointageEventPersistant detailP : listDetail){ %>
								<span style="line-height: 5px;width: 47px;float: left;">
									<i class="fa fa-clock-o" style="color: red;"></i> 
									<%=DateUtil.dateToString(detailP.getDate_pointage(), "HH:mm") %>
								</span>
								<%if(x == 1){ %>
									<span style="line-height: 5px;height: 5px;width: 100px;float: left;"></span>
								<%
									x = -1;
								}
								x++; 
							}
						} %>
						<%
						// Pointage
						if(periode != null && periode>0){%>
							<span style="line-height: 5px;height: 5px;width: 100px;float: left;"></span>
						<%
							long h = periode / 60 % 24;
							long min = h > 0 ? (periode % 60) : periode;
							String valDuree = (h>0 ? h+"h ":"")+(min>0?min+"min":"");
							%>
							<span class='span-td-2'><%=valDuree %></span>
							<%
						}
					}
					%>					
				</tr>
			  <%} %>
			  </tbody>				
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