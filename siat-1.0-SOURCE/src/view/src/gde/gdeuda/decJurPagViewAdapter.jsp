<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarDecJurPag.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
 	<h1><bean:message bundle="gde" key="gde.decJurPagAdapter.title"/></h1>
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
						<bean:write name="decJurPagAdapterVO" property="decJurPag.decJur.cuenta.numeroCuenta"/>
					</td>
				</tr>
				<tr>
					<td>
						<label><bean:message bundle="def" key="def.recurso.label"/>: </label>
					</td>
					<td class="normal">
						<bean:write name="decJurPagAdapterVO" property="decJurPag.decJur.recurso.desRecurso"/>
					</td>
				</tr>
				<tr>
					<td>
						<label><bean:message bundle="gde" key="gde.decJur.periodo.label"/>: </label>
					</td>
					<td class="normal">
						<bean:write name="decJurPagAdapterVO" property="decJurPag.decJur.desPeriodo"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.decJurAdapter.tipoDJ"/>: </label></td>
					<td class="normal">
						<bean:write name="decJurPagAdapterVO" property="decJurPag.decJur.tipDecJurRec.tipDecJur.desTipo"/>
					</td>
				</tr>
				<logic:equal name="decJurPagAdapterVO" property="decJurPag.decJur.esDrei" value="true">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.decJurAdapter.canPer.label"/>: </label></td>
						<td class="normal"><bean:write name="decJurPagAdapterVO" property="decJurPag.decJur.valRefMinIntView"/></td>
					</tr>
				</logic:equal>
				<logic:equal name="decJurPagAdapterVO" property="decJurPag.decJur.esEtur" value="true">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.decJurAdapter.radio.label"/>: </label></td>
						<td class="normal">bean:write name="decJurPagAdapterVO" property="decJurPag.decJur.valRefMinIntView" size="5"/></td>			
					</tr>
				</logic:equal>
				<logic:notEmpty name="decJurPagAdapterVO" property="decJurPag.decJur.minRec">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.decJurAdapter.minimo.label"/>: </label></td>
						<td class="normal">
							<p class="msgBold"><bean:write name="decJurPagAdapterVO" property="decJurPag.decJur.minRecView"/></p>
						</td>
					</tr>
				</logic:notEmpty>
			</table>
	</fieldset>
	
	<!-- DecJurPag -->
	<fieldset>
	<legend><bean:message bundle="gde" key="gde.decJurPagAdapter.detallePago.legend"/></legend>
	<table class="tabladatos">
		<tr>
			<td><label><bean:message bundle="gde" key="gde.decJurPagAdapter.tipoPago.label"/>: </label></td>
			<td class="normal">
				<bean:write name="decJurPagAdapterVO" property="decJurPag.tipPagDecJur.desTipPag"/>
			</td>
		</tr>
		<tr>
			<td><label><bean:message bundle="gde" key="gde.decJurPagAdapter.importe.label"/>: </label></td>
			<td class="normal">
				<bean:write name="decJurPagAdapterVO" property="decJurPag.importeView"/>
			</td>
		</tr>
		<logic:equal name="decJurPagAdapterVO" property="decJurPag.conCertificado" value="true">
			<tr>
				<td>
					<label><bean:message bundle="gde" key="gde.decJurPagAdapter.certificado.label"/>: </label>
				</td>
				<td class="normal">
					<bean:write  name="decJurPagAdapterVO" property="decJurPag.certificado"/>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.decJurPagAdapter.cuitAgente.label"/>: </label></td>
				<td class="normal">
					<bean:write  name="decJurPagAdapterVO" property="decJurPag.cuitAgente"/>
				</td>
			</tr>
		</logic:equal>
		
	</table>	
		
	</fieldset>
	
	<p align="right">
		<logic:equal name="decJurPagAdapterVO" property="act" value="eliminar">
			<button type="button" class="boton" onclick="submitForm('eliminar','');">
				<bean:message bundle="base" key="abm.button.eliminar"/>
			</button>
		</logic:equal>
	</p>
	<p align="left">
		<button type="button" name="btn2" class="boton" onclick="submitForm('volver','');"><bean:message bundle="base" key="abm.button.volver"/></button>
	</p>
	<script>
		mostrar();
	</script>

	
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
		
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
</html:form>
<!-- Fin formulario -->