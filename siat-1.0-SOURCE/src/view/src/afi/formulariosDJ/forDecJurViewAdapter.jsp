<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/afi/AdministrarForDecJur.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="afi" key="afi.forDecJurViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
	
		<!-- Datos de Cabecera Encriptada -->
		<fieldset>
			<legend><bean:message bundle="afi" key="afi.forDecJur.datosCabecera.label"/></legend>
			<table class="tabladatos">			
				<tr>
					<td><label><bean:message bundle="bal" key="bal.envioOsiris.idEnvioAfip.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.envioOsiris.idEnvioAfipView"/></td>

					<td><label><bean:message bundle="bal" key="bal.tranAfip.idTransaccionAfip.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.tranAfip.idTransaccionAfipView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.codJurCab.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.codJurCabView"/></td>

					<td><label><bean:message bundle="afi" key="afi.forDecJur.nroFormulario.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.nroFormularioView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.impuesto.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.impuestoView"/></td>
				
					<td><label><bean:message bundle="afi" key="afi.forDecJur.concepto.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.conceptoView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.cuit.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.cuit"/></td>
					
					<td><label><bean:message bundle="afi" key="afi.forDecJur.nroInscripcion.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.nroInscripcion"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.periodoFiscal.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.periodoFiscalView"/></td>

					<td><label><bean:message bundle="afi" key="afi.forDecJur.codRectif.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.codRectifView"/></td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.hora.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.hora"/></td>
				
					<td><label><bean:message bundle="afi" key="afi.forDecJur.fechaVencimiento.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.fechaVencimientoView"/></td>
				</tr>	
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.version.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.versionView"/></td>
				
					<td><label><bean:message bundle="afi" key="afi.forDecJur.release.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.releaseView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.versionInterna.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.versionInterna"/></td>
				
					<td><label><bean:message bundle="afi" key="afi.forDecJur.nroVerificador.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.nroVerificadorView"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- Datos de Cabecera Encriptada -->
				
		<!-- Datos Generales de la Empresa -->
		<fieldset>
			<legend><bean:message bundle="afi" key="afi.forDecJur.datosEmpresa.label"/></legend>
			<table class="tabladatos">	
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.tipoOrg.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.tipoOrgView"/></td>
				</tr>		
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.categoria.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.categoriaView"/></td>
				
					<td><label><bean:message bundle="afi" key="afi.forDecJur.nroInsImpIIBB.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.nroInsImpIIBB"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.fechaInsIIBB.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.fechaInsIIBBView"/></td>
				
					<td><label><bean:message bundle="afi" key="afi.forDecJur.fechaBajaIIBB.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.fechaBajaIIBBView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.nroTelefono.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.nroTelefono"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.email.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.email"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.otrLocFueCiu.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.otrLocFueCiuView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.concursado.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.concursadoView"/></td>
				
					<td><label><bean:message bundle="afi" key="afi.forDecJur.fechaPreCon.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.fechaPreConView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.contribFallido.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.contribFallidoView"/></td>
				
					<td><label><bean:message bundle="afi" key="afi.forDecJur.fechaDecQui.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.fechaDecQuiView"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- Datos Generales de la Empresa -->
				
		<!-- Datos de Convenio -->
		<fieldset>
			<legend><bean:message bundle="afi" key="afi.forDecJur.datosConvenio.label"/></legend>
			<table class="tabladatos">	
				<tr>				
					<td><label><bean:message bundle="afi" key="afi.forDecJur.regimenGeneral.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.regimenGeneralView"/></td>

					<td><label><bean:message bundle="afi" key="afi.forDecJur.coeficienteSF.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.coeficienteSFView"/></td>
				</tr>
				<tr>				
					<td><label><bean:message bundle="afi" key="afi.forDecJur.regimenEspecial.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.regimenEspecialView"/></td>
				</tr>
				<logic:equal name="forDecJurAdapterVO" property="paramRegimenEspecial" value="true">
					<tr>
						<td><label><bean:message bundle="afi" key="afi.forDecJur.articulo6.label"/>: </label></td>
						<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.articulo6View"/></td>
					</tr>
					<tr>				
						<td><label><bean:message bundle="afi" key="afi.forDecJur.articulo7.label"/>: </label></td>
						<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.articulo7View"/></td>
					</tr>			
					<tr>
						<td><label><bean:message bundle="afi" key="afi.forDecJur.articulo8.label"/>: </label></td>
						<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.articulo8View"/></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="afi" key="afi.forDecJur.articulo9.label"/>: </label></td>
						<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.articulo9View"/></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="afi" key="afi.forDecJur.articulo10.label"/>: </label></td>
						<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.articulo10View"/></td>
					</tr>
					<tr>				
						<td><label><bean:message bundle="afi" key="afi.forDecJur.articulo11.label"/>: </label></td>
						<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.articulo11View"/></td>
					</tr>			
					<tr>
						<td><label><bean:message bundle="afi" key="afi.forDecJur.articulo12.label"/>: </label></td>
						<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.articulo12View"/></td>
					</tr>
					<tr>				
						<td><label><bean:message bundle="afi" key="afi.forDecJur.articulo13.label"/>: </label></td>
						<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.articulo13View"/></td>
					</tr>
				</logic:equal>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.otrLocFueCiuPorCon.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.otrLocFueCiuPorConView"/></td>
				
					<td><label><bean:message bundle="afi" key="afi.forDecJur.coefIntercomunal.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.coefIntercomunalView"/></td>
				</tr>		
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.otrLocFueProvPorCon.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.otrLocFueProvPorConView"/></td>				
				</tr>
				<tr>					
					<td><label><bean:message bundle="afi" key="afi.forDecJur.fechaInsConMul.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.fechaInsConMulView"/></td>
					
					<td><label><bean:message bundle="afi" key="afi.forDecJur.fechaBajaConMul.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.fechaBajaConMulView"/></td>				
				</tr>
			</table>
		</fieldset>	
		<!-- Datos de Convenio -->
				
		<!-- Liquidacion de DJ Mensual DREI -->
		<fieldset>
			<legend><bean:message bundle="afi" key="afi.forDecJur.datosLiqMensual.label"/></legend>
			<table class="tabladatos">				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.derecho.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.derechoView"/></td>
				
					<td><label><bean:message bundle="afi" key="afi.forDecJur.totRetYPer.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.totRetYPerView"/></td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.retYPerPerAnt.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.retYPerPerAntView"/></td>

					<td><label><bean:message bundle="afi" key="afi.forDecJur.perRetYPerPerAnt.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.perRetYPerPerAnt"/></td>
				</tr>
				<tr>										
					<td><label><bean:message bundle="afi" key="afi.forDecJur.codRecRetPerPerAnt.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.codRecRetPerPerAntView"/></td>				

					<td><label><bean:message bundle="afi" key="afi.forDecJur.montoRetPerPerAnt.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.montoRetPerPerAntView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.aFavorContrib.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.aFavorContribView"/></td>
				
					<td><label><bean:message bundle="afi" key="afi.forDecJur.aFavorDirMun.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.aFavorDirMunView"/></td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.fecVenLiqMen.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.fecVenLiqMenView"/></td>
				
					<td><label><bean:message bundle="afi" key="afi.forDecJur.fecPagPre.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.fecPagPreView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.tasaInteres.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.tasaInteresView"/></td>
				
					<td><label><bean:message bundle="afi" key="afi.forDecJur.recargoInteres.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.recargoInteresView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.forDecJur.derechoAdeuda.label"/>: </label></td>
					<td class="normal"><bean:write name="forDecJurAdapterVO" property="forDecJur.derechoAdeudaView"/></td>				
				</tr>
			</table>
		</fieldset>	
		<!--  Liquidacion de DJ Mensual DREI  -->
		
		<!-- Log de Observaciones -->
		<fieldset>
		<legend><bean:message bundle="afi" key="afi.forDecJur.observaciones.label"/></legend>
			<table class="tabladatos">
				<tr>
					<!-- Observaciones-->		
					<td colspan="3" class="normal"><html:textarea style="height:150px;width:650px" name="forDecJurAdapterVO" property="forDecJur.observaciones" cols="75" rows="15" readonly="true"/></td>					
				</tr>	
			</table>
		</fieldset>
		<!-- Log de Observaciones -->
		
	   	 <!-- Socio -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="afi" key="afi.forDecJur.listSocio.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="forDecJurAdapterVO" property="forDecJur.listSocio">	    	
			    	<tr>						
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
						<th align="left"><bean:message bundle="afi" key="afi.socio.apellidoYNombre.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.socio.documento.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.socio.cuit.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.socio.enCaracterDe.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.socio.soloFirmante.label"/></th>
					</tr>
					<logic:iterate id="SocioVO" name="forDecJurAdapterVO" property="forDecJur.listSocio">			
						<tr>
							<!-- Ver/Seleccionar -->	
							<td>
								<logic:notEqual name="forDecJurAdapterVO" property="modoSeleccionar" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verSocio', '<bean:write name="SocioVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:notEqual>																
							</td>
							<td><bean:write name="SocioVO" property="apellidoYNombreView"/>&nbsp;</td>
							<td><bean:write name="SocioVO" property="documentoView"/>&nbsp;</td>
							<td><bean:write name="SocioVO" property="cuit"/>&nbsp;</td>
							<td><bean:write name="SocioVO" property="enCaracterDeView"/>&nbsp;</td>
							<td><bean:write name="SocioVO" property="soloFirmanteView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="forDecJurAdapterVO" property="forDecJur.listSocio">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		 <!-- Socio -->
		 
	   	 <!-- Local -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="afi" key="afi.forDecJur.listLocal.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="forDecJurAdapterVO" property="forDecJur.listLocal">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->						
						<th align="left"><bean:message bundle="afi" key="afi.local.numeroCuenta.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.local.nombreFantasia.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.local.fecVigAct.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.local.derechoTotal.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.local.paga.label"/></th>
					</tr>
					<logic:iterate id="LocalVO" name="forDecJurAdapterVO" property="forDecJur.listLocal">			
						<tr>					
						<!-- Ver/Seleccionar -->	
						<td>
							<logic:notEqual name="forDecJurAdapterVO" property="modoSeleccionar" value="true">
								<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verLocal', '<bean:write name="LocalVO" property="id" bundle="base" formatKey="general.format.id"/>');">
									<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
								</a>
							</logic:notEqual>																
						</td>	
							<td><bean:write name="LocalVO" property="numeroCuenta"/>&nbsp;</td>
							<td><bean:write name="LocalVO" property="nombreFantasia"/>&nbsp;</td>
							<td><bean:write name="LocalVO" property="fecVigActView"/>&nbsp;</td>
							<td><bean:write name="LocalVO" property="derechoTotalView"/>&nbsp;</td>
							<td><bean:write name="LocalVO" property="pagaView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="forDecJurAdapterVO" property="forDecJur.listLocal">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- Local -->
		
		<!-- RetYPer -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="afi" key="afi.forDecJur.listRetYPer.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="forDecJurAdapterVO" property="forDecJur.listRetYPer">	    	
			    	<tr>					
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->	
						<th align="left"><bean:message bundle="afi" key="afi.retYPer.tipoDeduccion.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.retYPer.cuitAgente.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.retYPer.nroConstancia.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.retYPer.fecha.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.retYPer.importe.label"/></th>
					</tr>
					<logic:iterate id="RetYPerVO" name="forDecJurAdapterVO" property="forDecJur.listRetYPer">			
						<tr>				
							<!-- Ver/Seleccionar -->	
							<td>
								<logic:notEqual name="forDecJurAdapterVO" property="modoSeleccionar" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verRetYPer', '<bean:write name="RetYPerVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:notEqual>																
							</td>	
							<td><bean:write name="RetYPerVO" property="tipoDeduccionView"/>&nbsp;</td>
							<td><bean:write name="RetYPerVO" property="cuitAgente"/>&nbsp;</td>
							<td><bean:write name="RetYPerVO" property="nroConstancia"/>&nbsp;</td>
							<td><bean:write name="RetYPerVO" property="fechaView"/>&nbsp;</td>
							<td><bean:write name="RetYPerVO" property="importeView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="forDecJurAdapterVO" property="forDecJur.listRetYPer">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>				
			</tbody>
		</table>
		<!-- RetYPer -->
		
		<!-- TotDerYAccDJ -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="afi" key="afi.forDecJur.listTotDerYAccDJ.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="forDecJurAdapterVO" property="forDecJur.listTotDerYAccDJ">	    	
			    	<tr>					
						<th width="1">&nbsp;</th> 	<!-- Ver/Seleccionar -->	
						<th align="left"><bean:message bundle="afi" key="afi.totDerYAccDJ.codImpuesto.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.totDerYAccDJ.concepto.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.totDerYAccDJ.totalMontoIngresado.label"/></th>
					</tr>
					<logic:iterate id="TotDerYAccDJVO" name="forDecJurAdapterVO" property="forDecJur.listTotDerYAccDJ">			
						<tr>	
							<!-- Ver/Seleccionar -->	
							<td>
								<logic:notEqual name="forDecJurAdapterVO" property="modoSeleccionar" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verTotDerYAccDJ', '<bean:write name="TotDerYAccDJVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:notEqual>																
							</td>								
							<td><bean:write name="TotDerYAccDJVO" property="codImpuestoView"/>&nbsp;</td> 	
							<td><bean:write name="TotDerYAccDJVO" property="conceptoView"/>&nbsp;</td>
							<td><bean:write name="TotDerYAccDJVO" property="totalMontoIngresadoView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="forDecJurAdapterVO" property="forDecJur.listTotDerYAccDJ">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>				
			</tbody>
		</table>
		<!-- TotDerYAccDJ -->
		
		<!-- DatosDomicilio -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="afi" key="afi.forDecJur.listDatosDomicilio.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="forDecJurAdapterVO" property="forDecJur.listDatosDomicilio">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->				
						<th align="left"><bean:message bundle="afi" key="afi.datosDomicilio.codPropietario.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.datosDomicilio.direccion.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.datosDomicilio.localidad.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.datosDomicilio.asociacion.label"/></th>
					</tr>
					<logic:iterate id="DatosDomicilioVO" name="forDecJurAdapterVO" property="forDecJur.listDatosDomicilio">			
						<tr>				
							<!-- Ver/Seleccionar -->	
							<td>
								<logic:notEqual name="forDecJurAdapterVO" property="modoSeleccionar" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verDatosDomicilio', '<bean:write name="DatosDomicilioVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:notEqual>																
							</td>	
							<td><bean:write name="DatosDomicilioVO" property="codPropietarioView"/>&nbsp;</td>
							<td><bean:write name="DatosDomicilioVO" property="direccionView"/>&nbsp;</td>
							<td><bean:write name="DatosDomicilioVO" property="localidadView"/>&nbsp;</td>
							<td><bean:write name="DatosDomicilioVO" property="asociacionView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="forDecJurAdapterVO" property="forDecJur.listDatosDomicilio">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>				
			</tbody>
		</table>
		<!-- DatosDomicilio -->
		
		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	   <logic:equal name="forDecJurAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>					
	   	    	</td>
	   	    	<td align="right" width="100%">
	   	    	   <logic:equal name="forDecJurAdapterVO" property="act" value="generarDecJur">
		   	    	    <html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('generarDecJur', '');">
						    <bean:message bundle="afi" key="afi.forDecJurAdapter.button.generarDecJur"/>
					    </html:button>
					</logic:equal>					
	   	    	</td>
	   	    </tr>
	   	 </table>
				
	    <input type="hidden" name="name"  value="<bean:write name='forDecJurAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->