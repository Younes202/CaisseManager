<%@page import="appli.model.domaine.personnel.service.ISalariePaieService"%>
<%@page import="appli.model.domaine.personnel.persistant.paie.SalairePersistant"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.NumericUtil"%>
<%@page import="framework.model.common.constante.ProjectConstante"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.component.complex.table.RequestTableBean"%>
<%@page import="appli.model.domaine.personnel.persistant.EmployePersistant"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<style>
.tdWritable{

}
.add_tarif:HOVER{
		background-color: #ffd9d9;
	}
.tdWritable:HOVER{
	background-color: #ffd9d9;
}
.data_td{
	border-bottom-color: orange;
	border-bottom-style: solid;
	width: 100%;
    border-bottom-width: 1px;
}
</style>

<script type="text/javascript">
	$(document).ready(function (){
		$(document).off('click', '#paie_table_body .add_tarif');
		$(document).on('click', '#paie_table_body .add_tarif', function(){
			var empl = $(this).closest("tr").attr("empl");
			var dt = $(this).attr("dt");
			var det = $(this).attr("det");
			
			$("#lnk_saisie_paie").attr("params", "empl="+empl+"&dt="+dt+"&det="+det).trigger("click");
		});
		
		
		$(document).off('click', 'a[id="del_paie_lnk"]');
		$(document).on('click', 'a[id="del_paie_lnk"]', function(){
			showConfirmDeleteBox('<%=EncryptionUtil.encrypt("paie.employePaie.deleteMassePaie")%>', $(this).attr("params"), $(this), "Vous êtes sur le point de supprimer des jours de paie.<br>Voulez-vous confirmer ?", null, "Suppression des jours de paie");
		});
		
		$(document).off('click', 'td[id^="td_"]');
		$(document).on('click', 'td[id^="td_"]', function(){
			var empl = $(this).attr("empl");
			var idxMois = $(this).attr("idxMois");
			
			if($(this).attr("mode") == 'C'){
				$("#td_paieC_lnk").attr("params", "empl="+empl+"&idxMois="+idxMois).trigger("click");				
			} else{
				$("#td_paie_lnk").attr("params", "empl="+empl+"&idxMois="+idxMois).trigger("click");
			}
		});	
	});
</script>	

<%
String dt = ""+ControllerUtil.getMenuAttribute("CURR_DATE", request);
if(dt.indexOf("/") != -1) {
	dt = dt.substring(dt.indexOf("/")+1);
}
Integer currAnnee = Integer.valueOf(dt);

ISalariePaieService service = (ISalariePaieService)ServiceUtil.getBusinessBean(ISalariePaieService.class);
Date today = new Date();
List<EmployePersistant> listEmploye = (List<EmployePersistant>)request.getAttribute("listEmploye");
List<EmployePersistant> listEmployePagger = (List<EmployePersistant>)request.getAttribute("listEmployePagger");
Map<String, String> mapMois = (Map)request.getAttribute("mapMois");
Map<String, SalairePersistant> mapPaie = (Map<String, SalairePersistant>)request.getAttribute("mapPaie");
Map<Integer, Boolean> mapEtatMois = (Map<Integer, Boolean>)request.getAttribute("mapEtatMois");

%>

<!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion des paies</li>
         <li class="active">Fiche paie employé</li>
     </ul>
 </div>

<std:form name="search-form">
  <div class="page-header position-relative">
  	  <div class="header-title" style="padding-top: 4px;width: 100%;">
  	  		<% request.setAttribute("mnuTop", "M"); %>
			<jsp:include page="/domaine/personnel/paie/header_btn_include.jsp" />
  	  </div>
      <!--Header Buttons-->
      <jsp:include page="/WEB-INF/fragment/shortcut.jsp" />
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
			<std:linkPopup action="paie.salariePaie.work_edit" id="td_paie_lnk" style="display:none;" />
			<std:linkPopup action="paie.salariePaie.work_init_create" id="td_paieC_lnk" style="display:none;" />
			
			<c:set var="dateUtil" value="<%=new DateUtil() %>" />
		
		     <!-- Row Start -->
            <div class="row">
              <div class="col-lg-12 col-md-12">
              	<div class="widget">
