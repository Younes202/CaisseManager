<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#generic_modal_body{
	width: 920px;
	margin-left: -10%;
}
</style>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">D&eacute;tail des articles</span>
		</div>
		<div class="widget-body" style="max-height: 450px;overflow-y: auto;overflow-x: hidden;">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row" style="margin: 0px;">
				<table class="table table-hover table-striped table-bordered" style="font-size: 11px;">
                     <thead class="bordered-blueberry">
                         <tr>
                         	 <th style="width: 40px;"></th>
                             <th>
                                 ARTICLE
                             </th>
                             <th style="width: 120px">
                                 QUANTIE
                             </th>
                             <th style="width: 120px">
                                 MONTANT
                             </th>
                             <th style="width: 120px">
                                 MARGE ARTICLE
                             </th>
                             <th style="width: 120px">
                                 MARGE CALCULEE
                             </th>
                         </tr>
                        </thead>
                        <tbody> 
                         	<c:set var="cptCmd" value="1" />
                         	<c:set var="mttTtl" value="${0 }" />
                         	<c:set var="margeTtl" value="${0 }" />
                         	
							<c:forEach items='${listMvm }' var="caisseMvmDet">
								<c:set var="tauxMarge" value="${caisseMvmDet.opc_article.taux_marge_caissier }" />
								<c:if test="${caisseMvmDet.taux_marge_cai != null and caisseMvmDet.taux_marge_cai != 0 }">
									<c:set var="tauxMarge" value="${caisseMvmDet.taux_marge_cai }" />
								</c:if>
							
								<tr>
									<td>
										${cptCmd }
									</td>
									<td style="font-weight: bold;color: blue;">${caisseMvmDet.opc_article.libelle}</td>
									<td style="font-weight:bold;" align="right"><fmt:formatDecimal value="${caisseMvmDet.quantite}"/></td>
									<td style="font-weight:bold;" align="right"><fmt:formatDecimal value="${caisseMvmDet.mtt_total}"/></td>
									<td style="font-weight:bold;" align="right"><fmt:formatDecimal value="${tauxMarge}"/>%</td>
									
									<c:set var="marge" value="${ (caisseMvmDet.mtt_total*tauxMarge)/100 }" />
									
									<td style="font-weight:bold;" align="right"><fmt:formatDecimal value="${marge}"/></td>
									
									<c:set var="mttTtl" value="${mttTtl + caisseMvmDet.mtt_total }" />
                         			<c:set var="margeTtl" value="${margeTtl + marge }" />
								</tr>
								<c:set var="cptCmd" value="${cptCmd+1 }" />  
							</c:forEach>
							<c:if test="${listMvm.size() == 0 }">
								<tr align="center" class="alt" style=""><td align="center" colspan="6">- Aucun élément à afficher -</td></tr>
							</c:if>
							<c:if test="${listMvm.size() > 0 }">
								<td colspan="3"></td>
								<td style="font-weight:bold;font-size: 14px;color: fuchsia;" align="right"><fmt:formatDecimal value="${mttTtl}"/></td>
								<td></td>
								<td style="font-weight:bold;font-size: 14px;color: fuchsia;" align="right"><fmt:formatDecimal value="${margeTtl}"/></td>
							</c:if>
						<tbody>
					</table>
				</div>	
					
				<div class="row" style="text-align: center;margin-top: 10px;">
					<div class="col-md-12">
						<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</div>
		</div>
</std:form>

