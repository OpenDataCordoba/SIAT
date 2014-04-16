<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rec/AdministrarAnulacionObra.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="rec" key="rec.anulacionObraEditAdapter.title"/></h1>

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- AnulacionObra -->
	<fieldset>
		<legend><bean:message bundle="rec" key="rec.anulacionObra.title"/></legend>
		
		<table class="tabladatos">
			<tr>
				<!-- Fecha de Anulacion -->
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.anulacionObra.fechaAnulacion.label"/>: </label></td>
				<td class="normal">
					<html:text name="anulacionObraAdapterVO" property="anulacionObra.fechaAnulacionView" styleId="fechaAnulacionView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaAnulacionView');" id="a_fechaAnulacionView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>

			<tr> 
				<!-- Lista de Obras -->	
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.anulacionObra.obra.label" />: </label></td>
				<td class="normal" colspan="3">
					<html:select name="anulacionObraAdapterVO" property="anulacionObra.obra.id" styleClass="select" onchange="submitForm('paramObra', '');">
						<html:optionsCollection name="anulacionObraAdapterVO" property="listObra" label="desObraConNumeroObra" value="id" />
					</html:select>
				</td>					
			</tr>

			<tr> 
				<!-- Lista de Planillas de Cuadra Ejecutadas -->	
				<td><label><bean:message bundle="rec" key="rec.anulacionObra.planillaCuadra.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="anulacionObraAdapterVO" property="anulacionObra.planillaCuadra.id" styleClass="select" onchange="submitForm('paramPlanillaCuadra', '');">
						<html:optionsCollection name="anulacionObraAdapterVO" property="listPlanillaCuadra" label="descripcion" value="id" />
					</html:select>
				</td>					
			</tr>

			<tr> 
				<!-- Lista de Cuentas de CdM -->	
				<td><label><bean:message bundle="rec" key="rec.anulacionObra.plaCuaDet.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="anulacionObraAdapterVO" property="anulacionObra.plaCuaDet.cuentaCdM.id" styleClass="select" >
						<html:optionsCollection name="anulacionObraAdapterVO" property="listPlaCuaDet" label="cuentaCdM.numeroCuenta" value="id" />
					</html:select>
				</td>					
			</tr>

			<tr>
				<!-- Fecha de Vencimiento -->
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.anulacionObra.fechaVencimiento.label"/>: </label></td>
				<td class="normal">
					<html:text name="anulacionObraAdapterVO" property="anulacionObra.fechaVencimientoView" styleId="fechaVencimientoView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaVencimientoView');" id="a_fechaVencimientoView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>			
			
			<tr>
				<!-- Observacion -->
				<td><label><bean:message bundle="rec" key="rec.anulacionObra.observacion.label"/>: </label></td>
				<td class="normal">
					<html:textarea name="anulacionObraAdapterVO" 
						property="anulacionObra.observacion" styleClass="datos" cols="80" rows="15"/>
				</td>					
			</tr>			

			<!-- Inclucion de Caso -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="anulacionObraAdapterVO" property="anulacionObra"/>
					<bean:define id="voName" value="anulacionObra" />
					<%@ include file="/cas/caso/includeCaso.jsp" %>
				</td>
			</tr>
			
			<logic:equal name="anulacionObraAdapterVO" property="act" value="modificar">
				<!-- Estado del Proceso -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="anulacionObraAdapterVO" 
						property="anulacionObra.corrida.estadoCorrida.desEstadoCorrida"/></td>
				</tr>
			</logic:equal>
			
		</table>
	</fieldset>	
	<!-- AnulacionObra -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="anulacionObraAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="anulacionObraAdapterVO" property="act" value="agregar">
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
