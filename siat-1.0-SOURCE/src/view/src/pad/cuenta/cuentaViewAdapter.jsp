<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">
          <%@include file="/base/submitForm.js"%>  
</script>

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
               
               <logic:notEqual name="cuentaAdapterVO" property="act" value="desactivar">
               		<td><label><bean:message bundle="pad" key="pad.cuenta.fechaBaja.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaAdapterVO" property="cuenta.fechaBajaView" /></td>
				</logic:notEqual>
					
				<logic:equal name="cuentaAdapterVO" property="act" value="desactivar">
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.fechaBaja.label"/>: </label></td>
					<td class="normal">
						<html:text name="cuentaAdapterVO" property="cuenta.fechaBajaView" styleId="fechaBajaView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaBajaView');" id="a_fechaBajaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
						</a>
					</td>
				</logic:equal>
            </tr>

            <!-- Catastral del domicilio de envio -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.catDomEnv.label"/>: </label></td>
				<td class="normal"><bean:write name="cuentaAdapterVO" property="cuenta.catDomEnv"/></td>
		        <td><label><bean:message bundle="pad" key="pad.cuenta.esExcluidaEmision.label"/>: </label></td>
               <td class="normal"><bean:write name="cuentaAdapterVO" property="cuenta.esExcluidaEmision.value"/></td>
            </tr>
             <!-- Observacion -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.observacion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cuentaAdapterVO" property="cuenta.observacion"/>
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
			
			<!-- Inclucion de CasoView -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="cuentaAdapterVO" property="cuenta.broCue"/>
					<%@ include file="/cas/caso/includeCasoView.jsp" %>				
				</td>
			</tr>
			<!-- Fin Inclucion de CasoView -->
			<tr>
				<!-- Fecha Alta -->
				<td><label>&nbsp;<bean:message bundle="pad" key="pad.broCue.fechaAlta.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cuentaAdapterVO" property="cuenta.broCue.fechaAltaView"/></td>					
			</tr>
		  </logic:equal>

         </table>
      </fieldset>
      <!-- Fin Broche -->

      <!-- Solo mostramos si la accion es "ver" -->
      <logic:equal name="cuentaAdapterVO" property="act" value="ver">
      
      	<!-- Domicilio de envio -->
        	<bean:define id="domicilioVO" name="cuentaAdapterVO" property="cuenta.domicilioEnvio"/>
	        <%@ include file="/pad/ubicacion/includeDomicilioView.jsp" %>
      	<!-- Fin Domicilio de Envio -->
      
         <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
            <caption><bean:message bundle="pad" key="pad.cuenta.listCuentaTitular.label"/></caption>
             <tbody>
               <logic:notEmpty  name="cuentaAdapterVO" property="cuenta.listCuentaTitular">
                   <tr>
                     <th width="1">&nbsp;</th> <!-- Ver -->
                 <th align="left"><bean:message bundle="pad" key="pad.cuentaTitular.fechaDesde.label"/></th>
                 <th align="left"><bean:message bundle="pad" key="pad.cuentaTitular.fechaHasta.label"/></th>
                 <th align="left"><bean:message bundle="pad" key="pad.cuentaTitular.titular.label"/></th>
                 <th align="left"><bean:message bundle="pad" key="pad.documento.tipoDocumentoNumero.label"/></th>
                 <th align="left"><bean:message bundle="pad" key="pad.persona.cuit.label"/></th>
                 <th align="left"><bean:message bundle="pad" key="pad.tipoTitular.label"/></th>
                 <th align="left"><bean:message bundle="pad" key="pad.persona.tipoPersona.label"/></th>
                     
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
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="cuentaAdapterVO" property="verCuentaTitularEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
							</logic:notEqual>
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
            </tbody>
         </table>
      
      
      	<!-- RecAtrCue -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="def" key="def.recurso.listRecAtrCue.ref"/></caption>
	    	<tbody>
				<logic:notEmpty  name="cuentaAdapterVO" property="cuenta.listRecAtrCueDefinition">	    	
			    	<tr>
						<th align="left"><bean:message bundle="def" key="def.atributo.label"/></th>
						<th align="left" colspan="2">Valor</th>
					</tr>
					<logic:iterate id="RecAtrCueDefinition" name="cuentaAdapterVO" property="cuenta.listRecAtrCueDefinition" indexId="count">
						<bean:define id="AtrVal" name="RecAtrCueDefinition"/>
						<tr>
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
      
      </logic:equal>
      
      <!-- Fin bloque ver -->
            
      <table class="tablabotones" width="100%">
            <tr>
                 <td align="left" width="50%">
                  <logic:notEqual name="cuentaAdapterVO" property="act" value="quitarBrocheInit">
                   <html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
                      <bean:message bundle="base" key="abm.button.volver"/>
                   </html:button>
                  </logic:notEqual>
                  <logic:equal name="cuentaAdapterVO" property="act" value="quitarBrocheInit">
                   <html:button property="btnVolver" styleClass="boton" onclick="submitForm('volverBroche', '');">
                      <bean:message bundle="base" key="abm.button.volver"/>
                   </html:button>
                  </logic:equal>

                </td>
                <td align="right" width="50%">
               <logic:equal name="cuentaAdapterVO" property="act" value="quitarBrocheInit">
                  <html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('quitarBroche', '');">
                     <bean:message bundle="base" key="abm.button.quitar"/>
                  </html:button>
               </logic:equal>
               <logic:equal name="cuentaAdapterVO" property="act" value="eliminar">
                  <html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
                     <bean:message bundle="base" key="abm.button.eliminar"/>
                  </html:button>
               </logic:equal>
               <logic:equal name="cuentaAdapterVO" property="act" value="activar">
                  <html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
                     <bean:message bundle="base" key="abm.button.activar"/>
                  </html:button>
               </logic:equal>
               <logic:equal name="cuentaAdapterVO" property="act" value="desactivar">
                  <html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
                     <bean:message bundle="base" key="abm.button.desactivar"/>
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

	<!-- Inclucion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
   </html:form>
   <!-- Fin Tabla que contiene todos los formularios -->
