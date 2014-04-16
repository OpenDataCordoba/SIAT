	<!-- Deuda En Gestion Administrativa -->	
	<logic:notEmpty name="liqDeudaAdapterVO" property="listGestionDeudaAdmin">
		<div class="horizscroll">
	    <table class="tableDeuda" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.title"/></caption>
	      	<tbody>
	      	<!-- Item LiqDeudaAdminVO (unico)-->
	       	<logic:iterate id="GDeudaAdmin" name="liqDeudaAdapterVO" property="listGestionDeudaAdmin">
		       	<tr>
		       		<logic:equal name="liqDeudaAdapterVO" property="mostrarColumnaSeleccionar" value="true">
			       		<th align="center" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.seleccionar.label"/>'>
			       			<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.seleccionar"/>
			       			
			       			<logic:equal name="liqDeudaAdapterVO" property="mostrarChkAllAdmin" value="true">
				       			<br>
				       			<input type="checkbox" onclick="changeChk('filter', 'listIdDeudaSelected', this)"/>
				       		</logic:equal>
			       		</th>
	  				</logic:equal>
		       		<logic:equal name="liqDeudaAdapterVO" property="mostrarColumnaVer" value="true">
			       		<th align="left">&nbsp;</th>
	  				</logic:equal>
				  	
				  	<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.codRefPag.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.codRefPag"/></th>
				  	<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.periodoDeuda.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.periodoDeuda"/></th>
				  	<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.fechaVto.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.fechaVto"/></th>
				  	
				  	<!-- Para recursos autoliquidables -->
				  	<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				  		<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.declarado.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.declarado"/></th>
				  		<th class="separadorAuto">&nbsp;</th>
				  		<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.fechaPago.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.fechaPago"/></th>
				  		<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.importePago.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.importePago"/></th>
				  		<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.saldo.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.saldo"/></th>
				  		<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion"/></th>
				  		<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total"/></th>
				  	</logic:equal>
				  	
				  	<!-- Para recursos NO autoliquidables -->
				  	<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				  		<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.importe.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.importe"/></th>
				  		<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion"/></th>
				  		<th align="left" title='<bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total.label"/>'><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total"/></th>
				  	</logic:notEqual>

				</tr>
				
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqDeudaAdmin" name="GDeudaAdmin" property="listDeuda">
					<tr>
			       		<logic:equal name="liqDeudaAdapterVO" property="mostrarColumnaSeleccionar" value="true">
							<!-- Seleccion de deuda para su operacion -->
				  			<td align="center">
				  				<logic:equal name="LiqDeudaAdmin" property="esSeleccionable" value="true">
			  						<html:multibox name="GDeudaAdmin" property="listIdDeudaSelected" title="Seleccionar">
                    	            	<bean:write name="LiqDeudaAdmin" property="idSelect"/>
                        	        </html:multibox>
			  					</logic:equal>
				  				<logic:notEqual name="LiqDeudaAdmin" property="esSeleccionable" value="true">
				  					<input type="checkbox" disabled="disabled"/>
				  				</logic:notEqual>
				  			</td>
		   				</logic:equal>
			       		<logic:equal name="liqDeudaAdapterVO" property="mostrarColumnaVer" value="true">
				  			<!-- Ver Detalle Deuda -->
				  			<td>
				  				<logic:equal name="LiqDeudaAdmin" property="verDetalleDeudaEnabled" value="true">
							        <a style="cursor: pointer;" 
				  						href="<bean:write name="LiqDeudaAdmin" property="linkVerDetalle"/>">
							        	<img title="Ver" src="<%=request.getContextPath()%>/images/iconos/selec0.gif" border="0"/>
						    	    </a>
				  				</logic:equal>
				  				<logic:notEqual name="LiqDeudaAdmin" property="verDetalleDeudaEnabled" value="true">
				  					<img title="Ver" src="<%=request.getContextPath()%>/images/iconos/selec1.gif" border="0"/>
				  				</logic:notEqual>
				  				
			  				</td>
		   				</logic:equal>
			  			
			  			<td><bean:write name="LiqDeudaAdmin" property="codRefPagView"/>&nbsp;</td>
			  			<td><bean:write name="LiqDeudaAdmin" property="periodoDeuda"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaAdmin" property="fechaVto"/>&nbsp;</td>
				       	
					    <!-- Si NO posee Observacion -->				       	
				        <logic:notEqual name="LiqDeudaAdmin" property="poseeObservacion" value="true">
				        	
				        	<!-- Para recursos autoliquidables -->
				        	<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
					        	<td><bean:write name="LiqDeudaAdmin" property="importeCurrencyView" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
								<td class="separadorAuto">&nbsp;</td>
					        	<td><bean:write name="LiqDeudaAdmin" property="fechaPago"/>&nbsp;</td>
								<td><bean:write name="LiqDeudaAdmin" property="importePago" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
								<td><bean:write name="LiqDeudaAdmin" property="saldoCurrencyView"/>&nbsp;</td>
						        <td><bean:write name="LiqDeudaAdmin" property="actualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        <td><bean:write name="LiqDeudaAdmin" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        </logic:equal>

							<!-- Para recursos NO autoliquidables -->
							<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">	
						        <td><bean:write name="LiqDeudaAdmin" property="saldoCurrencyView" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        <td><bean:write name="LiqDeudaAdmin" property="actualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        <td><bean:write name="LiqDeudaAdmin" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				       		</logic:notEqual>
				       		
					    </logic:notEqual>
				       	
				       	<!-- Si posee Convenio -->
				       	<logic:equal name="LiqDeudaAdmin" property="poseeConvenio" value="true">
				       		<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				       			<td colspan="8">
				       		</logic:equal>
				       		<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				       			<td colspan="4">
				       		</logic:notEqual>
				       			<logic:equal name="liqDeudaAdapterVO" property="verConvenioEnabled" value="true">
					       			<a href="/siat/gde/AdministrarLiqDeuda.do?method=verConvenio&selectedId=<bean:write name="LiqDeudaAdmin" property="idLink" bundle="base" formatKey="general.format.id"/>&cuentaId=<bean:write name="liqDeudaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>" >
					       				<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.nroConvenio.label"/>:
					       				<bean:write name="LiqDeudaAdmin" property="observacion"/>
					       			</a>
					       		</logic:equal>
					       		<logic:notEqual name="liqDeudaAdapterVO" property="verConvenioEnabled" value="true">
					       			<b><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.nroConvenio.label"/>:
					       			<bean:write name="LiqDeudaAdmin" property="observacion"/></b>
					       		</logic:notEqual>
				       		</td>
				       	</logic:equal>
				       	
					    <!-- Es Indeterminada -->
		       			<logic:equal name="LiqDeudaAdmin" property="esIndeterminada" value="true">
		       				
		       				<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				       			<td colspan="7">
				       		</logic:equal>
				       		<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				       			<td colspan="3">
				       		</logic:notEqual>
		       					<b><bean:write name="LiqDeudaAdmin" property="observacion"/>&nbsp;</b>
							</td>
							
		       			</logic:equal>

					    <!-- Es Pendiente Pago Osiris -->
		       			<logic:equal name="LiqDeudaAdmin" property="esOsirisPagoPendiente" value="true">
		       				
		       				<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				       			<td colspan="7">
				       		</logic:equal>
				       		<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				       			<td colspan="3">
				       		</logic:notEqual>
		       					<b><bean:write name="LiqDeudaAdmin" property="observacion"/>&nbsp;</b>
							</td>
							
		       			</logic:equal>
					    
					    <!-- Es Reclamada -->
				       	<logic:equal name="LiqDeudaAdmin" property="esReclamada" value="true">
		       				<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				       			<td colspan="8">
				       		</logic:equal>
				       		<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				       			<td colspan="4">
				       		</logic:notEqual>
		       					<b><bean:write name="LiqDeudaAdmin" property="observacion"/>&nbsp;</b>
							</td>
				    	</logic:equal>
				    	
					    <!-- Es Exento Pago -->
				       	<logic:equal name="LiqDeudaAdmin" property="esExentoPago" value="true">
		       				<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				       			<td colspan="8">
				       		</logic:equal>
				       		<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				       			<td colspan="4">
				       		</logic:notEqual>
		       					<b><bean:write name="LiqDeudaAdmin" property="observacion"/>&nbsp;</b>
							</td>
				    	</logic:equal>
					    
			       	</tr>
				</logic:iterate>
				<!-- Fin Item LiqDeudaVO -->
				
				<tr>
					<td colspan="5" class="celdatotales"> &nbsp;</td>
		       		
			        <!-- Para recursos autoliquidables -->
			        <logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
			       		<td class="celdatotales" align="left">
				        	<b><bean:write name="GDeudaAdmin" property="subTotalImporte" bundle="base" formatKey="general.format.currency"/></b>
				        </td>
				        <td class="separadorAuto">&nbsp;</td>
				        <td class="celdatotales">&nbsp;</td>
				        <td class="celdatotales" align="left">
					      	<b><bean:write name="GDeudaAdmin" property="subTotalImportePago" bundle="base" formatKey="general.format.currency"/></b>
					    </td>
			        </logic:equal>
				    
				    <td class="celdatotales" align="left">
				      	<b><bean:write name="GDeudaAdmin" property="subTotalSaldo" bundle="base" formatKey="general.format.currency"/></b>
				    </td>
			        
			        <td class="celdatotales" align="left">
			        	<b><bean:write name="GDeudaAdmin" property="subTotalActualizacion" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       		<td class="celdatotales" align="left">
			        	<b><bean:write name="GDeudaAdmin" property="subTotal" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       	</tr>
		       	<tr>
		       		<!-- Para recursos autoliquidables -->
		       		<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
		       			<td colspan="12" class="celdatotales" align="right">
		       		</logic:equal>
		       		<!-- Para recursos NO autoliquidables -->
		       		<logic:notEqual name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
		       			<td colspan="8" class="celdatotales" align="right">
		       		</logic:notEqual>
		       		
			        	<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.subtotal.label"/>: 
			        	<b><bean:write name="GDeudaAdmin" property="subTotal" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       	</tr>	       	
	       	</logic:iterate>
	       	<!-- Fin GDeudaAdmin -->
	      	</tbody>
		</table>
		</div>
	</logic:notEmpty>
	<!-- Fin Deuda En Gestion Administrativa -->