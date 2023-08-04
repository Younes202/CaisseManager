<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="java.util.Date"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<%

Map<String, Map<String, List<Object[]>>> MapOfMaps = (Map<String, Map<String, List<Object[]>>>)request.getAttribute("MaplistEmploye");
List<Object[]> listEmpNombreApp = (List<Object[]>)request.getAttribute("listEmpNombreApp");
int allUnconfirmedEmp = (int)request.getAttribute("allUnconfirmedEmp");
int allConfirmedEmp = (int)request.getAttribute("allConfirmedEmp");


%>

<style>
 
  .red {
  color: red !important;
}
.circle {
    width: 15px;
    height: 15px;
    border-radius: 50%;
    background-color: gray;
    display: inline-block;
    margin-right: 3px;
  }

.circle.red {
    background-color: red;
  }
 .borderStyle{
         border: 3px solid #eeeeee;
 
 }
 
 
 .firstRow{
 --tw-bg-opacity: 1;
   font-weight: bolder;
   color: white;
   font-size: x-large;
   background-color: #afb0b1;
   border: none;
   text-align: center;
   padding: 0 40px;
 
 }

</style>


 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion des personnels</li>
         <li>Fiche des employ&eacute;s</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
<std:form name="search-form">
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
          <std:link actionGroup="C" classStyle="btn btn-default" style="float:left;" action="pers.employe.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
          |
          <std:linkPopup noJsValidate="true" actionGroup="C" style='margin-left: 10px;' action="admin.dataForm.work_find" icon="fa fa-cogs" tooltip="Données formulaire" value="Formulaire" params="tp=EMPLOYE" />
      </div>
      <!--Header Buttons-->
      <jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
      <!--Header Buttons End-->
  </div>
  <!-- /Page Header -->

<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
	
	<c:set var="encryptionUtil" value="<%=new EncryptionUtil() %>" />
	
	<!-- row -->
	<div class="row">
		
		<!-- Liste des employes -->
		<cplx:table name="list_employe" checkable="false" autoHeight="true" showDataState="true" transitionType="simple" width="100%" titleKey="employe.list" initAction="pers.employe.work_find">
			<cplx:header>
				<cplx:th type="empty" />
				<cplx:th type="long[]" value="Famille" field="article.opc_famille.id" groupValues="${listeFamille }" groupKey="id" groupLabel="libelle" width="0" filterOnly="true"/><!-- Filter only -->
