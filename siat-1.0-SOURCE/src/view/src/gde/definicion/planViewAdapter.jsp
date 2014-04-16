<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarPlan.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>

	<h1><bean:message bundle="gde" key="gde.planViewAdapter.title" /></h1>
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- Plan -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.plan.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.desPlan.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.desPlan" /></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.ordenanza.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.ordenanza"/></td>
				</tr>
				<tr>	
					<td><label><bean:message bundle="def" key="def.viaDeuda.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.viaDeuda.desViaDeuda"/></td>					
				</tr>
				<tr>	
					<td><label><bean:message bundle="bal" key="bal.sistema.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.sistema.desSistema"/></td>					
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.fechaAlta.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.fechaAltaView" /></td>
				</tr>
				<tr>	
					<td><label><bean:message bundle="gde" key="gde.plan.esManual.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.esManual.value"/></td>					
				</tr>

		<!-- Campos condicionales a esManual -->
		<logic:equal name="planAdapterVO" property="plan.esManual.value" value="No">
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.fecVenDeuDes.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.fecVenDeuDesView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.fecVenDeuHas.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.fecVenDeuHasView"/></td>
				</tr>

				<tr>	
					<td><label><bean:message bundle="gde" key="gde.plan.aplicaTotalImpago.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.aplicaTotalImpago.value"/></td>					
				</tr>

				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.canMaxCuo.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.canMaxCuoView" /></td>			
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.canMinPer.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.canMinPerView" /></td>			
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.canMinCuoParCuoSal.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.canMinCuoParCuoSalView" /></td>			
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.canCuoAImpEnForm.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.canCuoAImpEnFormView" /></td>			
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.cuoDesParaRec.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.cuoDesParaRecView" /></td>			
				</tr>
				<tr>	
					<td><label><bean:message bundle="gde" key="gde.plan.poseeActEsp.label"/>: </label></td>
					<td class="normal">
						<bean:write name="planAdapterVO" property="plan.poseeActEsp.value"/>
					</td>					
				</tr>
				<tr>	
					<td><label><bean:message bundle="gde" key="gde.tipoDeudaPlan.label"/>: </label></td>
					<td class="normal">
						<bean:write name="planAdapterVO" property="plan.tipoDeudaPlan.desTipoDeudaPlan" />
					</td>					
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.aplicaPagCue.label"/>: </label></td>
					<td class="normal">
						<bean:write name="planAdapterVO" property="plan.aplicaPagCue.value"/>
					</td>
				</tr>
		</logic:equal>
		<!-- Fin Campos condicionales a esManual -->				
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.nameSequence.label"/>: </label></td>
					<td class="normal">
						<bean:write name="planAdapterVO" property="plan.nameSequence"/>
					</td>			
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.formulario.label"/>: </label></td>
					<td class="normal">
						<bean:write name="planAdapterVO" property="plan.formulario.codFormulario"/>
					</td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.formulario.leyenda.label"/>: </label></td>
					<td class="normal">
						<bean:write name="planAdapterVO" property="plan.leyendaForm"/>
					</td>
				</tr>
				
				<!-- Inclucion de CasoView -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="planAdapterVO" property="plan"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>				
					</td>
				</tr>
				<!-- Fin Inclucion de CasoView -->
				
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.leyendaPlan.label"/>: </label></td>
					<td class="normal" colspan="3">
						<bean:write name="planAdapterVO" property="plan.leyendaPlan" />
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.linkNormativa.label"/>: </label></td>
					<td class="normal" colspan="3">
						<a href="<bean:write name="planAdapterVO" property="plan.linkNormativa"/>" target="blank">
							<bean:write name="planAdapterVO" property="plan.linkNormativaView"/>
						</a>
					</td>
				</tr>
				
				<logic:notEqual name="planAdapterVO" property="act" value="desactivar">
               		<td><label><bean:message bundle="gde" key="gde.plan.fechaBaja.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.fechaBajaView" /></td>
				</logic:notEqual>
					
				<logic:equal name="planAdapterVO" property="act" value="desactivar">
					<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.plan.fechaBaja.label"/>: </label></td>
					<td class="normal">
						<html:text name="planAdapterVO" property="plan.fechaBajaView" styleId="fechaBajaView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaBajaView');" id="a_fechaBajaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
						</a>
					</td>
				</logic:equal>
								
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.estado.value"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- Plan -->
	
 <logic:equal name="planAdapterVO" property="act" value="ver">	
 	<!-- Recurso -->
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
			<caption><bean:message bundle="gde" key="gde.plan.listRecurso.label"/></caption>
			<tbody>
				<logic:notEmpty name="planAdapterVO" property="plan.listPlanRecurso">
					<tr>
						<th align="left" width="20%"><bean:message bundle="gde" key="gde.planClaDeu.fechaDesde.label"/></th>
						<th align="left" width ="20%"><bean:message bundle="gde" key="gde.planClaDeu.fechaHasta.label"/></th>
						<th align="left" width="60%"><bean:message bundle="def" key="def.recurso.label"/></th>						
					</tr>
					<logic:iterate id="PlanRecurso" name="planAdapterVO" property="plan.listPlanRecurso">
						<tr>
                     		<td><bean:write name="PlanRecurso" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="PlanRecurso" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="PlanRecurso" property="recurso.desRecurso" />&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="planAdapterVO" property="plan.listPlanRecurso">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
	<logic:equal name="planAdapterVO" property="plan.esManual.value" value="No">
		<!-- PlanClaDeu -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="gde" key="gde.plan.listPlanClaDeu.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="planAdapterVO" property="plan.listPlanClaDeu">	    	
			    	<tr>
						<th align="left"><bean:message bundle="gde" key="gde.planClaDeu.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planClaDeu.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recClaDeu.desClaDeu.label"/></th>						
					</tr>
					<logic:iterate id="PlanClaDeuVO" name="planAdapterVO" property="plan.listPlanClaDeu">
						<tr>
							<td><bean:write name="PlanClaDeuVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="PlanClaDeuVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="PlanClaDeuVO" property="recClaDeu.desClaDeu" />&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="planAdapterVO" property="plan.listPlanClaDeu">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- PlanClaDeu -->
	</logic:equal>
				
		<!-- PlanMotCad -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="gde" key="gde.plan.listPlanMotCad.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="planAdapterVO" property="plan.listPlanMotCad">	    	
			    	<tr>
						<th align="left"><bean:message bundle="gde" key="gde.planMotCad.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planMotCad.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planMotCad.desPlanMotCad.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planMotCad.esEspecial.label"/></th>
					</tr>
					<logic:iterate id="PlanMotCadVO" name="planAdapterVO" property="plan.listPlanMotCad">
						<tr>
							<td><bean:write name="PlanMotCadVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="PlanMotCadVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="PlanMotCadVO" property="desPlanMotCad" />&nbsp;</td>
							<td><bean:write name="PlanMotCadVO" property="esEspecial.value" />&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="planAdapterVO" property="plan.listPlanMotCad">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- PlanMotCad -->
		
	<logic:equal name="planAdapterVO" property="plan.esManual.value" value="No">		
		<!-- PlanForActDeu -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="gde" key="gde.plan.listPlanForActDeu.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="planAdapterVO" property="plan.listPlanForActDeu">	    	
			    	<tr>
						<th align="left"><bean:message bundle="gde" key="gde.planForActDeu.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planForActDeu.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planForActDeu.fecVenDeuDes.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planForActDeu.esComun.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planForActDeu.porcentaje.label"/></th>
					</tr>
					<logic:iterate id="PlanForActDeuVO" name="planAdapterVO" property="plan.listPlanForActDeu">
						<tr>
							<td><bean:write name="PlanForActDeuVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="PlanForActDeuVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="PlanForActDeuVO" property="fecVenDeuDesView"/>&nbsp;</td>
							<td><bean:write name="PlanForActDeuVO" property="esComun.value"/>&nbsp;</td>
							<td><bean:write name="PlanForActDeuVO" property="porcentajeView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="planAdapterVO" property="plan.listPlanForActDeu">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- PlanForActDeu -->
	</logic:equal>
	
	<logic:equal name="planAdapterVO" property="plan.esManual.value" value="No">	
		<!-- PlanImpMin -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="gde" key="gde.plan.listPlanImpMin.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="planAdapterVO" property="plan.listPlanImpMin">	    	
			    	<tr>
						<th align="left"><bean:message bundle="gde" key="gde.planImpMin.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planImpMin.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planImpMin.cantidadCuotas.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planImpMin.impMinDeu.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planImpMin.impMinCuo.label"/></th>
					</tr>
					<logic:iterate id="PlanImpMinVO" name="planAdapterVO" property="plan.listPlanImpMin">
			
						<tr>
							<td><bean:write name="PlanImpMinVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="PlanImpMinVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="PlanImpMinVO" property="cantidadCuotasView"/>&nbsp;</td>
							<td><bean:write name="PlanImpMinVO" property="impMinDeuView"/>&nbsp;</td>
							<td><bean:write name="PlanImpMinVO" property="impMinCuoView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="planAdapterVO" property="plan.listPlanImpMin">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- PlanImpMin -->
	</logic:equal>
	
	<logic:equal name="planAdapterVO" property="plan.esManual.value" value="No">	
		<!-- PlanDescuento -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="gde" key="gde.plan.listPlanDescuento.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="planAdapterVO" property="plan.listPlanDescuento">	    	
			    	<tr>
						<th align="left"><bean:message bundle="gde" key="gde.planDescuento.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planDescuento.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planDescuento.cantidadCuotasPlan.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planDescuento.porDesCap.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planDescuento.porDesAct.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planDescuento.porDesInt.label"/></th>
					</tr>
					<logic:iterate id="PlanDescuentoVO" name="planAdapterVO" property="plan.listPlanDescuento">
						<tr>
							<td><bean:write name="PlanDescuentoVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="PlanDescuentoVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="PlanDescuentoVO" property="cantidadCuotasPlanView"/>&nbsp;</td>
							<td><bean:write name="PlanDescuentoVO" property="porDesCapView"/>&nbsp;</td>
							<td><bean:write name="PlanDescuentoVO" property="porDesActView"/>&nbsp;</td>
							<td><bean:write name="PlanDescuentoVO" property="porDesIntView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="planAdapterVO" property="plan.listPlanDescuento">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- PlanDescuento -->
	</logic:equal>
	
	<logic:equal name="planAdapterVO" property="plan.esManual.value" value="No">	
		<!-- PlanIntFin -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="gde" key="gde.plan.listPlanIntFin.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="planAdapterVO" property="plan.listPlanIntFin">	    	
			    	<tr>
						<th align="left"><bean:message bundle="gde" key="gde.planIntFin.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planIntFin.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planIntFin.cuotaHasta.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planIntFin.interes.label"/></th>
					</tr>
					<logic:iterate id="PlanIntFinVO" name="planAdapterVO" property="plan.listPlanIntFin">
			
						<tr>
							<td><bean:write name="PlanIntFinVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="PlanIntFinVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="PlanIntFinVO" property="cuotaHastaView"/>&nbsp;</td>
							<td><bean:write name="PlanIntFinVO" property="interesView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="planAdapterVO" property="plan.listPlanIntFin">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- PlanIntFin -->
	</logic:equal>
		
		<!-- PlanVen -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="gde" key="gde.plan.listPlanVen.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="planAdapterVO" property="plan.listPlanVen">	    	
			    	<tr>
						<th align="left"><bean:message bundle="gde" key="gde.planVen.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planVen.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planVen.cuotaHasta.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.vencimiento.label"/></th>
					</tr>
					<logic:iterate id="PlanVenVO" name="planAdapterVO" property="plan.listPlanVen">
			
						<tr>
							<td><bean:write name="PlanVenVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="PlanVenVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="PlanVenVO" property="cuotaHastaView"/>&nbsp;</td>
							<td><bean:write name="PlanVenVO" property="vencimiento.desVencimiento"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="planAdapterVO" property="plan.listPlanVen">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- PlanVen -->
	
	<logic:equal name="planAdapterVO" property="plan.esManual.value" value="No">
		<!-- PlanAtrVal -->
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="gde" key="gde.plan.listPlanAtrVal.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="planAdapterVO" property="plan.listPlanAtrVal">	    	
			    	<tr>
						<th align="left"><bean:message bundle="gde" key="gde.planAtrVal.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planAtrVal.fechaHasta.label"/></th>
					</tr>
					<logic:iterate id="PlanAtrValVO" name="planAdapterVO" property="plan.listPlanAtrVal">
						<tr>
							<td><bean:write name="PlanAtrValVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="PlanAtrValVO" property="fechaHastaView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="planAdapterVO" property="plan.listPlanAtrVal">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- PlanAtrVal -->				
	</logic:equal>
	
	<logic:equal name="planAdapterVO" property="plan.esManual.value" value="No">
		<!-- PlanExe -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="gde" key="gde.plan.listPlanExe.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="planAdapterVO" property="plan.listPlanExe">	    	
			    	<tr>
						<th align="left"><bean:message bundle="gde" key="gde.planExe.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planExe.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="exe" key="exe.exencion.label"/></th>
					</tr>
					<logic:iterate id="PlanExeVO" name="planAdapterVO" property="plan.listPlanExe">
						<tr>
							<td><bean:write name="PlanExeVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="PlanExeVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="PlanExeVO" property="exencion.desExencion"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="planAdapterVO" property="plan.listPlanExe">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- PlanExe -->
	</logic:equal>	
</logic:equal>	
<!-- Cierre del equal act=ver -->			
				
		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
		   	    	<html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						<bean:message bundle="base" key="abm.button.imprimir"/>
					</html:button>
	   	    	</td>   
	   	    	<td align="right">
					<logic:equal name="planAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="planAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="planAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	     <input type="hidden" name="name"  value="<bean:write name='planAdapterVO' property='name'/>" id="name"/>
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
