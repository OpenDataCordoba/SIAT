<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarMovBanDet.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.movBanDetAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- MovBanDet -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.movBanDet.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="bal" key="bal.movBanDet.nroCierreBanco.label"/>: </label></td>
					<td class="normal"><bean:write name="movBanDetAdapterVO" property="movBanDet.nroCierreBancoView"/></td>				
	
					<td><label><bean:message bundle="bal" key="bal.movBanDet.bancoRec.label"/>: </label></td>
					<td class="normal"><bean:write name="movBanDetAdapterVO" property="movBanDet.bancoRecView"/></td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.movBanDet.nroCuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="movBanDetAdapterVO" property="movBanDet.nroCuenta"/></td>				
	
					<td><label><bean:message bundle="bal" key="bal.movBanDet.impuesto.label"/>: </label></td>
					<td class="normal"><bean:write name="movBanDetAdapterVO" property="movBanDet.impuestoView"/></td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.movBanDet.debito.label"/>: </label></td>
					<td class="normal"><bean:write name="movBanDetAdapterVO" property="movBanDet.debitoView"/></td>				
	
					<td><label><bean:message bundle="bal" key="bal.movBanDet.credito.label"/>: </label></td>
					<td class="normal"><bean:write name="movBanDetAdapterVO" property="movBanDet.creditoView"/></td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.movBanDet.moneda.label"/>: </label></td>
					<td class="normal"><bean:write name="movBanDetAdapterVO" property="movBanDet.monedaView"/></td>				
	
					<td><label><bean:message bundle="bal" key="bal.movBanDet.conciliado.label"/>: </label></td>
					<td class="normal"><bean:write name="movBanDetAdapterVO" property="movBanDet.conciliado.value"/></td>				
				</tr>
			</table>
		</fieldset>	
		<!-- MovBanDet -->

		<table class="tablabotones"  width="100%">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right">
	   	    	   <logic:equal name="movBanDetAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
					<logic:equal name="movBanDetAdapterVO" property="act" value="conciliar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('conciliar', '');">
							<bean:message bundle="base" key="abm.button.conciliar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='movBanDetAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
