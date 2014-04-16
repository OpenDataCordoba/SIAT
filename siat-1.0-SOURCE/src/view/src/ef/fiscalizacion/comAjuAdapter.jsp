<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/ef/AdministrarComAju.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.comAjuAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
	<!-- ComAju -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.comAju.label"/></legend>
		
		<table class="tabladatos">
		<!-- fechaSolicitud -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.comAju.fechaSolicitud.label"/>: </label></td>
			<td class="normal">
				<bean:write name="comAjuAdapterVO" property="comAju.fechaSolicitudView"/>
			</td>
		</tr>
		
		<!-- fechaReconocimiento -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.comAju.fechaAplicacion.label"/>: </label></td>
			<td class="normal">
				<bean:write name="comAjuAdapterVO" property="comAju.fechaAplicacionView"/>
			</td>
		</tr>
		
		<!-- cuenta -->
		<tr>	
			<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
			<td class="normal">
				<bean:write name="comAjuAdapterVO" property="comAju.detAju.ordConCue.cuenta.numeroCuenta"/>
			</td>
		</tr>
		
		<!-- saldoFavorOriginal -->
			<tr>
				<td><label><bean:message bundle="ef" key="ef.comAju.saldoFavorOriginal.label"/>: </label></td>
				<td class="normal"><bean:write name="comAjuAdapterVO" property="comAju.saldoFavorOriginalView"/></td>
			</tr>
			
			<!-- totalCompensado -->
			<tr>
				<td><label><bean:message bundle="ef" key="ef.comAju.totalCompensado.label"/>: </label></td>
				<td class="normal"><bean:write name="comAjuAdapterVO" property="comAju.totalCompensadoView"/></td>
			</tr>
		
		<tr>
			<td colspan="4"> 
				<bean:define id="modificarEncabezadoEnabled" name="comAjuAdapterVO" property="modificarEncabezadoEnabled"/>
				<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
					'<bean:write name="comAjuAdapterVO" property="comAju.id" bundle="base" formatKey="general.format.id"/>');" 
					value="<bean:message bundle="base" key="abm.button.modificar"/>"/>		
			</td>
		</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- ComAju -->
		
	<!-- detalle de compensaciones -->
	<logic:greaterThan name="comAjuAdapterVO" property="comAju.id" value="0">				
		<logic:notEmpty name="comAjuAdapterVO" property="comAju.listComAjuDet">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<caption><bean:message bundle="ef" key="ef.comAju.title"/></caption>				
             	<tbody>
			               	<tr>
			               		<th><bean:message bundle="ef" key="ef.comAjuDet.periodo.label"/></th><!-- periodo -->
			               		<th><bean:message bundle="ef" key="ef.comAjuDet.ajusteOriginal.label"/></th><!-- Ajuste original -->
			               		<th><bean:message bundle="ef" key="ef.comAjuDet.actualizacion.label"/></th><!-- actualizacion -->
								<th><bean:message bundle="ef" key="ef.comAjuDet.capitalCompensado.label"/></th><!-- capital compensado -->
								<th><bean:message bundle="ef" key="ef.comAjuDet.actCom.label"/></th><!-- ActCom -->								
							</tr>
							
							<logic:iterate id="ComAjuDetVO" name="comAjuAdapterVO" property="comAju.listComAjuDet">
								<tr>		
									
									<td><bean:write name="ComAjuDetVO" property="detAjuDet.plaFueDatCom.periodoAnioView"/></td>
									
									<td><bean:write name="ComAjuDetVO" property="ajusteOriginalView"/></td>
									
									<td><bean:write name="ComAjuDetVO" property="actualizacionView"/></td>
									
									<td><bean:write name="ComAjuDetVO" property="capitalCompensadoView"/></td>
									
									<td><bean:write name="ComAjuDetVO" property="actComView"/></td>
									
								</tr>
							</logic:iterate>             	
             	</tbody>
             </table>			
		</logic:notEmpty>
		
		<logic:empty name="comAjuAdapterVO" property="comAju.listComAjuDet">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<caption><bean:message bundle="ef" key="ef.comAju.title"/></caption>				
             	<tbody>
		          <tr>
		           	<td align="center"><bean:message bundle="ef" key="ef.comAjuEditAdapter.msg.listComAjuDetVacia"/></td>
		          </tr>
			    </tbody>
			 </table>       	
		</logic:empty>
	</logic:greaterThan>	
	<!-- FIN detalle de compensaciones -->
				
		<table class="tablabotones">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
					<html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
		   		    	<bean:message bundle="base" key="abm.button.imprimir"/>
					</html:button>
		
				</td>   	    			
			</tr>
		</table>
		<input type="hidden" name="name"  value="<bean:write name='comAjuAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
<!-- comAjuAdapter.jsp -->