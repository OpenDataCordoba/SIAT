<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarLiqDeuda.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<!-- Para la web lo muestro, para usuario Interno NO -->
	<logic:equal name="userSession" property="isAnonimoView" value="1">
		<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
			<%@ include file="/gde/gdeuda/includeDivButtons.jsp" %>
		</logic:notEqual>
		<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
			<%@ include file="/gde/gdeuda/includeDivButtonsAuto.jsp" %>
		</logic:equal>
	</logic:equal>
	
	<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
		<h1><bean:message bundle="gde" key="gde.liqDeudaAdapter.autoTitle"/></h1>
	</logic:equal>
	<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
		<h1><bean:message bundle="gde" key="gde.liqDeudaAdapter.title"/></h1>
	</logic:notEqual>	

	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
					<p>
						<%=ar.gov.rosario.siat.fra.iface.util.Frase.getInstance().getMessageBykey("gde.liquidacionDeuda.leyendaEncabezado")%>
					</p>

			</td>				
			<td align="right">
	 			<logic:notEqual name="userSession" property="isAnonimoView" value="1">
					<!-- Volver -->
					<button type="button" name="btnVolver" class="boton" onclick="submitForm('volver', '');">
				  	    <bean:message bundle="base" key="abm.button.volver"/>
					</button>
				</logic:notEqual>
			</td>
		</tr>
	</table>
	
	
	<!-- Filtros aplicado para autoliquidables -->
	<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.liqDeudaAdapter.filtrosAplicados.title"/> </legend>
			<p>
				<label><bean:message bundle="gde" key="gde.liqDeudaIngresoGRAdapter.periodo.label"/>: </label>
				<bean:write name="liqDeudaAdapterVO" property="cuenta.estadoPeriodo.value"/>
				&nbsp;&nbsp;&nbsp;
				<label><bean:message bundle="gde" key="gde.liqDeudaIngresoGRAdapter.clasificacionDeuda.label"/>: </label>
				<bean:write name="liqDeudaAdapterVO" property="cuenta.recClaDeu.desClaDeu"/>
			</p>
			
			<p>
				<label><bean:message bundle="gde" key="gde.liqDeudaIngresoGRAdapter.fechaVtoDesde.label"/>: </label>
				<bean:write name="liqDeudaAdapterVO" property="cuenta.fechaVtoDesdeView"/>
				&nbsp;
				<label><bean:message bundle="gde" key="gde.liqDeudaIngresoGRAdapter.fechaVtoHasta.label"/>: </label>
				<bean:write name="liqDeudaAdapterVO" property="cuenta.fechaVtoHastaView"/>
			</p>
		</fieldset>
	</logic:equal>
	<!-- Fin filtros aplicado para autoliquidables -->
	
	<!-- LiqCuenta -->
		<bean:define id="DeudaAdapterVO" name="liqDeudaAdapterVO"/>
		<%@ include file="/gde/gdeuda/includeLiqCuenta.jsp" %>
	<!-- LiqCuenta -->
	
	
	<!-- CuentasRel -->
	<logic:notEmpty name="liqDeudaAdapterVO" property="listCuentaRel" >
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuentasRelacionadas.title"/> </legend>			
			<ul>
				<logic:iterate id="CuentaRel" name="liqDeudaAdapterVO" property="listCuentaRel">
					<li>
						<!-- Permitido ver Cuentas Relacionadas al Objeto Imponible -->
						<logic:equal name="liqDeudaAdapterVO" property="verCuentaRelEnabled" value="true">
				      		<a href="/siat/gde/AdministrarLiqDeuda.do?method=verCuentaRel&selectedId=<bean:write name="CuentaRel" property="idCuenta" bundle="base" formatKey="general.format.id"/>" >
					      		<bean:write name="CuentaRel" property="nroCuenta"/> -
					      		<bean:write name="CuentaRel" property="desCategoria"/> -
					      		<bean:write name="CuentaRel" property="desRecurso"/>
				      		</a>
				      	</logic:equal>
				      	<logic:notEqual name="liqDeudaAdapterVO" property="verCuentaRelEnabled" value="true">
				      		<bean:write name="CuentaRel" property="nroCuenta"/> -
					      		<bean:write name="CuentaRel" property="desCategoria"/> -
					      		<bean:write name="CuentaRel" property="desRecurso"/>
				      	</logic:notEqual>
					</li>
				</logic:iterate>
			</ul>		
		</fieldset>
	</logic:notEmpty>
	<!-- Fin CuentasRel -->
	
	<!-- Exencion - Caso Solical - Otro -->
		<!--  Utilizamos en bean definido para la inclucion de liqCuenta -->
		<%@ include file="/gde/gdeuda/includeExenciones.jsp" %>
	<!-- Fin Exencion - Caso Solical - Otro -->
	
	
	<!-- Bloque ver historicos  -->
	<logic:equal name="liqDeudaAdapterVO" property="poseePermisoAHistorico" value="true">
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.liqDeudaAdapter.historicos.title"/> </legend>		
			
			<p align="center">

				<!-- Ver Detalle de Objeto Imponible -->
				<logic:equal name="DeudaAdapterVO" property="verDetalleObjImpEnabled" value="true">
					<button type="button" name="btnVerObjImp" class="boton" onclick="submitForm('verDetalleObjImp', '<bean:write name="DeudaAdapterVO" property="cuenta.idObjImp" bundle="base" formatKey="general.format.id"/>');">
			  			<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.verDetalleObjImp"/>
					</button>
					<html:hidden name="DeudaAdapterVO" property="cuenta.idCuenta" />
				</logic:equal>
				
				<logic:notEqual name="DeudaAdapterVO" property="verDetalleObjImpEnabled" value="true">
					<button type="button" name="btnVerObjImp" class="botonDeshabilitado" >
			  			<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.verDetalleObjImp"/>
					</button>
				</logic:notEqual>
			
				<!-- ver Historico de Contribuyentes -->
				<logic:equal name="DeudaAdapterVO" property="verHistoricoContribEnabled" value="true">
					<button type="button" name="btnVerHistoricoContrib" class="boton" onclick="submitForm('verHistoricoContrib', '<bean:write name="DeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
			  			<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.verHistoricoContrib"/>
					</button>
				</logic:equal>
			
				<logic:notEqual name="DeudaAdapterVO" property="verHistoricoContribEnabled" value="true">
					<button type="button" name="btnVerObjImp" class="botonDeshabilitado" >
			  			<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.verHistoricoContrib"/>
					</button>
				</logic:notEqual>
			
				<!-- ver Historico de Exenciones -->
				<logic:equal name="DeudaAdapterVO" property="verHistoricoExeEnabled" value="true">
					<button type="button" name="btnVerHistoricoExe" class="boton" onclick="submitForm('verHistoricoExe', '<bean:write name="DeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>' );">
				  	    <bean:message bundle="gde" key="gde.liqDeudaAdapter.button.verHistoricoExe"/>
					</button>
				</logic:equal>
				
				<logic:notEqual name="DeudaAdapterVO" property="verHistoricoExeEnabled" value="true">
					<button type="button" name="btnVerObjImp" class="botonDeshabilitado" >
			  			<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.verHistoricoExe"/>
					</button>
				</logic:notEqual>
				
			</p>
		</fieldset>
	</logic:equal>
	<!-- Fin Bloque ver historicos  -->
	
	<!-- Concurso y Quiebra -->
	<logic:notEmpty name="liqDeudaAdapterVO" property="listProcedimientoCyQ">
		<div class="horizscroll">
	    <table class="tableDeuda" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockCyQ.title"/></caption>
	      	<tbody>
	      	<!-- LiqProcedimientoCyQVO -->
	       	<logic:iterate id="Procedimiento" name="liqDeudaAdapterVO" property="listProcedimientoCyQ">
		       	<tr>
		       		
		       		<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
		       			<td colspan="6">
		       		</logic:equal>
		       		<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
		       			<td colspan="4">
		       		</logic:notEqual>
		       			<b><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockCyQ.nroProcedimiento.label"/>:</b> 
		       			<bean:write name="Procedimiento" property="nroProcedimiento"/>
		       		</td>

		       		<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
		       			<td colspan="6">
		       		</logic:equal>
		       		<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
		       			<td colspan="4">
		       		</logic:notEqual>
		       			<b><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockCyQ.fechaActualizDeu.label"/>:</b> 
		       			<bean:write name="Procedimiento" property="fechaActualizacion"/>
		       		</td>
		       	</tr>
		       	<tr>
		       		<th align="center">
		       			<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.seleccionar"/> 
		       			<logic:equal name="Procedimiento" property="mostrarChkAllCyQ" value="true">
			       			<br>
				       		<input type="checkbox" onclick="changeChkProcurador('filter', 'listIdDeudaSelected', this, 'procedimiento<bean:write name="Procedimiento" property="idProcedimiento" bundle="base" formatKey="general.format.id"/>');"/>
			       		</logic:equal>	
		       		</th>
		       		
		       		<th align="left">&nbsp;</th>
		       		<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.codRefPag"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.periodoDeuda"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.fechaVto"/></th>
				  	
				  	<!-- Para recursos autoliquidables -->
				  	<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				  		<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.declarado"/></th>
				  		<th class="separadorAuto">&nbsp;</th>
				  		<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.fechaPago"/></th>
				  		<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.importePago"/></th>
				  		<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.saldo"/></th>
				  		<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion"/></th>
				  		<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total"/></th>
				  	</logic:equal>
				  	
				  	<!-- Para recursos NO autoliquidables -->
				  	<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				  		<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.importe"/></th>
				  		<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion"/></th>
				  		<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total"/></th>
				  	</logic:notEqual>
				  	
				</tr>
				
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqDeudaCYQ" name="Procedimiento" property="listDeuda">
					<tr>
						<!-- Seleccion de deuda para su operacion -->
			  			<td align="center">
			  				<logic:equal name="LiqDeudaCYQ" property="esSeleccionable" value="true">
                                <input type="checkbox" 
                                		name="listIdDeudaSelected" 
                                		id="procedimiento<bean:write name="Procedimiento" property="idProcedimiento" bundle="base" formatKey="general.format.id"/>"
                                		value="<bean:write name="LiqDeudaCYQ" property="idSelect"/>"
                                		title="Seleccionar"/>
                                
			  				</logic:equal>
			  				<logic:notEqual name="LiqDeudaCYQ" property="esSeleccionable" value="true">
			  					<input type="checkbox" disabled="disabled"/>
			  				</logic:notEqual>
			  			</td>
			  			<!-- Ver detalle Deuda -->
			  			<td>
			  				<logic:equal name="LiqDeudaCYQ" property="verDetalleDeudaEnabled" value="true">
				  				<a style="cursor: pointer;" 
				  					href="<bean:write name="LiqDeudaCYQ" property="linkVerDetalle"/>">
						        	<img title="Ver" src="<%=request.getContextPath()%>/images/iconos/selec0.gif" border="0"/>
						        </a>
			  				</logic:equal>
			  				<logic:notEqual name="LiqDeudaCYQ" property="verDetalleDeudaEnabled" value="true">
			  					<img title="Ver" src="<%=request.getContextPath()%>/images/iconos/selec1.gif" border="0"/>
			  				</logic:notEqual>
			  			</td>
	
						<td><bean:write name="LiqDeudaCYQ" property="codRefPagView"/>&nbsp;</td>
			  			<td><bean:write name="LiqDeudaCYQ" property="periodoDeuda"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaCYQ" property="fechaVto"/>&nbsp;</td>
				        
				        <logic:notEqual name="LiqDeudaCYQ" property="poseeConvenio" value="true">
						
							<!-- Para recursos autoliquidables -->
				        	<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
					        	<td><bean:write name="LiqDeudaCYQ" property="importeCurrencyView" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
								<td class="separadorAuto">&nbsp;</td>
					        	<td><bean:write name="LiqDeudaCYQ" property="fechaPago"/>&nbsp;</td>
								<td><bean:write name="LiqDeudaCYQ" property="importePago" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
								<td><bean:write name="LiqDeudaCYQ" property="saldoCurrencyView"/>&nbsp;</td>
						        <td><bean:write name="LiqDeudaCYQ" property="actualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        <td><bean:write name="LiqDeudaCYQ" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        </logic:equal>

							<!-- Para recursos NO autoliquidables -->
							<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">	
						        <td><bean:write name="LiqDeudaCYQ" property="saldoCurrencyView" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        <td><bean:write name="LiqDeudaCYQ" property="actualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        <td><bean:write name="LiqDeudaCYQ" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				       		</logic:notEqual>
						
						</logic:notEqual>
				        
				        <logic:equal name="LiqDeudaCYQ" property="poseeConvenio" value="true">
				       		<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				       			<td colspan="8">
				       		</logic:equal>
				       		<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				       			<td colspan="4">
				       		</logic:notEqual>
				       			<logic:equal name="liqDeudaAdapterVO" property="verConvenioEnabled" value="true">
					       			<a href="/siat/gde/AdministrarLiqDeuda.do?method=verConvenio&selectedId=<bean:write name="LiqDeudaCYQ" property="idLink" bundle="base" formatKey="general.format.id"/>&cuentaId=<bean:write name="liqDeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>" >
					       				<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockJudicial.nroConvenio.label"/>:
					       				<bean:write name="LiqDeudaCYQ" property="observacion"/>
					       			</a>
					       		</logic:equal>
					       		<logic:notEqual name="liqDeudaAdapterVO" property="verConvenioEnabled" value="true">
					       			<b><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockJudicial.nroConvenio.label"/>:
					       			<bean:write name="LiqDeudaCYQ" property="observacion"/></b>
					       		</logic:notEqual>
				       		</td>
					    </logic:equal>
					       				
			       	</tr>
				</logic:iterate>
				<!-- Fin Item LiqDeudaVO -->				
	       	</logic:iterate>
	       	<!-- Fin LiqProcedimientoCyQVO -->
	      	</tbody>
		</table>
		</div>
	</logic:notEmpty>
	<!-- Fin Concurso y Quiebra -->
	
	<!-- Deuda en Gestion Judicial -->
	<logic:notEmpty name="liqDeudaAdapterVO" property="listProcurador">
		<div class="horizscroll">
	    <table class="tableDeuda" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockJudicial.title"/></caption>
	      	<tbody>
	      	<!-- LiqProcuradorVO -->
	       	<logic:iterate id="Procurador" name="liqDeudaAdapterVO" property="listProcurador">
		       	<tr>
		       		
		       		<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
		       			<td colspan="13">
		       		</logic:equal>
		       		<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
		       			<td colspan="9">
		       		</logic:notEqual>
		       		
		       			<b><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockJudicial.procurador.label"/>:</b> 
		       			<bean:write name="Procurador" property="idProcuradorView"/> - <bean:write name="Procurador" property="desProcurador"/>
		       		</td>		       		
		       	</tr>
		       	<tr>
		       		<th align="center" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.seleccionar.label"/>'>
		       			<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.seleccionar"/> 
		       			<logic:equal name="Procurador" property="mostrarChkAllJudicial" value="true">
			       			<br>
				       		<input type="checkbox" onclick="changeChkProcurador('filter', 'listIdDeudaSelected', this, 'procurador<bean:write name="Procurador" property="idProcurador" bundle="base" formatKey="general.format.id"/>');"/>
			       		</logic:equal>	
		       		</th>
		       		<th align="left">&nbsp;</th>
					<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.nroPlanilla.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.nroPlanilla"/></th>				  	
				  	<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.codRefPag.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.codRefPag"/></th>
				  	<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.periodoDeuda.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.periodoDeuda"/></th>
				  	<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.fechaVto.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.fechaVto"/></th>
				  	
				  	<!-- Para recursos autoliquidables -->
				  	<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				  		<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.declarado.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.declarado"/></th>
				  		<th class="separadorAuto">&nbsp;</th>
				  		<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.fechaPago.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.fechaPago"/></th>
				  		<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.importePago.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.importePago"/></th>
				  		<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.saldo.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.saldo"/></th>
				  		<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion"/></th>
				  		<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total"/></th>
				  	</logic:equal>
				  	
				  	<!-- Para recursos NO autoliquidables -->
				  	<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				  		<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.importe.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.importe"/></th>
				  		<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion"/></th>
				  		<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total"/></th>
				  	</logic:notEqual>
				  	
				</tr>
				
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqDeudaJud" name="Procurador" property="listDeuda">
					<tr>
						<!-- Seleccion de deuda para su operacion -->
			  			<td align="center">
			  				<logic:equal name="LiqDeudaJud" property="esSeleccionable" value="true">
                                <input type="checkbox" 
                                		name="listIdDeudaSelected" 
                                		id="procurador<bean:write name="Procurador" property="idProcurador" bundle="base" formatKey="general.format.id"/>"
                                		value="<bean:write name="LiqDeudaJud" property="idSelect"/>"
                                		title="Seleccionar"/>
                                
			  				</logic:equal>
			  				<logic:notEqual name="LiqDeudaJud" property="esSeleccionable" value="true">
			  					<input type="checkbox" disabled="disabled"/>
			  				</logic:notEqual>
			  			</td>
			  			<!-- Ver detalle deuda -->
			  			<td>
					     	<logic:equal name="LiqDeudaJud" property="verDetalleDeudaEnabled" value="true">
						        <a style="cursor: pointer;" 
				  					href="<bean:write name="LiqDeudaJud" property="linkVerDetalle"/>">
						        	<img title="Ver" src="<%=request.getContextPath()%>/images/iconos/selec0.gif" border="0"/>
						        </a>
							</logic:equal>
							<logic:notEqual name="LiqDeudaJud" property="verDetalleDeudaEnabled" value="true">
								<img title="Ver" src="<%=request.getContextPath()%>/images/iconos/selec1.gif" border="0"/>
							</logic:notEqual>
			  			</td>
			  			
			  			<td style="font-size: 70%">
			  				<logic:equal name="liqDeudaAdapterVO" property="verPlanillaEnabled" value="true">
					  			<a href="/siat/gde/AdministrarLiqDeuda.do?method=verPlanilla&selectedId=<bean:write name="LiqDeudaJud" property="idPlanilla" bundle="base" formatKey="general.format.id"/>&cuentaId=<bean:write name="liqDeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>" >
				  					<bean:write name="LiqDeudaJud" property="perAnioPlanilla"/>&nbsp;
				       			</a>
			       			</logic:equal>
			       			<logic:notEqual name="liqDeudaAdapterVO" property="verPlanillaEnabled" value="true">
			       				<b><bean:write name="LiqDeudaJud" property="perAnioPlanilla"/></b>&nbsp;
			       			</logic:notEqual>
			  			</td>
			  			<td><bean:write name="LiqDeudaJud" property="codRefPagView"/>&nbsp;</td>
			  			<td><bean:write name="LiqDeudaJud" property="periodoDeuda"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaJud" property="fechaVto"/>&nbsp;</td>
				       	
				       	<!-- Si NO posee Observacion -->				       	
				        <logic:notEqual name="LiqDeudaJud" property="poseeObservacion" value="true">
					        
					    	<!-- Para recursos autoliquidables -->
				        	<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
					        	<td><bean:write name="LiqDeudaJud" property="importeCurrencyView" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
								<td class="separadorAuto">&nbsp;</td>
					        	<td><bean:write name="LiqDeudaJud" property="fechaPago"/>&nbsp;</td>
								<td><bean:write name="LiqDeudaJud" property="importePago" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
								<td><bean:write name="LiqDeudaJud" property="saldoCurrencyView"/>&nbsp;</td>
						        <td><bean:write name="LiqDeudaJud" property="actualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        <td><bean:write name="LiqDeudaJud" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        </logic:equal>

							<!-- Para recursos NO autoliquidables -->
							<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">	
						        <td><bean:write name="LiqDeudaJud" property="saldoCurrencyView" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        <td><bean:write name="LiqDeudaJud" property="actualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        <td><bean:write name="LiqDeudaJud" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				       		</logic:notEqual>
					    	
					    </logic:notEqual>
				       	
				       	<!-- Si posee Convenio -->
				       	<logic:equal name="LiqDeudaJud" property="poseeConvenio" value="true">
				       		<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				       			<td colspan="8">
				       		</logic:equal>
				       		<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				       			<td colspan="4">
				       		</logic:notEqual>
				       			<logic:equal name="liqDeudaAdapterVO" property="verConvenioEnabled" value="true">
					       			<a href="/siat/gde/AdministrarLiqDeuda.do?method=verConvenio&selectedId=<bean:write name="LiqDeudaJud" property="idLink" bundle="base" formatKey="general.format.id"/>&cuentaId=<bean:write name="liqDeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>" >
					       				<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockJudicial.nroConvenio.label"/>:
					       				<bean:write name="LiqDeudaJud" property="observacion"/>
					       			</a>
					       		</logic:equal>
					       		<logic:notEqual name="liqDeudaAdapterVO" property="verConvenioEnabled" value="true">
					       			<b><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockJudicial.nroConvenio.label"/>:
					       			<bean:write name="LiqDeudaJud" property="observacion"/></b>
					       		</logic:notEqual>
				       		</td>
				       	</logic:equal>
				       	
					    <!-- Es Indeterminada -->
		       			<logic:equal name="LiqDeudaJud" property="esIndeterminada" value="true">
		       				<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				       			<td colspan="8">
				       		</logic:equal>
				       		<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				       			<td colspan="4">
				       		</logic:notEqual>
		       					<b><bean:write name="LiqDeudaJud" property="observacion"/>&nbsp;</b>
							</td>
		       			</logic:equal>
					    
					    <!-- Es Reclamada -->
				       	<logic:equal name="LiqDeudaJud" property="esReclamada" value="true">
		       				<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				       			<td colspan="8">
				       		</logic:equal>
				       		<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				       			<td colspan="4">
				       		</logic:notEqual>
		       					<b><bean:write name="LiqDeudaJud" property="observacion"/>&nbsp;</b>
							</td>
				    	</logic:equal>
			
					    <!-- Es Exento Pago -->
				       	<logic:equal name="LiqDeudaJud" property="esExentoPago" value="true">
		       				<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				       			<td colspan="8">
				       		</logic:equal>
				       		<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				       			<td colspan="4">
				       		</logic:notEqual>
		       					<b><bean:write name="LiqDeudaJud" property="observacion"/>&nbsp;</b>
							</td>
				    	</logic:equal>
				    	
				    	 <!-- Es Constancia No Habilitada -->
				       	<logic:equal name="LiqDeudaJud" property="esNoHabilitada" value="true">
		       				<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				       			<td colspan="8">
				       		</logic:equal>
				       		<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				       			<td colspan="4">
				       		</logic:notEqual>
		       					<b><bean:write name="LiqDeudaJud" property="observacion"/>&nbsp;</b>
							</td>
				    	</logic:equal>
			       	</tr>
				</logic:iterate>
				<!-- Fin Item LiqDeudaVO -->
				<tr>
					<td colspan="6" class="celdatotales"> &nbsp;</td>
					
			        <!-- Para recursos autoliquidables -->
				    <logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
			       		<td class="celdatotales" align="left">
				        	<b><bean:write name="Procurador" property="subTotalImporte" bundle="base" formatKey="general.format.currency"/></b>
				        </td>
				        <td class="separadorAuto">&nbsp;</td>
				        <td class="celdatotales">&nbsp;</td>
				         <td class="celdatotales" align="left">
					      	<b><bean:write name="Procurador" property="subTotalImportePago" bundle="base" formatKey="general.format.currency"/></b>
					    </td>
			        </logic:equal>
			        
		        	<td class="celdatotales" align="left">
			        	<b><bean:write name="Procurador" property="subTotalSaldo" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
			        
			        <td class="celdatotales" align="left">
			        	<b><bean:write name="Procurador" property="subTotalActualizacion" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       		<td class="celdatotales" align="left">
			        	<b><bean:write name="Procurador" property="subTotal" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       	</tr>
		       	
		       	<tr>       		
		       		<!-- Para recursos autoliquidables -->
		       		<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
		       			<td colspan="13" class="celdatotales" align="right">
		       		</logic:equal>
		       		<!-- Para recursos NO autoliquidables -->
		       		<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
		       			<td colspan="9" class="celdatotales" align="right">
		       		</logic:notEqual>
		       		
			        	<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockJudicial.subtotal.label"/>: 
			        	<b><bean:write name="Procurador" property="subTotal" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       	</tr>	       	
	       	</logic:iterate>
	       	<!-- Fin LiqProcuradorVO -->
	      	</tbody>
		</table>
		</div>

	</logic:notEmpty>
	<!-- Fin Deuda en Gestion Judicial -->
	
	<!-- Deuda En Gestion Administrativa -->	
	<%@ include file="/gde/gdeuda/includeDeudaGestionAdmin.jsp" %>
	<!-- Fin Deuda En Gestion Administrativa -->
	
	<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
		<p class="msgBold">
      		(*) <bean:message bundle="gde" key="gde.liqDeudaAdapter.msgTotalRecursoAutoliquidable"/>	      		
		</p>
	</logic:equal>

	<!-- Total -->
	<div class="borde">
		<h3>
			<bean:message bundle="gde" key="gde.liqDeudaAdapter.total"/>: 
			<bean:write name="liqDeudaAdapterVO" property="total" bundle="base" formatKey="general.format.currency"/>
		</h3>
	</div>

	<!-- Fecha Acentamiento -->
	<p>
		<label><bean:message bundle="gde" key="gde.liqDeudaAdapter.alaFechaAcentamiento"/>:</label>
		<bean:write name="liqDeudaAdapterVO" property="fechaAcentamiento"/>
	</p>

	<!-- Acciones -->
	<fieldset>
		<p align="center">
				
			<!-- Reconfeccionar -->
			<logic:equal name="liqDeudaAdapterVO" property="reconfeccionarVisible" value="true">
				<logic:equal name="liqDeudaAdapterVO" property="reconfeccionarEnabled" value="true">
					<button type="button" name="btnReimp" class="boton" onclick="submitForm('reconfeccionar', '<bean:write name="liqDeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
						<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.reimpresion"/>
					</button>
				</logic:equal>
				<logic:notEqual name="liqDeudaAdapterVO" property="reconfeccionarEnabled" value="true">
					<button type="button" name="btnReimp" class="botonDeshabilitado" disabled="disabled">
						<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.reimpresion"/>
					</button>
				</logic:notEqual>
			</logic:equal>	
				
			<!-- ReconfeccionarEsp -->
			<logic:equal name="liqDeudaAdapterVO" property="reconfeccionarEspVisible" value="true">
				<logic:equal name="liqDeudaAdapterVO" property="reconfeccionarEspEnabled" value="true">
					<button type="button" name="btnReimp" class="boton" onclick="submitForm('reconfeccionarEsp', '<bean:write name="liqDeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
						<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.reimpresionEsp"/>
					</button>
				</logic:equal>
				<logic:notEqual name="liqDeudaAdapterVO" property="reconfeccionarEspEnabled" value="true">
					<button type="button" name="btnReimp" class="botonDeshabilitado" disabled="disabled">
						<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.reimpresionEsp"/>
					</button>
				</logic:notEqual>
			</logic:equal>
				
			<!-- Imprimir Informe de Deuda -->
			<logic:equal name="liqDeudaAdapterVO" property="imprimirInformeDeudaVisible" value="true">
				<logic:equal name="liqDeudaAdapterVO" property="imprimirInformeDeudaEnabled" value="true">
					<button type="button" name="btnImpLiqDeuda" class="boton" onclick="submitForm('irImprimirLiqDeuda', '<bean:write name="liqDeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
			  			<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.impresionLiqDeu"/>
					</button>
				</logic:equal>
				<logic:notEqual name="liqDeudaAdapterVO" property="imprimirInformeDeudaEnabled" value="true">
					<button type="button" name="btnImpLiqDeuda" class="botonDeshabilitado" disabled="disabled">
			  			<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.impresionLiqDeu"/>
					</button>
				</logic:notEqual>
			</logic:equal>
			
			<!-- Formalizar Convenio -->
			<logic:equal name="liqDeudaAdapterVO" property="formalizarConvenioVisible" value="true">
		    	<logic:equal name="liqDeudaAdapterVO" property="formalizarConvenioEnabled" value="true">
			    	<button type="button" name="btnFormPlan" class="boton" onclick="submitForm('formalizarConvenio', '<bean:write name="liqDeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
				  		<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.opcionesPago"/>
			  		</button>
		    	</logic:equal>
		    	<logic:notEqual name="liqDeudaAdapterVO" property="formalizarConvenioEnabled" value="true">
			    	<button type="button" name="btnFormPlan" class="botonDeshabilitado" disabled="disabled">
				  		<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.opcionesPago"/>
			  		</button>
		    	</logic:notEqual>		    
		    </logic:equal>
		    
		    <!-- Formalizar Convenio Especial (Plan Manual)-->
		    <logic:equal name="liqDeudaAdapterVO" property="formalizarConvenioEspVisible" value="true">
		    	<logic:equal name="liqDeudaAdapterVO" property="formalizarConvenioEspEnabled" value="true">
			  		<button type="button" name="btnFormEspPlan" class="boton" onclick="submitForm('formalizarConvenioEsp', '<bean:write name="liqDeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
				  		<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.opcionesPagoEspecial"/>
			  		</button>	    	
		    	</logic:equal>
		    	<logic:notEqual name="liqDeudaAdapterVO" property="formalizarConvenioEspEnabled" value="true">
			  		<button type="button" name="btnFormEspPlan" class="botonDeshabilitado" disabled="disabled">
				  		<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.opcionesPagoEspecial"/>
			  		</button>	    	
		    	</logic:notEqual>		    
		    </logic:equal>	
		    
		    <!-- Desglose Ajuste-->
		    <logic:equal name="liqDeudaAdapterVO" property="desglosarAjusteVisible" value="true">
		    	<logic:equal name="liqDeudaAdapterVO" property="desglosarAjusteEnabled" value="true">
			  		<button type="button" name="btnFormDesAju" class="boton" onclick="submitForm('desglosarAjuste', '<bean:write name="liqDeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
				  		<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.desgloseAjuste"/>
			  		</button>	    	
		    	</logic:equal>
		    	<logic:notEqual name="liqDeudaAdapterVO" property="desglosarAjusteEnabled" value="true">
			  		<button type="button" name="btnFormDesAju" class="botonDeshabilitado" disabled="disabled">
				  		<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.desgloseAjuste"/>
			  		</button>	    	
		    	</logic:notEqual>		    
		    </logic:equal>	
		    
		    <!-- Informe para Escribano -->
		    <logic:notEqual name="liqDeudaAdapterVO" property="recurso.esAutoliquidable.id" value="1">
			    <logic:equal name="liqDeudaAdapterVO" property="infDeudaEscribanoVisible" value="true">
			    	<logic:equal name="liqDeudaAdapterVO" property="infDeudaEscribanoEnabled" value="true">
						<button type="button" name="btnImpLiqDeuda" class="boton" onclick="submitForm('infDeudaEscribano', '<bean:write name="liqDeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
							<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.imprimirInformeEscribano"/>
						</button>
					</logic:equal>
			    	<logic:notEqual name="liqDeudaAdapterVO" property="infDeudaEscribanoEnabled" value="true">
						<button type="button" name="btnImpLiqDeuda" class="botonDeshabilitado" disabled="disabled">
							<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.imprimirInformeEscribano"/>
						</button>
					</logic:notEqual>							
				</logic:equal>
			</logic:notEqual>
			
			<!-- Cambio de plan CDM -->
			<logic:equal name="liqDeudaAdapterVO" property="cambioPlanCDMVisible" value="true">
				<logic:equal name="liqDeudaAdapterVO" property="cambioPlanCDMEnabled" value="true">
					<button type="button" name="btnCambioDePlan" class="boton" onclick="submitForm('cambioPlanCDM', '<bean:write name="liqDeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
						<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.cambioPlan"/>
					</button>
				</logic:equal>
				<logic:notEqual name="liqDeudaAdapterVO" property="cambioPlanCDMEnabled" value="true">
					<button type="button" name="btnCambioDePlan" class="botonDeshabilitado" disabled="disabled">
						<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.cambioPlan"/>
					</button>
				</logic:notEqual>				
			</logic:equal>
			
			<!-- Cuota Saldo CDM -->
			<logic:equal name="liqDeudaAdapterVO" property="cuotaSaldoCDMVisible" value="true">	
				<logic:equal name="liqDeudaAdapterVO" property="cuotaSaldoCDMEnabled" value="true">
			    	<button type="button" name="btnCuotaSaldoCDM" class="boton" onclick="submitForm('cuotaSaldoCDM', '<bean:write name="liqDeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
				    	<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.cuotaSaldoCDM"/>
			    	</button>
				</logic:equal>
				<logic:notEqual name="liqDeudaAdapterVO" property="cuotaSaldoCDMEnabled" value="true">
			    	<button type="button" name="btnCuotaSaldoCDM" class="botonDeshabilitado" disabled="disabled">
				    	<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.cuotaSaldoCDM"/>
			    	</button>
				</logic:notEqual>				
			</logic:equal>
			
			<!-- Cierre de comercio -->
			<logic:equal name="liqDeudaAdapterVO" property="cierreComercioVisible" value="true">	
				<logic:equal name="liqDeudaAdapterVO" property="cierreComercioEnabled" value="true">
			    	<button type="button" name="btnCuotaSaldoCDM" class="boton" onclick="submitForm('cierreComercio', '<bean:write name="liqDeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
				    	<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.cierreComercio"/>
			    	</button>
				</logic:equal>
				<logic:notEqual name="liqDeudaAdapterVO" property="cierreComercioEnabled" value="true">
			    	<button type="button" name="btnCuotaSaldoCDM" class="botonDeshabilitado" disabled="disabled">
				    	<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.cierreComercio"/>
			    	</button>
				</logic:notEqual>				
			</logic:equal>
				<!--Imprimir Cierre de comercio -->
			<logic:equal name="liqDeudaAdapterVO" property="imprimirCierreComercioVisible" value="true">	
				<logic:equal name="liqDeudaAdapterVO" property="imprimirCierreComercioEnabled" value="true">
			    	<button type="button" name="btnImpCierre" class="boton"  onclick="submitForm('imprimirCierre', '');">
				    	<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.imprimirCierre"/>
			    	</button>
			    	</logic:equal>
				<logic:notEqual name="liqDeudaAdapterVO" property="imprimirCierreComercioEnabled" value="true">
			    	<button type="button" name="btnImpCierre" class="botonDeshabilitado" disabled="disabled">
				  		<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.imprimirCierre"/>
			  		</button>	    	
		    	 </logic:notEqual>				
			</logic:equal>
			
			<!-- Declaracion Jurada Masiva -->
			<logic:equal name="liqDeudaAdapterVO" property="decJurMasivaVisible" value="true">
				<logic:equal name="liqDeudaAdapterVO" property="decJurMasivaEnabled" value="true">
			    	<button type="button" name="btnImpCierre" class="boton"  onclick="submitForm('decJurMasiva', '<bean:write name="liqDeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
				    	<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.decJurMasiva"/>
			    	</button>
			    </logic:equal>
				<logic:notEqual name="liqDeudaAdapterVO" property="decJurMasivaEnabled" value="true">
			    	<button type="button" name="btnImpCierre" class="botonDeshabilitado" disabled="disabled">
				  		<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.imprimirCierre"/>
			  		</button>	    	
		    	 </logic:notEqual>				
			</logic:equal>
			
			<!-- Reclamar Asentamiento -->
			<logic:equal name="liqDeudaAdapterVO" property="reclamarAcentVisible" value="true">
		    	<logic:equal name="liqDeudaAdapterVO" property="reclamarAcentEnabled" value="true">
			    	<button type="button" name="btnRecAcent" class="boton" onclick="submitForm('reclamarAcent', '<bean:write name="liqDeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
			  			<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.reclamarAcentamiento"/>
					</button>
				</logic:equal>
				<logic:notEqual name="liqDeudaAdapterVO" property="reclamarAcentEnabled" value="true">
					<button type="button" name="btnImpCierre" class="botonDeshabilitado" disabled="disabled">
				  		<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.reclamarAcentamiento"/>
			  		</button>	
				</logic:notEqual>
			</logic:equal>	
			
			<!-- Declaracion Jurada de Entradas Vendidas (Emision) -->
			<logic:equal name="liqDeudaAdapterVO" property="ddjjEntVenHabVisible" value="true">
				<logic:equal name="liqDeudaAdapterVO" property="ddjjEntVenHabEnabled" value="true">
					<button type="button" name="btnDdjjEntVen" class="boton" onclick="submitForm('ddjjEntVen', '<bean:write name="liqDeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
						<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.ddjjEntVen"/>
					</button>
				</logic:equal>
				<logic:notEqual name="liqDeudaAdapterVO" property="ddjjEntVenHabEnabled" value="true">
					<button type="button" name="btnDdjjEntVen" class="botonDeshabilitado" disabled="disabled">
						<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.ddjjEntVen"/>
					</button>
				</logic:notEqual>				
			</logic:equal>
			
			<!-- Ver Novedades de Regimen Simplificado-->
			<logic:equal name="liqDeudaAdapterVO" property="verNovedadesRSVisible" value="true">
				<logic:equal name="liqDeudaAdapterVO" property="verNovedadesRSEnabled" value="true">
					<button type="button" name="btnVerNodevadesRS" class="boton" onclick="submitForm('verNovedadesRS', '<bean:write name="liqDeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
						<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.verNovedadesRS"/>
					</button>
				</logic:equal>
				<logic:notEqual name="liqDeudaAdapterVO" property="verNovedadesRSEnabled" value="true">
					<button type="button" name="btnVerNodevadesRS" class="botonDeshabilitado" disabled="disabled">
						<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.verNovedadesRS"/>
					</button>
				</logic:notEqual>				
			</logic:equal>
			
			<!-- Generar Volante de Pago de Intereses de Regimen Simplificado-->
			<logic:equal name="liqDeudaAdapterVO" property="volantePagoIntRSVisible" value="true">
				<logic:equal name="liqDeudaAdapterVO" property="volantePagoIntRSEnabled" value="true">
					<button type="button" name="btnVerVolandePagoRS" class="boton" onclick="submitForm('volantePagoIntRS', '<bean:write name="liqDeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
						<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.volantePagoIntRS"/>
					</button>												 
				</logic:equal>
				<logic:notEqual name="liqDeudaAdapterVO" property="volantePagoIntRSEnabled" value="true">
					<button type="button" name="btnVerNodevadesRS" class="botonDeshabilitado" disabled="disabled">
						<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.volantePagoIntRS"/>
					</button>
				</logic:notEqual>				
			</logic:equal>
		</p>
	</fieldset>
	<!-- Fin Acciones -->
	
	<logic:notEqual name="userSession" property="isAnonimoView" value="1">
		<!-- Volver -->
		<button type="button" name="btnVolver" class="boton" onclick="submitForm('volver', '');">
	  	    <bean:message bundle="base" key="abm.button.volver"/>
		</button>
	</logic:notEqual>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="cuentaId" value="<bean:write name="liqDeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>"/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->
