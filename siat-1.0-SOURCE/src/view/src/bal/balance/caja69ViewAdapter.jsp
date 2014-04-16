<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarCaja69.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.caja69ViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Caja69 -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.caja69.title"/></legend>
			<table class="tabladatos">
			<tr>
				<!-- Sistema -->
				<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.sistema.label"/>: </label></td>
				<td class="normal"><bean:write name="caja69AdapterVO" property="caja69.sistemaView"/></td>	
				<!-- Nro Comprobante -->
				<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.transaccion.nroComprobante.label"/>: </label></td>
				<td class="normal"><bean:write name="caja69AdapterVO" property="caja69.nroComprobanteView"/></td>	
			</tr>
			<tr>
				<!-- Clave -->
				<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.transaccion.clave.label"/>: </label></td>
				<td class="normal"><bean:write name="caja69AdapterVO" property="caja69.clave"/></td>	
				<!-- Resto -->
				<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.transaccion.resto.label"/>: </label></td>
				<td class="normal"><bean:write name="caja69AdapterVO" property="caja69.restoView"/></td>	
			</tr>
	    	<tr>
				<!-- Fecha Pago -->
				<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.transaccion.fechaPago.label"/>: </label></td>
				<td class="normal"><bean:write name="caja69AdapterVO" property="caja69.fechaPagoView"/></td>	
				<!-- Importe -->
				<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.transaccion.importe.label"/>: </label></td>
				<td class="normal"><bean:write name="caja69AdapterVO" property="caja69.importeView"/></td>	
			</tr>
			<tr>
			    <!-- Recargo -->
				<td><label>&nbsp;<bean:message bundle="bal" key="bal.transaccion.recargo.label"/>: </label></td>
				<td class="normal"><bean:write name="caja69AdapterVO" property="caja69.recargoView"/></td>	
			</tr>
			<tr>
			    <!-- Paquete -->
				<td><label>&nbsp;<bean:message bundle="bal" key="bal.transaccion.paquete.label"/>: </label></td>
				<td class="normal"><bean:write name="caja69AdapterVO" property="caja69.paqueteView"/></td>	
			    <!-- Caja -->
				<td><label>&nbsp;<bean:message bundle="bal" key="bal.transaccion.caja.label"/>: </label></td>
				<td class="normal"><bean:write name="caja69AdapterVO" property="caja69.cajaView"/></td>	
			</tr>
			<tr>
			    <!-- Recibo Tr -->
				<td><label>&nbsp;<bean:message bundle="bal" key="bal.transaccion.reciboTr.label"/>: </label></td>
				<td class="normal"><bean:write name="caja69AdapterVO" property="caja69.reciboTrView"/></td>				
				<!-- Fecha Balance -->
				<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.transaccion.fechaBalance.label"/>: </label></td>
				<td class="normal"><bean:write name="caja69AdapterVO" property="caja69.fechaBalanceView"/></td>				
			</tr>
			</table>
		</fieldset>	
		<!-- Caja69 -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	   <logic:equal name="caja69AdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
					<logic:equal name="caja69AdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='caja69AdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
