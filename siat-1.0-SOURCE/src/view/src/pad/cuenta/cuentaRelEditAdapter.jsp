<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/pad/AdministrarCuentaRel.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<!-- Busqueda de Recuso -->
		<span id="blockBusqueda" style="display:none"> 
			<bean:define id="adapterVO" name="cuentaRelAdapterVO"/>
			<bean:define id="poseeParam" value="false" />
			<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
		</span>

		<span id="blockSimple" style="display:block">
		
		<h1><bean:message bundle="pad" key="pad.cuentaRelAdapter.title"/></h1>
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
			
		<!-- Cuenta Origen -->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.cuentaRel.cuentaOrigen.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaOrigen.recurso.desRecurso"/></td>
					<td><label><bean:message bundle="pad" key="pad.objImp.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaOrigen.objImp.clave"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaOrigen.numeroCuenta"/></td>
					<td><label><bean:message bundle="pad" key="pad.cuenta.codGesCue.ref"/>: </label></td>
					<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaOrigen.codGesCue"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cuenta.fechaAlta.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaOrigen.fechaAltaView"/></td>
					<td><label><bean:message bundle="pad" key="pad.cuenta.fechaBaja.label"/>: </label></td>
					<td class="normal">
	 				    <logic:equal name="cuentaRelAdapterVO" property="act" value="agregar">
		               		<html:text name="cuentaRelAdapterVO" property="cuentaRel.cuentaOrigen.fechaBajaView" styleId="fechaBajaView" size="15" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaBajaView');" id="a_fechaBajaView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/> 
							</a>
		               	</logic:equal>
		               	<logic:equal name="cuentaRelAdapterVO" property="act" value="modificar">
							<bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaOrigen.fechaBajaView"/>
		               	</logic:equal>
	               	</td>
				</tr>
			    <logic:equal name="cuentaRelAdapterVO" property="act" value="agregar">
					<tr>
						<td class="normal" colspan = "4"><bean:message bundle="pad" key="pad.cuenta.bajaPorVinculacion.label"/></td>
					</tr>
               	</logic:equal>
			</table>
		</fieldset>
		<!-- Fin Cuenta Origen-->
		
		<logic:equal name="cuentaRelAdapterVO" property="act" value="modificar">
			<!-- Cuenta Destino -->
			<fieldset>
				<legend><bean:message bundle="pad" key="pad.cuentaRel.cuentaDestino.title"/></legend>
				<table class="tabladatos">
					<tr>
						<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaDestino.recurso.desRecurso"/></td>
						<td><label><bean:message bundle="pad" key="pad.objImp.label"/>: </label></td>
						<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaDestino.objImp.clave"/></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.label"/>: </label></td>
						<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaDestino.numeroCuenta"/></td>
						<td><label><bean:message bundle="pad" key="pad.cuenta.codGesCue.ref"/>: </label></td>
						<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaDestino.codGesCue"/></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="pad" key="pad.cuenta.fechaAlta.label"/>: </label></td>
						<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaDestino.fechaAltaView"/></td>
						<td><label><bean:message bundle="pad" key="pad.cuenta.fechaBaja.label"/>: </label></td>
						<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaDestino.fechaBajaView"/></td>
					</tr>
				</table>
			</fieldset>
			<!-- Fin Cuenta Destino-->
		</logic:equal>
		
		<fieldset width="100%">
		<legend><bean:message bundle="pad" key="pad.cuentaRel.cuentaDestino.title"/></legend>
		<table class="tabladatos">	
		    <logic:equal name="cuentaRelAdapterVO" property="act" value="agregar">
			<!-- Cuenta -->		
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="cuentaRelAdapterVO" property="cuentaRel.cuentaDestino.recurso.id" styleClass="select" styleId="cboRecurso" style="width:90%">
						<bean:define id="includeRecursoList" name="cuentaRelAdapterVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="cuentaRelAdapterVO" property="cuentaRel.cuentaDestino.recurso.id"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
					
					<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
						<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
						src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
					</a>
					
				</td>
			</tr>
			<tr>
				<td><label>&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
			    <td class="normal" colspan="3">
					<html:text name="cuentaRelAdapterVO" property="cuentaRel.cuentaDestino.numeroCuenta" size="20"/>
					<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
						<bean:message bundle="base" key="abm.button.buscar"/>
					</html:button>
				</td>
			</tr>
			<!-- Fin Cuenta -->	
			</logic:equal>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuentaRel.fechaDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="cuentaRelAdapterVO" property="cuentaRel.fechaDesdeView" styleId="fechaDesdeView" size="10" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
					</a>
				</td>
				<td><label><bean:message bundle="pad" key="pad.cuentaRel.fechaHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="cuentaRelAdapterVO" property="cuentaRel.fechaHastaView" styleId="fechaHastaView" size="10" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
					</a>
				</td>
			</tr>
		</table>
	</fieldset>
				
	      <table class="tablabotones" width="100%">
	            <tr>
	                 <td align="left" width="50%">
	                   <html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	                      <bean:message bundle="base" key="abm.button.volver"/>
	                   </html:button>
	                </td>
	               <td align="right" width="50%">
		               <logic:equal name="cuentaRelAdapterVO" property="act" value="modificar">
		                  <html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('modificar', '');">
		                     <bean:message bundle="base" key="abm.button.modificar"/>
		                  </html:button>
		               </logic:equal>
		               <logic:equal name="cuentaRelAdapterVO" property="act" value="agregar">
		                  <html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('agregar', '');">
		                     <bean:message bundle="base" key="abm.button.agregar"/>
		                  </html:button>
		               </logic:equal>
	             	</td>
	             </tr>
	      </table>
	   </span>
	   	
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
		<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclucion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->