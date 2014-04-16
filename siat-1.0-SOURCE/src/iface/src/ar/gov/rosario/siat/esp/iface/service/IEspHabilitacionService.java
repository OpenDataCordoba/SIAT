//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.service;

import ar.gov.rosario.siat.esp.iface.model.EntHabAdapter;
import ar.gov.rosario.siat.esp.iface.model.EntHabVO;
import ar.gov.rosario.siat.esp.iface.model.EntVenAdapter;
import ar.gov.rosario.siat.esp.iface.model.EntVenVO;
import ar.gov.rosario.siat.esp.iface.model.HabExeAdapter;
import ar.gov.rosario.siat.esp.iface.model.HabExeVO;
import ar.gov.rosario.siat.esp.iface.model.HabilitacionAdapter;
import ar.gov.rosario.siat.esp.iface.model.HabilitacionSearchPage;
import ar.gov.rosario.siat.esp.iface.model.HabilitacionVO;
import ar.gov.rosario.siat.esp.iface.model.LugarEventoAdapter;
import ar.gov.rosario.siat.esp.iface.model.LugarEventoSearchPage;
import ar.gov.rosario.siat.esp.iface.model.LugarEventoVO;
import ar.gov.rosario.siat.esp.iface.model.PrecioEventoAdapter;
import ar.gov.rosario.siat.esp.iface.model.PrecioEventoVO;
import ar.gov.rosario.siat.esp.iface.model.TipoEntradaAdapter;
import ar.gov.rosario.siat.esp.iface.model.TipoEntradaSearchPage;
import ar.gov.rosario.siat.esp.iface.model.TipoEntradaVO;
import ar.gov.rosario.siat.esp.iface.model.ValoresCargadosAdapter;
import ar.gov.rosario.siat.esp.iface.model.ValoresCargadosSearchPage;
import ar.gov.rosario.siat.esp.iface.model.ValoresCargadosVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.UserContext;

public interface IEspHabilitacionService {
	
	// ---> ABM Habilitacion
	public HabilitacionSearchPage getHabilitacionSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public HabilitacionSearchPage getHabilitacionSearchPageResult(UserContext userContext, HabilitacionSearchPage habilitacionSearchPage) throws DemodaServiceException;
	public HabilitacionSearchPage getHabilitacionSearchPageParamCuenta(UserContext userContext, HabilitacionSearchPage habilitacionSearchPage) throws DemodaServiceException;
	public HabilitacionSearchPage paramPersona(UserContext userContext, HabilitacionSearchPage habilitacionSearchPage, Long selectedId) throws DemodaServiceException;
	
