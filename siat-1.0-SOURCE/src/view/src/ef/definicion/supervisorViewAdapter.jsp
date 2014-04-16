<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarSupervisor.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.supervisorViewAdapter.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
		
		<!-- supervisor -->
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.supervisor.title"/></legend>
			<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="pad" key="pad.persona.label"/>: </label></td>
				<td class="normal"><bean:write name="supervisorAdapterVO" property="supervisor.desPersona"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.supervisor.desSupervisor.label"/>: </label></td>
				<td class="normal"><bean:write name="supervisorAdapterVO" property="supervisor.desSupervisor"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.supervisor.fechaDesde.label"/>: </label></td>
				<td class="normal"><bean:write name="supervisorAdapterVO" property="supervisor.fechaDesdeView"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.supervisor.fechaHasta.label"/>: </label></td>
				<td class="normal"><bean:write name="supervisorAdapterVO" property="supervisor.fechaHastaView"/></td>				
			</tr>

			</table>
		</fieldset>	
		<!-- supervisor -->
        
		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="supervisorAdapterVO" property="act" value="eliminar">
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