<!--                    <div class="widget-header"> -->
<!--                        <span class="widget-caption"> -->
<%--                        		Saisie pour l'année <b><%=currAnnee %></b> --%>
<!--                        </span> -->
<!--                    </div>Widget Header -->
                   <div class="widget-body">
                       <div class="table-responsive">
                       
                      <table class="table table-condensed table-striped table-bordered table-hover no-margin" id="paie_table_body">
                         
                         <%for(EmployePersistant employeB : listEmployePagger){ 
                         	
                         	%>
	                        <thead>
	                        	<tr>
		                            <th rowspan="2" style="vertical-align: middle;background-color: #efefef;">
		                            </th>
		                            <th rowspan="2" style="width: 55px;">
		                            	Type
		                            </th>
		                            
		                            <%for(String idxMois : mapMois.keySet()){ %>
			                            <th style="width:75px;text-align:center;color: red;" class="hidden-phone add_tarif">
				                            <%=idxMois %>
				                            <%
				                            SalairePersistant currPaie = mapPaie.get(employeB.getId()+"-"+Integer.valueOf(idxMois).intValue());
				                            if(currPaie != null){
					                            if(currPaie.getDate_paiement() != null){ %>
					                            	<std:link action="paie.salariePaie.downloadEmployePaiePDF" params='<%="empl="+employeB.getId()+"&mode=oneMonth&annee="+currAnnee+"&mois="+idxMois %>' target="downloadframe" tooltip="Editer la paie" classStyle="fa fa-file-pdf-o" />
					                            <%}
				                            }%>
				                            <std:link classStyle="" params='<%="mois="+idxMois %>' icon="fa-refresh" action="paie.salariePaie.calculSalaireFromPointage" tooltip="Calculer les salaires depuis le pointage" />
			                            </th>
		                           <%} %>
		                          </tr>

		                          <tr>
		                          	
		                           <%for(String idxMois : mapMois.keySet()){ 
		                           		SalairePersistant currPaie = mapPaie.get(employeB.getId()+"-"+Integer.valueOf(idxMois).intValue());
		                           		%>
		                           		<th style="width:50px;text-align:center;
		                           			font-weight: normal;color: white;background-color: black;" class="hidden-phone">
		                           			<%if(currPaie != null) {
			                           			if(currPaie.getDate_paiement() != null){
			                           			%>
			                           				<i class="fa fa-check-square-o"></i>
			                           			<%}
			                           		}%>
		                           			<%=mapMois.get(idxMois) %>
		                           		
		                           		</th>
		                           <%} %>

		                          </tr>
	                        </thead>
	                        
	                        <tbody>
		                        <!-- Poster les données -->
		                        <tr>
		                        	<td style="background-color: #b1b1b1;vertical-align: middle;text-align: left;font-weight: bold;font-size: 15px;">
								    	<span style='<%=employeB.getDate_sortie()!=null ? "text-decoration:line-through;color:#B3B3B3;":""%>'>
								      	<%=employeB.getIdx() %> - <%=employeB.getNomPrenom() %> 
								      	<span style="color:green;"><%=StringUtil.isNotEmpty(employeB.getCin())?"<br>["+employeB.getCin()+"]" : "" %></span>
								      </span>
								      <br>
								      <img alt="" src='resourcesCtrl?elmnt=<%=EncryptionUtil.encrypt(employeB.getId().toString()) %>&path=employe&rdm=<%=(employeB.getDate_maj()!=null?employeB.getDate_maj().getTime():"a")%>' width='60' height='60' onerror="this.onerror=null;this.remove();"/>		 
								    </td>
								    <td style="background-color: black;color: white;">
								    	<div class="data_td" title="Nombres d'unité Heure ou Jour travaillé">Travaillé</div>
								    	<div class="data_td" title="Tarif selon l'unité de calcul : Heure ou journée">Tarif base</div>
								    	<div class="data_td" title="Congés payés">Congé P</div>
								    	<div class="data_td" title="Congés non payés">Congé NP</div>
								    	<div class="data_td">Prime</div>
								    	<div class="data_td">Indemnité</div>
								    	<div class="data_td">Avance</div>
								    	<div class="data_td">Prêt</div>
								    	<div class="data_td">Reliquat</div>
								    	<div class="data_td">Retenue</div>
								    	<div class="data_td">Descipline</div>
								    	<div class="data_td" style="font-size:16px;font-weight: bold;">Total</div>
								    </td>
								    
								     <%for(String idxMois : mapMois.keySet()){ %>
								     	<c:set var="currEmployePaie" value='<%=mapPaie.get(employeB.getId()+"-"+Integer.valueOf(idxMois).intValue()) %>' scope="request" />
								     	
								     	<td mode="${currEmployePaie==null ? 'C':'U' }" id="td_<%=employeB.getId() %>-<%=idxMois %>" idxMois="<%=idxMois %>" empl="<%=employeB.getId() %>" class="add_tarif" style="cursor: pointer;text-align: right;">
								     		<jsp:include page="salariePaie_td_include.jsp" />
								     	</td>
								     <%} %>
		                         </tr>
		                    <%} %> 
<%-- 		                    <%if(listEmployePagger.size() > 0){  --%>
<%-- 		                    %>  --%>
<!-- 			                    <tr> -->
<!-- 		                          	<td></td> -->
<!-- 		                          	<td></td> -->
<%-- 		                          		<%for(String idxMois : mapMois.keySet()){ %> --%>
<!-- 				                          <td align="center"> -->
<%-- 				                          	<%if(mapEtatMois.get(Integer.valueOf(idxMois)+1) != null){ %> --%>
<%-- 				                          		<std:link action="paie.salariePaie.downloadEmployePaiePDF" params='<%="mode=allMonth&annee="+currAnnee+"&mois="+idxMois %>' target="downloadframe" value="Editer paie" /> --%>
<%-- 				                          	<%} %>	 --%>
<!-- 				                          </td> -->
<%-- 			                          <%}%> --%>
		                          
<!-- 		                          </tr> -->
<%-- 		                     	<%} %> --%>
	                      </tbody>
                      </table>
                    </div>
                   </div><!--Widget Body-->
               </div><!--Widget-->
              	

              </div>
            </div>
	</div>
</div>

</std:form>