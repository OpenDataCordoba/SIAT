<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/gde/AdministrarConDeuTit.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.constanciaDeuAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
	 <!-- ConstanciaDeu -->	
 		<bean:define id="act" name="conDeuTitAdapterVO" property="act"/>	 		
 		<bean:define id="constanciaDeu" name="conDeuTitAdapterVO" property="conDeuTit.constanciaDeu"></bean:define>
		<%@ include file="/gde/adminDeuJud/includeConDeuViewDatos.jsp" %>
	 <!-- ConstanciaDeu -->
				
     <!-- Domicilio de envio -->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.domicilio.envio.title"/></legend>
			<tr>&nbsp </tr>
			<tr>
		    	<td><label><bean:message bundle="pad" key="pad.domicilio.label"/>: </label></td>
		    	<td class="normal"><bean:write name="conDeuTitAdapterVO" property="strDomEnv"/></td>
		    </tr>
	    </fieldset>
	 <!-- Fin Domicilio de Envio -->

	  <!-- Datos Titular -->
	  	<fieldset>
			<legend><bean:message bundle="gde" key="gde.constanciaDeu.conDeuTit.persona.label"/></legend>
			<table class="tabladatos">
	           	<tbody>
					<logic:equal name="conDeuTitAdapterVO" property="conDeuTit.persona.esPersonaFisica" value="true">
		               <tr>	
		               		<td><label><bean:message bundle="pad" key="pad.persona.apellido.label"/>:&nbsp;</label></td>
							<td class="normal"><bean:write name="conDeuTitAdapterVO" property="conDeuTit.persona.apellido"/></td>
							<td><label><bean:message bundle="pad" key="pad.persona.nombre.label"/>:&nbsp;</label></td>							
							<td class="normal"><bean:write name="conDeuTitAdapterVO" property="conDeuTit.persona.nombres" /></td>
						</tr>
						<tr>
							<td><label><bean:message bundle="pad" key="pad.tipoDocumento.label"/>:&nbsp;</td>	
							<td class="normal"><bean:write name="conDeuTitAdapterVO" property="conDeuTit.persona.documento.tipoDocumento.abreviatura"/></td>
							<td><label><bean:message bundle="pad" key="pad.documento.numero.ref"/>:&nbsp;</label></td>
							<td class="normal"><bean:write name="conDeuTitAdapterVO" property="conDeuTit.persona.documento.numeroView"/></td>
						</tr>
					</logic:equal>
					<logic:notEqual name="conDeuTitAdapterVO" property="conDeuTit.persona.esPersonaFisica" value="true">
						<tr>
							<td><label><bean:message bundle="pad" key="pad.persona.razonSocial.label"/>:&nbsp;</td>	
							<td class="normal" colspan="3"><bean:write name="conDeuTitAdapterVO" property="conDeuTit.persona.razonSocial"/></td>
						</tr>
					</logic:notEqual>
						
					<tr>	
						<td><label><bean:message bundle="pad" key="pad.persona.cuit.label"/>:&nbsp;</label></td>
						<td class="normal" colspan="3"><bean:write name="conDeuTitAdapterVO" property="conDeuTit.persona.cuit"/></td>
					</tr>
				</tbody>
			</table>
		</fieldset>

	  <!-- FIN Datos Titular -->
	  
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left"  width="50%">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right" width="50%">
					<logic:equal name="conDeuTitAdapterVO" property="act" value="eliminar">
						<logic:equal name="conDeuTitAdapterVO" property="eliminarEnabled" value="enabled">
							<html:button property="btnEliminar" onclick="submitForm('eliminar', '');" styleClass="boton">
								<bean:message bundle="base" key="abm.button.eliminar"/>" 
							</html:button>	
						</logic:equal>
					</logic:equal>												
				</td>   	    			
			</tr>			
		</table>
	   	
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
