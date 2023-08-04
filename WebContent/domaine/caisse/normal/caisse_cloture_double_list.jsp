<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseJourneePersistant"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="java.util.List"%>
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
boolean isAdmin = BooleanUtil.isTrue(ContextAppli.getUserBean().getIs_admin());
%>
 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion des journ&eacute;es</li>
         <li>Double cl&ocirc;ture des shifts</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
      <%if(ControllerUtil.getMenuAttribute("IS_DASH_JRN", request) == null){ %>
      	<std:link classStyle="btn btn-default" action="caisse.journee.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
      <%} %>	
      </div>
      <!--Header Buttons-->
      <jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
      <!--Header Buttons End-->
  </div>
  <!-- /Page Header -->

<%
List<CaisseJourneePersistant> listDataShift = (List) request.getAttribute("listDataShift");
%>

<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
<std:form name="data-form">	
	<div class="row">
		<table class="table table-hover table-striped table-bordered">
			<thead class="bordered-blueberry">
				<tr>
					<th>Shift</th>
					<th style="width: 150px;">Statut</th>
					<th>Caissier</th>
					<th style="width: 140px"></th>
				</tr>
			</thead>
			<tbody>
			<%if(listDataShift != null){
				String oldCaisse = null;
				int idxShift = 0;
				for(CaisseJourneePersistant caisseJouneeP : listDataShift){
					if(!"C".equals(caisseJouneeP.getStatut_caisse())){
						continue;
					}
					
					boolean isCaisseDoubleCloturee = (caisseJouneeP.getMtt_cloture_old_espece() != null || caisseJouneeP.getMtt_cloture_old_cb() != null || 
							caisseJouneeP.getMtt_cloture_old_cheque() != null || caisseJouneeP.getMtt_cloture_old_dej()!= null);
					
					idxShift++;
					
					if(oldCaisse == null || !oldCaisse.equals(caisseJouneeP.getOpc_caisse().getReference())){
						idxShift = 1;
					%>
						<tr>
							<td style="color: #8bc34a;font-size: 16px;" colspan="4"><b><%=caisseJouneeP.getOpc_caisse().getReference().toUpperCase() %></b></td>
						</tr>
					<%}
					oldCaisse = caisseJouneeP.getOpc_caisse().getReference();
					%>
					<tr>
						<td style="font-size: 16px;padding-left: 30px;">
								Shift <b><%=idxShift %></b> de <b><%=DateUtil.dateToString(caisseJouneeP.getDate_ouverture(), "HH:mm:ss") %></b>
						</td>
						<td>
							<%if(caisseJouneeP.getMtt_cloture_old_espece() != null || caisseJouneeP.getMtt_cloture_old_cb() != null || 
									  caisseJouneeP.getMtt_cloture_old_cheque() != null || caisseJouneeP.getMtt_cloture_old_dej()!= null){%>
									 <span style='color:red;font-weight: bold;'>Clos (Double)</span>
							<%} else{ %>
								<%if("O".equals(caisseJouneeP.getStatut_caisse())){%>
									<span style='color:green;'>Ouvert</span>
								<%} else if("E".equals(caisseJouneeP.getStatut_caisse())){%>
									<span style='color:orange;'>En Cloture</span>
								<%} else{%>
									<span style='color:red;'>Clos</span>
								<%} 
							}%>
						</td>
						<td>
							<b style="color: black;font-size: 13px;"><%=(caisseJouneeP.getOpc_user()!=null?caisseJouneeP.getOpc_user().getLogin():"") %></b>
						</td>
						<td style="text-align: center;">
							<%if("C".equals(caisseJouneeP.getStatut_caisse()) && (!isCaisseDoubleCloturee || isAdmin)){ %>
								<%String params = "rect=1&ca="+caisseJouneeP.getId(); %> 
								<std:linkPopup actionGroup="X" classStyle="btn btn-primary" action="caisse.caisse.init_cloturer_definitive" params="<%=params %>" icon="fa-3x fa-pie-chart" tooltip="Modifier shift"  value="Modifier le shift"/>
							<%} %>
						</td>
					</tr>
					<%
				}
			}%>
			</tbody>
		</table>
 	</div>
 	</std:form>
</div>
