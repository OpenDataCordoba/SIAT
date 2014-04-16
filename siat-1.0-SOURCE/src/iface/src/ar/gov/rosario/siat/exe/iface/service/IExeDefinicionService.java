//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.service;

import ar.gov.rosario.siat.exe.iface.model.ContribExeAdapter;
import ar.gov.rosario.siat.exe.iface.model.ContribExeSearchPage;
import ar.gov.rosario.siat.exe.iface.model.ContribExeVO;
import ar.gov.rosario.siat.exe.iface.model.EstadoCueExeAdapter;
import ar.gov.rosario.siat.exe.iface.model.EstadoCueExeSearchPage;
import ar.gov.rosario.siat.exe.iface.model.EstadoCueExeVO;
import ar.gov.rosario.siat.exe.iface.model.ExeRecConAdapter;
import ar.gov.rosario.siat.exe.iface.model.ExeRecConVO;
import ar.gov.rosario.siat.exe.iface.model.ExencionAdapter;
import ar.gov.rosario.siat.exe.iface.model.ExencionSearchPage;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;
import ar.gov.rosario.siat.exe.iface.model.TipSujExeAdapter;
import ar.gov.rosario.siat.exe.iface.model.TipSujExeVO;
import ar.gov.rosario.siat.exe.iface.model.TipoSujetoAdapter;
import ar.gov.rosario.siat.exe.iface.model.TipoSujetoSearchPage;
import ar.gov.rosario.siat.exe.iface.model.TipoSujetoVO;
import ar.gov.rosario.siat.pad.iface.model.BrocheVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IExeDefinicionService {
	
	//	 ---> ABM Exencion
	public ExencionSearchPage getExencionSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public ExencionSearchPage getExencionSearchPageResult(UserContext usercontext, ExencionSearchPage exencionSearchPage) throws DemodaServiceException;

	public ExencionAdapter getExencionAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ExencionAdapter getExencionAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public ExencionAdapter getExencionAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public ExencionVO createExencion(UserContext userContext, ExencionVO exencionVO ) throws DemodaServiceException;
	public ExencionVO updateExencion(UserContext userContext, ExencionVO exencionVO ) throws DemodaServiceException;
	public ExencionVO deleteExencion(UserContext userContext, ExencionVO exencionVO ) throws DemodaServiceException;
	public ExencionVO activarExencion(UserContext userContext, ExencionVO exencionVO ) throws DemodaServiceException;
	public ExencionVO desactivarExencion(UserContext userContext, ExencionVO exencionVO ) throws DemodaServiceException;	
	public ExencionAdapter imprimirExencion(UserContext userContext, ExencionAdapter exencionAdapter) throws  DemodaServiceException;

	//	 ---> ABM ExeRecCon
	public ExeRecConAdapter getExeRecConAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ExeRecConAdapter getExeRecConAdapterForCreate(UserContext userContext, ExeRecConAdapter exeRecConAdapterVO) throws DemodaServiceException;
	public ExeRecConAdapter getExeRecConAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public ExeRecConVO createExeRecCon(UserContext userContext, ExeRecConVO exeRecConVO ) throws DemodaServiceException;
	public ExeRecConVO updateExeRecCon(UserContext userContext, ExeRecConVO exeRecConVO ) throws DemodaServiceException;
	public ExeRecConVO deleteExeRecCon(UserContext userContext, ExeRecConVO exeRecConVO ) throws DemodaServiceException;
	// <--- ABM ExeRecCon
	
	// <--- ABM Exencion
	
	//	 ---> ABM ContribExe
	public ContribExeSearchPage getContribExeSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public ContribExeSearchPage getContribExeSearchPageParamContribuyente
	(UserContext userContext, ContribExeSearchPage contribExeSearchPage) throws DemodaServiceException;	
	public ContribExeSearchPage getContribExeSearchPageResult(UserContext usercontext, 
		ContribExeSearchPage contribExeSearchPage) throws DemodaServiceException;
	public ContribExeAdapter getContribExeAdapterForView(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public ContribExeAdapter getContribExeAdapterParamContribuyente
		(UserContext userContext, ContribExeAdapter contribExeAdapter) throws DemodaServiceException;	
	public ContribExeAdapter getContribExeAdapterForCreate(UserContext userContext) 
		throws DemodaServiceException;
	public ContribExeAdapter getContribExeAdapterForUpdate(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public ContribExeVO createContribExe(UserContext userContext, ContribExeVO contribExeVO ) 
		throws DemodaServiceException;
	public ContribExeVO updateContribExe(UserContext userContext, ContribExeVO contribExeVO ) 
		throws DemodaServiceException;
	public ContribExeVO deleteContribExe(UserContext userContext, ContribExeVO contribExeVO ) 
		throws DemodaServiceException;
	public ContribExeVO activarContribExe(UserContext userContext, ContribExeVO contribExeVO ) 
		throws DemodaServiceException;
	public ContribExeVO desactivarContribExe(UserContext userContext, ContribExeVO contribExeVO ) 
		throws DemodaServiceException;	

	public ContribExeVO paramAsignarBroche(UserContext userContext, ContribExeVO contribExeVO, CommonKey idBroche) throws DemodaServiceException;
	public ContribExeVO paramQuitarBroche(UserContext userContext, ContribExeVO contribExeVO) throws DemodaServiceException;
	public BrocheVO paramGetBroche(UserContext userSession, CommonKey keyBroche)  throws DemodaServiceException;

	
	// <--- ABM ContribExe
		
	// <--- ABM Tipo Sujeto
	public TipoSujetoSearchPage getTipoSujetoSearchPageInit(UserContext userContext)throws DemodaServiceException;
	public TipoSujetoSearchPage getTipoSujetoSearchPageResult(UserContext userContext, TipoSujetoSearchPage tipoSujetoSearchPage) throws DemodaServiceException; 
	
	public TipoSujetoAdapter getTipoSujetoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException; 
	public TipoSujetoAdapter getTipoSujetoAdapterForUpdate(UserContext userContext, CommonKey commonKey)throws DemodaServiceException; 
	
	public TipoSujetoAdapter getTipoSujetoAdapterForCreate(UserContext userContext)throws DemodaServiceException;
	
	public TipoSujetoVO createTipoSujeto(UserContext userContext, TipoSujetoVO tipoSujetoVO)throws DemodaServiceException;
	public TipoSujetoVO updateTipoSujeto(UserContext userContext, TipoSujetoVO tipoSujetoVO)throws DemodaServiceException;
	
	public TipoSujetoVO deleteTipoSujeto (UserContext userContext, TipoSujetoVO tipoSujetoVO)throws DemodaServiceException; 
	public TipoSujetoVO activarTipoSujeto (UserContext userContext, TipoSujetoVO TipoSujetoVO)throws DemodaServiceException; 
	public TipoSujetoVO desactivarTipoSujeto (UserContext userContext, TipoSujetoVO tipoSujetoVO)throws DemodaServiceException; 
	
	// <--- ABM Tipo Sujeto
	
	// <--- ABM TipSujExe      
	public TipSujExeAdapter getTipSujExeAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException; 
	public TipSujExeAdapter getTipSujExeAdapterForCreate(UserContext userContext, CommonKey commonKey)throws DemodaServiceException;	
	public TipSujExeVO createTipSujExe(UserContext userContext, TipSujExeVO tipSujExeVO)throws DemodaServiceException;	
	public TipSujExeVO deleteTipSujExe (UserContext userContext, TipSujExeVO tipSujExeVO)throws DemodaServiceException; 
	public TipSujExeAdapter  getTipSujExeAdapterParamRecurso(UserContext userContext, TipSujExeAdapter tipSujExeAdapter)throws DemodaServiceException; 	
	// <--- ABM TipSujExe	
	
	
	// <--- ABM Estado Cuenta/Exencion
	public EstadoCueExeSearchPage getEstadoCueExeSearchPageInit(UserContext userContext)throws DemodaServiceException;
	public EstadoCueExeSearchPage getEstadoCueExeSearchPageResult(UserContext userContext, EstadoCueExeSearchPage estadoCueExeSearchPage) throws DemodaServiceException; 
	
	public EstadoCueExeAdapter getEstadoCueExeAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException; 
	
	public EstadoCueExeAdapter getEstadoCueExeAdapterForUpdate(UserContext userContext, CommonKey commonKey)throws DemodaServiceException; 
	
	public EstadoCueExeAdapter getEstadoCueExeAdapterForCreate(UserContext userContext)throws DemodaServiceException;
	
	public EstadoCueExeVO createEstadoCueExe(UserContext userContext, EstadoCueExeVO estadoCueExeVO)throws DemodaServiceException;
	public EstadoCueExeVO updateEstadoCueExe(UserContext userContext, EstadoCueExeVO estadoCueExeVO)throws DemodaServiceException;
	
	public EstadoCueExeVO deleteEstadoCueExe (UserContext userContext, EstadoCueExeVO estadoCueExeVO)throws DemodaServiceException; 
	public EstadoCueExeVO activarEstadoCueExe (UserContext userContext, EstadoCueExeVO estadoCueExeVO)throws DemodaServiceException; 
	public EstadoCueExeVO desactivarEstadoCueExe (UserContext userContext, EstadoCueExeVO estadoCueExeVO)throws DemodaServiceException; 
	
	// <--- ABM Estado Cuenta/Exencion
}
