<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarSolicitudEmiPerRetro.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="ef" key="ef.solicitudEmiPerRetroEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- SolicitudEmiPerRetro -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.solicitudEmiPerRetroEditAdapter.title"/></legend>
		
		<table class="tabladatos">
  			<tr>	
				<td><label><bean:message bundle="ef" key="ef.ordenControl.label"/>: </label></td>
				<td class="normal">
					<html:select name="ordenControlAdapterVO" property="ordenControl.id" styleClass="select" onchange="submitForm('paramOrdenControl','')">
						<html:optionsCollection name="ordenControlAdapterVO" property="listOrdenControl" label="ordenControlView" value="id" />
					</html:select>
				</td>					
			</tr>
			
			 <!-- Lista OrdConCue -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/></label></td>
				<td class="normal" colspan="3" align="left">
					
					<!-- se muestran las cuentas agrupadas por recurso, pero se submite el id de ordConCue -->
					<html:select name="ordenControlAdapterVO" property="ordConCue.id" styleClass="select" onchange="submitForm('paramOrdConCue','')" >
						<% String idCategoriaAnt = "";%>
						<bean:define id="listOrdConCue" name="ordenControlAdapterVO" property="ordenControl.listOrdConCue"/>
						<logic:notEmpty name="listOrdConCue">
							<logic:iterate id="ordConCue" name="listOrdConCue">
								
								<bean:define id="recurso" name="ordConCue" property="cuenta.recurso"/>
								
								<logic:equal name="ordConCue" property="id" value="-1">
									<option value='<bean:write name="ordConCue" property="id" bundle="base" formatKey="general.format.id"/>'>
										Seleccionar...
									</option>
								</logic:equal>
								
								<logic:notEqual name="ordConCue" property="id" value="-1">
									<logic:notEqual name="recurso" property="categoria.id" value="<%=idCategoriaAnt%>">
										<optgroup label='<bean:write name="recurso" property="categoria.desCategoria"/>'>
										<bean:define id="catId" name="recurso" property="categoria.id"/>
										<% idCategoriaAnt = ""+catId+"";%>
									</logic:notEqual>
									<logic:equal name="recurso" property="categoria.id" value="<%=idCategoriaAnt%>">
										<bean:define id="ordConCueId" name="ordenControlAdapterVO" property="ordConCue.id"/>
										<% String idOrdConCue = ""+ordConCueId+"";%>				         
											<option value='<bean:write name="ordConCue" property="id" bundle="base" formatKey="general.format.id"/>'												 					
												<logic:equal name="ordConCue" property="id" value="<%=idOrdConCue%>">
													selected="selected"											
												</logic:equal>
											>
											<bean:write name="ordConCue" property="cuenta.numeroCuenta"/>
										</option>
									</logic:equal>
									<logic:notEqual name="recurso" property="categoria.id" value="<%=idCategoriaAnt%>">	
										</optgroup>
									</logic:notEqual>
								</logic:notEqual>
							</logic:iterate>
						</logic:notEmpty>
					</html:select>	
        		</td>
			</tr>
	
		<!-- periodoDesde -->
	
			<tr>
				<td><LABEL><bean:message bundle="ef" key="ef.periodoOrdenEditAdapter.periodoDesde.label"/></LABEL></td>
				<td class="normal">
					<html:text name="ordenControlAdapterVO" property="periodoOrdenDesde.periodo" size="3" maxlength="2"/>/
					<html:text name="ordenControlAdapterVO" property="periodoOrdenDesde.anio"  size="5" maxlength="4"/>
					(mm/aaaa)
				</td>		
			
			<!-- periodoHasta -->	
				<td><LABEL><bean:message bundle="ef" key="ef.periodoOrdenEditAdapter.periodoHasta.label"/></LABEL></td>
				<td class="normal">
					<html:text name="ordenControlAdapterVO" property="periodoOrdenHasta.periodo"  size="3" maxlength="2"/>/
					<html:text name="ordenControlAdapterVO" property="periodoOrdenHasta.anio"  size="5" maxlength="4"/>
					(mm/aaaa)
				</td>
			</tr>					
			<!-- <#Campos#> -->
			<tr>
		      	<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.ordenControl.observacion.label"/></label></td>
			    <td class="normal" colspan="3">
					<html:textarea name="ordenControlAdapterVO" property="observacion" rows="2" cols="30"/>
			    </td>
		    </tr>		
		</table>
			
	</fieldset>	
	<!-- OrdenControl -->
			
	<table class="tablabotones">
	   	<tr>
	    	<td align="left">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="left">
		    	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('enviarSolicitud', '');">
			    	<bean:message bundle="ef" key="ef.ordenControlAdapter.button.enviarSol"/>
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
<!-- periodoOrdenEditAdapter.jsp -->