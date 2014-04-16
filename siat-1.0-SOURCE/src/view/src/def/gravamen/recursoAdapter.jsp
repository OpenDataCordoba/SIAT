<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
	<%@include file="/base/submitForm.js"%>	 
  	<%@include file="/base/calendar.js"%>   
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
	
	<!-- RecAtrVal -->		
	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
		<caption><bean:message bundle="def" key="def.recurso.listRecAtrVal.ref"/></caption>
    	<tbody>
			<logic:notEmpty  name="recursoAdapterVO" property="recursoDefinitionForRecAtrVal.listGenericAtrDefinition">	    	
		    	<tr>
					<th width="1">&nbsp;</th> <!-- Ver -->
					<th width="1">&nbsp;</th> <!-- Modificar -->
					<th width="1">&nbsp;</th> <!-- Eliminar -->
					<th align="left"><bean:message bundle="def" key="def.atributo.label"/></th>
					<th align="left">Valor</th>
					<th align="left">Vigencia</th>
				</tr>
				<logic:iterate id="GenericAtrDefinition" name="recursoAdapterVO" property="recursoDefinitionForRecAtrVal.listGenericAtrDefinition" indexId="count">
						
						<bean:define id="AtrVal" name="GenericAtrDefinition"/>
						
						<tr>
						<!-- Ver -->
						<td>
							<logic:equal name="recursoAdapterVO" property="verRecAtrValEnabled" value="enabled">							
								<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verRecAtrVal', '<bean:write name="AtrVal" property="idDefinition" bundle="base" formatKey="general.format.id"/>');">
									<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
								</a>
							</logic:equal>
							<logic:notEqual name="recursoAdapterVO" property="verRecAtrValEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
							</logic:notEqual>
						</td>
						<td>
							<!-- Modificar-->
							<logic:equal name="recursoAdapterVO" property="modificarRecAtrValEnabled" value="enabled">
								<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarRecAtrVal', '<bean:write name="AtrVal" property="idDefinition" bundle="base" formatKey="general.format.id"/>');">
									<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
								</a>
							</logic:equal>
							<logic:notEqual name="recursoAdapterVO" property="modificarRecAtrValEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
							</logic:notEqual>
						</td>
						<td>
							<!-- Eliminar-->
							<logic:equal name="recursoAdapterVO" property="eliminarRecAtrValEnabled" value="enabled">
								<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarRecAtrVal', '<bean:write name="AtrVal" property="idDefinition" bundle="base" formatKey="general.format.id"/>');">
									<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
								</a>
							</logic:equal>
							<logic:notEqual name="recursoAdapterVO" property="eliminarRecAtrValEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
							</logic:notEqual>
						</td>
						<%@ include file="/def/atrDefinitionView4Edit.jsp" %>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			<logic:empty  name="recursoAdapterVO" property="recurso.listRecAtrVal">
				<tr><td align="center">
				<bean:message bundle="base" key="base.noExistenRegitros"/>
				</td></tr>
			</logic:empty>					
		<td colspan="20" align="right">
 				<bean:define id="agregarRecAtrValEnabled" name="recursoAdapterVO" property="agregarRecAtrValEnabled"/>
			<input type="button" <%=agregarRecAtrValEnabled%> class="boton" 
				onClick="submitForm('agregarRecAtrVal', '<bean:write name="recursoAdapterVO" property="recurso.id" bundle="base" formatKey="general.format.id"/>');" 
				value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
		</td>
		</tbody>
		</table>
	<!-- RecAtrVal -->	
	
	<!-- RecCon -->		
	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
		<caption><bean:message bundle="def" key="def.recurso.listRecCon.ref"/></caption>
    	<tbody>
			<logic:notEmpty  name="recursoAdapterVO" property="recurso.listRecCon">	    	
		    	<tr>
					<th width="1">&nbsp;</th> <!-- Ver -->
					<th width="1">&nbsp;</th> <!-- Modificar -->
					<th width="1">&nbsp;</th> <!-- Eliminar -->
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
						<td>
							<!-- Modificar-->
							<logic:equal name="recursoAdapterVO" property="modificarRecConEnabled" value="enabled">
								<logic:equal name="RecConVO" property="modificarEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarRecCon', '<bean:write name="RecConVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
									</a>
								</logic:equal>
								<logic:notEqual name="RecConVO" property="modificarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="recursoAdapterVO" property="modificarRecConEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
							</logic:notEqual>
						</td>
						<td>
							<!-- Eliminar-->
							<logic:equal name="recursoAdapterVO" property="eliminarRecConEnabled" value="enabled">
								<logic:equal name="RecConVO" property="eliminarEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarRecCon', '<bean:write name="RecConVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
									</a>
								</logic:equal>	
								<logic:notEqual name="RecConVO" property="eliminarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="recursoAdapterVO" property="eliminarRecConEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
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
		<td colspan="20" align="right">
 				<bean:define id="agregarRecConEnabled" name="recursoAdapterVO" property="agregarRecConEnabled"/>
			<input type="button" <%=agregarRecConEnabled%> class="boton" 
				onClick="submitForm('agregarRecCon', '<bean:write name="recursoAdapterVO" property="recurso.id" bundle="base" formatKey="general.format.id"/>');" 
				value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
		</td>
		</tbody>
		</table>
	<!-- RecCon -->	
	
			<p> &nbsp;</p>

	<!-- RecClaDeu -->		
	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
		<caption><bean:message bundle="def" key="def.recurso.listRecClaDeu.ref"/></caption>
    	<tbody>
			<logic:notEmpty  name="recursoAdapterVO" property="recurso.listRecClaDeu">	    	
		    	<tr>
					<th width="1">&nbsp;</th> <!-- Ver -->
					<th width="1">&nbsp;</th> <!-- Modificar -->
					<th width="1">&nbsp;</th> <!-- Eliminar -->
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
						<td>
							<!-- Modificar-->
							<logic:equal name="recursoAdapterVO" property="modificarRecClaDeuEnabled" value="enabled">
								<logic:equal name="RecClaDeuVO" property="modificarEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarRecClaDeu', '<bean:write name="RecClaDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
									</a>
								</logic:equal>
								<logic:notEqual name="RecClaDeuVO" property="modificarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="recursoAdapterVO" property="modificarRecClaDeuEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
							</logic:notEqual>
						</td>
						<td>
							<!-- Eliminar-->
							<logic:equal name="recursoAdapterVO" property="eliminarRecClaDeuEnabled" value="enabled">
								<logic:equal name="RecClaDeuVO" property="eliminarEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarRecClaDeu', '<bean:write name="RecClaDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
									</a>
								</logic:equal>	
								<logic:notEqual name="RecClaDeuVO" property="eliminarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="recursoAdapterVO" property="eliminarRecClaDeuEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
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
		<td colspan="20" align="right">
 				<bean:define id="agregarRecClaDeuEnabled" name="recursoAdapterVO" property="agregarRecClaDeuEnabled"/>
			<input type="button" <%=agregarRecClaDeuEnabled%> class="boton" 
				onClick="submitForm('agregarRecClaDeu', '<bean:write name="recursoAdapterVO" property="recurso.id" bundle="base" formatKey="general.format.id"/>');" 
				value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
		</td>
		</tbody>
		</table>
	<!-- RecClaDeu -->	

	<!-- RecAtrCue -->		
	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
		<caption><bean:message bundle="def" key="def.recurso.listRecAtrCue.ref"/></caption>
    	<tbody>
			<logic:notEmpty  name="recursoAdapterVO" property="recursoDefinitionForRecAtrCue.listRecAtrCueDefinition">	    	
		    	<tr>
					<th width="1">&nbsp;</th> <!-- Ver -->
					<th width="1">&nbsp;</th> <!-- Modificar -->
					<th width="1">&nbsp;</th> <!-- Eliminar -->
					<th align="left"><bean:message bundle="def" key="def.atributo.label"/></th>
					<th align="left">Valor por Defecto</th>
					<th align="left">Vigencia</th>
				</tr>
				<logic:iterate id="GenericAtrDefinition" name="recursoAdapterVO" property="recursoDefinitionForRecAtrCue.listRecAtrCueDefinition" indexId="count">
						
						<bean:define id="AtrVal" name="GenericAtrDefinition"/>
						
						<tr>
						<!-- Ver -->
						<td>
							<logic:equal name="recursoAdapterVO" property="verRecAtrCueEnabled" value="enabled">							
								<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verRecAtrCue', '<bean:write name="AtrVal" property="atributo.id" bundle="base" formatKey="general.format.id"/>');">
									<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
								</a>
							</logic:equal>
							<logic:notEqual name="recursoAdapterVO" property="verRecAtrCueEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
							</logic:notEqual>
						</td>
						<td>
							<!-- Modificar-->
							<logic:equal name="recursoAdapterVO" property="modificarRecAtrCueEnabled" value="enabled">
								<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarRecAtrCue', '<bean:write name="AtrVal" property="atributo.id" bundle="base" formatKey="general.format.id"/>');">
									<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
								</a>
							</logic:equal>
							<logic:notEqual name="recursoAdapterVO" property="modificarRecAtrCueEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
							</logic:notEqual>
						</td>
						<td>
							<!-- Eliminar-->
							<logic:equal name="recursoAdapterVO" property="eliminarRecAtrCueEnabled" value="enabled">
								<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarRecAtrCue', '<bean:write name="AtrVal" property="atributo.id" bundle="base" formatKey="general.format.id"/>');">
									<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
								</a>
							</logic:equal>
							<logic:notEqual name="recursoAdapterVO" property="eliminarRecAtrCueEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
							</logic:notEqual>
						</td>
						<%@ include file="/def/atrDefinitionView4Edit.jsp" %>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			<logic:empty  name="recursoAdapterVO" property="recurso.listRecAtrCue">
				<tr><td align="center">
				<bean:message bundle="base" key="base.noExistenRegitros"/>
				</td></tr>
			</logic:empty>					
		<td colspan="20" align="right">
 				<bean:define id="agregarRecAtrCueEnabled" name="recursoAdapterVO" property="agregarRecAtrCueEnabled"/>
			<input type="button" <%=agregarRecAtrCueEnabled%> class="boton" 
				onClick="submitForm('agregarRecAtrCue', '<bean:write name="recursoAdapterVO" property="recurso.id" bundle="base" formatKey="general.format.id"/>');" 
				value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
		</td>
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
					<th width="1">&nbsp;</th> <!-- Modificar -->
					<th width="1">&nbsp;</th> <!-- Eliminar -->
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
						<td>
							<!-- Modificar-->
							<logic:equal name="recursoAdapterVO" property="modificarRecGenCueAtrVaEnabled" value="enabled">
								<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarRecGenCueAtrVa', '<bean:write name="AtrVal" property="idDefinition" bundle="base" formatKey="general.format.id"/>');">
									<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
								</a>
							</logic:equal>
							<logic:notEqual name="recursoAdapterVO" property="modificarRecGenCueAtrVaEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
							</logic:notEqual>
						</td>
						<td>
							<!-- Eliminar-->
							<logic:equal name="recursoAdapterVO" property="eliminarRecGenCueAtrVaEnabled" value="enabled">
								<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarRecGenCueAtrVa', '<bean:write name="AtrVal" property="idDefinition" bundle="base" formatKey="general.format.id"/>');">
									<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
								</a>
							</logic:equal>
							<logic:notEqual name="recursoAdapterVO" property="eliminarRecGenCueAtrVaEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
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
		<td colspan="20" align="right">
 				<bean:define id="agregarRecGenCueAtrVaEnabled" name="recursoAdapterVO" property="agregarRecGenCueAtrVaEnabled"/>
			<input type="button" <%=agregarRecGenCueAtrVaEnabled%> class="boton" 
				onClick="submitForm('agregarRecGenCueAtrVa', '<bean:write name="recursoAdapterVO" property="recurso.id" bundle="base" formatKey="general.format.id"/>');" 
				value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
		</td>
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
					<th width="1">&nbsp;</th> <!-- Modificar -->
					<th width="1">&nbsp;</th> <!-- Eliminar -->
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
						<td>
							<!-- Modificar-->
							<logic:equal name="recursoAdapterVO" property="modificarRecEmiEnabled" value="enabled">
								<logic:equal name="RecEmiVO" property="modificarEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarRecEmi', '<bean:write name="RecEmiVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
									</a>
								</logic:equal>
								<logic:notEqual name="RecEmiVO" property="modificarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="recursoAdapterVO" property="modificarRecEmiEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
							</logic:notEqual>
						</td>
						<td>
							<!-- Eliminar-->
							<logic:equal name="recursoAdapterVO" property="eliminarRecEmiEnabled" value="enabled">
								<logic:equal name="RecEmiVO" property="eliminarEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarRecEmi', '<bean:write name="RecEmiVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
									</a>
								</logic:equal>	
								<logic:notEqual name="RecEmiVO" property="eliminarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="recursoAdapterVO" property="eliminarRecEmiEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
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
		<td colspan="20" align="right">
 				<bean:define id="agregarRecEmiEnabled" name="recursoAdapterVO" property="agregarRecEmiEnabled"/>
			<input type="button" <%=agregarRecEmiEnabled%> class="boton" 
				onClick="submitForm('agregarRecEmi', '<bean:write name="recursoAdapterVO" property="recurso.id" bundle="base" formatKey="general.format.id"/>');" 
				value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
		</td>
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
					<th width="1">&nbsp;</th> <!-- Modificar -->
					<th width="1">&nbsp;</th> <!-- Eliminar -->
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
						<td>
							<!-- Modificar-->
							<logic:equal name="recursoAdapterVO" property="modificarRecAtrCueEmiEnabled" value="enabled">
								<logic:equal name="RecAtrCueEmiVO" property="modificarEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarRecAtrCueEmi', '<bean:write name="RecAtrCueEmiVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
									</a>
								</logic:equal>
								<logic:notEqual name="RecAtrCueEmiVO" property="modificarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="recursoAdapterVO" property="modificarRecAtrCueEmiEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
							</logic:notEqual>
						</td>
						<td>
							<!-- Eliminar-->
							<logic:equal name="recursoAdapterVO" property="eliminarRecAtrCueEmiEnabled" value="enabled">
								<logic:equal name="RecAtrCueEmiVO" property="eliminarEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarRecAtrCueEmi', '<bean:write name="RecAtrCueEmiVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
									</a>
								</logic:equal>	
								<logic:notEqual name="RecAtrCueEmiVO" property="eliminarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="recursoAdapterVO" property="eliminarRecAtrCueEmiEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
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
		<td colspan="20" align="right">
 				<bean:define id="agregarRecAtrCueEmiEnabled" name="recursoAdapterVO" property="agregarRecAtrCueEmiEnabled"/>
			<input type="button" <%=agregarRecAtrCueEmiEnabled%> class="boton" 
				onClick="submitForm('agregarRecAtrCueEmi', '<bean:write name="recursoAdapterVO" property="recurso.id" bundle="base" formatKey="general.format.id"/>');" 
				value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
		</td>
		</tbody>
	</table>
	<!-- RecAtrCueEmi -->	
	
		<p> &nbsp;</p>

	<!-- RecursoArea -->		
	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
		<caption><bean:message bundle="def" key="def.recurso.listRecursoArea.ref"/></caption>
    	<tbody>
			<logic:notEmpty  name="recursoAdapterVO" property="recurso.listRecursoArea">	    	
		    	<tr>
					<th align="left"><bean:message bundle="def" key="def.area.label"/></th>
					<th align="left"><bean:message bundle="def" key="def.recursoArea.perCreaEmi.label"/></th>
				</tr>
				<logic:iterate id="RecursoAreaVO" name="recursoAdapterVO" property="recurso.listRecursoArea">
					<tr>	
						<td><bean:write name="RecursoAreaVO" property="area.desArea"/>&nbsp;</td>
						<td><bean:write name="RecursoAreaVO" property="perCreaEmi.value"/>&nbsp;</td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			<logic:empty  name="recursoAdapterVO" property="recurso.listRecursoArea">
				<tr><td align="center">
				<bean:message bundle="base" key="base.noExistenRegitros"/>
				</td></tr>
			</logic:empty>					
		</tbody>
	</table>
	<!-- RecursoArea -->	
	
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