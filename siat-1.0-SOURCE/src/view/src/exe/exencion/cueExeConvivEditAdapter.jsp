<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/exe/AdministrarCueExeConviv.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="exe" key="exe.cueExeConvivAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volverAVer', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- CueExe -->
	<fieldset>
		<legend><bean:message bundle="exe" key="exe.cueExe.title"/></legend>
		
		<table class="tabladatos">
			
			<!-- Fecha Solicitud -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.cueExe.fechaSolicitud.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeConvivAdapterVO" property="cueExe.fechaSolicitudView"/>
				</td>	
			</tr>
			
			<!-- Recurso -->
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal">
					<bean:write name="cueExeConvivAdapterVO" property="cueExe.recurso.desRecurso"/>
				</td>	
			
			<!-- Cuenta -->			
				<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeConvivAdapterVO" property="cueExe.cuenta.numeroCuenta"/>
				</td>
			</tr>
			<!-- Exencion -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.exencion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="cueExeConvivAdapterVO" property="cueExe.exencion.desExencion"/>
				</td>
			</tr>
			
			<!-- Tipo Sujeto Solicitante -->
			<tr>
				<td><label><bean:message bundle="exe" key="exe.tipoSujeto.label"/>: </label></td>
				<td class="normal">
					<bean:write name="cueExeConvivAdapterVO" property="cueExe.tipoSujeto.desTipoSujeto"/>
				</td>	
			<!--  Nro Beneficiario -->
				<td><label><bean:message bundle="exe" key="exe.cueExe.nroBeneficiario.label"/>: </label></td>
				<td class="normal"><bean:write name="cueExeConvivAdapterVO" property="cueExe.nroBeneficiario" /></td>						
			</tr>
			
			<!-- Estado -->
			<tr>
				<td width="50%"><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
				<td width="50%" class="normal">
					<bean:write name="cueExeConvivAdapterVO" property="cueExe.estadoCueExe.desEstadoCueExe"/>
				</td>	
			</tr>
		</table>
	</fieldset>
	
				
	<!-- Datos Conviviente -->	
	<fieldset>
		<legend><bean:message bundle="exe" key="exe.cueExeConvivAdapter.conviviente.legend"/></legend>		
		<table class="tabladatos">			            
			<tbody>
	    		<!-- nombre -->
	    		<tr>
	    			<td><label>(*)&nbsp;<bean:message bundle="exe" key="exe.conviviente.nombre"/>:</label>
	    			<td class="normal" colspan="3">
	    				<html:text name="cueExeConvivAdapterVO" property="conviviente.convNombre"/>
	    			</td>	    			
	    		</tr>
	    		
	    		<!-- tipo y nro doc -->
	    		<tr>
	    			<td><label><bean:message bundle="exe" key="exe.conviviente.documento.tipo"/>:</label>
	    			<td class="normal">
	    				<html:text name="cueExeConvivAdapterVO" property="conviviente.convTipodoc" maxlength="4" size="4"/>
	    			</td>
	    			<td><label><bean:message bundle="exe" key="exe.conviviente.documento.nro"/></label>
	    			<td class="normal">
	    				<html:text name="cueExeConvivAdapterVO" property="conviviente.convNrodoc" maxlength="13" size="12"/>
	    			</td>
	    		</tr>
	    		
	    		<tr>
	    			<!-- Parentezco -->
	    			<td><label><bean:message bundle="exe" key="exe.conviviente.parentezco"/>:</label>
	    			<td class="normal">
	    				<html:text name="cueExeConvivAdapterVO" property="conviviente.convParentesco"/>
	    			</td>
	    			
	    			<!-- Edad -->	
	    			<td><label><bean:message bundle="exe" key="exe.conviviente.edad"/>:</label>
	    			<td class="normal">
	    				<html:text name="cueExeConvivAdapterVO" property="conviviente.convEdad" maxlength="2" size="4"/>
	    			</td>
	    		</tr>	    		
			</tbody>
		</table>
	</fieldset>	
		<!-- FIN Datos Conviviente -->

	<!-- CueExe -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volverAVer', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">   	    		
   	    		<logic:equal name="cueExeConvivAdapterVO" property="act" value="agregar">
		   	    	<html:button property="btnAgregar" styleClass="boton" onclick="submitForm('agregar', '');">
		   	    		<bean:message bundle="base" key="abm.button.agregar"/>
		   	    	</html:button>
		   	    </logic:equal>
		   	    <logic:equal name="cueExeConvivAdapterVO" property="act" value="modificar">
		   	    	<html:button property="btnAgregar" styleClass="boton" onclick="submitForm('modificar', '');">
		   	    		<bean:message bundle="base" key="abm.button.modificar"/>
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
