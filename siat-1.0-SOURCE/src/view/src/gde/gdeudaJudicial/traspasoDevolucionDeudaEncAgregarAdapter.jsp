<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<h1><bean:message bundle="gde" key="gde.traspasoDevolucionDeudaAdapter.title"/></h1>	

<!-- TraspasoDevolucionDeuda -->
<fieldset>
	<legend><bean:message bundle="gde" key="gde.traspasoDevolucionDeuda.title"/></legend>
	
	<table class="tabladatos">

			<!-- Accion -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.traspasoDevolucionDeudaAdapter.accion"/>:</label></td>
				<td class="normal" colspan="3">
					<html:select name="encTraspasoDevolucionDeudaAdapterVO" property="accionTraspasoDevolucion.id" styleClass="select" onchange="submitForm('paramAccion', '');">
						<html:optionsCollection name="encTraspasoDevolucionDeudaAdapterVO" property="listAccionTraspasoDevolucion" label="value" value="id" />
					</html:select>
				</td>
			</tr>
			<!-- Combo Recurso -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="encTraspasoDevolucionDeudaAdapterVO" property="recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');" styleId="cboRecurso" style="width:90%">
						<bean:define id="includeRecursoList"       name="encTraspasoDevolucionDeudaAdapterVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="encTraspasoDevolucionDeudaAdapterVO" property="recurso.id"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
					
					<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
						<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
						src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
					</a>
					
				</td>
			</tr>
			<!-- procurador origen y procurador destino -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.traspasoDevolucionDeudaAdapter.procuradorOrigen.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="encTraspasoDevolucionDeudaAdapterVO" property="procuradorOrigen.id" styleClass="select">
						<html:optionsCollection name="encTraspasoDevolucionDeudaAdapterVO" property="listProcuradorOrigen" label="nombreConCod" value="id" />
					</html:select>
				</td>
			</tr>
			
			<logic:equal name="encTraspasoDevolucionDeudaAdapterVO" property="accionTraspasoDevolucion.esTraspaso" value="true">
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.traspasoDevolucionDeudaAdapter.procuradorDestino.label"/>: </label></td>
					<td class="normal" colspan="3">
						<html:select name="encTraspasoDevolucionDeudaAdapterVO" property="procuradorDestino.id" styleClass="select">
							<html:optionsCollection name="encTraspasoDevolucionDeudaAdapterVO" property="listProcuradorDestino" label="nombreConCod" value="id" />
						</html:select>
					</td>
				</tr>
			</logic:equal>				
			<!-- cuenta con boton buscar -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.label" />: </label></td>
				<td class="normal" colspan="3">
					<logic:equal name="encTraspasoDevolucionDeudaAdapterVO" property="createByConstancia" value="true">
						<html:text name="encTraspasoDevolucionDeudaAdapterVO" property="cuenta.numeroCuenta" 
							size="10" maxlength="10" styleClass="datos" disabled="true" /> 
						<html:button property="btnBuscarCuenta" styleClass="boton" disabled="true">
							<bean:message bundle="gde" key="gde.traspasoDevolucionDeudaAdapter.button.buscarCuenta" />
						</html:button>
					</logic:equal>
					<logic:notEqual name="encTraspasoDevolucionDeudaAdapterVO" property="createByConstancia" value="true">
						<html:text name="encTraspasoDevolucionDeudaAdapterVO" property="cuenta.numeroCuenta" 
							size="10" maxlength="10" styleClass="datos"  /> 
						<html:button property="btnBuscarCuenta" styleClass="boton" onclick="submitForm('buscarCuenta', '');" >
							<bean:message bundle="gde" key="gde.traspasoDevolucionDeudaAdapter.button.buscarCuenta" />
						</html:button>
					</logic:notEqual>			
				</td>
			</tr>
			<!-- Observacion -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.traspasoDevolucionDeudaAdapter.observacion"/>: </label></td>
				<td class="normal" colspan="3">	
					<html:textarea name="encTraspasoDevolucionDeudaAdapterVO" property="observacion" cols="80" rows="15"/>
				</td>
			</tr>
	</table>
</fieldset>	
<!-- TraspasoDevolucionDeuda -->