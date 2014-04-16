<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Formulario filter -->
<html:form styleId="filter" action="/ef/AdministrarEncActa.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="ef" key="ef.actaEditAdapter.title"/></h1>		

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
				<logic:equal name="encActaAdapterVO" property="act" value="agregar">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</logic:equal>	
				<logic:equal name="encActaAdapterVO" property="act" value="modificar">
					<bean:define name="encActaAdapterVO" property="acta.id" id="idActa"/>
		   			<input type="button" class="boton" onclick="submitForm('volver', '<%=idActa.toString() %>');"
		   				value="<bean:message bundle="base" key="abm.button.volver"/>"
					/>
				</logic:equal>				
				
			</td>
		</tr>
	</table>
	
	<!-- Acta -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.acta.title"/></legend>
		
		<table class="tabladatos">
		
		<!-- numeroActa -->
		<logic:equal name="encActaAdapterVO" property="act" value="modificar">
		<tr>	
			<td><label><bean:message bundle="ef" key="ef.acta.nroActa.label"/>: </label></td>
			<td class="normal" colspan="3">
					<bean:write name="encActaAdapterVO" property="acta.numeroActaView"/>
			</td>					
		</tr>
		</logic:equal>
		
		
		<!-- tipoActa -->
		<tr>	
			<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.tipoActa.label"/>: </label></td>
			<td class="normal" colspan="3">
				<logic:equal name="encActaAdapterVO" property="act" value="agregar">
					<html:select name="encActaAdapterVO" property="acta.tipoActa.id" styleClass="select" onchange="submitForm('paramTipoActa', '');">
						<html:optionsCollection name="encActaAdapterVO" property="listTipoActa" label="desTipoActa" value="id" />
					</html:select>
				</logic:equal>
				<logic:equal name="encActaAdapterVO" property="act" value="modificar">
					<bean:write name="encActaAdapterVO" property="acta.tipoActa.desTipoActa"/>
				</logic:equal>
			</td>					
		</tr>
		
		<!-- fechaVisita -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.acta.fechaVisita.label"/>: </label></td>
			<td class="normal">
				<html:text name="encActaAdapterVO" property="acta.fechaVisitaView" styleId="fechaVisitaView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaVisitaView');" id="a_fechaVisitaView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>

		<!-- HoraVisita -->
			<td><label><bean:message bundle="ef" key="ef.acta.horaVisita.label"/>: </label></td>
			<td class="normal">
				<html:text name="encActaAdapterVO" property="acta.horaVisitaView" styleId="horaVisitaView" size="10" maxlength="10" styleClass="datos" />
			</td>
		</tr>
		
		<!-- persona -->
		<tr>
			<td><label><bean:message bundle="pad" key="pad.persona.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:text name="encActaAdapterVO" property="acta.persona.represent" size="20" disabled="true"/>
				<html:button property="btnBuscarPersona"  styleClass="boton" onclick="submitForm('buscarPersona', '');">
					<bean:message bundle="ef" key="ef.actaAdapter.button.buscarPersona"/>
				</html:button>
			</td>
		</tr>
		
		<!-- en caracter -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.acta.enCaracter.label"/>: </label></td>
			<td class="normal" colspan="3"><html:text name="encActaAdapterVO" property="acta.enCaracter" size="20" maxlength="100"/></td>			
		</tr>
		
		<logic:notEqual name="encActaAdapterVO" property="acta.tipoActa.id" value="2">
			<!-- fechaPresentacion -->
			<tr>
				<td><label><bean:message bundle="ef" key="ef.acta.fechaPresentacion.label"/>: </label></td>
				<td class="normal">
					<html:text name="encActaAdapterVO" property="acta.fechaPresentacionView" styleId="fechaPresentacionView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaPresentacionView');" id="a_fechaPresentacionView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			
			<!-- horaPresentacion -->
				<td><label><bean:message bundle="ef" key="ef.acta.horaPresentacion.label"/>: </label></td>
				<td class="normal">
					<html:text name="encActaAdapterVO" property="acta.horaPresentacionView" styleId="horaPresentacionView" size="10" maxlength="10" styleClass="datos" />
				</td>
			</tr>
			
			<!-- luegarPresentacion -->
			<tr>
				<td><label><bean:message bundle="ef" key="ef.acta.lugarPresentacion.label"/>: </label></td>
				<td class="normal" colspan="3"><html:text name="encActaAdapterVO" property="acta.lugarPresentacion" size="20" maxlength="100"/></td>			
			</tr>
		</logic:notEqual>
			<!-- <#Campos#> -->
		</table>
	</fieldset>
	<!-- Acta -->
	
	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left">
				<logic:equal name="encActaAdapterVO" property="act" value="agregar">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</logic:equal>	
				<logic:equal name="encActaAdapterVO" property="act" value="modificar">
					<bean:define name="encActaAdapterVO" property="acta.id" id="idActa"/>
		   			<input type="button" class="boton" onclick="submitForm('volver', '<%=idActa.toString() %>');"
		   				value="<bean:message bundle="base" key="abm.button.volver"/>"
					/>
				</logic:equal>
			</td>
			<td align="right">
				<logic:equal name="encActaAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="encActaAdapterVO" property="act" value="agregar">
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
<!-- actaEncEditAdapter.jsp -->
