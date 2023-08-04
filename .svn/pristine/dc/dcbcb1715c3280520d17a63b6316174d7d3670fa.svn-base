<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<style>
.lnk-pointe{
    height: 27px;
    padding-top: 4px;
    background-color: #a0d468;
 }
.lnk-non-pointe{
    height: 27px;
    padding-top: 4px;
    background-color: #5db2ff;
 }
</style>

<script type="text/javascript">
	$(document).ready(function (){
		$(document).off('click', "a[id^='lnk_det']");
		$(document).on('click', "a[id^='lnk_det']", function(){
			setTrContent($(this).attr("curr"), "<%=EncryptionUtil.encrypt("admin.compteBancaire.editTrMvmCheque")%>");	
		});
		$("#fournisseur").change(function(){
			submitAjaxForm('<%=EncryptionUtil.encrypt("admin.compteBancaire.gestion_cheque")%>', 'tp=p', $("#search-form"), $(this));
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
         <li>Gestion de ch&egrave;que</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
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
	<div class="widget">
	    <div class="row">
	        <div class="col-lg-12 col-sm-12 col-xs-12">
	              <div class="tabbable">
	                    <ul class="nav nav-tabs" id="myTab">
	                          <li class="<%="nonp".equals(ControllerUtil.getMenuAttribute("tp", request)) ?" active":""%>">
	                              <a data-toggle="tab" href="#descripton" wact="<%=EncryptionUtil.encrypt("admin.compteBancaire.gestion_cheque")%>" params="tp=nonp">
	                               Ch&egrave;que non point&eacute;s
	                              </a>
	                           </li>
	                            <li class="<%="p".equals(ControllerUtil.getMenuAttribute("tp", request)) ?" active":""%>">
	                              <a data-toggle="tab" href="#journeeCaisse" wact="<%=EncryptionUtil.encrypt("admin.compteBancaire.gestion_cheque")%>" params="tp=p">
	                               Ch&egrave;que point&eacute;s
	                              </a>
	                            </li>
	                     </ul>
	                </div>
	          </div>
	      </div>
	      <div class="widget-body">
	<!-- row -->
	<div class="row">
		<std:form name="search-form">
<%
if("nonp".equals(ControllerUtil.getMenuAttribute("tp", request))){
%>		
			<!-- Liste des mouvements -->
			<cplx:table name="list_cheque_nonpointe" transitionType="simple" width="100%" title="Ch&egrave;ques non point&eacute;s" initAction="admin.compteBancaire.gestion_cheque" checkable="false" autoHeight="true">
				<cplx:header>
					<cplx:th type="long[]" value="Fournisseur" field="paiement.opc_fournisseur.id" groupValues="${listFournisseur }" groupKey="id" groupLabel="libelle" width="150" filterOnly="true" />
					<cplx:th type="string" value="Num&eacute;ro ch&egrave;que" field="paiement.num_cheque" width="100" />
					<cplx:th type="date" value="Date mouvement" field="paiement.date_mouvement" width="150" />
					<cplx:th type="string" value="Libell&eacute;" field="paiement.libelle" />
					<cplx:th type="date" value="Date &eacute;ch&eacute;ance" field="paiement.date_echeance" width="150" />
					<cplx:th type="decimal" value="Montant" field="paiement.montant" width="140" />
					<cplx:th type="empty" width="100" />
				</cplx:header>
				<cplx:body>
					<c:set var="oldFourn" value="${null }"></c:set>
					
					<!-- **************************** mouvements ************************ -->
					<c:forEach items="${listDataNonPointe }" var="detail">
						<c:if test="${oldFourn == null  or oldFourn != detail.opc_fournisseur.id }">
						     <tr>
								<td colspan="6" noresize="true" class="separator-group">
									<span class="fa fa-fw fa-folder-open-o separator-icon"></span> ${detail.opc_fournisseur.libelle}
								</td>
							</tr>	
						</c:if>
						<c:set var="oldFourn" value="${detail.opc_fournisseur.id }"></c:set>
						
						<cplx:tr workId="${detail.id }">
							<cplx:td style="padding-left: 21px;">
								<a href="javascript:" id="lnk_det" is_dep=${detail.num_virement=='DEP' ? '1':'0' } curr="${detail.id}" >
									<span class="fa fa-plus" style="color:green;"></span>  ${empty detail.num_cheque ? 'Sans num&eacute;ro' : detail.num_cheque}
								</a>
							</cplx:td>
							<cplx:td style="font-weight:bold" align="center" value="${detail.date_mouvement}"></cplx:td>
							<cplx:td value="${detail.libelle}"></cplx:td>
							<cplx:td align="center" value="${detail.date_echeance}"></cplx:td>
							<cplx:td align="right" style="font-weight:bold;color:#c90000;" value="${detail.montant }"></cplx:td>
							<cplx:td align="center">
								<std:linkPopup id="check_pointe_lnk" classStyle="btn btn-default lnk-pointe" action="admin.compteBancaire.checked_pointe" workId="${detail.id }" value="Pointer" />
							</cplx:td>
						</cplx:tr>
						<tr style="display: none;" id="tr_det_${detail.id}" class="sub">
							<td colspan="6" noresize="true" style="background-color: #fff4d3;" align="center" id="tr_consult_${detail.id}">
								
							</td>
						</tr>
					</c:forEach>
					<c:if test="${not empty listDataNonPointe }">
						<tr>
							<td colspan="6" noresize="true" style="background-color: #fff4d3;">
								<std:label classStyle="control-label col-md-6" value="Total montant non point&eacute" />
								<span style="font-size: 14px !important;font-weight: bold;margin-top: 6px;" class="badge badge-orange"><fmt:formatDecimal value="${ttl_non_pointe }"/></span>
							</td>
						</tr>
					</c:if>
				</cplx:body>
			</cplx:table>
	
	<%} else{ %>
	
	<div class="row">
        <div class="form-group">
        	<std:label classStyle="control-label col-md-2" value="Fournisseur" />
            <div class="col-md-4">
                 <std:select name="fournisseur" style="width:100%;" type="long" required="true" key="id" labels="libelle" data="${listFournisseur }" value="${fournId }" />
            </div>
       </div>
   </div>
	
		<!-- Liste des mouvements -->
	<cplx:table name="list_cheque_pointe" transitionType="simple" width="100%" title="Ch&egrave;ques point&eacute;s" initAction="admin.compteBancaire.gestion_cheque" checkable="false" sortable="false" autoHeight="true" exportable="false" paginate="false">
		<cplx:header>
			<cplx:th type="long[]" value="Fournisseur" field="paiement.opc_fournisseur.id" groupValues="${listFournisseur }" groupKey="id" groupLabel="libelle" width="150" filterOnly="true" />
			<cplx:th type="string" value="Num&eacute;ro ch&egrave;que" field="paiement.num_cheque" width="150" />		
			<cplx:th type="date" value="Date" field="paiement.date_mouvement" width="150" />
			<cplx:th type="string" value="Libell&eacute;" field="paiement.libelle" />
			<cplx:th type="date" value="Date &eacute;ch&eacute;ance" field="paiement.date_echeance" width="150" />
			<cplx:th type="date" value="Date encaissement" field="paiement.date_encaissement" width="150" />
			<cplx:th type="decimal" value="Montant" field="paiement.montant" width="140" />
			<cplx:th type="empty" width="150" />
		</cplx:header>
		<cplx:body>
			<c:set var="oldFourn" value="${null }"></c:set>
			<c:forEach items="${listDataPointe }" var="detail">
				<c:if test="${oldFourn == null  or oldFourn != detail.opc_fournisseur.id }">
				     <tr>
						<td colspan="7" noresize="true" class="separator-group">
							<span class="fa fa-fw fa-folder-open-o separator-icon"></span> ${detail.opc_fournisseur.libelle}
						</td>
					</tr>	
				</c:if>
				<c:set var="oldFourn" value="${detail.opc_fournisseur.id }"></c:set>
				
				<cplx:tr workId="${detail.id }">
					<cplx:td style="padding-left: 21px;">
						<a href="javascript:" id="lnk_det" curr="${detail.id}">
							<span class="fa fa-plus" style="color:green;"></span>${detail.num_cheque }</a>
					</cplx:td>
					<cplx:td align="center" value="${detail.date_mouvement}"></cplx:td>
					<cplx:td align="center" value="${detail.libelle}"></cplx:td>
					<cplx:td align="center" value="${detail.date_echeance}"></cplx:td>
					<cplx:td align="center" value="${detail.date_encaissement}"></cplx:td>
					<cplx:td align="right" style="font-weight:bold;color:#c90000;" value="${detail.montant }"></cplx:td>
					<cplx:td align="center">
						<std:link action="admin.compteBancaire.pointerCheque" classStyle="btn btn-default lnk-non-pointe" workId="${detail.id }" value="Annuler pointage" />
					</cplx:td>
				</cplx:tr>
				<tr style="display: none;" id="tr_det_${detail.id}" class="sub">
					<td colspan="7" noresize="true" style="background-color: #fff4d3;" id="tr_consult_${detail.id}" align="center">
						
					</td>
				</tr>
			</c:forEach>
			<c:if test="${not empty listDataPointe }">
				<tr>
					<td colspan="7" noresize="true" style="background-color: #fff4d3;">
						<std:label classStyle="control-label col-md-6" value="Total montant point&eacute" />
						<span style="font-size: 14px !important;font-weight: bold;margin-top: 6px;" class="badge badge-orange"><fmt:formatDecimal value="${ttl_pointe }"/></span>
					</td>
				</tr>
			</c:if>
		</cplx:body>
	</cplx:table>
<%} %>	
 </std:form>	
 	</div>
 </div>	
</div>
</div>
<!-- end widget div -->