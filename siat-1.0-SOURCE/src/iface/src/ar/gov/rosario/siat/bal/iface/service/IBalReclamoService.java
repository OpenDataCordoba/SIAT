//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.service;

import ar.gov.rosario.siat.bal.iface.model.ReclamoAdapter;
import ar.gov.rosario.siat.bal.iface.model.ReclamoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.ReclamoVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IBalReclamoService {
	
	// ---> Administrar Deuda Reclamada (SINC)
	public LiqDeudaAdapter marcarReclamada(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException;
	public LiqDeudaAdapter marcarNOReclamada(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException;
	
	// <--- Administrar Deuda Reclamada (SINC)
	
	// ---> ABM Reclamo
	
	public ReclamoSearchPage getReclamoSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public ReclamoSearchPage getReclamoSearchPageResult(UserContext usercontext, ReclamoSearchPage reclamoSearchPage) throws DemodaServiceException;
	public ReclamoAdapter getReclamoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ReclamoAdapter getReclamoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ReclamoAdapter getParamEstadoReclamo(UserContext userSession, ReclamoAdapter reclamoAdapterVO) throws DemodaServiceException;
	
	public ReclamoVO updateReclamo(UserContext userContext, ReclamoVO reclamoVO) throws DemodaServiceException;
	public ReclamoAdapter imprimirReclamo(UserContext userContext, ReclamoAdapter reclamoAdapterVO) throws DemodaServiceException;

	public ReclamoAdapter buscarMasDatosDelReclamo(UserContext userSession, ReclamoAdapter reclamoAdapterVO) throws DemodaServiceException;
	// <--- ABM Reclamo
}
