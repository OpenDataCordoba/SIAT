<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/AdministrarEmisionExterna.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="emi" key="emi.emisionExternaViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Emision -->
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.emision.title"/></legend>
			<table class="tabladatos">
				
				<tr>
					<!-- Archivo -->
					<td><label><bean:message bundle="emi" key="emi.emision.archivo.label"/>: </label></td>
					<td class="normal"><bean:write name="emisionExternaAdapterVO" property="emision.observacion"/></td>
				</tr>
				
				<tr>
					<!-- Recurso -->
					<td><label><bean:message bundle="emi" key="emi.emision.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="emisionExternaAdapterVO" property="emision.recurso.desRecurso"/></td>
				</tr>

				<tr>
					<!-- Anio -->
					<td><label><bean:message bundle="emi" key="emi.emisionExternaViewAdapter.anio.label"/>: </label></td>
					<td class="normal"><bean:write name="emisionExternaAdapterVO" property="anioView"/></td>
				</tr>

				<tr>
					<!-- Periodo -->
					<td><label><bean:message bundle="emi" key="emi.emisionExternaViewAdapter.periodo.label"/>: </label></td>
					<td class="normal"><bean:write name="emisionExternaAdapterVO" property="periodoView"/></td>
				</tr>

				<tr>
					<!-- Estado del Proceso-->
					<td><label><bean:message bundle="emi" key="emi.emision.estadoCorrida.label"/>: </label></td>
					<td class="normal"><bean:write name="emisionExternaAdapterVO" property="emision.corrida.estadoCorrida.desEstadoCorrida"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- EmisionMas -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="emisionExternaAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
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
<!-- emisionExternaViewAdapter.jsp -->