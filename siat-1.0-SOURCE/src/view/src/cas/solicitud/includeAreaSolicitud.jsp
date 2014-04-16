	<logic:notEmpty name="Sender" property="listAreaSolicitud">
		<fieldset>
			<legend><bean:message bundle="cas" key="cas.areaSolicitud.title"/></legend>
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
		    	<caption><bean:message bundle="cas" key="cas.areaSolicitud.seleccionAreas.title"/></caption>
		      	<tbody>
		      	<tr>
		      		<td colspan="2">
		      			<label><bean:message bundle="cas" key="cas.areaSolicitud.solicitudAEnviar.title"/>:</label> &nbsp;
						<bean:write name="Sender" property="listAreaSolicitud[0].tipoSolicitud.descripcion"/>
		      		</td>
		      	</tr>
		       	<tr>
		       		<th align="center">
		       			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		       			<input type="checkbox" name="checkAll" onclick="changeChk('filter', 'listIdAreaSolicSelected', this)"/>
		       		</th>
				  	<th align="left"><bean:message bundle="def" key="def.area.label"/></th>
				</tr>
		       	<logic:iterate id="AreaSolicitudVO" name="Sender" property="listAreaSolicitud">
					<tr>
			  			<td align="center">
			  				<html:multibox name="Sender" property="listIdAreaSolicSelected">
	             	            <bean:write name="AreaSolicitudVO" property="areaDestino.id" bundle="base" formatKey="general.format.id"/>
	                 	    </html:multibox>
			  			</td>
						<td>
							<bean:write name="AreaSolicitudVO" property="areaDestino.desArea"/>
						</td>					
					</tr>
				</logic:iterate>
			</table>
		</fieldset>
	</logic:notEmpty>
	