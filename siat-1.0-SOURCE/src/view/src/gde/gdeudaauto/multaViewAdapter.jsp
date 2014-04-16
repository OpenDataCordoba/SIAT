<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarMulta.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.multaViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>	
			</tr>
		</table>
		
		<!-- Multa -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.multa.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="gde" key="gde.tipoMulta.label"/>: </label></td>
					<td class="normal"><bean:write name="multaAdapterVO" property="multa.tipoMulta.desTipoMulta" />	</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.multa.importe.label"/>: </label></td>
					<td class="normal"><bean:write name="multaAdapterVO" property="multa.importeView" />	</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.deuda.fechaVencimiento.label"/>: </label></td>
					<td class="normal"><bean:write name="multaAdapterVO" property="multa.fechaVencimientoView" />	</td>
				</tr>
								
			    <!-- Recurso -->
			    <tr>
		    		<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal">	
						<bean:write name="multaAdapterVO" property="multa.cuenta.recurso.desRecurso" />
					</td>
			    </tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="multaAdapterVO" property="multa.cuenta.numeroCuenta" />	</td>
				</tr>
				<logic:equal name="multaAdapterVO" property="multa.tipoMulta.esImporteManual.id" value="0">
			         	<td><label><bean:message bundle="gde" key="gde.descuentoMulta.label"/>: </label></td>
						<td class="normal">	
							<bean:write name="multaAdapterVO" property="multa.descuentoMulta.descripcion" />
						</td>
		         </logic:equal>
				<!-- CuentaTitular -->
				<logic:equal name="multaAdapterVO" property="act" value="ver">
			        <tr>
						<td><label><bean:message bundle="pad" key="pad.cuenta.listCuentaTitular.label"/>: </label></td>
					</tr>
					<logic:notEmpty  name="multaAdapterVO" property="multa.cuenta.listCuentaTitular">
		              <logic:iterate id="CuentaTitularVO" name="multaAdapterVO" property="multa.cuenta.listCuentaTitular">
		                 <tr>
		                 	<td>&nbsp; </td>
		                    <td class="normal"><bean:write name="CuentaTitularVO" property="contribuyente.viewWithCuit" />&nbsp;</td>
		                 </tr>
		         	  </logic:iterate>
		         	
		         	
		         	
		           </logic:notEmpty>
		           <logic:empty name="multaAdapterVO" property="multa.cuenta.listCuentaTitular">
						<tr><td align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>
		    	</logic:equal>
				<!--CuentaTitular -->				
			</table>
		</fieldset>
		<!-- Multa -->
		
		<logic:equal name="multaAdapterVO" property="multa.tipoMulta.esImporteManual.id" value="0">	
		<!-- Multa Historico -->
		<logic:notEmpty name="multaAdapterVO" property="multa.listMultaHistorico">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="gde" key="gde.multaHistorico.title"/></caption>
		    	<tbody>
					<tr>
						<th align="left"><bean:message bundle="gde" key="gde.multaHistorico.fecha.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.multaHistorico.caso.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.multaHistorico.importe.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.multaHistorico.porcentaje.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.multaHistorico.periodoDesde"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.multaHistorico.periodoHasta"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.multaHistorico.observacion.label"/></th>
					</tr>	    	
					<logic:iterate id="MultaHistoricoVO" name="multaAdapterVO" property="multa.listMultaHistorico" indexId="count">
						<tr>
							<td><bean:write name="MultaHistoricoVO" property="fechaView"/>&nbsp;</td>
							<td><bean:write name="MultaHistoricoVO" property="caso.casoView"/>&nbsp;</td>
							<td><bean:write name="MultaHistoricoVO" property="importeTotalView"/>&nbsp;</td>
							<td><bean:write name="MultaHistoricoVO" property="porcentajeView" />&nbsp;</td>
							<td><bean:write name="MultaHistoricoVO" property="periodoAnioDesdeView"/>&nbsp;</td>
							<td><bean:write name="MultaHistoricoVO" property="periodoAnioHastaView"/>&nbsp;</td>
							<td><bean:write name="MultaHistoricoVO" property="observacion"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</tbody>
			</table>
		</logic:notEmpty>
		<!-- Multa Historico -->
		
		<!-- Resultado MultaDet -->	
		<logic:equal name="multaAdapterVO" property="esTipoRevision" value="0">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="gde" key="gde.multaDet.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="multaAdapterVO" property="multa.listMultaDet">
						<tr>
							<th align="left"><bean:message bundle="ef" key="ef.periodoOrden.periodo.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.periodoOrden.anio.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.importeBase.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.importeAct.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.porOri.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.porDes.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.importeAplicado.label"/></th>
						</tr>	    	
						<logic:iterate id="MultaDetVO" name="multaAdapterVO" property="multa.listMultaDet" indexId="count">
							<tr>
								<td><bean:write name="MultaDetVO" property="periodoView"/>&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="anioView" />&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="importeBaseView"/>&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="importeActView"/>&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="porOriView"/>&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="porDesView"/>&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="importeAplicadoView"/>&nbsp;</td>
							</tr>
						</logic:iterate>
						<td colspan="6" class="celdatotales"> &nbsp;</td>       		
			       		<td class="celdatotales" align="left">
			       			<bean:message bundle="gde" key="gde.multaEditAdpater.total.label"/>:
				        	<b><bean:write name="multaAdapterVO" property="multa.importeView" bundle="base" formatKey="general.format.currency"/></b>
				        </td>
					</logic:notEmpty>
					<logic:empty  name="multaAdapterVO" property="multa.listMultaDet">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>					
				</tbody>
			</table>
			</logic:equal>
			<!-- Multa por revision -->
			<logic:equal name="multaAdapterVO" property="esTipoRevision" value="1">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="gde" key="gde.multaDet.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="multaAdapterVO" property="multa.listMultaDet">
						<tr>
							<th align="left"><bean:message bundle="ef" key="ef.periodoOrden.periodo.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.periodoOrden.anio.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.importeBase.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.pagoContadoOBueno.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.resto.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.pagoActualizado.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.restoActualizado.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.aplicado.label"/></th>
						</tr>	    	
						<logic:iterate id="MultaDetVO" name="multaAdapterVO" property="multa.listMultaDet" indexId="count">
							<tr>
								<td><bean:write name="MultaDetVO" property="periodoView"/>&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="anioView" />&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="importeBaseView"/>&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="pagoContadoOBuenoView"/>&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="restoView"/>&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="pagoActualizadoView"/></td>
								<td><bean:write name="MultaDetVO" property="restoActualizadoView"/>&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="aplicadoView"/>&nbsp;</td>
							</tr>
						</logic:iterate>
						
					</logic:notEmpty>
					
					<logic:empty  name="multaAdapterVO" property="multa.listMultaDet">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>					
				</tbody>
			</table>
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.multaDet.importes.label"/></legend>
				<table class="tabladatos">
					<tr>						     	
			       		<td class="normal">
			       			<label><bean:message bundle="gde" key="gde.multaEditAdpater.totalAplicado.label"/>:</label>
				        	<b><bean:write name="multaAdapterVO" property="multa.totalAplicadoView" bundle="base" formatKey="general.format.currency"/></b>
				        </td>
				    </tr>
				    <tr>						      	
			       		<td class="normal">			       			
				        	<label><bean:message bundle="gde" key="gde.multaEditAdpater.importeMultaAnterior.label"/>:</label>
				        	<b><bean:write name="multaAdapterVO" property="multa.importeMultaAnteriorView" bundle="base" formatKey="general.format.currency"/></b>
				        
				        </td>
				    </tr>
				    <tr>						  		
			       		<td class="normal">			       							      
				        	<label><bean:message bundle="gde" key="gde.multaEditAdpater.totalEmitir.label"/>:</label>
				        	<b><bean:write name="multaAdapterVO" property="multa.importeView" bundle="base" formatKey="general.format.currency"/></b>
				        </td>
				    </tr>
				</table>
			</fieldset>
		</logic:equal>
		<!-- Fin Multa por revision -->		
		</logic:equal>
		
		<!-- Fin MultaDet -->
		
		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	   <logic:equal name="multaAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitForm('imprimir', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
					<logic:equal name="multaAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="multaAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="multaAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	<input type="hidden" name="name"  value="<bean:write name='multaAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->