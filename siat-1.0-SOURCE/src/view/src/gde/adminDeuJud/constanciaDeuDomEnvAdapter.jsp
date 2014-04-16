<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Formulario filter -->
<html:form styleId="filter" action="/gde/AdministrarEncConstanciaDeu.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="gde" key="gde.constanciaDeuEditAdapter.title"/></h1>		
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	<!-- ConstanciaDeu -->
	<bean:define id="act" name="encConstanciaDeuAdapterVO" property="act"/>
	<bean:define id="constanciaDeu" name="encConstanciaDeuAdapterVO" property="constanciaDeu"/>
	<%@ include file="/gde/adminDeuJud/includeConDeuViewDatos.jsp" %>
	<!-- ConstanciaDeu -->
	
	<!--  Domicilio -->
	  <bean:define id="domicilioVO" name="encConstanciaDeuAdapterVO" property="constanciaDeu.domicilio"/>
      <bean:define id="listSiNo" name="encConstanciaDeuAdapterVO" property="listSiNo"/>
      <bean:define id="domicilioEnvio" value="true"/> <!-- indica que se trata de domicilio de envio -->
      <%@ include file="/pad/ubicacion/includeDomicilioEdit.jsp" %>
	
	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<td align="right">
				<logic:equal name="encConstanciaDeuAdapterVO" property="act" value="modificarDomicilioEnvio">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificarDomicilio', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="encConstanciaDeuAdapterVO" property="act" value="agregar">
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
