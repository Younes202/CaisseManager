<%@page import="framework.model.common.util.StrimUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script type="text/javascript">
	$(document).ready(function (){
		$(".spinbox-up").click(function(){
			$("#quantite_custom").val(parseInt($("#quantite_custom").val())+1);
		});
		$(".spinbox-down").click(function(){
			if($("#quantite_custom").val() != '' && parseInt($("#quantite_custom").val()) > 1){
				$("#quantite_custom").val(parseInt($("#quantite_custom").val())-1);
			}
		});
	});
</script>
	
<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Changer la quantit&eacute;</span>
			
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal" style="margin-top: 2px;margin-right: 5px;">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row" style="margin-left: 0px;margin-right: 0px;">
				<div class="col-lg-4 col-sm-4 col-xs-4"></div>
				<div class="col-lg-3 col-sm-3 col-xs-3">
					 <div class="spinbox spinbox-horizontal spinbox-two-sided">
	                      <div class="spinbox-buttons btn-group spinbox-buttons-left">
	                          <button type="button" class="btn spinbox-down danger">
	                              <i class="fa fa-minus"></i>
	                          </button>
	                      </div>
	                      <std:text name="quantite_custom" type="long" readOnly="true" style="width: 83%;font-size: 20px;" value="1" classStyle="spinbox-input form-control" />
	                      <div class="spinbox-buttons btn-group spinbox-buttons-right">
	                          <button type="button" class="btn spinbox-up blue">
	                              <i class="fa fa-plus"></i>
	                          </button>
	                      </div>
	                  </div>
	               </div>  
	               <div class="col-lg-5 col-sm-5 col-xs-5"></div>
			</div>	
			<hr>	
			<div class="row" style="text-align: center;">
				<div class="col-md-12">
					<std:button actionGroup="X" classStyle="btn btn-success" style="border-radius: 37px;height: 52px;font-size: 21px;" action="caisse-web.caisseWeb.initDupMnu" targetDiv="left-div" params="${params }&sub=1" icon="fa-save" value="Valider" closeOnSubmit="true" />
				</div>
			</div>
		</div>
	</div>
</std:form>