<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarDesEspExe.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.desEspExeViewAdapter.title"/></h1>
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- DesEspExe -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.desEspExe.title"/></legend>
			<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="gde" key="gde.desEspExe.fechaDesde.label"/>: </label></td>
				<td class="normal"><bean:write name="desEspExeAdapterVO" property="desEspExe.fechaDesdeView"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.desEspExe.fechaHasta.label"/>: </label></td>
				<td class="normal"><bean:write name="desEspExeAdapterVO" property="desEspExe.fechaHastaView"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="exe" key="exe.exencion.codExencion.ref"/>: </label></td>
				<td class="normal"><bean:write name="desEspExeAdapterVO" property="desEspExe.exencion.codExencion"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="exe" key="exe.exencion.desExencion.ref"/>: </label></td>
				<td class="normal"><bean:write name="desEspExeAdapterVO" property="desEspExe.exencion.desExencion"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
				<td class="normal"><bean:write name="desEspExeAdapterVO" property="desEspExe.estado.value"/></td>
			</tr>
				<!-- <#BeanWrites#> -->
			</table>
		</fieldset>	
		<!-- DesEspExe -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="desEspExeAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="desEspExeAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="desEspExeAdapterVO" property="act" value="desactivar">
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
