<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion de stock</li>
         <li>Fiche de mouvement
		</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:link classStyle="btn btn-default" action="stock.composant.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
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
        <div class="col-lg-12 col-sm-12 col-xs-12">
              <%request.setAttribute("comp_tab", "mvm"); %>
              <jsp:include page="composant_tab_include.jsp" />
              
<div class="tab-content">  
<std:form name="search-form">	
	<cplx:table name="list_mouvement" transitionType="simple" width="100%" titleKey="mouvement.list" initAction="stock.composant.find_mouvement" checkable="false" autoHeight="true">
		<cplx:header>
			<cplx:th type="string" valueKey="mouvement.type" field="detail.opc_mouvement.type_mvmnt" width="100" groupValues="${typeMouvement }"/>
			<cplx:th type="string" value="BL/Fact" field="detail.opc_mouvement.num_bl" width="100"/>
			<cplx:th type="string" valueKey="mouvement.opc_emplacement" field="detail.opc_mouvement.opc_emplacement.titre"/>
			<cplx:th type="string" valueKey="mouvement.opc_destination" field="detail.opc_mouvement.opc_destination.titre"/>
			<cplx:th type="string" valueKey="mouvement.opc_fournisseur" field="detail.opc_mouvement.opc_fournisseur.libelle"/>
			<cplx:th type="long" valueKey="mouvement.quantite" field="detail.quantite" width="70"/>
			<cplx:th type="decimal" valueKey="mouvement.prix_ht" field="detail.prix_ht" width="100"/>
			<cplx:th type="long" valueKey="mouvement.tva_enum" field="detail.opc_tva_enum.id" width="60"/>
		</cplx:header>
		<cplx:body>
		
		<c:set var="oldDate" value="${null }"></c:set>
			<c:forEach items="${list_mouvement }" var="detail">
				<c:if test="${oldDate == null  or oldDate != detail.opc_mouvement.date_mouvement }">
					<tr>
						<td colspan="9" noresize="true" class="separator-group">
							<span class="fa fa-fw fa-folder-open-o separator-icon"></span> <fmt:formatDate value="${detail.opc_mouvement.date_mouvement}"/>
						</td>
					</tr>	
				</c:if>
				<c:set var="oldDate" value="${detail.opc_mouvement.date_mouvement }"></c:set>
			
				<cplx:tr workId="${detail.id }">
					<cplx:td value="${detail.opc_mouvement.getTypeMvmntLib()}"></cplx:td>
					<cplx:td value="${detail.opc_mouvement.num_bl} ${detail.opc_mouvement.num_facture}"></cplx:td>
					<cplx:td value="${detail.opc_mouvement.opc_emplacement.titre}"></cplx:td>
					<cplx:td value="${detail.opc_mouvement.opc_destination.titre}"></cplx:td>
					<cplx:td value="${detail.opc_mouvement.opc_fournisseur.libelle}"></cplx:td>
					<cplx:td align="right" value="${detail.quantite}"></cplx:td>
					<cplx:td align="right" value="${detail.prix_ht}"></cplx:td>
					<cplx:td align="right" value="${detail.opc_tva_enum.libelle}"></cplx:td>
				</cplx:tr>
			</c:forEach>
		</cplx:body>
	</cplx:table>
 </std:form>	
 	</div>		
			</div>
      </div>

 </div>
					<!-- end widget content -->

				<!-- end widget div -->