<%@page import="java.util.Map"%>
<%@page import="appli.model.domaine.personnel.persistant.EmployePersistant"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#pointage-div .row {
	margin: 0px;
}
.note-editor .note-editable{
	height: 100px !important;
}
</style>

<%
EmployePersistant employeP = (EmployePersistant) request.getAttribute("employeP");
String type = (String)request.getAttribute("tpp");
String dt = (String)request.getAttribute("dt");
%>
<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget" id="pointage-div">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption"><%=request.getAttribute("ttl") %> (<%=dt %>)</span>
			<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="stock.emplacement.work_init_update" workId="${emplacement.id}" icon="fa fa-pencil" tooltip="Cr&eacute;er" />
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>


<% if(type.equals("hr")){
	Map<Long, String> mapData = (Map<Long, String>) request.getAttribute("mapHours");
%>
	<div class="row">
		<div class="form-group" style="margin-left: 20px;">
			<%
			int i = 0;
			for(Long key : mapData.keySet()){ %>
				<div style="width: 20%;float: left;">
					<std:label classStyle="control-label"  value='<%="Heure "+(i%2==0 ? "Début":"Fin") %>' />
				</div>
				<div style="width: 20%;float: left;margin-right: 5%;margin-bottom: 5px;">
					<std:text mask="99:99" type="string" name='<%="heure_"+key%>' style="width:90px;" value="<%=mapData.get(key) %>"/>
				</div>
			<%
				i++;
			}%>
		</div>
	</div>	
<%} else if(type.equals("tr")){ %>
      	<div class="row">
      		
			<div class="form-group">
				<std:label classStyle="control-label col-md-3" value="Dur&eacute;e" />
				
				<div class="col-md-9">
				<%if("J".equals(employeP.getMode_paie())){ %>
					<std:select name="pointage.duree" type="decimal" data="${dureeArray }" required="true" addBlank="false"/>
				<%} else{ %>
					<std:text name="pointage.duree" type="decimal" required="true" style="width: 150px;"/>
				<%} %>
				</div>
			</div>
			<div class="form-group">	
				<std:label classStyle="control-label col-md-3" value='<%="Tarif / "+employeP.getMode_paie() %>' />
				<div class="col-md-9">
					<std:text name="pointage.tarif_ref" type="decimal" maxlength="14" required="true" style="width: 150px;" value="${tarifJourRef }"/>
				</div>
			</div>
		</div>
 <%} else if(type.equals("pr") || type.equals("rt") || type.equals("av")){ %>
		<div class="row" style="text-align: left;margin-top: 5px;">	
			<div class="form-group">
				<std:label classStyle="control-label col-md-3" value="Montant" />
				<div class="col-md-9">
					<std:text name="pointage.montant" type="decimal" maxlength="14" required="true" style="width: 150px;"/>
				</div>
			</div>
		</div>
		<div class="row" style="text-align: left;margin-top: 5px;">	
			<div class="form-group">
				<std:label classStyle="control-label col-md-3" value="commentaire" />
				<div class="col-md-9">
					<std:textarea name="pointage.commentaire" rows="3" cols="30" maxlength="255" />
				</div>
			</div>
		</div>
<%} else if(type.equals("cg")){ %>		
		<div class="row">
			<div class="form-group">
				<std:label classStyle="control-label col-md-3" value="Dur&eacute;e" />
				<div class="col-md-9">
					<std:select name="pointage.duree" type="decimal" data="${dureeArray }" required="true" addBlank="false"/>
				</div>
			</div>
		</div>
		<div class="row" style="text-align: left;margin-top: 5px;">	
			<div class="form-group">
				<std:label classStyle="control-label col-md-3" value="Type" />
				<div class="col-md-9">
					<std:select name="pointage.text_1" type="string" data="${typeCongeArray }" required="true" />
				</div>
			</div>
		</div>
		<div class="form-group">	
				<std:label classStyle="control-label col-md-3" value='<%="Tarif / "+employeP.getMode_paie() %>' />
				<div class="col-md-9">
					<std:text name="pointage.tarif_ref" type="decimal" maxlength="14" required="true" style="width: 150px;" value="${tarifJourRef }"/>
				</div>
			</div>
		<div class="row" style="text-align: left;margin-top: 5px;">	
			<div class="form-group">
				<std:label classStyle="control-label col-md-3" value="commentaire" />
				<div class="col-md-9">
					<std:textarea name="pointage.commentaire" rows="3" cols="30" maxlength="255" />
				</div>
			</div>
		</div>
	<%} else if(type.equals("ds")){ %>	
				<div class="row" style="text-align: left;margin-top: 5px;">	
					<div class="form-group">
						<std:label classStyle="control-label col-md-3" value="Type" />
						<div class="col-md-9">
							<std:select type="string" name="pointage.text_1" data="${typeSanctionArray }" required="true"/>
						</div>
					</div>
				</div>	
				<div class="row" style="text-align: left;">		
					<div class="form-group">
						<std:label classStyle="control-label col-md-6" value="Description du probl&egrave;me" style="text-align: left;"/>
						<div class="col-md-12">
							<std:textarea-rich name="pointage.description"/>
						</div>
					</div>
				</div>	
				<div class="row" style="text-align: left;">	
					<div class="form-group">		
						<std:label classStyle="control-label col-md-6" value="Sanctions" style="text-align: left;" />
						<div class="col-md-12">
							<std:textarea-rich name="pointage.commentaire"/>
						</div>
					</div>
				</div>
	<%} %>
			<hr>
			<div class="row" style="text-align: center;">
				<div class="col-md-12">
				<%String params = "tpp="+type+"&dt="+dt+"&empl="+employeP.getId(); %>
					<std:button actionGroup="M" closeOnSubmit="true" classStyle="btn btn-success" action="pers.pointage.mergePointage" params="<%=params %>" workId="${pointage.id }" icon="fa-save" value="Sauvegarder" />
					<std:button actionGroup="D" closeOnSubmit="true" classStyle="btn btn-danger" action="pers.pointage.deletePointage" params="type=ds" workId="${pointage.id }" icon="fa-trash-o" value="Supprimer" />
					<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
						<i class="fa fa-times"></i> Fermer
					</button>
				</div>
			</div>
		</div>
	</div>

</std:form>
