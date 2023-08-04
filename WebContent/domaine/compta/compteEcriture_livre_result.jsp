<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>


<c:set var="isPassed" value="${false}"/>
	<c:set var="totalDebit" value="${0}" />
	<c:set var="totalCredit" value="${0}" />
	<c:set var="oldGroup" value="${null }" />
		
	<table width="100%" id="livreTab" style="margin-left: 15px;width: 97%;border: 1px solid #eee;">
		<tr>
			<th style="background-color: #D8D8D8" align="left">Date</th>
			<th style="background-color: #D8D8D8" align="left">Libell&eacute; de l'&eacute;criture</th>
			<th style="background-color: #D8D8D8" align="right">Montant d&eacute;bit</th>
			<th style="background-color: #D8D8D8" align="right">Montant cr&eacute;dit</th>
			<th style="background-color: #D8D8D8" align="right">Solde</th>
		</tr>	
		
		<c:if test="${listEcriture == null or listEcriture.size() == 0 }">
			<tr align="center"><td colspan="5">Aucune &eacute;criture &agrave; afficher.</td></tr>
		</c:if>
		
		<c:forEach items="${listEcriture }" var="ecriture">
			<c:set var="currentGroup" value="${ecriture.opc_compte.code}"/>

			<c:if test="${not empty oldGroup && currentGroup != oldGroup }">
				<c:set var="isPassed" value="${true}"/>
				<tr>
					<td align="right" style="background-color: #f4f4f4;" colspan="2"><b>TOTAL COMPTE ${lastCompte } : </b></td>
					<td align="right" style="background-color: #f4f4f4;color: red;"><b><fmt:formatDecimal zeroVal="false" value="${totalDebit==0?null:totalDebit }"/></b></td>
					<td align="right" style="background-color: #f4f4f4;color: green;"><b><fmt:formatDecimal zeroVal="false" value="${totalCredit==0?null:totalCredit }"/></b></td>
					<td align="right" style="background-color: #f4f4f4;"><b><fmt:formatDecimal zeroVal="false" value="${totalCredit-totalDebit }"/></b></td>
				</tr>
				<c:set var="totalDebit" value="${0 }"/>
				<c:set var="totalCredit" value="${0 }"/>
			</c:if>
			
			<c:if test="${empty oldGroup or isPassed }"> 
				<c:set var="isPassed" value="${false}"/>
				<c:set var="totalDebit" value="${mapSoldeInitial.get(ecriture.opc_compte.code).intValue()<0?mapSoldeInitial.get(ecriture.opc_compte.code).abs():0}"/>
				<c:set var="totalCredit" value="${mapSoldeInitial.get(ecriture.opc_compte.code).intValue()>0?mapSoldeInitial.get(ecriture.opc_compte.code).abs():0}"/>
				
				<tr>
					<td style="background-color: #CCFFBB;" colspan="4" align="left"><b style="font-size: 14px;">${ecriture.opc_compte.code } - ${ecriture.opc_compte.libelle }</b></td>
					<%--Solde initial --%>					
					<td style="background-color: #CCFFBB;" align="right"><fmt:formatDecimal zeroVal="false" value="${mapSoldeInitial.get(ecriture.opc_compte.code) }"/></td>
				</tr>
			</c:if>
			
			<%-- Total montant --%>
			<c:set var="totalDebit" value="${totalDebit+(ecriture.sens=='D' ? ecriture.montant : 0) }"/>
			<c:set var="totalCredit" value="${totalCredit+(ecriture.sens=='C' ? ecriture.montant : 0) }"/>
			
			<%-- ********************************** --%>
			
			<tr>
				<td style="font-weight: bold;"><fmt:formatDate value="${ecriture.date_mouvement }"/></td> 
				<td>${ecriture.libelle}</td>
				<!-- Debit et credit -->
				<td align="right" style="color: red;"><fmt:formatDecimal zeroVal="false" value="${ecriture.sens=='D' ? ecriture.montant : null}"/></td>
				<td align="right" style="color: green;"><fmt:formatDecimal value="${ecriture.sens=='C' ? ecriture.montant : null}"/></td>
				<td align="right"><fmt:formatDecimal value="${totalCredit-totalDebit}"/></td> 
			</tr>
		
			<c:set var="oldGroup" value="${currentGroup }"/>
			<c:set var="lastCompte" value="${ecriture.opc_compte.code }"/>
		</c:forEach>
		
		<!-- Last line -->
		<c:if test="${not empty oldGroup and listEcriture.size() > 0}">
			<tr>
				<td align="right" style="background-color: #f4f4f4;" colspan="2"><b>TOTAL COMPTE ${lastCompte }: </b></td>
				<td align="right" style="background-color: #f4f4f4;color: red;"><b><fmt:formatDecimal zeroVal="false" value="${totalDebit==0?null:totalDebit }" /></b></td>
				<td align="right" style="background-color: #f4f4f4;color: green;"><b><fmt:formatDecimal zeroVal="false" value="${totalCredit==0?null:totalCredit }"/></b></td>
				<td align="right" style="background-color: #f4f4f4;"><b><fmt:formatDecimal zeroVal="false" value="${totalCredit-totalDebit }"/></b></td>
			</tr>
		</c:if>
	</table>
