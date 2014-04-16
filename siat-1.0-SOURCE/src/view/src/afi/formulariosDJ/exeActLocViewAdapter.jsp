<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/afi/AdministrarExeActLoc.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="afi" key="afi.exeActLocViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- ExeActLoc -->
		<fieldset>
			<legend><bean:message bundle="afi" key="afi.exeActLoc.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="afi" key="afi.exeActLoc.numeroCuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="exeActLocAdapterVO" property="exeActLoc.numeroCuenta"/></td>
		
					<td><label><bean:message bundle="afi" key="afi.exeActLoc.codActividad.label"/>: </label></td>
					<td class="normal"><bean:write name="exeActLocAdapterVO" property="exeActLoc.codActividadView"/></td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.exeActLoc.nroResolucion.label"/>: </label></td>
					<td class="normal"><bean:write name="exeActLocAdapterVO" property="exeActLoc.nroResolucion"/></td>
		
					<td><label><bean:message bundle="afi" key="afi.exeActLoc.fechaEmision.label"/>: </label></td>
					<td class="normal"><bean:write name="exeActLocAdapterVO" property="exeActLoc.fechaEmisionView"/></td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.exeActLoc.fechaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="exeActLocAdapterVO" property="exeActLoc.fechaDesdeView"/></td>
		
					<td><label><bean:message bundle="afi" key="afi.exeActLoc.fechaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="exeActLocAdapterVO" property="exeActLoc.fechaHastaView"/></td>
				</tr>	
				
				
			</table>
		</fieldset>	
		<!-- ExeActLoc -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	   <logic:equal name="exeActLocAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
					<logic:equal name="exeActLocAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="exeActLocAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="exeActLocAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='exeActLocAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->