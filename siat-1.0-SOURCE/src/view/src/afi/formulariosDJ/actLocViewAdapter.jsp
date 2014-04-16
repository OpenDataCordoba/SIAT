<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/afi/AdministrarActLoc.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="afi" key="afi.actLocViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- ActLoc -->
		<fieldset>
			<legend><bean:message bundle="afi" key="afi.actLoc.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="afi" key="afi.actLoc.numeroCuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="actLocAdapterVO" property="actLoc.numeroCuenta"/></td>
		
					<td><label><bean:message bundle="afi" key="afi.actLoc.codActividad.label"/>: </label></td>
					<td class="normal"><bean:write name="actLocAdapterVO" property="actLoc.codActividadView"/></td>
				</tr>				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.actLoc.fechaInicio.label"/>: </label></td>
					<td class="normal"><bean:write name="actLocAdapterVO" property="actLoc.fechaInicioView"/></td>
		
					<td><label><bean:message bundle="afi" key="afi.actLoc.marcaPrincipal.label"/>: </label></td>
					<td class="normal"><bean:write name="actLocAdapterVO" property="actLoc.marcaPrincipalView"/></td>
				</tr>				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.actLoc.tratamiento.label"/>: </label></td>
					<td class="normal"><bean:write name="actLocAdapterVO" property="actLoc.tratamientoView"/></td>		
				</tr>
			</table>
		</fieldset>	
		<!-- ActLoc -->
	   	<!--ExeActLoc -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="afi" key="afi.actLoc.listExeActLoc.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="actLocAdapterVO" property="actLoc.listExeActLoc">	    	
			    	<tr>						
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->	
						<th align="left"><bean:message bundle="afi" key="afi.exeActLoc.numeroCuenta.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.exeActLoc.codActividadView.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.exeActLoc.nroResolucion.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.exeActLoc.fechaEmisionView.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.exeActLoc.fechaDesdeView.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.exeActLoc.fechaHastaView.label"/></th>
					</tr>
					<logic:iterate id="ExeActLocVO" name="actLocAdapterVO" property="actLoc.listExeActLoc">					
						<tr>						
							<!-- Ver/Seleccionar -->	
							<td>
								<logic:notEqual name="actLocAdapterVO" property="modoSeleccionar" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verExeActLoc', '<bean:write name="ExeActLocVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:notEqual>																
							</td>						
							<td><bean:write name="ExeActLocVO" property="numeroCuenta"/>&nbsp;</td>
							<td><bean:write name="ExeActLocVO" property="codActividadView"/>&nbsp;</td>
							<td><bean:write name="ExeActLocVO" property="nroResolucion"/>&nbsp;</td>
							<td><bean:write name="ExeActLocVO" property="fechaEmisionView"/>&nbsp;</td>
							<td><bean:write name="ExeActLocVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="ExeActLocVO" property="fechaHastaView"/>&nbsp;</td>
						</tr>						
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="actLocAdapterVO" property="actLoc.listExeActLoc">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- ExeActLoc -->
		
		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	   <logic:equal name="actLocAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>				
	   	    	</td>
	   	    </tr>
	   	</table>
	    <input type="hidden" name="name"  value="<bean:write name='actLocAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->