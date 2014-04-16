<%@ page buffer="64kb" autoFlush="false"%>
<%@ page import="ar.gov.rosario.siat.gde.buss.bean.*" %>
<%@ page import="coop.tecso.demoda.iface.helper.*" %>
<%@ page import="java.util.*" %>

<%
  ActualizaDeuda.getInstance().test001();
%>
<p>Test finalizado. El resultado se genero en un archivo en /tmp/actuadeu.log del server donde este el servidor de tomcat.
<p>IMPORTANTE: No puede haber mas de un test corriendo en simultaneo. Tenga cuidado cuando los ejecuta.
