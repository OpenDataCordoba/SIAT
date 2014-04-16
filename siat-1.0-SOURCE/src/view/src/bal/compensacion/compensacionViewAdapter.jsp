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
					<!-- Cuenta -->		
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="compensacionAdapterVO" property="compensacion.cuenta.numeroCuenta"/></td>
				</tr>
				<tr>
					<!-- Descripcion -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.compensacion.descripcion.label"/>: </label></td>
					<td class="normal"><bean:write name="compensacionAdapterVO" property="compensacion.descripcion"/></td>
				</tr>
				<!-- Caso -->
				<!-- Inclucion de CasoView -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="compensacionAdapterVO" property="compensacion"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>				
					</td>
				</tr>
				<!-- Fin Inclucion de CasoView -->
			</table>
		</fieldset>
	
		<logic:equal name="compensacionAdapterVO" property="act" value="ver">		
		<!-- SaldoAFavor -->		
		<div style="border:0px" class="horizscroll">
		<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.saldoAFavor.label"/></caption>
	    	<tbody>
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
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
						  	<td><bean:write name="SaldoAFavorVO" property="fechaGeneracionView"/>&nbsp;</td>
							<td><bean:write name="SaldoAFavorVO" property="tipoOrigen.desTipoOrigen"/>&nbsp;</td>
							<td><bean:write name="SaldoAFavorVO" property="importeView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
					<tr>
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
					<td align="right"><bean:write name="compensacionAdapterVO" property="totalComDeu"/>&nbsp;</td>
					<td>&nbsp;</td>
					</tr>
				</logic:notEmpty>
				<logic:empty  name="compensacionAdapterVO" property="compensacion.listComDeu">
					<tr><td colspan="9" align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			</table>
			</div>
		<!-- ComDeu -->			
		</logic:equal>

		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="compensacionAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
	   	    		<logic:equal name="compensacionAdapterVO" property="act" value="enviar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('enviar', '');">
							<bean:message bundle="base" key="abm.button.enviar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="compensacionAdapterVO" property="act" value="devolver">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('devolver', '');">
							<bean:message bundle="base" key="abm.button.devolver"/>
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
		
		