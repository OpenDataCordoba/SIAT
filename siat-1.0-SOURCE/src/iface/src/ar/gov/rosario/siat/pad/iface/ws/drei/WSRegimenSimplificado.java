//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.ws.drei;

import ar.gov.rosario.siat.pad.iface.model.MRCategoriaRS;

public interface WSRegimenSimplificado {

	
	public MRCategoriaRS procesarServicioRS (
			String tipoTransaccion, String usuario, Integer tipoUsuario, String cuit, String desCont, Integer tipoContribuyente, String isib, String nroCuenta, String listActividades, 
			Integer mesInicio, Integer anioInicio, Double ingBruAnu, Double supAfe, Double publicidad,  Double redHabSoc, Integer adicEtur, 
			Double precioUnitario, Integer canPer, boolean confirmado, String domicilioLocal, String telefono, String email, Integer mesBaja, Integer anioBaja, Integer motivoBaja, Long idTramite  ) throws Exception;

	
}	
