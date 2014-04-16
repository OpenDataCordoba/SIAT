<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/ef/AdministrarPlaFueDat.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.plaFueDatAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<input type="button" class="boton" onclick="submitForm('volver', '');"
		   				value="<bean:message bundle="base" key="abm.button.volver"/>"/>					
				</td>
			</tr>
		</table>
		
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.plaFueDat.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="ef" key="ef.fuenteInfo.label"/>: </label></td>
					<td class="normal"><bean:write name="plaFueDatAdapterVO" property="plaFueDat.fuenteInfo.nombreFuente"/></td>
					
					<td><label><bean:message bundle="ef" key="ef.plaFueDat.observacion.label"/>: </label></td>
					<td class="normal"><bean:write name="plaFueDatAdapterVO" property="plaFueDat.observacion" /></td>
				</tr>												
			</table>
		</fieldset>	
				
		<!-- planilla -->		
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.plaFueDat.Planilla.title"/></legend>
			
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
			<caption><bean:message bundle="ef" key="ef.plaFueDat.listPlaFueDatCol.label"/></caption>
	    	<tbody>
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th width="1"><bean:message bundle="ef" key="ef.plaFueDat.periodo.label"/></th> <!-- Periodo -->
					<!-- genera la cabecera con la lista de PlaFueDatCol -->	
						<logic:iterate id="PlaFueDatColVO" name="plaFueDatAdapterVO" property="plaFueDat.listPlaFueDatCol">	    	
							<th align="left">
								<bean:write name="PlaFueDatColVO" property="colName"/>
							</th>
						</logic:iterate>							
					</tr>
					
					
					<!-- itera los detalles -->
					<bean:define id="idModificar" name="plaFueDatAdapterVO" property="plaFueDatDet.idView"/>
					<logic:iterate id="PlaFueDatDetVO" name="plaFueDatAdapterVO" property="plaFueDat.listPlaFueDatDet">
												
						<logic:notEqual name="PlaFueDatDetVO" property="idView" value="<%=idModificar.toString() %>">
						
							<tr>
								<!-- Modificar-->								
								<td>
									<logic:equal name="plaFueDatAdapterVO" property="modificarPlaFueDatDetEnabled" value="enabled">
										<logic:equal name="PlaFueDatDetVO" property="modificarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('irModificarPlaFueDatDet', '<bean:write name="PlaFueDatDetVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="PlaFueDatDetVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="plaFueDatAdapterVO" property="modificarPlaFueDatDetEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</td>
								
								<!-- Eliminar-->								
								<td>
									<logic:equal name="plaFueDatAdapterVO" property="eliminarPlaFueDatDetEnabled" value="enabled">
										<logic:equal name="PlaFueDatDetVO" property="eliminarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarPlaFueDatDet', '<bean:write name="PlaFueDatDetVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="PlaFueDatDetVO" property="eliminarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="plaFueDatAdapterVO" property="eliminarPlaFueDatDetEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</td>
								
								<td>
									<!-- periodo/anio -->
									<bean:write name="PlaFueDatDetVO" property="periodoView"/>/
									<bean:write name="PlaFueDatDetVO" property="anioView"/>
								</td>
								
								
								<logic:present name="PlaFueDatDetVO" property="col1">
									<td><bean:write name="PlaFueDatDetVO" property="col1View"/></td>
								</logic:present>
	
								<logic:present name="PlaFueDatDetVO" property="col2" >
									<td><bean:write name="PlaFueDatDetVO" property="col2View"/></td>
								</logic:present>
	
								<logic:present name="PlaFueDatDetVO" property="col3" >
									<td><bean:write name="PlaFueDatDetVO" property="col3View"/></td>
								</logic:present>
	
								<logic:present name="PlaFueDatDetVO" property="col4" >
									<td><bean:write name="PlaFueDatDetVO" property="col4View"/></td>
								</logic:present>
																						
								<logic:present name="PlaFueDatDetVO" property="col5" >
									<td><bean:write name="PlaFueDatDetVO" property="col5View"/></td>
								</logic:present>
	
								<logic:present name="PlaFueDatDetVO" property="col6" >
									<td><bean:write name="PlaFueDatDetVO" property="col6View"/></td>
								</logic:present>
	
								<logic:present name="PlaFueDatDetVO" property="col7" >
									<td><bean:write name="PlaFueDatDetVO" property="col7View"/></td>
								</logic:present>
	
								<logic:present name="PlaFueDatDetVO" property="col8" >
									<td><bean:write name="PlaFueDatDetVO" property="col8View"/></td>
								</logic:present>
								
								<logic:present name="PlaFueDatDetVO" property="col9" >
									<td><bean:write name="PlaFueDatDetVO" property="col9View"/></td>
								</logic:present>
	
								<logic:present name="PlaFueDatDetVO" property="col10" >
									<td><bean:write name="PlaFueDatDetVO" property="col10View"/></td>
								</logic:present>
								
								<logic:present name="PlaFueDatDetVO" property="col11" >
									<td><bean:write name="PlaFueDatDetVO" property="col11View"/></td>
								</logic:present>
	
								<logic:present name="PlaFueDatDetVO" property="col12" >
									<td><bean:write name="PlaFueDatDetVO" property="col12View"/></td>
								</logic:present>																																										
							</tr>		
						</logic:notEqual>
						
						<logic:equal name="PlaFueDatDetVO" property="idView" value="<%=idModificar.toString() %>">
							<tr>
								<!-- Modificar-->								
								<td><img border="0" src="<%=request.getContextPath()%>/images/iconos/modificando0.gif"/></td>
								
								<!-- Eliminar-->								
								<td><img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/></td>
								
								<td>
									<!-- periodo/anio -->
									<bean:write name="PlaFueDatDetVO" property="periodoView"/>/
									<bean:write name="PlaFueDatDetVO" property="anioView"/>
								</td>
								
								
								<logic:present name="PlaFueDatDetVO" property="col1">
									<td><html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col1"/></td>
								</logic:present>
	
								<logic:present name="PlaFueDatDetVO" property="col2" >
									<td><html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col2"/></td>
								</logic:present>
	
								<logic:present name="PlaFueDatDetVO" property="col3" >
									<td><html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col3"/></td>
								</logic:present>
	
								<logic:present name="PlaFueDatDetVO" property="col4" >
									<td><html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col4"/></td>
								</logic:present>
																						
								<logic:present name="PlaFueDatDetVO" property="col5" >
									<td><html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col5"/></td>
								</logic:present>
	
								<logic:present name="PlaFueDatDetVO" property="col6" >
									<td><html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col6"/></td>
								</logic:present>
	
								<logic:present name="PlaFueDatDetVO" property="col7" >
									<td><html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col7"/></td>
								</logic:present>
	
								<logic:present name="PlaFueDatDetVO" property="col8" >
									<td><html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col8"/></td>
								</logic:present>
								
								<logic:present name="PlaFueDatDetVO" property="col9" >
									<td><html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col9"/></td>
								</logic:present>
	
								<logic:present name="PlaFueDatDetVO" property="col10" >
									<td><html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col10"/></td>
								</logic:present>
								
								<logic:present name="PlaFueDatDetVO" property="col11" >
									<td><html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col11"/></td>
								</logic:present>
	
								<logic:present name="PlaFueDatDetVO" property="col12" >
									<td><html:text name="plaFueDatAdapterVO" property="plaFueDatDet.col12"/></td>
								</logic:present>																																										
							</tr>
						</logic:equal>					
					</logic:iterate>
												
			</table>
		</fieldset>
				
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
		   			<input type="button" class="boton" onclick="submitForm('volver', '');"
		   				value="<bean:message bundle="base" key="abm.button.volver"/>"/>
				</td>
				<logic:greaterThan name="plaFueDatAdapterVO" property="plaFueDatDet.id" value="0">
					<td align="center">
			   			<input type="button" class="boton" onclick="submitForm('modificarPlaFueDatDet', '');"
			   				value="<bean:message bundle="ef" key="ef.plaFueDatAdapter.button.planilla.modificarRegistro"/>"/>
					</td>   	    			
				</logic:greaterThan>
			</tr>
		</table>
	   	
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
<!-- plaFueDatPlanillaAdapter.jsp -->