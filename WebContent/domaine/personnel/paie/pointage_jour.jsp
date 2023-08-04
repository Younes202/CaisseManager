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
   		line-height: 35px;
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
		// Add fix col
		var i = 0;
   		$('.tbl-pointage tr').find('td:eq(' + 0 + ')').each(function(){
   			var cloneTh = $(this).clone(true).addClass('th-static').css("height", ($(this).height()+3)+"px");
   			$('.tbl-pointage').find('tr').eq(i).find('td').eq(0).before(cloneTh);
   			i++;
   		});
   			
		// Scroll position	
		$('.tbl-pointage').closest("div").animate({
		    scrollLeft: $('.tbl-pointage td[idx="'+(currIdx>=5 ? currIdx-5 : currIdx)+'"]').offset().left
		   }, 'slow');
		// Color current day
		$('.tbl-pointage tr').find('td:eq(' + currIdx + ')').css('background-color', "#ffdead");
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
<div class="page-header position-relative">
	<div class="header-title" style="top: 4px;display: inline;">
		<% request.setAttribute("mnuTop", "J"); %>
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
                   	
<%--                    	<jsp:include page="/domaine/personnel/paie/header_pagger_include.jsp" /> --%>
                   	
			<div id="table-scroll" class="row" style="overflow: auto;">	
				<std:linkPopup action="pers.pointage.loadDataJour" id="pointage-lnk" style="display:none;" />		
		
			<table class="tbl-pointage">
				<tr>
					<td style="width: 60px;border:0px;"></td>
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
						<td idx="<%=i %>" style='background-color: <%=listIdx.contains(i) ?"red":"#e1e1e1;" %>'><%=day %></td>
					<%} %>	
				</tr>
				<%
				List<EmployePersistant> listEmployeCombo = (List<EmployePersistant>)request.getAttribute("listEmployePagger");
				for(EmployePersistant employeP : listEmployeCombo){
					String key = employeP.getNumero()+"-"+employeP.getNom()+" "+StringUtil.getValueOrEmpty(employeP.getPrenom());
					Map<String, List<PointageEventPersistant>> mapDetailHoraire = mapHorairePointage.get(key);
					Map<String, Long> mapTotalHoraire = mapTotalHorairePointage.get(key);
				%> 
					<tr>
						<td style="width: 60px;border:0px;"></td>
						<%for(int i=1; i<=maxDay; i++){ %>
							<td idx="<%=i %>" style='background-color: <%=listIdx.contains(i) ?"red":"#c7c7c7;"%>'><%=(i<10) ? "0"+i : i %></td>
						<%} %>	
					</tr>
				
					<tr>
						<td style="border: 0px;"></td>
						<td style="background-color: black;font-size: 15px;text-align: left;padding-left: 64px;color: white;font-weight: bold;" colspan="<%=maxDay%>">
							<span style="position: sticky;left: 70px;">
								<%=employeP.getNumero()+"-"+employeP.getNomPrenom() %>
								<%if(employeP.getOpc_poste() != null){ %>
									[<%=employeP.getOpc_poste().getIntitule() %>]								
								<%} %>
							</span>
						</td>
					</tr>
					<tr>
						<td style="background-color: #6d6d78;font-weight: bold;color: white;">Pointage</td>
						<%for(int i=1; i<=maxDay; i++){
							String stIdx = (i<10 ? "0"+i : ""+i);
							String dt = stIdx+"/"+dateDebutStr;
							List<PointageEventPersistant> listDetail = mapDetailHoraire.get(stIdx);
							Long periode = mapTotalHoraire.get(stIdx);
						%>
							<td style="width:100px;vertical-align: bottom;" empl="<%=employeP.getId() %>" tpp="hr" ttl="Temps travaillé" dt='<%=dt%>'>
								<%if(listDetail != null){
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
										} %>
									 <%
									x++; 
									}
								} %>
								<span style="line-height: 5px;height: 5px;width: 100px;float: left;"></span>
								<%
								if(periode != null && periode>0){
									long h = periode / 60 % 24;
									long min = h > 0 ? (periode % 60) : periode;
									String valDuree = (h>0 ? h+"h ":"")+(min>0?min+"min":"");%>
									<span style="font-weight: bold;
										    background-color: #e7dada;
										    width: 100px;
										    float: left;
										    height: 14px;
										    line-height: 14px;"><%=valDuree %></span>					
								<%}%>
						<%} %>
						</td>
					</tr>
					<tr>
						<td style="background-color: blue;font-weight: bold;color: white;">Travail</td>
						<%for(int i=1; i<=maxDay; i++){ 
							String dt = (i<10 ? "0"+i : i)+"/"+dateDebutStr;
							PointagePersistant pointage = mapDataPointage.get(employeP.getId()+"-tr-"+dt);
						%>
							<td empl="<%=employeP.getId() %>" tpp="tr" ttl="Temps travaillé" dt='<%=dt%>'>
								<%=(pointage!=null ? pointage.getDureeStr() : "") %>
							</td>
						<%} %>
					</tr>
					<tr>
						<td style="background-color: #ccc;font-weight: bold;color: white;">Congé</td>
						<%for(int i=1; i<=maxDay; i++){ 
							String dt = (i<10 ? "0"+i : i)+"/"+ControllerUtil.getMenuAttribute("CURR_DATE", request);
							PointagePersistant pointage = mapDataPointage.get(employeP.getId()+"-cg-"+dt);
						%>
							<td empl="<%=employeP.getId() %>" dt='<%=dt%>' tpp="cg" ttl=Congé dt='<%=dt%>'>
								<%=(pointage!=null ? pointage.getDureeStr() : "") %>
							</td>
						<%} %>
					</tr>
					<tr>
						<td style="background-color: green;font-weight: bold;color: white;">Prime</td>
						<%for(int i=1; i<=maxDay; i++){ 
							String dt = (i<10 ? "0"+i : i)+"/"+dateDebutStr;
							PointagePersistant pointage = mapDataPointage.get(employeP.getId()+"-pr-"+dt);
						%>
							<td empl="<%=employeP.getId() %>" tpp="pr" ttl="Prime" dt='<%=dt%>'>
								<%=(pointage!=null ? BigDecimalUtil.formatNumberZeroBd(pointage.getMontant()) : "") %>
							</td>
						<%} %>
					</tr>
					<tr>
						<td style="background-color: orange;font-weight: bold;color: white;">Avance</td>
						<%for(int i=1; i<=maxDay; i++){ 
							String dt = (i<10 ? "0"+i : i)+"/"+dateDebutStr;
							PointagePersistant pointage = mapDataPointage.get(employeP.getId()+"-av-"+dt);
						%>
							<td empl="<%=employeP.getId() %>" tpp="av" ttl="Avance" dt='<%=dt%>'>
								<%=(pointage!=null ?  BigDecimalUtil.formatNumberZeroBd(pointage.getMontant()) : "") %>
							</td>
						<%} %>
					</tr>
					<tr>
						<td style="background-color: red;font-weight: bold;color: white;">Retenue</td>
						<%for(int i=1; i<=maxDay; i++){ 
							String dt = (i<10 ? "0"+i : i)+"/"+dateDebutStr;
							PointagePersistant pointage = mapDataPointage.get(employeP.getId()+"-rt-"+dt);
						%>
							<td empl="<%=employeP.getId() %>" tpp="rt" ttl="Retenue" dt='<%=dt%>'>
								<%=(pointage!=null ?  BigDecimalUtil.formatNumberZeroBd(pointage.getMontant()) : "") %>
							</td>
						<%} %>
					</tr>
					<tr>
						<td style="background-color: #e98484;font-weight: bold;color: white;">Discipline</td>
						<%for(int i=1; i<=maxDay; i++){ 
							String dt = (i<10 ? "0"+i : i)+"/"+dateDebutStr;
							PointagePersistant pointage = mapDataPointage.get(employeP.getId()+"-ds-"+dt);
							String val = "";
							if(pointage != null){
								if(StringUtil.isNotEmpty(pointage.getDescription())){
									val = pointage.getDescription();
								}
								if(StringUtil.isNotEmpty(pointage.getCommentaire())){
									val = val + " / "+ pointage.getCommentaire();
								}
							}
						%>
							<td empl="<%=employeP.getId() %>" tpp="ds" ttl="Discipline" dt='<%=dt%>' class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title='<%=val%>'>
								<%=(pointage!=null ? "..." : "") %>
							</td>
						<%} %>
					</tr>	
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