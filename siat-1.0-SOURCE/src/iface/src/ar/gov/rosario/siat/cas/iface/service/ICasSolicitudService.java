//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.service;

import ar.gov.rosario.siat.cas.iface.model.AreaSolicitudAdapter;
import ar.gov.rosario.siat.cas.iface.model.AreaSolicitudVO;
import ar.gov.rosario.siat.cas.iface.model.SolicitudAdapter;
import ar.gov.rosario.siat.cas.iface.model.SolicitudSearchPage;
import ar.gov.rosario.siat.cas.iface.model.SolicitudVO;
import ar.gov.rosario.siat.cas.iface.model.TipoSolicitudAdapter;
import ar.gov.rosario.siat.cas.iface.model.TipoSolicitudSearchPage;
import ar.gov.rosario.siat.cas.iface.model.TipoSolicitudVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;


public interface ICasSolicitudService {
	
	// ---> ABM Solicitud
	public SolicitudSearchPage getSolicitudSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public SolicitudSearchPage getSolicitudSearchPageResult(UserContext usercontext, SolicitudSearchPage solicitudSearchPage) throws DemodaServiceException;
	/** Obtiene las solicitudes pendientes del area a la que pertenece el usuario logueado */
	public SolicitudSearchPage getSolicitudSearchPagePendArea(UserContext userContext, SolicitudSearchPage solicitudSearchPage) throws DemodaServiceException;
	
	/** Obtiene las solicitudes generadas por el area a la que pertenece el usuario logueado */
	public SolicitudSearchPage getSolicitudSearchPageEmitidasArea(UserContext userContext, SolicitudSearchPage solicitudSearchPage) throws DemodaServiceException;
	public SolicitudSearchPage getSolicitudSearchPageParamCuenta(UserContext userContext, SolicitudSearchPage solicitudSearchPage) throws DemodaServiceException;
	public SolicitudAdapter getSolicitudAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public SolicitudAdapter getSolicitudAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public SolicitudAdapter getSolicitudAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public SolicitudAdapter getSolicitudAdapterParamAreaDestino(UserContext userContext, SolicitudAdapter solicitudAdapter) throws DemodaServiceException;
	
	public SolicitudVO createSolicitud(UserContext userContext, SolicitudVO solicitudVO ) throws DemodaServiceException;
	public SolicitudVO updateSolicitud(UserContext userContext, SolicitudVO solicitudVO ) throws DemodaServiceException;
	public SolicitudVO deleteSolicitud(UserContext userContext, SolicitudVO solicitudVO ) throws DemodaServiceException;
	public SolicitudVO activarSolicitud(UserContext userContext, SolicitudVO solicitudVO ) throws DemodaServiceException;
	public SolicitudVO desactivarSolicitud(UserContext userContext, SolicitudVO solicitudVO ) throws DemodaServiceException;	
	public SolicitudAdapter getSolicitudAdapterParamCuenta(UserContext userContext, SolicitudAdapter solicitudAdapterVO) throws DemodaServiceException;
//	 <--- ABM Solicitud
	
	public SolicitudAdapter getSolicitudAdapterForCambiarEstado(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public SolicitudAdapter cambiarEstado(UserContext userContext, SolicitudAdapter solicitudAdapter) throws DemodaServiceException;
	
	// ---> ABM TipoSolicitud
	public TipoSolicitudAdapter getTipoSolicitudAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public TipoSolicitudAdapter getTipoSolicitudAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public TipoSolicitudAdapter getTipoSolicitudAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public TipoSolicitudVO createTipoSolicitud(UserContext userContext,TipoSolicitudVO tipoSolicitud) throws DemodaServiceException;
	public TipoSolicitudVO updateTipoSolicitud(UserContext userContext,TipoSolicitudVO tipoSolicitud) throws DemodaServiceException;
	public TipoSolicitudVO deleteTipoSolicitud(UserContext userContext,TipoSolicitudVO tipoSolicitud) throws DemodaServiceException;
	public TipoSolicitudVO activarTipoSolicitud(UserContext userContext,TipoSolicitudVO tipoSolicitud) throws DemodaServiceException;
	public TipoSolicitudVO desactivarTipoSolicitud(UserContext userContext,TipoSolicitudVO tipoSolicitud) throws DemodaServiceException;
	public TipoSolicitudAdapter getTipoSolicitudAdapterParam(UserContext userContext, TipoSolicitudAdapter tipoSolicitudAdapterVO) throws DemodaServiceException;
	public TipoSolicitudAdapter imprimirTipoSolicitud(UserContext userContext,TipoSolicitudAdapter tipoSolicitudAdapterVO) throws DemodaServiceException;	
	
	public TipoSolicitudSearchPage getTipoSolicitudSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public TipoSolicitudSearchPage getTipoSolicitudSearchPageResult(UserContext userContext, TipoSolicitudSearchPage tipoSolicitudSearchPage) throws DemodaServiceException;
	//<--- ABM TipoSolicitud
	
	// ---> ABM AreaSolicitud
	public AreaSolicitudAdapter getAreaSolicitudAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public AreaSolicitudAdapter getAreaSolicitudAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public AreaSolicitudAdapter getAreaSolicitudAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public AreaSolicitudVO createAreaSolicitud(UserContext userContext,	AreaSolicitudVO areaSolicitud) throws DemodaServiceException;
	public AreaSolicitudVO updateAreaSolicitud(UserContext userContext,	AreaSolicitudVO areaSolicitud) throws DemodaServiceException;
	public AreaSolicitudVO deleteAreaSolicitud(UserContext userContext,	AreaSolicitudVO areaSolicitud) throws DemodaServiceException;
	public AreaSolicitudVO activarAreaSolicitud(UserContext userContext, AreaSolicitudVO areaSolicitud) throws DemodaServiceException;
	public AreaSolicitudVO desactivarAreaSolicitud(UserContext userContext,	AreaSolicitudVO areaSolicitud) throws DemodaServiceException;
	public AreaSolicitudAdapter getAreaSolicitudAdapterParam(UserContext userContext, AreaSolicitudAdapter areaSolicitudAdapterVO) throws DemodaServiceException;
	public AreaSolicitudAdapter imprimirAreaSolicitud(UserContext userContext, AreaSolicitudAdapter areaSolicitudAdapterVO) throws DemodaServiceException;		
	//<--- ABM AreaSolicitud
}