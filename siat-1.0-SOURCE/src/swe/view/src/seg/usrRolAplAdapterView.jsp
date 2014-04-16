<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/swe/submitForm.js"%>	    
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/seg/AdministrarUsrRolApl.do">

		<!-- Mensajes/Advertencias -->
		<%@ include file="/swe/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="seg" key="usrRolApl.title"/></h1>
		
		<!-- UsrRolApl -->
		<fieldset>
			<legend><bean:message bundle="seg" key="usrRolAplAdapter.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="seg" key="usrRolAplAdapter.label.aplicacion.codigo"/>:</label></td>
					<td class="normal"><bean:write name="usrRolAplAdapterVO" property="usrRolApl.usrApl.aplicacion.codigo"/></td>
					<td><label><bean:message bundle="seg" key="usrRolAplAdapter.label.aplicacion.descripcion"/>:</label></td>
					<td class="normal"><bean:write name="usrRolAplAdapterVO" property="usrRolApl.usrApl.aplicacion.descripcion"/></td>
				</tr>
				<tr>
					<td> <label><bean:message bundle="seg" key="usrRolAplAdapter.label.usrRolApl.username"/>:</label></td>
					<td class="normal"><bean:write name="usrRolAplAdapterVO" property="usrRolApl.usrApl.username"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="seg" key="usrRolAplAdapter.label.rolApl.codigo"/>:</label></td>
					<td class="normal"><bean:write name="usrRolAplAdapterVO" property="usrRolApl.rolApl.codigo"/></td>
					<td><label><bean:message bundle="seg" key="usrRolAplAdapter.label.rolApl.descripcion"/>:</label></td>
					<td class="normal"><bean:write name="usrRolAplAdapterVO" property="usrRolApl.rolApl.descripcion"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- UsrRolApl -->
		
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
		
		<html:hidden name="usrRolAplAdapterVO" property="usrRolApl.id" />
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
