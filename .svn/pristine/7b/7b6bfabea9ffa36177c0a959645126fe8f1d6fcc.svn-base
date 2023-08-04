<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Fiche client</li>
		<li class="active">Situation</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link classStyle="btn btn-default" action="pers.client.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
	<std:form name="data-form">
		<!-- widget grid -->
		<div class="widget">
	
	<%if(ControllerUtil.getMenuAttribute("IS_MNU", request) == null){ %>	
	<div class="row">
        <div class="col-lg-12 col-sm-12 col-xs-12">
              <% request.setAttribute("curMnu", "situatBO"); %>
				<jsp:include page="/domaine/personnel/client_header_tab.jsp" />
          </div>
        </div>  
     <%} %>   
     
     <c:set var="bigDecimalUtil" value="<%=new BigDecimalUtil() %>" />
      
			<div class="widget-body">
				<div class="row">
					<cplx:table name="list_etat_client" transitionType="simple" width="100%" title="Etat client" initAction="pers.client.init_situation" autoHeight="true" checkable="false" >
						<cplx:header>
						<%if(ControllerUtil.getMenuAttribute("IS_MNU", request) != null){ %>
							<cplx:th type="long[]" value="Client" field="client.id" groupValues="${listClient }" groupKey="id" groupLabel="nom"/>
						<%} %>	
							<cplx:th type="decimal" value="Montant total" width="120" filtrable="false"/>
							<cplx:th type="decimal" value="Montant pay&eacute;" width="120" filtrable="false"/>
							<cplx:th type="decimal" value="Montant restant" width="120" filtrable="false"/>
							<cplx:th type="empty" />
						</cplx:header>
						<cplx:body>
							<c:forEach items="${list_client }" var="client">
								<cplx:tr workId="${client.id }">
									<%if(ControllerUtil.getMenuAttribute("IS_MNU", request) != null){ %>
										<cplx:td value="${client.nom } ${client.prenom }" />
									<%} %>
					                <cplx:td align="right" value="${bigDecimalUtil.formatNumberZeroBd(client.mtt_total) }"/>
									<cplx:td align="right" style="color:${client.mtt_paye>client.mtt_total?'red':''};" value="${bigDecimalUtil.formatNumberZeroBd(client.mtt_paye) }"/>
									<cplx:td align="right" style="color:${client.mtt_non_paye>0?'red':''};" value="${bigDecimalUtil.formatNumberZeroBd(client.mtt_non_paye) }"/>
									<cplx:td>
										<std:link classStyle="btn btn-sm btn-purple" action="pers.client.situation_detail" params="fo=${client.id }" icon="fa fa-bars" tooltip="Détail situation"/>
									</cplx:td>
								</cplx:tr>
							</c:forEach>
							<%if(ControllerUtil.getMenuAttribute("IS_MNU", request) != null){ %>
							<c:if test="${list_client.size() > 0 }">
								<tr>
									<td noresize="true"></td>
									<td noresize="true" style="font-size: 15px;font-weight: bold;color:green;text-align: right;background-color:#b4b4b4;" ><fmt:formatDecimal value="${total_mtt }"/></td>
									<td noresize="true" style="font-size: 15px;font-weight: bold;color:${total_mtt<total_paye?'red':'green' };text-align: right;background-color:#b4b4b4;" ><fmt:formatDecimal value="${total_paye }"/></td>
									<td noresize="true" style="font-size: 15px;font-weight: bold;color:${total_restant>0?'red':'green' };text-align: right;background-color:#b4b4b4;" ><fmt:formatDecimal value="${total_restant }"/></td>
									<td noresize="true"></td>
								</tr>
							</c:if>
							<%} %>
						</cplx:body>
					</cplx:table>
				</div>
		</div>
		</div>
	</std:form>
</div>
