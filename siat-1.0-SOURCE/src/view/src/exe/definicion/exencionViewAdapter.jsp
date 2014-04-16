<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/exe/AdministrarExencion.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="exe" key="exe.exencionViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Exencion -->
		<fieldset>
			<legend><bean:message bundle="exe" key="exe.exencion.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal">
						<bean:write name="exencionAdapterVO" property="exencion.recurso.desRecurso"/>
					</td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="exe" key="exe.exencion.codExencion.label"/>: </label></td>
					<td class="normal"><bean:write name="exencionAdapterVO" property="exencion.codExencion"/></td>
				</tr>
				
				<tr>	
					<td><label><bean:message bundle="exe" key="exe.exencion.desExencion.label"/>: </label></td>
					<td class="normal"><bean:write name="exencionAdapterVO" property="exencion.desExencion" /></td>
				</tr>
				
				<tr>	
					<td><label><bean:message bundle="exe" key="exe.exencion.actualizaDeuda.label"/>: </label></td>
					<td class="normal"><bean:write name="exencionAdapterVO" property="exencion.actualizaDeuda.value"/></td>					
				</tr>
				
				<tr>	
					<td><label><bean:message bundle="exe" key="exe.exencion.enviaJudicial.label"/>: </label></td>
					<td class="normal"><bean:write name="exencionAdapterVO" property="exencion.enviaJudicial.value"/></td>					
				</tr>
				
				<tr>	
					<td><label><bean:message bundle="exe" key="exe.exencion.enviaCyQ.label"/>: </label></td>
					<td class="normal"><bean:write name="exencionAdapterVO" property="exencion.enviaCyQ.value"/></td>					
				</tr>
				<tr>	
					<td><label><bean:message bundle="exe" key="exe.exencion.afectaEmision.label"/>: </label></td>
					<td class="normal"><bean:write name="exencionAdapterVO" property="exencion.afectaEmision.value"/></td>					
				</tr>
				
				<tr>	
					<td><label><bean:message bundle="exe" key="exe.exencion.esParcial.label"/>: </label></td>
					<td class="normal"><bean:write name="exencionAdapterVO" property="exencion.esParcial.value"/></td>					
				</tr>
				
				<tr>
					<td><label><bean:message bundle="exe" key="exe.exencion.montoMinimo.label"/>: </label></td>
					<td class="normal"><bean:message bundle="base" key="base.currency"/><bean:write name="exencionAdapterVO" property="exencion.montoMinimoView"/></td>
				</tr>
				<tr>	
					<td><label><bean:message bundle="exe" key="exe.exencion.aplicaMinimo.label"/>: </label></td>
					<td class="normal"><bean:write name="exencionAdapterVO" property="exencion.aplicaMinimo.value"/></td>					
				</tr>				
				<tr>	
					<td><label><bean:message bundle="exe" key="exe.exencion.permiteManPad.label"/>: </label></td>
					<td class="normal"><bean:write name="exencionAdapterVO" property="exencion.permiteManPad.value"/></td>					
				</tr>
				<!-- Inclucion de CasoView -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="exencionAdapterVO" property="exencion"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>				
					</td>
				</tr>
				<!-- Fin Inclucion de CasoView -->
				
			</table>			
		</fieldset>	
		<!-- Exencion -->
		
		<!-- ExeRecCon -->
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="exe" key="exe.exencion.listExeRecCon.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="exencionAdapterVO" property="exencion.listExeRecCon">	    	
			    	<tr>						
						<th align="left"><bean:message bundle="exe" key="exe.exeRecCon.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="exe" key="exe.exeRecCon.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recCon.label"/></th>						
						<th align="left"><bean:message bundle="exe" key="exe.exeRecCon.porcentaje.label"/></th>							
						<th align="left"><bean:message bundle="exe" key="exe.exeRecCon.montoFijo.label"/></th>
					</tr>
					<logic:iterate id="ExeRecConVO" name="exencionAdapterVO" property="exencion.listExeRecCon">
						<tr>
							<td>&nbsp;<bean:write name="ExeRecConVO" property="fechaDesdeView"/>&nbsp;</td>
							<td>&nbsp;<bean:write name="ExeRecConVO" property="fechaHastaView"/>&nbsp;</td>
							<td>&nbsp;<bean:write name="ExeRecConVO" property="recCon.desRecCon" />&nbsp;</td>
							<td>&nbsp;<bean:write name="ExeRecConVO" property="porcentajeView"/>&nbsp;%</td>
							<td><bean:message bundle="base" key="base.currency"/><bean:write name="ExeRecConVO" property="montoFijoView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="exencionAdapterVO" property="exencion.listExeRecCon">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- ExeRecCon -->
		
		<table class="tablabotones">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="left">
					<logic:equal name="exencionAdapterVO" property="act" value="ver">
		   	    	   <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					   </html:button>
	   	            </logic:equal>
					<logic:equal name="exencionAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="exencionAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="exencionAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
			</tr>
		</table>
		 <input type="hidden" name="name"  value="<bean:write name='exencionAdapterVO' property='name'/>" id="name"/>
	   	 <input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	 
	   	<input type="hidden" name="method" value=""/>
		<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
		<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->