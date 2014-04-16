<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/BuscarDisParPla.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.disParPlaAdapter.title"/></h1>		
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
					<td class="normal"><bean:write name="disParPlaSearchPageVO" property="disParPla.disPar.recurso.desRecurso"/></td>
				</tr>
				<!-- DesDisPar-->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.disPar.desDisPar.label"/>: </label></td>
					<td class="normal"><bean:write name="disParPlaSearchPageVO" property="disParPla.disPar.desDisPar"/></td>					
				</tr>
				<tr>
			</table>
		</fieldset>
		<!-- Fin DisPar -->

		<!-- DisParPla -->
		<div id="resultadoFiltro">
		<logic:equal name="disParPlaSearchPageVO" property="viewResult" value="true">		
			<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.disParPla.label"/></caption>
	    	<tbody>
			<logic:notEmpty  name="disParPlaSearchPageVO" property="listDisParPla">	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="bal" key="bal.disParPla.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.disParPla.fechaHasta.label"/></th>						
						<th align="left"><bean:message bundle="bal" key="bal.disParPla.planVia.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.atributo.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.disParPla.valor.label"/></th>

					</tr>
					<logic:iterate id="DisParPlaVO" name="disParPlaSearchPageVO" property="listDisParPla">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="disParPlaSearchPageVO" property="verEnabled" value="enabled">							
									<logic:equal name="DisParPlaVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="DisParPlaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="DisParPlaVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="disParPlaSearchPageVO" property="verEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Modificar-->
								<logic:equal name="disParPlaSearchPageVO" property="modificarEnabled" value="enabled">
									<logic:equal name="DisParPlaVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="DisParPlaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="DisParPlaVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="disParPlaSearchPageVO" property="modificarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Eliminar-->
								<logic:equal name="disParPlaSearchPageVO" property="eliminarEnabled" value="enabled">
									<logic:equal name="DisParPlaVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="DisParPlaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="DisParPlaVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="disParPlaSearchPageVO" property="eliminarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="DisParPlaVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="DisParPlaVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="DisParPlaVO" property="plan.desPlan"/> - <bean:write name="DisParPlaVO" property="plan.viaDeuda.desViaDeuda"/>&nbsp;</td>
							<logic:equal name="DisParPlaVO" property="tieneAtributo" value="true">	
								<td><bean:write name="DisParPlaVO" property="disPar.recurso.atributoAse.desAtributo"/>&nbsp;</td>
							</logic:equal>
							<logic:equal name="DisParPlaVO" property="tieneAtributo" value="false">	
								<td>No posee&nbsp;</td>
							</logic:equal>
							<td><bean:write name="DisParPlaVO" property="valorView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
					<tr>
						<td class="paginador" align="center" colspan="11">
							<bean:define id="pager" name="disParPlaSearchPageVO"/>
							<%@ include file="/base/pager.jsp" %>
						</td>
					</tr>
				</logic:notEmpty>
				<logic:empty  name="disParPlaSearchPageVO" property="listDisParPla">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			<td colspan="20" align="right">
  				<bean:define id="agregarEnabled" name="disParPlaSearchPageVO" property="agregarEnabled"/>
				<input type="button" <%=agregarEnabled%> class="boton" 
					onClick="submitForm('agregar', '<bean:write name="disParPlaSearchPageVO" property="disParPla.disPar.id" bundle="base" formatKey="general.format.id"/>');" 
					value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
			</td>
			</tbody>
			</table>
		</logic:equal>				
		</div>
		<!-- Fin DisParPla -->
		
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
		
		