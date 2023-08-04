<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">
			Log du job du <b><fmt:formatDate value="${job.start_date }" pattern="dd/MM/yyyy" /></b>  
					(<fmt:formatDate value="${job.start_date }" pattern="HH:mm:ss" />-<fmt:formatDate value="${job.end_date }" pattern="HH:mm:ss" />) 
			&nbsp;&nbsp;&nbsp;
			Statut : <span style="font-weight: bold;color:${job.statut=='E' ? 'red':'green'};">${job.statut }</span>
			</span>
		</div>
		<div class="widget-body">
			<div class="row" style="margin-left: 0px;text-align: center;">
				<span style="color: #03a9f4; font-weight: bold;">${job.job_label }</span>
			</div>
			<hr>
			<div class="row" style="margin-left: 0px;max-height: 500px;">
				${job.job_log }
			</div>
			<hr>
			<div class="row" style="text-align: center;">
				<div class="col-md-12">
					<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
						<i class="fa fa-times"></i> Fermer
					</button>
				</div>
			</div>
		</div>
	</div>
