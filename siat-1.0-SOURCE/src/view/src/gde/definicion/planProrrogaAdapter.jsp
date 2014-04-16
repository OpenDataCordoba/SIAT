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
					<td><label><bean:message bundle="def" key="def.viaDeuda.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.viaDeuda.desViaDeuda"/></td>					
				</tr>
				<tr>	
					<td><label><bean:message bundle="bal" key="bal.sistema.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.sistema.desSistema"/></td>					
				</tr>
				<tr>	
					<td><label><bean:message bundle="gde" key="gde.plan.esManual.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.esManual.value"/></td>					
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.fechaAlta.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.fechaAltaView" /></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.fecVenDeuDes.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.fecVenDeuDesView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.fecVenDeuHas.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.fecVenDeuHasView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.impMinDeu.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.impMinDeuView" /></td>			
				</tr>
				<tr>	
					<td><label><bean:message bundle="gde" key="gde.plan.aplicaTotalImpago.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.aplicaTotalImpago.value"/></td>					
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.impMinCuo.label"/>: </label></td>
					<td class="normal"><bean:write name="planAdapterVO" property="plan.impMinCuoView" /></td>			
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
			</table>
		</fieldset>	
		<!-- Plan -->
		
		<!-- PlanProrroga -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="gde" key="gde.plan.listPlanProrroga.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="planAdapterVO" property="plan.listPlanProrroga">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="gde" key="gde.planProrroga.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planProrroga.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planProrroga.desPlanProrroga.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planProrroga.fecVto.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.planProrroga.fecVtoNue.label"/></th>
					</tr>
					<logic:iterate id="PlanProrrogaVO" name="planAdapterVO" property="plan.listPlanProrroga">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="planAdapterVO" property="verPlanProrrogaEnabled" value="enabled">
									<logic:equal name="PlanProrrogaVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verPlanProrroga', '<bean:write name="PlanProrrogaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanProrrogaVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="verPlanProrrogaEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Modificar-->								
							<td>
								<logic:equal name="planAdapterVO" property="modificarPlanProrrogaEnabled" value="enabled">
									<logic:equal name="PlanProrrogaVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarPlanProrroga', '<bean:write name="PlanProrrogaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanProrrogaVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="modificarPlanProrrogaEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="planAdapterVO" property="eliminarPlanProrrogaEnabled" value="enabled">
									<logic:equal name="PlanProrrogaVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarPlanProrroga', '<bean:write name="PlanProrrogaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanProrrogaVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="planAdapterVO" property="eliminarPlanProrrogaEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="PlanProrrogaVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="PlanProrrogaVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="PlanProrrogaVO" property="desPlanProrroga" />&nbsp;</td>
							<td><bean:write name="PlanProrrogaVO" property="fecVtoView"/>&nbsp;</td>
							<td><bean:write name="PlanProrrogaVO" property="fecVtoNueView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="planAdapterVO" property="plan.listPlanProrroga">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarEnabled" name="planAdapterVO" property="agregarPlanProrrogaEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregarPlanProrroga', '<bean:write name="planAdapterVO" property="plan.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
			</tbody>
		</table>
		<!-- PlanProrroga -->
				
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
