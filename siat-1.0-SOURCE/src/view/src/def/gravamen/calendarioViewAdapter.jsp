<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/def/AdministrarCalendario.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.calendarioViewAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Calendario -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.calendario.title"/></legend>
			<table class="tabladatos">
				<!-- Recurso -->
				<tr>
					<td><label><bean:message bundle="def" key="def.calendario.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="calendarioAdapterVO" property="calendario.recurso.desRecurso"/></td>
				</tr>
				
				<!-- Zona -->
				<tr>
					<td><label><bean:message bundle="def" key="def.calendario.zona.label"/>: </label></td>
					<td class="normal"><bean:write name="calendarioAdapterVO" property="calendario.zona.descripcion"/></td>
				</tr>
				
				<!-- Periodo -->
				<tr>
					<td><label><bean:message bundle="def" key="def.calendario.periodo.label"/>: </label></td>
					<td class="normal"><bean:write name="calendarioAdapterVO" property="calendario.periodo"/></td>
				</tr>

				<!-- Fecha Vencimiento -->
				<tr>
					<td><label><bean:message bundle="def" key="def.calendario.fechaVencimiento.label"/>: </label></td>
					<td class="normal"><bean:write name="calendarioAdapterVO" property="calendario.fechaVencimientoView"/></td>
				</tr>

				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="calendarioAdapterVO" property="calendario.estado.value"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- Calendario -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="calendarioAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="calendarioAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="calendarioAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
