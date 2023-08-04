<%@page import="framework.controller.ControllerUtil"%>
<%@page import="appli.model.domaine.vente.persistant.JourneePersistant"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<script type="text/javascript">
	var interval_id;
	$(document).ready(function (){
		for (var i = 1; i < interval_id; i++){
	        window.clearInterval(i);
		}
		
		$("#stopTimer").click(function(){
			for (var i = 1; i <= interval_id; i++){
		        window.clearInterval(i);
			}
			alertify.success("Le rafraichissement automatique est d&eacute;sactiv&eacute;.");
		});
		$("#startTimer").click(function(){
			interval_id = setInterval(function() {
				submitAjaxForm('<%=EncryptionUtil.encrypt("caisse_restau.caisseConfigurationRestau.find_ecrans_cuisine")%>', '', $("#search-form"), $("#lnk_suivi"));
			 }, <%=request.getAttribute("delaisRefresh")%>);
			
			alertify.success("Le rafraichissement automatique est activ&eacute;.");
		});
	});
</script>

<std:form name="search-form">	
	<a href="javascript:" style="display: none;" id="lnk_suivi" targetDiv="targetRefreshDiv"></a>
	
			<!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion de caisse</li>
         <li>D&eacute;tails des caisses</li>
         <li class="active">Recherche</li>
     </ul>
 </div>

<%
String tp = (String)ControllerUtil.getMenuAttribute("tp", request);
%>

 <!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
	      <%if(!"mnu".equals(tp)){ %>
	      <std:link actionGroup="C" style='font-weight:bold;color:black !important;' classStyle="btn btn-link label label-primary graded" value="Retour aux caisses" action="caisse.caisse.work_find" icon="fa fa-mail-reply-all" tooltip="Vue caisses" />
	      <%} else{ %>
	      &nbsp;
	      <%} %>
      </div>
      <span style="margin-left: 50px;">Fr&eacute;quence rafraichissement : </span>
      <select name="delaisRefresh" style="margin-top: 3px;border: 2px solid #2196F3;">
		<option value="5000" ${delaisRefresh==5000?'selected="selected"':'' }>5 s</option>
      	<option value="10000" ${delaisRefresh==10000?'selected="selected"':'' }>10 s</option>
      	<option value="20000" ${delaisRefresh==20000?'selected="selected"':'' }>20 s</option>
      	<option value="30000" ${delaisRefresh==30000?'selected="selected"':'' }>30 s</option>
      </select>
      
      <std:link classStyle="btn btn-success" action="caisse_restau.caisseConfigurationRestau.find_ecrans_cuisine" params="isp=1" targetDiv="targetRefreshDiv" icon="fa fa fa-refresh" />
      <std:link classStyle="btn btn-success" action="" style="margin-left: 50px;margin-left: 50px;" id="startTimer" icon="fa fa-play" tooltip="D&eacute;marrer le rafraichissement automatique"/>
      <std:link classStyle="btn btn-danger" action="" icon="fa fa-ban" tooltip="Arr&ecirc;ter le timer" id="stopTimer"/>
      
      <span style="margin-left: 50px;">Statut de la commande</span> 
		<%
		String act = EncryptionUtil.encrypt("caisse_restau.caisseConfigurationRestau.find_ecrans_cuisine");
		String url = "submitAjaxForm('" + act + "', '', $('#search-form'), $('#lnk_suivi'));";
		%>
		<std:select mode="standard" name="statut" style="margin-top: 3px;border: 2px solid #2196F3;" type="string" data="${listStatut }" onChange="<%=url %>" value="${statut }" width="180px;" />
      
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
	<div class="row" id="targetRefreshDiv">
		<jsp:include page="caisse_detail_list.jsp"></jsp:include>
	</div>

	<!-- end widget content -->
</div>
<!-- end widget div -->
	
</std:form>