<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript">

function mostrarOcultar(blo) {

	var trs	 = document.getElementsByTagName('tr')
	var primTd = 1;
	var contTr = 0;
	var tdSpan = null;		

	for(i=0; i < trs.length; i++) {
		var tr = trs[i];
		
		if (tr.id != null){

			var id = tr.id;
			var idO = id.split("#")[0];

			if (idO == blo) {
				if (tr.style.display == '') {
					tr.style.display = 'none';
				} else {
					tr.style.display = '';
					contTr = contTr + 1;
					if (primTd == 1) {
						tdSpan = document.getElementById("td" + id );
						primTd = primTd +1;
					} else {
					
						var eleElim = document.getElementById("td" + id);
						
						if (eleElim != null) {
							tr.removeChild(eleElim);
						}

					}
				}
			} 
		}
	}

	if (tdSpan != null) {
		tdSpan.setAttribute('rowspan', contTr) ;
	}

}

function ocultar() {

	// recupero todos los trs
	var trs	 = document.getElementsByTagName('tr')

	for(i=0; i < trs.length; i++) {
		var tr = trs[i];
		
		if (tr.id != null && tr.id != ""){
		
			var id = tr.id;
			var cont = id.indexOf("#");

			if (cont > -1 ) {
				tr.style.display= 'none';
			} 
		}
	}
}

