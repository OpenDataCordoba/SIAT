<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarTranBal.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="bal" key="bal.tranBalEditAdapter.title"/></h1>	

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
			<td class="normal"><html:text name="tranBalAdapterVO" property="tranBal.sistemaView" size="5" maxlength="10"/></td>			
			<!-- Nro Comprobante -->
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.transaccion.nroComprobante.label"/>: </label></td>
			<td class="normal"><html:text name="tranBalAdapterVO" property="tranBal.nroComprobanteView" size="15" maxlength="10"/></td>			
		</tr>
		<tr>
			<!-- Clave -->
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.transaccion.clave.label"/>: </label></td>
			<td class="normal"><html:text name="tranBalAdapterVO" property="tranBal.clave" size="10" maxlength="6"/></td>			
			<!-- Resto -->
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.transaccion.resto.label"/>: </label></td>
			<td class="normal"><html:text name="tranBalAdapterVO" property="tranBal.restoView" size="10" maxlength="10"/></td>			
		<tr>
		<tr>
			<!-- Fecha Pago -->
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.transaccion.fechaPago.label"/>: </label></td>
			<td class="normal">
				<html:text name="tranBalAdapterVO" property="tranBal.fechaPagoView" styleId="fechaPagoView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaPagoView');" id="a_fechaPagoView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		    <!-- Importe -->
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.transaccion.importe.label"/>: </label></td>
			<td class="normal"><html:text name="tranBalAdapterVO" property="tranBal.importeView" size="20" maxlength="100"/></td>			
		</tr>
		<tr>
		    <!-- Recargo -->
			<td><label>&nbsp;<bean:message bundle="bal" key="bal.transaccion.recargo.label"/>: </label></td>
			<td class="normal"><html:text name="tranBalAdapterVO" property="tranBal.recargoView" size="20" maxlength="100"/></td>			
		    <!-- Filler -->
			<td><label>&nbsp;<bean:message bundle="bal" key="bal.transaccion.filler.label"/>: </label></td>
			<td class="normal"><html:text name="tranBalAdapterVO" property="tranBal.filler" size="10" maxlength="10"/></td>			
		</tr>
		<tr>
		    <!-- Paquete -->
			<td><label>&nbsp;<bean:message bundle="bal" key="bal.transaccion.paquete.label"/>: </label></td>
			<td class="normal"><html:text name="tranBalAdapterVO" property="tranBal.paqueteView" size="20" maxlength="100"/></td>			
		    <!-- Caja -->
			<td><label>&nbsp;<bean:message bundle="bal" key="bal.transaccion.caja.label"/>: </label></td>
			<td class="normal"><html:text name="tranBalAdapterVO" property="tranBal.cajaView" size="10" maxlength="10"/></td>			
		</tr>
		<tr>
		    <!-- CodPago -->
			<td><label>&nbsp;<bean:message bundle="bal" key="bal.transaccion.codPago.label"/>: </label></td>
			<td class="normal"><html:text name="tranBalAdapterVO" property="tranBal.codPagoView" size="10" maxlength="10"/></td>			
		    <!-- CodTr -->
			<td><label>&nbsp;<bean:message bundle="bal" key="bal.transaccion.codTr.label"/>: </label></td>
			<td class="normal"><html:text name="tranBalAdapterVO" property="tranBal.codTrView" size="10" maxlength="10"/></td>			
		</tr>
		<tr>
		    <!-- Recibo Tr -->
			<td><label>&nbsp;<bean:message bundle="bal" key="bal.transaccion.reciboTr.label"/>: </label></td>
			<td class="normal"><html:text name="tranBalAdapterVO" property="tranBal.reciboTrView" size="20" maxlength="100"/></td>			

		    <!-- Filler -->
			<td><label>&nbsp;<bean:message bundle="bal" key="bal.transaccion.marcaTr.label"/>: </label></td>
			<td class="normal"><html:text name="tranBalAdapterVO" property="tranBal.marcaTrView" size="10" maxlength="10"/></td>			
		</tr>
		<tr>
			<!-- Fecha Balance -->
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.transaccion.fechaBalance.label"/>: </label></td>
			<td class="normal">
				<html:text name="tranBalAdapterVO" property="tranBal.fechaBalanceView" styleId="fechaBalanceView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaBalanceView');" id="a_fechaBalanceView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>
		</table>
	</fieldset>	
	<!-- TranBal -->
	
	<table class="tablabotones">
	   	<tr>
  	   		<td align="left">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="left">
				<logic:equal name="tranBalAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="tranBalAdapterVO" property="act" value="agregar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
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
