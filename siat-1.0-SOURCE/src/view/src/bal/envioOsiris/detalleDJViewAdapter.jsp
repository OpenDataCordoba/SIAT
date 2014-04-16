<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarDetalleDJ.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.detalleDJViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- DetalleDJ -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.detalleDJ.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="bal" key="bal.tranAfip.idTransaccioAfip.label"/>: </label></td>
					<td class="normal">
						<bean:write name="detalleDJAdapterVO" property="detalleDJ.tranAfip.idTransaccionAfipView" />
					</td>					
					<td><label><bean:message bundle="bal" key="bal.detalleDJ.fechaProceso.label"/>: </label></td>
					<td class="normal">
						<bean:write name="detalleDJAdapterVO" property="detalleDJ.fechaProcesoView" />
					</td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.detalleDJ.registro.ref"/>: </label></td>
					<td class="normal">
						<bean:write name="detalleDJAdapterVO" property="detalleDJ.registroView" />
					</td>					
					<td><label><bean:message bundle="bal" key="bal.detalleDJ.fila.ref"/>: </label></td>
					<td class="normal">
						<bean:write name="detalleDJAdapterVO" property="detalleDJ.filaView" />
					</td>				
				</tr>
				<tr>					
					<td><label><bean:message bundle="bal" key="bal.detalleDJ.estDetDJ.descripcion.ref"/>: </label></td>
					<td class="normal">
						<bean:write name="detalleDJAdapterVO" property="detalleDJ.estDetDJ.descripcion"/>
					</td>			
				</tr>	
				<tr>					
					<td><label><bean:message bundle="bal" key="bal.detalleDJ.contenido.ref"/>: </label></td>				
					<td class="normal">
					
					
					</td>																					
				</tr>			
			</table>
		</fieldset>	
		<!-- Columnas y Contenido -->
		<logic:notEqual name="detalleDJAdapterVO" property="act" value="eliminar">
			<fieldset>
					<legend><bean:message bundle="bal" key="bal.detalleDJ.contenido.ref"/></legend>
	 			   	<!-- Contenido -->
					<p align="left">
		                	 <html:textarea style="width:640px; height:480px; font-family: monospace; font-size: 8pt; color:grey;" name="detalleDJAdapterVO" property="detalleDJ.dataStrView" readonly="true"></html:textarea>
		            </p>
			</fieldset>	
	   
			<logic:equal name="detalleDJAdapterVO" property="paramContenidoParseado" value="true">
			<!-- Contenido Parseado -->
			<fieldset>
					<legend><bean:message bundle="bal" key="bal.detalleDJ.contenidoParseado.label"/></legend>
	 			   	<!-- Contenido Parseado-->
					<p align="left">
		                	 <html:textarea style="width:640px; height:480px; font-family: monospace; font-size: 8pt; color:grey;" name="detalleDJAdapterVO" property="detalleDJ.contenidoParseadoView" readonly="true"></html:textarea>
		            </p>
			</fieldset>	
			</logic:equal>
		</logic:notEqual>	 
		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   
					<logic:equal name="detalleDJAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminarDetalleDJ', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>	    			
	   	    	</td>	   	    	
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='detalleDJAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->