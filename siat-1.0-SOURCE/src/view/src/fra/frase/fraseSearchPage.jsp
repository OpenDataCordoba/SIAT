<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/fra/BuscarFrase.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="fra" key="fra.fraseSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">&nbsp;</td>				
			<td align="right">
	 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
		
	<p>
		<logic:equal name="fraseSearchPageVO" property="modoSeleccionar" value="true">
			<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
			<bean:message bundle="fra" key="fra.frase.label"/>
		</logic:equal>
		<logic:notEqual name="fraseSearchPageVO" property="modoSeleccionar" value="true">
			<bean:message bundle="fra" key="fra.fraseSearchPage.legend"/>
		</logic:notEqual>		
	</p>
		
	<!-- Filtro -->
	<fieldset>
	<legend><bean:message bundle="base" key="base.parametrosBusqueda"/></legend>
		<table class="tabladatos">
		   
		    <tr>	
				<td><label><bean:message bundle="fra" key="fra.frase.modulo.label"/>: </label></td>
				<td class="normal">
					<html:select name="fraseSearchPageVO" property="modulo.id" styleClass="select">
						<html:optionsCollection name="fraseSearchPageVO" property="listModulo" label="value" value="id" />
					</html:select>
				</td>
				<td><label><bean:message bundle="fra" key="fra.frase.pagina.label"/>: </label></td>
				<td class="normal"><html:text name="fraseSearchPageVO" property="frase.pagina" size="15" maxlength="20" styleClass="datos" /></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="fra" key="fra.frase.etiqueta.label"/>: </label></td>
				<td class="normal"><html:text name="fraseSearchPageVO" property="frase.etiqueta" size="15" maxlength="20" styleClass="datos" /></td>
			
				<td><label><bean:message bundle="fra" key="fra.frase.valorPublico.label"/>: </label></td>
				<td class="normal"><html:text name="fraseSearchPageVO" property="frase.valorPublico" size="20" maxlength="100" styleClass="datos" /></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="fra" key="fra.frase.desFrase.label"/>: </label></td>
				<td class="normal"><html:text name="fraseSearchPageVO" property="frase.desFrase" size="20" maxlength="100" styleClass="datos" /></td>
			</tr>
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
		<logic:equal name="fraseSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="fraseSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<logic:notEqual name="fraseSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Ver -->
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Publicar -->
							</logic:notEqual>
							<th align="left"><bean:message bundle="fra" key="fra.frase.modulo.label"/>/<bean:message bundle="fra" key="fra.frase.pagina.label"/>/<bean:message bundle="fra" key="fra.frase.etiqueta.label"/></th>
							<th align="left"><bean:message bundle="fra" key="fra.frase.valorPublico.label"/></th>
							<th align="left"><bean:message bundle="fra" key="fra.frase.desFrase.label"/></th>
						</tr>
							
						<logic:iterate id="FraseVO" name="fraseSearchPageVO" property="listResult">
							<tr>
								 <!-- Ver -->
								<td>
									<logic:equal name="fraseSearchPageVO" property="verEnabled" value="enabled">									
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="FraseVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="FraseVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</td>	
								<!-- Modificar-->								
								<td>
									<logic:equal name="fraseSearchPageVO" property="modificarEnabled" value="enabled">
										<logic:equal name="FraseVO" property="modificarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="FraseVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="FraseVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="fraseSearchPageVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</td>

								
								<td>
									<!-- Publicar -->
									<logic:equal name="fraseSearchPageVO" property="publicarEnabled" value="enabled">
										<logic:equal name="FraseVO" property="publicarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('publicar', '<bean:write name="FraseVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="fra" key="fra.fraseSearchPage.button.publicar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/activar0.gif"/>
											</a>
										</logic:equal> 
										<logic:notEqual name="FraseVO" property="publicarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="fraseSearchPageVO" property="publicarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
									</logic:notEqual>
								</td>
						
								<td>
								   <bean:write name="FraseVO" property="modulo"/><br>
								   <br><bean:write name="FraseVO" property="pagina"/><br>
								   <br><bean:write name="FraseVO" property="etiqueta"/><br>&nbsp; 
								</td>
							    <td><bean:write name="FraseVO" property="valorPublico"/>&nbsp;</td>
								<td><bean:write name="FraseVO" property="desFrase" />&nbsp;</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="fraseSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="fraseSearchPageVO" property="listResult">
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
		</tr>
	</table>
	<input type="hidden" name="name"  value="<bean:write name='fraseSearchPageVO' property='name'/>" id="name"/>
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