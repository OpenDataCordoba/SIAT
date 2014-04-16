<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Formulario filter -->
<html:form styleId="filter" action="/rec/AdministrarEncPlanillaCuadra.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>

	<h1><bean:message bundle="rec" key="rec.planillaCuadraEditAdapter.title"/></h1>

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>

	<!-- PlanillaCuadra -->
	<fieldset>
		<legend><bean:message bundle="rec" key="rec.planillaCuadra.title"/></legend>

		<table class="tabladatos">
			<logic:notEqual name="encPlanillaCuadraAdapterVO" property="act" value="modificarNumeroCuadra">
				<!-- Nro planilla -->			
				<logic:equal name="encPlanillaCuadraAdapterVO" property="act" value="modificar">
					<tr>
						<td><label><bean:message bundle="rec" key="rec.planillaCuadra.numeroPlanilla.label"/>: </label></td>
						<td class="normal"><bean:write name="encPlanillaCuadraAdapterVO" property="planillaCuadra.idView"/></td>
					</tr>
				</logic:equal>	
				<!-- Recurso -->
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="3">
						<html:select name="encPlanillaCuadraAdapterVO" property="planillaCuadra.recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');">
							<bean:define id="includeRecursoList" name="encPlanillaCuadraAdapterVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="encPlanillaCuadraAdapterVO" property="planillaCuadra.idRecurso"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>
					</td>				
				</tr>
				<!-- Contrato -->
				<tr>	
					<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.contrato.label"/>: </label></td>
					<td class="normal">
						<html:select name="encPlanillaCuadraAdapterVO" property="planillaCuadra.contrato.id" styleClass="select" onchange="submitForm('paramContrato', '');">
							<html:optionsCollection name="encPlanillaCuadraAdapterVO" property="listContrato" label="descripcion" value="id" />
						</html:select>
					</td>					
				</tr>
				<!-- TipoObra -->
				<tr>	
					<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.tipoObra.label"/>: </label></td>
					<td class="normal">
						<html:select name="encPlanillaCuadraAdapterVO" property="planillaCuadra.tipoObra.id" styleClass="select" onchange="submitForm('paramTipoObra', '');">
							<html:optionsCollection name="encPlanillaCuadraAdapterVO" property="listTipoObra" label="desTipoObra" value="id" />
						</html:select>
					</td>					
				</tr>
				<tr>
					<!-- Fecha -->
					<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.planillaCuadra.fechaCarga.label"/>: </label></td>
					<td class="normal">
						<html:text name="encPlanillaCuadraAdapterVO" property="planillaCuadra.fechaCargaView" styleId="fechaCargaView" size="15" maxlength="10" styleClass="datos"/>
						<a class="link_siat" onclick="return show_calendar('fechaCargaView');" id="a_fechaCargaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>
				<tr>
					<!-- Descripcion -->
					<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.planillaCuadra.descripcion.label"/>: </label></td>
					<td class="normal"><html:text name="encPlanillaCuadraAdapterVO" property="planillaCuadra.descripcion" size="40" maxlength="100" styleClass="datos"/></td>					
				</tr>
				<tr>
					<!-- costo cuadra -->
					<td><label><bean:message bundle="rec" key="rec.planillaCuadra.costoCuadra.label"/>: </label></td>
					<td class="normal"><html:text name="encPlanillaCuadraAdapterVO" property="planillaCuadra.costoCuadraView" size="10" maxlength="10" styleClass="datos"/></td>					
				</tr>
				<tr>
					<!-- Calle Principal -->
					<td><label><bean:message bundle="rec" key="rec.planillaCuadra.callePpal.label"/>: </label></td>
					<td class="normal" colspan="3"><html:text name="encPlanillaCuadraAdapterVO" property="planillaCuadra.callePpal.nombreCalle" size="20" maxlength="100" styleClass="datos" disabled="true"/>
						<html:button property="btnBuscarCallePpal"  styleClass="boton" onclick="submitForm('buscarCalle', '1');">
							<bean:message bundle="rec" key="rec.planillaCuadraEditAdapter.button.buscarCalle"/>
						</html:button>&nbsp;&nbsp;
						<html:button property="btnBuscarCallePpal"  styleClass="boton" onclick="submitForm('limpiarCalles', '');">
							<bean:message bundle="rec" key="rec.planillaCuadraEditAdapter.button.limpiarCalles"/>
						</html:button>
					</td>
				</tr>
				<tr>
					<!-- Calle Desde -->
					<td><label><bean:message bundle="rec" key="rec.planillaCuadra.calleDesde.label"/>: </label></td>
					<td class="normal" colspan="3"><html:text name="encPlanillaCuadraAdapterVO" property="planillaCuadra.calleDesde.nombreCalle" size="20" maxlength="100" styleClass="datos" disabled="true"/>
						<html:button property="btnBuscarCallePpal"  styleClass="boton" onclick="submitForm('buscarCalle', '2');">
							<bean:message bundle="rec" key="rec.planillaCuadraEditAdapter.button.buscarCalle"/>
						</html:button>
					</td>	
				</tr>
				<tr>
					<!-- Calle Hasta -->
					<td><label><bean:message bundle="rec" key="rec.planillaCuadra.calleHasta.label"/>: </label></td>
					<td class="normal" colspan="3"><html:text name="encPlanillaCuadraAdapterVO" property="planillaCuadra.calleHasta.nombreCalle" size="20" maxlength="100" styleClass="datos" disabled="true"/>
						<html:button property="btnBuscarCallePpal"  styleClass="boton" onclick="submitForm('buscarCalle', '3');">
							<bean:message bundle="rec" key="rec.planillaCuadraEditAdapter.button.buscarCalle"/>
						</html:button>
					</td>	
				</tr>
				<tr>
					<!-- Observacion -->
					<td><label><bean:message bundle="rec" key="rec.planillaCuadra.observacion.label"/>: </label></td>
					<td class="normal">
						<html:textarea name="encPlanillaCuadraAdapterVO" 
							property="planillaCuadra.observacion" styleClass="datos" cols="80" rows="15"/>
					</td>					
				</tr>
				<!-- Estado Actual -->			
				<logic:equal name="encPlanillaCuadraAdapterVO" property="act" value="modificar">
					<tr>
						<td><label><bean:message bundle="rec" key="rec.estPlaCua.label"/>: </label></td>
						<td class="normal"><bean:write name="encPlanillaCuadraAdapterVO" property="planillaCuadra.estPlaCua.desEstPlaCua"/></td>
					</tr>
				</logic:equal>	
			</logic:notEqual>
			
			<logic:equal name="encPlanillaCuadraAdapterVO" property="act" value="modificarNumeroCuadra">
					<!-- Nro planilla -->			
					<tr>
						<td><label><bean:message bundle="rec" key="rec.planillaCuadra.numeroPlanilla.label"/>: </label></td>
						<td class="normal"><bean:write name="encPlanillaCuadraAdapterVO" property="planillaCuadra.idView"/></td>
					</tr>
	
					<!-- Recurso -->
					<tr>
						<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal"><bean:write name="encPlanillaCuadraAdapterVO" property="planillaCuadra.recurso.desRecurso"/></td>
					</tr>
					<!-- Contrato -->
					<tr>	
						<td><label><bean:message bundle="rec" key="rec.contrato.label"/>: </label></td>
						<td class="normal"><bean:write name="encPlanillaCuadraAdapterVO" property="planillaCuadra.contrato.descripcion"/></td>				
					</tr>
					<!-- TipoObra -->
					<tr>	
						<td><label><bean:message bundle="rec" key="rec.tipoObra.label"/>: </label></td>
						<td class="normal"><bean:write name="encPlanillaCuadraAdapterVO" property="planillaCuadra.tipoObra.desTipoObra"/></td>				
					</tr>
					<tr>
						<!-- Fecha -->
						<td><label><bean:message bundle="rec" key="rec.planillaCuadra.fechaCarga.label"/>: </label></td>
						<td class="normal"><bean:write name="encPlanillaCuadraAdapterVO" property="planillaCuadra.fechaCargaView"/></td>				
					</tr>
					<tr>
						<!-- Descripcion -->
						<td><label><bean:message bundle="rec" key="rec.planillaCuadra.descripcion.label"/>: </label></td>
						<td class="normal"><bean:write name="encPlanillaCuadraAdapterVO" property="planillaCuadra.descripcion"/></td>				
					</tr>
					<tr>
						<!-- costo cuadra -->
						<td><label><bean:message bundle="rec" key="rec.planillaCuadra.costoCuadra.label"/>: </label></td>
						<td class="normal"><bean:write name="encPlanillaCuadraAdapterVO" property="planillaCuadra.costoCuadraView"/></td>				
					</tr>
		
					<tr>
						<!-- Calle Principal -->
						<td><label><bean:message bundle="rec" key="rec.planillaCuadra.callePpal.label"/>: </label></td>
						<td class="normal"><bean:write name="encPlanillaCuadraAdapterVO" property="planillaCuadra.callePpal.nombreCalle"/></td>
					</tr>
					<tr>
						<!-- Calle Desde -->
						<td><label><bean:message bundle="rec" key="rec.planillaCuadra.calleDesde.label"/>: </label></td>
						<td class="normal"><bean:write name="encPlanillaCuadraAdapterVO" property="planillaCuadra.calleDesde.nombreCalle"/></td>				
					</tr>
					<tr>
						<!-- Calle Hasta -->
						<td><label><bean:message bundle="rec" key="rec.planillaCuadra.calleHasta.label"/>: </label></td>
						<td class="normal"><bean:write name="encPlanillaCuadraAdapterVO" property="planillaCuadra.calleHasta.nombreCalle"/></td>				
					</tr>
					<tr>
						<!-- Observacion -->
						<td><label><bean:message bundle="rec" key="rec.planillaCuadra.observacion.label"/>: </label></td>
						<td class="normal"><bean:write name="encPlanillaCuadraAdapterVO" property="planillaCuadra.observacion"/></td>				
					</tr>
					<tr>
						<!-- Estado Actual -->
						<td><label><bean:message bundle="rec" key="rec.estPlaCua.label"/>: </label></td>
						<td class="normal"><bean:write name="encPlanillaCuadraAdapterVO" property="planillaCuadra.estPlaCua.desEstPlaCua"/></td>
					</tr>
					<tr>
						<!-- Numero de Cuadra -->
						<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.planillaCuadra.numeroCuadra.label"/>: </label></td>
						<td class="normal"><html:text name="encPlanillaCuadraAdapterVO" property="planillaCuadra.numeroCuadra" size="6" maxlength="6" styleClass="datos"/></td>					
					</tr>
			</logic:equal>

		</table>
	</fieldset>
	<!-- PlanillaCuadra -->
	
	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left" width="50%">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<td align="right" width="50%">
				<logic:equal name="encPlanillaCuadraAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>

				<logic:equal name="encPlanillaCuadraAdapterVO" property="act" value="modificarNumeroCuadra">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>


				<logic:equal name="encPlanillaCuadraAdapterVO" property="act" value="agregar">
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