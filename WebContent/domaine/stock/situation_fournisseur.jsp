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
		<li>Gestion de stock</li>
		<li>Fiche fournisseur</li>
		<li class="active">Situation</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
	<%if(ControllerUtil.getMenuAttribute("IS_MNU", request) == null){ %>
		<std:link classStyle="btn btn-default" action="stock.fournisseur.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
	<%} %>	
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
		<%request.setAttribute("curMnu", "sit");  %>
		<jsp:include page="/domaine/stock/fournisseur_header_tab.jsp" />
     </div>  
     <%} %>   
      
		<div class="widget-body">
   
				<div class="row">
					<cplx:table name="list_situation" transitionType="simple" width="100%" title="Etat fournisseur" initAction="stock.fournisseur.init_situation" autoHeight="true" checkable="false" >
						<cplx:header>
						<%if(ControllerUtil.getMenuAttribute("IS_MNU", request) != null){ %>
							<cplx:th type="long[]" value="Famille" field="fournisseur.opc_famille.libelle" groupValues="${listFamille }" groupKey="id" groupLabel="libelle" width="0" filterOnly="true"/><!-- Filter only -->
							<cplx:th type="long[]" value="Fournisseur" field="fournisseur.libelle" groupValues="${listFournisseur }" groupKey="id" groupLabel="libelle"/>
						<%} %>	
							<cplx:th type="decimal" value="Montant total" width="120" filtrable="false"/>
							<cplx:th type="decimal" value="Montant payé" width="120" filtrable="false"/>
							<cplx:th type="decimal" value="Montant avoir" width="120" filtrable="false"/>
							<cplx:th type="decimal" value="Montant restant"  width="120" filtrable="false"/>
							<cplx:th type="empty" />
						</cplx:header>
						<cplx:body>
							<c:set var="oldfam" value="${null }"></c:set>
							
							<c:forEach items="${list_situation }" var="fourn">
								<%if(ControllerUtil.getMenuAttribute("IS_MNU", request) != null){ %>
								<c:if test="${empty oldfam or fourn.opc_famille.id != oldfam}">
								     <tr>
										<td colspan="6" noresize="true" class="separator-group" style="padding-left: ${fourn.opc_famille.level*10}px;">
											<span class="fa fa-fw fa-folder-open-o separator-icon"></span>  ${fourn.opc_famille.code}-${fourn.opc_famille.libelle}
										</td>
									</tr>
								</c:if>		
								<c:set var="oldfam" value="${fourn.opc_famille.id }"></c:set>
								<%} %>
								<cplx:tr workId="${fourn.id }">
									<%if(ControllerUtil.getMenuAttribute("IS_MNU", request) != null){ %>
										<cplx:td value="${fourn.code }-${fourn.libelle }" />
									<%} %>
					                <cplx:td align="right" style="font-weight:bold;" value="${fourn.mtt_total }" />
									<cplx:td align="right" style="font-weight:bold;color:green;" value="${fourn.mtt_paye }" />
									<cplx:td align="right" style="font-weight:bold;color:green;" value="${fourn.mtt_avoir }" /> 
									
									<c:set var="mttRest" value="${fourn.mtt_total-fourn.mtt_avoir-fourn.mtt_paye }" />
									
									<cplx:td align="right" style="font-weight:bold;color:${mttRest>0?'red':''};" value="${mttRest }"/>
									<cplx:td>
										<std:link classStyle="btn btn-sm btn-purple" action="stock.fournisseur.situation_detail" params="fo=${fourn.id }" icon="fa fa-bars" tooltip="Détail situation"/>
									</cplx:td>
								</cplx:tr>
							</c:forEach>
							<%if(ControllerUtil.getMenuAttribute("IS_MNU", request) != null){ %>
							<c:if test="${list_situation.size() > 0 }">
								<tr>
									<td noresize="true"></td>
									<td noresize="true" style="font-size: 15px;font-weight: bold;color:green;text-align: right;background-color:#b4b4b4;" >
										<fmt:formatDecimal value="${total_mtt }"/>
									</td>
									<td noresize="true" style="font-size: 15px;font-weight: bold;color:${total_mtt<total_paye?'red':'green' };text-align: right;background-color:#b4b4b4;" >
										<fmt:formatDecimal value="${total_paye }"/>
									</td>
									<td noresize="true" style="font-size: 15px;font-weight: bold;color:green;text-align: right;background-color:#b4b4b4;" >
										<fmt:formatDecimal value="${total_avoir }"/>
									</td>
									
									<c:set var="mttRest" value="${total_mtt-total_avoir-total_paye }" />
									
									<td noresize="true" style="font-size: 15px;font-weight: bold;color:${mttRest>0?'red':'green' };text-align: right;background-color:#b4b4b4;" >
										<fmt:formatDecimal value="${mttRest }"/>
									</td>
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
