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
            <td><label><bean:message bundle="pad" key="pad.localidad.label"/>: </label></td>
            <td class="normal" colspan="3"><bean:write name="domicilioVO" property="localidad.descripcionPostal"/></td>
         </tr>
         
		<!--  codPostal para localidades fuera de Rosario -->
		<logic:equal name="domicilioVO" property="localidad.esRosario" value="false">
			<tr>
				<td><label><bean:message bundle="pad" key="pad.localidad.codPostal.label"/>:</label></td>
				<td class="normal" colspan="3">
					<bean:write name="domicilioVO" property="codPostalFueraRosario"/>
				</td>				
		</logic:equal>
         
         <tr> <!-- Calle -->
            <td><label><bean:message bundle="pad" key="pad.calle.label"/>: </label></td>
            <td class="normal"><bean:write name="domicilioVO" property="calle.nombreCalle"/></td>
            <!-- Numero -->
            <td><label><bean:message bundle="pad" key="pad.domicilio.numero.label"/>: </label></td>
            <td class="normal"><bean:write name="domicilioVO" property="numeroView"/></td>
         </tr>
         <tr> <!-- Letra -->
            <td><label><bean:message bundle="pad" key="pad.domicilio.letraCalle.label"/>: </label></td>
            <td class="normal"><bean:write name="domicilioVO" property="letraCalle" /></td>
            <!-- Bis -->
            <td><label><bean:message bundle="pad" key="pad.domicilio.bis.label"/>: </label></td>
            <td class="normal"><bean:write name="domicilioVO" property="bis.value" /></td>
         </tr>
         <tr> <!-- Piso -->
            <td><label><bean:message bundle="pad" key="pad.domicilio.piso.label"/>: </label></td>
            <td class="normal"><bean:write name="domicilioVO" property="piso" /></td>
            <!-- Departamento -->
            <td><label><bean:message bundle="pad" key="pad.domicilio.depto.label"/>: </label></td>
            <td class="normal"><bean:write name="domicilioVO" property="depto" /></td>
         </tr>
         <tr> <!-- Monoblock -->
            <td><label><bean:message bundle="pad" key="pad.domicilio.monoblock.label"/>: </label></td>
            <td class="normal"><bean:write name="domicilioVO" property="monoblock" /></td>
         </tr>
         <tr> <!-- RefGeografica -->
            <td><label><bean:message bundle="pad" key="pad.domicilio.refGeografica.label"/>: </label></td>
            <td class="normal" colspan="3"><bean:write name="domicilioVO" property="refGeografica" /></td>
         </tr>
         
         <logic:notEmpty name="act">
         	<tr>
         		<td colspan="4" align="right">
    				<logic:equal name="act" value="agregar">
         				<!-- Agregar -->
         				<logic:notEmpty name="agregarDomicilioEnabled">
         					<logic:equal name="agregarDomicilioEnabled" value="enabled">
								<html:button property="btnAccionAgregarDomicilio"  styleClass="boton" onclick="submitForm('agregarDomicilio', '');">
				                     <bean:message bundle="base" key="abm.button.agregar"/>
                  				</html:button>
         					</logic:equal >
         					<logic:notEqual name="agregarDomicilioEnabled" value="enabled">
								<html:button property="btnAccionAgregarDomicilio" disabled="true" styleClass="boton" >
				                     <bean:message bundle="base" key="abm.button.agregar"/>
                  				</html:button>
         					</logic:notEqual >
         				</logic:notEmpty >
	  					
         			</logic:equal>	
         			<logic:equal name="act" value="modificar">
         				<!-- Modificar -->
         				<logic:notEmpty name="modificarDomicilioEnabled">
         					<logic:equal name="modificarDomicilioEnabled" value="enabled">
								<html:button property="btnAccionModificarDomicilio"  styleClass="boton" onclick="submitForm('modificarDomicilio', '');">
				                     <bean:message bundle="base" key="abm.button.modificar"/>
                  				</html:button>
         					</logic:equal >
         					<logic:notEqual name="modificarDomicilioEnabled" value="enabled">
								<html:button property="btnAccionModificarDomicilio" disabled="true" styleClass="boton" >
				                     <bean:message bundle="base" key="abm.button.modificar"/>
                  				</html:button>
         					</logic:notEqual >
         				</logic:notEmpty >
         			</logic:equal>	
         		</td>
         	</tr>
         </logic:notEmpty >
         
      </table>
  </fieldset>   
<!-- Domicilio -->