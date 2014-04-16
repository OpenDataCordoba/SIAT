<%@ page buffer="128kb" autoFlush="false"%>
<%@ page import="java.io.*" %>
<%@ page import="coop.tecso.demoda.buss.dao.*"%>
<%@ page import="coop.tecso.demoda.iface.helper.*"%>
<%@ page import="ar.gov.rosario.siat.emi.buss.dao.*" %>
<%@ page import="ar.gov.rosario.siat.emi.buss.bean.*" %>
<%@ page import="java.util.*" %>
<%@ page import="ar.gov.rosario.siat.def.buss.bean.*" %>

<html>
<body>
<pre>
<%=ar.gov.rosario.siat.def.buss.bean.Recurso.getRecInfoVigentes() %>



</pre>  
</body>
</html>
