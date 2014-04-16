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
						<th width="1">&nbsp;</th> <!-- modificar alicuotas -->
						<th width="1"><bean:message bundle="ef" key="ef.plaFueDatCom.periodo.label"/></th>
						<th width="1"><bean:message bundle="ef" key="ef.plaFueDatCom.totalPais"/></th>
						<th width="1"><bean:message bundle="ef" key="ef.plaFueDatCom.coefStaFe"/></th>
						<th width="1"><bean:message bundle="ef" key="ef.plaFueDatCom.baseStaFe"/></th>
						<th width="1"><bean:message bundle="ef" key="ef.plaFueDatCom.coefRosario"/></th>
						<th width="1"><bean:message bundle="ef" key="ef.plaFueDatCom.baseRosario"/></th>													
					</tr>
										
					<!-- itera los detalles -->
					<logic:iterate id="PlaFueDatComVO" name="ordConBasImpAdapterVO" property="ordConBasImp.compFuente.listPlaFueDatCom">
												
						<tr>
							<bean:define id="idPlafueDatCom" name="PlaFueDatComVO" property="id"/>
							<logic:equal name="ordConBasImpAdapterVO" property="plaFueDatCom.idView" value="<%=idPlafueDatCom.toString()%>">
								<td>
									<img title="<bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.button.cargarAjustes"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>																
								</td>
								
								<td>
									<!-- periodo/anio -->
									<bean:write name="PlaFueDatComVO" property="periodoView"/>/
									<bean:write name="PlaFueDatComVO" property="anioView"/>
								</td>								
								
								<td class="datos" nowrap="nowrap">
									<bean:write name="PlaFueDatComVO" property="totalPaisView" />
								</td>
								
								<td class="datos">
									<html:text name="ordConBasImpAdapterVO" property="plaFueDatCom.coefStaFe" size="5"/>
								</td>
	
								<td class="datos" nowrap="nowrap">
									<bean:write name="PlaFueDatComVO" property="baseStaFeView"/>
								</td>
															
								<td class="datos">
									<html:text name="ordConBasImpAdapterVO" property="plaFueDatCom.coefRosario" size="5"/>
								</td>
								
								<td class="datos" nowrap="nowrap">
									<bean:write name="PlaFueDatComVO" property="baseRosarioView" />
								</td>							
							</logic:equal>
							
							<logic:notEqual name="ordConBasImpAdapterVO" property="plaFueDatCom.idView" value="<%=idPlafueDatCom.toString()%>">
								<!-- modificar alicuotas -->
								<td>
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('irModificarAlicuota', '<bean:write name="PlaFueDatComVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.button.cargarAjustes"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
									</a>							
								</td>
								
								<td>
									<!-- periodo/anio -->
									<bean:write name="PlaFueDatComVO" property="periodoView"/>/
									<bean:write name="PlaFueDatComVO" property="anioView"/>
								</td>								
								
								<td class="datos" nowrap="nowrap">
									<bean:write name="PlaFueDatComVO" property="totalPaisView" />
								</td>
								
								<td class="datos" nowrap="nowrap">
									<bean:write name="PlaFueDatComVO" property="coefStaFeView" />
								</td>
	
								<td class="datos" nowrap="nowrap">
									<bean:write name="PlaFueDatComVO" property="baseStaFeView" />
								</td>
															
								<td class="datos" nowrap="nowrap">
									<bean:write name="PlaFueDatComVO" property="coefRosarioView" />
								</td>
								
								<td class="datos" nowrap="nowrap">
									<bean:write name="PlaFueDatComVO" property="baseRosarioView" />
								</td>
							</logic:notEqual>								
							
						</tr>						
					</logic:iterate>
																				
			</table>
		</div>	
		<!-- plaFueDatCom -->
		
		<table class="tabladatos">
			<tr>
				<td>
					<label>
						<bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.coeficiente.label"/>:
					</label>
				</td>
				<td class="normal">
					<html:text name="ordConBasImpAdapterVO" property="coeficienteView" size="8"/>
				</td>
				<td>
					<html:select name="ordConBasImpAdapterVO" property="valorTipoCoef">
						<html:optionsCollection name="ordConBasImpAdapterVO" property="listTipoCoeficiente" label="etiqueta" value="valor"/>
					</html:select>
				</td>
				<td>
					<label>
						<bean:message bundle="ef" key="ef.ordConBasImpAdapter.rango.label"/>
					</label>
				</td>
				<td class="normal">
					<html:select name="ordConBasImpAdapterVO" property="perOrdDesde">
						<html:optionsCollection name="ordConBasImpAdapterVO" property="listPeriodoDesde" label="periodoAnioView" value="periodoAnioView"/>
					</html:select>
				</td>
				<td class="normal">
					<html:select name="ordConBasImpAdapterVO" property="perOrdHasta">
						<html:optionsCollection name="ordConBasImpAdapterVO" property="listPeriodoHasta" label="periodoAnioView" value="periodoAnioView"/>
					</html:select>
				</td>
				<td>
					<input type="button" class="boton" onclick="submitForm('modificarCoeficientes', '');"
			   				value="<bean:message bundle="ef" key="ef.ordConBasImpAdapter.button.modificarCoeficientes"/>"/>
				</td>
			</tr>
		</table>
				
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left" width="50%">
		   			<input type="button" class="boton" onclick="submitForm('volver', '<bean:write name="ordConBasImpAdapterVO" property="ordConBasImp.ordenControl.idView" />');"
		   				value="<bean:message bundle="base" key="abm.button.volver"/>"/>
				</td>   	    
				
				
				<logic:greaterThan name="ordConBasImpAdapterVO" property="plaFueDatCom.id" value="0">
					<td align="right" width="50%">
			   			<input type="button" class="boton" onclick="submitForm('modificarAlicuotas', '<bean:write name="ordConBasImpAdapterVO" property="ordConBasImp.ordenControl.idView" />');"
			   				value="<bean:message bundle="ef" key="ef.ordConBasImpAdapter.button.modificarAlicuotas"/>"/>
					</td>
				</logic:greaterThan>
													
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
<!-- ordConBasImpAlicuotasAdapter.jsp -->