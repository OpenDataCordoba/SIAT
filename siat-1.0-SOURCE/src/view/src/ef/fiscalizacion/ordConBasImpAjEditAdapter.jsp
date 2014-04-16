<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" src="<%= request.getContextPath()%>/base/calculator.js"></script>

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
		   			<input type="button" class="boton" onclick="submitForm('volver', '<bean:write name="ordConBasImpAdapterVO" property="ordConBasImp.idView" />');"
		   				value="<bean:message bundle="base" key="abm.button.volver"/>"/>					
				</td>
			</tr>
		</table>
		
		<!-- ordConBasImp -->
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.ordConBasImp.label"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="ef" key="ef.fuenteInfo.label"/>: </label></td>
					<td class="normal"><bean:write name="ordConBasImpAdapterVO" property="ordConBasImp.compFuente.plaFueDat.tituloView"/></td>
					
				</tr>
				<tr>
					<td><label><bean:message bundle="ef" key="ef.ordConBasImp.periodoDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="ordConBasImpAdapterVO" property="ordConBasImp.periodoAnioDesdeView"/></td>
					
				</tr>
				<tr>
					<td><label><bean:message bundle="ef" key="ef.ordConBasImp.periodoHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="ordConBasImpAdapterVO" property="ordConBasImp.periodoAnioHastaView"/></td>
					
				</tr>																
			</table>
		</fieldset>	
		<!-- ordConBasImp -->
				
			
		<!-- plaFueDatCom -->
		<div id="listaPlaFueDatCom" class="scrolable" style="height: 350px;" align="center">
			<table>
				<tr>
					<td><label><bean:message bundle="ef" key="ef.ordConBasImpAdapter.msg.PeriodoModifAjuste"/>: </label></td>
					<td class="normal">
						<!-- periodo/anio -->
						<bean:write name="ordConBasImpAdapterVO" property="plaFueDatCom.periodoView"/>/
						<bean:write name="ordConBasImpAdapterVO" property="plaFueDatCom.anioView"/>					
					</td>					
				</tr>
				<tr>
					<td colspan="2"  align="center">
					
						<table border="0" cellpadding="0" cellspacing="1" align="center">
				    		<th>Columna</th>
			    			<th>Valor</th>
			    			<th>Ajuste</th>				    		
				    	<tbody>
				    			    		
				    		<tr>
				    			<!-- itera la columnas -->
				    			<td>
				    				<table border="0" cellpadding="0" cellspacing="12">
					    				<tbody>		    				
											<logic:iterate id="compFuenteColVO" name="ordConBasImpAdapterVO" property="ordConBasImp.compFuente.listCompFuenteCol">
												<logic:equal name="compFuenteColVO" property="oculta.id" value="0">
													<logic:equal name="compFuenteColVO" property="sumaEnTotal.id" value="1">
														<tr>							
															<td class="normal">
																<bean:write name="compFuenteColVO" property="colName"/>																										
															</td>										
														</tr>	
													</logic:equal>
												</logic:equal>		    	
											</logic:iterate>
					    				</tbody>
				    				</table>
				    			</td>
				    			
				    			<!-- itera los valores -->
				    			<td>
				    				<table border="0" cellpadding="0" cellspacing="12">
					    				<tbody>		    				
																							
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col1">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<bean:write name="ordConBasImpAdapterVO" property="plaFueDatCom.col1" bundle="base" formatKey="general.format.currency"/>									
												</td>
											</tr>	
										</logic:present>
			
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col2">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<bean:write name="ordConBasImpAdapterVO" property="plaFueDatCom.col2" bundle="base" formatKey="general.format.currency"/>									
												</td>
											</tr>	
										</logic:present>
			
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col3">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<bean:write name="ordConBasImpAdapterVO" property="plaFueDatCom.col3" bundle="base" formatKey="general.format.currency"/>									
												</td>
											</tr>	
										</logic:present>
			
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col4">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<bean:write name="ordConBasImpAdapterVO" property="plaFueDatCom.col4" bundle="base" formatKey="general.format.currency"/>									
												</td>
											</tr>	
										</logic:present>
			
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col5">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<bean:write name="ordConBasImpAdapterVO" property="plaFueDatCom.col5" bundle="base" formatKey="general.format.currency"/>									
												</td>
											</tr>	
										</logic:present>
			
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col6">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<bean:write name="ordConBasImpAdapterVO" property="plaFueDatCom.col6" bundle="base" formatKey="general.format.currency"/>									
												</td>
											</tr>	
										</logic:present>
			
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col7">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<bean:write name="ordConBasImpAdapterVO" property="plaFueDatCom.col7" bundle="base" formatKey="general.format.currency"/>									
												</td>
											</tr>	
										</logic:present>
			
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col8">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<bean:write name="ordConBasImpAdapterVO" property="plaFueDatCom.col8" bundle="base" formatKey="general.format.currency"/>									
												</td>
											</tr>	
										</logic:present>
			
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col9">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<bean:write name="ordConBasImpAdapterVO" property="plaFueDatCom.col9" bundle="base" formatKey="general.format.currency"/>									
												</td>
											</tr>	
										</logic:present>
			
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col10">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<bean:write name="ordConBasImpAdapterVO" property="plaFueDatCom.col10" bundle="base" formatKey="general.format.currency"/>									
												</td>
											</tr>	
										</logic:present>
			
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col11">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<bean:write name="ordConBasImpAdapterVO" property="plaFueDatCom.col11" bundle="base" formatKey="general.format.currency"/>									
												</td>
											</tr>	
										</logic:present>
			
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col12">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<bean:write name="ordConBasImpAdapterVO" property="plaFueDatCom.col12" bundle="base" formatKey="general.format.currency"/>									
												</td>
											</tr>	
										</logic:present>
			
									
					    				</tbody>
				    				</table>	    			
				    			</td>
				    			
				    			<!-- itera los ajustes -->
				    			<td>
				    				<table border="0" cellpadding="0" cellspacing="1">
					    				<tbody>		    				
																							
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col1">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<html:text name="ordConBasImpAdapterVO" property="plaFueDatCom.aj1" size="8"/>									
												</td>
											</tr>	
										</logic:present>
			
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col2">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<html:text name="ordConBasImpAdapterVO" property="plaFueDatCom.aj2" size="8" />									
												</td>
											</tr>	
										</logic:present>
			
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col3">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<html:text name="ordConBasImpAdapterVO" property="plaFueDatCom.aj3" size="8" />									
												</td>
											</tr>	
										</logic:present>
			
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col4">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<html:text name="ordConBasImpAdapterVO" property="plaFueDatCom.aj4" size="8" />									
												</td>
											</tr>	
										</logic:present>
			
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col5">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<html:text name="ordConBasImpAdapterVO" property="plaFueDatCom.aj5" size="8" />									
												</td>
											</tr>	
										</logic:present>
			
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col6">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<html:text name="ordConBasImpAdapterVO" property="plaFueDatCom.aj6" size="8" />									
												</td>
											</tr>	
										</logic:present>
			
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col7">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<html:text name="ordConBasImpAdapterVO" property="plaFueDatCom.aj7" size="8" />									
												</td>
											</tr>	
										</logic:present>
			
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col8">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<html:text name="ordConBasImpAdapterVO" property="plaFueDatCom.aj8" size="8" />									
												</td>
											</tr>	
										</logic:present>
			
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col9">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<html:text name="ordConBasImpAdapterVO" property="plaFueDatCom.aj9" size="8" />									
												</td>
											</tr>	
										</logic:present>
			
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col10">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<html:text name="ordConBasImpAdapterVO" property="plaFueDatCom.aj10" size="8" />									
												</td>
											</tr>	
										</logic:present>
			
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col11">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<html:text name="ordConBasImpAdapterVO" property="plaFueDatCom.aj11" size="8" />									
												</td>
											</tr>	
										</logic:present>
			
										<logic:present name="ordConBasImpAdapterVO" property="plaFueDatCom.col12">
											<tr>
												<td class="datos" nowrap="nowrap" >
													<html:text name="ordConBasImpAdapterVO" property="plaFueDatCom.aj12" size="8" />									
												</td>
											</tr>	
										</logic:present>
			
									
					    				</tbody>
				    				</table>
				    			</td>	
				    		</tr>
				    	</tbody>					    		
						</table>
					</td>
				</tr>	
			</table>
			
		</div>	
		<!-- plaFueDatCom -->
				
							
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left" width="50%">
		   			<input type="button" class="boton" onclick="submitForm('volver', '<bean:write name="ordConBasImpAdapterVO" property="ordConBasImp.idView" />');"
		   				value="<bean:message bundle="base" key="abm.button.volver"/>"/>
				</td>   	    
				<td align="left" width="50%">
		   			<input type="button" class="boton" onclick="submitForm('updateAjustes', '<bean:write name="ordConBasImpAdapterVO" property="ordConBasImp.idView" />');"
		   				value="<bean:message bundle="ef" key="ef.ordConBasImpAdapter.button.cargarAjustes"/>"/>
				</td>				
								
			</tr>
		</table>
	   	
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
<!-- ordConBasImpAjEditAdapter.jsp -->