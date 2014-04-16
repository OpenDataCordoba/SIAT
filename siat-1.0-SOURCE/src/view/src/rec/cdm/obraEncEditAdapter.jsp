<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
	<%@include file="/base/submitForm.js"%>
   	<%@include file="/base/calendar.js"%>
</script>

<!-- Formulario -->
<html:form styleId="filter" action="/rec/AdministrarEncObra.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="rec" key="rec.obraEditAdapter.title"/></h1>		
	
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
				<!-- Recurso -->
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="encObraAdapterVO" property="obra.recurso.id" styleClass="select" >
						<bean:define id="includeRecursoList" name="encObraAdapterVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="encObraAdapterVO" property="obra.idRecurso"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
				</td>				
			</tr>

			
			<tr>
				<!-- Numero Obra -->
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.obra.numeroObra.label"/>: </label></td>
				<td class="normal"><html:text name="encObraAdapterVO" property="obra.numeroObraView" size="20" maxlength="100"/></td>			
			</tr>
			<tr>
				<!-- Descripcion -->
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.obra.desObra.label"/>: </label></td>
				<td class="normal" colspan="3"><html:text name="encObraAdapterVO" property="obra.desObra" size="50" maxlength="100"/></td>
			</tr>
			<tr>	
				<!--Permite Cambio a Plan Mayor -->
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.obra.permiteCamPlaMay.label"/>: </label></td>
				<td class="normal">
					<html:select name="encObraAdapterVO" property="obra.permiteCamPlaMay.id" styleClass="select">
						<html:optionsCollection name="encObraAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>					
			</tr>
			<tr>	
				<!-- Es Por Valuacion -->
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.obra.esPorValuacion.label"/>: </label></td>
				<td class="normal">
					<html:select name="encObraAdapterVO" property="obra.esPorValuacion.id" onchange="submitForm('paramValuacion', '');" styleClass="select">
						<html:optionsCollection name="encObraAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>					
			</tr>
	
			<tr>	
				<!-- Es Costo Especial -->
				<logic:equal name="encObraAdapterVO" property="obra.esCostoEspEnabled" value="true">
					<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.obra.esCostoEsp.label"/>: </label></td>
					<td class="normal">
						<html:select name="encObraAdapterVO" property="obra.esCostoEsp.id" 
							onchange="submitForm('paramEsCostoEsp', '');" styleClass="select">
							<html:optionsCollection name="encObraAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
					<!-- Costo Especial -->
					<logic:equal name="encObraAdapterVO" property="obra.costoEspEnabled" value="true">					
						<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.obra.costoEsp.label"/>: </label></td>
						<td class="normal"><html:text name="encObraAdapterVO" property="obra.costoEspView" size="10" maxlength="10"/></td>				
					</logic:equal>
				</logic:equal>
			</tr>
	
			<!-- Inclucion de Caso -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="encObraAdapterVO" property="obra"/>
					<bean:define id="voName" value="obra" />
					<%@ include file="/cas/caso/includeCaso.jsp" %>
				</td>
			</tr>
			<!-- Fin Inclucion de Caso -->

			<!-- Estado -->
			<logic:equal name="encObraAdapterVO" property="act" value="modificar">			
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="encObraAdapterVO" property="obra.estadoObra.desEstadoObra"/></td>
				</tr>
			</logic:equal>
	

			<logic:equal name="encObraAdapterVO" property="act" value="agregar">			
				<tr>
					<!-- Monto Minimio Cuota -->
					<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.obraFormaPago.montoMinimoCuota.label"/>: </label></td>
					<td class="normal"><html:text name="encObraAdapterVO" property="obra.montoMinimoCuotaView" size="10" maxlength="10"/></td>
					<!-- Interes Financiero -->
					<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.obraFormaPago.interesFinanciero.label"/>: </label></td>
					<td class="normal"><html:text name="encObraAdapterVO" property="obra.interesFinancieroView" size="10" maxlength="10"/></td>			
				</tr>
			</logic:equal>
		</table>
	</fieldset>
	<!-- Obra -->
	
	<!-- Leyendas de los Recibos-->
	<fieldset>
		<legend><bean:message bundle="rec" key="rec.obra.leyendas.label"/></legend>
		
		<table class="tabladatos">
			
			<!-- Leyenda Contado -->			
			<tr>
				<td><label><bean:message bundle="rec" key="rec.obra.leyCon.label"/>: </label></td>
				<td class="normal" colspan="3"><html:text name="encObraAdapterVO" property="obra.leyCon" size="50" maxlength="255"/></td>
			</tr>
			
			<!-- Leyenda Primer Cuota -->			
			<tr>
				<td><label><bean:message bundle="rec" key="rec.obra.leyPriCuo.label"/>: </label></td>
				<td class="normal" colspan="3"><html:text name="encObraAdapterVO" property="obra.leyPriCuo" size="50" maxlength="255"/></td>
			</tr>
			
			<!-- Leyenda Cuota Restante -->			
			<tr>
				<td><label><bean:message bundle="rec" key="rec.obra.leyResCuo.label"/>: </label></td>
				<td class="normal" colspan="3"><html:text name="encObraAdapterVO" property="obra.leyResCuo" size="50" maxlength="255"/></td>
			</tr>

			<!-- Leyenda Cambio de Plan -->			
			<tr>
				<td><label><bean:message bundle="rec" key="rec.obra.leyCamPla.label"/>: </label></td>
				<td class="normal" colspan="3"><html:text name="encObraAdapterVO" property="obra.leyCamPla" size="50" maxlength="255"/></td>
			</tr>
		
		</table>
	</fieldset>
	
	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<td align="right">
				<logic:equal name="encObraAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="encObraAdapterVO" property="act" value="agregar">
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
<!-- Fin Formulario -->