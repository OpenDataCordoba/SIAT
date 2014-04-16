<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarOpeInvCon.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="ef" key="ef.opeInvConEditAdapter.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- OpeInv -->
	<bean:define id="opeInvVO" name="opeInvConAdapterVO" property="opeInvCon.opeInv"/>
	<%@include file="/ef/investigacion/includeOpeInvView.jsp" %>	
	<!-- OpeInv -->
	
	<!-- Contribuyente -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.opeInvConAdapter.contribuyente.label"/></legend>
		
		<table class="tabladatos">
		<!-- Persona -->
		<tr>
			<td><label><bean:message bundle="pad" key="pad.persona.label"/>: </label></td>
			<td class="normal" colspan="3">
				<bean:write name="opeInvConAdapterVO" property="opeInvCon.contribuyente.persona.represent"/>
			</td>
		</tr>
		<tr>
			<td><label><bean:message bundle="ef" key="ef.opeInvConAdapter.nroIsib.label"/>: </td>
			<td class="normal"><bean:write name="opeInvConAdapterVO" property="opeInvCon.contribuyente.nroIsib"/></td>
		</tr>
		<tr>
			<td><label><bean:message bundle="ef" key="ef.estadoOpeInvCon.label"/>: </label></td>
			<td class="normal" colspan="3">
				<bean:write name="opeInvConAdapterVO" property="opeInvCon.estadoOpeInvCon.desEstadoOpeInvCon"/>
			</td>
		</tr>		
			<!-- <#Campos#> -->
			
		<logic:equal name="opeInvConAdapterVO" property="act" value="agregar">					
			<tr>
				<td align="right" colspan="4">					
					<button type="button" name="btnModificar" class="boton" onclick="submitForm('buscarPersona', '');">
							<bean:message bundle="ef" key="ef.opeInvConAdapter.button.buscarPersona"/>
					</button>
				</td>
			</tr>
		</logic:equal>
		</table>
	</fieldset>	
	
	<!-- OpeInvCon -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.opeInvCon.title"/></legend>
		
		<table class="tabladatos">
			<bean:define id="personaVO" name="opeInvConAdapterVO" property="opeInvCon.contribuyente.persona"/>
			<%@ include file="/pad/persona/includePersona.jsp"%>

			<tr>
				<td align="right" colspan="4">
					
					<logic:greaterThan name="opeInvConAdapterVO" property="opeInvCon.contribuyente.persona.id" value="0">
						<button type="button" name="btnModificar" class="boton" onclick="submitForm('modificarPersona', '<bean:write name="opeInvConAdapterVO" property="opeInvCon.contribuyente.persona.id" bundle="base" formatKey="general.format.id"/>');">
								Modificar Datos
						</button>
					</logic:greaterThan>
				</td>
			</tr>
		</table>		
	</fieldset>

	

	
	<!-- OpeInvConCue -->
	<a name="cuentas">&nbsp;</a>
	
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.opeInvConCue.legend"/></legend>
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
			<th width="1">&nbsp;</th> <!-- Estado cuenta -->
			<th width="1">&nbsp;</th> <!-- Seleccionar -->
			<th><bean:message bundle="pad" key="pad.cuenta.label"/></th>
			<th><bean:message bundle="pad" key="pad.cuenta.recurso.label"/></th>
			<th><bean:message bundle="ef" key="ef.opeInvConCue.seleccionada.label"/></th>
			<logic:notEmpty name="opeInvConAdapterVO" property="opeInvCon.listOpeInvConCue">
				<logic:iterate id="opeInvConCue" name="opeInvConAdapterVO" property="opeInvCon.listOpeInvConCue" indexId="idx">
					<tr>
					
						<!-- Liquidacion deuda - Se elimino -->
							
						<!-- Estado cuenta -->								
						<td>
							<logic:equal name="opeInvConAdapterVO" property="estadoCuentaEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('estadoCuenta', '<bean:write name="opeInvConCue" property="cuenta.idView"/>');">
										<img title="<bean:message bundle="gde" key="gde.button.estadoCuenta"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/estadoCuenta1.gif"/>
									</a>										
							</logic:equal>
							<logic:notEqual name="opeInvConAdapterVO" property="estadoCuentaEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/estadoCuenta1.gif"/>
							</logic:notEqual>
						</td>						
		
						<!-- Seleccionar cuenta -->
						<td>
							<logic:equal name="opeInvConCue" property="seleccionarBussEnabled" value="true">
								<logic:equal name="opeInvConCue" property="esSeleccionada" value="true">
										<img title="<bean:message bundle="ef" key="ef.opeInvConAdapter.opeInvConCue.seleccionar.icono"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado1.gif"/>														
								</logic:equal>
								<logic:equal name="opeInvConCue" property="esSeleccionada" value="false">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('paramSelectCuenta','<%=idx%>');">
										<img title="<bean:message bundle="ef" key="ef.opeInvConAdapter.opeInvConCue.seleccionar.icono"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado0.gif"/>
									</a>
								</logic:equal>
							</logic:equal>
							<logic:equal name="opeInvConCue" property="seleccionarBussEnabled" value="false">
								<img title="<bean:message bundle="ef" key="ef.opeInvConAdapter.opeInvConCue.seleccionar.icono"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado1.gif"/>							
							</logic:equal>	
						</td>
						<td><bean:write name="opeInvConCue" property="cuenta.numeroCuenta"/></td>
						<td><bean:write name="opeInvConCue" property="desRecurso"/></td>
						<td><bean:write name="opeInvConCue" property="seleccionadaStr"/></td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			<logic:empty name="opeInvConAdapterVO" property="opeInvCon.listOpeInvConCue">
				<tr>
					<td colspan="6" align="center"><bean:message bundle="ef" key="ef.opeInvConAdapter.listOpeInvConCueVacio"/></td>
				</tr>
			</logic:empty>
		</table>
	</fieldset>
	
	<a name="estados">&nbsp;</a>	
	<!-- estados -->
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.opeInvConAdapter.estOpeInvCon.legend"/></legend>
			<table class="tabladatos">
				<tr>
					<td>
						<label><bean:message bundle="ef" key="ef.hisEstOpeInvCon.estado.label"/>:</label>
					</td>
					<td class="normal">
						<html:select name="opeInvConAdapterVO" property="opeInvCon.estadoOpeInvCon.id" styleClass="select" onchange="submitForm('paramEstado','');">
							<html:optionsCollection name="opeInvConAdapterVO" property="listEstadoOpeInvCon" label="desEstadoOpeInvCon" value="id"/>
						</html:select>
					</td>
				</tr>
				
		<!-- se ingresan obs dependiendo del estado seleccionado -->		
				<logic:equal name="opeInvConAdapterVO" property="verObsClasificacion" value="true">
				<tr>
					<td><label><bean:message bundle="ef" key="ef.opeInvConAdapter.Observacion.label"/>:</label></td>
					<td class="normal">
						<html:textarea style="height:100px;width:350px" name="opeInvConAdapterVO" property="opeInvCon.obsClasificacion"/>							
					</td>
				</tr>	 				
				</logic:equal>

				<logic:equal name="opeInvConAdapterVO" property="verObsExclusion" value="true">
				<tr>
					<td><label><bean:message bundle="ef" key="ef.opeInvConAdapter.Observacion.label"/>:</label></td>
					<td class="normal">
						<html:textarea style="height:100px;width:350px" name="opeInvConAdapterVO" property="opeInvCon.obsExclusion"/>
					</td>	
				</tr>	 				
				</logic:equal>
												
			</table>
		</fieldset>
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="opeInvConAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="opeInvConAdapterVO" property="act" value="agregar">
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

	<logic:present name="irA">
		<script type="text/javascript">document.location = document.URL + '#<bean:write name="irA"/>';</script>
	</logic:present>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
<!-- OpeInvConEditAdapter.jsp -->