	public HabilitacionAdapter getHabilitacionAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public HabilitacionAdapter getHabilitacionAdapterForCreate(UserContext userContext) throws DemodaServiceException;	
	public HabilitacionAdapter getHabilitacionAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public HabilitacionAdapter getHabilitacionAdapterParamTipoHab(UserContext userContext, HabilitacionAdapter habilitacionAdapter) throws DemodaServiceException;
	public HabilitacionAdapter getHabilitacionAdapterParamCuenta(UserContext userContext, HabilitacionAdapter habilitacionAdapter) throws DemodaServiceException;
	public HabilitacionAdapter paramPersonaForAdapter(UserContext userContext, HabilitacionAdapter habilitacionAdapter, Long selectedId) throws DemodaServiceException;
	public HabilitacionAdapter getHabilitacionAdapterParamLugarEvento(UserContext userContext, HabilitacionAdapter habilitacionAdapter) throws DemodaServiceException;
	public HabilitacionAdapter getHabilitacionAdapterForDDJJ(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;

	public HabilitacionAdapter verificarHabilitacion(UserContext userContext, HabilitacionAdapter habilitacionAdapter) throws DemodaServiceException;
	public HabilitacionAdapter emisionInicial(UserContext userContext, HabilitacionAdapter habilitacionAdapter) throws DemodaServiceException;
	public HabilitacionAdapter seleccionarHabilitacion(UserContext userContext, HabilitacionAdapter habilitacionAdapter) throws DemodaServiceException;
	public HabilitacionAdapter cambiarEstadoHabilitacion(UserContext userContext, HabilitacionAdapter habilitacionAdapter) throws DemodaServiceException;
	public HabilitacionVO createHabilitacion(UserContext userContext, HabilitacionVO habilitacionVO) throws DemodaServiceException;
	public HabilitacionVO updateHabilitacion(UserContext userContext, HabilitacionVO habilitacionVO) throws DemodaServiceException;
	public HabilitacionVO deleteHabilitacion(UserContext userContext, HabilitacionVO habilitacionVO) throws DemodaServiceException;
	
	public PrintModel imprimirHabilitacion(UserContext userContext,  HabilitacionAdapter habilitacionAdapterVO ) throws DemodaServiceException;
	public PrintModel imprimirEntHabSinEntVen(UserContext userContext, HabilitacionAdapter habilitacionAdapter)throws DemodaServiceException;
	public PrintModel imprimirHabSinEntVen(UserContext userContext, HabilitacionSearchPage habilitacionSearchPage)throws DemodaServiceException;
	
	public HabilitacionSearchPage getHabilitacionReportInit(UserContext userContext) throws DemodaServiceException;
	public PrintModel generarReporteHabilitacion(UserContext userContext, HabilitacionSearchPage habilitacionSearchPage)throws DemodaServiceException;
	// <--- ABM Habilitacion

	// ---> ABM PrecioEvento
	public PrecioEventoAdapter getPrecioEventoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PrecioEventoAdapter getPrecioEventoAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public PrecioEventoAdapter getPrecioEventoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public PrecioEventoVO createPrecioEvento(UserContext userContext, PrecioEventoVO precioEventoVO) throws DemodaServiceException;
	public PrecioEventoVO updatePrecioEvento(UserContext userContext, PrecioEventoVO precioEventoVO) throws DemodaServiceException;
	public PrecioEventoVO deletePrecioEvento(UserContext userContext, PrecioEventoVO precioEventoVO) throws DemodaServiceException;
	// <--- ABM PrecioEvento

	// ---> ABM EntHab
	public EntHabAdapter getEntHabAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public EntHabAdapter getEntHabAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public EntHabAdapter getEntHabAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public EntHabVO createEntHab(UserContext userContext, EntHabVO entHabVO) throws DemodaServiceException;
	public EntHabVO updateEntHab(UserContext userContext, EntHabVO entHabVO) throws DemodaServiceException;
	public EntHabVO deleteEntHab(UserContext userContext, EntHabVO entHabVO) throws DemodaServiceException;
	// <--- ABM EntHab

	// ---> ABM EntVen
	public EntVenAdapter getEntVenAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public EntVenAdapter getEntVenAdapterForCreate(UserContext userContext, CommonKey commonKey,String origen) throws DemodaServiceException;	
	public EntVenAdapter getEntVenAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public EntVenAdapter calcular(UserContext userContext, EntVenAdapter entVenAdapter) throws DemodaServiceException;
	public EntVenAdapter anular(UserContext userContext, EntVenAdapter entVenAdapter) throws DemodaServiceException;
	public EntVenAdapter getEntVenAdapter(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public EntVenAdapter createEntVen(UserContext userContext, EntVenAdapter entVenAdapterVO) throws DemodaServiceException;
	public EntVenVO updateEntVen(UserContext userContext, EntVenVO entVenVO) throws DemodaServiceException;
	public EntVenVO deleteEntVen(UserContext userContext, EntVenVO entVenVO) throws DemodaServiceException;
	
	public EntVenAdapter calcularOIT(UserContext userContext, EntVenAdapter entVenAdapterVO) throws DemodaServiceException;
	public EntVenAdapter paramArea(UserContext userContext, EntVenAdapter entVenAdapter) throws DemodaServiceException;
	public EntVenAdapter createEntVenOIT(UserContext userContext, EntVenAdapter entVenAdapterVO)throws DemodaServiceException;
	// <--- ABM EntVen

	// ---> ABM HabExe
	public HabExeAdapter getHabExeAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public HabExeAdapter getHabExeAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public HabExeAdapter getHabExeAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public HabExeVO createHabExe(UserContext userContext, HabExeVO habExeVO) throws DemodaServiceException;
	public HabExeVO updateHabExe(UserContext userContext, HabExeVO habExeVO) throws DemodaServiceException;
	public HabExeVO deleteHabExe(UserContext userContext, HabExeVO habExeVO) throws DemodaServiceException;
	// <--- ABM HabExe

	// ---> ABM ValoresCargados
	public ValoresCargadosSearchPage getValoresCargadosSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public ValoresCargadosSearchPage getValoresCargadosSearchPageResult(UserContext usercontext, ValoresCargadosSearchPage valoresCargadosSearchPage) throws DemodaServiceException;
	
	public ValoresCargadosAdapter getValoresCargadosAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ValoresCargadosAdapter getValoresCargadosAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public ValoresCargadosAdapter getValoresCargadosAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public ValoresCargadosVO createValoresCargados(UserContext userContext, ValoresCargadosVO valoresCargadosVO ) throws DemodaServiceException;
	public ValoresCargadosVO updateValoresCargados(UserContext userContext, ValoresCargadosVO valoresCargadosVO ) throws DemodaServiceException;
	public ValoresCargadosVO deleteValoresCargados(UserContext userContext, ValoresCargadosVO valoresCargadosVO ) throws DemodaServiceException;
	
	public ValoresCargadosAdapter imprimirValoresCargados(UserContext userContext, ValoresCargadosAdapter valoresCargadosVO ) throws DemodaServiceException;	
	// <--- ABM ValoresCargados
	
	// ---> ABM TipoEntrada
	public TipoEntradaSearchPage getTipoEntradaSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public TipoEntradaSearchPage getTipoEntradaSearchPageResult(UserContext usercontext, TipoEntradaSearchPage tipoEntradaSearchPage) throws DemodaServiceException;
	
	public TipoEntradaAdapter getTipoEntradaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public TipoEntradaAdapter getTipoEntradaAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public TipoEntradaAdapter getTipoEntradaAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public TipoEntradaVO createTipoEntrada(UserContext userContext, TipoEntradaVO tipoEntradaVO ) throws DemodaServiceException;
	public TipoEntradaVO updateTipoEntrada(UserContext userContext, TipoEntradaVO tipoEntradaVO ) throws DemodaServiceException;
	public TipoEntradaVO deleteTipoEntrada(UserContext userContext, TipoEntradaVO tipoEntradaVO ) throws DemodaServiceException;
	
	public TipoEntradaAdapter imprimirTipoEntrada(UserContext userContext, TipoEntradaAdapter tipoEntradaVO ) throws DemodaServiceException;
	// <--- TipoEntrada

	// ---> ABM LugarEvento
	public LugarEventoSearchPage getLugarEventoSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public LugarEventoSearchPage getLugarEventoSearchPageResult(UserContext usercontext, LugarEventoSearchPage lugarEventoSearchPage) throws DemodaServiceException;
	
	public LugarEventoAdapter getLugarEventoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public LugarEventoAdapter getLugarEventoAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public LugarEventoAdapter getLugarEventoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public LugarEventoVO createLugarEvento(UserContext userContext, LugarEventoVO lugarEventoVO ) throws DemodaServiceException;
	public LugarEventoVO updateLugarEvento(UserContext userContext, LugarEventoVO lugarEventoVO ) throws DemodaServiceException;
	public LugarEventoVO deleteLugarEvento(UserContext userContext, LugarEventoVO lugarEventoVO ) throws DemodaServiceException;
	
	public LugarEventoAdapter imprimirLugarEvento(UserContext userContext, LugarEventoAdapter lugarEventoVO ) throws DemodaServiceException;
	// <--- LugarEvento
}
