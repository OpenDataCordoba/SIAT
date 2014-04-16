<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/BuscarOrdenControlContr.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	<script>
		function mostrarCampos(){
			var tipo=document.getElementById('tipo');
			var botones = document.getElementById('botones');
			var selectedIndex = tipo.selectedIndex;
			var valor = tipo[selectedIndex].value;
			var tipProc = document.getElementById('tipProc');
			var tipOC = document.getElementById('tipOC');
			var numeroProc=document.getElementById('numeroProc');
			var resultados = document.getElementById('resultadoFiltro');
			var persona = document.getElementById('persona');
			
			if (tipo == null){
				botones.style.display="none";
				persona.style.display="none";
			}
			
			
			tipOC.style.display="none";
			tipProc.style.display="none";
			numeroProc.style.display="none";
			persona.style.display="none";
			
			if (valor == 2){
				tipProc.style.display="table-row";
				numeroProc.style.display="table-row";
			}else if (valor == 3){
				tipOC.style.display="table-row";
			}else if (valor >= 4){
				persona.style.display="block";
			}
			
			if (selectedIndex > 0 && valor < 4){
				botones.style.display="block";
			}else{
				botones.style.display="none";
			}
		}
	</script>
		
	<h1><bean:message bundle="ef" key="ef.ordenControlContrSearchPage.title"/></h1>	
	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right">
	 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
			
	<!-- Filtro -->
	<fieldset>
	<legend><bean:message bundle="base" key="base.parametrosBusqueda"/></legend>
		<table class="tabladatos">
		<logic:notEqual name="ordenControlContrSearchPageVO" property="esManual" value="true">
			<tr>	
				<td><label><bean:message bundle="ef" key="ef.planFiscal.label"/>: </label></td>
				<td class="normal">
					<html:select name="ordenControlContrSearchPageVO" property="opeInvCon.opeInv.planFiscal.id" styleClass="select" onchange="submitForm('paramPlan','')">
						<html:optionsCollection name="ordenControlContrSearchPageVO" property="listPlanFiscal" label="desPlanFiscal" value="id" />
					</html:select>
				</td>					
			</tr>
			<tr>	
				<td><label><bean:message bundle="ef" key="ef.opeInv.label"/>: </label></td>
				<td class="normal">
					<html:select name="ordenControlContrSearchPageVO" property="opeInvCon.opeInv.id" styleClass="select">
						<html:optionsCollection name="ordenControlContrSearchPageVO" property="listOpeInv" label="desOpeInv" value="id" />
					</html:select>
				</td>					
			</tr>
		</logic:notEqual>
		<logic:equal name="ordenControlContrSearchPageVO" property="esManual" value="true">
			<tr>
				<td><label><bean:message bundle="ef" key="ef.origen.label"/></label>: </td>
				<td class="normal">
					<html:select name="ordenControlContrSearchPageVO" property="ordenControl.origenOrden.id" onchange="mostrarCampos();" styleId="tipo">
						<html:optionsCollection name="ordenControlContrSearchPageVO" property="listOrigenOrden" label="desOrigen" value="id"/>
					</html:select>
				</td>
			</tr>
			<tr id="tipProc" style="display: none">
				<td><label><bean:message bundle="ef" key="ef.ordenControlSearchPage.tipoProceso.label"/>: </label></td>
				<td class="normal">
					<html:select name="ordenControlContrSearchPageVO" property="ordenControl.procedimiento.tipoProceso.id">
						<html:optionsCollection name="ordenControlContrSearchPageVO" property="listTipoProceso" value="id" label="desTipoProceso"/>
					</html:select>
				</td>
			</tr>
			<tr id="numeroProc" style="display: none">
				<td><label><bean:message bundle="ef" key="ef.ordenControlSearchPage.numerProc.label"/>: </label></td>
				<td class="normal"><html:text name="ordenControlContrSearchPageVO" property="ordenControl.procedimiento.numeroView" size="10"/> / 
					<html:text name="ordenControlContrSearchPageVO" property="ordenControl.procedimiento.anioView" size="6"/>
				</td>
			</tr>
			<tr id="tipOC" style="display: none">
				<td><label><bean:message bundle="ef" key="ef.ordenControlContrSearchPage.ordenControl.numero.label"/>: </label></td>
				<td class="normal"><html:text name="ordenControlContrSearchPageVO" property="ordenControl.numeroOrdenView" size="10"/> / 
					<html:text name="ordenControlContrSearchPageVO" property="ordenControl.anioOrdenView" size="6"/>
				</td>
			</tr>
			
			
		</logic:equal>
			<!-- <#Filtros#> -->
		</table>
		
		<logic:notEqual name="ordenControlContrSearchPageVO" property="esManual" value="true">
			<p align="center">
			  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');">
					<bean:message bundle="base" key="abm.button.limpiar"/>
				</html:button>
				&nbsp;
			  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
					<bean:message bundle="base" key="abm.button.buscar"/>
				</html:button>
			</p>
		</logic:notEqual>
			
		<p align="center" id="botones" style="display: none">
		  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');">
				<bean:message bundle="base" key="abm.button.limpiar"/>
			</html:button>
			&nbsp;
		  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
				<bean:message bundle="base" key="abm.button.buscar"/>
			</html:button>
		</p>
		<p align="center" id="persona" style="display: none">					
					<button type="button" name="btnModificar" class="boton" onclick="submitForm('buscarPersona', '');">
							<bean:message bundle="ef" key="ef.opeInvConAdapter.button.buscarPersona"/>
					</button>
		</p>
	</fieldset>	
	
	
	
	
	<!-- Fin Filtro -->
		
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="ordenControlContrSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="ordenControlContrSearchPageVO" property="listResult">	
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
	               		<logic:notEqual name="ordenControlContrSearchPageVO" property="esManual" value="true">
			               	<tr>
								<th width="1"> <!-- Seleccionar -->
									<input type="checkbox" onclick="changeChk('filter', 'listIdOpeInvConSelected', this)"/>
								</th>
								<th width="1">&nbsp;</th> <!-- LiqDeuda -->
								<th width="1">&nbsp;</th> <!-- EstadoCuenta -->
								<th><bean:message bundle="pad" key="pad.persona.label"/></th> <!-- Contribuyente -->
								<th><bean:message bundle="ef" key="ef.estadoOpeInvCon.label"/></th>
								<th><bean:message bundle="ef" key="ef.opeInv.label"/>
								<!-- <#ColumnTitles#> -->
							</tr>
								
							<logic:iterate id="OpeInvConVO" name="ordenControlContrSearchPageVO" property="listResult">
								<tr>
									<!-- Seleccionar -->
										<td>
											<html:multibox name="ordenControlContrSearchPageVO" property="listIdOpeInvConSelected" >
		                    	            	<bean:write name="OpeInvConVO" property="idView"/>
		                        	        </html:multibox>
										</td>
									<!-- Liquidacion deuda -->
									<td>
										<logic:equal name="ordenControlContrSearchPageVO" property="liquidacionDeudaEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('liquidacionDeuda', '<bean:write name="OpeInvConVO" property="cuenta.id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="gde" key="gde.button.liquidacionDeuda"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/liquidacionDeuda0.gif"/>
												</a>
										</logic:equal>
										<logic:notEqual name="ordenControlContrSearchPageVO" property="liquidacionDeudaEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/liquidacionDeuda1.gif"/>
										</logic:notEqual>
									</td>
									
									<!-- Estado cuenta -->								
									<td>
										<logic:equal name="ordenControlContrSearchPageVO" property="estadoCuentaEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('estadoCuenta', '<bean:write name="OpeInvConVO" property="cuenta.idView"/>');">
													<img title="<bean:message bundle="gde" key="gde.button.estadoCuenta"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/estadoCuenta1.gif"/>
												</a>										
										</logic:equal>
										<logic:notEqual name="ordenControlContrSearchPageVO" property="estadoCuentaEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/estadoCuenta1.gif"/>
										</logic:notEqual>
									</td>												
									<td><bean:write name="OpeInvConVO" property="contribuyente.persona.represent"/></td>
									<td><bean:write name="OpeInvConVO" property="estadoOpeInvCon.desEstadoOpeInvCon"/></td>		
									<td><bean:write name="OpeInvConVO" property="opeInv.desOpeInv"/></td>						
								</tr>
							</logic:iterate>
					
							<tr>
								<td colspan="2" align="right"><label>(*)&nbsp;<bean:message bundle="ef" key="ef.tipoOrden.label"/>: </label></td>
								<td class="normal" colspan="2" align="left">
									<html:select name="ordenControlContrSearchPageVO" property="idTipoOrdenSelected" styleClass="select">
										<html:optionsCollection name="ordenControlContrSearchPageVO" property="listTipoOrdenVO" label="desTipoOrden" value="id" />
									</html:select>
								</td>					
								<td colspan="1" align="right"><label>(*)&nbsp;<bean:message bundle="ef" key="ef.tipoPeriodo.label"/>: </label></td>
								<td class="normal" colspan="1" align="left">
									<html:select name="ordenControlContrSearchPageVO" property="idTipoPeriodoSelected" styleClass="select">
										<html:optionsCollection name="ordenControlContrSearchPageVO" property="listTipoPeriodoVO" label="desTipoPeriodo" value="id" />
									</html:select>
								</td>					
							</tr>
						</logic:notEqual>
						<logic:equal name="ordenControlContrSearchPageVO" property="ordenControl.origenOrden.id" value="2">
							<tr>
								<th width="1">&nbsp; </th>
								<th><bean:message bundle="ef" key="ef.ordenControlSearchPage.procedimiento.numero.label"/></th>
								<th><bean:message bundle="ef" key="ef.ordenControlSearchPage.procedimiento.anio.label"/></th>
								<th><bean:message bundle="ef" key="ef.ordenControlSearchPage.procedimiento.contribuyente.label"/></th>
								<th><bean:message bundle="ef" key="ef.ordenControlSearchPage.procedimiento.fechaAuto.label"/></th>
								<th><bean:message bundle="ef" key="ef.ordenControlSearchPage.procedimiento.fechaVerificacion.label"/></th>
							</tr>
							<logic:iterate id="ProcedimientoVO" name="ordenControlContrSearchPageVO" property="listResult">
								<tr>
									<td>
										<!-- Seleccionar -->
										<html:multibox name="ordenControlContrSearchPageVO" property="listIdOpeInvConSelected" >
		                    	           	<bean:write name="ProcedimientoVO" property="idView"/>
		                        	    </html:multibox>
									</td>
									<td>
										<bean:write name="ProcedimientoVO" property="numeroView"/>
									</td>
									<td>
										<bean:write name="ProcedimientoVO" property="anioView"/>
									</td>
									<td>
										<bean:write name="ProcedimientoVO" property="desContribuyente"/>
									</td>
									<td>
										<bean:write name="ProcedimientoVO" property="fechaAutoView"/>
									</td>
									<td>
										<bean:write name="ProcedimientoVO" property="fechaVerOpoView"/>
									</td>	
								</tr>
							</logic:iterate>
							<tr>
								<td colspan="2" align="right"><label>(*)&nbsp;<bean:message bundle="ef" key="ef.tipoOrden.label"/>: </label></td>
								<td class="normal" colspan="2" align="left">
									<html:select name="ordenControlContrSearchPageVO" property="idTipoOrdenSelected" styleClass="select">
										<html:optionsCollection name="ordenControlContrSearchPageVO" property="listTipoOrdenVO" label="desTipoOrden" value="id" />
									</html:select>
								</td>					
								<td colspan="1" align="right"><label>(*)&nbsp;<bean:message bundle="ef" key="ef.tipoPeriodo.label"/>: </label></td>
								<td class="normal" colspan="1" align="left">
									<html:select name="ordenControlContrSearchPageVO" property="idTipoPeriodoSelected" styleClass="select">
										<html:optionsCollection name="ordenControlContrSearchPageVO" property="listTipoPeriodoVO" label="desTipoPeriodo" value="id" />
									</html:select>
								</td>					
							</tr>
							
							
						</logic:equal>
						
						
						<logic:equal name="ordenControlContrSearchPageVO" property="ordenControl.origenOrden.id" value="3">
							<tr>
								<th width="1">&nbsp; </th>
								<th><bean:message bundle="ef" key="ef.ordenControlSearchPage.ordenControl.numero.label"/></th>
								<th><bean:message bundle="ef" key="ef.ordenControlSearchPage.ordenControl.anio.label"/></th>
								<th><bean:message bundle="ef" key="ef.ordenControlSearchPage.ordenControl.contribuyente.label"/></th>
								<th><bean:message bundle="ef" key="ef.ordenControlSearchPage.ordenControl.tipo.label"/></th>
								<th><bean:message bundle="ef" key="ef.ordenControlSearchPage.ordenControl.inspector.label"/></th>
							</tr>
							<logic:iterate id="OrdenControlVO" name="ordenControlContrSearchPageVO" property="listResult">
								<tr>
									<td>
										<!-- Seleccionar -->
										<html:multibox name="ordenControlContrSearchPageVO" property="listIdOpeInvConSelected" >
		                    	           	<bean:write name="OrdenControlVO" property="idView"/>
		                        	    </html:multibox>
									</td>
									<td>
										<bean:write name="OrdenControlVO" property="numeroOrdenView"/>
									</td>
									<td>
										<bean:write name="OrdenControlVO" property="anioOrdenView"/>
									</td>
									<td>
										<bean:write name="OrdenControlVO" property="contribuyente.persona.represent"/>
									</td>
									<td>
										<bean:write name="OrdenControlVO" property="tipoOrden.desTipoOrden"/>
									</td>
									<td>
										<bean:write name="OrdenControlVO" property="inspector.desInspector"/>
									</td>	
								</tr>
							</logic:iterate>
							<tr>
								<td colspan="2" align="right"><label>(*)&nbsp;<bean:message bundle="ef" key="ef.tipoOrden.label"/>: </label></td>
								<td class="normal" colspan="2" align="left">
									<html:select name="ordenControlContrSearchPageVO" property="idTipoOrdenSelected" styleClass="select">
										<html:optionsCollection name="ordenControlContrSearchPageVO" property="listTipoOrdenVO" label="desTipoOrden" value="id" />
									</html:select>
								</td>					
								<td colspan="1" align="right"><label>(*)&nbsp;<bean:message bundle="ef" key="ef.tipoPeriodo.label"/>: </label></td>
								<td class="normal" colspan="1" align="left">
									<html:select name="ordenControlContrSearchPageVO" property="idTipoPeriodoSelected" styleClass="select">
										<html:optionsCollection name="ordenControlContrSearchPageVO" property="listTipoPeriodoVO" label="desTipoPeriodo" value="id" />
									</html:select>
								</td>					
							</tr>
							
						</logic:equal>
						
						<logic:greaterThan name="ordenControlContrSearchPageVO" property="ordenControl.origenOrden.id" value="3">
							<tr>
								<th width="1">&nbsp; </th>
								<th><bean:message bundle="pad" key="pad.persona.label"/></th>
								<th><bean:message bundle="pad" key="pad.persona.esContribuyente.label"/></th>
								<th><bean:message bundle="pad" key="pad.altaOficio.contribuyente.nroIsib.label"/></th>
								<th><bean:message bundle="pad" key="pad.persona.cuit.label"/></th>
								<th><bean:message bundle="pad" key="pad.persona.domicilio.label"/>
							</tr>
							<logic:iterate id="ContribuyenteVO" name="ordenControlContrSearchPageVO" property="listResult">
								<tr>
									<td>
										<!-- Seleccionar -->
										<html:multibox name="ordenControlContrSearchPageVO" property="listIdOpeInvConSelected" >
		                    	           	<bean:write name="ContribuyenteVO" property="idView"/>
		                        	    </html:multibox>
									</td>
									<td>
										<bean:write name="ContribuyenteVO" property="persona.represent"/>
									</td>
									<td>
										<bean:write name="ContribuyenteVO" property="persona.esContribuyenteView"/>
									</td>
									<td>
										<bean:write name="ContribuyenteVO" property="nroIsib"/>
									</td>
									<td>
										<bean:write name="ContribuyenteVO" property="persona.cuit"/>
									</td>	
									<td>
										<bean:write name="ContribuyenteVO" property="persona.domicilio.view"/>
									</td>	
								</tr>
							</logic:iterate>
							<tr>
								<td colspan="2" align="right"><label>(*)&nbsp;<bean:message bundle="ef" key="ef.tipoOrden.label"/>: </label></td>
								<td class="normal" colspan="2" align="left">
									<html:select name="ordenControlContrSearchPageVO" property="idTipoOrdenSelected" styleClass="select">
										<html:optionsCollection name="ordenControlContrSearchPageVO" property="listTipoOrdenVO" label="desTipoOrden" value="id" />
									</html:select>
								</td>					
								<td colspan="1" align="right"><label>(*)&nbsp;<bean:message bundle="ef" key="ef.tipoPeriodo.label"/>: </label></td>
								<td class="normal" colspan="1" align="left">
									<html:select name="ordenControlContrSearchPageVO" property="idTipoPeriodoSelected" styleClass="select">
										<html:optionsCollection name="ordenControlContrSearchPageVO" property="listTipoPeriodoVO" label="desTipoPeriodo" value="id" />
									</html:select>
								</td>					
							</tr>
							
							
						</logic:greaterThan>
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="ordenControlContrSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="ordenControlContrSearchPageVO" property="listResult">
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
			<td align="left" width="50%">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<logic:equal name="ordenControlContrSearchPageVO" property="viewResult" value="true">
  	    		<td align="right" width="50%">
  	    			<logic:notEmpty name="ordenControlContrSearchPageVO" property="listResult">
						<bean:define id="emitirEnabled" name="ordenControlContrSearchPageVO" property="emitirEnabled"/>
						<input type="button" <%=emitirEnabled%> class="boton" 
							onClick="submitForm('emitir', '0');" 
							value="<bean:message bundle="ef" key="ef.ordenControlContrSearchPage.button.emitir"/>"/>
  	    			</logic:notEmpty>  	    			
				</td>
			</logic:equal>			
		</tr>
	</table>
	<script>
		function mostrarCampos();
	</script>
		
	<input type="hidden" name="method" value=""/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>
	<script>
		mostrarCampos();
	</script>
	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
<!-- ordenControlContrSearchPage.jsp -->