</script>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rec/BuscarPlaCuaDet.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="rec" key="rec.plaCuaDetSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="plaCuaDetSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="rec" key="rec.plaCuaDet.label"/>
					</logic:equal>
					<logic:notEqual name="plaCuaDetSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="rec" key="rec.plaCuaDetSearchPage.legend"/>
					</logic:notEqual>		
				</p>

			</td>				
			<td align="right">
	 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>


	<!-- PlanillaCuadra -->
	<fieldset>
		<legend><bean:message bundle="rec" key="rec.planillaCuadra.title"/></legend>
		<table class="tabladatos">
			<!-- Nro planilla -->			
			<tr>
				<td><label><bean:message bundle="rec" key="rec.planillaCuadra.numeroPlanilla.label"/>: </label></td>
				<td class="normal"><bean:write name="plaCuaDetSearchPageVO" property="plaCuaDet.planillaCuadra.idView"/></td>
			</tr>
			
			<!-- Recurso -->
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal"><bean:write name="plaCuaDetSearchPageVO" property="plaCuaDet.planillaCuadra.recurso.desRecurso"/></td>
			</tr>
			<!-- Contrato -->
			<tr>	
				<td><label><bean:message bundle="rec" key="rec.contrato.label"/>: </label></td>
				<td class="normal"><bean:write name="plaCuaDetSearchPageVO" property="plaCuaDet.planillaCuadra.contrato.descripcion"/></td>				
			</tr>
			<!-- TipoObra -->
			<tr>	
				<td><label><bean:message bundle="rec" key="rec.tipoObra.label"/>: </label></td>
				<td class="normal"><bean:write name="plaCuaDetSearchPageVO" property="plaCuaDet.planillaCuadra.tipoObra.desTipoObra"/></td>				
			</tr>
			<tr>
				<!-- Fecha -->
				<td><label><bean:message bundle="rec" key="rec.planillaCuadra.fechaCarga.label"/>: </label></td>
				<td class="normal"><bean:write name="plaCuaDetSearchPageVO" property="plaCuaDet.planillaCuadra.fechaCargaView"/></td>				
			</tr>
			<tr>
				<!-- Descripcion -->
				<td><label><bean:message bundle="rec" key="rec.planillaCuadra.descripcion.label"/>: </label></td>
				<td class="normal"><bean:write name="plaCuaDetSearchPageVO" property="plaCuaDet.planillaCuadra.descripcion"/></td>				
			</tr>
			<tr>
				<!-- costo cuadra -->
				<td><label><bean:message bundle="rec" key="rec.planillaCuadra.costoCuadra.label"/>: </label></td>
				<td class="normal"><bean:write name="plaCuaDetSearchPageVO" property="plaCuaDet.planillaCuadra.costoCuadraView"/></td>				
			</tr>

			<tr>
				<!-- Calle Principal -->
				<td><label><bean:message bundle="rec" key="rec.planillaCuadra.callePpal.label"/>: </label></td>
				<td class="normal"><bean:write name="plaCuaDetSearchPageVO" property="plaCuaDet.planillaCuadra.callePpal.nombreCalle"/></td>
			</tr>
			<tr>
				<!-- Calle Desde -->
				<td><label><bean:message bundle="rec" key="rec.planillaCuadra.calleDesde.label"/>: </label></td>
				<td class="normal"><bean:write name="plaCuaDetSearchPageVO" property="plaCuaDet.planillaCuadra.calleDesde.nombreCalle"/></td>				
			</tr>
			<tr>
				<!-- Calle Hasta -->
				<td><label><bean:message bundle="rec" key="rec.planillaCuadra.calleHasta.label"/>: </label></td>
				<td class="normal"><bean:write name="plaCuaDetSearchPageVO" property="plaCuaDet.planillaCuadra.calleHasta.nombreCalle"/></td>				
			</tr>
			<tr>
				<!-- Observacion -->
				<td><label><bean:message bundle="rec" key="rec.planillaCuadra.observacion.label"/>: </label></td>
				<td class="normal"><bean:write name="plaCuaDetSearchPageVO" property="plaCuaDet.planillaCuadra.observacion"/></td>				
			</tr>
		</table>
	</fieldset>	
	<!-- PlanillaCuadra -->
		
	<!-- Filtro -->
	<fieldset>
	<legend><bean:message bundle="base" key="base.parametrosBusqueda"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="rec" key="rec.planillaCuadra.manzana1.label"/>: </label></td>
				<td class="normal"><html:text name="plaCuaDetSearchPageVO" property="plaCuaDet.planillaCuadra.manzana1" size="15" maxlength="20" styleClass="datos" /></td>
			
				<td><label><bean:message bundle="rec" key="rec.planillaCuadra.manzana2.label"/>: </label></td>
				<td class="normal"><html:text name="plaCuaDetSearchPageVO" property="plaCuaDet.planillaCuadra.manzana2" size="15" maxlength="20" styleClass="datos" /></td>
			</tr>
		</table>
			
		<p align="center">
		  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');">
				<bean:message bundle="base" key="abm.button.limpiar"/>
			</html:button>
			&nbsp;
		  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
				<bean:message bundle="base" key="abm.button.buscar"/>
			</html:button>
		</p>
	</fieldset>	
	<!-- Fin Filtro -->
		
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="plaCuaDetSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="plaCuaDetSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<th align="left"><bean:message bundle="rec" key="rec.plaCuaDet.tipPlaCuaDet.label"/></th>							
							<th align="left"><bean:message bundle="rec" key="rec.plaCuaDet.catastral.label"/></th>
							<th align="left"><bean:message bundle="rec" key="rec.plaCuaDet.cuentaTGI.label"/></th>							
							<th align="left"><bean:message bundle="rec" key="rec.plaCuaDet.porcPH.label"/></th>
							<th align="left"><bean:message bundle="rec" key="rec.plaCuaDet.ubicacion.label"/></th>
						</tr>
						<logic:iterate id="PlaCuaDetVO" name="plaCuaDetSearchPageVO" property="listResult" indexId="count">
						
								<tr>
									<td>
										<logic:equal name="PlaCuaDetVO" property="seleccionarBussEnabled" value="true">
											<html:multibox name="plaCuaDetSearchPageVO"  property="listId">
		                                		<bean:write name="PlaCuaDetVO" property="idSelect"/>	
											</html:multibox> 
										</logic:equal>
										<logic:notEqual name="PlaCuaDetVO" property="seleccionarBussEnabled" value="true">
				  							<input type="checkbox" disabled="disabled"/>
						  				</logic:notEqual>
									</td>
									<td>
										<logic:equal name="PlaCuaDetVO" property="isCarpeta" value="true">
											<a style="cursor: pointer; cursor: hand;" onclick="mostrarOcultar('<bean:write name="PlaCuaDetVO" property="idSelect"/>');">
												<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<bean:write name="PlaCuaDetVO" property="tipPlaCuaDetView"/>
									</td>
									<td><bean:write name="PlaCuaDetVO" property="cuentaTGI.objImp.claveFuncional"/></td>
									<logic:equal name="PlaCuaDetVO" property="seleccionarBussEnabled" value="true">
										<td><bean:write name="PlaCuaDetVO" property="cuentaTGI.numeroCuenta"/></td>
									</logic:equal>
									<logic:notEqual name="PlaCuaDetVO" property="seleccionarBussEnabled" value="true">
										<td><font color="red"><bean:write name="PlaCuaDetVO" property="cuentaTGI.numeroCuenta"/></td>
					  				</logic:notEqual>
									<td><bean:write name="PlaCuaDetVO" property="porcPHView"/></td>	
									<td><bean:write name="PlaCuaDetVO" property="ubicacionFinca"/></td>								
								</tr>
								<!--  Itero la lista de hijos -->
								<logic:iterate id="PlaCuaDetHijoVO" name="PlaCuaDetVO" property="listPlaCuaDet" indexId="countDet">
									<tr id="<bean:write name="PlaCuaDetVO" property="idSelect"/>#<%= countDet %>">
										<td id="td<bean:write name="PlaCuaDetVO" property="idSelect"/>#<%= countDet %>" colspan="2">&nbsp;</td>
										<td><bean:write name="PlaCuaDetHijoVO" property="cuentaTGI.objImp.claveFuncional"/>&nbsp;</td>
										<logic:equal name="PlaCuaDetHijoVO" property="seleccionarBussEnabled" value="true">
											<td><bean:write name="PlaCuaDetHijoVO" property="cuentaTGI.numeroCuenta"/>&nbsp;</td>
										</logic:equal>
										<logic:notEqual name="PlaCuaDetHijoVO" property="seleccionarBussEnabled" value="true">
											<td><font color="red"><bean:write name="PlaCuaDetHijoVO" property="cuentaTGI.numeroCuenta"/>&nbsp;</td>
						  				</logic:notEqual>
										<td><bean:write name="PlaCuaDetHijoVO" property="porcPHView"/>&nbsp;</td>	
										<td><bean:write name="PlaCuaDetHijoVO" property="ubicacionFinca"/>&nbsp;</td>
									</tr>
								</logic:iterate>
						</logic:iterate>
					</tbody>
				</table>
			<script> ocultar();</script>
			</logic:notEmpty>
			
			<logic:empty name="plaCuaDetSearchPageVO" property="listResult">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
                	<tbody>
						<tr><td align="center">
							<bean:message bundle="base" key="base.resultadoVacio"/>
						</td></tr>
					</tbody>			
				</table>
			</logic:empty>
		</logic:equal>			
	</div>
	<!-- Fin Resultado Filtro -->

	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<logic:equal name="plaCuaDetSearchPageVO" property="viewResult" value="true">
				<td align="right">
					<bean:define id="agregarEnabled" name="plaCuaDetSearchPageVO" property="agregarEnabled"/>
					<input type="button" <%=agregarEnabled%> class="boton" 
						onClick="submitForm('agregar', '0');" 
						value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
				</td>				
			</logic:equal>
		</tr>
	</table>
		
	<input type="hidden" name="method" value=""/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->