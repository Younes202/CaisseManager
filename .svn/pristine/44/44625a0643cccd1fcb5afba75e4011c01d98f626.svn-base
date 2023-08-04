<%@page import="appli.controller.domaine.util_erp.ContextAppli"%> 

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
	$('#keyboard-div').mousedown(handle_mousedown);	
});
</script>

<div class="widget" id="keyboard-div" style="position: fixed;bottom:-26px;display: none;width: 99%;z-index: 100000;margin-left: 10px;"> 
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
     <div class="widget-body">
		<div class="row" id="keys-keyboard">
			<div class="col-md-4">
					<%
						for(int i=0; i<10; i++){
					%>
					<a href="javascript:void(0);" class="btn btn-info btn-circle num_auth_stl"><%=i%></a>
				<%
					}
				%>
				<a href="javascript:void(0);" class="btn btn-info btn-circle num_auth_stl">.</a>
			</div>
			<div class="col-md-8">
				<%
					for(int i=0; i<ContextAppli.ALPHABET.length; i++){
				%>
					<a href="javascript:void(0);" class="btn btn-success btn-circle num_auth_stl"><%=ContextAppli.ALPHABET[i]%></a>
				<%} %>
				
					<a href="javascript:void(0);" class="btn btn-info btn-circle num_auth_stl btn_code_bar"> </a>
					<a href="javascript:void(0);" class="btn btn-info btn-circle num_auth_stl">-</a>
					<a href="javascript:void(0);" class="btn btn-info btn-circle num_auth_stl">_</a>
					
					<a href="javascript:void(0);" id="back" class="btn btn-warning btn-circle num_auth_stl" style="font-size: 12px;margin-left: 20px;"><i class="fa fa-mail-reply"></i></a>
					<a href="javascript:void(0);" id="reset" class="btn btn-warning btn-circle num_auth_stl" style="font-size: 12px;"><i class="fa fa-times"></i></a>
			</div>	
		</div>		
     </div>
 </div>