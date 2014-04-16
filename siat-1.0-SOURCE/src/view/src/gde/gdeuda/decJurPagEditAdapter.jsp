<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarDecJurPag.do">


	<script>
		function mostrar(){
			var tipo = document.getElementById('tipoPago');
			var indice = tipo.selectedIndex;
			var act=document.getElementById('currentAct');
			
			if (indice > 0 || act.value == "modificar"){
				var valor=tipo.options[indice].value;
				var lVal=valor.split(",");
				var ret = document.getElementById('certificado');
				var cuitA=document.getElementById('cuitAg');
				if (lVal[1]==1){
					ret.style.display="table-row";
					cuitA.style.display="table-row";
				} else{
					ret.style.display="none";
					cuitA.style.display="none";
				}
				
			}
		
		}
	</script>
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
						<td class="normal"><bean:write name="decJurPagAdapterVO" property="decJurPag.decJur.valRefMinIntView"/></td>			
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
				<html:select name="decJurPagAdapterVO" styleId="tipoPago" property="decJurPag.tipPagDecJur.idWithCertificado" styleClass="select" onchange="mostrar();">
					<html:optionsCollection name="decJurPagAdapterVO" property="listTipPagDecJur" label="desTipPag" value="idWithCertificado" />
				</html:select>
			</td>
		</tr>
		<tr>
			<td><label><bean:message bundle="gde" key="gde.decJurPagAdapter.importe.label"/>: </label></td>
			<td class="normal">
				<html:text name="decJurPagAdapterVO" property="decJurPag.importeView"/>
			</td>
		</tr>
			<tr id="certificado" style="display: none">
				<td>
					<label><bean:message bundle="gde" key="gde.decJurPagAdapter.certificado.label"/>: </label>
				</td>
				<td class="normal">
					<html:text  name="decJurPagAdapterVO" property="decJurPag.certificado"/>
				</td>
			</tr>
			<tr id="cuitAg" style="display: none">
				<td><label><bean:message bundle="gde" key="gde.decJurPagAdapter.cuitAgente.label"/>: </label></td>
				<td class="normal">
					<html:select name="decJurPagAdapterVO" property="decJurPag.cuitAgente" styleClass="select" >
						<html:optionsCollection name="decJurPagAdapterVO" property="listAgeRet" label="desCuitView" value="cuit" />
					</html:select>
				</td>
			</tr>
	</table>	
		
	</fieldset>
	
	<p align="right">
		<logic:equal name="decJurPagAdapterVO" property="act" value="agregar">
			<button type="button" class="boton" onclick="submitForm('agregarDecJurPag','');">
				<bean:message bundle="base" key="abm.button.agregar"/>
			</button>
		</logic:equal>
		<logic:equal name="decJurPagAdapterVO" property="act" value="modificar">
			<button type="button" class="boton" onclick="submitForm('modificarDecJurPag','');">
				<bean:message bundle="base" key="abm.button.modificar"/>
			</button>
		</logic:equal>
	</p>
	<p align="left">
		<button type="button" name="btn2" class="boton" onclick="submitForm('volver','');"><bean:message bundle="base" key="abm.button.volver"/></button>
	</p>


	
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
		<input type="hidden" name"act" value="<bean:write name="decJurPagAdapterVO" property="act"/>" id="currentAct"/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
	<script>
		mostrar();
	</script>
</html:form>
<!-- Fin formulario -->