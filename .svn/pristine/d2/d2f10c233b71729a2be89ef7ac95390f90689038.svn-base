<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<%
	String typeMouvement = (String) ControllerUtil.getMenuAttribute("type_mvmnt", request);
%>

<script type="text/javascript">
	$(document).ready(function (){
		$(document).off('click', "a[id^='lnk_det']");
		$(document).on('click', "a[id^='lnk_det']", function(){
			setTrContent($(this).attr("curr"), "<%=EncryptionUtil.encrypt("stock.mouvement.editTrMvm")%>");
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
         <li>Gestion de stock</li>
         <li class="active">Achats group&eacute;s</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
      	  <std:link actionGroup="C" params="tp=a" style='font-weight:bold;' noJsValidate="true" classStyle="btn btn-link label label-warning graded" value="Vue mouvements" action="stock.mouvement.work_find" icon="fa fa-reorder" tooltip="Vue mouvements" />
      	  
      	  <std:link actionGroup="C" classStyle="btn btn-default" style="color:black;" action="stock.regroupementMvm.work_init_create" params="tpR=MVM" icon="fa-3x fa fa-archive" value="Regrouper" tooltip="Regrouper les achats" />
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

	<!-- row -->
	<div class="row">
  
<std:form name="search-form">	
	<!-- Liste des mouvements -->
	<% String nomTable="mouvement.achat.list";
	%>
	
	<div class="row">
        <div class="form-group">
        	<std:label classStyle="control-label col-md-2" value="Date d&eacute;but" />
            <div class="col-md-2">
                 <std:date name="dateDebut" required="true" value="${dateDebut }"/>
            </div>
            <div class="col-md-2" style="text-align: center;">
            	<std:link action="stock.mouvement.find_mvm_groupe" params="prev=1&is_fltr=1" icon="fa fa-arrow-circle-left" tooltip="Journ&eacute;e pr&eacute;c&eacute;dente" />
            	<std:link action="stock.mouvement.find_mvm_groupe" params="next=1&is_fltr=1" icon="fa fa-arrow-circle-right" tooltip="Journ&eacute;e suivante" />
            </div>
            
            <std:label classStyle="control-label col-md-1" value="Date fin" />
            <div class="col-md-2">
                 <std:date name="dateFin" required="true" value="${dateFin }"/>
            </div>
            <div class="col-md-2">
           	 	<std:button action="stock.mouvement.find_mvm_groupe" params="is_fltr=1" value="Filtrer" classStyle="btn btn-primary" />
           	 </div>	
       </div>
   </div>
   
	<cplx:table name="list_mouvement" transitionType="simple" width="100%" forceFilter="true" titleKey="<%=nomTable %>" initAction="stock.mouvement.find_mvm_groupe" autoHeight="true" checkable="false">
		<cplx:header>
			<cplx:th type="empty" />
			<cplx:th type="string" value="Facture" field="mouvement.num_facture" width="170"/>
			<cplx:th type="long[]" valueKey="mouvement.opc_fournisseur" field="mouvement.opc_fournisseur.id" groupValues="${listeFournisseur }" groupKey="id" groupLabel="libelle"/>
			<cplx:th type="string" value="Paiement" field="mouvement.opc_financement_enum.code" filtrable="false" sortable="false"/>
			<cplx:th type="string" value="Destination" field="" sortable="false" filtrable="false"></cplx:th>
			<cplx:th type="decimal" value="Montant H.T" field="mouvement.montant_ht" sortable="false" filtrable="false" width="110"/>
			<cplx:th type="decimal" value="Montant T.T.C" field="mouvement.montant_ttc" sortable="false" filtrable="false" width="110"/>
			<cplx:th type="decimal" value="Montant remise" sortable="false" filtrable="false" width="110"/>	
			<cplx:th type="decimal" value="Montant net" sortable="false" filtrable="false" width="110"/>
			<cplx:th type="empty" />
		</cplx:header>
		<cplx:body>
			<c:set var="oldDate" value="${null }"></c:set>
			<c:forEach items="${list_mouvement }" var="mouvement">
				<c:if test="${oldDate == null  or oldDate != mouvement.date_mouvement }">
					<tr>
						<td colspan="10" noresize="true" class="separator-group">
							<span class="fa fa-fw fa-folder-open-o separator-icon"></span> <fmt:formatDate value="${mouvement.date_mouvement}"/>
						</td>
					</tr>	
				</c:if>
				<c:set var="oldDate" value="${mouvement.date_mouvement }"></c:set>
			
				<cplx:tr workId="${mouvement.id }">
					<cplx:td>
						<std:link action="stock.regroupementMvm.work_edit" workId="${mouvement.id }" icon="fa fa-eye" classStyle="btn btn-sm btn-primary" params="curTbl=list_mouvement" />
					</cplx:td>
					<cplx:td>
						<span style="color:green;" title="Facture group&eacute;e" id="grp_parent_${mouvement.id }">
							<i class="fa fa-archive"></i>
							${empty mouvement.num_facture ? '------------' : mouvement.num_facture }
						</span>
					</cplx:td>
					<cplx:td value="${mouvement.opc_fournisseur.libelle}"></cplx:td>
					<cplx:td value="${mouvement.getPaiementsStr() }"></cplx:td>
					<cplx:td></cplx:td>
					<cplx:td align="right" style="font-weight:bold;" value="${mouvement.montant_ht }" />
					<cplx:td align="right" style="font-weight:bold;" value="${mouvement.montant_ttc }" />
					<cplx:td align="right" style="font-weight:bold;" value="${mouvement.montant_ttc_rem }" />
					<cplx:td align="right" style="font-weight:bold;" value="${mouvement.montant_ttc-mouvement.montant_ttc_rem }" />
					<cplx:td align="center">
						<std:link actionGroup="D" tooltip="Supprimer" classStyle="btn btn-sm btn-danger" action="stock.regroupementMvm.work_delete" workId="${mouvement.id }" params="curTbl=list_mouvement&nosave=true" icon="fa fa-trash-o" />
					</cplx:td>
				</cplx:tr>
			<!-- Ajout detail des groupes -->
			<c:forEach items="${mouvement.list_groupe }" var="mvmEnfant">
<!-- 			#ecddd8 -->
				<c:set var="isAvoir" value="${mvmEnfant.type_mvmnt eq 'av'}"/>
				<cplx:tr  classStyle="grp_parent_${mouvement.id }" workId="${mvmEnfant.id }" style="height:12px;background-color: ${isAvoir? '#d8e2e2':'rgb(251, 251, 251)' };display:none;">
					<cplx:td align="right" style="color:blue;">
						<work:edit-link style="background-color: transparent !important;color: black;"/>
					</cplx:td>
					
					<c:choose>
						<c:when test="${not empty mvmEnfant.num_bl}">
							<cplx:td>
								<a href="javascript:" id="lnk_det" curr="${mvmEnfant.id}">
									<span class="fa fa-plus" style="color:green;"></span>
									${mvmEnfant.num_bl} <label style="font-size: 10px;">[<fmt:formatDate value="${mvmEnfant.date_mouvement}"/>]</label>
								</a>
							</cplx:td>
						</c:when>
						<c:otherwise>
							<cplx:td>
								<a href="javascript:" id="lnk_det" curr="${mvmEnfant.id}">
									<span class="fa fa-plus" style="color:green;"></span>
									${mvmEnfant.num_facture} <span style="font-size: 10px;">[<fmt:formatDate value="${mvmEnfant.date_mouvement}"/>]</span>
								</a>
							</cplx:td>	
						</c:otherwise>	
					</c:choose>						
					<cplx:td value="${mvmEnfant.opc_fournisseur.libelle}"></cplx:td>
					<cplx:td value="${mvmEnfant.getPaiementsStr() }"></cplx:td>
					<cplx:td value="${mvmEnfant.opc_destination.titre}"></cplx:td>
					<cplx:td align="right" style="color:${isAvoir ? 'red':'green' };" value="${mvmEnfant.montant_ht}" />
					<cplx:td align="right" style="color:${isAvoir ? 'red':'green' };" value="${mvmEnfant.montant_ttc}" />
					<cplx:td align="right" style="font-weight:bold;" value="${mvmEnfant.montant_ttc_rem }" />
					<cplx:td align="right" style="font-weight:bold;" value="${isAvoir? -1*mvmEnfant.montant_ttc-mvmEnfant.montant_ttc_rem : mvmEnfant.montant_ttc-mvmEnfant.montant_ttc_rem }" />
					<cplx:td align="center">
				<c:if test="${dateMaxInventaire == null or !mvmEnfant.date_mvmEnfant.before(dateMaxInventaire)}">		
						<work:delete-link />
				</c:if>			
					</cplx:td>
				</cplx:tr>
				<tr style="display: none;" id="tr_det_${mvmEnfant.id}" class="sub">
					<td colspan="10" noresize="true" style="background-color: #fff4d3;cursor: default;" align="center" id="tr_consult_${mvmEnfant.id}">
						
					</td>
				</tr>
			</c:forEach>
			<!-- ********************************* End detail **************************************** -->
		</c:forEach>	
			<!-- Total -->
			<c:if test="${list_mouvement.size() > 0 }">
				<tr class="sub">
					<td colspan="5"></td>
					<td align="right">
						<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
							<fmt:formatDecimal value="${totalHt }"/>
						</span>
					</td>
					<td align="right">
						<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
							<fmt:formatDecimal value="${totalTtc }"/>
						</span>
					</td>
					<td align="right">
						<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
							<fmt:formatDecimal value="${totalRemise }"/>
						</span>
					</td>
					<td align="right">
						<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
							<fmt:formatDecimal value="${totalApresRemise }"/>
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