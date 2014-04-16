<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarMovBan.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.movBanAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- MovBan -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.movBan.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="bal" key="bal.movBan.idOrgRecAfip.label" />: </label></td>
					<td class="normal"><bean:write name="movBanAdapterVO" property="movBan.idOrgRecAfipView"/></td>
		
					<td><label><bean:message bundle="bal" key="bal.movBan.bancoAdm.label" />: </label></td>
					<td class="normal">
						<bean:write name="movBanAdapterVO" property="movBan.bancoAdmView" />
					</td>
				</tr>
				<tr>			
					<td><label><bean:message bundle="bal" key="bal.movBan.fechaAcredit.label" />: </label></td>
					<td class="normal"><bean:write name="movBanAdapterVO" property="movBan.fechaAcreditView"/></td>
		
					<td><label><bean:message bundle="bal" key="bal.movBan.fechaProceso.label" />: </label></td>
					<td class="normal"><bean:write name="movBanAdapterVO" property="movBan.fechaProcesoView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.movBan.totalDebito.label" />: </label></td>
					<td class="normal"><bean:write name="movBanAdapterVO" property="movBan.totalDebitoView"/></td>
		
					<td><label><bean:message bundle="bal" key="bal.movBan.totalCredito.label" />: </label></td>
					<td class="normal"><bean:write name="movBanAdapterVO" property="movBan.totalCreditoView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.movBan.cantDetalle.label" />: </label></td>
					<td class="normal"><bean:write name="movBanAdapterVO" property="movBan.cantDetalleView"/></td>
		
					<td><label><bean:message bundle="bal" key="bal.movBan.conciliado.label" />: </label></td>
					<td class="normal"><bean:write name="movBanAdapterVO" property="movBan.conciliado.value"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.movBan.usuario.label" />: </label></td>
					<td class="normal"><bean:write name="movBanAdapterVO" property="movBan.usuario"/></td>
		
					<td><label><bean:message bundle="bal" key="bal.movBan.fechaUltMdf.label" />: </label></td>
					<td class="normal"><bean:write name="movBanAdapterVO" property="movBan.fechaUltMdfView"/></td>
				</tr>
			</table>
		</fieldset>	

	<!-- SubTotales por Cuenta -->
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.movBan.subTotales.title"/></caption>
	    	<tbody>
				<logic:notEmpty  name="movBanAdapterVO" property="listSubtotalesPorCuenta">	    	
			    	<tr>
						<th align="left"><bean:message bundle="bal" key="bal.movBanDet.nroCuenta.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.movBan.subTotalDebito.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.movBan.subTotalCredito.label"/></th>
					</tr>
					<logic:iterate id="TotMovBanDetHelper" name="movBanAdapterVO" property="listSubtotalesPorCuenta">
						<tr>
							<td><bean:write name="TotMovBanDetHelper" property="nroCuenta"/>&nbsp;</td>
							<td><bean:write name="TotMovBanDetHelper" property="debitoView"/>&nbsp;</td>
							<td><bean:write name="TotMovBanDetHelper" property="creditoView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="movBanAdapterVO" property="listSubtotalesPorCuenta">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
	</fieldset>
	<!-- SubTotales por Cuenta -->
	
		<!-- MovBaDet -->	
		<div id="resultadoFiltro"  style="border:0px" class="horizscroll">	
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.movBan.listMovBanDet.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="movBanAdapterVO" property="movBan.listMovBanDet">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th align="left"><bean:message bundle="bal" key="bal.movBanDet.bancoRec.col"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.movBanDet.nroCierreBanco.label"/></th>	
						<th align="left"><bean:message bundle="bal" key="bal.movBanDet.nroCuenta.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.movBanDet.impuesto.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.movBanDet.debito.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.movBanDet.credito.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.movBanDet.moneda.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.movBanDet.conciliado.label"/></th>
					</tr>
					<logic:iterate id="MovBanDetVO" name="movBanAdapterVO" property="movBan.listMovBanDet">
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="movBanAdapterVO" property="verMovBanDetEnabled" value="enabled">
									<logic:equal name="MovBanDetVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verMovBanDet', '<bean:write name="MovBanDetVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="MovBanDetVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="movBanAdapterVO" property="verMovBanDetEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="MovBanDetVO" property="bancoRecView"/>&nbsp;</td>
							<td><bean:write name="MovBanDetVO" property="nroCierreBancoView"/>&nbsp;</td>
							<td><bean:write name="MovBanDetVO" property="nroCuenta"/>&nbsp;</td>
							<td><bean:write name="MovBanDetVO" property="impuestoView"/>&nbsp;</td>
							<td><bean:write name="MovBanDetVO" property="debitoView"/>&nbsp;</td>
							<td><bean:write name="MovBanDetVO" property="creditoView"/>&nbsp;</td>
							<td><bean:write name="MovBanDetVO" property="monedaView"/>&nbsp;</td>
							<td><bean:write name="MovBanDetVO" property="conciliado.value"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="movBanAdapterVO" property="movBan.listMovBanDet">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		</div>
		<!-- MovBaDet -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left"  width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="100%">
	   	    	    <logic:equal name="movBanAdapterVO" property="act" value="ver">
				   	   <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						   <bean:message bundle="base" key="abm.button.imprimir"/>
					   </html:button>
					</logic:equal>
					<logic:equal name="movBanAdapterVO" property="act" value="conciliar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('conciliar', '');">
							<bean:message bundle="base" key="abm.button.conciliar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="movBanAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	<input type="hidden" name="name"  value="<bean:write name='movBanAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	   	 		   	 	
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
