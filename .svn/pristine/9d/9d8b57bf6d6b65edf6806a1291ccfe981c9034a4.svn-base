<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<script type="text/javascript">
	$(document).ready(function (){
		$(document).off('click', "a[id^='lnk_det']");
		$(document).on('click', "a[id^='lnk_det']", function(){
			setTrContent($(this).attr("curr"), "<%=EncryptionUtil.encrypt("stock.fournisseurCheque.editTrMvm")%>");
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
         <li>Ch&egrave;ques fournisseur</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:linkPopup actionGroup="C" classStyle="btn btn-default" action="stock.fournisseurCheque.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
      </div>
      <!--Header Buttons-->
      <jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
      <!--Header Buttons End-->
  </div>
  <!-- /Page Header -->

<c:set var="dateUtil" value="<%=new DateUtil()%>" />
<c:set var="bigDecimalUtil" value="<%=new BigDecimalUtil()%>" />

<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
	<!-- row -->
	<div class="row">
	
<std:form name="search-form">

	<div class="row" style="margin-bottom: 10px;">
		<std:label classStyle="control-label col-md-2" value="Statut" />
		<div class="col-md-4">
			<std:select name="statutCheque" type="string" data="${listStatut }" value="${currStatut }" onChange="$('#refreshrow_list_fournisseurCheque').trigger('click');" width="100%" />
		</div>
	</div>

	<!-- Liste des fournisseurs -->
	<cplx:table name="list_fournisseurCheque" transitionType="simple" width="100%" title="Ch&egrave;ques fournisseur" initAction="stock.fournisseurCheque.work_find" autoHeight="true" checkable="false">
		<cplx:header>
			<cplx:th type="empty" />
			<cplx:th type="string" value="Num&eacute;ro ch&egrave;que" field="fournisseurCheque.num_cheque" width="130"/>
			<cplx:th type="long[]" value="Fournisseur" field="fournisseurCheque.opc_fournisseur.id" groupKey="id" groupLabel="libelle" groupValues="${listeFournisseur }" filterOnly="true"/>
			<cplx:th type="decimal" field="fournisseurCheque.montant" value="Montant" width="100"/>
			<cplx:th type="string" value="Statut" sortable="false" filtrable="false"/>
			<cplx:th type="empty" width="160"/>
			<cplx:th type="empty" />
		</cplx:header>
		<cplx:body>
			<c:set var="oldfourn" value="${null }"></c:set>
			<c:forEach items="${list_fournisseurCheque }" var="fournisseurCheque">
			
				<c:if test="${empty oldfourn or oldfourn != fournisseurCheque.opc_fournisseur.libelle}">
				     <tr>
						<td colspan="6" noresize="true" class="separator-group">
							<span class="fa fa-fw fa-folder-open-o separator-icon"></span> ${fournisseurCheque.opc_fournisseur.code }-${fournisseurCheque.opc_fournisseur.libelle }
						</td>
					</tr>
				</c:if>		
				
				<c:set var="oldfourn" value="${fournisseurCheque.opc_fournisseur.libelle }"></c:set>
			
				<cplx:tr workId="${fournisseurCheque.id }">
					<cplx:td>
						<work:edit-link-popup />
					</cplx:td>
					<cplx:td style="${fournisseurCheque.date_annulation != null ? 'text-decoration: line-through;color: red;':'' }" value="${fournisseurCheque.num_cheque}"></cplx:td>
					<cplx:td align="right" style="font-weight:bold;" value="${fournisseurCheque.montant }" />
					<cplx:td>
						<c:choose>
							<c:when test="${fournisseurCheque.elementId != null}">	
								<span style="color: green;margin-left: 15px;">Utilis&eacute;</span> 
								[${fournisseurCheque.source }]
								<a href="javascript:" id="lnk_det" curr="${fournisseurCheque.id }" style="font-size: 11px;"> (Voir d&eacute;tail)</a>
							</c:when>
							<c:when test="${fournisseurCheque.date_annulation != null }">
								<span style="color:red;margin-left: 15px;">Annul&eacute;</span>
								<img class="tooltip-blue" data-toggle="tooltip" data-placement="buttom" data-original-title="Date annulation : ${dateUtil.dateToString(fournisseurCheque.date_annulation)}" src="resources/framework/img/exclamation.png" style="vertical-align: bottom;"/>
							</c:when>
							<c:otherwise>
								<span style="color:gray;margin-left: 15px;">Non utilis&eacute;</span>
							</c:otherwise>
						</c:choose>
						
						<c:if test="${fournisseurCheque.date_encaissement != null }">
							<span style="color:green;">Encaiss&eacute;</span>
							<img class="tooltip-blue" data-toggle="tooltip" data-placement="buttom" data-original-title="Encaiss&eacute; le : ${dateUtil.dateToString(fournisseurCheque.date_encaissement)}<br>Montant : ${bigDecimalUtil.formatNumberZeroBd(fournisseurCheque.montant)}" src="resources/framework/img/exclamation.png" style="vertical-align: bottom;"/>
						</c:if>		
					</cplx:td>
					<cplx:td align="center">
					    <c:if test="${fournisseurCheque.elementId == null }">
						    <std:link action="stock.fournisseurCheque.annulerCheque" style="background-color: #fbe5a7;height: 29px;padding: 4px;" value="${fournisseurCheque.date_annulation == null ? 'Annuler ch&egrave;que' : 'R&eacute;activer' }" actionGroup="C" workId="${fournisseurCheque.id }" />
						</c:if>    
					</cplx:td>
					<cplx:td align="center">
						<c:if test="${fournisseurCheque.elementId == null }"> 
							<work:delete-link />
						</c:if>	
					</cplx:td>
				</cplx:tr>
				<c:if test="${fournisseurCheque.elementId != null }">
					<tr style="display: none;" id="tr_det_${fournisseurCheque.id}" class="sub">
						<td colspan="6" noresize="true" style="background-color: #fff4d3;cursor: default;" align="center" id="tr_consult_${fournisseurCheque.id}">
							
						</td>
					</tr>
				</c:if>
			</c:forEach>
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
					<!-- end widget content -->

				</div>
				<!-- end widget div -->
				