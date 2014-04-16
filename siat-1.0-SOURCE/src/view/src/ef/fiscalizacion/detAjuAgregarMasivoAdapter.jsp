<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/ef/AdministrarDetAju.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1>
			<logic:equal name="detAjuAdapterVO" property="tipoAgregarMasivo" value="1">
				<bean:message bundle="ef" key="ef.detAjuAgregarMasivoAdapter.Personal.title"/>			
			</logic:equal>

			<logic:equal name="detAjuAdapterVO" property="tipoAgregarMasivo" value="2">
				<bean:message bundle="ef" key="ef.detAjuAgregarMasivoAdapter.publicidad.title"/>			
			</logic:equal>

			<logic:equal name="detAjuAdapterVO" property="tipoAgregarMasivo" value="3">
				<bean:message bundle="ef" key="ef.detAjuAgregarMasivoAdapter.mesasYSillass.title"/>			
			</logic:equal>
			
			<logic:equal name="detAjuAdapterVO" property="tipoAgregarMasivo" value="4">
				<bean:message bundle="ef" key="ef.detAjuAgregarMasivoAdapter.porMulta.title"/>			
			</logic:equal>
		</h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<input type="button" class="boton" onclick="submitForm('volver', '<bean:write name="detAjuAdapterVO" property="detAju.id" bundle="base" formatKey="general.format.id"/>');"
						value='<bean:message bundle="base" key="abm.button.volver"/>'/>					
				</td>
			</tr>
		</table>
		
		<!-- DetAju -->
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.detAju.label"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="ef" key="ef.detAju.fecha.label"/>: </label></td>
					<td class="normal"><bean:write name="detAjuAdapterVO" property="detAju.fechaView"/></td>
					
					<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="detAjuAdapterVO" property="detAju.ordConCue.cuenta.numeroCuenta" /></td>
				</tr>
															
			</table>
		</fieldset>	
		<!-- DetAju -->
		
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.aliComFueColAdapter.agregarMasivo.legend"/></legend>
		
			<table class="tabladatos">
				<tr>
					<logic:equal name="detAjuAdapterVO" property="tipoAgregarMasivo" value="1">						
						<td>
							<label>(*)&nbsp;<bean:message bundle="ef" key="ef.detAjuAdapter.cantPersonalAgregarMasivo.label"/>:</label>
						</td>	
						<td class="normal"><html:text name="detAjuAdapterVO" property="cantPersonalAgregarMasivo" size="5" maxlength="6"/></td>								
					</logic:equal>	 
							
					<logic:equal name="detAjuAdapterVO" property="tipoAgregarMasivo" value="2">						
						<td>
							<label>(*)&nbsp;<bean:message bundle="ef" key="ef.detAjuAgregarMasivoAdapter.porcentajePubl.label"/>:</label>
						</td>	
						<td class="normal"><html:text name="detAjuAdapterVO" property="porcentajeAgregarMasivo" size="5" maxlength="6"/></td>
					</logic:equal>
							
					<logic:equal name="detAjuAdapterVO" property="tipoAgregarMasivo" value="3">						
						<td>
							<label>(*)&nbsp;<bean:message bundle="ef" key="ef.detAjuAgregarMasivoAdapter.porcentajeMyS.label"/>:</label>
						</td>	
						<td class="normal"><html:text name="detAjuAdapterVO" property="porcentajeAgregarMasivo" size="5" maxlength="6"/></td>
					</logic:equal>
					
					<logic:equal name="detAjuAdapterVO" property="tipoAgregarMasivo" value="4">						
						<td>
							<label>(*)&nbsp;<bean:message bundle="ef" key="ef.detAjuAgregarMasivoAdapter.porMulta.label"/>:</label>
						</td>	
						<td class="normal"><html:text name="detAjuAdapterVO" property="porcentajeAgregarMasivo" size="5" maxlength="6"/></td>
					</logic:equal>																					
				</tr>
				
				<tr>					
					<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.aliComFueColAdapter.periodoDesde.label"/>: </label></td>
					<td class="normal">
						<html:select name="detAjuAdapterVO" property="idDetAjuDetDesde">
							<html:optionsCollection name="detAjuAdapterVO" property="detAju.listDetAjuDet" label="plaFueDatCom.periodoAnioView" value="id"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.aliComFueColAdapter.periodoHasta.label"/>: </label></td>
					<td class="normal">
						<html:select name="detAjuAdapterVO" property="idDetAjuDetHasta">
							<html:optionsCollection name="detAjuAdapterVO" property="detAju.listDetAjuDet" label="plaFueDatCom.periodoAnioView" value="id"/>
						</html:select>
					</td>			
				</tr>														
			</table>
		</fieldset>
				
			
		<table class="tablabotones"  width="100%">
			<tr>				
				<td align="left">
		   			<input type="button" class="boton" onclick="submitForm('volver', '<bean:write name="detAjuAdapterVO" property="detAju.id" bundle="base" formatKey="general.format.id"/>');"
						value='<bean:message bundle="base" key="abm.button.volver"/>'/>
				</td>   	    			
				
				<td align="right">
	    			<html:button property="btnAgregar"  styleClass="boton" onclick="submitForm('agregarMasivo', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
					</html:button>
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
<!-- detAjuAgregarMasivoAdapter.jsp -->