<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/emi/AdministrarProcesoEmisionExterna.do">
	
		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="emi" key="emi.procesoEmisionExternaAdapter.title"/></h1>

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
			<legend><bean:message bundle="emi" key="emi.emision.title"/></legend>			
			<table class="tabladatos" width="100%">	
				
				<tr>
					<!-- Archivo -->
					<td><label><bean:message bundle="emi" key="emi.emision.archivo.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoEmisionExternaAdapterVO" property="emision.observacion"/></td>
				</tr>
				
				<tr>
					<!-- Recurso -->
					<td><label><bean:message bundle="emi" key="emi.emision.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoEmisionExternaAdapterVO" property="emision.recurso.desRecurso"/></td>
				</tr>

				<tr>
					<!-- Anio -->
					<td><label><bean:message bundle="emi" key="emi.procesoEmisionExternaAdapter.anio.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoEmisionExternaAdapterVO" property="anioView"/></td>
				</tr>

				<tr>
					<!-- Periodo -->
					<td><label><bean:message bundle="emi" key="emi.procesoEmisionExternaAdapter.periodo.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoEmisionExternaAdapterVO" property="periodoView"/></td>
				</tr>

				<tr>
					<!-- Estado del Proceso-->
					<td><label><bean:message bundle="emi" key="emi.emision.estadoCorrida.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoEmisionExternaAdapterVO" property="emision.corrida.estadoCorrida.desEstadoCorrida"/></td>
				</tr>
			</table>	
		</fieldset>
		<!-- FIN Datos de la Emision de TGI -->		
		
		<!-- 1-Generar DeudaAdmin -->		
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.procesoEmisionExternaAdapter.paso1.title"/></legend>			

			<table class="tabladatos" width="100%">
				<!-- Descripcion del Paso 1 -->		
				<tr>
					<td class="normal" colspan="4" align="left"><bean:message bundle="emi" key="emi.procesoEmisionExternaAdapter.paso1.descripcion"/></td>
				</tr>
			</table>
   			
   			<!-- Fecha Inicio, Hora Inicio, Estado Corrida y Observacion -->
   			<bean:define id="idCorrida" name="procesoEmisionExternaAdapterVO" property="emision.corrida.idView"/>
			<jsp:include page="/pro/AdministrarAdpCorrida.do">
   				<jsp:param name="method" value="estadoPaso" />
   				<jsp:param name="paso" value="1" />
   				<jsp:param name="id" value="<%= idCorrida %>" />
			</jsp:include>

			<logic:equal name="procesoEmisionExternaAdapterVO" property="paramPaso" value="1">
				<table class="tablabotones" width="100%">
				<tr>				
			    	<td align="center">
						<bean:define id="procesoEmisionAdapterVO" name="procesoEmisionExternaAdapterVO"/>
			    		<%@ include file="/emi/emision/includeBotonesProcesoEmision.jsp" %>
					</td>
				 </tr>
				</table>
			</logic:equal>

			<logic:equal name="procesoEmisionExternaAdapterVO" property="paramPaso" value="1">
				<!-- Reportes de AuxDeuda generadas  -->		
				<table class="tablabotones" width="100%">
					<tr>
						<td align="center">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('consultarAuxDeuda', '');">
								<bean:message bundle="emi" key="emi.procesoEmisionExternaAdapter.button.consultarAuxDeu"/>
							</html:button>
						</td>
					</tr>
				</table>				
				<!-- Fin Reportes de Deuda TGI para Analisis  -->
			</logic:equal>	

		</fieldset>
		<!--FIN 1-Generar DeudaAdmin -->		
		
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
		<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
		<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="fileParam" value=""/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->							