<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.util.Date"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<script type="text/javascript">
	$(document).ready(function (){
		$(document).off('click', "a[id^='lnk_det']");
		$(document).on('click', "a[id^='lnk_det']", function(){
			setTrContent($(this).attr("curr"), "<%=EncryptionUtil.encrypt("stock.mouvement.editTrMvm")%>");
		});
		$("#refreshTva").off("click").on("click", function(){
			submitAjaxForm('<%=EncryptionUtil.encrypt("dash.dashBoard2.etat_tva")%>', null, $("#search-form"), $(this));
		});
		
		$('.input-group.date, #dateDebut, #dateFin').datepicker({
	    	todayBtn: true,
	    	clearBtn: true,
		    language: "fr",
		    autoclose: true,
		    todayHighlight: true
	    });
	});
</script>

<<style>
/* .border_bg{ */
/*     border-right: 2px solid #595656; */
/*     border-bottom: 2px solid #595656; */
/* } */
/* .back_row{ */
/*     background-color: #b5aeae */
}

</style>
<std:form name="search-form">
 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Finance</li>
         <li>Etat de TVA</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
  		<span style="font-size: 22px;float: left;">P&eacute;riode du </span>
	  	<div class="input-group date" style="width: 190px;float: left;">
	  		<input type="text" class="form-control" name="dateDebut" id="dateDebut" style="font-size: 22px;color:green !important;font-weight: bold;border: 0px;" value="<%=StringUtil.getValueOrEmpty(DateUtil.dateToString((Date)request.getAttribute("dateDebut")))%>">
	  			<span class="input-group-addon" style="border: 1px solid #f3f3f3;">
	  				<i class="fa fa-calendar" style="font-size: 22px;color: #9C27B0;"></i>
	  			</span>
		</div>
		<span style="font-size: 22px;float: left;">au </span>
	  	<div class="input-group date" style="width: 190px;float: left;">
	  		<input type="text" class="form-control" name="dateFin" id="dateFin" style="font-size: 22px;color:green !important;font-weight: bold;border: 0px;"  value="<%=StringUtil.getValueOrEmpty(DateUtil.dateToString((Date)request.getAttribute("dateFin")))%>">
	  			<span class="input-group-addon" style="border: 1px solid #f3f3f3;">
	  				<i class="fa fa-calendar" style="font-size: 22px;color: #9C27B0;""></i>
	  			</span>
		</div>
			<a id="refreshTva" href="javascript:" title="Actualiser la TVA">
		     <i class="fa fa-refresh" style="font-size: 22px;margin-top: 8px;margin-left: 5px;"></i>
		 </a>
  
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


<%
	BigDecimal tauxVente = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig(ContextAppli.PARAM_APPLI_ENUM.TVA_VENTE.toString()));
BigDecimal mttTvaARecupererHT = (BigDecimal)request.getAttribute("mttVenteARecupererHT");
BigDecimal mttTvaARecupererTTC = (BigDecimal)request.getAttribute("mttVenteARecupererTTC");
BigDecimal mttTvaADonnerHT = (BigDecimal)request.getAttribute("mttVenteADonnerHT");
BigDecimal mttTvaADonnerTTC = (BigDecimal)request.getAttribute("mttVenteADonnerTTC");

String devise = StrimUtil.getGlobalConfigPropertie("devise.html");

