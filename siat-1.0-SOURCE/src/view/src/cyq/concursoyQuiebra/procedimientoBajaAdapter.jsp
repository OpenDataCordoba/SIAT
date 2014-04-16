<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Formulario filter -->
<html:form styleId="filter" action="/cyq/AdministrarBajaProcedimiento.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="cyq" key="cyq.procedimientoBajaAdapter.title"/></h1>		

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- Procedimiento -->
	<fieldset>
		<legend><bean:message bundle="cyq" key="cyq.procedimiento.title"/></legend>
		
		<table class="tabladatos">
			
			<tr>
				<!-- Numero / Año -->
				<td>
					<label>
						<bean:message bundle="cyq" key="cyq.procedimiento.numero.label"/>/ 
						<bean:message bundle="cyq" key="cyq.procedimiento.anio.label"/>: 
					</label>
				</td>
				<td class="normal" colspan="3">
					<bean:write name="encProcedimientoAdapterVO" property="procedimiento.numeroView"/>/
					<bean:write name="encProcedimientoAdapterVO" property="procedimiento.anioView"/>
				</td>
			</tr>
			
			<tr>
				<!-- fecha Alta -->
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaAlta.label"/>: </label></td>
				<td class="normal"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.fechaAltaView"/></td>
				<!-- fecha boletin -->
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaBoletin.label"/>: </label></td>
				<td class="normal"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.fechaBoletinView"/></td>
			</tr>
			
			<tr>
				<!-- auto-->
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.auto.label"/>: </label></td>
				<td class="normal"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.auto"/></td>
				<!-- fechaAuto // es la "Fecha de Actualizacion de Deuda" mostrada en la liquidacion de la deuda.--> 
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaAuto.label"/>: </label></td>
				<td class="normal"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.fechaAutoView"/></td>
			</tr>
			
			<!-- Caratula -->
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.caratula.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.caratula"/></td>
			</tr>				

			<!-- TipoProceso -->
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.tipoProceso.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.tipoProceso.desTipoProceso"/></td>
			</tr>
			<tr>
				<!-- Juzgado-->
				<td><label><bean:message bundle="cyq" key="cyq.juzgado.label"/>: </label></td>
				<td class="normal"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.juzgado.desJuzgado"/></td>
				<!-- Abogado-->				
				<td><label><bean:message bundle="cyq" key="cyq.abogado.label"/>: </label></td>
				<td class="normal"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.abogado.descripcion"/></td>
			</tr>

			<!-- fechaVerOpo 	// Fecha de Verificacion -->
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaVerOpo.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.fechaVerOpoView"/></td>
			</tr>

		   	<!-- Contribuyente -->
			<tr>
				<td colspan="4">
					<fieldset>
					<legend><bean:message bundle="pad" key="pad.contribuyente.label"/></legend>
						
						<bean:define id="personaVO" name="encProcedimientoAdapterVO" property="procedimiento.contribuyente"/>
						
						<%@ include file="/pad/persona/includePersona.jsp"%>
						
					</fieldset>
				</td>
			</tr>
			
			
			<!-- DesContribuyente -->
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.desContribuyente.label"/>: </label></td>
				<td class="normal"colspan="3"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.desContribuyente"/></td>
			</tr>
			<!-- Domicilio -->
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.domicilio.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.domicilio"/></td>
			</tr>

			<tr>
				<!-- Nro expediente - numero del expediente del juzgado -->
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.numExp.label"/>: </label></td>
				<td class="normal"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.numExpView" /></td>			
				<!-- Anio expediente - anio del expediente del juzgado -->
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.anioExp.label"/>: </label></td>
				<td class="normal"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.anioExpView" /></td>			
			</tr>
	
			<!-- Caso - Expediente de la Municipalidad. -->
			<!-- Inclucion de CasoView -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="encProcedimientoAdapterVO" property="procedimiento"/>
					<%@ include file="/cas/caso/includeCasoView.jsp" %>				
				</td>
			</tr>
			<!-- Fin Inclucion de CasoView -->

			<!-- perOpoDeu // Sindico Designado  -->
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.perOpoDeu.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.perOpoDeu"/></td>
			</tr>	

			<tr>
				<!-- lugarOposicion // Domicilio Síndico -->
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.lugarOposicion.label"/>: </label></td>
				<td class="normal"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.lugarOposicion"/></td>
				<!-- telefonoOposicion // Teléfono Sindico -->
				
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.telefonoOposicion.label"/>: </label></td>
				<td class="normal"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.telefonoOposicion"/></td>
			</tr>
			
			<!-- observacion-->
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.observacion.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.observacion"/></td>
			</tr>
			
			<!-- EstadoProced -->
			<tr>
				<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.estadoProced.desEstadoProced"/></td>
			</tr>
		</table>
	</fieldset>	
	<!-- Procedimiento -->
	
	<br>
	
	<fieldset>
		<legend><bean:message bundle="cyq" key="cyq.procedimientoAdapter.baja.title"/></legend>

		<table class="tabladatos">
			<!-- Motivo -->
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="cyq" key="cyq.motivoBaja.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="encProcedimientoAdapterVO" property="procedimiento.motivoBaja.id" styleClass="select">
						<html:optionsCollection name="encProcedimientoAdapterVO" property="listMotivoBaja" label="desMotivoBaja" value="id" />
					</html:select>
				</td>					
			</tr>
			<!-- FechaBaja -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="cyq" key="cyq.procedimiento.fechaBaja.label"/>: </label></td>
				<td class="normal">
					<html:text name="encProcedimientoAdapterVO" property="procedimiento.fechaBajaView" styleId="fechaBajaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaBajaView');" id="a_fechaBajaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			<!-- nroSentenciaBaja -->
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.nroSentenciaBaja.label"/>: </label></td>
				<td class="normal" colspan="3"><html:text name="encProcedimientoAdapterVO" property="procedimiento.nroSentenciaBaja" size="20" maxlength="100"/></td>			
			</tr>
			<!-- observacionBaja -->
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.observacionBaja.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:textarea name="encProcedimientoAdapterVO" property="procedimiento.observacionBaja" cols="80" rows="15"/>
				</td>
			</tr>
		</table>
	</fieldset>
	
	
	<!-- Areas para envio de Solitidud -->
	<bean:define id="Sender" name="encProcedimientoAdapterVO" property="procedimiento" />
	<%@ include file="/cas/solicitud/includeAreaSolicitud.jsp"%>
	
	
	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<td align="right">
				<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('baja', '');">
					<bean:message bundle="cyq" key="cyq.procedimientoAdapter.button.baja"/>
				</html:button>
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

<script type="text/javascript">
	function setChk(){
		var form = document.getElementById('filter');		
		form.elements['checkAll'].click();		
	}
</script>

<script type="text/javascript">setChk();</script>
<!-- Fin formulario -->
<!-- procedimientoBajaAdapter.jsp -->