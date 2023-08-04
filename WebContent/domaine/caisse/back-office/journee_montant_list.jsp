<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.model.domaine.vente.persistant.JourneePersistant"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.controller.Context"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<%
	boolean isDetailJourneeDroit = Context.isOperationAvailable("DETJRN");
JourneePersistant lastJournee = (JourneePersistant)request.getAttribute("lastJournee");
boolean isAdmin = BooleanUtil.isTrue(ContextAppli.getUserBean().getIs_admin());

boolean isPortefeuille =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("PORTEFEUILLE"));
boolean isPoints =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("POINTS"));

if(lastJournee != null){
%>

<style>
.tooltip-inner{
	text-align: right !important;
}
</style>
<%} %>
	
 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion des journ&eacute;es</li>
         <li>Fiche journ&eacute;e</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
      
      <c:choose>
	      <c:when test="${lastJournee == null || lastJournee.getStatut_journee()=='C' }">
	          <std:linkPopup actionGroup="C" classStyle="btn btn-success" action="caisse.journee.work_init_create" value="Ouvrir la journ&eacute;e" tooltip="Ouvrir la joun&eacute;e" />
	      </c:when>
	      <c:when test="${lastJournee != null && lastJournee.getStatut_journee()=='O' }"> 
	      	  <std:linkPopup action="caisse.journee.init_cloture" workId="<%=lastJournee.getId().toString() %>" actionGroup="C" classStyle="btn btn-warning" value="Clore la journ&eacute;e" tooltip="Clore la journ&eacute;e" />
	      </c:when>
      </c:choose>
      
      <std:link actionGroup="C" params="tp=std" style='font-weight:bold;margin-left: 70px;' classStyle="btn btn-link label label-warning graded" value="Vue standard" action="caisse.journee.work_find" icon="fa fa-reorder" tooltip="Vue standard" /> |
      </div>
      <!--Header Buttons-->
      <jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
      <!--Header Buttons End-->
  </div>
  <!-- /Page Header -->

<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
<div class="row">
        <div class="col-lg-12 col-sm-12 col-xs-12">
              <div class="tabbable">

<c:set var="bigDecimalUtil" value="<%=new BigDecimalUtil() %>" />
<c:set var="zero" value="<%=BigDecimalUtil.ZERO %>" />                    

