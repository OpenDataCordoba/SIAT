<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/gde/AdministrarAccionConvenio.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.accionConvenioAdapter.title"/></h1>
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- Datos del Rescate -->
	  	<fieldset>
			<legend><bean:message bundle="gde" key="gde.accionConvenioAdapter.title"/></legend>
			<table class="tabladatos">
	           	<tbody>
	               <tr>	
	               		<td><label><bean:message bundle="gde" key="gde.accionConvenioAdapter.accion.label"/></label></td>
	               		<td class="normal" colspan="3">
	               		<logic:notEqual name="accionMasivaConvenioAdapterVO" property="procesando" value="true">
							<html:select name="accionMasivaConvenioAdapterVO" property="accion" styleClass="select">
								<html:optionsCollection name="accionMasivaConvenioAdapterVO" property="listAcciones" value="etiqueta" label="etiqueta"/>
							</html:select>
						</logic:notEqual>
						<logic:equal name="accionMasivaConvenioAdapterVO" property="procesando" value="true">
							<bean:write name="accionMasivaConvenioAdapterVO" property="accion"/>
						</logic:equal>
					</td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.accionConvenioAdapter.listaId.label"/></td>	
						<td class="normal">
							<logic:notEqual name="accionMasivaConvenioAdapterVO" property="procesando" value="true">
								<html:textarea cols="20" rows="10" name="accionMasivaConvenioAdapterVO" property="listIds"/>
							</logic:notEqual>
							<logic:equal name="accionMasivaConvenioAdapterVO" property="procesando" value="true">		
								<html:textarea readonly="true" cols="20" rows="10" name="accionMasivaConvenioAdapterVO" property="listIds"/>
							</logic:equal>
						</td>
							
					</tr>
				</tbody>
			</table>
		</fieldset>
		
		<logic:equal name="accionMasivaConvenioAdapterVO" property="procesando" value="true">
			<fieldset>
			<legend><bean:message bundle="gde" key="gde.accionConvenioAdapter.corrida.title"/></legend>
			<table>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.accionConvenioAdapter.corrida.estado"/></label>
					<td class="normal"><bean:write name="accionMasivaConvenioAdapterVO" property="corrida.estadoCorrida.desEstadoCorrida"/></td>
				</tr>
				<tr><td>&nbsp;</td></tr>
			</table>
			</table>
	</fieldset>
		</logic:equal>

	  <!-- FIN Datos Titular -->
	  
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left"  width="50%">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				
				<td align="right" width="50%">
				<logic:notEqual name="accionMasivaConvenioAdapterVO" property="procesando" value="true">
					<html:button property="btnEliminar" onclick="submitForm('ejecutar', '');" styleClass="boton">
						<bean:message bundle="gde" key="gde.accionConvenioAdapter.button"/>" 
					</html:button>
				</logic:notEqual>
				<logic:equal name="accionMasivaConvenioAdapterVO" property="procesando" value="true">
					<html:button property="btnRefrescar" onclick="submitForm('refill', '');" styleClass="boton">
						<bean:message bundle="gde" key="gde.accionConvenioAdapter.buttonRefresh"/>" 
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
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
