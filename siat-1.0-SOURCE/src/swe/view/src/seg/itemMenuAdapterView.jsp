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
					<logic:equal name="itemMenuAdapterVO" property="itemMenu.tieneAccModApl" value="true">
						<tr>
							<td><label><bean:message bundle="seg" key="itemMenuAdapter.label.accModApl.nombreAccion"/>: </label></td>
							<td class="normal"><bean:write name="itemMenuAdapterVO" property="itemMenu.accModApl.nombreAccion"/></td>

							<td><label><bean:message bundle="seg" key="itemMenuAdapter.label.accModApl.nombreMetodo"/>: </label></td>
							<td class="normal"><bean:write name="itemMenuAdapterVO" property="itemMenu.accModApl.nombreMetodo"/></td>
						</tr>
					</logic:equal>					

					<logic:equal name="itemMenuAdapterVO" property="itemMenu.tieneHijos" value="false">
						<tr>
							<td><label><bean:message bundle="seg" key="itemMenuAdapter.label.itemMenu.url"/>:</label></td>
							<td class="normal"><bean:write name="itemMenuAdapterVO" property="itemMenu.url"/></td>
						</tr>
					</logic:equal>

				</table>
			
			<logic:notEmpty  name="itemMenuAdapterVO" property="itemMenu.listItemMenuHijos">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="seg" key="itemMenuAdapter.listItemMenuHijos.title"/></caption>
                	<tbody>
	                	<tr>
	                		<th align="left"><bean:message bundle="seg" key="itemMenuAdapter.label.listItemMenuHijos.nroOrden"/></th>
							<th align="left"><bean:message bundle="seg" key="itemMenuAdapter.label.listItemMenuHijos.titulo"/></th>
							<th align="left"><bean:message bundle="seg" key="itemMenuAdapter.label.listItemMenuHijos.descripcion"/></th>
							<th align="left"><bean:message bundle="seg" key="itemMenuAdapter.label.listItemMenuHijos.modulo"/></th>
							<th align="left"><bean:message bundle="seg" key="itemMenuAdapter.label.listItemMenuHijos.descAccion"/></th>
						</tr>
							
						<logic:iterate id="ItemMenuVO" name="itemMenuAdapterVO" property="itemMenu.listItemMenuHijos">
							<tr>
								<td><bean:write name="ItemMenuVO" property="nroOrdenView"/></td>
								<td><bean:write name="ItemMenuVO" property="titulo"/></td>
								<td><bean:write name="ItemMenuVO" property="descripcion" /></td>
								<td><bean:write name="ItemMenuVO" property="modAplView" /></td>
								<td><bean:write name="ItemMenuVO" property="descripcionView" /></td>								
							</tr>
						</logic:iterate>
					</tbody>
				</table>
			</logic:notEmpty>
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
					<logic:equal name="userSession" property="navModel.act" value="eliminar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
				</td>
	   	    </tr>
	   	</table>		
		
		<html:hidden name="itemMenuAdapterVO" property="itemMenu.id" />
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
