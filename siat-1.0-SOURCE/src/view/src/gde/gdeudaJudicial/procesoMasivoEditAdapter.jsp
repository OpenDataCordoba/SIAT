<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">		
	function changeFecha(){
		submitForm('paramRecurso', '');
	}
</script>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarProcesoMasivo.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="procesoMasivoAdapterVO"/>
		<bean:define id="poseeParam" value="true" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">	
	
		<h1><bean:message bundle="gde" key="gde.procesoMasivoAdapter.title"/></h1>	
		
		<!-- ProcesoMasivo -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.procesoMasivo.title"/></legend>
			
			<table class="tabladatos">
				
				<logic:equal name="procesoMasivoAdapterVO" property="act" value="agregar">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.procesoMasivo.id.label"/>: </label></td>
						<td class="normal" colspan="3"><bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.idView" /></td>
					</tr>
					<!-- TipProMas -->
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.procesoMasivo.tipProMas.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.tipProMas.desTipProMas" />
						</td>
					</tr>
					<!-- ViaDeuda -->
					<tr>
						<logic:equal name="procesoMasivoAdapterVO" property="procesoMasivo.viaDeudaBussEnabled" value="true">
							<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.procesoMasivo.viaDeuda.label"/>: </label></td>
							<td class="normal" colspan="3">	
								<html:select name="procesoMasivoAdapterVO" property="procesoMasivo.viaDeuda.id" styleClass="select">
									<html:optionsCollection name="procesoMasivoAdapterVO" property="listViaDeuda" label="desViaDeuda" value="id" />
								</html:select>
							</td>
						</logic:equal>
						<logic:notEqual name="procesoMasivoAdapterVO" property="procesoMasivo.viaDeudaBussEnabled" value="true">
							<td><label><bean:message bundle="gde" key="gde.procesoMasivo.viaDeuda.label"/>: </label></td>
							<td class="normal" colspan="3">	
								<bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.viaDeuda.desViaDeuda" />
							</td>
						</logic:notEqual>
					</tr>
					<!-- FechaEnvio -->
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.procesoMasivo.fechaEnvio.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:text name="procesoMasivoAdapterVO" property="procesoMasivo.fechaEnvioView" styleId="fechaEnvioView" size="15" maxlength="10" styleClass="datos" onchange="changeFecha();" />
							<a class="link_siat" onclick="return show_calendar_change('fechaEnvioView');" id="a_fechaEnvioView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
							</a>
						</td>
					</tr>
					<!-- Recurso -->
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:select name="procesoMasivoAdapterVO" property="procesoMasivo.recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');" styleId="cboRecurso" style="width:90%">
								<bean:define id="includeRecursoList" name="procesoMasivoAdapterVO" property="listRecurso"/>
								<bean:define id="includeIdRecursoSelected" name="procesoMasivoAdapterVO" property="procesoMasivo.recurso.id"/>
								<%@ include file="/def/gravamen/includeRecurso.jsp" %>
							</html:select>
							
							<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
								<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
								src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
							</a>
							
						</td>
					</tr>
					<!-- Caso -->
					<tr>
						<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
						<td colspan="3">
							<bean:define id="IncludedVO" name="procesoMasivoAdapterVO" property="procesoMasivo"/>
							<bean:define id="voName" value="procesoMasivo" />
							<%@ include file="/cas/caso/includeCaso.jsp" %>
						</td>
					</tr>		
	
					<!-- genera constacia de deudas? -->
					<logic:equal name="procesoMasivoAdapterVO" property="procesoMasivo.generaConstanciaEnabled" value="true">
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.procesoMasivo.generaConstancia.label"/>: </label></td>
						<td class="normal" colspan="3">	
							<html:select name="procesoMasivoAdapterVO" property="procesoMasivo.generaConstancia.id" styleClass="select">
								<html:optionsCollection name="procesoMasivoAdapterVO" property="listGeneraConstancia" label="value" value="id" />
							</html:select>
						</td>
					</tr>
					</logic:equal>
					
					<!-- ConCuentaExcSel -->
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.procesoMasivo.conCuentaExcSel.label"/>: </label></td>
						<td class="normal" colspan="3">	
							<html:select name="procesoMasivoAdapterVO" property="procesoMasivo.conCuentaExcSel.id" styleClass="select">
								<html:optionsCollection name="procesoMasivoAdapterVO" property="listConCuentaExcSel" label="value" value="id" />
							</html:select>
						</td>
					</tr>
					<!-- Observacion -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.procesoMasivo.observacion.label"/>: </label></td>
						<td class="normal" colspan="3">	
							<html:textarea name="procesoMasivoAdapterVO" property="procesoMasivo.observacion" cols="80" rows="15"/>
						</td>
					</tr>
					<!-- Si el Criterio de procurador esta habilitado: -->					
					<logic:equal name="procesoMasivoAdapterVO" property="procesoMasivo.criterioProcuradorEnabled" value="true">
						<!-- UtilizaCriterio y procurador -->
						<tr>
							<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.procesoMasivo.utilizaCriterio.label"/>: </label></td>
							<td class="normal">	
								<html:select name="procesoMasivoAdapterVO" property="procesoMasivo.utilizaCriterio.id" styleClass="select" onchange="submitForm('paramUtCri', '');">
									<html:optionsCollection name="procesoMasivoAdapterVO" property="listUtilizaCriterio" label="value" value="id" />
								</html:select>
							</td>
							
							<logic:equal name="procesoMasivoAdapterVO" property="procesoMasivo.utilizaCriterio.esNO" value="true">
								<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
								<td class="normal">	
									<html:select name="procesoMasivoAdapterVO" property="procesoMasivo.procurador.id" styleClass="select" disabled="false">
										<html:optionsCollection name="procesoMasivoAdapterVO" property="listProcurador" label="descripcion" value="id" />
									</html:select>
								</td>
							</logic:equal>
							<logic:notEqual name="procesoMasivoAdapterVO" property="procesoMasivo.utilizaCriterio.esNO" value="true">							
								<td><label><bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>							
								<td class="normal">	
									<html:select name="procesoMasivoAdapterVO" property="procesoMasivo.procurador.id" styleClass="select" disabled="true" >
										<html:optionsCollection name="procesoMasivoAdapterVO" property="listProcurador" label="descripcion" value="id" />
									</html:select>
								</td>
							</logic:notEqual>
						</tr>
					</logic:equal>
				</logic:equal>
	
				<logic:equal name="procesoMasivoAdapterVO" property="act" value="modificar">
				<!-- MODIFICAR -->				
					<!-- TipProMas -->
				    <tr>
					    <td><label><bean:message bundle="gde" key="gde.procesoMasivo.tipProMas.label"/>: </label></td>
					    <td class="normal" colspan="3"><bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.tipProMas.desTipProMas" /></td>
				    </tr>
				    <!-- ViaDeuda -->
				    <tr>
			    		<td><label><bean:message bundle="gde" key="gde.procesoMasivo.viaDeuda.label"/>: </label></td>
						<td class="normal" colspan="3">	
							<bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.viaDeuda.desViaDeuda" />
						</td>
				    </tr>
				    <!-- id del proceso -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.procesoMasivo.id.label"/>: </label></td>
						<td class="normal" colspan="3"><bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.idView" />	</td>
					</tr>
					
					
				    <!-- FechaEnvio -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.procesoMasivo.fechaEnvio.label"/>: </label></td>
						<td class="normal" colspan="3"><bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.fechaEnvioView" />	</td>
					</tr>
					<!-- Recurso -->					
					<tr>
						<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal" colspan="3"><bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.recurso.desRecurso" />	</td>
					</tr>
	
					<!-- genera constacia de deudas? -->
					<logic:equal name="procesoMasivoAdapterVO" property="procesoMasivo.generaConstanciaEnabled" value="true">
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.procesoMasivo.generaConstancia.label"/>: </label></td>
						<td class="normal" colspan="3">	
							<html:select name="procesoMasivoAdapterVO" property="procesoMasivo.generaConstancia.id" styleClass="select">
								<html:optionsCollection name="procesoMasivoAdapterVO" property="listGeneraConstancia" label="value" value="id" />
							</html:select>
						</td>
					</tr>
					</logic:equal>
	
					<!-- Caso -->
					<tr>
						<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
						<td colspan="3">
							<bean:define id="IncludedVO" name="procesoMasivoAdapterVO" property="procesoMasivo"/>
							<bean:define id="voName" value="procesoMasivo" />
							<%@ include file="/cas/caso/includeCaso.jsp" %>
						</td>
					</tr>		
					<!-- ConCuentaExcSel -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.procesoMasivo.conCuentaExcSel.label"/>: </label></td>
						<td class="normal" colspan="3"><bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.conCuentaExcSel.value" />	</td>
					</tr>
					<!-- Observacion -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.procesoMasivo.observacion.label"/>: </label></td>
						<td class="normal" colspan="3">	
							<html:textarea name="procesoMasivoAdapterVO" property="procesoMasivo.observacion" cols="80" rows="15"/>
						</td>
					</tr>
					<!-- Si el Criterio de procurador esta habilitado: -->					
					<logic:equal name="procesoMasivoAdapterVO" property="procesoMasivo.criterioProcuradorEnabled" value="true">
						<!-- UtilizaCriterio y procurador -->
						<tr>
							<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.procesoMasivo.utilizaCriterio.label"/>: </label></td>
							<td class="normal">	
								<html:select name="procesoMasivoAdapterVO" property="procesoMasivo.utilizaCriterio.id" styleClass="select" onchange="submitForm('paramUtCri', '');">
									<html:optionsCollection name="procesoMasivoAdapterVO" property="listUtilizaCriterio" label="value" value="id" />
								</html:select>
							</td>
							<logic:equal name="procesoMasivoAdapterVO" property="procesoMasivo.utilizaCriterio.esNO" value="true">
								<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
								<td class="normal">	
									<html:select name="procesoMasivoAdapterVO" property="procesoMasivo.procurador.id" styleClass="select" disabled="false">
										<html:optionsCollection name="procesoMasivoAdapterVO" property="listProcurador" label="descripcion" value="id" />
									</html:select>
								</td>
							</logic:equal>
							<logic:notEqual name="procesoMasivoAdapterVO" property="procesoMasivo.utilizaCriterio.esNO" value="true">							
								<td><label><bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>							
								<td class="normal">	
									<html:select name="procesoMasivoAdapterVO" property="procesoMasivo.procurador.id" styleClass="select" disabled="true" >
										<html:optionsCollection name="procesoMasivoAdapterVO" property="listProcurador" label="descripcion" value="id" />
									</html:select>
								</td>
							</logic:notEqual>
						</tr>
					</logic:equal>
					<!-- Usuario Alta y Usuario de la corrida del proceso -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.procesoMasivo.usuarioAlta.label"/>: </label></td>
						<td class="normal">	<bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.usuarioAlta" /> </td>
						<td><label><bean:message bundle="pro" key="pro.corrida.usuario.ref"/>: </label></td>
						<td class="normal">	<bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.corrida.usuario" /> </td>
					</tr>								
					<!-- EstadoCorrida -->					
					<tr>
						<td><label><bean:message bundle="pro" key="pro.estadoCorrida.proceso.label"/>: </label></td>
						<td class="normal" colspan="3">	<bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.corrida.estadoCorrida.desEstadoCorrida" /> </td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.procesoMasivo.enviadoContr.label"/></label></td>
						<td class="normal" colspan="3"><bean:write name="procesoMasivoAdapterVO" property="procesoMasivo.enviadoContrSiNo" /></td>
					</tr>					
				</logic:equal>
			</table>
		</fieldset>	
		<!-- ProcesoMasivo -->
			
		<!-- Si la seleccion de Formularios esta habilitada -->
		<logic:equal name="procesoMasivoAdapterVO" property="procesoMasivo.seleccionFormularioEnabled" value="true">
			<!-- Datos del Reporte -->
			<fieldset >
				<legend><bean:message bundle="gde" key="gde.procesoMasivoAdapter.reportes.title"/></legend>
				<table class="tabladatos">
					<tr>
						<!-- Lista de los Formularios -->
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.procesoMasivoAdapter.formulario.label" />: </label></td>
						<td class="normal">
							<html:select name="procesoMasivoAdapterVO" property="procesoMasivo.formulario.id" styleClass="select" onchange="submitForm('paramFormulario', '');">
								<html:optionsCollection name="procesoMasivoAdapterVO" property="listFormulario" label="desFormulario" value="id" />
							</html:select>
						</td>					
					</tr>
	
					<tr>
						<!-- Formato de Salida -->
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.procesoMasivoAdapter.formatoSalida.label"/>: </label></td>
						<td class="normal">
							<html:select name="procesoMasivoAdapterVO" property="procesoMasivo.formulario.formatoSalida.id" styleClass="select" >
								<html:optionsCollection name="procesoMasivoAdapterVO" property="listFormatoSalida" label="value" value="id" />
							</html:select>
						</td>	
						
					</tr>
						<!-- Renglon Vacio -->
					<tr><td></td></tr>
				</table>	
				
				<!-- Campos del Formulario -->
				<logic:equal name="procesoMasivoAdapterVO" property="mostrarListForCam" value="true">
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
						<caption>Campos del Reporte</caption>
						<tbody>
							<logic:notEmpty name="procesoMasivoAdapterVO" property="procesoMasivo.formulario.listForCam">
								<logic:iterate id="ForCamVO" name="procesoMasivoAdapterVO" property="procesoMasivo.formulario.listForCam">
									<tr>
										<td width="40%">
											<bean:write name="ForCamVO" property="desForCam"/>
											&nbsp;(<bean:write name="ForCamVO" property="largoMaxView"/>)
											</td>
										<td width="60%">
												<textarea style="height:130px;width:400px" name="<bean:write name="ForCamVO" property="codForCam"/>"><bean:write name="ForCamVO" property="valorDefecto"/></textarea>
											</td>
									</tr>	
								</logic:iterate>
							</logic:notEmpty>
							<logic:empty name="procesoMasivoAdapterVO" property="procesoMasivo.formulario.listForCam">
								<tr>
									<td class="normal" align="center">
										<bean:message bundle="gde" key="gde.procesoMasivoAdapter.emptyListForCam.label"/>
									</td>
								</tr>
							</logic:empty>
						</tbody>
					</table>
				</logic:equal>
				<!--FIN Campos del Formulario -->
			</fieldset>
		</logic:equal>
		<!-- Fin Datos del Reporte -->
		
		<table class="tablabotones" width="100%" >
		   	<tr>
	  	   		<td align="left" width="50%">
		   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
		   	    		<bean:message bundle="base" key="abm.button.volver"/>
		   	    	</html:button>
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="procesoMasivoAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="procesoMasivoAdapterVO" property="act" value="agregar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
							<bean:message bundle="base" key="abm.button.agregar"/>
						</html:button>
					</logic:equal>
	   	    	</td>   	    	
	   	    </tr>
	   	</table>
	
	</span>

	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclucion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
