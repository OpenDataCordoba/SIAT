<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarEjercicio.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.ejercicioViewAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
	
		<!-- Ejercicio -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.ejercicio.title"/></legend>
			<table class="tabladatos">
			
			<!-- descripcion -->
			<tr>
				<td><label><bean:message bundle="bal" key="bal.ejercicio.desEjercicio.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="ejercicioAdapterVO" property="ejercicio.desEjercicio"/></td>
			</tr>
			
			<!-- fecha inicio -->
			<tr>
				<td><label><bean:message bundle="bal" key="bal.ejercicio.fecIniEje.label"/>: </label></td>
				<td class="normal"><bean:write name="ejercicioAdapterVO" property="ejercicio.fecIniEjeView"/></td>
				
			<!-- fecha fin -->					
				<td><label><bean:message bundle="bal" key="bal.ejercicio.fecFinEje.label"/>: </label></td>
				<td class="normal"><bean:write name="ejercicioAdapterVO" property="ejercicio.fecFinEjeView"/></td>				
			</tr>
			
			<!-- fecha Cierra -->
			<tr>
				<td><label><bean:message bundle="bal" key="bal.ejercicio.fechaCierre.label"/>: </label></td>
				<td class="normal"><bean:write name="ejercicioAdapterVO" property="ejercicio.fechaCierreView"/></td>				
			
			<!-- estado ejercicio --->
				<td><label><bean:message bundle="bal" key="bal.estEjercicio.desEjeBal.ref"/>: </label></td>
				<td class="normal"><bean:write name="ejercicioAdapterVO" property="ejercicio.estEjercicio.desEjeBal"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="ejercicioAdapterVO" property="ejercicio.estado.value"/></td>
			</tr>
				
				<!-- <#BeanWrites#> -->
			</table>
		</fieldset>	
		<!-- Ejercicio -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="ejercicioAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="ejercicioAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="ejercicioAdapterVO" property="act" value="desactivar">
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
