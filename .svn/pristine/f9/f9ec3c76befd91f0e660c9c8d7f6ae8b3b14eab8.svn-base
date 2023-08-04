<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<script type="text/javascript">
	$(document).ready(function (){
		$(document).off('click', "a[id^='lnk_det']");
		$(document).on('click', "a[id^='lnk_det']", function(){
			setTrContent($(this).attr("curr"), "<%=EncryptionUtil.encrypt("stock.mouvement.editTrEtatArticle")%>");
		});
	});
</script>

 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion de stock</li>
         <li>Etat des stocks</li>
         <li class="active">Recherche</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->
  <!-- Page Header -->
  <div class="page-header position-relative">
  	<%if(ControllerUtil.getMenuAttribute("IS_TTL_MTT", request) == null) {%>
  		<std:link style="margin-top: 5px;" actionGroup="C" classStyle="btn btn-primary" action="stock.mouvement.etat_article_total" icon="fa-3x fa-barcode" tooltip="Afficher les totaux" value="Afficher le total" />
  	<%} else{ %>
  		<std:link style="margin-top: 5px;" actionGroup="C" classStyle="btn btn-default" action="stock.mouvement.etat_article_total" icon="fa-3x fa-barcode" tooltip="Cacher les totaux" value="Cacher le total" />
  	<%} %>
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


<c:set var="bigDecimalUtil" value="<%=new BigDecimalUtil() %>"></c:set>

	<!-- row -->
	<div class="row">
<std:form name="search-form">
	<!-- Liste des articles -->
	<cplx:table name="list_etat_article" exportable="false" transitionType="simple" width="100%" titleKey="article.list" initAction="stock.mouvement.etat_article_work_find" autoHeight="true" checkable="false" sortable="false">
		<cplx:header>
			<cplx:th type="long[]" value="Famille" field="etatArticle.opc_article.opc_famille_stock.id" groupValues="${listeFaimlle }" groupKey="id" groupLabel="libelle" width="0" filterOnly="true"/>
			<cplx:th type="long[]" value="Article" field="etatArticle.opc_article.id" autocompleteAct="stock.mouvement.getListArticles" groupKey="id" groupLabel="code;'-';libelle"/>
			<cplx:th type="long[]" value="Emplacement" field="etatArticle.opc_emplacement.id" groupValues="${listEmplacement }" groupKey="id" groupLabel="titre"/>
			<cplx:th type="decimal" value="Qte restante" width="100" filtrable="false"/>
			<cplx:th type="decimal" value="Valeur HT" width="120" filtrable="false"/>
			<cplx:th type="decimal" value="Valeur TTC" width="120" filtrable="false"/>
			<cplx:th type="empty" width="60"/>
		</cplx:header>
		<cplx:body>
			<c:set var="oldfam" value="${null }"></c:set>
			<c:set var="oldArt" value="${null }"></c:set>
			
			<c:forEach items="${listEtatArt }" var="etatArticle">
				
				<!-- FAMILLE -->
				<c:set var="isFamChanged" value="${false }" />
				<c:if test="${etatArticle.opc_article.familleStr.size() > 0}">
					<c:forEach var="i" begin="0" end="${etatArticle.opc_article.familleStr.size()-1}">
						<c:if test="${empty oldfam or i>(oldfam.size()-1) or etatArticle.opc_article.familleStr.get(i).code != oldfam.get(i).code}">
						     <tr>
								<td colspan="7" noresize="true" class="separator-group" style="padding-left: ${etatArticle.opc_article.familleStr.get(i).level<=1?0:etatArticle.opc_article.familleStr.get(i).level*10}px;">
									<span class="fa fa-fw fa-folder-open-o separator-icon"></span> ${etatArticle.opc_article.familleStr.get(i).code}-${etatArticle.opc_article.familleStr.get(i).libelle}
								</td>
							</tr>
							<c:set var="isFamChanged" value="${true }" />
						</c:if>		
					</c:forEach>
				</c:if>
				<c:set var="oldfam" value="${etatArticle.opc_article.familleStr }"></c:set>
			
				<!-- ARTICLE -->
				<c:set var="isArtChanged" value="${empty oldArt or oldArt != etatArticle.opc_article.code}" />
				
				<cplx:tr workId="${etatArticle.opc_article.id }" style="border-top:${isArtChanged or isFamChanged?'3px solid #a29d9d;':'' }">
					<cplx:td>
						<c:if test="${isArtChanged}">
