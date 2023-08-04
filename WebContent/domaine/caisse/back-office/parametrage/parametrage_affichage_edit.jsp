<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
   .form-title{ 
    	margin-left: 12px;
	}
</style>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Param&eacute;trage</li>
		<li class="active">Interface graphique</li>
	</ul>
</div>

<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="C" classStyle="btn btn-default" action="admin.parametrage.work_init_update" icon="fa-3x fa-pencil" tooltip="Modifier" />
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
	<!-- widget grid -->
	<div class="widget">
		<std:form name="data-form">
		
			<div class="row">
		        <div class="col-lg-12 col-sm-12 col-xs-12">
		              <div class="tabbable">
		                    <ul class="nav nav-tabs" id="myTab">
		                          <%request.setAttribute("mnu_param", "interface"); %> 
			 					  <jsp:include page="parametrage_headers.jsp" />
		                     </ul>
		                </div>
		          </div>
		      </div>
		
			<div class="widget-body">
				<div class="row">
				
					<div class="col-lg-6 col-sm-6 col-xs-12">				
				<%if(ControllerUtil.isEditionWritePage(request)){ %>
					<a href="javascript:" wact="<%=EncryptionUtil.encrypt("admin.parametrage.setValeurDefaut") %>" params="skipP=1" style="text-decoration: underline;color: blue;position: absolute;right: 27px;">Remettre les valeurs par d&eacute;faut</a>
				<%} %>
						<c:set var="oldSubGroupe" value="" />
						<c:forEach items="${listParams }" var="parametre">
								<c:if test="${parametre.groupe == 'PANEL_COLOR'}">
								<c:if test="${oldSubGroupe != parametre.groupe_sub or empty oldSubGroupe}">
									<h3 style="color: #2dc3e8;padding-left: 20px;border-bottom: 1px dashed rgba(0, 0, 0, .2);">${parametre.groupe_sub }</h3>
								</c:if>
								<c:set var="oldSubGroupe" value="${parametre.groupe_sub }" />
								<div class="form-group">
									<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
									<div class="col-md-7">
										<c:choose>
											<c:when test="${parametre.type=='STRING'}">
												<std:text type="string" name="param_${parametre.code}" classStyle="form-control colorpicker" style="width:130px;float:left;" value="${parametre.valeur}" />
											</c:when>
										</c:choose>	
									</div>
								</div>
							</c:if>
						</c:forEach>
				</div>
				<div class="col-lg-6 col-sm-6 col-xs-12">
						<c:set var="oldSubGroupe" value="" />
						<c:forEach items="${listParams }" var="parametre">
							<c:if test="${parametre.groupe == 'BUTTON_COLOR'}">
								<c:if test="${oldSubGroupe != parametre.groupe_sub or empty oldSubGroupe}">
									<h3 style="color: #2dc3e8;padding-left: 20px;border-bottom: 1px dashed rgba(0, 0, 0, .2);">${parametre.groupe_sub }</h3>
								</c:if>
								<c:set var="oldSubGroupe" value="${parametre.groupe_sub }" />
								<div class="form-group">
									<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
									<div class="col-md-7">
										<c:choose>
											<c:when test="${parametre.type=='STRING'}">
												<std:text type="string" name="param_${parametre.code}" classStyle="form-control colorpicker" style="width:130px;float:left;" value="${parametre.valeur}" />
											</c:when>
										</c:choose>	
									</div>
								</div>
							</c:if>
						</c:forEach>	
					</div>
				</div>
				
				<div class="row">
					<div class="col-lg-6 col-sm-6 col-xs-12">
						<c:set var="oldSubGroupe" value="" />
						<c:forEach items="${listParams }" var="parametre">
							<c:if test="${parametre.groupe == 'FONT_COLOR'}">
								<c:if test="${oldSubGroupe != parametre.groupe_sub or empty oldSubGroupe}">
									<h3 style="color: #2dc3e8;padding-left: 20px;border-bottom: 1px dashed rgba(0, 0, 0, .2);">${parametre.groupe_sub }</h3>
								</c:if>
								<c:set var="oldSubGroupe" value="${parametre.groupe_sub }" />
								<div class="form-group">
									<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
									<div class="col-md-7">
										<c:choose>
											<c:when test="${parametre.type=='STRING'}">
												<std:text type="string" name="param_${parametre.code}" classStyle="form-control colorpicker" style="width:130px;float:left;" value="${parametre.valeur}" />
											</c:when>
										</c:choose>	
									</div>
								</div>
							</c:if>
						</c:forEach>
					</div>
				</div>	
					
				</div>
		<hr>
		<div class="form-actions">
			<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
				<std:button actionGroup="M" classStyle="btn btn-success" action="admin.parametrage.work_update" params="tp=ihm" icon="fa-save" value="Sauvegarder" />
			</div>
		</div>
		</std:form>
	</div>
</div>

<script src="resources/framework/js/colorpicker/jquery.minicolors.js"></script>

<script type="text/javascript">
//--jQuery MiniColors--
$('.colorpicker').each(function () {
    $(this).minicolors({
        control: $(this).attr('data-control') || 'hue',
        defaultValue: $(this).attr('data-defaultValue') || '',
        inline: $(this).attr('data-inline') === 'true',
        letterCase: $(this).attr('data-letterCase') || 'lowercase',
        opacity: $(this).attr('data-opacity'),
        position: $(this).attr('data-position') || 'bottom left',
        change: function (hex, opacity) {
            if (!hex) return;
            if (opacity) hex += ', ' + opacity;
            try {
                console.log(hex);
            } catch (e) { }
        },
        theme: 'bootstrap'
    });
});
</script>
