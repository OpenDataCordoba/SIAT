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
		
		<h1><bean:message bundle="seg" key="itemMenuAccModAdapter.title"/></h1>
		
		<!-- ItemMenu -->
		<fieldset>
			<legend><bean:message bundle="seg" key="itemMenuAdapter.title"/></legend>
			
			<p>
				<table class="tabladatos">
					<tr>
						<td><label><bean:message bundle="seg" key="itemMenuAdapter.label.aplicacion.codigo"/>: </label></td>
						<td class="normal"><bean:write name="itemMenuAdapterVO" property="itemMenu.aplicacion.codigo"/></td>

						<td><label><bean:message bundle="seg" key="itemMenuAdapter.label.aplicacion.descripcion"/>: </label></td>
						<td class="normal"><bean:write name="itemMenuAdapterVO" property="itemMenu.aplicacion.descripcion"/></td>
					</tr>

					<tr>
						<td><label><bean:message bundle="seg" key="itemMenuAdapter.label.itemMenu.titulo"/>: </label></td>
                        <td class="normal"><bean:write name="itemMenuAdapterVO" property="itemMenu.titulo"/></td>
						
						<td><label><bean:message bundle="seg" key="itemMenuAdapter.label.itemMenu.descripcion"/>: </label></td>
						<td class="normal"><bean:write name="itemMenuAdapterVO" property="itemMenu.descripcion"/></td>
					</tr>

					<logic:equal name="itemMenuAdapterVO" property="itemMenu.tieneItemMenuPadre" value="true">
						<tr>
							<td><label><bean:message bundle="seg" key="itemMenuAdapter.label.itemMenuPadre.titulo"/>: </label></td>
							<td class="normal"><bean:write name="itemMenuAdapterVO" property="itemMenu.itemMenuPadre.titulo"/></td>
							<td><label><bean:message bundle="seg" key="itemMenuAdapter.label.itemMenuPadre.descripcion"/>: </label></td>
							<td class="normal"><bean:write name="itemMenuAdapterVO" property="itemMenu.itemMenuPadre.descripcion"/></td>
						</tr>
					</logic:equal>
					
					<tr>
						<td><label><bean:message bundle="seg" key="itemMenuAdapter.label.accModApl"/>:</label></td>
						<td class="normal"><bean:write name="itemMenuAdapterVO" property="itemMenu.modAccMetAplView"/></td>
					</tr>

					<logic:equal name="itemMenuAdapterVO" property="itemMenu.tieneHijos" value="false">
						<tr>
							<td><label><bean:message bundle="seg" key="itemMenuAdapter.label.itemMenu.url"/>:</label></td>
	                        <td class="normal" colspan="3"><html:text name="itemMenuAdapterVO" property="itemMenu.url" size="80" maxlength="200"/></td>
						</tr>
					</logic:equal>

				</table>
			</p>

			<p>	
				<logic:equal name="itemMenuAdapterVO" property="itemMenu.tieneAccModApl" value="true">
					<html:button property="btnQuitarAccModApl"  styleClass="boton" onclick="submitForm('paramQuitarAccModApl', '');">
						<bean:message bundle="seg" key="abm.button.quitar.accMod"/>
					</html:button>
				</logic:equal>
				&nbsp;	
				<input type="button" name="btnBuscarAccModApl" value="<bean:message bundle="seg" key="abm.button.buscar.accMod"/>" 
					onclick="submitForm('buscarAccModApl', '<bean:write name="itemMenuAdapterVO" property="itemMenu.aplicacion.id" bundle="base" formatKey="general.format.id"/>' );" class="boton">
				&nbsp;		
				<logic:equal name="userSession" property="navModel.act" value="modificarAccModApl">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
			</p>
		</fieldset>	
		<!-- ItemMenu -->
		
		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    </tr>
	   	</table>

		<html:hidden name="itemMenuAdapterVO" property="itemMenu.id" />
		<html:hidden name="itemMenuAdapterVO" property="itemMenu.titulo" />
		<html:hidden name="itemMenuAdapterVO" property="itemMenu.descripcion" />
		<html:hidden name="itemMenuAdapterVO" property="itemMenu.itemMenuPadre.id" />
		<html:hidden name="itemMenuAdapterVO" property="itemMenu.aplicacion.id" />
		<html:hidden name="itemMenuAdapterVO" property="itemMenu.accModApl.id" />
		
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="selectedId" value= '<bean:write name="itemMenuAdapterVO" property="itemMenu.aplicacion.id" bundle="base" formatKey="general.format.id"/>'/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
