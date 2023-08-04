<%@page import="java.util.HashMap"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.Map"%>

<%
String devise = StrimUtil.getGlobalConfigPropertie("devise.symbole");
Map<String,Object> variables = (Map<String,Object>)request.getAttribute("dataSituation");
if(variables == null){
	variables = new HashMap();
}
BigDecimal mttNonPointes = (BigDecimal)variables.get("chequeNonPointes");
BigDecimal mttPertes = (BigDecimal)variables.get("pertesDeclares");
Integer nbrEmployeActif = (Integer)variables.get("employesActifs");

BigDecimal mttSalaire = (BigDecimal)variables.get("totalSalaire");
BigDecimal mttAchat = (BigDecimal)variables.get("totalAchat");
BigDecimal mttVente = (BigDecimal)variables.get("totalVente");
%>

<div class="databox-row">
	<div class="databox-cell cell-4 no-padding text-align-center bordered-right bordered-platinum">
		<span class="databox-number lightcarbon no-margin"> <%=BigDecimalUtil.formatNumberZero(mttNonPointes) %> <%=devise%></span> <span class="databox-text sonic-silver  no-margin">
		Montant ch&egrave;ques non point&eacute;s</span>
	</div>
	<div class="databox-cell cell-4 no-padding text-align-center bordered-right bordered-platinum">
		<span class="databox-number lightcarbon no-margin"> <%=BigDecimalUtil.formatNumberZero(mttPertes) %> <%=devise%></span> <span class="databox-text sonic-silver no-margin">
		Pertes articles ce mois</span>
	</div>
	<div class="databox-cell cell-4 no-padding text-align-center">
		<span class="databox-number lightcarbon no-margin"> <%=BigDecimalUtil.formatNumberZero(nbrEmployeActif) %></span> <span class="databox-text sonic-silver no-margin">
		Employ&eacute;s actifs</span>
	</div>
</div>
<hr>
<div class="databox-row">
	<div class="databox-cell cell-4 no-padding text-align-center bordered-right bordered-platinum">
		<span class="databox-text sonic-silver no-margin">Total achats TTC</span> <span class="databox-number lightcarbon no-margin"><%=BigDecimalUtil.formatNumberZero(mttAchat) %> <%=devise%></span>
	</div>
	<div class="databox-cell cell-4 no-padding text-align-center bordered-right bordered-platinum">
		<span class="databox-text sonic-silver no-margin">Total vente TTC</span> <span class="databox-number lightcarbon no-margin"><%=BigDecimalUtil.formatNumberZero(mttVente) %> <%=devise%></span>
	</div>
	<div class="databox-cell cell-4 no-padding text-align-center">
		<span class="databox-text sonic-silver no-margin">Total salaires</span> <span class="databox-number lightcarbon no-margin"><%=BigDecimalUtil.formatNumberZero(mttSalaire) %>	DH</span>
	</div>
</div>