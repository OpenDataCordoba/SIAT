<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/UploadEventoGesJud.do" enctype="multipart/form-data">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="gde" key="gde.uploadEventoGesJudAdapter.title"/></h1>	

	<p><bean:message bundle="gde" key="gde.uploadEventoGesJudAdapter.legend"/></p>

	<!-- cargar Archivo -->	
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.uploadEventoGesJudAdapter.datos"/></legend>
		<table>			
			<tr>
				<td>
					<label>(*)&nbsp;<bean:message bundle="gde" key="gde.uploadEventoGesJudAdapter.file.label"/>:</label>
				</td>
				<td>		
					<html:file property="file" size="50" value="/home/alejandro/eventosGesJud"/>
				</td>	
			</tr>
		</table>	
		<br>	
		<input type="button" value="Cargar" name="cargar" onclick="submitForm('upload', '')"/>
	</fieldset>	
	
	<!-- Resultado Analisis -->
	<logic:equal name="uploadEventoGesJudAdapterVO" property="verLogAnalisisEnabled" value="true">	
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.uploadEventoGesJudAdapter.resultAnalisis.legend"/></legend>
			<table>			
					<tr>
						<td width="50%">
							<logic:equal name="uploadEventoGesJudAdapterVO" property="resultExito" value="true">						
								<bean:message bundle="gde" key="gde.uploadEventoGesJudAdapter.resultAnalisis.exito"/>								
							</logic:equal>
							<logic:equal name="uploadEventoGesJudAdapterVO" property="resultExito" value="false">
								<bean:message bundle="gde" key="gde.uploadEventoGesJudAdapter.resultAnalisis.error"/>
								<br>	
								<input type="button" 
									value="<bean:message bundle="gde" key="gde.uploadEventoGesJudAdapter.button.descargarLog"/>" 
									name="descargarLogAnalisis" 
									onclick="submitForm('imprimirLogAnalisis', '')"/>
							</logic:equal>	
						</td>
				
						<td width="50%" align="right">
							<logic:equal name="uploadEventoGesJudAdapterVO" property="verResultadoCargaEnabled" value="false">	
								<bean:message bundle="gde" key="gde.uploadEventoGesJudAdapter.cantEventosGrabar"/>:
								<bean:write name="uploadEventoGesJudAdapterVO" property="cantEventosGrabar"/>
								<br>
								<input type="button" value="Continuar" name="Continuar" onclick="submitForm('cargarEventos', '')"
								  <logic:equal name="uploadEventoGesJudAdapterVO" property="cantEventosGrabar" value="0">
								  	disabled='disabled'
								  </logic:equal>	
								/>
						</logic:equal>	
						</td>
				</tr>	
			</table>		
		</fieldset>	
	</logic:equal>
			
	<!-- Resultado de la carga -->
	<logic:equal name="uploadEventoGesJudAdapterVO" property="verResultadoCargaEnabled" value="true">	
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.uploadEventoGesJudAdapter.resultCarga.legend"/></legend>
			
				<bean:message bundle="gde" key="gde.uploadEventoGesJudAdapter.resultCarga"/>
				<br>
				<bean:message bundle="gde" key="gde.uploadEventoGesJudAdapter.cantGrabados"/>:
				<bean:write name="uploadEventoGesJudAdapterVO" property="cantLineasGrabadas" bundle="base" formatKey="general.format.id"/>	
		</fieldset>
	</logic:equal>	

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
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>
	
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
