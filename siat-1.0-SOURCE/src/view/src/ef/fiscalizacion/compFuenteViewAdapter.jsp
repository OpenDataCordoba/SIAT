<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarCompFuente.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.compFuenteViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<input type="button" class="boton" onclick="submitForm('volver', '<bean:write name="compFuenteAdapterVO" property="compFuente.comparacion.id" bundle="base" formatKey="general.format.id"/>');"
		   				value="<bean:message bundle="base" key="abm.button.volver"/>"/>		   									
				</td>
			</tr>
		</table>
		
		<!-- CompFuente -->
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.compFuente.title"/></legend>
			<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="ef" key="ef.fuenteInfo.nombreFuente.label"/>: </label></td>
				<td class="normal"><bean:write name="compFuenteAdapterVO" property="compFuente.plaFueDat.tituloView"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.compFuente.periodoDesde.label"/>: </label></td>
				<td class="normal"><bean:write name="compFuenteAdapterVO" property="compFuente.periodoDesde4View"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.compFuente.periodoHasta.label"/>: </label></td>
				<td class="normal"><bean:write name="compFuenteAdapterVO" property="compFuente.periodoHasta4View"/></td>				
			</tr>
				<!-- <#BeanWrites#> -->
			</table>
		</fieldset>	
		<!-- CompFuente -->

		<!-- plaFueDatCom -->
		<div id="listaPlaFueDatCom" class="scrolable" style="height: 350px;">

			<table class="tramonline" border="0" cellpadding="0" cellspacing="1">
			<caption><bean:message bundle="ef" key="ef.compFuente.listPeriodos.label"/></caption>
	    	<tbody>
			    	<tr>
						<th width="1"><bean:message bundle="ef" key="ef.plaFueDatCom.periodo.label"/></th> <!-- Periodo -->
					<!-- genera la cabecera con la lista de PlaFueDatCol -->	
						<logic:iterate id="CompFuenteColVO" name="compFuenteAdapterVO" property="compFuente.listCompFuenteCol">
							<logic:equal name="CompFuenteColVO" property="oculta.id" value="0">
								<th align="left">
									<bean:write name="CompFuenteColVO" property="colName"/>
								</th>
							</logic:equal>	    	
						</logic:iterate>
						<th align="left">
							<bean:message bundle="ef" key="ef.plaFueDatAdapter.total.label"/>
						</th>							
					</tr>
										
					<!-- itera los detalles -->
					<logic:iterate id="PlaFueDatComVO" name="compFuenteAdapterVO" property="compFuente.listPlaFueDatCom">
												
						<tr>
							<td>
								<!-- periodo/anio -->
								<bean:write name="PlaFueDatComVO" property="periodoView"/>/
								<bean:write name="PlaFueDatComVO" property="anioView"/>
							</td>								
							
							<logic:present name="PlaFueDatComVO" property="col1">
								<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatComVO" property="col1" bundle="base" formatKey="general.format.currency"/></td>
							</logic:present>

							<logic:present name="PlaFueDatComVO" property="col2" >
								<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatComVO" property="col2" bundle="base" formatKey="general.format.currency"/></td>
							</logic:present>

							<logic:present name="PlaFueDatComVO" property="col3" >
								<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatComVO" property="col3" bundle="base" formatKey="general.format.currency"/></td>
							</logic:present>

							<logic:present name="PlaFueDatComVO" property="col4" >
								<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatComVO" property="col4" bundle="base" formatKey="general.format.currency"/></td>
							</logic:present>
																					
							<logic:present name="PlaFueDatComVO" property="col5" >
								<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatComVO" property="col5" bundle="base" formatKey="general.format.currency"/></td>
							</logic:present>

							<logic:present name="PlaFueDatComVO" property="col6" >
								<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatComVO" property="col6" bundle="base" formatKey="general.format.currency"/></td>
							</logic:present>

							<logic:present name="PlaFueDatComVO" property="col7" >
								<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatComVO" property="col7" bundle="base" formatKey="general.format.currency"/></td>
							</logic:present>

							<logic:present name="PlaFueDatComVO" property="col8" >
								<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatComVO" property="col8" bundle="base" formatKey="general.format.currency"/></td>
							</logic:present>
							
							<logic:present name="PlaFueDatComVO" property="col9" >
								<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatComVO" property="col9" bundle="base" formatKey="general.format.currency"/></td>
							</logic:present>

							<logic:present name="PlaFueDatComVO" property="col10" >
								<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatComVO" property="col10" bundle="base" formatKey="general.format.currency"/></td>
							</logic:present>
							
							<logic:present name="PlaFueDatComVO" property="col11" >
								<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatComVO" property="col11" bundle="base" formatKey="general.format.currency"/></td>
							</logic:present>

							<logic:present name="PlaFueDatComVO" property="col12" >
								<td class="datos" nowrap="nowrap" ><bean:write name="PlaFueDatComVO" property="col12" bundle="base" formatKey="general.format.currency"/></td>
							</logic:present>																																										
								
							<td class="datos" nowrap="nowrap" align="right"><bean:write name="PlaFueDatComVO" property="total" bundle="base" formatKey="general.format.currency"/></td>
						</tr>
					</logic:iterate>
					
					<tr>
						<bean:define id="cantPlaFueDatCol" name="compFuenteAdapterVO" property="compFuente.cantCompFuenteColVisible"/>
						<td colspan="<%=Integer.parseInt(cantPlaFueDatCol.toString())+1%>" align="right">
							<bean:message bundle="ef" key="ef.plaFueDatAdapter.total.label"/>:
						</td>
						<td class="datos" align="right"><bean:write name="compFuenteAdapterVO" property="compFuente.total" bundle="base" formatKey="general.format.currency"/></td>
					</tr>															
			</table>
		</div>	
		<!-- plaFueDatCom -->
		
		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   			<input type="button" class="boton" onclick="submitForm('volver', '<bean:write name="compFuenteAdapterVO" property="compFuente.comparacion.id" bundle="base" formatKey="general.format.id"/>');"
		   				value="<bean:message bundle="base" key="abm.button.volver"/>"/>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="compFuenteAdapterVO" property="act" value="eliminar">
						<input type="button" class="boton" onclick="submitForm('eliminar', '<bean:write name="compFuenteAdapterVO" property="compFuente.comparacion.id" bundle="base" formatKey="general.format.id"/>');"
							value="<bean:message bundle="base" key="abm.button.eliminar"/>"/>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='compFuenteAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
<!-- compFuenteViewAdapter.jsp -->
