<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
  	    <%@include file="/base/calendar.js"%>   
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarEncRecurso.do">

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
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.categoria.label"/>:</label></td>
					<td colspan="2" class="normal">
						<html:select name="encRecursoAdapterVO" property="recurso.categoria.id" styleClass="select">
							<html:optionsCollection name="encRecursoAdapterVO" property="listCategoria" label="desCategoria" value="id" />
						</html:select>
					</td>
				</tr>
				<tr>
					<!-- Codigo -->
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.codRecurso.label"/>:</label></td>
					<td class="normal"><html:text name="encRecursoAdapterVO" property="recurso.codRecurso" size="10" maxlength="10" styleClass="datos"/></td>
				</tr>					
				<tr>
					<!-- Descripcion -->
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.desRecurso.label"/>: </label></td>
					<td class="normal"><html:text name="encRecursoAdapterVO" property="recurso.desRecurso" size="25" maxlength="100" styleClass="datos"/></td>					
				</tr>
				<tr>
					<!-- EsAutoliquidable -->
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.esAutoliquidable.label"/>: </label></td>
					<td class="normal">	
						<html:select name="encRecursoAdapterVO" property="recurso.esAutoliquidable.id" styleClass="select">
							<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
					<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.esAutoliquidable.description"/></li></ul></td>				
				</tr>
				<tr>
					<!-- FecEsAut -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.fecEsAut.label"/>: </label></td>
					<td class="normal">
					<html:text name="encRecursoAdapterVO" property="recurso.fecEsAutView" styleId="fecEsAutView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fecEsAutView');" id="a_fecEsAutView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.fecEsAut.description"/></li></ul></td>				
				</tr>
					<tr>
					<!-- EsFiscalizable -->
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.esFiscalizable.label"/>: </label></td>
					<td class="normal">	
						<html:select name="encRecursoAdapterVO" property="recurso.esFiscalizable.id" styleClass="select">
							<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
					<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.esFiscalizable.description"/></li></ul></td>				
				</tr>
				<tr>
					<!-- FecEsFis -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.fecEsFis.label"/>: </label></td>
					<td class="normal">
					<html:text name="encRecursoAdapterVO" property="recurso.fecEsFisView" styleId="fecEsFisView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fecEsFisView');" id="a_fecEsFisView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.fecEsFis.description"/></li></ul></td>				
				</tr>
				<tr>
					<!-- FecDesIntDiaBan -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.fecDesIntDiaBan.label"/>: </label></td>
					<td class="normal">
					<html:text name="encRecursoAdapterVO" property="recurso.fecDesIntDiaBanView" styleId="fecDesIntDiaBanView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fecDesIntDiaBanView');" id="a_fecDesIntDiaBanView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>
				<tr>
					<!-- FechaAlta -->
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.fechaAlta.label"/>: </label></td>
					<td class="normal">
					<html:text name="encRecursoAdapterVO" property="recurso.fechaAltaView" styleId="fechaAltaView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaAltaView');" id="a_fechaAltaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
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
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.periodoDeuda.label"/>: </label></td>
					<td class="normal">	
						<html:select name="encRecursoAdapterVO" property="recurso.periodoDeuda.id" styleClass="select">
							<html:optionsCollection name="encRecursoAdapterVO" property="listPeriodoDeuda" label="desPeriodoDeuda" value="id" />
						</html:select>
					</td>
				</tr>
				<tr>
					<!-- GesDeuNoVen -->
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.gesDeuNoVen.label"/>: </label></td>
					<td class="normal">	
						<html:select name="encRecursoAdapterVO" property="recurso.gesDeuNoVen.id" styleClass="select">
							<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
					<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.gesDeuNoVen.description"/></li></ul></td>				
				</tr>
				<tr>
					<!-- DeuExiVen -->
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.deuExiVen.label"/>: </label></td>
					<td class="normal">	
						<html:select name="encRecursoAdapterVO" property="recurso.deuExiVen.id" styleClass="select">
							<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
				</tr>
				<tr>
					<!-- GesCobranza -->
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.gesCobranza.label"/>: </label></td>
					<td class="normal">	
						<html:select name="encRecursoAdapterVO" property="recurso.gesCobranza.id" styleClass="select">
							<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
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
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.perEmiDeuMas.label"/>: </label></td>
					<td class="normal">	
						<html:select name="encRecursoAdapterVO" property="recurso.perEmiDeuMas.id" styleClass="select" onchange="submitForm('paramPerEmiDeuMas', '');">
							<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
				</tr>
				<logic:equal name="encRecursoAdapterVO" property="paramPerEmiDeuMas" value="1">
					<tr>
						<!-- AtrEmiMas -->										
						<td align="right"><label><bean:message bundle="def" key="def.recurso.atrEmiMas.label"/>:</label></td>
						<td colspan="2" class="normal">
							<html:select name="encRecursoAdapterVO" property="recurso.atrEmiMas.id" styleClass="select">
								<html:optionsCollection name="encRecursoAdapterVO" property="listAtrSegment" label="desAtributo" value="id" />
							</html:select>
						</td>
					</tr>
				</logic:equal>
				<tr>
					<!-- PerEmiDeuPuntual -->
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.perEmiDeuPuntual.label"/>: </label></td>
					<td class="normal">	
						<html:select name="encRecursoAdapterVO" property="recurso.perEmiDeuPuntual.id" styleClass="select">
							<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
					<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.perEmiDeuPuntual.description"/></li></ul></td>
				</tr>
				<tr>
					<!-- PerEmiDeuExt -->
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.perEmiDeuExt.label"/>: </label></td>
					<td class="normal">	
						<html:select name="encRecursoAdapterVO" property="recurso.perEmiDeuExt.id" styleClass="select">
							<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
					<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.perEmiDeuExt.description"/></li></ul></td>				
				</tr>
				<tr>
					<!-- EsDeudaTitular -->
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.esDeudaTitular.label"/>: </label></td>
					<td class="normal">	
						<html:select name="encRecursoAdapterVO" property="recurso.esDeudaTitular.id" styleClass="select">
							<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
					<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.esDeudaTitular.description"/></li></ul></td>				
				</tr>
				<tr>
					<!-- Vencimiento -->										
					<td align="right"><label><bean:message bundle="def" key="def.recurso.vencimiento.label"/>:</label></td>
					<td class="normal">
						<html:select name="encRecursoAdapterVO" property="recurso.vencimiento.id" styleClass="select">
							<html:optionsCollection name="encRecursoAdapterVO" property="listVencimiento" label="desVencimiento" value="id" />
						</html:select>
					</td>
					<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.vencimiento.description"/></li></ul></td>
				</tr>
				<tr>
					<!-- AtributoAse -->										
					<td align="right"><label><bean:message bundle="def" key="def.recurso.atributoAse.label"/>:</label></td>
					<td colspan="2" class="normal">
						<html:select name="encRecursoAdapterVO" property="recurso.atributoAse.id" styleClass="select">
							<html:optionsCollection name="encRecursoAdapterVO" property="listAtributo" label="desAtributo" value="id" />
						</html:select>
					</td>
				</tr>
				<tr>
					<!-- PerImpMasDeu -->
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.perImpMasDeu.label"/>: </label></td>
					<td class="normal">	
						<html:select name="encRecursoAdapterVO" property="recurso.perImpMasDeu.id" styleClass="select" onchange="submitForm('paramPerImpMasDeu', '');">
							<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
				</tr>
				<logic:equal name="encRecursoAdapterVO" property="paramPerImpMasDeu" value="1">
					<tr>
						<!-- Formulario para Impresion Masiva -->										
						<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.formImpMasDeu.label"/>: </label></td>
						<td colspan="2" class="normal">
							<html:select name="encRecursoAdapterVO" property="recurso.formImpMasDeu.id" styleClass="select" style="width: 300px;">
								<html:optionsCollection name="encRecursoAdapterVO" property="listFormulario" label="desFormulario" value="id" />
							</html:select>
						</td>
					</tr>

					<tr>
						<!-- GenNotif -->
						<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.genNotif.label"/>: </label></td>
						<td class="normal">	
							<html:select name="encRecursoAdapterVO" property="recurso.genNotif.id" styleClass="select" onchange="submitForm('paramGenNotImpMas', '');">
								<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
							</html:select>
						</td>
						<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.genNotif.description"/></li></ul></td>
					</tr>
					<logic:equal name="encRecursoAdapterVO" property="paramGenNotImpMas" value="1">
						<tr>
							<!-- Formulario de Notificacion de Impresion Masiva -->									
							<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.formNotif.label"/>:  </label></td>
							<td colspan="2" class="normal">
								<html:select name="encRecursoAdapterVO" property="recurso.formNotif.id" styleClass="select" style="width: 300px;">
									<html:optionsCollection name="encRecursoAdapterVO" property="listFormulario" label="desFormulario" value="id" />
								</html:select>
							</td>
						</tr>
					</logic:equal>
						<tr>
							<!-- GenPadFir -->
							<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.genPadFir.label"/>: </label></td>
							<td class="normal">	
								<html:select name="encRecursoAdapterVO" property="recurso.genPadFir.id" styleClass="select" onchange="submitForm('paramGenPadFir', '');">
									<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
								</html:select>
							</td>
							<td><ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.genPadFir.description"/></li></ul></td>
						</tr>
					<logic:equal name="encRecursoAdapterVO" property="paramGenPadFir" value="1">
						<tr>
							<!-- Formulario de Padron de Firmas en Impresion Masiva -->									
							<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.formPadFir.label"/>:  </label></td>
							<td colspan="2" class="normal">
								<html:select name="encRecursoAdapterVO" property="recurso.formPadFir.id" styleClass="select" style="width: 300px;">
									<html:optionsCollection name="encRecursoAdapterVO" property="listFormulario" label="desFormulario" value="id" />
								</html:select>
							</td>
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
					<td colspan="2" class="normal">
						<html:select name="encRecursoAdapterVO" property="recurso.tipObjImp.id" styleClass="select" onchange="submitForm('paramTipObjImp', '');">
							<html:optionsCollection name="encRecursoAdapterVO" property="listTipObjImp" label="desTipObjImp" value="id" />
						</html:select>
					</td>
				</tr>
				<tr>
					<!-- EsLibreDeuda -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.esLibreDeuda.label"/>: </label></td>
					<td class="normal">	
						<logic:equal name="encRecursoAdapterVO" property="paramTipObjImp" value="1">		
							<html:select name="encRecursoAdapterVO" property="recurso.esLibreDeuda.id" styleClass="select" disabled="false">
								<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
							</html:select>
						</logic:equal>
						<logic:equal name="encRecursoAdapterVO" property="paramTipObjImp" value="0">		
							<html:select name="encRecursoAdapterVO" property="recurso.esLibreDeuda.id" styleClass="select" disabled="true">
								<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
							</html:select>
						</logic:equal>
					</td>
				</tr>
				<tr>
					<!-- EsPrincipal -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.esPrincipal.label"/>: </label></td>
					<td class="normal">	
						<logic:equal name="encRecursoAdapterVO" property="paramTipObjImp" value="1">		
							<html:select name="encRecursoAdapterVO" property="recurso.esPrincipal.id" styleClass="select" onchange="submitForm('paramEsPrincipal', '');" disabled="false">
								<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
							</html:select>
						</logic:equal>
						<logic:equal name="encRecursoAdapterVO" property="paramTipObjImp" value="0">		
							<html:select name="encRecursoAdapterVO" property="recurso.esPrincipal.id" styleClass="select" onchange="submitForm('paramTipObjImp', '');" disabled="true">
								<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
							</html:select>
						</logic:equal>
					</td>
				</tr>
				<tr>
					<!-- AltaCtaPorIface -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.altaCtaPorIface.label"/>: </label></td>
					<td class="normal">	
						<logic:equal name="encRecursoAdapterVO" property="paramEsPrincipal" value="1">		
							<html:select name="encRecursoAdapterVO" property="recurso.altaCtaPorIface.id" styleClass="select" disabled="false">
								<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
							</html:select>
						</logic:equal>
						<logic:notEqual name="encRecursoAdapterVO" property="paramEsPrincipal" value="1">		
							<html:select name="encRecursoAdapterVO" property="recurso.altaCtaPorIface.id" styleClass="select" disabled="true">
								<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
							</html:select>
						</logic:notEqual>
					</td>
				</tr>
				<tr>
					<!-- BajaCtaPorIface -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.bajaCtaPorIface.label"/>: </label></td>
					<td class="normal">	
						<logic:equal name="encRecursoAdapterVO" property="paramEsPrincipal" value="1">		
							<html:select name="encRecursoAdapterVO" property="recurso.bajaCtaPorIface.id" styleClass="select" disabled="false">
								<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
							</html:select>
						</logic:equal>
						<logic:notEqual name="encRecursoAdapterVO" property="paramEsPrincipal" value="1">		
							<html:select name="encRecursoAdapterVO" property="recurso.bajaCtaPorIface.id" styleClass="select" disabled="true">
								<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
							</html:select>
						</logic:notEqual>
					</td>
				</tr>
				<tr>
					<!-- ModiTitCtaPorIface -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.modiTitCtaPorIface.label"/>: </label></td>
					<td class="normal">	
						<logic:equal name="encRecursoAdapterVO" property="paramEsPrincipal" value="1">		
							<html:select name="encRecursoAdapterVO" property="recurso.modiTitCtaPorIface.id" styleClass="select" disabled="false">
								<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
							</html:select>
						</logic:equal>
						<logic:notEqual name="encRecursoAdapterVO" property="paramEsPrincipal" value="1">		
							<html:select name="encRecursoAdapterVO" property="recurso.modiTitCtaPorIface.id" styleClass="select" disabled="true">
								<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
							</html:select>
						</logic:notEqual>						
					</td>
				</tr>
				<tr>
					<!-- RecPri -->										
					<td align="right"><label><bean:message bundle="def" key="def.recurso.recPri.label"/>:</label></td>
					<td colspan="2" class="normal">
						<logic:equal name="encRecursoAdapterVO" property="paramEsPrincipal" value="0">		
							<html:select name="encRecursoAdapterVO" property="recurso.recPri.id" styleClass="select" disabled="false">
								<html:optionsCollection name="encRecursoAdapterVO" property="listRecurso" label="desRecurso" value="id" />
							</html:select>
						</logic:equal>
						<logic:notEqual name="encRecursoAdapterVO" property="paramEsPrincipal" value="0">		
							<html:select name="encRecursoAdapterVO" property="recurso.recPri.id" styleClass="select" disabled="true">
								<html:optionsCollection name="encRecursoAdapterVO" property="listRecurso" label="desRecurso" value="id" />
							</html:select>
						</logic:notEqual>
					</td>
				</tr>				
				<tr>
					<!-- AltaCtaPorPri -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.altaCtaPorPri.label"/>: </label></td>
					<td class="normal">	
						<logic:equal name="encRecursoAdapterVO" property="paramEsPrincipal" value="0">		
							<html:select name="encRecursoAdapterVO" property="recurso.altaCtaPorPri.id" styleClass="select" disabled="false">
								<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
							</html:select>
						</logic:equal>
						<logic:notEqual name="encRecursoAdapterVO" property="paramEsPrincipal" value="0">		
							<html:select name="encRecursoAdapterVO" property="recurso.altaCtaPorPri.id" styleClass="select" disabled="true">
								<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
							</html:select>
						</logic:notEqual>
					</td>
				</tr>
				<tr>
					<!-- BajaCtaPorPri -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.bajaCtaPorPri.label"/>: </label></td>
					<td class="normal">	
						<logic:equal name="encRecursoAdapterVO" property="paramEsPrincipal" value="0">		
							<html:select name="encRecursoAdapterVO" property="recurso.bajaCtaPorPri.id" styleClass="select" disabled="false">
								<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
						</logic:equal>
						<logic:notEqual name="encRecursoAdapterVO" property="paramEsPrincipal" value="0">		
							<html:select name="encRecursoAdapterVO" property="recurso.bajaCtaPorPri.id" styleClass="select" disabled="true">
								<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
							</html:select>
						</logic:notEqual>						
					</td>
				</tr>
				<tr>
					<!-- ModiTitCtaPorPri -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.modiTitCtaPorPri.label"/>: </label></td>
					<td class="normal">	
						<logic:equal name="encRecursoAdapterVO" property="paramEsPrincipal" value="0">							
							<html:select name="encRecursoAdapterVO" property="recurso.modiTitCtaPorPri.id" styleClass="select" disabled="false">
								<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
							</html:select>
						</logic:equal>													
						<logic:notEqual name="encRecursoAdapterVO" property="paramEsPrincipal" value="0">							
							<html:select name="encRecursoAdapterVO" property="recurso.modiTitCtaPorPri.id" styleClass="select" disabled="true">
								<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
							</html:select>
						</logic:notEqual>																			
					</td>
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
					<td colspan="2" class="normal">
						<html:select name="encRecursoAdapterVO" property="recurso.genCue.id" styleClass="select">
							<html:optionsCollection name="encRecursoAdapterVO" property="listGenCue" label="desGenCue" value="id" />
						</html:select>
					</td>
				</tr>
				<tr>
					<!-- GenCodGes -->										
					<td align="right"><label><bean:message bundle="def" key="def.recurso.genCodGes.label"/>:</label></td>
					<td colspan="2" class="normal">
						<html:select name="encRecursoAdapterVO" property="recurso.genCodGes.id" styleClass="select">
							<html:optionsCollection name="encRecursoAdapterVO" property="listGenCodGes" label="desGenCodGes" value="id" />
						</html:select>
					</td>
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
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.altaCtaManual.label"/>: </label></td>
					<td class="normal">	
						<html:select name="encRecursoAdapterVO" property="recurso.altaCtaManual.id" styleClass="select">
							<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
				</tr>
				<tr>
					<!-- BajaCtaManual -->
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.bajaCtaManual.label"/>: </label></td>
					<td class="normal">	
						<html:select name="encRecursoAdapterVO" property="recurso.bajaCtaManual.id" styleClass="select">
							<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
				</tr>
				<tr>
					<!-- ModiTitCtaManual -->
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.modiTitCtaManual.label"/>: </label></td>
					<td class="normal">	
						<html:select name="encRecursoAdapterVO" property="recurso.modiTitCtaManual.id" styleClass="select">
							<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
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
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.enviaJudicial.label"/>: </label></td>
					<td class="normal">	
						<html:select name="encRecursoAdapterVO" property="recurso.enviaJudicial.id" styleClass="select" onchange="submitForm('paramEnviaJudicial', '');">
							<html:optionsCollection name="encRecursoAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
				</tr>
				<tr>
					<!-- CriAsiPro -->										
					<td align="right"><label><bean:message bundle="def" key="def.recurso.criAsiPro.label"/>:</label></td>
					<td colspan="2" class="normal">
						<logic:equal name="encRecursoAdapterVO" property="paramEnviaJudicial" value="1">							
							<html:select name="encRecursoAdapterVO" property="recurso.criAsiPro.id" styleClass="select" disabled="false">
								<html:optionsCollection name="encRecursoAdapterVO" property="listCriAsiPro" label="desCriAsiPro" value="id" />
							</html:select>
						</logic:equal>
						<logic:equal name="encRecursoAdapterVO" property="paramEnviaJudicial" value="0">							
							<html:select name="encRecursoAdapterVO" property="recurso.criAsiPro.id" styleClass="select" disabled="true">
								<html:optionsCollection name="encRecursoAdapterVO" property="listCriAsiPro" label="desCriAsiPro" value="id" />
							</html:select>
						</logic:equal>
					</td>
				</tr>
			</table>
		</fieldset>
		<!-- Fin Caracteristicas de la gestion Judicial -->

		<!-- Fin Recurso -->
		
	<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
					<logic:equal name="encRecursoAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="encRecursoAdapterVO" property="act" value="agregar">
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
