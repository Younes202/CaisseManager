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
         <li>Caisse</li>
         <li>Liste de choix</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
      	 <c:if test="${empty isEditable or isEditable }">
           <std:linkPopup actionGroup="C" classStyle="btn btn-default" action="caisse.listChoix.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
        </c:if>   
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

	<span style="font-size: 10px;color:orange;">
	Si le libell&eacute; d'un &eacute;l&eacute;ment commance par <b style="color: red;">#</b> alors il ne s'affichera pas dans le ticket de caisse			
    </span>              
	
	<!-- Liste des listChoixs -->
	<cplx:table name="list_listChoix" transitionType="simple" width="100%" title="Liste de choix" initAction="caisse.listChoix.work_find" autoHeight="true" checkable="false">
		<cplx:header>
			<cplx:th type="empty" />
			<cplx:th type="string" value="Code" field="listChoix.code" width="150"/>
			<cplx:th type="string" value="libell&eacute;" field="listChoix.libelle"/>
			<cplx:th type="decimal" value="Nombre d'articles" width="120" filtrable="false" sortable="false"/>
			<cplx:th type="empty" width="90"/>
		</cplx:header>
		<cplx:body>
		<c:forEach items="${list_listChoix }" var="listChoix">
			<c:set var="stl" value="${listChoix.is_disable ? 'text-decoration: line-through;color:gray;':'' }" />
		
			<cplx:tr workId="${listChoix.id }">
					<cplx:td>
						<work:edit-link-popup/>
					</cplx:td>
					<cplx:td style="${stl }" value="${listChoix.code}"></cplx:td>
	                <cplx:td style="${stl }" value="${listChoix.libelle}"></cplx:td>
					<cplx:td align="right" style="font-weight:bold;" value="${listChoix.list_choix_detail.size()}"></cplx:td>
					<cplx:td align="center">
					 	<std:link action="caisse.listChoix.desactiver" workId="${listChoix.id }" actionGroup="C" icon="fa ${listChoix.is_disable?'fa-unlock':'fa-lock' }" classStyle="btn btn-sm btn-${listChoix.is_disable?'success':'warning'}" tooltip="${listChoix.is_disable?'Activer':'D&eacute;sactiver'}" />
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
