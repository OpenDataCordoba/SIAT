<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/AdministrarCuenta.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="pad" key="pad.cuentaAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
      <!-- Cuenta -->
      <fieldset>
         <legend><bean:message bundle="pad" key="pad.cuenta.title"/></legend>
         <table class="tabladatos">
            <tr>
               <td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
               <td class="normal"><bean:write name="cuentaAdapterVO" property="cuenta.recurso.desRecurso"/></td>
               <td><label><bean:message bundle="pad" key="pad.objImp.label"/>: </label></td>
               <td class="normal"><bean:write name="cuentaAdapterVO" property="cuenta.objImp.clave"/></td>
            </tr>
            <tr>
               <td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.label"/>: </label></td>
               <td class="normal"><bean:write name="cuentaAdapterVO" property="cuenta.numeroCuenta"/></td>
               <td><label><bean:message bundle="pad" key="pad.cuenta.codGesCue.label"/>: </label></td>
               <td class="normal"><bean:write name="cuentaAdapterVO" property="cuenta.codGesCue"/></td>
            </tr>
            <tr>
               <td><label><bean:message bundle="pad" key="pad.cuenta.fechaAlta.label"/>: </label></td>
               <td class="normal"><bean:write name="cuentaAdapterVO" property="cuenta.fechaAltaView"/></td>
               <td><label><bean:message bundle="pad" key="pad.cuenta.fechaBaja.label"/>: </label></td>
               <td class="normal"><bean:write name="cuentaAdapterVO" property="cuenta.fechaBajaView"/></td>
            </tr>
            <tr>
               <td><label><bean:message bundle="pad" key="pad.cuenta.esExcluidaEmision.label"/>: </label></td>
               <td class="normal"><bean:write name="cuentaAdapterVO" property="cuenta.esExcluidaEmision.value"/></td>
               <td><label><bean:message bundle="pad" key="pad.cuenta.permiteImpresion.label"/>: </label></td>
               <td class="normal"><bean:write name="cuentaAdapterVO" property="cuenta.permiteImpresion.value"/></td>
            </tr>
			<!-- Descripcion Titular principal -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.nomTitPri.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cuentaAdapterVO" property="cuenta.nomTitPri"/></td>
			</tr>
            <!-- Descripcion del domicilio de envio -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.desDomEnv.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cuentaAdapterVO" property="cuenta.desDomEnv"/></td>
			</tr>
			<!-- Catastral del domicilio de envio -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.catDomEnv.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cuentaAdapterVO" property="cuenta.catDomEnv"/></td>
			</tr>
	        <!-- Observacion -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.observacion.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cuentaAdapterVO" property="cuenta.observacion"/></td>
			</tr>
			<tr>				
				<td align="right" colspan="4">
					<bean:define id="modificarEncabezadoEnabled" name="cuentaAdapterVO" property="modificarEncabezadoEnabled"/>
					<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
						'<bean:write name="cuentaAdapterVO" property="cuenta.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="base" key="abm.button.modificar"/>"/>
				</td>
			</tr>	            
         </table>
      </fieldset>
      <!-- Fin Cuenta -->

      <!-- Broche -->
      <fieldset>
         <legend><bean:message bundle="pad" key="pad.broche.title"/></legend>
         <table class="tabladatos">

         <logic:equal name="cuentaAdapterVO" property="poseeBroche" value="false">
			<tr>
				<td class="normal"><label>No posee broche asignado.</label></td>
			</tr>
			<tr>				
				<td align="right" colspan="4">
					<logic:equal name="cuentaAdapterVO" property="asignarBrocheEnabled" value="enabled">
					    <input type="button" class="boton" onClick="submitForm('asignarBroche', 
						'<bean:write name="cuentaAdapterVO" property="cuenta.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="base" key="abm.button.asignar"/>"/>
					    </input>
					</logic:equal>
				</td>
			</tr>	            
         </logic:equal>
         
         <logic:equal name="cuentaAdapterVO" property="poseeBroche" value="true">
			<tr>
				<!-- Numero -->		
				<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.nro.label"/>: </label></td>
				<td class="normal"><bean:write name="cuentaAdapterVO" property="cuenta.broche.idView"/></td>					
				<!-- Tipo Broche -->		
				<td><label>&nbsp;<bean:message bundle="pad" key="pad.tipoBroche.label"/>: </label></td>
				<td class="normal"><bean:write name="cuentaAdapterVO" property="cuenta.broche.tipoBroche.desTipoBroche"/></td>
			</tr>
			<!-- Titular o Descripcion-->		
			<tr>
				<logic:equal name="cuentaAdapterVO" property="cuenta.broche.tipoBroche.id" value="1">	<!-- TipoBroche=Administrativo -->	
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.titular.label"/>: </label></td>
				</logic:equal>
				<logic:notEqual name="cuentaAdapterVO" property="cuenta.broche.tipoBroche.id" value="1">	<!-- TipoBroche<>Administrativo -->		
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.desBroche.label"/>: </label></td>
				</logic:notEqual>
				<td class="normal"><bean:write name="cuentaAdapterVO" property="cuenta.broche.desBroche"/></td>					
			</tr>
			
			<!-- Inclucion de Caso -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="cuentaAdapterVO" property="cuenta.broCue"/>
					<%@ include file="/cas/caso/includeCasoView.jsp" %>				
				</td>
			</tr>
			<!-- Fin Inclucion de Caso -->
		
			<tr>
				<!-- Fecha Alta -->
				<td><label>&nbsp;<bean:message bundle="pad" key="pad.broCue.fechaAlta.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cuentaAdapterVO" property="cuenta.broCue.fechaAltaView"/></td>					
			</tr>
			
			<tr>				
				<td align="right" colspan="4">
					<logic:equal name="cuentaAdapterVO" property="asignarBrocheEnabled" value="enabled">
					    <input type="button" class="boton" onClick="submitForm('modificarBrocheInit', 
						'<bean:write name="cuentaAdapterVO" property="cuenta.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="base" key="abm.button.modificar"/>"/>
					    </input>

					    <input type="button" class="boton" onClick="submitForm('quitarBrocheInit', 
						'<bean:write name="cuentaAdapterVO" property="cuenta.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="base" key="abm.button.quitar"/>"/>
					    </input>
				    </logic:equal>
				</td>
			</tr>	            

		  </logic:equal>

         </table>
      </fieldset>
      <!-- Fin Broche -->


      <!-- Domicilio de envio -->
         <!-- Inclusion de los datos del Domicilio de envio -->
         <bean:define id="domicilioVO" name="cuentaAdapterVO" property="cuenta.domicilioEnvio"/>
         <bean:define id="act"         name="cuentaAdapterVO" property="cuenta.actDomicilioEnvio"/>
         <bean:define id="modificarDomicilioEnabled" name="cuentaAdapterVO" property="modificarDomicilioEnvioEnabled"/>
         <bean:define id="agregarDomicilioEnabled"   name="cuentaAdapterVO" property="agregarDomicilioEnvioEnabled"/>
         <bean:define id="domicilioEnvio" value="true"/>
         <%@ include file="/pad/ubicacion/includeDomicilioView.jsp" %>
      <!-- Fin Domicilio de Envio -->

     <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
        <caption><bean:message bundle="pad" key="pad.cuenta.listCuentaTitular.label"/></caption>
         <tbody>
           <logic:notEmpty  name="cuentaAdapterVO" property="cuenta.listCuentaTitular">
               <tr>
                 <th width="1">&nbsp;</th> <!-- Ver -->
                 <th width="1">&nbsp;</th> <!-- Modificar -->
                 <th width="1">&nbsp;</th> <!-- Eliminar -->
                 <th width="1">&nbsp;</th> <!-- Setear titular principal -->
                 <th align="left"><bean:message bundle="pad" key="pad.cuentaTitular.fechaDesde.label"/></th>
                 <th align="left"><bean:message bundle="pad" key="pad.cuentaTitular.fechaHasta.label"/></th>
                 <th align="left"><bean:message bundle="pad" key="pad.cuentaTitular.titular.label"/></th>
                 <th align="left"><bean:message bundle="pad" key="pad.documento.tipoDocumentoNumero.label"/></th>
                 <th align="left"><bean:message bundle="pad" key="pad.persona.cuit.label"/></th>
                 <th align="left"><bean:message bundle="pad" key="pad.tipoTitular.label"/></th>
                 <th align="left"><bean:message bundle="pad" key="pad.cuentaTitular.informacion.label"/></th>
              </tr>
              <logic:iterate id="CuentaTitularVO" name="cuentaAdapterVO" property="cuenta.listCuentaTitular">
                 <tr>
                    <!-- Ver -->
                    <td>
						<logic:equal name="cuentaAdapterVO" property="verCuentaTitularEnabled" value="enabled">
							<logic:equal name="CuentaTitularVO" property="verEnabled" value="enabled">
								<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verCuentaTitular', '<bean:write name="CuentaTitularVO" property="id" bundle="base" formatKey="general.format.id"/>');">
									<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
								</a>
							</logic:equal>	
							<logic:notEqual name="CuentaTitularVO" property="verEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
							</logic:notEqual>
						</logic:equal>
						<logic:notEqual name="cuentaAdapterVO" property="verCuentaTitularEnabled" value="enabled">
							<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
						</logic:notEqual>
					</td>
					<!-- Modificar-->								
					<td>
						<logic:equal name="cuentaAdapterVO" property="modificarCuentaTitularEnabled" value="enabled">
							<logic:equal name="CuentaTitularVO" property="modificarEnabled" value="enabled">
								<logic:equal name="cuentaAdapterVO" property="cuenta.recurso.modiTitCtaManual.id" value="1">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarCuentaTitular', '<bean:write name="CuentaTitularVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
									</a>
								</logic:equal>	
								<logic:notEqual name="cuentaAdapterVO" property="cuenta.recurso.modiTitCtaManual.id" value="1">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</logic:equal>	
							<logic:notEqual name="CuentaTitularVO" property="modificarEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
							</logic:notEqual>
						</logic:equal>
						<logic:notEqual name="cuentaAdapterVO" property="modificarCuentaTitularEnabled" value="enabled">
							<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
						</logic:notEqual>
					</td>
					
					<!-- Eliminar-->								
					<td>
						<logic:equal name="cuentaAdapterVO" property="eliminarCuentaTitularEnabled" value="enabled">
							<logic:equal name="CuentaTitularVO" property="eliminarEnabled" value="enabled">
								<logic:equal name="cuentaAdapterVO" property="cuenta.recurso.modiTitCtaManual.id" value="1">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarCuentaTitular', '<bean:write name="CuentaTitularVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
									</a>
								</logic:equal>	
								<logic:notEqual name="cuentaAdapterVO" property="cuenta.recurso.modiTitCtaManual.id" value="1">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</logic:equal>	
							<logic:notEqual name="CuentaTitularVO" property="eliminarEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
							</logic:notEqual>
						</logic:equal>
						<logic:notEqual name="cuentaAdapterVO" property="eliminarCuentaTitularEnabled" value="enabled">
							<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
						</logic:notEqual>
					</td>
                    
                    <!-- Setear Titular Principal -->
                    <td>
                    	<logic:equal name="CuentaTitularVO" property="esTitularPrincipal.id" value="0">
							<logic:equal name="cuentaAdapterVO" property="marcarPrincipalEnabled" value="enabled">
								<logic:equal name="CuentaTitularVO" property="marcarPrincipalBussEnabled" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('marcarPrincipal', '<bean:write name="CuentaTitularVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="Marcar como principal" border="0" src="<%=request.getContextPath()%>/images/iconos/titularPpal0.gif"/>
									</a>
								</logic:equal>	
								<logic:notEqual name="CuentaTitularVO" property="marcarPrincipalBussEnabled" value="true">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/titularPpal1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="cuentaAdapterVO" property="marcarPrincipalEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/titularPpal1.gif"/>
							</logic:notEqual>
						</logic:equal>
						<logic:equal name="CuentaTitularVO" property="esTitularPrincipal.id" value="1">
							&nbsp;
						</logic:equal>
					</td>
                    
                    <td><bean:write name="CuentaTitularVO" property="fechaDesdeView"/>&nbsp;</td>
                    <td><bean:write name="CuentaTitularVO" property="fechaHastaView" />&nbsp;</td>
                    <td><bean:write name="CuentaTitularVO" property="contribuyente.persona.view" />&nbsp;</td>
                    <td><bean:write name="CuentaTitularVO" property="contribuyente.persona.documento.tipoyNumeroView" />&nbsp;</td>
                    <td><bean:write name="CuentaTitularVO" property="contribuyente.persona.cuit" />&nbsp;</td>
                    <td><bean:write name="CuentaTitularVO" property="tipoTitular.desTipoTitular" />&nbsp;</td>
                    <td><bean:write name="CuentaTitularVO" property="informacionView" />&nbsp;</td>
                 </tr>
              </logic:iterate>
           </logic:notEmpty>
           <logic:empty  name="cuentaAdapterVO" property="cuenta.listCuentaTitular">
              <tr><td align="center">
              <bean:message bundle="base" key="base.noExistenRegitros"/>
              </td></tr>
           </logic:empty>
           	<!-- Agregar -->
			<logic:equal name="cuentaAdapterVO" property="cuenta.recurso.modiTitCtaManual.id" value="1">
			<tr>
				<td colspan="20" align="right">
	  				<bean:define id="agregarCuentaTitularEnabled" name="cuentaAdapterVO" property="agregarCuentaTitularEnabled"/>
	  					<input type="button" <%=agregarCuentaTitularEnabled%> class="boton" 
							onClick="submitForm('agregarCuentaTitular', ' ');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
				</td>
			</tr>
			</logic:equal>
        </tbody>
     </table>


	<!-- RecAtrCue -->		
	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
		<caption><bean:message bundle="def" key="def.recurso.listRecAtrCue.ref"/></caption>
    	<tbody>
			<logic:notEmpty  name="cuentaAdapterVO" property="cuenta.listRecAtrCueDefinition">	    	
		    	<tr>
					<th width="1">&nbsp;</th> <!-- Modificar -->
					<th align="left"><bean:message bundle="def" key="def.atributo.label"/></th>
					<th align="left" colspan="2">Valor</th>
				</tr>
				<logic:iterate id="RecAtrCueDefinition" name="cuentaAdapterVO" property="cuenta.listRecAtrCueDefinition" indexId="count">
						
					<bean:define id="AtrVal" name="RecAtrCueDefinition"/>
						
					<tr>
						<td>
							<!-- Modificar-->
							<logic:equal name="cuentaAdapterVO" property="modificarRecAtrCueVEnabled" value="enabled">
								<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarRecAtrCueV', '<bean:write name="AtrVal" property="id" bundle="base" formatKey="general.format.id"/>');">
									<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
								</a>
							</logic:equal>
							<logic:notEqual name="cuentaAdapterVO" property="modificarRecAtrCueVEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
							</logic:notEqual>
						</td>
						
						<%@ include file="/def/atrDefinitionView4Edit.jsp" %>							
						
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			<logic:empty  name="cuentaAdapterVO" property="cuenta.listRecAtrCueDefinition">
				<tr><td align="center">
				<bean:message bundle="base" key="base.noExistenRegitros"/>
				</td></tr>
			</logic:empty>					
		
		</tbody>
	</table>
	<!-- RecAtrCue -->


	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
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
<!-- Fin Tabla que contiene todos los formularios -->