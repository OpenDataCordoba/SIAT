<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarDecJurDet.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
 	<h1><bean:message bundle="gde" key="gde.decJurDetAdapter.title"/></h1>
	<p align="right">
		<button type="button" name="btn2" class="boton" onclick="submitForm('volver','');">
			<bean:message bundle="base" key="abm.button.volver"/>
		</button>
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
						<bean:write name="decJurDetAdapterVO" property="decJurDet.decJur.cuenta.numeroCuenta"/>
					</td>
				</tr>
				<tr>
					<td>
						<label><bean:message bundle="def" key="def.recurso.label"/>: </label>
					</td>
					<td class="normal">
						<bean:write name="decJurDetAdapterVO" property="decJurDet.decJur.recurso.desRecurso"/>
					</td>
				</tr>
				<tr>
					<td>
						<label><bean:message bundle="gde" key="gde.decJur.periodo.label"/>: </label>
					</td>
					<td class="normal">
						<bean:write name="decJurDetAdapterVO" property="decJurDet.decJur.desPeriodo"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.decJurAdapter.tipoDJ"/>: </label></td>
					<td class="normal">
						<bean:write name="decJurDetAdapterVO" property="decJurDet.decJur.tipDecJurRec.tipDecJur.desTipo"/>
					</td>
				</tr>
				<logic:equal name="decJurDetAdapterVO" property="decJurDet.decJur.esDrei" value="true">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.decJurAdapter.canPer.label"/>: </label></td>
						<td class="normal"><bean:write name="decJurDetAdapterVO" property="decJurDet.decJur.valRefMinIntView"/></td>
					</tr>
				</logic:equal>
				<logic:equal name="decJurDetAdapterVO" property="decJurDet.decJur.esEtur" value="true">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.decJurAdapter.radio.label"/>: </label></td>
						<td class="normal">bean:write name="decJurDetAdapterVO" property="decJurDet.decJur.valRefMinIntView" size="5"/></td>			
					</tr>
				</logic:equal>
				<logic:notEmpty name="decJurDetAdapterVO" property="decJurDet.decJur.minRec">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.decJurAdapter.minimo.label"/>: </label></td>
						<td class="normal">
							<p class="msgBold"><bean:write name="decJurDetAdapterVO" property="decJurDet.decJur.minRecView"/></p>
						</td>
					</tr>
				</logic:notEmpty>
			</table>
	</fieldset>
	
	<!-- DecJurDet -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.decJurDetAdapter.actividad.label"/></legend>
			<p size="100%">
				<bean:write name="decJurDetAdapterVO" property="decJurDet.recConADec.codYDescripcion"/>
			</p>
	</fieldset>
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.decJurDetAdapter.decBaseImp.legend"/></legend>
			<table class="tabladatos">
				<tr>
					<td>
						<label><bean:message bundle="gde" key="gde.decJurDetAdapter.baseImponible.label"/>: </label>
					</td>
					<td class="normal">
						<bean:write name="decJurDetAdapterVO" property="decJurDet.baseView"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.decJurDetAdapter.alicuota.label"/>: </label></td>
					<td class="normal">
						<bean:write name="decJurDetAdapterVO" property="decJurDet.multiploView"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.decJurDetAdapter.subtotal.label"/>: </label></td>
					<td class="normal">
						<bean:write name="decJurDetAdapterVO" property="decJurDet.subtotal1View"/>
					</td>
				</tr>
			</table>	
	</fieldset>
	
	<logic:equal name="decJurDetAdapterVO" property="decJurDet.decJur.declaraPorCantidad" value="true">
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.decJurDetAdapter.decCantidad.legend"/></legend>
			<table class="tabladatos">
				<tr>
					<td>
						<label><bean:message bundle="gde" key="gde.decJurDetAdapter.cantidad.label"/>: </label>
					</td>
					<td class="normal">
						<bean:write name="decJurDetAdapterVO" property="decJurDet.canUni"/>
					</td>
				</tr>
			</table>
		</fieldset>
	</logic:equal>
	<logic:equal name="decJurDetAdapterVO" property="act" value="eliminar">
		<p align="right">
			<button type="button" class="boton" onclick="submitForm('eliminar','');">
				<bean:message bundle="base" key="abm.button.eliminar"/>
			</button>
		</p>
	</logic:equal>
	<p align="left">
		<button type="button" class="boton" onclick="submitForm('volver','');">
			<bean:message bundle="base" key="abm.button.volver"/>
		</button>
	</p>


	
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
		
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
</html:form>
<!-- Fin formulario -->