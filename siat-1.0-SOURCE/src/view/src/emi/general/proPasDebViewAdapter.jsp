<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/AdministrarProPasDeb.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="emi" key="emi.proPasDebViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- ProPasDeb -->
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.proPasDeb.title"/></legend>
			<table class="tabladatos">
				<tr>
					<!-- Recurso -->
					<td><label><bean:message bundle="emi" key="emi.proPasDeb.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="proPasDebAdapterVO" property="proPasDeb.recurso.desRecurso"/></td>
				</tr>

				<logic:equal name="proPasDebAdapterVO" property="selectAtrValEnabled" value="true">
					<tr>					
						<!-- Atributo -->
						<td><label><bean:write name="proPasDebAdapterVO" property="proPasDeb.atributo.desAtributo"/>: </label></td>
						<td class="normal"><bean:write name="proPasDebAdapterVO" property="proPasDeb.atrValor"/></td>
					</tr>
				</logic:equal>

				<tr>
					<!-- Periodos -->
					<td><label><bean:message bundle="emi" key="emi.proPasDeb.periodo.label"/>: </label></td>
					<td class="normal"><bean:write name="proPasDebAdapterVO" property="proPasDeb.periodoView"/>
						/<bean:write name="proPasDebAdapterVO" property="proPasDeb.anioView"/>
					</td>
				</tr>

				<tr>
					<!-- Fecha de Envio -->
					<td><label><bean:message bundle="emi" key="emi.proPasDeb.fechaEnvio.label"/>: </label></td>
					<td class="normal"><bean:write name="proPasDebAdapterVO" property="proPasDeb.fechaEnvioView"/></td>
				</tr>

				<tr>
					<!-- Estado del Proceso-->
					<td><label><bean:message bundle="emi" key="emi.proPasDeb.estadoCorrida.label"/>: </label></td>
					<td class="normal"><bean:write name="proPasDebAdapterVO" property="proPasDeb.corrida.estadoCorrida.desEstadoCorrida"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- ProPasDeb -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="proPasDebAdapterVO" property="act" value="eliminar">
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
<!-- proPasDebViewAdapter.jsp -->