<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/afi/AdministrarOtrosPagos.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="afi" key="afi.otrosPagosViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- OtrosPagos -->
		<fieldset>
			<legend><bean:message bundle="afi" key="afi.otrosPagos.title"/></legend>
			<table class="tabladatos">				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.otrosPagos.numeroCuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="otrosPagosAdapterVO" property="otrosPagos.numeroCuenta"/></td>
			
					<td><label><bean:message bundle="afi" key="afi.otrosPagos.tipoPago.label"/>: </label></td>
					<td class="normal"><bean:write name="otrosPagosAdapterVO" property="otrosPagos.tipoPagoView"/></td>
				</tr>				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.otrosPagos.fechaPago.label"/>: </label></td>
					<td class="normal"><bean:write name="otrosPagosAdapterVO" property="otrosPagos.fechaPagoView"/></td>
			
					<td><label><bean:message bundle="afi" key="afi.otrosPagos.periodoPago.label"/>: </label></td>
					<td class="normal"><bean:write name="otrosPagosAdapterVO" property="otrosPagos.periodoPagoView"/></td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.otrosPagos.nroResolucion.label"/>: </label></td>
					<td class="normal"><bean:write name="otrosPagosAdapterVO" property="otrosPagos.nroResolucion"/></td>
			
					<td><label><bean:message bundle="afi" key="afi.otrosPagos.anio.label"/>: </label></td>
					<td class="normal"><bean:write name="otrosPagosAdapterVO" property="otrosPagos.anioView"/></td>
				</tr>				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.otrosPagos.importePago.label"/>: </label></td>
					<td class="normal"><bean:write name="otrosPagosAdapterVO" property="otrosPagos.importePagoView"/></td>			
				</tr>
			</table>
		</fieldset>	
		<!-- OtrosPagos -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	   <logic:equal name="otrosPagosAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>					
	   	    	</td>
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='otrosPagosAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->