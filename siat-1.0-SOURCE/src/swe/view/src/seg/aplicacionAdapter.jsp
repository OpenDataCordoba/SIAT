<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/swe/submitForm.js"%>	    
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/seg/AdministrarAplicacion.do">

		<!-- Mensajes/Advertencias -->
		<%@ include file="/swe/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="seg" key="aplicacionAdapter.title"/></h1>	
		
		<!-- Aplicacion -->
		<fieldset>
			<legend><bean:message bundle="seg" key="aplicacionAdapter.subtitle"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="seg" key="aplicacionAdapter.label.codigo"/>: </label></td>
					<td class="normal"><html:text name="aplicacionAdapterVO" property="aplicacion.codigo" size="15" maxlength="20"/></td>
				</tr>

				<tr>
					<td><label><bean:message bundle="seg" key="aplicacionAdapter.label.descripcion"/>: </label></td>
					<td class="normal"><html:text name="aplicacionAdapterVO" property="aplicacion.descripcion" size="50" maxlength="100"/></td>
				</tr>

				<!--tr>
					<td><label><bean:message bundle="seg" key="aplicacionAdapter.label.segTimeOut"/>: </label></td>
					<td class="normal"><html:text name="aplicacionAdapterVO" property="aplicacion.segTimeOut" size="15" maxlength="6"/> &nbsp; Segundos &nbsp; (Tiempo m&aacute;ximo de inactividad para caducar la sessi&oacute;n)</td>
				</tr-->

				<tr>
					<td><label><bean:message bundle="seg" key="aplicacionAdapter.label.maxNivelMenu"/>: </label></td>
					<td class="normal"><html:text name="aplicacionAdapterVO" property="aplicacion.maxNivelMenuView" size="15" maxlength="6"/> </td>
				</tr>
				<tr>
					<td><label><bean:message bundle="seg" key="aplicacionAdapter.label.tipoAuth"/>: </label></td>
				<td class="normal">
				    <bean:define id="listTipoAuth" name="aplicacionAdapterVO" property="listTipoAuth"/>
					<html:select name="aplicacionAdapterVO" property="aplicacion.tipoAuth.id" styleClass="select" >													
					<html:optionsCollection name="aplicacionAdapterVO" property="listTipoAuth" label="descripcion" value="id" />
					</html:select>
				</td>
				</tr>
			</table>
	
		</fieldset>	
		<!-- Aplicacion -->
		
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
					<logic:equal name="userSession" property="navModel.act" value="agregar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
							<bean:message bundle="base" key="abm.button.agregar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	</table>
		
		<html:hidden name="aplicacionAdapterVO" property="aplicacion.id" />
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
