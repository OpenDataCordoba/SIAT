<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/cyq/AdministrarLiqDeudaCyq.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="cyq" key="cyq.liqDeudaCyqAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>

	<!-- Procedimiento -->
		<bean:define id="procedimientoVO" name="liqDeudaCyqAdapterVO" property="procedimiento"/>
		<%@ include file="/cyq/concursoyQuiebra/includeEncProcedimiento.jsp"%>
	<!-- Procedimiento -->
	
	<br>
	
		<fieldset>
				<legend><bean:message bundle="gde" key="gde.liqDeudaAdapter.conveniosAsociados.title"/></legend>
		
				<logic:notEmpty name="liqDeudaCyqAdapterVO" property="listConvenio" >
					
					<logic:iterate id="Convenio" name="liqDeudaCyqAdapterVO" property="listConvenio">
						<dd>
							<!-- Permitido ver Convenios Asociados  -->
							<logic:equal name="liqDeudaCyqAdapterVO" property="verConvenioEnabled" value="true">
					      		<a href="/siat/cyq/AdministrarLiqDeudaCyq.do?method=verConvenio&selectedId=<bean:write name="Convenio" property="idConvenio" bundle="base" formatKey="general.format.id"/>" >
						      		<bean:write name="Convenio" property="nroConvenio"/> -
						      		<bean:write name="Convenio" property="desPlan"/>
						      	</a>
							</logic:equal>
							<!-- No Permitido ver Convenios Asociados  -->
							<logic:notEqual name="liqDeudaCyqAdapterVO" property="verConvenioEnabled" value="true">
								<bean:write name="Convenio" property="nroConvenio"/> -
						      	<bean:write name="Convenio" property="desPlan"/>
							</logic:notEqual>
						</dd>
					</logic:iterate>
			</logic:notEmpty>		

		</fieldset>
	
	
	<br>
		
	<!-- Lista de Deuda -->	
	<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="cyq" key="cyq.liqDeudaCyqAdapter.blokeDeuda.title"/></caption>
	      	<tbody>
		       	<tr>
		       		<th align="center">
		       			<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.seleccionar"/>
		       			<logic:equal name="liqDeudaCyqAdapterVO" property="mostrarChkAll" value="true">
			       			<br>
			       			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			       			<input type="checkbox" onclick="changeChk('filter', 'listIdDeudaSelected', this)"/>
			       		</logic:equal>
		       		</th>
				  	<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
				  	<th align="left"><bean:message bundle="pad" key="pad.cuenta.label"/></th>
				  	<th align="left"><bean:message bundle="cyq" key="cyq.tipoPrivilegioEspecial.label"/></th>
				  	<th align="left"><bean:message bundle="cyq" key="cyq.tipoPrivilegioGeneral.label"/></th>
				  	<th align="left"><bean:message bundle="cyq" key="cyq.tipoPrivilegioQuirografario.label"/></th>
				  	<th align="left"><bean:message bundle="cyq" key="cyq.deudaPrivilegio.saldo.label"/></th>
				</tr>
				
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqDeudaCyq" name="liqDeudaCyqAdapterVO" property="listDeuda">
					<tr>
						<!-- Seleccion de deuda para su operacion -->
			  			<td align="center">
			  				<logic:equal name="LiqDeudaCyq" property="esSeleccionable" value="true">
			  					<html:multibox name="liqDeudaCyqAdapterVO" property="listIdDeudaSelected" >
                                	<bean:write name="LiqDeudaCyq" property="idDeuda" bundle="base" formatKey="general.format.id"/>
                                </html:multibox>
			  				</logic:equal>
			  				<logic:notEqual name="LiqDeudaCyq" property="esSeleccionable" value="true">
			  					<input type="checkbox" disabled="disabled"/>
			  				</logic:notEqual>
			  			</td>
			  			
			  			<td><bean:write name="LiqDeudaCyq" property="desRecurso"/> </td>
			  			<td><bean:write name="LiqDeudaCyq" property="numeroCuenta"/> </td>

						<logic:notEqual name="LiqDeudaCyq" property="poseeConvenio" value="true">
				  			<!-- Especial -->			  			
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
				  			<td><b><bean:write name="LiqDeudaCyq" property="saldo" bundle="base" formatKey="general.format.currency"/></b></td>
			       		</logic:notEqual>
			       		
						<logic:equal name="LiqDeudaCyq" property="poseeConvenio" value="true"> 
							<td colspan="4"><b><bean:write name="LiqDeudaCyq" property="observacion"/></b>&nbsp;</td>
						</logic:equal>			       		
			       	
			       	</tr>
				</logic:iterate>
				<!-- Fin Item LiqDeudaVO -->
		       	<tr>       		
		       		<td colspan="3" class="celdatotales" align="right">
			        	<bean:message bundle="cyq" key="cyq.liqDeudaCyqAdapter.totales.label"/>: 
			        </td>
			        <td class="celdatotales">
			        	<b><bean:write name="liqDeudaCyqAdapterVO" property="totalGeneral" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
			        <td class="celdatotales">
			        	<b><bean:write name="liqDeudaCyqAdapterVO" property="totalEspecial" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
			        <td class="celdatotales">
			        	<b><bean:write name="liqDeudaCyqAdapterVO" property="totalQuirografario" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
			        <td class="celdatotales">
			        	<b><bean:write name="liqDeudaCyqAdapterVO" property="totalSaldo" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       	</tr>
	      	</tbody>
		</table>
	</div>	
	<!-- Fin Lista de Deuda -->
	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"> 
				<input type="button" class="boton"  onClick="submitForm('agregarDeuda', 
					'<bean:write name="liqDeudaCyqAdapterVO" property="procedimiento.id" bundle="base" formatKey="general.format.id"/>');" 
					value="<bean:message bundle="base" key="abm.button.agregar"/>"/>		
			</td>
		</tr>
	</table>
	
	<br>

		<!-- Pagos -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="cyq" key="cyq.liqDeudaCyqAdapter.blokePago.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="liqDeudaCyqAdapterVO" property="listPago">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th align="left"><bean:message bundle="cyq" key="cyq.pagoPriv.tipoCancelacion.label"/></th>
						<th align="left"><bean:message bundle="cyq" key="cyq.pagoPriv.descripcion.label"/></th>
						<th align="left"><bean:message bundle="cyq" key="cyq.pagoPriv.fecha.label"/></th>
						<th align="left"><bean:message bundle="cyq" key="cyq.pagoPriv.importe.label"/></th>						
					</tr>
					<logic:iterate id="PagoPrivVO" name="liqDeudaCyqAdapterVO" property="listPago">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="liqDeudaCyqAdapterVO" property="verPagoPrivEnabled" value="enabled">
									<logic:equal name="PagoPrivVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verPagoPriv', '<bean:write name="PagoPrivVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PagoPrivVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="liqDeudaCyqAdapterVO" property="verPagoPrivEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							
							<td><bean:write name="PagoPrivVO" property="tipoCancelacion.value"/>&nbsp;</td>
							<td><bean:write name="PagoPrivVO" property="descripcion"/>&nbsp;</td>
							<td><bean:write name="PagoPrivVO" property="fechaView"/>&nbsp;</td>
							<td><bean:write name="PagoPrivVO" property="importeView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="liqDeudaCyqAdapterVO" property="listPago">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- Pagos -->

	<br>

	<fieldset>
		<table class="tablabotones" width="100%">
			<tr>
				<td align="center"> 
					<input type="button" class="boton"  onClick="submitForm('registrarPago', 
						'<bean:write name="liqDeudaCyqAdapterVO" property="procedimiento.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="cyq" key="cyq.liqDeudaCyqAdapter.registrarPago.button"/>"/>		
				
					<input type="button" class="boton"  onClick="submitForm('formalizarConvenio', 
						'<bean:write name="liqDeudaCyqAdapterVO" property="procedimiento.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="cyq" key="cyq.liqDeudaCyqAdapter.formalizarConvenio.button"/>"/>
						
					<input type="button" class="boton"  onClick="submitForm('formalizarConvenioEsp', 
						'<bean:write name="liqDeudaCyqAdapterVO" property="procedimiento.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="cyq" key="cyq.liqDeudaCyqAdapter.formalizarConvenioEsp.button"/>"/>		
				</td>
			</tr>
		</table>	
	</fieldset>
	
	<!-- Volver -->	
	<button type="button" name="btnVolver" class="boton" onclick="submitForm('volver', '');">
  	    <bean:message bundle="base" key="abm.button.volver"/>
	</button>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->