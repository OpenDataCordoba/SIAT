<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/swe/submitForm.js"%>	    
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/seg/AdministrarItemMenu.do">

		<!-- Mensajes/Advertencias -->
		<%@ include file="/swe/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="seg" key="itemMenu.title"/></h1>
		
		<!-- ItemMenu -->
		<fieldset>
			<legend><bean:message bundle="seg" key="itemMenuAdapter.title"/></legend>
				<table>
					<tr>
						<td><label><bean:message bundle="seg" key="itemMenuAdapter.label.aplicacion.codigo"/>: </label></td> 
						<td class="normal"><bean:write name="itemMenuAdapterVO" property="itemMenu.aplicacion.codigo"/></td>
						
						<td><label><bean:message bundle="seg" key="itemMenuAdapter.label.aplicacion.descripcion"/>: </label></td>
						<td valign="top" rowspan="2"><bean:write name="itemMenuAdapterVO" property="itemMenu.aplicacion.descripcion"/></td>
					</tr>
					
					<tr><td colspan="3">&nbsp;</td></tr>
					
					<logic:equal name="itemMenuAdapterVO" property="itemMenu.tieneItemMenuPadre" value="true">
						<tr>
							<td><label><bean:message bundle="seg" key="itemMenuAdapter.label.itemMenuPadre.titulo"/>:</label></td>
							<td class="normal"><bean:write name="itemMenuAdapterVO" property="itemMenu.itemMenuPadre.titulo"/></td>
							<td><label><bean:message bundle="seg" key="itemMenuAdapter.label.itemMenuPadre.descripcion"/>:</label></td>
							<td class="normal"><bean:write name="itemMenuAdapterVO" property="itemMenu.itemMenuPadre.descripcion"/></td>
						</tr>
					</logic:equal>

					<logic:equal name="itemMenuAdapterVO" property="itemMenu.tieneHijos" value="false">
						<tr>
							<td><label><bean:message bundle="seg" key="itemMenuAdapter.label.itemMenu.url"/>:</label></td>
							<td class="normal"><bean:write name="itemMenuAdapterVO" property="itemMenu.url"/></td>
						</tr>
					</logic:equal>

					<logic:equal name="userSession" property="navModel.act" value="modificar">
						<tr>
							<td><label><bean:message bundle="seg" key="itemMenuAdapter.label.itemMenu.titulo"/>:</label></td>
							<td class="normal"><html:text name="itemMenuAdapterVO" property="itemMenu.titulo" size="20" maxlength="100"/></td>
							<td><label><bean:message bundle="seg" key="itemMenuAdapter.label.itemMenu.descripcion"/>:</label></td>
							<td class="normal"><html:text name="itemMenuAdapterVO" property="itemMenu.descripcion" size="20" maxlength="100"/></td>
						</tr>
						<tr>
							<td><label><bean:message bundle="seg" key="itemMenuAdapter.label.itemMenu.nroOrden"/>: </label></td>
							<td class="normal"><html:text name="itemMenuAdapterVO" property="itemMenu.nroOrdenView" size="20" maxlength="100"/></td>
						</tr>
					</logic:equal>

					<logic:equal name="userSession" property="navModel.act" value="agregar">
						<tr>
							<td><label><bean:message bundle="seg" key="itemMenuAdapter.label.itemMenu.titulo"/>: </label></td>
							<td class="normal"><html:text name="itemMenuAdapterVO" property="itemMenu.titulo" size="20" maxlength="100"/></td>
							<td><label><bean:message bundle="seg" key="itemMenuAdapter.label.itemMenu.descripcion"/>: </label></td>
							<td class="normal"><html:text name="itemMenuAdapterVO" property="itemMenu.descripcion" size="20" maxlength="100"/></td>
						</tr>
						<tr>
							<td><label><bean:message bundle="seg" key="itemMenuAdapter.label.itemMenu.nroOrden"/>: </label></td>
							<td class="normal"><html:text name="itemMenuAdapterVO" property="itemMenu.nroOrdenView" size="20" maxlength="100"/></td>
						</tr>
					</logic:equal>
				</table>
		</fieldset>	
		<!-- ItemMenu -->
		
		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
				<td align="right" width="50%">
					<logic:equal name="userSession" property="navModel.act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					&nbsp;
					<logic:equal name="userSession" property="navModel.act" value="agregar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
							<bean:message bundle="base" key="abm.button.agregar"/>
						</html:button>
					</logic:equal>
				</td>
	   	    </tr>
	   	</table>
		
		<html:hidden name="itemMenuAdapterVO" property="itemMenu.id" />
		<html:hidden name="itemMenuAdapterVO" property="itemMenu.accModApl.id" />		
		<html:hidden name="itemMenuAdapterVO" property="itemMenu.itemMenuPadre.id" />
		<html:hidden name="itemMenuAdapterVO" property="itemMenu.aplicacion.id" />
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="selectedId" value= ''/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
