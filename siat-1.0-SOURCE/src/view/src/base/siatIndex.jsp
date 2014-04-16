<%@ page buffer="64kb" autoFlush="false"%>
<%@ page import="ar.gov.rosario.siat.base.view.util.*, ar.gov.rosario.siat.base.iface.model.*" %>

<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<h3>Bienvenido a SIAT</h3>

<p>
Para ingresar a la aplicaci&oacute;n presione <a href="/siat/login/LoginSsl.do?method=<%=SiatParam.isIntranetSiat() ? "intranetInit" : "webInit"%>">aquí</a>.
</p>

