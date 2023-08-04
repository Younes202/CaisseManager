<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.TYPE_MOUVEMENT_ENUM"%>
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
	if(typeMouvement == null){
		typeMouvement = "a";
	}

	TYPE_MOUVEMENT_ENUM typeMouvementEnum = TYPE_MOUVEMENT_ENUM.valueOf(typeMouvement);
%>

<script type="text/javascript">
	$(document).ready(function (){
		manageDropMenu("list_mouvement");
		
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
         <li>Fiche <%=typeMouvementEnum.getLibelle() %></li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
      	  <std:link actionGroup="C" classStyle="btn btn-default" action="stock.mouvement.work_init_create" icon="fa-3x fa-plus" tooltip="Créer" />
      	  <%if(TYPE_MOUVEMENT_ENUM.a.toString().equals(typeMouvement)){ %>
      	  	<std:link actionGroup="C" classStyle="btn btn-default" style="color:black;" action="stock.regroupementMvm.work_init_create" params="tpR=MVM" icon="fa-3x fa fa-archive" value="Regrouper" tooltip="Regrouper les d'achats" />
      	  	<std:link actionGroup="C" style='font-weight:bold;margin-left: 70px;' noJsValidate="true" classStyle="btn btn-link label label-warning graded" value="Mouvements groupés" action="stock.mouvement.find_mvm_groupe" icon="fa fa-reorder" tooltip="Mouvements groupés" /> |
      	  <%} else if(TYPE_MOUVEMENT_ENUM.t.toString().equals(typeMouvement)){ %>
      	  	<std:link actionGroup="C" classStyle="btn btn-info" action="stock.preparation.work_find" icon="fa-3x fa-cogs" tooltip="Gestion des préparations" value="Gestion des préparations" />
      	  	<std:link actionGroup="C" classStyle="btn btn-default" target="downloadframe" style="color:blue;" action="stock.mouvement.editPdfTransfert" icon="fa-3x fa fa-download" value="PDF transfert" tooltip="PDF transfert vide" />
      	  <%} else if(TYPE_MOUVEMENT_ENUM.cm.toString().equals(typeMouvement)){ %>
      	  	<std:linkPopup actionGroup="C" classStyle="btn btn-default" style="color:blue;" action="stock.mouvement.genererBonCommandeSeuilStock" icon="fa-3x fa fa-download" value="Bon commande seuil" tooltip="Générer le bon de commande depuis les seuils d'articles" />
      	  <%} %>
      	  |
      	  <std:linkPopup noJsValidate="true" actionGroup="C" style='margin-left: 10px;' action="admin.dataForm.work_find" icon="fa fa-cogs" tooltip="Données formulaire" value="Formulaire" params="tp=MVM_<%=typeMouvement %>" />
	</div>
      <!--Header Buttons-->
      <jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
      <!--Header Buttons End-->
      <img src="resources/framework/img/badge_scanner.png" style="width: 20px;position: absolute;right: 13px;top: -28px;" title="Lecteur badge utilisable sur cet écran">
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
        	<div class="col-md-11">
	        	<div class="col-md-12">
		        	<std:label classStyle="control-label col-md-1" value="Date début" />
		            <div class="col-md-2">
		                 <std:date name="dateDebut" required="true" value="${dateDebut }"/>
		            </div>
		            <div class="col-md-2" style="text-align: center;">
		            	<std:link action="stock.mouvement.work_find" params="prev=1&is_fltr=1" icon="fa fa-arrow-circle-left" tooltip="Journée précédente" />
		            	<std:link action="stock.mouvement.work_find" params="next=1&is_fltr=1" icon="fa fa-arrow-circle-right" tooltip="Journée suivante" />
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
		            <std:label addSeparator="false" classStyle="control-label col-md-1" value="Emplacement" />
		            <div class="col-md-2">
		            	<std:select type="long" key="id" labels="titre" name="stockFilter" data="${listeDestination }" value='<%=ControllerUtil.getMenuAttribute("stockFilter", request) %>' />
		            </div>
		       </div>     
		    </div>   
            <div class="col-md-1" style="padding-top: 20px;">
           	 	<std:button action="stock.mouvement.work_find" params="is_fltr=1" value="Filtrer" classStyle="btn btn-primary" />
           	 </div>	
       </div>
   </div>
   
	<cplx:table name="list_mouvement" transitionType="simple" width="100%" forceFilter="true" title="Liste des mouvements" initAction="stock.mouvement.work_find" autoHeight="true" checkable="false">
		<cplx:header>
			<cplx:th type="empty" />
			<% if(TYPE_MOUVEMENT_ENUM.a.toString().equals(typeMouvement)){%>
				<cplx:th type="string" value="Facture" field="mouvement.num_facture" width="150" filterOnly="true"/>
			<%} %>
			<cplx:th type="string" value="BL" field="mouvement.num_bl" width="150" filterOnly="true"/>
			<% if(TYPE_MOUVEMENT_ENUM.a.toString().equals(typeMouvement)){%>
				<cplx:th type="string" value="Reçu" field="mouvement.num_recu" width="150" filterOnly="true"/>
			<%} %>
			
			<cplx:th type="string" value="Numéro" field="mouvement.num_bl" width="170" sortable="false" filtrable="false"/>

			<% if(TYPE_MOUVEMENT_ENUM.a.toString().equals(typeMouvement)){%>
				<cplx:th type="long[]" valueKey="mouvement.opc_fournisseur" field="mouvement.opc_fournisseur.id" groupValues="${listeFournisseur }" groupKey="id" groupLabel="libelle"/>
				<cplx:th type="string" value="Paiement" field="mouvement.opc_financement_enum.code" filtrable="false" sortable="false" width="120"/>
			<% } else if(TYPE_MOUVEMENT_ENUM.v.toString().equals(typeMouvement)){ %>
				<cplx:th type="long[]" value="Client" field="mouvement.opc_client.id" groupValues="${listClient }" groupKey="id" groupLabel="nom;' ';prenom"/>
				<cplx:th type="string" value="Paiement" field="mouvement.opc_financement_enum.code" filtrable="false" sortable="false" width="120"/>
			<%} %>
			<% if(!TYPE_MOUVEMENT_ENUM.a.toString().equals(typeMouvement)){%>
			<cplx:th type="long[]" value="Stock" field="mouvement.opc_emplacement.id" fieldExport="mouvement.opc_emplacement.titre"  groupValues="${listeDestination }" groupKey="id" groupLabel="titre"/>
			<% }%>
			<% if(TYPE_MOUVEMENT_ENUM.a.toString().equals(typeMouvement) || "t".equals(typeMouvement)){%>
			<cplx:th type="long[]" valueKey="mouvement.opc_destination" field="mouvement.opc_destination.id" fieldExport="mouvement.opc_destination.titre" groupValues="${listeDestination }" groupKey="id" groupLabel="titre"/>
			<% }%>
			<%  if (TYPE_MOUVEMENT_ENUM.p.toString().equals(typeMouvement)) { %>
			<cplx:th type="long[]" value="Type perte" field="mouvement.opc_type_perte_enum.id" groupValues="${typePerte }" groupKey="id" groupLabel="libelle"/>
			<%} %>
			<cplx:th type="decimal" value="Montant H.T" field="mouvement.montant_ht" width="110"/>
			<cplx:th type="decimal" value="Montant TVA" field="mouvement.montant_tva" width="110"/>
			<%if(TYPE_MOUVEMENT_ENUM.a.toString().equals(typeMouvement)){%>
			<cplx:th type="decimal" value="Remise TTC" field="mouvement.montant_ttc_rem" width="110"/>
			<%} %>
			<cplx:th type="decimal" value="Montant T.T.C" field="mouvement.montant_ttc" width="110"/>
			<cplx:th type="empty" width="100px" />
		</cplx:header>
		
		
		<c:set var="bigDecimalUtil" value="<%=new BigDecimalUtil() %>" />
		
		<cplx:body>
			<%
			int nbrCell = 0;
			
			if(TYPE_MOUVEMENT_ENUM.a.toString().equals(typeMouvement)){
				nbrCell = 10;
			} else if("t".equals(typeMouvement) || TYPE_MOUVEMENT_ENUM.p.toString().equals(typeMouvement)){
				nbrCell = 8;
			} else if(TYPE_MOUVEMENT_ENUM.v.toString().equals(typeMouvement)){
				nbrCell = 9;	
			} else{
				nbrCell = 6;
			}
			%>
			<c:set var="dateUtil" value="<%=new DateUtil() %>" />
			<c:set var="oldDate" value="${null }"></c:set>
			<c:forEach items="${list_mouvement }" var="mouvement">
				<c:if test="${oldDate == null  or dateUtil.dateToString(oldDate) != dateUtil.dateToString(mouvement.date_mouvement) }">
					<tr>
						<td colspan="<%=nbrCell %>" noresize="true" class="separator-group">
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
						<c:choose>
							<c:when test="${not empty mouvement.num_bl}">
								<c:set var="blFac" value="${mouvement.num_bl}"/>
							</c:when>
							<c:otherwise>
								<c:set var="blFac" value="${mouvement.num_facture}" />
							</c:otherwise>	
						</c:choose>
						
						<a href="javascript:" id="lnk_det" curr="${mouvement.id}">
							<span class="fa fa-plus" style="color:green;"></span>
							${blFac }
						</a>
						
						<c:if test="${not empty mouvement.mouvement_group_id }">
							<i class="fa fa-folder-open" style="color: blue;"  title="Mouvement groupé en date du ${mapDateGroupement.get(mouvement.mouvement_group_id) }"></i>
						</c:if>
						
						<% if(TYPE_MOUVEMENT_ENUM.a.toString().equals(typeMouvement)){%>
							<c:if test="${mouvement.opc_mouvement.num_bl != null }">
								<i class="fa fa-tty" style="color: green;"  title="Achat généré depuis la commande ${mouvement.opc_mouvement.num_bl }"></i>
							</c:if>
						<%} %>
						<c:if test="${mouvement.nbr_annee_amo!=null}">
							<i class="fa fa-recycle" title="Immobilisation ${mouvement.nbr_annee_amo } ans"></i>
						</c:if>	
						<c:if test="${mouvement.opc_travaux.libelle!=null}">
							<i class="fa fa-wrench" style="color: fuchsia;" title="${mouvement.opc_travaux.libelle}"></i>
						</c:if>
					</cplx:td>
					<% if(TYPE_MOUVEMENT_ENUM.a.toString().equals(typeMouvement)){%>
						<cplx:td value="${mouvement.opc_fournisseur.libelle}"></cplx:td>
						<cplx:td value="${mouvement.getPaiementsStr() }"></cplx:td>
					<% } else if(TYPE_MOUVEMENT_ENUM.v.toString().equals(typeMouvement)){ %>
						<cplx:td value="${mouvement.opc_client.nom} ${mouvement.opc_client.prenom}"></cplx:td>
						<cplx:td value="${mouvement.getPaiementsStr() }"></cplx:td>
					<%} %>
					<%if(!TYPE_MOUVEMENT_ENUM.a.toString().equals(typeMouvement)){%>
					<cplx:td value="${mouvement.opc_emplacement.titre}"></cplx:td>
					<% }%>
					<% if(TYPE_MOUVEMENT_ENUM.a.toString().equals(typeMouvement) || "t".equals(typeMouvement)){%>
					<cplx:td value="${mouvement.opc_destination.titre}"></cplx:td>
					<% }%>
					<%  if (TYPE_MOUVEMENT_ENUM.p.toString().equals(typeMouvement)) { %>
					<cplx:td value="${mouvement.opc_type_perte_enum.libelle}"></cplx:td>
					<%} %>
					<cplx:td align="right" style="font-weight:bold;" value="${mouvement.montant_ht }" />
					<cplx:td align="right" style="font-weight:bold;" value="${mouvement.montant_tva }" />
					
					<%if(TYPE_MOUVEMENT_ENUM.a.toString().equals(typeMouvement)){%>
					<cplx:td align="right" style="font-weight:bold;">
						<fmt:formatDecimal value="${mouvement.montant_ttc_rem }" />
						<c:if test="${mouvement.montant_ht_rem!=null and mouvement.montant_ht_rem!=0 }">
							<i class="fa fa-fw fa-gift" style="color: red;font-size: 14px;" data-toggle="tooltip" data-placement="buttom" 
								data-original-title="
									HT avant rem :<br>${bigDecimalUtil.formatNumberZeroBd(bigDecimalUtil.add(mouvement.montant_ht, mouvement.montant_ht_rem))}
									<br>
									Remise HT :<br>${bigDecimalUtil.formatNumberZeroBd(mouvement.montant_ht_rem)}
									<br>
									Remise TTC :<br>${bigDecimalUtil.formatNumberZeroBd(mouvement.montant_ttc_rem)}
									<br>
									TTC avant rem :<br>${bigDecimalUtil.formatNumberZeroBd(bigDecimalUtil.add(mouvement.montant_ttc, mouvement.montant_ttc_rem))}
								"
							></i>
						</c:if>
					</cplx:td>
					<%} %>
					
					<cplx:td align="right" style="font-weight:bold;" value="${mouvement.montant_ttc }" />
					<cplx:td align="center">
			
					<c:if test="${mouvement.origine_id == null}">		
						<work:delete-link />
					</c:if>	
					
					<div class="btn-group">
                         <a class="btn btn-sm btn-palegreen dropdown-toggle shiny" data-toggle="dropdown" href="javascript:void(0);" aria-expanded="false"><i class="fa fa-angle-down"></i></a>
                         <ul class="dropdown-menu dropdown-primary">
                         		<li>
                         			<std:link actionGroup="C" value="Dupliquer" classStyle="" action="stock.mouvement.dupliquer" workId="${mouvement.id}" params="skipI=1&skipP=1" icon="fa-copy" tooltip="Dupliquer ce mouvement"/>
                         		</li>
                         		<% if(TYPE_MOUVEMENT_ENUM.a.toString().equals(typeMouvement)){%>
									<li>
										<std:link actionGroup="S" classStyle="" style="background-color: #dedede;" action="stock-caisse.mouvementStock.init_retour_vente" workId="${mouvement.id}" params="src=mvm" value="Déclarer retour" icon="fa-mail-reply-all" tooltip="Déclarer un retour" />
									</li>
									<li>
										<std:linkPopup actionGroup="S" classStyle="" style="color: ${mouvement.opc_travaux==null?'blue':'red' };" action="stock.mouvement.manage_travaux" workId="${mouvement.id}" params="src=mvm&trv=${mouvement.opc_travaux.id }" value="Associer aux travaux" icon="fa-mail-reply-all" tooltip="Associer aux travaux" />
									</li>		
								<%} else if(TYPE_MOUVEMENT_ENUM.v.toString().equals(typeMouvement)){%> 
									<li>
										<std:linkPopup actionGroup="S" classStyle="btn btn btn-sm btn-default" style="background-color: #dedede;text-align:left;" action="stock-caisse.mouvementStock.init_pdf_facture" workId="${mouvement.id}" params="src=mvm" value="Facture" icon="fa fa-file-pdf-o" tooltip="Télécharger la facture" />
									</li>	
								<%} else if(TYPE_MOUVEMENT_ENUM.t.toString().equals(typeMouvement)){%> 
									<li>
										<std:link actionGroup="X" classStyle="btn btn btn-sm btn-default" style="background-color: #dedede;" target="downloadframe" action="stock.mouvement.editPdfTransfert"  workId="${mouvement.id}" value="Fiche transfert" icon="fa fa-download" tooltip="Télécharger la fiche transfert" />
									</li>	
								<%} else if(TYPE_MOUVEMENT_ENUM.cm.toString().equals(typeMouvement)){%>
									<li>
										<c:if test="${!mouvement.is_valide}">										
											<std:link actionGroup="X" classStyle="btn btn btn-sm btn-default" style="background-color: #dedede;" action="stock.mouvement.validerBonCommande"  workId="${mouvement.id}" icon="fa fa-download" value="Valider" tooltip="Valider" />
										</c:if>
										<c:if test="${mouvement.is_valide}">
											<std:link actionGroup="X" classStyle="btn btn btn-sm btn-default" style="background-color: #dedede;" action="stock.mouvement.genererAchatFromCmd"  workId="${mouvement.id}" icon="fa fa-download" value="Transformer en achat" tooltip="Pointer et transformer en achat" />
										</c:if>	
									</li>	
									<li>
										<std:link actionGroup="X" classStyle="btn btn btn-sm btn-default" style="background-color: #dedede;" target="downloadframe" action="stock.mouvement.editPdfBonCommande"  workId="${mouvement.id}" icon="fa fa-download" value="Télécharger" tooltip="Télécharger le bon de comamnde" />
									</li>	
								<%} else if(TYPE_MOUVEMENT_ENUM.av.toString().equals(typeMouvement)){%> 
									<c:if test="${!mouvement.is_valide}">	
										<li>
											<std:link actionGroup="X" classStyle="btn btn btn-sm btn-default" style="background-color: #dedede;" action="stock.mouvement.validerAvoir"  workId="${mouvement.id}" icon="fa fa-download" value="Générer la vente" tooltip="Valider et transformer en vente" />
										</li>	
									</c:if>
									<li>
										<std:link actionGroup="X" classStyle="btn btn btn-sm btn-default" style="background-color: #dedede;" target="downloadframe" action="stock.mouvement.editPdfAvoir"  workId="${mouvement.id}" icon="fa fa-download" value="Télécharger" tooltip="Télécharger l'avoir" />
									</li>
								<%} %>							
						 	</ul>
						 </div>
					</cplx:td>
				</cplx:tr>
				
				<tr style="display: none;" id="tr_det_${mouvement.id}" class="sub">
					<td colspan="<%=nbrCell %>" noresize="true" style="background-color: #fff4d3;cursor: default;" align="center" id="tr_consult_${mouvement.id}">
						
					</td>
				</tr>
		</c:forEach>	
			<!-- Total -->
			<c:if test="${list_mouvement.size() > 0 }">
				<%if(TYPE_MOUVEMENT_ENUM.a.toString().equals(typeMouvement) || TYPE_MOUVEMENT_ENUM.v.toString().equals(typeMouvement)){ %>
				<tr class="sub">
					<td colspan="<%=nbrCell-(TYPE_MOUVEMENT_ENUM.a.toString().equals(typeMouvement)?5:4) %>"></td>
					<td align="right">
						<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
							<fmt:formatDecimal value="${totalHt }"/>
						</span>
					</td>
					<td align="right">
						<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
							<fmt:formatDecimal value="${totalTva }"/>
						</span>
					</td>
					
					<%if(TYPE_MOUVEMENT_ENUM.a.toString().equals(typeMouvement)){%>
					<td align="right">
						<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
							<fmt:formatDecimal value="${totalRemise }"/>
						</span>
					</td>
					<%} %>
					<td align="right">
						<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
							<fmt:formatDecimal value="${totalTtc }"/>
						</span>
					</td>
					<td></td>
				</tr>
			<%} %>
			</c:if>
		</cplx:body>
	</cplx:table>
 </std:form>	

 </div>
					<!-- end widget content -->

				</div>
				<!-- end widget div -->
				
				
<script type="text/javascript">
/*Handles ToolTips*/
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script>				