<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/AdministrarEmisionExterna.do" enctype="multipart/form-data">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="emi" key="emi.emisionExternaUploadAdapter.title"/></h1>	

	<p><bean:message bundle="emi" key="emi.emisionExternaUploadAdapter.legend"/></p>

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>

	<!-- cargar Archivo -->	
	<logic:equal name="emisionExternaAdapterVO" property="verBotonCargarEnabled" value="true">
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.emisionExternaUploadAdapter.datos"/></legend>
			<table>			
				<tr>
					<td>
						<label>(*)&nbsp;<bean:message bundle="emi" key="emi.emisionExternaUploadAdapter.file.label"/>:</label>
					</td>
					<td>		
						<html:file property="file" size="50" value=""/>
					</td>	
				</tr>
			</table>	
			<br>	
			<table class="tabladatos" width="100%">			
				<tr>
					<td align="right">
						<input type="button" value="Cargar" name="cargar" onclick="submitForm('upload', '')"/>
					</td>
				</tr>
			</table>
		</fieldset>
	</logic:equal>
	
	<!-- Boton Validar -->
	<logic:equal name="emisionExternaAdapterVO" property="verBloqueValidarEnabled" value="true">
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.emisionExternaUploadAdapter.validar.legend"/></legend>
			<table class="tabladatos" width="100%">			
				<tr>
					<td>
						<label><bean:message bundle="emi" key="emi.emisionExternaUploadAdapter.validar.legend"/>:</label>	
					</td>
					<td class="normal">
						<bean:write name="emisionExternaAdapterVO" property="fileName" />
					</td>
				</tr>
				
				<logic:equal name="emisionExternaAdapterVO" property="verBotonValidarEnabled" value="true">
					<tr>
						<td align="right">
							<input type="button" value="Validar" name="validar" onclick="submitForm('validar', '')"/>		
						</td>
					</tr>
				</logic:equal>				
			</table>		
		</fieldset>	
	</logic:equal>
	
	<!-- Bloque con los errores -->
	<logic:notEqual name="emisionExternaAdapterVO" property="errors" value="">
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.emisionExternaUploadAdapter.errores.legend"/></legend>		
			<html:textarea style="width:610px; height:270px;" name="emisionExternaAdapterVO" property="errors" readonly="true"></html:textarea>
		</fieldset>
	</logic:notEqual>		
			
	<!-- Resultado de la carga -->
	<logic:equal name="emisionExternaAdapterVO" property="verResultadoValidacionEnabled" value="true">	
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.emisionExternaUploadAdapter.resultValidacion.legend"/></legend>
			
				<bean:message bundle="emi" key="emi.emisionExternaUploadAdapter.cantLineas"/>:
				<bean:write name="emisionExternaAdapterVO" property="cantLineas" bundle="base" formatKey="general.format.id"/>
				
				<br><br>
				
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="emi" key="emi.emisionExternaUploadAdapter.totales.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="emisionExternaAdapterVO" property="listRecurso">	    	
				    	
				    	<logic:iterate id="Recurso" name="emisionExternaAdapterVO" property="listRecurso">
				    	
					    	<tr>
					    		<td colspan="3">
					    			<label><bean:message bundle="def" key="def.recurso.label"/>: </label>
					    			<bean:write name="Recurso" property="desRecurso"/>&nbsp;
					    		</td>
					    	</tr>
					    	<tr>
					    		<th>&nbsp;</th>
								<th align="left"><bean:message bundle="def" key="def.recCon.label"/></th>
								<th align="left"><bean:message bundle="emi" key="emi.emisionExternaUploadAdapter.total.label"/></th>						
							</tr>
							<logic:iterate id="RecCon" name="Recurso" property="listRecCon">
								<tr>
									<td>&nbsp;</td>
									<td><bean:write name="RecCon" property="desRecCon"/>&nbsp;</td>
									<td><bean:write name="RecCon" property="totalView"/>&nbsp;</td>
								</tr>
							</logic:iterate>
							
							<tr>
								<td>&nbsp;</td>
								<td class="celdatotales"><bean:message bundle="emi" key="emi.emisionExternaUploadAdapter.total.label"/></td>
								<td class="celdatotales"><bean:write name="Recurso" property="totalRecConView"/>&nbsp;</td>
							</tr>
							
						</logic:iterate>	
					</logic:notEmpty>
				</tbody>
			</table>
					
		</fieldset>
		
		<logic:equal name="emisionExternaAdapterVO" property="verBotonContinuarEnabled" value="enabled">
			<table class="tabladatos" width="100%">			
				<tr>
					<td align="right">
						<input type="button" value="Generar deuda para an&aacute;lisis" name="continuar" onclick="submitForm('continuar', '')"/>
					</td>
				</tr>
			</table>
		</logic:equal>
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
