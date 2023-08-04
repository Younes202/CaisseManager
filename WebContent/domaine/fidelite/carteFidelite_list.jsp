<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>
	
 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Liste des Cartes</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:link actionGroup="C" classStyle="btn btn-default" action="fidelite.carteFidelite.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
      </div>
      <!--Header Buttons-->
      <jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
      <!--Header Buttons End-->,
  <!-- /Page Header -->
</div>
<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
	
<div class="row">	
<!-- row -->
<std:form name="search-form">
	<!-- Liste des cartes -->
	<cplx:table name="list_carteFidelite" transitionType="simple" width="100%" title="Liste des cartes de fid&eacute;lit&eacute;" initAction="fidelite.carteFidelite.work_find" checkable="false" autoHeight="true">
		<cplx:header>
			<cplx:th type="empty" />
			<cplx:th type="string" value="Libell&eacute;" field="carteFidelite.libelle"/>
			<cplx:th type="long" value="Carte distribu&eacute;es" field="" width="110"/>
			<cplx:th type="long" value="Carte actives" field="" width="110"/>
			<cplx:th type="decimal" value="Montant gagn&eacute;" field="" width="130"/>
			<cplx:th type="decimal" value="Montant consomm&eacute;" field="" width="130"/>
			<cplx:th type="decimal" value="Montant non consomm&eacute;" field="" width="130"/>
			<cplx:th type="boolean" value="Active" field="carteFidelite.is_active" width="110"/>
			<cplx:th type="empty" width="80"/>		
		</cplx:header>
		<cplx:body>
			<c:forEach items="${list_carteFidelite }" var="carteFidelite">
				<c:set var="isActive" value="${empty carteFidelite.is_active or carteFidelite.is_active }"/>
			
				<cplx:tr workId="${carteFidelite.id }" style="${isActive?'':'text-decoration: line-through;' }">
					<cplx:td>
						<work:edit-link/>
					</cplx:td>
					<cplx:td value="${carteFidelite.libelle}"></cplx:td>
					<cplx:td align="right" value="${carteFidelite.list_cartes_client.size() }"></cplx:td>
					<cplx:td align="right" value="${carteFidelite.getNbrCarteActives() }"></cplx:td>
					<cplx:td align="right" value="${carteFidelite.getSoldeGagnes() }"></cplx:td>
					<cplx:td align="right" value="${carteFidelite.getSoldeActuel() }"></cplx:td>
					<cplx:td align="right" style="font-weight:bold;color:green;" value="${carteFidelite.getSoldeGagnes()-carteFidelite.getSoldeActuel() }"></cplx:td>
					<cplx:td align="center" value="${isActive }"></cplx:td>
					<cplx:td align="center">
					 	<std:link action="fidelite.carteFidelite.desactiver" workId="${carteFidelite.id }" actionGroup="C" icon="fa ${isActive?'fa-lock':'fa-unlock' }" classStyle="btn btn-sm btn-${isActive?'warning':'success'}" tooltip="${isActive?'D&eacute;sactiver':'Activer'}" />
						<work:delete-link /> 
					</cplx:td>
				</cplx:tr>
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
 			</div>
