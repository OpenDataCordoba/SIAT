<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarLiqFormConvenio.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<!-- Para la web lo muestro, para usuario Interno NO -->
	<logic:equal name="userSession" property="isAnonimoView" value="1">
		<%@ include file="/gde/gdeuda/includeDivButtons.jsp" %>
	</logic:equal>	
		
	<h1><bean:message bundle="gde" key="gde.liqFormConvenioAdapter.title"/></h1>	
	
	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>Permite realizar la formalización de un Plan de Pago para un Contribuyente, a partir de los Registros de Deuda seleccionados.</p>
				<p>Selección del Plan de Pago.</p>
			</td>				
			<td align="right">
	 			<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverAInit', '<bean:write name="liqFormConvenioAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
			  	    <bean:message bundle="base" key="abm.button.volver"/>
				</button>
			</td>
		</tr>
	</table>
	<!-- LiqCuenta -->
		<bean:define id="DeudaAdapterVO" name="liqFormConvenioAdapterVO"/>
		<%@ include file="/gde/gdeuda/includeLiqCuenta.jsp" %>
	<!-- LiqCuenta -->
	
	<!-- listDeuda -->
	<logic:notEmpty name="liqFormConvenioAdapterVO" property="listDeuda">
		<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="gde" key="gde.liqFormConvenioAdapter.listaDeudas.title"/></caption>
	      	<tbody>
		       	<tr>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqFormConvenioAdapter.liqDeuda.periodoDeuda"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqFormConvenioAdapter.liqDeuda.fechaVto"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqFormConvenioAdapter.liqDeuda.importe"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqFormConvenioAdapter.liqDeuda.actualizacion"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqFormConvenioAdapter.liqDeuda.total"/></th>
				</tr>
				
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqDeudaVO" name="liqFormConvenioAdapterVO" property="listDeuda">
					<tr>
			  			<!-- Ver detalle Deuda -->
			  			<td><bean:write name="LiqDeudaVO" property="periodoDeuda"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaVO" property="fechaVto"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaVO" property="saldo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaVO" property="actualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaVO" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
			       	</tr>
				</logic:iterate>
				<!-- Fin Item LiqDeudaVO -->
		       	<tr>       		
		       		<td colspan="5" align="right">
			        	<bean:message bundle="gde" key="gde.liqFormConvenioAdapter.total.label"/>: 
			        	<b><bean:write name="liqFormConvenioAdapterVO" property="total" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       	</tr>
	      	</tbody>
		</table>
		</div>
	</logic:notEmpty>
	<!-- Fin listDeuda -->
	
	<p>
		(*) Deuda actualizada a la Fecha de Formalización del Plan, sin considerar descuentos ni actualizaciones especiales.
	</p>
	
	<!-- Planes Vigentes -->
	<logic:notEmpty name="liqFormConvenioAdapterVO" property="listPlan">
		<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption>
	    		<bean:message bundle="gde" key="gde.liqFormConvenioAdapter.planesVigentes.title"/>
	    		<bean:write name="liqFormConvenioAdapterVO" property="fechaFormSelected" />
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
	<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverAInit', '<bean:write name="liqFormConvenioAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
  	    <bean:message bundle="base" key="abm.button.volver"/>
	</button>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->