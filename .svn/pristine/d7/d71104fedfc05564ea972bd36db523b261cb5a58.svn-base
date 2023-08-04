<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<style>
	.modal-backdrop{
		display: none;
	}
	#list_mouvement_filter_form{
		margin-left: 10px;
	}
	#list_mouvement_filter_div{
		margin-top: 20px;
	}
</style>

<script type="text/javascript">
$(document).ready(function (){
	<%
	// Pour fermer la popup
	if(request.getAttribute("PAGE_JS") != null){%>
	<%=request.getAttribute("PAGE_JS")%>
	<%}%>
	$("#div-histo").css("height", ($(window).height()-40)+"px");
});
</script>

<div id="div-histo" style="margin-top: 3px;margin-left: 4px;overflow-x: hidden;overflow-y: auto;">
<std:form name="search-form">
	<a href="javascript:" id="histo_targ_link" targetDiv="right-div"></a>
	<std:link action="caisse-web.caisseWeb.initHistorique" targetDiv="right-div" style="display:none;" id="histo-lnk" />
	<std:link id="annul_cmd_lnk" style="display:none;" targetDiv="right-div" />
	
	<div class="widget-header bordered-bottom bordered-blue">
		<span class="widget-caption">Historique des articles</span>
			<div class="widget-buttons buttons-bordered" style="margin-bottom: 10px;">
         		Activer le clavier visuel         
         		<label>
	                 <input class="checkbox-slider toggle colored-blue" type="checkbox" id="keyboard-activator" style="display: none;">
	                 <span class="text"></span>
	             </label>
         	</div>
	</div>
	<div class="widget-body">
		<div class="row">
			<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
		</div>
		
	<c:set var="encryptionUtil" value="<%=new EncryptionUtil() %>" />
	
	<!-- Liste des mouvements -->
	<cplx:table name="list_mouvement" transitionType="simple" width="100%" title="Historique des commandes" initAction="caisse-web.caisseWeb.initHistorique" exportable="false" checkable="false" autoHeight="true">
		<cplx:header>
			<cplx:th type="string" value="Code" field="articleBalance.code" align="right" width="150" />
			<cplx:th type="string" value="Article" field="articleBalance.opc_article.libelle" />
			<cplx:th type="decimal" value="Poids" field="articleBalance.poids" width="120"/>
			<cplx:th type="decimal" value="Prix" filtrable="false" sortable="false" width="120" />
			<cplx:th type="decimal" value="Total" filtrable="false" sortable="false" width="120" />
		</cplx:header>
		<cplx:body>
			<c:forEach items="${listArticleBalance }" var="articleBalance">
				<cplx:tr workId="${articleBalance.id }">
					<cplx:td style="font-weight:bold;" value="${articleBalance.code}"></cplx:td>
					<cplx:td value="${articleBalance.opc_article.libelle }"></cplx:td>
					<cplx:td value="${articleBalance.poids}" align="right" style="font-weight:bold;color:green;"></cplx:td>
					<cplx:td value="${articleBalance.opc_article.prix_vente}" align="right"></cplx:td>
					<cplx:td value="${articleBalance.poids*articleBalance.opc_article.prix_vente}" align="right" style="font-weight:bold;color:blue;"></cplx:td>
				</cplx:tr>
			</c:forEach>
		</cplx:body>
	</cplx:table>
</div>	
 </std:form>	
</div>