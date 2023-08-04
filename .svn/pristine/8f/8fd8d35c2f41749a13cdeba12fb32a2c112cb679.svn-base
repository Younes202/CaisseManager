<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@page errorPage="/commun/error.jsp" %>


 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion de stock</li>
         <li>Fiche des fournisseurs</li>
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
              <%request.setAttribute("comp_tab", "fourn"); %>
              <jsp:include page="composant_tab_include.jsp" />
              
<div class="tab-content">
<std:form name="search-form">
	<!-- Liste des fournisseurs -->
	<cplx:table name="list_fournisseur" transitionType="simple" titleKey="fournisseur.list" filtrable="false" sortable="false" initAction="stock.composant.find_mouvement" checkable="false" autoHeight="true" paginate="false">
		<cplx:header>
			<cplx:th type="string" value="Code" field="mouvementArticle.opc_mouvement.opc_fournisseur.code" />
			<cplx:th type="string" value="Nom" field="mouvementArticle.opc_mouvement.opc_fournisseur.libelle" filterOnly="true" />
			<cplx:th type="string" valueKey="fournisseur.marque" field="mouvementArticle.opc_mouvement.opc_fournisseur.marque"/>
			<cplx:th type="string" valueKey="fournisseur.adresse" field="mouvementArticle.opc_mouvement.opc_fournisseur.adresse" />
			<cplx:th type="string" valueKey="fournisseur.telephone" field="mouvementArticle.opc_mouvement.opc_fournisseur.telephone" width="150"/>
			<cplx:th type="string" valueKey="fournisseur.portable" field="mouvementArticle.opc_mouvement.opc_fournisseur.portable" width="150"/>
			<cplx:th type="string" valueKey="fournisseur.mail" field="mouvementArticle.opc_mouvement.opc_fournisseur.mail" />
		</cplx:header>
		<cplx:body>
			<c:set var="oldFourn" value="${null }"></c:set>
			<c:forEach items="${list_fournisseur }" var="mouvementArticle">
				<c:if test="${empty oldFourn or mouvementArticle.opc_mouvement.opc_fournisseur.code != oldFourn}">
				     <tr>
						<td noresize="true" class="separator-group" style="padding-left: 10px;">
							<span class="fa fa-fw fa-folder-open-o separator-icon"></span>  
							${mouvementArticle.opc_mouvement.opc_fournisseur.code}-${mouvementArticle.opc_mouvement.opc_fournisseur.libelle}
						</td>
						<td noresize="true">${mouvementArticle.opc_mouvement.opc_fournisseur.marque}</td>
						<td noresize="true">${mouvementArticle.opc_mouvement.opc_fournisseur.getAdressFactureFull()}</td>
						<td noresize="true">${mouvementArticle.opc_mouvement.opc_fournisseur.telephone}</td>
						<td noresize="true">${mouvementArticle.opc_mouvement.opc_fournisseur.portable}</td>
						<td noresize="true">${mouvementArticle.opc_mouvement.opc_fournisseur.mail}</td>
					</tr>
				</c:if>		
				<c:set var="oldFourn" value="${mouvementArticle.opc_mouvement.opc_fournisseur.code }"></c:set>
				
				<cplx:tr workId="${mouvementArticle.opc_mouvement.opc_fournisseur.id }">
					<td colspan="4" style="padding-left: 100px;">Mouvement : ${mouvementArticle.opc_mouvement.num_facture} ${mouvementArticle.opc_mouvement.num_bl} ${mouvementArticle.opc_mouvement.num_recu}</td>
					<cplx:td align="right" style="font-weight:bold;"><fmt:formatDecimal value="${mouvementArticle.prix_ttc}"/></cplx:td>
					<cplx:td align="center" style="color:blue;"><fmt:formatDate value="${mouvementArticle.opc_mouvement.date_mouvement}"/></cplx:td>
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