BigDecimal solde = BigDecimalUtil.substract(BigDecimalUtil.substract(mttTvaADonnerTTC, mttTvaADonnerHT), BigDecimalUtil.substract(mttTvaARecupererTTC,mttTvaARecupererHT));
%>
	<!-- row -->
	<div class="row">
 	<div class="widget-body" style="margin-bottom: -38px;">

            <div class="row" style="margin-bottom: 8px;">
				<div class="col-md-4">
	 			<span>Taux TVA vente :</span>
	 			<span style="height: 20px;font-weight: bold;" class="badge badge-blue">
	 				<%=tauxVente %>%
	 			</span>	
 			</div>
			</div>
            <div class="col-lg-8 col-sm-12 col-xs-12">
					<div class="row">
						<div class="col=lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="row">
								<div class="col-sm-6">Ventes TTC :</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<span style="font-size: 12px !important; font-weight: bold;" class="badge badge-orange"> <%=BigDecimalUtil.formatNumberZero(mttTvaADonnerTTC)%> <%=devise%>
									</span>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">Ventes HT :</div>
							</div>

							<div class="row">
							<div class="col-sm-6">
								<span style="font-size: 12px !important; font-weight: bold;" class="badge badge-orange"> <%=BigDecimalUtil.formatNumberZero(mttTvaADonnerHT)%> <%=devise%>
								</span>
							</div>
							</div>

							<div class="row">
								<div class="col-sm-6">Montant TVA :</div>
							</div>

							<div class="row">
							<div class="col-sm-6">
								<span style="font-size: 14px !important; font-weight: bold;" class="badge badge-orange"> <%=BigDecimalUtil.formatNumberZero(BigDecimalUtil.substract(mttTvaADonnerTTC, mttTvaADonnerHT))%> <%=devise%>
								</span>
							</div>
							</div>
						</div>
					
						
						<div class="col=lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="row">
								<div class="col-sm-6">Achats TTC :</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<span style="font-size: 12px !important; font-weight: bold;" class="badge badge-green"> <%=BigDecimalUtil.formatNumberZero(mttTvaARecupererTTC)%> <%=devise%>
									</span>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">Achats HT :</div>
							</div>

							<div class="row">
								<div class="col-sm-6">
									<span style="font-size: 12px !important; font-weight: bold;" class="badge badge-green"> <%=BigDecimalUtil.formatNumberZero(mttTvaARecupererHT)%> <%=devise%>
									</span>
								</div>
							</div>

							<div class="row">
								<div class="col-sm-6">Montant TVA :</div>
							</div>

							<div class="row">
								<div class="col-sm-6">
									<span style="font-size: 14px !important; font-weight: bold;" class="badge badge-green"> <%=BigDecimalUtil.formatNumberZero(BigDecimalUtil.substract(mttTvaARecupererTTC, mttTvaARecupererHT))%> <%=devise%>
									</span>
								</div>
							</div>
						</div>
						
						<div class="col=lg-4 col-md-4 col-sm-12 col-xs-12">

							<div class="row">

								<div class="col-md-9">
									<span style="font-weight: bold; font-size: 20px;"> Solde TVA <%=solde.compareTo(BigDecimalUtil.ZERO) >= 0 ? "&agrave; donner : " : "&agrave; r&eacute;cup&eacute;rer : "%>
									</span>
								</div>
							</div>

							<div class="row">
								<div class="col-sm-6">
									<span style="font-size: 20px !important; font-weight: bold; height: 28px; margin-top:2px;margin-left:45px;" class="badge badge-<%=solde.compareTo(BigDecimalUtil.ZERO) >= 0 ? "orange" : "green"%>"> <%=BigDecimalUtil.formatNumberZero(solde.abs())%> <%=devise%>
									</span>
								</div>
							</div>
						</div>
						
					
					</div>
				</div>
                     

<!-- 				<div class="row"> -->
<!--  			<div class="col-md-4"> -->
<!-- 	 			<span>Taux TVA vente :</span> -->
<!-- 	 			<span style="height: 20px;font-weight: bold;" class="badge badge-blue"> -->
<%-- 	 				<%=tauxVente %>% --%>
<!-- 	 			</span> -->
<!--  			</div> -->
<!--  			<div class="col-md-6"> -->
<!-- 	 			<span style="font-weight: bold;font-size: 20px;"> -->
<%-- 	 			Solde TVA <%=solde.compareTo(BigDecimalUtil.ZERO)>=0 ? "&agrave; donner : ":"&agrave; r&eacute;cup&eacute;rer : " %> --%>
<!-- 	 			</span> -->
<%-- 	 			<span style="font-size: 20px !important;font-weight: bold;height: 28px;margin-top: -5px;" class="badge badge-<%=solde.compareTo(BigDecimalUtil.ZERO)>=0 ? "orange":"green" %>"> --%>
<%-- 					<%=BigDecimalUtil.formatNumberZero(solde.abs()) %> <%=devise %>		 --%>
<!-- 	 			</span> -->
<!--  			</div> -->
<!--  		</div> -->
 		<hr>	
