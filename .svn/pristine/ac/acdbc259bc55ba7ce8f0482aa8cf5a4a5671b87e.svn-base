<%@page import="appli.model.domaine.stock.persistant.ArticlePersistant"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="java.util.List"%>
<%@page import="appli.model.domaine.stock.persistant.ArticleStockInfoPersistant"%>

<%
	List<ArticleStockInfoPersistant> listArtView = (List<ArticleStockInfoPersistant>)request.getAttribute("listArtView");
%>

<h4>R&eacute;sum&eacute;</h4>

<% if(listArtView.size() > 0){
	ArticleStockInfoPersistant articleView1 = listArtView.get(0);
	ArticlePersistant opcArt = articleView1.getOpc_article();
	opcArt = (opcArt == null ? new ArticlePersistant() : opcArt);
	%>
<div class="well bordered-top bordered-bottom bordered-pink">

<table style="width: 80%;margin-left: 10%;background-color: #ffffff;">
	<tr style="height: 13px;" class="sub">
	    <td align="right" style="border-left: 1px solid #c6d5e1;">Prix achat</td>
	    <td align="right" style="font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(opcArt.getPrix_achat_ht())%></td>
		<td align="right">Prix achat moyen</td>
		<td align="right" style="font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(opcArt.getPrix_achat_moyen_ht())%></td>
		<td align="right">Prix achat TTC</td>
		<td align="right" style="font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(opcArt.getPrix_achat_ttc())%></td>
	</tr>
	<tr style="height: 13px;" class="sub">	
		<td align="right" style="border-left: 1px solid #c6d5e1;">Montant TVA</td>
		<td align="right" style="font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(opcArt.getMtt_tva())%></td>
		<td  align="right">TVA</td>
		<td align="right" style="font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(BigDecimalUtil.get(opcArt.getOpc_tva_enum()!=null?opcArt.getOpc_tva_enum().getCode():"0"))%></td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
</table>
<br>
<h4>Quantit&eacute; stock</h4>
<table style="width: 80%;margin-left: 10%;background-color: #ffffff;">
	<tr style="height: 13px;" class="sub">
		<td style="background-color: #cddc95;">Emplacement</td>
		<td style="background-color: #cddc95;">Qte. entr&eacute;e</td>
		<td style="background-color: #cddc95;">Qte. sortie</td>
		<td style="background-color: #cddc95;">Qte. restante</td>
		<td style="background-color: #cddc95;">Seuil alerte</td>
	</tr>
<%	
	Long oldEmplacement = null;
	BigDecimal totalEntree = null;
	BigDecimal totalSortie = null;
	BigDecimal totalRestant = null;
	String unite = null;
	for(ArticleStockInfoPersistant articleView : listArtView){
		totalEntree = BigDecimalUtil.add(totalEntree, articleView.getQte_entree());
		totalSortie = BigDecimalUtil.add(totalSortie, articleView.getQte_sortie());
		totalRestant = BigDecimalUtil.add(totalRestant, articleView.getQte_restante());
		unite = (articleView.getOpc_article() != null && articleView.getOpc_article().getOpc_unite_achat_enum() != null) 
						? articleView.getOpc_article().getOpc_unite_achat_enum().getLibelle() : "";
	%>
		<tr style="height: 13px;" class="sub">
			<td><%=articleView.getOpc_emplacement().getTitre() %></td>
			<td align="right"><%=BigDecimalUtil.formatNumberZero(articleView.getQte_entree())%> <%=unite %></td>
			<td align="right"><%=BigDecimalUtil.formatNumberZero(articleView.getQte_sortie())%> <%=unite %></td>
			<td align="right"><%=BigDecimalUtil.formatNumberZero(articleView.getQte_restante())%> <%=unite %></td>
			<td align="right"><%=BigDecimalUtil.formatNumberZero(articleView.getOpc_emplacement_seuil()!=null?articleView.getOpc_emplacement_seuil().getQte_seuil():null)%> <%=unite %></td>
		</tr>
<%	}%>
	<tr>
		<td style="border-bottom: 0px;"></td>
		<td align="right" style="font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(totalEntree) %> <%=unite %></td>
		<td align="right" style="font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(totalSortie) %> <%=unite %></td>
		<td align="right" style="font-weight: bold;"><%=BigDecimalUtil.formatNumberZero(totalRestant) %> <%=unite %></td>
		<td style="border-bottom: 0px;"></td>
	</tr>

</table>
<br>
<%} else{ %>
	Aucun d&eacute;tail &agrave; afficher. 
<%} %>

<c:if test="${listProduit.size() > 0 }">
	<h4>Composition</h4>
	<table style="width: 50%;margin-left: 10%;background-color: #ffffff;">
		<tr style="height: 13px;" class="sub">
			<td style="background-color: #cddc95;">Composants</td>
			<td style="background-color: #cddc95;" width="150">Quantit&eacute;</td>
			<td style="background-color: #cddc95;" width="150">Prix achat</td>
		</tr>
		
		<c:if test="${listProduit.size() == 0 }">
			<tr><td colspan="3" style="font-size: 10px;">Aucun détail.</td></tr>
		</c:if>
		<c:forEach items="${listProduit}" var="artCompo">
			<tr style="height: 13px;" class="sub">
				<td style="padding-left: 50px;">${artCompo.opc_article_composant.code}-${artCompo.opc_article_composant.libelle}</td>
				<td align="right"><fmt:formatDecimal value="${artCompo.quantite}" /></td>
				<td align="right"><fmt:formatDecimal value="${artCompo.opc_article_composant.prix_achat_ht}" /></td>
			</tr>
		</c:forEach>
	</table>
	<br>
</c:if>
		
</div>
