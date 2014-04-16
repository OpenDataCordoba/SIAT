<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/BuscarOpeInv.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="ef" key="ef.opeInvSearchPage.title"/></h1>	
			
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="opeInvSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="ef" key="ef.opeInv.label"/>
					</logic:equal>
					<logic:notEqual name="opeInvSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="ef" key="ef.opeInvSearchPage.legend"/>
					</logic:notEqual>		
				</p>			
			</td>				
			<td align="right">
	 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
		
	<!-- Filtro -->
	<fieldset>
	<legend><bean:message bundle="base" key="base.parametrosBusqueda"/></legend>
		<table class="tabladatos">
		<tr>	
			<td><label><bean:message bundle="ef" key="ef.planFiscal.label"/>: </label></td>
			<td class="normal">
				<html:select name="opeInvSearchPageVO" property="opeInv.planFiscal.id" styleClass="select">
					<html:optionsCollection name="opeInvSearchPageVO" property="listPlanFiscal" label="desPlanFiscal" value="id" />
				</html:select>
			</td>					
		</tr>
		<tr>	
			<td><label><bean:message bundle="ef" key="ef.estOpeInv.label"/>: </label></td>
			<td class="normal">
				<html:select name="opeInvSearchPageVO" property="opeInv.estOpeInv.id" styleClass="select">
					<html:optionsCollection name="opeInvSearchPageVO" property="listEstOpeInv" label="desEstOpeInv" value="id" />
				</html:select>
			</td>					
		</tr>
			<!-- <#Filtros#> -->
		</table>
			
		<p align="center">
		  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');">
				<bean:message bundle="base" key="abm.button.limpiar"/>
			</html:button>
			&nbsp;
			<html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('baseImprimir', '1');">
				<bean:message bundle="base" key="abm.button.imprimir"/>
			</html:button>
			&nbsp;
		  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
				<bean:message bundle="base" key="abm.button.buscar"/>
			</html:button>
		</p>
	</fieldset>	
	<!-- Fin Filtro -->
		
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="opeInvSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="opeInvSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="opeInvSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->
								<th width="1">&nbsp;</th> <!-- Contribuyentes -->								
							</logic:notEqual>
							<th align="left"><bean:message bundle="ef" key="ef.opeInv.desOpeInv.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.planFiscal.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.opeInv.fechaInicio.label"/></th>
							<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
							<!-- <#ColumnTitles#> -->
						</tr>
							
						<logic:iterate id="OpeInvVO" name="opeInvSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="opeInvSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="OpeInvVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="opeInvSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="opeInvSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="OpeInvVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="OpeInvVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="opeInvSearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="OpeInvVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="OpeInvVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="OpeInvVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="opeInvSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
	
									<!-- Eliminar-->								
									<td>
										<logic:equal name="opeInvSearchPageVO" property="eliminarEnabled" value="enabled">
											<logic:equal name="OpeInvVO" property="eliminarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="OpeInvVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="OpeInvVO" property="eliminarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="opeInvSearchPageVO" property="eliminarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>
									
									<!-- Contribuyentes -->									
									<td>
										<logic:equal name="opeInvSearchPageVO" property="contribuyenteBussEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('admContribuyentes', '<bean:write name="OpeInvVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="ef" key="ef.opeInvSearchPage.button.contribuyentes"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/contribuyentes0.gif"/>
												</a>
										</logic:equal>
										<logic:notEqual name="opeInvSearchPageVO" property="contribuyenteBussEnabled" value="enabled">										
											<img title="<bean:message bundle="ef" key="ef.opeInvSearchPage.button.contribuyentes"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/contribuyentes1.gif"/>
										</logic:notEqual>

									</td>									
								</logic:notEqual>
								<td><bean:write name="OpeInvVO" property="desOpeInv"/>&nbsp;</td>
								<td><bean:write name="OpeInvVO" property="planFiscal.desPlanFiscal"/>&nbsp;</td>
								<td><bean:write name="OpeInvVO" property="fechaInicioView"/>&nbsp;</td>
								<td><bean:write name="OpeInvVO" property="estOpeInv.desEstOpeInv"/>&nbsp;</td>
								<!-- <#ColumnFiedls#> -->
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="opeInvSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="opeInvSearchPageVO" property="listResult">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
                	<tbody>
						<tr><td align="center">
							<bean:message bundle="base" key="base.resultadoVacio"/>
						</td></tr>
					</tbody>			
				</table>
			</logic:empty>
		</logic:equal>			
	</div>
	<!-- Fin Resultado Filtro -->

	<table class="tablabotones">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<logic:equal name="opeInvSearchPageVO" property="viewResult" value="true">
  	    		<td align="right">
  	    			<logic:equal name="opeInvSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="opeInvSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="opeInvSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="opeInvSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="opeInvSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
							</logic:equal>
					</logic:equal>
				</td>
			</logic:equal>
		</tr>
	</table>
	<input type="hidden" name="name"  value="<bean:write name='opeInvSearchPageVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
				
	<input type="hidden" name="method" value=""/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
