<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<% String urlReComenzar = request.getParameter("urlReComenzar"); %>

<p>Ha ocurrido un largo periodo de inactividad y debe comenzar nuevamente el traminte.<p>

<p>
<% if (!"".equals(urlReComenzar)) { %>
  <a href="<%=request.getContextPath()%>/seg/Login.do?method=anonimo&url=<%=urlReComenzar%>">Haga click aqu&iacute; para recomenzar...</a>
<% } %>
</p>
