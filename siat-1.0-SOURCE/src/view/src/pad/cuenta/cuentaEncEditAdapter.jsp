<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">
          <%@include file="/base/submitForm.js"%>  
</script>

   <!-- Tabla que contiene todos los formularios -->
   <html:form styleId="filter" action="/pad/AdministrarEncCuenta.do">

      <!-- Mensajes y/o Advertencias -->
      <%@ include file="/base/warning.jsp" %>
      <!-- Errors  -->
      <html:errors bundle="base"/>
      
      <h1><bean:message bundle="pad" key="pad.cuentaAdapter.title"/></h1>

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
      <!-- Cuenta -->
      <fieldset>
         <legend><bean:message bundle="pad" key="pad.cuenta.title"/></legend>
         <table class="tabladatos">
            <tr>
               <td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
               <td class="normal"><bean:write name="encCuentaAdapterVO" property="cuenta.recurso.desRecurso"/></td>
            </tr>
            <tr>
               <td><label><bean:message bundle="pad" key="pad.objImp.label"/>: </label></td>
               <td class="normal" colspan="3"><bean:write name="encCuentaAdapterVO" property="cuenta.objImp.clave"/></td>
            </tr>
            <tr>
               <td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.label"/>: </label></td>
               <td class="normal"><bean:write name="encCuentaAdapterVO" property="cuenta.numeroCuenta"/></td>
               <td><label><bean:message bundle="pad" key="pad.cuenta.codGesCue.label"/>: </label></td>
               <td class="normal"><bean:write name="encCuentaAdapterVO" property="cuenta.codGesCue"/></td>
            </tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.fechaAlta.label"/>: </label></td>
				<td class="normal">
					<html:text name="encCuentaAdapterVO" property="cuenta.fechaAltaView" styleId="fechaAltaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaAltaView');" id="a_fechaAltaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
					</a>
				</td>
               <td><label><bean:message bundle="pad" key="pad.cuenta.fechaBaja.label"/>: </label></td>
               <td class="normal">
               		<html:text name="encCuentaAdapterVO" property="cuenta.fechaBajaView" styleId="fechaBajaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaBajaView');" id="a_fechaBajaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/> 
					</a>
               	</td>
			</tr>
			<tr>	
				<!-- Es Excluida de la Emision -->
				<td><label><bean:message bundle="pad" key="pad.cuenta.esExcluidaEmision.label"/>: </label></td>
				<td class="normal">
					<html:select name="encCuentaAdapterVO" property="cuenta.esExcluidaEmision.id"  styleClass="select">
						<html:optionsCollection name="encCuentaAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
			</tr>
			<tr>	
				<!-- Es Excluida de Impresion -->
				<td><label><bean:message bundle="pad" key="pad.cuenta.permiteImpresion.label"/>: </label></td>
				<td class="normal">
					<html:select name="encCuentaAdapterVO" property="cuenta.permiteImpresion.id"  styleClass="select">
						<html:optionsCollection name="encCuentaAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
			</tr>
			
			<!-- Observacion -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.observacion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:textarea name="encCuentaAdapterVO" property="cuenta.observacion" cols="80" rows="15"/>
				</td>
			</tr>
			
         </table>
      </fieldset>
      <!-- Fin Cuenta -->
		      
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<html:button property="btnModificar"  styleClass="boton" onclick="submitForm('modificar', '');">
					<bean:message bundle="base" key="abm.button.modificar"/>
				</html:button>
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