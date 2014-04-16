<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">
          <%@include file="/base/submitForm.js"%>  
</script>

   <!-- Tabla que contiene todos los formularios -->
   <html:form styleId="filter" action="/pad/AdministrarEncCuenta.do">

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
               <td class="normal"><bean:write name="encCuentaAdapterVO" property="cuenta.recurso.desRecurso"/></td>
               <td><label><bean:message bundle="pad" key="pad.objImp.label"/>: </label></td>
               <td class="normal"><bean:write name="encCuentaAdapterVO" property="cuenta.objImp.clave"/></td>
            </tr>
            <tr>
               <td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.label"/>: </label></td>
               <td class="normal"><bean:write name="encCuentaAdapterVO" property="cuenta.numeroCuenta"/></td>
               <td><label><bean:message bundle="pad" key="pad.cuenta.codGesCue.label"/>: </label></td>
               <td class="normal"><bean:write name="encCuentaAdapterVO" property="cuenta.codGesCue"/></td>
            </tr>
            <tr>
               <td><label><bean:message bundle="pad" key="pad.cuenta.fechaAlta.label"/>: </label></td>
               <td class="normal"><bean:write name="encCuentaAdapterVO" property="cuenta.fechaAltaView"/></td>
           		<td><label><bean:message bundle="pad" key="pad.cuenta.fechaBaja.label"/>: </label></td>
				<td class="normal"><bean:write name="encCuentaAdapterVO" property="cuenta.fechaBajaView" /></td>
            </tr>
         </table>
      </fieldset>
      <!-- Fin Cuenta -->
      
      <bean:define id="domicilioVO" name="encCuentaAdapterVO" property="cuenta.domicilioEnvio"/>
      <bean:define id="listSiNo" name="encCuentaAdapterVO" property="listSiNo"/>
      <bean:define id="domicilioEnvio" value="true"/> <!-- indica que se trata de domicilio de envio -->
      <%@ include file="/pad/ubicacion/includeDomicilioEdit.jsp" %>
    	      
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:notEmpty name="encCuentaAdapterVO" property="cuenta.domicilioEnvio.id">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificarDomicilioEnvio', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:notEmpty >
				<logic:empty name="encCuentaAdapterVO" property="cuenta.domicilioEnvio.id">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregarDomicilioEnvio', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
					</html:button>
				</logic:empty >
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