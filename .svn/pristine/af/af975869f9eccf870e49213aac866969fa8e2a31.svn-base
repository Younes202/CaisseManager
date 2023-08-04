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
String type = (String) ControllerUtil.getMenuAttribute("tpD", request);
if(type == null){
	type = "D";
}
%>

<script type="text/javascript">
	$(document).ready(function (){
		manageDropMenu("list_chargeDivers");
		
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
         <% if(type.equals("D")){ %>
         <li>Fiche de d&eacute;pense</li>
         <%} else{%>
			<li>Fiche de recette</li>
		<%} %>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:link actionGroup="C" classStyle="btn btn-default" action="stock.chargeDivers.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
          <std:link actionGroup="C" classStyle="btn btn-default" style="color:black;" action="stock.regroupementMvm.work_init_create" params="tpR=CH" icon="fa-3x fa fa-archive" value="Regrouper" tooltip="Regrouper les d&eacute;penses" />
          <std:link actionGroup="C" style='font-weight:bold;margin-left: 70px;' noJsValidate="true" classStyle="btn btn-link label label-warning graded" value="Charges group&eacute;es" action="stock.chargeDivers.find_charge_groupe" icon="fa fa-reorder" tooltip="D&eacute;penses group&eacute;es" /> |
          <std:linkPopup actionGroup="C" style='font-weight:bold;margin-left: 10px;' noJsValidate="true" classStyle="btn btn-link label label-default graded" value="Libellés" action="stock.chargeDivers.load_labels" icon="fa fa-reorder" tooltip="Libellés paramétrés" /> 
          |
          <std:linkPopup noJsValidate="true" actionGroup="C" style='margin-left: 10px;' action="admin.dataForm.work_find" icon="fa fa-cogs" tooltip="Données formulaire" value="Formulaire" params="tp=CHARGE_<%=type %>" />
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
		String nomTable="chargeDivers.recette.list";
		if(type.equals("D")){
			nomTable="chargeDivers.depense.list";
		}
	%>
	
	
<std:form name="search-form">
	<div class="row">
        <div class="form-group">
        	<div class="col-md-11">
	        	<div class="col-md-12">
		        	<std:label classStyle="control-label col-md-1" value="Date d&eacute;but" />
		            <div class="col-md-2">
		                 <std:date name="dateDebut" required="true" value="${dateDebut }"/>
		            </div>
		            <div class="col-md-2" style="text-align: center;">
		            	<std:link action="stock.chargeDivers.work_find" params="prev=1&is_fltr=1" icon="fa fa-arrow-circle-left" tooltip="Journ&eacute;e pr&eacute;c&eacute;dente" />
		            	<std:link action="stock.chargeDivers.work_find" params="next=1&is_fltr=1" icon="fa fa-arrow-circle-right" tooltip="Journ&eacute;e suivante" />
		            </div>
		            
		            <std:label classStyle="control-label col-md-1" value="Date fin" />
		            <div class="col-md-2">
		                 <std:date name="dateFin" required="true" value="${dateFin }"/>
		            </div>
		        </div>   
		        <div class="col-md-12" style="margin-top: 5px;">   
		            <std:label addSeparator="false" classStyle="control-label col-md-1" value="Immobilisation" />
		            <div class="col-md-2">
		            	<std:select type="string" name="immoFilter" data="${immoData }" value='<%=ControllerUtil.getMenuAttribute("immoFilter", request) %>' />
		            </div>
		            <std:label addSeparator="false" classStyle="control-label col-md-1" value="Travaux" />
		            <div class="col-md-2">
		            	<std:select type="string" name="travauxFilter" data="${travauxData }" value='<%=ControllerUtil.getMenuAttribute("travauxFilter", request) %>' />
		            </div>
		            <std:label addSeparator="false" classStyle="control-label col-md-1" value="Type" />
		            <div class="col-md-2">
		            	<std:select name="consoFilter" width="100%" type="long" key="id" labels="code;'-';libelle" isTree="true" data="${familleConsommation }" value='<%=ControllerUtil.getMenuAttribute("consoFilter", request) %>' />
		            </div>
		            <std:label addSeparator="false" classStyle="control-label col-md-1" value="Fournisseur" />
		            <div class="col-md-2">
		            	<std:select name="fournFilter" width="100%" type="long" key="id" labels="libelle;' ';marque" isTree="true" groupKey="opc_famille.id" groupLabels="opc_famille.code;'-';opc_famille.libelle" data="${listeFournisseur }" value='<%=ControllerUtil.getMenuAttribute("fournFilter", request) %>' />
		            </div>
		        </div>
		    </div>    
            <div class="col-md-1" style="padding-top: 20px;">
           	 	<std:button action="stock.chargeDivers.work_find" value="Filtrer" params="is_fltr=1" classStyle="btn btn-primary" />
           	 </div>	
       </div>
   </div>
	<!-- Liste des chargeDivers -->
	<cplx:table name="list_chargeDivers" transitionType="simple" forceFilter="true" width="100%" titleKey="<%=nomTable %>" checkable="false" initAction="stock.chargeDivers.work_find" autoHeight="true">
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
			<cplx:th type="string" value="TVA" field="chargeDivers.opc_tva_enum.id" groupValues="${listeTva }" groupKey="id" groupLabel="libelle" width="80"/>
			<cplx:th type="decimal" valueKey="chargeDivers.montant" field="chargeDivers.montant" width="100"/>
			<cplx:th type="empty" width="130"/>
		</cplx:header>
		<cplx:body>
		
			<c:set var="oldDate" value="${null }"></c:set>
			<c:forEach items="${list_chargeDivers }" var="chargeDivers">
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
						<std:link actionGroup="C" classStyle="btn btn-sm btn-primary" workId="${chargeDivers.id }" action="stock.chargeDivers.work_edit" params="tpR=CH" icon="fa-3x fa fa-eye" tooltip="Groupe BL" />
					</cplx:td>
					<cplx:td>
						<c:if test="${chargeDivers.is_automatique}">
							<i class="fa fa-clock-o" style="color: green;"></i>
						</c:if>	
						<a href="javascript:" id="lnk_det" curr="${chargeDivers.id}"><span class="fa fa-plus" style="color:green;"></span> 
							${empty chargeDivers.num_bl ? '------------' : chargeDivers.num_bl }
						</a>
						<c:if test="${chargeDivers.nbr_annee_amo!=null}">
							<i class="fa fa-recycle" title="Immobilisation ${chargeDivers.nbr_annee_amo } ans"></i>
						</c:if>
						<c:if test="${chargeDivers.opc_travaux.libelle!=null}">
							<i class="fa fa-wrench" style="color: fuchsia;" title="${chargeDivers.opc_travaux.libelle}"></i>
						</c:if>
					</cplx:td>
					<cplx:td value="${chargeDivers.libelle}"></cplx:td>
					<cplx:td value="${chargeDivers.opc_famille_consommation.code}-${chargeDivers.opc_famille_consommation.libelle}"></cplx:td>
					<cplx:td value="${chargeDivers.opc_fournisseur.libelle}"></cplx:td>
					<cplx:td value="${chargeDivers.getPaiementsStr()}"></cplx:td>
					<cplx:td align="right" style="font-weight:bold;" value="${chargeDivers.opc_tva_enum.libelle}"></cplx:td>
					<cplx:td align="right" style="font-weight:bold;" value="${chargeDivers.montant}"></cplx:td>
					<cplx:td align="right">
						<work:delete-link/>
						<div class="btn-group">
	                         <a class="btn btn-sm btn-palegreen dropdown-toggle shiny" data-toggle="dropdown" href="javascript:void(0);" aria-expanded="false"><i class="fa fa-angle-down"></i></a>
	                         <ul class="dropdown-menu dropdown-primary">
		                         	<c:choose>
							 			<c:when test="${chargeDivers.is_automatique and chargeDivers.is_active}">
							 				<li>
							 					<std:link action="stock.chargeDivers.controleAutomate" icon="fa fa-pause" workId="${chargeDivers.id }" noJsValidate="true" value="Désactiver" tooltip="Désactiver" classStyle="btn btn-sm btn-warning shiny"/>
							 				</li>
							 				<li>
							 					<std:linkPopup actionGroup="U" action="stock.chargeDivers.automateCharge" icon="fa fa-pencil-square-o" noJsValidate="true" workId="${chargeDivers.id }" value="Automatiser" tooltip="Automatiser" classStyle="btn btn-sm btn-sky shiny"/>
							 				</li>	
							 			</c:when>
							 			<c:when test="${chargeDivers.is_automatique}">
							 				<li>
							 					<std:link action="stock.chargeDivers.controleAutomate" icon="fa fa-play" workId="${chargeDivers.id }" noJsValidate="true" value="Activer" tooltip="Activer" classStyle="btn btn-sm btn-success shiny"/>
							 				</li>
							 				<li>
							 					<std:linkPopup actionGroup="U" action="stock.chargeDivers.automateCharge" icon="fa fa-pencil-square-o" noJsValidate="true" workId="${chargeDivers.id }" value="Automatiser" tooltip="Automatiser" classStyle="btn btn-sm btn-sky shiny"/>
							 				</li>	
							 			</c:when>
							 			<c:otherwise>
							 				<li>
							 					<std:linkPopup action="stock.chargeDivers.automateCharge" icon="fa fa-clock-o" workId="${chargeDivers.id }" noJsValidate="true" value="Automatiser" tooltip="Automatiser" classStyle="btn btn-sm btn-success"/>
							 				</li>	
							 			</c:otherwise>
						 			</c:choose>
							 	<li>
									<std:linkPopup actionGroup="S" classStyle="" style="color: ${chargeDivers.opc_travaux==null?'blue':'red' };" action="stock.mouvement.manage_travaux" workId="${mouvement.id}" params="src=CD&trv=${mouvement.opc_travaux.id }" value="Associer aux travaux" icon="fa-mail-reply-all" tooltip="Associer aux travaux" />
								</li>
	                         </ul>
                         </div>
					</cplx:td>
				</cplx:tr>
				<tr style="display: none;" id="tr_det_${chargeDivers.id}" class="sub">
					<td colspan="10" noresize="true" style="background-color: #fff4d3;cursor: default;" align="center" id="tr_consult_${chargeDivers.id}">
						
					</td>
				</tr>
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