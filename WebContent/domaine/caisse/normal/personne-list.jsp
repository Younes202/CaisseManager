<%@page import="appli.model.domaine.fidelite.dao.IPortefeuille2Service"%>
<%@page import="appli.model.domaine.administration.service.IClientService"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="appli.model.domaine.fidelite.service.ICarteFideliteClientService"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%> 
<%@page errorPage="/commun/error.jsp"%>

<style>
#div_client{ 
	background-color: #e5e5e5;
    position: absolute;
    left: 34%;
    top: 34px;
    width: 380px;
    border: 2px solid red;
    border-radius: 15px; 
    z-index: 99999;
} 
#div_client .row{
    margin-left: 10px;
    margin-right: 3px;
}

#div_portefeuille{
	background-color: #e5e5e5;
    position: absolute;
    left: 34%;
    top: 34px;
    width: 403px;
    border: 2px solid red;
    border-radius: 15px; 
} 
#div_portefeuille .row{
    margin-left: 10px;
    margin-right: 3px;
}
/* .cli_modal{
    margin-left: 0%;
    width: 116%;
    position: absolute;
    height: 100vh;

} */

</style>

<%
boolean isPortefeuille = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("PORTEFEUILLE"));
boolean isPoints = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("POINTS"));
boolean isServeur = ContextAppli.getUserBean().isInProfile("SERVEUR");
boolean isCloseOnSelect =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CLOSE_SELECT_PERS"));
%>
<script type="text/javascript">
$(document).ready(function (){
	init_keyboard_events();
	
	$("#client-activator").click(function(){
		var isActivate = $(this).prop("checked");
		if(isActivate){
			$("#div_client").show('slide');
		} else{
			$("#div_client").hide('slide');
		}
	});
	
	$(document).off('click', "a[id='portefeuille_lnk']").on('click', "a[id='portefeuille_lnk']", function(){
		$("label[class='error']").remove();
		$("#portefeuille\\.mtt_recharge").val('');
		
		$("#select_portefeuille").attr("params", $(this).attr('params'));
		$("#div_portefeuille").show('slide');
	});
	
	if($("#check_modal_cli").length == 0){
		$("#generic_modal").append("<input type='hidden' id='check_modal_cli'>");
 		$(".cli_modal").css("width", "200%").css("margin-left", "-74%");
	}
});
</script>


<c:set var="carteFideliteService" value="<%=ServiceUtil.getBusinessBean(ICarteFideliteClientService.class)%>" />
<c:set var="portefeuilleService" value="<%=ServiceUtil.getBusinessBean(IPortefeuille2Service.class)%>" />

<div id="body-content" class="cli_modal" style="background-color: white;">
<!-- Page Header -->
<std:form name="search-form">
	<div class="widget-header bordered-bottom bordered-blue">
		<span class="widget-caption">Affectation détail commande</span>
           <c:if test="${tp == 'cli' }">
            <div class="widget-buttons buttons-bordered" style="margin-bottom: 10px;">
         		<i class="fa fa-user-plus" style="font-size: 20px;"></i>     
         		<label>
            		<input class="checkbox-slider toggle colored-blue" type="checkbox" id="client-activator" style="display: none;">
            		<span class="text"></span>
            	</label>
            </div>
			</c:if>
			<div class="widget-buttons buttons-bordered" style="margin-bottom: 10px;">
        		<i class="fa fa-keyboard-o" style="font-size: 20px;"></i>         
        		<label>
                 <input class="checkbox-slider toggle colored-blue" type="checkbox" id="keyboard-activator" style="display: none;">
                 <span class="text"></span>
             </label>
        	</div>
		<input type="text" name="tbl_filter" id="tbl_filter" placeholder="NUM, PHONE, CIN, NOM" style="border: 2px solid orange;height: 29px;width: 198px;margin-top: 4px;" value="${filterVal }">
		<std:link classStyle="btn btn-default btn-sm shiny icon-only blue" noJsValidate="true" action="caisse-web.caisseWeb.initPersonne" params="tp=${tp}&isFltr=1" targetDiv="generic_modal_body" style="margin-top: -4px;" icon="fa fa-search"></std:link>
		
		<button type="button" id="<%=isCloseOnSelect?"close_modal":"close_modalXDD" %>" onclick="$('#check_modal_cli').remove();" class="btn btn-primary" data-dismiss="modal" style="margin-top: 2px;margin-right: 5px;">
			<i class="fa fa-times"></i> Fermer
		</button>
	</div>
	
	<div class="row" style="margin-left: 0px;margin-right: 0px;">
         <div class="tabbable">
               <ul class="nav nav-tabs" id="myTab">
               		<%
               			if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_CLIENT"))){
               		%>
                     <li class="${tp=='cli'?'active':'' }">
                         <a data-toggle="tab" noval="true" href="#descripton" wact="<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.initPersonne")%>" targetDiv="body-content" params="tp=cli">
                          Clients
                         </a>
                      </li>
                      <%
                      	}
                      %>
                                             
                      <%
                      	if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_EMPLOYE"))){
                      %>
                       <li class="${tp=='empl'?'active':'' }">
                         <a data-toggle="tab" noval="true" href="#journeeCaisse" wact="<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.initPersonne")%>" targetDiv="body-content" params="tp=empl">
                          Employés consom
                         </a>
                       </li>
                       <%
                       	}
                       %>
                      <%
                      	if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_EMPL_SERV"))){
                      %>
                       <li class="${tp=='serv'?'active':'' }">
                         <a data-toggle="tab" noval="true" href="#journeeCaisse" wact="<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.initPersonne")%>" targetDiv="body-content" params="tp=serv">
                          Serveurs
                         </a>
                       </li>
                       <%
                       	}
                       %>
                       
                       <%
                      	if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_SOC_LIVR"))){
                      %>
                       <li class="${tp=='socLivr'?'active':'' }">
                         <a data-toggle="tab" noval="true" href="#journeeCaisse" wact="<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.initPersonne")%>" targetDiv="body-content" params="tp=socLivr">
                          Sociétés de livraison
                         </a>
                       </li>
                       <%
                       	}
                       %>
                       
                       <%
                      	if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_LIVR"))){
                      %>
                       <li class="${tp=='livr'?'active':'' }">
                         <a data-toggle="tab" noval="true" href="#journeeCaisse" wact="<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.initPersonne")%>" targetDiv="body-content" params="tp=livr">
                          Livreur
                         </a>
                       </li>
                       <%
                       	}
                       %>
                       
                </ul>
           </div>
      </div>
      
	<div class="widget-body">
	
