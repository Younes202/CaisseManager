<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>

	<c:set var="isPassed" value="${false}"/>
	<c:set var="totalDebit" value="${0}" />
	<c:set var="totalCredit" value="${0}" />
	<c:set var="oldGroup" value="${null }" />
	<c:set var="oldGroup2" value="${null }" />
		
	<table width="100%" id="journalTab" style="margin-left: 15px;width: 97%;border: 1px solid #eee;">
		<tr>
			<th style="background-color: #D8D8D8" align="left">Compte</th>
			<th style="background-color: #D8D8D8" align="left">Intitul&eacute; du compte</th>
			<th style="background-color: #D8D8D8" align="left">Libell&eacute;</th>
			<th style="background-color: #D8D8D8" align="right">D&eacute;bit</th>
			<th style="background-color: #D8D8D8" align="right">Cr&eacute;dit</th> 
		</tr>	
		
		<c:if test="${listEcriture == null or listEcriture.size() == 0 }">
			<tr align="center"><td colspan="5">Aucune &eacute;criture &agrave; afficher.</td></tr>
		</c:if>
		
		<c:forEach items="${listEcriture }" var="ecriture">
			<c:set var="currentGroup" value="${ecriture.date_mouvement}"/>
			<c:set var="currentGroup2" value="${ecriture.groupe}-${ecriture.source}-${ecriture.elementId}"/>
							
			<c:if test="${not empty oldGroup && currentGroup != oldGroup }">
				<c:set var="isPassed" value="${true}"/>
			</c:if>
							
			<c:if test="${not empty oldGroup2 && currentGroup2 != oldGroup2 }">
				<tr>
					<td style="background-color: #f4f4f4;" colspan="3"><b>Total des &eacute;critures : </b></td>
					<td align="right" style="background-color: #f4f4f4;"><b><fmt:formatDecimal zeroVal="false" value="${totalDebit==0?'':totalDebit }"/></b></td>
					<td align="right" style="background-color: #f4f4f4;"><b><fmt:formatDecimal zeroVal="false" value="${totalCredit==0?'':totalCredit }"/></b></td>
				</tr>
				
				<c:set var="totalDebit" value="${0 }"/>
				<c:set var="totalCredit" value="${0 }"/>
			</c:if>
			
							
			<c:if test="${empty oldGroup or isPassed }">
				<c:set var="isPassed" value="${false}"/>	
				<tr><td style="background-color: #CCFFBB;" colspan="5" align="center"><b>Date d'&eacute;criture : <fmt:formatDate value="${ecriture.date_mouvement }"/></b></td></tr>
			</c:if>
			
			<!-- Total montant --> 
			<c:set var="totalDebit" value="${totalDebit+(ecriture.sens=='D' ? ecriture.montant : 0) }"/>
			<c:set var="totalCredit" value="${totalCredit+(ecriture.sens=='C' ? ecriture.montant : 0) }"/>
			
			<tr>
				<td style="font-weight: bold;">${ecriture.opc_compte.getCode()}</td>
				<td>${ecriture.opc_compte.getLibelle()}</td>
				<td>${ecriture.libelle}</td>
				<!-- Debit et credit -->
				<td style="color: red;" align="right"><fmt:formatDecimal zeroVal="false" value="${(ecriture.sens=='D' && ecriture.montant!=0) ? ecriture.montant : null}"/></td>
				<td style="color: green;" align="right"><fmt:formatDecimal zeroVal="false" value="${(ecriture.sens=='C' && ecriture.montant != 0) ? ecriture.montant : null}"/></td>
			</tr>
			
			<c:set var="oldGroup" value="${currentGroup }"/>
			<c:set var="oldGroup2" value="${currentGroup2 }"/> 
		</c:forEach>
		<!-- Last line -->
		<c:if test="${not empty oldGroup2}">  
			<tr>
				<td align="right" style="background-color: #f4f4f4;" colspan="3"><b>TOTAL ECRITURES : </b></td>
				<td align="right" style="background-color: #f4f4f4;color: red;"><b><fmt:formatDecimal zeroVal="false" value="${totalDebit==0?'':totalDebit }"/></b></td>
				<td align="right" style="background-color: #f4f4f4;color: green;"><b><fmt:formatDecimal zeroVal="false" value="${totalCredit==0?'':totalCredit }"/></b></td>
			</tr>
		</c:if>
	</table>