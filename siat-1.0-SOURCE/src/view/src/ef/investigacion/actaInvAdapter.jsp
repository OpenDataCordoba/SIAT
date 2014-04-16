<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarActaInv.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="ef" key="ef.opeInvConEditAdapter.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
		
	<!-- OpeInv -->
	<bean:define id="opeInvVO" name="actaInvAdapterVO" property="opeInvCon.opeInv"/>
	<%@include file="/ef/investigacion/includeOpeInvView.jsp" %>	
	<!-- OpeInv -->
	
	<!-- OpeInvCon -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.opeInvConAdapter.contribuyente.label"/></legend>
		
		<table class="tabladatos">
		<!-- Persona -->
		<tr>
			<td><label><bean:message bundle="pad" key="pad.persona.label"/>: </label></td>
			<td class="normal" colspan="3">
				<bean:write name="actaInvAdapterVO" property="opeInvCon.contribuyente.persona.represent"/>
			</td>
		</tr>
		<tr>
			<td><label><bean:message bundle="ef" key="ef.opeInvConAdapter.nroIsib.label"/>: </td>
			<td class="normal"><bean:write name="actaInvAdapterVO" property="opeInvCon.contribuyente.nroIsib"/></td>
		</tr>
		<tr>
			<td><label><bean:message bundle="ef" key="ef.estadoOpeInvCon.label"/>: </label></td>
			<td class="normal" colspan="3">
				<bean:write name="actaInvAdapterVO" property="opeInvCon.estadoOpeInvCon.desEstadoOpeInvCon"/>
			</td>
		</tr>		
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	
	
	<!-- OpeInvCon datos de la persona  -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.opeInvCon.title"/></legend>
		
		<table class="tabladatos">
			<bean:define id="personaVO" name="actaInvAdapterVO" property="opeInvCon.contribuyente.persona"/>
			<%@ include file="/pad/persona/includePersona.jsp"%>
			
			<logic:equal name="actaInvAdapterVO" property="modificarBussEnabled" value="true">
				<tr>
					<td align="right" colspan="4">
						
						<button type="button" name="btnModificar" class="boton" onclick="submitForm('modificarPersona', '<bean:write name="actaInvAdapterVO" property="opeInvCon.contribuyente.persona.id" bundle="base" formatKey="general.format.id"/>');">
								Modificar Datos
						</button>
					</td>
				</tr>
			</logic:equal>			
		</table>		
	</fieldset>
	
	<!-- Datos del acta -->
	<logic:equal name="actaInvAdapterVO" property="modificarBussEnabled" value="true">
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.actaInvAdapter.datosActaInv.label"/></legend>
			
				<table class="tabladatos">
					<!-- Numero -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.numeroActa.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:text name="actaInvAdapterVO" property="opeInvCon.actaInv.numeroActa" size="10"/>
						</td>
					</tr>
	
					<!-- Anio -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.anioActa.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:text name="actaInvAdapterVO" property="opeInvCon.actaInv.anioActa" size="6" maxlength="4"/>
						</td>
					</tr>
					
					<!-- Fecha Inicio -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.fechaInicio.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:text name="actaInvAdapterVO" property="opeInvCon.actaInv.fechaInicioView" styleId="fechaInicioView" size="15" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaInicioView');" id="a_fechaInicioView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>													
						</td>
					</tr>	
	
					<!-- Fecha Fin -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.fechaFin.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:text name="actaInvAdapterVO" property="opeInvCon.actaInv.fechaFinView" styleId="fechaFinView" size="15" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaFinView');" id="a_fechaFinView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>													
						</td>
					</tr>	
	
					<!-- Estado Acta -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.estadoActa.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="actaInvAdapterVO" property="opeInvCon.actaInv.estadoActa.desEstadoActa"/>
						</td>
					</tr>	
								
					<!-- Investigador -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.investigador.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="actaInvAdapterVO" property="opeInvCon.actaInv.investigador.desInvestigador"/>
						</td>
					</tr>	
							
					<!-- Obs -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.observacion.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:textarea name="actaInvAdapterVO" property="opeInvCon.actaInv.observacion"/>
						</td>
					</tr>
					
					<!-- otros datos -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.otrosDatos.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:textarea name="actaInvAdapterVO" property="opeInvCon.actaInv.otrosDatos"/>
						</td>
					</tr>												
				</table>	
			</fieldset>
			<!-- Datos del objImp - editar-->
			<fieldset>
				<legend><bean:message bundle="ef" key="ef.actaInvAdapter.datosObjImp.label"/></legend>						
				<table class="tabladatos">
					<!-- nroFicha -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.nroFicha.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:text name="actaInvAdapterVO" property="opeInvCon.actaInv.nroFicha"/>
						</td>
					</tr>
					
					<!-- Fecha Inicio actividad -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.fecIniAct.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:text name="actaInvAdapterVO" property="opeInvCon.actaInv.fecIniActView" styleId="fecIniActView" size="15" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fecIniActView');" id="a_fecIniActView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>													
						</td>
					</tr>	
					
					<!-- PerEnRelDep -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.perEnRelDep.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:text name="actaInvAdapterVO" property="opeInvCon.actaInv.perEnRelDep"/>
						</td>
					</tr>
					
					<!-- ActDes -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.actDes.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:text name="actaInvAdapterVO" property="opeInvCon.actaInv.actDes"/>
						</td>
					</tr>
					
					<!-- LocRos -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.locRos.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:text name="actaInvAdapterVO" property="opeInvCon.actaInv.locRosario"/>
						</td>
					</tr>
					
					<!-- LocOtrProv -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.locOtrPro.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:text name="actaInvAdapterVO" property="opeInvCon.actaInv.locOtrPro"/>
						</td>
					</tr>					

					<!-- pubRod -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.pubRod.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:text name="actaInvAdapterVO" property="opeInvCon.actaInv.pubRod"/>
						</td>
					</tr>					

					<!-- locFueRosEnSFe -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.locFueRosEnSFe.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:text name="actaInvAdapterVO" property="opeInvCon.actaInv.locFueRosEnSFe"/>
						</td>
					</tr>
					
					<!-- ubicacionLocales -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.ubicacionLocales.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:text name="actaInvAdapterVO" property="opeInvCon.actaInv.ubicacionLocales"/>
						</td>
					</tr>
					
					<!-- cartelesPubl -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.cartelesPubl.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:text name="actaInvAdapterVO" property="opeInvCon.actaInv.cartelesPubl"/>
						</td>
					</tr>
					
					<!-- copiaContrato -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.copiaContrato.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:text name="actaInvAdapterVO" property="opeInvCon.actaInv.copiaContrato"/>
						</td>
					</tr>					
						
					<!-- terceros -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.terceros.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:text name="actaInvAdapterVO" property="opeInvCon.actaInv.terceros"/>
						</td>
					</tr>											
					
					<!-- ticFacNom -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.ticFacNom.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:text name="actaInvAdapterVO" property="opeInvCon.actaInv.ticFacNom"/>
						</td>
					</tr>					
				</table>
			</fieldset>			
			<!-- FIN Datos del objImp - editar -->
		</logic:equal>
			
			
		<logic:equal name="actaInvAdapterVO" property="modificarBussEnabled" value="false">
			<bean:define id="actaInv" name="actaInvAdapterVO" property="opeInvCon.actaInv"/>
			<%@include file="/ef/investigacion/includeActaInvView.jsp" %>						
		</logic:equal>					
	<!-- FIN Datos del acta -->
	
	
	
	<!-- estado del contribuyente -->
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.actaInvAdapter.cambioEstadoContr.label"/></legend>
			<table class="tabladatos">
					<!-- Numero -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.estadoContr.label"/>: </label></td>
						<td class="normal" colspan="3">
						   	<logic:equal name="actaInvAdapterVO" property="modificarBussEnabled" value="true">
								<html:select name="actaInvAdapterVO" property="opeInvCon.estadoOpeInvCon.id">
									<html:optionsCollection name="actaInvAdapterVO" property="listEstadoOpeInvCon" label="desEstadoOpeInvCon" value="id"/>
								</html:select>							
							</logic:equal>
							<logic:equal name="actaInvAdapterVO" property="modificarBussEnabled" value="false">
								<bean:write name="actaInvAdapterVO" property="opeInvCon.estadoOpeInvCon.desEstadoOpeInvCon"/>
							</logic:equal>
						</td>
					</tr>
			</table>
		</fieldset>
	
	<!-- fin estado del contribuyente -->
		
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="33%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td width="33%">
   	    		<logic:equal name="actaInvAdapterVO" property="pedidoAprobacionBussEnabled" value="true">
					<bean:define id="pedidoAprobacionEnabled" name="actaInvAdapterVO" property="pedidoAprobacionEnabled"/>
					<input type="button" <%=pedidoAprobacionEnabled%> class="boton" 
						onClick="submitForm('pedidoAprobacion', '0');" 
						value="<bean:message bundle="ef" key="ef.actaInvAdapter.button.pedidoAprobacion"/>"/>   	    	
	   	   		</logic:equal>
   	    	</td>
   	    	<td align="right" width="33%">
   	    		<logic:equal name="actaInvAdapterVO" property="modificarBussEnabled" value="true">
					<input type="button" class="boton" onclick="submitForm('guardar', '');"
					value="<bean:message bundle="ef" key="ef.actaInvAdapter.button.guardar"/>"
					/>									
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
<!-- pageName: ActaInvAdapter.jsp -->