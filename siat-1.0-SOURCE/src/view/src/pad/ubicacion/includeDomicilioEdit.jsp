<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Domicilio -->
	<fieldset>
		<legend>
     		<logic:empty name="domicilioEnvio">
     			<bean:message bundle="pad" key="pad.domicilio.title"/>
	     	</logic:empty>
	     	<logic:notEmpty name="domicilioEnvio">
	     		<bean:message bundle="pad" key="pad.domicilio.envio.title"/>
	     	</logic:notEmpty>
	    </legend>
		
		<table class="tabladatos">			
			<tr>   <!-- Localidad -->
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.localidad.label"/>: </label></td>
				<td class="normal" colspan="3">										
					<html:text name="domicilioVO" property="localidad.descripcionPostal" size="20" maxlength="20" disabled="true" />				 	
				 	<html:button property="btnBucarLocalidad"  styleClass="boton" onclick="submitForm('buscarLocalidad', '');">
						<bean:message bundle="pad" key="pad.domicilioEditAdapter.button.buscarLocalidad"/>						
					</html:button>
				</td>
			</tr>

			<!--  codPostal para localidades fuera de Rosario -->
			<logic:equal name="domicilioVO" property="localidad.esRosario" value="false">
				<tr>
					<td><label><bean:message bundle="pad" key="pad.localidad.codPostal.label"/>:</label></td>
					<td class="normal" colspan="3">
						<html:text name="domicilioVO" property="codPostalFueraRosario" size="12" maxlength="10" styleClass="datos"/>
					</td>				
			</logic:equal>

						
			<tr> <!-- Calle -->
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.calle.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="domicilioVO" property="calle.nombreCalle" size="20" maxlength="25"/>
					<logic:equal  name="domicilioVO" property="localidad.esRosario" value="true" >
						<html:button property="btnBuscarCalle"  styleClass="boton" onclick="submitForm('buscarCalle', '');">
							<bean:message bundle="pad" key="pad.domicilioEditAdapter.button.buscarCalle"/>
						</html:button>
					</logic:equal>
					<logic:notEqual  name="domicilioVO" property="localidad.esRosario" value="true" >
						<html:button property="btnValidarDomicilio"  styleClass="boton" disabled="true">
							<bean:message bundle="pad" key="pad.domicilioEditAdapter.button.buscarCalle"/>
						</html:button>
					</logic:notEqual>	
				</td>
			</tr>	
			<tr>	
				<!-- Numero -->
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.domicilio.numero.label"/>: </label></td>
				<td class="normal" colspan="3"><html:text name="domicilioVO" property="numeroView" size="20" maxlength="5"/></td>
			</tr>
			<tr> <!-- Letra -->
				<td><label><bean:message bundle="pad" key="pad.domicilio.letraCalle.label"/>: </label></td>
				<td class="normal"><html:text name="domicilioVO" property="letraCalle" size="20" maxlength="1"/></td>
				<!-- Bis -->
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.domicilio.bis.label"/>: </label></td>
				<td class="normal">
					<html:select name="domicilioVO" property="bis.id" styleClass="select">
						<html:optionsCollection name="listSiNo" label="value" value="id" />
					</html:select>
				</td>
			</tr>
			<tr> <!-- Piso -->
				<td><label><bean:message bundle="pad" key="pad.domicilio.piso.label"/>: </label></td>
				<td class="normal"><html:text name="domicilioVO" property="piso" size="20" maxlength="2"/></td>
				<!-- Departamento -->
				<td><label><bean:message bundle="pad" key="pad.domicilio.depto.label"/>: </label></td>
				<td class="normal"><html:text name="domicilioVO" property="depto" size="20" maxlength="4"/></td>
			</tr>
			<tr> <!-- Monoblock -->
				<td><label><bean:message bundle="pad" key="pad.domicilio.monoblock.label"/>: </label></td>
				<td class="normal"><html:text name="domicilioVO" property="monoblock" size="20" maxlength="4"/></td>
			</tr>
			<tr>	
				<!-- RefGeografica -->
				<td><label><bean:message bundle="pad" key="pad.domicilio.refGeografica.label"/>: </label></td>
				<td class="normal" colspan="3"><html:text name="domicilioVO" property="refGeografica" size="50" maxlength="100"/></td>
			</tr>
			<tr>
				<td colspan="4" align="right">
					<logic:equal  name="domicilioVO" property="localidad.esRosario" value="true" >
						<html:button property="btnValidarDomicilio"  styleClass="boton" onclick="submitForm('validarDomicilio', '');">
							<bean:message bundle="pad" key="pad.domicilioEditAdapter.button.validarDomicilio"/>
						</html:button>
					</logic:equal>
					<logic:notEqual  name="domicilioVO" property="localidad.esRosario" value="true" >
						<html:button property="btnValidarDomicilio"  styleClass="boton" disabled="true">
							<bean:message bundle="pad" key="pad.domicilioEditAdapter.button.validarDomicilio"/>
						</html:button>
					</logic:notEqual>
				</td>
			</tr>
		</table>
	</fieldset>	
	<!-- Domicilio -->