<%@page import="framework.controller.ControllerUtil"%>
<%@page import="appli.model.domaine.administration.persistant.ParametragePersistant"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script type="text/javascript">
	$(document).ready(function (){
		$(document).off('click', '#tab_etq a').on('click', '#tab_etq a', function(){
			$("#btn_conf_etq").attr("params", "tab="+$(this).attr("tab")); 
		});		
		
	});
</script>	

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Configuration imprimante &eacute;tiquette</span>
			<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="stock.emplacement.work_init_update" workId="${emplacement.id}" icon="fa fa-pencil" tooltip="Cr&eacute;er" />
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row" style="margin-left: -6px;margin-right: -6px;">
				<div class="tabbable">
					<%
					int i=0;
					String[] imprimantesParams = {"ETIQUETTE_PRIX", "ETIQUETTE_BARRE"};
					if(ControllerUtil.getMenuAttribute("is_cuis", request) == null){ %>
						<ul class="nav nav-tabs" id="tab_etq">
						<%
						String[] imprimantesLib = {"Etiquette prix", "Etiquette code barre"};
						for(String param : imprimantesParams){%>
							<li <%=(i==0?"class='active'":"") %>><a tab="<%=param %>" data-toggle="tab" href="#tab_<%=param %>"><%=imprimantesLib[i] %></a></li>
						<%
						i++;
						} %>	
						</ul>
					<%} else{
						imprimantesParams = new String[]{"CAISSE_CUISINE_ETQ"};
					} %>
					
					<div class="tab-content">
						<%
						i = 0;
						for(String param : imprimantesParams){%>
						<div  class="tab-pane <%=(i==0?" active":"") %>" id="tab_<%=param %>">
						<%
							i++;
							List<ParametragePersistant> listParams = (List<ParametragePersistant>)request.getAttribute("param_"+param);
							%>
							<c:set var="listParams" value="<%=listParams %>" />
							<c:set var="currParam" value="<%=param %>" />
							
							<c:forEach items="${listParams }" var="parametre">
								<div class="form-group">
									<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
									<div class="col-md-7">
									<c:choose>
										<c:when test="${parametre.type=='STRING'}">
											<c:choose>
												<c:when test="${parametre.code == currParam.concat('_ORIENTATION') || parametre.code == 'ETIQUETTE_ORIENTATION' }">
													<std:select name="param_${parametre.code}" type="string" style="width:50%;float:left;" data="${orientationBalance }" value="${parametre.valeur}" />
												</c:when>
												<c:when test="${parametre.code == currParam.concat('_PRINT') || parametre.code == 'ETIQUETTE_PRINT' }">
													<std:select name="param_${parametre.code}" type="string" data="${list_imprimante}" width="70%" value="${parametre.valeur}" />
												</c:when>
												<c:otherwise>
													<std:text name="param_${parametre.code}" type="string" style="width:50%;float:left;" maxlength="120" value="${parametre.valeur}" />	
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:when test="${parametre.type=='TEXT'}">
											<std:textarea name="param_${parametre.code}" style="width:50%;float:left;" rows="3" value="${parametre.valeur}" />
										</c:when>
										<c:when test="${parametre.type=='NUMERIC'}">
											<std:text name="param_${parametre.code}" type="long" style="width:120px;float:left;" maxlength="120" value="${parametre.valeur}" />
										</c:when>
										<c:when test="${parametre.type=='DECIMAL'}">
											<std:text name="param_${parametre.code}" type="decimal" style="width:120px;float:left;" maxlength="120" value="${parametre.valeur}" />
										</c:when>
										<c:when test="${parametre.type=='BOOLEAN'}">	
											<std:checkbox name="param_${parametre.code}" checked='${parametre.valeur }' />
										</c:when>
									</c:choose>
									<c:if test="${parametre.help != null && parametre.help != ''}">
										<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="${parametre.help}" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
									</c:if>
									
									</div>
									</div>
								</c:forEach>
							</div>
						<%} %>
					</div>	
				</div>
			</div>	
			<div class="row" style="text-align: center;margin-top: 11px;">
				<div class="col-md-12">
				<%if(ControllerUtil.getMenuAttribute("is_cuis", request) == null){ %>
					<std:button actionGroup="M" id="btn_conf_etq" closeOnSubmit="true" classStyle="btn btn-success" action="stock.article.update_params_imprimante" icon="fa-save" value="Sauvegarder" />
				<%} else{ %>
					<std:button actionGroup="M" closeOnSubmit="true" classStyle="btn btn-success" action="caisse-web.cuisinePilotage.update_params_imprimante" icon="fa-save" value="Sauvegarder" />
				<%} %>
					
					<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
						<i class="fa fa-times"></i> Fermer
					</button>
				</div>
			</div>
		</div>
	</div>
</std:form>

<script type="text/javascript">
/*Handles ToolTips*/
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script>  