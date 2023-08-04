<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>


<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Modules avancés</li>
		<li class="active">Logs</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
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

<c:set var="encryptionUtil" value="<%=new EncryptionUtil ()%>" />

	<!-- row -->
	<div class="row">
		<std:form name="search-form"> 
			<div class="col-xs-12 col-md-6">
               <div class="well with-header  with-footer">
                   <div class="header bg-blue">
                       Fichiers de log disponibles
                   </div>
                   <table class="table table-hover">
                       <tbody>
                       <c:forEach var="file" items="${listFiles }">
                           <tr>
                               <td>
                                  <a href="front?w_f_act=<%=EncryptionUtil.encrypt("admin.requeteur.download_logs")%>&path=${encryptionUtil.encrypt(file.getPath()) }" target="_blank">${file.getName() }</a>
                               </td>
                           </tr>
                          </c:forEach> 
                       </tbody>
                   </table>

                   <div class="footer">
                       <code>Module pour service technique</code>
                   </div>
               </div>
			</div>
		</std:form>

	</div>
	<!-- end widget content -->

</div>
<!-- end widget div -->
