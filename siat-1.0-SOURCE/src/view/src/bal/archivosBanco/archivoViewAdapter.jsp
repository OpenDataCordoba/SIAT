<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarArchivo.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.archivoAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.archivo.title"/></legend>			
			<table class="tabladatos" width="100%">
				<!-- Nombre -->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.archivo.nombre.label"/>: </label></td>
					<td class="normal"><bean:write name="archivoAdapterVO" property="archivo.nombre"/></td>
				</tr>	
				<!-- Fecha Banco -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.archivo.fechaBanco.label"/>: </label></td>
					<td class="normal"><bean:write name="archivoAdapterVO" property="archivo.fechaBancoView"/></td>
				</tr>		
				<!-- Prefijo y Nro Banco -->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.archivo.prefix.label"/>: </label></td>
					<td class="normal"><bean:write name="archivoAdapterVO" property="archivo.prefix"/></td>
					<td><label><bean:message bundle="bal" key="bal.archivo.nroBanco.label"/>: </label></td>
					<td class="normal"><bean:write name="archivoAdapterVO" property="archivo.nroBancoView"/></td>
				</tr>	
				<!-- Tipo Archivo y Estado-->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.tipoArc.label"/>: </label></td>
					<td class="normal"><bean:write name="archivoAdapterVO" property="archivo.tipoArc.descripcion"/></td>					

					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="archivoAdapterVO" property="archivo.estadoArc.descripcion"/></td>					
				</tr>					
				<!-- Cant. Trans y Total -->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.archivo.cantTrans.label"/>: </label></td>
					<td class="normal"><bean:write name="archivoAdapterVO" property="archivo.cantTransView"/></td>

					<td><label><bean:message bundle="bal" key="bal.archivo.total.label"/>: </label></td>
					<td class="normal"><bean:write name="archivoAdapterVO" property="archivo.totalView"/></td>
				</tr>	
				<logic:equal name="archivoAdapterVO" property="act" value="ver">
					<tr>
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.archivo.observacion.label"/>: </label></td>
						<td colspan="4" class="normal"><bean:write name="archivoAdapterVO" property="archivo.observacion"/></td>
					</tr>
				</logic:equal>
			</table>
		</fieldset>
		
		<logic:equal name="archivoAdapterVO" property="act" value="anular">
			<fieldset>
				<legend><bean:message bundle="bal" key="bal.archivoAdapter.anulacion"/></legend>			
				<table class="tabladatos" width="100%">
				<!-- Observacion-->		
					<tr>
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.archivo.observacion.label"/>: </label></td>
						<td class="normal"><html:textarea name="archivoAdapterVO" property="archivo.observacion" cols="75" rows="15"/></td>					
					</tr>
				</table>
			</fieldset>
		</logic:equal>
				
		
		<logic:equal name="archivoAdapterVO" property="act" value="ver">
		<!-- TranArc -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.archivo.listTranArc.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="archivoAdapterVO" property="archivo.listTranArc">	    	
			    	<tr>
						<th align="left"><bean:message bundle="bal" key="bal.tranArc.nroLinea.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.tranArc.linea.label"/></th>						
						<th align="left"><bean:message bundle="bal" key="bal.tranArc.importe.label"/></th>
					</tr>
					<logic:iterate id="TranArcVO" name="archivoAdapterVO" property="archivo.listTranArc">
						<tr>
							<td><bean:write name="TranArcVO" property="nroLineaView"/>&nbsp;</td>
							<td><bean:write name="TranArcVO" property="linea"/>&nbsp;</td>
							<td>$<bean:write name="TranArcVO" property="importeView" />&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="archivoAdapterVO" property="archivo.listTranArc">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			</table>
		<!-- TranArc -->	
		</logic:equal>
	
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="archivoAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="archivoAdapterVO" property="act" value="anular">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('anular', '');">
							<bean:message bundle="bal" key="bal.archivoSearchPage.adm.button.anular"/>
						</html:button>
					</logic:equal>
					<logic:equal name="archivoAdapterVO" property="act" value="aceptar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('aceptar', '');">
							<bean:message bundle="bal" key="bal.archivoSearchPage.adm.button.aceptar"/>
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
		
		