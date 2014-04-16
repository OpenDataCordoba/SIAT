<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/swe/submitForm.js"%>	    
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/seg/AdministrarRolApl.do">

		<!-- Mensajes/Advertencias -->
		<%@ include file="/swe/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="seg" key="seg.rolAplAdapter.title"/></h1>	
		
		<!-- RolApl -->
		<fieldset>
			<legend><bean:message bundle="seg" key="seg.rolAplAdapter.subtitle"/></legend>
			
			<table class="tabladatos">
				<tr>	
					<td><label><bean:message bundle="seg" key="seg.rolAplAdapter.rolApl.aplicacion.codigo.label"/>: </label></td>
					<td class="normal"><bean:write name="rolAplAdapterVO" property="rolApl.aplicacion.codigo"/></td>

					<td><label><bean:message bundle="seg" key="seg.rolAplAdapter.rolApl.aplicacion.descripcion.label"/>: </label></td>
					<td class="normal"><bean:write name="rolAplAdapterVO" property="rolApl.aplicacion.descripcion"/></td>
					<html:hidden name="rolAplAdapterVO" property="rolApl.aplicacion.descripcion"/>
					<html:hidden name="rolAplAdapterVO" property="rolApl.aplicacion.id"/>
				</tr>
				<tr>
					<td><label><bean:message bundle="seg" key="seg.rolAplAdapter.rolApl.codigo.label"/>: </label></td>
					<td class="normal"><html:text name="rolAplAdapterVO" property="rolApl.codigo" size="15" maxlength="20"/></td>
					<td><label><bean:message bundle="seg" key="seg.rolAplAdapter.rolApl.descripcion.label"/>: </label></td>
					<td class="normal"><html:text name="rolAplAdapterVO" property="rolApl.descripcion" size="20" maxlength="100"/></td>
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
	   	
		<html:hidden name="rolAplAdapterVO" property="rolApl.aplicacion.id"/>
		<html:hidden name="rolAplAdapterVO" property="rolApl.aplicacion.codigo" />
		<html:hidden name="rolAplAdapterVO" property="rolApl.aplicacion.descripcion" />						
		<html:hidden name="rolAplAdapterVO" property="rolApl.id" />
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
