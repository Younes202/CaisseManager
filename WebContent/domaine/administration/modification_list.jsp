<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<script type="text/javascript">
$(function() {
	$("#modification\\.menu").select2({
		allowClear: true,
		formatResult: function (state) {
			//if (!state.id) return state.text;
			 //if (state.id.indexOf(".")==-1) {
				 var icon = $(state.element).data('image');
				 
				 if(!icon || icon == ''){
					 return '<span><img src="resources/framework/img/tree/ltDoc.gif" />' + state.text + '</span>';
				 } else{
					 return '<span><img src="resources/framework/img/tree/custom/'+icon+'" />' + state.text + '</span>';
				 }	
			// } else{
			//	 return '<span><img src="resources/framework/img/tree/ltDoc.gif" />' + state.text + '</span>';
			 //}
		},
		formatSelection: formatState
	});
	
	function formatState(state) {
		
	};
});	
</script>

	<std:form name="data_bean">
	<html:ajaxExcludBlock>
		 <table cellpadding="1" cellspacing="0" border="0">
	  	 <tr>
			<td nowrap="nowrap">
                <std:label valueKey="modification.date_debut"/>
			</td>
			<td>
			   <std:date name="modification.date_debut" />
			</td>
			<td width="15%"></td>
			<td nowrap="nowrap">
			    <std:label valueKey="modification.date_fin"/>
			</td>
			<td>
			    <std:date name="modification.date_fin" />
			</td>
		</tr>
		 <tr>
			<td nowrap="nowrap">
                <std:label valueKey="modification.user"/>
			</td>
			<td>
			   <std:select name="modification.user" type="string" data="${list_user}" key="login" labels="fname;sname" width="250" />
			</td>
			<td width="15%"></td>
			<td nowrap="nowrap">
			    <std:label valueKey="modification.menu"/>
			</td>
			<td>
			    <c:set var="cpt" value="${0 }"></c:set>
			    <select id="modification.menu" name="modification.menu" class="select" style="width:300px;" multiple>
				    <c:forEach items="${ list_menu}" var="menu">
				    	<c:if test="${cpt > 0 }">
						    	<c:if test="${cpt == 1 and menu.rightBorn-menu.leftBorn>1 }">
						    		<optgroup label="${menu.linkText }">
						    	</c:if>
						    
						    	<c:if test="${cpt != 1 and menu.rightBorn-menu.leftBorn>1 }">
						    		</optgroup>
						    	 	<optgroup  data-image="${menu.icon }" label="${menu.linkText }">
						    	</c:if>
								
								<c:if test="${menu.rightBorn-menu.leftBorn == 1 }">
						    		<option value="${menu.compositId }" data-image="${menu.icon }">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${menu.linkText }</option>
						    	</c:if>	
						</c:if>
						
						<c:set var="cpt" value="${cpt+1 }"></c:set>				    	
				    </c:forEach>
   			    	</optgroup>
			    </select>
			</td>
		</tr>
		 <tr>
			<td nowrap="nowrap">
                <std:label valueKey="modification.type_operation"/>
			</td>
			<td colspan="4">
			   <std:select name="modification.type_operation" type="string" data="${list_operation}" width="250" />
			</td>
		</tr>
	</table>
	<br>

	<br>
		<table width="100%"><tr><td align="center">
			<std:button action="admin.modification.work_find" value="Rechercher" />
		</td></tr></table>
	<br>
	</html:ajaxExcludBlock>
	<!-- Liste des modifications -->
	<cplx:table name="list_modification" transitionType="simple" width="100%" titleKey="modification.list" checkable="false" initAction="">
		<cplx:header>
			<cplx:th type="dateTime" valueKey="modification.date" field="modification.date" width="150"/>
			<cplx:th type="string" value="Libell&eacute;" field="modification.libelle"/>
			<cplx:th type="string" valueKey="modification.menu" field="modification.menu" groupValues="${menu_array}" width="180"/>
			<cplx:th type="string" valueKey="modification.user" field="modification.user" width="180"/>
			<cplx:th type="string" valueKey="modification.type_operation" field="modification.type_operation" groupValues="${list_operation }" width="110" />
		</cplx:header>
		<cplx:body>
			<c:forEach items="${listModification }" var="modification">
				<cplx:tr workId="${modification.id }">
					<cplx:td value="${modification.date}"></cplx:td>
					<cplx:td value="${modification.libelle}"></cplx:td>
					<cplx:td value="${modification.menu}"></cplx:td>
					<cplx:td value="${modification.user}"></cplx:td>
					<cplx:td value="${modification.type_operation}"></cplx:td>
				</cplx:tr>
			</c:forEach>
		</cplx:body>
	</cplx:table>

	</std:form>
