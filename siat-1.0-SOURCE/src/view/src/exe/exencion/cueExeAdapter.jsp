<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/exe/AdministrarCueExe.do">

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
	<logic:greaterThan name="cueExeAdapterVO" property="cueExe.cuenta.id" value="0">
		<fieldset>
			<legend><bean:message bundle="exe" key="exe.objImp.title"/></legend>		
				<table class="tabladatos">
					<tr>
						<td><label><bean:write name="cueExeAdapterVO" property="cueExe.cuenta.objImp.desClave"/>:</label></td>
						<td class="normal">
							&nbsp;<bean:write name="cueExeAdapterVO" property="cueExe.cuenta.objImp.clave"/>
						</td>
					</tr>
					<tr>
						<td><label><bean:write name="cueExeAdapterVO" property="cueExe.cuenta.objImp.desClaveFuncional"/>:</label></td>
						<td class="normal">
							&nbsp;<bean:write name="cueExeAdapterVO" property="cueExe.cuenta.objImp.claveFuncional"/>
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
					<bean:write name="cueExeAdapterVO" property="cueExe.fechaSolicitudView"/>
				</td>	
			</tr>
			
			<!-- Recurso -->
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeAdapterVO" property="cueExe.recurso.desRecurso"/>
				</td>	
			</tr>
			
			<!-- Exencion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.exencion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeAdapterVO" property="cueExe.exencion.desExencion"/>
				</td>
			</tr>
			
			<!-- Cuenta -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeAdapterVO" property="cueExe.cuenta.numeroCuenta"/>
				</td>
			</tr>
			
			<!-- Tipo Sujeto Solicitante -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.tipoSujeto.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeAdapterVO" property="cueExe.tipoSujeto.desTipoSujeto"/>
				</td>	
			</tr>
			
			<!-- Fecha Desde / Hasta  -->
			<tr>

				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaDesde.label"/>: </label></td>
				<td class="normal"><bean:write name="cueExeAdapterVO" property="cueExe.fechaDesdeView" /></td>
				
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaHasta.label"/>: </label></td>
				<td class="normal"><bean:write name="cueExeAdapterVO" property="cueExe.fechaHastaView" /></td>
			</tr>
		
			<!-- Solicitante -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.solicitante.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeAdapterVO" property="cueExe.solicitante.represent"/>
				</td>	
			</tr>
			
			<!-- Descripcion Solicitante -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.solicDescripcion.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cueExeAdapterVO" property="cueExe.solicDescripcion"/></td>			
			</tr>
			
			<!-- Tipo Documento -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.tipoDocumento.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cueExeAdapterVO" property="cueExe.tipoDocumento"/></td>			
			</tr>
			
			<!-- Nro Documento -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.nroDocumento.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cueExeAdapterVO" property="cueExe.nroDocumento"/></td>			
			</tr>
			
			<!-- Fecha Resolucion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaResolucion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeAdapterVO" property="cueExe.fechaResolucionView"/>
				</td>	
			</tr>
			
			<!-- Fecha Ultima Inspeccion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaUltIns.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeAdapterVO" property="cueExe.fechaUltInsView"/>
				</td>
			</tr>
			
			<!-- Fecha Presentacion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaPresent.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeAdapterVO" property="cueExe.fechaPresentView"/>
				</td>
			</tr>
			
			<!-- Fecha Caducidad Habilitacion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaCadHab.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeAdapterVO" property="cueExe.fechaCadHabView"/>
				</td>
			</tr>
			
			<!-- Documentacion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.documentacion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeAdapterVO" property="cueExe.documentacion"/>
				</td>
			</tr>
			
			<!-- Ordenanza / Articulo -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.ordenanza.label"/>: </label></td>
				<td class="normal"><bean:write name="cueExeAdapterVO" property="cueExe.ordenanza"/></td>			
				<td><label><bean:message bundle="exe" key="exe.cueExe.articulo.label"/>: </label></td>
				<td class="normal"><bean:write name="cueExeAdapterVO" property="cueExe.articulo"/></td>			
			</tr>

			<!-- Inciso -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.inciso.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cueExeAdapterVO" property="cueExe.inciso"/></td>
			</tr>
			
			<!-- Observaciones -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.observaciones.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeAdapterVO" property="cueExe.observaciones"/>
				</td>
			</tr>
			
			<!-- Caso -->
			<!-- Inclucion de CasoView -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="cueExeAdapterVO" property="cueExe"/>
					<%@ include file="/cas/caso/includeCasoView.jsp" %>				
				</td>
			</tr>
			<!-- Fin Inclucion de CasoView -->
			
			<!-- solic Fecha Desde / solic Hasta  -->
			<logic:equal name="cueExeAdapterVO" property="poseeSolicFechas" value="true">
				<tr>
					<td><bean:message bundle="exe" key="exe.cueExe.solicFechaMigracion.msg"/>: </td>
					<td class="normal" colspan="3">
						<bean:message bundle="exe" key="exe.cueExe.solicFechaDesde.label"/>: &nbsp;
						<bean:write name="cueExeAdapterVO" property="cueExe.solicFechaDesdeView"/>
						&nbsp; -
						<bean:message bundle="exe" key="exe.cueExe.solicFechaHasta.label"/>: &nbsp;
						<bean:write name="cueExeAdapterVO" property="cueExe.solicFechaHastaView"/>					
					</td>
				</tr>
			</logic:equal>
		</table>
	</fieldset>

