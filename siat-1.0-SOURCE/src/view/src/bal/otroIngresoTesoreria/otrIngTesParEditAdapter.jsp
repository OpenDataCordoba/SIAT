<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarOtrIngTesPar.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.otrIngTesAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
				
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.otrIngTes.title"/></legend>			
			<table class="tabladatos" width="100%">
			<!-- Recurso -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="otrIngTesParAdapterVO" property="otrIngTesPar.otrIngTes.recurso.desRecurso"/></td>
				</tr>
				<tr>
			<!-- Area -->		
					<td><label>&nbsp;<bean:message bundle="def" key="def.area.label"/>: </label></td>
					<td  colspan="4" class="normal"><bean:write name="otrIngTesParAdapterVO" property="otrIngTesPar.otrIngTes.areaOrigen.desArea"/></td>
				</tr>
				<tr>
			<!-- CuentaBanco Origen -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.otrIngTes.cueBanOrigen.label"/>: </label></td>
					<td class="normal"><bean:write name="otrIngTesParAdapterVO" property="otrIngTesPar.otrIngTes.cueBanOrigen.nroCuenta"/></td>
				</tr>	
				<tr>
			<!-- Fecha -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.otrIngTes.fechaOtrIngTes.label"/>: </label></td>
					<td class="normal"><bean:write name="otrIngTesParAdapterVO" property="otrIngTesPar.otrIngTes.fechaOtrIngTesView"/></td>
				</tr>
			<!-- Importe -->	
				<tr>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.otrIngTes.importe.label"/>: </label></td>
					<td class="normal"><bean:write name="otrIngTesParAdapterVO" property="otrIngTesPar.otrIngTes.importeView"/></td>
				</tr>	
			<!-- Descripcion -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.otrIngTes.descripcion.label"/>: </label></td>
					<td colspan="4" class="normal"><bean:write name="otrIngTesParAdapterVO" property="otrIngTesPar.otrIngTes.descripcion"/></td>
				</tr>
	
			</table>
		</fieldset>
		
		
		<!-- OtrIngTesPar -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.otrIngTesPar.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<!-- Partida -->		
					<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.partida.label"/>: </label></td>
					<td  colspan="4" class="normal">	
						<html:select name="otrIngTesParAdapterVO" property="otrIngTesPar.partida.id" styleClass="select" >
							<html:optionsCollection name="otrIngTesParAdapterVO" property="listPartida" label="desPartidaView" value="id" />
						</html:select>
					</td>
				</tr>
				<!-- Importe -->	
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.otrIngTesPar.importe.label"/>: </label></td>
					<td class="normal"><html:text name="otrIngTesParAdapterVO" property="otrIngTesPar.importeView" size="10" maxlength="22" /></td>
				</tr>	
			</table>
		</fieldset>
				
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
					<logic:equal name="otrIngTesParAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="otrIngTesParAdapterVO" property="act" value="agregar">
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
		