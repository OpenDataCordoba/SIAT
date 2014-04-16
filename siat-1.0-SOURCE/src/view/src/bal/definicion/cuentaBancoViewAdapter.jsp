<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarCuentaBanco.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.cuentaBancoViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- CuentaBanco -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.cuentaBanco.title"/></legend>
			<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="bal" key="bal.cuentaBanco.nroCuenta.label"/>: </label></td>
				<td class="normal"><bean:write name="cuentaBancoAdapterVO" property="cuentaBanco.nroCuenta"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.banco.label"/>: </label></td>
				<td class="normal"><bean:write name="cuentaBancoAdapterVO" property="cuentaBanco.banco.desBanco"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.area.label"/>: </label></td>
				<td class="normal"><bean:write name="cuentaBancoAdapterVO" property="cuentaBanco.area.desArea"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.cuentaBanco.tipCueBan.label"/>: </label></td>
				<td class="normal"><bean:write name="cuentaBancoAdapterVO" property="cuentaBanco.tipCueBan.descripcion"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.cuentaBanco.observaciones.label"/>: </label></td>
				<td class="normal"><bean:write name="cuentaBancoAdapterVO" property="cuentaBanco.observaciones"/></td>				
			</tr>
				<!-- <#BeanWrites#> -->
			</table>
		</fieldset>	
		<!-- CuentaBanco -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	   <logic:equal name="cuentaBancoAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
					<logic:equal name="cuentaBancoAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="cuentaBancoAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="cuentaBancoAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='cuentaBancoAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
