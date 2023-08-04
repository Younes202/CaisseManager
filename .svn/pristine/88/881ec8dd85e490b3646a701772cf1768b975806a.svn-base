<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page errorPage="/commun/error.jsp"%>

		<style type="text/css">
			.col-lg-1, .col-lg-10, .col-lg-11, .col-lg-12, .col-lg-2, .col-lg-3, .col-lg-4, .col-lg-5, .col-lg-6, .col-lg-7, .col-lg-8, .col-lg-9, .col-md-1, .col-md-10, .col-md-11, .col-md-12, .col-md-2, .col-md-3, .col-md-4, .col-md-5, .col-md-6, .col-md-7, .col-md-8, .col-md-9, .col-sm-1, .col-sm-10, .col-sm-11, .col-sm-12, .col-sm-2, .col-sm-3, .col-sm-4, .col-sm-5, .col-sm-6, .col-sm-7, .col-sm-8, .col-sm-9, .col-xs-1, .col-xs-10, .col-xs-11, .col-xs-12, .col-xs-2, .col-xs-3, .col-xs-4, .col-xs-5, .col-xs-6, .col-xs-7, .col-xs-8, .col-xs-9{
				padding: 1px;
			}
			.border-paneaux{
			    background-color: #eee;
			    border: 1px solid #53a93f;
			    border-radius: 4px;
			 }
    		 /**Commandes*/
    		 .menu-root-style{
    		 	font-size: 14px;
    		 	background-color: #191919;
    			color: #fbfbfb;
    		 	font-weight: bold;
    		 }
    		 .menu-cat-style{
    		 	font-size: 14px;
    		 	background-color: #a1c2dd;
    			color: #fbfbfb;
    		 	font-weight: bold;
    		 }
    		 .menu-style{
    		 	font-size: 12px;
    		 	color: #d42b11;
    		 	font-weight: bold;
    		 }
    		 .group-style{
    		 	font-size: 12px;
    		 	color: black;
    		 	font-weight: normal;
    		 }
    		 .group-style td{
    		 	padding-left: 15px;
    		 }
    		 .ligne-style{
    		 	font-size: 12px;
    		 	color: blue;
    		 	font-weight: bold;
    		 	background-color: #FFF9C4;
    		 }
    		 .ligne-style td{
    		 	padding-left: 30px;
    		 }
    		 /*Hors menu*/
    		 .famille-style{
    		 	font-size: 12px;
    		 	color: green;
    		 	font-weight: bold;
    		 }
    		 .ligne-fam-style{
    		 	font-size: 11px;
    		 	color: blue;
    		 	font-weight: normal;
    		 }
    		 .ligne-fam-style td{
    		 	padding-left: 15px;
    		 }
    		 #cmd-tablexxxx tr{
    		 	 border-bottom: 1px dotted #777;
    		 	 height: 12px;
    		 }
    		 #cmd-tablexxxx td{
    		 	padding-right: 5px;
    		 }
		</style>
		
		<script type="text/javascript">
			$(document).ready(function (){
				
				$(".statutBtn").click(function(){
					if($(this).attr("id") != 'valid-cmds'){
						$("#curr-btn").remove();
						$(this).css("font-weight", "bold").prepend("<i id='curr-btn' style='color: red;' class='fa fa-hand-o-right'></i>");;
					}
				});
				$("#pagger_slct").change(function(){
					writeLocalStorage('pagger_pr', $(this).val());
				});
				<%
			/*	Cookie[] cookies = request.getCookies();
				String valPaggerSt = "4";
				if (cookies != null) {
				 for (Cookie cookie : cookies) {
				   if (cookie.getName().equals("pagger_pr")) {
					   valPaggerSt = cookie.getValue();
				    }
				  }
				}*/
				%>
				
				$("#pagger_slct").val(readLocalStorage("pagger_pr"));
				// Arr�ter les anciens timers
				for (var i = 1; i < interval_tracker_id; i++){
			        window.clearInterval(i);
				}
				//
				interval_tracker_id = setInterval(function() {
					submitAjaxForm('<%=EncryptionUtil.encrypt("caisse-web.presentoire.loadCommande")%>', '', $("#data-form"), $("#generic_id"));
			 	}, 10000);
				// Premier appel
				submitAjaxForm('<%=EncryptionUtil.encrypt("caisse-web.presentoire.loadCommande")%>', '', $("#data-form"), $("#generic_id"));
				//
				var widowHeight = $(window).height();
				var widowWidth = $(window).width();
				$(".page-body").css("width", "100%");
				$("#detail-pres-div").css("height", (widowHeight-60)+"px");
				
				$("#left-div").hide();
				$("#right-div").css("width", "99%");
			});
		</script>
	<a href="javascript:" id="generic_id" targetDiv="detail-pres-div"></a>

			<!-- Page Breadcrumb -->
				<div class="page-header position-relative" style="position: relative;left: 0px;top: 0px;background: #262626;height: 42px;">
					<div class="header-title" style="padding-top: 4px;">
				        <std:link classStyle="btn btn-yellow shiny statutBtn" style="color:white;" targetDiv="detail-pres-div" action="caisse-web.presentoire.loadCommande" params="tp=val" id="cmd-valide" icon="fa-3x fa-cutlery" value="Valid&eacute;es" tooltip="Valid&eacute;es" />
				        |
				        <std:link classStyle="btn btn-magenta shiny statutBtn" style="color:white;" targetDiv="detail-pres-div" action="caisse-web.presentoire.loadCommande" params="tp=pre" icon="fa-3x fa-check-square-o" value="Pr&ecirc;tes" tooltip="Pr&ecirc;tes" />
				        |
				        <std:link classStyle="btn btn-success shiny statutBtn" style="color:white;" targetDiv="detail-pres-div" action="caisse-web.presentoire.loadCommande" params="tp=liv" icon="fa-3x fa-history" value="Livr&eacute;es" tooltip="Livr&eacute;es" />
				        
				        
				         <select id="pagger_slct" style="background-color: #eeeeee;margin-left: 20px;">
				        	<option>6</option>
				        	<option>4</option>
				        	<option>3</option>
				        	<option>2</option>
				        </select>
				        | 
				        
			      	</div>
			      <!--Header Buttons-->
			      <div class="header-buttons">
				     <a class="refresh" id="refresh-toggler" href="javascript:void(0);" wact="<%=EncryptionUtil.encrypt("caisse-web.presentoire.loadCommande")%>" targetDiv="detail-pres-div">
				         <i class="glyphicon glyphicon-refresh"></i>
				     </a>
				 </div>
			      <!--Header Buttons End-->
			  </div>
			  <!-- /Page Header -->
			
			<div class="page-body" style="margin: 0px;overflow:auto;" id="detail-pres-div">
				
			</div>
