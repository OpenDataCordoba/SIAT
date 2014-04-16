<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/swe/submitForm.js"%>	    
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/seg/AdministrarAccModApl.do">

		<!-- Mensajes/Advertencias -->
		<%@ include file="/swe/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>

		<h1><bean:message bundle="seg" key="accModApl.title"/></h1>

		<!-- AccModApl -->
		<fieldset>
			<legend><bean:message bundle="seg" key="accModAplAdapter.title"/></legend>
			<table class="tabladatos">			
				<tr>
					<td><label><bean:message bundle="seg" key="accModAplAdapter.label.nombreAplicacion"/>:</label></td>
					<td class="normal"><bean:write name="accModAplAdapterVO" property="accModApl.modApl.aplicacion.descripcion"/></td>					
					<td><label><bean:message bundle="seg" key="accModAplAdapter.label.nombreModulo"/>:</label></td>
					<td class="normal"><bean:write name="accModAplAdapterVO" property="accModApl.modApl.nombreModulo"/></td>
				</tr>
				<tr>			
					<td><label><bean:message bundle="seg" key="accModAplAdapter.label.nombreAccion"/>:</label></td>
					<td class="normal"><bean:write name="accModAplAdapterVO" property="accModApl.nombreAccion"/></td>
					<td><label><bean:message bundle="seg" key="accModAplAdapter.label.nombreMetodo"/>:</label></td>
					<td class="normal"><bean:write name="accModAplAdapterVO" property="accModApl.nombreMetodo"/></td>
				</tr>				
				<tr>
					<td><label><bean:message bundle="seg" key="accModAplAdapter.label.descripcion"/>: </label></td>
					<td class="normal"><bean:write name="accModAplAdapterVO" property="accModApl.descripcion" /></td>
				</tr>
			</table>
		</fieldset>	
		<!-- ModApl -->
		
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
	   	
		<html:hidden name="accModAplAdapterVO" property="accModApl.id" />
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
