<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarLiqReconfeccion.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<!-- Para la web lo muestro, para usuario Interno NO -->
	<logic:equal name="userSession" property="isAnonimoView" value="1">
		<%@ include file="/gde/gdeuda/includeDivButtons.jsp" %>
	</logic:equal>	
	
	<h1><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.title"/></h1>
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
				<%=ar.gov.rosario.siat.fra.iface.util.Frase.getInstance().getMessageBykey("gde.reconfeccion.leyendaEncabezado")%>
				
				
				</p>
			</td>				
			<td align="right">
	 			<logic:notEqual name="userSession" property="isAnonimoView" value="1">
					<!-- Volver -->
					<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuenta', '');">
				  	    <bean:message bundle="base" key="abm.button.volver"/>
					</button>
				</logic:notEqual>
			</td>
		</tr>
	</table>
	
	<!-- Procedimiento -->
	<logic:equal name="liqReconfeccionAdapterVO" property="cuenta" value="">
		<bean:define id="procedimientoVO" name="liqReconfeccionAdapterVO" property="procedimiento"/>
		<%@ include file="/cyq/concursoyQuiebra/includeEncProcedimiento.jsp"%>
	<!-- Procedimiento -->
	</logic:equal>

	<logic:notEqual name="liqReconfeccionAdapterVO" property="cuenta" value="">
		<!-- LiqCuenta -->
			<bean:define id="DeudaAdapterVO" name="liqReconfeccionAdapterVO"/>
			<%@ include file="/gde/gdeuda/includeLiqCuenta.jsp" %>
		<!-- LiqCuenta -->
	
		<!-- CuentasRel -->
		<logic:notEmpty name="liqReconfeccionAdapterVO" property="listCuentaRel" >
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuentasRelacionadas.title"/> </legend>			
				<ul>
					<logic:iterate id="CuentaRel" name="liqReconfeccionAdapterVO" property="listCuentaRel">
						<li>
							<!-- Permitido ver Cuentas Relacionadas al Objeto Imponible -->
							<logic:equal name="liqReconfeccionAdapterVO" property="verCuentaRelEnabled" value="true">
					      		<a href="/siat/gde/AdministrarLiqDeuda.do?method=verCuentaRel&selectedId=<bean:write name="CuentaRel" property="idCuenta" bundle="base" formatKey="general.format.id"/>" >
						      		<bean:write name="CuentaRel" property="nroCuenta"/> -
						      		<bean:write name="CuentaRel" property="desCategoria"/> -
						      		<bean:write name="CuentaRel" property="desRecurso"/>
					      		</a>
					      	</logic:equal>
					      	<logic:notEqual name="liqReconfeccionAdapterVO" property="verCuentaRelEnabled" value="true">
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
			
		<!-- listDeuda -->
		<logic:notEmpty name="liqReconfeccionAdapterVO" property="listDeuda">
			<div class="horizscroll">
		    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
		    	<caption><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.listaDeudas.title"/></caption>
		      	<tbody>
			       	<tr>
					  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.periodoDeuda"/></th>
					  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.fechaVto"/></th>
					  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.saldo"/></th>
					  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion"/></th>
					  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total"/></th>
					</tr>
					
					<!-- Item LiqDeudaVO -->
					<logic:iterate id="LiqDeudaVO" name="liqReconfeccionAdapterVO" property="listDeuda">
						<tr>
				  			<!-- Ver detalle Deuda -->
				  			<td><bean:write name="LiqDeudaVO" property="periodoDeuda"/>&nbsp;</td>
					        <td><bean:write name="LiqDeudaVO" property="fechaVto"/>&nbsp;</td>
					        <td>
					        	<logic:notEqual name="LiqDeudaVO" property="valorCero" value="true">
					        		<bean:write name="LiqDeudaVO" property="saldo" bundle="base" formatKey="general.format.currency"/>&nbsp;
					        	</logic:notEqual>
					        	<logic:equal name="LiqDeudaVO" property="valorCero" value="true">
					        		<bean:message bundle="gde" key="gde.liqReconfeccionAdapter.deuda.valorCero.legend"/>
					        	</logic:equal>
					        </td>
					        
					        <td>
					        	<logic:notEqual name="LiqDeudaVO" property="valorCero" value="true">
					        		<bean:write name="LiqDeudaVO" property="actualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;
					        	</logic:notEqual>
					        	<logic:equal name="LiqDeudaVO" property="valorCero" value="true">
					        		<bean:write name="LiqDeudaVO" property="actRecCeroView"/>&nbsp;
					        	</logic:equal>
					        </td>
					        <td>
					        	<logic:notEqual name="LiqDeudaVO" property="valorCero" value="true">
					        		<bean:write name="LiqDeudaVO" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;
					        	</logic:notEqual>
					        	<logic:equal name="LiqDeudaVO" property="valorCero" value="true">
					        		<bean:message bundle="gde" key="gde.liqReconfeccionAdapter.deuda.valorCero.legend"/>
					        	</logic:equal>
					        </td>
				       	</tr>
					</logic:iterate>
					<!-- Fin Item LiqDeudaVO -->
		      	</tbody>
			</table>
			</div>
		</logic:notEmpty>
		<!-- Fin listDeuda -->
		
	</logic:notEqual>
	
	<!-- listCuota -->
	<logic:notEmpty name="liqReconfeccionAdapterVO" property="listCuotas">
		<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption>Convenio: <bean:write name="liqReconfeccionAdapterVO" property="convenio.nroConvenio"/>&nbsp; - <bean:message bundle="gde" key="gde.liqReconfeccionAdapter.listaCuotas.title"/></caption>
	      	<tbody>
		       	<tr>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.nroCuota.label"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.fechaVto.label"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.capital.label"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.interes.label"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.total.label"/></th>
				</tr>
				
				<!-- Item LiqCuotaVO -->
				<logic:iterate id="LiqCuotaVO" name="liqReconfeccionAdapterVO" property="listCuotas">
					<tr>
			  			<!-- Ver detalle Deuda -->
			  			<td><bean:write name="LiqCuotaVO" property="nroCuota"/>&nbsp;</td>
				        <td><bean:write name="LiqCuotaVO" property="fechaVto"/>&nbsp;</td>
				        <td><bean:write name="LiqCuotaVO" property="capital" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqCuotaVO" property="interes" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqCuotaVO" property="recargo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqCuotaVO" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
			       	</tr>
				</logic:iterate>
				<!-- Fin Item LiqCuotaVO -->
	      	</tbody>
		</table>
		</div>
	</logic:notEmpty>
	<!-- Fin listCuota -->
	
	<logic:equal name="liqReconfeccionAdapterVO" property="esReconfeccionEspecial" value="true">
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.fechaReconfEsp.title"/></legend>
			<table border="0">
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.liqReconfeccionAdapter.fechaActualizacionEsp.label"/></label></td>
					<td class="normal">
						<html:text name="liqReconfeccionAdapterVO" property="fechaActualizacionEspView" styleId="fechaActualizacionEspView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaActualizacionEspView');" id="a_fechaActualizacionEspView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>

				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.liqReconfeccionAdapter.fechaVencimientoEsp.label"/></label></td>
					<td class="normal">
						<html:text name="liqReconfeccionAdapterVO" property="fechaVencimientoEspView" styleId="fechaVencimientoEspView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaVencimientoEspView');" id="a_fechaVencimientoEspView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>

				<!-- Caso -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
						<td colspan="3">
						<bean:define id="IncludedVO" name="liqReconfeccionAdapterVO" property="casoContainer"/>
						<bean:define id="voName" value="casoContainer" />
						<%@ include file="/cas/caso/includeCaso.jsp" %>
					</td>
				</tr>

			</table>		
		</fieldset>	
	</logic:equal>
	
	<logic:equal name="liqReconfeccionAdapterVO" property="esReconfeccionEspecial" value="false">
		<!-- Dias disponibles -->
			<fieldset>
			<logic:notEqual name="liqReconfeccionAdapterVO" property="tieneDeudaVencida" value="false">
				<legend><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.listaFechas.title"/></legend>
				<p><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.listaFechas.legend"/></p>
			</logic:notEqual>
			<logic:equal name="liqReconfeccionAdapterVO" property="tieneDeudaVencida" value="false">
				<legend><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.tipoDec.title"/></legend>
			</logic:equal>
			
				<logic:notEmpty name="liqReconfeccionAdapterVO" property="listFechasReconfView">				
					<table border="0" width="100%">
						<logic:notEqual name="liqReconfeccionAdapterVO" property="tieneDeudaVencida" value="false">
				      		<tr>
				      			<td width="43%">&nbsp;</td>
								<td width="57%" align="left">
										<html:select name="liqReconfeccionAdapterVO" property="fechaReconfSelected" styleClass="select" >
											<logic:iterate id="fecha" name="liqReconfeccionAdapterVO" property="listFechasReconfView">			
													<option value="<bean:write name="fecha"/>">
														<bean:write name="fecha"/>
													</option>	
											</logic:iterate>
										</html:select>
								</td>
							</tr>
						</logic:notEqual>
						<logic:equal name="liqReconfeccionAdapterVO" property="recEnBlanco" value="true">
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td align="right"><label><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.tipoReconfeccion.label"/>: </label></td>
								<td class="normal">
									<html:select name="liqReconfeccionAdapterVO" property="decJur.tipDecJurRec.id" styleClass="select">
										<html:optionsCollection name="liqReconfeccionAdapterVO" property="listTipDecJurRec" label="tipDecJur.desTipo" value="id"/>
									</html:select>
								</td>
							</tr>
						</logic:equal>
					</table>
				</logic:notEmpty>
				<logic:empty name="liqReconfeccionAdapterVO" property="listFechasReconfView">
					<p><bean:message bundle="gde" key="gde.liqReconfeccionAdapter.listaFechas.vacia"/></p>				
				</logic:empty>
				<logic:equal name="liqReconfeccionAdapterVO" property="verMensajeCaducidad" value="true">
					<p class="msgBold"><bean:write name="liqReconfeccionAdapterVO" property="mensajeCaducidad"/></p>
				</logic:equal>
				
			</fieldset>
		<!-- Fin Dias disponibles -->
	</logic:equal>
	
	<!--  boton reconfeccionar -->
		<logic:notEmpty name="liqReconfeccionAdapterVO" property="listFechasReconfView">			
			<p align="center">
				<button type="button" name="btnReconfeccionar" class="boton" onclick="submitForm('reconfeccionar', '');">
			  	    <bean:message bundle="gde" key="gde.liqReconfeccionAdapter.button.reconfeccionar"/>
				</button>
			</p>
		</logic:notEmpty>	
	<!--  FIN boton reconfeccionar -->
			
	<logic:notEqual name="userSession" property="isAnonimoView" value="1">
		<!-- Volver -->
		<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuenta', '');">
	  	    <bean:message bundle="base" key="abm.button.volver"/>
		</button>
	</logic:notEqual>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin formulario -->