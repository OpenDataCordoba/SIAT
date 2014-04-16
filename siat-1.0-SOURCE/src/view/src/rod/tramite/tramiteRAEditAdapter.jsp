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
				
				
					<logic:equal name="tramiteRAAdapterVO" property="act" value="modificar">
						<td><label><bean:message bundle="rod" key="rod.tramiteRA.nroTramite.label"/>: </label></td>
						<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.nroTramiteView"/></td>
				 	</logic:equal>
				 	
					<logic:equal name="tramiteRAAdapterVO" property="act" value="agregar">
						<td><label>(*)&nbsp;<bean:message bundle="rod" key="rod.tramiteRA.nroTramite.label"/>: </label></td>
						<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.nroTramite" size="15" maxlength="20" /></td>
				 	</logic:equal>	
				

			<!-- Numero Comuna -->

				<td><label><bean:message bundle="rod" key="rod.tramiteRA.nroComuna.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.nroComunaView" size="20" maxlength="100"/></td>			
			</tr>
			<!--Fecha-->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="rod" key="rod.tramiteRA.fecha.label"/>: </label></td>
				<td class="normal">
					<html:text name="tramiteRAAdapterVO" property="tramiteRA.fechaView" styleId="fechaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaView');" id="a_fechaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			<!-- Tipo de Tramite -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="rod" key="rod.tramiteRA.tipoTramite.label"/>: </label></td>
				<td class="normal">
					<logic:equal name="tramiteRAAdapterVO" property="act" value="agregar">
						<html:select name="tramiteRAAdapterVO" property="tramiteRA.tipoTramite.codTipoTramite" styleClass="select" onchange="submitForm('paramTipoTramite', '');">
							<html:optionsCollection name="tramiteRAAdapterVO" property="listTipoTramite" label="tipoTramiteView" value="codTipoTramite" />
						</html:select>
					</logic:equal>
					<logic:equal name="tramiteRAAdapterVO" property="act" value="modificar">
						<bean:write name="tramiteRAAdapterVO" property="tramiteRA.desCodTipoTramite"/>
					</logic:equal>
				</td>
			
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
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.ANroPatente" size="20" maxlength="100"/></td>

			<!-- Digito de control -->

				<td><label><bean:message bundle="rod" key="rod.tramiteRA.ADigVerif.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.ADigVerifView" size="20" maxlength="100"/></td>			
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
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.BCodModeloView" size="20" maxlength="100"/></td>
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.modelo.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.BDesModelo" size="20" maxlength="100"/></td>
			
	  	   		<td align="left" width="50%">
		   	    	<html:button property="btnBuscar" styleClass="boton" onclick="submitForm('buscarModelo', '');">
		   	    		<bean:message bundle="base" key="abm.button.buscar"/>
		   	    	</html:button>
	   	    	</td>
	   				
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
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BDesTipoVeh"/></td>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BCodTipoVeh.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.BCodTipoVeh"/></td>
			</tr>			
			
			<tr>
			<!-- Anio -->	
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.anio.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.BAnioView" size="10" maxlength="100"/></td>			
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BCodFab.label"/>: </label></td>
				<td class="normal">
					<html:select name="tramiteRAAdapterVO" property="tramiteRA.BTipoFab.codFab" styleClass="select" onchange="submitForm('paramTipoFabricacion', '');">
						<html:optionsCollection name="tramiteRAAdapterVO" property="listTipoFabricacion" label="tipoFabView" value="codFab" />
					</html:select>
				</td>
			<tr>
			</tr>
			<!-- Codigo tipo motor -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BCodTM.label"/>: </label></td>
				<td class="normal">
					<html:select name="tramiteRAAdapterVO" property="tramiteRA.BTM.codTipoMotor" styleClass="select" onchange="submitForm('paramBTipoMotor', '');">
						<html:optionsCollection name="tramiteRAAdapterVO" property="listTipoMotor" label="tipoMotorView" value="codTipoMotor" />
					</html:select>
				</td>
				
			<!-- Numero Motor -->	
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.nroMotor.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.BNroMotor" size="10" maxlength="100"/></td>
			</tr>
			<tr>
			<!-- uso -->		
			<td><label><bean:message bundle="rod" key="rod.tramiteRA.BTipoUso.label"/>: </label></td>
			<td class="normal">
				<html:select name="tramiteRAAdapterVO" property="tramiteRA.BTipoUso.codUso" styleClass="select" onchange="submitForm('paramTipoUso', '');">
					<html:optionsCollection name="tramiteRAAdapterVO" property="listTipoUso" label="tipoUsoView" value="codUso" />
				</html:select>
			</td>	
						
			
			<!-- HPoC -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BHPoC.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.BHPoC" size="10" maxlength="100"/></td>			
			</tr>
			<tr>
			<!-- Peso Vacio -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BPesoVacio.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.BPesoVacioView" size="10" maxlength="100"/></td>			
			
			
			<!-- Precio Alta -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BPrecioAlta.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.BPrecioAltaView" size="10" maxlength="100"/></td>
			</tr>
			<!-- Fecha Factura -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BFechaFactura.label"/>: </label></td>
				<td class="normal">
					<html:text name="tramiteRAAdapterVO" property="tramiteRA.BFechaFacturaView" styleId="BFechaFacturaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('BFechaFacturaView');" id="a_BFechaFacturaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>			
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
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.BPesoCargaView" size="10" maxlength="100"/></td>			
			<!-- Tipo Carga -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BTipoCarga.label"/>: </label></td>
				
				<td class="normal">
					<html:select name="tramiteRAAdapterVO" property="tramiteRA.BTipoCarga.codTipoCarga" styleClass="select" onchange="submitForm('paramTipoCarga', '');">
						<html:optionsCollection name="tramiteRAAdapterVO" property="listTipoCarga" label="tipoCargaView" value="codTipoCarga" />
					</html:select>
				</td>
			</tr>
			<tr>						
			<!-- Capacidad Carga -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BCapacidadCarga.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.BCapCargaView" size="10" maxlength="100"/></td>			
			
			<!-- Unidad Medida -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BUnidadMedida.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.BUnidadMedida" size="10" maxlength="100"/></td>			
			
			</tr>
			<tr>
			<!-- Cantidad -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BCantidad.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.BCantidadView" size="10" maxlength="100"/></td>			
			
			<!-- Asientos -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BAsientos.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.BAsientosView" size="10" maxlength="100"/></td>			
			
			</tr>
			<tr>
			<!-- Cant. Ejes -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BCantEjes.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.BCantEjesView" size="10" maxlength="100"/></td>			
			
			<!-- Alto Carr -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BAltoCarr.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.BAltoCarrView" size="10" maxlength="100"/></td>			
			
			</tr>
			<tr>
			<!-- Largo Carr -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.BLargoCarr.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.BLargoCarrView" size="10" maxlength="100"/></td>			
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
                 <th width="1">&nbsp;</th> <!-- Modificar -->
                 <th width="1">&nbsp;</th> <!-- Eliminar -->
                 <th width="1">&nbsp;</th> <!-- Setear titular principal -->
                 
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
						<!-- Modificar-->								
						<td>
							<logic:equal name="tramiteRAAdapterVO" property="modificarPropietarioActualEnabled" value="enabled">
								<logic:equal name="tramiteRAAdapterVO" property="modificarEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarPropietarioActual', '<bean:write name="PropietarioVO" property="nroDoc" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
									</a>							
								</logic:equal>	
								<logic:notEqual name="tramiteRAAdapterVO" property="modificarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="tramiteRAAdapterVO" property="modificarPropietarioActualEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
							</logic:notEqual>
						</td>
						
						<!-- Eliminar-->								
						<td>
							<logic:equal name="tramiteRAAdapterVO" property="eliminarPropietarioActualEnabled" value="enabled">
								<logic:equal name="tramiteRAAdapterVO" property="eliminarEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarPropietarioActual', '<bean:write name="PropietarioVO" property="nroDoc" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
									</a>							
								</logic:equal>	
								<logic:notEqual name="tramiteRAAdapterVO" property="eliminarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="tramiteRAAdapterVO" property="eliminarPropietarioActualEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
							</logic:notEqual>
						</td>
	                    
	                    <!-- Setear Propietario Principal -->
	                    <td>
	                    	<logic:equal name="PropietarioVO" property="esPropPrincipal.id" value="0">
								<logic:equal name="tramiteRAAdapterVO" property="marcarPrincipalEnabled" value="enabled">
									<logic:equal name="tramiteRAAdapterVO" property="tramiteRA.marcarPrincipalBussEnabled" value="true">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('marcarTitularActual', '<bean:write name="PropietarioVO" property="nroDoc" bundle="base" formatKey="general.format.id"/>');">
											<img title="Marcar como principal" border="0" src="<%=request.getContextPath()%>/images/iconos/titularPpal0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="tramiteRAAdapterVO" property="tramiteRA.marcarPrincipalBussEnabled" value="true">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/titularPpal1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="tramiteRAAdapterVO" property="marcarPrincipalEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/titularPpal1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:equal name="PropietarioVO" property="esPropPrincipal.id" value="1">
								&nbsp;
							</logic:equal>
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
           	<!-- Agregar -->
			
			<tr>
				<td colspan="20" align="right">
	  				<bean:define id="agregarPropietarioActualEnabled" name="tramiteRAAdapterVO" property="agregarPropietarioActualEnabled"/>
	  					<input type="button" <%=agregarPropietarioActualEnabled%> class="boton" 
							onClick="submitForm('agregarPropietarioActual', '');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
				</td>
			</tr>
			
        </tbody>
     </table>
	<!-- Fin propietarios actuales-->			
					
			<tr>
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
				
				<td class="normal" colspan="3">										
					<html:text name="tramiteRAAdapterVO" property="tramiteRA.DDesLocalidad" size="20" maxlength="20" disabled="true" />				 	
				 	<html:button property="btnBucarLocalidad"  styleClass="boton" onclick="submitForm('buscarLocalidad', '');">
						<bean:message bundle="rod" key="rod.domicilioEditAdapter.button.buscarLocalidad"/>						
					</html:button>
				</td>
				
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.DCodLocalidad.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.DCodLocalidad" bundle="base" formatKey="general.format.id" /></td>
				
			</tr>
			
			<!-- Calle -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.DDesCalle.label"/>: </label></td>
				<td><html:text name="tramiteRAAdapterVO" property="tramiteRA.DDesCalle" size="20" maxlength="25"/></td>
				<td>					
					<logic:equal  name="tramiteRAAdapterVO" property="tramiteRA.DDomicilio.localidad.esRosario" value="true" >
						<html:button property="btnBuscarCalle"  styleClass="boton" onclick="submitForm('buscarCalle', '');">
							<bean:message bundle="rod" key="rod.tramiteRAEditAdapter.button.buscarCalle"/>
						</html:button>
					</logic:equal>
					<logic:notEqual name="tramiteRAAdapterVO" property="tramiteRA.DDomicilio.localidad.esRosario" value="true" >
						<html:button property="btnValidarDomicilio" styleClass="boton" disabled="true">
							<bean:message bundle="rod" key="rod.tramiteRAEditAdapter.button.buscarCalle"/>
						</html:button>
					</logic:notEqual>
				</td>
			</tr>
			<tr>
			<!-- Numero -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.DNumero.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.DNumero" size="10" maxlength="100"/></td>
			</tr>
			<!-- Piso -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.DPiso.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.DPiso" size="10" maxlength="100"/></td>
			
			<!-- dpto -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.DDpto.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.DDpto" size="10" maxlength="100"/></td>
			
			<!-- bis -->			
				<bean:define id="listSiNo" name="tramiteRAAdapterVO" property="listSiNo"/>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.DBis.label"/>: </label></td>
				<td class="normal">
					<html:select name="tramiteRAAdapterVO" property="tramiteRA.DDomicilio.bis.id" styleClass="select">
						<html:optionsCollection name="listSiNo" label="value" value="id" />
					</html:select>
				</td>
				
			</tr>
			<table class="tablabotones" width="100%">
				<tr>
					<td align="right"><logic:equal  name="tramiteRAAdapterVO" property="tramiteRA.DDomicilio.localidad.esRosario" value="true" >
						<html:button property="btnValidarDomicilio"  styleClass="boton" onclick="submitForm('validarDomicilio', '');">
							<bean:message bundle="pad" key="pad.domicilioEditAdapter.button.validarDomicilio"/>
						</html:button>
					</logic:equal></td>
				</tr>
			</table>
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
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.EDesMarca" size="20" maxlength="100"/></td>

			<!-- Motor -->
			<td><label><bean:message bundle="rod" key="rod.tramiteRA.BCodTM.label"/>: </label></td>
			<td class="normal">
				<html:select name="tramiteRAAdapterVO" property="tramiteRA.ETipoMotor.codTipoMotor" styleClass="select" onchange="submitForm('paramETipoMotor', '');">
					<html:optionsCollection name="tramiteRAAdapterVO" property="listETipoMotor" label="tipoMotorView" value="codTipoMotor" />
				</html:select>
			</td>
		
			</tr>
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.ENroMotor.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.ENroMotor" size="20" maxlength="100"/></td>
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
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.FDesMotivoBaja" size="20" maxlength="100"/></td>
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
                 <th width="1">&nbsp;</th> <!-- Modificar -->
                 <th width="1">&nbsp;</th> <!-- Eliminar -->
                 <th width="1">&nbsp;</th> <!-- Setear titular principal -->
                 
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
						<!-- Modificar-->								
						<td>
							<logic:equal name="tramiteRAAdapterVO" property="modificarPropietarioActualEnabled" value="enabled">
								<logic:equal name="tramiteRAAdapterVO" property="modificarEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarPropietarioAnterior', '<bean:write name="PropietarioVO" property="nroDoc" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
									</a>							
								</logic:equal>	
								<logic:notEqual name="tramiteRAAdapterVO" property="modificarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="tramiteRAAdapterVO" property="modificarPropietarioActualEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
							</logic:notEqual>
						</td>
						
						<!-- Eliminar-->								
						<td>
							<logic:equal name="tramiteRAAdapterVO" property="eliminarPropietarioActualEnabled" value="enabled">
								<logic:equal name="tramiteRAAdapterVO" property="eliminarEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarPropietarioAnterior', '<bean:write name="PropietarioVO" property="nroDoc" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
									</a>							
								</logic:equal>	
								<logic:notEqual name="tramiteRAAdapterVO" property="eliminarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="tramiteRAAdapterVO" property="eliminarPropietarioActualEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
							</logic:notEqual>
						</td>
	                    
	                    <!-- Setear Propietario Principal -->
	                    <td>
	                    	<logic:equal name="PropietarioVO" property="esPropPrincipal.id" value="0">
								<logic:equal name="tramiteRAAdapterVO" property="marcarPrincipalEnabled" value="enabled">
									<logic:equal name="tramiteRAAdapterVO" property="tramiteRA.marcarPrincipalBussEnabled" value="true">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('marcarTitularAnterior', '<bean:write name="PropietarioVO" property="nroDoc" bundle="base" formatKey="general.format.id"/>');">
											<img title="Marcar como principal" border="0" src="<%=request.getContextPath()%>/images/iconos/titularPpal0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="tramiteRAAdapterVO" property="tramiteRA.marcarPrincipalBussEnabled" value="true">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/titularPpal1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="tramiteRAAdapterVO" property="marcarPrincipalEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/titularPpal1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:equal name="PropietarioVO" property="esPropPrincipal.id" value="1">
								&nbsp;
							</logic:equal>
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
           	<!-- Agregar -->
			
			<tr>
				<td colspan="20" align="right">
	  				<bean:define id="agregarPropietarioActualEnabled" name="tramiteRAAdapterVO" property="agregarPropietarioActualEnabled"/>
	  					<input type="button" <%=agregarPropietarioActualEnabled%> class="boton" 
							onClick="submitForm('agregarPropietarioAnterior', '');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
				</td>
			</tr>
			
        </tbody>
     </table>
	<!-- Fin propietarios anteriores-->			
			
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.GDomicilio.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.GDesDomicilio" size="30" maxlength="100"/></td>
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
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.HPatentePad" size="20" maxlength="100"/></td>
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.HPatenteCorr.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.HPatenteCorr" size="20" maxlength="100"/></td>
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
				<td class="normal">
					<html:select name="tramiteRAAdapterVO" property="tramiteRA.ITipoPago.codPago" styleClass="select" onchange="submitForm('paramTipoPago', '');">
						<html:optionsCollection name="tramiteRAAdapterVO" property="listTipoPago" label="desPago" value="codPago" />
					</html:select>
				</td>			
			</tr>
			<!-- Importe 1-->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.IImporte.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.IImporte1View" size="20" maxlength="100"/></td>			

			<!-- Fecha 1-->

				<td><label><bean:message bundle="rod" key="rod.tramiteRA.fecha.label"/>: </label></td>
				<td class="normal">
					<html:text name="tramiteRAAdapterVO" property="tramiteRA.IFecha1View" styleId="IFecha1View" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('IFecha1View');" id="a_IFecha1View">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>			
			</tr>
			<!-- Importe 2-->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.IImporte.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.IImporte2View" size="20" maxlength="100"/></td>			

			<!-- Fecha 2-->

				<td><label><bean:message bundle="rod" key="rod.tramiteRA.fecha.label"/>: </label></td>	
				<td class="normal">
					<html:text name="tramiteRAAdapterVO" property="tramiteRA.IFecha2View" styleId="IFecha2View" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('IFecha2View');" id="a_IFecha2View">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>		
			</tr>
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.IMuniOComuna.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRAAdapterVO" property="tramiteRA.IDesBancoMuni" size="20" maxlength="100"/></td>			
				
			</tr>
		</table>
	</fieldset>	
	<fieldset>
		<legend><bean:message bundle="rod" key="rod.tramiteRA.observaciones.label"/></legend>
		
		<table class="tabladatos">	
			<!-- Observaciones -->
			<tr>
				<td class="normal"><label><bean:message bundle="rod" key="rod.tramiteRA.observacion.label"/>: </label>
				<html:textarea name="tramiteRAAdapterVO" property="tramiteRA.observacion" rows="10" cols="30"/></td>			
			
			<!-- Observaciones API-->
			
				<td class="normal"><label><bean:message bundle="rod" key="rod.tramiteRA.observacionesAIP.label"/>: </label>
				<html:textarea name="tramiteRAAdapterVO" property="tramiteRA.observacionAPI" rows="10" cols="30"/></td>			
			</tr>
			<!-- Observaciones RNPA-->
			<tr>
				<td class="normal"><label><bean:message bundle="rod" key="rod.tramiteRA.observacionesRNPA.label"/>: </label>
				<html:textarea name="tramiteRAAdapterVO" property="tramiteRA.observacionRNPA" rows="10" cols="30"/></td>			
			</tr>
		</table>
	</fieldset>
	<!-- TramiteRA -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
	   	    	
				<logic:equal name="tramiteRAAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="tramiteRAAdapterVO" property="act" value="agregar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
					</html:button>
				</logic:equal>
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