<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.ControllerUtil"%>

<%
String mnu = (String)request.getAttribute("curMnu");
boolean isPortefeuilleMnu = ("PORTEFEUILLE".equals(ControllerUtil.getUserAttribute("MNU_FIDELITE", request)) && ControllerUtil.getMenuAttribute("IS_MENU_CMLIENT", request) == null); 
boolean isPointMnu = ("POINT".equals(ControllerUtil.getUserAttribute("MNU_FIDELITE", request)) && ControllerUtil.getMenuAttribute("IS_MENU_CMLIENT", request) == null); 
%>

<div class="tabbable">
	<ul class="nav nav-tabs" id="myTab">
	<%if(!isPortefeuilleMnu && !isPointMnu){ %>
		<li <%="fiche".equals(mnu)?" class='active'":"" %>><a data-toggle="tab" href="#descripton" wact="<%=EncryptionUtil.encrypt("pers.client.work_edit")%>"> Fiche </a></li>
	<%} %>	
	<% if (ControllerUtil.getMenuAttribute("clientId", request) != null) { %>
		<%if(!isPointMnu && !isPortefeuilleMnu){ %>
			<li <%="mvm".equals(mnu)?" class='active'":"" %>><a data-toggle="tab" href="#mouvement" wact="<%=EncryptionUtil.encrypt("pers.client.find_mvm_client")%>"> Commandes </a></li>
			<li <%="reduc".equals(mnu)?" class='active'":"" %>><a data-toggle="tab" href="#mouvement" wact="<%=EncryptionUtil.encrypt("pers.client.find_reduction")%>"> R&eacute;ductions </a></li>
			<li <%="artPrix".equals(mnu)?" class='active'":"" %>><a data-toggle="tab" href="#mouvement" wact="<%=EncryptionUtil.encrypt("pers.composantClientPrix.work_find")%>"> Offres articles </a></li>
			<li <%="situatBO".equals(mnu)?" class='active'":"" %>><a data-toggle="tab" href="#mouvement" wact="<%=EncryptionUtil.encrypt("pers.client.init_situation")%>"> Situation BO</a></li>
			<li <%="situat".equals(mnu)?" class='active'":"" %>><a data-toggle="tab" href="#mouvement" wact="<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.init_situation")%>" " params="curMnu=cli"> Situation </a></li>
		<%} %>	
		
		<%if(!isPortefeuilleMnu){ %>
			<li <%="pointG".equals(mnu)?" class='active'":"" %>>
	           <a style="color: blue;" data-toggle="tab" href="#Point" wact="<%=EncryptionUtil.encrypt("fidelite.carteFideliteClient.findPointsGain")%>">
	            | Points gagnés
	           </a>
	         </li>
	         <li <%="pointC".equals(mnu)?" class='active'":"" %>>
	           <a style="color: blue;" data-toggle="tab" href="#Point" wact="<%=EncryptionUtil.encrypt("fidelite.carteFideliteClient.findPointsConso")%>">
	            Points consommés
	           </a>
	         </li>
	      <%} %>
	     <%if(!isPointMnu){ %>    
	         <li <%="porteR".equals(mnu)?" class='active'":"" %>>
	          <a style="color:green;" data-toggle="tab" href="#client" wact="<%=EncryptionUtil.encrypt("fidelite.portefeuille.find_recharge")%>">
	           | Recharge portefeuille
	          </a>
	        </li>
	        <li <%="porteCmd".equals(mnu)?" class='active'":"" %>>
	          <a style="color:green;" data-toggle="tab" href="#client" wact="<%=EncryptionUtil.encrypt("fidelite.portefeuille.find_utilisation")%>">
	            Cmd portefeuille
	          </a>
	        </li>
		<% } %>
		<%} %>
	</ul>
</div>