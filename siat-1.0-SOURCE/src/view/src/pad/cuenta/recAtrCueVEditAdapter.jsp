<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">
          <%@include file="/base/submitForm.js"%>  
</script>

   <!-- Tabla que contiene todos los formularios -->
   <html:form styleId="filter" action="/pad/AdministrarRecAtrCueV.do">

      <!-- Mensajes y/o Advertencias -->
      <%@ include file="/base/warning.jsp" %>
      <!-- Errors  -->
      <html:errors bundle="base"/>
      
      <h1><bean:message bundle="pad" key="pad.cuentaAdapter.title"/></h1>

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
      <!-- Cuenta -->
      <fieldset>
         <legend><bean:message bundle="pad" key="pad.cuenta.title"/></legend>
         <table class="tabladatos">
            <tr>
               <td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
               <td class="normal"><bean:write name="recAtrCueVAdapterVO" property="cuenta.recurso.desRecurso"/></td>
               <td><label><bean:message bundle="pad" key="pad.objImp.label"/>: </label></td>
               <td class="normal"><bean:write name="recAtrCueVAdapterVO" property="cuenta.objImp.clave"/></td>
            </tr>
            <tr>
               <td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.label"/>: </label></td>
               <td class="normal"><bean:write name="recAtrCueVAdapterVO" property="cuenta.numeroCuenta"/></td>
               <td><label><bean:message bundle="pad" key="pad.cuenta.codGesCue.label"/>: </label></td>
               <td class="normal"><bean:write name="recAtrCueVAdapterVO" property="cuenta.codGesCue"/></td>
            </tr>
            <tr>
               <td><label><bean:message bundle="pad" key="pad.cuenta.fechaAlta.label"/>: </label></td>
               <td class="normal"><bean:write name="recAtrCueVAdapterVO" property="cuenta.fechaAltaView"/></td>
               <td><label><bean:message bundle="pad" key="pad.cuenta.fechaBaja.label"/>: </label></td>
               <td class="normal"><bean:write name="recAtrCueVAdapterVO" property="cuenta.fechaBajaView"/></td>
            </tr>
            <tr>
               <td><label><bean:message bundle="pad" key="pad.cuenta.esExcluidaEmision.label"/>: </label></td>
               <td class="normal" colspan="3"><bean:write name="recAtrCueVAdapterVO" property="cuenta.esExcluidaEmision.value"/></td>
            </tr>
            <tr>
               <td><label><bean:message bundle="pad" key="pad.cuenta.permiteImpresion.label"/>: </label></td>
               <td class="normal" colspan="3"><bean:write name="recAtrCueVAdapterVO" property="cuenta.permiteImpresion.value"/></td>
            </tr>
            
            <!-- Observacion -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.observacion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="recAtrCueVAdapterVO" property="cuenta.observacion"/>
				</td>
			</tr>
			
            <!-- Descripcion del domicilio de envio -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.desDomEnv.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="recAtrCueVAdapterVO" property="cuenta.desDomEnv"/>
				</td>
			</tr>

         </table>
      </fieldset>
      <!-- Fin Cuenta -->
		
	
	<fieldset>
		<legend><bean:message bundle="def" key="def.atributo.label"/></legend>
         <table class="tabladatos">
            <tr>
            	<bean:define id="AtrVal" name="recAtrCueVAdapterVO" property="recAtrCueDefinition"/>
            	
				<%@ include file="/def/atrDefinition4Edit.jsp" %>
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
				<html:button property="btnModificar"  styleClass="boton" onclick="submitForm('modificar', '');">
					<bean:message bundle="base" key="abm.button.modificar"/>
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