<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarBalance.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.balanceAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.balance.title"/></legend>			
			<table class="tabladatos" width="100%">
				<!-- Fecha Balance -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.balance.fechaBalance.label"/>: </label></td>
					<td class="normal"><bean:write name="balanceAdapterVO" property="balance.fechaBalanceView"/></td>
				</tr>		
				<!-- Ejercicio -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.ejercicio.label"/>: </label></td>
					<td class="normal"><bean:write name="balanceAdapterVO" property="balance.ejercicio.desEjercicio"/></td>
				</tr>		
				<!-- Fecha Desde y Hasta -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.balance.fechaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="balanceAdapterVO" property="balance.fechaDesdeView"/></td>
					<td><label><bean:message bundle="bal" key="bal.balance.fechaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="balanceAdapterVO" property="balance.fechaHastaView"/></td>
				</tr>						
				<!-- Observacion-->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.balance.observacion.label"/>: </label></td>
					<td colspan="3" class="normal"><bean:write name="balanceAdapterVO" property="balance.observacion"/></td>					
				</tr>
				<!-- Estado Corrida-->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.balance.estadoProceso.label"/>: </label></td>
					<td class="normal"><bean:write name="balanceAdapterVO" property="balance.corrida.estadoCorrida.desEstadoCorrida"/></td>					
				</tr>
			</table>
		</fieldset>
		
		<logic:equal name="balanceAdapterVO" property="paramProcesadoOk" value="true">
		<!-- Reportes del Balance para visualizar -->		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.balanceAdapter.reportes.legend"/></legend>			
			<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.balanceAdapter.reportes.title"/></caption>
	    	<tbody>
				<logic:notEmpty  name="balanceAdapterVO" property="listFileCorrida">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Adjunto -->
						<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
						<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
					</tr>
					<logic:iterate id="FileCorridaVO" name="balanceAdapterVO" property="listFileCorrida">
							<tr>
							<!-- Adjunto -->
							<td>
								<logic:equal name="FileCorridaVO" property="esPdf" value="true">							
									<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="FileCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/imprimir0.gif"/>
									</a>
								</logic:equal>
								<logic:equal name="FileCorridaVO" property="esPlanilla" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="FileCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/excel0.gif"/>
									</a>
								</logic:equal>
								<logic:equal name="FileCorridaVO" property="esTxt" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="FileCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/sincFile0.gif"/>
									</a>
								</logic:equal>
							</td>
							<td><bean:write name="FileCorridaVO" property="nombre"/>&nbsp;</td>
							<td><bean:write name="FileCorridaVO" property="observacion"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="balanceAdapterVO" property="listFileCorrida">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			</table>
		</fieldset>
		<!-- Fin Reportes del Balance para visualizar -->		
		</logic:equal>
	
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left" width="50%">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="100%">
					<logic:equal name="balanceAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="balanceAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="balanceAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
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
		<input type="hidden" name="fileParam" value=""/>
		
		<!-- Inclusion del Codigo Javascript del Calendar-->
		<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
		
		