<%@ page import="ar.gov.rosario.siat.def.iface.util.*" %>
<%@ page import="ar.gov.rosario.siat.def.iface.service.*" %>

<html><body style="margin:0px; padding:0px; font-size:10px">
<%
try {
  String cache = request.getParameter("cache");
  System.out.println("Cache: request de refrescar cache desde: " + request.getRemoteAddr());
  IDefConfiguracionService defConf = DefServiceLocator.getConfiguracionService();
  defConf.refreshCache(cache);
  out.println("OK");
} catch (Exception e) {
  System.out.println("Cache: Exception:" + e);
  e.printStackTrace();
  out.println("ERROR: " + e);
}
%>
</body></html>