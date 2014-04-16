<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/gde/AdministrarCierreComercio.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="pad" key="pad.cierreComercioViewAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Atributo -->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.cierreComercio.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="gde" key="gde.cierreComercio.fechaCierreDefinitivo.label"/>: </label></td>
					<td class="normal"><bean:write name="cierreComercioAdapterVO" property="cierreComercio.fechaCierreDefView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.cierreComercio.fechaCeseActividad.label"/>: </label></td>
					<td class="normal"><bean:write name="cierreComercioAdapterVO" property="cierreComercio.fechaCeseActividadView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>: </label></td>
					<td class="normal"><bean:write name="cierreComercioAdapterVO" property="cierreComercio.cuentaVO.numeroCuenta"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cuenta.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="cierreComercioAdapterVO" property="cierreComercio.cuentaVO.recurso.desRecurso"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.cierreComercio.fechaInicioTramite.label"/>: </label></td>
					<td class="normal"><bean:write name="cierreComercioAdapterVO" property="cierreComercio.fechaTramiteView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td class="normal"><bean:write name="cierreComercioAdapterVO" property="cierreComercio.caso.casoView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="cierreComercioAdapterVO" property="cierreComercio.cuentaVO.estCue.descripcion"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- Atributo -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="cierreComercioAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="cierreComercioAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="cierreComercioAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
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
