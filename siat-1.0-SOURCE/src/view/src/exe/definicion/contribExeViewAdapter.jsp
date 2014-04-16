<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/exe/AdministrarContribExe.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="exe" key="exe.contribExeViewAdapter.title"/></h1>
	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<logic:notEqual name="contribExeAdapterVO" property="act" value="quitarBrocheInit">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	 	    	 </logic:notEqual>
	   	    	 <logic:equal name="contribExeAdapterVO" property="act" value="quitarBrocheInit">
	                <html:button property="btnVolver" styleClass="boton" onclick="submitForm('volverBroche', '');">
	                     <bean:message bundle="base" key="abm.button.volver"/>
	                </html:button>
	             </logic:equal>
			</td>
		</tr>
	</table>
	
	<!-- Contribuyente -->
    <fieldset>
    	<legend><bean:message bundle="pad" key="pad.contribuyente.title"/></legend>
        <table class="tabladatos">
			<bean:define id="personaVO" name="contribExeAdapterVO" property="contribExe.contribuyente.persona"/>
			<%@ include file="/pad/persona/includePersonaReducida.jsp"%>
		</table>
	</fieldset>


      <!-- Broche -->
      <fieldset>
         <legend><bean:message bundle="pad" key="pad.broche.title"/></legend>
         <table class="tabladatos">

         <logic:equal name="contribExeAdapterVO" property="poseeBroche" value="false">
			<tr>
				<td class="normal"><label>No posee broche asignado.</label></td>
			</tr>
         </logic:equal>
         
         <logic:equal name="contribExeAdapterVO" property="poseeBroche" value="true">
			<tr>
				<!-- Numero -->		
				<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.nro.label"/>: </label></td>
				<td class="normal"><bean:write name="contribExeAdapterVO" property="contribExe.broche.idView"/></td>					
				<!-- Tipo Broche -->		
				<td><label>&nbsp;<bean:message bundle="pad" key="pad.tipoBroche.label"/>: </label></td>
				<td class="normal"><bean:write name="contribExeAdapterVO" property="contribExe.broche.tipoBroche.desTipoBroche"/></td>
			</tr>
			<!-- Titular o Descripcion-->		
			<tr>
				<logic:equal name="contribExeAdapterVO" property="contribExe.broche.tipoBroche.id" value="1">	<!-- TipoBroche=Administrativo -->	
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.titular.label"/>: </label></td>
				</logic:equal>
				<logic:notEqual name="contribExeAdapterVO" property="contribExe.broche.tipoBroche.id" value="1">	<!-- TipoBroche<>Administrativo -->		
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.desBroche.label"/>: </label></td>
				</logic:notEqual>
				<td class="normal"><bean:write name="contribExeAdapterVO" property="contribExe.broche.desBroche"/></td>					
			</tr>
		  </logic:equal>

         </table>
      </fieldset>
      <!-- Fin Broche -->




	<!-- ContribExe -->
	<fieldset>
		<legend><bean:message bundle="exe" key="exe.exencion.title"/></legend>
		
		<table class="tabladatos">
			<!-- Descripcion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.contribExe.desContribExe.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="contribExeAdapterVO" property="contribExe.desContribExe"/></td>
			</tr>
			<!-- Exencion -->
			<tr>	
				<td><label><bean:message bundle="exe" key="exe.exencion.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="contribExeAdapterVO" property="contribExe.exencion.desExencion"/></td>				
			</tr>
			
			<!-- Fecha Desde/Hasta -->			
			<tr>
				<td><label><bean:message bundle="exe" key="exe.contribExe.fechaDesde.label"/>: </label></td>
				<td class="normal"><bean:write name="contribExeAdapterVO" property="contribExe.fechaDesdeView"/></td>				
				<td><label><bean:message bundle="exe" key="exe.contribExe.fechaHasta.label"/>: </label></td>
				<td class="normal"><bean:write name="contribExeAdapterVO" property="contribExe.fechaHastaView"/></td>
			</tr>
		
		</table>
	</fieldset>	
	<!-- ContribExe -->

	<table class="tablabotones" width="100%">
    	<tr>
 	    	<td align="left" width="50%">
				<logic:notEqual name="contribExeAdapterVO" property="act" value="quitarBrocheInit">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	 	    	 </logic:notEqual>
	   	    	 <logic:equal name="contribExeAdapterVO" property="act" value="quitarBrocheInit">
	                <html:button property="btnVolver" styleClass="boton" onclick="submitForm('volverBroche', '');">
	                     <bean:message bundle="base" key="abm.button.volver"/>
	                </html:button>
	             </logic:equal>
   	    	</td>   	    	
   	    	<td align="right" width="50%">
   	    	
   	    	    <logic:equal name="contribExeAdapterVO" property="act" value="quitarBrocheInit">
					<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('quitarBroche', '');">
						<bean:message bundle="base" key="abm.button.quitar"/>
					</html:button>
				</logic:equal>
				
				<logic:equal name="contribExeAdapterVO" property="act" value="eliminar">
					<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
						<bean:message bundle="base" key="abm.button.eliminar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="contribExeAdapterVO" property="act" value="activar">
					<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
						<bean:message bundle="base" key="abm.button.activar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="contribExeAdapterVO" property="act" value="desactivar">
					<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
						<bean:message bundle="base" key="abm.button.desactivar"/>
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
	
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
