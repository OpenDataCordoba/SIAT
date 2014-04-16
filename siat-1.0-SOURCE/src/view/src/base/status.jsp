<%@ page buffer="64kb" autoFlush="false"%>
<%@ page import="java.io.*" %>
<%@ page import="coop.tecso.demoda.buss.dao.*"%>
<%@ page import="coop.tecso.demoda.iface.helper.*"%>
<%@ page import="ar.gov.rosario.siat.emi.buss.dao.*" %>
<%@ page import="ar.gov.rosario.siat.emi.buss.bean.*" %>
<%@ page import="java.util.*" %>

<html>
<body>
<pre>
<% System.gc(); %>

<% ar.gov.rosario.siat.base.buss.bean.ConsoleManager.getInstance().status(new PrintWriter(out)); %>

</pre>  
</body>
</html>
