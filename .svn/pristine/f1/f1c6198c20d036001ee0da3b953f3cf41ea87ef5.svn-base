<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>

<div id="paramInitWizard" class="wizard" data-target="#paramInitWizard-steps">
	<ul class="steps">
<%
String tabName = (String)request.getAttribute("tabName");
String[] tabs = {"BO", "caisse", "cuisine", "ticket", "divers", "maintenance","emplacement","famille"};
String[] tabsLib = {"Backoffice", "Caisse", "Cuisine", "Ticket de caisse", "Divers", "Maintenance","Emplacement","Famille"};
// Calcul position
int currPosIdx = 0;
int idx = 1;
for(String tab : tabs){
	if(tab.equals(tabName)){
		currPosIdx = idx;
		break;
	}
	idx++;
}

idx = 1;
for(String tab : tabs){ 
	String classStl = "";
	if(tab.equals(tabName)){
		classStl = " class=\"active\"";
	} else if(idx < currPosIdx){
		classStl = " class=\"complete\"";
	}%>
	<li data-target="#paramInitWizardstep<%=idx %>" <%=classStl %>>
		<span class="step"><%=idx %></span><%=tabsLib[idx-1] %><span class="chevron"></span>
	</li>
<%
idx++;
} %>
	</ul>
	<div class="actions" id="paramInitWizard-actions">
		<c:if test="${pAction != null}">
			<button wact="${pAction}" class="btn btn-default btn-sm shiny btn-prev " params="sens=p">
				<i class="fa fa-angle-left"></i>Préc
			</button>
		</c:if>
		<c:if test="${sAction != null}">
			<button wact="${sAction}" class="btn btn-default btn-sm shiny btn-next " params="sens=s&tp=ST" data-last="Valider">
				Suiv<i class="fa fa-angle-right"></i>
			</button>
		</c:if>
		<c:if test="${sAction == null}">
			<std:button  classStyle="btn btn-default btn-sm shiny btn-next" action="caisse.wizardInstall.saveAll"  icon="fa fa-angle-right" value="valider"/>
		</c:if>
	</div>
</div>
