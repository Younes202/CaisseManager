<%@page import="framework.model.util.audit.ReplicationGenerationEventListener"%>
<%@page import="framework.controller.ControllerUtil"%>
<script type="text/javascript">
<%
boolean isAvailableConnexion = ControllerUtil.isAvailableConnexion(request);
if(!isAvailableConnexion){%>
	var isMobContext = (window.location.href.indexOf("mob-") != -1);
	var isJtn = (window.location.href.indexOf("jtn") != -1);
	
	var url = window.location.toString().substring(0, window.location.toString().lastIndexOf("/"));
	var fragSync = null;
	
	if(isJtn){
		var idxTkn = window.location.toString().indexOf("jtn=");
		if(idxTkn != -1){
			var idxNext = window.location.toString().indexOf("&", idxTkn);
			idxNext = (idxNext == -1 ? window.location.toString().length : idxNext);
			
			fragSync = window.location.toString().substring(idxTkn, idxNext);
		}
	}
	
	if(isMobContext){
		window.location.reload();
	} else if(isJtn){
		//window.location = url+"/bo?lgo=1&"+fragSync;
		window.location = url+"?lgo=1&"+fragSync;
	} else{
		window.location = url+"?lgo=1";
	}
	
<%}%>
</script> 
