	<tr>
		<!-- No posee Dominio -->
		<logic:equal name="AtrVal" property="poseeDominio" value="false">
			<!-- Si posee vigencia colspan=4 -->
			<td
				<logic:equal name="AtrVal" property="poseeVigencia" value="true">
					colspan="2"
				</logic:equal>
			> <!-- Cierre del td -->
			
				<label>
					<logic:equal name="AtrVal" property="esRequerido" value="true">
						(*)&nbsp;
					</logic:equal>
					<bean:write name="AtrVal" property="atributo.desAtributo"/>
				</label>
			&nbsp;
			</td>
			
			<td class="normal"
				<logic:equal name="AtrVal" property="poseeVigencia" value="true">
					colspan="2"
				</logic:equal>
			> <!-- Cierre del td -->
			
				<!-- Si no posee Vigencia seteo el valor -->
				<input type="text" size="10" maxlength="100" 
					name='<bean:write name="AtrVal" property="atributo.codAtributo"/>'
					id='<bean:write name="AtrVal" property="atributo.codAtributo"/>'
					<logic:equal name="AtrVal" property="showValor" value="true">
						value='<bean:write name="AtrVal" property="valorView"/>' 
					</logic:equal>
				/>
				<!-- Si el tipo de dato es una fecha muestro el calendario -->
				<logic:equal name="AtrVal" property="atributo.tipoAtributo.codTipoAtributo" value="Date">								
					<a class="link_siat" onclick="return show_calendar('<bean:write name="AtrVal" property="atributo.codAtributo"/>');" 
						id="a_<bean:write name="AtrVal" property="atributo.codAtributo"/>">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</logic:equal>
			<!-- /td -->
		</logic:equal>
		
		<!-- Posee Dominio -->
		<logic:equal name="AtrVal" property="poseeDominio" value="true">
			<td
				<logic:equal name="AtrVal" property="poseeVigencia" value="true">
					colspan="2"
				</logic:equal>
			> <!-- Cierre del td -->
				<label>
					<logic:equal name="AtrVal" property="esRequerido" value="true">
						(*)&nbsp;
					</logic:equal>
					<bean:write name="AtrVal" property="atributo.desAtributo"/>
				</label>
			&nbsp;
			</td>
			
			<!-- No es Multivalor -->
			<logic:equal name="AtrVal" property="esMultivalor" value="false">
				<td class="normal" 
					<logic:equal name="AtrVal" property="poseeVigencia" value="true">
						colspan="2"
					</logic:equal>
				> <!-- Cierre del td -->
				 
					<!-- Combo -->
					<bean:define id="selVal" name="AtrVal" property="valorView"/>
					<% String selectedVal = ""+selVal+"";%>
					
					<select name='<bean:write name="AtrVal" property="atributo.codAtributo"/>'>
						<option value='-1'>Seleccionar...</option>
						
						<!-- Si no posee vigencia, dejo la opcion seleccionada en el combo Sino queda el Seleccionar -->						
						<logic:iterate id="AtrValChk" name="AtrVal" property="atributo.domAtr.listDomAtrVal">
							<option value='<bean:write name="AtrValChk" property="valor" bundle="base" formatKey="general.format.id"/>'
								
								<logic:equal name="AtrVal" property="showValor" value="true">
									<logic:equal name="AtrValChk" property="valor" value="<%=selectedVal%>">
										selected="selected"
									</logic:equal>
								</logic:equal>
								
								> <!-- Cierre del opcion -->
								
								<bean:write name="AtrValChk" property="desValor"/>
							</option>
						</logic:iterate>
					</select>
				<!-- /td-->
			</logic:equal>
			
			<!-- Es Multivalor -->
			<logic:equal name="AtrVal" property="esMultivalor" value="true">
				<td class="normal"
					<logic:equal name="AtrVal" property="poseeVigencia" value="true">
						colspan="2"
					</logic:equal>
				> <!-- Cierre del td -->
				 
					<!-- Checkbox -->
					<logic:iterate id="AtrValChk" name="AtrVal" property="atributo.domAtr.listDomAtrVal">
						<input type="checkbox" name='<bean:write name="AtrVal" property="atributo.codAtributo"/>.<bean:write name="AtrValChk" property="id" bundle="base" formatKey="general.format.id"/>'
							<logic:iterate id="ChkdVal" name="AtrVal" property="listValor">
						    	<bean:define id="chkVal" name="ChkdVal"/> 
						    	<% String chekedVal = ""+chkVal+""; %>
						    	
						    	<logic:equal name="AtrValChk" property="valor" value="<%=chekedVal%>">
									checked="checked"
								</logic:equal>						    	
							</logic:iterate>
						/> 
						<bean:write name="AtrValChk" property="desValor"/> <br/>
					</logic:iterate>
				<!-- /td-->
			</logic:equal>
		</logic:equal>
	
	
	
	&nbsp;
	</td> <!-- td cierre del combo, text o check del valor a ingresar -->
	
</tr>  <!-- Fin fila Atributo   -->
	
<!-- Posee Vigencia -->
<logic:equal name="AtrVal" property="poseeVigencia" value="true">
<tr>

	<td><label>(*)&nbsp; Fecha Novedad</label></td>
	<td class="normal"> 
		<input type="text" size="10" maxlength="10" name="fechaNovedadView" id="fechaNovedadView"
		value='<bean:write name="AtrVal" property="fechaNovedadView"/>'
		/>
		<a class="link_siat" onclick="return show_calendar('fechaNovedadView');" id="a_fechaNovedadView">
			<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
	</td>
	<td><label>(*)&nbsp; Fecha Desde</label></td>
	<td>
		<input type="text" size="10" maxlength="10" name="fechaDesdeView" id="fechaDesdeView"
		value='<bean:write name="AtrVal" property="fechaDesdeView"/>'
		/>
		<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
			<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>				
	</td>				
</tr>

<tr>
	<td colspan="4">
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
        <tbody>
        	<tr>
				<th align="left">Fecha Novedad</th> <!-- Fecha Novedad -->
				<th align="left">Fecha Desde</th> <!-- Fecha Desde -->
				<th align="left">Fecha Hasta</th> <!-- Fecha Hasta -->
				<th align="left">Valor</th> <!-- Valor -->
			</tr>
				
			<logic:notEmpty name="AtrVal" property="listAtrValVig">
				<logic:iterate id="AtrValVig" name="AtrVal" property="listAtrValVig">					
				<tr>
					<td>
						<bean:write name="AtrValVig" property="fechaNovedadView"/>&nbsp;
					</td>
					<td>
						<bean:write name="AtrValVig" property="fechaDesdeView"/>&nbsp; 
					</td>
					<td>
						<bean:write name="AtrValVig" property="fechaHastaView"/>&nbsp;
					</td>
					<td>
						<bean:write name="AtrValVig" property="valor"/>&nbsp;
					</td>
				</tr>
				</logic:iterate>
			</logic:notEmpty>
		</tbody>
		</table>	
	</td>
</tr>
</logic:equal>
	