<%-- 				<cplx:th type="string" valueKey="employe.numero" field="employe.numero" width="100" /> --%>
				<cplx:th type="string" value="Nom et pr&eacute;nom" field="employe.nom" width="150" filtrable="false" />
				<cplx:th type="string" valueKey="employe.nom"    field="employe.nom"    filterOnly="true"/>
				<cplx:th type="string" valueKey="employe.prenom" field="employe.prenom" filterOnly="true"/>
				<cplx:th type="long[]" valueKey="employe.poste" field="employe.opc_poste.id" width="150" groupValues="${listPoste }" groupKey="id" groupLabel="intitule"/>
				<cplx:th type="date" valueKey="employe.date_entree" field="employe.date_entree" width="100" />
				
				<cplx:th type="string"  value="Type Contrat" field="employe.type_contrat_enum.libelle"  />
				<cplx:th type="string"   value="Tele" field="employe.telephone"  />
				<cplx:th type="string"   value="Gendre" field="employe.civilite"  />
				<cplx:th type="string"   value="Situation" field="employe.situation_fam"  />
				<cplx:th type="Date"   value="Date Naissance" field="date_nissance"  />
				<cplx:th type="string"   value="Appercisiation" field="appercisiation"  />
				<cplx:th type="empty" width="80" />
			</cplx:header>
			<cplx:body>
					<c:set var="currDate" value="<%=new Date() %>"/>
					<c:set var="dateUtil" value="<%=new DateUtil() %>"/>
					<c:set var="oldfam" value="${null }"></c:set>
					<cplx:tr>
					  <td colspan="4" style="--tw-bg-opacity: 1;
											    font-weight: bolder;
											    color: white;
											    font-size: initial;
											    background-color: #525659;
											    border: none;
											    text-align: center;
											    padding: 0 40px;">Total Employés Confirmé: <%=allConfirmedEmp %></td>
					    <td colspan="4" class="expandableCell"  style="--tw-bg-opacity: 1;
											    font-weight: bolder;
											    color: white;
											    font-size: x-large;
											    background-color: #525659;
											    border: none;
											    text-align: center;
											    ">
						      
					       Total <%=allConfirmedEmp+allUnconfirmedEmp %>
					    </td>
					    <td colspan="4" style="--tw-bg-opacity: 1;
											    font-weight: bolder;
											    color: white;
											    font-size: initial;
											    background-color: #525659;
											    border: none;
											    text-align: center;
											    ">Total Employés Non Confirmé: <%=allUnconfirmedEmp %></td>
					</cplx:tr >
					<% 
					
					for (Map.Entry<String, Map<String, List<Object[]>>> outerEntry : MapOfMaps.entrySet()) {
					    String outerKey = outerEntry.getKey();
					    if ("TotalEmp".equals(outerKey)) {
					        // Skip this entry and continue with the next iteration of the loop
					        continue;
					    }
					    Map<String, List<Object[]>> innerMap = outerEntry.getValue();
					    
					    int confirmedWorkersByFamille = 1;
					    int unConfirmedWorkersByFamille = 1;
					    
					    String[] split_values = outerKey.split("-");
					  %>
					  
					  <cplx:tr>
					  <td colspan="4" style="--tw-bg-opacity: 1;
											    font-weight: bolder;
											    color: white;
											    font-size: large;
											    background-color: #afb0b1;
											    border: none;
											    text-align: center;
											    padding: 0 40px;">Confirmé: <%=split_values[1] %></td>
					    <td colspan="4" class="expandableCell"  style="--tw-bg-opacity: 1;
											    font-weight: bolder;
											    color: white;
											    font-size: x-large;
											    background-color: #afb0b1;
											    border: none;
											    text-align: center;
											    ">
						      
					       <%=split_values[0] %>
					    </td>
					    <td colspan="4" style="--tw-bg-opacity: 1;
											    font-weight: bolder;
											    color: white;
											    font-size: large;
											    background-color: #afb0b1;
											    border: none;
											    text-align: center;
											    ">Non Confirmé: <%=split_values[2] %></td>
					</cplx:tr >
					  <%
					    // Process outerKey and innerMap
					    
					    for (Map.Entry<String, List<Object[]>> innerEntry : innerMap.entrySet()) {
					        String innerKey = innerEntry.getKey();
					        List<Object[]> list_employe_map = innerEntry.getValue();
						%>
					        
						 <cplx:tr>
						 	
						   <td class="expandableCell" colspan="12" style="--tw-bg-opacity: 1;
										    background-color: #dbddde;
										    font-weight: bolder;
										    color: 616161;
										    font-size: initial;
										    text-align: center;
										    border: none;">
						        <% 
						        int index = innerKey.indexOf("-");
						        String substring = (index != -1 && index < innerKey.length() - 1) ? innerKey.substring(index + 1) : "";
						        out.print(substring);
						        %>
						    </td>
						    
						</cplx:tr>
					
				<c:forEach items="<%=list_employe_map %>" var="employe">
					<c:set var="isSortie" value="${employe.date_sortie != null && employe.getDate_sortie().compareTo(currDate) <= 0 }" />
				
				<c:if test="${employe.familleStr.size() > 0}">
					<%-- <c:forEach var="i" begin="0" end="${employe.familleStr.size()-1}">
						<c:if test="${empty oldfam or i>(oldfam.size()-1) or employe.familleStr.get(i).code != oldfam.get(i).code}">
						     <tr>
								<td colspan="${((empty isEditable or isEditable)?maxCol:minCol)+listDataValueForm.size()}" noresize="true" class="separator-group" style="padding-left: ${employe.familleStr.get(i).level*10}px;">
									<span class="fa fa-fw fa-folder-open-o separator-icon"></span> ${employe.familleStr.get(i).code}-${employe.familleStr.get(i).libelle}
								</td>
							</tr>
						</c:if>		
					</c:forEach> --%>
				</c:if>
				<c:set var="oldfam" value="${employe.familleStr }"></c:set>
				
					<cplx:tr workId="${employe.id }" style="${employe.is_disable?'text-decoration: line-through;':'' }">
						<cplx:td>
							<work:edit-link />
						</cplx:td>
						<%-- <cplx:td value="${employe.numero}">
							<c:if test="${isSortie }">
								<i class="fa fa-fw fa-child" style="color: red;" title="Sortie le ${dateUtil.dateToString(employe.date_sortie) }"></i>							
							</c:if>
							<img alt="" src='resourcesCtrl?elmnt=${encryptionUtil.encrypt(employe.getId().toString())}&path=employe&rdm=${employe.date_maj.getTime()}' width='24' height='24' onerror="this.onerror=null;this.remove();"/>
						</cplx:td> --%>
						<cplx:td classStyle="borderStyle" style="    font-weight: 700;font-size: large;" value="${employe.nom} ${employe.prenom}"></cplx:td>
						<cplx:td classStyle="borderStyle" value="${employe.opc_poste.intitule}"></cplx:td>
						<cplx:td classStyle="borderStyle" align="center" value="${adresse.date_entree }"></cplx:td>
						<cplx:td classStyle="borderStyle" align="center" value="${employe.type_contrat_enum.libelle }"></cplx:td>
						<cplx:td classStyle="borderStyle" align="center" value="${employe.telephone }"></cplx:td>
						<cplx:td classStyle="borderStyle" align="center" >
						    <c:choose>
						        <c:when test="${employe.civilite eq 'H'}">
						            Homme
						        </c:when>
						        <c:when test="${employe.civilite eq 'F'}">
						            Femme
						        </c:when>
						    </c:choose>
						</cplx:td>
						<cplx:td classStyle="borderStyle" align="center" >
						    <c:choose>
						        <c:when test="${employe.situation_fam eq 'CJ'}">
						            Conjoint
						        </c:when>
						        <c:when test="${employe.situation_fam eq 'DJ'}">
						            Disjoint
						        </c:when>
						    </c:choose>
						</cplx:td>
						<cplx:td classStyle="borderStyle" align="center" value="${employe.date_naissance }"></cplx:td>
						<cplx:td  classStyle="borderStyle" >
						<c:forEach var="empNombreApp" items="${listEmpNombreApp}">
					  <c:choose>
					    <c:when test="${empNombreApp[0] == employe.id}">
					        <div>
					            <i class="circle ${empNombreApp[1] >= 1 ? 'red' : ''}" style="color:#53a93f "></i>
					            <i class="circle ${empNombreApp[1] >= 2 ? 'red' : ''}" style="color:#53a93f "></i>
					            <i class="circle ${empNombreApp[1] >= 3 ? 'red' : ''}" style="color:#53a93f "></i>
					            <i class="circle ${empNombreApp[1] >= 4 ? 'red' : ''}" style="color:#53a93f "></i>
					        </div>
					    </c:when>
					  </c:choose>
						</c:forEach>
						  </cplx:td>
						
						
						<cplx:td align="center">
							<std:link action="pers.employe.desactiver" workId="${employe.id }" actionGroup="C" style="color:${employe.is_disable?'green':'orange'};" icon="fa ${employe.is_disable?'fa-unlock':'fa-lock' }" classStyle="btn btn-sm btn-${employe.is_disable?'warning':'succes'}" tooltip="${employe.is_disable?'Activer':'Désactiver'}" />
							<work:delete-link />
						</cplx:td>
					</cplx:tr>
				</c:forEach>
				<%}}%>
			</cplx:body>
		</cplx:table>
	</div>
	<!-- end widget content -->

</div>
<!-- end widget div -->
</std:form>
