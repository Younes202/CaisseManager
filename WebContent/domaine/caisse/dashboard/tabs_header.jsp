<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS"%>
<%@page import="java.util.HashMap"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.util.Date"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="java.util.Map"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>

<style>
	#journeeTab a{
	    height: 44px;
    	font-size: 14px;
	}
	#journeeTab i{
		font-size: 18px;
	}
	#journeeTab li{
	    height: 44px;
	    border-right: 1px solid silver;
	}
</style>
<%
Map<String, Integer> mapAlerte = (Map<String, Integer>)ControllerUtil.getMenuAttribute("mapAlerte", request);
SOFT_ENVS env = SOFT_ENVS.valueOf(StrimUtil.getGlobalConfigPropertie("context.soft"));
String tp = (String) request.getAttribute("tp");
	
String tab = (String)request.getAttribute("tab");
boolean isCompta = ContextGloabalAppli.getAbonementBean().isOptCompta();
boolean isPromo = ContextGloabalAppli.getAbonementBean().isOptCommercial();
boolean isRh = ContextGloabalAppli.getAbonementBean().isOptRh();
boolean isStock = ContextGloabalAppli.getAbonementBean().isOptStock();
%>

<ul class="nav nav-tabs tab-header" id="journeeTab">		
<%
switch(env){ 
	case restau : { %>
	<!-- ************************************ RESTAU *********************************** -->
		<li <%="journee".equals(tab)?" class='active'":"" %>>
			<a data-toggle="tab" href="#repartitionVente" wact="<%=EncryptionUtil.encrypt("dash.dashBoard.init_dashBoard")%>">
				<i class="fa fa-tachometer" style="color: blue;"></i> Chiffres 
			</a>
		</li>
		<li <%="caisse".equals(tab)?" class='active'":"" %>>
			<a data-toggle="tab" href="#repartitionVente" wact="<%=EncryptionUtil.encrypt("dash.dashCaisse.init_dashboard")%>">
				<i class="fa fa fa-fax" style="color: #e216ff;"></i> Ventes caisse 
			</a>
		</li>
		<li <%="venteBO".equals(tab)?" class='active'":"" %>>
			<a data-toggle="tab" href="#repartitionVente" wact="<%=EncryptionUtil.encrypt("dash.dashBoard.init_VENTESBO")%>">
				<i class="fa fa-exchange" style="color: blue;"></i> Ventes BO 
			</a>
		</li>
		<%if(isStock){ %>
			<li <%="stock".equals(tab)?" class='active'":"" %>>
				<a data-toggle="tab" href="#repartitionVente" wact="<%=EncryptionUtil.encrypt("dash.dashStock.work_init")%>">
					<i class="fa fa-exchange" style="color: #795548;"></i> Stock
					<%if(mapAlerte != null && mapAlerte.get("nbr_alertStock") > 0){ %>
					<span style="background-color: red;color: white;font-size: 13px;font-weight: bold;padding: 1px 5px;border-radius: 50%;"><%=mapAlerte.get("nbr_alertStock") %></span>
					<%} %> 
				</a>
			</li>
		<%} else{%>
			<li><a style="color:#e0e0e0;"><i class="fa fa-exchange" style="color: fushia;"></i> Stock </a></li>
		<%} %>

		<%if(isPromo){ %>
			<li <%="offre".equals(tab)?" class='active'":"" %>>
				<a data-toggle="tab" href="#repartitionVente" wact="<%=EncryptionUtil.encrypt("dash.dashOffre.work_init")%>">
					<i class="fa fa-gift" style="color: #001110;"></i>  Offres  
				</a>
			</li>
		<%} else{%>
			<li><a style="color:#e0e0e0;"><i class="fa fa-gift" style="color: fushia;"></i> Offres </a></li>
		<%} %>
		
		<%if(isCompta){ %>
			<li <%="indic".equals(tab)?" class='active'":"" %>>
				<a data-toggle="tab" href="#indicateurs" wact="<%=EncryptionUtil.encrypt("dash.dashBoard2.init_indicateur")%>"> 
					<i class="fa fa-th" style="color: green;"></i> Indicateurs 
				</a>
			</li>
			<li <%="banque".equals(tab)?" class='active'":"" %>>
				<a data-toggle="tab" href="#ibanque" wact="<%=EncryptionUtil.encrypt("dash.dashBoard.init_banque")%>"> 
					<i class="fa fa-university" style="color: orange;"></i> Banque 
				</a>
			</li>
		<%} else{%>
			<li><a style="color:#e0e0e0;"> <i class="fa fa-th" style="color: green;"></i> Indicateurs </a></li>
			<li><a style="color:#e0e0e0;"> <i class="fa fa-university" style="color: orange;"></i> Banque </a></li>
		<%} %>			
		<%if(isCompta){ %>
			<li <%="compta".equals(tab)?" class='active'":"" %>>
				<a data-toggle="tab" href="#ibanque" wact="<%=EncryptionUtil.encrypt("dash.dashCompta.init_dashboard")%>"> 
					<i class="fa fa-table" style="color: black;"></i> Comptabilité
					<%if(mapAlerte != null && mapAlerte.get("nbr_alertCompta") > 0){ %>
						<span style="background-color: red;color: white;font-size: 13px;font-weight: bold;padding: 1px 5px;border-radius: 50%;"><%=mapAlerte.get("nbr_alertCompta") %></span>
					<%} %>	 
				</a>
			</li>
		<%} else{%>
			<li><a style="color:#e0e0e0;"> <i class="fa fa-table" style="color: orange;"></i> Comptabilité </a></li>
		<%} %>
		
		<%if(isRh){ %>
			<li <%="rh".equals(tab)?" class='active'":"" %>>
				<a data-toggle="tab" href="#ibanque" wact="<%=EncryptionUtil.encrypt("dash.dashRH.init_dashboard")%>"> <i class="fa fa-group" style="color: olive;"></i> R.H </a>
				<%if(mapAlerte != null && mapAlerte.get("nbr_alertRH") > 0){ %>
					<span style="background-color: red;color: white;font-size: 13px;font-weight: bold;padding: 1px 5px;border-radius: 50%;"><%=mapAlerte.get("nbr_alertRH") %></span>
				<%} %>
			</li>
		<%} else{%>
			<li><a style="color:#e0e0e0;"> <i class="fa fa-group" style="color: olive;"></i> R.H </a></li>
		<%} %>		
					
	<%}; break;
	case market : { %>
	<!-- ************************************ POS *********************************** -->
		<li <%="journee".equals(tab)?" class='active'":"" %>>
			<a data-toggle="tab" href="#repartitionVente" wact="<%=EncryptionUtil.encrypt("dash.dashBoard.init_dashBoard")%>">
				<i class="fa fa-tachometer" style="color: blue;"></i> Journée 
			</a>
		</li>
		<li <%="caisse".equals(tab)?" class='active'":"" %>>
			<a data-toggle="tab" href="#repartitionVente" wact="<%=EncryptionUtil.encrypt("dash.dashCaisse.init_dashboard")%>">
				<i class="fa fa fa-fax" style="color: #e216ff;"></i> Caisse 
			</a>
		</li>
		<%if(isStock){ %>
			<li <%="stock".equals(tab)?" class='active'":"" %>>
				<a data-toggle="tab" href="#repartitionVente" wact="<%=EncryptionUtil.encrypt("dash.dashStock.work_init")%>">
					<i class="fa fa-exchange" style="color: #795548;"></i> Stock
					<%if(mapAlerte != null && mapAlerte.get("nbr_alertStock") > 0){ %>
					<span style="background-color: red;color: white;font-size: 13px;font-weight: bold;padding: 1px 5px;border-radius: 50%;"><%=mapAlerte.get("nbr_alertStock") %></span>
					<%} %> 
				</a>
			</li>
		<%} else{%>
			<li><a style="color:#e0e0e0;"><i class="fa fa-exchange" style="color: fushia;"></i> Stock </a></li>
		<%} %>
		
		<%if(isPromo){ %>
			<li <%="offre".equals(tab)?" class='active'":"" %>>
				<a data-toggle="tab" href="#repartitionVente" wact="<%=EncryptionUtil.encrypt("dash.dashOffre.work_init")%>">
					<i class="fa fa-gift" style="color: #001110;"></i>  Offres  
				</a>
			</li>
		<%} else{%>
			<li><a style="color:#e0e0e0;"><i class="fa fa-gift" style="color: fushia;"></i> Offres </a></li>
		<%} %>
		
		<%if(isCompta){ %>
			<li <%="indic".equals(tab)?" class='active'":"" %>>
				<a data-toggle="tab" href="#indicateurs" wact="<%=EncryptionUtil.encrypt("dash.dashBoard2.init_indicateur")%>"> 
					<i class="fa fa-th" style="color: green;"></i> Indicateurs 
				</a>
			</li>
			<li <%="banque".equals(tab)?" class='active'":"" %>>
				<a data-toggle="tab" href="#ibanque" wact="<%=EncryptionUtil.encrypt("dash.dashBoard.init_banque")%>"> 
					<i class="fa fa-university" style="color: orange;"></i> Banque 
				</a>
			</li>
		<%} else{%>
			<li><a style="color:#e0e0e0;"> <i class="fa fa-th" style="color: green;"></i> Indicateurs </a></li>
			<li><a style="color:#e0e0e0;"> <i class="fa fa-university" style="color: orange;"></i> Banque </a></li>
		<%} %>
		
		<%if(isCompta){ %>
			<li <%="compta".equals(tab)?" class='active'":"" %>>
				<a data-toggle="tab" href="#ibanque" wact="<%=EncryptionUtil.encrypt("dash.dashCompta.init_dashboard")%>"> 
					<i class="fa fa-table" style="color: black;"></i> Comptabilité
					<%if(mapAlerte != null && mapAlerte.get("nbr_alertCompta") > 0){ %>
						<span style="background-color: red;color: white;font-size: 13px;font-weight: bold;padding: 1px 5px;border-radius: 50%;"><%=mapAlerte.get("nbr_alertCompta") %></span>
					<%} %>	 
				</a>
			</li>
		<%} else{%>
			<li><a style="color:#e0e0e0;"> <i class="fa fa-table" style="color: orange;"></i> Comptabilité </a></li>
		<%} %>
		
		<%if(isRh){ %>
			<li <%="rh".equals(tab)?" class='active'":"" %>>
				<a data-toggle="tab" href="#ibanque" wact="<%=EncryptionUtil.encrypt("dash.dashRH.init_dashboard")%>"> <i class="fa fa-group" style="color: olive;"></i> R.H </a>
				<%if(mapAlerte != null && mapAlerte.get("nbr_alertRH") > 0){ %>
					<span style="background-color: red;color: white;font-size: 13px;font-weight: bold;padding: 1px 5px;border-radius: 50%;"><%=mapAlerte.get("nbr_alertRH") %></span>
				<%} %>
			</li>
		<%} else{%>
			<li><a style="color:#e0e0e0;"> <i class="fa fa-group" style="color: olive;"></i> R.H </a></li>
		<%} %>		
								
	<%}; break;
}%>

</ul>	
	