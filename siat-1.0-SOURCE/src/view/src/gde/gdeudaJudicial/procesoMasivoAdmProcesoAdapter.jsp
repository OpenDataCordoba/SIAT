<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>



	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/gde/AdministrarProcesoProcesoMasivo.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.title"/></h1>	
		
		<!-- Envio Judicial -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.procesoMasivo.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.id.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.idView" /></td>
				</tr>

				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.tipProMas.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.tipProMas.desTipProMas" />	</td>
				</tr>
				<!-- ViaDeuda -->
				<tr>
		    		<td><label><bean:message bundle="gde" key="gde.procesoMasivo.viaDeuda.label"/>: </label></td>
					<td class="normal" colspan="3">	
						<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.viaDeuda.desViaDeuda" />
					</td>
			    </tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.fechaEnvio.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.fechaEnvioView" />	</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.recurso.desRecurso" />	</td>
				</tr>
				<!-- Inclucion de CasoView -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>				
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.observacion.label"/>: </label> </td>
					<td class="normal" colspan="3">	<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.observacion" /> </td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.conCuentaExcSel.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.conCuentaExcSel.value" />	</td>
				</tr>
				<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.criterioProcuradorEnabled" value="true">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.procesoMasivo.utilizaCriterio.label"/>: </label></td>
						<td class="normal"><bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.utilizaCriterio.value" />	</td>
						<td><label><bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
						<td class="normal">	<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.procurador.descripcion" />	</td>
					</tr>
				</logic:equal>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.usuarioAlta.label"/>: </label></td>
					<td class="normal">	<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.usuarioAlta" /> </td>
					<td><label><bean:message bundle="pro" key="pro.corrida.usuario.ref"/>: </label></td>
					<td class="normal">	<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.corrida.usuario" /> </td>
				</tr>								
				<tr>
					<td><label><bean:message bundle="pro" key="pro.estadoCorrida.proceso.label"/>: </label></td>
					<td class="normal" colspan="3">	<bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.corrida.estadoCorrida.desEstadoCorrida" /> </td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procesoMasivo.enviadoContr.label"/></label></td>
					<td class="normal" colspan="3"><bean:write name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.enviadoContrSiNo" /></td>
				</tr>				
				
			</table>
			<p align="right">
				<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="modificarProcesoMasivoEnabled" value="enabled">
					<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.modificarEnabled" value="enabled">
						<html:button property="btnModificarProcesoMasivo"  styleClass="boton" onclick="submitForm('modificarProcesoMasivo',	'');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>	   	    			
					</logic:equal>
					<logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.modificarEnabled" value="enabled">
						<html:button property="btnModificarProcesoMasivo"  styleClass="boton" disabled="true"> 
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>	   	    			
					</logic:notEqual>
				</logic:equal>
				<logic:notEqual name="procesoMasivoAdmProcesoAdapterVO" property="modificarProcesoMasivoEnabled" value="enabled">
					<html:button property="btnModificarProcesoMasivo"  styleClass="boton" disabled="true"> 
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>	   	    			
				</logic:notEqual>
			</p>
		</fieldset>	
		<!-- Fin Envio Judicial -->
		
		<!-- Determinacion de la Deuda a Enviar -->
		<%@ include file="includeProcesoMasivoAdmSelDeuda.jsp" %>
		<!-- Fin Determinacion de la Deuda a Enviar -->

	  
		<!-- Asignar Procuradores -->
		<%@ include file="includeProcesoMasivoAdmCritProcur.jsp" %>		
		<!-- Fin Asignar Procuradores -->	  

        <!-- Por ahora los tres casos apuntan al mismo jsp, dejo los if por las dudas se necesite especializar algun caso -->
		<!-- es envio judicial ? -->
		<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.tipProMas.id" value="1"> 
		  <!-- Realizar Envio -->
		  <%@ include file="includeProcesoMasivoAdmResultado.jsp" %>
		  <!-- Fin Realizar Envio -->
		</logic:equal>

		<!-- es envio Pre Envio ? -->
		<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.tipProMas.id" value="2"> 
		  <!-- Realizar Pre Envio -->
		  <%@ include file="includeProcesoMasivoAdmResultado.jsp" %>
		  <!-- Fin Realizar Pre Envio -->
		  <%@ include file="includeProcesoMasivoAdmRetrocederFinOk.jsp" %>
		</logic:equal>

		<!-- es envio Reconfeccion ? -->
		<logic:equal name="procesoMasivoAdmProcesoAdapterVO" property="procesoMasivo.tipProMas.id" value="3"> 
		  <!-- Realizar Reconfeccion -->
		  <%@ include file="includeProcesoMasivoAdmResultado.jsp" %>
		  <!-- Fin Realizar Reconfeccion -->
		  <%@ include file="includeProcesoMasivoAdmRetrocederFinOk.jsp" %>
		</logic:equal>

		
		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	 		
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
		<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="fileParam" value=""/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		<input type="hidden" id="locateFocus" name="locateFocus" value="<%= request.getParameter("locateFocus")%>"/>
		

	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
	
