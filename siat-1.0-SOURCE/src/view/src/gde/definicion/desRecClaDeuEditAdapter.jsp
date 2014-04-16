<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarDesRecClaDeu.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.desRecClaDeuEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- DesRecClaDeu -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.desRecClaDeu.title"/></legend>
		
		<table class="tabladatos">
		<!-- desDesEsp -->
		<tr>
			<td><label><bean:message bundle="gde" key="gde.desEsp.desDesEsp.ref"/>: </label></td>
			<td class="normal"><bean:write name="desRecClaDeuAdapterVO" property="desRecClaDeu.desEsp.desDesEsp"/></td>
		<!-- recurso.desRecurso -->
			<td><label><bean:message bundle="def" key="def.recurso.desRecurso.ref"/>: </label></td>
			<td class="normal"><bean:write name="desRecClaDeuAdapterVO" property="desRecClaDeu.desEsp.recurso.desRecurso"/></td>
		</tr>
		<!-- tipoDeuda.desTipoDeuda -->
		<tr>
			<td><label><bean:message bundle="gde" key="gde.desEsp.tipoDeuda.desTipoDeuda.ref"/>: </label></td>
			<td class="normal"><bean:write name="desRecClaDeuAdapterVO" property="desRecClaDeu.desEsp.tipoDeuda.desTipoDeuda"/></td>
		<!-- viaDeuda.desViaDeuda -->
			<td><label><bean:message bundle="gde" key="gde.desEsp.viaDeuda.desViaDeuda.ref"/>: </label></td>
			<td class="normal"><bean:write name="desRecClaDeuAdapterVO" property="desRecClaDeu.desEsp.viaDeuda.desViaDeuda"/></td>
		</tr>
		<!-- RecClaDeu -->
		<tr>	
			<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recClaDeu.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:select name="desRecClaDeuAdapterVO" property="desRecClaDeu.recClaDeu.id" styleClass="select">
					<html:optionsCollection name="desRecClaDeuAdapterVO" property="listRecClaDeu" label="desClaDeu" value="id" />
				</html:select>
			</td>					
		</tr>
		<!-- fechaDesde -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.desRecClaDeu.fechaDesde.label"/>: </label></td>
			<td class="normal">
				<html:text name="desRecClaDeuAdapterVO" property="desRecClaDeu.fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		<!-- fechaHasta -->
			<td><label><bean:message bundle="gde" key="gde.desRecClaDeu.fechaHasta.label"/>: </label></td>
			<td class="normal">
				<html:text name="desRecClaDeuAdapterVO" property="desRecClaDeu.fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- DesRecClaDeu -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="desRecClaDeuAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="desRecClaDeuAdapterVO" property="act" value="agregar">
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
