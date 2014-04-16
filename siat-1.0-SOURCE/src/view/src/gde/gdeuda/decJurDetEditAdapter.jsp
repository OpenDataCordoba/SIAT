<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarDecJurDet.do">


	<script>
		function calcularSubtotal1(){
		var base;
		var alicuota;
		var baseImp = document.getElementById('base');
		var indice;
		if(baseImp==null){
			base=0;
		}else{
			base=baseImp.value;
		}
		var ali = document.getElementById('alicuota');
		if (ali != null){
			indice = ali.selectedIndex;
		}else{
			indice=0;
		}
		if (indice==0){
			alicuota = 0;
		}else{
			alicuota=ali.options[indice].value;
		}
		var subtotal=document.getElementById("subtotal");
		if (subtotal != null){
			subtotal.value= (base * alicuota).toFixed(2);
		}

		
		}
		
		function asignarFoco(){
			var idFoco = document.getElementById('focus').value;
			var fieldCant = document.getElementById('fieldCant');
			if (idFoco=="tipUni"){
				var control = document.getElementById(idFoco);
				fieldCant.focus();
				control.focus();
			}
			fieldCant.style.display="none";
		}
		
		function calcularSubtotal2(){
			var canUni=document.getElementById("canUni").value;
			var minUni = document.getElementById("minUni").value;
			var subtotal2 = document.getElementById("subtotal2");
			subtotal2.value= canUni * minUni;
			 
		}
		
		function calcularTotal(){
			calcularSubtotal1();
			calcularSubtotal2();
			var cSubtotal1 = document.getElementById("subtotal");
			var cSubtotal2 = document.getElementById("subtotal2");
			var subtotal1;
			var subtotal2;
			if (cSubtotal1==null){
				subtotal1 = 0;
			}else{
				subtotal1 = cSubtotal1.value;
			}
			if (cSubtotal2 ==null){
				subtotal2 = 0;
			}else{
				subtotal2 = cSubtotal2.value;
			}
			var total = document.getElementById("total");
			subtotal1 = parseFloat(subtotal1);
			subtotal2=parseFloat(subtotal2);
			if (subtotal1 > subtotal2){
				total.value = subtotal1.toFixed(2);
			}else{
				total.value = subtotal2.toFixed(2);
			}			
		}

		
	</script>
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
 	<h1><bean:message bundle="gde" key="gde.decJurDetAdapter.title"/></h1>
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
						<td class="normal"><bean:write name="decJurDetAdapterVO" property="decJurDet.decJur.valRefMinIntView"/></td>			
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
			<p align="center">
				<label><bean:message bundle="gde" key="gde.decJurDetAdapter.filtroActividad.label"/>: </label>
				<input type="text" name="mytext" onkeyup="fillin(this, 'wholetext');"><br/>
			</p>
			<p>
				<html:select name="decJurDetAdapterVO" property="decJurDet.recConADec.id" style="width:100%" styleClass="select" styleId="wholetext">					
					<html:optionsCollection name="decJurDetAdapterVO" property="listRecConADec" label="codYDescripcion" value="id"/>
				</html:select>
			</p>
	</fieldset>
	<logic:equal name="decJurDetAdapterVO" property="decJurDet.decJur.esEtur" value="true">
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.decJurDetAdapter.actEspecifica.legend"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="gde" key="gde.decJurDetAdapter.actEspecifica.label"/>: </label></td>
					<td class="normal">
						<html:select name="decJurDetAdapterVO" property="decJurDet.tipoUnidad.id" styleId="actEspEtur" onchange="submitForm('paramTipUni','');">
							<html:optionsCollection name="decJurDetAdapterVO" property="listTipoUnidad" label="codYDescripcion" value="id"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td>
						<label><bean:message bundle="gde" key="gde.decJurDetAdapter.baseImponible.label"/>: </label>
					</td>
					<td class="normal">
						<html:text  styleId="base" name="decJurDetAdapterVO" property="decJurDet.baseView"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.decJurDetAdapter.alicuota.label"/>: </label></td>
					<td class="normal" filter="false">
						<bean:write name="decJurDetAdapterVO" property="decJurDet.multiploView" filter="false"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.decJurDetAdapter.derechoCalculado.label"/>: </label></td>
					<td class="normal">
						<html:text name="decJurDetAdapterVO" property="decJurDet.subtotal1View" disabled="true"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.decJurDetAdapter.minimoCalculado.label"/>: </label></td>
					<td class="normal">
						<html:text name="decJurDetAdapterVO" property="decJurDet.minimoView" disabled="true"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.decJurDetAdapter.derechoDeterminado.label"/>: </label></td>
					<td class="normal">
						<html:text name="decJurDetAdapterVO" property="decJurDet.totalConceptoView" disabled="true"/>
					</td>
				</tr>
			</table>
		</fieldset>
	</logic:equal>
	<logic:equal name="decJurDetAdapterVO" property="decJurDet.decJur.declaraBaseImp" value="true">
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.decJurDetAdapter.decBaseImp.legend"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td>
						<label><bean:message bundle="gde" key="gde.decJurDetAdapter.baseImponible.label"/>: </label>
					</td>
					<td class="normal">
						<html:text  styleId="base" name="decJurDetAdapterVO" property="decJurDet.baseView" onchange="calcularTotal();"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.decJurDetAdapter.alicuota.label"/>: </label></td>
					<td class="normal">
						<html:select name="decJurDetAdapterVO" styleId="alicuota" property="decJurDet.multiplo" styleClass="select" onchange="calcularTotal();" >
							<html:optionsCollection filter="false" name="decJurDetAdapterVO" property="listRecAli" label="alicuotaView" value="alicuota" />
						</html:select>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.decJurDetAdapter.subtotal.label"/>: </label></td>
					<td class="normal">
						<html:text name="decJurDetAdapterVO" property="decJurDet.subtotal1View" styleId="subtotal" disabled="true"/>
					</td>
				</tr>
			</table>	
			
		</fieldset>
	</logic:equal>
	<logic:equal name="decJurDetAdapterVO" property="decJurDet.decJur.declaraPorCantidad" value="true">
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.decJurDetAdapter.decCantidad.legend"/></legend>
			<table class="tabladatos">
				<tr>
					<td>
						<label><bean:message bundle="gde" key="gde.decJurDetAdapter.cantidad.label"/>: </label>
					</td>
					<td class="normal">
						<html:text name="decJurDetAdapterVO" property="decJurDet.canUni" styleId="canUni" onchange="calcularTotal();"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.decJurDetAdapter.unidad.label"/>: </label></td>
					<td class="normal">
						<html:select name="decJurDetAdapterVO" styleId="uni" property="decJurDet.recTipUni.id" onchange="submitForm('paramUnidad','');" styleClass="select">
							<html:optionsCollection filter="false" name="decJurDetAdapterVO" property="listRecTipUni" label="nomenclatura" value="id" />
						</html:select>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.decJurDetAdapter.tipoUnidad.label"/>: </label></td>
					<td class="normal">
						<html:select name="decJurDetAdapterVO" style="width:100%" styleId="tipUni" property="decJurDet.tipoUnidad.id" onchange="submitForm('paramTipUni','');" styleClass="select">
							<html:optionsCollection name="decJurDetAdapterVO" property="listTipoUnidad" label="codYDescripcion" value="id"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.decJurDetAdapter.minimoUnidad.label"/>: </label></td>
					<td class="normal">
						<html:text name="decJurDetAdapterVO" property="decJurDet.valUnidadView" styleId="minUni" disabled="true"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.decJurDetAdapter.subtotal2.label"/>: </label></td>
					<td class="normal">
						<html:text name="decJurDetAdapterVO" property="decJurDet.subtotal2View" styleId="subtotal2" disabled="true"/>
					</td>
				</tr>
			</table>
		</fieldset>
	</logic:equal>
	<!-- este textarea sirve para posicionar el adapter luego del param -->	
		<textarea id="fieldCant">&nbsp;</textarea>
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.decJurDetAdapter.totales.legend"/></legend>
			<p align="center">
				<label><bean:message bundle="gde" key="gde.decJurDetAdapter.total.label"/>: </label>
				<html:text name="decJurDetAdapterVO" property="decJurDet.totalConceptoView" disabled="true" styleId="total"/>
			</p>
		</fieldset>
	<p align="right">
		<logic:equal name="decJurDetAdapterVO" property="act" value="agregar">
			<button type="button" class="boton" onclick="submitForm('agregarDecJurDet','');">
				<bean:message bundle="base" key="abm.button.agregar"/>
			</button>
		</logic:equal>
		<logic:equal name="decJurDetAdapterVO" property="act" value="modificar">
			<button type="button"  id="fieldCant"class="boton" onclick="submitForm('modificarDecJurDet','');">
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
		<input type="hidden" name="focus" id="focus" value="<bean:write name="decJurDetAdapterVO" property="idFoco"/>"/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
	<script>
		asignarFoco();
		calcularTotal();
	</script>
</html:form>
<!-- Fin formulario -->