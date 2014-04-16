<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/exe/AdministrarEncCueExe.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="exe" key="exe.cueExeEditAdapter.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>

	<!-- datos del Objeto Imponible -->
	<logic:greaterThan name="encCueExeAdapterVO" property="cueExe.cuenta.id" value="0">
		<fieldset>
			<legend><bean:message bundle="exe" key="exe.objImp.title"/></legend>		
				<table class="tabladatos">
					<tr>
						<td><label><bean:write name="encCueExeAdapterVO" property="cueExe.cuenta.objImp.desClave"/>:</label></td>
						<td class="normal">
							&nbsp;<bean:write name="encCueExeAdapterVO" property="cueExe.cuenta.objImp.clave"/>
						</td>
					</tr>
					<tr>
						<td><label><bean:write name="encCueExeAdapterVO" property="cueExe.cuenta.objImp.desClaveFuncional"/>:</label></td>
						<td class="normal">
							&nbsp;<bean:write name="encCueExeAdapterVO" property="cueExe.cuenta.objImp.claveFuncional"/>
						</td>
					</tr>
				</table>				
		</fieldset>
	</logic:greaterThan>
	
	<!-- CueExe -->
	<fieldset>
		<legend><bean:message bundle="exe" key="exe.cueExe.title"/></legend>
		
		<table class="tabladatos">
			
			<!-- Fecha Solicitud -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaSolicitud.label"/>: </label></td>
				<td class="normal" colspan="3">
					<logic:equal name="encCueExeAdapterVO" property="act" value="agregar">
						<html:text name="encCueExeAdapterVO" property="cueExe.fechaSolicitudView" styleId="fechaSolicitudView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaSolicitudView');" id="a_fechaSolicitudView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</logic:equal>
					
					<logic:equal name="encCueExeAdapterVO" property="act" value="modificar">	
						<bean:write name="encCueExeAdapterVO" property="cueExe.fechaSolicitudView"/>
					</logic:equal>	
				</td>	
			</tr>
			
			<!-- Recurso -->
			<tr>
				<logic:equal name="encCueExeAdapterVO" property="act" value="modificar">
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="3">
						<bean:write name="encCueExeAdapterVO" property="cueExe.recurso.desRecurso"/>
					</td>	
				</logic:equal>
				<logic:equal name="encCueExeAdapterVO" property="act" value="agregar">
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="3">
						<logic:equal name="encCueExeAdapterVO" property="disableCombo" value="true">
							<html:select name="encCueExeAdapterVO" property="cueExe.recurso.id" styleClass="select" disabled="true">
								<html:optionsCollection name="encCueExeAdapterVO" property="listRecurso" label="desRecurso" value="id" />
							</html:select>						
						</logic:equal>
						<logic:equal name="encCueExeAdapterVO" property="disableCombo" value="false">
							<html:select name="encCueExeAdapterVO" property="cueExe.recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');">
								<bean:define id="includeRecursoList" name="encCueExeAdapterVO" property="listRecurso"/>
								<bean:define id="includeIdRecursoSelected" name="encCueExeAdapterVO" property="cueExe.recurso.id"/>
								<%@ include file="/def/gravamen/includeRecurso.jsp" %>
							</html:select>
						</logic:equal>	
					</td>
				</logic:equal>
			</tr>
			
			<!-- Exencion -->
			<tr>
				<logic:equal name="encCueExeAdapterVO" property="act" value="modificar">
					<td><label><bean:message bundle="exe" key="exe.exencion.label"/>: </label></td>
					<td class="normal" colspan="3">
						<bean:write name="encCueExeAdapterVO" property="cueExe.exencion.desExencion"/>
					</td>	
				</logic:equal>
				<logic:equal name="encCueExeAdapterVO" property="act" value="agregar">					
					<td><label>(*)&nbsp;<bean:message bundle="exe" key="exe.exencion.label"/>: </label></td>
					<td class="normal" colspan="3">
						<logic:equal name="encCueExeAdapterVO" property="disableCombo" value="true">
							<html:select name="encCueExeAdapterVO" property="cueExe.exencion.id" styleClass="select" disabled="true">
								<html:optionsCollection name="encCueExeAdapterVO" property="listExencion" label="desExencion" value="id" />
							</html:select>
						</logic:equal>
						<logic:equal name="encCueExeAdapterVO" property="disableCombo" value="false">
							<html:select name="encCueExeAdapterVO" property="cueExe.exencion.id" styleClass="select" onchange="submitForm('paramExencion', '');">
								<html:optionsCollection name="encCueExeAdapterVO" property="listExencion" label="desExencion" value="id" />
							</html:select>
						</logic:equal>	
					</td>		
				</logic:equal>
			</tr>
			
			<!-- Cuenta -->
			<tr>
				<logic:equal name="encCueExeAdapterVO" property="act" value="agregar">
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>: </label></td>
					<td class="normal" colspan="3">
						<html:text name="encCueExeAdapterVO" property="cueExe.cuenta.numeroCuenta" size="15" maxlength="10" styleClass="datos"/>
						
						<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
							<bean:message bundle="exe" key="exe.cueExeEditAdapter.button.buscarCuenta"/>
						</html:button>
					</td>
				</logic:equal>
				<logic:equal name="encCueExeAdapterVO" property="act" value="modificar">
					<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>: </label></td>
					<td class="normal" colspan="3">
						<bean:write name="encCueExeAdapterVO" property="cueExe.cuenta.numeroCuenta"/>
					</td>
				</logic:equal>
			</tr>
			
			<!-- Tipo Sujeto Solicitante -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="exe" key="exe.tipoSujeto.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="encCueExeAdapterVO" property="cueExe.tipoSujeto.id" styleClass="select">
						<html:optionsCollection name="encCueExeAdapterVO" property="listTipoSujeto" label="desTipoSujeto" value="id" />
					</html:select>
				</td>
			</tr>
			
			<!-- Fecha Desde / Hasta  -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="exe" key="exe.cueExe.fechaDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="encCueExeAdapterVO" property="cueExe.fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="encCueExeAdapterVO" property="cueExe.fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
		
			<!-- Solicitante -->
			<tr>
				<td><label>(**)&nbsp;<bean:message bundle="exe" key="exe.cueExe.solicitante.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="encCueExeAdapterVO" property="cueExe.solicitante.view" size="20" disabled="true"/>
					<html:button property="btnBuscarSolicitante"  styleClass="boton" onclick="submitForm('buscarSolicitante', '');">
						<bean:message bundle="exe" key="exe.cueExeAdapter.button.buscarSolicitante"/>
					</html:button>
				</td>
			</tr>
			
			<!-- Descripcion Solicitante -->
			<tr>
				<td><label>(**)&nbsp;<bean:message bundle="exe" key="exe.cueExe.solicDescripcion.label"/>: </label></td>
				<td class="normal"><html:text name="encCueExeAdapterVO" property="cueExe.solicDescripcion" size="20" maxlength="100"/></td>			
			</tr>
			<tr>
				<td colspan="4" align="right">
					(**) Al menos uno de los dos datos es requerido
				</td>
			</tr>			


			<!-- Tipo Documento -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.tipoDocumento.label"/>: </label></td>
				<td class="normal" colspan="3"><html:text name="encCueExeAdapterVO" property="cueExe.tipoDocumento" size="20" maxlength="100"/></td>			
			</tr>
			
			<!-- Nro Documento -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.nroDocumento.label"/>: </label></td>
				<td class="normal" colspan="3"><html:text name="encCueExeAdapterVO" property="cueExe.nroDocumento" size="20" maxlength="100"/></td>			
			</tr>

			<!-- Fecha Resolucion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaResolucion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="encCueExeAdapterVO" property="cueExe.fechaResolucionView" styleId="fechaResolucionView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaResolucionView');" id="a_fechaResolucionView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>	
			</tr>
			
			<!-- Fecha Ultima Inspeccion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaUltIns.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="encCueExeAdapterVO" property="cueExe.fechaUltInsView" styleId="fechaUltInsView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaUltInsView');" id="a_fechaUltInsView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>	
			</tr>
			
			<!-- Fecha Presentacion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaPresent.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="encCueExeAdapterVO" property="cueExe.fechaPresentView" styleId="fechaPresentView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaPresentView');" id="a_fechaPresentView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>	
			</tr>
			
			<!-- Fecha Caducidad Habilitacion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaCadHab.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="encCueExeAdapterVO" property="cueExe.fechaCadHabView" styleId="fechaCadHabView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaCadHabView');" id="a_fechaCadHabView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>	
			</tr>
			
			<!-- Documentacion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.documentacion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:textarea name="encCueExeAdapterVO" property="cueExe.documentacion" cols="80" rows="10"/>
				</td>
			</tr>
		
			<!-- Ordenanza / Articulo -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.ordenanza.label"/>: </label></td>
				<td class="normal"><html:text name="encCueExeAdapterVO" property="cueExe.ordenanza" size="20" maxlength="100"/></td>			
				<td><label><bean:message bundle="exe" key="exe.cueExe.articulo.label"/>: </label></td>
				<td class="normal"><html:text name="encCueExeAdapterVO" property="cueExe.articulo" size="20" maxlength="100"/></td>			
			</tr>

			<!-- Inciso -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.inciso.label"/>: </label></td>
				<td class="normal" colspan="3"><html:text name="encCueExeAdapterVO" property="cueExe.inciso" size="20" maxlength="100"/></td>
			</tr>
			
			<!-- Observaciones -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.observaciones.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:textarea name="encCueExeAdapterVO" property="cueExe.observaciones" cols="80" rows="10"/>
				</td>
			</tr>
			
			<!-- Caso -->
			<!-- Inclucion de Caso -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="encCueExeAdapterVO" property="cueExe"/>
					<bean:define id="voName" value="cueExe" />
					<%@ include file="/cas/caso/includeCaso.jsp" %>
				</td>
			</tr>
			<!-- Fin Inclucion de Caso -->
			
			
			<!-- solic Fecha Desde / solic Hasta  -->
			<logic:equal name="encCueExeAdapterVO" property="poseeSolicFechas" value="true">
				<tr>
					<td><bean:message bundle="exe" key="exe.cueExe.solicFechaMigracion.msg"/>: </td>
					<td class="normal" colspan="3">
						<bean:message bundle="exe" key="exe.cueExe.solicFechaDesde.label"/>: &nbsp;
						<bean:write name="encCueExeAdapterVO" property="cueExe.solicFechaDesdeView"/>
						&nbsp; -
						<bean:message bundle="exe" key="exe.cueExe.solicFechaHasta.label"/>: &nbsp;
						<bean:write name="encCueExeAdapterVO" property="cueExe.solicFechaHastaView"/>					
					</td>
				</tr>
			</logic:equal>
			
		</table>
	</fieldset>

