<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<html:form styleId="filter" action="/gde/AdministrarLiqReclamo.do">

<logic:equal name="liqReclamoAdapterVO" property="reclamoCreado" value="false">
	<table width="100%" border="0">
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
	</table>
</logic:equal>


	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>


<logic:equal name="liqReclamoAdapterVO" property="reclamoCreado" value="true">

	<!-- LiqReclamo -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqReclamoEditAdapter.Reclamo.title"/></legend>
		
		<dl class="listahorizontalSiat">
     	   	<dt><bean:message bundle="gde" key="gde.liqReclamo.Recurso.label"/>: </dt> 
     	   	<dd><bean:write name="liqReclamoAdapterVO" property="liqReclamo.desRecurso"/></dd>
     	</dl>
     	<dl class="listahorizontalSiat">
     	   	<dt><bean:message bundle="gde" key="gde.liqReclamo.Cuenta.label"/>: </dt> 
     	   		<dd><bean:write name="liqReclamoAdapterVO" property="liqReclamo.numeroCuenta"/></dd>
     	   	<dt><bean:message bundle="gde" key="gde.liqReclamo.Deuda.label"/>:  </dt> 
     	   		<dd><bean:write name="liqReclamoAdapterVO" property="liqReclamo.periodoAnio"/></dd>
     	</dl>
		<dl class="listahorizontalSiat">
     	   	<dt><bean:message bundle="gde" key="gde.liqReclamo.FechaVto.label"/>: </dt> 
     	   		<dd><bean:write name="liqReclamoAdapterVO" property="liqReclamo.fechaVto"/> </dd>
     	   	<dt><bean:message bundle="gde" key="gde.liqReclamo.Importe.label"/>: </dt> 
     	   		<dd><bean:write name="liqReclamoAdapterVO" property="liqReclamo.importe" bundle="base" formatKey="general.format.currency"/></dd>
     	</dl>
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.liqReclamoEditAdapter.DatosComprobantePago.title"/></legend>
			
			<p>
				<label>
					<bean:message bundle="gde" key="gde.liqReclamo.FechaPago.label"/>:
			 	</label>
					<bean:write name="liqReclamoAdapterVO" property="liqReclamo.fechaPagoView"/>					
			</p>
			
			<p>
				<label>
				 	<bean:message bundle="gde" key="gde.liqReclamo.ImportePagado.label"/>:
				</label>
					<bean:write name="liqReclamoAdapterVO" property="liqReclamo.importePagadoView" />
			</p>

			<p>
				<label>
					<bean:message bundle="gde" key="gde.liqReclamo.LugarPago.label"/>:
	      		</label>	
	      			<bean:write name="liqReclamoAdapterVO" property="liqReclamo.banco"/>
	      	</p>		
		</fieldset>	    			

		<fieldset>
			<legend><bean:message bundle="gde" key="gde.liqReclamoEditAdapter.DatosContacto.title"/></legend>
						
			<p>
				<label>
					<bean:message bundle="gde" key="gde.liqReclamo.Nombre.label"/>: 				
				</label>
					<bean:write name="liqReclamoAdapterVO" property="liqReclamo.nombre"/>
			</p>
					      		
			<p>
				<label>
					<bean:message bundle="gde" key="gde.liqReclamo.Apellido.label"/>:
				</label>
					<bean:write name="liqReclamoAdapterVO" property="liqReclamo.apellido"/>
			</p>

			<p>
				<label>
					<bean:message bundle="gde" key="gde.liqReclamo.TipoyNroDoc.label"/>: </label>
	      			<bean:write name="liqReclamoAdapterVO" property="liqReclamo.tipoDoc"/>
					&nbsp; 	
	      			<bean:write name="liqReclamoAdapterVO" property="liqReclamo.nroDoc"/>
			</p>
							
			<p>
				<label>
					<bean:message bundle="gde" key="gde.liqReclamo.Email.label"/>:
				</label>
					<bean:write name="liqReclamoAdapterVO" property="liqReclamo.email"/>
			</p>

			<table>
				<tr>
					<td valign="top">
						<label>
							<bean:message bundle="gde" key="gde.liqReclamo.Observacion.label"/>:
						</label>
					</td>
					<td>
		      			<bean:write name="liqReclamoAdapterVO" property="liqReclamo.observacion"/>
					</td>				
				</tr>
			</table>
		</fieldset>
	</fieldset>
	<!-- LiqReclamo -->
</logic:equal>

<logic:equal name="liqReclamoAdapterVO" property="reclamoCreado" value="false">
	<table width="100%" border="0">
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
	</table>
</logic:equal>

	
	<table width="100%" border="0">
		<tr>
			<td align="right">
			  	<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuenta', '<bean:write name="liqReclamoAdapterVO" property="liqReclamo.idCuenta" bundle="base" formatKey="general.format.id"/>');">
					Continuar
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