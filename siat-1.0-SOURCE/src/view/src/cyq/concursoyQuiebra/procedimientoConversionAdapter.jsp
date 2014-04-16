<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Formulario filter -->
<html:form styleId="filter" action="/cyq/AdministrarConversionProcedimiento.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="cyq" key="cyq.procedimientoConversionAdapter.title"/></h1>		

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
		
		<table class="tabladatos" border="0">
		<tr>
			<td>
				<label>
					<bean:message bundle="cyq" key="cyq.procedimiento.numero.label"/> / 
					<bean:message bundle="cyq" key="cyq.procedimiento.anio.label"/>:
				</label>
			</td>
			<td class="normal" colspan="3">
				<html:text name="encProcedimientoAdapterVO" property="procedimiento.numeroView" size="5" maxlength="100"/>/ 
				<bean:write name="encProcedimientoAdapterVO" property="procedimiento.anioView"/>
			</td>			
		</tr>

		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="cyq" key="cyq.procedimiento.fechaAlta.label"/>: </label></td>
			<td class="normal">
				<html:text name="encProcedimientoAdapterVO" property="procedimiento.fechaAltaView" styleId="fechaAltaView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaAltaView');" id="a_fechaAltaView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>

			<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaBoletin.label"/>: </label></td>
			<td class="normal">
				<html:text name="encProcedimientoAdapterVO" property="procedimiento.fechaBoletinView" styleId="fechaBoletinView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaBoletinView');" id="a_fechaBoletinView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>
		
		<!-- Auto -->
		<tr>
			<td><label><bean:message bundle="cyq" key="cyq.procedimiento.auto.label"/>: </label></td>
			<td class="normal"><html:text name="encProcedimientoAdapterVO" property="procedimiento.auto" size="20" maxlength="100"/></td>			
		
			<!-- Fecha Auto -->
			<td><label>(*)&nbsp;<bean:message bundle="cyq" key="cyq.procedimiento.fechaAuto.label"/>: </label></td>
			<td class="normal">
				<html:text name="encProcedimientoAdapterVO" property="procedimiento.fechaAutoView" styleId="fechaAutoView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaAutoView');" id="a_fechaAutoView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>
		
		<!-- Caratula -->		
		<tr>
			<td><label><bean:message bundle="cyq" key="cyq.procedimiento.caratula.label"/>: </label></td>
			<td class="normal" colspan="3"><html:text name="encProcedimientoAdapterVO" property="procedimiento.caratula" size="50" maxlength="100"/></td>			
		</tr>
		
		<!-- Tipo Proceso -->
		<tr>	
			<td><label>(*)&nbsp;<bean:message bundle="cyq" key="cyq.tipoProceso.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:select name="encProcedimientoAdapterVO" property="procedimiento.tipoProceso.id" styleClass="select">
					<html:optionsCollection name="encProcedimientoAdapterVO" property="listTipoProceso" label="desTipoProceso" value="id" />
				</html:select>
			</td>					
		</tr>
		
		<!-- Juzgado -->
		<tr>	
			<td><label><bean:message bundle="cyq" key="cyq.juzgado.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:select name="encProcedimientoAdapterVO" property="procedimiento.juzgado.id" styleClass="select" onchange="submitForm('paramJuzgado', '');">
					<html:optionsCollection name="encProcedimientoAdapterVO" property="listJuzgado" label="desJuzgado" value="id" />
				</html:select>
			</td>					
		</tr>
		
		<!-- Abogado -->
		<tr>	
			<td><label><bean:message bundle="cyq" key="cyq.abogado.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:select name="encProcedimientoAdapterVO" property="procedimiento.abogado.id" styleClass="select">
					<html:optionsCollection name="encProcedimientoAdapterVO" property="listAbogado" label="descripcion" value="id" />
				</html:select>
			</td>					
		</tr>
		
		<!-- Fecha Verificacion -->
		<tr>
			<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaVerOpo.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:text name="encProcedimientoAdapterVO" property="procedimiento.fechaVerOpoView" styleId="fechaVerOpoView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaVerOpoView');" id="a_fechaVerOpoView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>
		
		<!-- Contribuyente -->
		<tr>
			<td colspan="4">
				<fieldset>
				<legend>(**)&nbsp;<bean:message bundle="pad" key="pad.contribuyente.label"/></legend>
					
					<bean:define id="personaVO" name="encProcedimientoAdapterVO" property="procedimiento.contribuyente"/>
					
					<%@ include file="/pad/persona/includePersona.jsp"%>
					
				</fieldset>
			</td>
		</tr>
		
		<tr>
			<td colspan="4" align="right">
				<html:button property="btnBuscarContribuyente"  styleClass="boton" onclick="submitForm('buscarContribuyente', '');">
					<bean:message bundle="cyq" key="cyq.procedimientoAdapter.button.buscarContribuyente"/>
				</html:button>
			</td>				
		</tr>				
		
		<tr>
			<td><label>(**)&nbsp;<bean:message bundle="cyq" key="cyq.procedimiento.desContribuyente.label"/>: </label></td>
			<td class="normal" colspan="3"><html:text name="encProcedimientoAdapterVO" property="procedimiento.desContribuyente" size="50" maxlength="100"/></td>			
		</tr>
		<tr>
			<td class="normal" colspan="4">
				(**) Al menos uno de los dos datos es requerido
			</td>
		</tr>
		
		<!-- Domicilio -->	
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="cyq" key="cyq.procedimiento.domicilio.label"/>: </label></td>
			<td class="normal" colspan="3"><html:text name="encProcedimientoAdapterVO" property="procedimiento.domicilio" size="50" maxlength="100"/></td>			
		</tr>
		
		<tr>
			<td><label><bean:message bundle="cyq" key="cyq.procedimiento.numExp.label"/>: </label></td>
			<td class="normal" colspan="3"><html:text name="encProcedimientoAdapterVO" property="procedimiento.numExp" size="20" maxlength="100"/></td>
		</tr>
		<tr>	
			<td><label><bean:message bundle="cyq" key="cyq.procedimiento.anioExp.label"/>: </label></td>
			<td class="normal" colspan="3"><html:text name="encProcedimientoAdapterVO" property="procedimiento.anioExp" size="20" maxlength="100"/></td>			
		</tr>
		
		<!-- Inclucion de Caso -->
		<tr>
			<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
			<td colspan="3">
				<bean:define id="IncludedVO" name="encProcedimientoAdapterVO" property="procedimiento"/>
				<bean:define id="voName" value="procedimiento" />
				<%@ include file="/cas/caso/includeCaso.jsp" %>
			</td>
		</tr>
		<!-- Fin Inclucion de Caso -->
		
		<tr>
			<td><label><bean:message bundle="cyq" key="cyq.procedimiento.perOpoDeu.label"/>: </label></td>
			<td class="normal" colspan="3"><html:text name="encProcedimientoAdapterVO" property="procedimiento.perOpoDeu" size="20" maxlength="100"/></td>
		</tr>
		<tr>				
			<td><label><bean:message bundle="cyq" key="cyq.procedimiento.lugarOposicion.label"/>: </label></td>
			<td class="normal" colspan="3"><html:text name="encProcedimientoAdapterVO" property="procedimiento.lugarOposicion" size="20" maxlength="100"/></td>			
		</tr>
		<tr>
			<td><label><bean:message bundle="cyq" key="cyq.procedimiento.telefonoOposicion.label"/>: </label></td>
			<td class="normal" colspan="3"><html:text name="encProcedimientoAdapterVO" property="procedimiento.telefonoOposicion" size="20" maxlength="100"/></td>			
		</tr>
		
		
		<tr>
			<td><label><bean:message bundle="cyq" key="cyq.procedimiento.observacion.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:textarea name="encProcedimientoAdapterVO" property="procedimiento.observacion" cols="80" rows="15"/>
			</td>
		</tr>
		
		<!-- Estado -->
		<logic:equal name="encProcedimientoAdapterVO" property="act" value="modificar">
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.estadoProced.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="encProcedimientoAdapterVO" property="procedimiento.estadoProced.id" styleClass="select">
						<html:optionsCollection name="encProcedimientoAdapterVO" property="listEstadoProced" label="desEstadoProced" value="id" />
					</html:select>
				</td>					
			</tr>
		</logic:equal>
			<!-- <#Campos#> -->
		</table>
	</fieldset>
	<!-- Procedimiento -->
	
	
	<!-- Procedimiento Anterior -->
	<fieldset>
		<legend><bean:message bundle="cyq" key="cyq.procedimiento.procedAnt.label"/></legend>
		
		<table class="tabladatos" border="0">
			<tr>
				<td>
					<label>
						<bean:message bundle="cyq" key="cyq.procedimiento.numero.label"/> / 
						<bean:message bundle="cyq" key="cyq.procedimiento.anio.label"/>:
					</label>
				</td>
				<td class="normal" colspan="3">
					<bean:write name="encProcedimientoAdapterVO" property="procedimiento.procedAnt.numeroView"/> / 
					<bean:write name="encProcedimientoAdapterVO" property="procedimiento.procedAnt.anioView"/>
				</td>			
			</tr>
		
			<tr>
				<!-- fecha Alta -->
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaAlta.label"/>: </label></td>
				<td class="normal"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.procedAnt.fechaAltaView"/></td>
				<!-- fecha boletin -->
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaBoletin.label"/>: </label></td>
				<td class="normal"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.procedAnt.fechaBoletinView"/></td>
			</tr>
			
			<tr>
				<!-- auto-->
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.auto.label"/>: </label></td>
				<td class="normal"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.procedAnt.auto"/></td>
				<!-- fechaAuto // es la "Fecha de Actualizacion de Deuda" mostrada en la liquidacion de la deuda.--> 
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaAuto.label"/>: </label></td>
				<td class="normal"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.procedAnt.fechaAutoView"/></td>
			</tr>
			
			<!-- Caratula -->
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.caratula.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.procedAnt.caratula"/></td>
			</tr>				

			<!-- TipoProceso -->
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.tipoProceso.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.procedAnt.tipoProceso.desTipoProceso"/></td>
			</tr>
			<tr>
				<!-- Juzgado-->
				<td><label><bean:message bundle="cyq" key="cyq.juzgado.label"/>: </label></td>
				<td class="normal"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.procedAnt.juzgado.desJuzgado"/></td>
				<!-- Abogado-->				
				<td><label><bean:message bundle="cyq" key="cyq.abogado.label"/>: </label></td>
				<td class="normal"><bean:write name="encProcedimientoAdapterVO" property="procedimiento.procedAnt.abogado.descripcion"/></td>
			</tr>
		</table>
	</fieldset>	
	
	<!-- Areas para envio de Solitidud -->
	<bean:define id="Sender" name="encProcedimientoAdapterVO" property="procedimiento"/>	
	<%@ include file="/cas/solicitud/includeAreaSolicitud.jsp"%>
	
	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<td align="right">
				<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('convertir', '');">
					<bean:message bundle="cyq" key="cyq.procedimientoConversionAdapter.button.convertir"/>
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
<!-- procedimientoConversionAdapter.jsp -->