<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/esp/AdministrarEncHabilitacion.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="esp" key="esp.habilitacionAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
				
		<!-- Habilitacion Enc -->
		<fieldset>
			<legend><bean:message bundle="esp" key="esp.habilitacion.title"/></legend>
			
			<table class="tabladatos">
				<!-- Recurso -->		
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="3">
					<logic:equal name="encHabilitacionAdapterVO" property="act" value="agregar">		
						<html:select name="encHabilitacionAdapterVO" property="habilitacion.recurso.id" styleClass="select" >
							<bean:define id="includeRecursoList" name="encHabilitacionAdapterVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="encHabilitacionAdapterVO" property="habilitacion.recurso.id"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>
											
		  			</logic:equal>
					<logic:equal name="encHabilitacionAdapterVO" property="act" value="modificar">		
						<bean:write name="encHabilitacionAdapterVO" property="habilitacion.recurso.desRecurso"/>
					</logic:equal>
					</td>
				</tr>

				<logic:equal name="encHabilitacionAdapterVO" property="act" value="modificar">
					<tr>
						<!-- Numero -->		
						<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.numero.label"/>: </label></td>
						<td class="normal"><bean:write name="encHabilitacionAdapterVO" property="habilitacion.numeroView"/></td>
	
						<!-- Anio -->		
						<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.anio.label"/>: </label></td>
						<td class="normal"><bean:write name="encHabilitacionAdapterVO" property="habilitacion.anioView"/></td>
					</tr>
				</logic:equal>			

				<!-- Tipo Habilitacion -->		
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="esp" key="esp.tipoHab.label"/>: </label></td>
					<td class="normal" colspan="3">
						<logic:equal name="encHabilitacionAdapterVO" property="act" value="agregar">		
							<html:select name="encHabilitacionAdapterVO" property="habilitacion.tipoHab.id" styleClass="select" onchange="submitForm('paramTipoHab', '');" >
								<html:optionsCollection name="encHabilitacionAdapterVO" property="listTipoHab" label="descripcion" value="id" />
							</html:select>
			  			</logic:equal>
						<logic:equal name="encHabilitacionAdapterVO" property="act" value="modificar">		
							<bean:write name="encHabilitacionAdapterVO" property="habilitacion.tipoHab.descripcion"/>
						</logic:equal>
					</td>
				</tr>
				<logic:equal name="encHabilitacionAdapterVO" property="habilitacion.paramTipoHab" value="EXT">
					<tr>
						<!-- Cuenta -->		
						<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
					    <td class="normal" colspan="3">
							<logic:equal name="encHabilitacionAdapterVO" property="act" value="agregar">		
									<html:text name="encHabilitacionAdapterVO" property="habilitacion.cuenta.numeroCuenta" size="20"/>
									<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
										<bean:message bundle="esp" key="esp.habilitacion.button.buscarCuenta"/>
									</html:button>
							</logic:equal>
							<logic:equal name="encHabilitacionAdapterVO" property="act" value="modificar">		
								<bean:write name="encHabilitacionAdapterVO" property="habilitacion.cuenta.numeroCuenta"/>
							</logic:equal>
						</td>
					</tr>
				</logic:equal>
				<logic:notEqual name="encHabilitacionAdapterVO" property="habilitacion.paramTipoHab" value="EXT">
					<tr>
						<!-- Valores Cargados -->		
						<td><label>(*)&nbsp;<bean:message bundle="esp" key="esp.valoresCargados.label"/>: </label></td>
						<td class="normal" colspan="3">
						<logic:equal name="encHabilitacionAdapterVO" property="act" value="agregar">		
							<html:select name="encHabilitacionAdapterVO" property="habilitacion.valoresCargados.id" styleClass="select" >
								<html:optionsCollection name="encHabilitacionAdapterVO" property="listValoresCargados" label="descripcion" value="id" />
							</html:select>
			  			</logic:equal>
			  			<logic:equal name="encHabilitacionAdapterVO" property="act" value="modificar">		
							<bean:write name="encHabilitacionAdapterVO" property="habilitacion.valoresCargados.descripcion"/>
						</logic:equal>
			  			</td>	
					</tr>
				</logic:notEqual>
				
				<tr>
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.tipoEvento.label"/>: </label></td>
					<td class="normal">						
						<html:select name="encHabilitacionAdapterVO" property="habilitacion.tipoEvento.id" styleClass="select" >
							<html:optionsCollection name="encHabilitacionAdapterVO" property="listTipoEvento" label="descripcion" value="id" />
						</html:select>
	  				</td>
	  				<!-- Clasificacion del Evento -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.claHab.label"/>: </label></td>
					<td class="normal">						
						<html:select name="encHabilitacionAdapterVO" property="habilitacion.claHab.id" styleClass="select" >
							<html:optionsCollection name="encHabilitacionAdapterVO" property="listClaHab" label="value" value="id" />
						</html:select>
	  				</td>
				</tr>
				<!-- Tipo Cobro -->		
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="esp" key="esp.tipoCobro.label"/>: </label></td>
					<td class="normal" colspan="3">
						<logic:equal name="encHabilitacionAdapterVO" property="act" value="agregar">		
							<html:select name="encHabilitacionAdapterVO" property="habilitacion.tipoCobro.id" styleClass="select" >
								<html:optionsCollection name="encHabilitacionAdapterVO" property="listTipoCobro" label="descripcion" value="id" />
							</html:select>
			  			</logic:equal>
			  			<logic:equal name="encHabilitacionAdapterVO" property="act" value="modificar">		
							<bean:write name="encHabilitacionAdapterVO" property="habilitacion.tipoCobro.descripcion"/>
						</logic:equal>
			  		</td>	
				</tr>
				
				<tr>			
					<!-- Fecha Habilitacion -->		
					<td><label>(*)&nbsp;<bean:message bundle="esp" key="esp.habilitacion.fechaHab.label"/>: </label></td>
					<td class="normal" colspan="3">
					<logic:equal name="encHabilitacionAdapterVO" property="act" value="agregar">		
						<html:text name="encHabilitacionAdapterVO" property="habilitacion.fechaHabView" styleId="fechaHabView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaHabView');" id="a_fechaHabView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</logic:equal>
			  		<logic:equal name="encHabilitacionAdapterVO" property="act" value="modificar">		
						<bean:write name="encHabilitacionAdapterVO" property="habilitacion.fechaHabView"/>
					</logic:equal>
					</td>
				</tr>
				<!-- Descripcion -->		
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="esp" key="esp.habilitacion.descripcion.label"/>: </label></td>
					<td class="normal" colspan="3"><html:text name="encHabilitacionAdapterVO" property="habilitacion.descripcion" size="20" maxlength="20" styleClass="datos"/></td>					
				</tr>
				<!-- Lugar Evento -->		
				<tr>				
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.lugarEvento.label"/>: </label></td>
					<td class="normal">
						<html:select name="encHabilitacionAdapterVO" property="habilitacion.lugarEvento.id" styleClass="select" onchange="submitForm('paramLugarEvento', '');" >
							<html:optionsCollection name="encHabilitacionAdapterVO" property="listLugarEvento" label="descripcion" value="id" />
						</html:select>
					</td>
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.factorOcupacional.label"/>: </label></td>
					<td class="normal"><html:text name="encHabilitacionAdapterVO" property="habilitacion.factorOcupacionalView" size="20" maxlength="20" styleClass="datos"/></td>					
				</tr>
				<tr>
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.lugarEventoStr.label"/>: </label></td>
					<td class="normal" colspan="3"><html:text name="encHabilitacionAdapterVO" property="habilitacion.lugarEventoStr" size="20" maxlength="20" styleClass="datos"/></td>					
				</tr>
				<tr>
					<!-- Cantidad Funciones -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.cantFunciones.label"/>: </label></td>			
					<td class="normal"><html:text name="encHabilitacionAdapterVO" property="habilitacion.cantFuncionesView" size="15" maxlength="20" styleClass="datos"/></td>					
										
					<!-- Hora Acceso -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.horaAcceso.label"/>: </label></td>
					<td class="normal"><html:text name="encHabilitacionAdapterVO" property="habilitacion.horaAccesoView" styleId="horaAccesoView" size="15" maxlength="10" styleClass="datos" />
					</td>
				</tr>
				<tr>			
					<!-- Fecha Evento Desde -->		
					<td><label>(*)&nbsp;<bean:message bundle="esp" key="esp.habilitacion.fecEveDes.label"/>: </label></td>
					<td class="normal">
						<html:text name="encHabilitacionAdapterVO" property="habilitacion.fecEveDesView" styleId="fecEveDesView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fecEveDesView');" id="a_fecEveDesView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					<!-- Fecha Evento Hasta -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.fecEveHas.label"/>: </label></td>
					<td class="normal">
						<html:text name="encHabilitacionAdapterVO" property="habilitacion.fecEveHasView" styleId="fecEveHasView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fecEveHasView');" id="a_fecEveHasView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>
				<!-- Observacion-->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.observaciones.label"/>: </label></td>
					<td colspan="3" class="normal"><html:textarea name="encHabilitacionAdapterVO" property="habilitacion.observaciones" cols="75" rows="15"/></td>					
				</tr>
			</table>
		</fieldset>
	
		<fieldset>
		<legend><bean:message bundle="esp" key="esp.habilitacion.perHab.label"/></legend>
		<table class="tabladatos">
				<!-- Datos Persona -->	
					<bean:define id="personaVO" name="encHabilitacionAdapterVO" property="habilitacion.perHab"/>		
					<logic:equal name="encHabilitacionAdapterVO" property="act" value="agregar">		
					 <tr>
						<!-- Clasificacion del Organizador -->		
						<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.claOrg.label"/>: </label></td>
						<td class="normal">						
							<html:select name="encHabilitacionAdapterVO" property="habilitacion.claOrg.id" styleClass="select" >
								<html:optionsCollection name="encHabilitacionAdapterVO" property="listClaOrg" label="value" value="id" />
							</html:select>
		  				</td>
					 </tr>
					 <tr>
					 	<td colspan="4">
							<table width="100%">
							 	<tr>
								    <!-- Ingreso de datos -->
								    <td class="normal"><label>(*)&nbsp;<bean:message bundle="pad" key="pad.persona.sexo.label"/>: </label>								
										<select name="habilitacion.perHab.sexo.id" class="select">
											<option value="-1" <logic:equal name="personaVO" property="sexo.codigo" value="S">selected="selected"</logic:equal>>Seleccionar...</option>									
											
											<option value="1" <logic:equal name="personaVO" property="sexo.codigo" value="M">selected="selected"</logic:equal>>Masculino</option>
											
											<option value="0" <logic:equal name="personaVO" property="sexo.codigo" value="F">selected="selected" </logic:equal>>Femenino</option>
										</select>
										
										<label>(*)&nbsp;<bean:message bundle="pad" key="pad.documento.numero.ref"/>: </label> 
										<input type="text" name="habilitacion.perHab.documento.numeroView" value='<bean:write name="personaVO" property="documento.numeroView"/>' />
											
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
					
						
					<logic:equal name="personaVO" property="personaBuscada" value="true">
						
						<!-- Errores -->
						<logic:equal name="personaVO" property="hasError" value="true">
							<tr>
								<td colspan="6">
									<table width="100%">
										<tr>
											<td class="normal" colspan="6">
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
												</td>
												<td>
												</td>
												<td>
												</td>
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
								<td colspan="6">
									<table width="100%">
										<logic:equal name="personaVO" property="personaEncontrada" value="true">
											<tr>
												<td colspan="6">
													<div id="messagesNotFound" class="messages"> 
														<bean:message bundle="pad" key="pad.msgPersonaEncontrada"/>											  
												  	</div>
												</td>
											</tr>
										</logic:equal>									
										<tr>
											<logic:equal name="personaVO" property="personaEncontrada" value="true">
												<%@ include file="/pad/persona/includePersonaReducida.jsp"%>
											</logic:equal>
										</tr>
										<tr>
											<td>
											</td>
											<td>
											</td>
											<td>
											</td>
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
		<!--Fin Datos Persona -->
			</logic:equal>
			<logic:equal name="encHabilitacionAdapterVO" property="act" value="modificar">		
				<tr>
					<%@ include file="/pad/persona/includePersonaReducida.jsp"%>
				</tr>
			</logic:equal>
		</table>
		</fieldset>
	
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
					<logic:equal name="encHabilitacionAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="encHabilitacionAdapterVO" property="act" value="agregar">
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
		