<logic:equal name="encCueExeAdapterVO" property="esExencionJubilado" value="true">
	<fieldset>
		<legend><bean:message bundle="exe" key="exe.cueExeAdapter.datosExtraJubilado"/></legend>
		<table class="tabladatos">		
			
			<!--  Nro Beneficiario / Caja -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.nroBeneficiario.label"/>: </label></td>
				<td class="normal"><html:text name="encCueExeAdapterVO" property="cueExe.nroBeneficiario" size="20" maxlength="100"/></td>			
				<td><label><bean:message bundle="exe" key="exe.cueExe.caja.label"/>: </label></td>
				<td class="normal"><html:text name="encCueExeAdapterVO" property="cueExe.caja" size="20" maxlength="100"/></td>			
			</tr>
			
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.clase.label"/>: </label></td>
				<td class="normal"><html:text name="encCueExeAdapterVO" property="cueExe.clase" size="20" maxlength="100"/></td>			
			</tr>
			
			<!-- Fecha Vencimiento Contrato inquilino  -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaVencContInq.label"/>: </label></td>
				<td class="normal">
					<html:text name="encCueExeAdapterVO" property="cueExe.fechaVencContInqView" styleId="fechaVencContInqView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaVencContInqView');" id="a_fechaVencContInqView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			
		</table>
	</fieldset>		
