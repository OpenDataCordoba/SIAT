<%@ page isErrorPage="true" contentType="text/html" %>
<%@ page import="org.apache.log4j.Logger"%> 

<p>Lo siento, pero ha ocurrido un error.
Intente nuevamente, si el error persiste, informe de este error al administrador. 
</p>

<% if (session.getAttribute("userSession") != null) { %>
<p>
  <a href="<%=request.getContextPath()%>/seg/SiatMenu.do?method=build">Haga click aqu&iacute; para ir al men&uacute;...</a>
</p>
<% } %>

<% //logueamos la excepcion del JSP
Logger log = Logger.getLogger(coop.tecso.demoda.iface.helper.DemodaUtil.class);
log.error("JspException:", pageContext.getErrorData().getThrowable());
%>

<jsp:useBean id="now" class="java.util.Date"/>
<pre style="font-family:times">  
  <%= now %>
  Request that failed: <%= pageContext.getErrorData().getRequestURI() %>
  Status code: <%= pageContext.getErrorData().getStatusCode() %>
  Exception: <%= pageContext.getErrorData().getThrowable() %> 
</pre>

