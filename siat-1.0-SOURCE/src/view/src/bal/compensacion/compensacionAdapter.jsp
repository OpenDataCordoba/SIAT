<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarCompensacion.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.compensacionAdapter.title"/></h1>		
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
			<legend><bean:message bundle="bal" key="bal.compensacion.title"/></legend>			
			<table class="tabladatos" width="100%">
				<tr>
					<!-- Fecha Alta -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.compensacion.fechaAlta.label"/>: </label></td>
					<td class="normal"><bean:write name="compensacionAdapterVO" property="compensacion.fechaAltaView"/></td>
				</tr>
				<tr>
					<!-- Recurso -->		
					<td><label>&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="compensacionAdapterVO" property="compensacion.cuenta.recurso.desRecurso"/></td>
				</tr>	
				<tr>
					<!-- Numero Cuenta -->		
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="compensacionAdapterVO" property="compensacion.cuenta.numeroCuenta"/></td>
				</tr>
				<tr>
					<!-- Descripcion -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.compensacion.descripcion.label"/>: </label></td>
					<td class="normal"><bean:write name="compensacionAdapterVO" property="compensacion.descripcion"/></td>
				</tr>
			</table>
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="right">
					<bean:define id="modificarEncabezadoEnabled" name="compensacionAdapterVO" property="modificarEncabezadoEnabled"/>
					<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
						'<bean:write name="compensacionAdapterVO" property="compensacion.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="base" key="abm.button.modificar"/>"/>
				</td>
			</tr>
		</table>
		</fieldset>
		
		<!-- SaldoAFavor -->		
		<div style="border:0px" class="horizscroll">
		<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.saldoAFavor.label"/></caption>
	    	<tbody>
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
					  	<th align="left"><bean:message bundle="bal" key="bal.saldoAFavor.fechaGeneracion.label"/></th>
  						<th align="left"><bean:message bundle="bal" key="bal.saldoAFavor.tipoOrigen.label"/></th>
					  	<th align="left"><bean:message bundle="bal" key="bal.saldoAFavor.importe.label"/></th>
					</tr>
					<logic:notEmpty  name="compensacionAdapterVO" property="compensacion.listSaldoAFavor">	    	
					<logic:iterate id="SaldoAFavorVO" name="compensacionAdapterVO" property="compensacion.listSaldoAFavor">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="compensacionAdapterVO" property="verSaldoAFavorEnabled" value="enabled">							
									<logic:equal name="SaldoAFavorVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verSaldoAFavor', '<bean:write name="SaldoAFavorVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="SaldoAFavorVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="compensacionAdapterVO" property="verSaldoAFavorEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							<td>
							<logic:equal name="compensacionAdapterVO" property="compensacion.paramEstado" value="false">
								<!-- Excluir-->
								<logic:equal name="compensacionAdapterVO" property="excluirSaldoAFavorEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('excluirSaldoAFavor', '<bean:write name="SaldoAFavorVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
									</a>
								</logic:equal>
								<logic:notEqual name="compensacionAdapterVO" property="excluirSaldoAFavorEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:equal name="compensacionAdapterVO" property="compensacion.paramEstado" value="true">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
							</logic:equal>
							</td>
						  	<td><bean:write name="SaldoAFavorVO" property="fechaGeneracionView"/>&nbsp;</td>
							<td><bean:write name="SaldoAFavorVO" property="tipoOrigen.desTipoOrigen"/>&nbsp;</td>
							<td><bean:write name="SaldoAFavorVO" property="importeView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
					<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td align="right"><bean:write name="compensacionAdapterVO" property="totalSaldoAFavor"/>&nbsp;</td>
					</tr>
				</logic:notEmpty>
				<logic:empty  name="compensacionAdapterVO" property="compensacion.listSaldoAFavor">
					<tr><td colspan="9" align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			<td colspan="20" align="right">
  				<bean:define id="incluirSaldoAFavorEnabled" name="compensacionAdapterVO" property="incluirSaldoAFavorEnabled"/>
				<input type="button" <%=incluirSaldoAFavorEnabled%> class="boton" 
					onClick="submitForm('incluirSaldoAFavor', '<bean:write name="compensacionAdapterVO" property="compensacion.id" bundle="base" formatKey="general.format.id"/>');" 
					value="<bean:message bundle="bal" key="bal.compensacionAdapter.adm.button.incluir"/>"	align="left" />
			</td>
			</tbody>
			</table>
			</div>
		<!-- SaldoAFavor -->	
			
		<!-- ComDeu -->		
		<div style="border:0px" class="horizscroll">
		<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.comDeu.label"/></caption>
	    	<tbody>
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
						<th align="left"><bean:message bundle="pad" key="pad.cuenta.label"/></th>						
						<th align="left"><bean:message bundle="bal" key="bal.comDeu.periodoDeuda.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.comDeu.saldoDeuda.label"/></th>						
						<th align="left"><bean:message bundle="bal" key="bal.comDeu.actDeuda.label"/></th>						
						<th align="left"><bean:message bundle="bal" key="bal.comDeu.totalDeuda.label"/></th>						
						<th align="left"><bean:message bundle="bal" key="bal.comDeu.importe.label"/></th>						
						<th align="left"><bean:message bundle="bal" key="bal.tipoComDeu.ref"/></th>	
					</tr>
					<logic:notEmpty  name="compensacionAdapterVO" property="compensacion.listComDeu">	    	
					<logic:iterate id="ComDeuVO" name="compensacionAdapterVO" property="compensacion.listComDeu">
							<tr>
							<td>
							<logic:equal name="compensacionAdapterVO" property="compensacion.paramEstado" value="false">
							<!-- Eliminar-->
								<logic:equal name="compensacionAdapterVO" property="eliminarComDeuEnabled" value="enabled">
									<logic:equal name="ComDeuVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarComDeu', '<bean:write name="ComDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="ComDeuVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="compensacionAdapterVO" property="eliminarComDeuEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:equal name="compensacionAdapterVO" property="compensacion.paramEstado" value="true">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
							</logic:equal>
							</td>
							<td><bean:write name="ComDeuVO" property="deuda.desRecurso"/>&nbsp;</td>
							<td><bean:write name="ComDeuVO" property="deuda.nroCuenta"/>&nbsp;</td>
							<td><bean:write name="ComDeuVO" property="deuda.periodoDeuda"/>&nbsp;</td>
							<td><bean:write name="ComDeuVO" property="deuda.saldo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
							<td><bean:write name="ComDeuVO" property="deuda.actualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
							<td><bean:write name="ComDeuVO" property="deuda.total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
							<td><bean:write name="ComDeuVO" property="importeView"/>&nbsp;</td>
							<td><bean:write name="ComDeuVO" property="tipoComDeu.descripcion"/>&nbsp;</td>
						</tr>
					</logic:iterate>
					<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td align="right"><bean:write name="compensacionAdapterVO" property="totalComDeu"/>&nbsp;</td>
					<td>&nbsp;</td>
					</tr>
				</logic:notEmpty>
				<logic:empty  name="compensacionAdapterVO" property="compensacion.listComDeu">
					<tr><td colspan="9" align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			<td colspan="20" align="right">
  				<bean:define id="agregarCompensacionEnabled" name="compensacionAdapterVO" property="agregarComDeuEnabled"/>
				<input type="button" <%=agregarCompensacionEnabled%> class="boton" 
					onClick="submitForm('agregarComDeu', '<bean:write name="compensacionAdapterVO" property="compensacion.id" bundle="base" formatKey="general.format.id"/>');" 
					value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
			</td>
			</tbody>
			</table>
			</div>
		<!-- ComDeu -->			
			
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