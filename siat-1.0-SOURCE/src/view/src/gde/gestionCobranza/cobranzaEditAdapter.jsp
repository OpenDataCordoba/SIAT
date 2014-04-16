<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarCobranza.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
 	<h1><bean:message bundle="gde" key="gde.cobranzaAdapter.title"/></h1>
	<p align="right">
		<button type="button" name="btn2" class="boton" onclick="submitForm('volver','');"><bean:message bundle="base" key="abm.button.volver"/></button>
	</p>
	 

	<!-- Cobranza -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.cobranzaAdapter.legend"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="pad" key="pad.contribuyente.label"/>: </label></td>
					<td class="normal"><bean:write name="cobranzaAdapterVO" property="cobranza.contribuyente.persona.view"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.cobranzaAdapter.ordenControl.label"/>: </label></td>
					<td class="normal"><bean:write name="cobranzaAdapterVO" property="cobranza.ordenControl.ordenControlyTipoView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.cobranza.fechaInicio.label"/>: </label></td>
					<td class="normal"><bean:write name="cobranzaAdapterVO" property="cobranza.fechaInicioView"/></td>
					<td><label><bean:message bundle="gde" key="gde.cobranza.fechaFin.label"/>: </label></td>
					<td class="normal"><bean:write name="cobranzaAdapterVO" property="cobranza.fechaFinView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.cobranza.estadoCobranza.label"/>: </label></td>
					<td class="normal"><bean:write name="cobranzaAdapterVO" property="cobranza.estadoCobranza.desEstado"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.cobranza.importeACobrar.label"/></label></td>
					<td class="normal"><bean:write name="cobranzaAdapterVO" property="cobranza.importeACobrarView"/></td>
					<td><label><bean:message bundle="gde" key="gde.cobranza.impPagGes.label"/>: </label></td>
					<td class="normal"><bean:write name="cobranzaAdapterVO" property="cobranza.impPagGesView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.cobranza.proFecCon.label"/>: </label></td>
					<td class="normal"><bean:write name="cobranzaAdapterVO" property="cobranza.proFecConView"/></td>
				</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.cobranzaDet.legend"/></legend>
			<logic:notEmpty  name="cobranzaAdapterVO" property="cobranza.listCobranzaDet">
				<div class="horizscroll"  style="width: 660px;">
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
						<caption><bean:message bundle="gde" key="gde.cobranzaAdapter.detalle.label"/></caption>
			           	<tbody>
			           		<tr>
				           		<th><bean:message bundle="gde" key="gde.cobranzaDet.cuenta.label"/></th>
				           		<th><bean:message bundle="gde" key="gde.cobranzaDet.periodo.label"/></th>
				           		<th><bean:message bundle="gde" key="gde.cobranzaDet.declarado.label"/></th>
				           		<th><bean:message bundle="gde" key="gde.cobranzaDet.ajuste.label"/></th>
				           		<th><bean:message bundle="gde" key="gde.cobranzaDet.estadoAjuste.label"/></th>
				           		<th><bean:message bundle="gde" key="gde.cobranzaDet.totalPeriodo.label"/></th>
				           		<th><bean:message bundle="gde" key="gde.cobranzaDet.pagos.label"/></th>
				           		<th><bean:message bundle="gde" key="gde.cobranzaDet.convenioVigente.label"/></th>
				           		<th><bean:message bundle="gde" key="gde.cobranzaDet.convenioCaduco.label"/></th>
				           		<th><bean:message bundle="gde" key="gde.cobranzaDet.diferencia.label"/></th>
				           	</tr>           
					        <logic:iterate name="cobranzaAdapterVO" property="cobranza.listCobranzaDet" id="CobranzaDetVO">
					        	<tr>
					        		<td>
					        			<a href="javascript:submitForm('irALiqDeuda','<bean:write name="CobranzaDetVO" property="cuenta.id" bundle="base" formatKey="general.format.id"/>');" >
					        			<bean:write name="CobranzaDetVO" property="cuentaView"/>
					        			</a>
					        		</td>
					        		<td>
					        			<bean:write name="CobranzaDetVO" property="periodoAnioView"/>
					        		</td>
					        		<td>
					        			<bean:write name="CobranzaDetVO" property="importeInicialView"/>
					        		</td>
					        		<td>
					        			<bean:write name="CobranzaDetVO" property="ajusteView"/>
					        		</td>
					        		<td>
					        			<bean:write name="CobranzaDetVO" property="estadoAjuste.desEstado"/>
					        		</td>
					        		<td>
					        			<bean:write name="CobranzaDetVO" property="totalPeriodoView"/>
					        		</td>
					        		<td>
					        			<bean:write name="CobranzaDetVO" property="pagosView"/>
					        		</td>
					        		<td>
					        			<bean:write name="CobranzaDetVO" property="enConVigView"/>
					        		</td>
					        		<td>
					        			<bean:write name="CobranzaDetVO" property="enConCadView"/>
					        		</td>
					        		<td>
					        			<bean:write name="CobranzaDetVO" property="diferenciaView"/>
					        		</td>
					        	</tr>
					        </logic:iterate>
					        <tr>
					        	<td colspan="5" class="celdatotales" align="right">
						        	<bean:message bundle="gde" key="gde.decJurAdapter.totales.label"/>: 
						        </td>
						        <td class="celdatotales">
						        	<b><bean:write name="cobranzaAdapterVO" property="cobranza.subtotalDeclaradoView"/></b>
						        </td>
						        <td class="celdatotales">
						        	<b><bean:write name="cobranzaAdapterVO" property="cobranza.subtotalPagosView"/></b>
						        </td>
						        <td class="celdatotales">
						        	<b><bean:write name="cobranzaAdapterVO" property="cobranza.subtotalConVigView"/></b>
						        </td>
						        <td class="celdatotales">
						        	<b><bean:write name="cobranzaAdapterVO" property="cobranza.subtotalConCadView"/></b>
						        </td>
						        <td class="celdatotales">
						        	<b><bean:write name="cobranzaAdapterVO" property="cobranza.subtotalDiferenciaView"/></b>
						        </td>
					        </tr>
				               
				            	
			            </tbody>
			    	</table>
		    	</div>   
		    	
		    </logic:notEmpty>
		    <logic:empty name="cobranzaAdapterVO" property="cobranza.listCobranzaDet">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="gde" key="gde.cobranzaAdapter.detalle.label"/></caption>
                	<tbody>
						<tr><td align="center">
							<bean:message bundle="base" key="base.resultadoVacio"/>
						</td></tr>
					</tbody>			
				</table>
			</logic:empty>
		</fieldset>
		
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.cobranza.agregarResolucion.legend"/></legend>
				
				<table class="tabladatos">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.multaEditAdapter.resolucion.label"/>: </label></td>
						<!-- Inclucion de CasoView -->
						<td>
							<bean:define id="IncludedVO" name="cobranzaAdapterVO" property="cobranza"/>
							<bean:define id="voName" value="cobranza" />
							<%@ include file="/cas/caso/includeCasoSubmitQuitar.jsp" %>				
						</td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.multaEditAdapter.fechaResolucion.label"/>: </label></td>
						<td class="normal">
							<html:text name="cobranzaAdapterVO" property="cobranza.fechaResolucionView" styleId="fechaResolucionView" size="15" maxlength="10" styleClass="datos" onchange="changeFecha();" />
								<a class="link_siat" onclick="return show_calendar_change('fechaResolucionView');" id="a_fechaResolucionView">
									<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
								</a>
						</td>
					</tr>
				</table>
				<p align="center">
			    	<html:button property="btnEmitir" styleClass="boton" onclick="submitForm('emitirAjustes','');">
			    		<bean:message bundle="gde" key="gde.cobranzaAdapter.emitir.button"/>
			    	</html:button>
			    </p>
			</fieldset>
		
		
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.cobranzaAdapter.gesCob.legend"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="gde" key="gde.cobranzaAdapter.gesCob.fecha.label"/>: </label></td>
					<td class="normal"><html:text name="cobranzaAdapterVO" property="gesCob.fechaView" size="12" styleId="fechaView"/>
						<a class="link_siat" onclick="return show_calendar('fechaView');" id="a_fechaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
						</a>
					</td>
					
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.cobranzaAdapter.gesCob.observacion.label"/>: </label></td>
					<td class="normal">
						<html:textarea name="cobranzaAdapterVO" property="gesCob.observacion"></html:textarea>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.cobranzaSearchPage.estadoCobranza.label"/>: </label></td>
					<td class="normal">
						<html:select name="cobranzaAdapterVO" property="gesCob.estadoCobranza.id">
							<html:optionsCollection name="cobranzaAdapterVO" property="listEstadoCobranza" label="desEstado" value="id"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.cobranza.proFecCon.label"/>: </label></td>
					<td class="normal">
						<html:text name="cobranzaAdapterVO" property="gesCob.fechaControlView" styleId="fechaControlView"size="12"/>
						<a class="link_siat" onclick="return show_calendar('fechaControlView');" id="a_fechaControlView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
						</a>
					</td>
				</tr>
			</table>
			<p align="right">
				<html:button property="btnAddGesCob" styleClass="boton" onclick="submitForm('agregarGesCob','');">
					<bean:message bundle="base" key="abm.button.agregar"/>
				</html:button>
			</p>
		</fieldset>
		
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.cobranzaAdapter.histGesCob.legend"/></legend>
			<logic:notEmpty name="cobranzaAdapterVO" property="cobranza.listGesCob">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<tbody>
						<caption><bean:message bundle="gde" key="gde.cobranzaAdapter.listGesCob.label"/></caption>
						<tr>
							<th><bean:message bundle="gde" key="gde.cobranzaAdapter.gesCob.fecha.label"/></th>
							<th><bean:message bundle="gde" key="gde.cobranzaAdapter.gesCob.observacion.label"/></th>
							<th><bean:message bundle="gde" key="gde.cobranzaAdapter.listGesCob.estadoCobranza"/></th>
							<th><bean:message bundle="gde" key="gde.cobranza.proFecCon.label"/></th>
						</tr>
						<logic:iterate name="cobranzaAdapterVO" property="cobranza.listGesCob" id="GesCobVO">
							<tr>
								<td><bean:write name="GesCobVO" property="fechaView"/></td>
								<td><bean:write name="GesCobVO" property="observacion"/></td>
								<td><bean:write name="GesCobVO" property="estadoCobranza.desEstado"/></td>
								<td><bean:write name="GesCobVO" property="fechaControlView"/></td>
							</tr>
						</logic:iterate>
					</tbody>
				</table>
			</logic:notEmpty>
			<logic:empty name="cobranzaAdapterVO" property="cobranza.listCobranzaDet">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="gde" key="gde.cobranzaAdapter.listGesCob.label"/></caption>
                	<tbody>
						<tr><td align="center">
							<bean:message bundle="base" key="base.resultadoVacio"/>
						</td></tr>
					</tbody>
				</table>
			</logic:empty>
		</fieldset>
		
		<table class="tablabotones">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
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
<!-- Fin formulario -->