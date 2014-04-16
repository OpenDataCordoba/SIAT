<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pro/AdministrarEnvioArchivos.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>

	<fieldset>
		<legend><bean:message bundle="pro" key="pro.envioArchivosAdapter.title"/></legend>					
		
		<table class="tramonline" border="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="pro" key="pro.envioArchivosAdapter.legend"/></caption>
	    	<tbody>
				<logic:notEmpty  name="envioArchivosAdapterVO" property="listFileCorrida">	    	
			    	<tr>
						<!-- Seleccionar Todo -->
						<th width="1" style="padding: 3px 5px;">
							<input type="checkbox" onclick="changeChk('filter', 'listFileNameSelected', this)"/>
						</th>
						<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
						<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
					</tr>
					<logic:iterate id="FileCorridaVO" name="envioArchivosAdapterVO" property="listFileCorrida">
						<tr>
							<td>
								<html:multibox name="envioArchivosAdapterVO" property="listFileNameSelected">
									<bean:write name="FileCorridaVO" property="fileName"/>	
								</html:multibox>
							</td> 
							<td><bean:write name="FileCorridaVO" property="nombre"/>&nbsp;</td>
							<td><bean:write name="FileCorridaVO" property="observacion"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="envioArchivosAdapterVO" property="listFileCorrida">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
		</table>
	</fieldset>

	<fieldset>
		<legend><bean:message bundle="pro" key="pro.envioArchivosAdapter.datosEnvio"/></legend>
		<table border="0" cellspacing="1" width="100%" style="padding:14px;">
			<tbody>
				<tr>
					<!-- Usuario -->
					<td><label><bean:message bundle="pro" key="pro.envioArchivosAdapter.usuario.label"/>:</label>&nbsp;
		            	<bean:write name="envioArchivosAdapterVO" property="usuario"/>
					</td>
					
					<!-- Password
					<td><label>(*)&nbsp;<bean:message bundle="pro" key="pro.envioArchivosAdapter.password.label"/>: </label>&nbsp;
					<html:password name="envioArchivosAdapterVO" property="password" size="10"/></td> 
					-->
				</tr>
				<tr>
					<!-- Destino -->
					<td style="padding-top:20px;"><label>(*)&nbsp;<bean:message bundle="pro" key="pro.envioArchivosAdapter.destino.label"/>:</label>&nbsp;
						<html:select name="envioArchivosAdapterVO" property="siatScript.id" styleClass="select">
							<html:optionsCollection name="envioArchivosAdapterVO" property="listSiatScript" label="descripcion" value="id" />
						</html:select>
					</td>					
				</tr>
			</tbody>
		</table>
	</fieldset>

	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left" width="50%">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<td align="right" width="50%">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('validateEnvio', '');">
					<bean:message bundle="base" key="abm.button.enviar"/>
				</html:button>
			</td>
		</tr>
	</table>

	<input type="hidden" name="method" value=""/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
