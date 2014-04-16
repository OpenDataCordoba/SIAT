<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/def/AdministrarCalendario.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	<h1><bean:message bundle="def" key="def.calendarioEditAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- Calendario -->
	<fieldset>
		<legend><bean:message bundle="def" key="def.calendario.title"/></legend>
		
		<table class="tabladatos">

			<!-- Recurso -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.calendario.recurso.label"/>: </label></td>
				<td class="normal">
					<html:select name="calendarioAdapterVO" property="calendario.recurso.id" styleClass="select" >
						<bean:define id="includeRecursoList" name="calendarioAdapterVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="calendarioAdapterVO" property="calendario.recurso.id"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>			
				</td>
			</tr>

			<!-- Zona -->
			<tr>	
				<td><label><bean:message bundle="def" key="def.calendario.zona.label"/>: </label></td>
				<td class="normal">
					<html:select name="calendarioAdapterVO" property="calendario.zona.id" styleClass="select">
						<html:optionsCollection name="calendarioAdapterVO" property="listZona" label="descripcion" value="id" />
					</html:select>
				</td>					
			</tr>

			<!-- Periodo -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.calendario.periodo.label"/>: </label></td>
				<td class="normal"><html:text name="calendarioAdapterVO" property="calendario.periodo" size="15" maxlength="50"/><label>&nbsp;&nbsp;&nbsp;(aaaamm)</label></td>			
			</tr>

			<!-- Fecha Vencimiento-->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.calendario.fechaVencimiento.label"/>: </label></td>
				<td class="normal">
					<html:text name="calendarioAdapterVO" property="calendario.fechaVencimientoView" styleId="fechaVencimientoView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaVencimientoView');" id="a_fechaVencimientoView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			
		</table>
	</fieldset>	
	<!-- Calendario -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="calendarioAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="calendarioAdapterVO" property="act" value="agregar">
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
