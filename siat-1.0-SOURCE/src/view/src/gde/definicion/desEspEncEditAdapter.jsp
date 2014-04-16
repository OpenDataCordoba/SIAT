<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarEncDesEsp.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.desEspEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- DesEsp -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.desEsp.title"/></legend>
		
		<table class="tabladatos">
			<!-- Descripcion -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.desEsp.desDesEsp.label"/>:</label></td>
				<td class="normal"><html:text name="encDesEspAdapterVO" property="desEsp.desDesEsp" size="20" maxlength="100"/></td>			
			</tr>
			
			<!-- Combo Recurso -->
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="encDesEspAdapterVO" property="desEsp.recurso.id" styleClass="select">
						<html:optionsCollection name="encDesEspAdapterVO" property="listRecurso" label="desRecurso" value="id" />
					</html:select>
				</td>					
			</tr>
			
			<!-- Combo Tipo Deuda -->
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.desEsp.tipoDeuda.label"/>: </label></td>
				<td class="normal">
					<html:select name="encDesEspAdapterVO" property="desEsp.tipoDeuda.id" styleClass="select">
						<html:optionsCollection name="encDesEspAdapterVO" property="listTipoDeuda" label="desTipoDeuda" value="id" />
					</html:select>
				</td>
				<!-- Combo Via Deuda -->
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.desEsp.viaDeuda.label"/>: </label></td>
				<td class="normal">
					<html:select name="encDesEspAdapterVO" property="desEsp.viaDeuda.id" styleClass="select">
						<html:optionsCollection name="encDesEspAdapterVO" property="listViaDeuda" label="desViaDeuda" value="id" />
					</html:select>
				</td>
			</tr>			
			<!-- Fecha Vto. Desde -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.desEsp.fechaVtoDeudaDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="encDesEspAdapterVO" property="desEsp.fechaVtoDeudaDesdeView" styleId="fechaVtoDeudaDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaVtoDeudaDesdeView');" id="a_fechaVtoDeudaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				<!-- Fecha Vto. Hasta -->
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.desEsp.fechaVtoDeudaHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="encDesEspAdapterVO" property="desEsp.fechaVtoDeudaHastaView" styleId="fechaVtoDeudaHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaVtoDeudaHastaView');" id="a_fechaVtoDeudaHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>				
			</tr>
			
			<!-- % Desc Capital -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.desEsp.porDesCap.label"/>: </label></td>
				<td class="normal"><html:text name="encDesEspAdapterVO" property="desEsp.porDesCapView" size="20" maxlength="100"/></td>			
			</tr>
			
			<!-- % Desc Actualiz. -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.desEsp.porDesAct.label"/>: </label></td>
				<td class="normal"><html:text name="encDesEspAdapterVO" property="desEsp.porDesActView" size="20" maxlength="100"/></td>
				<!-- % Desc interés -->
				<td><label><bean:message bundle="gde" key="gde.desEsp.porDesInt.label"/>: </label></td>
				<td class="normal"><html:text name="encDesEspAdapterVO" property="desEsp.porDesIntView" size="20" maxlength="100"/></td>
			</tr>			
			
			<!-- Leyenda -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.desEsp.leyenda"/>: </label></td>
				<td class="normal"><html:text name="encDesEspAdapterVO" property="desEsp.leyendaDesEsp" size="20" maxlength="100"/></td>
			</tr>
						
			<!-- Inclucion de Caso -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="encDesEspAdapterVO" property="desEsp"/>
					<bean:define id="voName" value="desEsp" />
					<%@ include file="/cas/caso/includeCaso.jsp" %>
				</td>
			</tr>
			<!-- Fin Inclucion de Caso -->
		
		</table>
	</fieldset>	
	<!-- DesEsp -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="encDesEspAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="encDesEspAdapterVO" property="act" value="agregar">
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
