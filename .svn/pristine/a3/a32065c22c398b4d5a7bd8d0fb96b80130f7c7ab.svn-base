<%@page import="framework.model.util.MenuMappingService"%> 
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.controller.bean.MenuBean"%>
<%@page import="java.util.List"%>
<%@page import="framework.controller.Context"%>
<meta charset="utf-8">
		<meta name="author" content="">
		
		<meta name="description" content="Dashboard" />
	    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
	    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
	    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	
	    <!--Basic Styles-->
	    <link href="resources/framework/css/bootstrap.min.css" rel="stylesheet" />
	    <link id="bootstrap-rtl-link" href="#" rel="stylesheet" />
	    <link href="resources/framework/css/font-awesome.min.css" rel="stylesheet" />
	    <link href="resources/framework/css/weather-icons.min.css" rel="stylesheet" />
	
		<link rel="shortcut icon" href="resources/<%=StrimUtil.getGlobalConfigPropertie("context.soft") %>/img/fav-icon.png" type="image/x-icon">
	
	    <!--Fonts-->
<!-- 	    <link href="http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,400,600,700,300" rel="stylesheet" type="text/css"> -->
<!-- 	    <link href='http://fonts.googleapis.com/css?family=Roboto:400,300' rel='stylesheet' type='text/css'> -->
<!-- 		<link href="resources/framework/css/google_font_1.css" rel="stylesheet" type="text/css" /> -->
<!-- 		<link href="resources/framework/css/google_font_2.css" rel="stylesheet" type="text/css" /> -->


	    <!--Beyond styles-->
	    <link id="beyond-link" href="resources/framework/css/beyond.min.css?v=1.5" rel="stylesheet" type="text/css" />
	    <link href="resources/framework/css/demo.min.css" rel="stylesheet" />
	    <link href="resources/framework/css/typicons.min.css" rel="stylesheet" />
	    <link href="resources/framework/css/animate.min.css" rel="stylesheet" />
	    <link id="skin-link" href="#" rel="stylesheet" type="text/css" />
	
		<link id="bsdp-css" href="resources/framework/css/datepicker/bootstrap-datepicker3.css" rel="stylesheet">
	
	    <!--Skin Script: Place this script in head to load scripts for skins and rtl support-->
	    <script src="resources/framework/js/skins.min.js"></script>
				
		<link rel="stylesheet" type="text/css" media="screen" href="resources/framework/css/default.css?v=<%=StrimUtil.getGlobalConfigPropertie("version.resources")%>">
		<link href="resources/framework/css/table/table.css?v=<%=StrimUtil.getGlobalConfigPropertie("version.resources")%>" rel="stylesheet" type="text/css"/>
		<link href="resources/framework/css/alertify.core.css" rel="stylesheet" type="text/css"/>
		<link href="resources/framework/css/select2/select2.css" rel="stylesheet" type="text/css"/>
		
		<script src="resources/framework/js/jquery.min.js"></script>
		<script src="resources/framework/js/jquery.ba-bbq.js"></script>
		<script src="resources/framework/js/labels_fr.js"></script>

<%if(request.getAttribute("SKIP_HEADER_JS") == null){ %>
		
<script type="text/javascript">		
	$(function(){
		 $(window).bind('hashchange', function(e) {
			 var retConfirm = true;
			 if($("button[targetbtn='M']").length > 0){
				 retConfirm = confirm("Vos éventuelles modifications seront perdues. Souhaitez-vous continuer ?");
			 }
			 if(retConfirm){
			 	exec_action();
			 }
		 });
	    <%-- D�clencher l'�vennement --%>
	    exec_action(true);
	    
	    <%-- Trigger news --%>
	    if($("#global_news_div").length > 0){
		    setTimeout(() => {
		    	$("#global-news-href").trigger("click");	
			}, 1000);
	    }
	});
	
function exec_action(isReloaded){
	if($(window).width() < 800){
		$('#sidebar').hide(100);
	}
	
	<%-- Eviter erreur javascript au login --%>
	 try {$.param.fragment();} catch(e){return;}
	
	 var url = $.param.fragment();
	 var lmnu = getParamsValue(url, 'lmnu');
	 if(lmnu == 'lgo'){
		 clearAllCookies();
	 }
	  <%-- Gerer le refresh du navigateur sur le lmmnu=lgo (d�connexion) --%>
	  if(isReloaded && lmnu == null){
		  <%-- Si tableau de bord disponible --%>
		  <%if(Context.isMenuAvailable("dashboard", false)){%>
		  		window.location.href = "#lmnu=dashboard&rdm="+randString(4);
		  <%} else{ 
			  // Premier menu valide 
			  String menuId = null; 
			  List<MenuBean> listMenu = MenuMappingService.mapMenu;
			  for(MenuBean menuBean : listMenu){
				  if(!menuBean.isSheet()){
					  continue;
				  }
				  if(menuBean.getIsVisible() && Context.isMenuAvailable(menuBean.getId(), false)){
					  menuId = menuBean.getId();
					  break;
				  }
			  }
		  %>
		  window.location.href = "#lmnu=<%=menuId%>&rdm="+randString(4);
		  <%}%>
		  return;
	  }
	  
	  <%-- Si premiere connexion --%>
	  if(!url || (url == '')){
			updateMenuLeft();
			return;
	  }
	  
	  callForwardAjaxUrl("front?"+url);
 	  updateMenuLeft();
}
	
function updateMenuLeft(){
	var url = $.param.fragment(); 
	var lmnu = getParamsValue(url, 'lmnu');
	
	$("li[class='active']").removeAttr("class");
	
		// Update button menu
		$('#li_'+lmnu).parents("li").each(function(){
			$(this).attr("class", "open");
		});
		$('#li_'+lmnu).attr("class", "active");
}
</script>

<%}%>