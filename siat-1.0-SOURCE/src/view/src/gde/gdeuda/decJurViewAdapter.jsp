<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarDecJur.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
 	<h1><bean:message bundle="gde" key="gde.decJurAdapter.title"/></h1>
	<p align="right">
		<button type="button" name="btn2" class="boton" onclick="submitForm('volver','');"><bean:message bundle="base" key="abm.button.volver"/></button>
	</p>

	<!-- DecJur -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.decJurAdapter.legend"/></legend>
		
		<table class="tabladatos">
			<tr>
				<td>
					<label><bean:message bundle="gde" key="gde.decJurSearchPage.cuenta.label"/>: </label>
				</td>
				<td class="normal">
					<bean:write name="decJurAdapterVO" property="decJur.cuenta.numeroCuenta"/>
				</td>
			</tr>
			<tr>
				<td>
					<label><bean:message bundle="def" key="def.recurso.label"/>: </label>
				</td>
				<td class="normal">
					<bean:write name="decJurAdapterVO" property="decJur.recurso.desRecurso"/>
				</td>
			</tr>
			<tr>
				<td>
					<label><bean:message bundle="gde" key="gde.decJur.fechaPresentacion.label"/>: </label>
				</td>
				<td class="normal">
					<bean:write name="decJurAdapterVO" property="decJur.fechaPresentacionView"/>
				</td>
				<td>
					<label><bean:message bundle="base" key="base.usuarioUltMdf.label"/>: </label>
				</td>
				<td class="normal">
					<bean:write name="decJurAdapterVO" property="decJur.usuario"/>
				</td>
				
			</tr>
			<tr>
				<td>
					<label><bean:message bundle="gde" key="gde.decJur.periodo.label"/>: </label>
				</td>
				<td class="normal">
					<bean:write name="decJurAdapterVO" property="decJur.desPeriodo"/>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.decJurAdapter.tipoDJ"/>: </label></td>
				<td class="normal">
					<bean:write name="decJurAdapterVO" property="decJur.tipDecJurRec.tipDecJur.desTipo"/>
				</td>
			</tr>
			<logic:equal name="decJurAdapterVO" property="decJur.esDrei" value="true">
				<tr>
					<td><label><bean:message bundle="gde" key="gde.decJurAdapter.canPer.label"/>: </label></td>
					<td class="normal"><bean:write name="decJurAdapterVO" property="decJur.valRefMinIntView"/></td>
				</tr>
			</logic:equal>
			<logic:equal name="decJurAdapterVO" property="decJur.esEtur" value="true">
				<tr>
					<td><label><bean:message bundle="gde" key="gde.decJurAdapter.radio.label"/>: </label></td>
					<td class="normal"><bean:write name="decJurAdapterVO" property="decJur.valRefMinIntView"/></td>			
				</tr>
			</logic:equal>
			<logic:notEmpty name="decJurAdapterVO" property="decJur.minRec">
				<tr>
					<td><label><bean:message bundle="gde" key="gde.decJurAdapter.minimo.label"/>: </label></td>
					<td class="normal">
						<p class="msgBold"><bean:write name="decJurAdapterVO" property="decJur.minRecView"/></p>
					</td>
				</tr>
			</logic:notEmpty>
		</table>	
			
	</fieldset>
	
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.decJurAdapter.detalle.legend"/></legend>
		<logic:notEmpty  name="decJurAdapterVO" property="decJur.listDecJurDet">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<caption><bean:message bundle="gde" key="gde.decJurAdapter.actividades.label"/></caption>
	           	<tbody>
	           		<tr>
		           		<th><bean:message bundle="gde" key="gde.decJurAdapter.actividad.label"/></th>
		           		<th><bean:message bundle="gde" key="gde.decJurAdapter.baseAplicada.label"/></th>
		           		<th><bean:message bundle="gde" key="gde.decJurAdapter.multiploAplicado.label"/></th>
		           		<th><bean:message bundle="gde" key="gde.decJurAdapter.subtotal.label"/></th>
		           	</tr>           
			        <logic:iterate name="decJurAdapterVO" property="decJur.listDecJurDet" id="DecJurDetVO">
			        	<tr>
			        		<td>
			        			<bean:write name="DecJurDetVO" property="recConADec.codYDescripcion"/>
			        		</td>
			        		<td>
			        			<bean:write name="DecJurDetVO" property="baseAplicadaView"/>
			        		</td>
			        		<td>
			        			<bean:write name="DecJurDetVO" property="multiploAplicadoView" filter="false"/>
			        		</td>
			        		<td>
			        			<bean:write name="DecJurDetVO" property="totalConceptoView"/>
			        		</td>
			        	</tr>
			        </logic:iterate>
			        <tr>
			        	<td colspan="5" class="celdatotales" align="right">
				        	<bean:message bundle="gde" key="gde.decJurAdapter.totalAct.label"/>: 
				        	<b><bean:write name="decJurAdapterVO" property="decJur.subtotalDeclaradoView"/></b>
				        </td>
			        </tr>
		               	
	            </tbody>
	    	</table>
	    </logic:notEmpty>
	    <logic:empty name="decJurAdapterVO" property="decJur.listDecJurDet">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<caption><bean:message bundle="gde" key="gde.decJurAdapter.actividades.label"/></caption>
               	<tbody>
					<tr><td align="center">
						<bean:message bundle="base" key="base.resultadoVacio"/>
					</td></tr>
				</tbody>			
			</table>
		</logic:empty>
		
	</fieldset>
	
	<logic:equal name="decJurAdapterVO" property="decJur.declaraAdicionales" value="true">
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.decJurAdapter.adicionales.legend"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="gde" key="gde.decJurAdapter.subtotalDeterminado.label"/>: </label></td>
					<td class="normal"><p class="msgBold"><bean:write name="decJurAdapterVO" property="decJur.subtotalView"/></p></td>
				</tr>
				<logic:equal name="decJurAdapterVO" property="decJur.esDrei" value="true">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.decJurAdapter.aliPub.label"/>: </label></td>
						
						<td id="aliPubView" style="display:table-cell" class="normal">
							<bean:write name="decJurAdapterVO" property="decJur.aliPubView" filter="false"/>
						</td>
						<td><label><bean:message bundle="gde" key="gde.decJurAdapter.totalPublicidad.label"/>: </label></td>
						<td class="normal">
							<bean:write name="decJurAdapterVO" property="decJur.totalPublicidadView"/>
						</td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.decJurAdapter.aliMesYSil"/>: </label></td>
						
						<td class="normal" id="aliMesView" style="table-cell">
							<bean:write name="decJurAdapterVO" property="decJur.aliMesYSilView" filter="false"/>
						</td>
						<td><label><bean:message bundle="gde" key="gde.decJurAdapter.totalMesYSil.label"/>: </label></td>
						<td class="normal">
							<bean:write name="decJurAdapterVO" property="decJur.totMesYSilView" />
						</td>
					</tr>
				</logic:equal>
				<tr>
					<td>&nbsp;</td><td>&nbsp;</td>
					<td><label><bean:message bundle="gde" key="gde.decJurAdapter.totalDeclarado.label"/>: </label></td>
					<td class="normal"><bean:write name="decJurAdapterVO" property="decJur.totalDeclaradoView"/></td>
				</tr>
			</table>
				
		</fieldset>
		
		<logic:equal name="decJurAdapterVO" property="decJur.mostrarTotales" value="true">
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.decJurAdapter.otrosPagos.legend"/></legend>
				<logic:notEmpty  name="decJurAdapterVO" property="decJur.listDecJurPag">
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
						<caption><bean:message bundle="gde" key="gde.decJurAdapter.detallePagos.label"/></caption>
			           	<tbody>
			           		<tr>
				           		<th><bean:message bundle="gde" key="gde.decJurAdapter.tipoPago.label"/></th>
				           		<th><bean:message bundle="gde" key="gde.decJurAdapter.tipoPago.importe.label"/></th>
				           	</tr>           
					        <logic:iterate name="decJurAdapterVO" property="decJur.listDecJurPag" id="DecJurPagVO">
					        	<tr>
					        		
					        		<td>
					        			<bean:write name="DecJurPagVO" property="tipPagDecJur.desTipPag"/>
					        		</td>
					        		<td>
					        			<bean:write name="DecJurPagVO" property="importeView"/>
					        		</td>
					        	</tr>
					        </logic:iterate>
					        
					        <tr>
					        	<td colspan="5" class="celdatotales" align="right">
						        	<bean:message bundle="gde" key="gde.decJurAdapter.totalAct.label"/>: 
						        	<b><bean:write name="decJurAdapterVO" property="decJur.otrosPagosView"/></b>
						        </td>
					        </tr>
				               	
			            </tbody>
			    	</table>
			    </logic:notEmpty>
			    <logic:empty name="decJurAdapterVO" property="decJur.listDecJurPag">
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
						<caption><bean:message bundle="gde" key="gde.decJurAdapter.detallePagos.label"/></caption>
	                	<tbody>
							<tr><td align="center">
								<bean:message bundle="base" key="base.resultadoVacio"/>
							</td></tr>
						</tbody>			
					</table>
				</logic:empty>
				
			</fieldset>
			
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.decJurAdapter.totales.legend"/></legend>
				<table class="tabladatos">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.decJurAdapter.totDerDet.label"/>: </label></td>
						<td class="normal">
							<p class="msgBold"><bean:write name="decJurAdapterVO" property="decJur.totalDeclaradoView"/></p>
						</td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.decJurAdapter.otrosPagos.label"/>: </label></td>
						<td class="normal">
							<p class="msgBold"><bean:write name="decJurAdapterVO" property="decJur.otrosPagosView"/></p>
						</td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.decJurAdapter.totalDJ.label"/>: </label></td>
						<td class="normal">
							<p class="msgBold"><bean:write name="decJurAdapterVO" property="decJur.totalDJView"/></p>
						</td>
					</tr>
				</table>
			</fieldset>
			
		</logic:equal>
	</logic:equal>
		
	<logic:equal name="decJurAdapterVO" property="decJur.mostrarDatosAfip" value="true">
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.decJur.observaciones.label"/></legend>
				<!-- Observaciones -->
				<p align="left">
	                 <html:textarea style="width:310px; height:270px; font-family: monospace; font-size: 8pt; color:grey;" name="decJurAdapterVO" property="decJur.observaciones" readonly="true"></html:textarea>
	            </p>
			</fieldset>
			
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.decJurAdapter.datosAfip.legend"/></legend>
				<p>
					<a href="/siat/gde/AdministrarDecJur.do?method=verForDecJur&idDecJur=<bean:write name="decJurAdapterVO" property="decJur.id" bundle="base" formatKey="general.format.id"/>">
						<bean:message bundle="gde" key="gde.decJurAdapter.forDecJur.link"/>
					</a>
				</p>
			</fieldset>
	</logic:equal>
				
	<table class="tablabotones" width="100%">
    	<tr>
 	    		<td align="left" width="50%">
	   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>	   	    			
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="decJurAdapterVO" property="act" value="eliminar">
					<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
						<bean:message bundle="base" key="abm.button.eliminar"/>
					</html:button>
				</logic:equal>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="decJurAdapterVO" property="act" value="vueltaAtras">
					<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('vueltaAtras', '');">
						<bean:message bundle="gde" key="gde.decJurSearchPage.abm.button.vueltaAtras"/>
					</html:button>
				</logic:equal>
   	    	</td>
   	    </tr>
   	 </table>
	   	 
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="editar"  value="<bean:write name="decJurAdapterVO" property="decJur.editaAdicionales"/>" id="editar"/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
		<input type="hidden" name="idDecJur" value="<bean:write name="decJurAdapterVO" property="decJur.id" bundle="base" formatKey="general.format.id"/>"/>
		<input type="hidden" name="idDeuda" value="<bean:write name="decJurAdapterVO" property="decJur.idDeuda" bundle="base" formatKey="general.format.id"/>"/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		<input type="hidden" name="subtotal" value="<bean:write name="decJurAdapterVO" property="decJur.subtotalView"/>" id="subtot"/>
		
</html:form>
<!-- Fin formulario -->