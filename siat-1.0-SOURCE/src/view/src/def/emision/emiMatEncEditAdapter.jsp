<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Formulario filter -->
<html:form styleId="filter" action="/def/AdministrarEncEmiMat.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="def" key="def.emiMatEditAdapter.title"/></h1>		

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- EmiMat -->
	<fieldset>
		<legend><bean:message bundle="def" key="def.emiMat.title"/></legend>
		
		<table class="tabladatos">
			<tr>
				<!-- Recurso -->
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.emiMat.recurso.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="encEmiMatAdapterVO" property="emiMat.recurso.id" styleClass="select"  style="width:300px;">
						<bean:define id="includeRecursoList" name="encEmiMatAdapterVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="encEmiMatAdapterVO" property="emiMat.recurso.id"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
				</td>				
			</tr>

			<tr>
				<!-- Codigo -->
				<logic:equal name="encEmiMatAdapterVO" property="act" value="agregar">					
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.emiMat.codEmiMat.label"/>:</label></td>
					<td class="normal"><html:text name="encEmiMatAdapterVO" property="emiMat.codEmiMat" size="15" maxlength="15" styleClass="datos"/></td>
				</logic:equal>
				<logic:equal name="encEmiMatAdapterVO" property="act" value="modificar">
					<td><label><bean:message bundle="def" key="def.emiMat.codEmiMat.label"/>:</label></td>					
					<td class="normal"><bean:write name="encEmiMatAdapterVO" property="emiMat.codEmiMat"/></td>
				</logic:equal>
				
			</tr>
		</table>
	</fieldset>
	<!-- EmiMat -->
	
	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<td align="right">
				<logic:equal name="encEmiMatAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="encEmiMatAdapterVO" property="act" value="agregar">
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
<!-- emiMatEncEditAdapter.jsp -->