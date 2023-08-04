<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="fn" %>
<%@page errorPage="/commun/error.jsp" %>

	<std:form name="data_form">
	<html:ajaxExcludBlock>
	<br>
		<table border="0" width="100%">
			<tr>
				<td nowrap="nowrap">
				  <std:label valueKey="modification.user"/>
				</td>
				<td>
				   <std:text name="modification.user" type="string" />
				</td>
				<td width="10%"></td>
				<td nowrap="nowrap">
				   <std:label valueKey="modification.menu"/>
				</td>
				<td>
				   <std:text name="modification.menu" type="string" />
				</td>
			</tr>
			<tr>
				<td nowrap="nowrap">
				   <std:label valueKey="modification.date"/>
				</td>
				<td>
				   <std:date name="modification.date" />
				</td>
				<td width="10%"></td>
				<td nowrap="nowrap">
				   <std:label valueKey="modification.type_operation"/>
				</td>
				<td>
				   <std:select name="modification.type_operation" type="string" data="${list_operation}" width="250" />
				</td>
			</tr>
			<tr><td colspan="5">&nbsp;</td></tr>
			<tr>
				<td nowrap="nowrap" valign="top" colspan="5">
				  <std:label valueKey="modification.entity_detail"/>
				</td>
			</tr>
			<tr><td colspan="5"><hr></td></tr>
			<tr>	
				<td colspan="5">
					<table>
						<c:forEach items="${listModification }" var="modif">
							<tr>
								<td>
									<fieldset legende="Donn&eacute;e avant modification">
										<span>
					   						${modif.entity_detail_old }
					   					</span>
									</fieldset>
								</td>
								<td width="1%">&nbsp;</td>
								<td>
									Donn&eacute;e apr&egrave;s modification
									<br>
										<span>
					   						${modif.entity_detail }
					   					</span>
								</td>
							</tr>
						</c:forEach>
					</table>
				</td>
			</tr>
		</table>
		<br><br>
		<br><br>
		</html:ajaxExcludBlock>
	</std:form>
