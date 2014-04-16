<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/swe/submitForm.js"%>	    
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/seg/AdministrarRolAccModApl.do">

		<!-- Mensajes/Advertencias -->
		<%@ include file="/swe/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="seg" key="rolAccModAplAdapterView.title"/></h1>	
		
		<!-- RolApl -->
		<fieldset>
			<legend><bean:message bundle="seg" key="rolAccModAplAdapterView.subtitle"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="seg" key="rolAccModAplAdapterView.rol.label"/>:</label></td>
					<td class="normal"><bean:write name="rolAccModAplAdapterVO" property="rolAccModApl.rolApl.descripcion"/></td>
					<td><label><bean:message bundle="seg" key="rolAccModAplAdapterView.modulo.label"/>:</label></td>
					<td class="normal"><bean:write name="rolAccModAplAdapterVO" property="rolAccModApl.accModApl.modApl.nombreModulo"/></td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="seg" key="rolAccModAplAdapterView.nombreAccion.label"/>: </label></td>
					<td class="normal"><bean:write name="rolAccModAplAdapterVO" property="rolAccModApl.accModApl.nombreAccion"/></td>
					<td><label><bean:message bundle="seg" key="rolAccModAplAdapterView.nombreMetodo.label"/>: </label></td>
					<td class="normal"><bean:write name="rolAccModAplAdapterVO" property="rolAccModApl.accModApl.nombreMetodo"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- RolApl -->
		
		<table class="tablabotones" width="100%">
		   	<tr>
	  	   		<td align="left" width="50%">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right" width="50%">	
					<logic:equal name="userSession" property="navModel.act" value="eliminar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
				</td>
			</tr>
		</table>
		
		<html:hidden name="rolAccModAplAdapterVO" property="rolAccModApl.id" />
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
