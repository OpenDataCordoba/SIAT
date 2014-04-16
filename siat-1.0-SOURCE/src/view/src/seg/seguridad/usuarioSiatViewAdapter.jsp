<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/seg/AdministrarUsuarioSiat.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="seg" key="seg.usuarioSiatViewAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>

		
		<!-- UsuarioSiat -->
		<fieldset>
			<legend><bean:message bundle="seg" key="seg.usuarioSiat.title"/></legend>
			<table class="tabladatos">
				<!-- nombre -->
				<tr>
					<td><label><bean:message bundle="seg" key="seg.usuarioSiat.usuarioSIAT.label"/>: </label></td>
					<td class="normal"><bean:write name="usuarioSiatAdapterVO" property="usuarioSiat.usuarioSIAT"/></td>
				</tr>
				<!-- Area -->
				<tr>
					<td><label><bean:message bundle="def" key="def.area.label"/>: </label></td>
					<td class="normal"><bean:write name="usuarioSiatAdapterVO" property="usuarioSiat.area.desArea"/></td>
				</tr>
				<!-- Procurador -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
					<td class="normal"><bean:write name="usuarioSiatAdapterVO" property="usuarioSiat.procurador.descripcion"/></td>
				</tr>
				
				<!-- Investigador -->
				<tr>
					<td><label><bean:message bundle="ef" key="ef.investigador.label"/>: </label></td>
					<td class="normal"><bean:write name="usuarioSiatAdapterVO" property="usuarioSiat.investigador.desInvestigador"/></td>
				</tr>
				
				<!-- Inspector -->
				<tr>
					<td><label><bean:message bundle="ef" key="ef.inspector.label"/>: </label></td>
					<td class="normal"><bean:write name="usuarioSiatAdapterVO" property="usuarioSiat.inspector.desInspector"/></td>
				</tr>
				
				<!-- supervisor -->
				<tr>
					<td><label><bean:message bundle="ef" key="ef.supervisor.label"/>: </label></td>
					<td class="normal"><bean:write name="usuarioSiatAdapterVO" property="usuarioSiat.supervisor.desSupervisor"/></td>
				</tr>
				
				<!-- Mandatario -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.mandatario.label"/>: </label></td>
					<td class="normal"><bean:write name="usuarioSiatAdapterVO" property="usuarioSiat.mandatario.descripcion"/></td>
				</tr>
				
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="usuarioSiatAdapterVO" property="usuarioSiat.estado.value"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- UsuarioSiat -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="usuarioSiatAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="usuarioSiatAdapterVO" property="act" value="desactivar">
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