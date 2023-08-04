<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.util.FileUtil"%>
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
         <li>Fiche des marques</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:link actionGroup="C" classStyle="btn btn-default" action="stock.marque.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
          |
          <std:linkPopup noJsValidate="true" actionGroup="C" style='margin-left: 10px;' action="admin.dataForm.work_find" icon="fa fa-cogs" tooltip="Données formulaire" value="Formulaire" params="tp=MARQUE" />
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
	
	<c:set var="encryptionUtil" value="<%=new EncryptionUtil() %>" />
	
<std:form name="search-form">
	<!-- Liste des marques -->
	<cplx:table name="list_marque" transitionType="simple" checkable="false" width="100%" title="List des marques" initAction="stock.marque.work_find" autoHeight="true">
		<cplx:header>
			<cplx:th type="empty" />
			<cplx:th type="string" value="Code" field="marque.code" width="100"/>
			<cplx:th type="string" value="Libellé" field="marque.libelle"/>
			<cplx:th type="string" value="Logo" field="marque.logo" filtrable="false" sortable="false"/>
			<cplx:th type="string" value="Description" field="marque.description" filtrable="false" sortable="false"/>
			<cplx:th type="empty" width="120" />
		</cplx:header>
		<cplx:body>
			<c:forEach items="${list_marque }" var="marque">
				<cplx:tr workId="${marque.id }" style="${marque.is_disable?'text-decoration: line-through;':'' }">
					<cplx:td>
						<work:edit-link />
					</cplx:td>
					<cplx:td value="${marque.code}"></cplx:td>
					<cplx:td value="${marque.libelle}"></cplx:td>
					<cplx:td>
						<img alt="" src='resourcesCtrl?elmnt=${encryptionUtil.encrypt(marque.getId().toString())}&path=marque&rdm=${marque.date_maj.getTime()}' height='24' onerror="this.onerror=null;this.remove();"/>
					</cplx:td>
					<cplx:td value="${marque.description}"></cplx:td>
					<cplx:td align="center">
					 	<std:link action="stock.marque.desactiver" workId="${marque.id }" actionGroup="C" icon="fa ${marque.is_disable?'fa-unlock':'fa-lock' }" classStyle="btn btn-sm btn-${marque.is_disable?'success':'warning'}" tooltip="${marque.is_disable?'Activer':'D&eacute;sactiver'}" />
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