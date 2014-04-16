<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarEvento.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.eventoEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>	
	<!-- Evento -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.evento.title"/></legend>
		
		<table class="tabladatos">
			<!-- Codigo -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.evento.codigo.label"/>: </label></td>
				<td class="normal">
					<logic:equal name="eventoAdapterVO" property="act" value="modificar">
						<bean:write name="eventoAdapterVO" property="evento.codigoView"/>
				 	</logic:equal>
					<logic:equal name="eventoAdapterVO" property="act" value="agregar">
						<html:text name="eventoAdapterVO" property="evento.codigoView" size="15" maxlength="20" />
				 	</logic:equal>	
				</td>
			</tr>
			<!-- Descripcion -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.evento.descripcion.label"/>: </label></td>
				<td class="normal"><html:text name="eventoAdapterVO" property="evento.descripcion" size="20" maxlength="100"/></td>			
			</tr>

			<!-- Etapa Procesal -->
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.evento.etapaProcesal.label"/>: </label></td>
				<td class="normal">
					<html:select name="eventoAdapterVO" property="evento.etapaProcesal.id" styleClass="select">
						<html:optionsCollection name="eventoAdapterVO" property="listEtapaProcesal" label="desEtapaProcesal" value="id" />
					</html:select>
				</td>					
			</tr>

			<!-- AfectaCadJui -->
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.evento.afectaCadJui.label"/>: </label></td>
				<td class="normal">
					<html:select name="eventoAdapterVO" property="evento.afectaCadJui.id" styleClass="select">
						<html:optionsCollection name="eventoAdapterVO" property="listSiNoAfectaCadJui" label="value" value="id" />
					</html:select>
				</td>					
			</tr>

			<!-- AfectaPresSen -->
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.evento.afectaPresSen.label"/>: </label></td>
				<td class="normal">
					<html:select name="eventoAdapterVO" property="evento.afectaPresSen.id" styleClass="select">
						<html:optionsCollection name="eventoAdapterVO" property="listSiNoAfectaPresSen" label="value" value="id" />
					</html:select>
				</td>					
			</tr>

			<!-- Es Unico en Gesjud -->
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.evento.esUnicoEnGesJud.label"/>: </label></td>
				<td class="normal">
					<html:select name="eventoAdapterVO" property="evento.esUnicoEnGesJud.id" styleClass="select">
						<html:optionsCollection name="eventoAdapterVO" property="listSiNoAfectaPresSen" label="value" value="id" />
					</html:select>
				</td>					
			</tr>
						
		</table>
	</fieldset>	
	<!-- Evento -->

	<!-- Lista de Predecesores -->
		<div class="horizscroll">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="gde" key="gde.evento.predecesores.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="eventoAdapterVO" property="listEventos">
				    	<tr>
							<th align="left"><bean:message bundle="gde" key="gde.eventoEditAdapter.seleccionar"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.evento.codigo.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.evento.descripcion.label"/></th>
						</tr>
						<logic:iterate id="eventoVO" name="eventoAdapterVO" property="listEventos">
							<tr>
					  			<td align="center">
									<logic:equal name="eventoVO" property="esSeleccionable" value="true">
				  						<html:multibox name="eventoAdapterVO" property="listIdSelected" >
            	        	            	<bean:write name="eventoVO" property="idView"/>
                	        	        </html:multibox>
			  						</logic:equal>

				  					<logic:notEqual name="eventoVO" property="esSeleccionable" value="true">
				  						<input type="checkbox" disabled="disabled"/>
				  					</logic:notEqual>
					  			</td>
								<td><bean:write name="eventoVO" property="codigoView" />&nbsp;</td>
								<td><bean:write name="eventoVO" property="descripcion" />&nbsp;</td>								
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					
					<logic:empty  name="eventoAdapterVO" property="listEventos">
						<tr>
							<td align="center">
								<bean:message bundle="base" key="base.noExistenRegitros"/>
							</td>
						</tr>
					</logic:empty>					

				</tbody>
			</table>
		</div>	
	<!-- Lista de Predecesores -->

	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="eventoAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="eventoAdapterVO" property="act" value="agregar">
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
