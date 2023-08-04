<%@page import="appli.model.domaine.administration.service.IClientService"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
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
         <li>Fiche des fournisseurs</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:link actionGroup="C" classStyle="btn btn-default" action="stock.fournisseur.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
          |
          <std:linkPopup noJsValidate="true" actionGroup="C" style='margin-left: 10px;' action="admin.dataForm.work_find" icon="fa fa-cogs" tooltip="Données formulaire" value="Formulaire" params="tp=FOURNISSEUR" />
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
	<!-- Liste des fournisseurs -->
	<cplx:table name="list_fournisseur" showDataState="true" checkable="false" transitionType="simple" width="100%" titleKey="fournisseur.list" initAction="stock.fournisseur.work_find" autoHeight="true">
		<cplx:header>
			<cplx:th type="empty" />
			<cplx:th type="string" valueKey="fournisseur.code" field="fournisseur.code" width="100"/>
			<cplx:th type="string" valueKey="fournisseur.libelle" field="fournisseur.libelle"/>
			<cplx:th type="string" valueKey="fournisseur.marque" field="fournisseur.marque"/>
			<cplx:th type="string" value="Adresse" field="fournisseur.adresseFull" filtrable="false" sortable="false"/>
			<cplx:th type="string" valueKey="fournisseur.telephone" field="fournisseur.telephone" width="100"/>
			<cplx:th type="string" valueKey="fournisseur.portable" field="fournisseur.portable" width="100"/>
			<cplx:th type="string" valueKey="fournisseur.mail" field="fournisseur.mail"/>
			
			<c:forEach items="${listDataValueForm }" var="data">
				<cplx:th type="string" value="${data.opc_data_form.data_label }" filtrable="false" sortable="false" />
			</c:forEach>
			
			<cplx:th type="empty" />
			<cplx:th type="empty" />
		</cplx:header>
		<cplx:body>
			<c:set var="clientService" value="<%=ServiceUtil.getBusinessBean(IClientService.class) %>" />
		
			<c:forEach items="${list_fournisseur }" var="fournisseur">
				<c:set var="listDataVal" value="${clientService.loadDataForm(fournisseur.id, 'FOURNISSEUR') }" />
			
		<c:if test="${fournisseur.familleStr.size()!=0 }">
			<c:forEach var="i" begin="0" end="${fournisseur.familleStr.size()-1}">
				<c:if test="${empty oldfam or i>(oldfam.size()-1) or fournisseur.familleStr.get(i).code != oldfam.get(i).code}">
			    	 <tr>
						<td colspan="${listDataValueForm.size()+11 }" noresize="true" class="separator-group" style="padding-left: ${fournisseur.familleStr.get(i).level<=1?0:fournisseur.familleStr.get(i).level*10}px;">
							<span class="fa fa-fw fa-folder-open-o separator-icon"></span> ${fournisseur.familleStr.get(i).code}-${fournisseur.familleStr.get(i).libelle}
						</td>
					</tr>
				</c:if>		
			</c:forEach>
		</c:if>
			
			<c:set var="oldfam" value="${fournisseur.familleStr }"></c:set>
				<cplx:tr workId="${fournisseur.id }" style="${fournisseur.is_disable?'text-decoration: line-through;':'' }">
					<cplx:td>
						<work:edit-link />
					</cplx:td>
					<cplx:td value="${fournisseur.code}"></cplx:td>
					<cplx:td value="${fournisseur.libelle}"></cplx:td>
					<cplx:td value="${fournisseur.marque}"></cplx:td>
					<cplx:td value="${fournisseur.getAdressFull()}"></cplx:td>
					<cplx:td value="${fournisseur.telephone}"></cplx:td>
					<cplx:td value="${fournisseur.portable}"></cplx:td>
					<cplx:td value="${fournisseur.mail}"></cplx:td>
					
					<c:forEach items="${listDataValueForm }" var="dataV">		
						<c:forEach items="${listDataVal }" var="data">
							<c:if test="${dataV.opc_data_form.id==data.opc_data_form.id }">
								<c:set var="currDV" value="${data.data_value }" />
								<c:set var="currAlign" value="${(data.opc_data_form.data_type=='LONG' or data.opc_data_form.data_type=='DECIMAL') ? 'right':'center' }" />
							</c:if>
						</c:forEach>
						<cplx:td align="${currAlign }" value="${currDV }" />
					</c:forEach>
					
					<cplx:td align="center">
					 	<std:link action="stock.fournisseur.desactiver" workId="${fournisseur.id }" actionGroup="C" icon="fa ${fournisseur.is_disable?'fa-unlock':'fa-lock' }" classStyle="btn btn-sm btn-${fournisseur.is_disable?'success':'warning'}" tooltip="${fournisseur.is_disable?'Activer':'D&eacute;sactiver'}" />
					</cplx:td>
					<cplx:td align="center">
						<work:delete-link />
					</cplx:td>
				</cplx:tr>
			</c:forEach>
		</cplx:body>
	</cplx:table>
 </std:form>
 </div>
					<!-- end widget content -->

				</div>
				<!-- end widget div -->