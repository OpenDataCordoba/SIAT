<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript">
   	function calcTotal(inx){
		
		var determinado = document.getElementById('determinado' + inx );
		var adicPub = document.getElementById('adicPub' + inx);		
		var adicOtro = document.getElementById('adicOtro' + inx);
		var retencion = document.getElementById('retencion' + inx);
		var pago = document.getElementById('pago' + inx);
		var declarado = document.getElementById('declarado' + inx);
		var enConvenio = document.getElementById('enConvenio' + inx);
		var total = document.getElementById('total' + inx);
		
		if (!isValidDecimal(pago.value)){
			alert("Formato del Pago incorrecto");
			pago.focus();
			return;
		}
		
		var vtotal = (determinado.value * 1) + (adicPub.value * 1) + (adicOtro.value * 1); 
		
		vtotal -= (retencion.value * 1);
		
		var vdeuda = (declarado.value * 1) + (enConvenio.value * 1);  
		
		//El total no puede ser negativo
		if ((pago.value * 1) >= vtotal || vdeuda >= vtotal){
			vtotal = 0;
		} else {
			// Seteamos el mayor de los dos valores, si son iguales, queda uno de los dos :)
			if ((pago.value * 1) > vdeuda){
				vtotal -= (pago.value * 1);
			} else {
				vtotal -= vdeuda;
			}	
		}		
		
		
		
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
				<p><bean:message bundle="gde" key="gde.liqDecJurAdapter.general.legend"/></p>
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
		<legend><bean:message bundle="gde" key="gde.liqDecJurAdapter.general.title"/> </legend>
		
		<p>
		
		<div style="overflow:auto; width:700px; height:350px;">	
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="gde" key="gde.liqDecJurAdapter.general.title"/></caption>
		    	<tbody>
					<logic:notEmpty name="liqDecJurAdapterVO" property="listGeneral">
				    	<tr>
							<!-- Chk -->
							<th>
								<input type="checkbox" onclick="changeChk('filter', 'listIdGeneralSelected', this)"/>
							</th> 
							<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.periodo.label"/></th>
							<logic:equal name="liqDecJurAdapterVO" property="esDReI" value="true">
								<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.cantPer.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.minCantPer.label"/></th>
							</logic:equal>
							<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.determinado.label"/></th>

							<logic:equal name="liqDecJurAdapterVO" property="esDReI" value="true">
								<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.adicPub.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.adicOtro.label"/></th>
							</logic:equal>

							<th align="left" colspan="2" ><bean:message bundle="gde" key="gde.liqDecJurAdapter.retencion.label"/></th>
							
							<logic:equal name="liqDecJurAdapterVO" property="esDReI" value="true">
								<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.pago.label"/></th>
							</logic:equal>
							
							<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.declarado.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.enConvenio.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.total.label"/></th>
						</tr>
						<logic:iterate id="DetalleVO" name="liqDecJurAdapterVO" property="listGeneral">
				
							<tr>
								<!-- Chk -->
								<td>
									<html:multibox name="liqDecJurAdapterVO" property="listIdGeneralSelected" >
                    	            	<bean:write name="DetalleVO" property="idView"/>
                        	        </html:multibox>
								</td>
								<td><bean:write name="DetalleVO" property="periodoAnioView"/>&nbsp;</td>
								
								<logic:equal name="liqDecJurAdapterVO" property="esDReI" value="true">
									<td><bean:write name="DetalleVO" property="cantPerView"/>&nbsp;</td>
									<td><bean:write name="DetalleVO" property="minCantPerView"/>&nbsp;</td>
								</logic:equal>
								
								<td><bean:write name="DetalleVO" property="determinadoView"/>&nbsp;</td>

								<logic:equal name="liqDecJurAdapterVO" property="esDReI" value="true">								
									<!-- Adicionl Pub -->
									<td>
										<bean:write name="DetalleVO" property="adicPubView"/>&nbsp;(<bean:write name="DetalleVO" property="porcAdicPubView"/>%)
									</td>
									<!-- Adicional Otros -->
									<td>
										<bean:write name="DetalleVO" property="adicOtroView"/>&nbsp;(<bean:write name="DetalleVO" property="porcAdicOtroView"/>%)
									</td>
								</logic:equal>	
								
								<logic:notEmpty name="DetalleVO" property="certificadosView">
									<td>
								</logic:notEmpty>
								<logic:empty name="DetalleVO" property="certificadosView">
									<td colspan="2">
								</logic:empty>
									<bean:write name="DetalleVO" property="retencionView"/>
								</td>
								<logic:notEmpty name="DetalleVO" property="certificadosView">
									<td title='<bean:write name="DetalleVO" property="certificadosView" />'>
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/constancia0.gif"/>
									</td>	
								</logic:notEmpty>

								<logic:equal name="liqDecJurAdapterVO" property="esDReI" value="true">									
									<td>
										<input type="hidden"
											name='determinado<bean:write name="DetalleVO" property="idView"/>'
											id='determinado<bean:write name="DetalleVO" property="idView"/>'
											value='<bean:write name="DetalleVO" property="determinadoView" />' />
											
										<input type="hidden"
											name='adicPub<bean:write name="DetalleVO" property="idView"/>'
											id='adicPub<bean:write name="DetalleVO" property="idView"/>'
											value='<bean:write name="DetalleVO" property="adicPubView" />' />
											
										<input type="hidden"
											name='adicOtro<bean:write name="DetalleVO" property="idView"/>'
											id='adicOtro<bean:write name="DetalleVO" property="idView"/>'
											value='<bean:write name="DetalleVO" property="adicOtroView" />' />	
										
										<input type="hidden"
											name='retencion<bean:write name="DetalleVO" property="idView"/>'
											id='retencion<bean:write name="DetalleVO" property="idView"/>'
											value='<bean:write name="DetalleVO" property="retencionView" />' />
										
										<input type="hidden"
											name='declarado<bean:write name="DetalleVO" property="idView"/>'
											id='declarado<bean:write name="DetalleVO" property="idView"/>'
											value='<bean:write name="DetalleVO" property="declaradoView" />' />	
										
										<input type="hidden"
											name='enConvenio<bean:write name="DetalleVO" property="idView"/>'
											id='enConvenio<bean:write name="DetalleVO" property="idView"/>'
											value='<bean:write name="DetalleVO" property="enConvenioView" />' />	
											
										<logic:equal name="liqDecJurAdapterVO" property="editPagoEnabled" value="enabled">
											<input type="text" size="10" maxlength="15"
												name='pago<bean:write name="DetalleVO" property="idView"/>'
												id='pago<bean:write name="DetalleVO" property="idView"/>'
												value='<bean:write name="DetalleVO" property="pagoView" />' 
												onchange='calcTotal(<bean:write name="DetalleVO" property="idView"/>);'
											/>
										</logic:equal>
										<logic:notEqual name="liqDecJurAdapterVO" property="editPagoEnabled" value="enabled">
											<input type="hidden"
												name='pago<bean:write name="DetalleVO" property="idView"/>'
												id='pago<bean:write name="DetalleVO" property="idView"/>'
												value='<bean:write name="DetalleVO" property="pagoView" />' 
											/>
											<bean:write name="DetalleVO" property="pagoView" />&nbsp;
										</logic:notEqual>
									</td>
								</logic:equal>
								
								<td>
									<bean:write name="DetalleVO" property="declaradoView"/>
								</td>
								<td>
									<bean:write name="DetalleVO" property="enConvenioView"/>
								</td>
								<td>
									<div id='total<bean:write name="DetalleVO" property="idView"/>'>
										$&nbsp;<bean:write name="DetalleVO" property="totalView"/>
									</div>
								</td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
				
					</tbody>
				</table>
			</div>
		</p>
		
		<logic:equal name="liqDecJurAdapterVO" property="esDReI" value="true">
			<table class="tramonline">
				<caption><bean:message bundle="gde" key="gde.liqDecJurAdapter.adicionales"/></caption>
				<tbody>
				<tr>
					<td>
						<!-- Adicional Publicidad -->
						<label><bean:message bundle="gde" key="gde.liqDecJurAdapter.aliPub"/>:</label>
	
						<html:select name="liqDecJurAdapterVO" property="aliPub.id" styleClass="select" style="width:100px">					
							<html:optionsCollection name="liqDecJurAdapterVO" property="listAliPub" label="alicuotaView" value="id" filter="false"/>
						</html:select>
						
						<button type="button" name="btnAddPub" class="boton" onclick="submitForm('agregarPub', '');">
				  	    	<bean:message bundle="gde" key="gde.liqDecJurAdapter.button.agregarPub"/>
						</button>
						
						&nbsp;&nbsp;
						
						<!-- Adicional Mesas y Sillas -->
						<label><bean:message bundle="gde" key="gde.liqDecJurAdapter.aliMyS"/>:</label>
						<html:select name="liqDecJurAdapterVO" property="aliMyS.id" styleClass="select" style="width:100px">					
							<html:optionsCollection name="liqDecJurAdapterVO" property="listAliMyS" label="alicuotaView" value="id" filter="false"/>
						</html:select>
						
						<button type="button" name="btnAddOtro" class="boton" onclick="submitForm('agregarOtro', '');">
				  	    	<bean:message bundle="gde" key="gde.liqDecJurAdapter.button.agregarOtro"/>
						</button>
					
					</td>
				</tr>
				</tbody>
			</table>
		
			<br><br>
		</logic:equal>	
		
		<table class="tramonline">
			<caption><bean:message bundle="gde" key="gde.liqDecJurAdapter.retenciones"/></caption>
			<tbody>
				<tr>
					<td>
						<label><bean:message bundle="gde" key="gde.liqDecJurAdapter.importeRetencion.label"/></label>
						<html:text name="liqDecJurAdapterVO" property="retencion.importeView" size="5"/>
	
						<label><bean:message bundle="gde" key="gde.liqDecJurAdapter.certificado.label"/>: </label>
						<html:text  name="liqDecJurAdapterVO" property="retencion.certificado" size="10"/>
					
						<label><bean:message bundle="gde" key="gde.liqDecJurAdapter.ageRet.label"/>: </label>
						<html:select name="liqDecJurAdapterVO" property="retencion.ageRet.id" styleClass="select" style="width:100px" >
							<html:optionsCollection name="liqDecJurAdapterVO" property="listAgeRet" label="desCuitView" value="id" />
						</html:select>
						
						<button type="button" name="btnImputar" class="boton" onclick="submitForm('imputarRetencion', '');">
			  	    	<bean:message bundle="gde" key="gde.liqDecJurAdapter.button.imputarRetencion"/>
					</button>
					</td>
				</tr>
			</tbody>
		</table>
		
	</fieldset>
	
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
				<!-- Volver -->
				<button type="button" name="btnAtras" class="boton" onclick="submitForm('volverDetalle', '');">
			  	    <bean:message bundle="gde" key="gde.liqDecJurAdapter.atras"/>
				</button>
   	    	</td>
   	    	<td align="right" width="50%">
				<html:button property="btnSiguiente"  styleClass="boton" onclick="submitForm('simular', '');">
					<bean:message bundle="gde" key="gde.liqDecJurAdapter.simular"/>
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