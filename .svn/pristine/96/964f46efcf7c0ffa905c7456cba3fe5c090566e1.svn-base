<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fn" prefix="fn"%>
<%@page errorPage="/commun/error.jsp" %>

<style>
	#conf_cuis_table th{
		border: 1px solid #000000;
	    background-color: #f4b400;
	}
	#conf_cuis_table td{
		border-left: 1px solid #aeaeb1;
		border-right: 1px solid #aeaeb1;
		border-bottom: 1px solid #aeaeb1;
		vertical-align: top;
		padding-left: 6px;
   		font-size: 11px;
	}
	.span-ttl-cuis{
	    font-weight: bold;
	    font-size: 14px;
	    width: 100%;
	    background-color: #abc8cc;
	    float: left;
	    text-align: center;
	    margin-left: -3px;
    }
    .sep_border{
   		 background-color: #ffffff;
    }
</style>
 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Caisse enregistreuse</li>
         <li>Cuisine</li>
         <li class="active">Vérification conf</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
<std:form name="search-form">
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
	
		<c:if test="${listCuisine.size() == 0 }">
			<h2>Aucune configuration disponible.</h2>
		</c:if>
	
		<!-- row -->
		<div class="row" style="margin-left: 0px;margin-right: 0px;">
			<table style="width: 100%;" id="conf_cuis_table">
				<tr>
					<c:forEach items="${listCuisine }" var="caisse">
						<th>
							<span style="width: 40%;float: left;text-align: center;font-size: 18px;text-transform: uppercase;">${caisse.reference }</span> 
							<span style="width: 30%;float: left;"><i class="fa fa-map-marker"></i><span style="color: blue;font-weight: normal;"> ${caisse.opc_stock_cible.titre }</span></span>
							<span style="width: 30%;float: left;"><i class="fa fa-print"></i><span style="color: #9c27b0;font-weight: normal;"> ${caisse.imprimantes }</span></span>
						</th>
						<th style="width: 1%;border-bottom: 0px #ddd;background-color: #ffffff" class="sep_border"></th>
					</c:forEach>
				</tr>
				<tr>
					<c:forEach items="${listCuisine }" var="caisse">
						<c:set var="listArticles" value="${mapData.get(caisse.id).get(0) }" />
						<c:set var="listFamilles" value="${mapData.get(caisse.id).get(1) }" />
						<c:set var="listMenus" value="${mapData.get(caisse.id).get(2) }" />
						
						<td style="background-color: #eeeeee;">
							<c:if test="${listArticles.size() > 0 }">
								<span class="span-ttl-cuis">LES ARTICLES</span><br>
								<c:forEach items="${listArticles }" var="data">
									${data }<br>
								</c:forEach>
							</c:if>
							
							<c:if test="${listFamilles.size() > 0 }">
								<span class="span-ttl-cuis">LES FAMILLES</span><br>
								<c:forEach items="${listFamilles }" var="data">
									${data }<br>
								</c:forEach>
							</c:if>
							
							<c:if test="${listMenus.size() > 0 }">
								<span class="span-ttl-cuis">LES MENUS</span><br>
								<c:forEach items="${listMenus }" var="data">
									${data }<br>
								</c:forEach>
							</c:if>
						</td>
						<td class="sep_border" style="border-top: 0px #ddd;"></td>
					</c:forEach>		
				</tr>
			</table>
	 	</div>
	</div>
 </std:form>
 