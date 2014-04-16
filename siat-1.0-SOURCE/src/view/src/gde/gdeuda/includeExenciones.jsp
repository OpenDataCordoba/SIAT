<!-- 
	Inclucion de datos de las Exenciones Otorgadas, Denegadas y en Tramite
	Recibe un LiqDeudaAdapter
-->	
	
	
	<!-- Exencion - Caso Solical - Otro -->
	<logic:equal name="DeudaAdapterVO" property="exenciones.poseeExencion" value="true" >
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.liqDeudaAdapter.exenciones.title"/></legend>
			<!-- Exenciones Vigentes -->
			<logic:notEmpty name="DeudaAdapterVO" property="exenciones.listExeVigentes">
				<dl class="listabloqueSiat">     	   
     	   			<dt><bean:message bundle="gde" key="gde.liqDeudaAdapter.exencionesVigentes.title"/>:</dt>

					<logic:iterate id="Exencion" name="DeudaAdapterVO" property="exenciones.listExeVigentes">
     	   				<dd>
     	   					<label><bean:write name="Exencion" property="desExencion"/> </label>  -&nbsp;
							<label><bean:message bundle="gde" key="gde.liqDeudaAdapter.exenciones.vigencia.label"/> </label>
							<bean:write name="Exencion" property="fechaDesde"/>  -&nbsp;
							<bean:write name="Exencion" property="fechaHasta"/> &nbsp;

							<logic:equal name="Exencion" property="poseeCaso" value="true">
								<label><bean:message bundle="gde" key="gde.liqDeudaAdapter.exenciones.caso.label"/></label>
								<bean:write name="Exencion" property="caso.sistemaOrigen.desSistemaOrigen"/>
								&nbsp;
								<bean:write name="Exencion" property="caso.numero"/>
							</logic:equal>
						</dd>
					</logic:iterate>
				</dl>
			</logic:notEmpty>
			
			<!-- Exenciones Denegadas -->
			<logic:notEmpty name="DeudaAdapterVO" property="exenciones.listExeDenegados">
				<dl class="listabloqueSiat">
					<dt><bean:message bundle="gde" key="gde.liqDeudaAdapter.exencionesDenegadas.title"/>:</dt>

					<logic:iterate id="SolExeDen" name="DeudaAdapterVO" property="exenciones.listExeDenegados">
	    	   			<dd>
	    	   				<label><bean:write name="SolExeDen" property="desExencion"/></label> -&nbsp;
							<label><bean:message bundle="gde" key="gde.liqDeudaAdapter.exenciones.fechaSolicitud.label"/></label>
							<bean:write name="SolExeDen" property="fechaSolicitud"/>&nbsp;
							
							<logic:equal name="SolExeDen" property="poseeCaso" value="true">
								<label><bean:message bundle="gde" key="gde.liqDeudaAdapter.exenciones.caso.label"/></label>
								<bean:write name="SolExeDen" property="caso.sistemaOrigen.desSistemaOrigen"/>
								&nbsp;
								<bean:write name="SolExeDen" property="caso.numero"/>								
							</logic:equal>
						</dd>
					</logic:iterate>
				</dl>
			</logic:notEmpty>
			
			<!-- Exenciones En Tramite (Solicitud Exencion) -->
			<logic:notEmpty name="DeudaAdapterVO" property="exenciones.listExeTramite">
				<dl class="listabloqueSiat">     	   
     	   			<dt><bean:message bundle="gde" key="gde.liqDeudaAdapter.exencionesEnTramite.title"/>:</dt>
     			
					<logic:iterate id="SolExeET" name="DeudaAdapterVO" property="exenciones.listExeTramite">
	    	   			<dd>
		    	   			<label><bean:write name="SolExeET" property="desExencion"/></label> -&nbsp;
							<label><bean:message bundle="gde" key="gde.liqDeudaAdapter.exenciones.fechaSolicitud.label"/></label>
							<bean:write name="SolExeET" property="fechaSolicitud"/>&nbsp;
							
							<logic:equal name="SolExeET" property="poseeCaso" value="true">
								<label><bean:message bundle="gde" key="gde.liqDeudaAdapter.exenciones.caso.label"/></label>
								<bean:write name="SolExeET" property="caso.sistemaOrigen.desSistemaOrigen"/>
								&nbsp;
								<bean:write name="SolExeET" property="caso.numero"/>
							</logic:equal>
						</dd>
					</logic:iterate>
				</dl>
			</logic:notEmpty>
						
		</fieldset>
	</logic:equal>
	<!-- Fin Exencion - Caso Solical - Otro -->