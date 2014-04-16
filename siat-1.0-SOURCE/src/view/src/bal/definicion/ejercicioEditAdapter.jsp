<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarEjercicio.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="bal" key="bal.ejercicioEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- Ejercicio -->
	<fieldset>
		<legend><bean:message bundle="bal" key="bal.ejercicio.title"/></legend>
		
		<table class="tabladatos">
		<!-- fecIniEje -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.ejercicio.fecIniEje.label"/>: </label></td>
			<td class="normal">
				<html:text name="ejercicioAdapterVO" property="ejercicio.fecIniEjeView" styleId="fecIniEjeView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fecIniEjeView');" id="a_fecIniEjeView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>

		<!-- fecFinEje -->

			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.ejercicio.fecFinEje.label"/>: </label></td>
			<td class="normal">
				<html:text name="ejercicioAdapterVO" property="ejercicio.fecFinEjeView" styleId="fecFinEjeView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fecFinEjeView');" id="a_fecFinEjeView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>
		<!-- fechaCierre -->
		<tr>
			<td><label><bean:message bundle="bal" key="bal.ejercicio.fechaCierre.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:text name="ejercicioAdapterVO" property="ejercicio.fechaCierreView" styleId="fechaCierreView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaCierreView');" id="a_fechaCierreView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>
		<!-- EstEjercicio -->
		<tr>	
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.estEjercicio.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:select name="ejercicioAdapterVO" property="ejercicio.estEjercicio.id" styleClass="select">
					<html:optionsCollection name="ejercicioAdapterVO" property="listEstEjercicio" label="desEjeBal" value="id" />
				</html:select>
			</td>					
		</tr>
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.ejercicio.desEjercicio.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:textarea name="ejercicioAdapterVO" property="ejercicio.desEjercicio" cols="80" rows="15"/>
			</td>
		</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- Ejercicio -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="ejercicioAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="ejercicioAdapterVO" property="act" value="agregar">
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
