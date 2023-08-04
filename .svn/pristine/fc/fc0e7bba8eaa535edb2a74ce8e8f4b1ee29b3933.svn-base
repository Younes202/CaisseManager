<!DOCTYPE html>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.controller.ControllerUtil"%>
<html lang="fr-fr">	

<%
		// Purge
	ControllerUtil.cleanAll(request);
	%>	
	
<!-- Head -->	
	<head>
		<title>Gestion de la caisse automatique</title>
		<meta name="description" content="">
		<jsp:include page="WEB-INF/fragment/header-resources.jsp"></jsp:include>
		<link href="resources/framework/css/back-style.css" rel="stylesheet" />
		<link href="resources/framework/css/default.css" rel="stylesheet" />
			
	 <script type="text/javascript">
	<%if(!ContextAppli.APPLI_ENV.back.toString().equals(ControllerUtil.getUserAttribute("CURRENT_ENV", request))){%>
		window.location = '<%=request.getRequestURL().substring(0, request.getRequestURL().lastIndexOf("/"))%>/caisse';
	<% } %>
	
	$(document).ready(function (){
		$('body').on('click', function (e) {
		    //did not click a popover toggle or popover
		    if ($(e.target).data('toggle') !== 'popover'
		        && $(e.target).parents('.popover.in').length === 0) { 
		        $('[data-toggle="popover"]').popover('hide');
		    }
		});
	});
	
	let mybutton = document.getElementById("myBtn");

	//When the user scrolls down 20px from the top of the document, show the button
	window.onscroll = function() {scrollFunction()};

	function scrollFunction() {
		if(mybutton){
			if(document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
			 	mybutton.style.display = "block";
			} else{
			 	mybutton.style.display = "none";
			}
		}
	}

	//When the user clicks on the button, scroll to the top of the document
	function topFunction() {
	/*	document.body.scrollTop = 0;
		document.documentElement.scrollTop = 0;
		*/
		$("html, body").animate({ scrollTop: 0 }, "slow");
	}

	
    </script>	
    
    <style>
    
    .closeTabStl{
	    color: red;
	    position: absolute;
	    right: -5px;
	    top: 14px;
	    z-index: 9999;
	    cursor: pointer;
	    margin-right: 3px;
    }
    .page-header.page-header-fixed+.page-body{
    	margin-top: 16px !important;
    	margin-left: -15px !important;;
    	margin-right: -16px !important;;
    }
    
	#myBtn {
	     display: none;
	  position: fixed;
	  bottom: 20px;
	  right: 30px;
	  z-index: 99;
	  font-size: 18px;
	  border: none;
	  outline: none;
	   background-color: red; /*green #34990a */
	  color: white;
	  cursor: pointer;
	  padding: 15px;
	  border-radius: 4px;
	}
	
	#myBtn:hover {
	  background-color: #555;
	}
	    
    
    </style>
    
    	
	</head>
<!-- /Head -->
<!-- Body -->
<body>
    <!-- Loading Container -->
    <div class="loading-container">
        <div class="loader"></div>
    </div>
    <button type="button" onclick="topFunction();" id="myBtn" title="Aller au top"><i class="fa fa-arrow-up"></i></button>
    
    <div class="navbar">
    	<jsp:include page="WEB-INF/fragment/nav-barre.jsp"></jsp:include>
    </div>
    
    <!-- Main Container -->
    <div class="main-container container-fluid">
        <!-- Page Container -->
        <div class="page-container">
        <!-- Page Sidebar -->
            <div class="page-sidebar" id="sidebar">
        		<jsp:include page="WEB-INF/fragment/menu-left.jsp"></jsp:include>
        	</div>
        	<!-- /Page Sidebar -->
            <!-- Page Content -->
            <div class="page-content">
	           <ul class="nav nav-tabs tab-header" id="tabMnuNav">	
					<li class="active">  
					    <a data-toggle="tab" href="#tab_nav_0" class="tabTtlH">
							<i class="fa fa-genderless" style="color: blue;"></i><span id="tab_first_span">..............</span>
						</a>
					</li>
				</ul>
				<div class="tab-content" id="tabMnuNavContent">
					<div id="tab_nav_0" class="tab-pane topNavContent in active">
			            <div id="body-content">
			            	
			            </div>
			         </div> 
				</div>
			</div>
             <!-- /Page Content -->	
        </div>
        <!-- /Page Container -->
    </div>
    <!-- Main Container -->
    
    <jsp:include page="WEB-INF/fragment/footer-resources.jsp"></jsp:include>
	<jsp:include page="WEB-INF/fragment/static-panels.jsp"/>

  </body>
    
</html>