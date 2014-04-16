<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/afi/AdministrarDatosPagoCta.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="afi" key="afi.datosPagoCtaViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- DatosPagoCta -->
		<fieldset>
			<legend><bean:message bundle="afi" key="afi.datosPagoCta.title"/></legend>
			<table class="tabladatos">			
					<tr>
					<td><label><bean:message bundle="afi" key="afi.datosPagoCta.numeroCuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="datosPagoCtaAdapterVO" property="datosPagoCta.numeroCuenta"/></td>
		
					<td><label><bean:message bundle="afi" key="afi.datosPagoCta.idCuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="datosPagoCtaAdapterVO" property="datosPagoCta.idCuentaView"/></td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.datosPagoCta.codImpuesto.label"/>: </label></td>
					<td class="normal"><bean:write name="datosPagoCtaAdapterVO" property="datosPagoCta.codImpuestoView"/></td>
		
					<td><label><bean:message bundle="afi" key="afi.datosPagoCta.totalMontoIngresado.label"/>: </label></td>
					<td class="normal"><bean:write name="datosPagoCtaAdapterVO" property="datosPagoCta.totalMontoIngresadoView"/></td>
				</tr>
				
			</table>
		</fieldset>	
		<!-- DatosPagoCta -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	   <logic:equal name="datosPagoCtaAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>				
	   	    	</td>
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='datosPagoCtaAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->