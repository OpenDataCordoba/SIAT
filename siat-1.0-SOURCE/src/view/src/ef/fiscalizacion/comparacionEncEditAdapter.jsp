<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Formulario filter -->
<html:form styleId="filter" action="/ef/AdministrarEncComparacion.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="ef" key="ef.comparacionEditAdapter.title"/></h1>		

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- Comparacion -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.comparacion.title"/></legend>
		
		<table class="tabladatos">
		<tr>
			<td><label><bean:message bundle="ef" key="ef.comparacion.fecha.label"/>: </label></td>
			<td class="normal">
				<bean:write name="encComparacionAdapterVO" property="comparacion.fechaView"/>
			</td>
		</tr>
		<tr>
			<td><label><bean:message bundle="ef" key="ef.comparacion.descripcion.label"/>: </label></td>
			<td class="normal"><html:text name="encComparacionAdapterVO" property="comparacion.descripcion" size="20" maxlength="100"/></td>			
		</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>
	<!-- Comparacion -->
	
	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<td align="right">
				<logic:equal name="encComparacionAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="encComparacionAdapterVO" property="act" value="agregar">
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
<!-- Fin formulario -->
<!-- comparacionEncEditAdapter.jsp -->
