<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarTramite.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volverACuenta', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	<!-- Filtro -->
	<fieldset>
	<legend>Datos del Certificado</legend>
		<table class="tabladatos">
			<tr>	
				<td><label><bean:message bundle="gde" key="gde.tipoTramite.label"/>: </label></td>
				<td class="normal">
					<html:select name="tramiteAdapterVO" property="tramite.tipoTramite.codTipoTramite" styleClass="select">
						<html:optionsCollection name="tramiteAdapterVO" property="listTipoTramite" label="desTipoTramite" value="codTipoTramite" />
					</html:select>
				</td>					
			</tr>
			<tr>
				<td><label>Cod.Ref.Pag o Nro. Recibo/Año Recibo:</label>
				<br>
				Ej.: 5954618 o 45384/2009 &nbsp;&nbsp;&nbsp;
				</td>
				<td class="normal">
					<html:text name="tramiteAdapterVO" property="tramite.codRefPag" size="20" maxlength="100"/>
					<!--
					<html:text name="tramiteAdapterVO" property="tramite.nroRecibo" size="10" maxlength="100"/>/ 
					<html:text name="tramiteAdapterVO" property="tramite.anioRecibo" size="10" maxlength="100"/>
					-->
				</td>
			</tr>
		</table>
			
		<p align="center">
		  	<html:button property="btnValidar"  styleClass="boton" onclick="submitForm('validaTramite', '');">
				<bean:message bundle="base" key="abm.button.aceptar"/>
			</html:button>
		</p>
	</fieldset>	
	<!-- Fin Filtro -->

	<table class="tablabotones">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volverACuenta', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
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
