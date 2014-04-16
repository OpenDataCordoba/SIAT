<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/AdministrarEmisionMas.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="emi" key="emi.emisionMasViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- EmisionMas -->
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.emision.title"/></legend>
			<table class="tabladatos">
				<tr>
					<!-- Recurso -->
					<td><label><bean:message bundle="emi" key="emi.emision.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="emisionMasAdapterVO" property="emision.recurso.desRecurso"/></td>
				</tr>

				<logic:equal name="emisionMasAdapterVO" property="selectAtrValEnabled" value="true">
					<tr>					
						<!-- Atributo -->
						<td><label><bean:write name="emisionMasAdapterVO" property="emision.atributo.desAtributo"/>: </label></td>
						<td class="normal"><bean:write name="emisionMasAdapterVO" property="emision.valor"/></td>
					</tr>
				</logic:equal>

				<tr>
					<!-- Anio -->
					<td><label><bean:message bundle="emi" key="emi.emision.anio.label"/>: </label></td>
					<td class="normal"><bean:write name="emisionMasAdapterVO" property="emision.anioView"/></td>
				</tr>

				<tr>
					<!-- Periodo Desde -->
					<td><label><bean:message bundle="emi" key="emi.emision.periodoDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="emisionMasAdapterVO" property="emision.periodoDesdeView"/></td>

					<!-- Periodo Hasta -->
					<td><label><bean:message bundle="emi" key="emi.emision.periodoHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="emisionMasAdapterVO" property="emision.periodoHastaView"/></td>
				</tr>

				<tr>
					<!-- Estado del Proceso-->
					<td><label><bean:message bundle="emi" key="emi.emision.estadoCorrida.label"/>: </label></td>
					<td class="normal"><bean:write name="emisionMasAdapterVO" property="emision.corrida.estadoCorrida.desEstadoCorrida"/></td>
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
					<logic:equal name="emisionMasAdapterVO" property="act" value="eliminar">
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
<!-- emisionMasViewAdapter.jsp -->