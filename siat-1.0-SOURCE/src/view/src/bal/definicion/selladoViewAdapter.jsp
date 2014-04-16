<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarSellado.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.selladoViewAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Sellado -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.sellado.title"/></legend>
			<table class="tabladatos">
				<!-- BeanwriteCodigo -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.sellado.codSellado.label"/>: </label></td>
					<td class="normal"><bean:write name="selladoAdapterVO" property="sellado.codSellado"/></td>
			
				<!-- BeanwriteDescripcion -->
			
					<td><label><bean:message bundle="bal" key="bal.sellado.desSellado.label"/>: </label></td>
					<td class="normal"><bean:write name="selladoAdapterVO" property="sellado.desSellado"/></td>
				</tr>
				<!-- FieldEstado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="selladoAdapterVO" property="sellado.estado.value"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- Sellado -->
	
		<!-- ImpSel -->		
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="bal" key="bal.sellado.listImpSel.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="selladoAdapterVO" property="sellado.listImpSel">
				    	<tr>
							<th width="1">&nbsp;</th> <!-- Ver -->
							<th align="left"><bean:message bundle="bal" key="bal.impSel.fechaDesde.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.impSel.fechaHasta.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.impSel.tipoSellado.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.impSel.costo.label"/></th>
						</tr>
						<logic:iterate id="ImpSelVO" name="selladoAdapterVO" property="sellado.listImpSel">
				
							<tr>
								<!-- Ver -->
								<td>
									<logic:equal name="ImpSelVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verImpSel', '<bean:write name="ImpSelVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									
									<logic:notEqual name="ImpSelVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</td>
								<td><bean:write name="ImpSelVO" property="fechaDesdeView"/>&nbsp;</td>
								<td><bean:write name="ImpSelVO" property="fechaHastaView"/>&nbsp;</td>
								<td><bean:write name="ImpSelVO" property="tipoSellado.desTipoSellado"/>&nbsp;</td>
								<td><bean:write name="ImpSelVO" property="costoView"/>&nbsp;</td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="selladoAdapterVO" property="sellado.listImpSel">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>					
				</tbody>
			</table>
		<!-- ImpSel -->
		
		<!-- Accion Sellado -->		
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="bal" key="bal.sellado.listAccionSellado.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="selladoAdapterVO" property="sellado.listAccionSellado">
				    	<tr>
							<th width="1">&nbsp;</th> <!-- Ver -->
							<th align="left"><bean:message bundle="bal" key="bal.accionSellado.fechaDesde.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.accionSellado.fechaHasta.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.accionSellado.accion.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.accionSellado.recurso.label"/></th>
						</tr>
						<logic:iterate id="AccionSelladoVO" name="selladoAdapterVO" property="sellado.listAccionSellado">
				
							<tr>
								<!-- Ver -->
								<td>
									<logic:equal name="AccionSelladoVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verAccionSellado', '<bean:write name="AccionSelladoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									
									<logic:notEqual name="AccionSelladoVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</td>
								<td><bean:write name="AccionSelladoVO" property="fechaDesdeView" /></td>
								<td><bean:write name="AccionSelladoVO" property="fechaHastaView" /></td>								
								<td><bean:write name="AccionSelladoVO" property="accion.desAccion" /></td>
								<td><bean:write name="AccionSelladoVO" property="recurso.desRecurso" /></td>

							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="selladoAdapterVO" property="sellado.listAccionSellado">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>					
				</tbody>
			</table>
		<!-- Accion Sellado -->
		
		<!-- ParSel -->		
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="bal" key="bal.sellado.listParSel.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="selladoAdapterVO" property="sellado.listParSel">
				    	<tr>
							<th width="1">&nbsp;</th> <!-- Ver -->
							<th align="left"><bean:message bundle="bal" key="bal.parSel.partida.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.parSel.tipoDistrib.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.parSel.monto.label"/></th>
						</tr>
						<logic:iterate id="ParSelVO" name="selladoAdapterVO" property="sellado.listParSel">
							<tr>
								<!-- Ver -->
								<td>
									<logic:equal name="ParSelVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verParSel', '<bean:write name="ParSelVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									
									<logic:notEqual name="ParSelVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</td>
								<td><bean:write name="ParSelVO" property="partida.desPartidaView" /></td>
								<td><bean:write name="ParSelVO" property="tipoDistrib.desTipoDistrib" /></td>								
								<td><bean:write name="ParSelVO" property="montoView" /></td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="selladoAdapterVO" property="sellado.listParSel">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>					
				</tbody>
			</table>
		<!-- ParSel -->
		
		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="selladoAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="selladoAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="selladoAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