<!-- row -->
<std:form name="search-form">
	<div class="row">
        <div class="form-group">
        	<std:label classStyle="control-label col-md-2" value="Date d&eacute;but" />
            <div class="col-md-2">
                 <std:date name="dateDebut" required="true" value="${dateDebut }"/>
            </div>
            <div class="col-md-2" style="text-align: center;">
            	<std:link action="caisse.journee.work_find" params="prev=1" icon="fa fa-arrow-circle-left" tooltip="Mois pr&eacute;c&eacute;dent" />
            	<std:link action="caisse.journee.work_find" params="next=1" icon="fa fa-arrow-circle-right" tooltip="Mois suivant" />
            </div>
            
            <std:label classStyle="control-label col-md-1" value="Date fin" />
            <div class="col-md-2">
                 <std:date name="dateFin" required="true" value="${dateFin }"/>
            </div>
            <div class="col-md-2">
           	 	<std:button action="caisse.journee.work_find" value="Filtrer" classStyle="btn btn-primary" />
           	 </div>	
       </div>
   </div>
	<!-- Liste des caisses -->
	<cplx:table name="list_journee" transitionType="simple" width="100%" forceFilter="true" titleKey="journee.list" initAction="caisse.journee.work_find" checkable="false" autoHeight="true">
		<cplx:header>
			<cplx:th type="empty" />
			<cplx:th type="date" valueKey="journee.date_journee" field="journee.date_journee" width="90"/>
			<cplx:th type="string" valueKey="journee.statut_journee" field="journee.statut_journee" groupValues="${statutArray }" /> 
	<%if(isDetailJourneeDroit){ %>		
			<cplx:th type="decimal" value="Especes" field="journee.mtt_espece" width="110" />
			<cplx:th type="decimal" value="Ch&egrave;que" field="journee.mtt_cheque" width="100" />
			<cplx:th type="decimal" value="Ch&egrave;que d&eacute;j." field="journee.mtt_dej" width="100" />
			<cplx:th type="decimal" value="Carte" field="journee.mtt_cb" width="110" />
			<%if(isPoints){ %>	
			<cplx:th type="decimal" value="Fid&eacute;lit&eacute;" width="110" field="journee.mtt_donne_point" />
			<%} %>
			<%if(isPortefeuille){ %>	
			<cplx:th type="decimal" value="Portefeuille" width="100" field="journee.mtt_portefeuille" />
			<%} %>
			<cplx:th type="decimal" value="Mtt r&eacute;duction Cmd" field="journee.mtt_reduction" width="80" />
			<cplx:th type="decimal" value="Mtt r&eacute;duction Art" field="journee.mtt_art_reduction" width="80" />
			<cplx:th type="decimal" value="Mtt offert" field="journee.mtt_art_offert" width="100" />
			<cplx:th type="decimal" value="Mtt livraison" width="110" filtrable="false" sortable="false" />
	<%} %>		
		</cplx:header>
		<cplx:body>
			<c:forEach items="${list_journee }" var="journee">
				<cplx:tr workId="${journee.id }">
					<cplx:td>
						<work:edit-link/>
					</cplx:td>
					<cplx:td value="${journee.date_journee}"></cplx:td>
					<cplx:td align="center">
						<c:choose>
							<c:when test="${journee.statut_journee =='O' }">
								<span class="label" style="font-weight: bold; color: green;">Ouverte</span>
							</c:when>
							<c:when test="${journee.statut_journee =='C' }">
								<span class="label" style="font-weight: bold; color:orange;">Cl&ocirc;tur&eacute;e</span>
							</c:when>
						</c:choose>
					</cplx:td>
	<%if(isDetailJourneeDroit){ %>		 				
					<cplx:td style="font-weight:bold;" align="right" value="${journee.mtt_espece}"></cplx:td>
					<cplx:td align="right" value="${journee.mtt_cheque }"></cplx:td>
					<cplx:td align="right" value="${journee.mtt_dej }"></cplx:td>
					<cplx:td align="right" value="${journee.mtt_cb}"></cplx:td>
					<%if(isPoints){ %>	
					<cplx:td align="right" value="${journee.mtt_donne_point}"></cplx:td>
					<%} %>
					<%if(isPortefeuille){ %>	
					<cplx:td align="right" value="${journee.mtt_portefeuille }"></cplx:td>
					<%} %>
					<cplx:td align="right" value="${journee.mtt_reduction }"></cplx:td>
					<cplx:td align="right" value="${journee.mtt_art_reduction }"></cplx:td>
					<cplx:td align="right" value="${journee.mtt_art_offert }"></cplx:td>
					<cplx:td align="right" value="${journee.getMttLivraisonGlobal() }"></cplx:td>
	<%} %>				
				</cplx:tr>
			</c:forEach>
		    <c:if test="${!list_journee.isEmpty()}">
				<tr class="sub">
					<td colspan="3"></td>
	<%if(isDetailJourneeDroit){ %>						
					<td align="right">
						<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
							<fmt:formatDecimal value="${journee_total.mtt_espece }"/>
						</span>
					</td>
					<td align="right">
						<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
							<fmt:formatDecimal value="${journee_total.mtt_cheque }"/>
						</span>
					</td>
					<td align="right">
						<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
							<fmt:formatDecimal value="${journee_total.mtt_dej }"/>
						</span>
					</td>
					<td align="right">
						<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
							<fmt:formatDecimal value="${journee_total.mtt_cb }"/>
						</span>
					</td>
					<%if(isPoints){ %>	
					<td align="right">
						<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
							<fmt:formatDecimal value="${journee_total.mtt_donne_point }"/>
						</span>
					</td>
					<%} %>
					<%if(isPortefeuille){ %>	
					<td align="right">
						<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
							<fmt:formatDecimal value="${journee_total.mtt_portefeuille }"/>
						</span>
					</td>
					<%} %>
					<td align="right">
						<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
							<fmt:formatDecimal value="${journee_total.mtt_reduction }"/>
						</span>
					</td>
					<td align="right">
						<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
							<fmt:formatDecimal value="${journee_total.mtt_art_reduction }"/>
						</span>
					</td>
					<td align="right">
						<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
							<fmt:formatDecimal value="${journee_total.mtt_art_offert }"/>
						</span>
					</td>
					<td align="right">
						<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
							<fmt:formatDecimal value="${journee_total.mtt_livraison }"/>
						</span>
					</td>
		<%} %>			
				</tr>
			</c:if>	
		</cplx:body>
		
<script type="text/javascript">
/*Handles ToolTips*/
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script> 
	</cplx:table>
 </std:form>			
</div>
 			</div>
		</div>
	</div>
