<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarGesJudDeu.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.gesJudAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<!-- GesJud -->
	 	<bean:define id="act" name="gesJudDeuAdapterVO" property="act"/>
	 	<bean:define id="gesJud" name="gesJudDeuAdapterVO" property="gesJudDeu.gesJud"/>	 	
		<%@ include file="/gde/adminDeuJud/includeGesJud.jsp" %>
		<!-- GesJud -->
	
		<!-- ConstanciaDeu -->
		<%@ include file="/gde/adminDeuJud/includeGesJudConstanciaDeuView.jsp" %>
		<!-- FIN ConstanciaDeu -->
			
		<logic:equal name="gesJudDeuAdapterVO" property="act" value="agregar">			
				<!-- Cuenta -->
				<table class="tabladatos">
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
						<td class="normal">
							<html:text name="gesJudDeuAdapterVO" property="cuenta.numeroCuenta" size="20" maxlength="100" disabled="true"/>
							<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
								<bean:message bundle="exe" key="exe.cueExeSearchPage.button.buscarCuenta"/>
							</html:button>
						</td>			
					</tr>
					<tr>
						<td colspan="2" align="center">
			    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('buscar', '');">
								<bean:message bundle="base" key="abm.button.buscar"/>
							</html:button>				
						</td>
					</tr>	
				</table>
		</logic:equal>		
		
	<!-- Deudas a incluir -->	
		<logic:equal name="gesJudDeuAdapterVO" property="verResultados" value="true">				
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<caption><bean:message bundle="gde" key="gde.gesJudDeu.deudasAIncluir.label"/></caption>
				<tbody>
					<logic:notEmpty  name="gesJudDeuAdapterVO" property="listDeudaJudicial">          	
		               	<tr>
							<th width="1"><input type="checkbox" checked="checked" onclick="changeChk('filter', 'idsDeudaSelected', this)"/></th> <!-- Check -->
							<th align="left"><bean:message bundle="gde" key="gde.deuda.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.deuda.importe.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.deuda.saldo.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.deuda.fechaVencimiento.label"/></th>					
						</tr>
							
						<logic:iterate id="DeudaJudicialVO" name="gesJudDeuAdapterVO" property="listDeudaJudicial">
							<tr>
								<!-- Seleccionar -->
								<td>
									<logic:equal name="DeudaJudicialVO" property="esValidaParaGesJud" value="true">
										<html:multibox name="gesJudDeuAdapterVO" property="idsDeudaSelected">
											<bean:write name="DeudaJudicialVO" property="idView"/>
										</html:multibox>
									</logic:equal>
									<logic:notEqual name="DeudaJudicialVO" property="esValidaParaGesJud" value="true">
										<input type="checkBox" disabled="disabled"/>
									</logic:notEqual>
								</td>														
								<td><bean:write name="DeudaJudicialVO" property="nroBarraAnio"/>&nbsp;</td>
								<td><bean:write name="DeudaJudicialVO" property="importeView" />&nbsp;</td>
								<td><bean:write name="DeudaJudicialVO" property="saldoView"/>&nbsp;</td>
								<td><bean:write name="DeudaJudicialVO" property="fechaVencimientoView"/>&nbsp;</td>														
							</tr>
						</logic:iterate>
					</logic:notEmpty>
		
					<logic:empty name="gesJudDeuAdapterVO" property="listDeudaJudicial">
						<tr><td align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>			
					</logic:empty>
					
					<logic:notEmpty name="gesJudDeuAdapterVO" property="listDeudaJudicial">
						<logic:equal name="gesJudDeuAdapterVO" property="agregarEnabled" value="enabled">					
								<tr>
									<td colspan="9" align="right">				
										<input type="button" onclick="submitForm('agregar', '');" value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
									</td>
								</tr>	
						</logic:equal>
					</logic:notEmpty>	
				</tbody>
			</table>
		</logic:equal>
	<!-- FIN Deudas a incluir -->		
		
		<logic:equal name="gesJudDeuAdapterVO" property="act" value="modificar">
		
		<!--  Deuda -->		
			<fieldset>
			<legend><bean:message bundle="gde" key="gde.gesJudDeu.title"/></legend>
			<table class="tabladatos">
				<tr>	
					<td><label><bean:message bundle="gde" key="gde.deuda.label"/>:</label></td>
					<td class="normal">
						<bean:write name="gesJudDeuAdapterVO" property="gesJudDeu.deuda.nroBarraAnio"/>
					</td>
					<td><label><bean:message bundle="gde" key="gde.deuda.importe.label"/>:</label></td>
					<td class="normal"><bean:write name="gesJudDeuAdapterVO" property="gesJudDeu.deuda.importeView" /></td>						
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.deuda.saldo.label"/>:</label></td>
					<td class="normal"><bean:write name="gesJudDeuAdapterVO" property="gesJudDeu.deuda.saldoView"/></td>
					<td><label><bean:message bundle="gde" key="gde.deuda.fechaVencimiento.label"/>:</label></td>
					<td class="normal"><bean:write name="gesJudDeuAdapterVO" property="gesJudDeu.deuda.fechaVencimientoView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.constanciaDeu.numero.ref"/>:</label></td>
					<td class="normal"><bean:write name="gesJudDeuAdapterVO" property="gesJudDeu.constanciaDeu.numeroView"/></td>
					<td><label><bean:message bundle="gde" key="gde.constanciaDeu.anio.ref"/>:</label></td>
					<td class="normal"><bean:write name="gesJudDeuAdapterVO" property="gesJudDeu.constanciaDeu.anioView"/></td>						
				</tr>
				<tr>
					<td><bean:message bundle="gde" key="gde.gesJudDeu.observaciones.label"/></td>
					<td class="normal" colspan="3"><html:textarea name="gesJudDeuAdapterVO" property="gesJudDeu.observacion" cols="80" rows="15"/></td>
				</tr>			
			</table>			
			</fieldset>
			<!--  FIN Deuda -->
			
		</logic:equal>
		
						
		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="gesJudDeuAdapterVO" property="act" value="modificar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					
					<logic:equal name="gesJudDeuAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
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
