	<tr>
		<!-- No posee Dominio -->
		<logic:equal name="AtrVal" property="poseeDominio" value="false">
			<td>
				<label>
					<logic:equal name="AtrVal" property="esRequerido" value="true">
						(*)&nbsp;
					</logic:equal>
					<bean:write name="AtrVal" property="atributo.desAtributo"/>: 
				</label>
			</td>
			
			<td class="normal"> 
				<input type="text" size="20" maxlength="100" 
					name='<bean:write name="AtrVal" property="atributo.codAtributo"/>'
					id='<bean:write name="AtrVal" property="atributo.codAtributo"/>'
					value='<bean:write name="AtrVal" property="valorView"/>' 
				/>
				<!-- Si el tipo de dato es una fecha muestro el calendario -->
				<logic:equal name="AtrVal" property="atributo.tipoAtributo.codTipoAtributo" value="Date">								
					<a class="link_siat" onclick="return show_calendar('<bean:write name="AtrVal" property="atributo.codAtributo"/>');" 
						id="a_<bean:write name="AtrVal" property="atributo.codAtributo"/>">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</logic:equal>
			</td>
		</logic:equal>
		
		<!-- Posee Dominio -->
		<logic:equal name="AtrVal" property="poseeDominio" value="true">
			<td>
				<label>
					<logic:equal name="AtrVal" property="esRequerido" value="true">
						(*)&nbsp;
					</logic:equal>
					<bean:write name="AtrVal" property="atributo.desAtributo"/>:
				</label>
			</td>
			
			<!-- No es Multivalor -->
			<logic:equal name="AtrVal" property="esMultivalor" value="false">
				<td class="normal"> 
					<!-- Combo -->
					<bean:define id="selVal" name="AtrVal" property="valorView"/>
					<% String selectedVal = ""+selVal+"";%>
					
					<select name='<bean:write name="AtrVal" property="atributo.codAtributo"/>'>
						<option value='-1'>Seleccionar...</option>
						<logic:iterate id="AtrValChk" name="AtrVal" property="atributo.domAtr.listDomAtrVal">
							<option value='<bean:write name="AtrValChk" property="valor" bundle="base" formatKey="general.format.id"/>'
								<logic:equal name="AtrValChk" property="valor" value="<%=selectedVal%>">
									selected='selected'
								</logic:equal>
								> <!-- Cierre del opcion -->
								<bean:write name="AtrValChk" property="desValor"/>
							</option>
						</logic:iterate>
					</select>
				</td>
			</logic:equal>
			
			<!-- Es Multivalor -->
			<logic:equal name="AtrVal" property="esMultivalor" value="true">
				<td>&nbsp;</td>
				
			</tr>
			<tr>
				<td class="normal" colspan="2">
					<div id="listaMultivalor" class="scrolable" style="height: 100px;">
						<!-- Checkbox -->
						<logic:iterate id="AtrValChk" name="AtrVal" property="atributo.domAtr.listDomAtrVal">
							<input type="checkbox" name='<bean:write name="AtrVal" property="atributo.codAtributo"/>.<bean:write name="AtrValChk" property="valor" bundle="base" formatKey="general.format.id"/>'
								<logic:iterate id="ChkdVal" name="AtrVal" property="listMultiValor">
							    	<bean:define id="chkVal" name="ChkdVal"/> 
							    	
							    	<% String chekedVal = ""+chkVal+""; %>						    						    	
							    	
							    	<logic:equal name="AtrValChk" property="valor" value="<%=chekedVal%>">
										checked='checked'
									</logic:equal>						    	
								</logic:iterate>
							/> 
							<bean:write name="AtrValChk" property="valor"/> - 
							<bean:write name="AtrValChk" property="desValor"/> <br/>
						</logic:iterate>
					</div>	
				</td>
			</logic:equal>
		</logic:equal>
	<!-- Para evitar el error del enter en las cajas de texto -->
    <input type="text" style="display:none"/>	 
	</tr>