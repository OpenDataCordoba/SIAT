	<tr>
		<!-- No posee Dominio -->
		<logic:equal name="AtrVal" property="poseeDominio" value="false">
			<td>
				<label><bean:write name="AtrVal" property="atributo.desAtributo"/></label>&nbsp;
			</td>
			<td class="normal"> 
				<bean:write name="AtrVal" property="valorView"/>&nbsp;
			</td>
		</logic:equal>
		
		<!-- Posee Dominio -->
		<logic:equal name="AtrVal" property="poseeDominio" value="true">
			<td>
				<label><bean:write name="AtrVal" property="atributo.desAtributo"/></label>&nbsp;
			</td>
			
			<!-- No es Multivalor -->
			<logic:equal name="AtrVal" property="esMultivalor" value="false">
				<td class="normal">
					<bean:write name="AtrVal" property="valorFromDominioView"/>&nbsp;
				</td>
			</logic:equal>
			
			<!-- Es Multivalor -->
			<logic:equal name="AtrVal" property="esMultivalor" value="true">
				<td class="normal"> 
					<!-- Valores del dominio -->
					<logic:iterate id="AtrValChk" name="AtrVal" property="atributo.domAtr.listDomAtrVal">
						<logic:iterate id="ChkdVal" name="AtrVal" property="listMultiValor">
					    	<bean:define id="chkVal" name="ChkdVal"/> 
					    	<% String chekedVal = ""+chkVal+""; %>
					    	<logic:equal name="AtrValChk" property="valor" value="<%=chekedVal%>">
								<bean:write name="AtrValChk" property="desValor"/> <br/>
							</logic:equal>						    	
						</logic:iterate>
					</logic:iterate>
					&nbsp;
				</td>
			</logic:equal>
		</logic:equal>
	</tr>