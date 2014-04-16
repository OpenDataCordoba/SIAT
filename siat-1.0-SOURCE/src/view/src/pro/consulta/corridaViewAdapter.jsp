<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pro/AdministrarCorrida.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="pro" key="pro.corridaViewAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
			
		<!-- Corrida -->
		<fieldset>
			<legend><bean:message bundle="pro" key="pro.corrida.title"/></legend>
			<table class="tabladatos">
				<!-- Proceso -->
				<tr>
					<td><label><bean:message bundle="pro" key="pro.corrida.proceso.label"/>: </label></td>
					<td class="normal"><bean:write name="corridaAdapterVO" property="corrida.proceso.desProceso"/></td>
				</tr>
				<!-- Fecha Inicio -->
				<tr>
					<td><label><bean:message bundle="pro" key="pro.corrida.fechaInicio.label"/>: </label></td>
					<td class="normal"><bean:write name="corridaAdapterVO" property="corrida.fechaInicioView"/></td>
				</tr>
				<!-- Descripcion -->
				<tr>
					<td><label><bean:message bundle="pro" key="pro.corrida.desCorrida.label"/>: </label></td>
					<td class="normal"><bean:write name="corridaAdapterVO" property="corrida.desCorrida"/></td>
				</tr>
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="pro" key="pro.corrida.estadoCorrida.label"/>: </label></td>
					<td class="normal"><bean:write name="corridaAdapterVO" property="corrida.estadoCorrida.desEstadoCorrida"/></td>
				</tr>
	
			</table>
		</fieldset>	
		<!-- Corrida -->
		
		<logic:equal name="corridaAdapterVO" property="paramProcesadoOk" value="true">
		<!-- Reportes de la corrida -->		
		<fieldset>
			<legend><bean:message bundle="pro" key="pro.corridaViewAdapter.reportes.legend"/></legend>			
			<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="pro" key="pro.corridaViewAdapter.reportes.title"/></caption>
	    	<tbody>
				<logic:notEmpty  name="corridaAdapterVO" property="listFileCorrida">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Adjunto -->
						<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
						<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
					</tr>
					<logic:iterate id="FileCorridaVO" name="corridaAdapterVO" property="listFileCorrida">
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
				<logic:empty  name="corridaAdapterVO" property="listFileCorrida">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			</table>
		</fieldset>
		<!-- Fin Reportes de la corrida -->		
		</logic:equal>
		
		<!-- LogCorrida -->
		<fieldset>
			<legend><bean:message bundle="pro" key="pro.LogCorrida.title"/></legend>
				<logic:iterate id="LogCorridaVO" name="corridaAdapterVO" property="corrida.listLogCorrida">
					<tr>
						<td><bean:write name="LogCorridaVO" property="log"/></td><br />
					</tr>
				</logic:iterate>
				<logic:empty name="corridaAdapterVO" property="corrida.listLogCorrida">
					<tr>
						<td><bean:message bundle="pro" key="pro.LogCorrida.emptyLog"/>&nbsp;</td>
					</tr>
				</logic:empty>
		</fieldset>	
		<!-- LogCorrida -->
		

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
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
		<input type="hidden" name="fileParam" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
