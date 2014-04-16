<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/BuscarDisParRec.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.disParRecAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- DisPar -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.disPar.title"/></legend>			
			<table class="tabladatos" width="100%">
				<!-- Recurso -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="disParRecSearchPageVO" property="disParRec.disPar.recurso.desRecurso"/></td>
				</tr>
				<!-- DesDisPar-->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.disPar.desDisPar.label"/>: </label></td>
					<td class="normal"><bean:write name="disParRecSearchPageVO" property="disParRec.disPar.desDisPar"/></td>					
				</tr>
				<tr>
				<logic:equal name="disParRecSearchPageVO" property="tieneAtributo" value="true">		
				<!-- Atributo -->		
					<td><label>&nbsp;<bean:message bundle="def" key="def.atributo.label"/>: </label></td>
					<td class="normal"><bean:write name="disParRecSearchPageVO" property="disParRec.disPar.recurso.atributoAse.desAtributo"/></td>
				</tr>
				</logic:equal>
			</table>
		</fieldset>
		<!-- Fin DisPar -->

		<!-- DisParRec -->
		<div id="resultadoFiltro">
		<logic:equal name="disParRecSearchPageVO" property="viewResult" value="true">		
			<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.disParRec.label"/></caption>
	    	<tbody>
			<logic:notEmpty  name="disParRecSearchPageVO" property="listDisParRec">	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="bal" key="bal.disParRec.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.disParRec.fechaHasta.label"/></th>						
						<th align="left"><bean:message bundle="def" key="def.viaDeuda.label"/></th>
						<logic:equal name="disParRecSearchPageVO" property="tieneAtributo" value="true">	
							<th align="left"><bean:message bundle="bal" key="bal.disParRec.valor.label"/></th>
						</logic:equal>
					</tr>
					<logic:iterate id="DisParRecVO" name="disParRecSearchPageVO" property="listDisParRec">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="disParRecSearchPageVO" property="verEnabled" value="enabled">							
									<logic:equal name="DisParRecVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="DisParRecVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="DisParRecVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="disParRecSearchPageVO" property="verEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Modificar-->
								<logic:equal name="disParRecSearchPageVO" property="modificarEnabled" value="enabled">
									<logic:equal name="DisParRecVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="DisParRecVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="DisParRecVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="disParRecSearchPageVO" property="modificarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Eliminar-->
								<logic:equal name="disParRecSearchPageVO" property="eliminarEnabled" value="enabled">
									<logic:equal name="DisParRecVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="DisParRecVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="DisParRecVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="disParRecSearchPageVO" property="eliminarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="DisParRecVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="DisParRecVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="DisParRecVO" property="viaDeuda.desViaDeuda"/>&nbsp;</td>
							<logic:equal name="disParRecSearchPageVO" property="tieneAtributo" value="true">	
								<td><bean:write name="DisParRecVO" property="valorView"/>&nbsp;</td>
							</logic:equal>
						</tr>
					</logic:iterate>
					<tr>
						<td class="paginador" align="center" colspan="11">
							<bean:define id="pager" name="disParRecSearchPageVO"/>
							<%@ include file="/base/pager.jsp" %>
						</td>
					</tr>
				</logic:notEmpty>
				<logic:empty  name="disParRecSearchPageVO" property="listDisParRec">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			<td colspan="20" align="right">
  				<bean:define id="agregarEnabled" name="disParRecSearchPageVO" property="agregarEnabled"/>
				<input type="button" <%=agregarEnabled%> class="boton" 
					onClick="submitForm('agregar', '<bean:write name="disParRecSearchPageVO" property="disParRec.disPar.id" bundle="base" formatKey="general.format.id"/>');" 
					value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
			</td>
			</tbody>
			</table>
		</logic:equal>				
		</div>
		<!-- Fin DisParRec -->
		
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
		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->		
		
		