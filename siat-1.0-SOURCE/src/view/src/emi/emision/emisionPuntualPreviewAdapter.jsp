<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/AdministrarEmisionPuntualPreview.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="emi" key="emi.emisionPuntualPreviewAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<fieldset>
		<legend><bean:message bundle="emi" key="emi.emision.title"/></legend>
		
		<table class="tabladatos">
			<tr>
				<!-- Recurso -->
				<td><label><bean:message bundle="emi" key="emi.emision.recurso.label"/>: </label></td>
				<td class="normal"><bean:write name="emisionPuntualPreviewAdapterVO" property="emision.recurso.desRecurso"/></td>
			</tr>

			<tr>
				<!-- Cuenta -->
				<td><label><bean:message bundle="emi" key="emi.emision.cuenta.label"/>: </label></td>
				<td class="normal"><bean:write name="emisionPuntualPreviewAdapterVO" property="emision.cuenta.numeroCuenta"/></td>
			</tr>

			<tr>
				<!-- Fecha Emision -->
				<td><label><bean:message bundle="emi" key="emi.emision.fechaEmision.label"/>: </label></td>
				<td class="normal"><bean:write name="emisionPuntualPreviewAdapterVO" property="emision.fechaEmisionView"/></td>
			</tr>

			<tr>
				<!-- Anio -->
				<td><label><bean:message bundle="emi" key="emi.emision.anio.label"/>: </label></td>
				<td class="normal"><bean:write name="emisionPuntualPreviewAdapterVO" property="emision.anioView"/></td>
			</tr>

			<tr>
				<!-- Periodo Desde -->
				<td><label><bean:message bundle="emi" key="emi.emision.periodoDesde.label"/>: </label></td>
				<td class="normal"><bean:write name="emisionPuntualPreviewAdapterVO" property="emision.periodoDesdeView"/></td>

				<!-- Periodo Hasta -->
				<td><label><bean:message bundle="emi" key="emi.emision.periodoHasta.label"/>: </label></td>
				<td class="normal"><bean:write name="emisionPuntualPreviewAdapterVO" property="emision.periodoHastaView"/></td>
			</tr>
		</table>
	</fieldset>	

	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:notEmpty  name="emisionPuntualPreviewAdapterVO" property="emision.listAuxDeuda">	
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
               	<tbody>
	               	<tr>
						<th align="left"><bean:message bundle="emi" key="emi.auxDeuda.deuda.label"/></th>

						<th align="left"><bean:message bundle="emi" key="emi.auxDeuda.fechaVencimiento.label"/></th>

						<th align="left"><bean:message bundle="emi" key="emi.auxDeuda.importe.label"/></th>

						<logic:equal name="emisionPuntualPreviewAdapterVO" property="mostrarConcepto1" value="true">
							<th align="left"><bean:write name="emisionPuntualPreviewAdapterVO" property="nameConcepto1"/></th>
						</logic:equal>
						<logic:equal name="emisionPuntualPreviewAdapterVO" property="mostrarConcepto2" value="true">
							<th align="left"><bean:write name="emisionPuntualPreviewAdapterVO" property="nameConcepto2"/></th>
						</logic:equal>
						<logic:equal name="emisionPuntualPreviewAdapterVO" property="mostrarConcepto3" value="true">
							<th align="left"><bean:write name="emisionPuntualPreviewAdapterVO" property="nameConcepto3"/></th>
						</logic:equal>
						<logic:equal name="emisionPuntualPreviewAdapterVO" property="mostrarConcepto4" value="true">
							<th align="left"><bean:write name="emisionPuntualPreviewAdapterVO" property="nameConcepto4"/></th>
						</logic:equal>
					</tr>
						
					<logic:iterate id="AuxDeudaVO" name="emisionPuntualPreviewAdapterVO" property="emision.listAuxDeuda">
						<tr>
							<td>
								<logic:notEqual name="AuxDeudaVO" property="periodoView" value="0">
									<bean:write name="AuxDeudaVO" property="periodoView"/>/
									<bean:write name="AuxDeudaVO" property="anioView"/>&nbsp;
								</logic:notEqual>
								<logic:equal name="AuxDeudaVO" property="periodoView" value="0">
									<bean:write name="emisionPuntualPreviewAdapterVO" property="emision.observacion"/>&nbsp;
								</logic:equal>
							</td>

							<td><bean:write name="AuxDeudaVO" property="fechaVencimientoView"/>&nbsp;</td>

							<td><bean:write name="AuxDeudaVO" property="importeView"/>&nbsp;</td>

							<logic:equal name="emisionPuntualPreviewAdapterVO" property="mostrarConcepto1" value="true">								
								<td><bean:write name="AuxDeudaVO" property="conc1View"/>&nbsp;</td>
							</logic:equal>

							<logic:equal name="emisionPuntualPreviewAdapterVO" property="mostrarConcepto2" value="true">								
								<td><bean:write name="AuxDeudaVO" property="conc2View"/>&nbsp;</td>
							</logic:equal>

							<logic:equal name="emisionPuntualPreviewAdapterVO" property="mostrarConcepto3" value="true">								
								<td><bean:write name="AuxDeudaVO" property="conc3View"/>&nbsp;</td>
							</logic:equal>

							<logic:equal name="emisionPuntualPreviewAdapterVO" property="mostrarConcepto4" value="true">								
								<td><bean:write name="AuxDeudaVO" property="conc4View"/>&nbsp;</td>
							</logic:equal>
						</tr>
					</logic:iterate>
				</tbody>
			</table>
		</logic:notEmpty>
		
		<logic:empty name="emisionPuntualPreviewAdapterVO" property="emision.listAuxDeuda">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
               	<tbody>
					<tr><td align="center">
						<bean:message bundle="base" key="base.resultadoVacio"/>
					</td></tr>
				</tbody>			
			</table>
		</logic:empty>
	</div>

	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('confirmar', '');">
					<bean:message bundle="emi" key="emi.emisionPuntualPreviewAdapter.button.confirmar"/>
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
<!-- Fin Tabla que contiene todos los formularios -->
<!-- emisionPuntualPreviewAdapter.jsp -->