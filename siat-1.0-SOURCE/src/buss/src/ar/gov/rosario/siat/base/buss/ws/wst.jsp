<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.xml.namespace.*"%>
<%@ page import="org.apache.axis2.addressing.*"%>
<%@ page import="org.apache.axis2.client.*"%>
<%@ page import="org.apache.axis2.rpc.client.*"%>

<%!
String wsinvoke(String url, String method, String input) throws Exception {
	RPCServiceClient rpcClient = new RPCServiceClient();
	Options options = rpcClient.getOptions();
	EndpointReference targetEPR = new EndpointReference(url);
	options.setTo(targetEPR);
	options.setAction("urn:" + method);
	@SuppressWarnings("unchecked") 
	ArrayList args = new ArrayList();
	if (input != null) {
		args.add(input);
	}

	Object obj [] = rpcClient.invokeBlocking(new QName("http://siat.rosario.gov.ar/siat/ws", method), args.toArray(), 
	new Class[]{String.class});

	return obj.length > 0 ? (String) obj[0] : null;
} 
%>

<%
  String wshost = request.getParameter("wshost");
  String wssend = request.getParameter("wssend");
  String wsurl = request.getParameter("wsurl");
  String wsmethod = request.getParameter("wsmethod");
  String wsinput = request.getParameter("wsinput");
  String wsresponse = "";
  
  if (wssend != null) {
	try { wsresponse = wsinvoke(wsurl, wsmethod, wsinput); } 
	catch (Exception e) {
		StringWriter result = new StringWriter();
		PrintWriter printWriter = new PrintWriter(result);
		e.printStackTrace(printWriter);
		wsresponse = "axis2 exception:" +  result.toString();
	} 
  }
%>
<html>
<head>

<style>
body {
        background-color : "#FFFFFF";
        leftmargin:"0";
        topmargin:"0";
        font-family:sans;
        font-size:10pt;
}
h1           { font-size: 16pt; font-weight: bold }
h2           { font-size: 13pt; font-weight: bold; color: #5D0D7D }
h3           { font-size: 12pt; font-weight:bold }
input        { font-size: 8pt; border: 1px solid #afafaf; }
textarea     { font-size: 8pt; border: 1px solid #0f0f0f; }
div          { margin-top: 10px; color: #0daf07; }
</style>

<script>
</script>

</head>

<body>  
  <h1>Siat - Web Service Test Page</h1>
  <p>
  <form id="ftest" name="ftest" method="POST">
  <div>url:<br/><input type="text" name="wsurl" size="80" value="<%=wsurl%>"/></div>
  <div>method:<br/><input type="text" name="wsmethod" size="20" value="<%=wsmethod%>"/></div>    
  <div>input:
	  <input type="submit" name="wssend" value="Enviar"/></br>
	  <textarea name="wsinput" cols="80" rows="24"><%=wsinput%></textarea>
  </div>
  <div>response:</br><textarea name="wsresponse" cols="80" rows="24"><%=wsresponse%></textarea></div>
  </form>
  </p>

  <div>servicios:<a href="services/listServices">aqui</a></div>
  
</body>
</html>
