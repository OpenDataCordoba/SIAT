<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<SCRIPT>
// Registrar todos los id de menu
var coll = new Array('hci_administrar_paciente','hci_informes' , 'hci_depurar', 'turnos_informes', 'farmacia_informes');

function showhide(list) {
    for (i = 0; i < coll.length; i++) {    
        if (coll[i] != list) {
            document.getElementById(coll[i]).style.display = "none";
        } else {
        	alert()
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

<div class="menu">
	<ul class="level1">
		<li class="encabezado childs">Historias Cl&iacute;nicas</li>
		<li class="childs"> <a href="/aps/per/BuscarPersona.do?method=inicializar&menu=true">Administrar Paciente</a> </li>
		<li id="hci_administrar_paciente" class="submenu">
			<ul class="level2">
			</ul>
		</li>
		<li class="childs"><a href="/aps/hc/AdministrarHCF.do?method=inicializar">Administrar HCF</a></li>
		<li id="hci_administrar_hcf" class="submenu">
			<ul class="level2">
			</ul>
		</li>
		<li class="childs"><a href="javascript:showhide(\'hci_depurar\')">Depurar HCI</a></li>
		<li id="hci_depurar" class="submenu">
			<ul class="level2">
				<li class="subchilds"><a href="../hc/depurar_hci_xinactividad.html">Por Inactividad       </a></li>
				<li class="subchilds"><a href="../hc/depurar_hci_xduplicacion.html">Por Duplicaci&oacute;n</a></li>
			</ul>
		</li>
		<li class="childs"><a href="javascript:showhide(\'hci_informes\')">Informes</a></li>
		<li id="hci_informes" class="submenu">
			<ul class="level2">
				<li class="subchilds"><a href="../hc/informe_pacientes_no_adscriptos.html">Pacientes No Adscriptos</a></li>
				<li class="subchilds"><a href="../hc/informe_pacientes_no_asignados_hcf.html">Pacientes No Asignados a HCF</a></li>
			</ul>
		</li>
		<li class="encabezado childs">Turnos</li>
		<li class="childs"> <a href="../tur/consultar_agenda_personal.html">Consultar Agenda Personal</a> </li>
		<li class="childs"> <a href="../tur/consultar_turnos_paciente.html">Consultar Turnos Paciente</a> </li>
		<li class="childs"> <a href="../tur/asignar_turnos_equipo_referencia.html">Asignar Turnos ER</a> </li>
		<li class="childs"> <a href="../tur/alta_turnos.html">Asignar Turnos</a> </li>
		<li class="childs"> <a href="../tur/administrar_excepciones.html">Administrar Excepciones</a> </li>
		<li class="childs"> <a href="../tur/generar_agenda_turnos.html">Generar Agenda de Turnos</a> </li>
		<li class="encabezado  childs">Atenciones</li>
		<li class="childs"><a href="../ate/administrar_atenciones.html">Administrar Atenciones</a></li>
		<li class="childs"><a href="../ate/registrar_resultados_practicas.html">Registrar Resultados de Pr&aacute;cticas</a></li>
		<li class="childs"><a href="javascript:showhide(\'turnos_informes\')">Informes</a></li>
		<li id="turnos_informes" class="submenu">
			<ul class="level2">
				<li class="subchilds"><a href="../ate/informe_proyeccion_vacunacion.html">Proyecci&oacute;n de Vacunaci&oacute;n</a></li>
				<li class="subchilds"><a href="../ate/informe_cumplimiento_informe_cumplimiento_calendario_vacunacion.html">Informe de cumplimiento del calendario de Vacunaci&oacute;n</a></li>
				<li class="subchilds"><a href="../ate/informe_control_asistencia_pacientes.html">Control de Asistencia de Pacientes</a></li>
				<li class="subchilds"><a href="../ate/informe_pacientes_fuera_er.html">Pacientes que se Atienden por Fuera de su ER</a></li>
				<li class="subchilds"><a href="../ate/informe_productividad_profesional.html">Productividad de Profesionales</a></li>
			</ul>
		</li>
		<li class="encabezado  childs">Farmacia</li>
		<li class="childs"><a href="../hc/seleccionar_paciente_entrega_medicamentos.html">Entrega Medicamentos a Pacientes</a></li>
		<li class="childs"><a href="../frm/registrar_abonados_tratamiento.html">Registrar Abandonos de Tratamientos</a></li>
		<li class="childs"><a href="../frm/registrar_autorizacion_medicamentos.html">Registrar Autorizacion de Medicamentos</a></li>
		<li class="childs"><a href="javascript:showhide(\'farmacia_informes\')">Informes</a></li>
		<li id="farmacia_informes" class="submenu">
			<ul class="level2">
				<li class="subchilds"><a href="../frm/consumo_insumos.html">Consumo de Insumos</a></li>
				<li class="subchilds"><a href="../frm/cumplimiento_tratamientos.html">Cumplimiento de Tratamientos</a></li>
				<li class="subchilds"><a href="../frm/demanda_insatifecha.html">Demanda Insatisfecha</a></li>
			</ul>
		</li>
		<li class="encabezado  childs">Personal</li>
		<li class="childs"> <a href="../per/administrar_personal.html">Administrar Personal</a> </li>
		<li class="childs"> <a href="../per/mantener_equipos_referencia.html">Administrar ERs</a> </li>
		<li class="encabezado">Seguridad</li>
		<li class="childs"> <a href="/aps/seg/AdministrarUsuario.do">Administrar Usuarios</a> </li>
		<li class="childs"> <a href="../seg/administrar_roles.html">Administrar roles</a> </li>
		<li class="encabezado">Administraci&oacute;n</li>
		<li class="childs"><a href="../adm/efectores.html">Efectores</a></li>
		<li class="childs"><a href="../adm/servicios.html">Servicios</a></li>
		<li class="childs"><a href="../adm/especialidades.html">Especialidades</a></li>
		<li class="childs"><a href="../adm/diagnosticos.html">Diagnosticos</a></li>
		<li class="childs"><a href="../adm/practicas.html">Practicas</a></li>
		<li class="childs"><a href="../adm/tratamientos.html">Tratamientos</a></li>
		<li class="childs"><a href="../frm/administrar_insumos.html">Insumos</a></li>
		<li class="childs"><a href="../adm/origenes.html">Or&iacute;genes</a></li>
		<li class="encabezado">Estad&iacute;stica</li>
	</ul>
	<ul class="level1">
		<li class="misc"><a href="../seg/login.html">Salir</a></li>
	</ul>
</div>