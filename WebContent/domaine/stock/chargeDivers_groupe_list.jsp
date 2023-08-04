<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@page errorPage="/commun/error.jsp" %>

<% 
	String type = (String) ControllerUtil.getMenuAttribute("tp", request);
	type = (type == null ? "D" : type);
%>

<script type="text/javascript">
	$(document).ready(function (){
		$(document).off('click', "a[id^='lnk_det']");
		$(document).on('click', "a[id^='lnk_det']", function(){
			setTrContent($(this).attr("curr"), "<%=EncryptionUtil.encrypt("stock.chargeDivers.editTrMvm")%>");
		});
		$(document).off('click', "span[id^='grp_parent']");
		$(document).on('click', "span[id^='grp_parent']", function(){
			$("."+$(this).attr("id")).toggle(500);
		});
	});
</script>

 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Finance</li>
         <li class="active">Regroupement</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:link actionGroup="C" style='font-weight:bold;' noJsValidate="true" classStyle="btn btn-link label label-warning graded" value="Vue d&eacute;penses" action="stock.chargeDivers.work_find" icon="fa fa-reorder" tooltip="D&eacute;penses" /> |
          <std:link actionGroup="C" classStyle="btn btn-default" style="color:black;" action="stock.regroupementMvm.work_init_create" params="tpR=CH" icon="fa-3x fa fa-archive" value="Regrouper" tooltip="Regrouper les d&eacute;penses" />
      </div>
      <!--Header Buttons-->
      <jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
      <!--Header Buttons End-->
  </div>
  <!-- /Page Header -->

<!-- Page Body -->
<div class="page-body" style="overflow: visible important!;position:absolute important!;z-index:1;">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>

	<!-- row -->
	<div class="row" style="overflow: visible important!;position:absolute important!;z-index:5;">
	
	<% 
		String nomTable = "chargeDivers.depense.list";
	%>
	
	
