<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
	.control-label{
	margin-top: -4px;
	}
</style>

	<!-- widget grid -->
	<div class="widget" style="width: 50%;margin: 5px;">
         <div class="widget-body" style="border-radius: 15px;">
			<div class="row" style="margin-left: 15px;">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Seuil alerte" />
					<div class="col-md-2" style="font-weight: bold;color: blue;">
						<fmt:formatDecimal value="${artEtatBean.opc_emplacement_seuil.qte_seuil }"/>
					</div>
					<std:label classStyle="control-label col-md-2" value="TVA" />
					<div class="col-md-2">
						<fmt:formatDecimal value="${artEtatBean.opc_article.opc_tva_enum.code}"/>%
					</div>					
				</div>
				<div class="form-group">	
					<std:label classStyle="control-label col-md-2" value="Prix achat HT" />
					<div class="col-md-2">
						<fmt:formatDecimal value="${artEtatBean.opc_article.prix_achat_ht}"/>
					</div>
					<std:label classStyle="control-label col-md-2" value="Prix achat TTC" />
					<div class="col-md-2">
						<fmt:formatDecimal value="${artEtatBean.opc_article.prix_achat_ttc}"/>
					</div>
				</div>
				<div class="form-group">	
					<std:label classStyle="control-label col-md-2" value="Prix moyen HT" />
					<div class="col-md-2">
						<fmt:formatDecimal value="${artEtatBean.opc_article.prix_achat_moyen_ht}"/>
					</div>
					<std:label classStyle="control-label col-md-2" value="Montant TVA" />
					<div class="col-md-2">
						<fmt:formatDecimal value="${artEtatBean.opc_article.mtt_tva}"/>
					</div>
				</div>
			</div>
		</div>
	</div>