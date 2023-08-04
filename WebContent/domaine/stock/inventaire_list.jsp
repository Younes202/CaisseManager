<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="java.util.Date"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="appli.model.domaine.stock.persistant.InventairePersistant"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<script type="text/javascript">
	$(document).ready(function (){
		manageDropMenu("list_inventaire");
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
         <li>Fiche des inventaires</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
      <c:if test="${!isInvetaireNonValide }">
          <std:link actionGroup="C" classStyle="btn btn-default" style="background-color: #dedede;" action="stock.inventaire.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
        </c:if>  
      	  <std:linkPopup actionGroup="S" classStyle="btn btn-default" style="background-color: #dedede;" action="stock.inventaire.create_pdfInventaire" icon="fa fa-download" value="Fiche inventaire" tooltip="T&eacute;l&eacute;charger fichier pdf" />
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
	<!-- Liste des inventaires -->
	<cplx:table name="list_inventaire" transitionType="simple" autoHeight="true" width="100%" titleKey="inventaire.list" initAction="stock.inventaire.work_find" checkable="false" exportable="false">
		<cplx:header>
			<cplx:th type="empty" />
			<cplx:th type="date" valueKey="inventaire.date_realisation" field="inventaire.date_realisation" width="150" filterOnly="true"/>
			<cplx:th type="long[]" valueKey="inventaire.type" field="inventaire.opc_type_enum.id" groupValues="${listeType }" groupKey="id" groupLabel="libelle" width="150"/>
			<cplx:th type="long[]" valueKey="inventaire.opc_emplacement" field="inventaire.opc_emplacement.id" groupValues="${listEmplacement }" groupKey="id" groupLabel="titre"/>
			<cplx:th type="long[]" valueKey="inventaire.opc_responsable" field="inventaire.opc_responsable.id" groupValues="${listEmploye }" groupKey="id" groupLabel="nom" width="200"/>
			<cplx:th type="long[]" valueKey="inventaire.opc_saisisseur" field="inventaire.opc_saisisseur.id" groupValues="${listEmploye }" groupKey="id" groupLabel="nom" width="200"/>
			<cplx:th type="string" valueKey="inventaire.is_valid" field="inventaire.is_valid" width="120"/>
			<cplx:th type="empty" width="190"/>
		</cplx:header>
		<cplx:body>
			<%
			Date oldDate = null;
			List<InventairePersistant> listinventaire = (List<InventairePersistant>)request.getAttribute("list_inventaire");
			for(InventairePersistant inventaire : listinventaire){
				Date dateMaxInventaire = (Date)request.getAttribute("dateMaxInven_"+inventaire.getOpc_emplacement().getId());
				boolean isPassed = false;
		
				if(oldDate == null  || oldDate.compareTo(inventaire.getDate_realisation()) != 0){ %>
					<tr>
						<td colspan="8" noresize="true" class="separator-group">
							<span class="fa fa-fw fa-folder-open-o separator-icon"></span> <%=DateUtil.dateToString(inventaire.getDate_realisation()) %>
						</td>
					</tr>	
				<%}
				oldDate = inventaire.getDate_realisation();
				 %>
			
				<cplx:tr workId="<%=inventaire.getId().toString() %>">
					<cplx:td>
						<work:edit-link />
					</cplx:td>
					<cplx:td value='<%=inventaire.getOpc_type_enum()!=null?inventaire.getOpc_type_enum().getLibelle():"" %>'></cplx:td>
					<cplx:td style="color:blue;" value='<%=inventaire.getOpc_emplacement().getTitre() %>'></cplx:td>
					<cplx:td value='<%=inventaire.getOpc_responsable()!=null?inventaire.getOpc_responsable().getNom():"" %>'></cplx:td>
					<cplx:td value='<%=inventaire.getOpc_saisisseur()!=null?inventaire.getOpc_saisisseur().getNom():"" %>'></cplx:td>
					<cplx:td align="center">
						<%if(!BooleanUtil.isTrue(inventaire.getIs_valid())){%>
							<span class="label" style="color:orange;font-weight: bold;">Non valid&eacute;</span>
						<%} else{ %>
							<span class="label" style="color:green;font-weight: bold;">Valid&eacute;</span>
						<%} %>
					</cplx:td>
					<cplx:td align="center">
						<std:link classStyle="btn btn-sm btn-default shiny" action="stock.inventaire.find_synth_inventaire" params="skipI=true&skipP=true" workId="<%=inventaire.getId().toString() %>" value="Ecart" tooltip="Consulter les &eacute;carts" icon="fa fa-th-list"/>
						<std:link target="downloadframe" classStyle="btn btn-sm btn-default shiny" action="stock.inventaire.downloadInventaireEcartPDF" params="skipI=true&skipP=true" workId="<%=inventaire.getId().toString() %>" tooltip="Imprimer les &eacute;carts" icon="fa fa-print"/>
						
						<a class="btn btn-sm btn-palegreen dropdown-toggle shiny" data-toggle="dropdown" href="javascript:void(0);" aria-expanded="false">
							<i class="fa fa-angle-down"></i>
						</a>
						
                        <ul class="dropdown-menu dropdown-primary">
                        <%if(!BooleanUtil.isTrue(inventaire.getIs_valid())){%>
							<li><std:link actionGroup="C" classStyle="" style="color:blue;" action="stock.inventaire.valider_inventaire" workId="<%=inventaire.getId().toString() %>" value="Valider" tooltip="Valider l'inventaire" /></li>
						<% }
						 if (BooleanUtil.isTrue(inventaire.getIs_valid()) && (dateMaxInventaire == null || inventaire.getDate_realisation().compareTo(dateMaxInventaire)==0) && !isPassed){ %>
							<% isPassed = true; %> 
							<li><std:link actionGroup="C" classStyle="" style="color:green;" action="stock.inventaire.work_init_update" params="is_corr=1" workId="<%=inventaire.getId().toString() %>" value="Corriger" tooltip="Corriger la saisie" /></li>
							<li><std:link actionGroup="C" classStyle="" style="color:orange;" action="stock.inventaire.annuler_validation" workId="<%=inventaire.getId().toString() %>" value="Annuler" tooltip="Annuler la validation" /></li>
						<% } %>
						<%if (!BooleanUtil.isTrue(inventaire.getIs_valid()) && (dateMaxInventaire == null || inventaire.getDate_realisation().compareTo(dateMaxInventaire)>=0)){ %>
							<li>
								<std:link actionGroup="D" classStyle="" style="color:red;" action="stock.inventaire.work_delete" workId="<%=inventaire.getId().toString() %>" value="Supprimer" tooltip="Supprimer" />
							</li>
						<% } %>	
                       </ul>
					</cplx:td>
				</cplx:tr>
			<%} %>
		</cplx:body>
	</cplx:table>
 </std:form>			
 </div>
</div>
<script src="resources/framework/js/keyboard/my_keyboard.js?v=<%=StrimUtil.getGlobalConfigPropertie("version.resources")%>"></script>