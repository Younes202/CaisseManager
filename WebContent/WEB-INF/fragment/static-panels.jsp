  <%@page import="java.util.List"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
  <%@page import="framework.controller.ControllerUtil"%>


<jsp:include page="menu-cercle.jsp" />

 <a href="javascript:" id="global-msg-href" data-toggle="modal" data-backdrop="static" data-keyboard="false" data-target="#global_messages_div" style="display: none;"></a>
 <div id="global_messages_div" class="modal modal-message modal-warning fade" style="display: none;z-index: 99999" aria-hidden="true">
     <div class="modal-dialog">
         <div class="modal-content" style="width:450px;">
             <div class="modal-header">
                 <i class="fa fa-warning"></i>
             </div>
             <div class="modal-title" id="global-msg-title"></div>

             <div class="modal-body" id="global-msg-body"></div>
             <div class="modal-footer">
                 <button type="button" class="btn btn-warning" data-dismiss="modal">Fermer</button>
             </div>
         </div> <!-- / .modal-content -->
     </div> <!-- / .modal-dialog -->
 </div>
    
 <div id="global-dialog-quastion" style="display:none;">
     <div class="row">
        <div class="col-md-12" id="global-dialog-quastion-content">
        </div>
     </div>
</div>


<div id="spinner" class="spinner" style="display: none;"> 
<% if(!request.getHeader("User-Agent").contains("Mobi")) {%>
	<img id="img-spinner" src="resources/<%=StrimUtil.getGlobalConfigPropertie("context.soft") %>/img/loading2.svg" alt="Loading" />
<%} else{ %>
	<img id="img-spinner" src="resources/framework/img/ajax-loader2.svg" alt="Loading" />
<%} %>
</div>

<div id="showConfirmDeleteBox" title="Suppression d'un &eacute;l&eacute;ment" style="display:none;">
	<p>
		<img src='resources/framework/img/icon/delete.png' style='float:left;'/>&nbsp;
		<span class='question'>
		Cet &eacute;l&eacute;ment sera d&eacute;finitivement supprim&eacute;.<br>
		Voulez-vous continuer ?
		</span>
	</p>
</div>

<div id="generic_modal" class="modal modal-message modal-warning fade" style="display: none;" aria-hidden="true">
	<div class="modal-dialog">  
		<div class="modal-content" id="generic_modal_body">
        </div>
    </div>
</div>
<div id="generic_modal2" class="modal modal-message modal-warning fade" style="display: none;" aria-hidden="true">
	<div class="modal-dialog">  
		<div class="modal-content" id="generic_modal_body2">
        </div>
    </div>
</div>

<iframe id="downloadframe" name="downloadframe" style="display: none;"></iframe>
<div id="div_gen_printer" style="display: none;">

</div>


  <a href="javascript:" id="abn-lnk-href" data-toggle="modal" data-backdrop="static" data-keyboard="false" data-target="#abn_div" style="display: none;"></a>
  <div id="abn_div" class="modal modal-message modal-warning fade" style="display: none;z-index: 99999" aria-hidden="true">
     <div class="modal-dialog">
         <div class="modal-content" style="width:450px;">
             <div class="modal-header">
                 <i class="fa fa-warning"></i>
             </div>
             <div class="modal-title">Echéance abonnement expirée</div>
             <div class="modal-body">
	            <div class="row">
	                <div class="col-md-12">
	                	<br><br>
	                	Abonnement expir&eacute; depuis <b><%=ControllerUtil.getUserAttribute("CHECK_DIFF_DAYS", request)%></b> jours.<br>
	                	Merci de r&eacute;gler votre abonnement afin d'éviter l'arrêt du système.
	                	<br><br>
	                </div>	
	            </div>
	            <div class="row">
	            	<span style="font-size: 3em;color:red;" id="abn_span_count"></span>
	            	<br><br>
	            	<button type="button" id="abn_close_pop" style="display: none;" data-dismiss="modal"></button>
	            </div>
	        </div>
	     </div>
	  </div>
	</div>
	
<a href="javascript:" id="closeTabLink" wact="<%=EncryptionUtil.encrypt("admin.parametrage.clearTabSession") %>" targetDiv="xxxxx" style="display: none;"></a>

<script type="text/javascript">
<%
String env = (String) request.getSession(true).getAttribute("ENV_MOBILE");

if(env == null || !env.startsWith("mobile-client")){
	boolean isAvailableConnexion = ControllerUtil.isAvailableConnexion(request); 
	if(!isAvailableConnexion){%> 
		location.reload();
	<%}
}%>
</script>
		        