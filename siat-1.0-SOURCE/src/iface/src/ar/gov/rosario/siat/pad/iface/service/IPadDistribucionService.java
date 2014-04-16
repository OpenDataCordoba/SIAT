//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.service;

import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.iface.model.BroCueAdapter;
import ar.gov.rosario.siat.pad.iface.model.BroCueSearchPage;
import ar.gov.rosario.siat.pad.iface.model.BroCueVO;
import ar.gov.rosario.siat.pad.iface.model.BrocheAdapter;
import ar.gov.rosario.siat.pad.iface.model.BrocheSearchPage;
import ar.gov.rosario.siat.pad.iface.model.BrocheVO;
import ar.gov.rosario.siat.pad.iface.model.CriRepCalleAdapter;
import ar.gov.rosario.siat.pad.iface.model.CriRepCalleVO;
import ar.gov.rosario.siat.pad.iface.model.CriRepCatAdapter;
import ar.gov.rosario.siat.pad.iface.model.CriRepCatVO;
import ar.gov.rosario.siat.pad.iface.model.RepartidorAdapter;
import ar.gov.rosario.siat.pad.iface.model.RepartidorSearchPage;
import ar.gov.rosario.siat.pad.iface.model.RepartidorVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;


public interface IPadDistribucionService {

	// ---> ABM Repartidor
	public RepartidorSearchPage getRepartidorSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public RepartidorSearchPage getRepartidorSearchPageResult(UserContext userContext, RepartidorSearchPage repartidorSearchPage) throws DemodaServiceException;
	public RepartidorSearchPage getRepartidorSearchPageParamRecurso(UserContext userContext, RepartidorSearchPage repartidorSearchPage) throws DemodaServiceException;
	
	public RepartidorAdapter getRepartidorAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public RepartidorAdapter getRepartidorAdapterForCreate(UserContext userContext) throws DemodaServiceException;	
	public RepartidorAdapter getRepartidorAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public RepartidorAdapter getRepartidorAdapterParamRecurso(UserContext userContext, RepartidorAdapter repartidorAdapter) throws DemodaServiceException;
	public RepartidorAdapter paramPersonaRepartidor(UserContext userContext, RepartidorAdapter repartidorAdapter, Long selectedId) throws DemodaServiceException;
	public RepartidorAdapter getRepartidorAdapterParamTipoRepartidor(UserContext userContext, RepartidorAdapter repartidorAdapter) throws DemodaServiceException; 

	public RepartidorVO createRepartidor(UserContext userContext, RepartidorVO repartidorVO) throws DemodaServiceException;
	public RepartidorVO updateRepartidor(UserContext userContext, RepartidorVO repartidorVO) throws DemodaServiceException;
	public RepartidorVO deleteRepartidor(UserContext userContext, RepartidorVO repartidorVO) throws DemodaServiceException;
	public RepartidorVO activarRepartidor(UserContext userContext, RepartidorVO repartidorVO) throws DemodaServiceException;
	public RepartidorVO desactivarRepartidor(UserContext userContext, RepartidorVO repartidorVO) throws DemodaServiceException;
	// <--- ABM Repartidor

	// ---> ABM CriRepCat
	public CriRepCatAdapter getCriRepCatAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public CriRepCatAdapter getCriRepCatAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public CriRepCatAdapter getCriRepCatAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public CriRepCatVO createCriRepCat(UserContext userContext, CriRepCatVO criRepCatVO) throws DemodaServiceException;
	public CriRepCatVO updateCriRepCat(UserContext userContext, CriRepCatVO criRepCatVO) throws DemodaServiceException;
	public CriRepCatVO deleteCriRepCat(UserContext userContext, CriRepCatVO criRepCatVO) throws DemodaServiceException;
	// <--- ABM CriRepCat

	// ---> ABM CriRepCalle
	public CriRepCalleAdapter getCriRepCalleAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public CriRepCalleAdapter getCriRepCalleAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public CriRepCalleAdapter getCriRepCalleAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public CriRepCalleAdapter paramCalleCriRepCalle(UserContext userContext, CriRepCalleAdapter criRepCalleAdapter, Long selectedId) throws DemodaServiceException;
	
	public CriRepCalleVO createCriRepCalle(UserContext userContext, CriRepCalleVO criRepCalleVO) throws DemodaServiceException;
	public CriRepCalleVO updateCriRepCalle(UserContext userContext, CriRepCalleVO criRepCalleVO) throws DemodaServiceException;
	public CriRepCalleVO deleteCriRepCalle(UserContext userContext, CriRepCalleVO criRepCalleVO) throws DemodaServiceException;
	// <--- ABM CriRepCalle
	
	// ---> ABM Broche
	public BrocheSearchPage getBrocheSearchPageInit(UserContext userContext, RecursoVO recursoReadOnly) throws DemodaServiceException;
	public BrocheSearchPage getBrocheSearchPageResult(UserContext userContext, BrocheSearchPage brocheSearchPage) throws DemodaServiceException;

	public BrocheAdapter getBrocheAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public BrocheAdapter getBrocheAdapterForCreate(UserContext userContext) throws DemodaServiceException;	
	public BrocheAdapter getBrocheAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public BrocheAdapter getBrocheAdapterParamTipoBroche(UserContext userContext, BrocheAdapter brocheAdapter) throws DemodaServiceException;	
	
	public BrocheVO createBroche(UserContext userContext, BrocheVO brocheVO) throws DemodaServiceException;
	public BrocheVO updateBroche(UserContext userContext, BrocheVO brocheVO) throws DemodaServiceException;
	public BrocheVO deleteBroche(UserContext userContext, BrocheVO brocheVO) throws DemodaServiceException;
	public BrocheVO activarBroche(UserContext userContext, BrocheVO brocheVO) throws DemodaServiceException;
	public BrocheVO desactivarBroche(UserContext userContext, BrocheVO brocheVO) throws DemodaServiceException;
	// <--- ABM Broche
	
	// ---> ABM BroCue
	public BroCueSearchPage getBroCueSearchPageInit(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public BroCueSearchPage getBroCueSearchPageResult(UserContext userContext, BroCueSearchPage broCueSearchPage) throws DemodaServiceException;
	
	public BroCueAdapter getBroCueAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public BroCueAdapter getBroCueAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public BroCueAdapter paramCuentaBroCue(UserContext userContext, BroCueAdapter broCueAdapter, Long selectedId) throws DemodaServiceException;
	
	public BroCueVO createBroCue(UserContext userContext, BroCueVO broCueVO) throws DemodaServiceException;
	public BroCueVO updateBroCue(UserContext userContext, BroCueVO broCueVO) throws DemodaServiceException;
	public BroCueVO deleteBroCue(UserContext userContext, BroCueVO broCueVO) throws DemodaServiceException;
	// <--- ABM BroCue
}
