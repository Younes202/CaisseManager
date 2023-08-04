<%@page import="appli.controller.domaine.stock.bean.FamilleBean"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<link href="resources/framework/css/tree/jquery.treeview.css?v=1.0" rel="stylesheet" type="text/css" />
<script src="resources/framework/js/tree/jquery.treeview.js"></script>

<script type="text/javascript">
$(document).ready(function (){
	getTabElement("#treeattack-ul").treeview({
		collapsed: true,
		animated: "fast",
		persist: "cookie",
		unique: true,
		cookieId: "treefamille"
	});
	
	$(document).on('click', ".filetree a", function(){
		getTabElement(".filetree a").css("background-color", "transparent");
		$(this).css("background-color", "#CDDC39");
		
		getTabElement(".widget-buttons a").attr("params", "fam="+$(this).attr("fam"));
		getTabElement("#generic_modal_body .widget-caption").html($(this).text());
	});
});
</script>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion de stock</li>
		<li>Fiche des types de famille</li>
		<li class="active">Recherche</li>
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

	<!-- row -->
	<div class="row">
		<std:form name="search-form">

			<div class="col-lg-6 col-sm-6 col-xs-12">
				<div class="widget">
					<div class="widget-header header-large">
						<span class="widget-caption">List de Types Famille</span>
						<div class="widget-buttons">
						
							<a href="javascript:" wact="<%=EncryptionUtil.encrypt("stock.typeFamille.work_init_create") %>" 
							 targetdiv="generic_modal_body" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#generic_modal"
							 >
							<span class="glyphicon glyphicon-plus" aria-hidden="true"></span></a>
							
							<a href="javascript:" wact="<%=EncryptionUtil.encrypt("stock.typeFamille.work_update") %>" 
							 data-toggle="modal" data-backdrop="static" data-keyboard="false" data-target="#generic_modal">
							 <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>
							 
							<a href="javascript:" wact="<%=EncryptionUtil.encrypt("stock.typeFamille.work_delete") %>" 
							 data-toggle="modal" data-backdrop="static" data-keyboard="false" data-target="#generic_modal">
							 <span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
							
						</div>
					</div>
		<!--   Div widget boy    -->
					<div class="widget-body">



						<ul id="treeattack-ul" class="filetree">
							<%
		List<FamilleBean> listFamille = (List<FamilleBean>)request.getAttribute("listTypesFamilles");
      	Integer oldLevel = null;
      
		for(FamilleBean familleBean : listFamille){
			boolean isNode = (familleBean.getB_right()- familleBean.getB_left() > 1);
			if(oldLevel != null){
				if(oldLevel > familleBean.getLevel()){
					while(oldLevel > familleBean.getLevel()){	%>
							</li>
						</ul>
						<%
						oldLevel--;
					}
				} else if(oldLevel == familleBean.getLevel()){%>
						</li>
						<%	}
			}
			%>
						<li><span class="<%=isNode ? "folder" :"file"%>"> <a href="javascript:" fam="<%=EncryptionUtil.encrypt(""+familleBean.getId())%>"> <%=familleBean.getLibelle() %>
							</a>
						</span> <% 
				
				if(isNode){ %>
							<ul>
								<%} %>
								<%
			oldLevel = familleBean.getLevel();
		}
		%>
							</li>
						</ul>
						</ul>
			
					</div>
					<!--  end div widget body -->
				</div>
				<!--  end div the whole widget -->
			</div>




		</std:form>

	</div>
	<!-- end widget row -->

</div>
<!-- end page-body div -->

