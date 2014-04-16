<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>


<script type="text/javascript">

function calcularValor(campo) {
	campo.value = eval(campo.value); 
	if(!isNumber(campo.value)){
		campo.value = 0;
	}
}

function isNumber( value )
{
return isFinite( (value * 1.0) );
} 

</script>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarAuxCaja7.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="bal" key="bal.auxCaja7Adapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- AuxCaja7 -->
	<fieldset>
		<legend><bean:message bundle="bal" key="bal.auxCaja7.title"/></legend>
		
		<table class="tabladatos">
		<!-- fecha -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.auxCaja7.fecha.label"/>: </label></td>
			<td class="normal">
				<html:text name="auxCaja7AdapterVO" property="auxCaja7.fechaView" styleId="fechaView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaView');" id="a_fechaView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>
	    <!-- Importe -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.auxCaja7.importe.label"/>: </label></td>
			<td class="normal"><html:text name="auxCaja7AdapterVO" property="auxCaja7.importeView" size="20" maxlength="255" onchange="calcularValor(this);"/></td>			
			<td><label>&nbsp;<bean:message bundle="bal" key="bal.auxCaja7.ejercicio.label"/>: </label></td>
			<td class="normal">	
				<html:select name="auxCaja7AdapterVO" property="auxCaja7.actualOVencido" styleClass="select">
					<html:optionsCollection name="auxCaja7AdapterVO" property="opcionActualVencido" label="nombreColumna" value="valor" />
				</html:select>
			</td>
		</tr>
		<!-- Partida -->
		<tr>	
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.partida.label"/>: </label></td>
			<td class="normal">
				<html:select name="auxCaja7AdapterVO" property="auxCaja7.partida.id" styleClass="select">
					<html:optionsCollection name="auxCaja7AdapterVO" property="listPartida" label="desPartidaView" value="id" />
				</html:select>
			</td>					
		</tr>
		<tr>
			<td><label><bean:message bundle="bal" key="bal.auxCaja7.observacion.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:textarea name="auxCaja7AdapterVO" property="auxCaja7.observacion" cols="80" rows="15"/>
			</td>
		</tr>
		
		</table>
	</fieldset>	
	<!-- AuxCaja7 -->
	
	<table class="tablabotones">
	   	<tr>
  	   		<td align="left">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="left">
				<logic:equal name="auxCaja7AdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="auxCaja7AdapterVO" property="act" value="agregar">
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

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