<c:choose>
	<c:when test="${tp == 'cli' }">
	<%
		if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_CLIENT"))){
	%>
		<!-- Liste des clients -->
		<cplx:table name="list_client" transitionType="simple" autoHeight="true" width="100%" titleKey="client.list" initAction="caisse-web.caisseWeb.initPersonne" exportable="false" filtrable="false" checkable="false">
			<cplx:header>
				<cplx:th type="empty" width="45"/>
				<cplx:th type="string" valueKey="client.numero" field="client.numero" width="100" />
				<cplx:th type="string" valueKey="client.nom" field="client.nom" />
				
				<c:forEach items="${listDataValueForm }" var="data">
					<cplx:th type="string" value="${data.opc_data_form.data_label }" filtrable="false" sortable="false" />
				</c:forEach>
				
				<cplx:th type="string" value="CIN" field="client.cin" width="90" />
				<cplx:th type="string" value="Téléphone" field="client.telephone" />
				<%
					if(isPoints && !isServeur){
				%>
				<cplx:th type="decimal" value="Montant fidélité" sortable="false" filtrable="false" width="80" />
				<%
					}
				%>
				<% if(isPortefeuille && !isServeur){ %>
					<cplx:th type="decimal" value="Montant portefeuille" sortable="false" filtrable="false" width="130" />
					<cplx:th type="empty" width="230"/>
				<% } else{ %>
					<cplx:th type="empty" width="50"/>
				<% } %>
			</cplx:header>
			<cplx:body>
				<c:set var="clientService" value="<%=ServiceUtil.getBusinessBean(IClientService.class) %>" />
			
				<c:forEach items="${listClient }" var="client">
					<c:set var="listDataVal" value="${clientService.loadDataForm(client.id, 'CLIENT') }" />
					
					<cplx:tr workId="${client.id }">
						<cplx:td align="center">
							<std:link noJsValidate="true" classStyle="btn btn-primary btn-sm icon-only white" style="color:blue;background-color:#f0f1f3;" workId="${client.id}" action="caisse-web.caisseWeb.selectPersonne" targetDiv="left-div" icon="fa fa-thumb-tack" tooltip="Sélectionner" />
						</cplx:td>
						<cplx:td value="${client.numero}"></cplx:td>
						<cplx:td value="${client.civilite} ${client.nom} ${client.prenom}"></cplx:td>
				
					<c:forEach items="${listDataValueForm }" var="dataV">		
						<c:forEach items="${listDataVal }" var="data">
							<c:if test="${dataV.opc_data_form.id==data.opc_data_form.id }">
								<c:set var="currDV" value="${data.data_value }" />
								<c:set var="currAlign" value="${(data.opc_data_form.data_type=='LONG' or data.opc_data_form.data_type=='DECIMAL') ? 'right':'center' }" />
							</c:if>
						</c:forEach>
						<cplx:td align="${currAlign }" value="${currDV }" />
					</c:forEach>
						
						<cplx:td value="${client.cin}"></cplx:td>
						
						<c:set var="phone" value="${client.telephone }${(not empty client.telephone and not empty client.telephone2) ? ' / ':'' }${client.telephone2}" />
						
						<cplx:td>
							<c:if test="${not empty phone}">
								<span onclick="javascript: Notify('<b>${phone}</b>', 'top-right', '5000', 'success', 'fa fa-phone', true); return false;">
									<i class="fa fa-hand-o-down" style="color: red;"></i>
									${phone }
								</span>
							</c:if>
						</cplx:td>
						<%
							if(isPoints && !isServeur){
						%>
						<cplx:td align="right" style="color:blue;font-weight:bold;" value="${carteFideliteService.getCarteClientActive(client.id).mtt_total}"></cplx:td>
						<%
							}
						%>
						<%
							if(isPortefeuille && !isServeur){
						%>
						<cplx:td align="right">
							<std:link style="color:${client.solde_portefeuille<0?'red':'blue' };font-weight:bold;" action="caisse-web.caisseWeb.init_situation" params="elmentId=${client.id }&isSub=1" targetDiv="right-div" onClick="$('#close_modal').trigger('click');">
								<fmt:formatDecimal value="${client.solde_portefeuille }" />
							</std:link>
						</cplx:td>
						<%
							}
						%>
						<cplx:td align="center">
							<std:link classStyle="btn btn-success btn-sm icon-only white" style="height: 26px;" action="pers.client.loadAjaxClient" noJsValidate="true" params="cli=${client.id }" targetDiv="div_client" icon="fa fa-pencil-square-o" onClick="$('#div_client').show('slide');"/>
							<%if(isPortefeuille && !isServeur){ %> 
								<std:link actionGroup="S" classStyle="btn btn-sm btn-default shiny" style="height: 26px;color:black;padding-top: 4px;" params="cli=${client.id }" onClick="$('#close_modal').trigger('click');" action="pers.clientCaisse.print_ticket_client" targetDiv="div_gen_printer" icon="fa fa-print" tooltip="Imprimer en un ticket" />
								<std:link actionGroup="S" classStyle="btn btn-sm btn-default shiny" value="Sit" style="height: 26px;color:black;padding-top: 4px;" params="cli=${client.id }&isSit=1" onClick="$('#close_modal').trigger('click');" action="pers.clientCaisse.print_ticket_client" targetDiv="div_gen_printer" tooltip="Imprimer situation">
									<i class="fa fa-print" style="color: blue;"></i>
								</std:link>
								<std:link id="portefeuille_lnk" classStyle="btn btn-warning btn-sm icon-only white" style="height: 26px;color:blue;background-color:#f0f1f3;" workId="${client.id}" action="" icon="fa fa-briefcase" tooltip="Recharger portefeuille" />
								<std:link action="pers.client.desactiver" workId="${client.id }" icon="fa fa-lock" onClick="$('#close_modal').trigger('click');" classStyle="btn btn-sm btn-danger shiny" style="height: 26px;color:white;padding-top: 4px;" tooltip="D&eacute;sactiver" />
							<% }%> 
						</cplx:td>
					</cplx:tr>
				</c:forEach>
			</cplx:body>
		</cplx:table>
	<%
		}
	%>	
	</c:when>
		<c:when test="${tp == 'empl'}">
		<%
				if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_EMPLOYE"))){
			%>
		<!-- Liste des employes -->
			<cplx:table name="list_employe" transitionType="simple" width="100%" autoHeight="true" titleKey="employe.list" initAction="caisse-web.caisseWeb.initPersonne" exportable="false" filtrable="false" checkable="false">
				<cplx:header>
					<cplx:th type="empty" width="50"/>
					<cplx:th type="string" valueKey="employe.numero" field="employe.numero" width="100" />
					<cplx:th type="string" valueKey="employe.nom" field="employe.nom" />
					<cplx:th type="string" value="CIN" field="employe.cin" width="90" />
				</cplx:header>
				<cplx:body>
					<c:forEach items="${listEmploye }" var="employe">
						<cplx:tr workId="${employe.id }">
							<cplx:td>
								<std:link noJsValidate="true" classStyle="btn btn-primary btn-sm icon-only white" style="color:blue;background-color:#f0f1f3;" workId="${employe.id}" action="caisse-web.caisseWeb.selectPersonne" targetDiv="left-div" icon="fa fa-thumb-tack" tooltip="Sélectionner" />
							</cplx:td>
							<cplx:td value="${employe.numero}"></cplx:td>
							<cplx:td value="${employe.civilite} ${employe.nom} ${employe.prenom}"></cplx:td>
							<cplx:td value="${employe.cin}"></cplx:td>
						</cplx:tr>
					</c:forEach>
				</cplx:body>
			</cplx:table>
		<%} %>	
		</c:when>
	<c:when test="${tp == 'serv' }">
		<%
				if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_EMPL_SERV"))){
			%>
		<!-- Liste des employes -->
			<cplx:table name="list_serveur" transitionType="simple" width="100%" autoHeight="true" title="Liste des serveurs" initAction="caisse-web.caisseWeb.initPersonne" exportable="false" filtrable="false" checkable="false">
				<cplx:header>
					<cplx:th type="empty" width="50"/>
					<cplx:th type="string" value="Login" field="employe.numero" />
				</cplx:header>
				<cplx:body>
					<c:forEach items="${listServeur }" var="user">
						<cplx:tr workId="${user.id }">
							<cplx:td>
								<std:link noJsValidate="true" classStyle="btn btn-primary btn-sm icon-only white" style="color:blue;background-color:#f0f1f3;" workId="${user.id}" action="caisse-web.caisseWeb.selectPersonne" targetDiv="left-div" icon="fa fa-thumb-tack" tooltip="Sélectionner" />
							</cplx:td>
							<cplx:td value="${user.login}"></cplx:td>
						</cplx:tr>
					</c:forEach>
				</cplx:body>
			</cplx:table>
		<%} %>	
		</c:when>
		<c:when test="${tp == 'livr' }">
		<%
				if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_LIVR"))){
			%>
		<cplx:table name="list_livreur" transitionType="simple" width="100%" autoHeight="true" title="Liste des livreurs" initAction="caisse-web.caisseWeb.initPersonne" exportable="false" filtrable="false" checkable="false">
				<cplx:header>
					<cplx:th type="empty" width="50"/>
					<cplx:th type="string" value="Login" field="employe.numero" />
				</cplx:header>
				<cplx:body>
					<c:forEach items="${listLivreur }" var="user">
						<cplx:tr workId="${user.id }">
							<cplx:td>
								<std:link noJsValidate="true" classStyle="btn btn-primary btn-sm icon-only white" style="color:blue;background-color:#f0f1f3;" workId="${user.id}" action="caisse-web.caisseWeb.selectPersonne" targetDiv="left-div" icon="fa fa-thumb-tack" tooltip="Sélectionner" />
							</cplx:td>
							<cplx:td value="${user.login}"></cplx:td>
						</cplx:tr>
					</c:forEach>
				</cplx:body>
			</cplx:table>
		<%} %>	
		</c:when>
	<c:when test="${tp == 'socLivr' }">
			<%
				if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_SOC_LIVR"))){
			%>
		<!-- Liste des société livraison -->
			<cplx:table name="list_societeLiv" transitionType="simple" width="100%" title="Liste des sociétés" initAction="caisse-web.caisseWeb.initPersonne" exportable="false" filtrable="false" checkable="false">
				<cplx:header>
					<cplx:th type="empty" width="50"/>
					<cplx:th type="string" value="Société" field="societeLivr.nom" />
				</cplx:header>
				<cplx:body>
					<c:forEach items="${listSocieteLivr }" var="societeLivr">
						<cplx:tr workId="${societeLivr.id }">
							<cplx:td>
								<std:link noJsValidate="true" classStyle="btn btn-primary btn-sm icon-only white" style="color:blue;background-color:#f0f1f3;" workId="${societeLivr.id}" action="caisse-web.caisseWeb.selectPersonne" targetDiv="left-div" icon="fa fa-thumb-tack" tooltip="Sélectionner" />
							</cplx:td>
							<cplx:td value="${societeLivr.nom}"></cplx:td>
						</cplx:tr>
					</c:forEach>
				</cplx:body>
			</cplx:table>
		<%} %>	
	</c:when>

	</c:choose>	
	</div>
</std:form>	
	<!-- end widget content -->
	<!-- Client form -->
<div class="row" id="div_client" style="display: none;">
	<jsp:include page="/domaine/caisse/normal/caisse_client_edit.jsp"></jsp:include>
</div>

<div class="row" id="div_portefeuille" style="display: none;">
	<jsp:include page="/domaine/caisse/normal/caisse_portefeuille_edit.jsp"></jsp:include>
</div>
</div>

<!--Page Related Scripts-->
    <script src="resources/framework/js/toastr/toastr.js?v=1.0"></script>
	<jsp:include page="/commun/print-local.jsp" /> 