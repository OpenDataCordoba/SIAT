<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/cyq/AdministrarProCueNoDeu.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="cyq" key="cyq.proCueNoDeuEditAdapter.title"/></h1>	

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
					<bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.procedimiento.numeroView"/>/
					<bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.procedimiento.anioView"/>
				</td>
			</tr>
			
			<tr>
				<!-- fecha Alta -->
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaAlta.label"/>: </label></td>
				<td class="normal"><bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.procedimiento.fechaAltaView"/></td>
				<!-- fecha boletin -->
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaBoletin.label"/>: </label></td>
				<td class="normal"><bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.procedimiento.fechaBoletinView"/></td>
			</tr>
			
			<tr>
				<!-- auto-->
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.auto.label"/>: </label></td>
				<td class="normal"><bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.procedimiento.auto"/></td>
				<!-- fechaAuto // es la "Fecha de Actualizacion de Deuda" mostrada en la liquidacion de la deuda.--> 
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaAuto.label"/>: </label></td>
				<td class="normal"><bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.procedimiento.fechaAutoView"/></td>
			</tr>
			
			<!-- Caratula -->
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.caratula.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.procedimiento.caratula"/></td>
			</tr>				

			<!-- TipoProceso -->
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.tipoProceso.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.procedimiento.tipoProceso.desTipoProceso"/></td>
			</tr>
			<tr>
				<!-- Juzgado-->
				<td><label><bean:message bundle="cyq" key="cyq.juzgado.label"/>: </label></td>
				<td class="normal"><bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.procedimiento.juzgado.desJuzgado"/></td>
				<!-- Abogado-->				
				<td><label><bean:message bundle="cyq" key="cyq.abogado.label"/>: </label></td>
				<td class="normal"><bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.procedimiento.abogado.descripcion"/></td>
			</tr>

			<!-- fechaVerOpo 	// Fecha de Verificacion -->
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaVerOpo.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.procedimiento.fechaVerOpoView"/></td>
			</tr>

		   	<!-- Contribuyente -->
			<tr>
				<td colspan="4">
					<fieldset>
						<legend> 
							<a onclick="toggle(this, 'bloqueContribuyente')" style="cursor: pointer;"> (-) &nbsp; </a> 
							<bean:message bundle="pad" key="pad.contribuyente.label"/>
						</legend>

						<span id="bloqueContribuyente" style="display:block">
						
							<bean:define id="personaVO" name="proCueNoDeuAdapterVO" property="proCueNoDeu.procedimiento.contribuyente"/>
							
							<%@ include file="/pad/persona/includePersona.jsp"%>
						
						</span>
					</fieldset>
				</td>
			</tr>
			
			
			<!-- DesContribuyente -->
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.desContribuyente.label"/>: </label></td>
				<td class="normal"colspan="3"><bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.procedimiento.desContribuyente"/></td>
			</tr>
			<!-- Domicilio -->
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.domicilio.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.procedimiento.domicilio"/></td>
			</tr>

			<tr>
				<!-- Nro expediente - numero del expediente del juzgado -->
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.numExp.label"/>: </label></td>
				<td class="normal"><bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.procedimiento.numExpView" /></td>			
				<!-- Anio expediente - anio del expediente del juzgado -->
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.anioExp.label"/>: </label></td>
				<td class="normal"><bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.procedimiento.anioExpView" /></td>			
			</tr>
	
			<!-- Caso - Expediente de la Municipalidad. -->
			<!-- Inclucion de CasoView -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="proCueNoDeuAdapterVO" property="proCueNoDeu.procedimiento"/>
					<%@ include file="/cas/caso/includeCasoView.jsp" %>				
				</td>
			</tr>
			<!-- Fin Inclucion de CasoView -->

			<!-- perOpoDeu // Sindico Designado  -->
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.perOpoDeu.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.procedimiento.perOpoDeu"/></td>
			</tr>	

			<tr>
				<!-- lugarOposicion // Domicilio Síndico -->
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.lugarOposicion.label"/>: </label></td>
				<td class="normal"><bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.procedimiento.lugarOposicion"/></td>
				<!-- telefonoOposicion // Teléfono Sindico -->
				
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.telefonoOposicion.label"/>: </label></td>
				<td class="normal"><bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.procedimiento.telefonoOposicion"/></td>
			</tr>
			
			<!-- observacion-->
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.observacion.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.procedimiento.observacion"/></td>
			</tr>
			
			<!-- EstadoProced -->
			<tr>
				<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.procedimiento.estadoProced.desEstadoProced"/></td>
			</tr>

		</table>
	</fieldset>	
	<!-- Procedimiento -->
	
	<br>
	
	<!-- ProCueNoDeu -->
	<fieldset>
		<legend><bean:message bundle="cyq" key="cyq.proCueNoDeu.title"/></legend>
		
		<table class="tabladatos">
			<!-- Recurso -->
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal"> <bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.recurso.desRecurso"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>: </label></td>
				<td class="normal"> <bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.cuenta.numeroCuenta"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.proCueNoDeu.observacion.label"/>: </label></td>
				<td class="normal"><bean:write name="proCueNoDeuAdapterVO" property="proCueNoDeu.observacion"/></td>
			</tr>	
		</table>
	</fieldset>	
	<!-- ProCueNoDeu -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="proCueNoDeuAdapterVO" property="act" value="eliminar">
					<html:button property="btnEliminar"  styleClass="boton" onclick="submitForm('eliminar', '');">
						<bean:message bundle="base" key="abm.button.eliminar"/>
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
