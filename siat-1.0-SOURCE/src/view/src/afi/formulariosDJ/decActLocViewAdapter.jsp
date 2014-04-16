<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/afi/AdministrarDecActLoc.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="afi" key="afi.decActLocViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Datos de Declaracion -->
		<fieldset>
			<legend><bean:message bundle="afi" key="afi.decActLoc.datosDeclaracion.label"/></legend>
			<table class="tabladatos">
					<tr>
					<td><label><bean:message bundle="afi" key="afi.decActLoc.numeroCuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.numeroCuenta"/></td>
			
					<td><label><bean:message bundle="afi" key="afi.decActLoc.codActividad.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.codActividadView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.decActLoc.baseImpExenta.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.baseImpExentaView"/></td>
			
					<td><label><bean:message bundle="afi" key="afi.decActLoc.baseImponible.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.baseImponibleView"/></td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.decActLoc.ajuCamCoe.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.ajuCamCoeView"/></td>
			
					<td><label><bean:message bundle="afi" key="afi.decActLoc.baseImpAjustada.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.baseImpAjustadaView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.decActLoc.aliCuota.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.aliCuotaView"/></td>
			
					<td><label><bean:message bundle="afi" key="afi.decActLoc.derechoCalculado.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.derechoCalculadoView"/></td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.decActLoc.cantidad.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.cantidadView"/></td>
			
					<td><label><bean:message bundle="afi" key="afi.decActLoc.unidadMedida.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.unidadMedidaView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.decActLoc.tipoUnidad.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.tipoUnidadView"/></td>
			
					<td><label><bean:message bundle="afi" key="afi.decActLoc.minimoPorUnidad.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.minimoPorUnidadView"/></td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.decActLoc.minimoCalculado.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.minimoCalculadoView"/></td>
			
					<td><label><bean:message bundle="afi" key="afi.decActLoc.derechoDet.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.derechoDetView"/></td>
				</tr>
				<logic:equal name="decActLocAdapterVO" property="paramEtur" value="true">
					<tr>
						<td><label><bean:message bundle="afi" key="afi.decActLoc.alcanceEtur.label"/>: </label></td>
						<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.alcanceEturView"/></td>			
					</tr>
				</logic:equal>
			</table>
		</fieldset>	
		<!-- Datos de Declaracion -->
				
		<!-- Ajuste Base Imponible por Cambio de Coeficiente por Local -->
		<fieldset>
			<legend><bean:message bundle="afi" key="afi.decActLoc.datosAjuste.label"/></legend>
			<table class="tabladatos">	
				<tr>
					<td><label><bean:message bundle="afi" key="afi.decActLoc.difBaseEnero.label"/>: </label></td>				
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.difBaseEneroView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.decActLoc.difBaseFebrero.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.difBaseFebreroView"/></td>
				</tr>
				<tr>			
					<td><label><bean:message bundle="afi" key="afi.decActLoc.difBaseMarzo.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.difBaseMarzoView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.decActLoc.difBaseAbril.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.difBaseAbrilView"/></td>
				</tr>
				<tr>			
					<td><label><bean:message bundle="afi" key="afi.decActLoc.difBaseMayo.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.difBaseMayoView"/></td>
				</tr>				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.decActLoc.difBaseJunio.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.difBaseJunioView"/></td>
				</tr>
				<tr>			
					<td><label><bean:message bundle="afi" key="afi.decActLoc.difBaseJulio.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.difBaseJulioView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.decActLoc.difBaseAgosto.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.difBaseAgostoView"/></td>
				</tr>
				<tr>			
					<td><label><bean:message bundle="afi" key="afi.decActLoc.difBaseSeptiembre.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.difBaseSeptiembreView"/></td>
				</tr>	
				<tr>
					<td><label><bean:message bundle="afi" key="afi.decActLoc.difBaseOctubre.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.difBaseOctubreView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.decActLoc.difBaseNoviembre.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.difBaseNoviembreView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.decActLoc.difBaseDiciembre.label"/>: </label></td>
					<td class="normal"><bean:write name="decActLocAdapterVO" property="decActLoc.difBaseDiciembreView"/></td>
				</tr>	
			</table>
		</fieldset>	
		<!-- Ajuste Base Imponible por Cambio de Coeficiente por Local -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	   <logic:equal name="decActLocAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='decActLocAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->