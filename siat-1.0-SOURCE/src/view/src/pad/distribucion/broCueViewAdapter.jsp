<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/AdministrarBroCue.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="pad" key="pad.broCueAdapter.title"/></h1>		
	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>

  <!-- Broche -->      
  <fieldset>
        <legend><bean:message bundle="pad" key="pad.broche.title"/></legend>
        <table class="tabladatos">
		<!-- Numero -->		
		<tr>
			<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.nro.label"/>: </label></td>
			<td class="normal"><bean:write name="broCueAdapterVO" property="broCue.broche.idView"/></td>					
		</tr>
		<!-- Titular o Descripcion-->		
		<tr>
			<logic:equal name="broCueAdapterVO" property="paramTipoBroche" value="1">	<!-- TipoBroche=Administrativo -->	
				<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.titular.label"/>: </label></td>
			</logic:equal>
			<logic:notEqual name="broCueAdapterVO" property="paramTipoBroche" value="1">	<!-- TipoBroche<>Administrativo -->		
				<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.desBroche.label"/>: </label></td>
			</logic:notEqual>
			<td class="normal"><bean:write name="broCueAdapterVO" property="broCue.broche.desBroche"/></td>					
		</tr>
		<logic:equal name="broCueAdapterVO" property="paramTipoBroche" value="1">	<!-- TipoBroche=Administrativo -->	
		<!-- Domicilio -->		
		<tr>
			<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.strDomicilioEnvio.label"/>: </label></td>
			<td class="normal"><bean:write name="broCueAdapterVO" property="broCue.broche.strDomicilioEnvio"/></td>					
		</tr>
		<!-- Telefono -->		
		<tr>
			<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.telefono.label"/>: </label></td>
			<td class="normal"><bean:write name="broCueAdapterVO" property="broCue.broche.telefono"/></td>					
		</tr>
		</logic:equal>
		<!-- Repartidor -->	
		<tr>
			<td><label>&nbsp;<bean:message bundle="pad" key="pad.repartidor.label"/>: </label></td>
			<td class="normal">
				<logic:notEqual name="broCueAdapterVO" property="paramTipoBroche" value="2">	<!-- TipoBroche<>'Repartidor Fuera de Zona' & TipoBroche<>'Repartido Zona' -->		
					<bean:write name="broCueAdapterVO" property="broCue.broche.repartidor.descripcionForCombo"/>
				</logic:notEqual>	
			</td>	
		</tr>	
     </table>
     </fieldset>    
  <!-- Fin Broche -->      	
	
  <!-- Cuenta -->
     <fieldset>
        <legend><bean:message bundle="pad" key="pad.cuenta.title"/></legend>
        <table class="tabladatos">
           <tr>
              <td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
              <td class="normal"><bean:write name="broCueAdapterVO" property="broCue.cuenta.recurso.desRecurso"/></td>
              <td><label><bean:message bundle="pad" key="pad.objImp.label"/>: </label></td>
              <td class="normal"><bean:write name="broCueAdapterVO" property="broCue.cuenta.objImp.clave"/></td>
           </tr>
           <tr>
              <td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.label"/>: </label></td>
              <td class="normal"><bean:write name="broCueAdapterVO" property="broCue.cuenta.numeroCuenta"/></td>
              <td><label><bean:message bundle="pad" key="pad.cuenta.codGesCue.label"/>: </label></td>
              <td class="normal"><bean:write name="broCueAdapterVO" property="broCue.cuenta.codGesCue"/></td>
           </tr>
        </table>
     </fieldset>
     <!-- Fin Cuenta -->
     
     <!-- Asignacion a Broche -->
  <fieldset>
        <legend><bean:message bundle="pad" key="pad.broCue.title"/></legend>
        <table class="tabladatos">
		<!-- Inclucion de CasoView -->
		<tr>
			<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
			<td colspan="3">
				<bean:define id="IncludedVO" name="broCueAdapterVO" property="broCue"/>
				<%@ include file="/cas/caso/includeCasoView.jsp" %>				
			</td>
		</tr>
		<!-- Fin Inclucion de CasoView -->	
		<tr>
			<!-- Fecha Alta -->
			<td><label>&nbsp;<bean:message bundle="pad" key="pad.broCue.fechaAlta.label"/>: </label></td>
			<td class="normal"><bean:write name="broCueAdapterVO" property="broCue.fechaAltaView"/></td>					
			<!-- Fecha Baja -->
			<td><label>&nbsp;<bean:message bundle="pad" key="pad.broCue.fechaBaja.label"/>: </label></td>
			<td class="normal"><bean:write name="broCueAdapterVO" property="broCue.fechaBajaView"/></td>					
		</tr>
     </table>
     </fieldset>    
     <!-- Fin Asignacion a Broche -->
     
  		<table class="tablabotones" width="100%">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
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
		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->