</logic:equal>
			

	<fieldset>
		<legend><bean:message bundle="exe" key="exe.cueExeAdapter.estado.title"/></legend>
	
		<table width="100%" class="tabladatos">
				<!-- Estado -->
			<tr>
				
				<logic:equal name="encCueExeAdapterVO" property="act" value="agregar">
					<logic:equal name="encCueExeAdapterVO" property="cueExe.permSelecEstadoInicial" value="false">
						<td width="50%"><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
						<td width="50%" class="normal">En Tramite</td>					
					</logic:equal>
					<logic:equal name="encCueExeAdapterVO" property="cueExe.permSelecEstadoInicial" value="true">					
						<td width="50%"><label>(*)&nbsp;<bean:message bundle="base" key="base.estado.label"/>: </label></td>
						<td width="50%" class="normal">
							<html:select name="encCueExeAdapterVO" property="cueExe.estadoCueExe.id" styleClass="select">
								<html:optionsCollection name="encCueExeAdapterVO" property="listEstadoCueExe" label="desEstadoCueExe" value="id" />
							</html:select>
						</td>		
					</logic:equal>
				</logic:equal>
				
				<logic:equal name="encCueExeAdapterVO" property="act" value="modificar">
					<td width="50%"><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td width="50%" class="normal">
						<bean:write name="encCueExeAdapterVO" property="cueExe.estadoCueExe.desEstadoCueExe"/>
					</td>	
				</logic:equal>
			</tr>
		</table>		
	</fieldset>

	<!-- CueExe -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="encCueExeAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="encCueExeAdapterVO" property="act" value="agregar">
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
