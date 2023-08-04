<style>

#generic_modal_body{
	width: 300px;
}
#div-calc a{
    font-size: 20px;
    margin: 5px;
}
</style>

<script type="text/javascript">
$(document).ready(function (){
	$("#div-calc a").click(function(){
			if($(this).attr("v")=='B'){
				$("#calc-screen").val($("#calc-screen").val().substring(0, $("#calc-screen").val().length-1));
			} else if($(this).attr("v") == 'C'){
				$("#calc-screen").val('');
			} else if($(this).attr("v") == '='){
				var result = eval($("#calc-screen").val());
				$("#calc-screen").val(result);
			} else{
				$("#calc-screen").val($("#calc-screen").val()+$(this).attr("v"));	
			}
	});
});
</script>

<div class="widget">
	<div class="widget-header bordered-bottom bordered-blue">
		<span class="widget-caption">Calculatrice</span>
		<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal" style="margin-top: 2px;margin-right: 2px;">
			<i class="fa fa-times"></i> Fermer
		</button>
	</div>
	<div class="widget-body">
		<div class="row" id="div-calc">
			<div class="col-lg-12" style="margin-left: 19px;">
				<div class="form-group" style="margin:0px; margin-bottom: 1px;">
		     		<input type="text" readonly="readonly" size="15" maxlength="17" id="calc-screen" class="form-control" style="width: 225px;float: left;background-color: #fb6e52;height: 40px;font-size: 20px;text-align: right;">
		     		<a href="javascript:void(0);" style="margin-top: -5px;margin-left: 5px;" class="btn btn-magenta btn-circle btn-sm shiny" v="C">
		     			<i class="fa fa-eraser"></i>
		     		</a>
		    	</div>
			</div>
			<div class="col-lg-12" style="margin-left: 19px;">
				<a href="javascript:void(0);" class="btn btn-palegreen btn-circle btn-sm shiny" v="%">%</a>
				<a href="javascript:void(0);" class="btn btn-warning btn-circle btn-sm shiny" v="(">(</a>
				<a href="javascript:void(0);" class="btn btn-warning btn-circle btn-sm shiny" v=")">)</a>
				<a href="javascript:void(0);" class="btn btn-palegreen btn-circle btn-sm shiny" v="/">/</a>
			
				<a href="javascript:void(0);" class="btn btn-info btn-circle btn-sm shiny" v="7">7</a>
				<a href="javascript:void(0);" class="btn btn-info btn-circle btn-sm shiny" v="8">8</a>
				<a href="javascript:void(0);" class="btn btn-info btn-circle btn-sm shiny" v="9">9</a>
				<a href="javascript:void(0);" class="btn btn-palegreen btn-circle btn-sm shiny" v="*">x</a>
			
				<a href="javascript:void(0);" class="btn btn-info btn-circle btn-sm shiny" v="4">4</a>
				<a href="javascript:void(0);" class="btn btn-info btn-circle btn-sm shiny" v="5">5</a>
				<a href="javascript:void(0);" class="btn btn-info btn-circle btn-sm shiny" v="6">6</a>
				<a href="javascript:void(0);" class="btn btn-palegreen btn-circle btn-sm shiny" v="-">-</a>
				
				<a href="javascript:void(0);" class="btn btn-info btn-circle btn-sm shiny" v="1">1</a>
				<a href="javascript:void(0);" class="btn btn-info btn-circle btn-sm shiny" v="2">2</a>
				<a href="javascript:void(0);" class="btn btn-info btn-circle btn-sm shiny" v="3">3</a>
				<a href="javascript:void(0);" class="btn btn-palegreen btn-circle btn-sm shiny" v="+">+</a>

				<a href="javascript:void(0);" class="btn btn-info btn-circle btn-sm shiny" v="0">0</a>
				<a href="javascript:void(0);" class="btn btn-warning btn-circle btn-sm shiny" v=".">.</a>
				<a href="javascript:void(0);" class="btn btn-palegreen btn-circle btn-sm shiny" v="=">=</a>
				<a href="javascript:void(0);" class="btn btn-danger btn-circle btn-sm shiny" v="B"><i class="fa fa-mail-reply"></i></a>
			</div>
		</div>
	</div>
</div>		