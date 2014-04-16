<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarEncCompensacion.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.compensacionAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
				
		<!-- Compensacion Enc -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.compensacion.title"/></legend>
			<table class="tabladatos">
				<!-- Fecha Compensacion -->
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.compensacion.fechaAlta.label"/>: </label></td>
					<td class="normal">
						<html:text name="encCompensacionAdapterVO" property="compensacion.fechaAltaView" styleId="fechaAltaView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar_change('fechaAltaView');" id="a_fechaAltaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>		
				<!-- Descripcion -->		
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.compensacion.descripcion.label"/>: </label></td>
					<td class="normal"><html:text name="encCompensacionAdapterVO" property="compensacion.descripcion" size="35" maxlength="100" styleClass="datos"/></td>					
				</tr>
				<logic:equal name="encCompensacionAdapterVO" property="act" value="agregar">
					<tr>
						<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal">
						<html:select name="encCompensacionAdapterVO" property="compensacion.cuenta.recurso.id" styleClass="select" >
							<bean:define id="includeRecursoList" name="encCompensacionAdapterVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="encCompensacionAdapterVO" property="compensacion.cuenta.recurso.id"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>
			   		 </tr>
					<tr>
					<!-- Cuenta -->		
						<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
					    <td class="normal" colspan="4">
								<html:text name="encCompensacionAdapterVO" property="compensacion.cuenta.numeroCuenta" size="20"/>
								<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
									<bean:message bundle="bal" key="bal.compensacion.button.buscarCuenta"/>
								</html:button>
						</td>
					</tr>
					<!-- Caso -->
					<!-- Inclucion de CasoView -->
					<tr>
						<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
						<td colspan="3">
							<bean:define id="IncludedVO" name="encCompensacionAdapterVO" property="compensacion"/>
							<bean:define id="voName" value="compensacion" />
							<%@ include file="/cas/caso/includeCaso.jsp" %>				
						</td>
					</tr>
					<!-- Fin Inclucion de CasoView -->		
				</logic:equal>
				<logic:equal name="encCompensacionAdapterVO" property="act" value="modificar">
					<tr>
						<!-- Recurso -->		
						<td><label>&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal"><bean:write name="encCompensacionAdapterVO" property="compensacion.cuenta.recurso.desRecurso"/></td>
					</tr>
					<tr>
						<!-- Cuenta -->		
						<td><label>&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
						<td class="normal"><bean:write name="encCompensacionAdapterVO" property="compensacion.cuenta.numeroCuenta"/></td>
					</tr>
					<!-- Caso -->
					<!-- Inclucion de CasoView -->
					<tr>
						<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
						<td colspan="3">
							<bean:define id="IncludedVO" name="encCompensacionAdapterVO" property="compensacion"/>
							<%@ include file="/cas/caso/includeCasoView.jsp" %>				
						</td>
					</tr>
					<!-- Fin Inclucion de CasoView -->
				</logic:equal>
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
					<logic:equal name="encCompensacionAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="encCompensacionAdapterVO" property="act" value="agregar">
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
		