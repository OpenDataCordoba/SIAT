<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/BuscarOpeInvCon.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="ef" key="ef.opeInvConSearchPage.title"/></h1>	
				
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="opeInvConSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="ef" key="ef.opeInvCon.label"/>
					</logic:equal>
					<logic:notEqual name="opeInvConSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="ef" key="ef.opeInvConSearchPage.legend"/>
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
		
	<!-- OpeInv -->
	<bean:define id="opeInvVO" name="opeInvConSearchPageVO" property="opeInvCon.opeInv"/>
	<%@include file="/ef/investigacion/includeOpeInvView.jsp" %>	
	<!-- OpeInv -->
			
	<a name="busqueda">&nbsp;</a>		
	<!-- Filtro -->
	<fieldset>
	<legend><bean:message bundle="base" key="base.parametrosBusqueda"/></legend>
		<table class="tabladatos">
		<tr>	
			<td><label><bean:message bundle="ef" key="ef.estadoOpeInvCon.label"/>: </label></td>
			<td class="normal">
				<html:select name="opeInvConSearchPageVO" property="opeInvCon.estadoOpeInvCon.id" styleClass="select">
					<html:optionsCollection name="opeInvConSearchPageVO" property="listEstadoOpeInvCon" label="desEstadoOpeInvCon" value="id" />
				</html:select>
			</td>					
		</tr>
		<tr>
			<td><label><bean:message bundle="ef" key="ef.opeInvConSearchPage.busquedas.title"/></td>
			<td class="normal">
				<html:select name="opeInvConSearchPageVO" property="opeInvCon.opeInvBus.id" styleClass="select" style="width: 70%">
					<html:optionsCollection name="opeInvConSearchPageVO" property="listOpeInvBus" label="descripcion" value="id"/>
				</html:select>
			</td>
		</tr>
			<!-- <#Filtros#> -->
		</table>
			
		<p align="center">
		  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
				<bean:message bundle="base" key="abm.button.buscar"/>
			</html:button>
		</p>
	</fieldset>	
	<!-- Fin Filtro -->
		
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
			<logic:notEmpty  name="opeInvConSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="ef" key="ef.opeInvConSearchPage.listContr.label"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="opeInvConSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Excluir de seleccion -->
								<th><bean:message bundle="pad" key="pad.persona.label"/></th> <!-- Contribuyente -->
								<th><bean:message bundle="ef" key="ef.estadoOpeInvCon.label"/></th> <!-- Estado -->
							</logic:notEqual>
							<!-- <#ColumnTitles#> -->
						</tr>
							
						<logic:iterate id="OpeInvConVO" name="opeInvConSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="opeInvConSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="OpeInvConVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="opeInvConSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="opeInvConSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="OpeInvConVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="OpeInvConVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="opeInvConSearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="OpeInvConVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="OpeInvConVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="OpeInvConVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="opeInvConSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
	
									<!-- Exlcuir de seleccion-->								
									<td>
										<logic:equal name="opeInvConSearchPageVO" property="excluirDeSelecEnabled" value="enabled">
											<logic:equal name="OpeInvConVO" property="excluirDeSelecBussEnabled" value="true">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('excluirDeSelec', '<bean:write name="OpeInvConVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="ef" key="ef.opeInvConSearchPage.button.exluirDeSeleccion"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="OpeInvConVO" property="excluirDeSelecBussEnabled" value="true">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="opeInvConSearchPageVO" property="excluirDeSelecEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
										</logic:notEqual>
									</td>

								</logic:notEqual>
								<!-- <#ColumnFiedls#> -->
								<td><bean:write name="OpeInvConVO" property="datosContribuyente"/></td>
								<td><bean:write name="OpeInvConVO" property="estadoOpeInvCon.desEstadoOpeInvCon"/></td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="opeInvConSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="opeInvConSearchPageVO" property="listResult">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="ef" key="ef.opeInvConSearchPage.listContr.label"/></caption>
                	<tbody>
						<tr><td align="center">
							<bean:message bundle="base" key="base.resultadoVacio"/>
						</td></tr>
					</tbody>			
				</table>
			</logic:empty>			
	</div>
	<!-- Fin Resultado Filtro -->

	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left" width="50%">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
  	    			<logic:equal name="opeInvConSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="opeInvConSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="opeInvConSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="opeInvConSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="opeInvConSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
							</logic:equal>
					</logic:equal>				
			</td>
			<logic:equal name="opeInvConSearchPageVO" property="viewResult" value="false">
				<td align="right" width="50%">
  	    			<logic:equal name="opeInvConSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarMasivoEnabled" name="opeInvConSearchPageVO" property="agregarMasivoEnabled"/>
						<input type="button" <%=agregarMasivoEnabled%> class="boton" 
							onClick="submitForm('agregarMasivo', '0');" 
							value="<bean:message bundle="ef" key="ef.opeInvConSearchPage.button.agregarMasivo"/>"/>
					</logic:equal>
  	    			<logic:equal name="opeInvConSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="eliminarMasivoEnabled" name="opeInvConSearchPageVO" property="eliminarMasivoEnabled"/>
						<input type="button" <%=eliminarMasivoEnabled%> class="boton" 
							onClick="submitForm('eliminarMasivo', '0');" 
							value="<bean:message bundle="ef" key="ef.opeInvConSearchPage.button.eliminarMasivo"/>"/>
					</logic:equal>					
				</td>								
			</logic:equal>
		</tr>
	</table>
		
	<input type="hidden" name="method" value=""/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<logic:present name="irA">
		<script type="text/javascript">document.location = document.URL + '#<bean:write name="irA"/>';</script>
	</logic:present>
	
	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
