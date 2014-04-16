<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript">
   	function calcTotal(inx){
		
		var montoImponible = document.getElementById('montoImponible' + inx );
		var alicuota = document.getElementById('alicuota' + inx);		
		var total = document.getElementById('total' + inx);
		
		if (!isValidDecimal(montoImponible.value)){
			alert("Formato de Monto Imponible incorrecto");
			privGen.focus();
			return;
		}
		
		if (!isValidDecimal(alicuota.value)){
			alert("Formato de Alicuota incorrecto");
			privEsp.focus();
			return;
		}
		
		var vtotal = (montoImponible.value * 1) * (alicuota.value * 1); 
		
		if (vtotal == 0)
			total.innerHTML = "$ 0.00";
		else
			total.innerHTML = "$ " + vtotal.toFixed(2);
		
	}
	
	function isValidDecimal(value) {

        if (value == '')
			return(true);

        return /^\d+(\.\d+)?$|^\.\d+$/.test(value);
    }
    
</script>
    
<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarLiqDecJur.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<!-- Para la web lo muestro, para usuario Interno NO -->
	<logic:equal name="userSession" property="isAnonimoView" value="1">
		<%@ include file="/gde/gdeuda/includeDivButtons.jsp" %>
	</logic:equal>	
	
	
	<h1><bean:message bundle="gde" key="gde.liqDecJurAdapterInit.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p><bean:message bundle="gde" key="gde.liqDecJurAdapter.detalle.legend"/></p>
			</td>				
		</tr>
	</table>

	<fieldset>
		<legend>
			<bean:message bundle="gde" key="gde.liqDeudaAdapter.cuentaSeleccionada"/>: 
			<bean:write name="liqDecJurAdapterVO" property="cuenta.nroCuenta"/>
		</legend>
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuenta.desRecurso.label"/>:</label>
	      		<bean:write name="liqDecJurAdapterVO" property="cuenta.desRecurso"/>
			</p>
	</fieldset>

	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqDecJurAdapter.detalle.title"/> </legend>
		
		<p>
		
		<div style="overflow:auto; width:700px; height:350px;">	
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<caption><bean:message bundle="gde" key="gde.liqDecJurAdapter.detalle.title"/></caption>
		    	<tbody>
					<logic:notEmpty name="liqDecJurAdapterVO" property="listDetalle">
				    	
				    	<tr>
							<!-- Chk -->
							<th>
								<input type="checkbox" onclick="changeChk('filter', 'listIdDetalleSelected', this)"/>
							</th> 
							<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.periodo.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.actividad.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.montoImponible.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.alicuota.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.total1.label"/></th>
							
							<logic:equal name="liqDecJurAdapterVO" property="esDReI" value="true">
								<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.cantUni.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.unidad.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.tipoUnidad.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.valorUnit.label"/></th>
							</logic:equal>
							<logic:equal name="liqDecJurAdapterVO" property="esEtur" value="true">
								<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.minimo.label"/></th>
							</logic:equal>
							<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.total2.label"/></th>
						</tr>
				    	
						<logic:iterate id="DetalleVO" name="liqDecJurAdapterVO" property="listDetalle">
				
							<tr>
								<!-- Chk -->
								<td>
									<html:multibox name="liqDecJurAdapterVO" property="listIdDetalleSelected" >
                    	            	<bean:write name="DetalleVO" property="idView"/>
                        	        </html:multibox>
								</td>
								<td><bean:write name="DetalleVO" property="periodoAnioView"/>&nbsp;</td>
								<td title='<bean:write name="DetalleVO" property="actividad.codYDescripcion"/>'>
									<bean:write name="DetalleVO" property="actividad.codYDescripcionCorto"/>&nbsp;
								</td>
								<td> 
									<input type="text" size="10" maxlength="15" 
										name='montoImponible<bean:write name="DetalleVO" property="idView"/>'
										id='montoImponible<bean:write name="DetalleVO" property="idView"/>'
										value='<bean:write name="DetalleVO" property="montoImponibleView" />' 
										onchange='calcTotal(<bean:write name="DetalleVO" property="idView"/>);'
									/>
								</td>
								<td>
									<input type="hidden"
										name='alicuota<bean:write name="DetalleVO" property="idView"/>'
										id='alicuota<bean:write name="DetalleVO" property="idView"/>'
										value='<bean:write name="DetalleVO" property="alicuotaView" />'
										onchange='calcTotal(<bean:write name="DetalleVO" property="idView"/>);'
									/>
									<bean:write name="DetalleVO" property="alicuotaView" />&nbsp;
								</td>
								<!-- SubTotal1 -->
								<td>
									<div id='total<bean:write name="DetalleVO" property="idView"/>'>
										$&nbsp;<bean:write name="DetalleVO" property="subTotal1View"/>
									</div>								
								</td>
								<!-- Para Drei -->
								<logic:equal name="liqDecJurAdapterVO" property="esDReI" value="true">
									<td>
										<bean:write name="DetalleVO" property="cantUniView"/>&nbsp;								
									</td>
									<td>
										<bean:write name="DetalleVO" property="unidad.nomenclatura"/>&nbsp;
									</td>
									<td title='<bean:write name="DetalleVO" property="tipoUnidad.codYDescripcion"/>'>
										<bean:write name="DetalleVO" property="tipoUnidad.codYDescripcionCorto"/>&nbsp;
									</td>
								</logic:equal>

								<!-- Para Ambos, Drei=Valor Unit    Etur=Minimo -->
								<td>
									<bean:write name="DetalleVO" property="valorUniView"/>&nbsp;								
								</td>
								
								<td>
									$&nbsp;<bean:write name="DetalleVO" property="subTotal2View"/>&nbsp;
								</td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
				
					</tbody>
				</table>
			</div>
		</p>
		
		<a name="unidad">&nbsp;</a> 
		
		<table class="tramonline">
			<caption><bean:message bundle="gde" key="gde.liqDecJurAdapter.alicuotas"/></caption>
			<tbody>
			<tr>
				<td>
					<label><bean:message bundle="gde" key="gde.liqDecJurAdapter.alicuota"/>:</label>

					<html:select name="liqDecJurAdapterVO" property="alicuota.id" styleClass="select" style="width:100px">					
						<html:optionsCollection name="liqDecJurAdapterVO" property="listAlicuota" label="alicuotaView" value="id" filter="false"/>
					</html:select>
				
					<button type="button" name="btnAddCant" class="boton" onclick="submitForm('agregarAlicuota', '');">
			  	    	<bean:message bundle="gde" key="gde.liqDecJurAdapter.button.agregarAlicuota"/>
					</button>	
				
				</td>
			</tr>
			</tbody>
		</table>
		
		<br><br>
		
		<logic:equal name="liqDecJurAdapterVO" property="esDReI" value="true">
			<table class="tramonline">
				<caption><bean:message bundle="gde" key="gde.liqDecJurAdapter.unidades"/></caption>
				<tbody>
				<tr>
					<td>
						<label><bean:message bundle="gde" key="gde.liqDecJurAdapter.cantUni"/>:</label>
						<html:text name="liqDecJurAdapterVO" property="cantUniView" size="5"/>
						
						<label><bean:message bundle="gde" key="gde.liqDecJurAdapter.unidad"/>:</label>
						<html:select name="liqDecJurAdapterVO" property="unidad.id" styleClass="select" onchange="submitForm('paramUnidad','');">					
							<html:optionsCollection name="liqDecJurAdapterVO" property="listUnidad" label="nomenclatura" value="id"/>
						</html:select>
	
						<label><bean:message bundle="gde" key="gde.liqDecJurAdapter.tipoUnidad"/>:</label>
						<html:select name="liqDecJurAdapterVO" property="tipoUnidad.id" styleClass="select" style="width:100px">					
							<html:optionsCollection name="liqDecJurAdapterVO" property="listTipoUnidad" label="codYDescripcion" value="id"/>
						</html:select>
					
						<button type="button" name="btnAddCant" class="boton" onclick="submitForm('agregarCantidad', '');">
				  	    	<bean:message bundle="gde" key="gde.liqDecJurAdapter.button.agregarCantidad"/>
						</button>	
					
					</td>
				</tr>
				</tbody>
			</table>
		</logic:equal>
		
		<logic:equal name="liqDecJurAdapterVO" property="esEtur" value="true">
			<table class="tramonline">
				<caption><bean:message bundle="gde" key="gde.liqDecJurAdapter.minimos"/></caption>
				<tbody>
				<tr>
					<td>
						<label><bean:message bundle="gde" key="gde.liqDecJurAdapter.minimo"/>:</label>
	
						<html:select name="liqDecJurAdapterVO" property="minimo.id" styleClass="select" style="width:100px">					
							<html:optionsCollection name="liqDecJurAdapterVO" property="listMinimo" label="valorUnitarioView" value="id" filter="false"/>
						</html:select>
					
						<button type="button" name="btnAddCant" class="boton" onclick="submitForm('agregarMinimo', '');">
				  	    	<bean:message bundle="gde" key="gde.liqDecJurAdapter.button.agregarMinimo"/>
						</button>	
					
					</td>
				</tr>
				</tbody>
			</table>
		</logic:equal>
	</fieldset>
	
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
				<!-- Volver -->
				<button type="button" name="btnAtras" class="boton" onclick="submitForm('irInit', '');">
			  	    <bean:message bundle="gde" key="gde.liqDecJurAdapter.atras"/>
				</button>
   	    	</td>
   	    	<td align="right" width="50%">
				<html:button property="btnSiguiente"  styleClass="boton" onclick="submitForm('irGeneral', '');">
					<bean:message bundle="gde" key="gde.liqDecJurAdapter.siguiente"/>
				</html:button>
   	    	</td>   	    	
   	    </tr>
   	</table>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin formulario -->

<script type="text/javascript">
	function irAUnidad() {
   		document.location = document.URL + '#unidad';
	}
</script>

<logic:equal name="liqDecJurAdapterVO" property="irUnidad" value="true">
	<script type="text/javascript">irAUnidad();</script>
</logic:equal>