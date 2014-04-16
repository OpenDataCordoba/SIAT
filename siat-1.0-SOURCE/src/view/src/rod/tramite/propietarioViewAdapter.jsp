<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rod/AdministrarPropietario.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="rod" key="rod.propietarioAdapter.title"/></h1>	

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
		<legend><bean:message bundle="rod" key="rod.c"/></legend>
		
		<table class="tabladatos">
		
			<!-- C -->
			<!-- C-DATOS DEL PROPIETARIO -->
			
	<!-- Propietarios -->		
					
			<!-- Apellido o Razon -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CApellidoORazon.label"/>: </label></td>
				<td class="normal"><bean:write name="propietarioAdapterVO" property="propietario.apellidoORazon"/></td>
							
			</tr>
			<!-- Tipo Doc -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CTipoDoc.label"/>: </label></td>
				<td class="normal"><bean:write name="propietarioAdapterVO" property="propietario.desTipoDoc"/></td>
				
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CNroDoc.label"/>: </label></td>
				<td class="normal"><bean:write name="propietarioAdapterVO" property="propietario.nroDocView"/></td>			
			</tr>
			<tr>
				<!-- Numero Ingreso Bruto -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CNroIB.label"/>: </label></td>
				<td class="normal"><bean:write name="propietarioAdapterVO" property="propietario.nroIBView"/></td>
				
				
			<!-- Número productor agropec. -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CNroProdAgr.label"/>: </label></td>
				<td class="normal"><bean:write name="propietarioAdapterVO" property="propietario.nroProdAgrView"/></td>			
						
			</tr>
			<!-- cuit -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CNroCuit.label"/>: </label></td>
				<td class="normal"><bean:write name="propietarioAdapterVO" property="propietario.nroCuit"/></td>			
			
			
			<!-- Cod Tipo Prop. -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CCodTipoProp.label"/>: </label></td>
				<td class="normal"><bean:write name="propietarioAdapterVO" property="propietario.desTipoProp"/></td>
			</tr>
			<tr>
			<!-- Fecha Nac. -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CFechaNac.label"/>: </label></td>
				<td class="normal"><bean:write name="propietarioAdapterVO" property="propietario.fechaNacView"/></td>	
			</tr>
			<tr>
			<!-- Cod Estado Civil -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CCodEstCiv.label"/>: </label></td>
				<td class="normal"><bean:write name="propietarioAdapterVO" property="propietario.desEstCiv"/></td>
				
			
			<!-- Cod Sexo -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CCodSexo.label"/>: </label></td>
				<td class="normal"><bean:write name="propietarioAdapterVO" property="propietario.desSexo"/></td>
			
			
			</tr>
		</table>
	</fieldset>
		
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
   	    		<logic:equal name="propietarioAdapterVO" property="act" value="eliminar">
					<html:button property="btnEliminar"  styleClass="boton" onclick="submitForm('eliminar', '');">
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

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->