<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<a name="comienzo" id="comienzo"></a>

<h1>Ingreso - Selecci&oacute; Oficina de trabajo.</h1>
<p>Para ingresar, por favor elija la oficina de trabajo desde la que esta ingresando.</p>
		
<fieldset>
	<html:form action="/login/LoginSsl.do?method=loginSelecOficina">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>

		<p>
			<label>Oficina de Trabajo:</label>
			<html:select value="0" property="oficina.id" styleClass="select">
				<html:optionsCollection name="areaOrigenVO" property="listOficina" label="desOficina" value="id" />
			</html:select>
			
			<input type="submit" name="ingresar" value="Aceptar"/>
            <input type="hidden" name="anonimo" value="<%=request.getParameter("anonimo")%>"/>
            <input type="hidden" name="urlReComenzar" value="<%=request.getParameter("urlReComenzar")%>"/>
		</p>
	</html:form>
</fieldset>