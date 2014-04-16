<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarAccionSellado.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="bal" key="bal.accionSelladoEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- AccionSellado -->
	<fieldset>
		<legend><bean:message bundle="bal" key="bal.accionSellado.title"/></legend>
		
		<table class="tabladatos">
		<!-- Sellado -->		
		<tr>
			<td><label><bean:message bundle="bal" key="bal.sellado.codSellado.ref"/>: </label></td>
			<td class="normal">
				<bean:write name="accionSelladoAdapterVO" property="accionSellado.sellado.codSellado"/>				
			</td>
			<td><label><bean:message bundle="bal" key="bal.sellado.desSellado.ref"/>: </label></td>
			<td class="normal">
				<bean:write name="accionSelladoAdapterVO" property="accionSellado.sellado.desSellado"/>				
			</td>
		</tr>
		
		<!-- Accion Sellado -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.accionSellado.accion.label"/>: </label></td>
			<td class="normal">
				<html:select name="accionSelladoAdapterVO" property="accionSellado.accion.id" styleClass="select" >
					<html:optionsCollection name="accionSelladoAdapterVO" property="listAccion" label="desAccion" value="id" />
				</html:select>
			</td>
		</tr>
		
		<!-- Recurso -->
		<tr>
			<td><label><bean:message bundle="bal" key="bal.accionSellado.recurso.label"/>: </label></td>
			<td class="normal" colspan="4">
				<html:select name="accionSelladoAdapterVO" property="accionSellado.recurso.id" styleClass="select" >
					<bean:define id="includeRecursoList" name="accionSelladoAdapterVO" property="listRecurso"/>
					<bean:define id="includeIdRecursoSelected" name="accionSelladoAdapterVO" property="accionSellado.recurso.id"/>
					<%@ include file="/def/gravamen/includeRecurso.jsp" %>
				</html:select>			
			</td>
		</tr>
		
		<tr>
	
		<!-- Es Especial -->
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.accionSellado.esEspecial.label"/>: </label></td>
			<td class="normal">
				<html:select name="accionSelladoAdapterVO" property="accionSellado.esEspecial.id" styleClass="select" onchange="submitForm('paramEsEspecial', '');">
					<html:optionsCollection name="accionSelladoAdapterVO" property="listSiNo" label="value" value="id" />
				</html:select>
			</td>	

		<!-- Cantidad de Periodos -->
			<td ><label><bean:message bundle="bal" key="bal.accionSellado.cantPeriodos.label"/>: </label></td>
			<td class="normal">	
				<logic:equal name="accionSelladoAdapterVO" property="paramEsEspecial" value="true">
						<html:text name="accionSelladoAdapterVO" property="accionSellado.cantPeriodosView" size="15" maxlength="100"/>
				</logic:equal>
				<logic:equal name="accionSelladoAdapterVO" property="paramEsEspecial" value="false">
						<html:text name="accionSelladoAdapterVO" property="accionSellado.cantPeriodosView" disabled="true" size="15" maxlength="100"/>
				</logic:equal>
			</td>
		</tr>
		
		<!-- Nombre Clase Prog -->
		<tr>
			<td colspan="3"><label><bean:message bundle="bal" key="bal.accionSellado.classForName.label"/>: </label></td>
			<td class="normal">
				<logic:equal name="accionSelladoAdapterVO" property="paramEsEspecial" value="false">					
					<html:text name="accionSelladoAdapterVO" property="accionSellado.classForName" size="20" maxlength="100"/>
				</logic:equal>
				<logic:equal name="accionSelladoAdapterVO" property="paramEsEspecial" value="true">
					<html:text name="accionSelladoAdapterVO" property="accionSellado.classForName" disabled="true" size="20" maxlength="100"/>
				</logic:equal>
			</td>
		</tr>
		
		<tr>
		<!-- fechaDesde -->
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.accionSellado.fechaDesde.label"/>: </label></td>
			<td class="normal">
				<html:text name="accionSelladoAdapterVO" property="accionSellado.fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>

		<!-- fechaHasta -->
			<td><label><bean:message bundle="bal" key="bal.accionSellado.fechaHasta.label"/>: </label></td>
			<td class="normal">
				<html:text name="accionSelladoAdapterVO" property="accionSellado.fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>
		
	</fieldset>	
	<!-- AccionSellado -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="accionSelladoAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="accionSelladoAdapterVO" property="act" value="agregar">
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
