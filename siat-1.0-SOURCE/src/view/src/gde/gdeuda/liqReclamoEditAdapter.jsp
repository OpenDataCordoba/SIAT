<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarLiqReclamo.do">
	
	<h1><bean:message bundle="gde" key="gde.liqReclamoEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<%=ar.gov.rosario.siat.fra.iface.util.Frase.getInstance().getMessageBykey("gde.reclamoAsentamientoDeuda.textoAyudaEncabezadoParrafoUno")%>
				</p>
				<p>
					<%=ar.gov.rosario.siat.fra.iface.util.Frase.getInstance().getMessageBykey("gde.reclamoAsentamientoDeuda.textoAyudaEncabezadoParrafoDos")%>
				</p>
				<p>	
					<%=ar.gov.rosario.siat.fra.iface.util.Frase.getInstance().getMessageBykey("gde.reclamoAsentamientoDeuda.textoAyudaEncabezadoParrafoTres")%>
				</p>
			</td>				
			<td align="right">
	 			<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuenta', '<bean:write name="liqReclamoAdapterVO" property="liqReclamo.idCuenta" bundle="base" formatKey="general.format.id"/>');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</button>
			</td>
		</tr>
	</table>
	
	
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
			
			<!-- Mensajes y/o Advertencias -->
			<%@ include file="/base/warning.jsp" %>
			<!-- Errors  -->
			<html:errors bundle="base"/>
			
			<p>
				<label>
					(*)&nbsp;<bean:message bundle="gde" key="gde.liqReclamo.LugarPago.label"/>:
	      			<html:select name="liqReclamoAdapterVO" property="liqReclamo.banco" styleClass="select">
						<html:optionsCollection name="liqReclamoAdapterVO" property="listLugaresPago" label="label" value="value" />
					</html:select>
	      		</label>	
	      	</p>		

			<p>
				<label>
					(*)&nbsp;<bean:message bundle="gde" key="gde.liqReclamo.FechaPago.label"/>:
					<html:text name="liqReclamoAdapterVO" property="liqReclamo.fechaPagoView" styleId="fechaPagoView" size="10" maxlength="10"/>
					<a class="link_siat" onclick="return show_calendar('fechaPagoView');" id="a_fechaPagoView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
					</a>					
			 	</label>
			</p>
			
			<p>
				<label>
				 	(*)&nbsp;<bean:message bundle="gde" key="gde.liqReclamo.ImportePagado.label"/>:
					<html:text name="liqReclamoAdapterVO" property="liqReclamo.importePagadoView" size="10" maxlength="10"/>
				</label>
			</p>

		</fieldset>	    			

		<fieldset>
			<legend><bean:message bundle="gde" key="gde.liqReclamoEditAdapter.DatosContacto.title"/></legend>
						
			<p>
				<label>
					(*)&nbsp;<bean:message bundle="gde" key="gde.liqReclamo.Nombre.label"/>: 				
					<html:text name="liqReclamoAdapterVO" property="liqReclamo.nombre" size="10" maxlength="50"/>
				</label>
			</p>
					      		
			<p>
				<label>
					(*)&nbsp;<bean:message bundle="gde" key="gde.liqReclamo.Apellido.label"/>:
					<html:text name="liqReclamoAdapterVO" property="liqReclamo.apellido" size="10" maxlength="50"/>
				</label>
			</p>

			<p>
				<label>
					(*)&nbsp;<bean:message bundle="gde" key="gde.liqReclamo.TipoyNroDoc.label"/>: </label>
	      			<html:select name="liqReclamoAdapterVO" property="liqReclamo.tipoDoc" styleClass="select">
						<html:optionsCollection name="liqReclamoAdapterVO" property="listTipoDocumento" label="abreviatura" value="id" />
					</html:select>
	      			<html:text name="liqReclamoAdapterVO" property="liqReclamo.nroDoc" size="10" maxlength="10"/>
			</p>

			<p>
				<label>
					(*)&nbsp;<bean:message bundle="gde" key="gde.liqReclamo.Email.label"/>:
					<html:text name="liqReclamoAdapterVO" property="liqReclamo.email" size="20" maxlength="50"/>
				</label>
			</p>

			<table>
				<tr>
					<td valign="top">
						<label>
							<bean:message bundle="gde" key="gde.liqReclamo.Observacion.label"/>:
						</label>
					</td>
					<td>
		      			<html:textarea name="liqReclamoAdapterVO" property="liqReclamo.observacion" rows="2" cols="30"/>
					</td>				
				</tr>
			</table>
		</fieldset>
	  	
	  	<div style="text-align:center">
	  		
			<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuenta', '<bean:write name="liqReclamoAdapterVO" property="liqReclamo.idCuenta" bundle="base" formatKey="general.format.id"/>');">
				<bean:message bundle="base" key="abm.button.volver"/>
			</button>
	  		
	  		<button type="button" name="btnRegistrar" onclick="submitForm('registrarReclamo', '<bean:write name="liqReclamoAdapterVO" property="liqReclamo.idSelect" bundle="base" formatKey="general.format.id"/>');" class="boton">
	  			<bean:message bundle="gde" key="gde.liqReclamoEditAdapter.button.registrarReclamo"/>
	  		</button>	  	
	  	</div>
	  	
	</fieldset>
	<!-- LiqReclamo -->
	
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
