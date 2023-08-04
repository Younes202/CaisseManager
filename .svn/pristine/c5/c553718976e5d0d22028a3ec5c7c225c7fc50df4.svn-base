<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#div_client{ 
	background-color: #e5e5e5;
    position: absolute;
    left: 34%;
    top: 34px;
    width: 303px;
    border: 2px solid red;
    border-radius: 15px; 
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

</style>

<script type="text/javascript">
$(document).ready(function (){
	$("#client-activator").click(function(){
		var isActivate = $(this).prop("checked");
		if(isActivate){
			$("#div_client").show('slide');
		} else{
			$("#div_client").hide('slide');
		}
	});
});
</script>

<div id="body-content">
<!-- Page Header -->
<std:form name="search-form">
	<div class="widget-header bordered-bottom bordered-blue">
		<span class="widget-caption">Clients et employ&eacute;s</span>
           <c:if test="${tp != 'emp' }">
            <div class="widget-buttons buttons-bordered" style="margin-bottom: 10px;">
         		<i class="fa fa-user-plus" style="font-size: 20px;"></i>     
         		<label>
            		<input class="checkbox-slider toggle colored-blue" type="checkbox" id="client-activator" style="display: none;">
            		<span class="text"></span>
            	</label>
            </div>
			</c:if>
		<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal" style="margin-top: 2px;margin-right: 5px;">
			<i class="fa fa-times"></i> Fermer
		</button>
	</div>
	
	<div class="row" style="margin-left: 10px;margin-bottom: 10px;">
		<input type="text" name="tbl_filter" id="tbl_filter" placeholder="NUM, PHONE, CIN, NOM" style="border: 2px solid orange;height: 29px;width: 198px;margin-top: 4px;" value="${filterVal }">
		<std:link classStyle="btn btn-default btn-sm shiny icon-only blue" noJsValidate="true" action="caisse-web.caisseWeb.initPersonne" params="${tp=='emp'?'tp=emp':'tp=cli' }&isFltr=1" targetDiv="generic_modal_body" style="margin-top: -4px;" icon="fa fa-search"></std:link>
	</div>
	
	<div class="row" style="margin-left: 0px;margin-right: 0px;">
         <div class="tabbable">
               <ul class="nav nav-tabs" id="myTab">
               		<%
               			if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_CLIENT"))){
               		%>
                     <li class="${tp=='emp'?'':'active' }">
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
                       <li class="${tp=='emp'?'active':'' }">
                         <a data-toggle="tab" noval="true" href="#journeeCaisse" wact="<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.initPersonne")%>" targetDiv="body-content" params="tp=emp">
                          Employ&eacute;s
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
	<c:when test="${tp != 'emp' }">
	<%
		if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_CLIENT"))){
	%>
		<!-- Liste des clients -->
		<cplx:table name="list_client" transitionType="simple" width="100%" titleKey="client.list" initAction="caisse-web.caisseWeb.initPersonne" exportable="false" filtrable="false" checkable="false">
			<cplx:header>
				<cplx:th type="empty" width="40"/>
				<cplx:th type="string" valueKey="client.numero" field="client.numero" width="80" />
				<cplx:th type="string" valueKey="client.nom" field="client.nom" />
			</cplx:header>
			<cplx:body>
				<c:forEach items="${listClient }" var="client">
					<cplx:tr workId="${client.id }">
						<cplx:td align="center">
							<std:link noJsValidate="true" classStyle="btn btn-primary btn-sm icon-only white" style="color:blue;background-color:#f0f1f3;" workId="${client.id}" action="caisse-web.caisseWeb.selectPersonne" closeOnSubmit="true" targetDiv="left-div" icon="fa fa-thumb-tack" tooltip="S&eacute;lectionner" />
						</cplx:td>
						<cplx:td value="${client.numero}"></cplx:td>
						<cplx:td value="${client.civilite} ${client.nom} ${client.prenom}"></cplx:td>
					</cplx:tr>
				</c:forEach>
			</cplx:body>
		</cplx:table>
	<%
		}
	%>	
	</c:when>
	<c:otherwise>	
		<%
				if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_EMPLOYE"))){
			%>
		<!-- Liste des employes -->
			<cplx:table name="list_employe" transitionType="simple" width="100%" titleKey="employe.list" initAction="caisse-web.caisseWeb.initPersonne" exportable="false" filtrable="false" checkable="false">
				<cplx:header>
					<cplx:th type="empty" width="40"/>
					<cplx:th type="string" valueKey="employe.numero" field="employe.numero" width="80" />
					<cplx:th type="string" valueKey="employe.nom" field="employe.nom" />
				</cplx:header>
				<cplx:body>
					<c:forEach items="${listEmploye }" var="employe">
						<cplx:tr workId="${employe.id }">
							<cplx:td>
								<std:link noJsValidate="true" classStyle="btn btn-primary btn-sm icon-only white" style="color:blue;background-color:#f0f1f3;" workId="${employe.id}" action="caisse-web.caisseWeb.selectPersonne" closeOnSubmit="true" targetDiv="left-div" icon="fa fa-thumb-tack" tooltip="S&eacute;lectionner" />
							</cplx:td>
							<cplx:td value="${employe.numero}"></cplx:td>
							<cplx:td value="${employe.civilite} ${employe.nom} ${employe.prenom}"></cplx:td>
						</cplx:tr>
					</c:forEach>
				</cplx:body>
			</cplx:table>
		<%} %>	
		</c:otherwise>
	</c:choose>	
	</div>

	<!-- end widget content -->
	<!-- Client form -->
<div class="row" id="div_client" style="display: none;">
	<jsp:include page="/domaine/caisse/normal/caisse_client_edit.jsp"></jsp:include>
</div>

<div class="row" id="div_portefeuille" style="display: none;">
	<jsp:include page="/domaine/caisse/normal/caisse_portefeuille_edit.jsp"></jsp:include>
</div>
</std:form>	
</div>
<!--Page Related Scripts-->
    <script src="resources/framework/js/toastr/toastr.js?v=1.0"></script>
<jsp:include page="/commun/print-local.jsp" /> 