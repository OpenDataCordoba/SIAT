<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<a name="comienzo" id="comienzo"></a>

<h1>Ingreso a SWE</h1>
<p>Para acceder a la p&aacute;gina deseada, debe ingresar su usuario y contrase&ntilde;a</p>

<fieldset>
	<html:form action="/seg/Login.do?method=login">
		
		<!-- Mensajes/Advertencias -->
		<%@ include file="/swe/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>

		<p>
			<label>Usuario:</label>
            <html:text name="usuarioVO" property="username" size="10" maxlength="15"/>
			<label>Contrase&ntilde;a:</label>
			<html:password name="usuarioVO" property="password" size="10" maxlength="15"/>
			<input type="submit" name="ingresar" value="Aceptar"/>
		</p>
	</html:form>
</fieldset>
