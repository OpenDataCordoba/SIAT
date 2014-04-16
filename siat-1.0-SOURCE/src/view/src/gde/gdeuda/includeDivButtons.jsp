<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<div id="menuhorizontal">
	<ul>
		<logic:notPresent name="userSession" property="idRecurso">
			<logic:present parameter="id" >
				<bean:parameter id="idRecursoPage" name="id" />
			</logic:present>
			<logic:notPresent parameter="id" >
				<bean:define id="idRecursoPage" value="14" />
				<bean:define id="verCambioDomicilio" value="true" />
			</logic:notPresent>
		</logic:notPresent>
		<logic:present name="userSession" property="idRecurso">
			<bean:define id="idRecursoPage" name="userSession" property="idRecurso"  />
		</logic:present>
		
		
		<li><a href="<%= request.getContextPath()%>/gde/AdministrarLiqDeuda.do?method=inicializarContr&id=<bean:write name="idRecursoPage"/>" >Gestión de deuda </a></li>
		<logic:equal name="verCambioDomicilio" value="true" >
			<li><a href="<%= request.getContextPath()%>/pad/AdministrarCambiarDomEnvio.do?method=inicializar&id=<bean:write name="idRecursoPage"/>">Cambio domicilio</a> </li>
		</logic:equal>
		
		<li><a href="<%= request.getContextPath()%>/gde/gdeuda/mesa_ayuda.jsp">Mesa de ayuda </a> </li>
		<li><a href="<%= request.getContextPath()%>/gde/gdeuda/faqs.jsp">Preguntas frecuentes </a> </li>
	</ul>
</div>