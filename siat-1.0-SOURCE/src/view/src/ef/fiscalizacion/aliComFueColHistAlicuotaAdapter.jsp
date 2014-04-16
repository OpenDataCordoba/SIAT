<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/ef/AdministrarAliComFueCol.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.aliComFueColAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- DetAju -->
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.detAju.label"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="ef" key="ef.detAju.fecha.label"/>: </label></td>
					<td class="normal"><bean:write name="aliComFueColAdapterVO" property="detAju.fechaView"/></td>
					
					<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="aliComFueColAdapterVO" property="detAju.ordConCue.cuenta.numeroCuenta" /></td>
				</tr>
					<tr>
						<td><label><bean:message bundle="ef" key="ef.plaFueDatCol.label"/>: </label></td>
						<td class="normal"><bean:write name="aliComFueColAdapterVO" property="compFuenteCol.colName" /></td>						
					</tr>															
			</table>
		</fieldset>	
		<!-- DetAju -->
		
		<div id="listHist" class="scrolable" style="height: 350px;">		
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">			           
				<caption><bean:message bundle="ef" key="ef.aliComFueColAdapter.listHistorico.label"/></caption>
		    	<tbody>
		    		
							<logic:notEmpty  name="aliComFueColAdapterVO" property="compFuenteCol.listAliComFueCol">	    	
						    	<tr>
									<th width="1">&nbsp;</th> <!-- modificar -->
									<th width="1">&nbsp;</th> <!-- eliminar -->
									<th align="left"><bean:message bundle="ef" key="ef.aliComFueColAdapter.vigenciaDesde.label"/></th>
									<th align="left"><bean:message bundle="ef" key="ef.aliComFueColAdapter.vigenciaHasta.label"/></th>
									<logic:notEqual name="aliComFueColAdapterVO" property="aliComFueCol.esOrdConCueEtur" value="true">
										<th align="left"><bean:message bundle="ef" key="ef.aliComFueCol.label"/></th>
										<th align="left"><bean:message bundle="ef" key="ef.aliComFueCol.cantidad.label"/></th>
										<th align="left"><bean:message bundle="ef" key="ef.aliComFueCol.valorUnitario.label"/></th>
									</logic:notEqual>
									<logic:equal  name="aliComFueColAdapterVO" property="aliComFueCol.esOrdConCueEtur" value="true">
										<th align="left"><bean:message bundle="ef" key="ef.aliComFueCol.actEtur.label"/></th>
										<th align="left"><bean:message bundle="ef" key="ef.aliComFueCol.radio.label"/></th>
									</logic:equal>
								</tr>
								<logic:iterate id="AliComFueColVO" name="aliComFueColAdapterVO" property="compFuenteCol.listAliComFueCol">
									<logic:equal name="aliComFueColAdapterVO" property="act" value="irModificar">
										<bean:define name="aliComFueColAdapterVO" property="aliComFueCol.id" id="idModificar"/>
										<logic:notEqual name="AliComFueColVO" property="idView" value="<%=idModificar.toString()%>">
											<tr>
												<!-- modificar -->
												<td>
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('irModificar', '<bean:write name="AliComFueColVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
													</a>
												</td>
												<!-- eliminar -->
												<td>
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="AliComFueColVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
													</a>											
												</td>
												<td><bean:write name="AliComFueColVO" property="periodoAnioDesdeView"/></td>
												<td><bean:write name="AliComFueColVO" property="periodoAnioHastaView"/></td>
												<logic:notEqual name="AliComFueColVO" property="esOrdConCueEtur" value="true">
													<td><bean:write name="AliComFueColVO" property="valorAlicuotaView" filter="false"/></td>
													<td><bean:write name="AliComFueColVO" property="cantidadView"/></td>
													<td><bean:write name="AliComFueColVO" property="valorUnitarioView"/></td>
												</logic:notEqual>
												<logic:equal name="AliComFueColVO" property="esOrdConCueEtur" value="true">
													<td><bean:write name="AliComFueColVO" property="tipoUnidad.codYDescripcion"/></td>
													<td><bean:write name="AliComFueColVO" property="radioView"/></td>
												</logic:equal>
											</tr>
										</logic:notEqual>
										<logic:equal name="AliComFueColVO" property="idView" value="<%=idModificar.toString()%>">
											<tr>
												<td><img border="0" src="<%=request.getContextPath()%>/images/iconos/modificando0.gif"/></td>												
												<td><img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/></td>
												
												<td>
													<html:text name="aliComFueColAdapterVO" property="aliComFueCol.periodoDesde" size="2" maxlength="2"/>/
													<html:text name="aliComFueColAdapterVO" property="aliComFueCol.anioDesde" size="4" maxlength="4"/>
													(mm/aaaa)				
												</td>			
												<td>
													<html:text name="aliComFueColAdapterVO" property="aliComFueCol.periodoHasta" size="2" maxlength="2"/>/				
											
													<html:text name="aliComFueColAdapterVO" property="aliComFueCol.anioHasta" size="4" maxlength="4"/>
													(mm/aaaa)			
												</td>	
												<logic:notEqual name="AliComFueColVO" property="esOrdConCueEtur" value="true">
													<td><html:text name="aliComFueColAdapterVO" property="aliComFueCol.valorAlicuota" size="6"></html:text></td>
													<td><html:text name="aliComFueColAdapterVO" property="aliComFueCol.cantidadView" size="6"></html:text></td>
													<td><html:text name="aliComFueColAdapterVO" property="aliComFueCol.valorUnitarioView" size="6"></html:text></td>
												</logic:notEqual>
												<logic:equal name="AliComFueColVO" property="esOrdConCueEtur" value="true">
													<td>
														<html:select name="aliComFueColAdapterVO" property="aliComFueCol.tipoUnidad.id" styleClass="select" style="width:200px">
															<html:optionsCollection name="aliComFueColAdapterVO" property="listTipoUnidad" label="codYDescripcion" value="id"/>
														</html:select>
													</td>
													<td>
														<html:text name="aliComFueColAdapterVO" property="aliComFueCol.radioView" size="6"/>
													</td>
												</logic:equal>
											</tr>
										</logic:equal>
									</logic:equal>
									<logic:notEqual name="aliComFueColAdapterVO" property="act" value="irModificar">
										<tr>
											<!-- modificar -->
											<td>
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('irModificar', '<bean:write name="AliComFueColVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</td>
											<!-- eliminar -->
											<td>
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="AliComFueColVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>											
											</td>
											<td><bean:write name="AliComFueColVO" property="periodoAnioDesdeView"/></td>
											<td><bean:write name="AliComFueColVO" property="periodoAnioHastaView"/></td>
											<logic:notEqual name="AliComFueColVO" property="esOrdConCueEtur" value="true">
												<td><bean:write name="AliComFueColVO" property="valorAlicuotaView" filter="false"/></td>
												<td><bean:write name="AliComFueColVO" property="cantidadView"/></td>
												<td><bean:write name="AliComFueColVO" property="valorUnitarioView"/></td>
											</logic:notEqual>
											<logic:equal name="AliComFueColVO" property="esOrdConCueEtur" value="true">
												<td><bean:write name="AliComFueColVO" property="tipoUnidad.codYDescripcion"/></td>
												<td><bean:write name="AliComFueColVO" property="radioView"/></td>
											</logic:equal>
										</tr>
									</logic:notEqual>	
								</logic:iterate>
								
								<logic:equal name="aliComFueColAdapterVO" property="act" value="irAgregar">
									<tr>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>
											<html:text name="aliComFueColAdapterVO" property="aliComFueCol.periodoDesde" size="2" maxlength="2"/>
											&nbsp;/&nbsp;
											<html:text name="aliComFueColAdapterVO" property="aliComFueCol.anioDesde" size="4" maxlength="4"/>
											&nbsp;(mm/aaaa)				
										</td>			
										<td>
											<html:text name="aliComFueColAdapterVO" property="aliComFueCol.periodoHasta" size="2" maxlength="2"/>
											&nbsp;/&nbsp;				
									
											<html:text name="aliComFueColAdapterVO" property="aliComFueCol.anioHasta" size="4" maxlength="4"/>
											&nbsp;(mm/aaaa)			
										</td>	
										<logic:notEqual name="aliComFueColAdapterVO" property="aliComFueCol.esOrdConCueEtur" value="true">
												<td><html:text name="aliComFueColAdapterVO" property="aliComFueCol.valorAlicuota" size="6"></html:text></td>
												<td><html:text name="aliComFueColAdapterVO" property="aliComFueCol.cantidadView" size="6"></html:text></td>
												<td><html:text name="aliComFueColAdapterVO" property="aliComFueCol.valorUnitarioView" size="6"></html:text></td>
											</logic:notEqual>
											<logic:equal name="aliComFueColAdapterVO" property="aliComFueCol.esOrdConCueEtur" value="true">
												<td>
													<html:select name="aliComFueColAdapterVO" property="aliComFueCol.tipoUnidad.id" styleClass="select" style="width:200px">
														<html:optionsCollection name="aliComFueColAdapterVO" property="listTipoUnidad" label="codYDescripcion" value="id"/>
													</html:select>
												</td>
												<td>
													<html:text name="aliComFueColAdapterVO" property="aliComFueCol.radioView" size="6"/>
												</td>
										</logic:equal>
												
									</tr>	
								</logic:equal>								
								
							</logic:notEmpty>
							<logic:empty  name="aliComFueColAdapterVO" property="compFuenteCol.listAliComFueCol">
								<logic:equal name="aliComFueColAdapterVO" property="act" value="irAgregar">
							    	<tr>
										<th width="1">&nbsp;</th> <!-- modificar -->
										<th width="1">&nbsp;</th> <!-- eliminar -->
										<th align="left"><bean:message bundle="ef" key="ef.aliComFueColAdapter.vigenciaDesde.label"/></th>
										<th align="left"><bean:message bundle="ef" key="ef.aliComFueColAdapter.vigenciaHasta.label"/></th>
										<logic:notEqual name="aliComFueColAdapterVO" property="aliComFueCol.esOrdConCueEtur" value="true">
											<th align="left"><bean:message bundle="ef" key="ef.aliComFueCol.label"/></th>
											<th align="left"><bean:message bundle="ef" key="ef.aliComFueCol.cantidad.label"/></th>
											<th align="left"><bean:message bundle="ef" key="ef.aliComFueCol.valorUnitario.label"/></th>
										</logic:notEqual>
										<logic:equal name="aliComFueColAdapterVO" property="aliComFueCol.esOrdConCueEtur" value="true">
											<th align="left"><bean:message bundle="ef" key="ef.aliComFueCol.actEtur.label"/></th>
											<th align="left"><bean:message bundle="ef" key="ef.aliComFueCol.radio.label"/></th>
										</logic:equal>
									</tr>
								
									<tr>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>
											<html:text name="aliComFueColAdapterVO" property="aliComFueCol.periodoDesde" size="2" maxlength="2"/>
											&nbsp;/&nbsp;
											<html:text name="aliComFueColAdapterVO" property="aliComFueCol.anioDesde" size="4" maxlength="4"/>
											&nbsp;(mm/aaaa)				
										</td>			
										<td>
											<html:text name="aliComFueColAdapterVO" property="aliComFueCol.periodoHasta" size="2" maxlength="2"/>
											&nbsp;/&nbsp;				
									
											<html:text name="aliComFueColAdapterVO" property="aliComFueCol.anioHasta" size="4" maxlength="4"/>
											&nbsp;(mm/aaaa)			
										</td>	
											<logic:notEqual name="aliComFueColAdapterVO" property="aliComFueCol.esOrdConCueEtur" value="true">
												<td><html:text name="aliComFueColAdapterVO" property="aliComFueCol.valorAlicuota" size="6"></html:text></td>
												<td><html:text name="aliComFueColAdapterVO" property="aliComFueCol.cantidadView" size="6"></html:text></td>
												<td><html:text name="aliComFueColAdapterVO" property="aliComFueCol.valorUnitarioView" size="6"></html:text></td>
											</logic:notEqual>
											<logic:equal name="aliComFueColAdapterVO" property="aliComFueCol.esOrdConCueEtur" value="true">
												<td>
													<html:select name="aliComFueColAdapterVO" property="aliComFueCol.tipoUnidad.id" styleClass="select" style="width:200px">
														<html:optionsCollection name="aliComFueColAdapterVO" property="listTipoUnidad" label="codYDescripcion" value="id"/>
													</html:select>
												</td>
												<td>
													<html:text name="aliComFueColAdapterVO" property="aliComFueCol.radioView" size="6"/>
												</td>
											</logic:equal>
									</tr>	
								</logic:equal>
								<logic:notEqual name="aliComFueColAdapterVO" property="act" value="irAgregar">							
									<tr><td align="center">
									<bean:message bundle="base" key="base.noExistenRegitros"/>
									</td></tr>
								</logic:notEqual>	
							</logic:empty>						
				</tbody>
			</table>
		</div>	
		
				
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left" width="33%">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<logic:notEqual name="aliComFueColAdapterVO" property="act" value="irAgregar">
					<td align="right" width="33%">
						<bean:define id="agregarAliComFueColEnabled" name="aliComFueColAdapterVO" property="agregarAliComFueColEnabled"/>					
				   	    <input type="button" <%=agregarAliComFueColEnabled%> class="boton" onclick="submitForm('irAgregar', '<bean:write name="aliComFueColAdapterVO" property="compFuenteCol.idView"/>');"
				   	    	value="<bean:message bundle="base" key="abm.button.agregar"/>"/>	
					</td>
				</logic:notEqual>
				<logic:equal name="aliComFueColAdapterVO" property="act" value="irAgregar">
					<td align="right" width="33%">
						<input type="button" class="boton" onclick="submitForm('agregar', '');"
				   	    	value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
				   	    				
					</td>
				</logic:equal>	
				<logic:equal name="aliComFueColAdapterVO" property="act" value="irModificar">
					<td>
						<input type="button" class="boton" onclick="submitForm('modificar', '');"
				   	    	value="<bean:message bundle="base" key="abm.button.modificar"/>"/>
				   	    				
					</td>
				</logic:equal>				
			</tr>
		</table>
	   	
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
<!-- aliComFueColHistAlicuotaAdapter.jsp -->