<std:form name="search-form">
	<div class="row">
        <div class="form-group">
        	<std:label classStyle="control-label col-md-2" value="Date d&eacute;but" />
            <div class="col-md-2">
                 <std:date name="dateDebut" required="true" value="${dateDebut }"/>
            </div>
            <div class="col-md-2" style="text-align: center;">
            	<std:link action="stock.chargeDivers.find_charge_groupe" params="prev=1&is_fltr=1" icon="fa fa-arrow-circle-left" tooltip="Journ&eacute;e pr&eacute;c&eacute;dente" />
            	<std:link action="stock.chargeDivers.find_charge_groupe" params="next=1&is_fltr=1" icon="fa fa-arrow-circle-right" tooltip="Journ&eacute;e suivante" />
            </div>
            
            <std:label classStyle="control-label col-md-1" value="Date fin" />
            <div class="col-md-2">
                 <std:date name="dateFin" required="true" value="${dateFin }"/>
            </div>
            <div class="col-md-2">
           	 	<std:button action="stock.chargeDivers.find_charge_groupe" value="Filtrer" params="is_fltr=1" classStyle="btn btn-primary" />
           	 </div>	
       </div>
   </div>
	<!-- Liste des chargeDivers -->
	<cplx:table name="list_chargeDivers" transitionType="simple" forceFilter="true" width="100%" titleKey="<%=nomTable %>" checkable="false" initAction="stock.chargeDivers.find_charge_groupe" autoHeight="true">
		<cplx:header>
			<cplx:th type="empty" />
			<cplx:th type="date" valueKey="chargeDivers.date_mouvement" field="chargeDivers.date_mouvement" width="100" filterOnly="true"/>
			
			<cplx:th type="string" valueKey="chargeDivers.num_bl" field="chargeDivers.num_bl" width="150"/>
			<cplx:th type="string" valueKey="chargeDivers.libelle" field="chargeDivers.libelle"/>
			<% if(type.equals("D")){ %>
			<cplx:th type="long[]" valueKey="chargeDivers.type_depense" field="chargeDivers.opc_famille_consommation.id" groupValues="${familleConsommation }" groupKey="id" groupLabel="code;'-';libelle" width="170"/>
			<%} else{ %>
			<cplx:th type="long[]" valueKey="chargeDivers.type_recette" field="chargeDivers.opc_famille_consommation.id" groupValues="${familleConsommation }" groupKey="id" groupLabel="code;'-';libelle" width="170"/>
			<%} %>
			<cplx:th type="long[]" valueKey="chargeDivers.opc_fournisseur" field="chargeDivers.opc_fournisseur.id" groupValues="${listeFournisseur }" groupKey="id" groupLabel="libelle"/>
			<cplx:th type="string" valueKey="chargeDivers.opc_financement_enum" field="chargeDivers.opc_financement_enum.libelle" sortable="false" filtrable="false" width="100"/>
			<cplx:th type="long[]" value="TVA" field="chargeDivers.opc_tva_enum.id" groupValues="${listeTva }" groupKey="id" groupLabel="libelle" width="80"/>
			<cplx:th type="decimal" valueKey="chargeDivers.montant" field="chargeDivers.montant" width="100"/>
			<cplx:th type="empty" width="130"/>
		</cplx:header>
		<cplx:body>
		
			<c:set var="oldDate" value="${null }"></c:set>
			<c:forEach items="${list_charge }" var="chargeDivers">
				<c:if test="${oldDate == null  or oldDate != chargeDivers.date_mouvement }">
					<tr>
						<td colspan="9" noresize="true" class="separator-group">
							<span class="fa fa-fw fa-folder-open-o separator-icon"></span> <fmt:formatDate value="${chargeDivers.date_mouvement}"/>
						</td>
					</tr>	
				</c:if>
				<c:set var="oldDate" value="${chargeDivers.date_mouvement }"></c:set>
				
				<cplx:tr workId="${chargeDivers.id }">
					<cplx:td>
						<std:link actionGroup="C" classStyle="btn btn-sm btn-primary" workId="${chargeDivers.id }" action="stock.regroupementMvm.work_edit" params="tpR=CH" icon="fa-3x fa fa-eye" tooltip="Groupe BL" />
					</cplx:td>
					<cplx:td>
						<c:if test="${chargeDivers.is_automatique}">
							<i class="fa fa-clock-o" style="color: green;"></i>
						</c:if>	
						
						<span class="fa fa-plus" style="color:green;"></span>
						<span style="color:green;" title="Facture group&eacute;e" id="grp_parent_${chargeDivers.id }">
							${empty chargeDivers.num_bl ? '------------' : chargeDivers.num_bl } : ${chargeDivers.opc_fournisseur.libelle}
							<i class="fa fa-archive"></i>
						</span>
					</cplx:td>
					<cplx:td value="${chargeDivers.libelle}"></cplx:td>
					<cplx:td value="${chargeDivers.opc_famille_consommation.code}-${chargeDivers.opc_famille_consommation.libelle}"></cplx:td>
					<cplx:td value="${chargeDivers.opc_fournisseur.libelle}"></cplx:td>
					<cplx:td value="${chargeDivers.getPaiementsStr()}"></cplx:td>
					<cplx:td align="right" style="font-weight:bold;" value="${chargeDivers.opc_tva_enum.libelle}"></cplx:td>
					<cplx:td align="right" style="font-weight:bold;" value="${chargeDivers.montant}"></cplx:td>
					<cplx:td align="right">
				 		<work:delete-link/>
					</cplx:td>
				</cplx:tr>
				<c:if test="${empty chargeDivers.is_groupant }">				
					<tr style="display: none;" id="tr_det_${chargeDivers.id}" class="sub">
						<td colspan="8" noresize="true" style="background-color: #fff4d3;cursor: default;" align="center" id="tr_consult_${chargeDivers.id}">
							
						</td>
					</tr>
				</c:if>
			
				<!-- Ajout detail des groupes -->
				<c:forEach items="${chargeDivers.list_groupe }" var="mvmEnfant">
					<cplx:tr classStyle="grp_parent_${chargeDivers.id }" workId="${mvmEnfant.id }" style="height:12px;background-color: rgb(251, 251, 251);display:none;">
						<cplx:td>
						</cplx:td>
						<cplx:td>
							<work:edit-link style="background-color: transparent !important;color: black;"/>
							<a href="javascript:" id="lnk_det" curr="${mvmEnfant.id}">
								+ ${mvmEnfant.num_bl}
							</a>
						</cplx:td>
						<cplx:td style="color:blue;">
							<a href="javascript:" id="lnk_det" curr="${mvmEnfant.id}">
								+ ${mvmEnfant.libelle}
							</a>
						</cplx:td>
						<cplx:td value="${mvmEnfant.opc_famille_consommation.code}-${mvmEnfant.opc_famille_consommation.libelle}"></cplx:td>
						<cplx:td value="${mvmEnfant.opc_fournisseur.libelle}"></cplx:td>
						<cplx:td value="${mvmEnfant.getPaiementsStr()}"></cplx:td>
						<cplx:td align="right" style="font-weight:bold;" value="${mvmEnfant.opc_tva_enum.libelle}"></cplx:td>
						<cplx:td align="right" style="font-weight:bold;" value="${mvmEnfant.montant}"></cplx:td>
						<cplx:td align="right">
							<c:choose>
					 			<c:when test="${mvmEnfant.is_automatique and mvmEnfant.is_active }">
					 				<std:link action="stock.chargeDivers.controleAutomate" icon="fa fa-pause" workId="${mvmEnfant.id }" noJsValidate="true" tooltip="D&eacute;sactiver" classStyle="btn btn-sm btn-warning shiny"/>
					 				<std:linkPopup actionGroup="U" action="stock.chargeDivers.automateCharge" noJsValidate="true" icon="fa fa-pencil-square-o" workId="${mvmEnfant.id }" tooltip="Automatiser" classStyle="btn btn-sm btn-sky shiny"/>
					 			</c:when>
					 			<c:when test="${mvmEnfant.is_automatique }">
					 				<std:link action="stock.chargeDivers.controleAutomate" icon="fa fa-play" noJsValidate="true" workId="${mvmEnfant.id }" tooltip="Activer" classStyle="btn btn-sm btn-success shiny"/>
					 				<std:linkPopup actionGroup="U" action="stock.chargeDivers.automateCharge" noJsValidate="true" icon="fa fa-pencil-square-o" workId="${mvmEnfant.id }" tooltip="Automatiser" classStyle="btn btn-sm btn-sky shiny"/>
					 			</c:when>
					 			<c:otherwise>
					 				<std:linkPopup action="stock.chargeDivers.automateCharge" icon="fa fa-clock-o" workId="${mvmEnfant.id }" tooltip="Automatiser" classStyle="btn btn-sm btn-success"/>
					 			</c:otherwise>
					 		</c:choose>
					 		
							<work:delete-link />
						</cplx:td>
					</cplx:tr>
					<tr style="display: none;" id="tr_det_${mvmEnfant.id}" class="sub">
						<td colspan="8" noresize="true" style="background-color: #fff4d3;cursor: default;" align="center" id="tr_consult_${mvmEnfant.id}">
							
						</td>
					</tr>
				</c:forEach>
			<!-- ********************************* End detail **************************************** -->
				
			</c:forEach>
	<c:if test="${list_chargeDivers.size() > 0 }">
			<tr class="sub">
				<td colspan="7"></td>
				<td align="right">
					<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
						<fmt:formatDecimal value="${totalTtc }"/>
					</span>
				</td>
				<td></td>
				<td></td>
			</tr>
	</c:if>		
		</cplx:body>
	</cplx:table>
 </std:form>
 </div>
				<!-- end widget content -->

</div>
				<!-- end widget div -->