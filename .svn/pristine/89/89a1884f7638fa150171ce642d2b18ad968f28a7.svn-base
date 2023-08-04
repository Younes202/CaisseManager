<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page errorPage="/commun/error.jsp"%>

<!DOCTYPE html style="overflow:hidden;">

<html lang="fr-fr" style="overflow:hidden;">	
<%
// Purge
ControllerUtil.cleanAll(request);
%>	
	
<!-- Head -->	
	<head>
		<title>Pr&eacute;sentoire</title>
		<meta name="description" content="">
		<jsp:include page="/WEB-INF/fragment/header-resources.jsp"></jsp:include>
		<script src="resources/restau/js/voice.js?v=1.0"></script>
		
		<style>
		body{ 
			background-color:#f4f3ef;
		}
		.cardCmd {
		    text-align: center;
		    width: 30%;
		    box-shadow: 7px 23px 57px -13px #7B7446;
		    border-radius: 10px;
		    background-color: #FFFFFF;
		    color: #252422;
		    margin: 20px;
		    margin-right: 1%;
		    float: left;
		}
		.refCmdStyle{
		    font-size: 7em;
    		font-weight: bold;
    		font-family: cursive;
		}
		@keyframes blink {
		  0% {
		    opacity: 1;
		  }
		  50% {
		    opacity: 0;
		  }
		}
		div.blink {
		  animation: blink 2s infinite steps(1);
		}
		</style>
		
		<script type="text/javascript">
			$(document).ready(function (){
				setTimeout(function(){ $("#refresh-toggler").trigger("click"); }, 100);
				
				<%
				Integer delaisRefresh = (Integer)ControllerUtil.getUserAttribute("DELAIS_REFRESH_ECRAN_SECONDE", request);
				if(delaisRefresh != null){%>
					setInterval(function() {
 						submitAjaxForm('<%=EncryptionUtil.encrypt("caisse-web.afficheurClient.loadCommande")%>', '', $("#data-form"), $("#generic_id"));
 					  }, <%=ControllerUtil.getUserAttribute("DELAIS_REFRESH_ECRAN_SECONDE", request)%>); 
				<%}%>
				
				$(".header-title a").click(function(){
					if($(this).attr("id") != 'valid-cmds'){
						$("#curr-btn").remove();
						$(this).css("font-weight", "bold").prepend("<i id='curr-btn' style='color: red;' class='fa fa-hand-o-right'></i>");
					}
				});
				
				// Commandes area height ----------------------------------
				refreshSize();
				//
				var doit;
				window.onresize = function(){
				  clearTimeout(doit);
				  doit = setTimeout(refreshSize, 100);
				};
				$("#fullscreen-toggler").click(function(){
					setTimeout(function(){
						refreshSize();
					}, 1000);
				});
			});
			function refreshSize(){
				var widowHeight = $(window).height();
				$("#corp-div").css("height", (widowHeight-30)+"px");
			}
		</script>
	</head>
<!-- /Head -->
<!-- Body -->
<body>
	<a href="javascript:" id="generic_id" targetDiv="corp-div"></a>

<std:form name="data-form">
    <!-- Main Container -->
    <div class="main-container container-fluid">
        <!-- Page Container -->
        <div class="page-container">
			<!-- Page Breadcrumb -->
				<div class="page-header position-relative" style="position: relative;left: 0px;top: 0px;background: #262626;height: 42px;">
					<div class="header-title" style="padding-top: 4px;">
						|
				        <std:link classStyle="btn btn-default" style="color:red;" action="commun.login.disconnect" targetDiv="corp-div" icon="fa-3x fa-lock" tooltip="Quitter" />
				        <span style="font-size: 19px;font-weight: bold;color: #a1c2dd;margin-left: 10px;margin-right:10px; font-style: italic;">
				        	COMMANDES PRÊTES
				        </span>
			      	</div>
			      <!--Header Buttons-->
			      <div class="header-buttons">
				     <a class="refresh" id="refresh-toggler" href="javascript:void(0);" wact="<%=EncryptionUtil.encrypt("caisse-web.afficheurClient.loadCommande")%>" targetDiv="corp-div">
				         <i class="glyphicon glyphicon-refresh"></i>
				     </a>
				     <a class="fullscreen" id="fullscreen-toggler" href="#">
				         <i class="glyphicon glyphicon-fullscreen"></i>
				     </a>
				 </div>
			      <!--Header Buttons End-->
			  </div>
			  <!-- /Page Header -->
			
			<div class="page-body" style="margin: 0px;overflow:auto;" id="corp-div">
				
			</div>
        </div>
        <!-- /Page Container -->
    </div>
    <!-- Main Container -->
 </std:form>   
    <jsp:include page="/WEB-INF/fragment/footer-resources.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/fragment/static-panels.jsp"/>
    
    <!-- ******************************************************** -->
     <button id="dialog_alert_cmd" class="btn btn-success" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-success" style="display: none;"></button>
	 <!--Success Modal Templates-->
    <div id="modal-success" class="modal modal-message modal-success fade" style="display: none;" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <i class="glyphicon glyphicon-check"></i>
                </div>
                <div class="modal-title" id="dialog_cmd_title"></div>
                <div class="modal-body"></div>
                <div class="modal-footer">
                    <button id="dialog_cmd_close" type="button" class="btn btn-success" data-dismiss="modal" style="display: none;"></button>
                </div>
            </div> <!-- / .modal-content -->
        </div> <!-- / .modal-dialog -->
    </div>
    <!-- ******************************************************** -->
    
  </body>
    
</html>