<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/RelacionarCuenta.do">
	
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
               <td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
               <td class="normal"><bean:write name="relacionarCuentaAdapterVO" property="cuenta.recurso.desRecurso"/></td>
               <td><label><bean:message bundle="pad" key="pad.objImp.label"/>: </label></td>
               <td class="normal"><bean:write name="relacionarCuentaAdapterVO" property="cuenta.objImp.clave"/></td>
            </tr>
            <tr>
               <td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.label"/>: </label></td>
               <td class="normal"><bean:write name="relacionarCuentaAdapterVO" property="cuenta.numeroCuenta"/></td>
               <td><label><bean:message bundle="pad" key="pad.cuenta.codGesCue.label"/>: </label></td>
               <td class="normal"><bean:write name="relacionarCuentaAdapterVO" property="cuenta.codGesCue"/></td>
            </tr>
            <tr>
               <td><label><bean:message bundle="pad" key="pad.cuenta.fechaAlta.label"/>: </label></td>
               <td class="normal"><bean:write name="relacionarCuentaAdapterVO" property="cuenta.fechaAltaView"/></td>
               <td><label><bean:message bundle="pad" key="pad.cuenta.fechaBaja.label"/>: </label></td>
               <td class="normal"><bean:write name="relacionarCuentaAdapterVO" property="cuenta.fechaBajaView"/></td>
            </tr>
            <!-- Descripcion del domicilio de envio -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.desDomEnv.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="relacionarCuentaAdapterVO" property="cuenta.desDomEnv"/>
				</td>
			</tr>
			<!-- Catastral del domicilio de envio -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.catDomEnv.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="relacionarCuentaAdapterVO" property="cuenta.catDomEnv"/>
				</td>
			</tr>
            <!-- Observacion -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.observacion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="relacionarCuentaAdapterVO" property="cuenta.observacion"/>
				</td>
			</tr>

         </table>
      </fieldset>
      <!-- Fin Cuenta -->

     <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
        <caption><bean:message bundle="pad" key="pad.cuenta.listCuentaRel.label"/></caption>
         <tbody>
           <logic:notEmpty  name="relacionarCuentaAdapterVO" property="listCuentaRel">
               <tr>
                 <th width="1">&nbsp;</th> <!-- Ver -->
                 <th width="1">&nbsp;</th> <!-- Modificar -->
                 <th width="1">&nbsp;</th> <!-- Eliminar -->
                 <th align="left"><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.label"/></th>
                 <th align="left"><bean:message bundle="pad" key="pad.cuentaRel.fechaDesde.label"/></th>
                 <th align="left"><bean:message bundle="pad" key="pad.cuentaRel.fechaHasta.label"/></th>
              </tr>
              <logic:iterate id="CuentaRelVO" name="relacionarCuentaAdapterVO" property="listCuentaRel">
                 <tr>
                    <!-- Ver -->
                    <td>
						<logic:equal name="relacionarCuentaAdapterVO" property="verCuentaRelEnabled" value="enabled">
							<logic:equal name="CuentaRelVO" property="verEnabled" value="enabled">
								<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verCuentaRel', '<bean:write name="CuentaRelVO" property="id" bundle="base" formatKey="general.format.id"/>');">
									<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
								</a>
							</logic:equal>	
							<logic:notEqual name="CuentaRelVO" property="verEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
							</logic:notEqual>
						</logic:equal>
						<logic:notEqual name="relacionarCuentaAdapterVO" property="verCuentaRelEnabled" value="enabled">
							<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
						</logic:notEqual>
					</td>
					<!-- Modificar-->								
					<td>
						<logic:equal name="relacionarCuentaAdapterVO" property="modificarCuentaRelEnabled" value="enabled">
							<logic:equal name="CuentaRelVO" property="modificarEnabled" value="enabled">
								<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarCuentaRel', '<bean:write name="CuentaRelVO" property="id" bundle="base" formatKey="general.format.id"/>');">
									<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
								</a>
							</logic:equal>
							<logic:notEqual name="CuentaRelVO" property="modificarEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
							</logic:notEqual>
						</logic:equal>
						<logic:notEqual name="relacionarCuentaAdapterVO" property="modificarCuentaRelEnabled" value="enabled">
							<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
						</logic:notEqual>
					</td>
                    <!-- Eliminar-->								
					<td>
						<logic:equal name="relacionarCuentaAdapterVO" property="eliminarCuentaRelEnabled" value="enabled">
							<logic:equal name="CuentaRelVO" property="eliminarEnabled" value="enabled">
								<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarCuentaRel', '<bean:write name="CuentaRelVO" property="id" bundle="base" formatKey="general.format.id"/>');">
									<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
								</a>
							</logic:equal>	
							<logic:notEqual name="CuentaRelVO" property="eliminarEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
							</logic:notEqual>
						</logic:equal>
						<logic:notEqual name="relacionarCuentaAdapterVO" property="eliminarCuentaRelEnabled" value="enabled">
							<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
						</logic:notEqual>
					</td>
                    <td><bean:write name="CuentaRelVO" property="cuentaDestino.numeroCuenta" />&nbsp;</td>
                    <td><bean:write name="CuentaRelVO" property="fechaDesdeView"/>&nbsp;</td>
                    <td><bean:write name="CuentaRelVO" property="fechaHastaView" />&nbsp;</td>
                 </tr>
              </logic:iterate>
           </logic:notEmpty>
           <logic:empty  name="relacionarCuentaAdapterVO" property="listCuentaRel">
              <tr><td align="center">
              <bean:message bundle="base" key="base.noExistenRegitros"/>
              </td></tr>
           </logic:empty>
           	<!-- Relacionar -->
			<tr>
				<td colspan="20" align="right">
	  				<bean:define id="agregarCuentaRelEnabled" name="relacionarCuentaAdapterVO" property="agregarCuentaRelEnabled"/>
	  					<input type="button" <%=agregarCuentaRelEnabled%> class="boton" 
							onClick="submitForm('agregarCuentaRel', ' ');" 
							value="<bean:message bundle="base" key="abm.button.relacionar"/>"
						/>
				</td>
			</tr>
        </tbody>
     </table>

	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
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