<logic:equal name="cueExeAdapterVO" property="esExencionJubilado" value="true">
	<fieldset>
		<legend><bean:message bundle="exe" key="exe.cueExeAdapter.datosExtraJubilado"/></legend>
		<table class="tabladatos">		
			
			<!--  Nro Beneficiario / Caja -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.nroBeneficiario.label"/>: </label></td>
				<td class="normal"><bean:write name="cueExeAdapterVO" property="cueExe.nroBeneficiario" /></td>			
				<td><label><bean:message bundle="exe" key="exe.cueExe.caja.label"/>: </label></td>
				<td class="normal"><bean:write name="cueExeAdapterVO" property="cueExe.caja"/></td>			
			</tr>
			
			<!-- Clase -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.clase.label"/>: </label></td>
				<td class="normal"><bean:write name="cueExeAdapterVO" property="cueExe.claseView"/></td>			
			</tr>
			
			<!-- Fecha Vencimiento Contrato inquilino  -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaVencContInq.label"/>: </label></td>
				<td class="normal">
					<bean:write name="cueExeAdapterVO" property="cueExe.fechaVencContInqView"/>
				</td>
			</tr>
		</table>
	</fieldset>		
</logic:equal>
		
		<logic:notEqual name="cueExeAdapterVO" property="modoVer" value="true">
			<logic:equal name="cueExeAdapterVO" property="modificarEnabled" value="enabled">
				<table width="100%">
					<tr>
						<td colspan="4" align="right"> 
							<input type="button" class="boton" onClick="submitForm('modificarEncabezado', 
								'<bean:write name="cueExeAdapterVO" property="cueExe.id" bundle="base" formatKey="general.format.id"/>');" 
								value="<bean:message bundle="base" key="abm.button.modificar"/>"/>		
						</td>
					</tr>
				</table>
			</logic:equal>
		</logic:notEqual>

		<!-- Historio Estados -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="exe" key="exe.cueExe.listHisEstCueExe.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="cueExeAdapterVO" property="cueExe.listHisEstCueExe">	    	
			    	<tr>
			    		<logic:notEqual name="cueExeAdapterVO" property="modoVer" value="true">
							<th width="1">&nbsp;</th> <!-- Modificar -->
							<th width="1">&nbsp;</th> <!-- Eliminar -->
						</logic:notEqual>
						<th align="left"><bean:message bundle="exe" key="exe.hisEstCueExe.fecha.label"/></th>
						<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
						<th align="left"><bean:message bundle="exe" key="exe.hisEstCueExe.observaciones.label"/></th>
						<th align="left"><bean:message bundle="exe" key="exe.hisEstCueExe.logCambios.label"/></th>						
					</tr>
					<logic:iterate id="HisEstCueExeVO" name="cueExeAdapterVO" property="cueExe.listHisEstCueExe">
			
						<tr>
							<!-- Si no es modo ver -->
							<logic:notEqual name="cueExeAdapterVO" property="modoVer" value="true">
								<td>
									<!-- Cambiar estado solo si es una solicitud y el usuario tiene permiso-->								
									<logic:equal name="HisEstCueExeVO" property="estadoCueExe.tipo" value="S">
										<logic:equal name="HisEstCueExeVO" property="modificarHisEstCueExeEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarHisEstCueExe', '<bean:write name="HisEstCueExeVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="HisEstCueExeVO" property="modificarHisEstCueExeEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="HisEstCueExeVO" property="estadoCueExe.tipo" value="S">
										&nbsp;
									</logic:notEqual>
								</td>
								<!-- Eliminar-->								
								<td>
									<logic:equal name="cueExeAdapterVO" property="eliminarHisEstCueExeEnabled" value="enabled">
										<logic:equal name="HisEstCueExeVO" property="eliminarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarHisEstCueExe', '<bean:write name="HisEstCueExeVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="HisEstCueExeVO" property="eliminarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="cueExeAdapterVO" property="eliminarHisEstCueExeEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</td>
							</logic:notEqual>
							<td><bean:write name="HisEstCueExeVO" property="fechaView"/>&nbsp;</td>
							<td><bean:write name="HisEstCueExeVO" property="estadoCueExe.desEstadoCueExe"/>&nbsp;</td>
							<td><bean:write name="HisEstCueExeVO" property="observaciones"/>&nbsp;</td>
							<td><bean:write name="HisEstCueExeVO" property="logCambios"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="cueExeAdapterVO" property="cueExe.listHisEstCueExe">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- Historio Estados -->

	<!-- Estado -->
	<fieldset>
		<legend><bean:message bundle="exe" key="exe.cueExeAdapter.estado.title"/></legend>
	
		<table width="100%" class="tabladatos">
			<!-- Estado -->
			<tr>
				<td width="50%"><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
				<td width="50%" class="normal">
					<bean:write name="cueExeAdapterVO" property="cueExe.estadoCueExe.desEstadoCueExe"/>
				</td>	
			</tr>
		</table>		
	</fieldset>

	<!-- convivientes -->
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="exe" key="exe.cueExe.listConviviente.label"/></caption>
			    	<tbody>
			    			<logic:notEmpty  name="cueExeAdapterVO" property="cueExe.listConviviente">
						    	<tr>
						    		<th align="left"><bean:message bundle="exe" key="exe.conviviente.nombre"/></th>
									<th align="left"><bean:message bundle="exe" key="exe.conviviente.documento"/></th>
									<th align="left"><bean:message bundle="exe" key="exe.conviviente.parentezco"/></th>
									<th align="left"><bean:message bundle="exe" key="exe.conviviente.edad"/></th>
								</tr>
								<logic:iterate id="ConvivienteVO" name="cueExeAdapterVO" property="cueExe.listConviviente">
									<tr>
										<td><bean:write name="ConvivienteVO" property="convNombre"/>&nbsp;</td>
										<td>
											<bean:write name="ConvivienteVO" property="convTipodoc"/>&nbsp;
											<bean:write name="ConvivienteVO" property="convNrodoc"/>								
										</td>
										<td><bean:write name="ConvivienteVO" property="convParentesco"/>&nbsp;</td>							
										<td><bean:write name="ConvivienteVO" property="convEdadView"/>&nbsp;</td>
									</tr>
								</logic:iterate>
							</logic:notEmpty>
							<logic:empty  name="cueExeAdapterVO" property="cueExe.listConviviente">
								<tr><td align="center">
								<bean:message bundle="base" key="base.noExistenRegitros"/>
								</td></tr>
							</logic:empty>
					</tbody>
				</table>
<!-- Convivientes -->	
	
	<!-- CueExe -->
	
	<table class="tablabotones">
	   	<tr>
  	   		<td align="left">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="left">
				<logic:notEqual name="cueExeAdapterVO" property="modoVer" value="true">
				     <logic:equal name="cueExeAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
					&nbsp;
					<logic:equal name="cueExeAdapterVO" property="cambiarEstadoEnabled" value="enabled">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('cambiarEstado', '');">
							<bean:message bundle="exe" key="exe.cueExeAdapter.button.cambiarEstado"/>
						</html:button>
					</logic:equal>	
					&nbsp;
					<logic:equal name="cueExeAdapterVO" property="agregarSolicitudEnabled" value="enabled">	
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregarSolicitud', '');">
							<bean:message bundle="exe" key="exe.cueExeAdapter.button.agregarSolicitud"/>
						</html:button>
					</logic:equal>	
				</logic:notEqual>
   	    	</td>   	    	
   	    </tr>
   	</table>
   	
   	<input type="hidden" name="name"  value="<bean:write name='cueExeAdapterVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
