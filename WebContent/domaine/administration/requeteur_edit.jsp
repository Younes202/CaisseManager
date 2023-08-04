<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
	#request {display: none;}
	
	/*styles for the pre tag*/
	#layer {
		font-family: monospace;
		font-size: 12px;
		line-height: 20px;
		
		border: 1px solid #ccc;
		background: linear-gradient(white, white 19px, #ddd 19px, #ddd 20px);
		background-size: 20px 20px;
		
		padding: 20px 30px;
		
		white-space: pre-wrap; /*to wrap words*/
		word-wrap: break-word; /*IE 5.5+*/
	}
	
	/*colors*/
	.sc {color: brown;}
	.keyword {color: purple; font-weight: bold;}
	.string {color: darkgreen;}
	.comment {color: green; background: lightyellow;}
	.function {color: red;}
</style>

<script type="text/javascript">
$(document).ready(function(){
	$("#layer").click(function(){
		$("#request").show().focus();
		$("#layer").hide();
	});
	$("#request").focusout(function(){
		$("#request").hide();
		$("#layer").show();
		loadHightLighter();
	});
	
	$("#div_tables a").click(function(){
		$("#request").val($("#request").val()+" "+$(this).text());
		$("#request").show().focus();
		$("#layer").hide();
	});
});

	function loadHightLighter(){
		//full list of reserved words: http://dev.mysql.com/doc/refman/5.0/en/reserved-words.html
		var k = ["AND", "AS", "ASC", "BETWEEN", "BY", "CASE", "CURRENT_DATE", "CURRENT_TIME", "DELETE", "DESC", "DISTINCT", "EACH", "ELSE", "ELSEIF", "FALSE", "FOR", "FROM", "GROUP", "HAVING", "IF", "IN", "INSERT", "INTERVAL", "INTO", "IS", "JOIN", "KEY", "KEYS", "LEFT", "LIKE", "LIMIT", "MATCH", "NOT", "NULL", "ON", "OPTION", "OR", "ORDER", "OUT", "OUTER", "REPLACE", "RIGHT", "SELECT", "SET", "TABLE", "THEN", "TO", "TRUE", "UPDATE", "VALUES", "WHEN", "WHERE"];
		//adding lowercase keyword support
		var len = k.length;
		for(var i = 0; i < len; i++)
		{
			k.push(k[i].toLowerCase());
		}
		
		var re;
		var c = $("#request").val(); //raw code
		
		//regex time
		//highlighting special characters. /, *, + are escaped using a backslash
		//'g' = global modifier = to replace all occurances of the match
		//$1 = backreference to the part of the match inside the brackets (....)
		c = c.replace(/(=|%|\/|\*|-|,|;|\+|<|>)/g, "<span class=\"sc\">$1</span>");
		
		//strings - text inside single quotes and backticks
		c = c.replace(/(['`].*?['`])/g, "<span class=\"string\">$1</span>");
		
		//numbers - same color as strings
		c = c.replace(/(\d+)/g, "<span class=\"string\">$1</span>");
		
		//functions - any string followed by a '('
		c = c.replace(/(\w*?)\(/g, "<span class=\"function\">$1</span>(");
		
		//brackets - same as special chars
		c = c.replace(/([\(\)])/g, "<span class=\"sc\">$1</span>");
		
		//reserved mysql keywords
		for(var i = 0; i < k.length; i++)
		{
			//regex pattern will be formulated based on the array values surrounded by word boundaries. since the replace function does not accept a string as a regex pattern, we will use a regex object this time
			re = new RegExp("\\b"+k[i]+"\\b", "g");
			c = c.replace(re, "<span class=\"keyword\">"+k[i]+"</span>");
		}
		
		//comments - tricky...
		//comments starting with a '#'
		c = c.replace(/(#.*?\n)/g, clear_spans);
		
		//comments starting with '-- '
		//first we need to remove the spans applied to the '--' as a special char
		c = c.replace(/<span class=\"sc\">-<\/span><span class=\"sc\">-<\/span>/g, "--");
		c = c.replace(/(-- .*?\n)/g, clear_spans);
		
		//comments inside /*...*/
		//filtering out spans attached to /* and */ as special chars
		c = c.replace(/<span class=\"sc\">\/<\/span><span class=\"sc\">\*<\/span>/g, "/*");
		c = c.replace(/<span class=\"sc\">\*<\/span><span class=\"sc\">\/<\/span>/g, "*/");
		//In JS the dot operator cannot match newlines. So we will use [\s\S] as a hack to select everything(space or non space characters)
		c = c.replace(/(\/\*[\s\S]*?\*\/)/g, clear_spans);
		
		$("#layer").html(c); //injecting the code into the pre tag
	}	
	//as you can see keywords are still purple inside comments.
	//we will create a filter function to remove those spans. This function will noe be used in .replace() instead of a replacement string
	function clear_spans(match)
	{
		match = match.replace(/<span.*?>/g, "");
		match = match.replace(/<\/span>/g, "");
		return "<span class=\"comment\">"+match+"</span>";
	}

</script>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Modules avanc&eacute;s</li>
		<li class="active">Requ&ecirc;teur</li> 
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="C" classStyle="btn btn-default" action="admin.job.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<!-- Page Body -->
<div class="page-body">
	<std:form name="data-form">
		<div class="row">
			<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
		</div>
	
		<div class="row">
	           <div class="col-lg-12 col-sm-12 col-xs-12">
	                <div class="widget">
	                    <div class="widget-header bg-blueberry">
	                        <i class="widget-icon fa fa-arrow-right"></i>
	                        <span class="widget-caption">Tables</span>
	                    </div><!--Widget Header-->
	                    <div class="widget-body" id="div_tables" style="height: 150px;overflow: auto;">
	                        <c:forEach items="${listTables }" var="table">
								<a href="javascript:" style="padding-right: 20px;color: blue;font-style: italic;">${table }</a> | 
							</c:forEach>
	                    </div><!--Widget Body-->
	                </div><!--Widget-->
	            </div>
		</div>
		<!-- row -->
		<div class="row">
			<pre id="layer"></pre>
		    <textarea id="request" name="request" rows="10" cols="120" style="width:100%;"></textarea>
		</div>
		
		<div class="form-actions">
			<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
				<std:button actionGroup="C" classStyle="btn btn-success" targetDiv="data_div" action="admin.requeteur.executeRequest" icon="fa-save" value="Ex&eacute;cuter la requ&ecirc;te" />
			</div>
		</div>
		
		<hr>
		
		<div class="row" id="data_div" style="width: 100%;overflow: auto;">
		
		
		</div>
		

		<!-- end widget content -->
	</std:form>
	</div>
	<!-- end widget div -->
