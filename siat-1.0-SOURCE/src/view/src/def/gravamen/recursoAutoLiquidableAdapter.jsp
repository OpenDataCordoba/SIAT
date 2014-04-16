<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
  	    <%@include file="/base/calendar.js"%>   
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarRecursoAutoLiquidable.do">

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
					<td colspan="3"><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.esAutoliquidable.description"/></li></ul></td>				
				</tr>
				<tr>
					<!-- FecEsAut -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.fecEsAut.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.fecEsAutView"/></td>
					<td colspan="3"><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.fecEsAut.description"/></li></ul></td>				
				</tr>
					<tr>
					<!-- EsFiscalizable -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.esFiscalizable.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.esFiscalizable.value"/></td>					
					<td colspan="3"><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.esFiscalizable.description"/></li></ul></td>				
				</tr>
				<tr>
					<!-- FecEsFis -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.fecEsFis.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.fecEsFisView"/></td>
					<td colspan="3"><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.fecEsFis.description"/></li></ul></td>				
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
					<!-- PerEmiDeu -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.perEmiDeuMas.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.perEmiDeuMas.value"/></td>					
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
		<!-- Atributo de Asentamiento -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recursoAdapter.fieldset8"/></legend>			
			<table>
				<tr>
					<!-- AtributoAse -->										
					<td align="right"><label><bean:message bundle="def" key="def.recurso.atributoAse.label"/>:</label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.atributoAse.desAtributo"/></td>										
				</tr>
			</table>
		</fieldset>
		<!-- Fin Atributo de Asentamiento -->
		
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="right">
					<bean:define id="modificarEncabezadoEnabled" name="recursoAdapterVO" property="modificarEncabezadoEnabled"/>
					<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
						'<bean:write name="recursoAdapterVO" property="recurso.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="base" key="abm.button.modificar"/>"/>
				</td>
			</tr>
		</table>

		<!-- Fin Recurso -->
		
		<!-- RecMinDec -->
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="def" key="def.recMinDec.title"/></caption>
	    	<tbody>
	    	<logic:equal name="recursoAdapterVO" property="recurso.esAutoliquidable.id" value="1">
				<logic:notEmpty  name="recursoAdapterVO" property="recurso.listRecMinDec">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
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
							<td>
								<!-- Modificar-->
								<logic:equal name="recursoAdapterVO" property="modificarRecMinDecEnabled" value="enabled">
									<logic:equal name="RecMinDecVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarRecMinDec', '<bean:write name="RecMinDecVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="RecMinDecVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="recursoAdapterVO" property="modificarRecMinDecEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Eliminar-->
								<logic:equal name="recursoAdapterVO" property="eliminarRecMinDecEnabled" value="enabled">
									<logic:equal name="RecMinDecVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarRecMinDec', '<bean:write name="RecMinDecVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="RecMinDecVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="recursoAdapterVO" property="eliminarRecMinDecEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
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
				
				<td colspan="20" align="right">
	  				<bean:define id="agregarRecursoEnabled" name="recursoAdapterVO" property="agregarRecMinDecEnabled"/>
					<input type="button" <%=agregarRecursoEnabled%> class="boton" 
						onClick="submitForm('agregarRecMinDec', '<bean:write name="recursoAdapterVO" property="recurso.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
				</td>							
			</logic:equal>
			</tbody>
		</table>
			
		<!-- RecMinDec -->

		<!-- RecAli -->
		<% try { %>
	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
		<caption><bean:message bundle="def" key="def.recAli.title"/></caption>
		<tbody>
			<logic:equal name="recursoAdapterVO" property="recurso.esAutoliquidable.id" value="1">
				<logic:notEmpty  name="recursoAdapterVO" property="recurso.listRecAli">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
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
							<td>
								<!-- Modificar-->
								<logic:equal name="recursoAdapterVO" property="modificarRecAliEnabled" value="enabled">
									<logic:equal name="RecAliVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarRecAli', '<bean:write name="RecAliVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="RecAliVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="recursoAdapterVO" property="modificarRecAliEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Eliminar-->
								<logic:equal name="recursoAdapterVO" property="eliminarRecAliEnabled" value="enabled">
									<logic:equal name="RecAliVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarRecAli', '<bean:write name="RecAliVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="RecAliVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="recursoAdapterVO" property="eliminarRecAliEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
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
				
				<td colspan="20" align="right">
					<bean:define id="agregarRecursoEnabled" name="recursoAdapterVO" property="agregarRecAliEnabled"/>
					<input type="button" <%=agregarRecursoEnabled%> class="boton" 
						onClick="submitForm('agregarRecAli', '<bean:write name="recursoAdapterVO" property="recurso.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
				</td>							
			</logic:equal>
		</tbody>
	</table>
	<% } catch (Exception e ) { out.print("Ocurrio una excepcion" + e.toString()); }%>
		<!-- RecAli -->
		
		
		<!-- RecConADec -->
		<% try { %>
		<logic:equal name="recursoAdapterVO" property="recurso.esAutoliquidable.id" value="1">
			
			 <div class="scrolable">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="700px">
			<caption><bean:message bundle="def" key="def.recurso.listRecConADec"/></caption>
			<tbody>
				<logic:notEmpty name="recursoAdapterVO" property="recurso.listRecConADec">
					<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
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
							<td>
								<!-- Modificar-->
								<logic:equal name="recursoAdapterVO" property="modificarRecConADecEnabled" value="enabled">
									<logic:equal name="RecConADecVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarRecConADec', '<bean:write name="RecConADecVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="RecConADecVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="recursoAdapterVO" property="modificarRecConADecEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Eliminar-->
								<logic:equal name="recursoAdapterVO" property="eliminarRecConADecEnabled" value="enabled">
									<logic:equal name="RecConADecVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarRecConADec', '<bean:write name="RecConADecVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="RecConADecVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="recursoAdapterVO" property="eliminarRecClaDeuEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
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
			<p align="right">
				<input type="button" <%=agregarRecursoEnabled%> class="boton" 
					onClick="submitForm('agregarRecConADec', '');" 
					value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
			</p>
			</div>
			

		</logic:equal>
		<% } catch (Exception e ) { out.print("Ocurrio una excepcion" + e.toString()); }%>
		<!-- ReConADec -->
		<p> &nbsp;</p>
	
		
		<table class="tablabotones">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
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