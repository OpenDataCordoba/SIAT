<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rec/AdministrarCatRSDrei.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="rec" key="rec.catRSDreiEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- CatRSDrei -->
	<fieldset>
		<legend><bean:message bundle="rec" key="rec.catRSDrei.title"/></legend>
		
		<table class="tabladatos">
			<!-- <#Campos#> -->
				<tr>
					<td><label><bean:message bundle="rec" key="rec.catRSDrei.nroCatRSDrei.label"/>: </label></td>
					<td class="normal"><html:text name="catRSDreiAdapterVO" property="catRSDrei.nroCategoriaView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="rec" key="rec.catRSDrei.cantEmpleados.label"/>: </label></td>
					<td class="normal"><html:text name="catRSDreiAdapterVO" property="catRSDrei.cantEmpleadosView" size="20" maxlength="100"/></td>			
				</tr>
				<tr>
					<td><label><bean:message bundle="rec" key="rec.catRSDrei.ingBruAnu.label"/>: </label></td>
					<td class="normal"><html:text name="catRSDreiAdapterVO" property="catRSDrei.ingBruAnuView" size="20" maxlength="100"/></td>			
				</tr>
				<tr>
					<td><label><bean:message bundle="rec" key="rec.catRSDrei.superficie.label"/>: </label></td>
					<td class="normal"><html:text name="catRSDreiAdapterVO" property="catRSDrei.superficieView" size="20" maxlength="100"/></td>			
				</tr>
				<tr>
					<td><label><bean:message bundle="rec" key="rec.catRSDrei.importe.label"/>: </label></td>
					<td class="normal"><html:text name="catRSDreiAdapterVO" property="catRSDrei.importeView" size="20" maxlength="100"/></td>			
				</tr>
				<tr>			
					<td><label><bean:message bundle="rec" key="rec.catRSDreiSearchPage.fechacatRSDreiDesde.label"/>: </label></td>
					<td class="normal"><html:text name="catRSDreiAdapterVO" property="catRSDrei.fechaDesdeView" size="12" styleId="fechaDesdeView"/>
						<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					<td><label><bean:message bundle="rec" key="rec.catRSDreiSearchPage.fechacatRSDreiHasta.label"/>: </label></td>
					<td class="normal"><html:text name="catRSDreiAdapterVO" property="catRSDrei.fechaHastaView" size="12" styleId="fechaHastaView"/>
						<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				</tr>
	
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- CatRSDrei -->
	
	<table class="tablabotones" width="100%" >
		<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="catRSDreiAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="catRSDreiAdapterVO" property="act" value="agregar">
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