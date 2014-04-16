<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/ef/AdministrarOrdConBasImp.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.ordConBasImpAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<input type="button" class="boton" onclick="submitForm('volver', '<bean:write name="ordConBasImpAdapterVO" property="ordConBasImp.ordenControl.idView" />');"
		   				value="<bean:message bundle="base" key="abm.button.volver"/>"/>					
				</td>
			</tr>
		</table>
		
		<!-- ordConBasImp -->
		<bean:define id="OrdConBasImpVO" name="ordConBasImpAdapterVO" property="ordConBasImp"/>
		<%@include file="/ef/fiscalizacion/includeOrdConBasImpView.jsp" %>
		<!-- ordConBasImp -->
				
			
		<!-- plaFueDatCom -->
		<div id="listaPlaFueDatCom" class="scrolable" style="height: 350px;">

			<table class="tramonline" border="0" cellpadding="0" cellspacing="1">
			<caption><bean:message bundle="ef" key="ef.compFuente.listPeriodos.label"/></caption>
	    	<tbody>
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Agregar ajuste -->
						<th width="1"><bean:message bundle="ef" key="ef.plaFueDatCom.periodo.label"/></th> <!-- Periodo -->
					<!-- genera la cabecera con la lista de PlaFueDatCol -->							
						<logic:iterate id="compFuenteColVO" name="ordConBasImpAdapterVO" property="ordConBasImp.compFuente.listCompFuenteCol">
							<th align="left">
								<bean:write name="compFuenteColVO" property="colName"/>
							</th>
						</logic:iterate>
						<th align="left">
							<bean:message bundle="ef" key="ef.ordConBasImpAdapter.totalAjustes.label"/>
						</th>							
						<th align="left">
							<bean:message bundle="ef" key="ef.ordConBasImpAdapter.total.label"/>
						</th>
					</tr>
										
					<!-- itera los detalles -->
					<logic:iterate id="PlaFueDatComVO" name="ordConBasImpAdapterVO" property="ordConBasImp.compFuente.listPlaFueDatCom">
												
						<tr>
							<!-- agregar ajuste -->
							<td>
								<a style="cursor: pointer; cursor: hand;" onclick="submitForm('irCargarAjustes', '<bean:write name="PlaFueDatComVO" property="id" bundle="base" formatKey="general.format.id"/>');">
									<img title="<bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.button.cargarAjustes"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
								</a>							
							</td>
							
							<td>
								<!-- periodo/anio -->
								<bean:write name="PlaFueDatComVO" property="periodoView"/>/
								<bean:write name="PlaFueDatComVO" property="anioView"/>
							</td>								
							
							<logic:present name="PlaFueDatComVO" property="col1">
								<td class="datos" nowrap="nowrap" >
									<bean:write name="PlaFueDatComVO" property="col1ForAjustes" />									
								</td>
							</logic:present>

							<logic:present name="PlaFueDatComVO" property="col2" >
								<td class="datos" nowrap="nowrap" >
									<bean:write name="PlaFueDatComVO" property="col2ForAjustes" />									
								</td>
							</logic:present>

							<logic:present name="PlaFueDatComVO" property="col3" >
								<td class="datos" nowrap="nowrap" >
									<bean:write name="PlaFueDatComVO" property="col3ForAjustes" />									
								</td>
							</logic:present>

							<logic:present name="PlaFueDatComVO" property="col4" >
								<td class="datos" nowrap="nowrap" >
									<bean:write name="PlaFueDatComVO" property="col4ForAjustes" />									
								</td>
							</logic:present>
																					
							<logic:present name="PlaFueDatComVO" property="col5" >
								<td class="datos" nowrap="nowrap" >
									<bean:write name="PlaFueDatComVO" property="col5ForAjustes" />									
								</td>
							</logic:present>

							<logic:present name="PlaFueDatComVO" property="col6" >
								<td class="datos" nowrap="nowrap" >
									<bean:write name="PlaFueDatComVO" property="col6ForAjustes" />									
								</td>
							</logic:present>

							<logic:present name="PlaFueDatComVO" property="col7" >
								<td class="datos" nowrap="nowrap" >
									<bean:write name="PlaFueDatComVO" property="col7ForAjustes" />									
								</td>
							</logic:present>

							<logic:present name="PlaFueDatComVO" property="col8" >
								<td class="datos" nowrap="nowrap" >
									<bean:write name="PlaFueDatComVO" property="col8ForAjustes" />																	
								</td>
							</logic:present>
							
							<logic:present name="PlaFueDatComVO" property="col9" >
								<td class="datos" nowrap="nowrap" >
									<bean:write name="PlaFueDatComVO" property="col9ForAjustes" />																	
								</td>
							</logic:present>

							<logic:present name="PlaFueDatComVO" property="col10" >
								<td class="datos" nowrap="nowrap" >
									<bean:write name="PlaFueDatComVO" property="col10ForAjustes" />																		
								</td>
							</logic:present>
							
							<logic:present name="PlaFueDatComVO" property="col11" >
								<td class="datos" nowrap="nowrap" >
									<bean:write name="PlaFueDatComVO" property="col11ForAjustes" />																	
								</td>
							</logic:present>

							<logic:present name="PlaFueDatComVO" property="col12" >
								<td class="datos" nowrap="nowrap" >
									<bean:write name="PlaFueDatComVO" property="col12ForAjustes" />																	
								</td>
							</logic:present>																																										
							
							<!-- total ajustes -->
							<td class="datos" align="right">
								<bean:write name="PlaFueDatComVO" property="totalAjustes" bundle="base" formatKey="general.format.currency"/>
							</td>
								
							<!--total ajustado --> 	
							<td class="datos" align="right">
								<bean:write name="PlaFueDatComVO" property="totalAjustado" bundle="base" formatKey="general.format.currency"/>
							</td>
						</tr>						
					</logic:iterate>
					
					<tr>
						<bean:define id="cantPlaFueDatCol" name="ordConBasImpAdapterVO" property="ordConBasImp.compFuente.cantCompFuenteColVisibleSuma"/>
						<td colspan="<%=Integer.parseInt(cantPlaFueDatCol.toString())+2%>" align="right">
							<b><bean:message bundle="ef" key="ef.ordConBasImpAdapter.totales.label"/>:</b>
						</td>
						<td class="datos" nowrap="nowrap"  align="right"><b><bean:write name="ordConBasImpAdapterVO" property="ordConBasImp.compFuente.totalAjustes" bundle="base" formatKey="general.format.currency"/></b></td>
						<td class="datos" nowrap="nowrap"  align="right"><b><bean:write name="ordConBasImpAdapterVO" property="ordConBasImp.compFuente.totalAjustado" bundle="base" formatKey="general.format.currency"/></b></td>
					</tr>															
			</table>
		</div>	
		<!-- plaFueDatCom -->
				
		<table class="tablabotones">
			<tr>				
				<td align="left">
		   			<input type="button" class="boton" onclick="submitForm('volver', '<bean:write name="ordConBasImpAdapterVO" property="ordConBasImp.ordenControl.idView" />');"
		   				value="<bean:message bundle="base" key="abm.button.volver"/>"/>
				</td>  
				<td align="left">
					<input type="button" class="boton"  onclick="submitForm('imprimir', '<bean:write name="ordConBasImpAdapterVO" property="ordConBasImp.idView" />');" 
								value="<bean:message bundle="base" key="abm.button.imprimir"/>"/>		
	             </td>				
				 <td align="right">
		   			<input type="button" class="boton" onclick="submitForm('irAjustarPeriodos', '<bean:write name="ordConBasImpAdapterVO" property="ordConBasImp.ordenControl.idView" />');"
		   				value="<bean:message bundle="ef" key="ef.ordConBasImpAdapter.button.ajustarPeriodo"/>"/>
				</td>					
							
			</tr>
		</table>
		<input type="hidden" name="name"  value="<bean:write name='ordConBasImpAdapterVO' property='name'/>" id="name"/>
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
<!-- ordConBasImpAdapter.jsp -->