<!-- 				     <tr> -->
<!-- 						<td noresize="true" class="separator-group" style="padding-left: 50px;"> -->
							<a href="javascript:" style="margin-left: 24px;" id="lnk_det" curr="${etatArticle.opc_article.id}-${etatArticle.opc_emplacement.id}">
								<span class="fa fa-plus" style="color:green;"></span> ${etatArticle.opc_article.code}-${etatArticle.opc_article.getLibelleDataVal()}
							</a>
<!-- 						</td> -->
<!-- 						<td class="separator-group" align="center"> -->
<%-- 							<std:link classStyle="btn btn-sm btn-purple" workId="${etatArticle.opc_article.id }" params="qte=${bigDecimalUtil.substract(etatArticle.qte_entree, etatArticle.qte_sortie) }&stck=${etatArticle.opc_emplacement.titre}" action="stock.mouvement.etat_article_detail" icon="fa fa-bars" tooltip="D&eacute;tail mouvement"/> --%>
<!-- 						</td> -->
<!-- 					</tr> -->
<%-- 					<tr style="display: none;" id="tr_det_${etatArticle.opc_article.id}-${etatArticle.opc_emplacement.id}" class="sub"> --%>
<%-- 						<td colspan="7" noresize="true" style="background-color: #fff4d3;cursor: default;" align="center" id="tr_consult_${etatArticle.opc_article.id}-${etatArticle.opc_emplacement.id}"> --%>
							
<!-- 						</td> -->
<!-- 					</tr> -->
					</c:if>	
				</cplx:td>	
				
				<c:set var="qte" value="${bigDecimalUtil.substract(etatArticle.qte_entree, etatArticle.qte_sortie) }"></c:set>
				<c:set var="qteHt" value="${bigDecimalUtil.multiply(etatArticle.opc_article.prix_achat_ht, qte) }"></c:set>
				<c:set var="qteTtc" value="${bigDecimalUtil.multiply(etatArticle.opc_article.prix_achat_ttc, qte) }"></c:set>
				<c:set var="unite" value="${etatArticle.opc_article.opc_unite_achat_enum.libelle }" />
				
					
                    <cplx:td value="${etatArticle.opc_emplacement.titre}"></cplx:td>
					<cplx:td style="font-weight:bold;color:${qte>=0?'green':'red' };" value="${bigDecimalUtil.formatNumberZeroBd(qte) } ${unite }" align="right"></cplx:td>
					<cplx:td style="color:${qte>=0?'green':'red' };" value="${qteHt}" align="right"></cplx:td>
					<cplx:td style="color:${qte>=0?'green':'red' };" value="${qteTtc}" align="right"></cplx:td>
					<cplx:td align="center">
						<c:if test="${empty oldArt or oldArt != etatArticle.opc_article.code}">
							<std:link classStyle="btn btn-sm btn-purple" workId="${etatArticle.opc_article.id }" params="qte=${qte }&stck=${etatArticle.opc_emplacement.titre}" action="stock.mouvement.etat_article_detail" icon="fa fa-bars" tooltip="D&eacute;tail mouvement"/>
						</c:if>
					</cplx:td>
				</cplx:tr>

				<tr style="display: none;" id="tr_det_${etatArticle.opc_article.id}-${etatArticle.opc_emplacement.id}" class="sub">
					<td colspan="7" noresize="true" style="background-color: #fff4d3;cursor: default;" align="center" id="tr_consult_${etatArticle.opc_article.id}-${etatArticle.opc_emplacement.id}">
							
					</td>
				</tr>
				
				<c:set var="oldArt" value="${etatArticle.opc_article.code }"></c:set>				
					
			</c:forEach>
		<%if(ControllerUtil.getMenuAttribute("IS_TTL_MTT", request) != null){ %>
			<c:if test="${listEtatArt.size() > 0 }">
				<tr>
					<td colspan='3' noresize="true" style="background-color:#ebefc4;"></td>
					<td noresize="true" style="font-size: 15px;font-weight: bold;color:green;text-align: right;background-color:#ebefc4;" ><fmt:formatDecimal value="${totalEtatHt }"/></td>
					<td noresize="true" style="font-size: 15px;font-weight: bold;color:green;text-align: right;background-color:#ebefc4;" ><fmt:formatDecimal value="${totalEtatTtc }"/></td>
					<td></td>
				</tr>
			</c:if>
		<%} %>
		</cplx:body>
	</cplx:table>
 </std:form>			

 </div>
					<!-- end widget content -->

				</div>
				<!-- end widget div -->
