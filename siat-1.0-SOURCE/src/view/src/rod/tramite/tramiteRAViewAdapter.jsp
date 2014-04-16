<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rod/AdministrarTramiteRA.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="rod" key="rod.tramiteRAEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- TramiteRA -->
	<fieldset>
		<legend><bean:message bundle="rod" key="rod.tramiteRA.title"/></legend>
		
		<table class="tabladatos">
			<!-- TextNroTramite -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.nroTramite.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.nroTramiteView"/></td>				 	

			<!-- Numero Comuna -->

				<td><label><bean:message bundle="rod" key="rod.tramiteRA.nroComuna.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.nroComunaView"/></td>			
			</tr>
			<!--Fecha-->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.fecha.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.fechaView"/></td>
			</tr>
			<!-- Tipo de Tramite -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.tipoTramite.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.desCodTipoTramite"/></td>			

			<!-- Rubros -->
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.tipoTramite.rubros.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.rubros"/></td>
			</tr>
		</table>
	</fieldset>
	<fieldset>		
		<legend><bean:message bundle="rod" key="rod.a"/></legend>
		
		<table class="tabladatos">
		
			<!-- A-IDENTIFICACION DEL VEHICULO -->
			<!-- Numero Patente -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.ANroPatente.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.ANroPatente"/></td>

			<!-- Digito de control -->

				<td><label><bean:message bundle="rod" key="rod.tramiteRA.ADigVerif.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.ADigVerifView"/></td>			
			</tr>
			
		</table>	
	</fieldset>
	<fieldset>
		<legend><bean:message bundle="rod" key="rod.b"/></legend>
		
		<table class="tabladatos">
		
			<!-- B-CARACTERISTICA DEL VEHICULO -->
			<!-- Modelo -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.modelo.codModelo.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BCodModeloView"/></td>
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.modelo.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BDesModelo"/></td>
				
			</tr>
			<!-- Marca -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.marca.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BDesMarca" /></td>
				
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.codMarca.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BCodMarcaView" /></td>
			</tr>
			<tr>
			<!-- Codigo tipo vehiculo -->
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BTipoVeh.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BCodTipoVeh"/></td>
				
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BCodTipoVeh.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BDesTipoVeh"/></td>
			</tr>			
			
			<tr>
			<!-- Anio -->	
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.anio.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BAnioView"/></td>			
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BCodFab.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BCodFabView"/></td>
			<tr>
			</tr>
			<!-- Codigo tipo motor -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BCodTM.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BCodTipoMotorView"/></td>
				
			<!-- Numero Motor -->	
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.nroMotor.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BNroMotor"/></td>
			</tr>
			<tr>
			<!-- uso -->		
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BTipoUso.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BDesUso"/></td>	
						
			
			<!-- HPoC -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BHPoC.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BHPoC"/></td>			
			</tr>
			<tr>
			<!-- Peso Vacio -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BPesoVacio.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BPesoVacioView"/></td>			
			
			
			<!-- Precio Alta -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BPrecioAlta.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BPrecioAltaView"/></td>
			</tr>
			<!-- Fecha Factura -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BFechaFactura.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BFechaFacturaView"/></td>
			</tr>	
		</table>		
	</fieldset>		
	<fieldset>
		<legend><bean:message bundle="rod" key="rod.BVehiculosCarga"/></legend>
		
		<table class="tabladatos">
		
			<!-- PARA VEHICULOS DE CARGA O PASAJEROS -->
			<!-- Peso Carga -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BPesoCarga.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BPesoCargaView"/></td>			
			<!-- Tipo Carga -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BTipoCarga.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BDesTipoCarga"/></td>
			</tr>
			<tr>						
			<!-- Capacidad Carga -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BCapacidadCarga.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BCapCargaView"/></td>			
			
			<!-- Unidad Medida -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BUnidadMedida.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BUnidadMedida"/></td>			
			
			</tr>
			<tr>
			<!-- Cantidad -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BCantidad.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BCantidadView"/></td>			
			
			<!-- Asientos -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BAsientos.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BAsientosView"/></td>			
			
			</tr>
			<tr>
			<!-- Cant. Ejes -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BCantEjes.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BCantEjesView"/></td>			
			
			<!-- Alto Carr -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BAltoCarr.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BAltoCarrView"/></td>			
			
			</tr>
			<tr>
			<!-- Largo Carr -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BLargoCarr.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BLargoCarrView"/></td>			
			</tr>
			
		</table>
	</fieldset>
	<fieldset>
		<legend><bean:message bundle="rod" key="rod.c"/></legend>
		
		<table class="tabladatos">
		
			<!-- C -->
			<!-- C-DATOS DEL PROPIETARIO -->
			<!-- Propietarios Actuales-->		
			
 	 <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
        <caption><bean:message bundle="rod" key="rod.tramiteRA.listPropietarioActual.label"/></caption>
         <tbody>
           <logic:notEmpty  name="tramiteRAAdapterVO" property="tramiteRA.listPropietario">
               <tr>
                 <th width="1">&nbsp;</th> <!-- Ver -->
                 
                 <th align="left"><bean:message bundle="rod" key="rod.tramiteRA.CApellidoORazon.label"/></th>
                 <th align="left"><bean:message bundle="rod" key="rod.tramiteRA.CTipoDoc.label"/></th>
                 <th align="left"><bean:message bundle="rod" key="rod.tramiteRA.CNroDoc.label"/></th>
                 <th align="left"><bean:message bundle="rod" key="rod.tramiteRA.CNroCuit.label"/></th>
                 
              </tr>
              <logic:iterate id="PropietarioVO" name="tramiteRAAdapterVO" property="tramiteRA.listPropietario">
	              <logic:equal name="PropietarioVO" property="tipoPropietario" value="1">
	                 <tr>
	                    <!-- Ver -->
	                    <td>
							<logic:equal name="tramiteRAAdapterVO" property="verPropietarioActualEnabled" value="enabled">
								<logic:equal name="tramiteRAAdapterVO" property="verEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verPropietarioActual', '<bean:write name="PropietarioVO" property="nroDoc" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:equal>	
								<logic:notEqual name="tramiteRAAdapterVO" property="verEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="tramiteRAAdapterVO" property="verPropietarioActualEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
							</logic:notEqual>
						</td>
						
			            <td><bean:write name="PropietarioVO" property="apellidoORazon" />&nbsp;</td>
	                    <td><bean:write name="PropietarioVO" property="desTipoDoc" />&nbsp;</td>
	                    <td><bean:write name="PropietarioVO" property="nroDoc" bundle="base" formatKey="general.format.id" />&nbsp;</td>
	                    <td><bean:write name="PropietarioVO" property="nroCuit"/>&nbsp;</td>
	                    
	                 </tr>
	             </logic:equal>
              </logic:iterate>
           </logic:notEmpty>
           <logic:empty  name="tramiteRAAdapterVO" property="tramiteRA.listPropietario">
              <tr><td align="center">
              <bean:message bundle="base" key="base.noExistenRegitros"/>
              </td></tr>
           </logic:empty>
           	
        </tbody>
     </table>
	<!-- Fin propietarios actuales-->			
	
			<!-- Cantidad de Dueños -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CCantidadDuenios.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.CCantDueniosView"/></td>			
			</tr>
	
		</table>
	</fieldset>
	<fieldset>
		<legend><bean:message bundle="rod" key="rod.d"/></legend>
		
		<table class="tabladatos">
			<!--D -->
			<!-- D-DOMICILIO -->
			<!-- Localidad -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.DLocalidad.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.DDesLocalidad"/></td>
				
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.DCodLocalidad.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.DCodLocalidad" bundle="base" formatKey="general.format.id" /></td>
				
			</tr>
			
			<!-- Calle -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.DDesCalle.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.DDesCalle"/></td>
			</tr>
			<tr>
			<!-- Numero -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.DNumero.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.DNumeroView"/></td>
			</tr>
			<!-- Piso -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.DPiso.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.DPiso"/></td>
			
			<!-- dpto -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.DDpto.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.DDpto"/></td>
			
			<!-- bis -->			
				
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.DBis.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.DBisView"/></td>
				
			</tr>
			
		</table>
	</fieldset>
	<fieldset>
		<legend><bean:message bundle="rod" key="rod.e"/></legend>
		
		<table class="tabladatos">
			<!--E -->
			<!-- E-CAMBIO DE MOTOR -->
			<!-- Marca -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.EMarca.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.EDesMarca"/></td>

			<!-- Motor -->
			<td><label><bean:message bundle="rod" key="rod.tramiteRA.BCodTM.label"/>: </label></td>
			<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.EDesTipoMotor"/></td>
		
			</tr>
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.ENroMotor.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.ENroMotor"/></td>
			</tr>
		</table>
	</fieldset>
	<fieldset>
		<legend><bean:message bundle="rod" key="rod.f"/></legend>
		
		<table class="tabladatos">
			<!-- F -->
			<!-- F-BAJA TOTAL -->
			<!-- Motivo de la baja -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.FdesMotivoBaja.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.FDesMotivoBaja"/></td>
			</tr>
		</table>
	</fieldset>
	<fieldset>
		<legend><bean:message bundle="rod" key="rod.g"/></legend>
		<table class="tabladatos">
			<!-- G -->
			<!-- G-DATOS DEL PROPIETARIO ANTERIOR -->
			<!-- Propietarios Anteriores-->		
			
 	 <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
        <caption><bean:message bundle="rod" key="rod.tramiteRA.listPropietarioAnterior.label"/></caption>
         <tbody>
           <logic:notEmpty  name="tramiteRAAdapterVO" property="tramiteRA.listPropietario">
               <tr>
                 <th width="1">&nbsp;</th> <!-- Ver -->
                 
                 <th align="left"><bean:message bundle="rod" key="rod.tramiteRA.CApellidoORazon.label"/></th>
                 <th align="left"><bean:message bundle="rod" key="rod.tramiteRA.CTipoDoc.label"/></th>
                 <th align="left"><bean:message bundle="rod" key="rod.tramiteRA.CNroDoc.label"/></th>
                 <th align="left"><bean:message bundle="rod" key="rod.tramiteRA.CNroCuit.label"/></th>
                 
              </tr>
              <logic:iterate id="PropietarioVO" name="tramiteRAAdapterVO" property="tramiteRA.listPropietario">
              	<logic:equal name="PropietarioVO" property="tipoPropietario" value="2">
	                 <tr>
	                    <!-- Ver -->
	                    <td>
							<logic:equal name="tramiteRAAdapterVO" property="verPropietarioActualEnabled" value="enabled">
								<logic:equal name="tramiteRAAdapterVO" property="verEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verPropietarioAnterior', '<bean:write name="PropietarioVO" property="nroDoc" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:equal>	
								<logic:notEqual name="tramiteRAAdapterVO" property="verEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="tramiteRAAdapterVO" property="verPropietarioActualEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
							</logic:notEqual>
						</td>
				        
	                    <td><bean:write name="PropietarioVO" property="apellidoORazon" />&nbsp;</td>
	                    <td><bean:write name="PropietarioVO" property="desTipoDoc" />&nbsp;</td>
	                    <td><bean:write name="PropietarioVO" property="nroDoc" bundle="base" formatKey="general.format.id" />&nbsp;</td>
	                    <td><bean:write name="PropietarioVO" property="nroCuit"/>&nbsp;</td>
	                    
	                 </tr>
	             </logic:equal>
              </logic:iterate>
           </logic:notEmpty>
           <logic:empty  name="tramiteRAAdapterVO" property="tramiteRA.listPropietario">
              <tr><td align="center">
              <bean:message bundle="base" key="base.noExistenRegitros"/>
              </td></tr>
           </logic:empty>
           	
        </tbody>
     </table>
	<!-- Fin propietarios anteriores-->			
	
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.GDomicilio.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.GDesDomicilio"/></td>
			</tr>
		</table>
	</fieldset>
	<fieldset>
		<legend><bean:message bundle="rod" key="rod.h"/></legend>
		
		<table class="tabladatos">	
			<!-- H -->
			<!-- H-CAMBIO DE NUMERO DE PATENTE -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.HPatentePad.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.HPatentePad"/></td>
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.HPatenteCorr.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.HPatenteCorr"/></td>
			</tr>
		</table>
	</fieldset>
	
	<fieldset>
	<legend><bean:message bundle="rod" key="rod.i"/></legend>
		
		<table class="tabladatos">
			<!--I -->
			<!-- I-DATOS DEL PAGO O PERCEPCION -->
			<!-- pago-->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.ICodPago.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.IDesPago"/></td>			
			</tr>
			<!-- Importe 1-->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.IImporte.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.IImporte1View"/></td>			

			<!-- Fecha 1-->

				<td><label><bean:message bundle="rod" key="rod.tramiteRA.fecha.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.IFecha1View"/></td>			
			</tr>
			<!-- Importe 2-->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.IImporte.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.IImporte2View"/></td>			

			<!-- Fecha 2-->

				<td><label><bean:message bundle="rod" key="rod.tramiteRA.fecha.label"/>: </label></td>	
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.IFecha2View"/></td>		
			</tr>
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.IMuniOComuna.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.IDesBancoMuni"/></td>			
				
			</tr>
		</table>
	</fieldset>	
	<fieldset>
		<legend><bean:message bundle="rod" key="rod.tramiteRA.observaciones.label"/></legend>
		
		<table class="tabladatos">	
			<!-- Observaciones -->
			<tr>
				<td class="normal"><label><bean:message bundle="rod" key="rod.tramiteRA.observacion.label"/>: </label>
				<bean:write name="tramiteRAAdapterVO" property="tramiteRA.observacion"/></td>			
			
			<!-- Observaciones API-->
			
				<td class="normal"><label><bean:message bundle="rod" key="rod.tramiteRA.observacionesAIP.label"/>: </label>
				<bean:write name="tramiteRAAdapterVO" property="tramiteRA.observacionAPI"/></td>			
			</tr>
			<!-- Observaciones RNPA-->
			<tr>
				<td class="normal"><label><bean:message bundle="rod" key="rod.tramiteRA.observacionesRNPA.label"/>: </label>
				<bean:write name="tramiteRAAdapterVO" property="tramiteRA.observacionRNPA"/></td>			
			</tr>
		</table>
	</fieldset>
	<!-- TramiteRA -->
	
	<!-- Historico de Cambios de Estado del TramiteRA -->		
	<logic:equal name="tramiteRAAdapterVO" property="act" value="ver">
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="rod" key="rod.tramiteRA.listHisEstTra.label"/></caption>
			<tbody>
				<logic:notEmpty  name="tramiteRAAdapterVO" property="tramiteRA.listHisEstTra">	    	
				    <tr>
						<th align="left"><bean:message bundle="rod" key="rod.tramiteRA.hisEtsTra.fecha.label"/></th>
						<th align="left"><bean:message bundle="rod" key="rod.hisEsttra.logCambios.label"/></th>
						<th align="left"><bean:message bundle="rod" key="rod.hisEstTra.estado.label"/></th>
					</tr>
					<logic:iterate id="HisEstTraVO" name="tramiteRAAdapterVO" property="tramiteRA.listHisEstTra">
						<tr>
							<td><bean:write name="HisEstTraVO" property="fechaView" />&nbsp;</td>
							<td><bean:write name="HisEstTraVO"property="logCambios" />&nbsp;</td>
							<td><bean:write name="HisEstTraVO" property="estTra.desEstTra" />&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="tramiteRAAdapterVO" property="tramiteRA.listHisEstTra">
					<tr><td align="center"><bean:message bundle="base" key="base.noExistenRegitros"/></td></tr>
				</logic:empty>
			</tbody>
		</table>			
	</logic:equal>

	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">  	   			
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="100%">
				<logic:equal name="tramiteRAAdapterVO" property="act" value="eliminar">
					<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
						<bean:message bundle="base" key="abm.button.eliminar"/>
					</html:button>
				</logic:equal>
				<logic:notEqual name="tramiteRAAdapterVO" property="act" value="eliminar">
					<html:button property="btnValidarDatos"  styleClass="boton" onclick="submitForm('validarDatos', '');">
						<bean:message bundle="rod" key="rod.button.validarDatos"/>
					</html:button>
					<html:button property="btnImprimir"  styleClass="boton" onclick="submitForm('imprimir', '1');">
						<bean:message bundle="base" key="abm.button.imprimir"/>
					</html:button>
				</logic:notEqual>
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
<!-- Fin Tabla que contiene todos los formularios -->