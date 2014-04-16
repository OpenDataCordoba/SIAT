<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Formulario filter -->
<html:form styleId="filter" action="/cyq/AdministrarCambioEstadoProcedimiento.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="cyq" key="cyq.cambioEstadoProcedimientoAdapter.title"/></h1>		

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
				<bean:write name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.numeroView"/> / 
				<bean:write name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.anioView"/>
			</td>			
		</tr>

		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="cyq" key="cyq.procedimiento.fechaAlta.label"/>: </label></td>
			<td class="normal">
				<html:text name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.fechaAltaView" styleId="fechaAltaView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaAltaView');" id="a_fechaAltaView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>

			<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaBoletin.label"/>: </label></td>
			<td class="normal">
				<html:text name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.fechaBoletinView" styleId="fechaBoletinView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaBoletinView');" id="a_fechaBoletinView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>
		
		<!-- Auto -->
		<tr>
			<td><label><bean:message bundle="cyq" key="cyq.procedimiento.auto.label"/>: </label></td>
			<td class="normal"><html:text name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.auto" size="20" maxlength="100"/></td>			
		
			<!-- Fecha Auto -->
			<td><label>(*)&nbsp;<bean:message bundle="cyq" key="cyq.procedimiento.fechaAuto.label"/>: </label></td>
			<td class="normal">
				<html:text name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.fechaAutoView" styleId="fechaAutoView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaAutoView');" id="a_fechaAutoView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>
		
		<!-- Caratula -->		
		<tr>
			<td><label><bean:message bundle="cyq" key="cyq.procedimiento.caratula.label"/>: </label></td>
			<td class="normal" colspan="3"><html:text name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.caratula" size="50" maxlength="100"/></td>			
		</tr>
		
		<!-- Tipo Proceso -->
		<tr>	
			<td><label>(*)&nbsp;<bean:message bundle="cyq" key="cyq.tipoProceso.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:select name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.tipoProceso.id" styleClass="select">
					<html:optionsCollection name="cambioEstadoProcedimientoAdapterVO" property="listTipoProceso" label="desTipoProceso" value="id" />
				</html:select>
			</td>					
		</tr>
		
		<!-- Juzgado -->
		<tr>	
			<td><label>(*)&nbsp;<bean:message bundle="cyq" key="cyq.juzgado.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:select name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.juzgado.id" styleClass="select" onchange="submitForm('paramJuzgado', '');">
					<html:optionsCollection name="cambioEstadoProcedimientoAdapterVO" property="listJuzgado" label="desJuzgado" value="id" />
				</html:select>
			</td>					
		</tr>
		
		<!-- Abogado -->
		<tr>	
			<td><label><bean:message bundle="cyq" key="cyq.abogado.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:select name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.abogado.id" styleClass="select">
					<html:optionsCollection name="cambioEstadoProcedimientoAdapterVO" property="listAbogado" label="descripcion" value="id" />
				</html:select>
			</td>					
		</tr>
		
		<!-- Fecha Verificacion -->
		<tr>
			<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaVerOpo.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:text name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.fechaVerOpoView" styleId="fechaVerOpoView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaVerOpoView');" id="a_fechaVerOpoView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>
		
		<!-- Contribuyente -->
		<tr>
			<td colspan="4">
				<fieldset>
				<legend>(**)&nbsp;<bean:message bundle="pad" key="pad.contribuyente.label"/></legend>
					
					<bean:define id="personaVO" name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.contribuyente"/>
					
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
			<td class="normal" colspan="3"><html:text name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.desContribuyente" size="50" maxlength="100"/></td>			
		</tr>
		<tr>
			<td class="normal" colspan="4">
				(**) Al menos uno de los dos datos es requerido
			</td>
		</tr>
		
		<!-- Domicilio -->	
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="cyq" key="cyq.procedimiento.domicilio.label"/>: </label></td>
			<td class="normal" colspan="3"><html:text name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.domicilio" size="50" maxlength="100"/></td>			
		</tr>
		
		<tr>
			<td><label><bean:message bundle="cyq" key="cyq.procedimiento.numExp.label"/>: </label></td>
			<td class="normal" colspan="3"><html:text name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.numExp" size="20" maxlength="100"/></td>
		</tr>
		<tr>	
			<td><label><bean:message bundle="cyq" key="cyq.procedimiento.anioExp.label"/>: </label></td>
			<td class="normal" colspan="3"><html:text name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.anioExp" size="20" maxlength="100"/></td>			
		</tr>
		
		<!-- Inclucion de Caso -->
		<tr>
			<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
			<td colspan="3">
				<bean:define id="IncludedVO" name="cambioEstadoProcedimientoAdapterVO" property="procedimiento"/>
				<bean:define id="voName" value="procedimiento" />
				<%@ include file="/cas/caso/includeCaso.jsp" %>
			</td>
		</tr>
		<!-- Fin Inclucion de Caso -->
		
		<tr>
			<td><label><bean:message bundle="cyq" key="cyq.procedimiento.perOpoDeu.label"/>: </label></td>
			<td class="normal" colspan="3"><html:text name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.perOpoDeu" size="20" maxlength="100"/></td>
		</tr>
		<tr>				
			<td><label><bean:message bundle="cyq" key="cyq.procedimiento.lugarOposicion.label"/>: </label></td>
			<td class="normal" colspan="3"><html:text name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.lugarOposicion" size="20" maxlength="100"/></td>			
		</tr>
		<tr>
			<td><label><bean:message bundle="cyq" key="cyq.procedimiento.telefonoOposicion.label"/>: </label></td>
			<td class="normal" colspan="3"><html:text name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.telefonoOposicion" size="20" maxlength="100"/></td>			
		</tr>
		
		
		<tr>
			<td><label><bean:message bundle="cyq" key="cyq.procedimiento.observacion.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:textarea name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.observacion" cols="80" rows="15"/>
			</td>
		</tr>
		
		<!-- Estado -->
		<logic:equal name="cambioEstadoProcedimientoAdapterVO" property="act" value="modificar">
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.estadoProced.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.estadoProced.id" styleClass="select">
						<html:optionsCollection name="cambioEstadoProcedimientoAdapterVO" property="listEstadoProced" label="desEstadoProced" value="id" />
					</html:select>
				</td>					
			</tr>
		</logic:equal>
			
		</table>
	</fieldset>
	<!-- Procedimiento -->
	
	<!-- Historio Estados -->
	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
		<caption><bean:message bundle="cyq" key="cyq.procedimiento.listHisEstProced.label"/></caption>
    	<tbody>
			<logic:notEmpty  name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.listHisEstProced">	    	
		    	<tr>
					<th align="left"><bean:message bundle="cyq" key="cyq.hisEstProced.fecha.label"/></th>
					<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
					<th align="left"><bean:message bundle="cyq" key="cyq.hisEstProced.observaciones.label"/></th>
					<th align="left"><bean:message bundle="cyq" key="cyq.hisEstProced.logCambios.label"/></th>						
				</tr>
				<logic:iterate id="HisEstProcedVO" name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.listHisEstProced">
					<tr>
						<td><bean:write name="HisEstProcedVO" property="fechaView"/>&nbsp;</td>
						<td><bean:write name="HisEstProcedVO" property="estadoProced.desEstadoProced"/>&nbsp;</td>
						<td><bean:write name="HisEstProcedVO" property="observaciones"/>&nbsp;</td>
						<td><bean:write name="HisEstProcedVO" property="logCambios"/>&nbsp;</td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			<logic:empty  name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.listHisEstProced">
				<tr><td align="center">
				<bean:message bundle="base" key="base.noExistenRegitros"/>
				</td></tr>
			</logic:empty>
		</tbody>
	</table>
	<!-- Historio Estados -->

	<!-- Nuevo Estado -->
	<fieldset>
		<legend><bean:message bundle="cyq" key="cyq.cambioEstadoProcedimientoAdapter.nuevoEstado.title"/></legend>
	
		<table width="100%" class="tabladatos">
			
			<!-- Estado Actual -->
			<tr>
				<td width="50%"><label><bean:message bundle="cyq" key="cyq.cambioEstadoProcedimientoAdapter.estadoActual.title"/>: </label></td>
				<td width="50%" class="normal">
					<bean:write name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.estadoProced.desEstadoProced"/>
				</td>	
			</tr>
			
			<!-- Fecha -->
			<tr>
				<td width="50%"><label>(*)<bean:message bundle="cyq" key="cyq.hisEstProced.fecha.label"/>: </label></td>
				<td width="50%" class="normal">
					<html:text name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.hisEstProced.fechaView" styleId="fechaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaView');" id="a_fechaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					
				</td>	
			</tr>
			
			<!-- Estado -->
			<tr>
				<td width="50%"><label>(*)&nbsp;<bean:message bundle="base" key="base.estado.label"/>: </label></td>
				<td width="50%" class="normal">
					<html:select name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.hisEstProced.estadoProced.id" styleClass="select">
						<html:optionsCollection name="cambioEstadoProcedimientoAdapterVO" property="listEstadoProced" label="desEstadoProced" value="id" />
					</html:select>
				</td>	
			</tr>
			
			<!-- Observacion de cambio de estado -->
			<tr>
				<td width="50%"><label>(*)&nbsp;<bean:message bundle="cyq" key="cyq.hisEstProced.observaciones.label"/>: </label></td>
				<td width="50%" class="normal">
					<html:textarea name="cambioEstadoProcedimientoAdapterVO" property="procedimiento.hisEstProced.observaciones" cols="80" rows="10"/>
				</td>	
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
			<td align="right" width="50%">
				<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('cambiarEstado', '');">
					<bean:message bundle="cyq" key="cyq.cambioEstadoProcedimientoAdapter.button.cambiarEstado"/>
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
<!-- Fin formulario -->
<!-- procedimientoEncEditAdapter.jsp -->