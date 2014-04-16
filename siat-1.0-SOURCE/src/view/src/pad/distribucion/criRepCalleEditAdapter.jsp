<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/pad/AdministrarCriRepCalle.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="pad" key="pad.repartidorAdapter.title"/></h1>		

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
		<!-- Datos del Repartidor -->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.repartidor.title"/></legend>			
			<table class="tabladatos">
				<tr>
					<!-- Recurso -->		
					<td><label>&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="criRepCalleAdapterVO" property="criRepCalle.repartidor.recurso.desRecurso"/></td>
					<!-- Tipo Repartidor -->		
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.tipoRepartidor.label"/>: </label></td>
					<td class="normal"><bean:write name="criRepCalleAdapterVO" property="criRepCalle.repartidor.tipoRepartidor.desTipoRepartidor"/></td>
					</td>	
				</tr>
				<tr>
					<!-- Persona -->		
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.persona.label"/>: </label></td>
					<td class="normal"><bean:write name="criRepCalleAdapterVO" property="criRepCalle.repartidor.personaView"/></td>
					<!-- Legajo -->		
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.repartidor.legajo.label"/>: </label></td>
					<td class="normal"><bean:write name="criRepCalleAdapterVO" property="criRepCalle.repartidor.legajo"/></td>
				</tr>
				<tr>
					<!-- Zona -->		
					<td><label>&nbsp;<bean:message bundle="def" key="def.zona.label"/>: </label></td>
					<td class="normal"><bean:write name="criRepCalleAdapterVO" property="criRepCalle.repartidor.zona.descripcion"/></td>
					<!-- Broche -->		
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.label"/>: </label></td>
					<td class="normal"><bean:write name="criRepCalleAdapterVO" property="criRepCalle.repartidor.broche.desBroche"/></td>
				</tr>				
			</table>
		</fieldset>
		<!-- Fin Datos del Repartidor -->
		
		<!-- CriRepCalle -->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.criRepCalle.title"/></legend>			
			<table class="tabladatos">
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.criRepCalle.zona.label"/>: </label></td>
					<td class="normal">	
						<html:select name="criRepCalleAdapterVO" property="criRepCalle.zona.id" styleClass="select">
							<html:optionsCollection name="criRepCalleAdapterVO" property="listZona" label="descripcion" value="id" />
						</html:select>
					</td>
				</tr>		
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.calle.label"/>: </label></td>
					<td class="normal">			
						<html:text name="criRepCalleAdapterVO" property="criRepCalle.calle.nombreCalle" size="30" maxlength="100" disabled="true"/>
					</td>
					<td>	
							<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('buscarCalle', '');">
								<bean:message bundle="pad" key="pad.criRepCalleAdapter.adm.button.buscarCalle"/>
							</html:button>
					</td>
				</tr>
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.criRepCalle.nroDesde.label"/>: </label></td>
					<td class="normal"><html:text name="criRepCalleAdapterVO" property="criRepCalle.nroDesdeView" size="10" maxlength="22" /></td>
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.criRepCalle.nroHasta.label"/>: </label></td>
					<td class="normal"><html:text name="criRepCalleAdapterVO" property="criRepCalle.nroHastaView" size="10" maxlength="22"/></td>					
				</tr>	
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.criRepCalle.fechaDesde.label"/>: </label></td>
					<td class="normal">
						<html:text name="criRepCalleAdapterVO" property="criRepCalle.fechaDesdeView" styleId="fechaDesdeView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.criRepCalle.fechaHasta.label"/>: </label></td>
					<td class="normal">
						<html:text name="criRepCalleAdapterVO" property="criRepCalle.fechaHastaView" styleId="fechaHastaView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>		
			</table>
		</fieldset>
		<!-- Fin CriRepCalle -->
		
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
					<logic:equal name="criRepCalleAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="criRepCalleAdapterVO" property="act" value="agregar">
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