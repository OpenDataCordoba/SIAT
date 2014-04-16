<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/cyq/AdministrarLiqFormConvenio.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="cyq" key="cyq.liqFormConvenioAdapter.title"/></h1>
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>Detalle de Deuda seleccionada.</p>
			</td>				
			<td align="right">
				<!-- Volver -->
				<logic:notEqual name="liqFormConvenioAdapterVO" property="esEspecial" value="true">
					<button type="button" name="btnVolver" class="boton" onclick="submitForm('alternativaCuotas', '');">
				  	    <bean:message bundle="base" key="abm.button.volver"/>
					</button>
				</logic:notEqual>
				<logic:equal name="liqFormConvenioAdapterVO" property="esEspecial" value="true">
					<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuotasEsp', '');">
				  	    <bean:message bundle="base" key="abm.button.volver"/>
					</button>	
				</logic:equal>
			</td>
		</tr>
	</table>


	<!-- Procedimiento -->
		<bean:define id="procedimientoVO" name="liqFormConvenioAdapterVO" property="procedimiento"/>
		<%@ include file="/cyq/concursoyQuiebra/includeEncProcedimiento.jsp"%>
	<!-- Procedimiento -->
		
	<br>
	
	<!-- Lista de Deuda -->	
	<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="cyq" key="cyq.liqFormConvenioAdapter.deudaVerifHomo.label"/></caption>
	      	<tbody>
		       	<tr>
				  	<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
				  	<th align="left"><bean:message bundle="pad" key="pad.cuenta.label"/></th>
				  	<th align="left"><bean:message bundle="cyq" key="cyq.tipoPrivilegioEspecial.label"/></th>
				  	<th align="left"><bean:message bundle="cyq" key="cyq.tipoPrivilegioGeneral.label"/></th>
				  	<th align="left"><bean:message bundle="cyq" key="cyq.tipoPrivilegioQuirografario.label"/></th>
				  	<th align="left"><bean:message bundle="cyq" key="cyq.liqFormConvenioAdapter.total.label"/></th>
				</tr>
				
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqDeudaCyq" name="liqFormConvenioAdapterVO" property="listDeuda">
					<tr>
			  			<!-- Especial -->			  			
			  			<td><bean:write name="LiqDeudaCyq" property="desRecurso"/> </td>
			  			<td><bean:write name="LiqDeudaCyq" property="numeroCuenta"/> </td>
			  			<logic:equal name="LiqDeudaCyq" property="idTipoPrivilegio" value="1">
							<td><b><bean:write name="LiqDeudaCyq" property="importe" bundle="base" formatKey="general.format.currency"/></b></td>
			  				<td>&nbsp;</td><td>&nbsp;</td>
			  			</logic:equal>
			  			<!-- General -->
						<logic:equal name="LiqDeudaCyq" property="idTipoPrivilegio" value="2">
							<td>&nbsp;</td>
							<td><b><bean:write name="LiqDeudaCyq" property="importe" bundle="base" formatKey="general.format.currency"/></b></td>
			  				<td>&nbsp;</td>
			  			</logic:equal>
			  			<!-- Quiro -->
			  			<logic:equal name="LiqDeudaCyq" property="idTipoPrivilegio" value="3">
			  				<td>&nbsp;</td><td>&nbsp;</td>
							<td><b><bean:write name="LiqDeudaCyq" property="importe" bundle="base" formatKey="general.format.currency"/></b></td>
			  			</logic:equal>
						<!-- Total -->
						<td><b><bean:write name="LiqDeudaCyq" property="importe" bundle="base" formatKey="general.format.currency"/></b></td>
			  			
			       	</tr>
				</logic:iterate>
				<!-- Fin Item LiqDeudaVO -->
		       	<tr>       		
		       		<td class="celdatotales" align="right" colspan="2">
			        	<bean:message bundle="cyq" key="cyq.liqDeudaCyqAdapter.totales.label"/>: 
			        </td>
			        <td class="celdatotales">
			        	<b><bean:write name="liqFormConvenioAdapterVO" property="totalGeneral" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
			        <td class="celdatotales">
			        	<b><bean:write name="liqFormConvenioAdapterVO" property="totalEspecial" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
			        <td class="celdatotales">
			        	<b><bean:write name="liqFormConvenioAdapterVO" property="totalQuirografario" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
			        <td class="celdatotales">
			        	<b><bean:write name="liqFormConvenioAdapterVO" property="total" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       	</tr>
	      	</tbody>
		</table>
	</div>	
	<!-- Fin Lista de Deuda -->
	
	<!--  Detalle de las Cuotas del Plan Seleccionado -->
	<fieldset>	
	    <legend> Detalle de las Cuotas del Plan Seleccionado</legend>
		
	    <table class="tabladatos">
	      	<tbody>
	      		<tr>
		       		<td class="normal">
	       				<label>Plan:</label>
	       				<bean:write name="liqFormConvenioAdapterVO" property="planSelected.desPlan"/>
		       		</td>
		       	</tr>
		       	<tr>
		       		<td class="normal">
	       				<label>V&iacute;a Deuda:</label>
	       				<bean:write name="liqFormConvenioAdapterVO" property="planSelected.desViaDeuda"/>
		       		</td>
		       	</tr>
		       	
		       	<logic:notEqual name="liqFormConvenioAdapterVO" property="esEspecial" value="true">
			       	<tr>
			       		<td class="normal">
		       				<label>Descuento Capital:</label>	       				
			       			<bean:write name="liqFormConvenioAdapterVO" property="planSelected.desCapitalOriginal" bundle="base" formatKey="general.format.porcentaje"/>
			       		</td>
			       	</tr>
			       	<tr>		       	
			       		<td class="normal">
		       				<label>Descuento Actualizaci&oacute;n:</label>
			       			<bean:write name="liqFormConvenioAdapterVO" property="planSelected.desActualizacion" bundle="base" formatKey="general.format.porcentaje"/>	       				
			       		</td>
			       	</tr>
			       	<tr>		       	
			       		<td class="normal">
		       				<label>Interes: </label>
		       				<bean:write name="liqFormConvenioAdapterVO" property="planSelected.interes" bundle="base" formatKey="general.format.porcentaje"/>
		       				&nbsp;
		       				<label>Descuento Interes: </label>
		       				<bean:write name="liqFormConvenioAdapterVO" property="planSelected.desInteres" bundle="base" formatKey="general.format.porcentaje"/>
		       				&nbsp;
		       				<label>Interes Aplicado: </label>
		       				<bean:write name="liqFormConvenioAdapterVO" property="planSelected.interesAplicado" bundle="base" formatKey="general.format.porcentaje"/>
			       		</td>
			       	</tr>
		       	</logic:notEqual>
		    </tbody>
		</table>
		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
			<tbody>
	       	<tr>
	       		<th align="left">Nro Cuota</th>
	       		<th align="left">Capital</th>
			  	<th align="left">Interés Financiero</th>
	       		<th align="left">Importe</th>
			  	<th align="left">Fec. Vto.</th>
			</tr>
			
			<!-- Item LiqDeudaVO -->
			<logic:iterate id="LiqCuotaVO" name="liqFormConvenioAdapterVO" property="planSelected.listCuotasForm">
				<tr>
					<td><bean:write name="LiqCuotaVO" property="nroCuota"/>&nbsp;</td>
					<td><bean:write name="LiqCuotaVO" property="capital" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					<td><bean:write name="LiqCuotaVO" property="interes" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					<td><bean:write name="LiqCuotaVO" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					<td><bean:write name="LiqCuotaVO" property="fechaVto"/>&nbsp;</td>
				</tr>
			</logic:iterate>
			
			<tr>
				<td align="right"> <b>Total:</b> </td>
				<td><bean:write name="liqFormConvenioAdapterVO" property="planSelected.totalCapital" bundle="base" formatKey="general.format.currency"/></td>
				<td><bean:write name="liqFormConvenioAdapterVO" property="planSelected.totalInteres" bundle="base" formatKey="general.format.currency"/></td>
				<td><bean:write name="liqFormConvenioAdapterVO" property="planSelected.totalImporte" bundle="base" formatKey="general.format.currency"/></td>
				<td>&nbsp;</td>				
			</tr>			
			</tbody>
		</table>
		<logic:equal name="liqFormConvenioAdapterVO" property="tieneSellado" value="true">
			<p>(*)El importe de la cuota incluye $ <bean:write name="liqFormConvenioAdapterVO" property="importeSelladoView"/> referente a sellado de formalizaci&oacute;n del convenio</p>
		</logic:equal>
		
	</fieldset>
			
	<a name="persona">&nbsp;</a> 
		
	<!-- Datos Persona -->
	<fieldset>
		<legend> Datos Persona </legend>
			
		<table class="tabladatos">
			
			<bean:define id="personaVO" name="liqFormConvenioAdapterVO" property="convenio.persona"/>
				
			<logic:equal name="personaVO" property="personaBuscada" value="true">
				
				<!-- Errores -->
				<logic:equal name="personaVO" property="hasError" value="true">
					<tr>
						<td>
							<table width="100%">
								<tr>
									<td class="normal">
										<ul class="error" id="errorsSearch">
											<logic:iterate id="valueError" name="personaVO" property="listErrorValues">
												<li>
											  		<bean:write name="valueError"/>
												</li>
											</logic:iterate>
										</ul>
									</td>
								</tr>
								
								
								<logic:equal name="personaVO" property="personaEncontrada" value="false">
									<tr>
										<td>
											<div align="center">
												<html:button property="btnBuscarSolicitante"  styleClass="boton" onclick="submitForm('buscarPersona', '');">
													<bean:message bundle="pad" key="pad.button.busquedaAvanzada"/>
												</html:button>
											</div>
										</td>
									</tr>
								</logic:equal>
							</table>						
						</td>
					</tr>				
				</logic:equal>
				
				<logic:notEqual name="personaVO" property="hasError" value="true">
					<!-- Messages -->
					<tr>
						<td>
							<table width="100%">
								
								<logic:equal name="personaVO" property="personaEncontrada" value="true">
									<tr>
										<td>
											<div id="messagesNotFound" class="messages"> 
												<bean:message bundle="pad" key="pad.msgPersonaEncontrada"/>											  
										  	</div>
										</td>
									</tr>
								</logic:equal>
								
								<tr>
									<td>
										<div align="center">
											<html:button property="btnBuscarSolicitante"  styleClass="boton" onclick="submitForm('buscarPersona', '');">
												<bean:message bundle="pad" key="pad.button.busquedaAvanzada"/>
											</html:button>
										</div>
									</td>
								</tr>
							</table>						
						</td>
					</tr>
				</logic:notEqual>	
			</logic:equal>
			
			 <tr>
			 	<td colspan="4">
					<table width="100%">
					 	<tr>
						    <!-- Ingreso de datos -->
						    <td class="normal"><label>(*)&nbsp;<bean:message bundle="pad" key="pad.persona.sexo.label"/>: </label>								
								<select name="convenio.persona.sexo.id" class="select">
									<option value="-1" <logic:equal name="personaVO" property="sexo.codigo" value="S">selected="selected"</logic:equal>>Seleccionar...</option>									
									
									<option value="1" <logic:equal name="personaVO" property="sexo.codigo" value="M">selected="selected"</logic:equal>>Masculino</option>
									
									<option value="0" <logic:equal name="personaVO" property="sexo.codigo" value="F">selected="selected" </logic:equal>>Femenino</option>
								</select>
								
								<label>(*)&nbsp;<bean:message bundle="pad" key="pad.documento.numero.ref"/>: </label> 
								<input type="text" name="convenio.persona.documento.numeroView" value='<bean:write name="personaVO" property="documento.numeroView"/>' />
									
							   	&nbsp;
							
							   	<html:button property="btnBuscarSolicitante"  styleClass="boton" onclick="submitForm('buscarPersonaSimple', '');">
									<bean:message bundle="pad" key="pad.button.buscarPersona"/>
							   	</html:button>
							   	&nbsp;
							   	<html:button property="btnBuscarSolicitante"  styleClass="boton" onclick="submitForm('limpiarPersona', '');">
									<bean:message bundle="base" key="abm.button.limpiar"/>
								</html:button>		   
							</td>							
					    </tr>
					  </table>
				 </td>
			 </tr>
			
			
			<%@ include file="/pad/persona/includePersona.jsp"%>

			<tr>
				<td align="right" colspan="4">
					<logic:equal name="liqFormConvenioAdapterVO" property="convenio.poseeDatosPersona" value="true">
						<button type="button" name="btnModificar" class="boton" onclick="submitForm('modificarPersona', '<bean:write name="liqFormConvenioAdapterVO" property="convenio.persona.id" bundle="base" formatKey="general.format.id"/>');">
							Modificar Datos
						</button>	
					</logic:equal>
				</td>
			</tr>
		</table>
		
	</fieldset>
	<!-- Datos Persona -->
	
	<!-- Datos Formalizacion -->
	<fieldset>
		<legend> Datos Formalizaci&oacute;n </legend>
		
		<table class="tabladatos">
			 
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="cyq" key="cyq.liqFormConvenioAdapter.enCaracterDe.label"/>: </label></td>
				<td class="normal">
					<html:select name="liqFormConvenioAdapterVO" property="convenio.tipoPerFor.id" styleClass="select">
						<html:optionsCollection name="liqFormConvenioAdapterVO" property="listTipoPerFor" label="desTipoPerFor" value="id" />
					</html:select>
				</td>
			</tr>
			
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="cyq" key="cyq.liqFormConvenioAdapter.docApo.label"/>: </label></td>
				<td class="normal">
					<html:select name="liqFormConvenioAdapterVO" property="convenio.tipoDocApo.id" styleClass="select">
						<html:optionsCollection name="liqFormConvenioAdapterVO" property="listTipoDocApo" label="desTipoDocApo" value="id" />
					</html:select>
				</td>
			</tr>
			
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.liqFormConvenioAdapter.observacion.label"/>:</label></td>
				<td class="normal">
					<html:textarea name="liqFormConvenioAdapterVO" property="convenio.observacionFor" cols="80" rows="15">
					</html:textarea>
				</td>
			</tr>
			
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.liqFormConvenioAdapter.lugarForm.label"/>:</label></td>
				<td class="normal"><bean:write name="liqFormConvenioAdapterVO" property="convenio.lugarFor"/></td>
			</tr>
		</table>
	</fieldset>
	<!-- Datos Formalizacion -->
	
	<!--  boton Seleccionar Plan -->
	<p align="right">
		<button type="button" name="btnFormConvenioar" class="boton" onclick="submitForm('grabarFormalizacion', '');">		  	    
	  	    <bean:message bundle="cyq" key="cyq.liqFormConvenioAdapter.button.formalizarPlan"/>
		</button>
	</p>
	<!--  FIN boton Seleccionar Plan -->
	
	<!-- Volver -->
	<logic:notEqual name="liqFormConvenioAdapterVO" property="esEspecial" value="true">
		<button type="button" name="btnVolver" class="boton" onclick="submitForm('alternativaCuotas', '');">
	  	    <bean:message bundle="base" key="abm.button.volver"/>
		</button>
	</logic:notEqual>
	<logic:equal name="liqFormConvenioAdapterVO" property="esEspecial" value="true">
		<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuotasEsp', '');">
	  	    <bean:message bundle="base" key="abm.button.volver"/>
		</button>	
	</logic:equal>
	
	
	<input type="hidden" name="idPlan" value="<bean:write name="liqFormConvenioAdapterVO" property="planSelected.idPlan" bundle="base" formatKey="general.format.id"/>"/>
	
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->

<script type="text/javascript">
	function irAPersona() {
   		document.location = document.URL + '#persona';
   		//alert('buscada');
	}
</script>

<logic:equal name="liqFormConvenioAdapterVO" property="convenio.persona.personaBuscada" value="true">
	<script type="text/javascript">irAPersona();</script>
</logic:equal>