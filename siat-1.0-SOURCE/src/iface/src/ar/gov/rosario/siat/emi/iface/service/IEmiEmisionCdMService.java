//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.service;

import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import ar.gov.rosario.siat.emi.iface.model.ProcesoEmisionCdMAdapter;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IEmiEmisionCdMService extends IEmiEmisionServiceOld {
	
	// ---> ABM Proceso Emision CdM
	public ProcesoEmisionCdMAdapter getProcesoEmisionCdMAdapterInit(UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException;
	public EmisionVO activar(UserContext userContext, EmisionVO emisionVO) throws DemodaServiceException;
	public EmisionVO cancelar(UserContext userContext, EmisionVO emisionVO) throws DemodaServiceException;
	public EmisionVO reiniciar(UserContext userContext, EmisionVO emisionVO) throws DemodaServiceException;
	// <--- ABM Proceso Emision CdM
}