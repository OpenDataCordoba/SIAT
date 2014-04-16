<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">
          <%@include file="/base/submitForm.js"%>  
</script>

   <!-- Tabla que contiene todos los formularios -->
   <html:form styleId="filter" action="/pad/AdministrarCuenta.do">

      <!-- Mensajes y/o Advertencias -->
      <%@ include file="/base/warning.jsp" %>
      <!-- Errors  -->
      <html:errors bundle="base"/>
      
      <h1><bean:message bundle="pad" key="pad.cuentaAdapter.title"/></h1>

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volverBroche', '');">
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
               <td class="normal"><bean:write name="cuentaAdapterVO" property="cuenta.recurso.desRecurso"/></td>
               <td><label><bean:message bundle="pad" key="pad.objImp.label"/>: </label></td>
               <td class="normal"><bean:write name="cuentaAdapterVO" property="cuenta.objImp.clave"/></td>
            </tr>
            <tr>
               <td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.label"/>: </label></td>
               <td class="normal"><bean:write name="cuentaAdapterVO" property="cuenta.numeroCuenta"/></td>
               <td><label><bean:message bundle="pad" key="pad.cuenta.codGesCue.label"/>: </label></td>
               <td class="normal"><bean:write name="cuentaAdapterVO" property="cuenta.codGesCue"/></td>
            </tr>
            <tr>
               <td><label><bean:message bundle="pad" key="pad.cuenta.fechaAlta.label"/>: </label></td>
               <td class="normal" colspan="3"><bean:write name="cuentaAdapterVO" property="cuenta.fechaAltaView"/></td>
            </tr>
            <tr>
               <td><label><bean:message bundle="pad" key="pad.cuenta.esExcluidaEmision.label"/>: </label></td>
               <td class="normal" colspan="3"><bean:write name="cuentaAdapterVO" property="cuenta.esExcluidaEmision.value"/></td>
            </tr>
         </table>
      </fieldset>
      <!-- Fin Cuenta -->


      <!-- Broche -->
      <fieldset>
         <legend><bean:message bundle="pad" key="pad.broche.title"/></legend>
         <table class="tabladatos">

         <logic:equal name="cuentaAdapterVO" property="poseeBroche" value="false">
			<tr>
				<td class="normal"><label>No posee broche asignado.</label></td>
			</tr>
         </logic:equal>
         
         <logic:equal name="cuentaAdapterVO" property="poseeBroche" value="true">
			<tr>
				<!-- Numero -->		
				<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.nro.label"/>: </label></td>
				<td class="normal"><bean:write name="cuentaAdapterVO" property="cuenta.broche.idView"/></td>					
				<!-- Tipo Broche -->		
				<td><label>&nbsp;<bean:message bundle="pad" key="pad.tipoBroche.label"/>: </label></td>
				<td class="normal"><bean:write name="cuentaAdapterVO" property="cuenta.broche.tipoBroche.desTipoBroche"/></td>
			</tr>
			<!-- Titular o Descripcion-->		
			<tr>
				<logic:equal name="cuentaAdapterVO" property="cuenta.broche.tipoBroche.id" value="1">	<!-- TipoBroche=Administrativo -->	
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.titular.label"/>: </label></td>
				</logic:equal>
				<logic:notEqual name="cuentaAdapterVO" property="cuenta.broche.tipoBroche.id" value="1">	<!-- TipoBroche<>Administrativo -->		
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.desBroche.label"/>: </label></td>
				</logic:notEqual>
				<td class="normal"><bean:write name="cuentaAdapterVO" property="cuenta.broche.desBroche"/></td>					
			</tr>
			
			<!-- Inclucion de Caso -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="cuentaAdapterVO" property="cuenta.broCue"/>
					<bean:define id="voName" value="cuenta.broCue" />
					<%@ include file="/cas/caso/includeCaso.jsp" %>
				</td>
			</tr>
			<!-- Fin Inclucion de Caso -->
		
			<tr>
				<!-- Fecha Alta -->
				<td><label>&nbsp;<bean:message bundle="pad" key="pad.broCue.fechaAlta.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cuentaAdapterVO" property="cuenta.broCue.fechaAltaView"/></td>					
			</tr>
		  </logic:equal>

         </table>
      </fieldset>
      <!-- Fin Broche -->

      
      <table class="tablabotones" width="100%">
            <tr>
                 <td align="left" width="50%">
                   <html:button property="btnVolver" styleClass="boton" onclick="submitForm('volverBroche', '');">
                      <bean:message bundle="base" key="abm.button.volver"/>
                   </html:button>
                </td>
               <td align="right" width="50%">
	               <logic:equal name="cuentaAdapterVO" property="act" value="modificar">
	                  <html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('guardarAsignarBroche', '');">
	                     <bean:message bundle="base" key="abm.button.asignar"/>
	                  </html:button>
	               </logic:equal>
	               
	               <logic:equal name="cuentaAdapterVO" property="act" value="modificarBrocheInit">
	                   <input type="button" class="boton" onClick="submitForm('modificarBroche', 
							'<bean:write name="cuentaAdapterVO" property="cuenta.broCue.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.modificar"/>"/>
	               </logic:equal>
	               
                </td>
             </tr>
      </table>
         
      <input type="hidden" name="method" value=""/>
<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

      <input type="hidden" name="selectedId" value=""/>
      <input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclucion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
   </html:form>
   <!-- Fin Tabla que contiene todos los formularios -->
