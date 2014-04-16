<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarOrdenControlFis.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- OrdenControlFis -->
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.ordenControl.title"/></legend>
			<table class="tabladatos">
			
				<!-- nroOrden -->
				<tr>
					<td><label><bean:message bundle="ef" key="ef.ordenControl.nroOrden.label"/>: </label></td>
					<td class="normal"><bean:write name="ordenControlFisAdapterVO" property="ordenControl.numeroOrden" bundle="base" formatKey="general.format.id"/></td>				
				
				<!-- anioOrden -->
					<td><label><bean:message bundle="ef" key="ef.ordenControl.anioOrden.label"/>: </label></td>
					<td class="normal"><bean:write name="ordenControlFisAdapterVO" property="ordenControl.anioOrden" bundle="base" formatKey="general.format.id"/></td>				
				</tr>
				
				<tr>
				<!-- Inclusion de Caso -->
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="ordenControlFisAdapterVO" property="ordenControl"/>
						<bean:define id="voName" value="ordenControl" />
						<%@ include file="/cas/caso/includeCaso.jsp" %>
					</td>
				<!-- Fin Inclusion de Caso -->	
				</tr>
				
				<tr>
				<!-- origenOrden -->
					<td><label><bean:message bundle="ef" key="ef.origen.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="ordenControlFisAdapterVO" property="ordenControl.origenOrden.desOrigen"/></td>
				</tr>
				
				<tr>
				<!-- Inspector -->
					<td><label><bean:message bundle="ef" key="ef.inspector.label"/>: </label></td>
					<td class="normal"><bean:write name="ordenControlFisAdapterVO" property="ordenControl.inspector.desInspector"/></td>
	
				<!-- Supervisor -->
					<td><label><bean:message bundle="ef" key="ef.supervisor.label"/>: </label></td>
					<td class="normal"><bean:write name="ordenControlFisAdapterVO" property="ordenControl.supervisor.desSupervisor"/></td>
				</tr>
				
				<!-- tipoOrden -->
				<tr>
					<td><label><bean:message bundle="ef" key="ef.tipoOrden.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="ordenControlFisAdapterVO" property="ordenControl.tipoOrden.desTipoOrden"/></td>
				</tr>
				
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="ef" key="ef.estadoOrden.label"/>: </label></td>
					<td class="normal" colspan="3"><b><bean:write name="ordenControlFisAdapterVO" property="ordenControl.estadoOrden.desEstadoOrden"/></b></td>
				</tr>
							
				<tr>
				<!-- fechaEmision -->
					<td><label><bean:message bundle="ef" key="ef.ordenControl.fechaEmision.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="ordenControlFisAdapterVO" property="ordenControl.fechaEmisionView"/></td>				
				</tr>
				
				<!-- observacion -->
				<tr>
					<td><label><bean:message bundle="ef" key="ef.ordenControl.observacion.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="ordenControlFisAdapterVO" property="ordenControl.observacion"/></td>				
				</tr>				
			</table>
		</fieldset>	
		<!-- FIN OrdenControlFis -->

		<!-- datos del contribuyente-->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.contribuyente.title"/></legend>
			<table class="tabladatos">
			
				<tr>
					<td><label><bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.nombreContr"/>: </label></td>
					<td class="normal" colspan="3">
						<bean:write name="ordenControlFisAdapterVO" property="ordenControl.contribuyente.persona.represent"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.persona.domicilio.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="ordenControlFisAdapterVO" property="ordenControl.contribuyente.persona.domicilio.view"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.persona.cuit.label"/>: </td>
					<td class="normal"><bean:write name="ordenControlFisAdapterVO" property="ordenControl.contribuyente.persona.cuit"/></td>
				
					<td><label><bean:message bundle="ef" key="ef.opeInvConAdapter.nroIsib.label"/>: </td>
					<td class="normal"><bean:write name="ordenControlFisAdapterVO" property="ordenControl.contribuyente.nroIsib"/></td>
				</tr>
			
			</table>
		</fieldset>			
		<!-- FIN datos del contribuyente-->
		
		<a name="cuentas">&nbsp;</a>
		<!-- lista de cuentas -->
		<logic:notEmpty name="ordenControlFisAdapterVO" property="ordenControl.listOrdConCue">
			<fieldset>
				<legend><bean:message bundle="ef" key="ef.ordenControl.listOrdConCue.title"/></legend>
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.listOrdConCue.title"/></caption>
					<tbody>
						<th><bean:message bundle="pad" key="pad.cuenta.label"/></th>
						<th><bean:message bundle="pad" key="pad.cuenta.recurso.label"/></th>
						<th><bean:message bundle="pad" key="pad.cuenta.fechaAlta.label"/></th>
					
						<logic:iterate id="OrdConCueVO" name="ordenControlFisAdapterVO" property="ordenControl.listOrdConCue">
							<tr>
								<td><bean:write name="OrdConCueVO" property="cuenta.numeroCuenta"/></td>
								<td><bean:write name="OrdConCueVO" property="cuenta.recurso.desRecurso"/></td>
								<td><bean:write name="OrdConCueVO" property="cuenta.fechaAltaView"/></td>
							</tr>	
						</logic:iterate>
					</tbody>	
				</table>
			</fieldset>				
		</logic:notEmpty>
		<!-- fin lista de cuentas -->
		
		
		<table class="tablabotones" width="100%">		
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    													
	   	    	</td>
	   	    	<td align="right">
				<logic:equal name="ordenControlFisAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
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
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
<!-- ordenControlFisEditAdapter.jsp -->
