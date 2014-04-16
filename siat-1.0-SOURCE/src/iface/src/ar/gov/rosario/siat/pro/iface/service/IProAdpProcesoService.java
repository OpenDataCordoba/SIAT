//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.iface.service;

import ar.gov.rosario.siat.pro.iface.model.AdpCorridaAdapter;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import ar.gov.rosario.siat.pro.iface.model.EnvioArchivosAdapter;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IProAdpProcesoService {

	// ---> ABM_CORRIDA
	public AdpCorridaAdapter getAdpCorridaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;

	public CorridaVO activarProceso(UserContext userContext, CorridaVO corridaVO) throws DemodaServiceException;
	public CorridaVO reprogramarProceso(UserContext userContext, CorridaVO corridaVO) throws DemodaServiceException;
	public CorridaVO cancelarProceso(UserContext userContext, CorridaVO corridaVO) throws DemodaServiceException;
	public CorridaVO reiniciarProceso(UserContext userContext, CorridaVO corridaVO) throws DemodaServiceException;
	public CorridaVO siguientePaso(UserContext userContext, CorridaVO corridaVO) throws DemodaServiceException;

	public String obtenerFileCorridaName(Long idFileCorrida) throws DemodaServiceException;
	// <--- ABM_CORRIDA

	public AdpCorridaAdapter getEstadoPaso(UserContext userSession, CommonKey id, Integer paso) throws DemodaServiceException;
	public String getLogFile(UserContext userSession, CommonKey id) throws DemodaServiceException;

	// ---> Envio de Archivos 
	public EnvioArchivosAdapter getEnvioArchivosAdapterForCreate(UserContext userSession, Long idPasoCorrida) 
		throws DemodaServiceException;
	public EnvioArchivosAdapter getEnvioArchivosAdapterForPreview(UserContext userSession, EnvioArchivosAdapter envioArchivosAdapterVO) 
		throws DemodaServiceException;
	public EnvioArchivosAdapter enviarArchivos(UserContext userSession, EnvioArchivosAdapter envioArchivosAdapterVO) 
		throws DemodaServiceException;
	// <--- Envio de Archivos
}
