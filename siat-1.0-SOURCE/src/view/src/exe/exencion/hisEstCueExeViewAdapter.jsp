<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/exe/AdministrarHisEstCueExe.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="exe" key="exe.hisEstCueExeViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- HisEstCueExe -->
		<fieldset>
			<legend><bean:message bundle="exe" key="exe.hisEstCueExe.title"/></legend>
			<table class="tabladatos">
				<!-- Fecha -->
				<tr>
					<td><label><bean:message bundle="exe" key="exe.hisEstCueExe.fecha.label"/>: </label></td>
					<td class="normal"><bean:write name="hisEstCueExeAdapterVO" property="hisEstCueExe.fechaView"/></td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="hisEstCueExeAdapterVO" property="hisEstCueExe.estadoCueExe.desEstadoCueExe"/></td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="exe" key="exe.hisEstCueExe.observaciones.label"/>: </label></td>
					<td class="normal"><bean:write name="hisEstCueExeAdapterVO" property="hisEstCueExe.observaciones"/></td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="exe" key="exe.hisEstCueExe.logCambios.label"/>: </label></td>
					<td class="normal"><bean:write name="hisEstCueExeAdapterVO" property="hisEstCueExe.logCambios"/></td>
				</tr>
				
			</table>
		</fieldset>	
		<!-- HisEstCueExe -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="hisEstCueExeAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
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
<!-- hisEstCueExeViewAdapter.jsp -->