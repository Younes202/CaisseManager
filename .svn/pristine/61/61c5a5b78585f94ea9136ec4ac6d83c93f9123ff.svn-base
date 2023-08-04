<%@page import="framework.model.common.util.StringUtil"%>
<%
String pack = (String)request.getAttribute("pack_abonn");
String opts = (String)request.getAttribute("recap_abonn");
String tarif = (String)request.getAttribute("tarif_abonn");
%>

<table class="table table-hover">
	<%if(StringUtil.isNotEmpty(pack)){ %>
	<tr style="background-color: #FFC107;font-weight: bold;text-align: center;">
    	<td><%=pack %></td>
    </tr>
    <tr>	
    	<td><%=opts %></td>
    </tr>
    <tr style="background-color: #262626;color: white;font-size: 15px;text-align: center;font-weight: bold;">
		<td><%=tarif %></td>
    </tr>
	<%} else{ %>
	<tr>
    	<td style="font-style: italic;color: gray;">Aucun abonnement.</td>
    </tr>
	<%} %>

    
</table>