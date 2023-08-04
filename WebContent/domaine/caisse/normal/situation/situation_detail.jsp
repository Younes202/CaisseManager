<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.controller.domaine.caisse.ContextAppliCaisse"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.controller.Context"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<%
Map<String, Object> mapData = (Map)request.getAttribute("mapData");
String mnu = (String)ControllerUtil.getMenuAttribute("curMnu", request);
%>
<script type="text/javascript">
$(document).ready(function (){
	<%if(ContextAppliCaisse.getCaisseBean() != null){%>
		$("#left-div").hide();
		$("#div-sit").css("width", (screen.width-120)+"px").css("height", (screen.height-180)+"px").css("margin-left", "3%");
	<%}%>
});
</script>

<%
int cols = 2;
if("livr".equals(mnu)){
	cols = 5;
} else if("socLivr".equals(mnu)){
	cols = 3;
}
%>
<div id="div-sit" style="margin-top: 3px;margin-left: 4px;padding-right: 5px;overflow-x: hidden;overflow-y: auto;border: 2px solid green;border-radius: 9px;">
	 <div class="col-md-12" style="margin-top: 10px;">
	 	<span style="font-size: 17px;color: gray;padding-left: 30px;font-weight: bold;text-transform: uppercase;">${cliSituation.nom } ${cliSituation.prenom }</span>
	</div>
	
	 <div class="col-md-7">
	<table class="table table-striped table-bordered table-hover" style="margin-top: 15px;width: 90%;">
			<tr style="background-color: #ddd8d8;">
				<td colspan="<%=cols %>">
					SITUATION <span style="font-size: 15px;color: blue;padding-left: 7px;font-weight: bold;">DU <%=mapData.get("dtStart") %> AU <%=mapData.get("dtEnd") %></span>
					<c:if test="${listMouvement.size() > 0 }">
						<std:link actionGroup="S" classStyle="btn btn-sm btn-default shiny" style="color:black;margin-left:10px;" params="src=cai&tpp=T" action="pers.clientCaisse.print_ticket_cmd" targetDiv="div_gen_printer" icon="fa fa-print" value="Imprimer commandes" tooltip="Imprimer" />
						<std:link actionGroup="S" classStyle="btn btn-sm btn-default shiny" style="color:black;margin-left:10px;" params="src=cai&tpp=T&isRes=1" action="pers.clientCaisse.print_ticket_cmd" targetDiv="div_gen_printer" icon="fa fa-print" value="Imprimer commandes portefeuille" tooltip="Imprimer" />
					</c:if>
				</td>
			</tr>
			
		<tr style="background-color: #000000;color: white;">
			<td><i class="fa fa-credit-card"></i> MODE DE PAIEMENT</td>
			<td style="text-align: right;"><i class="fa fa-money"></i> MONTANT</td>
			<%if(!"cli".equals(mnu)){%>
			<td style="text-align: right;"><i class="fa fa-magnet"></i> MARGE LIVREUR</td>
			<%} %>
			<%if("livr".equals(mnu)){%>
				<td style="text-align: right;"><i class="fa fa-magnet"></i> MARGE SOCIETE</td>
				<td style="text-align: right;"><i class="fa fa-magnet"></i> MONTANT LIVAISON</td>
			<%} %>
			
		</tr>
	<%
	BigDecimal ttlMtt = null, ttlMarge = null, ttlMargeSoc = null, ttlLivraison = null;
	boolean isPassec = false;
	//
	for(String key : mapData.keySet()){
		if(key.startsWith("paie_")){
			isPassec = true;
			
			if("cli".equals(mnu)){
				BigDecimal val = (BigDecimal)mapData.get(key);
				ttlMtt = BigDecimalUtil.add(ttlMtt, val);
			%>	
				<tr>
					<td><%=("RESERVE".equals(key.substring(5)) ? "PORTEFEUILLE":key.substring(5)) %></td>
					<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumber(val) %></td>
				</tr>	
			        
			<%} else if("socLivr".equals(mnu)){
				Object[] val = (Object[])mapData.get(key);
				ttlMtt = BigDecimalUtil.add(ttlMtt, BigDecimalUtil.get(""+val[1]));
				ttlMarge = BigDecimalUtil.add(ttlMarge, BigDecimalUtil.get(""+val[2]));
				%>
				<tr>
					<td><%=("RESERVE".equals(key.substring(5)) ? "PORTEFEUILLE":key.substring(5)) %></td>
					<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumber(BigDecimalUtil.get(""+val[1])) %></td>
					<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumber(BigDecimalUtil.get(""+val[2])) %></td>
				</tr>	
			
			<%} else if("livr".equals(mnu)){
				Object[] val = (Object[])mapData.get(key);
				BigDecimal mttLivraison = BigDecimalUtil.get(""+val[2]);
				BigDecimal mttLivreur = BigDecimalUtil.get(""+val[3]);
				BigDecimal mttSoc = BigDecimalUtil.get(""+val[4]);
				
				ttlMtt = BigDecimalUtil.add(ttlMtt, BigDecimalUtil.get(""+val[1]));
				ttlMarge = BigDecimalUtil.add(ttlMarge, mttLivreur);
				ttlMargeSoc = BigDecimalUtil.add(ttlMargeSoc, mttSoc);
				ttlLivraison = BigDecimalUtil.add(ttlLivraison, mttLivraison);
				%>
				<tr>
					<td><%=("RESERVE".equals(key.substring(5)) ? "PORTEFEUILLE":key.substring(5)) %></td>
					<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumber(BigDecimalUtil.get(""+val[1])) %></td>
					<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumber(mttLivreur) %></td>
					<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumber(mttSoc) %></td>
					<td style="text-align: right;font-weight: bold;"><%=BigDecimalUtil.formatNumber(mttLivraison) %></td>
				</tr>	
		<%}
		}
	}
	
	if(!isPassec){
	%>
		<tr>
			<td colspan='<%=cols %>'><span style="color: blue;">Aucun détail disponible</span></td>
		</tr>
	<%}else { %>
		<tr>
			<td style="text-align: right;font-weight: bold;">TOTAL PAIEMENT</td>
			
			<td style="text-align: right;">
				<span style="color: #3a790a;font-weight: bold;text-align: right;font-size: 18px;">
					<%=BigDecimalUtil.formatNumberZero(ttlMtt) %>
				</span>
			</td>
			<%if(!"cli".equals(mnu)){ %>
			<td style="text-align: right;">
				<span style="color: #3a790a;font-weight: bold;text-align: right;font-size: 18px;">
					<%=BigDecimalUtil.formatNumberZero(ttlMarge) %>
				</span>
			</td>
			<%} %>
			<%if("livr".equals(mnu)){ %>
			<td style="text-align: right;">
				<span style="color: #3a790a;font-weight: bold;text-align: right;font-size: 18px;">
					<%=BigDecimalUtil.formatNumberZero(ttlMargeSoc) %>
				</span>
			</td>
			<td style="text-align: right;">
				<span style="color: #3a790a;font-weight: bold;text-align: right;font-size: 18px;">
					<%=BigDecimalUtil.formatNumberZero(ttlLivraison) %>
				</span>
			</td>
			<%} %>
		</tr>
		<%if("cli".equals(mnu)){ %>
		<tr>
			<td style="text-align: right;font-weight: bold;">TOTAL RECHARGEMENT</td>
			<td style="text-align: right;">
				<span style="color: #9c27b0;font-weight: bold;text-align: right;font-size: 18px;">
					<%=BigDecimalUtil.formatNumberZero((BigDecimal)mapData.get("totalRechargePeriode")) %>
				</span>
			</td>
		</tr>
		<%} %>
		<%if(mapData.get("nbrCmd") != null){ %>
		<tr>
			<td style="text-align: right;font-weight: bold;">NOMBRE COMMANDES</td>
			<td style="text-align: right;">
				<span style="color: #3a790a;font-weight: bold;text-align: right;font-size: 18px;">
					<%=mapData.get("nbrCmd") %>
				</span>
			</td>		
		</tr>
		<%} %>
	<%} %>
	</table>
	</div>
	 <div class="col-md-5">
	<table class="table table-striped table-bordered table-hover" style="margin-top: 15px;">
			<tr style="background-color: #ddd8d8;">
				<td colspan="2">
					SITUATION GLOBALE
					<c:if test="${listMouvement.size() > 0 }">
						<std:link actionGroup="S" classStyle="btn btn-sm btn-primary shiny" style="color:#ffffff;margin-left:1%;" params="src=cai&tpp=T&isRes=1&isSit=1" action="pers.clientCaisse.print_ticket_cmd" targetDiv="div_gen_printer" icon="fa fa-print" value="Imprimer situation" tooltip="Imprimer situation" />
					</c:if>
				</td>
			</tr>
		<%if("cli".equals(mnu)){%>
			<tr style="background-color: #defbff;font-weight: bold;">
				<td>MONTANT RECHARGE PORTEFEUILLE :</td>
				<td style="text-align: right;"> 
					<span style="color: #3a790a;font-weight: bold;font-size: 18px;">
						<%=BigDecimalUtil.formatNumberZero((BigDecimal)mapData.get("totalRecharge")) %>
					</span>
				</td>
			</tr>
			<tr style="background-color: #defbff;font-weight: bold;">
				<td>MONTANT COMMANDES PORTEFEUILLE :</td>
				<td style="text-align: right;"> 
					<span style="color: #9c27b0;font-weight: bold;font-size: 18px;">
						<%=BigDecimalUtil.formatNumberZero((BigDecimal)mapData.get("totalMvmPortefeuille")) %>
					</span>	
				</td>
			</tr>
			<tr style="background-color: #defbff;font-weight: bold;">
				<td>
					NOMBRE COMMANDES PORTEFEUILLE :
				</td>
				<td style="text-align: right;">	 
					<span style="color: #3a790a;font-weight: bold;font-size: 18px;">
						<%=mapData.get("nbrAllCmd") %>
					</span>
				</td>
			</tr>
			<tr style="background-color: #defbff;font-weight: bold;height: 63px;">
				<td style="font-size: 21px;vertical-align: middle;">SOLDE PORTEFEUILLE (RESERVE) :</td>
				<td style="text-align: right;vertical-align: middle;">
					<span style="color: #e91e63;font-weight: bold;font-size: 21px;">
						<%=BigDecimalUtil.formatNumberZero((BigDecimal)mapData.get("solde")) %>
					</span>
				</td>
			</tr>
		<%} else{ %>
			<tr style="background-color: #defbff;font-weight: bold;">
				<td>TOTAL MONTANT COMMANDES :</td>
				<td style="text-align: right;"> 
					<span style="color: #3a790a;font-weight: bold;font-size: 18px;">
						<%=BigDecimalUtil.formatNumberZero((BigDecimal)mapData.get("totalCmdNet")) %>
					</span>	
				</td>
			</tr>
			<tr>	
				<td>PART LIVREUR :</td>
				<td style="text-align: right;"> 
					<span style="color: #3a790a;font-weight: bold;font-size: 18px;">
						<%=BigDecimalUtil.formatNumberZero((BigDecimal)mapData.get("totalCmdFraisLivr")) %>
					</span>
				</td>
			</tr>
			<% if("livr".equals(mnu)){ %>
				<tr>	
					<td>PART SOCIETE :</td>
					<td style="text-align: right;"> 
						<span style="color: #3a790a;font-weight: bold;font-size: 18px;">
							<%=BigDecimalUtil.formatNumberZero((BigDecimal)mapData.get("totalCmdFraisSoc")) %>
						</span>
					</td>
				</tr>
			<%} %>
		<%} %>
		
	</table>
	</div>
	
	<hr style="margin-top: 11px;margin-bottom: 10px;width: 100%;">
	
	<jsp:include page="/domaine/caisse/normal/situation/situation_mvm.jsp" />
</div>