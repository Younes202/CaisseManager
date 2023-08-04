<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

	<%
	String typeEnum = (String) ControllerUtil.getMenuAttribute("typeEnum", request);
	%>
 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Modules avancés</li>
         <li>Typesénumurés</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
      <% if (StringUtil.isNotEmpty(typeEnum)){%>
          <std:linkPopup actionGroup="C" classStyle="btn btn-default" action="admin.valTypeEnum.work_init_create" icon="fa-3x fa-plus" tooltip="Créer" />
      <% } %>  
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

<std:form name="search-form">
	<!-- row -->
	<div class="row">
		
			<input type="hidden" id="maxCtrl" name="maxCtrl">

			<div class="form-group">

				<std:label classStyle="control-label col-md-2" valueKey="valEnum.opc_typenum" />
				<div class="col-md-6">
				<%
				String act = EncryptionUtil.encrypt("admin.valTypeEnum.work_init");
				//String url = "submitAjaxForm('" + act + "', '', $('#data-form'));";
				String url = "submitAjaxForm('" + act + "', null, $('#search-form'), null);";
				%>
					<std:select name="valEnum.opc_typenum.id" type="long" key="id" labels="libelle" data="${listTypeEnum}" required="true" onChange="<%=url %>" forceWriten="true" value="${typeEnum }" width="100%;" />
				</div>
			</div>
	</div>		
<hr>
			<c:if test="${not empty list_valeurs }">
					<div class="row">
						<!-- Liste des valeurs -->
						<cplx:table name="list_valeurs" width="100%" titleKey="valTypeEnum.list" initAction="admin.valTypeEnum.work_find" transitionType="simple">
							<cplx:header>
								<cplx:th type="empty" />
								<cplx:th type="string" valueKey="valTypeEnum.libelle" field="valTypeEnum.libelle" />
								<cplx:th type="empty" />
								<cplx:th type="empty" />
							</cplx:header>
							<cplx:body>
								<c:forEach items="${list_valeurs }" var="valTypeEnum">
									<cplx:tr workId="${valTypeEnum.id }">
										<cplx:td>
											<work:edit-link-popup />
										</cplx:td>
										<cplx:td align="left" value="${valTypeEnum.libelle}"></cplx:td>
										<cplx:td align="center">
										 	<std:link action="admin.valTypeEnum.desactiver" workId="${valTypeEnum.id }" actionGroup="C" icon="fa ${valTypeEnum.is_disable?'fa-unlock':'fa-lock' }" classStyle="btn btn-sm btn-${valTypeEnum.is_disable?'success':'warning'}" tooltip="${valTypeEnum.is_disable?'Activer':'Désactiver'}" />
										</cplx:td>
										<cplx:td>
											<work:delete-link />
										</cplx:td>
									</cplx:tr>
								</c:forEach>
							</cplx:body>
						</cplx:table>
					</div>
			</c:if>

		</std:form>
	</div>	