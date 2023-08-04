<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="java.util.List"%>
<%@page import="appli.model.domaine.personnel.persistant.EmployePersistant"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>

<%
String mnuTop = (String)request.getAttribute("mnuTop");
List<EmployePersistant> listEmployeCombo = (List<EmployePersistant>)request.getAttribute("listEmployeCombo");

String act = "paie.salariePaie.loadVueMois";
if("J".equals(mnuTop) || "JD".equals(mnuTop) || "H".equals(mnuTop)){
	act = "paie.salariePaie.loadVueJour";
}

String startV = "1"; 
String pattern = "mm/yyyy";
//
if("M".equals(mnuTop)){
	pattern = "yyyy";
	startV = "'years'";
} else if("H".equals(mnuTop)){
	pattern = "dd/mm/yyyy";
	startV = "'day'";
}
%>
<script type="text/javascript">
$(document).ready(function (){
	$('.input-group.date, #dateDebut').datepicker({
		clearBtn: true,
	    language: "fr",
	    autoclose: true,
	    format: '<%=pattern%>',
	    startView: <%=startV%>,
	    minViewMode: <%=startV%>
	});
	$('.input-group.date, #dateDebut').datepicker().on("changeDate", function(e) {
// 	    var currDate = $('#dateDebut').datepicker('getFormattedDate');
<%-- 	    submitAjaxForm('<%=EncryptionUtil.encrypt(act)%>', 'dttop='+currDate, $("#search-form"), $(this)); --%>
		$("#btn_filter").trigger("click");
	});
});
</script>
		
		<div style="width: 180px;float: left;margin-right: 4px;">
             <std:select placeholder="Employé" name="employe" type="long" value='<%=ControllerUtil.getMenuAttribute("CURR_EMPLOYE", request) %>' data="<%=listEmployeCombo %>" key="id" labels="nom;' ';prenom"/>
        </div>     
             <std:text name="cin" placeholder="CIN" style="width:120px;float: left;" value='<%=ControllerUtil.getMenuAttribute("CURR_CIN", request) %>' type="string" />
             <std:button id="btn_filter" classStyle="btn btn-primary" params="isFltr=1" action='<%=act %>' icon="fa-search" value="Filtrer" style="margin-left: 4px;float: left;"/>

		<div class="input-group date" style="width: 150px;
	    float: revert;
	    position: absolute;
	    right: 120px;">
			<input type="text" class="form-control" name="dateDebut" id="dateDebut" style="width: 110px;font-size: 15px;color:green !important;font-weight: bold;border: 1px solid gray;" value="<%=StringUtil.getValueOrEmpty(ControllerUtil.getMenuAttribute("CURR_DATE", request))%>">
			<span class="input-group-addon" style="border: 1px solid black;padding-top: 4px;">
				<i class="fa fa-calendar" style="font-size: 18px;color: #9C27B0;"></i>
			</span>
		</div>
