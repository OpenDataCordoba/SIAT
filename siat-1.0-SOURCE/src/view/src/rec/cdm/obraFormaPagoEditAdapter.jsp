<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>
   	    <%@include file="/base/calendar.js"%>
</script>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rec/AdministrarObraFormaPago.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="rec" key="rec.obraFormaPagoEditAdapter.title"/></h1>

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- Obra -->
	<fieldset>
		<legend><bean:message bundle="rec" key="rec.obra.title"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="obraFormaPagoAdapterVO" property="obraFormaPago.obra.recurso.desRecurso"/>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="rec" key="rec.obra.numeroObra.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="obraFormaPagoAdapterVO" property="obraFormaPago.obra.numeroObraView"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="rec" key="rec.obra.desObra.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="obraFormaPagoAdapterVO" property="obraFormaPago.obra.desObra"/></td>
			</tr>
			
			<tr>
				<td><label><bean:message bundle="rec" key="rec.obra.permiteCamPlaMay.label"/>: </label></td>
				<td class="normal"><bean:write name="obraFormaPagoAdapterVO" property="obraFormaPago.obra.permiteCamPlaMay.value"/></td>				
			</tr>
			
			<tr>
				<td><label><bean:message bundle="rec" key="rec.obra.esPorValuacion.label"/>: </label></td>
				<td class="normal"><bean:write name="obraFormaPagoAdapterVO" property="obraFormaPago.obra.esPorValuacion.value"/></td>				
			</tr>

			<tr>	
				<td><label><bean:message bundle="rec" key="rec.obra.esCostoEsp.label"/>: </label></td>
				<td class="normal"><bean:write name="obraFormaPagoAdapterVO" property="obraFormaPago.obra.esCostoEsp.value"/></td>				
				<logic:equal name="obraFormaPagoAdapterVO" property="obraFormaPago.obra.costoEspEnabled" value="true">					
					<td><label><bean:message bundle="rec" key="rec.obra.costoEsp.label"/>: </label></td>
					<td class="normal"><bean:write name="obraFormaPagoAdapterVO" property="obraFormaPago.obra.costoEspView"/></td>
				</logic:equal>
			</tr>

			<!-- Inclucion de CasoView -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="obraFormaPagoAdapterVO" property="obraFormaPago.obra"/>
					<%@ include file="/cas/caso/includeCasoView.jsp" %>				
				</td>
			</tr>
			<!-- Fin Inclucion de CasoView -->
			
			<tr>
				<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
				<td class="normal"><bean:write name="obraFormaPagoAdapterVO" property="obraFormaPago.obra.estadoObra.desEstadoObra"/></td>
			</tr>
		</table>
	</fieldset>	
	<!-- Obra -->
	
	<!-- ObraFormaPago -->
	<fieldset>
		<legend><bean:message bundle="rec" key="rec.obraFormaPago.title"/></legend>
		<table class="tabladatos">
			<!-- de Obra Forma Pago -->	
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.obraFormaPago.esCantCuotasFijas.label"/>: </label></td>
				<td class="normal">
					<html:select name="obraFormaPagoAdapterVO" property="obraFormaPago.esCantCuotasFijas.id" styleClass="select">
						<html:optionsCollection name="obraFormaPagoAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>					
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.obraFormaPago.cantCuotas.label"/>: </label></td>
				<td class="normal">
					<html:text name="obraFormaPagoAdapterVO" property="obraFormaPago.cantCuotasView" size="15" maxlength="20" />
				</td>
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.obraFormaPago.montoMinimoCuota.label"/>: </label></td>
				<td class="normal">
					<html:text name="obraFormaPagoAdapterVO" property="obraFormaPago.montoMinimoCuotaView" size="15" maxlength="20" />
				</td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.obraFormaPago.descuento.label"/>: </label></td>
				<td class="normal">
					<html:text name="obraFormaPagoAdapterVO" property="obraFormaPago.descuentoView" size="15" maxlength="20" />
				</td>
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.obraFormaPago.interesFinanciero.label"/>: </label></td>
				<td class="normal">
					<html:text name="obraFormaPagoAdapterVO" property="obraFormaPago.interesFinancieroView" size="15" maxlength="20" />
				</td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.obraFormaPago.fechaDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="obraFormaPagoAdapterVO" property="obraFormaPago.fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				<td><label><bean:message bundle="rec" key="rec.obraFormaPago.fechaHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="obraFormaPagoAdapterVO" property="obraFormaPago.fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.obraFormaPago.esEspecial.label"/>: </label></td>
				<td class="normal">
					<html:select name="obraFormaPagoAdapterVO" property="obraFormaPago.esEspecial.id" 
						onchange="submitForm('paramEsEspecial', '');" styleClass="select">
						<html:optionsCollection name="obraFormaPagoAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
				<logic:equal name="obraFormaPagoAdapterVO" property="obraFormaPago.exencionEnabled" value="true">					
					<td><label>(*)&nbsp;<bean:message bundle="exe" key="exe.exencion.label"/>: </label></td>
					<td class="normal">
						<html:select name="obraFormaPagoAdapterVO" property="obraFormaPago.exencion.id" styleClass="select">
							<html:optionsCollection name="obraFormaPagoAdapterVO" property="listExencion" label="desExencion" value="id" />
						</html:select>
					</td>		
				</logic:equal>
			</tr>
		</table>
	</fieldset>	
	<!-- ObraFormaPago -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="obraFormaPagoAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="obraFormaPagoAdapterVO" property="act" value="agregar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
					</html:button>
				</logic:equal>
   	    	</td>   	    	
   	    </tr>
   	</table>
	
	<input type="hidden" name="method" value=""/>
<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
