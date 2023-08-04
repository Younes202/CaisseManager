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

<script type="text/javascript">
	$(document).ready(function (){
		$(document).off('click', "a[id^='lnk_det']");
		$(document).on('click', "a[id^='lnk_det']", function(){
			setTrContent($(this).attr("curr"), "<%=EncryptionUtil.encrypt("stock.transformation.editTrMvm")%>");
		});
		$(document).off('click', "span[id^='grp_parent']");
		$(document).on('click', "span[id^='grp_parent']", function(){
			$("."+$(this).attr("id")).toggle(500);
		});
	});
</script>

<%
boolean isReferentiel = (ControllerUtil.getMenuAttribute("IS_REF", request) != null);
%>

 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion de stock</li>
         <li>Fiche transformation</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
      	  <std:link actionGroup="C" classStyle="btn btn-default" action="stock.transformation.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
      	  <std:link actionGroup="C" classStyle="btn btn-info" action="stock.preparationTransfo.work_find" icon="fa-3x fa-cogs" tooltip="Gestion des recettes" value="Gestion des recettes" />
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
	<div class="row">
        <div class="form-group">
        	<std:label classStyle="control-label col-md-2" value="Date d&eacute;but" />
            <div class="col-md-2">
                 <std:date name="dateDebut" required="true" value="${dateDebut }"/>
            </div>
            <div class="col-md-2" style="text-align: center;">
            	<std:link action="stock.transformation.work_find" params="prev=1&is_fltr=1" icon="fa fa-arrow-circle-left" tooltip="Journ&eacute;e pr&eacute;c&eacute;dente" />
            	<std:link action="stock.transformation.work_find" params="next=1&is_fltr=1" icon="fa fa-arrow-circle-right" tooltip="Journ&eacute;e suivante" />
            </div>
            
            <std:label classStyle="control-label col-md-1" value="Date fin" />
            <div class="col-md-2">
                 <std:date name="dateFin" required="true" value="${dateFin }"/>
            </div>
            <div class="col-md-2">
           	 	<std:button action="stock.transformation.work_find" params="is_fltr=1" value="Filtrer" classStyle="btn btn-primary" />
           	 </div>	
       </div>
   </div>
   
   <%if(isReferentiel){ %>
	<cplx:table name="list_mouvement" transitionType="simple" width="100%" forceFilter="true" title="Liste des recettes de transformation " initAction="stock.transformation.work_find" autoHeight="true" checkable="false">
		<cplx:header>
			<cplx:th type="empty" />
			<cplx:th type="string" value="BL" field="mouvement.num_bl" width="150" />
			<cplx:th type="string[]" value="Emplacement" field="mouvement.opc_emplacement.titre" groupValues="${listEmplacement }" groupKey="titre" groupLabel="titre"/>
			<cplx:th type="string[]" value="Destination" field="mouvement.opc_destination.titre" groupValues="${listEmplacement }" groupKey="titre" groupLabel="titre"/>
			<cplx:th type="decimal" value="Montant H.T" field="mouvement.montant_ht" sortable="false" filtrable="false" width="110"/>
			<cplx:th type="decimal" value="Montant T.T.C" field="mouvement.montant_ttc" sortable="false" filtrable="false" width="110"/>
			<cplx:th type="empty" />
		</cplx:header>
		<cplx:body>
			<c:set var="oldDate" value="${null }"></c:set>
			<c:forEach items="${list_mouvement }" var="mouvement">
				<c:if test="${oldDate == null  or oldDate != mouvement.date_mouvement }">
					<tr>
						<td colspan="7" noresize="true" class="separator-group">
							<span class="fa fa-fw fa-folder-open-o separator-icon"></span>  <fmt:formatDate value="${mouvement.date_mouvement}"/>
						</td>
					</tr>	
				</c:if>
				<c:set var="oldDate" value="${mouvement.date_mouvement }"></c:set>
			
				<cplx:tr workId="${mouvement.id }">
					<cplx:td>
						<work:edit-link/>
					</cplx:td>
					<cplx:td>
						<a href="javascript:" id="lnk_det" curr="${mouvement.id}"><span class="fa fa-plus" style="color:green;"></span> ${mouvement.num_bl }</a>
					</cplx:td>
					<cplx:td value="${mouvement.opc_emplacement.titre}"></cplx:td>
					<cplx:td value="${mouvement.opc_destination.titre}"></cplx:td>
					<cplx:td align="right" style="font-weight:bold;" value="${mouvement.montant_ht }" />
					<cplx:td align="right" style="font-weight:bold;" value="${mouvement.montant_ttc }" />
					<cplx:td align="center">
						<work:delete-link />
					</cplx:td>
				</cplx:tr>
				<tr style="display: none;" id="tr_det_${mouvement.id}" class="sub">
					<td colspan="7" noresize="true" style="background-color: #fff4d3;cursor: default;" align="center" id="tr_consult_${mouvement.id}">
						
					</td>
				</tr>
			</c:forEach>	
		</cplx:body>
	</cplx:table>   
   
   <%} else{ %>
	<cplx:table name="list_mouvement" transitionType="simple" width="100%" forceFilter="true" title="Liste des transformation" initAction="stock.transformation.work_find" autoHeight="true" checkable="false">
		<cplx:header>
			<cplx:th type="empty" />
			<cplx:th type="string" value="BL" field="mouvement.num_bl" width="150" />
			<cplx:th type="string[]" value="Emplacement" field="mouvement.opc_emplacement.titre" groupValues="${listEmplacement }" groupKey="titre" groupLabel="titre"/>
			<cplx:th type="string[]" value="Destination" field="mouvement.opc_destination.titre" groupValues="${listEmplacement }" groupKey="titre" groupLabel="titre"/>
			<cplx:th type="decimal" value="Montant H.T" field="mouvement.montant_ht" sortable="false" filtrable="false" width="110"/>
			<cplx:th type="decimal" value="Montant T.T.C" field="mouvement.montant_ttc" sortable="false" filtrable="false" width="110"/>
			<cplx:th type="empty" />
		</cplx:header>
		<cplx:body>
			<c:set var="oldDate" value="${null }"></c:set>
			<c:forEach items="${list_mouvement }" var="mouvement">
				<c:if test="${oldDate == null  or oldDate != mouvement.date_mouvement }">
					<tr>
						<td colspan="7" noresize="true" class="separator-group">
							<span class="fa fa-fw fa-folder-open-o separator-icon"></span>  <fmt:formatDate value="${mouvement.date_mouvement}"/>
						</td>
					</tr>	
				</c:if>
				<c:set var="oldDate" value="${mouvement.date_mouvement }"></c:set>
			
				<cplx:tr workId="${mouvement.id }">
					<cplx:td>
						<work:edit-link/>
					</cplx:td>
					<cplx:td>
						<a href="javascript:" id="lnk_det" curr="${mouvement.id}"><span class="fa fa-plus" style="color:green;"></span> ${mouvement.num_bl }</a>
					</cplx:td>
					<cplx:td value="${mouvement.opc_emplacement.titre}"></cplx:td>
					<cplx:td value="${mouvement.opc_destination.titre}"></cplx:td>
					<cplx:td align="right" style="font-weight:bold;" value="${mouvement.montant_ht }" />
					<cplx:td align="right" style="font-weight:bold;" value="${mouvement.montant_ttc }" />
					<cplx:td align="center">
						<work:delete-link />
					</cplx:td>
				</cplx:tr>
				<tr style="display: none;" id="tr_det_${mouvement.id}" class="sub">
					<td colspan="7" noresize="true" style="background-color: #fff4d3;cursor: default;" align="center" id="tr_consult_${mouvement.id}">
						
					</td>
				</tr>
			</c:forEach>	
		</cplx:body>
	</cplx:table>
	<%} %>
	
 </std:form>	

 </div>
					<!-- end widget content -->

				</div>
				<!-- end widget div -->