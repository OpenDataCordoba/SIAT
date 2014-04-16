<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/afi/AdministrarLocal.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="afi" key="afi.localViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Datos Generales del Local -->
		<fieldset>
			<legend><bean:message bundle="afi" key="afi.local.datosLocal.label"/></legend>
			<table class="tabladatos">			
				<tr>
					<td><label><bean:message bundle="afi" key="afi.local.numeroCuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="localAdapterVO" property="local.numeroCuenta"/></td>
		
					<td><label><bean:message bundle="afi" key="afi.local.cantPersonal.label"/>: </label></td>
					<td class="normal"><bean:write name="localAdapterVO" property="local.cantPersonalView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.local.fecIniAct.label"/>: </label></td>
					<td class="normal"><bean:write name="localAdapterVO" property="local.fecIniActView"/></td>
		
					<td><label><bean:message bundle="afi" key="afi.local.fecCesAct.label"/>: </label></td>
					<td class="normal"><bean:write name="localAdapterVO" property="local.fecCesActView"/></td>
				</tr>				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.local.nombreFantasia.label"/>: </label></td>
					<td class="normal"><bean:write name="localAdapterVO" property="local.nombreFantasia"/></td>		
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.local.centralizaIngresos.label"/>: </label></td>
					<td class="normal"><bean:write name="localAdapterVO" property="local.centralizaIngresosView"/></td>
				</tr>				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.local.contribEtur.label"/>: </label></td>
					<td class="normal"><bean:write name="localAdapterVO" property="local.contribEturView"/></td>		
					<td><label><bean:message bundle="afi" key="afi.local.radio.label"/>: </label></td>
					<td class="normal"><bean:write name="localAdapterVO" property="local.radioView"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- Datos Generales del Local -->
				
		<!-- Totales de las Actividades Declaradas -->
		<fieldset>
			<legend><bean:message bundle="afi" key="afi.local.datosTotales.label"/></legend>
			<table class="tabladatos">	
				<tr>
					<td><label><bean:message bundle="afi" key="afi.local.pagaMinimo.label"/>: </label></td>
					<td class="normal"><bean:write name="localAdapterVO" property="local.pagaMinimoView"/></td>		
				
					<td><label><bean:message bundle="afi" key="afi.local.derDetTot.label"/>: </label></td>
					<td class="normal"><bean:write name="localAdapterVO" property="local.derDetTotView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.local.minimoGeneral.label"/>: </label></td>
					<td class="normal"><bean:write name="localAdapterVO" property="local.minimoGeneralView"/></td>
		
					<td><label><bean:message bundle="afi" key="afi.local.derecho.label"/>: </label></td>
					<td class="normal"><bean:write name="localAdapterVO" property="local.derechoView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.local.alicuotaPub.label"/>: </label></td>
					<td class="normal"><bean:write name="localAdapterVO" property="local.alicuotaPubView"/></td>
		
					<td><label><bean:message bundle="afi" key="afi.local.publicidad.label"/>: </label></td>
					<td class="normal"><bean:write name="localAdapterVO" property="local.publicidadView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.local.alicuotaMesYSil.label"/>: </label></td>
					<td class="normal"><bean:write name="localAdapterVO" property="local.alicuotaMesYSilView"/></td>
		
					<td><label><bean:message bundle="afi" key="afi.local.mesasYSillas.label"/>: </label></td>
					<td class="normal"><bean:write name="localAdapterVO" property="local.mesasYSillasView"/></td>
				</tr>	
				<tr>
					<td><label><bean:message bundle="afi" key="afi.local.subTotal1.label"/>: </label></td>
					<td class="normal"><bean:write name="localAdapterVO" property="local.subTotal1View"/></td>
		
					<td><label><bean:message bundle="afi" key="afi.local.otrosPagos.label"/>: </label></td>
					<td class="normal"><bean:write name="localAdapterVO" property="local.otrosPagosView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.local.computado.label"/>: </label></td>
					<td class="normal"><bean:write name="localAdapterVO" property="local.computadoView"/></td>
		
					<td><label><bean:message bundle="afi" key="afi.local.resto.label"/>: </label></td>
					<td class="normal"><bean:write name="localAdapterVO" property="local.restoView"/></td>
				</tr>	
				<tr>
					<td><label><bean:message bundle="afi" key="afi.local.derechoTotal.label"/>: </label></td>
					<td class="normal"><bean:write name="localAdapterVO" property="local.derechoTotalView"/></td>
		
					<td><label><bean:message bundle="afi" key="afi.local.paga.label"/>: </label></td>
					<td class="normal"><bean:write name="localAdapterVO" property="local.pagaView"/></td>
				</tr>		
			</table>
		</fieldset>	
		<!--  Totales de las Actividades Declaradas -->
   	
   		<!--HabLoc -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="afi" key="afi.local.listHabLoc.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="localAdapterVO" property="local.listHabLoc">	    	
			    	<tr>						
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->	
						<th align="left"><bean:message bundle="afi" key="afi.habLoc.numeroCuenta.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.habLoc.codRubro.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.habLoc.fechaHabilitacion.label"/></th>
					</tr>
					<logic:iterate id="HabLocVO" name="localAdapterVO" property="local.listHabLoc">					
						<tr>						
							<!-- Ver/Seleccionar -->	
							<td>
								<logic:notEqual name="localAdapterVO" property="modoSeleccionar" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verHabLoc', '<bean:write name="HabLocVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:notEqual>																
							</td>						
							<td><bean:write name="HabLocVO" property="numeroCuenta"/>&nbsp;</td>
							<td><bean:write name="HabLocVO" property="codRubroView"/>&nbsp;</td>
							<td><bean:write name="HabLocVO" property="fechaHabilitacionView"/>&nbsp;</td>
						</tr>						
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="localAdapterVO" property="local.listHabLoc">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- HabLoc -->
   	
   		<!--ActLoc -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="afi" key="afi.local.listActLoc.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="localAdapterVO" property="local.listActLoc">	    	
			    	<tr>						
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->	
						<th align="left"><bean:message bundle="afi" key="afi.actLoc.numeroCuenta.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.actLoc.actividad.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.actLoc.fechaInicio.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.actLoc.marcaPrincipal.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.actLoc.tratamiento.label"/></th>
					</tr>
					<logic:iterate id="ActLocVO" name="localAdapterVO" property="local.listActLoc">					
						<tr>						
							<!-- Ver/Seleccionar -->	
							<td>
								<logic:notEqual name="localAdapterVO" property="modoSeleccionar" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verActLoc', '<bean:write name="ActLocVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:notEqual>																
							</td>						
							<td><bean:write name="ActLocVO" property="numeroCuenta"/>&nbsp;</td>
							<td><bean:write name="ActLocVO" property="actividadView"/>&nbsp;</td>
							<td><bean:write name="ActLocVO" property="fechaInicioView"/>&nbsp;</td>
							<td><bean:write name="ActLocVO" property="marcaPrincipalView"/>&nbsp;</td>
							<td><bean:write name="ActLocVO" property="tratamientoView"/>&nbsp;</td>
						</tr>						
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="localAdapterVO" property="local.listActLoc">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- ActLoc -->
   	
   		<!--OtrosPagos -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="afi" key="afi.local.listOtrosPagos.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="localAdapterVO" property="local.listOtrosPagos">	    	
			    	<tr>						
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->	
						<th align="left"><bean:message bundle="afi" key="afi.otrosPagos.tipoPago.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.otrosPagos.periodoPago.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.otrosPagos.fechaPago.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.otrosPagos.resolucion.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.otrosPagos.importePago.label"/></th>
					</tr>
					<logic:iterate id="OtrosPagosVO" name="localAdapterVO" property="local.listOtrosPagos">					
						<tr>						
							<!-- Ver/Seleccionar -->	
							<td>
								<logic:notEqual name="localAdapterVO" property="modoSeleccionar" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verOtrosPagos', '<bean:write name="OtrosPagosVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:notEqual>																
							</td>						
							<td><bean:write name="OtrosPagosVO" property="tipoPagoView"/>&nbsp;</td>
							<td><bean:write name="OtrosPagosVO" property="periodoPagoView"/>&nbsp;</td>
							<td><bean:write name="OtrosPagosVO" property="fechaPagoView"/>&nbsp;</td>
							<td><bean:write name="OtrosPagosVO" property="resolucionView"/>&nbsp;</td>
							<td><bean:write name="OtrosPagosVO" property="importePagoView"/>&nbsp;</td>
						</tr>						
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="localAdapterVO" property="local.listOtrosPagos">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- OtrosPagos -->
   	
   		<!--DecActLoc -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="afi" key="afi.local.listDecActLoc.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="localAdapterVO" property="local.listDecActLoc">	    	
			    	<tr>						
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->	
						<th align="left"><bean:message bundle="afi" key="afi.decActLoc.codActividad.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.decActLoc.baseImpOCantidad.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.decActLoc.aliOMinPorUni.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.decActLoc.derechoCalculado.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.decActLoc.derechoDet.label"/></th>
					</tr>
					<logic:iterate id="DecActLocVO" name="localAdapterVO" property="local.listDecActLoc">					
						<tr>						
							<!-- Ver/Seleccionar -->	
							<td>
								<logic:notEqual name="localAdapterVO" property="modoSeleccionar" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verDecActLoc', '<bean:write name="DecActLocVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:notEqual>																
							</td>						
							<td><bean:write name="DecActLocVO" property="codActividadView"/>&nbsp;</td>
							<td><bean:write name="DecActLocVO" property="baseImpOCantidadView"/>&nbsp;</td>
							<td><bean:write name="DecActLocVO" property="aliOMinPorUniView"/>&nbsp;</td>
							<td><bean:write name="DecActLocVO" property="derechoOMinimoView"/>&nbsp;</td>
							<td><bean:write name="DecActLocVO" property="derechoDetView"/>&nbsp;</td>
						</tr>						
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="localAdapterVO" property="local.listDecActLoc">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- DecActLoc -->
		
	   	<!-- DatosPagoCta -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="afi" key="afi.local.listDatosPagoCta.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="localAdapterVO" property="local.listDatosPagoCta">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
						<th align="left"><bean:message bundle="afi" key="afi.datosPagoCta.codImpuesto.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.datosPagoCta.totalMontoIngresado.label"/></th>
					</tr>
					<logic:iterate id="DatosPagoCtaVO" name="localAdapterVO" property="local.listDatosPagoCta">			
						<tr>						
							<!-- Ver/Seleccionar -->	
							<td>
								<logic:notEqual name="localAdapterVO" property="modoSeleccionar" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verDatosPagoCta', '<bean:write name="DatosPagoCtaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:notEqual>																
							</td>						
							<td><bean:write name="DatosPagoCtaVO" property="codImpuestoView"/>&nbsp;</td>
							<td><bean:write name="DatosPagoCtaVO" property="totalMontoIngresadoView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="localAdapterVO" property="local.listDatosPagoCta">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- DatosPagoCta -->
							   	 
	   	 <table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	   <logic:equal name="localAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>					
	   	    	</td>
	   	    </tr>
	   	</table>
	    <input type="hidden" name="name"  value="<bean:write name='localAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->