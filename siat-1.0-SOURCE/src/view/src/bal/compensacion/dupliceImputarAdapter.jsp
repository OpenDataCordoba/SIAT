<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarDuplice.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.dupliceAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
		</table>	
		
		<!-- Buscar Cuenta -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.dupliceAdapter.imputarA.title"/></legend>			
			<table class="tabladatos">
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal">
					<html:select name="dupliceAdapterVO" property="recurso.id" styleClass="select" >
						<bean:define id="includeRecursoList" name="dupliceAdapterVO" property="listRecurso"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
		   		 </tr>
				<tr>
				<!-- Cuenta -->		
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
				    <td class="normal" colspan="4">
							<html:text name="dupliceAdapterVO" property="cuenta.numeroCuenta" size="20"/>
							<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
								<bean:message bundle="bal" key="bal.duplice.button.buscarCuenta"/>
							</html:button>
					</td>
				</tr>
 				<tr>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.dupliceAdapter.tipoBoleta.label"/>: </label></td>
					<td class="normal">
					<html:select name="dupliceAdapterVO" property="tipoBoleta.id" styleClass="select" >
						<html:optionsCollection name="dupliceAdapterVO" property="listTipoBoleta" label="value" value="id" />
					</html:select>
					</td>		
			    </tr>	
			</table>
			<table class="tablabotones" width="100%">
			<tr>				
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('paramActualizar', '');">
						<bean:message bundle="base" key="abm.button.actualizar"/>
					</html:button>
				</td>
			</tr>
		</table>
		<logic:notEmpty  name="dupliceAdapterVO" property="listDeuda">			
		<!-- Lista de Deuda a Imputar-->
		<logic:equal name="dupliceAdapterVO" property="activarScroll" value="true">
			<div style="border:0px" class="scrolable" style="height: 300px;">
		</logic:equal>
		<logic:equal name="dupliceAdapterVO" property="activarScroll" value="false">
			<div style="border:0px">
		</logic:equal>
		<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.dupliceAdapter.listDeuda.title"/></caption>
	    	<tbody>
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
					  	<th align="left"><bean:message bundle="bal" key="bal.dupliceAdapter.periodoDeuda.label"/></th>
  						<th align="left"><bean:message bundle="bal" key="bal.dupliceAdapter.fechaVenDeuda.label"/></th>
					  	<th align="left"><bean:message bundle="bal" key="bal.dupliceAdapter.importeDeuda.label"/></th>
					  	<th align="left"><bean:message bundle="bal" key="bal.dupliceAdapter.saldoDeuda.label"/></th>
					  	<th align="left"><bean:message bundle="bal" key="bal.dupliceAdapter.actDeuda.label"/></th>
					  	<th align="left"><bean:message bundle="bal" key="bal.dupliceAdapter.totalDeuda.label"/></th>
					</tr>
					<logic:notEmpty  name="dupliceAdapterVO" property="listDeuda">	    	
					<logic:iterate id="LiqDeudaVO" name="dupliceAdapterVO" property="listDeuda">
						<tr>
							<td align="center">	&nbsp;	
								<bean:define id="idTran"><bean:write name="LiqDeudaVO" property="idDeuda" bundle="base" formatKey="general.format.id"/></bean:define>
					            <html:radio name="dupliceAdapterVO" property="idAImputar" value="<%=idTran%>"   styleId="<%=idTran%>"/>			
			  				</td>
						  	<td><bean:write name="LiqDeudaVO" property="periodoDeuda"/>&nbsp;</td>
							<td><bean:write name="LiqDeudaVO" property="fechaVto"/>&nbsp;</td>
				        	<td><bean:write name="LiqDeudaVO" property="importe" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="LiqDeudaVO" property="saldo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="LiqDeudaVO" property="actualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="LiqDeudaVO" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="dupliceAdapterVO" property="listDeuda">
					<tr><td colspan="7" align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
		</table>
		</div>
		<!-- Fin Lista de Deuda a Imputar -->	
		</logic:notEmpty>
		
		<logic:notEmpty  name="dupliceAdapterVO" property="listCuota">			
		<!-- Lista de Cuota a Imputar-->
		<logic:equal name="dupliceAdapterVO" property="activarScroll" value="true">
			<div style="border:0px" class="scrolable" style="height: 300px;">
		</logic:equal>
		<logic:equal name="dupliceAdapterVO" property="activarScroll" value="false">
			<div style="border:0px">
		</logic:equal>
		<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.dupliceAdapter.listCuota.title"/></caption>
	    	<tbody>
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
					  	<th align="left"><bean:message bundle="bal" key="bal.dupliceAdapter.nroConvenio.label"/></th>
					  	<th align="left"><bean:message bundle="bal" key="bal.dupliceAdapter.nroCuota.label"/></th>
  						<th align="left"><bean:message bundle="bal" key="bal.dupliceAdapter.fechaVenCuota.label"/></th>
					  	<th align="left"><bean:message bundle="bal" key="bal.dupliceAdapter.capital.label"/></th>
					  	<th align="left"><bean:message bundle="bal" key="bal.dupliceAdapter.interes.label"/></th>
					  	<th align="left"><bean:message bundle="bal" key="bal.dupliceAdapter.actualizacion.label"/></th>
					  	<th align="left"><bean:message bundle="bal" key="bal.dupliceAdapter.importeCuota.label"/></th>
					</tr>
					<logic:notEmpty  name="dupliceAdapterVO" property="listCuota">	    	
					<logic:iterate id="LiqCuotaVO" name="dupliceAdapterVO" property="listCuota">
						<tr>
							<td align="center">	&nbsp;	
								 <bean:define id="idTran"><bean:write name="LiqCuotaVO" property="idCuota" bundle="base" formatKey="general.format.id"/></bean:define>
					             <html:radio name="dupliceAdapterVO" property="idAImputar" value="<%=idTran%>"   styleId="<%=idTran%>"/>			
			  				</td>
						  	<td><bean:write name="LiqCuotaVO" property="nroConvenio"/>&nbsp;</td>
						  	<td><bean:write name="LiqCuotaVO" property="nroCuota"/>&nbsp;</td>
							<td><bean:write name="LiqCuotaVO" property="fechaVto"/>&nbsp;</td>
				        	<td><bean:write name="LiqCuotaVO" property="capital" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="LiqCuotaVO" property="interes" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="LiqCuotaVO" property="actualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="LiqCuotaVO" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="dupliceAdapterVO" property="listCuota">
					<tr><td colspan="7" align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
		</table>
		</div>
		<!-- Fin Lista de Cuota a Imputar -->	
		</logic:notEmpty>

	
		</fieldset>
		<!-- Fin Buscar Cuenta -->
			
		<!-- Duplice -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.duplice.title"/></legend>
			<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.sistema.label"/>: </label></td>
							<td class="normal"><bean:write name="dupliceAdapterVO" property="duplice.sistema"/></td>
			
				<td><label><bean:message bundle="bal" key="bal.indet.nroComprobante.label"/>: </label></td>
				<td class="normal"><bean:write name="dupliceAdapterVO" property="duplice.nroComprobante"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.clave.label"/>: </label></td>
				<td class="normal"><bean:write name="dupliceAdapterVO" property="duplice.clave"/></td>
			
				<td><label><bean:message bundle="bal" key="bal.indet.partida.label"/>: </label></td>
				<td class="normal"><html:text name="dupliceAdapterVO" property="duplice.partida" size="20" maxlength="14" styleClass="datos" /></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.resto.label"/>: </label></td>
				<td class="normal"><html:text name="dupliceAdapterVO" property="duplice.resto" size="10" maxlength="4" styleClass="datos" /></td>
			
				<td><label><bean:message bundle="bal" key="bal.indet.importeCobrado.label"/>: </label></td>
				<td class="normal"><html:text name="dupliceAdapterVO" property="duplice.importeCobradoView" size="10" maxlength="100" styleClass="datos" /></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.recargo.label"/>: </label></td>
				<td class="normal"><html:text name="dupliceAdapterVO" property="duplice.recargoView" size="10" maxlength="100" styleClass="datos" /></td>
				<logic:equal name="dupliceAdapterVO" property="act" value="agregar">
					<td><label><bean:message bundle="bal" key="bal.indet.importeBasico.label"/>: </label></td>
					<td class="normal"><html:text name="dupliceAdapterVO" property="duplice.importeBasicoView" size="10" maxlength="100" styleClass="datos" /></td>
				</logic:equal>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.codIndet.label"/>: </label></td>
				<td class="normal"><html:text name="dupliceAdapterVO" property="duplice.codIndetView" size="5" maxlength="2" styleClass="datos" /></td>

				<logic:equal name="dupliceAdapterVO" property="act" value="agregar">
					<td><label><bean:message bundle="bal" key="bal.indet.importeCalculado.label"/>: </label></td>
					<td class="normal"><html:text name="dupliceAdapterVO" property="duplice.importeCalculadoView" size="10" maxlength="100" styleClass="datos" /></td>
				</logic:equal>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.fechaPago.label"/>: </label></td>
				<td class="normal">
					<html:text name="dupliceAdapterVO" property="duplice.fechaPagoView" styleId="fechaPagoView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaPagoView');" id="a_fechaPagoView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.caja.label"/>: </label></td>
				<td class="normal"><html:text name="dupliceAdapterVO" property="duplice.cajaView" size="5" maxlength="100" styleClass="datos" /></td>

				<td><label><bean:message bundle="bal" key="bal.indet.paquete.label"/>: </label></td>
				<td class="normal"><html:text name="dupliceAdapterVO" property="duplice.paqueteView" size="5" maxlength="20" styleClass="datos" /></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.indet.codPago.label"/>: </label></td>
				<td class="normal"><html:text name="dupliceAdapterVO" property="duplice.codPagoView" size="5" maxlength="5" styleClass="datos" /></td>
			
				<td><label><bean:message bundle="bal" key="bal.indet.fechaBalance.label"/>: </label></td>
				<td class="normal">
					<html:text name="dupliceAdapterVO" property="duplice.fechaBalanceView" styleId="fechaBalanceView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaBalanceView');" id="a_fechaBalanceView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>		
			</tr>
			<tr>			
				<td><label><bean:message bundle="bal" key="bal.indet.reciboTr.label"/>: </label></td>
				<td class="normal"><html:text name="dupliceAdapterVO" property="duplice.reciboTr" size="15" maxlength="100" styleClass="datos" /></td>
			</tr>
			
			</table>
		</fieldset>
		
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
				<logic:equal name="dupliceAdapterVO" property="act" value="imputar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('imputar', '');">
						<bean:message bundle="bal" key="bal.indetSearchPage.adm.button.reingresar"/>
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
	
	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
			
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
		