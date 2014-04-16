<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarDetallePago.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.detallePagoViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button> 
				</td>
			</tr>
		</table>
			
		<!-- DetallePago -->
		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.detallePago.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="bal" key="bal.detallePago.caja.label"/>: </label></td>
					<td class="normal">
						<bean:write name="detallePagoAdapterVO" property="detallePago.cajaView" />
					</td>					
					<td><label><bean:message bundle="bal" key="bal.detallePago.fechaPago.label"/>: </label></td>
					<td class="normal">
						<bean:write name="detallePagoAdapterVO" property="detallePago.fechaPagoView" />
					</td>				
				</tr>
				
				<tr>
					<td><label><bean:message bundle="bal" key="bal.detallePago.numeroCuenta.label"/>: </label></td>
					<td class="normal">
						<bean:write name="detallePagoAdapterVO" property="detallePago.numeroCuenta" />
					</td>
				</tr>
						
				<tr>	
					<td><label><bean:message bundle="bal" key="bal.detallePago.periodo.ref"/>: </label></td>				
					<td class="normal">
						<bean:write name="detallePagoAdapterVO" property="detallePago.periodoView" />						
					</td>	
					<td><label><bean:message bundle="bal" key="bal.detallePago.anio.ref"/>: </label></td>
					<td class="normal">
						<bean:write name="detallePagoAdapterVO" property="detallePago.anioView"/>
					</td>			
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.detallePago.impuesto.ref"/>: </label></td>
					<td class="normal">
						<bean:write name="detallePagoAdapterVO" property="detallePago.impuestoView"/>
					</td>
					<td><label><bean:message bundle="bal" key="bal.detallePago.importePago.ref"/>: </label></td>
					<td class="normal">
						<bean:write name="detallePagoAdapterVO" property="detallePago.importePagoView"/>
					</td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.detallePago.estDetPago.descripcion.ref"/>: </label></td>
					<td class="normal">							
						<bean:write name="detallePagoAdapterVO" property="detallePago.estDetPago.descripcion" />					
					</td>				
				</tr>
			</table>
		</fieldset>	
		
		<!-- DetallePago -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	   
					<logic:equal name="detallePagoAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminarDetallePago', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>	   			
	   	    	</td>	   	    	
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='detallePagoAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->