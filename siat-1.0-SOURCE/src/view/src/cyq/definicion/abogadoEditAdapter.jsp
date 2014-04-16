<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/cyq/AdministrarAbogado.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="cyq" key="cyq.abogadoEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- Abogado -->
	<fieldset>
		<legend><bean:message bundle="cyq" key="cyq.abogado.title"/></legend>
		
		<table class="tabladatos">
		<!-- descripcion -->
		<tr>
			<td><label><bean:message bundle="cyq" key="cyq.abogado.descripcion.label"/>: </label></td>
			<td class="normal"><html:text name="abogadoAdapterVO" property="abogado.descripcion" size="20" maxlength="100"/></td>			
		</tr>
		<!-- domicilio -->
		<tr>
			<td><label><bean:message bundle="cyq" key="cyq.abogado.domicilio.label"/>: </label></td>
			<td class="normal"><html:text name="abogadoAdapterVO" property="abogado.domicilio" size="20" maxlength="100"/></td>			
		</tr>
		<!-- telefono -->
		<tr>
			<td><label><bean:message bundle="cyq" key="cyq.abogado.telefono.label"/>: </label></td>
			<td class="normal"><html:text name="abogadoAdapterVO" property="abogado.telefono" size="20" maxlength="100"/></td>			
		</tr>
		
		<logic:equal name="abogadoAdapterVO" property="act" value="modificar">
		<!-- estado -->
		<tr>
			<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
			<td class="normal"><bean:write name="abogadoAdapterVO" property="abogado.estado.value"/></td>			
		</tr>
		</logic:equal>
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- Abogado -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="abogadoAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="abogadoAdapterVO" property="act" value="agregar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
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

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
