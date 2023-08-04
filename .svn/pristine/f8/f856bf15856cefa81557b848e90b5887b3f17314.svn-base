<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fn" prefix="fn"%>
<%@page errorPage="/commun/error.jsp" %>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Fiche synchronisation</span>
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
	
		<table style="margin-left: 5%;width: 100%;">
		<c:forEach items="${listBalance }" var="balance">
			<tr>
				<td style="font-size: 14px;">${balance.reference }</td>
				<td>
					<std:link action="stock.composant.sync_balance" params="isSub=1&cai=${balance.id }" value="Synchroniser" icon="fa-3x fa-exchange" />
				</td>
			</tr>
		</c:forEach>
		</table>
  	</div>
</div>
</div>
</std:form>