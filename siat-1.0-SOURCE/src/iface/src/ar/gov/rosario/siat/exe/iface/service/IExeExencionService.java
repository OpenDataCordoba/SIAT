//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.service;

import ar.gov.rosario.siat.exe.iface.model.ConvivienteVO;
import ar.gov.rosario.siat.exe.iface.model.CueExeAdapter;
import ar.gov.rosario.siat.exe.iface.model.CueExeConvivAdapter;
import ar.gov.rosario.siat.exe.iface.model.CueExeSearchPage;
import ar.gov.rosario.siat.exe.iface.model.CueExeVO;
import ar.gov.rosario.siat.exe.iface.model.HisEstCueExeAdapter;
import ar.gov.rosario.siat.exe.iface.model.HisEstCueExeVO;
import ar.gov.rosario.siat.exe.iface.model.MarcaCueExeSearchPage;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.UserContext;

public interface IExeExencionService {
	
	// ---> ABM CueExe
	public CueExeSearchPage getCueExeSearchPageInit(UserContext usercontext, CueExeSearchPage cueExeSearchPage) throws DemodaServiceException;
	public CueExeSearchPage getCueExeSearchPageResult(UserContext usercontext, CueExeSearchPage cueExeSearchPage) throws DemodaServiceException;
	public CueExeSearchPage getCueExeSearchPageParamCuenta(UserContext usercontext, CueExeSearchPage cueExeSearchPage) throws DemodaServiceException;
	public CueExeSearchPage getCueExeSearchPageParamRecurso(UserContext usercontext, CueExeSearchPage cueExeSearchPage) throws DemodaServiceException;
	
	public CueExeAdapter getCueExeAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	//public CueExeAdapter getCueExeAdapterForCreate(UserContext userContext, CommonKey ckIdExePreset) throws DemodaServiceException;
	public CueExeAdapter getCueExeAdapterForCreate(UserContext userContext, boolean permiteManPad) throws DemodaServiceException;
	public CueExeAdapter getCueExeAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public CueExeAdapter getCueExeAdapterParamCuenta(UserContext userContext, CueExeAdapter cueExeAdapter) throws DemodaServiceException;
	public CueExeAdapter getCueExeAdapterParamRecurso(UserContext userContext, CueExeAdapter cueExeAdapter) throws DemodaServiceException;	
	public CueExeAdapter getCueExeAdapterParamExencion(UserContext userContext, CueExeAdapter cueExeAdapter) throws DemodaServiceException;
	public CueExeAdapter getCueExeAdapterParamSolicitante(UserContext userContext, CueExeAdapter cueExeAdapter) throws DemodaServiceException;
	
	public CueExeAdapter getCueExeAdapterForCambioEstado(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public CueExeAdapter getCueExeAdapterForAgregarSolicitud(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public CueExeAdapter getCueExeAdapterForModificarHisEstCueExe(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	
	public CueExeVO createCueExe(UserContext userContext, CueExeVO cueExeVO ) throws DemodaServiceException;
	public CueExeVO updateCueExe(UserContext userContext, CueExeVO cueExeVO ) throws DemodaServiceException;
	public CueExeVO deleteCueExe(UserContext userContext, CueExeVO cueExeVO ) throws DemodaServiceException;
	public CueExeAdapter imprimirCueExe(UserContext userContext, CueExeAdapter cueExeAdapterVO) throws DemodaServiceException;
	
	public CueExeVO agregarSolicitudCueExe(UserContext userContext, CueExeVO cueExeVO ) throws DemodaServiceException;
	public CueExeAdapter cambiarEstadoCueExe(UserContext userContext, CueExeAdapter cueExeAdapterVO ) throws DemodaServiceException;
	public CueExeAdapter buscarDeudaAsocida(UserContext userContext, CueExeAdapter cueExeAdapter) throws DemodaServiceException;
	
	public HisEstCueExeAdapter getHisEstCueExeAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public HisEstCueExeVO deleteHisEstCueExe(UserContext userContext, HisEstCueExeVO hisEstCueExeVO) throws DemodaServiceException;

	public PrintModel getImprimirHistorico(UserContext userContext, CommonKey cuentaKey) throws DemodaServiceException, Exception;
	// <--- ABM CueExe

	
	// ---> ABM SolCueExeConviv
	public CueExeConvivAdapter getCueExeConvivAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public CueExeConvivAdapter getCueExeConvivAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ConvivienteVO updateCueExeConviv(UserContext userContext, ConvivienteVO convivienteVO) throws DemodaServiceException;
	public ConvivienteVO deleteCueExeConviv(UserContext userContext, CueExeConvivAdapter cueExeConvivAdapter) throws DemodaServiceException;
	public ConvivienteVO getCueExeConvivAdapterForDelete(UserContext userContext, CommonKey idConviviente)throws DemodaServiceException;
	public CueExeConvivAdapter getCueExeConvivAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public CueExeConvivAdapter createCueExeConviv(UserContext userContext, CueExeConvivAdapter cueExeConvivAdapter) throws DemodaServiceException;
	// <--- ABM SolCueExeConviv
	
	// ---> ADM Marcas de Exenciones
	public MarcaCueExeSearchPage getMarcaCueExeSearchPageInit(UserContext userContext, MarcaCueExeSearchPage marcaCueExeSearchPage) throws DemodaServiceException;
	public MarcaCueExeSearchPage getMarcaCueExeSearchPageParamRecurso(UserContext userContext, MarcaCueExeSearchPage marcaCueExeSearchPage) throws DemodaServiceException;
	public MarcaCueExeSearchPage getMarcaCueExeSearchPageResult(UserContext userContext, MarcaCueExeSearchPage marcaCueExeSearchPage) throws DemodaServiceException;
	public MarcaCueExeSearchPage getMarcaCueExeSearchPageParamCuenta(UserContext userContext, MarcaCueExeSearchPage marcaCueExeSearchPage) throws DemodaServiceException;
	public MarcaCueExeSearchPage getMarcarCueExe(UserContext userContext, MarcaCueExeSearchPage marcaCueExeSearchPage) throws DemodaServiceException;
}
