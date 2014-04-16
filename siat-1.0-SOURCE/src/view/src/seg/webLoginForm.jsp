<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<a name="comienzo" id="comienzo"></a>


<script type="text/javascript" language="javascript">			   
   	 
/*
  Se agrega este metodo para deshabilitar el boton de "Aceptar" durante el logeo. (Solicitado en Mantis 5354)
*/
function submitLogin() {

	var form = document.getElementById('loginForm');
	
	form.elements["ingresar"].disabled = "true";
	
	form.submit();
}

</script>

<h1>Ingreso</h1>
<p>Para acceder a la p&aacute;gina deseada, debe ingresar su usuario y contrase&ntilde;a</p>
		
<fieldset>
	<html:form styleId="loginForm" action="/login/LoginSsl.do?method=webLogin">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>

		<p>
			<label>Usuario:</label>
            <html:text name="usuarioVO" property="username" size="10" maxlength="15"/>
			<label>Contrase&ntilde;a:</label>
			<html:password name="usuarioVO" property="password" size="10" maxlength="15"/>
			<input type="button" name="ingresar" onClick="submitLogin();"  value="Aceptar"/>

            <input type="hidden" name="anonimo" value="<%=request.getParameter("anonimo")%>"/>
            <input type="hidden" name="urlReComenzar" value="<%=request.getParameter("urlReComenzar")%>"/>
		</p>
	</html:form>
</fieldset>
