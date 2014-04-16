<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/AdministrarEmisionExt.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="emi" key="emi.emisionExtViewAdapter.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>

	<!-- Datos de Emision -->
	<fieldset>
	
		<legend><bean:message bundle="emi" key="emi.emision.title"/></legend>	

		<table class="tabladatos">

			<!-- Recurso -->
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal"><bean:write name="emisionExtAdapterVO" property="emision.recurso.desRecurso"/></td>
			</tr>
			<!-- Fecha Emision -->
			<tr>
				<td><label><bean:message bundle="emi" key="emi.emision.label"/>: </label></td>
				<td class="normal"><bean:write name="emisionExtAdapterVO" property="emision.fechaEmisionView"/></td>
			</tr>
		</table>
	</fieldset>	
	<!-- fin Datos de Emision -->

	
	<!-- Emision Ext -->
	<fieldset> 
		<legend><bean:message bundle="emi" key="emi.emisionExtAdapter.deuda.title"/></legend>
		<table class="tabladatos">
			<tr>
				<!-- Cuenta -->	
				<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>:</label></td>
				<td class="normal" colspan="3">
					<bean:write name="emisionExtAdapterVO" property="cuenta.numeroCuenta"/>
				</td>
			</tr>
			
			<tr>
				<!--  Periodo -->
				<td><label><bean:message bundle="emi" key="emi.emisionExt.periodo.label"/>:</label></td>
				<td class="normal">
					<bean:write name="emisionExtAdapterVO" property="periodoView"/>				
				</td>
				
				<!-- Anio -->	
				<td><label><bean:message bundle="emi" key="emi.emisionExt.anio.label"/>:</label></td>
				<td class="normal">
					<bean:write name="emisionExtAdapterVO" property="anioView"/>
				</td>				
			</tr>
			
			<tr>
				<!--  Fecha Vto -->
				<td><label><bean:message bundle="emi" key="emi.emisionExt.fechaVto.label"/>: </label></td>
				<td class="normal">
					<bean:write name="emisionExtAdapterVO" property="fechaVtoView"/>
				</td>
				<td><label><bean:message bundle="emi" key="emi.emisionExt.estadoDeuda.label"/>: </label></td>
				<td class="normal">
					<bean:write name="emisionExtAdapterVO" property="desEstadoDeuda"/>
				</td>				
			</tr>	
			
			<tr>
				<!-- Inclucion de CasoView -->
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td>				
					<bean:define id="IncludedVO" name="emisionExtAdapterVO" property="emision"/>
					<%@ include file="/cas/caso/includeCasoView.jsp" %>				
				</td>		
				<!-- Fin Inclucion de CasoView -->
			
				<!-- RecClaDeu -->
				<td><label><bean:message bundle="emi" key="emi.emisionExt.recClaDeu.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="emisionExtAdapterVO" property="desRecClaDeu"/>
				</td>				
			</tr>
								
			<tr>
				<!-- Observaciones -->
				<td><label><bean:message bundle="emi" key="emi.emisionExt.observaciones.label"/>:</label></td>
				<td class="normal" colspan="3">
					<bean:write name="emisionExtAdapterVO" property="emision.observacion"/>
				</td>			
			</tr>
			
			<tr>
				<!-- Lista de conceptos -->
				<td><label><bean:message bundle="emi" key="emi.emisionExt.conceptos.label"/>:</label></td>
				<td>					
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1">
							<tbody>
							<logic:notEmpty name="emisionExtAdapterVO" property="listDeuAdmRecConVO">
								<logic:iterate id="DeuAdmRecConVO" name="emisionExtAdapterVO" property="listDeuAdmRecConVO">
									<tr>
										<td><bean:write name="DeuAdmRecConVO" property="descripcion"/>&nbsp;</td>
										<td>
											$<bean:write name="DeuAdmRecConVO" property="importe" />&nbsp;
										</td>
									</tr>	
								</logic:iterate>
							</logic:notEmpty>			
						</tbody>
						</table>					
				</td>
				<!-- FIN Lista de conceptos -->
			</tr>				
			
			<tr>
				<!-- Importe -->
				<td><label><bean:message bundle="emi" key="emi.emisionExt.importe.label"/>:</label></td>
				<td class="normal" colspan="3">
					<bean:write name="emisionExtAdapterVO" property="importe" bundle="base" formatKey="general.format.currency"/>
				</td>			
			</tr>	
		</table>
	</fieldset>
	<!-- Fin Emision Ext -->

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
	<input type="hidden" name="isSubmittedForm" value="true"/>
	<input type="hidden" name="idTipoEmision" value="<bean:write name="emisionExtAdapterVO" property="emision.tipoEmision.id" bundle="base" formatKey="general.format.id"/>"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
