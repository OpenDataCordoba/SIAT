<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarDisPar.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.disParAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
			
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.disPar.title"/></legend>			
			<table class="tabladatos" width="100%">
				<tr>
					<!-- Recurso -->		
					<td><label>&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="disParAdapterVO" property="disPar.recurso.desRecurso"/></td>
				</tr>
				<tr>
					<!-- DesDisPar -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.disPar.desDisPar.label"/>: </label></td>
					<td class="normal"><bean:write name="disParAdapterVO" property="disPar.desDisPar"/></td>
				</tr>
				<logic:equal name="disParAdapterVO" property="disPar.paramTipoImporte" value="true">
				<tr>
					<!-- TipoImporte -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.tipoImporte.label"/>: </label></td>
					<td class="normal"><bean:write name="disParAdapterVO" property="disPar.tipoImporte.desTipoImporte"/></td>
				</tr>
				</logic:equal>
			</table>
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="right">
					<bean:define id="modificarEncabezadoEnabled" name="disParAdapterVO" property="modificarEncabezadoEnabled"/>
					<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
						'<bean:write name="disParAdapterVO" property="disPar.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="base" key="abm.button.modificar"/>"/>
				</td>
			</tr>
		</table>

		</fieldset>
		<!-- DisParDet -->		
		<div style="border:0px" class="horizscroll">
		<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.disParDet.label"/></caption>
	    	<tbody>
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="bal" key="bal.disParDet.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.disParDet.fechaHasta.label"/></th>						
						<th align="left"><bean:message bundle="def" key="def.recCon.label"/>
			   			<logic:equal name="disParAdapterVO" property="paramTipoImporte" value="true">
							<html:select name="disParAdapterVO" property="recConSelected.id" styleClass="select" onchange="submitForm('paramRecCon', '' );" > 
								<html:optionsCollection name="disParAdapterVO" property="listRecCon" label="desRecCon" value="id" />
							</html:select>
						</logic:equal>
						<logic:equal name="disParAdapterVO" property="paramTipoImporte" value="false">
							<html:select name="disParAdapterVO" property="recConSelected.id" styleClass="select" onchange="submitForm('paramRecCon', '');" disabled="true"> 
								<html:optionsCollection name="disParAdapterVO" property="listRecCon" label="desRecCon" value="id" />
							</html:select>
						</logic:equal>
						</th>
						<th align="left"><bean:message bundle="bal" key="bal.disParDet.porcentaje.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.disParDet.partida.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.tipoImporte.label"/>
						<html:select name="disParAdapterVO" property="tipoImporteSelected.id" styleClass="select" onchange="submitForm('paramTipoImporte', '');"> 
							<html:optionsCollection name="disParAdapterVO" property="listTipoImporte" label="desTipoImporte" value="id" />
						</html:select>
						</th>
					</tr>
					<logic:notEmpty  name="disParAdapterVO" property="disPar.listDisParDet">	    	
					<logic:iterate id="DisParDetVO" name="disParAdapterVO" property="disPar.listDisParDet">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="disParAdapterVO" property="verDisParDetEnabled" value="enabled">							
									<logic:equal name="DisParDetVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verDisParDet', '<bean:write name="DisParDetVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="DisParDetVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="disParAdapterVO" property="verDisParDetEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Modificar-->
								<logic:equal name="disParAdapterVO" property="modificarDisParDetEnabled" value="enabled">
									<logic:equal name="DisParDetVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarDisParDet', '<bean:write name="DisParDetVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="DisParDetVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="disParAdapterVO" property="modificarDisParDetEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Eliminar-->
								<logic:equal name="disParAdapterVO" property="eliminarDisParDetEnabled" value="enabled">
									<logic:equal name="DisParDetVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarDisParDet', '<bean:write name="DisParDetVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="DisParDetVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="disParAdapterVO" property="eliminarDisParDetEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="DisParDetVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="DisParDetVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="DisParDetVO" property="recCon.desRecCon"/>&nbsp;</td>
							<td><bean:write name="DisParDetVO" property="porcentajeView"/>&nbsp;</td>
							<td><bean:write name="DisParDetVO" property="partida.desPartidaView"/>&nbsp;</td>
							<td><bean:write name="DisParDetVO" property="tipoImporte.desTipoImporte"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="disParAdapterVO" property="disPar.listDisParDet">
					<tr><td colspan="9" align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			<td colspan="20" align="right">
  				<bean:define id="agregarDisParEnabled" name="disParAdapterVO" property="agregarDisParDetEnabled"/>
				<input type="button" <%=agregarDisParEnabled%> class="boton" 
					onClick="submitForm('agregarDisParDet', '<bean:write name="disParAdapterVO" property="disPar.id" bundle="base" formatKey="general.format.id"/>');" 
					value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
			</td>
			</tbody>
			</table>
			</div>
		<!-- DisParDet -->			
		
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
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->		