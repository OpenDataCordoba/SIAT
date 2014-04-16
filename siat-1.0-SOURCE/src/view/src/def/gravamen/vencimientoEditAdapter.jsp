<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/def/AdministrarVencimiento.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="def" key="def.vencimientoEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>

<!-- Vencimiento -->
	<fieldset>
		<legend><bean:message bundle="def" key="def.vencimiento.title"/></legend>
		
		<table class="tabladatos">
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.vencimiento.dia.label"/>: </label></td>
				<td class="normal">
						<html:text name="vencimientoAdapterVO" property="vencimiento.diaView" size="10" maxlength="2" />
				</td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.vencimiento.mes.label"/>: </label></td>
				<td class="normal">
						<html:text name="vencimientoAdapterVO" property="vencimiento.mesView" size="10" maxlength="2" />
				</td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.vencimiento.esHabil.label"/>: </label></td>
				<td class="normal">	
					<html:select name="vencimientoAdapterVO" property="vencimiento.esHabil.id" styleClass="select">
						<html:optionsCollection name="vencimientoAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
			</tr>		
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.vencimiento.cantDias.label"/>: </label></td>
				<td class="normal">
						<html:text name="vencimientoAdapterVO" property="vencimiento.cantDiasView" size="10" maxlength="20" />
				</td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.vencimiento.cantMes.label"/>: </label></td>
				<td class="normal">
						<html:text name="vencimientoAdapterVO" property="vencimiento.cantMesView" size="10" maxlength="20" />
				</td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.vencimiento.cantSemana.label"/>: </label></td>
				<td class="normal">
					<html:text name="vencimientoAdapterVO" property="vencimiento.cantSemanaView" size="10" maxlength="20" />
				</td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.vencimiento.primeroSemana.label"/>: </label></td>
				<td class="normal">
				<html:text name="vencimientoAdapterVO" property="vencimiento.primeroSemana.value" /></td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.vencimiento.ultimoSemana.label"/>: </label></td>
				<td class="normal">
				<html:text name="vencimientoAdapterVO" property="vencimiento.ultimoSemana.value" /></td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.vencimiento.esUltimo.label"/>: </label></td>
				<td class="normal">	
					<html:select name="vencimientoAdapterVO" property="vencimiento.esUltimo.id" styleClass="select">
						<html:optionsCollection name="vencimientoAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
			</tr>		
			<tr>
				<td><label><bean:message bundle="def" key="def.vencimiento.desVencimiento.label"/>: </label></td>
				<td class="normal">
						<html:text name="vencimientoAdapterVO" property="vencimiento.desVencimiento" size="30" maxlength="100" />
				</td>
			</tr>
			
			
		</table>
	</fieldset>	
	<!-- Vencimiento -->
	
<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="vencimientoAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="vencimientoAdapterVO" property="act" value="agregar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
					</html:button>
				</logic:equal>
   	    	</td>   	    	
   	    </tr>
   	</table>

	<input type="text" style="display:none"/>
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>
	
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->		
