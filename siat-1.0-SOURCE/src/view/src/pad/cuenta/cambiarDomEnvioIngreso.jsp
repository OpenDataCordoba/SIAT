<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/AdministrarCambiarDomEnvio.do">
	
	<logic:equal name="userSession" property="isAnonimoView" value="1">
		<bean:define id="verCambioDomicilio" value="true" toScope="session" />
		<%@ include file="/gde/gdeuda/includeDivButtons.jsp" %>
	</logic:equal>	

		
	<h1><bean:message bundle="pad" key="pad.cambiarDomEnvioAdapter.title"/> 
		<bean:write name="cambiarDomEnvioAdapterVO" property="cuenta.recurso.desRecurso"/> 
	</h1>	

	<p>
		<bean:message bundle="pad" key="pad.cambiarDomEnvioAdapter.ingreso.legend"/> <br/>
		<bean:message bundle="pad" key="pad.cambiarDomEnvioAdapter.ingreso1.legend"/>
	</p>
	
	<!-- CambiarDomEnvio -->
	<fieldset>
		<legend><bean:message bundle="pad" key="pad.cambiarDomEnvioAdapter.ingreso.title"/></legend>
		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>				

		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="def" key="def.tributo.label"/>: </label></td>			
				<td class="normal" colspan="3"> 
					<!-- DESAHABILITADO y HARCODEADO-->
					<select name="idTributo" disabled="disabled">
                    	<option value="TGI" selected="selected">
                    		<bean:write name="cambiarDomEnvioAdapterVO" property="cuenta.recurso.desRecurso"/>
                    	</option>  
                    </select>
				</td>
			</tr>
			<tr>
				<td><label>(*) <label/><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
				<td class="normal"><html:text name="cambiarDomEnvioAdapterVO" property="cuenta.numeroCuenta" size="20" maxlength="10"/></td>
				
				<logic:equal name="cambiarDomEnvioAdapterVO" property="codGesPerRequerido" value="true">
					<td><label>(*) <label/><label><bean:message bundle="pad" key="pad.cuenta.codGesCue.ref"/>: </label></td>
					<td class="normal"><html:text name="cambiarDomEnvioAdapterVO" property="cuenta.codGesCue" size="20" maxlength="12"/></td>
				</logic:equal>
		      	<logic:equal name="cambiarDomEnvioAdapterVO" property="codGesPerRequerido" value="false">
		      		<td colspan="2">&nbsp;</td>
		      	</logic:equal>	
	      		
			</tr>
			<tr>
				<td align="right" colspan="4">
					<html:button property="btnIngresar"  styleClass="boton" onclick="submitForm('ingresar', '');">
						<bean:message bundle="base" key="abm.button.aceptar"/>
					</html:button>
	   	    	</td>   	    	
			</tr>
			
		</table>
	</fieldset>	
	<!-- CambiarDomEnvio -->
	
   <!-- requerimientos -->
	<br>
		<h3>Requerimientos m&iacute;nimos del Sistema:</h3>
		<ul class="vinieta">
          <li>Impresora (resoluci&oacute;n m&iacute;nima requerida 300 dpi).</li>
          <li>Papel tama&ntilde;o A4; 80 gramos (recomendado).</li>

          <li>Acrobat Reader instalado. [<a href="http://www.adobe.com/products/acrobat/readstep2.html" target="_blank">Descargar el programa</a>]. </li>
        </ul>
	<p>
		<strong>.::</strong>
		<a href="http://www.rosario.gov.ar/gdt/reimpresion/mesa_ayuda.jsp"> Mesa de Ayuda y soporte a trav&eacute;s de correo electr&oacute;nico </a>
	</p>
  <!-- fin requerimientos -->
	
	<input type="hidden" name="method" value=""/>
<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
