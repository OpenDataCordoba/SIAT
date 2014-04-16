<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/cyq/AdministrarDeudaCyq.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="cyq" key="cyq.deudaCyqAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>

	<!-- LiqCuenta -->
		<bean:define id="DeudaAdapterVO" name="liqDeudaAdapterVO"/>
		<%@ include file="/gde/gdeuda/includeLiqCuenta.jsp" %>
	<!-- LiqCuenta -->
	
	<!-- CuentasRel -->
	<logic:notEmpty name="liqDeudaAdapterVO" property="listCuentaRel" >
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuentasRelacionadas.title"/> </legend>			
			<ul>
				<logic:iterate id="CuentaRel" name="liqDeudaAdapterVO" property="listCuentaRel">
					<li>
			      		<bean:write name="CuentaRel" property="nroCuenta"/> -
			      		<bean:write name="CuentaRel" property="desCategoria"/> -
			      		<bean:write name="CuentaRel" property="desRecurso"/>
					</li>
				</logic:iterate>
			</ul>		
		</fieldset>
	</logic:notEmpty>
	<!-- Fin CuentasRel -->
	
	<!-- Exencion - Caso Solical - Otro -->
		<!--  Utilizamos en bean definido para la inclucion de liqCuenta -->
		<%@ include file="/gde/gdeuda/includeExenciones.jsp" %>
	<!-- Fin Exencion - Caso Solical - Otro -->	
	
	
	<!-- Concurso y Quiebra -->
	<logic:notEmpty name="liqDeudaAdapterVO" property="listProcedimientoCyQ">
		<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockCyQ.title"/></caption>
	      	<tbody>
	      	<!-- LiqProcedimientoCyQVO -->
	       	<logic:iterate id="Procedimiento" name="liqDeudaAdapterVO" property="listProcedimientoCyQ">
		       	<tr>
		       		<td colspan="4">
		       			<b><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockCyQ.nroProcedimiento.label"/>:</b> 
		       			<bean:write name="Procedimiento" property="nroProcedimiento"/>
		       		</td>
		       		<td colspan="2">
		       			<b><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockCyQ.fechaActualizDeu.label"/>:</b> 
		       			<bean:write name="Procedimiento" property="fechaActualizacion"/>
		       		</td>
		       	</tr>
		       	<tr>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.periodoDeuda"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.fechaVto"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.saldo"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total"/></th>
				</tr>
				
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqDeudaCYQ" name="Procedimiento" property="listDeuda">
					<tr>
			  			<td><bean:write name="LiqDeudaCYQ" property="periodoDeuda"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaCYQ" property="fechaVto"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaCYQ" property="saldo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaCYQ" property="actualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaCYQ" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
			       	</tr>
				</logic:iterate>
				<!-- Fin Item LiqDeudaVO -->
		       	<tr>       		
		       		<td colspan="6" class="celdatotales" align="right">
			        	<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockCyQ.subtotal.label"/>: 
			        	<b><bean:write name="Procedimiento" property="subTotal" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       	</tr>	       	
	       	</logic:iterate>
	       	<!-- Fin LiqProcedimientoCyQVO -->
	      	</tbody>
		</table>
		</div>
	</logic:notEmpty>
	<!-- Fin Concurso y Quiebra -->
	
	<!-- Deuda en Gestion Judicial -->
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
		       			<logic:equal name="Procurador" property="mostrarChkAllJudicial" value="true">
			       			<br>
			       			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				       		<input type="checkbox" onclick="changeChkProcurador('filter', 'listIdDeudaSelected', this, 'procurador<bean:write name="Procurador" property="idProcurador" bundle="base" formatKey="general.format.id"/>');"/>
			       		</logic:equal>	
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
						<!-- Seleccion de deuda para su operacion -->
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
					        					        
					        <!-- Posee convenio -->	
							<logic:equal name="LiqDeudaJud" property="poseeConvenio" value="true">
						        <td><b><bean:write name="LiqDeudaJud" property="saldo" bundle="base" formatKey="general.format.currency"/></b></td>
						        <td><b><bean:write name="LiqDeudaJud" property="actualizacion" bundle="base" formatKey="general.format.currency"/></b></td>
						        <td><b><bean:write name="LiqDeudaJud" property="total" bundle="base" formatKey="general.format.currency"/></b></td>
							</logic:equal>

							<!-- NO Posee convenio -->
					       <logic:notEqual name="LiqDeudaJud" property="poseeConvenio" value="true">
						        <td><bean:write name="LiqDeudaJud" property="saldo" bundle="base" formatKey="general.format.currency"/></td>
						        <td><bean:write name="LiqDeudaJud" property="actualizacion" bundle="base" formatKey="general.format.currency"/></td>
						        <td><bean:write name="LiqDeudaJud" property="total" bundle="base" formatKey="general.format.currency"/></td>
							</logic:notEqual>
							
					    </logic:notEqual>
					    
				       	<!-- Si posee observacion, se utiliza para armar link a verConvenio -->
				       	<logic:equal name="LiqDeudaJud" property="poseeObservacion" value="true">
		       				<td colspan="3">
		       					<b><bean:write name="LiqDeudaJud" property="observacion"/></b>
							</td>
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
	<!-- Fin Deuda en Gestion Judicial -->
	
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
		       			<logic:equal name="liqDeudaAdapterVO" property="mostrarChkAllAdmin" value="true">
			       			<br>
			       			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			       			<input type="checkbox" onclick="changeChk('filter', 'listIdDeudaSelected', this)"/>
			       		</logic:equal>
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
						<!-- Seleccion de deuda para su operacion -->
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
							
							<!-- Posee convenio -->	
							<logic:equal name="LiqDeudaAdmin" property="poseeConvenio" value="true">
						        <td><b><bean:write name="LiqDeudaAdmin" property="saldo" bundle="base" formatKey="general.format.currency"/></b></td>
						        <td><b><bean:write name="LiqDeudaAdmin" property="actualizacion" bundle="base" formatKey="general.format.currency"/></b></td>
						        <td><b><bean:write name="LiqDeudaAdmin" property="total" bundle="base" formatKey="general.format.currency"/></b></td>
							</logic:equal>

							<!-- NO Posee convenio -->
					       <logic:notEqual name="LiqDeudaAdmin" property="poseeConvenio" value="true">
						        <td><bean:write name="LiqDeudaAdmin" property="saldo" bundle="base" formatKey="general.format.currency"/></td>
						        <td><bean:write name="LiqDeudaAdmin" property="actualizacion" bundle="base" formatKey="general.format.currency"/></td>
						        <td><bean:write name="LiqDeudaAdmin" property="total" bundle="base" formatKey="general.format.currency"/></td>
							</logic:notEqual>
					        
					    </logic:notEqual>
				       	
				       	<!-- Si posee Observacion -->
				       	<logic:equal name="LiqDeudaAdmin" property="poseeObservacion" value="true">
				       		<td colspan="3">
			       				<b><bean:write name="LiqDeudaAdmin" property="observacion"/></b>
							</td>
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

	<logic:equal name="liqDeudaAdapterVO" property="seleccionarDeuda4Cyq" value="COLOCAR_TRUE_AQUI">
		<p>
			<label><bean:message bundle="cyq" key="cyq.deudaCyqAdapter.continuaGesViaOri.label"/>: </label></td>
			<html:select name="liqDeudaAdapterVO" property="continuaGesViaOri.id" styleClass="select">
				<html:optionsCollection name="liqDeudaAdapterVO" property="listSiNo" label="value" value="id" />
			</html:select>
		</p>
	</logic:equal>
	
	<p align="center">
		<logic:equal name="liqDeudaAdapterVO" property="seleccionarDeuda4Cyq" value="true">
			<button type="button" name="btnSeleccionar" class="boton" onclick="submitForm('seleccionar', '');">
	  	    	<bean:message bundle="cyq" key="cyq.deudaCyqAdapter.button.seleccionarDeuda"/>
			</button>
		</logic:equal>
		<logic:notEqual name="liqDeudaAdapterVO" property="seleccionarDeuda4Cyq" value="true">
			<button type="button" name="btnSeleccionar" class="boton" disabled="disabled">
	  	    	<bean:message bundle="cyq" key="cyq.deudaCyqAdapter.button.seleccionarDeuda"/>
			</button>		
		</logic:notEqual>
		&nbsp;
		<logic:equal name="liqDeudaAdapterVO" property="seleccionarCuenta4Cyq" value="true">
			<button type="button" name="btnSeleccionarCuenta" class="boton" onclick="submitForm('seleccionarCuenta', '');">
	  	    	<bean:message bundle="cyq" key="cyq.deudaCyqAdapter.button.seleccionarCuenta"/>
			</button>
		</logic:equal>
		<logic:notEqual name="liqDeudaAdapterVO" property="seleccionarCuenta4Cyq" value="true">
			<button type="button" name="btnSeleccionarCuenta" class="boton" disabled="disabled">
	  	    	<bean:message bundle="cyq" key="cyq.deudaCyqAdapter.button.seleccionarCuenta"/>
			</button>
		</logic:notEqual>
	</p>
	
	
	<!-- Volver -->	
	<button type="button" name="btnVolver" class="boton" onclick="submitForm('volver', '');">
  	    <bean:message bundle="base" key="abm.button.volver"/>
	</button>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->