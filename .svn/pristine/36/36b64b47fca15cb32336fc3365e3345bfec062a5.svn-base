 <style>
 .num_auth_stl{ 
 	font-size: 26px; 
     font-weight: bold; 
     color: #262626; 
     margin-left: 10px; 
     margin-top: 10px; 
 } 
 #keys-keyboard a{
    width: 50px;
    height: 50px;
    padding-top: 3px;
}
</style>

<script type="text/javascript">
$(document).ready(function (){
	$('#keyboard-div-num').mousedown(handle_mousedown);	
});
</script>

<input type="hidden" id="num_key_auto" value="1">
<div class="widget" id="keyboard-div-num" style="position: fixed;bottom:-26px;display: none;width: 99%;z-index: 100000;margin-left: 10px;"> 
     <div class="widget-header">
         <span class="widget-caption">
			<a class="btn btn-danger btn-sm icon-only white" href="javascript:void(0);" id="close-keyboard"><i class="fa fa-times "></i></a>
		</span>
         <div class="widget-buttons buttons-bordered">
         	Maj.
             <label>
                 <input class="checkbox-slider toggle colored-blue" type="checkbox" id="upper-keyboard" style="display: none;">
                 <span class="text"></span>
             </label>
         </div>
     </div>
     <div class="widget-body" style="text-align: center;">
		<div class="row" id="keys-keyboard">
			<div class="col-md-12">
					<%for(int i=0; i<10; i++){ %>
					<a href="javascript:void(0);" class="btn btn-info btn-circle num_auth_stl"><%=i %></a>
				<%} %>
				<a href="javascript:void(0);" class="btn btn-info btn-circle num_auth_stl">.</a>
				<a href="javascript:void(0);" id="back" class="btn btn-warning btn-circle num_auth_stl" style="font-size: 12px;margin-left: 20px;"><i class="fa fa-mail-reply"></i></a>
					<a href="javascript:void(0);" id="reset" class="btn btn-warning btn-circle num_auth_stl" style="font-size: 12px;"><i class="fa fa-times"></i></a>
			</div>
		</div>		
     </div>
 </div>