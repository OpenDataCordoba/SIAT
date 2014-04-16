<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/BuscarLocalidad.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="pad" key="pad.localidadSearchPage.title"/></h1>	
		

	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p><bean:message bundle="pad" key="pad.localidadSearchPage.legend"/></p>
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
				<td><label><bean:message bundle="pad" key="pad.provincia.label"/>: </label></td>
				<td class="normal">
					<html:select name="localidadSearchPageVO" property="localidad.provincia.id" styleClass="select">
						<html:optionsCollection name="localidadSearchPageVO" property="listProvincia" label="descripcion" value="id" />
					</html:select>
				</td>

				<td><label><bean:message bundle="pad" key="pad.localidad.label"/>: </label></td>
				<td class="normal"><html:text name="localidadSearchPageVO" property="localidad.descripcionPostal" size="15" maxlength="20" styleClass="datos" /></td>			
			<tr>
		</table>
			
		<p align="center">
		  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');">
				<bean:message bundle="base" key="abm.button.limpiar"/>
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
		<logic:equal name="localidadSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="localidadSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Seleccionar -->
							<th align="left"><bean:message bundle="pad" key="pad.localidad.descripcionPostal.label"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.provincia.label"/></th>
						</tr>
							
						<logic:iterate id="LocalidadVO" name="localidadSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<td>	
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="LocalidadVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</td>
								
								<td><bean:write name="LocalidadVO" property="descripcionPostal"/>&nbsp;</td>
								<td><bean:write name="LocalidadVO" property="provincia.descripcion"/>&nbsp;</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="localidadSearchPageVO"/>
								<%@ include file="/base/pagerSinPU.jsp" %> 
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="localidadSearchPageVO" property="listResult">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
                	<tbody>
						<logic:greaterThan name="localidadSearchPageVO" property="pageNumber" value="1">
							<tr>
								<td align="center">
									<bean:message bundle="base" key="base.resultadoPaginaVacio"/>
								</td>
							</tr>
							<tr>
								<td class="paginador" align="center">
									<bean:define id="pager" name="localidadSearchPageVO"/>
									<%@ include file="/base/pagerSinPU.jsp" %>
								</td>
							</tr>
						</logic:greaterThan>
						<logic:lessEqual name="localidadSearchPageVO" property="pageNumber" value="1">
							<tr>
								<td align="center">
									<bean:message bundle="base" key="base.resultadoVacio"/>
								</td>
							</tr>
						</logic:lessEqual>
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
		
	<input type="hidden" name="method" value=""/>
<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>
	<!-- para que no submita automaticamente con enter al tener un solo text -->
	<input type="text" name="solucionProb" value="" style="display:none"/>	
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
