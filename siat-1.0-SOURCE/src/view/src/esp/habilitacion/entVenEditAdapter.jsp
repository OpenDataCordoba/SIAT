<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>
 
	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/esp/AdministrarEntVen.do">

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

		<!-- Datos de la Habilitacion -->
		<fieldset>
			<legend><bean:message bundle="esp" key="esp.habilitacion.title"/></legend>			
			<table class="tablabotones" width="100%">
				<tr>
					<!-- Recurso -->		
					<td><label>&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.habilitacion.recurso.desRecurso"/></td>
				</tr>
				<tr>
					<!-- Numero -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.numero.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.habilitacion.numeroView"/></td>
					<!-- Anio -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.anio.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.habilitacion.anioView"/></td>
				</tr>
				<tr>
					<!-- Tipo Habilitacion -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.tipoHab.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.habilitacion.tipoHab.descripcion"/></td>
					<!-- Tipo Cobro -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.tipoCobro.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.habilitacion.tipoCobro.descripcion"/></td>
				</tr>
				<logic:equal name="entVenAdapterVO" property="entVen.habilitacion.paramTipoHab" value="EXT">
				<tr>
					<!-- Cuenta -->		
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.habilitacion.cuenta.numeroCuenta"/></td>
				</tr>
				</logic:equal>
				<logic:notEqual name="entVenAdapterVO" property="entVen.habilitacion.paramTipoHab" value="EXT">
				<tr>
					<!-- Valores Cargados -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.valoresCargados.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.habilitacion.valoresCargados.descripcion"/></td>
				</tr>
				</logic:notEqual>
				<tr>
					<!-- Fecha Habilitacion -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.fechaHab.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.habilitacion.fechaHabView"/></td>
				</tr>
				<tr>
					<!-- Descripcion -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.descripcion.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.habilitacion.descripcion"/></td>
				</tr>
				<!-- Lugar Evento -->		
				<tr>				
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.lugarEvento.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.habilitacion.lugarEvento.descripcion"/></td>				
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.factorOcupacional.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.habilitacion.factorOcupacionalView"/></td>
				</tr>
				<tr>
					<!-- Desc. Lugar Evento -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.lugarEventoStr.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.habilitacion.lugarEventoStr"/></td>
				</tr>
				<tr>
					<!-- Hora Acceso -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.horaAcceso.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.habilitacion.horaAccesoView"/></td>

					<!-- Cantidad Funciones -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.cantFunciones.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.habilitacion.cantFuncionesView"/></td>
				</tr>
				<tr>
					<!-- Fecha Evento Desde -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.fecEveDes.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.habilitacion.fecEveDesView"/></td>
					<!-- Fecha Evento Hasta -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.fecEveHas.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.habilitacion.fecEveHasView"/></td>
				</tr>
				<tr>
					<!-- Observaciones -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.observaciones.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.habilitacion.observaciones"/></td>
				</tr>
				<tr>
					<!-- Estado Habilitacion -->		
					<td><label>&nbsp;<bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.habilitacion.estHab.desEstHab"/></td>
					<!-- Clasificacion del Evento -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.claHab.label"/>: </label></td>
					<td class="normal"><bean:write name="entVenAdapterVO" property="entVen.habilitacion.claHab.value"/></td>
				</tr>
			</table>
		</fieldset>
		<!-- Persona que Habilita -->		
		<fieldset>
			<legend><bean:message bundle="esp" key="esp.habilitacion.perHab.label"/></legend>			
			<table class="tablabotones" width="100%">
				<tr>
					<td colspan="6">
						<tr>
							<td class="normal">
								<bean:define id="personaVO" name="entVenAdapterVO" property="entVen.habilitacion.perHab"/>	
								<%@ include file="/pad/persona/includePersonaReducida.jsp"%>
							</td>
						</tr>
					</td>
				</tr>
			</table>
		</fieldset>
		<!-- Fin Datos Habilitacion -->
		
		<logic:equal name="entVenAdapterVO" property="esInterna" value="0">
		<!-- Datos de la entrada habilitada -->
		<fieldset>
			<table class="tabladatos" width="100%">
			<logic:notEqual name="entVenAdapterVO" property="act" value="anular">
					<logic:equal name="entVenAdapterVO" property="paramDDJJ" value="false">
						<tr>
							<td><label>(*)&nbsp;<bean:message bundle="esp" key="esp.entVen.fechaVencimiento.label"/>: </label></td>
							<td class="normal" width="100%" >
									
								<html:text name="entVenAdapterVO" property="fechaVencimientoView" styleId="fechaVencimientoView" size="15" maxlength="10" styleClass="datos" />
								<a class="link_siat" onclick="return show_calendar('fechaVencimientoView');" id="a_fechaVencimientoView">
									<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
							</td>					
						</tr>
					</logic:equal>
					<logic:equal name="entVenAdapterVO" property="paramDDJJ" value="true">
							<tr>
								<td><label>(*)&nbsp;<bean:message bundle="esp" key="esp.entVen.fechaVencimiento.label"/>: </label></td>
								<td class="normal"><bean:write name="entVenAdapterVO" property="fechaVencimientoView"/></td>
							</tr>
					</logic:equal>
			</logic:notEqual>		
		</table>
		</fieldset>
		<!-- Fin Datos de la entrada habilitada -->
		</logic:equal>
		
		<!-- Entradas Habilitadas vender-->	
		<logic:notEqual name="entVenAdapterVO" property="act" value="anular">	
		<div style="border:0px" class="horizscroll">
		<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="esp" key="esp.entHab.label"/></caption>
	    	<tbody>
			    	<tr>
						
						<th align="left"><bean:message bundle="esp" key="esp.tipoEntrada.label"/></th>
						<th align="left"><bean:message bundle="esp" key="esp.entVen.serie.label"/></th>						
						<th align="left"><bean:message bundle="esp" key="esp.entVen.descripcion.label"/></th>												
						<th align="left"><bean:message bundle="esp" key="esp.entVen.nroDesde.label"/></th>						
						<th align="left"><bean:message bundle="esp" key="esp.entVen.nroHasta.label"/></th>						
						<th align="left"><bean:message bundle="esp" key="esp.entVen.cantHabilitadas.label"/></th>						
						<th align="left"><bean:message bundle="esp" key="esp.entVen.cantRestantes.label"/></th>
						<th align="left"><bean:message bundle="esp" key="esp.entVen.vendidas.label"/></th>
					</tr>
					<logic:notEmpty  name="entVenAdapterVO" property="listEntHab">	    	
					<logic:iterate id="EntHabVO" name="entVenAdapterVO" property="listEntHab">
							<tr>
							<td><bean:write name="EntHabVO" property="precioEvento.descripcion"/>&nbsp;</td>
							<td><bean:write name="EntHabVO" property="serie"/>&nbsp;</td>
							<td><bean:write name="EntHabVO" property="descripcion"/>&nbsp;</td>							
							<td><bean:write name="EntHabVO" property="nroDesdeView"/>&nbsp;</td>
							<td><bean:write name="EntHabVO" property="nroHastaView"/>&nbsp;</td>
							<td><bean:write name="EntHabVO" property="totalHabilitadasView"/>&nbsp;</td>
							<td><bean:write name="EntHabVO" property="totalRestantesView"/>&nbsp;</td>
							<logic:equal name="EntHabVO" property="totalRestantesView" value="0">
								<logic:equal name="EntHabVO" property="vendidasView" value="0">
									<td class="normal"  > 
									<input type="text" size="10" maxlength="10"   disabled="true"
										name='vendidas<bean:write name="EntHabVO" property="idView"/>'
										id='vendidas<bean:write name="EntHabVO" property="idView"/>'
										value='<bean:write name="EntHabVO" property="vendidasView" />' 
									/>
									</td>
								</logic:equal>
								<logic:notEqual name="EntHabVO" property="vendidasView" value="0">
									<td class="normal"  > 
									<input type="text" size="10" maxlength="10" 
										name='vendidas<bean:write name="EntHabVO" property="idView"/>'
										id='vendidas<bean:write name="EntHabVO" property="idView"/>'
										value='<bean:write name="EntHabVO" property="vendidasView" />' 
									/>
									</td>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="EntHabVO" property="totalRestantesView" value="0">
								<td class="normal"> 
								<input type="text" size="10" maxlength="10" 
									name='vendidas<bean:write name="EntHabVO" property="idView"/>'
									id='vendidas<bean:write name="EntHabVO" property="idView"/>'
									value='<bean:write name="EntHabVO" property="vendidasView" />' 
								/>
								</td>
							</logic:notEqual>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="entVenAdapterVO" property="listEntHab">
					<tr><td colspan="9" align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
		
			</tbody>
			</table>
			</div>
			
		&nbsp;
		</logic:notEqual>
		<!-- Entradas Habilitadas vender -->			
				
		
		<!-- Entradas Habilitadas anuladas -->		
		<logic:equal name="entVenAdapterVO" property="act" value="anular">
		<div style="border:0px" class="horizscroll">
		<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="esp" key="esp.entHab.label"/></caption>
	    	<tbody>
			    	<tr>
						
						<th align="left"><bean:message bundle="esp" key="esp.tipoEntrada.label"/></th>
						<th align="left"><bean:message bundle="esp" key="esp.entVen.serie.label"/></th>						
						<th align="left"><bean:message bundle="esp" key="esp.entVen.descripcion.label"/></th>												
						<th align="left"><bean:message bundle="esp" key="esp.entVen.nroDesde.label"/></th>						
						<th align="left"><bean:message bundle="esp" key="esp.entVen.nroHasta.label"/></th>						
						<th align="left"><bean:message bundle="esp" key="esp.entVen.cantHabilitadas.label"/></th>						
						<th align="left"><bean:message bundle="esp" key="esp.entVen.cantRestantes.label"/></th>
						<th align="left"><bean:message bundle="esp" key="esp.entVen.anuladas.label"/></th>
					</tr>
					<logic:notEmpty  name="entVenAdapterVO" property="listEntHab">	    	
					<logic:iterate id="EntHabVO" name="entVenAdapterVO" property="listEntHab">
							<tr>
							<td><bean:write name="EntHabVO" property="precioEvento.descripcion"/>&nbsp;</td>
							<td><bean:write name="EntHabVO" property="serie"/>&nbsp;</td>
							<td><bean:write name="EntHabVO" property="descripcion"/>&nbsp;</td>							
							<td><bean:write name="EntHabVO" property="nroDesdeView"/>&nbsp;</td>
							<td><bean:write name="EntHabVO" property="nroHastaView"/>&nbsp;</td>
							<td><bean:write name="EntHabVO" property="totalHabilitadasView"/>&nbsp;</td>
							<td><bean:write name="EntHabVO" property="totalRestantesView"/>&nbsp;</td>
							<logic:equal name="EntHabVO" property="totalRestantesView" value="0">
								<logic:equal name="EntHabVO" property="anuladasView" value="0">
									<td class="normal"  > 
									<input type="text" size="10" maxlength="10"   disabled="true"
										name='anuladas<bean:write name="EntHabVO" property="idView"/>'
										id='anuladas<bean:write name="EntHabVO" property="idView"/>'
										value='<bean:write name="EntHabVO" property="anuladasView" />' 
									/>
									</td>
								</logic:equal>
								<logic:notEqual name="EntHabVO" property="anuladasView" value="0">
									<td class="normal"  > 
									<input type="text" size="10" maxlength="10" 
										name='anuladas<bean:write name="EntHabVO" property="idView"/>'
										id='anuladas<bean:write name="EntHabVO" property="idView"/>'
										value='<bean:write name="EntHabVO" property="anuladasView" />' 
									/>
									</td>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="EntHabVO" property="totalRestantesView" value="0">
								<td class="normal"> 
								<input type="text" size="10" maxlength="10" 
									name='anuladas<bean:write name="EntHabVO" property="idView"/>'
									id='anuladas<bean:write name="EntHabVO" property="idView"/>'
									value='<bean:write name="EntHabVO" property="anuladasView" />' 
								/>
								</td>
							</logic:notEqual>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="entVenAdapterVO" property="listEntHab">
					<tr><td colspan="9" align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
		
			</tbody>
			</table>
			</div>
			
			</logic:equal>
			
		<!-- Entradas Habilitadas anuladas -->
		
				
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
					<logic:equal name="entVenAdapterVO" property="act" value="anular">
						<html:button property="btnCalcular"  styleClass="boton" onclick="submitForm('anular', '');">
							<bean:message bundle="esp" key="abm.button.anular"/>
						</html:button>
					</logic:equal>
					<logic:equal name="entVenAdapterVO" property="esInterna" value="0">
						<logic:notEqual name="entVenAdapterVO" property="act" value="anular">
			   	    		<html:button property="btnCalcular"  styleClass="boton" onclick="submitForm('calcular', '');">
								<bean:message bundle="esp" key="abm.button.calcular"/>
							</html:button>
						</logic:notEqual>
					</logic:equal>
					<logic:equal name="entVenAdapterVO" property="esInterna" value="1">
						<logic:notEqual name="entVenAdapterVO" property="act" value="anular">
							<html:button property="btnCalcular"  styleClass="boton" onclick="submitForm('agregarForInt', '');">
								<bean:message bundle="base" key="abm.button.agregar"/>
							</html:button>
						</logic:notEqual>
					</logic:equal>
				</td>
				<td align="right">
					<logic:equal name="entVenAdapterVO" property="act" value="modificar">
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
		
		<!-- Inclusion del Codigo Javascript del Calendar-->
		<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
				
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->