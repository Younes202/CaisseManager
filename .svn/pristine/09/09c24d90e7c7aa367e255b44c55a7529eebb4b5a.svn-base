<%@page import="appli.model.domaine.personnel.persistant.paie.SalairePersistant"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>

<!-- Jours/heures travaillés -->
<div class="data_td">
	&nbsp;<fmt:formatDecimal value="${currEmployePaie.nbr_jours }" />
</div>

<!-- Tarif base -->
<div class="data_td">
	&nbsp;<fmt:formatDecimal value="${currEmployePaie.tarif_jour }" />
</div>

<!-- Congés payés-->
<div class="data_td">
	&nbsp;<fmt:formatDecimal value="${currEmployePaie.nbr_conge }" />
</div>

<!-- Congés non payés-->
<div class="data_td">
	&nbsp;<fmt:formatDecimal value="${currEmployePaie.nbr_conge_np }" />
</div>

<!-- Prime -->
<div class="data_td" style="color: green;">
	&nbsp;<fmt:formatDecimal value="${currEmployePaie.mt_prime }" />
	<c:if test="${not empty currEmployePaie.prime_lib }">
		<i title="${currEmployePaie.prime_lib }" class="fa fa-info-circle"></i>
	</c:if>
</div>

<!-- Indemnité -->
<div class="data_td" style="color: blue;">
	&nbsp;<fmt:formatDecimal value="${currEmployePaie.mt_indemnite }" />
	<c:if test="${not empty currEmployePaie.indemnite_lib }">
		<i title="${currEmployePaie.indemnite_lib }" class="fa fa-info-circle"></i>
	</c:if>
</div>

<!-- Avance -->
<div class="data_td" style="color: blue;">
	&nbsp;<fmt:formatDecimal value="${currEmployePaie.mt_avance }" />
</div>

<!-- Prêt -->
<div class="data_td" style="color: gray;">
	&nbsp;<fmt:formatDecimal value="${currEmployePaie.mt_pret }" />
</div>

<!-- Reliquat -->
<div class="data_td" style="color: blue;">
	&nbsp;<fmt:formatDecimal value="${currEmployePaie.mt_reliquat }" />
</div>

<!-- Retenue -->
<div class="data_td" style="color: red;">
	&nbsp;<fmt:formatDecimal value="${currEmployePaie.mt_retenue }" />
</div>

<!-- Descipline -->
<div class="data_td" style="color: red;">
	&nbsp;
	<c:if test="${not empty currEmployePaie.descipline }">
		<i style="color: red;" title="${currEmployePaie.descipline }" class="fa fa-warning"></i>		
	</c:if>
</div>

<!-- Montant net -->
<div class="data_td" style="font-weight: bold;">
	&nbsp;<fmt:formatDecimal value="${currEmployePaie.montant_net }" />
</div>