<!-- 		<div class="row"> -->
<%-- 			<std:label classStyle="control-label col-md-2" value="Ventes TTC" /> --%>
<!-- 			<div class="col-md-2" style="margin-top: 2px;"> -->
<!-- 				<span style="font-size: 12px !important;font-weight: bold;" class="badge badge-orange"> -->
<%-- 					<%=BigDecimalUtil.formatNumberZero(mttTvaADonnerTTC)%> <%=devise %> --%>
<!-- 				</span> -->
<!-- 			</div> -->
<%-- 			<std:label classStyle="control-label col-md-2" value="Ventes HT" /> --%>
<!-- 			<div class="col-md-2" style="margin-top: 2px;"> -->
<!-- 				<span style="font-size: 12px !important;font-weight: bold;" class="badge badge-orange"> -->
<%-- 					<%=BigDecimalUtil.formatNumberZero(mttTvaADonnerHT)%> <%=devise %> --%>
<!-- 				</span> -->
<!-- 			</div> -->
<%-- 			<std:label classStyle="control-label col-md-2" value="Montant TVA" style="font-weight:bold;"/> --%>
<!-- 			<div class="col-md-2" style="margin-top: 2px;"> -->
<!-- 				<span style="font-size: 14px !important;font-weight: bold;" class="badge badge-orange"> -->
<%-- 					<%=BigDecimalUtil.formatNumberZero(BigDecimalUtil.substract(mttTvaADonnerTTC,mttTvaADonnerHT))%> <%=devise %> --%>
<!-- 				</span> -->
<!-- 			</div> -->
<!-- 		</div> -->
		
		<div class="row">
