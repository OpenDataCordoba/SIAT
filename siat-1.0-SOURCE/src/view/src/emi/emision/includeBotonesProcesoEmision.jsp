<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<%/*
	Botonera comun a los procesos de Emision.
	Recibe un "procesoEmisionAdapterVO".
	Muestra un boton si y solo si existe el 
	flag correspondiente en el adapter. 
*/%>

		<!-- Boton Activar -->							
		<logic:equal name="procesoEmisionAdapterVO" property="activarEnabled" value="true">
			<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='false'>
				<bean:message bundle="pro" key="pro.abm.button.activar"/>
			</html:button>&nbsp;&nbsp;
		</logic:equal>
		<logic:equal name="procesoEmisionAdapterVO" property="activarEnabled" value="false">
			<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='true'>
				<bean:message bundle="pro" key="pro.abm.button.activar"/>
			</html:button>&nbsp;&nbsp;
		</logic:equal>
		
		<!-- Boton Cancelar -->
		<logic:present name="procesoEmisionAdapterVO" property="cancelarEnabled">							
			<logic:equal name="procesoEmisionAdapterVO" property="cancelarEnabled" value="true">
				<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cancelar', '');" disabled='false'>
					<bean:message bundle="pro" key="pro.abm.button.cancelar"/>
				</html:button>&nbsp;&nbsp;
			</logic:equal>
			<logic:equal name="procesoEmisionAdapterVO" property="cancelarEnabled" value="false">
				<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cancelar', '');" disabled='true'>
					<bean:message bundle="pro" key="pro.abm.button.cancelar"/>
				</html:button>&nbsp;&nbsp;
			</logic:equal>
		</logic:present>
		
		<!-- Boton Reiniciar -->
		<logic:present name="procesoEmisionAdapterVO" property="reiniciarEnabled">
			<logic:equal name="procesoEmisionAdapterVO" property="reiniciarEnabled" value="true">
				<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reiniciar', '');" disabled='false'>
					<bean:message bundle="pro" key="pro.abm.button.reiniciar"/>
				</html:button>&nbsp;&nbsp;	
			</logic:equal>			
			<logic:equal name="procesoEmisionAdapterVO" property="reiniciarEnabled" value="false">
				<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reiniciar', '');" disabled='true'>
					<bean:message bundle="pro" key="pro.abm.button.reiniciar"/>
				</html:button>&nbsp;&nbsp;	
			</logic:equal>
		</logic:present>						

		<!-- Boton Refrescar -->
		<logic:present name="procesoEmisionAdapterVO" property="refrescarEnabled">
			<logic:equal name="procesoEmisionAdapterVO" property="refrescarEnabled" value="true">
				<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('refill', '');" disabled='false' styleId="locate_paso">
					<bean:message bundle="pro" key="pro.abm.button.refill"/>
				</html:button>&nbsp;&nbsp;
			</logic:equal>
			<logic:equal name="procesoEmisionAdapterVO" property="refrescarEnabled" value="false">
				<html:button property="btnAccionBase"  styleClass="boton" disabled='true' styleId="locate_paso">
					<bean:message bundle="pro" key="pro.abm.button.refill"/>
				</html:button>&nbsp;&nbsp;
			</logic:equal>
		</logic:present>

		<!-- Boton Logs -->
		<logic:present name="procesoEmisionAdapterVO" property="verLogsEnabled">
			<logic:equal name="procesoEmisionAdapterVO" property="verLogsEnabled" value="true">
				<html:button property="btnAccionBase"  styleClass="boton" onclick="submitDownload('downloadLogFile', '');" disabled='false'>
					<bean:message bundle="pro" key="pro.abm.button.verLog"/>
				</html:button>&nbsp;&nbsp;
			</logic:equal>
			<logic:equal name="procesoEmisionAdapterVO" property="verLogsEnabled" value="false">
				<html:button property="btnAccionBase"  styleClass="boton" disabled='true'>
					<bean:message bundle="pro" key="pro.abm.button.verLog"/>
				</html:button>&nbsp;&nbsp;
			</logic:equal>
		</logic:present>
