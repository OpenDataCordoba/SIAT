//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.service;


import ar.gov.rosario.siat.gde.iface.model.NovedadRSAdapter;
import ar.gov.rosario.siat.gde.iface.model.NovedadRSSearchPage;
import ar.gov.rosario.siat.rec.iface.model.CatRSDreiAdapter;
import ar.gov.rosario.siat.rec.iface.model.CatRSDreiSearchPage;
import ar.gov.rosario.siat.rec.iface.model.CatRSDreiVO;
import ar.gov.rosario.siat.rec.iface.model.NovedadRSVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IRecDreiService {
	
	public NovedadRSSearchPage getNovedadRSSearchPageInit(UserContext userContext, NovedadRSSearchPage novedadRSSearchPageFiltro) throws DemodaServiceException;
	
	public NovedadRSSearchPage getNovedadRSSearchPageResult(UserContext userContext, NovedadRSSearchPage novedadRSSearchPage)throws DemodaServiceException;
	
	public NovedadRSAdapter getNovedadRSAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
		
	public NovedadRSAdapter getNovedadRSAdapterForSimular(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public NovedadRSAdapter getNovedadRSAdapterForMasivo(UserContext userContext) throws DemodaServiceException;
	
	public NovedadRSVO aplicarNovedadRS(UserContext userContext, NovedadRSVO novedadRSVO) throws DemodaServiceException;
	
	public NovedadRSAdapter aplicarMasivoNovedadRS(UserContext userContext, NovedadRSAdapter novedadRSAdapter) throws DemodaServiceException;
	
	public CatRSDreiAdapter getCatRSDreiAdapterForView(UserContext userContext, Long selectedId)throws DemodaServiceException;
	
	public CatRSDreiAdapter getCatRSDreiAdapterForUpdate(UserContext userContext, Long selectedId)throws DemodaServiceException;	
	
	public CatRSDreiAdapter getCatRSDreiAdapterForCreate(UserContext userContext)throws DemodaServiceException;	
	
	public CatRSDreiVO updateCatRSDrei(UserContext userContext, CatRSDreiVO catRSDreiVO) throws DemodaServiceException;
	
	public CatRSDreiVO deleteCatRSDrei(UserContext userContext, CatRSDreiVO catRSDreiVO) throws DemodaServiceException;
	
	public CatRSDreiVO createCatRSDrei (UserContext userContext, CatRSDreiVO catRSDreiVO)throws DemodaServiceException;
		
	public CatRSDreiSearchPage getCatRSDreiSearchPageInit (UserContext userContext) throws DemodaServiceException;
	
	public CatRSDreiSearchPage getCatRSDreiSearchPageResult (UserContext userContext, CatRSDreiSearchPage catRSDreiSearchPage) throws DemodaServiceException;

	public CatRSDreiVO activarCatRSDrei(UserContext userContext,CatRSDreiVO catRSDrei) throws DemodaServiceException;

	public CatRSDreiVO desactivarCatRSDrei(UserContext userContext,	CatRSDreiVO catRSDrei) throws DemodaServiceException;
	
	public CatRSDreiAdapter imprimirCatRSDrei(UserContext userContext, CatRSDreiAdapter catRSDreiAdapterVO ) throws DemodaServiceException;
	
}
