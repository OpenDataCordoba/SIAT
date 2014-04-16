<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarAsentamiento.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.asentamientoAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.asentamiento.title"/></legend>			
			<table class="tabladatos" width="100%">
				<!-- Servicio Banco -->		
				<tr>
					<td><label><bean:message bundle="def" key="def.servicioBanco.label"/>: </label></td>
					<td class="normal"><bean:write name="asentamientoAdapterVO" property="asentamiento.servicioBanco.desServicioBanco"/></td>
				</tr>	
				<!-- Fecha Balance -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.asentamiento.fechaBalance.label"/>: </label></td>
					<td class="normal"><bean:write name="asentamientoAdapterVO" property="asentamiento.fechaBalanceView"/></td>
				</tr>		
				<!-- Ejercicio y Estado Ejercicio-->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.ejercicio.label"/>: </label></td>
					<td class="normal"><bean:write name="asentamientoAdapterVO" property="asentamiento.ejercicio.desEjercicio"/></td>
					<td><label><bean:message bundle="bal" key="bal.estEjercicio.label"/>: </label></td>
					<td  class="normal"><bean:write name="asentamientoAdapterVO" property="asentamiento.ejercicio.estEjercicio.desEjeBal"/></td>
				</tr>		
				<!-- Mensaje que depende del estado del ejercicio -->
				<tr>
				<logic:equal name="asentamientoAdapterVO" property="paramEstadoEjercicio" value="CERRADO">
				<td class="normal" colspan="4" align="center"><label>&nbsp;<bean:message bundle="bal" key="bal.asentamiento.ejercicioCerrado.label"/></label></td>
				</logic:equal>
				<logic:equal name="asentamientoAdapterVO" property="paramEstadoEjercicio" value="ABIERTO">
				<td class="normal" colspan="4" align="center"><label>&nbsp;<bean:message bundle="bal" key="bal.asentamiento.ejercicioAbierto.label"/></label></td>
				</logic:equal>
				</tr>
				
				<!-- Inclucion de CasoView -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="asentamientoAdapterVO" property="asentamiento"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>				
					</td>
				</tr>
				<!-- Fin Inclucion de CasoView -->
				
				<!-- Observacion-->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.asentamiento.observacion.label"/>: </label></td>
					<td colspan="3" class="normal"><bean:write name="asentamientoAdapterVO" property="asentamiento.observacion"/></td>					
				</tr>
				<!-- Estado Corrida-->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.asentamiento.estadoProceso.label"/>: </label></td>
					<td class="normal"><bean:write name="asentamientoAdapterVO" property="asentamiento.corrida.estadoCorrida.desEstadoCorrida"/></td>					
				</tr>
				<!-- Usuario Alta y Ultima Modificacion-->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.asentamiento.usuarioAlta.label"/>: </label></td>
					<td class="normal"><bean:write name="asentamientoAdapterVO" property="asentamiento.usuarioAlta"/></td>					
					<td><label><bean:message bundle="base" key="base.usuarioUltMdf.label"/>: </label></td>
					<td class="normal"><bean:write name="asentamientoAdapterVO" property="asentamiento.usuario"/></td>					
				</tr>
			</table>
		</fieldset>
	
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="asentamientoAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="asentamientoAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="asentamientoAdapterVO" property="act" value="desactivar">
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
		
		<!-- Inclusion del Codigo Javascript del Calendar-->
		<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
		
		