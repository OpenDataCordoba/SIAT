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
<html:form styleId="filter" action="/rec/AdministrarPlanillaCuadra.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="rec" key="rec.planillaCuadraAdapter.title"/></h1>

	<table class="tablabotones" width="100%">
		<tr>			
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
				<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.idView"/></td>
			</tr>
			<!-- Recurso -->
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.recurso.desRecurso"/></td>
			</tr>
			<!-- Contrato -->
			<tr>	
				<td><label><bean:message bundle="rec" key="rec.contrato.label"/>: </label></td>
				<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.contrato.descripcion"/></td>				
			</tr>
			<!-- TipoObra -->
			<tr>	
				<td><label><bean:message bundle="rec" key="rec.tipoObra.label"/>: </label></td>
				<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.tipoObra.desTipoObra"/></td>				
			</tr>
			<!-- Fecha de Ejecucion -->
			<tr>
				<td><label><bean:message bundle="rec" key="rec.planillaCuadra.fechaCarga.label"/>: </label></td>
				<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.fechaCargaView"/></td>				
			</tr>
			<tr>
				<!-- Descripcion -->
				<td><label><bean:message bundle="rec" key="rec.planillaCuadra.descripcion.label"/>: </label></td>
				<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.descripcion"/></td>				
			</tr>
			<tr>
				<!-- costo cuadra -->
				<td><label><bean:message bundle="rec" key="rec.planillaCuadra.costoCuadra.label"/>: </label></td>
				<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.costoCuadraView"/></td>				
			</tr>
	
			<tr>
				<!-- Calle Principal -->
				<td><label><bean:message bundle="rec" key="rec.planillaCuadra.callePpal.label"/>: </label></td>
				<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.callePpal.nombreCalle"/></td>
			</tr>
			<tr>
				<!-- Calle Desde -->
				<td><label><bean:message bundle="rec" key="rec.planillaCuadra.calleDesde.label"/>: </label></td>
				<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.calleDesde.nombreCalle"/></td>				
			</tr>
			<tr>
				<!-- Calle Hasta -->
				<td><label><bean:message bundle="rec" key="rec.planillaCuadra.calleHasta.label"/>: </label></td>
				<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.calleHasta.nombreCalle"/></td>				
			</tr>
			<tr>
				<!-- Observacion -->
				<td><label><bean:message bundle="rec" key="rec.planillaCuadra.observacion.label"/>: </label></td>
				<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.observacion"/></td>				
			</tr>

			<tr>
				<!-- Estado -->
				<td><label><bean:message bundle="rec" key="rec.estPlaCua.label"/>: </label></td>
				<td class="normal"><bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.estPlaCua.desEstPlaCua"/></td>				
			</tr>
		</table>
	</fieldset>	
	<!-- PlanillaCuadra -->
	
	<!-- PlaCuaDet -->		
	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
		<caption><bean:message bundle="rec" key="rec.planillaCuadra.listPlaCuaDet.label"/></caption>
	   	<tbody>
			<logic:notEmpty  name="planillaCuadraAdapterVO" property="planillaCuadra.listPlaCuaDetAgrupados">	    	
		    	<tr>
					<th width="1">&nbsp;</th> <!-- Ver -->
					<th width="1">&nbsp;</th> <!-- Modificar -->
					<th width="1">&nbsp;</th> <!-- Eliminar -->
					<th align="left"><bean:message bundle="rec" key="rec.plaCuaDet.tipPlaCuaDet.label"/></th>						
					<th align="left"><bean:message bundle="rec" key="rec.plaCuaDet.catastral.label"/></th>
					<th align="left"><bean:message bundle="rec" key="rec.plaCuaDet.cuentaTGI.label"/></th>							
					<th align="left"><bean:message bundle="rec" key="rec.plaCuaDet.ubicacion.label"/></th>
					<th align="left"><bean:message bundle="rec" key="rec.plaCuaDet.porcPH.label"/></th>					
					<th align="left"><bean:message bundle="rec" key="rec.plaCuaDet.valuacionTerreno.label"/></th>
					<th align="left"><bean:message bundle="rec" key="rec.plaCuaDet.estado.label"/></th>
				</tr>

				<logic:iterate id="PlaCuaDetVO" name="planillaCuadraAdapterVO" property="planillaCuadra.listPlaCuaDetAgrupados" indexId="count">
					<tr>
						<!-- Ver -->
						<td>
							<logic:equal name="planillaCuadraAdapterVO" property="verPlaCuaDetEnabled" value="enabled">
								<logic:equal name="PlaCuaDetVO" property="verEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verPlaCuaDet', '<bean:write name="PlaCuaDetVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:equal>	
								<logic:notEqual name="PlaCuaDetVO" property="verEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="planillaCuadraAdapterVO" property="verPlaCuaDetEnabled" value="enabled">										
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
							</logic:notEqual>
						</td>
						
						<!-- Modificar-->								
						<td>
							<logic:equal name="planillaCuadraAdapterVO" property="modificarPlaCuaDetEnabled" value="enabled">
								<logic:equal name="PlaCuaDetVO" property="modificarEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarPlaCuaDet', '<bean:write name="PlaCuaDetVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
									</a>
								</logic:equal>	
								<logic:notEqual name="PlaCuaDetVO" property="modificarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="planillaCuadraAdapterVO" property="modificarPlaCuaDetEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
							</logic:notEqual>
						</td>
						
						<!-- Eliminar-->								
						<td>
							<logic:equal name="planillaCuadraAdapterVO" property="eliminarPlaCuaDetEnabled" value="enabled">
								<logic:equal name="PlaCuaDetVO" property="eliminarEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarPlaCuaDet', '<bean:write name="PlaCuaDetVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
									</a>
								</logic:equal>	
								<logic:notEqual name="PlaCuaDetVO" property="eliminarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="planillaCuadraAdapterVO" property="eliminarPlaCuaDetEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
							</logic:notEqual>
						</td>
							<!-- Mostrar/Ocultar -->
						<td>
							<logic:equal name="PlaCuaDetVO" property="isCarpeta" value="true">
								<a style="cursor: pointer; cursor: hand;" onclick="mostrarOcultar('<bean:write name="PlaCuaDetVO" property="idSelect"/>');">
									<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
								</a>
							</logic:equal>
							<bean:write name="PlaCuaDetVO" property="tipPlaCuaDetView"/>
						</td>
						<td><bean:write name="PlaCuaDetVO" property="cuentaTGI.objImp.claveFuncional"/>&nbsp;</td>
						<td><bean:write name="PlaCuaDetVO" property="cuentaTGI.numeroCuenta"/>&nbsp;</td>
						<td><bean:write name="PlaCuaDetVO" property="ubicacionFinca"/>&nbsp;</td>
						<td><bean:write name="PlaCuaDetVO" property="porcPHView"/>&nbsp;</td>		
						<td><bean:write name="PlaCuaDetVO" property="valuacionTerrenoView"/>&nbsp;</td>
						<td><bean:write name="PlaCuaDetVO" property="estPlaCuaDet.desEstPlaCuaDet"/>&nbsp;</td>							
					</tr>
					<!--  Itero la lista de hijos -->
					<logic:iterate id="PlaCuaDetHijoVO" name="PlaCuaDetVO" property="listPlaCuaDet" indexId="countDet">
						<tr id="<bean:write name="PlaCuaDetVO" property="idSelect"/>#<%= countDet %>">
							<td id="td<bean:write name="PlaCuaDetVO" property="idSelect"/>#<%= countDet %>" colspan="4">&nbsp;</td>
							<td><bean:write name="PlaCuaDetHijoVO" property="cuentaTGI.objImp.claveFuncional"/>&nbsp;</td>
							<td><bean:write name="PlaCuaDetHijoVO" property="cuentaTGI.numeroCuenta"/>&nbsp;</td>
							<td><bean:write name="PlaCuaDetHijoVO" property="ubicacionFinca"/>&nbsp;</td>
							<td><bean:write name="PlaCuaDetHijoVO" property="porcPHView"/>&nbsp;</td>	
							<td><bean:write name="PlaCuaDetHijoVO" property="valuacionTerrenoView"/>&nbsp;</td>
							<td><bean:write name="PlaCuaDetHijoVO" property="estPlaCuaDet.desEstPlaCuaDet"/>&nbsp;</td>
						</tr>
					</logic:iterate>
							
					</tr>
				</logic:iterate>
				<script> ocultar();</script>
			</logic:notEmpty>
			<logic:empty  name="planillaCuadraAdapterVO" property="planillaCuadra.listPlaCuaDet">
				<tr><td align="center">
				<bean:message bundle="base" key="base.noExistenRegitros"/>
				</td></tr>
			</logic:empty>
			<tr>
				<td align="right" colspan="14">
  	    				<bean:define id="agregarEnabled" name="planillaCuadraAdapterVO" property="agregarPlaCuaDetEnabled"/>
					<input type="button" <%=agregarEnabled%> class="boton" 
						onClick="submitForm('agregarPlaCuaDet', '<bean:write name="planillaCuadraAdapterVO" property="planillaCuadra.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="base" key="abm.button.agregar"/>"
					/>
				</td>
			</tr>
		</tbody>
	</table>
	<!-- PlaCuaDet -->
			
	<table class="tablabotones">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>   	    			
		</tr>
	</table>
	 
	   	
	   <input type="hidden" name="method" value=""/>
       <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
       <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	   <input type="hidden" name="selectedId" value=""/>
	   <input type="hidden" name="isSubmittedForm" value="true"/>
	
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->