<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarEncServicioBanco.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.servicioBancoAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<!-- Servicio Banco Enc -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.servicioBanco.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.servicioBanco.codServicioBanco.label"/>: </label></td>
					<td class="normal"><html:text name="encServicioBancoAdapterVO" property="servicioBanco.codServicioBanco" size="15" maxlength="2" styleClass="datos"/></td>					
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.servicioBanco.desServicioBanco.label"/>: </label></td>
					<td class="normal"><html:text name="encServicioBancoAdapterVO" property="servicioBanco.desServicioBanco" size="20" maxlength="100" styleClass="datos"/></td>					
				</tr>
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.servicioBanco.partidaIndet.label"/>: </label></td>
					<td colspan="4" class="normal">	
						<html:select name="encServicioBancoAdapterVO" property="servicioBanco.partidaIndet.id" styleClass="select">
							<html:optionsCollection name="encServicioBancoAdapterVO" property="listPartida" label="desPartidaView" value="id" />
						</html:select>
					</td>
				</tr>	
				<tr>
					<td><label>&nbsp;<bean:message bundle="def" key="def.servicioBanco.parCuePue.label"/>: </label></td>
					<td colspan="4" class="normal">	
						<html:select name="encServicioBancoAdapterVO" property="servicioBanco.parCuePue.id" styleClass="select">
							<html:optionsCollection name="encServicioBancoAdapterVO" property="listPartida" label="desPartidaView" value="id" />
						</html:select>
					</td>
				</tr>	
				<tr>
					<!-- EsAutoliquidable -->
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.servicioBanco.esAutoliquidable.label"/>: </label></td>
					<td class="normal">	
						<html:select name="encServicioBancoAdapterVO" property="servicioBanco.esAutoliquidable.id" styleClass="select">
							<html:optionsCollection name="encServicioBancoAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
					<td colspan="4"><ul class="vinieta"><li style="text-align:left;"><bean:message bundle="def" key="def.servicioBanco.esAutoliquidable.description"/></li></ul></td>				
				</tr>
				<tr>
					<!-- Tipo Asentamiento -->
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.servicioBanco.tipoAsentamiento.label"/>: </label></td>
					<td class="normal">	
						<html:select name="encServicioBancoAdapterVO" property="servicioBanco.tipoAsentamiento" styleClass="select">
							<html:optionsCollection name="encServicioBancoAdapterVO" property="listTipoAsentamiento" label="descripcion" value="value" />
						</html:select>
					</td>
					<td colspan="4"><ul class="vinieta"><li><bean:message bundle="def" key="def.servicioBanco.tipoAsentamiento.description"/></li></ul></td>				
				</tr>
			</table>
		</fieldset>
		
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
					<logic:equal name="encServicioBancoAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="encServicioBancoAdapterVO" property="act" value="agregar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
							<bean:message bundle="base" key="abm.button.agregar"/>
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
