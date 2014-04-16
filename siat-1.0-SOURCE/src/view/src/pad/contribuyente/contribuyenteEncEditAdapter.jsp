<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/pad/AdministrarEncContribuyente.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="pad" key="pad.contribuyenteAdapter.title"/></h1>
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Persona -->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.persona.title"/></legend>
			
			<!-- Inclusion de los datos de la persona -->
			<bean:define id="personaVO" name="encContribuyenteAdapterVO" property="contribuyente.persona"/>
			<%@ include file="/pad/persona/includePersona.jsp" %>

		</fieldset>
		<!-- Fin Persona -->
				
		<!-- Contribuyente -->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.contribuyente.title"/></legend>
			<table class="tabladatos">
				<!-- Inclucion de Caso -->
				<tr>
					<td><label><bean:message bundle="pad" key="pad.contribuyente.casoDomFis.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="encContribuyenteAdapterVO" property="contribuyente"/>
						<bean:define id="voName" value="contribuyente" />
						<%@ include file="/cas/caso/includeCaso.jsp" %>
					</td>
				</tr>
				<!-- Fin Inclucion de Caso -->	
				<tr>
					<td>
						<label><bean:message bundle="pad" key="pad.altaOficio.contribuyente.nroIsib.label"/>: </label>
					</td>
					<td class="normal"><html:text name="encContribuyenteAdapterVO" property="contribuyente.nroIsib"/></td>
					<td>
						<label><bean:message bundle="pad" key="pad.contribuyente.fechaNroIsib.label"/>: </label>
					</td>
					<td class="normal">
						<html:text name="encContribuyenteAdapterVO" property="contribuyente.fechaDesdeIngBruView" styleId="fechaIngBruView"  size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaIngBruView');" id="a_fechaIngBruView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.contribuyente.fechaDesde.label"/>: </label></td>
					<td class="normal">
						<html:text name="encContribuyenteAdapterVO" property="contribuyente.fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					<td><label><bean:message bundle="pad" key="pad.contribuyente.fechaHasta.label"/>: </label></td>
					<td class="normal">
						<html:text name="encContribuyenteAdapterVO" property="contribuyente.fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>				
			</table>
		</fieldset>
		<!-- Fin Contribuyente -->
		
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
					<logic:equal name="encContribuyenteAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="encContribuyenteAdapterVO" property="act" value="agregar">
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
