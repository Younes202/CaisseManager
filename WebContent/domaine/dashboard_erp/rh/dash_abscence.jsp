<%-- <%@page import="java.util.List"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
List<Object[]> list_abscence = (List<Object[]>)request.getAttribute("list_abscence");
%>

<div class="row">
        <div class="form-group">
        	<std:label classStyle="control-label col-md-1" value="De" addSeparator="false"/>
            <div class="col-md-3">
                 <std:date name="dateDebut_abs" value="${dateDebut_abs }"/>
            </div>
            <div class="col-md-3" style="text-align: center;">
            	<std:link action="dash.dashRH.init_abscence" params="prev=1" icon="fa fa-arrow-circle-left" tooltip="Mois pr&eacute;c&eacute;dent" targetDiv="dash_abscence"/>
            	<std:link action="dash.dashRH.init_abscence" params="next=1" icon="fa fa-arrow-circle-right" tooltip="Mois suivant" targetDiv="dash_abscence"/>
            </div>
            
            <std:label classStyle="control-label col-md-1" value="A" addSeparator="false"/>
            <div class="col-md-3">
                 <std:date name="dateFin_abs" value="${dateFin_abs }"/>
            </div>
       </div>
   </div>
		<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 450px;">
			<%if(list_abscence.size() > 0){ %>
				<table class="table table-condensed table-striped table-bordered table-hover no-margin" style="font-size: 12px !important;">
					<thead>
						<tr>
							<th>Employ&eacute;</th>
							<th width="200">Jours cong&eacute;s non pay&eacute;s</th>
						</tr>
					</thead>
					<tbody>
						<%for(Object[] data : list_abscence){ %>
							<tr>
								<td><%=data[1] %></td>
								<td align="right"><%=data[0] %></td>
							</tr>
						<%} %>
					</tbody>
				</table>
			<%} else{%>   
	            <h6 class="left-align-text"><span style="color: green;">Aucune abscence.</span></h6>
	        <%} %>
		</div>

 --%>