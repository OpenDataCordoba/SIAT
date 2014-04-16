<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/cyq/AdministrarLiqFormConvenioEsp.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="cyq" key="cyq.liqFormConvenioAdapter.title"/></h1>	
	
	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>Permite realizar la formalización de un Plan de Pago para un Concurso y Quiebras, a partir de los Registros de Deuda seleccionados.</p>
				<p>Selección del Plan de Pago.</p>
			</td>				
			<td align="right">
	 			<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverAInit', '<bean:write name="liqFormConvenioAdapterVO" property="procedimiento.id" bundle="base" formatKey="general.format.id"/>');">
			  	    <bean:message bundle="base" key="abm.button.volver"/>
				</button>
			</td>
		</tr>
	</table>

	<!-- Procedimiento -->
		<bean:define id="procedimientoVO" name="liqFormConvenioAdapterVO" property="procedimiento"/>
		<%@ include file="/cyq/concursoyQuiebra/includeEncProcedimiento.jsp"%>
	<!-- Procedimiento -->
		
	<br>

	<!-- Lista de Deuda -->	
	<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="cyq" key="cyq.liqFormConvenioAdapter.deudaVerifHomo.label"/></caption>
	      	<tbody>
		       	<tr>
				  	<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
				  	<th align="left"><bean:message bundle="pad" key="pad.cuenta.label"/></th>
				  	<th align="left"><bean:message bundle="cyq" key="cyq.tipoPrivilegioEspecial.label"/></th>
				  	<th align="left"><bean:message bundle="cyq" key="cyq.tipoPrivilegioGeneral.label"/></th>
				  	<th align="left"><bean:message bundle="cyq" key="cyq.tipoPrivilegioQuirografario.label"/></th>
				  	<th align="left"><bean:message bundle="cyq" key="cyq.liqFormConvenioAdapter.total.label"/></th>
				</tr>
				
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqDeudaCyq" name="liqFormConvenioAdapterVO" property="listDeuda">
					<tr>
			  			<!-- Especial -->			  			
			  			<td><bean:write name="LiqDeudaCyq" property="desRecurso"/> </td>
			  			<td><bean:write name="LiqDeudaCyq" property="numeroCuenta"/> </td>
			  			<logic:equal name="LiqDeudaCyq" property="idTipoPrivilegio" value="1">
							<td><b><bean:write name="LiqDeudaCyq" property="importe" bundle="base" formatKey="general.format.currency"/></b></td>
			  				<td>&nbsp;</td><td>&nbsp;</td>
			  			</logic:equal>
			  			<!-- General -->
						<logic:equal name="LiqDeudaCyq" property="idTipoPrivilegio" value="2">
							<td>&nbsp;</td>
							<td><b><bean:write name="LiqDeudaCyq" property="importe" bundle="base" formatKey="general.format.currency"/></b></td>
			  				<td>&nbsp;</td>
			  			</logic:equal>
			  			<!-- Quiro -->
			  			<logic:equal name="LiqDeudaCyq" property="idTipoPrivilegio" value="3">
			  				<td>&nbsp;</td><td>&nbsp;</td>
							<td><b><bean:write name="LiqDeudaCyq" property="importe" bundle="base" formatKey="general.format.currency"/></b></td>
			  			</logic:equal>
						<!-- Total -->
						<td><b><bean:write name="LiqDeudaCyq" property="importe" bundle="base" formatKey="general.format.currency"/></b></td>
			  			
			       	</tr>
				</logic:iterate>
				<!-- Fin Item LiqDeudaVO -->
		       	<tr>       		
		       		<td class="celdatotales" align="right" colspan="2">
			        	<bean:message bundle="cyq" key="cyq.liqDeudaCyqAdapter.totales.label"/>: 
			        </td>
			        <td class="celdatotales">
			        	<b><bean:write name="liqFormConvenioAdapterVO" property="totalGeneral" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
			        <td class="celdatotales">
			        	<b><bean:write name="liqFormConvenioAdapterVO" property="totalEspecial" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
			        <td class="celdatotales">
			        	<b><bean:write name="liqFormConvenioAdapterVO" property="totalQuirografario" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
			        <td class="celdatotales">
			        	<b><bean:write name="liqFormConvenioAdapterVO" property="total" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       	</tr>
	      	</tbody>
		</table>
	</div>	
	<!-- Fin Lista de Deuda -->
	
	<p>
		(*) Deuda actualizada a la Fecha de Formalización del Plan, sin considerar descuentos ni actualizaciones especiales.
	</p>
	
	<!-- Planes Vigentes -->
	<logic:notEmpty name="liqFormConvenioAdapterVO" property="listPlan">
		<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption>
	    		<bean:message bundle="gde" key="gde.liqFormConvenioAdapter.planesVigentes.title"/>
	    		<bean:write name="liqFormConvenioAdapterVO" property="fechaFormalizacionView" />
	    	</caption>
	      	<tbody>
		       	<tr>
		       		<th align="left"></th>
		       		<th align="left"></th>
		       		<th align="left"></th>
				  	<th align="left">Plan</th>
				  	<th align="left">Beneficios</th>
				</tr>
				
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqPlanVO" name="liqFormConvenioAdapterVO" property="listPlan">
					<tr>
						<!-- Habilitado -->
						<logic:equal name="LiqPlanVO" property="esSeleccionable" value="true">
							<td>
								<input type="radio" name="idPlan" value="<bean:write name="LiqPlanVO" property="idPlan" bundle="base" formatKey="general.format.id"/>"/>
							</td>
							<td>
								<a style="cursor: pointer;" onclick="submitForm('verDetallePlan', '<bean:write name="LiqPlanVO" property="idPlan" bundle="base" formatKey="general.format.id"/>');">
									<img title="Ver Plan de Pago" src="<%=request.getContextPath()%>/images/iconos/selec0.gif" border="0"/>
				        		</a>
				        	</td>
				        	<td>
				        		<logic:notEqual name="LiqPlanVO" property="linkNormativa" value="">
									<a style="cursor: pointer;" 
										href="<bean:write name="LiqPlanVO" property="linkNormativa"/>" target="blank">
										<img title="Ver Normativa" src="<%=request.getContextPath()%>/images/iconos/normativa0.gif" border="0"/>
					        		</a>
					        	</logic:notEqual>	
					        	<logic:equal name="LiqPlanVO" property="linkNormativa" value="">
									<img title="No se encontr&oacute; la Normativa" src="<%=request.getContextPath()%>/images/iconos/normativa0.gif" border="0"/>					        		
					        	</logic:equal>
				        	</td>
							<td>
								<b><bean:write name="LiqPlanVO" property="desPlan"/></b>
							</td>
							<td>
								<bean:write name="LiqPlanVO" property="leyendaPlan"/>
							</td>
						</logic:equal>
						<!-- Deshabilitado -->
						<logic:notEqual name="LiqPlanVO" property="esSeleccionable" value="true">
							<td style="background-color: rgb(192, 192, 192);">
								<input type="radio" name="plan" value="0" disabled="disabled"/>
							</td>
							<td style="background-color: rgb(192, 192, 192);">
								<a style="cursor: pointer;" onclick="submitForm('verDetallePlan', '<bean:write name="LiqPlanVO" property="idPlan" bundle="base" formatKey="general.format.id"/>');">
									<img title="Ver Plan de Pago" src="<%=request.getContextPath()%>/images/iconos/selec0.gif" border="0"/>
				        		</a>
				        	</td>
				        	<td style="background-color: rgb(192, 192, 192);">
								<a style="cursor: pointer;" 
									href="<bean:write name="LiqPlanVO" property="linkNormativa"/>" target="blank">
									<img title="Ver Normativa" src="<%=request.getContextPath()%>/images/iconos/normativa0.gif" border="0"/>
				        		</a>				        	
				        	</td>
							<td style="background-color: rgb(192, 192, 192);">
								<b><bean:write name="LiqPlanVO" property="desPlan"/></b>
							</td>
							
							<td>
								<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
									<tr>	
										<td style="background-color: rgb(192, 192, 192);">
											<bean:write name="LiqPlanVO" property="leyendaPlan"/>
										</td>
									</tr>
									<tr>
										<td style="background-color: rgb(192, 192, 192);">
											<ul class="vinieta">	
	      										<li>
													<bean:write name="LiqPlanVO" property="msgDeshabilitado"/> 
	      										</li>
		      								</ul>
										</td>
									</tr>
								</table>
							</td>
						</logic:notEqual>						
					</tr>
				</logic:iterate>	
			</tbody>
		</table>
		</div>
	</logic:notEmpty>
	
	<logic:empty name="liqFormConvenioAdapterVO" property="listPlan">
		<p>
			No hay planes disponibles para los registros de deuda seleccionados
		</p>
	</logic:empty>
	<!-- Fin Planes Vigentes -->
	
	<!--  boton Seleccionar Plan -->
	<logic:notEmpty name="liqFormConvenioAdapterVO" property="listPlan">
		<p align="right">
			<button type="button" name="btnFormConvenioar" class="boton" onclick="submitForm('alternativaCuotas', '');"
								<bean:write name="liqFormConvenioAdapterVO" property="alternativasCuotasEnabled"/>>
		  	    <bean:message bundle="gde" key="gde.liqFormConvenioAdapter.button.alternativaCuotas"/>
			</button>
		</p>
	</logic:notEmpty>
	<!--  FIN boton Seleccionar Plan -->
			
	<!-- Volver -->
	<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverAInit', '<bean:write name="liqFormConvenioAdapterVO" property="procedimiento.id" bundle="base" formatKey="general.format.id"/>');">
  	    <bean:message bundle="base" key="abm.button.volver"/>
	</button>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->