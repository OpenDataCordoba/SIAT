<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarPeriodoOrden.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="ef" key="ef.periodoOrdenEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- PeriodoOrden -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.periodoOrden.title"/></legend>
		
		<table class="tabladatos">
		
			<!-- cuentas -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/></label></td>
				<td class="normal" colspan="3" align="left">
					
					<!-- se muestran las cuentas agrupadas por recurso, pero se submite el id de ordConCue -->
					<html:select name="periodoOrdenAdapterVO" property="periodoOrden.ordConCue.id" styleClass="select" >
						<% String idCategoriaAnt = "";%>
						<bean:define id="listOrdConCue" name="periodoOrdenAdapterVO" property="listOrdConCueOrderByRec"/>
						<logic:notEmpty name="listOrdConCue">
							<logic:iterate id="ordConCue" name="listOrdConCue">
								
								<bean:define id="recurso" name="ordConCue" property="cuenta.recurso"/>
								
								<logic:equal name="ordConCue" property="id" value="-1">
									<option value='<bean:write name="ordConCue" property="id" bundle="base" formatKey="general.format.id"/>'>
										Seleccionar...
									</option>
								</logic:equal>
								<logic:notEqual name="recurso" property="id" value="-1">
									<logic:notEqual name="recurso" property="categoria.id" value="<%=idCategoriaAnt%>">
										<optgroup label='<bean:write name="recurso" property="categoria.desCategoria"/>'>
										<bean:define id="catId" name="recurso" property="categoria.id"/>
										<% idCategoriaAnt = ""+catId+"";%>
									</logic:notEqual>
									<logic:equal name="recurso" property="categoria.id" value="<%=idCategoriaAnt%>">
										<bean:define id="ordConCueId" name="periodoOrdenAdapterVO" property="periodoOrden.ordConCue.id"/>
										<% String idOrdConCue = ""+ordConCueId+"";%>				
											<option value='<bean:write name="ordConCue" property="id" bundle="base" formatKey="general.format.id"/>'												 					
												<logic:equal name="ordConCue" property="id" value="<%=idOrdConCue%>">
													selected="selected"											
												</logic:equal>
											>
											<bean:write name="ordConCue" property="cuenta.numeroCuenta"/>
										</option>
									</logic:equal>
									<logic:notEqual name="recurso" property="categoria.id" value="<%=idCategoriaAnt%>">	
										</optgroup>
									</logic:notEqual>
								</logic:notEqual>
							</logic:iterate>
						</logic:notEmpty>
					</html:select>	

				
				</td>
			</tr>
			
			
			
			<!-- periodoDesde -->
			<tr>
				<td><LABEL><bean:message bundle="ef" key="ef.periodoOrdenEditAdapter.periodoDesde.label"/></LABEL></td>
				<td class="normal">
					<html:text name="periodoOrdenAdapterVO" property="periodoOrden.periodo" size="3" maxlength="2"/>/
					<html:text name="periodoOrdenAdapterVO" property="periodoOrden.anio"  size="5" maxlength="4"/>
					(mm/aaaa)
				</td>		
			
			<!-- periodoHasta -->	
				<td><LABEL><bean:message bundle="ef" key="ef.periodoOrdenEditAdapter.periodoHasta.label"/></LABEL></td>
				<td class="normal">
					<html:text name="periodoOrdenAdapterVO" property="periodoHasta"  size="3" maxlength="2"/>/
					<html:text name="periodoOrdenAdapterVO" property="anioHasta"  size="5" maxlength="4"/>
					(mm/aaaa)
				</td>
			</tr>					
			<!-- <#Campos#> -->
		</table>
		
		<p align="center">
		  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
				<bean:message bundle="base" key="abm.button.buscar"/>
			</html:button>		
		</p>		
	</fieldset>	
	<!-- PeriodoOrden -->
	
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="periodoOrdenAdapterVO" property="viewResult" value="true">
			<logic:notEmpty  name="periodoOrdenAdapterVO" property="mapAgrupPeriodoDeuda">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1"> <!-- Seleccionar -->
								<input type="checkbox" onclick="changeChk('filter', 'idsSelected', this)"/>
							</th>
							<th><bean:message bundle="ef" key="ef.periodoOrden.periodo.label"/></th>
							<th><bean:message bundle="ef" key="ef.periodoOrdenEditAdapter.deudaImporte.label"/></th>
							<th><bean:message bundle="ef" key="ef.periodoOrdenEditAdapter.deudaAct.label"/></th>
							<th><bean:message bundle="ef" key="ef.periodoOrdenEditAdapter.deudaTotal.label"/></th>						
							<!-- <#ColumnTitles#> -->
						</tr>
							
						<logic:iterate id="Periodo" name="periodoOrdenAdapterVO" property="mapAgrupPeriodoDeuda">
							<tr>
								<td>
									<html:multibox name="periodoOrdenAdapterVO" property="idsSelected">
										<bean:write name="Periodo" property="value[3]" bundle="base" formatKey="general.format.id"/>
									</html:multibox>
								</td>
																
								<!-- periodo/anio deuda -->
								<td><bean:write name="Periodo" property="key"/></td>
								
								<td><bean:write name="Periodo" property="value[0]" bundle="base" formatKey="general.format.currency"/></td>
								<td><bean:write name="Periodo" property="value[1]" bundle="base" formatKey="general.format.currency"/></td>
								<td><bean:write name="Periodo" property="value[2]" bundle="base" formatKey="general.format.currency"/></td>
							</tr>
							
						</logic:iterate>
																
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="periodoOrdenAdapterVO" property="mapAgrupPeriodoDeuda">
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
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="periodoOrdenAdapterVO" property="agregarBussEnabled" value="true">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
					</html:button>
				</logic:equal>
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
<!-- periodoOrdenEditAdapter.jsp -->