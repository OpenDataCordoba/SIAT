<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/BuscarTranBal.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.procesarBalanceAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p><bean:message bundle="bal" key="bal.tranBalSearchPage.legend"/></p>		
				</td>				
				<td align="right">
		 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>

		<!-- Datos del Balance -->		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.balance.title"/></legend>			
			<table class="tabladatos" width="100%">
				<!-- Fecha Balance -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.balance.fechaBalance.label"/>: </label></td>
					<td class="normal"><bean:write name="tranBalSearchPageVO" property="tranBal.balance.fechaBalanceView"/></td>
				</tr>		
				<!-- Ejercicio -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.ejercicio.label"/>: </label></td>
					<td class="normal"><bean:write name="tranBalSearchPageVO" property="tranBal.balance.ejercicio.desEjercicio"/></td>
				</tr>	
				<!-- Fecha Desde y Hasta -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.balance.fechaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="tranBalSearchPageVO" property="tranBal.balance.fechaDesdeView"/></td>
					<td><label><bean:message bundle="bal" key="bal.balance.fechaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="tranBalSearchPageVO" property="tranBal.balance.fechaHastaView"/></td>
				</tr>			
				<!-- Observacion-->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.balance.observacion.label"/>: </label></td>
					<td colspan="3" class="normal"><bean:write name="tranBalSearchPageVO" property="tranBal.balance.observacion"/></td>					
				</tr>
			</table>
		</fieldset>
		<!-- Fin Datos del Balance -->		
		
		<!-- Filtro -->
		<fieldset>
			<legend><bean:message bundle="base" key="base.parametrosBusqueda"/></legend>
				<table class="tabladatos">
					<tr>
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.tranBal.nroLinea.label"/>: </label></td>
						<td class="normal"><html:text name="tranBalSearchPageVO" property="tranBal.nroLineaView" size="10" maxlength="10" /></td>
				    </tr>				    
					<tr>
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.sistema.label"/>: </label></td>
						<td class="normal"><html:text name="tranBalSearchPageVO" property="tranBal.sistemaView" size="10" maxlength="10" /></td>
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.transaccion.nroComprobante.label"/>: </label></td>
						<td class="normal"><html:text name="tranBalSearchPageVO" property="tranBal.nroComprobanteView" size="15" maxlength="10" /></td>
				    </tr>				    
					<tr>
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.transaccion.clave.label"/>: </label></td>
						<td class="normal"><html:text name="tranBalSearchPageVO" property="tranBal.clave" size="10" maxlength="6" /></td>
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.transaccion.resto.label"/>: </label></td>
						<td class="normal"><html:text name="tranBalSearchPageVO" property="tranBal.restoView" size="10" maxlength="10" /></td>
				    </tr>				    
				</table>
			
			<p align="center">
		  		<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
					<bean:message bundle="base" key="abm.button.buscar"/>
				</html:button>
			</p>
		</fieldset>	
		<!-- Fin Filtro -->

	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
			<logic:notEmpty  name="tranBalSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
                	<tbody>
	                   	<tr>
							<th width="1">&nbsp;</th> <!-- Ver-->
							<th width="1">&nbsp;</th> <!-- Modificar -->
							<th width="1">&nbsp;</th> <!-- Eliminar -->
							<th align="left"><bean:message bundle="bal" key="bal.sistema.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.transaccion.nroComprobante.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.transaccion.clave.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.transaccion.resto.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.transaccion.importe.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.transaccion.fechaPago.label"/></th>
						</tr>
				
						<logic:iterate id="TranBalVO" name="tranBalSearchPageVO" property="listResult">
							<tr>						
								<!-- Ver-->
								<td>
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="TranBalVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</td>
								<logic:notEqual name="tranBalSearchPageVO" property="modoSeleccionar" value="true">								
								<td>
									<!-- Modificar-->								
									<logic:equal name="tranBalSearchPageVO" property="modificarEnabled" value="enabled">									
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="TranBalVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="tranBalSearchPageVO" property="modificarEnabled" value="enabled">
										<img border="0" srTranBalVOc="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</td>
								<td>
									<!-- Eliminar-->								
									<logic:equal name="tranBalSearchPageVO" property="eliminarEnabled" value="enabled">		
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="TranBalVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>							
									<logic:notEqual name="tranBalSearchPageVO" property="eliminarEnabled" value="enabled">		
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</td>
							</logic:notEqual>								
								<td><bean:write name="TranBalVO" property="sistemaView"/>&nbsp;</td>
								<td><bean:write name="TranBalVO" property="nroComprobanteView"/>&nbsp;</td>
								<td><bean:write name="TranBalVO" property="clave"/>&nbsp;</td>
								<td><bean:write name="TranBalVO" property="restoView"/>&nbsp;</td>
								<td><bean:write name="TranBalVO" property="importeView"/>&nbsp;$</td>
								<td><bean:write name="TranBalVO" property="fechaPagoView"/>&nbsp;</td>		
							</tr>
						</logic:iterate>
					<tr>
						<td class="paginador" align="center" colspan="11">
							<bean:define id="pager" name="tranBalSearchPageVO"/>
							<%@ include file="/base/pager.jsp" %>
						</td>
					</tr>
				</tbody>
			</table>
		</logic:notEmpty>
		
		<logic:empty name="tranBalSearchPageVO" property="listResult">
			<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
        	    	<tbody>
						<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
						<tr>
							<td align="center">
								<bean:message bundle="base" key="base.resultadoVacio"/>
							</td>
						</tr>
					</tbody>
				</table>
		</logic:empty>
	</div>
	<!-- Fin Resultado Filtro -->
		
	<table class="tablabotones">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<logic:equal name="tranBalSearchPageVO" property="viewResult" value="true">
    			<td align="right">
						<bean:define id="agregarEnabled" name="tranBalSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
				</td>
			</logic:equal>
		</tr>
	</table>
	<input type="hidden" name="name"  value="<bean:write name='tranBalSearchPageVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
		
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>
	<input type="text" style="display:none"/>

</html:form>
<!-- Fin Tabla que contiene todos los formularios -->		