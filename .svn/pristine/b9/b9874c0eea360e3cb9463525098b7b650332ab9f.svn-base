*<%@page import="framework.model.common.util.BooleanUtil"%>
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
			setTrContent($(this).attr("curr"), "<%=EncryptionUtil.encrypt("stock.fournisseur.editTrMvm")%>");
		});
	});
</script>

<%
Boolean isArticleAlerte = (Boolean) request.getAttribute("isArticleAlerte");
%>

<a href="" id="pdf_load_trig" target="downloadframe" style="display:none;"></a>

 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion de stock</li>
         <li class="active">Edition de factures</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
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
        	<std:label classStyle="control-label col-md-2" value="Client" />
            <div class="col-md-4">
                 <std:select name="client" type="long" data="${listClient }" width="90%" multiple="true" key="id" labels="numero;'-';nom" required="true" value="${client }"/>
            </div>
            <std:label classStyle="control-label col-md-2" value="Fournisseur" />
            <div class="col-md-4">
                 <std:select name="fournisseur" type="long" multiple="true" width="90%"  data="${listFournisseur }" key="id" labels="numero;'-';nom" value="${fournisseur }"/>
            </div>
         </div>   
        <div class="form-group">
        	<std:label classStyle="control-label col-md-2" value="Date d&eacute;but" />
            <div class="col-md-2">
                 <std:date name="dateDebut" required="true" value="${dateDebut }"/>
            </div>
            <std:label classStyle="control-label col-md-1" value="Date fin" />
            <div class="col-md-2">
                 <std:date name="dateFin" required="true" value="${dateFin }"/>
            </div>
       </div>
       <div class="form-group">
       		<std:label classStyle="control-label col-md-2" value="Numéro d&eacute;but" />
            <div class="col-md-2">
                 <std:text name="numeroDebut" type="long" maxlength="10" value="${numeroDebut }"/>
            </div>
             <std:label classStyle="control-label col-md-1" value="Numéro fin" />
            <div class="col-md-2">
                 <std:text name="numeroFin" type="long" maxlength="10" value="${numeroFin }"/>
            </div>
            <std:label classStyle="control-label col-md-2" value="Regrouper factures" />
            <div class="col-md-2" style="text-align: center;">
            	<std:checkbox name="is_regroup" />
            </div>
       </div>
       <div class="form-group">
       		<std:button action="stock.fournisseur.facture_edit" style="margin-left: 36%;" params="is_fltr=1" value="Aperçu mouvements" classStyle="btn btn-primary" />
			<std:button action="stock.fournisseur.facture_edit" style="margin-left: 15px;" params="is_fltr=1" value="Editer facture(s)" classStyle="btn btn-primary" />       
       </div>
   </div>
   
	<cplx:table name="list_commande" transitionType="simple" width="100%" forceFilter="true" title="Liste des commandes" initAction="stock.commande.facture_edit" autoHeight="true" checkable="false">
		<cplx:header>
			<cplx:th type="empty" />
			<cplx:th type="string" value="Bon de commande" field="commande.num_bc" width="170" sortable="false"/>
			<cplx:th type="long[]" value="Fournisseur" field="fournisseur.opc_fournisseur.id" groupValues="${listeFournisseur }" groupKey="id" groupLabel="libelle"/>
			<cplx:th type="decimal" value="Montant H.T" field="commande.montant_ht" sortable="false" filtrable="false" width="110"/>
			<cplx:th type="decimal" value="Montant T.T.C" field="commande.montant_ttc" sortable="false" filtrable="false" width="110"/>
			<cplx:th type="empty" width="180px" />
		</cplx:header>
		<cplx:body>	
			<c:set var="oldDate" value="${null }"></c:set>
			<c:forEach items="${list_commande }" var="commande">
				<c:if test="${oldDate == null  or oldDate != commande.date_commande }">
					<tr>
						<td colspan="6" noresize="true" class="separator-group">
							<span class="fa fa-fw fa-folder-open-o separator-icon"></span> <fmt:formatDate value="${commande.date_commande}"/>
						</td>
					</tr>	
				</c:if>
				<c:set var="oldDate" value="${commande.date_commande }"></c:set>
			
				<cplx:tr workId="${commande.id }">
					<cplx:td>
						<work:edit-link/>
					</cplx:td>
					<cplx:td>
						<a href="javascript:" id="lnk_det" curr="${commande.id}">
							<span class="fa fa-plus" style="color:green;"></span>
							${commande.num_bc }
						</a>
					</cplx:td>
					<cplx:td value="${commande.opc_fournisseur.libelle}"></cplx:td>
					<cplx:td align="right" style="font-weight:bold;" value="${commande.montant_ht }" />
					<cplx:td align="right" style="font-weight:bold;" value="${commande.montant_ttc }" />
					<cplx:td align="center">
						<work:delete-link />
						<std:link actionGroup="C" classStyle="btn btn-sm btn-info" action="stock.commande.work_init_duplic" workId="${commande.id }" params="isDuplic=1" icon="fa fa-copy" tooltip="Dupliquer" />
						<std:link actionGroup="C" classStyle="btn btn-sm btn-success" action="stock.commande.transformer_achat" workId="${commande.id }" icon="fa fa-share" tooltip="Transformer en achat" />
						<std:link action="stock.commande.downloadCommandePDF" workId="${commande.id }" tooltip="T&eacute;l&eacute;charger Pdf" classStyle="btn btn-sm btn-default" icon="fa fa-download" target="downloadframe"/>
					</cplx:td>
				</cplx:tr>
				<tr style="display: none;" id="tr_det_${commande.id}" class="sub">
					<td colspan="6" noresize="true" style="background-color: #fff4d3;cursor: default;" align="center" id="tr_consult_${commande.id}">
						
					</td>
				</tr>
		</c:forEach>	
			<!-- Total -->
			<c:if test="${list_commande.size() > 0 }">
				<tr class="sub">
					<td colspan="3"></td>
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