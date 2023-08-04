<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.Context"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
boolean isShowEcart = Context.isOperationAvailable("ECARTINV");
boolean isCorrection = StringUtil.isTrue(""+ControllerUtil.getMenuAttribute("MODE_CORECT", request));
%>

<table class="table table-bordered table-striped" style="width: 97%;margin-left: 20px">
	<thead>
		<tr>
			<th style="background-color: #c9e1e4;">Article</th>
	<%if(isShowEcart){%>
			<th style="background-color: #c9e1e4;" width="100px">Qte. th&eacute;orique</th>
	<%} %>		
			<th style="background-color: #c9e1e4;" width="100px">Qte. r&eacute;el</th>
			
	<%if(isShowEcart){%>
			<th style="background-color: #c9e1e4;" width="100px">Prix U</th>
			<th style="background-color: #c9e1e4;" width="100px">Montant</th>
			
			<th style="background-color: #c9e1e4;" width="100px">Ecart (-)</th>
			<th style="background-color: #c9e1e4;" width="100px">Ecart % (-)</th>
			<th style="background-color: #c9e1e4;" width="100px">Montant (-)</th>
			
			<th style="background-color: #c9e1e4;" width="100px">Ecart (+)</th>
			<th style="background-color: #c9e1e4;" width="100px">Ecart % (+)</th>
			<th style="background-color: #c9e1e4;" width="100px">Montant (+)</th>
			
			<th style="background-color: #c9e1e4;">Motif &eacute;cart</th>
	<%} %>
		</tr>
	</thead>
	<tbody>
	
	<c:set var="encryptionUtil" value="<%=new EncryptionUtil() %>" />
	<c:set var="bigDecimalUtil" value="<%=new BigDecimalUtil() %>" />
	
	<c:set var="totalTheorique" value="${0 }" />
	<c:set var="totalReel" value="${0 }" />
	<c:set var="totalMttReel" value="${0 }" />
	<c:set var="totalEcartNeg" value="${0 }" />
	<c:set var="totalEcartPos" value="${0 }" />
	<c:set var="totalMttNeg" value="${0 }" />
	<c:set var="totalMttPos" value="${0 }" />
	
	<!-- Creation modification -->
	<c:forEach var="entry" items="${mapInventaire}">
			<c:set var="detail" value="${entry.value }" />
			
			<!-- Famille -->
			<c:if test="${detail.familleStr.size() > 0}">
				<c:forEach var="i" begin="0" end="${detail.familleStr.size()-1}">
					<c:if test="${empty oldfam or i>(oldfam.size()-1) or detail.familleStr.get(i).code != oldfam.get(i).code}">
					     <tr>
							<td colspan="<%=isShowEcart?"12":"2" %>" noresize="true" class="separator-group" style="padding-left: ${detail.familleStr.get(i).level<=1?0:detail.familleStr.get(i).level*10}px;">
								<span class="fa fa-fw fa-folder-open-o separator-icon"></span>  
								<%if(isCorrection){ %>
									<std:linkPopup classStyle="" value="${detail.familleStr.get(i).code}-${detail.familleStr.get(i).libelle}" action="stock.inventaire.loadSaisieInventaire" actionGroup="C" params="empl=${encryptionUtil.encrypt(detail.opc_inventaire.opc_emplacement.id) }&fam=${encryptionUtil.encrypt(detail.opc_article.opc_famille_stock.id) }" />
									<i class="fa fa-hand-o-up" style="color: orange;"></i>
								<%} else{ %>
									${detail.familleStr.get(i).code}-${detail.familleStr.get(i).libelle}
								<%} %>
							</td>
						</tr>
					</c:if>		
				</c:forEach>
			</c:if>
			<c:set var="oldfam" value="${detail.familleStr }"></c:set>
			
			<tr>
				<td align="left" >
					${detail.article_lib}
				</td>
		<%if(isShowEcart){%>		
				<td align="right" >
					<fmt:formatDecimal value="${detail.qte_theorique}" />
				</td>
		<%} %>		
				<td align="right">
					<span><fmt:formatDecimal value="${detail.qte_reel }"/></span>
					<c:if test="${not bigDecimalUtil.isZero(detail.qte_reel_0) }">
						<i class="fa fa-info-circle" title="Ancienne quantit&eacute; : ${bigDecimalUtil.formatNumberZeroBd(detail.qte_reel_0) }"></i>
					</c:if>
				</td>
	<%if(isShowEcart){%>
				<td align="right">	
					<span style="color: blue;"><fmt:formatDecimal value="${detail.opc_article.prix_achat_ttc }"/>Dhs</span>
				</td>
				
				<td align="right">	
					<span style="color: blue;font-weight: bold;"><fmt:formatDecimal value="${detail.qte_reel*detail.opc_article.prix_achat_ttc }"/>Dhs</span>
				</td>
				
				<td align="right">	
					<c:if test="${detail.qte_ecart < 0 }">
						<span style="color: red; font-weight: bold;"><fmt:formatDecimal value="${detail.qte_ecart}"/></span>
					</c:if>
				</td>
				<td align="right" >
					<c:if test="${detail.pourcent_ecart < 0 }">
						<span style="color: red; font-weight: bold;"><fmt:formatDecimal value="${detail.pourcent_ecart}"/>%</span>
					</c:if>
				</td>
				<td align="right" >
					<c:if test="${detail.qte_ecart < 0 }">
						<span style="color: red; font-weight: bold;"><fmt:formatDecimal value="${detail.opc_article.prix_achat_ttc*detail.qte_ecart }"/>Dhs</span>
					</c:if>
				</td>
					
				<td align="right">
					<c:if test="${detail.qte_ecart > 0 }">
						<span style="color: green; font-weight: bold;">+<fmt:formatDecimal value="${detail.qte_ecart}"/></span>
					</c:if>
				</td>
				<td align="right" >
					<c:if test="${detail.pourcent_ecart > 0 }">
						<span style="color: green; font-weight: bold;">+<fmt:formatDecimal value="${detail.pourcent_ecart}"/>%</span>
					</c:if>
				</td>
				<td align="right" >
					<c:if test="${detail.qte_ecart > 0 }">
						<span style="color: green; font-weight: bold;"><fmt:formatDecimal value="${detail.opc_article.prix_achat_ttc*detail.qte_ecart }"/>Dhs</span>
					</c:if>
				</td>
				
				<td>${detail.motif_ecart}</td>
				
				<c:set var="totalTheorique" value="${totalTheorique+detail.qte_theorique }" />
				<c:set var="totalReel" value="${totalReel+detail.qte_reel }" />
				<c:set var="totalMttReel" value="${totalMttReel+(detail.qte_reel*detail.opc_article.prix_achat_ttc) }" />
				
				<c:if test="${detail.qte_ecart < 0 }">
					<c:set var="totalEcartNeg" value="${totalEcartNeg+detail.qte_ecart }" />
					<c:set var="totalMttNeg" value="${totalMttNeg+(detail.opc_article.prix_achat_ttc*detail.qte_ecart) }" />
				</c:if>
				<c:if test="${detail.qte_ecart >= 0 }">
					<c:set var="totalEcartPos" value="${totalEcartPos+detail.qte_ecart }" />
					<c:set var="totalMttPos" value="${totalMttPos+(detail.opc_article.prix_achat_ttc*detail.qte_ecart) }" />
				</c:if>
		<%} %>			
			</tr>
		</c:forEach>
		<tr style="font-weight: bold;background-color: orange;">
			<td style="background-color: white;"></td>
			<td align="right"><fmt:formatDecimal value="${totalTheorique}"/></td>
			<td align="right"><fmt:formatDecimal value="${totalReel}"/></td>
			<td style="background-color: white;"></td>
			<td align="right"><fmt:formatDecimal value="${totalMttReel}"/>Dhs</td> 
			<td align="right" style="color: red;"><fmt:formatDecimal value="${totalEcartNeg}"/></td>
			<td style="background-color: white;"></td>
			<td align="right" style="color: red;"><fmt:formatDecimal value="${totalMttNeg}"/>Dhs</td>

			<td align="right" style="color: green;"><fmt:formatDecimal value="${totalEcartPos}"/></td>
			<td style="background-color: white;"></td>
			<td align="right" style="color: green;"><fmt:formatDecimal value="${totalMttPos}"/>Dhs</td>
			<td style="background-color: white;"></td>
		</tr>
	</tbody>
</table>