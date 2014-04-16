<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<SCRIPT>
// Registrar todos los id de menu
var coll = new Array('inf', 'est', 'hc', 'tur', 'ate', 'frm', 'per', 'seg', 'adm', 'sis');

function showhide(list) {
    for (i = 0; i < coll.length; i++) {
	    	
       	if (coll[i] != list || list == '') {
       		if (document.getElementById(coll[i])!=null) {
	            document.getElementById(coll[i]).style.display = "none";
            }
   	    } else {
           	var listElementStyle = document.getElementById(list).style;
            if (listElementStyle.display == "none" || listElementStyle.display == "") {            
   	            listElementStyle.display = "block";
       	    } else {
           	    listElementStyle.display = "none";
            } 
    	}
	}
}

</SCRIPT>


<table width="100%" border="0" cellpadding="0" cellsapcing="0">
	<tr>
		<td>
			<div class="menu">
				<ul class="level1">
					<logic:equal name="userMenu" property="informesEnabled" value="enabled" >
						<li class="encabezado"><a href="javascript:showhide('inf')">Informes</a></li>
						<li id="inf" >
							<ul class="level2">
					<!-- Informes Operativos -->		
					<logic:equal name="userMenu" property="buscarPacienteAtendidoInfEnabled" value="enabled">
						<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarPacienteAtendidoInf&menu=inf">Pac. Atendidos</a> </li>
					</logic:equal>
					<logic:equal name="userMenu" property="buscarPacienteAdscriptoInfEnabled" value="enabled">
						<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarPacienteAdscriptoInf&menu=inf">Pac.Adscriptos</a> </li>
					</logic:equal>
					<logic:equal name="userMenu" property="buscarPacienteAdsAteFuErInfEnabled" value="enabled">
						<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarPacienteAdsAteFuErInf&menu=inf">Pac.Adsc.Ate.fuera ER.</a> </li>
					</logic:equal>
					<logic:equal name="userMenu" property="buscarPacienteDerivadoInfEnabled" value="enabled">
						<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarPacienteDerivadoInf&menu=inf">Pac. Derivados</a> </li>
					</logic:equal>
					<logic:equal name="userMenu" property="buscarPacienteAsistenciaInfEnabled" value="enabled">
						<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarPacienteAsistenciaInf&menu=inf">Asistencia Pac.</a> </li>
					</logic:equal>
					<logic:equal name="userMenu" property="buscarPacienteDiagTratPracInfEnabled" value="enabled">
						<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarPacienteDiagTratPracInf&menu=inf">Pac. Diag. Trat. Prac.</a> </li>
					</logic:equal>
					<logic:equal name="userMenu" property="buscarPersonalInfEnabled" value="enabled">
						<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarPersonalInf&menu=inf">Personal</a> </li>
					</logic:equal>
					<logic:equal name="userMenu" property="buscarAgendaPersonalInfEnabled" value="enabled">
						<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarAgendaPersonalInf&menu=inf">Agenda Personal</a> </li>
					</logic:equal>
					<logic:equal name="userMenu" property="buscarEstadoTurnoInfEnabled" value="enabled">
						<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarEstadoTurnoInf&menu=inf">Estado Turno</a> </li>
					</logic:equal>

					<!-- Informes Operativos Anteriores -->
					<logic:equal name="userMenu" property="buscarCumplimientoCalendarioVacunacionInfEnabled" value="enabled">
						<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarCumplimientoCalendarioVacunacionInf&menu=inf">Cump. Cal. Vac.</a> </li>
					</logic:equal>
					<logic:equal name="userMenu" property="buscarProyeccionVacunacionInfEnabled" value="enabled">			
						<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarProyeccionVacunacionInf&menu=inf">Proyecci&oacute;n Vac.</a> </li>
					</logic:equal>
					
					<logic:equal name="userMenu" property="buscarProductividadProfesionalInfEnabled" value="enabled">			
						<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarProductividadProfesionalInf&menu=inf">Productividad Prof.</a> </li>
					</logic:equal>					
					
					<!--Informes de Farmacia EN CONSTRUCCION -->
					<logic:equal name="userMenu" property="buscarGastoMedicamentoInfEnabled" value="enabled">			
						<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarGastoMedicamentoInf&menu=inf">Gasto en Medicamento</a> </li>
					</logic:equal>
					<logic:equal name="userMenu" property="buscarRankingPatologiaInfEnabled" value="enabled">			
						<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarRankingPatologiaInf&menu=inf">Ranking de Patolog&iacute;as</a> </li>
					</logic:equal>
					<!-- EN CONSTRUCCION -->
					<logic:equal name="userMenu" property="buscarGastoMedTratProRecInfEnabled" value="Enabled">			
						<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarGastoMedTratProRecInf&menu=inf">Gasto en Medic. (Trat / Prog / Rec)</a> </li>
					</logic:equal>
					
				</ul>
				</li>
			</logic:equal>
		
					<logic:equal name="userMenu" property="estadisticaEnabled" value="enabled" >
						<li class="encabezado"><a href="javascript:showhide('est')">Estad&iacute;stica</a></li>
							<li id="est" >
								<ul class="level2">
									<logic:equal name="userMenu" property="buscarEstadisticaAteEnabled" value="enabled">			
										<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarEstadisticaAte&menu=est">Estad&iacute;stica</a> </li>
									</logic:equal>
								</ul>
							</li>
					</logic:equal>
		
					<logic:equal name="userMenu" property="HCEnabled" value="enabled">
						<li class="encabezado"><a href="javascript:showhide('hc')">Historias Cl&iacute;nicas</a></li>
						<li id="hc" >
							<ul class="level2">
								<logic:equal name="userMenu" property="administrarHCIEnabled" value="enabled">			
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarPersona&menu=hc">Administrar Paciente</a> </li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarHCFEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarHCF&menu=hc">Administrar HCF</a></li>
								</logic:equal>
							</ul>
						</li>
					</logic:equal>

					<logic:equal name="userMenu" property="turnoEnabled" value="enabled">
						<li class="encabezado"><a href="javascript:showhide('tur')">Turnos</a></li>
						<li id="tur" >
							<ul class="level2">
								<logic:equal name="userMenu" property="administrarTurnoPersonalEnabled" value="enabled">			
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarTurnoPersonal&menu=tur">Consultar Turnos Profesional</a> </li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarTurnoPacienteEnabled" value="enabled">			
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarTurnoPaciente&menu=tur">Consultar Turnos Paciente</a> </li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarTurnoEquipoReferenciaEnabled" value="enabled">			
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarTurnoEquipoReferencia&menu=tur">Asignar Turnos ER</a> </li>
								</logic:equal>
 								<logic:equal name="userMenu" property="administrarAsignarTurnosEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarAsignarTurnos&menu=tur">Asignar Turnos</a> </li>
								</logic:equal>					
								<logic:equal name="userMenu" property="administrarExcepcionEnabled" value="enabled">			
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarExcepcion&menu=tur">Administrar Excepci&oacute;n</a> </li>
								</logic:equal>
								<logic:equal name="userMenu" property="generarAgendaEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=generarAgenda&menu=tur">Generar Agenda de Turnos</a> </li>
								</logic:equal>
							</ul>
						</li>
					</logic:equal>
		
					<logic:equal name="userMenu" property="atencionesEnabled" value="enabled">
						<li class="encabezado"><a href="javascript:showhide('ate')">Atenciones</a></li>
						<li id="ate" >
							<ul class="level2">
								<logic:equal name="userMenu" property="administrarAtencionEnabled" value="enabled">			
									<li > <a href="/aps/seg/SeleccionarOpcionMenu.do?method=administrarAtencion&menu=ate">Administrar Atenciones</a> </li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarAtencionPracticaEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarAtencionPractica&menu=ate">Registrar Resultados de Pr&aacute;cticas</a></li>
								</logic:equal>
							</ul>				
						</li>
					</logic:equal>
		
					<logic:equal name="userMenu" property="farmaciaEnabled" value="enabled">
						<li class="encabezado"><a href="javascript:showhide('frm')">Farmacia</a></li>
						<li id="frm" >
							<ul class="level2">
								<logic:equal name="userMenu" property="administrarEntregaMedicamentosEnabled" value="enabled">			
									<li > <a href="/aps/seg/SeleccionarOpcionMenu.do?method=administrarEntregaMedicamentos&menu=frm">Entrega Medicamentos a Pacientes</a> </li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarAutorizacionEnabled" value="enabled">
									<li > <a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarAutorizacion&menu=frm">Registrar Autorizaci&oacute;n de Medicamentos</a> </li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarAbandonosTratProEnabled" value="enabled">
									<li > <a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarAbandonoTratPro&menu=frm">Registrar Abandono de Tratamientos</a> </li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarPedIntEnabled" value="enabled">
									<li > <a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarPedInt&menu=frm">Pedido a CD Externo</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarRemIngIntEnabled" value="enabled">
									<li > <a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarRemIngInt&menu=frm">Remito Ingreso Interno</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarRemEgrIntEnabled" value="enabled">
									<li > <a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarRemEgrInt&menu=frm">Remito Egreso Interno</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarRegistroConsumoEnabled" value="enabled">
									<li > <a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarRegistroConsumo&menu=frm">Registro Consumo</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarPedExtEnabled" value="enabled">
									<li > <a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarPedExt&menu=frm">Pedido Externo</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarOrdenCompraEnabled" value="enabled">
									<li > <a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarOrdenCompra&menu=frm">Orden de Compra</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarRemIngExtEnabled" value="enabled">
									<li > <a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarRemIngExt&menu=frm">Remito Ingreso Externo</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarAjusteLoteInsumoEnabled" value="enabled">
									<li > <a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarAjusteLoteInsumo&menu=frm">Ajuste Lote Insumo</a></li>
								</logic:equal>
							</ul>				
						</li>
					</logic:equal>

					<logic:equal name="userMenu"	property="personalEnabled" value="enabled">
						<li class="encabezado"><a href="javascript:showhide('per')">Personal</a></li>
						<li id="per" >
							<ul class="level2">
								<logic:equal name="userMenu" property="administrarPersonalEnabled" value="enabled">			
									<li > <a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarPersonal&menu=per">Administrar Personal</a> </li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarEquipoReferenciaEnabled" value="enabled">			
									<li > <a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarEquipoReferencia&menu=per">Administrar ERs</a></li>
								</logic:equal>
							</ul>
						</li>
					</logic:equal>

					<logic:equal name="userMenu" property="seguridadEnabled" value="enabled">
						<li class="encabezado"><a href="javascript:showhide('seg')">Seguridad</a></li>
						<li id="seg" >
							<ul class="level2">
								<logic:equal name="userMenu" property="administrarUsuarioEnabled" value="enabled">					
									<li class="childs"> <a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarUsuario&menu=seg">Administrar Usuarios</a> </li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarRolAPSEnabled" value="enabled">		
									<li class="childs"> <a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarRolAPS&menu=seg">Administrar Roles</a> </li>
								</logic:equal>
							</ul>
						</li>
					</logic:equal>
	
					<logic:equal name="userMenu" property="ADMEnabled" value="enabled">
						<li class="encabezado"><a href="javascript:showhide('adm')">Administraci&oacute;n</a></li>
						<li id="adm" >
							<ul class="level2">

								<logic:equal name="userMenu" property="administrarTipoExcepcionEnabled" value="enabled">
									<li class="subchilds"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarTipoExcepcion&menu=adm">ABM Tipo Excepci&oacute;n</a></li>		
								</logic:equal>
								<logic:equal name="userMenu" property="administrarTipoDosisVacunaEnabled" value="enabled">		
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarTipoDosisVacuna&menu=adm">ABM Tipo Dosis Vacuna</a></li>				
								</logic:equal>
								<logic:equal name="userMenu" property="administrarMotivoVacunacionEnabled" value="enabled">		
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarMotivoVacunacion&menu=adm">ABM Motivo Vacunaci&oacute;n</a></li>				
								</logic:equal>
								<logic:equal name="userMenu" property="administrarResultadoEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarResultado&menu=adm">ABM Resultado</a></li>				
								</logic:equal>
								<logic:equal name="userMenu" property="administrarTipoPracticaEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarTipoPractica&menu=adm">ABM Tipo Pr&aacute;ctica</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarPracticaEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarPractica&menu=adm">ABM Pr&aacute;ctica</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarTipoDiagnosticoEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarTipoDiagnostico&menu=adm">ABM Tipo Diagn&oacute;stico</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarDiagnosticoEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarDiagnostico&menu=adm">ABM Diagn&oacute;stico</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarEstadoDiagnosticoAtencionEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarEstadoDiagnosticoAtencion&menu=adm">ABM Estado Diagn&oacute;stico Atenci&oacute;n</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarCategoriaAtencionEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarCategoriaAtencion&menu=adm">ABM Categor&iacute;a Atenci&oacute;n</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarRelacionEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarRelacion&menu=adm">ABM Relaci&oacute;n</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarTratamientoEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarTratamiento&menu=adm">ABM Tratamiento</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarEspecialidadEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarEspecialidad&menu=adm">ABM Especialidad</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarServicioEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarServicio&menu=adm">ABM Servicio</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarEfectorEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarEfector&menu=adm">ABM Efector</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarClaseEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarClase&menu=adm">ABM Clase</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarSubclaseEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarSubclase&menu=adm">ABM Subclase</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarConcentracionEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarConcentracion&menu=adm">ABM Concentracion</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarSalEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarSal&menu=adm">ABM Sal</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarFormaFarmEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarFormaFarm&menu=adm">ABM Forma Farmac&eacute;utica</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarATCNivel1Enabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarATCNivel1&menu=adm">ABM ATC</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarTipoInsumoEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarTipoInsumo&menu=adm">ABM Tipo de Insumo</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarInsumoEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarInsumo&menu=adm">ABM Insumo</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarDosisVacunaEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarDosisVacuna&menu=adm">ABM Dosis Vacuna</a></li>
								</logic:equal>																				
								<logic:equal name="userMenu" property="administrarMotivoAjusteEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarMotivoAjuste&menu=adm">ABM Motivo Ajuste</a></li>
								</logic:equal>					
								<logic:equal name="userMenu" property="administrarOrigenInsumoEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarOrigenInsumo&menu=adm">ABM Origen Insumo</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarProgramaEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarPrograma&menu=adm">ABM Programa</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarTipoTratamientoEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarTipoTratamiento&menu=adm">ABM Tipo de Tratamiento</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarProveedorEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarProveedor&menu=adm">ABM Proveedor</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarObraSocialEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarObraSocial&menu=adm">ABM Obra Social</a></li>
								</logic:equal>
								<logic:equal name="userMenu" property="administrarGrupoEtareoEnabled" value="enabled">
									<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=buscarGrupoEtareo&menu=adm">ABM Grupo Etareo</a></li>
								</logic:equal>
							</ul>
						</li>
					</logic:equal>

					<li class="encabezado"><a href="javascript:showhide('sis')">Sistema</a></li>
					<li id="sis" >
						<ul class="level2">
							<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=changePassword&menu=sis">Cambiar Password</a></li>
							<li class="childs"><a href="/aps/seg/SeleccionarOpcionMenu.do?method=logout&menu=sis">Salir</a></li>
						</ul>
					</li>
				</ul>	
			</div>
		</td>
	</tr>
</table>

<script>
showhide('<bean:write name="userMenu" property="lastSelectedOption"/>');
</script>