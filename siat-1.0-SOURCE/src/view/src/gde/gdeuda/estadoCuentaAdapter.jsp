<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarLiqEstadoCuenta.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.liqEstadoCuentaAdapter.title"/></h1>
	<logic:notEqual name="userSession" property="isAnonimoView" value="1">	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volverFromDetalle', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
	</logic:notEqual>
	
	<!-- LiqCuenta -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuentaSeleccionada"/>: 
			<bean:write name="estadoCuentaAdapterVO" property="cuenta.nroCuenta"/>
		</legend>
			<!-- Mensaje por si la cuenta esta inactiva (en estado CREADA, o con fecha baja caduca) -->
			<logic:equal name="estadoCuentaAdapterVO" property="cuenta.esActiva" value="false">
				<div id="messagesStruts" class="messages"> 
		        <bean:message bundle="pad" key="pad.cuenta.noVigente"/>
			  	</div>
			</logic:equal>

			<!-- Tributo -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuenta.desRecurso.label"/>:</label>
	      		<bean:write name="estadoCuentaAdapterVO" property="cuenta.desRecurso"/>
			</p>

	        <!-- CUIT - Denominacion de titular principal -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuenta.desTitularPrincipal.label"/>:</label>
				  <bean:write name="estadoCuentaAdapterVO" property="cuenta.cuitTitularPrincipal"/>&nbsp;&nbsp;
				  <bean:write name="estadoCuentaAdapterVO" property="cuenta.nombreTitularPrincipal"/>
			</p>

			<!-- CodigoGestionPersonal -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuenta.codGestionPersonal.label"/>:</label>
	      		<bean:write name="estadoCuentaAdapterVO" property="cuenta.codGestionPersonal"/>
			</p>

			<!-- Atributos del Objeto Imponible -->
			<logic:notEmpty name="estadoCuentaAdapterVO" property="cuenta.listAtributoObjImp" >
				<logic:iterate id="AtrVal" name="estadoCuentaAdapterVO" property="cuenta.listAtributoObjImp">
					<p>
			      		<label><bean:write name="AtrVal" property="label"/>:</label>
			      		
			      		<logic:notEqual name="AtrVal" property="esMultiValor" value="true">
			      			<bean:write name="AtrVal" property="value"/>
			      		</logic:notEqual>
			      		
			      		<logic:equal name="AtrVal" property="esMultiValor" value="true">
							<dl class="listabloqueSiat">
								<logic:iterate id="MultiValor" name="AtrVal" property="multiValue">
									<dd>
						      			<bean:write name="MultiValor"/>
						      		</dd>
						      	</logic:iterate>
							</dl>		
			      		</logic:equal>

						<!-- Para tipo de objeto imponible distinto de COMERCIO -->
					    <logic:notEqual name="estadoCuentaAdapterVO" property="cuenta.codTipObjImp" value="COMERCIO">
							<logic:equal name="AtrVal" property="key" value="Ubicacion">
						        <!-- Domicilio Envio de cuenta -->
						        &nbsp;
					      		<label><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuenta.desDomEnv.label"/>:</label>
				                <bean:write name="estadoCuentaAdapterVO" property="cuenta.desDomEnv"/>
							</logic:equal>
						</logic:notEqual>
					</p>
				</logic:iterate>
			</logic:notEmpty>

			<!-- Ver Detalle de Objeto Imponible -->
			<logic:equal name="estadoCuentaAdapterVO" property="verDetalleObjImpEnabled" value="true">
				<button type="button" name="btnVerObjImp" class="verfoto" onclick="submitForm('verDetalleObjImp', '<bean:write name="estadoCuentaAdapterVO" property="cuenta.idObjImp" bundle="base" formatKey="general.format.id"/>');">
		  			<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.verDetalleObjImp"/>
				</button>
				<html:hidden name="estadoCuentaAdapterVO" property="cuenta.idCuenta" />
			</logic:equal>

			<!-- Titulares -->
			<dl class="listabloqueSiat">
	     	   	<dt><bean:message bundle="gde" key="gde.liqDeudaAdapter.titulares.title"/>: </dt>
				<logic:notEmpty name="estadoCuentaAdapterVO" property="cuenta.listTitular" >
					<logic:iterate id="Titular" name="estadoCuentaAdapterVO" property="cuenta.listTitular">
						<dd>
							<!-- Permitido ver cuentas del titular -->
							<logic:equal name="estadoCuentaAdapterVO" property="verDeudaContribEnabled" value="true">
								<a href="/siat/gde/AdministrarLiqEstadoCuenta.do?method=verDeudaContrib&id=<bean:write name="Titular" property="idTitular" bundle="base" formatKey="general.format.id"/>&cuentaId=<bean:write name="estadoCuentaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>">
				      				<bean:write name="Titular" property="desTitular"/>
				      			</a>
				      		</logic:equal>
				      		<!-- No Permitido ver cuentas del titular -->
				      		<logic:notEqual name="estadoCuentaAdapterVO" property="verDeudaContribEnabled" value="true">
				      			<bean:write name="Titular" property="desTitular"/>
				      		</logic:notEqual>
						</dd>
					</logic:iterate>
				</logic:notEmpty>
			</dl>
			
			<!-- ver Historico de Contribuyentes -->
			<logic:equal name="estadoCuentaAdapterVO" property="verHistoricoContribEnabled" value="true">
				<button type="button" name="btnVerHistoricoContrib" class="verfoto" onclick="submitForm('verHistoricoContrib', '<bean:write name="estadoCuentaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
		  			<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.verHistoricoContrib"/>
				</button>
			</logic:equal>
			
			<!-- Atributos del Contribuyente -->
			<dl class="listahorizontalSiat">
				<logic:notEmpty name="estadoCuentaAdapterVO" property="cuenta.listAtributoContr" >
					<logic:iterate id="ConAtrVal" name="estadoCuentaAdapterVO" property="cuenta.listAtributoContr">						
	     	   			<dt>
	     	   				<bean:write name="ConAtrVal" property="label"/>:			      		
	     	   			</dt>
						<dd>
				      		<bean:write name="ConAtrVal" property="value"/>
						</dd>
					</logic:iterate>
				</logic:notEmpty>
			</dl>
			
			<!-- Convenios Asociados -->
			<dl class="listabloqueSiat">
				<logic:notEmpty name="estadoCuentaAdapterVO" property="cuenta.listConvenio" >
	     	   		<dt><bean:message bundle="gde" key="gde.liqDeudaAdapter.conveniosAsociados.title"/>: </dt> 
					
					<logic:iterate id="Convenio" name="estadoCuentaAdapterVO" property="cuenta.listConvenio">
						<dd>
							<!-- Permitido ver Convenios Asociados  -->
							<logic:equal name="estadoCuentaAdapterVO" property="verConvenioEnabled" value="true">
					      		<a href="/siat/gde/AdministrarLiqEstadoCuenta.do?method=verConvenio&selectedId=<bean:write name="Convenio" property="idConvenio" bundle="base" formatKey="general.format.id"/>&cuentaId=<bean:write name="estadoCuentaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>" >
						      		<bean:write name="Convenio" property="nroConvenio"/> -
						      		<bean:write name="Convenio" property="desPlan"/> -
						      		<bean:write name="Convenio" property="desViaDeuda"/>
					      		</a>
							</logic:equal>
							<!-- No Permitido ver Convenios Asociados  -->
							<logic:notEqual name="estadoCuentaAdapterVO" property="verConvenioEnabled" value="true">
								<bean:write name="Convenio" property="nroConvenio"/> -
						      	<bean:write name="Convenio" property="desPlan"/> -
						      	<bean:write name="Convenio" property="desViaDeuda"/>
							</logic:notEqual>
						</dd>
					</logic:iterate>
				</logic:notEmpty>
			</dl>
			
			<!-- Desgloses y Unificaciones -->
			<dl class="listabloqueSiat">
				<logic:notEmpty name="estadoCuentaAdapterVO" property="cuenta.listCuentaUnifDes" >
	     	   		<dt><bean:message bundle="gde" key="gde.liqDeudaAdapter.unificacionesDesgloses.title"/>: </dt> 
					
					<logic:iterate id="CuentaUnifDes" name="estadoCuentaAdapterVO" property="cuenta.listCuentaUnifDes">
						<dd>
				      		<!-- Degloses y Unificaciones --> 
				      			<bean:message bundle="gde" key="gde.liqDeudaAdapter.unificacionesDesgloses.cuenta.label"/>
						      	<bean:write name="CuentaUnifDes" property="nroCuenta"/>
						</dd>
					</logic:iterate>
				</logic:notEmpty>
			</dl>
	</fieldset>
	<!-- Fin LiqCuenta -->
	
	<!-- CuentasRel -->
	<logic:notEmpty name="estadoCuentaAdapterVO" property="listCuentaRel" >
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuentasRelacionadas.title"/> </legend>			
			<ul>
				<logic:iterate id="CuentaRel" name="estadoCuentaAdapterVO" property="listCuentaRel">
					<li>
						<!-- Cuentas Relacionadas al Objeto Imponible -->
				      		<bean:write name="CuentaRel" property="nroCuenta"/> -
					      		<bean:write name="CuentaRel" property="desCategoria"/> -
					      		<bean:write name="CuentaRel" property="desRecurso"/>
					</li>
				</logic:iterate>
			</ul>		
		</fieldset>
	</logic:notEmpty>
	<!-- Fin CuentasRel -->
		
	<!-- Exencion - Caso Solical - Otro -->
		<bean:define id="DeudaAdapterVO" name="estadoCuentaAdapterVO"/>
		<%@ include file="/gde/gdeuda/includeExenciones.jsp" %>
	<!-- Fin Exencion - Caso Solical - Otro -->
			
	<!-- listDeuda -->
		<div class="horizscroll">
		    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
		    	<logic:notEmpty name="estadoCuentaAdapterVO" property="listDeudaPagoDeuda">
			    	<caption><bean:message bundle="gde" key="ges.estadoCuenta.listaDeudas.title"/></caption>
			      	<tbody>
				       	<tr>
				       		<th align="left">&nbsp;</th><!-- ver -->
						  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.periodoDeuda"/></th>
						  	<th align="left"><bean:message bundle="gde" key="gde.estadoCuenta.viaDeuda.label"/></th>
						  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.fechaVto"/></th>
						  	<th align="left"><bean:message bundle="gde" key="gde.estadoCuenta.pago.importe.label"/></th>
						  	<th align="left"><bean:message bundle="gde" key="gde.estadoCuenta.pago.saldo.label"/></th>
						  	<th align="left"><bean:message bundle="gde" key="gde.estadoCuenta.deuda.actualizacion.label"/></th>
						  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total"/></th>
						  	<th align="left"><bean:message bundle="gde" key="gde.estadoCuenta.estado.label"/></th>						  	
						</tr>
						
						<!-- Item LiqDeudaVO -->
						<logic:iterate id="LiqDeudaPagoDeudaVO" name="estadoCuentaAdapterVO" property="listDeudaPagoDeuda">
							<tr>
								<!-- Ver detalle Deuda -->
				  				<td>
			  						<logic:equal name="estadoCuentaAdapterVO" property="verEnabled" value="enabled">
						  				<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verDetalleDeuda', '<bean:write name="LiqDeudaPagoDeudaVO" property="deuda.idDeuda" bundle="base" formatKey="general.format.id"/>-<bean:write name="LiqDeudaPagoDeudaVO" property="deuda.idEstadoDeuda" bundle="base" formatKey="general.format.id"/>');">
							    	    	<img title="Ver" src="<%=request.getContextPath()%>/images/iconos/selec0.gif" border="0"/>
						    	    	</a>
				  					</logic:equal>
					  				<logic:notEqual name="estadoCuentaAdapterVO" property="verEnabled" value="enabled">
					  					<img title="Ver" src="<%=request.getContextPath()%>/images/iconos/selec1.gif" border="0"/>
					  				</logic:notEqual>
					  			</td>
					  			<td><bean:write name="LiqDeudaPagoDeudaVO" property="deuda.periodoDeuda"/>&nbsp;</td>
					  			<td><bean:write name="LiqDeudaPagoDeudaVO" property="deuda.desViaDeuda"/>&nbsp;</td>
						        <td><bean:write name="LiqDeudaPagoDeudaVO" property="deuda.fechaVto"/>&nbsp;</td>
						        <td><bean:write name="LiqDeudaPagoDeudaVO" property="deuda.importe" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        <td><bean:write name="LiqDeudaPagoDeudaVO" property="deuda.saldo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        <td><bean:write name="LiqDeudaPagoDeudaVO" property="deuda.actualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        <td><bean:write name="LiqDeudaPagoDeudaVO" property="deuda.total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
								<td><bean:write name="LiqDeudaPagoDeudaVO" property="deuda.desEstado"/>&nbsp;</td>
						        
						       	<logic:notEmpty name="LiqDeudaPagoDeudaVO" property="listPagoDeuda">
							        </tr>
							        <tr>
							        	<td>&nbsp;</td>
							        	<td colspan="8">
							        		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
							        			<tbody>
							        				<tr>
		        									  	<th align="left" width="16%"><bean:message bundle="gde" key="gde.estadoCuenta.pago.fechaPago.label"/></th>
													  	<th align="left" width="29"><bean:message bundle="gde" key="gde.estadoCuenta.pago.tipoPago.label"/></th>
													  	<th align="left" width="10%"><bean:message bundle="gde" key="gde.estadoCuenta.pago.importe.label"/></th>						  	
													  	<th align="left" width="45%"><bean:message bundle="gde" key="gde.estadoCuenta.pago.descripcion.label"/></th>
							        				</tr>
										       		<logic:iterate id="LiqPagoDeudaVO" name="LiqDeudaPagoDeudaVO" property="listPagoDeuda">
														<tr>
															<td><bean:write name="LiqPagoDeudaVO" property="fechaPagoView"/>&nbsp;</td>
													        <td><bean:write name="LiqPagoDeudaVO" property="desTipoPago"/>&nbsp;</td>
													        <td><bean:write name="LiqPagoDeudaVO" property="importeTotalView"/>&nbsp;</td>
													        <td><bean:write name="LiqPagoDeudaVO" property="descripcion"/>&nbsp;</td>
												       	</tr>							        				
												    </logic:iterate>
							        			</tbody>
							        		</table>
							        	</td>
							        </tr>							       		
								  </logic:notEmpty>  
						</logic:iterate>
						<!-- Fin Item LiqDeudaVO -->
					</tbody>
				</logic:notEmpty>
				<logic:empty name="estadoCuentaAdapterVO" property="listDeudaPagoDeuda">
			    	<caption><bean:message bundle="gde" key="ges.estadoCuenta.listaDeudas.title"/></caption>
			      	<tbody>
				       	<tr><td align="center"><bean:message bundle="gde" key="gde.estadoCuenta.listaDeudas.vacia"/></td></tr>
				    </tbody>
				</logic:empty>       					
			</table>
		</div>
	<!-- Fin listDeuda -->
	
	<logic:notEqual name="userSession" property="isAnonimoView" value="1">
	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left" width="50%">
				<!-- Volver -->			
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volverFromDetalle', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<td align="right" width="50%">
				<!-- Imprimir -->
				<button type="button" name="btnImprimir" class="boton" onclick="submitForm('irImprimir', '');">
			  	    <bean:message bundle="gde" key="gde.liqEstadoCuentaAdapter.button.imprimir"/>
				</button>			
			</td>
			
		</tr>
	</table>		
	</logic:notEqual>
	
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->
