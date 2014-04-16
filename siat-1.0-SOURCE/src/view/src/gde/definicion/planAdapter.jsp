<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/gde/AdministrarPlan.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.planAdapter.title"/></h1>
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
						<bean:write filter="false" name="planAdapterVO" property="plan.leyendaForm"/>
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
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.fechaBaja.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.fechaBajaView" /></td>
				</tr>
				<tr>
					<td colspan="4"> 
						<bean:define id="modificarEncabezadoEnabled" name="planAdapterVO" property="modificarEncabezadoEnabled"/>
						<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
							'<bean:write name="planAdapterVO" property="plan.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.modificar"/>"/>		
					</td>
				</tr>
			</table>
		</fieldset>	
		<!-- Plan -->
		<!-- Recurso -->
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
			<caption><bean:message bundle="gde" key="gde.plan.listRecurso.label"/></caption>
			<tbody>
				<logic:notEmpty name="planAdapterVO" property="plan.listPlanRecurso">
					<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left" width="20%"><bean:message bundle="gde" key="gde.planClaDeu.fechaDesde.label"/></th>
						<th align="left" width ="20%"><bean:message bundle="gde" key="gde.planClaDeu.fechaHasta.label"/></th>
						<th align="left" width="60%"><bean:message bundle="def" key="def.recurso.label"/></th>						
					</tr>
					<logic:iterate id="PlanRecurso" name="planAdapterVO" property="plan.listPlanRecurso">
						<tr>
						<!-- Ver -->
							<td>
								<logic:equal name="planAdapterVO" property="verPlanRecursoEnabled" value="enabled">
									<logic:equal name="PlanRecurso" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verPlanRecurso', '<bean:write name="PlanRecurso" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanRecurso" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="verPlanRecursoEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Modificar-->								
							<td>
								<logic:equal name="planAdapterVO" property="modificarPlanRecursoEnabled" value="enabled">
									<logic:equal name="PlanRecurso" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarPlanRecurso', '<bean:write name="PlanRecurso" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanRecurso" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="modificarPlanRecursoEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="planAdapterVO" property="eliminarPlanRecursoEnabled" value="enabled">
									<logic:equal name="PlanRecurso" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarPlanRecurso', '<bean:write name="PlanRecurso" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanRecurso" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="eliminarPlanRecursoEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
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
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarPlanRecursoEnabled" name="planAdapterVO" property="agregarPlanRecursoEnabled"/>
						<input type="button" <%=agregarPlanRecursoEnabled%> class="boton" 
							onClick="submitForm('agregarPlanRecurso', '<bean:write name="planAdapterVO" property="plan.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
			</tbody>
		</table>
					
