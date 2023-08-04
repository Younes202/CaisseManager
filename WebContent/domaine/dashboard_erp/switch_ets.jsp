<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.beanContext.EtablissementPersistant"%>
<%@page import="org.hibernate.criterion.Order"%>
<%@page import="framework.model.beanContext.SocietePersistant"%>
<%@page import="appli.model.domaine.administration.service.ISocieteService"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<c:set var="currId" value="<%=ContextAppli.getEtablissementBean().getId() %>"/>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Etablissement</span>
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row" style="margin-left: -5px;">
				|
				<c:forEach var="ets" items="${listEtsAvailaible }">
					<c:if test="${ets.id != currId }">
						<std:link action="admin.societe.changer_ets" workId="${ets.id }" params="isSub=1" value="${ets.nom }" style="text-transform: uppercase;color: blue;padding: 10px;font-size: 17px;text-decoration: underline;" /> | 
					</c:if>
				</c:forEach>    
			</div>	
		</div>
	</div>
</std:form>