<%-- 			<std:label classStyle="control-label col-md-2" value="Achats TTC" /> --%>
<!-- 			<div class="col-md-2" style="margin-top: 2px;"> -->
<!-- 				<span style="font-size: 12px !important;font-weight: bold;" class="badge badge-green"> -->
<%-- 					<%=BigDecimalUtil.formatNumberZero(mttTvaARecupererTTC)%> <%=devise %> --%>
<!-- 				</span> -->
<!-- 			</div> -->
<%-- 			<std:label classStyle="control-label col-md-2" value="Achats HT" /> --%>
<!-- 			<div class="col-md-2" style="margin-top: 2px;"> -->
<!-- 				<span style="font-size: 12px !important;font-weight: bold;" class="badge badge-green"> -->
<%-- 					<%=BigDecimalUtil.formatNumberZero(mttTvaARecupererHT)%> <%=devise %> --%>
<!-- 				</span> -->
<!-- 			</div> -->
<%-- 			<std:label classStyle="control-label col-md-2" value="Montant TVA" style="font-weight:bold;"/> --%>
<!-- 			<div class="col-md-2" style="margin-top: 2px;"> -->
<!-- 				<span style="font-size: 14px !important;font-weight: bold;" class="badge badge-green"> -->
<%-- 					<%=BigDecimalUtil.formatNumberZero(BigDecimalUtil.substract(mttTvaARecupererTTC,mttTvaARecupererHT))%> <%=devise %> --%>
<!-- 				</span> -->
<!-- 			</div> -->
		</div>
 	</div>
 	
 	<hr>
 	
	<!-- Liste des mouvements -->
	<cplx:table name="list_etat_tva" transitionType="simple" width="100%" title="Etats TVA des achats et ventes" initAction="dash.dashBoard2.etat_tva" checkable="false" autoHeight="true">
		<cplx:header>
				<cplx:th type="date" valueKey="mouvement.date_mouvement" field="mvm.date_mouvement" width="150"/>
				<cplx:th type="decimal" value="Montant TTC" field="mvm.montant_ttc" width="160"/>
				<cplx:th type="decimal" value="Montant HT" field="mvm.montant_ht" width="160"/>
				<cplx:th type="decimal" value="Montant TVA" field="mvm.montant_tva" width="160"/>
				<cplx:th type="string" value="Type mouvement" field="mvm.type_mvmnt" groupValues="${typeMvmArray }"/>
		</cplx:header>
		<cplx:body>
			<c:forEach items="${listMvmTva }" var="detail">
				<cplx:tr workId="${detail.id }">
					<cplx:td>
						 <a href="javascript:" id="lnk_det" curr="${detail.id}"><fmt:formatDate value="${detail.date_mouvement}"/></a>
					</cplx:td>
					<cplx:td align="right" value="${detail.montant_ttc}"></cplx:td>
					<cplx:td align="right" value="${detail.montant_ht}"></cplx:td>
					<cplx:td align="right">
						<c:choose>
							<c:when test="${detail.type_mvmnt=='a' }">
								<span style="color:green;">+<fmt:formatDecimal value="${detail.montant_tva}"/></span>
							</c:when>
							<c:otherwise>
								<span style="color:red;">-<fmt:formatDecimal value="${detail.montant_tva}"/></span>
							</c:otherwise>	
						</c:choose>
					</cplx:td>
					<cplx:td value="${detail.type_mvmnt}" style="padding-left: 15px;"></cplx:td>
				</cplx:tr>
				<tr style="display: none;" id="tr_det_${detail.id}" class="sub">
					<td colspan="6" noresize="true" style="background-color: #fff4d3;cursor: default;" align="center" id="tr_consult_${detail.id}">
						
					</td>
				</tr>
			</c:forEach>
		</cplx:body>
	</cplx:table>
	<br>
	<br>
	<!-- Liste des chargeDivers -->
		<cplx:table name="list_chargeDivers" transitionType="simple" width="100%" title="Recettes et d&eacute;penses" initAction="stock.chargeDivers.work_find">
			<cplx:header>
				<cplx:th type="empty" />
				<cplx:th type="string" valueKey="chargeDivers.libelle" field="chargeDivers.libelle" width="150"/>
				<cplx:th type="string" valueKey="chargeDivers.num_bl" field="chargeDivers.num_bl" width="150"/>
				<cplx:th type="date" valueKey="chargeDivers.date_mouvement" field="chargeDivers.date_mouvement" width="150"/>
				<cplx:th type="string" value="Type" field="chargeDivers.opc_famille_consommation.code" width="150"/>
				<cplx:th type="string" valueKey="chargeDivers.opc_fournisseur" field="chargeDivers.opc_fournisseur.libelle" width="150"/>
				<cplx:th type="string" valueKey="chargeDivers.opc_financement_enum" field="chargeDivers.opc_financement_enum.libelle" sortable="false" filtrable="false" width="100"/>
				<cplx:th type="decimal" valueKey="chargeDivers.montant" field="chargeDivers.montant" width="100"/>
			</cplx:header>
			<cplx:body>
				<c:forEach items="${listCharge }" var="chargeDivers">
					<cplx:tr workId="${chargeDivers.id }">
						<cplx:td>
							<work:edit-link />
						</cplx:td>
						<cplx:td value="${chargeDivers.libelle}"></cplx:td>
						<cplx:td value="${chargeDivers.num_bl}">
							<c:if test="${chargeDivers.is_automatique}">
								<i class="fa fa-clock-o" style="color: green;"></i>
							</c:if>	
						</cplx:td>
						<cplx:td value="${chargeDivers.date_mouvement}"></cplx:td>
						<cplx:td value="${chargeDivers.opc_famille_consommation.code}-${chargeDivers.opc_famille_consommation.libelle}"></cplx:td>
						<cplx:td value="${chargeDivers.opc_fournisseur.libelle}"></cplx:td>
						<cplx:td value="${chargeDivers.getPaiementsStr()}"></cplx:td>
						<cplx:td align="right" style="color:${chargeDivers.sens=='D'?'red':'green'};">
									 ${chargeDivers.sens=='D'?'-':'+'}<fmt:formatDecimal value="${chargeDivers.montant}"/>
						</cplx:td>
					</cplx:tr>
				</c:forEach>
			</cplx:body>
		</cplx:table>	
 	</div>		
 	
 </div>	
	 </std:form>			<!-- end widget div -->