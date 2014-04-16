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
					<bean:write name="AtrVal" property="atributo.desAtributo"/>&nbsp;
				</label>
			</td>
			
			<td class="normal"
				<logic:equal name="AtrVal" property="poseeVigencia" value="true">
					colspan="2"
				</logic:equal>
			> <!-- Cierre del td -->
				<bean:write name="AtrVal" property="valorView"/>&nbsp;
			</td>
		</logic:equal>
		
		<!-- Posee Dominio -->
		<logic:equal name="AtrVal" property="poseeDominio" value="true">
			<td
				<logic:equal name="AtrVal" property="poseeVigencia" value="true">
					colspan="2"
				</logic:equal>
			> <!-- Cierre del td -->
				<label>					
					<bean:write name="AtrVal" property="atributo.desAtributo"/>&nbsp;
				</label>
			</td>
			
			<!-- No es Multivalor -->
			<logic:equal name="AtrVal" property="esMultivalor" value="false">
				<td class="normal" 
					<logic:equal name="AtrVal" property="poseeVigencia" value="true">
						colspan="2"
					</logic:equal>
				> <!-- Cierre del td -->
				 
					<bean:write name="AtrVal" property="valorFromDominioView"/>&nbsp;
				</td>
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
						<bean:write name="AtrValChk" property="desValor"/>&nbsp; <br/>
					</logic:iterate>
				</td>
			</logic:equal>
		</logic:equal>

</tr>  <!-- Fin fila Atributo   -->
	
<!-- Posee Vigencia -->
<logic:equal name="AtrVal" property="poseeVigencia" value="true">
<logic:notEmpty name="AtrVal" property="listAtrValVig">
<tr>	
	<td colspan="4">
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
        <tbody>
        	<tr>				
				<th align="left">Fecha Desde</th> <!-- Fecha Desde -->
				<th align="left">Valor</th> <!-- Valor -->
			</tr>
		
			<logic:iterate id="AtrValVig" name="AtrVal" property="listAtrValVig">					
			<tr>	
				<td>
					<bean:write name="AtrValVig" property="fechaDesdeView"/>&nbsp;
				</td>
				<td>
					<bean:write name="AtrValVig" property="valor"/>&nbsp;
				</td>
			</tr>
			</logic:iterate>		
		</tbody>
		</table>	
	</td>
</tr>
</logic:notEmpty>
</logic:equal>
	