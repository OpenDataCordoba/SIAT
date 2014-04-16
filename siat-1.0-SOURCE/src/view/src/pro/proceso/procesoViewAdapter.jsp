<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pro/AdministrarProceso.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="pro" key="pro.procesoViewAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
			
		<!-- Proceso -->
		<fieldset>
			<legend><bean:message bundle="pro" key="pro.proceso.title"/></legend>
			<table class="tabladatos">
			
			<!-- Codigo -->
			<tr>
				<td><label><bean:message bundle="pro" key="pro.proceso.codProceso.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoAdapterVO" property="proceso.codProceso"/></td>
			</tr>
			
			<!-- Descripcion -->
			<tr>
				<td><label><bean:message bundle="pro" key="pro.proceso.desProceso.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoAdapterVO" property="proceso.desProceso"/></td>
			</tr>
			
			<!-- CantPaso -->
			<tr>
				<td><label><bean:message bundle="pro" key="pro.proceso.cantPasos.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoAdapterVO" property="proceso.cantPasosView"/></td>				
			</tr>
			
			<!-- clase -->
			<tr>
				<td><label><bean:message bundle="pro" key="pro.proceso.classForName.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoAdapterVO" property="proceso.classForName"/></td>				
			</tr>
			
			<!-- usuario -->
			<tr>
				<td><label><bean:message bundle="pro" key="pro.proceso.usuario.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoAdapterVO" property="proceso.usuario"/></td>				
			</tr>
			
			<!-- ejecNodo -->
			<tr>
				<td><label><bean:message bundle="pro" key="pro.proceso.ejecNodo.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoAdapterVO" property="proceso.ejecNodo"/></td>				
			</tr>
			
			<!-- Locked -->
			<tr>
				<td><label><bean:message bundle="pro" key="pro.proceso.locked.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoAdapterVO" property="proceso.locked.value"/></td>				
			</tr>
			
			<!-- esAsincronico -->
			<tr>
				<td><label><bean:message bundle="pro" key="pro.proceso.esAsincronico.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoAdapterVO" property="proceso.esAsincronico.value"/></td>				
			</tr>
			
			<!-- TipoEjecucion -->
			<tr>	
				<td><label><bean:message bundle="pro" key="pro.proceso.tipoEjecucion.label"/>: </label></td>
				<td class="normal">
					<bean:write name="procesoAdapterVO" property="proceso.tipoEjecucion.desTipoEjecucion"/>
				</td>
			</tr>
			<logic:equal name="procesoAdapterVO" property="paramPeriodic" value="true">
			<!-- Period -->
				<tr>
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.proceso.cronExpression.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAdapterVO" property="proceso.cronExpression"/></td>		
				</tr>	
			</logic:equal>
			<!-- TipoProgEjec -->
			<tr>	
				<td><label><bean:message bundle="pro" key="pro.proceso.tipoProgEjec.label"/>: </label></td>
				<td class="normal">
					<bean:write name="procesoAdapterVO" property="proceso.tipoProgEjec.desTipoProgEjec"/>				
				</td>					
			</tr>
		
			<!-- directorioInput -->	
			<tr>
				<td><label><bean:message bundle="pro" key="pro.proceso.directorioInput.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoAdapterVO" property="proceso.directorioInput"/></td>			
			</tr>
			</table>
		</fieldset>	
		<!-- Proceso -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="procesoAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="procesoAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="procesoAdapterVO" property="act" value="desactivar">
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
