<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>
   	    <%@include file="/base/calendar.js"%>
   	       	    
</script>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/AdministrarDomicilio.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
		
	<!-- Domicilio -->
	<fieldset>
		<legend><bean:message bundle="pad" key="pad.domicilio.title"/></legend>
		
		<table class="tabladatos">			
			<tr>   <!-- Localidad -->
				<td><label><bean:message bundle="pad" key="pad.localidad.label"/>: </label></td>
				<td class="normal" colspan="3">										
					<html:text name="domicilioAdapterVO" property="domicilio.localidad.descripcionPostal" size="15" maxlength="20" disabled="true" />				 	
				 	<html:button property="btnBucarLocalidad"  styleClass="boton" onclick="submitForm('buscarLocalidad', '');">
						<bean:message bundle="pad" key="pad.domicilioEditAdapter.button.buscarLocalidad"/>						
					</html:button>
				</td>
			</tr>			
			<tr> <!-- Calle -->
				<td><label><bean:message bundle="pad" key="pad.calle.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="domicilioAdapterVO" property="domicilio.calle.nombreCalle" size="20" maxlength="100"/>
					<logic:equal  name="domicilioAdapterVO" property="domicilio.localidad.esRosario" value="true" >
						<html:button property="btnBuscarCalle"  styleClass="boton" onclick="submitForm('buscarCalle', '');">
							<bean:message bundle="pad" key="pad.domicilioEditAdapter.button.buscarCalle"/>
						</html:button>
					</logic:equal>
					<logic:notEqual  name="domicilioAdapterVO" property="domicilio.localidad.esRosario" value="true" >
						<html:button property="btnValidarDomicilio"  styleClass="boton" disabled="true">
							<bean:message bundle="pad" key="pad.domicilioEditAdapter.button.buscarCalle"/>
						</html:button>
					</logic:notEqual>	
				</td>
			</tr>	
			<tr>	
				<!-- Numero -->
				<td><label><bean:message bundle="pad" key="pad.documento.numero.label"/>: </label></td>
				<td class="normal" colspan="3"><html:text name="domicilioAdapterVO" property="domicilio.numeroView" size="20" maxlength="100"/></td>
			</tr>
			<tr> <!-- Letra -->
				<td><label><bean:message bundle="pad" key="pad.domicilio.letraCalle.label"/>: </label></td>
				<td class="normal"><html:text name="domicilioAdapterVO" property="domicilio.letraCalle" size="20" maxlength="2"/></td>
				<!-- Bis -->
				<td><label><bean:message bundle="pad" key="pad.domicilio.bis.label"/>: </label></td>
				<td class="normal">
					<html:select name="domicilioAdapterVO" property="domicilio.bis.id" styleClass="select">
						<html:optionsCollection name="domicilioAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
			</tr>
			<tr> <!-- Piso -->
				<td><label><bean:message bundle="pad" key="pad.domicilio.piso.label"/>: </label></td>
				<td class="normal"><html:text name="domicilioAdapterVO" property="domicilio.piso" size="20" maxlength="100"/></td>
				<!-- Departamento -->
				<td><label><bean:message bundle="pad" key="pad.domicilio.depto.label"/>: </label></td>
				<td class="normal"><html:text name="domicilioAdapterVO" property="domicilio.depto" size="20" maxlength="100"/></td>
			</tr>
			<tr> <!-- Monoblock -->
				<td><label><bean:message bundle="pad" key="pad.domicilio.monoblock.label"/>: </label></td>
				<td class="normal"><html:text name="domicilioAdapterVO" property="domicilio.monoblock" size="20" maxlength="100"/></td>
			</tr>
			<tr>	
				<!-- RefGeografica -->
				<td><label><bean:message bundle="pad" key="pad.domicilio.refGeografica.label"/>: </label></td>
				<td class="normal" colspan="3"><html:text name="domicilioAdapterVO" property="domicilio.refGeografica" size="50" maxlength="100"/></td>
			</tr>
			
			<tr>
				<td colspan="4" align="right">
					<logic:equal  name="domicilioAdapterVO" property="domicilio.localidad.esRosario" value="true" >
						<html:button property="btnValidarDomicilio"  styleClass="boton" onclick="submitForm('validarDomicilio', '');">
							<bean:message bundle="pad" key="pad.domicilioEditAdapter.button.validarDomicilio"/>
						</html:button>
					</logic:equal>
					<logic:notEqual  name="domicilioAdapterVO" property="domicilio.localidad.esRosario" value="true" >
						<html:button property="btnValidarDomicilio"  styleClass="boton" disabled="true">
							<bean:message bundle="pad" key="pad.domicilioEditAdapter.button.validarDomicilio"/>
						</html:button>
					</logic:notEqual>
				<td>
			</tr>
			
		</table>
		
	</fieldset>	
	<!-- Domicilio -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
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
