<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarTranBal.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.tranBalViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- TranBal -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.tranBal.title"/></legend>
			<table class="tabladatos">
			<tr>
				<!-- Sistema -->
				<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.sistema.label"/>: </label></td>
				<td class="normal"><bean:write name="tranBalAdapterVO" property="tranBal.sistemaView"/></td>	
				<!-- Nro Comprobante -->
				<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.transaccion.nroComprobante.label"/>: </label></td>
				<td class="normal"><bean:write name="tranBalAdapterVO" property="tranBal.nroComprobanteView"/></td>	
			</tr>
			<tr>
				<!-- Clave -->
				<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.transaccion.clave.label"/>: </label></td>
				<td class="normal"><bean:write name="tranBalAdapterVO" property="tranBal.clave"/></td>	
				<!-- Resto -->
				<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.transaccion.resto.label"/>: </label></td>
				<td class="normal"><bean:write name="tranBalAdapterVO" property="tranBal.restoView"/></td>	
			</tr>
	    	<tr>
				<!-- Fecha Pago -->
				<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.transaccion.fechaPago.label"/>: </label></td>
				<td class="normal"><bean:write name="tranBalAdapterVO" property="tranBal.fechaPagoView"/></td>	
				<!-- Importe -->
				<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.transaccion.importe.label"/>: </label></td>
				<td class="normal"><bean:write name="tranBalAdapterVO" property="tranBal.importeView"/></td>	
			</tr>
			<tr>
			    <!-- Recargo -->
				<td><label>&nbsp;<bean:message bundle="bal" key="bal.transaccion.recargo.label"/>: </label></td>
				<td class="normal"><bean:write name="tranBalAdapterVO" property="tranBal.recargoView"/></td>	
			    <!-- Filler -->
				<td><label>&nbsp;<bean:message bundle="bal" key="bal.transaccion.filler.label"/>: </label></td>
				<td class="normal"><bean:write name="tranBalAdapterVO" property="tranBal.filler"/></td>	
			</tr>
			<tr>
			    <!-- Paquete -->
				<td><label>&nbsp;<bean:message bundle="bal" key="bal.transaccion.paquete.label"/>: </label></td>
				<td class="normal"><bean:write name="tranBalAdapterVO" property="tranBal.paqueteView"/></td>	
			    <!-- Filler -->
				<td><label>&nbsp;<bean:message bundle="bal" key="bal.transaccion.marcaTr.label"/>: </label></td>
				<td class="normal"><bean:write name="tranBalAdapterVO" property="tranBal.marcaTrView"/></td>	
			</tr>
			<tr>
			    <!-- Recibo Tr -->
				<td><label>&nbsp;<bean:message bundle="bal" key="bal.transaccion.reciboTr.label"/>: </label></td>
				<td class="normal"><bean:write name="tranBalAdapterVO" property="tranBal.reciboTrView"/></td>				
				<!-- Fecha Balance -->
				<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.transaccion.fechaBalance.label"/>: </label></td>
				<td class="normal"><bean:write name="tranBalAdapterVO" property="tranBal.fechaBalanceView"/></td>				
			</tr>
			<tr>
			    <!-- Nro Linea -->
				<td><label>&nbsp;<bean:message bundle="bal" key="bal.tranBal.nroLinea.label"/>: </label></td>
				<td class="normal"><bean:write name="tranBalAdapterVO" property="tranBal.nroLineaView"/></td>				
				<!-- Linea -->
				<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.tranBal.linea.label"/>: </label></td>
				<td class="normal"><bean:write name="tranBalAdapterVO" property="tranBal.fechaBalanceView"/></td>				
			</tr>
			</table>
		</fieldset>	
		<!-- TranBal -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	   <logic:equal name="tranBalAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
					<logic:equal name="tranBalAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='tranBalAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
