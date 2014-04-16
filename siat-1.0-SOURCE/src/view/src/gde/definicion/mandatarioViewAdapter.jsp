<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarMandatario.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.mandatarioViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Mandatario -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.mandatario.label"/></legend>
			<table class="tabladatos">
				<!-- Descricion -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.mandatario.descripcion.label"/>: </label></td>
					<td class="normal"><bean:write name="mandatarioAdapterVO" property="mandatario.descripcion"/></td>
				</tr>
				<!-- Domicilio -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.mandatario.domicilio.label"/>: </label></td>
					<td class="normal"><bean:write name="mandatarioAdapterVO" property="mandatario.domicilio"/></td>			
				</tr>
				<!-- Telefono -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.mandatario.telefono.label"/>: </label></td>
					<td class="normal"><bean:write name="mandatarioAdapterVO" property="mandatario.telefono"/></td>			
				</tr>
				<!-- HorarioAtencion -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.mandatario.horarioAtencion.label"/>: </label></td>
					<td class="normal"><bean:write name="mandatarioAdapterVO" property="mandatario.horarioAtencion"/></td>		
				</tr>
				<!-- Observaciones -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.mandatario.observaciones.label"/>: </label></td>
					<td class="normal"><bean:write name="mandatarioAdapterVO" property="mandatario.observaciones"/></td>			
				</tr>
		
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="mandatarioAdapterVO" property="mandatario.estado.value"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- Mandatario -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	   <logic:equal name="mandatarioAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
					<logic:equal name="mandatarioAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="mandatarioAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="mandatarioAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	<input type="hidden" name="name"  value="<bean:write name='mandatarioAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->