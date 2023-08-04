<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="appli.model.domaine.personnel.persistant.ClientPersistant"%>
<%@page import="framework.model.beanContext.EtablissementPersistant"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script src="resources/framework/js/html5-qrcode.min.js"></script>

<style>
		#fullscreen-toggler{
			position: absolute;
		    right: 0px;
		    top: 0px;
		    z-index: 10000;
		    box-shadow: -1px 2px 10px rgba(7, 14, 8, 6.5) ;
		}
		.notyet{
			color: #acacac !important;
		    background-color: gray !important;
		}
		.html5-qrcode-element{
		    border-radius: 7px;
		    background-color: #a6edff;
		    padding: 6px;
		    float: inherit;
		    margin: 12px;
		    border: 1px solid black;
		}
		.modal-backdrop{
			position: absolute;
			right: 1000px;
		}
    </style>
    
<div style="margin-top: -1px;overflow-x: hidden;overflow-y: auto;">
	<std:form name="data-form">
		<div class="widget">
			<div class="widget-header bordered-bottom bordered-blue">
				<span class="widget-caption">Mes applications</span>
				<a href="javascript:" data-toggle="modal" data-backdrop="static" data-keyboard="false" data-target="#app_modal_div">Ajouter</a>
			</div>
			<div class="widget-body">
				<div class="row">
					<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
				</div>
				
				 <div id="app_modal_div" class="modal modal-message modal-warning fade" style="display: none;z-index: 99999" aria-hidden="true">
				     <div class="modal-dialog">
				         <div class="modal-content" style="width:350px;">
				             <div class="modal-body">
						             <div id="container" style="text-align: center;">
								    	<h2 style="color: white;">Cliquez pour scanner le QR code de l'établissement</h2>
										<div id="qr-reader" style="width:300px"></div>
									</div>
				             </div>
				             <div class="modal-footer">
				                 <button type="button" class="btn btn-warning" data-dismiss="modal">Fermer</button>
				             </div>
				         </div> <!-- / .modal-content -->
				     </div> <!-- / .modal-dialog -->
				 </div>
	 
				<std:link params="skipF=1" action="caisse.clientMobile.addAppClient" onComplete="$('#app_lnk').trigger('click');" id="qr_sub_lnk" style="display:none;"/>
				<std:hidden id="qr" name="qr" />
				
				<div class="row" style="margin-left: 2px;margin-right: 0px;">
					<table>
						<%
						Map<String, String> mapEnv = (Map<String, String>)request.getAttribute("mapEnv");
						if(mapEnv != null){
							for(String env : mapEnv.keySet()){ %>
								<tr>
									<td>
										<std:link classStyle="" targetDiv="main-ets-div" action="caisse.clientMobile.loadApp" 
											params='<%="app="+env %>' icon="fa fa-beer" value="<%=mapEnv.get(env)%>"
											style="font-size: 22px;
								    				width: 100%;
								    				float: left;
								    				padding-left: 20px;" />
								    </td>
								    <td>
								    	<std:link classStyle="" targetDiv="main-ets-div" action="caisse.clientMobile.removeEtsApp" actionGroup="D"
											params='<%="app="+env %>' icon="fa fa-trash" 
											style="font-size: 22px;
								    				width: 100%;
								    				float: left;
								    				padding-left: 20px;" />
								    </td>
								  </tr>  				
						  		<%}
							}%>
					  </table>
				</div>
			</div>
		</div>
	</std:form>
</div>

 <script type="text/javascript">
		var lastResult, countResults = 0;
	    function onScanSuccess(decodedText, decodedResult) {
         	if (decodedText !== lastResult) {
		        ++countResults;
		        lastResult = decodedText;
		        if(!decodedText.startsWith('APP') && !decodedText.startsWith('ETS')){
		    		alertify.error("Qr code non autorisé.");
		    		return;
		    	}
		        $("#qr").val(decodedText);
		        $("#qr_sub_lnk").trigger("click");
		    }
		}
		var html5QrcodeScanner = new Html5QrcodeScanner("qr-reader", { fps: 10, qrbox: 250 });
		html5QrcodeScanner.render(onScanSuccess);
	</script>
