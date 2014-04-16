<!-- 
	Inclucion de datos de la cuenta.
	Recibe un adapter que debe estar compuesto por un LiqCuentaVO
	
	y debe contener las siguientes banderas de permisos:
		- verDetalleObjImpEnabled
		- verDeudaContribEnabled
		- verHistoricoContribEnabled
		- verConvenioEnabled
		- verCuentaDesgUnifEnabled
 -->

	<!-- LiqCuenta -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuentaSeleccionada"/>: 
			<bean:write name="DeudaAdapterVO" property="cuenta.nroCuenta"/>
		</legend>
			<!-- Mensaje por si la cuenta esta inactiva (en estado CREADA, o con fecha baja caduca) -->
			<logic:equal name="DeudaAdapterVO" property="cuenta.esActiva" value="false">
				<div id="messagesStruts" class="messages"> 
		        <bean:message bundle="pad" key="pad.cuenta.noVigente"/>
			  	</div>
			</logic:equal>

			<!-- Tributo -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuenta.desRecurso.label"/>:</label>
	      		<bean:write name="DeudaAdapterVO" property="cuenta.desRecurso"/>
			</p>

	        <!-- CUIT - Denominacion de titular principal -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuenta.desTitularPrincipal.label"/>:</label>
	            <logic:notEmpty name="DeudaAdapterVO" property="cuenta.cuitTitularPrincipal">
				  <bean:write name="DeudaAdapterVO" property="cuenta.cuitTitularPrincipal"/>&nbsp;/&nbsp;
                </logic:notEmpty>
                <bean:write name="DeudaAdapterVO" property="cuenta.nombreTitularPrincipal"/>
			</p>

			<!-- CodigoGestionPersonal -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuenta.codGestionPersonal.label"/>:</label>
	      		<bean:write name="DeudaAdapterVO" property="cuenta.codGestionPersonal"/>
			</p>

			<!-- Atributos del Objeto Imponible -->
			<logic:notEmpty name="DeudaAdapterVO" property="cuenta.listAtributoObjImp" >
				<logic:iterate id="AtrVal" name="DeudaAdapterVO" property="cuenta.listAtributoObjImp">
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
					    <logic:notEqual name="DeudaAdapterVO" property="cuenta.codTipObjImp" value="COMERCIO">
							<logic:equal name="AtrVal" property="key" value="Ubicacion">
						        <!-- Domicilio Envio de cuenta -->
						        &nbsp;
					      		<label><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuenta.desDomEnv.label"/>:</label>
				                <bean:write name="DeudaAdapterVO" property="cuenta.desDomEnv"/>
							</logic:equal>
						</logic:notEqual>
					</p>
				</logic:iterate>
			</logic:notEmpty>
			
			<!-- Atributos de la Cuenta -->
			<logic:notEmpty name="DeudaAdapterVO" property="cuenta.listAtributoCuenta" >
				<logic:iterate id="AtrVal" name="DeudaAdapterVO" property="cuenta.listAtributoCuenta">
					<p>
			      		<label><bean:write name="AtrVal" property="label"/>:</label>
			      		<bean:write name="AtrVal" property="value"/>
					</p>
				</logic:iterate>
			</logic:notEmpty>
			
			<!-- Titulares -->
			<dl class="listabloqueSiat">
	     	   	<dt><bean:message bundle="gde" key="gde.liqDeudaAdapter.titulares.title"/>: </dt>
				<logic:notEmpty name="DeudaAdapterVO" property="cuenta.listTitular" >
					<logic:iterate id="Titular" name="DeudaAdapterVO" property="cuenta.listTitular">
						<dd>
							<logic:equal name="DeudaAdapterVO" property="verDeudaContribEnabled" value="true">
								<logic:equal name="Titular" property="existePersona" value="true">
									<!-- Permitido ver cuentas del titular -->
									<a href="/siat/gde/AdministrarLiqDeuda.do?method=verDeudaContrib&id=<bean:write name="Titular" property="idTitular" bundle="base" formatKey="general.format.id"/>&cuentaId=<bean:write name="DeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>">
					      				<bean:write name="Titular" property="desTitular"/>
					      			</a>
					      		</logic:equal>
					      		<logic:notEqual name="Titular" property="existePersona" value="true">
					      			<!-- Persona no existe -->
					      			<bean:write name="Titular" property="desTitular"/>					      		
					      		</logic:notEqual>	
				      		</logic:equal>
				      		<logic:notEqual name="DeudaAdapterVO" property="verDeudaContribEnabled" value="true">
				      			<!-- No Permitido ver cuentas del titular -->
				      			<bean:write name="Titular" property="desTitular"/>
				      		</logic:notEqual>
				      		<logic:equal name="Titular" property="existePersona" value="true">
								<logic:equal name="DeudaAdapterVO" property="buzonCambiosEnabled" value="true">
					      		<!-- Buzon de Cambio de datos del persona -->
									<button type="button" name="btnBuzonCambios" class="verfoto" onclick="submitForm('buzonCambios', '<bean:write name="Titular" property="idTitular" bundle="base" formatKey="general.format.id"/>');">
			  							<bean:message bundle="pad" key="pad.button.buzonCambios"/>
									</button>
					      		</logic:equal>
					      	</logic:equal>	
						</dd>
					</logic:iterate>
				</logic:notEmpty>
			</dl>
			
			<!-- Atributos del Contribuyente -->
			<dl class="listahorizontalSiat">
				<logic:notEmpty name="DeudaAdapterVO" property="cuenta.listAtributoContr" >
					<logic:iterate id="ConAtrVal" name="DeudaAdapterVO" property="cuenta.listAtributoContr">						
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
				<logic:notEmpty name="DeudaAdapterVO" property="cuenta.listConvenio" >
	     	   		<dt><bean:message bundle="gde" key="gde.liqDeudaAdapter.conveniosAsociados.title"/>: </dt> 
					
					<logic:iterate id="Convenio" name="DeudaAdapterVO" property="cuenta.listConvenio">
						<dd>
							<!-- Permitido ver Convenios Asociados  -->
							<logic:equal name="DeudaAdapterVO" property="verConvenioEnabled" value="true">
					      		<a href="/siat/gde/AdministrarLiqDeuda.do?method=verConvenio&selectedId=<bean:write name="Convenio" property="idConvenio" bundle="base" formatKey="general.format.id"/>&cuentaId=<bean:write name="DeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>" >
						      		<bean:write name="Convenio" property="nroConvenio"/> -
						      		<bean:write name="Convenio" property="desPlan"/> -
						      		<bean:write name="Convenio" property="desViaDeuda"/>
					      		</a>
							</logic:equal>
							<!-- No Permitido ver Convenios Asociados  -->
							<logic:notEqual name="DeudaAdapterVO" property="verConvenioEnabled" value="true">
								<bean:write name="Convenio" property="nroConvenio"/> -
						      	<bean:write name="Convenio" property="desPlan"/> -
						      	<bean:write name="Convenio" property="desViaDeuda"/>
							</logic:notEqual>
						</dd>
					</logic:iterate>
				</logic:notEmpty>
			</dl>
			
			<!-- Convenios Recompuestos -->
			<dl class="listabloqueSiat">
				<logic:notEmpty name="DeudaAdapterVO" property="cuenta.listConvenioRecompuesto" >
	     	   		<dt><bean:message bundle="gde" key="gde.liqDeudaAdapter.conveniosRecompuestos.title"/>: </dt> 
					
					<logic:iterate id="Convenio" name="DeudaAdapterVO" property="cuenta.listConvenioRecompuesto">
						<dd>
							<!-- Permitido ver Convenios Asociados  -->
							<logic:equal name="DeudaAdapterVO" property="verConvenioEnabled" value="true">
					      		<a href="/siat/gde/AdministrarLiqDeuda.do?method=verConvenio&selectedId=<bean:write name="Convenio" property="idConvenio" bundle="base" formatKey="general.format.id"/>&cuentaId=<bean:write name="DeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>" >
						      		<bean:write name="Convenio" property="nroConvenio"/> -
						      		<bean:write name="Convenio" property="desPlan"/> -
						      		<bean:write name="Convenio" property="desViaDeuda"/>
					      		</a>
							</logic:equal>
							<!-- No Permitido ver Convenios Asociados  -->
							<logic:notEqual name="DeudaAdapterVO" property="verConvenioEnabled" value="true">
								<bean:write name="Convenio" property="nroConvenio"/> -
						      	<bean:write name="Convenio" property="desPlan"/> -
						      	<bean:write name="Convenio" property="desViaDeuda"/>
							</logic:notEqual>
						</dd>
					</logic:iterate>
				</logic:notEmpty>
			</dl>
			
			<!-- Cuentas Vinculadas -->
			<dl class="listabloqueSiat">
				<logic:notEmpty name="DeudaAdapterVO" property="cuenta.listCuentaUnifDes" >
	     	   		<dt><bean:message bundle="gde" key="gde.liqDeudaAdapter.unificacionesDesgloses.title"/>: </dt> 
					
					<logic:iterate id="CuentaUnifDes" name="DeudaAdapterVO" property="cuenta.listCuentaUnifDes">
						<dd>
							<!-- Permitido ver Degloses y Unificaciones -->
							<logic:equal name="DeudaAdapterVO" property="verCuentaDesgUnifEnabled" value="true"> 
					      		<a href="/siat/gde/AdministrarLiqDeuda.do?method=verCuentaDesgUnif&selectedId=<bean:write name="CuentaUnifDes" property="idCuenta" bundle="base" formatKey="general.format.id"/>" >
						      		<bean:message bundle="gde" key="gde.liqDeudaAdapter.unificacionesDesgloses.cuenta.label"/>
						      		<bean:write name="CuentaUnifDes" property="nroCuenta"/> - 
					      			<bean:write name="CuentaUnifDes" property="desRecurso"/>
					      		</a>
							</logic:equal>
				      		<!-- No Permitido ver Degloses y Unificaciones -->
				      		<logic:notEqual name="DeudaAdapterVO" property="verCuentaDesgUnifEnabled" value="true"> 
				      			<bean:message bundle="gde" key="gde.liqDeudaAdapter.unificacionesDesgloses.cuenta.label"/>
						      	<bean:write name="CuentaUnifDes" property="nroCuenta"/> -
					      		<bean:write name="CuentaUnifDes" property="desRecurso"/>
				      		</logic:notEqual>
						</dd>
					</logic:iterate>
				</logic:notEmpty>
			</dl>
			
			<logic:equal name="DeudaAdapterVO" property="cuenta.esRecursoFiscalizable" value="true">
				<dl class="listabloqueSiat">
					<logic:notEmpty name="DeudaAdapterVO" property="cuenta.listOrdenControl">
		     	   		<dt><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuenta.ordenFiscal.label"/>: </dt> 
						
						<logic:iterate id="OrdenControl" name="DeudaAdapterVO" property="cuenta.listOrdenControl">
							<dd> 
							    <bean:write name="OrdenControl" property="ordenControlyTipoView"/>
							</dd>
						</logic:iterate>
					</logic:notEmpty>
				</dl>
			</logic:equal>
			
			<!-- Estado: para DreI -->
			<logic:notEmpty name="DeudaAdapterVO" property="cuenta.desEstado">
				<p>
		      		<label><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuenta.desEstado.label"/>:</label>
		      		<bean:write name="DeudaAdapterVO" property="cuenta.desEstado"/><bean:write name="DeudaAdapterVO" property="cuenta.expedienteCierre"/>
				</p>
			</logic:notEmpty>
			
			<logic:notEmpty name="DeudaAdapterVO" property="cuenta.observacion">
				<p>
		      		<label><bean:message bundle="pad" key="pad.cuenta.observacion.label"/>:</label>
		      		<bean:write name="DeudaAdapterVO" property="cuenta.observacion"/>
				</p>
			</logic:notEmpty>
			
	</fieldset>
	<!-- Fin LiqCuenta -->
