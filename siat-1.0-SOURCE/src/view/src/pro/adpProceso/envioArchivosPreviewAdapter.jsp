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
		<legend><bean:message bundle="pro" key="pro.envioArchivosPreviewAdapter.title"/></legend>
		<table class="normal" border="0" cellspacing="1" width="100%" style="font-size: 14px; padding-top:30px; padding-bottom:35px">            
	    	<tbody>
				<tr>
					<td>
						El usuario&nbsp;<b style="color: black;"><bean:write name="envioArchivosAdapterVO" property="usuario"/></b>&nbsp;enviar&aacute; los siguientes archivos:
					</td>
				</tr>
				
				<logic:notEmpty  name="envioArchivosAdapterVO" property="listFileNameSelected">	    	
					<logic:iterate id="FileName" name="envioArchivosAdapterVO" property="listFileNameSelected">
						<tr>
							<td style="font-size: 12px;"><ul class="vinieta"><li><bean:write name="FileName"/>&nbsp;</li></ul></td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="envioArchivosAdapterVO" property="listFileNameSelected">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
				
				<tr>
					<td>con destino &nbsp;<b style="color: black;"><bean:write name="envioArchivosAdapterVO" property="siatScript.descripcion"/></b>.</td>
				</tr>

			</tbody>
		</table>
	</fieldset>

	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left" width="50%">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volverFromPreview', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<td align="right" width="50%">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('enviar', '');">
					<bean:message bundle="base" key="abm.button.continuar"/>
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
