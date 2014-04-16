<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarRecurso.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.recursoAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>

		<logic:equal name="recursoAdapterVO" property="act" value="ver">		
		<!-- Recurso -->
		
		<!-- Caracteristicas Principales -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recursoAdapter.fieldset1"/></legend>
			
			<table>
				<tr>
					<!-- Categoria -->										
					<td align="right"><label><bean:message bundle="def" key="def.categoria.label"/>:</label></td>
					<td colspan="2" class="normal"><bean:write name="recursoAdapterVO" property="recurso.categoria.desCategoria"/></td>
				</tr>
				<tr>
					<!-- Codigo -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.codRecurso.label"/>:</label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.codRecurso"/></td>
				</tr>					
				<tr>
					<!-- Descripcion -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.desRecurso.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.desRecurso"/></td>					
				</tr>
				<tr>
					<!-- EsAutoliquidable -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.esAutoliquidable.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.esAutoliquidable.value"/></td>			
					<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.esAutoliquidable.description"/></li></ul></td>				
				</tr>
				<tr>
					<!-- FecEsAut -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.fecEsAut.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.fecEsAutView"/></td>
					<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.fecEsAut.description"/></li></ul></td>				
				</tr>
					<tr>
					<!-- EsFiscalizable -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.esFiscalizable.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.esFiscalizable.value"/></td>					
					<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.esFiscalizable.description"/></li></ul></td>				
				</tr>
				<tr>
					<!-- FecEsFis -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.fecEsFis.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.fecEsFisView"/></td>
					<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.fecEsFis.description"/></li></ul></td>				
				</tr>
				<tr>
					<!-- FecDesIntDiaBan -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.fecDesIntDiaBan.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.fecDesIntDiaBanView"/></td>
				</tr>
				<tr>
					<!-- FechaAlta -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.fechaAlta.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.fechaAltaView"/></td>
				</tr>
			</table>
		</fieldset>
		<!-- Fin Caracteristicas Principales -->
	
		<!-- Caracteristicas respecto de la Deuda/Cobranza -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recursoAdapter.fieldset2"/></legend>
			
			<table>
				<tr>
					<!-- PeriodoDeuda -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.periodoDeuda.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.periodoDeuda.desPeriodoDeuda"/></td>					
				</tr>
				<tr>
					<!-- GesDeuNoVen -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.gesDeuNoVen.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.gesDeuNoVen.value"/></td>					
					<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.gesDeuNoVen.description"/></li></ul></td>				
				</tr>
				<tr>
					<!-- DeuExiVen -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.deuExiVen.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.deuExiVen.value"/></td>					
					</tr>
				<tr>
					<!-- GesCobranza -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.gesCobranza.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.gesCobranza.value"/></td>					
					<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.gesCobranza.description"/></li></ul></td>				
				</tr>
			</table>
		</fieldset>
		<!-- Fin Caracteristicas respecto de la Deuda/Cobranza -->

		<!-- Caracteristicas de la Emisión -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recursoAdapter.fieldset3"/></legend>			
			<table>
				<tr>
					<!-- PerEmiDeuMas -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.perEmiDeuMas.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.perEmiDeuMas.value"/></td>
					<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.perEmiDeuMas.description"/></li></ul></td>					
				</tr>
				<logic:equal name="recursoAdapterVO" property="paramPerEmiDeuMas" value="1">
					<tr>
						<!-- AtrEmiMas -->										
						<td align="right"><label><bean:message bundle="def" key="def.recurso.atrEmiMas.label"/>:</label></td>
						<td colspan="3" class="normal"><bean:write name="recursoAdapterVO" property="recurso.atrEmiMas.desAtributo"/></td>										
					</tr>
				</logic:equal>
				<tr>
					<!-- PerEmiDeuPuntual -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.perEmiDeuPuntual.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.perEmiDeuPuntual.value"/></td>					
					<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.perEmiDeuPuntual.description"/></li></ul></td>
				</tr>
				<tr>
					<!-- PerEmiDeuExt -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.perEmiDeuExt.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.perEmiDeuExt.value"/></td>					
					<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.perEmiDeuExt.description"/></li></ul></td>				
				</tr>
				<tr>
					<!-- EsDeudaTitular -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.esDeudaTitular.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.esDeudaTitular.value"/></td>					
					<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.esDeudaTitular.description"/></li></ul></td>				
				</tr>
				<tr>
					<!-- Vencimiento -->										
					<td align="right"><label><bean:message bundle="def" key="def.recurso.vencimiento.label"/>:</label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.vencimiento.desVencimiento"/></td>										
					<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.vencimiento.description"/></li></ul></td>
				</tr>
				<tr>
					<!-- AtributoAse -->										
					<td align="right"><label><bean:message bundle="def" key="def.recurso.atributoAse.label"/>:</label></td>
					<td colspan="3" class="normal"><bean:write name="recursoAdapterVO" property="recurso.atributoAse.desAtributo"/></td>										
				</tr>
				<tr>
					<!-- PerImpMasDeu -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.perImpMasDeu.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.perImpMasDeu.value"/></td>
				</tr>
				<logic:equal name="recursoAdapterVO" property="paramPerImpMasDeu" value="1">
					<tr>
						<!-- FormImpMasDeu -->
						<td align="right"><label><bean:message bundle="def" key="def.recurso.formImpMasDeu.label"/>: </label></td>
						<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.formImpMasDeu.desFormulario"/></td>
					</tr>
					
					<tr>
						<!-- GenNotif -->
						<td align="right"><label><bean:message bundle="def" key="def.recurso.genNotif.label"/>: </label></td>
						<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.genNotif.value"/></td>
					</tr>

					<logic:equal name="recursoAdapterVO" property="paramGenNotImpMas" value="1">
						<tr>
							<!-- FormNotif -->
							<td align="right"><label><bean:message bundle="def" key="def.recurso.formNotif.label"/>: </label></td>
							<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.formNotif.desFormulario"/></td>
						</tr>
					</logic:equal>
						<tr>
							<!-- GenPadFir -->
							<td align="right"><label><bean:message bundle="def" key="def.recurso.genPadFir.label"/>: </label></td>
							<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.genPadFir.value"/></td>
							<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.genPadFir.description"/></li></ul></td>
						</tr>
					<logic:equal name="recursoAdapterVO" property="paramGenPadFir" value="1">
						<tr>
							<!-- FormPadFir -->
							<td align="right"><label><bean:message bundle="def" key="def.recurso.formPadFir.label"/>: </label></td>
							<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.formPadFir.desFormulario"/></td>
						</tr>
					</logic:equal>
				</logic:equal>
			</table>
		</fieldset>
		<!-- Fin Caracteristicas de la Emisión -->

		<!-- Relacion con Objetos Imponibles -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recursoAdapter.fieldset4"/></legend>
			
			<table>
				<tr>
					<!-- TipObjImp -->										
					<td align="right"><label><bean:message bundle="def" key="def.tipObjImp.label"/>:</label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.tipObjImp.desTipObjImp"/></td>						
				</tr>
				<tr>
					<!-- EsLibreDeuda -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.esLibreDeuda.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.esLibreDeuda.value"/></td>					
				</tr>
				<tr>
					<!-- EsPrincipal -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.esPrincipal.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.esPrincipal.value"/></td>					
				</tr>
				<tr>
					<!-- AltaCtaPorIface -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.altaCtaPorIface.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.altaCtaPorIface.value"/></td>					
				</tr>
				<tr>
					<!-- BajaCtaPorIface -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.bajaCtaPorIface.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.bajaCtaPorIface.value"/></td>									
				</tr>
				<tr>
					<!-- ModiTitCtaPorIface -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.modiTitCtaPorIface.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.modiTitCtaPorIface.value"/></td>					
				</tr>
				<tr>
					<!-- RecPri -->										
					<td align="right"><label><bean:message bundle="def" key="def.recurso.recPri.label"/>:</label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.recPri.desRecurso"/></td>					
				</tr>				
				<tr>
					<!-- AltaCtaPorPri -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.altaCtaPorPri.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.altaCtaPorPri.value"/></td>					
				</tr>
				<tr>
					<!-- BajaCtaPorPri -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.bajaCtaPorPri.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.bajaCtaPorPri.value"/></td>					
				</tr>
				<tr>
					<!-- ModiTitCtaPorPri -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.modiTitCtaPorPri.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.modiTitCtaPorPri.value"/></td>					
				</tr>
			</table>
		</fieldset>
		<!-- Fin Relacion con Objetos Imponibles -->

		<!-- Acciones respecto a la creacion de la Cuenta -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recursoAdapter.fieldset5"/></legend>			
			<table>
				<tr>
					<!-- GenCue -->										
					<td align="right"><label><bean:message bundle="def" key="def.recurso.genCue.label"/>:</label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.genCue.desGenCue"/></td>					
				</tr>
				<tr>
					<!-- GenCodGes -->										
					<td align="right"><label><bean:message bundle="def" key="def.recurso.genCodGes.label"/>:</label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.genCodGes.desGenCodGes"/></td>					
				</tr>
			</table>
		</fieldset>
		<!-- Fin Acciones respecto a la creacion de la Cuenta -->

		<!-- Acciones respecto de la actualizacion manual de las cuentas del recurso -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recursoAdapter.fieldset6"/></legend>			
			<table>
				<tr>
					<!-- AltaCtaManual -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.altaCtaManual.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.altaCtaManual.value"/></td>					
				</tr>
				<tr>
					<!-- BajaCtaManual -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.bajaCtaManual.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.bajaCtaManual.value"/></td>					
				</tr>
				<tr>
					<!-- ModiTitCtaManual -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.modiTitCtaManual.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.modiTitCtaManual.value"/></td>					
				</tr>
			</table>
		</fieldset>
		<!-- Fin Acciones respecto de la actualizacion manual de las cuentas del recurso -->

		<!-- Caracteristicas de la gestion Judicial -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recursoAdapter.fieldset7"/></legend>			
			<table>
				<tr>
					<!-- EnviaJudicial -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.enviaJudicial.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.enviaJudicial.value"/></td>					
				</tr>
				<logic:equal name="recursoAdapterVO" property="paramEnviaJudicial" value="1">							
					<tr>
						<!-- CriAsiPro -->										
						<td align="right"><label><bean:message bundle="def" key="def.recurso.criAsiPro.label"/>:</label></td>
						<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.criAsiPro.desCriAsiPro"/></td>					
					</tr>
				</logic:equal>
			</table>
		</fieldset>
		<!-- Fin Caracteristicas de la gestion Judicial -->
	
		<!-- Fin Recurso -->
		
		<!-- RecAtrVal -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="def" key="def.recurso.listRecAtrVal.ref"/></caption>
	    	<tbody>
				<logic:notEmpty  name="recursoAdapterVO" property="recurso.listRecAtrVal">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th align="left"><bean:message bundle="def" key="def.recAtrVal.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recAtrVal.fechaHasta.label"/></th>						
						<th align="left"><bean:message bundle="def" key="def.recurso.desRecurso.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recAtrVal.valor.label"/></th>
					</tr>
					<logic:iterate id="RecAtrValVO" name="recursoAdapterVO" property="recurso.listRecAtrVal">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="recursoAdapterVO" property="verRecAtrValEnabled" value="enabled">							
									<logic:equal name="RecAtrValVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verRecAtrVal', '<bean:write name="RecAtrValVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="RecAtrValVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="recursoAdapterVO" property="verRecAtrValEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="RecAtrValVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="RecAtrValVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="RecAtrValVO" property="atributo.desAtributo" />&nbsp;</td>
							<td><bean:write name="RecAtrValVO" property="valor"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="recursoAdapterVO" property="recurso.listRecAtrVal">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			</table>
		<!-- RecAtrVal -->	

		<!-- RecMinDec -->
		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="def" key="def.recMinDec.title"/></caption>
	    	<tbody>
	    	<logic:equal name="recursoAdapterVO" property="recurso.esAutoliquidable.id" value="1">
				<logic:notEmpty  name="recursoAdapterVO" property="recurso.listRecMinDec">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th align="left"><bean:message bundle="def" key="def.recMinDec.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recMinDec.fechaHasta.label"/></th>						
						<th align="left"><bean:message bundle="def" key="def.recMinDec.valRefDes.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recMinDec.valRefHas.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recMinDec.minimo.label"/></th>
					</tr>
					<logic:iterate id="RecMinDecVO" name="recursoAdapterVO" property="recurso.listRecMinDec">
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="recursoAdapterVO" property="verRecMinDecEnabled" value="enabled">							
									<logic:equal name="RecMinDecVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verRecMinDec', '<bean:write name="RecMinDecVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="RecMinDecVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="recursoAdapterVO" property="verRecMinDecEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
								</logic:notEqual>
							</td>
						
							<td><bean:write name="RecMinDecVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="RecMinDecVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="RecMinDecVO" property="valRefDesView" />&nbsp;</td>
							<td><bean:write name="RecMinDecVO" property="valRefHasView" />&nbsp;</td>
							<td><bean:write name="RecMinDecVO" property="minimoView" />&nbsp;</td>
						
						</tr>
					</logic:iterate>
				
				</logic:notEmpty>
			
				<logic:empty  name="recursoAdapterVO" property="recurso.listRecMinDec">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</logic:equal>
			</tbody>
		</table>
			
		<!-- RecMinDec -->
		
		<!-- RecAli -->
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="def" key="def.recAli.title"/></caption>
			<tbody>
			<logic:equal name="recursoAdapterVO" property="recurso.esAutoliquidable.id" value="1">
				<logic:notEmpty  name="recursoAdapterVO" property="recurso.listRecAli">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th align="left"><bean:message bundle="def" key="def.recAli.alicuota.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recAli.recTipAli.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recAli.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recAli.fechaHasta.label"/></th>						
						
					</tr>
					<logic:iterate id="RecAliVO" name="recursoAdapterVO" property="recurso.listRecAli">
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="recursoAdapterVO" property="verRecAliEnabled" value="enabled">							
									<logic:equal name="RecAliVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verRecAli', '<bean:write name="RecAliVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="RecAliVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="recursoAdapterVO" property="verRecAliEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
								</logic:notEqual>
							</td>
						
							<td><bean:write name="RecAliVO" property="alicuotaView" filter="false"/>&nbsp;</td>
							<td><bean:write name="RecAliVO" property="recTipAli.desTipoAlicuota" />&nbsp;</td>
							<td><bean:write name="RecAliVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="RecAliVO" property="fechaHastaView"/>&nbsp;</td>
							
						
						</tr>
					</logic:iterate>
				
				</logic:notEmpty>
			
				<logic:empty  name="recursoAdapterVO" property="recurso.listRecAli">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
											
			</logic:equal>
			</tbody>
			</table>
		<!-- RecAli -->

		<!-- RecConADec -->
		<logic:equal name="recursoAdapterVO" property="recurso.esAutoliquidable.id" value="1">
			
			 <div class="scrolable">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="700px">
			<caption><bean:message bundle="def" key="def.recurso.listRecConADec"/></caption>
			<tbody>
				<logic:notEmpty name="recursoAdapterVO" property="recurso.listRecConADec">
					<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th align="left"><bean:message bundle="def" key="def.recConADec.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recConADec.fechaHasta.label"/></th>						
						<th align="left"><bean:message bundle="def" key="def.recConADec.codConcepto.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recConADec.desConcepto.label"/></th>
					</tr>
					<logic:iterate name="recursoAdapterVO" property="recurso.listRecConADec" id="RecConADecVO">
						<tr>
							<td>
								<!-- Ver -->
								<logic:equal name="recursoAdapterVO" property="verRecConADecEnabled" value="enabled">							
									<logic:equal name="RecConADecVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verRecConADec', '<bean:write name="RecConADecVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="RecConADecVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="recursoAdapterVO" property="verRecConADecEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="RecConADecVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="RecConADecVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="RecConADecVO" property="codConcepto"/>&nbsp;</td>
							<td><bean:write name="RecConADecVO" property="desConcepto"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="recursoAdapterVO" property="recurso.listRecConADec">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>	
				<td colspan="20" align="right">
  				<bean:define id="agregarRecursoEnabled" name="recursoAdapterVO" property="agregarRecConADecEnabled"/>
				</td>
			</tbody>
			</table>
			
			</div>
		</logic:equal>
	<!-- RecConADec-->
	&nbsp;
	
		<!-- RecCon -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="def" key="def.recurso.listRecCon.ref"/></caption>
	    	<tbody>
				<logic:notEmpty  name="recursoAdapterVO" property="recurso.listRecCon">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th align="left"><bean:message bundle="def" key="def.recCon.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recCon.fechaHasta.label"/></th>						
						<th align="left"><bean:message bundle="def" key="def.recCon.codRecCon.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recCon.desRecCon.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recCon.abrRecCon.label"/></th>
					</tr>
					<logic:iterate id="RecConVO" name="recursoAdapterVO" property="recurso.listRecCon">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="recursoAdapterVO" property="verRecConEnabled" value="enabled">							
									<logic:equal name="RecConVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verRecCon', '<bean:write name="RecConVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="RecConVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="recursoAdapterVO" property="verRecConEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="RecConVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="RecConVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="RecConVO" property="codRecCon" />&nbsp;</td>
							<td><bean:write name="RecConVO" property="desRecCon" />&nbsp;</td>
							<td><bean:write name="RecConVO" property="abrRecCon" />&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="recursoAdapterVO" property="recurso.listRecCon">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			</table>
		<!-- RecCon -->	
	
		<!-- RecClaDeu -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="def" key="def.recurso.listRecClaDeu.ref"/></caption>
	    	<tbody>
				<logic:notEmpty  name="recursoAdapterVO" property="recurso.listRecClaDeu">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th align="left"><bean:message bundle="def" key="def.recClaDeu.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recClaDeu.fechaHasta.label"/></th>						
						<th align="left"><bean:message bundle="def" key="def.recClaDeu.desClaDeu.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recClaDeu.esOriginal.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recClaDeu.actualizaDeuda.label"/></th>
					</tr>
					<logic:iterate id="RecClaDeuVO" name="recursoAdapterVO" property="recurso.listRecClaDeu">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="recursoAdapterVO" property="verRecClaDeuEnabled" value="enabled">							
									<logic:equal name="RecClaDeuVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verRecClaDeu', '<bean:write name="RecClaDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="RecClaDeuVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="recursoAdapterVO" property="verRecClaDeuEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="RecClaDeuVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="RecClaDeuVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="RecClaDeuVO" property="desClaDeu"/>&nbsp;</td>
							<td><bean:write name="RecClaDeuVO" property="esOriginal.value"/>&nbsp;</td>
							<td><bean:write name="RecClaDeuVO" property="actualizaDeuda.value"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="recursoAdapterVO" property="recurso.listRecClaDeu">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			</table>
		<!-- RecClaDeu -->	

		<!-- RecAtrCue -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="def" key="def.recurso.listRecAtrCue.ref"/></caption>
	    	<tbody>
				<logic:notEmpty  name="recursoAdapterVO" property="recurso.listRecAtrCue">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th align="left"><bean:message bundle="def" key="def.recAtrVal.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recAtrVal.fechaHasta.label"/></th>						
						<th align="left"><bean:message bundle="def" key="def.recAtrVal.atributo.label"/></th>
					</tr>
					<logic:iterate id="RecAtrCueVO" name="recursoAdapterVO" property="recurso.listRecAtrCue">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="recursoAdapterVO" property="verRecAtrCueEnabled" value="enabled">							
									<logic:equal name="RecAtrCueVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verRecAtrCue', '<bean:write name="RecAtrCueVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="RecAtrCueVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="recursoAdapterVO" property="verRecAtrCueEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="RecAtrCueVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="RecAtrCueVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="RecAtrCueVO" property="atributo.desAtributo"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="recursoAdapterVO" property="recurso.listRecAtrCue">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			</table>
		<!-- RecAtrCue -->	
	
		<!-- RecGenCueAtrVa -->		
		<logic:notEqual name="recursoAdapterVO" property="paramEsPrincipal" value="1">
		<logic:notEqual name="recursoAdapterVO" property="paramTipObjImp" value="0">
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="def" key="def.recurso.listRecGenCueAtrVa.ref"/></caption>
	    	<tbody>
				<logic:notEmpty  name="recursoAdapterVO" property="recursoDefinitionForRecGenCueAtrVa.listGenericAtrDefinition">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th align="left"><bean:message bundle="def" key="def.atributo.label"/></th>
						<th align="left">Valor</th>
						<th align="left">Vigencia</th>
					</tr>
					<logic:iterate id="GenericAtrDefinition" name="recursoAdapterVO" property="recursoDefinitionForRecGenCueAtrVa.listGenericAtrDefinition" indexId="count">
							
							<bean:define id="AtrVal" name="GenericAtrDefinition"/>
							
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="recursoAdapterVO" property="verRecGenCueAtrVaEnabled" value="enabled">							
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verRecGenCueAtrVa', '<bean:write name="AtrVal" property="idDefinition" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:equal>
								<logic:notEqual name="recursoAdapterVO" property="verRecGenCueAtrVaEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
								</logic:notEqual>
							</td>
							<%@ include file="/def/atrDefinitionView4Edit.jsp" %>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="recursoAdapterVO" property="recurso.listRecGenCueAtrVa">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
		</table>
		</logic:notEqual>
		</logic:notEqual>
		
		<!-- RecGenCueAtrVa -->

		<!-- RecEmi -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%" style="display:none;">            
			<caption><bean:message bundle="def" key="def.recurso.listRecEmi.ref"/></caption>
	    	<tbody>
				<logic:notEmpty  name="recursoAdapterVO" property="recurso.listRecEmi">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th align="left"><bean:message bundle="def" key="def.recEmi.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recEmi.fechaHasta.label"/></th>						
						<th align="left"><bean:message bundle="def" key="def.recEmi.tipoEmision.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recEmi.periodoDeuda.label"/></th>
					</tr>
					<logic:iterate id="RecEmiVO" name="recursoAdapterVO" property="recurso.listRecEmi">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="recursoAdapterVO" property="verRecEmiEnabled" value="enabled">							
									<logic:equal name="RecEmiVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verRecEmi', '<bean:write name="RecEmiVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="RecEmiVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="recursoAdapterVO" property="verRecEmiEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="RecEmiVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="RecEmiVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="RecEmiVO" property="tipoEmision.desTipoEmision" />&nbsp;</td>
							<td><bean:write name="RecEmiVO" property="periodoDeuda.desPeriodoDeuda"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="recursoAdapterVO" property="recurso.listRecEmi">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
		</table>
		<!-- RecEmi -->

		<!-- RecAtrCueEmi -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="def" key="def.recurso.listRecAtrCueEmi.ref"/></caption>
	    	<tbody>
				<logic:notEmpty  name="recursoAdapterVO" property="recurso.listRecAtrCueEmi">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th align="left"><bean:message bundle="def" key="def.recAtrCueEmi.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recAtrCueEmi.fechaHasta.label"/></th>						
						<th align="left"><bean:message bundle="def" key="def.recAtrCueEmi.atributo.label"/></th>
					</tr>
					<logic:iterate id="RecAtrCueEmiVO" name="recursoAdapterVO" property="recurso.listRecAtrCueEmi">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="recursoAdapterVO" property="verRecAtrCueEmiEnabled" value="enabled">							
									<logic:equal name="RecAtrCueEmiVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verRecAtrCueEmi', '<bean:write name="RecAtrCueEmiVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="RecAtrCueEmiVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="recursoAdapterVO" property="verRecAtrCueEmiEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="RecAtrCueEmiVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="RecAtrCueEmiVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="RecAtrCueEmiVO" property="atributo.desAtributo"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="recursoAdapterVO" property="recurso.listRecAtrCueEmi">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
		</table>
		<!-- RecAtrCueEmi -->	
		
		</logic:equal>
		
		<logic:notEqual name="recursoAdapterVO" property="act" value="ver">	
		<!-- Datos del Recurso para eliminar/activar/desactivar -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recurso.title"/></legend>			
			<table>
				<tr>
					<!-- Tipo de Categoria -->										
					<td align="right"><label><bean:message bundle="def" key="def.categoria.tipo.label"/>:</label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.categoria.tipo.desTipo"/></td>
					<td></td>
					<!-- Categoria -->										
					<td align="right"><label><bean:message bundle="def" key="def.categoria.label"/>:</label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.categoria.desCategoria"/></td>
				</tr>
				<tr>
					<!-- Codigo -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.codRecurso.label"/>:</label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.codRecurso"/></td>
					<td></td>
					<!-- Descripcion -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.desRecurso.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.desRecurso"/></td>					
				</tr>
				<logic:equal name="recursoAdapterVO" property="act" value="desactivar">	
				<tr>
					<!-- FechaBaja -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.fechaBaja.label"/>: </label></td>
					<td class="normal">
					<html:text name="recursoAdapterVO" property="recurso.fechaBajaView" styleId="fechaBajaView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaBajaView');" id="a_fechaBajaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>
				</logic:equal>
			</table>
		</fieldset>
		<!-- Fin Datos del Recurso para eliminar/activar/desactivar -->
		</logic:notEqual>
			
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="recursoAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="recursoAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="recursoAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
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