<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<%
	int colLab = (request.getAttribute("colLab") == null ? 2 : (Integer)request.getAttribute("colLab"));
	int colInput = (request.getAttribute("colInput") == null ? 4 : (Integer)request.getAttribute("colInput"));
	String isForceWriter = (StringUtil.isTrue(""+request.getAttribute("forceW")) ? "true" : "false");
%>

<c:set var="encodeService" value="<%=new EncryptionUtil() %>" />

<div class="row">
	<c:forEach items="${listDataValue }" var="data">
		<c:set var="maxLength" value="${(data.opc_data_form.max_length==null or data.opc_data_form.max_length==0)?255:data.opc_data_form.max_length }" />
		<c:set var="dataType" value="${data.opc_data_form.data_type}" />
		<std:hidden name="eaiid_${data.opc_data_form.id}" value="${encodeService.encrypt(data.opc_data_form.id) }" />
		
		<c:choose>
			<c:when test="${dataType == 'TITRE' }">	
				<div class="row">
					<h3>${data.opc_data_form.data_label }</h3>
				</div>
			</c:when>
			<c:otherwise>
				<div class="form-group">
					<%if(colLab > 0){ %>
						<std:label classStyle='<%="control-label col-md-"+colLab%>' value="${data.opc_data_form.data_label }" />
					<%} %>
					<div class="col-md-<%=colInput%>">
						<c:choose>
							<c:when test="${dataType == 'STRING' }">
								<std:text forceWriten="<%=isForceWriter %>" name="data_value_${data.opc_data_form.id }" style="${data.opc_data_form.data_style }" type="string" placeholder="${data.opc_data_form.data_label }" maxlength="${maxLength }" value="${data.data_value }" />
							</c:when>
							<c:when test="${dataType == 'ENUM' }">
								<c:set var="enumName" value="listEnumValue_${data.opc_data_form.id }" />
								<c:set var="listEnumValue" value='${request.getAttribute(enumName)}' />
								<std:select forceWriten="<%=isForceWriter %>" name="data_value_${data.opc_data_form.id }" style="${data.opc_data_form.data_style }" type="long" data="${listEnumValue}" placeholder="${data.opc_data_form.data_label }" value="${data.data_value }" />	
							</c:when>
							<c:when test="${dataType == 'TEXT' }">
								<std:textarea forceWriten="<%=isForceWriter %>" name="data_value_${data.opc_data_form.id }" style="${data.opc_data_form.data_style }" maxlength="255" placeholder="${data.opc_data_form.data_label }" value="${data.data_value }" />	
							</c:when>
							<c:when test="${dataType == 'DATE' }">
								<std:date forceWriten="<%=isForceWriter %>" name="data_value_${data.opc_data_form.id }" style="${data.opc_data_form.data_style }" placeholder="${data.opc_data_form.data_label }" value="${data.data_value }" />
							</c:when>
							<c:when test="${dataType == 'DATETIME' }">
								<std:date forceWriten="<%=isForceWriter %>" name="data_value_${data.opc_data_form.id }" style="${data.opc_data_form.data_style }" placeholder="${data.opc_data_form.data_label }" value="${data.data_value }" />
							</c:when>
							<c:when test="${dataType == 'BOOLEAN' }">
								<std:checkbox forceWriten="<%=isForceWriter %>" name="data_value_${data.opc_data_form.id }" checked="${ data.data_value?'true':'false'}" />
							</c:when>
							<c:when test="${dataType == 'LONG' }">
								<std:text forceWriten="<%=isForceWriter %>" name="data_value_${data.opc_data_form.id }" style="width: 150px;${data.opc_data_form.data_style }" type="long" placeholder="${data.opc_data_form.data_label }" maxlength="${maxLength }" value="${data.data_value }" />
							</c:when>
							<c:when test="${dataType == 'DECIMAL' }">
								<std:text forceWriten="<%=isForceWriter %>" name="data_value_${data.opc_data_form.id }" style="width: 150px;${data.opc_data_form.data_style }" type="decimal" placeholder="${data.opc_data_form.data_label }" maxlength="${maxLength }" value="${data.data_value }" />
							</c:when>
							
						</c:choose>
					</div>
				</div>	
			</c:otherwise>	
		</c:choose>	
	</c:forEach>
</div>							