<logic:equal name="planAdapterVO" property="plan.esManual.value" value="No">		 
		<!-- PlanClaDeu -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="gde" key="gde.plan.listPlanClaDeu.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="planAdapterVO" property="plan.listPlanClaDeu">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="gde" key="gde.planClaDeu.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planClaDeu.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recClaDeu.desClaDeu.label"/></th>						
					</tr>
					<logic:iterate id="PlanClaDeuVO" name="planAdapterVO" property="plan.listPlanClaDeu">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="planAdapterVO" property="verPlanClaDeuEnabled" value="enabled">
									<logic:equal name="PlanClaDeuVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verPlanClaDeu', '<bean:write name="PlanClaDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanClaDeuVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="verPlanClaDeuEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Modificar-->								
							<td>
								<logic:equal name="planAdapterVO" property="modificarPlanClaDeuEnabled" value="enabled">
									<logic:equal name="PlanClaDeuVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarPlanClaDeu', '<bean:write name="PlanClaDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanClaDeuVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="modificarPlanClaDeuEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="planAdapterVO" property="eliminarPlanClaDeuEnabled" value="enabled">
									<logic:equal name="PlanClaDeuVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarPlanClaDeu', '<bean:write name="PlanClaDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanClaDeuVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="eliminarPlanClaDeuEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
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
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarPlanClaDeuEnabled" name="planAdapterVO" property="agregarPlanClaDeuEnabled"/>
						<input type="button" <%=agregarPlanClaDeuEnabled%> class="boton" 
							onClick="submitForm('agregarPlanClaDeu', '<bean:write name="planAdapterVO" property="plan.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
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
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="gde" key="gde.planMotCad.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planMotCad.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planMotCad.desPlanMotCad.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planMotCad.esEspecial.label"/></th>
					</tr>
					<logic:iterate id="PlanMotCadVO" name="planAdapterVO" property="plan.listPlanMotCad">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="planAdapterVO" property="verPlanMotCadEnabled" value="enabled">
									<logic:equal name="PlanMotCadVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verPlanMotCad', '<bean:write name="PlanMotCadVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanMotCadVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="verPlanMotCadEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							<!-- Modificar-->								
							<td>
								<logic:equal name="planAdapterVO" property="modificarPlanMotCadEnabled" value="enabled">
									<logic:equal name="PlanMotCadVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarPlanMotCad', '<bean:write name="PlanMotCadVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanMotCadVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="modificarPlanMotCadEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="planAdapterVO" property="eliminarPlanMotCadEnabled" value="enabled">
									<logic:equal name="PlanMotCadVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarPlanMotCad', '<bean:write name="PlanMotCadVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanMotCadVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="eliminarPlanMotCadEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
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
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarPlanMotCadEnabled" name="planAdapterVO" property="agregarPlanMotCadEnabled"/>
						<input type="button" <%=agregarPlanMotCadEnabled%> class="boton" 
							onClick="submitForm('agregarPlanMotCad', '<bean:write name="planAdapterVO" property="plan.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
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
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="gde" key="gde.planForActDeu.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planForActDeu.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planForActDeu.fecVenDeuDes.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planForActDeu.esComun.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planForActDeu.porcentaje.label"/></th>
					</tr>
					<logic:iterate id="PlanForActDeuVO" name="planAdapterVO" property="plan.listPlanForActDeu">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="planAdapterVO" property="verPlanForActDeuEnabled" value="enabled">
									<logic:equal name="PlanForActDeuVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verPlanForActDeu', '<bean:write name="PlanForActDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanForActDeuVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="verPlanForActDeuEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							<!-- Modificar-->								
							<td>
								<logic:equal name="planAdapterVO" property="modificarPlanForActDeuEnabled" value="enabled">
									<logic:equal name="PlanForActDeuVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarPlanForActDeu', '<bean:write name="PlanForActDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanForActDeuVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="modificarPlanForActDeuEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="planAdapterVO" property="eliminarPlanForActDeuEnabled" value="enabled">
									<logic:equal name="PlanForActDeuVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarPlanForActDeu', '<bean:write name="PlanForActDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanForActDeuVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="eliminarPlanForActDeuEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="PlanForActDeuVO" property="fechaDesdeView"/></td>
							<td><bean:write name="PlanForActDeuVO" property="fechaHastaView"/></td>
							<td><bean:write name="PlanForActDeuVO" property="fecVenDeuDesView"/></td>
							<td><bean:write name="PlanForActDeuVO" property="esComun.value"/></td>
							<td><bean:write name="PlanForActDeuVO" property="porcentajeView"/></td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="planAdapterVO" property="plan.listPlanForActDeu">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarPlanForActDeuEnabled" name="planAdapterVO" property="agregarPlanForActDeuEnabled"/>
						<input type="button" <%=agregarPlanForActDeuEnabled%> class="boton" 
							onClick="submitForm('agregarPlanForActDeu', '<bean:write name="planAdapterVO" property="plan.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
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
			    		<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="gde" key="gde.planImpMin.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planImpMin.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planImpMin.cantidadCuotas.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planImpMin.impMinDeu.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planImpMin.impMinCuo.label"/></th>
					</tr>
					<logic:iterate id="PlanImpMinVO" name="planAdapterVO" property="plan.listPlanImpMin">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="planAdapterVO" property="verPlanImpMinEnabled" value="enabled">
									<logic:equal name="PlanImpMinVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verPlanImpMin', '<bean:write name="PlanImpMinVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanImpMinVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="verPlanImpMinEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							<!-- Modificar-->								
							<td>
								<logic:equal name="planAdapterVO" property="modificarPlanImpMinEnabled" value="enabled">
									<logic:equal name="PlanImpMinVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarPlanImpMin', '<bean:write name="PlanImpMinVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanImpMinVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="modificarPlanImpMinEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="planAdapterVO" property="eliminarPlanImpMinEnabled" value="enabled">
									<logic:equal name="PlanImpMinVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarPlanImpMin', '<bean:write name="PlanImpMinVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanImpMinVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="eliminarPlanImpMinEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
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
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarPlanImpMinEnabled" name="planAdapterVO" property="agregarPlanImpMinEnabled"/>
						<input type="button" <%=agregarPlanImpMinEnabled%> class="boton" 
							onClick="submitForm('agregarPlanImpMin', '<bean:write name="planAdapterVO" property="plan.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
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
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="gde" key="gde.planDescuento.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planDescuento.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planDescuento.cantidadCuotasPlan.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planDescuento.porDesCap.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planDescuento.porDesAct.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planDescuento.porDesInt.label"/></th>
					</tr>
					<logic:iterate id="PlanDescuentoVO" name="planAdapterVO" property="plan.listPlanDescuento">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="planAdapterVO" property="verPlanDescuentoEnabled" value="enabled">
									<logic:equal name="PlanDescuentoVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verPlanDescuento', '<bean:write name="PlanDescuentoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanDescuentoVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="verPlanDescuentoEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							<!-- Modificar-->								
							<td>
								<logic:equal name="planAdapterVO" property="modificarPlanDescuentoEnabled" value="enabled">
									<logic:equal name="PlanDescuentoVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarPlanDescuento', '<bean:write name="PlanDescuentoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanDescuentoVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="modificarPlanDescuentoEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="planAdapterVO" property="eliminarPlanDescuentoEnabled" value="enabled">
									<logic:equal name="PlanDescuentoVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarPlanDescuento', '<bean:write name="PlanDescuentoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanDescuentoVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="eliminarPlanDescuentoEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
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
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarPlanDescuentoEnabled" name="planAdapterVO" property="agregarPlanDescuentoEnabled"/>
						<input type="button" <%=agregarPlanDescuentoEnabled%> class="boton" 
							onClick="submitForm('agregarPlanDescuento', '<bean:write name="planAdapterVO" property="plan.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
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
			    		<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="gde" key="gde.planIntFin.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planIntFin.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planIntFin.cuotaHasta.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planIntFin.interes.label"/></th>
					</tr>
					<logic:iterate id="PlanIntFinVO" name="planAdapterVO" property="plan.listPlanIntFin">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="planAdapterVO" property="verPlanIntFinEnabled" value="enabled">
									<logic:equal name="PlanIntFinVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verPlanIntFin', '<bean:write name="PlanIntFinVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanIntFinVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="verPlanIntFinEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							<!-- Modificar-->								
							<td>
								<logic:equal name="planAdapterVO" property="modificarPlanIntFinEnabled" value="enabled">
									<logic:equal name="PlanIntFinVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarPlanIntFin', '<bean:write name="PlanIntFinVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanIntFinVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="modificarPlanIntFinEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="planAdapterVO" property="eliminarPlanIntFinEnabled" value="enabled">
									<logic:equal name="PlanIntFinVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarPlanIntFin', '<bean:write name="PlanIntFinVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanIntFinVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="eliminarPlanIntFinEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
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
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarPlanIntFinEnabled" name="planAdapterVO" property="agregarPlanIntFinEnabled"/>
						<input type="button" <%=agregarPlanIntFinEnabled%> class="boton" 
							onClick="submitForm('agregarPlanIntFin', '<bean:write name="planAdapterVO" property="plan.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
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
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="gde" key="gde.planVen.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planVen.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planVen.cuotaHasta.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.vencimiento.label"/></th>
					</tr>
					<logic:iterate id="PlanVenVO" name="planAdapterVO" property="plan.listPlanVen">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="planAdapterVO" property="verPlanVenEnabled" value="enabled">
									<logic:equal name="PlanVenVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verPlanVen', '<bean:write name="PlanVenVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanVenVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="verPlanVenEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							<!-- Modificar-->								
							<td>
								<logic:equal name="planAdapterVO" property="modificarPlanVenEnabled" value="enabled">
									<logic:equal name="PlanVenVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarPlanVen', '<bean:write name="PlanVenVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanVenVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="modificarPlanVenEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="planAdapterVO" property="eliminarPlanVenEnabled" value="enabled">
									<logic:equal name="PlanVenVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarPlanVen', '<bean:write name="PlanVenVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanVenVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="eliminarPlanVenEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
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
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarPlanVenEnabled" name="planAdapterVO" property="agregarPlanVenEnabled"/>
						<input type="button" <%=agregarPlanVenEnabled%> class="boton" 
							onClick="submitForm('agregarPlanVen', '<bean:write name="planAdapterVO" property="plan.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
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
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="gde" key="gde.planAtrVal.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planAtrVal.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.atributo.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planAtrVal.valor.label"/></th>
					</tr>
					<logic:iterate id="PlanAtrValVO" name="planAdapterVO" property="plan.listPlanAtrVal">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="planAdapterVO" property="verPlanAtrValEnabled" value="enabled">
									<logic:equal name="PlanAtrValVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verPlanAtrVal', '<bean:write name="PlanAtrValVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanAtrValVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="verPlanAtrValEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							<!-- Modificar-->								
							<td>
								<logic:equal name="planAdapterVO" property="modificarPlanAtrValEnabled" value="enabled">
									<logic:equal name="PlanAtrValVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarPlanAtrVal', '<bean:write name="PlanAtrValVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanAtrValVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="modificarPlanAtrValEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="planAdapterVO" property="eliminarPlanAtrValEnabled" value="enabled">
									<logic:equal name="PlanAtrValVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarPlanAtrVal', '<bean:write name="PlanAtrValVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanAtrValVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="eliminarPlanAtrValEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="PlanAtrValVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="PlanAtrValVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="PlanAtrValVO" property="atributo.desAtributo"/>&nbsp;</td>
							<td><bean:write name="PlanAtrValVO" property="valor"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="planAdapterVO" property="plan.listPlanAtrVal">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarPlanAtrValEnabled" name="planAdapterVO" property="agregarPlanAtrValEnabled"/>
						<input type="button" <%=agregarPlanAtrValEnabled%> class="boton" 
							onClick="submitForm('agregarPlanAtrVal', '<bean:write name="planAdapterVO" property="plan.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
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
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="gde" key="gde.planExe.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planExe.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="exe" key="exe.exencion.label"/></th>
					</tr>
					<logic:iterate id="PlanExeVO" name="planAdapterVO" property="plan.listPlanExe">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="planAdapterVO" property="verPlanExeEnabled" value="enabled">
									<logic:equal name="PlanExeVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verPlanExe', '<bean:write name="PlanExeVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanExeVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="verPlanExeEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							<!-- Modificar-->								
							<td>
								<logic:equal name="planAdapterVO" property="modificarPlanExeEnabled" value="enabled">
									<logic:equal name="PlanExeVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarPlanExe', '<bean:write name="PlanExeVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanExeVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="modificarPlanExeEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="planAdapterVO" property="eliminarPlanExeEnabled" value="enabled">
									<logic:equal name="PlanExeVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarPlanExe', '<bean:write name="PlanExeVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanExeVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="eliminarPlanExeEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
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
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarPlanExeEnabled" name="planAdapterVO" property="agregarPlanExeEnabled"/>
						<input type="button" <%=agregarPlanExeEnabled%> class="boton" 
							onClick="submitForm('agregarPlanExe', '<bean:write name="planAdapterVO" property="plan.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
			</tbody>
		</table>
		<!-- PlanExe -->
</logic:equal>		
				
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
