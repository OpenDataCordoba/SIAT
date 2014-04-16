<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarSaldoEnCompensacion.do">

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
					<td class="normal"><bean:write name="admSaldoEnCompensacionAdapterVO" property="compensacion.fechaAltaView"/></td>
				</tr>	
				<tr>
					<!-- Recurso -->		
					<td><label>&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="admSaldoEnCompensacionAdapterVO" property="compensacion.cuenta.recurso.desRecurso"/></td>
				</tr>
				<tr>
					<!-- Cuenta -->		
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="admSaldoEnCompensacionAdapterVO" property="compensacion.cuenta.numeroCuenta"/></td>
				</tr>
				<tr>
					<!-- Descripcion -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.compensacion.descripcion.label"/>: </label></td>
					<td class="normal"><bean:write name="admSaldoEnCompensacionAdapterVO" property="compensacion.descripcion"/></td>
				</tr>
			</table>
		</fieldset>
	
		<!-- SaldoAFavor -->		
		<div style="border:0px" class="horizscroll">
		<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.saldoAFavor.label"/></caption>
	    	<tbody>
			    	<tr>
			    		<th align="center">
		       				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		       				<input type="checkbox" name="checkAll" onclick="changeChk('filter', 'listIdSaldoSelected', this)"/>
			       		</th>
						<th width="1">&nbsp;</th> <!-- Ver -->
					  	<th align="left"><bean:message bundle="bal" key="bal.saldoAFavor.fechaGeneracion.label"/></th>
  						<th align="left"><bean:message bundle="bal" key="bal.saldoAFavor.tipoOrigen.label"/></th>
					  	<th align="left"><bean:message bundle="bal" key="bal.saldoAFavor.importe.label"/></th>
					</tr>
					<logic:notEmpty  name="admSaldoEnCompensacionAdapterVO" property="listSaldoAFavor">	    	
					<logic:iterate id="SaldoAFavorVO" name="admSaldoEnCompensacionAdapterVO" property="listSaldoAFavor">
						<tr>
							<td align="center">
				  				<html:multibox name="admSaldoEnCompensacionAdapterVO" property="listIdSaldoSelected">
	        	     	            <bean:write name="SaldoAFavorVO" property="id" bundle="base" formatKey="general.format.id"/>
	            	     	    </html:multibox>
			  				</td>
							<!-- Ver -->
							<td>
								<logic:equal name="admSaldoEnCompensacionAdapterVO" property="verSaldoAFavorEnabled" value="enabled">							
									<logic:equal name="SaldoAFavorVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verSaldoAFavor', '<bean:write name="SaldoAFavorVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="SaldoAFavorVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="admSaldoEnCompensacionAdapterVO" property="verSaldoAFavorEnabled" value="enabled">
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
						<td>&nbsp;</td>
						<td align="right"><bean:write name="admSaldoEnCompensacionAdapterVO" property="totalSaldoAFavor"/>&nbsp;</td>
					</tr>
				</logic:notEmpty>
				<logic:empty  name="admSaldoEnCompensacionAdapterVO" property="listSaldoAFavor">
					<tr>
						<td colspan="9" align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td>
					</tr>
				</logic:empty>					
			</tbody>
			</table>
			</div>
		<!-- SaldoAFavor -->	
		
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="admSaldoEnCompensacionAdapterVO" property="act" value="incluirSaldoAFavor">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('incluirSaldoAFavor', '');">
							<bean:message bundle="bal" key="bal.compensacionAdapter.adm.button.incluir"/>
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