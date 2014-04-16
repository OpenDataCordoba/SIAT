<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rec/AdministrarAnulacionObra.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="rec" key="rec.anulacionObraViewAdapter.title"/></h1>

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- AnulacionObra -->
		<fieldset>
			<legend><bean:message bundle="rec" key="rec.anulacionObra.title"/></legend>
			<table class="tabladatos">
				<!-- Fecha de Anulacion -->
				<tr>
					<td><label><bean:message bundle="rec" key="rec.anulacionObra.fechaAnulacion.label"/>: </label></td>
					<td class="normal"><bean:write name="anulacionObraAdapterVO" property="anulacionObra.fechaAnulacionView"/></td>
				</tr>
				<!--  Obra -->
				<tr>
					<td><label><bean:message bundle="rec" key="rec.anulacionObra.obra.label"/>: </label></td>
					<td class="normal"><bean:write name="anulacionObraAdapterVO" property="anulacionObra.obra.desObraConNumeroObra"/></td>
				</tr>

				<!--  Planilla -->
				<tr>
					<td><label><bean:message bundle="rec" key="rec.anulacionObra.planillaCuadra.label"/>: </label></td>
					<td class="normal"><bean:write name="anulacionObraAdapterVO" property="anulacionObra.planillaCuadra.descripcion"/></td>
				</tr>
				
				<!--  Cuenta CdM -->
				<tr>
					<td><label><bean:message bundle="rec" key="rec.anulacionObra.plaCuaDet.label"/>: </label></td>
					<td class="normal"><bean:write name="anulacionObraAdapterVO" property="anulacionObra.plaCuaDet.cuentaCdM.numeroCuenta"/></td>
				</tr>

				<!-- Fecha de Vencimiento -->
				<tr>
					<td><label><bean:message bundle="rec" key="rec.anulacionObra.fechaVencimiento.label"/>: </label></td>
					<td class="normal"><bean:write name="anulacionObraAdapterVO" property="anulacionObra.fechaVencimientoView"/></td>
				</tr>
				
				<!-- Observacion -->
				<tr>
					<td><label><bean:message bundle="rec" key="rec.anulacionObra.observacion.label"/>: </label></td>
					<td class="normal"><bean:write name="anulacionObraAdapterVO" property="anulacionObra.observacion"/></td>
				</tr>

				<!-- Inclucion de CasoView -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="anulacionObraAdapterVO" property="anulacionObra"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>				
					</td>
				</tr>
				<!-- Fin Inclucion de CasoView -->

				<!-- Estado del Proceso -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="anulacionObraAdapterVO" 
						property="anulacionObra.corrida.estadoCorrida.desEstadoCorrida"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- AnulacionObra -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="anulacionObraAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="anulacionObraAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="anulacionObraAdapterVO" property="act" value="desactivar">
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
