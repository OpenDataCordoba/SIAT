<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/ef/AdministrarOrdenControl.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.ordenControl.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<input type="button" class="boton" onclick="submitForm('volver', '');"
		   				value="<bean:message bundle="base" key="abm.button.volver"/>"/>					
				</td>
			</tr>
		</table>
		
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.ordenControl.label"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="ef" key="ef.ordenControl.nroOrden.label"/>: </label></td>
					<td class="normal"><bean:write name="ordenControlAdapterVO" property="ordenControl.numeroYAnioView"/></td>
					<td><label><bean:message bundle="pad" key="pad.contribuyente.label"/>: </label></td>
					<td class="normal">
						<bean:write name="ordenControlAdapterVO" property="ordenControl.contribuyente.persona.represent"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="ef" key="ef.origen.label"/></label></td>
					<td class="normal"><bean:write name="ordenControlAdapterVO" property="ordenControl.origenOrden.desOrigen"/></td>
					<td><label><bean:message bundle="ef" key="ef.estadoOrden.label"/>: </label></td>
					<td class="normal">
						<logic:equal name="ordenControlAdapterVO" property="ordenControl.estadoOrden.esEstadoInv" value="true">
							<html:select name="ordenControlAdapterVO" property="ordenControl.estadoOrden.id" styleClass="select">
								<html:optionsCollection name="ordenControlAdapterVO" property="listEstadoOrden" label="desEstadoOrden" value="id"/>
							</html:select>
						</logic:equal>
						<logic:notEqual name="ordenControlAdapterVO" property="ordenControl.estadoOrden.esEstadoInv" value="true">
							<bean:write name="ordenControlAdapterVO" property="ordenControl.estadoOrden.desEstadoOrden"/>
						</logic:notEqual>
					</td>
				</tr>
				<tr>
					<td>
						<label><bean:message bundle="ef" key="ef.tipoOrden.label"/>: </label>
					</td>
					<td class="normal">
						<html:select name="ordenControlAdapterVO" property="ordenControl.tipoOrden.id" styleClass="select">
							<html:optionsCollection name="ordenControlAdapterVO" property="listTipoOrden" label="desTipoOrden" value="id"/>
						</html:select>
					</td>
					<td>
						<label><bean:message bundle="ef" key="ef.tipoPeriodo.label"/>: </label>
					</td>
					<td class="normal">
						<html:select name="ordenControlAdapterVO" property="ordenControl.tipoPeriodo.id" styleClass="select">
							<html:optionsCollection name="ordenControlAdapterVO" property="listTipoPeriodo" label="desTipoPeriodo" value="id"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td>
						<label><bean:message bundle="ef" key="ef.ordenControl.observacion.label"/>: </label>
					</td>
					<td class="normal" colspan="3">
						<html:textarea name="ordenControlAdapterVO" style="height:10" property="ordenControl.obsInv" cols="60"/>
					</td>
				</tr>
			</table>
		</fieldset>
		
		
		<!-- Procedimiento -->
		
					<logic:notEmpty name="ordenControlAdapterVO" property="ordenControl.procedimiento.id">
						<bean:define id="procedimientoVO" name="ordenControlAdapterVO" property="ordenControl.procedimiento"/>
						<%@ include file="/cyq/concursoyQuiebra/includeEncProcedimiento.jsp"%>
					<!-- Procedimiento -->
					</logic:notEmpty>
		<!-- FIN Procedimiento -->
		
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.ordenControl.listOrdConCue.title"/></legend>
			<table class="tramonline">
				<th><bean:message bundle="pad" key="pad.cuenta.label"/></th>
				<th><bean:message bundle="pad" key="pad.domicilio.label"/></th>
				<th><bean:message bundle="ef" key="ef.ordConCue.fiscalizar.label"/></th>
				<logic:iterate name="ordenControlAdapterVO" property="ordenControl.listOrdConCue" id="OrdConCueVO">
					<tr>
						<td>
							<bean:write name="OrdConCueVO" property="cuenta.recursoCuentaView"/>
						</td>
						<td>
							<bean:write name="OrdConCueVO" property="cuenta.desDomEnv"/>
						</td>
						<td>
							<html:multibox name="ordenControlAdapterVO" property="listIdOrdConCue">
									<bean:write name="OrdConCueVO" property="idView"/>	
							</html:multibox>
						</td>
					</tr>
				</logic:iterate>
			</table>
		</fieldset>
		
		
		<p align="right">
		   	<input type="button" class="boton" onclick="submitForm('modificar', '');"
		   		value="<bean:message bundle="base" key="abm.button.modificar"/>"/>
		</p>
		
		<table class="tablabotones">
			<tr>				
				<td align="left">
		   			<input type="button" class="boton" onclick="submitForm('volver', '');"
		   				value="<bean:message bundle="base" key="abm.button.volver"/>"/>
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
<!-- ordenControlInvAdapter.jsp -->