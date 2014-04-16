<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">
          <%@include file="/base/submitForm.js"%>  
</script>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/AdministrarEncCuenta.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
      
	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="encCuentaAdapterVO"/>
		<bean:define id="poseeParam" value="true" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>
        
    <span id="blockSimple" style="display:block">
    	
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
	              <logic:notEqual name="encCuentaAdapterVO" property="comboRecursoEnabled" value="true">
	               <td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
	               <td class="normal">
	               		<bean:write name="encCuentaAdapterVO" property="cuenta.recurso.desRecurso"/>
	               </td>
	              </logic:notEqual>	
	              	
	              <logic:equal name="encCuentaAdapterVO" property="comboRecursoEnabled" value="true">
	               	
	               	<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal">
						<html:select name="encCuentaAdapterVO" property="cuenta.recurso.id" styleClass="select" styleId="cboRecurso" style="width:90%" onchange="submitForm('paramRecurso', '');">
							<bean:define id="includeRecursoList" name="encCuentaAdapterVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="encCuentaAdapterVO" property="cuenta.recurso.id"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>
						
						<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
							<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
							src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
						</a>
					
					</td>
	              </logic:equal>
	           </tr>
	           
	           <logic:equal name="encCuentaAdapterVO" property="buscarObjImpEnabled" value="true">
	            <tr>
	               <td><label><bean:message bundle="pad" key="pad.objImp.label"/>: </label></td>
	               <td class="normal" colspan="3">
	               		<html:text name="encCuentaAdapterVO" property="cuenta.objImp.clave" size="20" maxlength="100" styleClass="datos" disabled="true"/>
						<html:button property="btnBucarObjImp" styleClass="boton" onclick="submitForm('buscarObjImp', '');">
							<bean:message bundle="pad" key="pad.cuentaAdapter.button.buscarObjImp"/>						
						</html:button>
	               </td>
	            </tr>
	           </logic:equal>
	           
	           <!-- numero cuenta -->
	           <logic:equal name="encCuentaAdapterVO" property="numeroCuentaEnabled" value="true">
	            <tr>
	               <td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.numeroCuenta.label"/>: </label></td>
	               <td class="normal">
	               		<html:text name="encCuentaAdapterVO" property="cuenta.numeroCuenta" size="15" maxlength="10" styleClass="datos"/>
	               </td>
	            </tr>
	           </logic:equal>
	           <logic:notEqual name="encCuentaAdapterVO" property="numeroCuentaEnabled" value="true">
	           	<tr>
	               <td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.label"/>: </label></td>
	               <td class="normal">
	               		<bean:write name="encCuentaAdapterVO" property="cuenta.recurso.genCue.desGenCue"/>
	               </td>	               
	            </tr>
	           </logic:notEqual>
	           
	           <!-- codigo gestion personal -->
	           <logic:equal name="encCuentaAdapterVO" property="codGesCueEnabled" value="true">
	            <tr>   
	               <td><label><bean:message bundle="pad" key="pad.cuenta.codGesCue.label"/>: </label></td>
	               <td class="normal"><html:text name="encCuentaAdapterVO" property="cuenta.codGesCue" size="15" maxlength="10" styleClass="datos" />
	               </td>
	            </tr>
	           </logic:equal>
	           <logic:notEqual name="encCuentaAdapterVO" property="codGesCueEnabled" value="true">
	           	<tr>   
	               <td><label><bean:message bundle="pad" key="pad.cuenta.codGesCue.label"/>: </label></td>
	               <td class="normal">
	               		<bean:write name="encCuentaAdapterVO" property="cuenta.recurso.genCodGes.desGenCodGes"/>
	               </td>
	            </tr>            
	           </logic:notEqual>
	           
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.fechaAlta.label"/>: </label></td>
				<td class="normal">
					<html:text name="encCuentaAdapterVO" property="cuenta.fechaAltaView" styleId="fechaAltaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaAltaView');" id="a_fechaAltaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
					</a>
				</td>
			</tr>
			
			<!-- Si no es usuario CMD preguntamos -->
			<logic:notEqual name="encCuentaAdapterVO" property="esCMD" value="true">
				<tr>	
					<!-- Es Excluida de la Emision -->
					<td><label><bean:message bundle="pad" key="pad.cuenta.esExcluidaEmision.label"/>: </label></td>
					<td class="normal">
						<html:select name="encCuentaAdapterVO" property="cuenta.esExcluidaEmision.id"  styleClass="select">
							<html:optionsCollection name="encCuentaAdapterVO" property="listSiNo" label="value" value="id"/>
						</html:select>
					</td>
				</tr>
			</logic:notEqual>
			
			<!-- Observacion -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.observacion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:textarea name="encCuentaAdapterVO" property="cuenta.observacion" cols="80" rows="15"/>
				</td>
			</tr>
			
	        </table>
	     </fieldset>
	     <!-- Fin Cuenta -->
	      
	     <!-- Titular Principal -->
	     <fieldset>
	        <legend><bean:message bundle="pad" key="pad.cuenta.titularPrincipal.label"/></legend>
	        <table class="tabladatos">
	     
	    		<bean:define id="personaVO" name="encCuentaAdapterVO" property="titular"/>
	    		
			<!-- Persona de solo lectura -->     		
	    		<logic:notEqual name="encCuentaAdapterVO" property="modificarTitularEnabled" value="true">
	     		<tr>
				 	<td colspan="4">
	     				<%@ include file="/pad/persona/includePersonaReducida.jsp"%>
	     			</td>
	     		</tr>	
	    		</logic:notEqual>
	    		
	    		<!-- Busqueda de persona habilitada -->
	    		<logic:equal name="encCuentaAdapterVO" property="modificarTitularEnabled" value="true">
				 <tr>
				 	<td colspan="4">
						<table width="100%">
						 	<tr>
							    <!-- Ingreso de datos -->
							    <td class="normal"><label>(*)&nbsp;<bean:message bundle="pad" key="pad.persona.sexo.label"/>: </label>								
									<select name="titular.sexo.id" class="select">
										<option value="-1" <logic:equal name="personaVO" property="sexo.codigo" value="S">selected="selected"</logic:equal>>Seleccionar...</option>									
										
										<option value="1" <logic:equal name="personaVO" property="sexo.codigo" value="M">selected="selected"</logic:equal>>Masculino</option>
										
										<option value="0" <logic:equal name="personaVO" property="sexo.codigo" value="F">selected="selected" </logic:equal>>Femenino</option>
									</select>
									
									<label>(*)&nbsp;<bean:message bundle="pad" key="pad.documento.numero.ref"/>: </label> 
									<input type="text" name="titular.documento.numeroView" value='<bean:write name="personaVO" property="documento.numeroView"/>' />
										
								   	&nbsp;
								
								   	<html:button property="btnBuscarSolicitante"  styleClass="boton" onclick="submitForm('buscarPersonaSimple', '');">
										<bean:message bundle="pad" key="pad.button.buscarPersona"/>
								   	</html:button>
								   	&nbsp;
								   	<html:button property="btnBuscarSolicitante"  styleClass="boton" onclick="submitForm('limpiarPersona', '');">
										<bean:message bundle="base" key="abm.button.limpiar"/>
									</html:button>		   
								</td>							
						    </tr>
						  </table>
					 </td>
				 </tr>
					
				<logic:equal name="personaVO" property="personaBuscada" value="true">
					
					<!-- Errores -->
					<logic:equal name="personaVO" property="hasError" value="true">
						<tr>
							<td colspan="6">
								<table width="100%">
									<tr>
										<td class="normal" colspan="6">
											<ul class="error" id="errorsSearch">
												<logic:iterate id="valueError" name="personaVO" property="listErrorValues">
													<li>
												  		<bean:write name="valueError"/>
													</li>
												</logic:iterate>
											</ul>
										</td>
									</tr>
									
									
									<logic:equal name="personaVO" property="personaEncontrada" value="false">
										<tr>
											<td>
											</td>
											<td>
											</td>
											<td>
											</td>
											<td>
												<div align="center">
													<html:button property="btnBuscarSolicitante"  styleClass="boton" onclick="submitForm('buscarPersona', '');">
														<bean:message bundle="pad" key="pad.button.busquedaAvanzada"/>
													</html:button>
												</div>
											</td>
										</tr>
									</logic:equal>
								</table>						
							</td>
						</tr>				
					</logic:equal>
					
					<logic:notEqual name="personaVO" property="hasError" value="true">
						<!-- Messages -->
						<tr>
							<td colspan="6">
								<table width="100%">
								
									<tr>
										<logic:equal name="personaVO" property="personaEncontrada" value="true">
											<%@ include file="/pad/persona/includePersonaReducida.jsp"%>
										</logic:equal>
									</tr>
									<tr>
										<td>
										</td>
										<td>
										</td>
										<td>
										</td>
										<td>
											<div align="center">
												<html:button property="btnBuscarSolicitante"  styleClass="boton" onclick="submitForm('buscarPersona', '');">
													<bean:message bundle="pad" key="pad.button.busquedaAvanzada"/>
												</html:button>
											</div>
										</td>
									</tr>
								</table>						
							</td>
						</tr>
					</logic:notEqual>	
				</logic:equal>
				
				</logic:equal>
			</table>
		</fieldset>      
	    <!-- Fin Titular Principal -->
	      
	     <!-- Otros Titulares -->
	     <logic:notEmpty name="encCuentaAdapterVO" property="cuenta.listCuentaTitular">
	     <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
	           <caption><bean:message bundle="pad" key="pad.cuenta.listCuentaTitular.label"/></caption>
	            <tbody>
	              <logic:notEmpty  name="encCuentaAdapterVO" property="cuenta.listCuentaTitular">
	                  <tr>
		                 <th align="left"><bean:message bundle="pad" key="pad.cuentaTitular.titular.label"/></th>
		                 <th align="left"><bean:message bundle="pad" key="pad.documento.tipoDocumentoNumero.label"/></th>
		                 <th align="left"><bean:message bundle="pad" key="pad.persona.cuit.label"/></th>
		                 <th align="left"><bean:message bundle="pad" key="pad.tipoTitular.label"/></th>
	                 </tr>
	                 <logic:iterate id="CuentaTitularVO" name="encCuentaAdapterVO" property="cuenta.listCuentaTitular">
	                    <tr>
		                    <td><bean:write name="CuentaTitularVO" property="contribuyente.persona.view" />&nbsp;</td>
		                    <td><bean:write name="CuentaTitularVO" property="contribuyente.persona.documento.tipoyNumeroView" />&nbsp;</td>
		                    <td><bean:write name="CuentaTitularVO" property="contribuyente.persona.cuit" />&nbsp;</td>
		                    <td><bean:write name="CuentaTitularVO" property="tipoTitular.desTipoTitular" />&nbsp;</td>
	                    </tr>
	                 </logic:iterate>
	              </logic:notEmpty>              
	           </tbody>
	        </table>
	     </logic:notEmpty>
	     <!-- Fin Otros Titulares -->
	      
	     <!-- Domicilio de Envio -->
	     <bean:define id="domicilioVO" name="encCuentaAdapterVO" property="cuenta.domicilioEnvio"/>
	     <bean:define id="listSiNo" name="encCuentaAdapterVO" property="listSiNo"/>
	     <bean:define id="domicilioEnvio" value="true"/> <!-- indica que se trata de domicilio de envio -->
	     <%@ include file="/pad/ubicacion/includeDomicilioEdit.jsp" %>
	     <!-- Fin Domicilio de Envio -->
	    	   
		<!-- Atributos a Valorizar -->
		<fieldset>
		 	<legend><bean:message bundle="pad" key="pad.cuenta.listRecAtrCueV.label"/></legend>
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="def" key="def.recurso.listRecAtrCue.ref"/></caption>
		    	<tbody>
					<logic:notEmpty name="encCuentaAdapterVO" property="cuenta.listRecAtrCueDefinition">	    	
				    	<tr>
							<th align="left"><bean:message bundle="def" key="def.atributo.label"/></th>
							<th align="left">Valor por Defecto</th>
						</tr>
						<logic:iterate id="RecAtrCueDefinition" name="encCuentaAdapterVO" property="cuenta.listRecAtrCueDefinition" indexId="count">
								
							<bean:define id="AtrVal" name="RecAtrCueDefinition"/>
							
							<tr>
								<%@ include file="/def/atrDefinition4Edit.jsp" %>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty name="encCuentaAdapterVO" property="cuenta.listRecAtrCueDefinition">
						<tr>
							<td align="center">
								<bean:message bundle="pad" key="pad.cuenta.noExistenAtributos"/>
							</td>
						</tr>
					</logic:empty>
				</tbody>
			</table>
		</fieldset>
		<!-- Fin Atributos a Valorizar -->
	    	      
		<table class="tablabotones" width="100%" >
		   	<tr>
	  	   		<td align="left" width="50%">
		   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
		   	    		<bean:message bundle="base" key="abm.button.volver"/>
		   	    	</html:button>
	   	    	</td>
	   	    	<td align="right" width="50%">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
					</html:button>
				</td>
	   	    </tr>
	   	</table>
	
	</span>

    <input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
    <input type="hidden" name="selectedId" value=""/>
    <input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
