<%@page import="framework.model.util.FileUtil"%>
<html>
<head>

</head>
<body>
<%
String flux = (String)request.getAttribute("img_viewer");
String stl = "background-size:cover !important;background: url(data:image/jpeg;base64,"+flux+") no-repeat;width:90vw;height:100vh";
//int height = (Integer)request.getAttribute("height_viewer");
//int width = (Integer)request.getAttribute("width_viewer");

%>
	<div style="overflow:auto;<%=stl%>;">
	
	</div>

</body>

</html>