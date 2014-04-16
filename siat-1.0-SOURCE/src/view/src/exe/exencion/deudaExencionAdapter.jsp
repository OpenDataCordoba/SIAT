<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/exe/AdministrarDeudaExencion.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="exe" key="exe.deudaExencionAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volverAIngreso', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>

	<!-- LiqCuenta -->
		<bean:define id="DeudaAdapterVO" name="liqDeudaAdapterVO"/>
		<%@ include file="/gde/gdeuda/includeLiqCuenta.jsp" %>
	<!-- LiqCuenta -->
	
	<!-- Exencion - Caso Solical - Otro -->
		<!--  Utilizamos en bean definido para la inclucion de liqCuenta -->
		<%@ include file="/gde/gdeuda/includeExenciones.jsp" %>
	<!-- Fin Exencion - Caso Solical - Otro -->

	<!-- Exencion Seleccionada -->
	
	<!-- Fin Exencion Seleccionada -->	
	
	<!-- Deuda en Jestion Judicial -->
	<logic:notEmpty name="liqDeudaAdapterVO" property="listProcurador">
		<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockJudicial.title"/></caption>
	      	<tbody>
	      	<!-- LiqProcuradorVO -->
	       	<logic:iterate id="Procurador" name="liqDeudaAdapterVO" property="listProcurador">
		       	<tr>
		       		<td colspan="7">
		       			<b><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockJudicial.procurador.label"/>:</b> 
		       			<bean:write name="Procurador" property="desProcurador"/>
		       		</td>		       		
		       	</tr>
		       	<tr>
		       		<th align="center">
		       			<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.seleccionar"/>
		       		</th>	
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.periodoDeuda"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.fechaVto"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.saldo"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total"/></th>
				  	
				</tr>
				
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqDeudaJud" name="Procurador" property="listDeuda">
					<tr>						
				        <td align="center">
							<logic:equal name="LiqDeudaJud" property="esSeleccionable" value="true">
                                <input type="checkbox" 
                                		name="listIdDeudaSelected" 
                                		id="procurador<bean:write name="Procurador" property="idProcurador" bundle="base" formatKey="general.format.id"/>"
                                		value="<bean:write name="LiqDeudaJud" property="idSelect"/>"/>
                                
			  				</logic:equal>
			  				<logic:notEqual name="LiqDeudaJud" property="esSeleccionable" value="true">
			  					<input type="checkbox" disabled="disabled"/>
			  				</logic:notEqual>				        
						</td>
			  			<td><bean:write name="LiqDeudaJud" property="periodoDeuda"/> </td>
				        <td><bean:write name="LiqDeudaJud" property="fechaVto"/></td>
				       	
				       	<!-- Si no posee Observacion -->				       	
				        <logic:notEqual name="LiqDeudaJud" property="poseeObservacion" value="true">
					        <td><bean:write name="LiqDeudaJud" property="saldo" bundle="base" formatKey="general.format.currency"/></td>
					        <td><bean:write name="LiqDeudaJud" property="actualizacion" bundle="base" formatKey="general.format.currency"/></td>
					        <td><bean:write name="LiqDeudaJud" property="total" bundle="base" formatKey="general.format.currency"/></td>
					    </logic:notEqual>
					    
				       	<!-- Si posee observacion, se utiliza para armar link a verConvenio -->
				       	<logic:equal name="LiqDeudaJud" property="poseeObservacion" value="true">
				       		<!-- Si posee Convenio -->
				       		<logic:equal name="LiqDeudaJud" property="poseeConvenio" value="true">
					       		<td colspan="4">
					       			<logic:equal name="liqDeudaAdapterVO" property="verConvenioEnabled" value="true">
						       			<a href="/siat/exe/AdministrarDeudaExencion.do?method=verConvenio&selectedId=<bean:write name="LiqDeudaJud" property="idLink" bundle="base" formatKey="general.format.id"/>" >
						       				<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockJudicial.nroConvenio.label"/>:
						       				<bean:write name="LiqDeudaJud" property="observacion"/>
						       			</a>
						       		</logic:equal>
						       		<logic:notEqual name="liqDeudaAdapterVO" property="verConvenioEnabled" value="true">
						       			<b><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockJudicial.nroConvenio.label"/>:
						       			<bean:write name="LiqDeudaJud" property="observacion"/></b>
						       		</logic:notEqual>
					       		</td>
					       	</logic:equal>
					       	
					  		<!-- Es Indeterminada -->
			       			<logic:equal name="LiqDeudaJud" property="esIndeterminada" value="true">
			       				<td colspan="4">
			       					<b><bean:write name="LiqDeudaJud" property="observacion"/></b>
								</td>
			       			</logic:equal>
			       			
			       			<!-- Es Rec lamada -->
					       	<logic:equal name="LiqDeudaJud" property="esReclamada" value="true">
			       				<td colspan="3">
			       					<b><bean:write name="LiqDeudaJud" property="observacion"/></b>
								</td>
			       			</logic:equal>
			       			
			       			<!-- Es Exento Pago -->
					       	<logic:equal name="LiqDeudaJud" property="esExentoPago" value="true">
			       				<td colspan="4">
			       					<b><bean:write name="LiqDeudaJud" property="observacion"/>&nbsp;</b>
								</td>
					    	</logic:equal>	
				       	</logic:equal>
			       	</tr>
				</logic:iterate>
				<!-- Fin Item LiqDeudaVO -->
		       	<tr>       		
		       		<td colspan="7" class="celdatotales" align="right">
			        	<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockJudicial.subtotal.label"/>: 
			        	<b><bean:write name="Procurador" property="subTotal" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       	</tr>	       	
	       	</logic:iterate>
	       	<!-- Fin LiqProcuradorVO -->
	      	</tbody>
		</table>
		</div>
	</logic:notEmpty>
	<!-- Fin Deuda en Jestion Judicial -->
	
	<!-- Deuda En Gestion Administrativa -->	
	<logic:notEmpty name="liqDeudaAdapterVO" property="listGestionDeudaAdmin">
		<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.title"/></caption>
	      	<tbody>
	      	<!-- Item LiqDeudaAdminVO (unico)-->
	       	<logic:iterate id="GDeudaAdmin" name="liqDeudaAdapterVO" property="listGestionDeudaAdmin">
		       	<tr>
		       		<th align="center">
		       			<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.seleccionar"/>		       			
		       		</th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.periodoDeuda"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.fechaVto"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.saldo"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total"/></th>
				</tr>
				
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqDeudaAdmin" name="GDeudaAdmin" property="listDeuda">
					<tr>
				        <!-- Seleccionar -->
				        <td align="center">
							<logic:equal name="LiqDeudaAdmin" property="esSeleccionable" value="true">
		  						<html:multibox name="GDeudaAdmin" property="listIdDeudaSelected" >
                   	            	<bean:write name="LiqDeudaAdmin" property="idSelect"/>
                       	        </html:multibox>
		  					</logic:equal>
			  				<logic:notEqual name="LiqDeudaAdmin" property="esSeleccionable" value="true">
			  					<input type="checkbox" disabled="disabled"/>
			  				</logic:notEqual>
				        </td>
			  			<td><bean:write name="LiqDeudaAdmin" property="periodoDeuda"/> </td>
				        <td><bean:write name="LiqDeudaAdmin" property="fechaVto"/></td>
				       	
				       	<!-- Si no posee Observacion -->
				        <logic:notEqual name="LiqDeudaAdmin" property="poseeObservacion" value="true">
					        <td><bean:write name="LiqDeudaAdmin" property="saldo" bundle="base" formatKey="general.format.currency"/></td>
					        <td><bean:write name="LiqDeudaAdmin" property="actualizacion" bundle="base" formatKey="general.format.currency"/></td>
					        <td><bean:write name="LiqDeudaAdmin" property="total" bundle="base" formatKey="general.format.currency"/></td>
					        
					    </logic:notEqual>
				       	
				       	<!-- Si posee Observacion -->
				       	<logic:equal name="LiqDeudaAdmin" property="poseeObservacion" value="true">
			       			<!-- Si posee Convenio -->
			       			<logic:equal name="LiqDeudaAdmin" property="poseeConvenio" value="true">
			       				<td colspan="4">
			       					<!-- Si tiene permiso para verConvenio -->
			       					<logic:equal name="liqDeudaAdapterVO" property="verConvenioEnabled" value="true">
				       					<a href="/siat/exe/AdministrarDeudaExencion.do?method=verConvenio&selectedId=<bean:write name="LiqDeudaAdmin" property="idLink" bundle="base" formatKey="general.format.id"/>" >
						       				<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.nroConvenio.label"/>:
						       				<bean:write name="LiqDeudaAdmin" property="observacion"/>
						       			</a>
						       		</logic:equal>
						       		<!-- Si NO tiene permiso para verConvenio -->
						       		<logic:notEqual name="liqDeudaAdapterVO" property="verConvenioEnabled" value="true">
						       			<b><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.nroConvenio.label"/>:
						       			<bean:write name="LiqDeudaAdmin" property="observacion"/></b>
						       		</logic:notEqual>	
			       				</td>
			       			</logic:equal>
			       			
			       			<!-- Es Indeterminada -->
			       			<logic:equal name="LiqDeudaAdmin" property="esIndeterminada" value="true">
			       				<td colspan="4">
			       					<b><bean:write name="LiqDeudaAdmin" property="observacion"/></b>
								</td>
			       			</logic:equal>
			       			
			       			<!-- Es Reclamada -->
					       	<logic:equal name="LiqDeudaAdmin" property="esReclamada" value="true">
			       				<td colspan="3">
			       					<b><bean:write name="LiqDeudaAdmin" property="observacion"/></b>
								</td>
			       			</logic:equal>
			       			
			       			<!-- Es Exento Pago -->
					       	<logic:equal name="LiqDeudaAdmin" property="esExentoPago" value="true">
			       				<td colspan="4">
			       					<b><bean:write name="LiqDeudaAdmin" property="observacion"/>&nbsp;</b>
								</td>
					    	</logic:equal>
				       	</logic:equal> 
				       	<!-- Fin si Posee Observacion -->
			       	</tr>
				</logic:iterate>
				<!-- Fin Item LiqDeudaVO -->
		       	<tr>       		
		       		<td colspan="7" class="celdatotales" align="right">
			        	<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.subtotal.label"/>: 
			        	<b><bean:write name="GDeudaAdmin" property="subTotal" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       	</tr>	       	
	       	</logic:iterate>
	       	<!-- Fin LiqProcuradorVO -->
	      	</tbody>
		</table>
		</div>	
	</logic:notEmpty>
	<!-- Fin Deuda En Gestion Administrativa -->
	
	<!-- Total -->
	<div class="borde">
		<h3>
			<bean:message bundle="gde" key="gde.liqDeudaAdapter.total"/>: 
			<bean:write name="liqDeudaAdapterVO" property="total" bundle="base" formatKey="general.format.currency"/>
		</h3>
	</div>
	
	<!-- Fecha Acentamiento -->
	<p>
		<label><bean:message bundle="gde" key="gde.liqDeudaAdapter.alaFechaAcentamiento"/>:</label>
		<bean:write name="liqDeudaAdapterVO" property="fechaAcentamiento"/>
	</p>
	
	
	<table class="tablabotones" width="100%">
	   	<tr>
  	   		<td width="50%" align="left">
				<!-- Volver -->
				<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverAIngreso', '');">
			  	    <bean:message bundle="base" key="abm.button.volver"/>
				</button>
			</td>
			<td width="50%" align="left">	
				<button type="button" name="btnAplicarQuitar" class="button" onclick="submitForm('aplicarQuitar', '');">
				 	<bean:message bundle="exe" key="exe.deudaExencionAdapter.button.aplicarQuitar"/>
				</button>
			</td>			
		</tr>				
	</table>
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->