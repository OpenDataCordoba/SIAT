<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarEvento.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.eventoViewAdapter.title"/></h1>
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- Evento -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.evento.title"/></legend>
			<table class="tabladatos">
				<!-- Codigo -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.evento.codigo.label"/>: </label></td>
					<td class="normal"><bean:write name="eventoAdapterVO" property="evento.codigoView"/></td>
				</tr>
				<!-- Descripcion -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.evento.descripcion.label"/>: </label></td>
					<td class="normal"><bean:write name="eventoAdapterVO" property="evento.descripcion"/></td>
				</tr>
				<!-- Etapa Procesal -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.evento.etapaProcesal.label"/>: </label></td>
					<td class="normal"><bean:write name="eventoAdapterVO" property="evento.etapaProcesal.desEtapaProcesal"/></td>
				</tr>
				<!-- AfectaCadJui -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.evento.afectaCadJui.label"/>: </label></td>
					<td class="normal"><bean:write name="eventoAdapterVO" property="evento.afectaCadJui.value"/></td>
				</tr>
				<!-- AfectaPresSen -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.evento.afectaPresSen.label"/>: </label></td>
					<td class="normal"><bean:write name="eventoAdapterVO" property="evento.afectaPresSen.value"/></td>
				</tr>				
				
				<!-- Es Unico en Gesjud -->
				<tr>	
					<td><label><bean:message bundle="gde" key="gde.evento.esUnicoEnGesJud.label"/>: </label></td>
					<td class="normal">
						<bean:write name="eventoAdapterVO" property="evento.esUnicoEnGesJud.value"/>
					</td>					
				</tr>
							
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="eventoAdapterVO" property="evento.estado.value"/></td>
				</tr>
			</table>
	
	</fieldset>	
		<!-- Evento -->
		
		<!-- Lista de Predecesores -->
		<logic:equal name="eventoAdapterVO" property="act" value="ver">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="gde" key="gde.evento.predecesores.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="eventoAdapterVO" property="listPredecesores">
				    	<tr>
							<th align="left"><bean:message bundle="gde" key="gde.evento.codigo.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.evento.descripcion.label"/></th>
						</tr>
						<logic:iterate id="eventoVO" name="eventoAdapterVO" property="listPredecesores">
							<tr>
								<td><bean:write name="eventoVO" property="codigoView" /></td>
								<td><bean:write name="eventoVO" property="descripcion" /></td>								
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="eventoAdapterVO" property="listPredecesores">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>					
				</tbody>
			</table>
		</logic:equal>
		<!-- Lista de Predecesores -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	    <logic:equal name="eventoAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
					<logic:equal name="eventoAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="eventoAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="eventoAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	<input type="hidden" name="name"  value="<bean:write name='eventoAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
