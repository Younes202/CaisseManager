<%@page import="java.util.List"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
List<Object[]> list_pointage = (List<Object[]>)request.getAttribute("list_pointage");
%>

<div class="row">
        <div class="form-group">
        	<std:label classStyle="control-label col-md-1" value="De" addSeparator="false"/>
            <div class="col-md-3">
                 <std:date name="dateDebut_pnt" value="${dateDebut_pnt }"/>
            </div>
            <div class="col-md-3" style="text-align: center;">
            	<std:link action="dash.dashRH.init_pointage" params="prev=1" icon="fa fa-arrow-circle-left" tooltip="Mois pr&eacute;c&eacute;dent" targetDiv="dash_pointage"/>
            	<std:link action="dash.dashRH.init_pointage" params="next=1" icon="fa fa-arrow-circle-right" tooltip="Mois suivant" targetDiv="dash_pointage"/>
            </div>
            
            <std:label classStyle="control-label col-md-1" value="A" addSeparator="false"/>
            <div class="col-md-3">
                 <std:date name="dateFin_pnt" value="${dateFin_pnt }"/>
            </div>
       </div>
   </div>
		<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 450px;">
			
		</div>

