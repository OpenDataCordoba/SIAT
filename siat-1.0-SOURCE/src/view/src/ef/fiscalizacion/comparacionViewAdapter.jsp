<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/ef/AdministrarComparacion.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.comparacionViewAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Comparacion -->
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.comparacion.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="ef" key="ef.comparacion.fecha.label"/>: </label></td>
					<td class="normal">
						<bean:write name="comparacionAdapterVO" property="comparacion.fechaView"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="ef" key="ef.comparacion.descripcion.label"/>: </label></td>
					<td class="normal"><bean:write name="comparacionAdapterVO" property="comparacion.descripcion"/></td>			
				</tr>				

			</table>
		</fieldset>	
		<!-- Comparacion -->
		
		<!-- CompFuente -->	
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.compFuente.title"/></legend>
			
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="ef" key="ef.comparacion.listCompFuente.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="comparacionAdapterVO" property="comparacion.listCompFuente">	    	
				    	<tr>
							<th align="left"><bean:message bundle="ef" key="ef.comparacion.descripcion.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.comparacionAdapter.desde.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.comparacionAdapter.hasta.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.comparacion.total.label"/></th>						
						</tr>
						<logic:iterate id="CompFuenteVO" name="comparacionAdapterVO" property="comparacion.listCompFuente">
				
							<tr>									
								<td><bean:write name="CompFuenteVO" property="plaFueDat.tituloView"/></td>
								<td><bean:write name="CompFuenteVO" property="periodoDesde4View"/></td>
								<td><bean:write name="CompFuenteVO" property="periodoHasta4View"/></td>
								<td><bean:write name="CompFuenteVO" property="totalView"/></td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="comparacionAdapterVO" property="comparacion.listCompFuente">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>				
				</tbody>
			</table>
		</fieldset>	
		<!-- CompFuente -->
				
		<!-- CompFuenteRes -->	
		<fieldset id="seccionCompFuenteRes">
			<legend><bean:message bundle="ef" key="ef.compFuenteRes.title"/></legend>
			
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="ef" key="ef.comparacion.listCompFuenteRes.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="comparacionAdapterVO" property="comparacion.listCompFuenteRes">	    	
				    	<tr>
							<th align="left"><bean:message bundle="ef" key="ef.compFuenteRes.operacion.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.compFuenteRes.diferencia.label"/></th>						
						</tr>
						<logic:iterate id="CompFuenteResVO" name="comparacionAdapterVO" property="comparacion.listCompFuenteRes">
				
							<tr>
								<td><bean:write name="CompFuenteResVO" property="operacion"/></td>
								<td>$&nbsp;<bean:write name="CompFuenteResVO" property="diferenciaView"/></td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="comparacionAdapterVO" property="comparacion.listCompFuenteRes">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>
	
				</tbody>
			</table>
		</fieldset>	
		<!-- CompFuenteRes -->				
		<table class="tablabotones">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<logic:equal name="comparacionAdapterVO" property="act" value="eliminar">
					<td align="left">
	   	    				<bean:define id="eliminarEnabled" name="comparacionAdapterVO" property="eliminarEnabled"/>
							<input type="button" <%=eliminarEnabled%> class="boton" 
								onClick="submitForm('eliminar', '<bean:write name="comparacionAdapterVO" property="comparacion.id" bundle="base" formatKey="general.format.id"/>');" 
								value="<bean:message bundle="base" key="abm.button.eliminar"/>"
							/>
					</td>
				</logic:equal>
				<logic:equal name="comparacionAdapterVO" property="act" value="ver">
					<td align="right">
							<bean:define id="imprimirComparacionEnabled" name="comparacionAdapterVO" property="imprimirComparacionEnabled"/>
							<input type="button" class="boton" <%=imprimirComparacionEnabled%> onclick="submitImprimir('imprimirReportFromAdapter', '1');" 
								value="<bean:message bundle="base" key="abm.button.imprimir"/>"/>		
	               </td>				
				</logic:equal>	
			</tr>
		</table>
		<input type="hidden" name="name"  value="<bean:write name='comparacionAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
		<logic:present name="irA">
			<script type="text/javascript">document.location = document.URL + '#<bean:write name="irA"/>';</script>
		</logic:present>
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
<!-- comparacionAdapter.jsp -->