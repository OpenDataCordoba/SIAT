<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarDesEspExe.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.desEspExeEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- DesEspExe -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.desEspExe.title"/></legend>
		
		<table class="tabladatos">
		<!-- desDesEsp -->
		<tr>
			<td><label><bean:message bundle="gde" key="gde.desEsp.desDesEsp.ref"/>: </label></td>
			<td class="normal"><bean:write name="desEspExeAdapterVO" property="desEspExe.desEsp.desDesEsp"/></td>
		<!-- recurso.desRecurso -->
			<td><label><bean:message bundle="def" key="def.recurso.desRecurso.ref"/>: </label></td>
			<td class="normal"><bean:write name="desEspExeAdapterVO" property="desEspExe.desEsp.recurso.desRecurso"/></td>
		</tr>
		<!-- tipoDeuda.desTipoDeuda -->
		<tr>
			<td><label><bean:message bundle="gde" key="gde.desEsp.tipoDeuda.desTipoDeuda.ref"/>: </label></td>
			<td class="normal"><bean:write name="desEspExeAdapterVO" property="desEspExe.desEsp.tipoDeuda.desTipoDeuda"/></td>
		<!-- viaDeuda.desViaDeuda -->
			<td><label><bean:message bundle="gde" key="gde.desEsp.viaDeuda.desViaDeuda.ref"/>: </label></td>
			<td class="normal"><bean:write name="desEspExeAdapterVO" property="desEspExe.desEsp.viaDeuda.desViaDeuda"/></td>
		</tr>	
				
		<!-- exencion.desExencion -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="exe" key="exe.exencion.codExencion.ref"/>: </label></td>
			<td class="normal">
				<html:select name="desEspExeAdapterVO" property="desEspExe.exencion.id" styleClass="select">
					<html:optionsCollection name="desEspExeAdapterVO" property="listExencion" label="desExencion" value="id" />
				</html:select>
			</td>
		</tr>
		<!-- fechaDesde -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.desEspExe.fechaDesde.label"/>: </label></td>
			<td class="normal">
				<html:text name="desEspExeAdapterVO" property="desEspExe.fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		<!-- fechaHasta -->
			<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.desEspExe.fechaHasta.label"/>: </label></td>
			<td class="normal">
				<html:text name="desEspExeAdapterVO" property="desEspExe.fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- DesEspExe -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="desEspExeAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="desEspExeAdapterVO" property="act" value="agregar">
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
