<%@page import="framework.controller.ControllerUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="appli.controller.domaine.personnel.bean.PlanningBean"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#generic_modal_body{
	width: 680px;
	margin-left: -10%;
}
.note-editable{
	height: 95px !important;
}
</style>

<%
Boolean isAllDayEvent = (Boolean)request.getAttribute("isAllDayEvent");
// Boolean isRepetition = (Boolean)request.getAttribute("isRepetition");
PlanningBean planningB = (PlanningBean)request.getAttribute("planning");
String currLieu = (String)request.getAttribute("currLieu");
Map<String, List<String>> mapLieu = (Map<String, List<String>>)request.getAttribute("mapLieu");
boolean isReadOnly = !ControllerUtil.isEditionWritePage(request);
%>

<script type="text/javascript">
    $(document).ready(function () {
    	<% if(BooleanUtil.isTrue(isAllDayEvent)){%>
			$('#heure-div').css("display", "none");
			$('#dateFin-div').css("display", "none");
		<%}%>
<%-- 		<% if(BooleanUtil.isTrue(isRepetition)){%> --%>
// 			$('#dtLimite-div').css("display", "");
<%-- 		<%}%> --%>
		
		$(document).off('change', 'select[id="planning.repetition"]');
		$(document).on('change', 'select[id="planning.repetition"]', function(){
			if($(this).val() != '' && $(this).val() != null){
				$("#dtLimite-div").css("display", "");
			} else{
				$("#dtLimite-div").css("display", "none");
			}
		});
    });
</script>
    
<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Fiche planning ou RDV</span>
			<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="pers.planning.work_init_update" workId="${planning.id}" icon="fa fa-pencil" tooltip="Modifier" />
		</div>
		<div class="widget-body" style="padding-right: 45px;">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Type planning" />
					<div class="col-md-3">
						<std:select type="long" key="id" name="planning.opc_type_planning.id" data="${listTypePlanning }" labels="libelle" required="true" width="100%">
							<std:ajax event="change" action="pers.planning.calcul_heure_fin" target="heure_fin" isInput="true"/>
						</std:select>
					</div>
					<std:label classStyle="control-label col-md-3" value="Rappel" />
					<div class="col-md-3">
						<std:select name="planning.rappel" type="string" data="${listRappel}" width="100%"/>
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Date début" />
					<div class="col-md-3">
						<std:date name="planning.date_debut" required="true" />
					</div>
					<div id="dateFin-div">
						<std:label classStyle="control-label col-md-3" value="Date fin" />
						<div class="col-md-3">
							<std:date name="planning.date_fin" />
						</div>
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Toute la journée" />
					<div class="col-md-9">
						<std:checkbox name="planning.is_all_day" onClick="$('#heure-div').toggle();$('#dateFin-div').toggle();"/>
					</div>
				</div>
				<div class="form-group" id="heure-div">
					<std:label classStyle="control-label col-md-3" value="Heure début" />
					<div class="col-md-3">
						<std:text mask="99:99" type="string" name="heure_debut" style="width:60px;" value="${heure_debut }">
							<std:ajax event="change" action="pers.planning.calcul_heure_fin" target="heure_fin" isInput="true"/>
						</std:text>
					</div>
					<std:label classStyle="control-label col-md-3" value="Heure fin" />
					<div class="col-md-3">
						<std:text mask="99:99" type="string" name="heure_fin" style="width:60px;" value="${heure_fin }"/>
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Lieu" />
					<div class="col-md-9">
						<select <%=isReadOnly?" disabled='disabled'":"" %> name="planning.lieu_array[]" id="planning.lieu_array" multiple="multiple" class="select2" style="width: 100%;">
						<%
							for(String lieu : mapLieu.keySet()){
								List<String> listDet = mapLieu.get(lieu);
						%>
						<optgroup label="<%=lieu%>"></optgroup>
						<% for(String det : listDet){ 
							String selected = "";
							if(planningB != null && planningB.getLieu_array() != null){
								for(String l : planningB.getLieu_array()){
									if(l.equals(det)){
										selected = " selected='selected'";
									}
								}
							}
							if(currLieu!=null && currLieu.equals(det)){
								selected = " selected='selected'";
							}
						%>
							<option <%=selected %> value="<%=det%>"><%=det %></option>
						<%} 
						}%>						
						
						</select>
					</div>
				</div>
<!-- 				<div class="form-group"> -->
<%-- 					<std:label classStyle="control-label col-md-3" value="Répétition" /> --%>
<!-- 					<div class="col-md-3"> -->
<%-- 						<std:select name="planning.repetition" type="string" data="${listRepetition}" width="100%"/> --%>
<!-- 					</div> -->
<!-- 					<div style="display:none;" id="dtLimite-div"> -->
<%-- 						<std:label classStyle="control-label col-md-3" value="Jusqu'&agrave;" /> --%>
<!-- 						<div class="col-md-3"> -->
<%-- 							<std:date name="planning.dt_fin_repet" /> --%>
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 				</div> -->
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Titre" />
					<div class="col-md-9">
						<std:text type="string" name="planning.titre" placeholder="Titre" required="true" maxlength="50"/>
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Employé(s)" />
					<div class="col-md-9">
						<std:select type="string[]" name="planning.employes_array" multiple="true" data="${listEmploye }" labels="nom;' ';prenom" key="id" width="100%"/>
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Client(s)" />
					<div class="col-md-9">
						<std:select type="string[]" name="planning.clients_array" multiple="true" data="${listClient }" labels="nom;' ';prenom" key="id" width="100%"/>
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Description" />
					<div class="col-md-9">
						<std:textarea-rich name="planning.description" />
					</div>
				</div>
			</div>	
			<div class="row" style="text-align: center;">
				<div class="col-md-12">
					<std:button actionGroup="M" classStyle="btn btn-success" action="pers.planning.work_merge" workId="${planning.id }" icon="fa-save" value="Sauvegarder" />
					<std:button actionGroup="D" classStyle="btn btn-danger" action="pers.planning.work_delete" workId="${planning.id }" icon="fa fa-trash-o" value="Supprimer" />
					<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
						<i class="fa fa-times"></i> Fermer
					</button>
				</div>
			</div>
		</div>
	</div>
</std:form>