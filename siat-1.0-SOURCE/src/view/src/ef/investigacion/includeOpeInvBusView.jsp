	<fieldset>
		<legend><bean:message bundle="ef" key="ef.opeInvBus.title"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="ef" key="ef.opeInvBus.fechaBusqueda.label"/>: </label></td>
				<td class="normal">
					<bean:write name="opeInvBusAdapterVO" property="opeInvBus.fechaBusquedaView"/>
				</td>
			</tr>
			<tr>
				<logic:equal name="opeInvBusAdapterVO" property="act" value="modificar">
					<td><label>(*)<bean:message bundle="ef" key="ef.opeInvBus.desOpeInvBus.label"/>: </label></td>
					<td class="normal">					
						<html:textarea name="opeInvBusAdapterVO" property="opeInvBus.descripcion" style="height:50px;width:300px"/>
					</td>	
				</logic:equal>
				<logic:notEqual name="opeInvBusAdapterVO" property="act" value="modificar">
					<td><label><bean:message bundle="ef" key="ef.opeInvBus.desOpeInvBus.label"/>: </label></td>
					<td class="normal">
						<bean:write name="opeInvBusAdapterVO" property="opeInvBus.descripcion"/>
					</td>	
				</logic:notEqual>							
			</tr>				
			<tr>
				<td><label><bean:message bundle="ef" key="ef.opeInvBus.paramSel.label"/>: </label></td>
				<td class="normal">
					<bean:write name="opeInvBusAdapterVO" property="opeInvBus.paramSelForHtml" filter="false"/>
				</td>
			</tr>			
		</table>
	</fieldset>