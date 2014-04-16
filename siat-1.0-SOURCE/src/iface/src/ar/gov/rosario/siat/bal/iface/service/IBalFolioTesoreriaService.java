//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.service;

import ar.gov.rosario.siat.bal.iface.model.FolComAdapter;
import ar.gov.rosario.siat.bal.iface.model.FolComVO;
import ar.gov.rosario.siat.bal.iface.model.FolDiaCobAdapter;
import ar.gov.rosario.siat.bal.iface.model.FolDiaCobSearchPage;
import ar.gov.rosario.siat.bal.iface.model.FolDiaCobVO;
import ar.gov.rosario.siat.bal.iface.model.FolioAdapter;
import ar.gov.rosario.siat.bal.iface.model.FolioSearchPage;
import ar.gov.rosario.siat.bal.iface.model.FolioVO;
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesSearchPage;
import ar.gov.rosario.siat.bal.iface.model.TipoCobAdapter;
import ar.gov.rosario.siat.bal.iface.model.TipoCobSearchPage;
import ar.gov.rosario.siat.bal.iface.model.TipoCobVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IBalFolioTesoreriaService {
	
	// ---> ABM Folio
	public FolioSearchPage getFolioSearchPageInit(UserContext userContext, FolioSearchPage folioSPFiltro) throws DemodaServiceException;
	public FolioSearchPage getFolioSearchPageResult(UserContext userContext, FolioSearchPage folioSearchPage) throws DemodaServiceException;
	
	public FolioAdapter getFolioAdapterForView(UserContext userContext, CommonKey commonKey, FolioAdapter folioAdapter) throws DemodaServiceException;
	public FolioAdapter getFolioAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public FolioAdapter getFolioAdapterForCreate(UserContext userContext) throws DemodaServiceException;	
	public FolioAdapter excluirOtrIngTes(UserContext userContext, FolioAdapter folioAdapter) throws DemodaServiceException;
	public OtrIngTesSearchPage incluirOtrIngTes(UserContext userContext, OtrIngTesSearchPage otrIngTesSearchPage) throws DemodaServiceException;
	
	public FolioVO enviarFolio(UserContext userContext, FolioVO folioVO) throws DemodaServiceException;
	public FolioVO devolverFolio(UserContext userContext, FolioVO folioVO) throws DemodaServiceException;
	
	public FolioVO createFolio(UserContext userContext, FolioVO folioVO) throws DemodaServiceException;
	public FolioVO updateFolio(UserContext userContext, FolioVO folioVO) throws DemodaServiceException;
	public FolioVO deleteFolio(UserContext userContext, FolioVO folioVO) throws DemodaServiceException;
	
	public FolioAdapter imprimirFolio(UserContext userContext, FolioAdapter folioAdapter) throws  DemodaServiceException;
	// <--- ABM Folio

	// ---> ABM FolCom	
	public FolComAdapter getFolComAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public FolComAdapter getFolComAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public FolComAdapter getFolComAdapterForCreate(UserContext userContext, CommonKey commonKey)throws DemodaServiceException;	
	public FolComAdapter getFolComAdapterParamCompensacion(UserContext userContext, FolComAdapter folComAdapter) throws DemodaServiceException;
		
	public FolComVO createFolCom(UserContext userContext, FolComVO folComVO) throws DemodaServiceException;
	public FolComVO updateFolCom(UserContext userContext, FolComVO folComVO) throws DemodaServiceException;
	public FolComVO deleteFolCom(UserContext userContext, FolComVO folComVO) throws DemodaServiceException;
	// <--- ABM FolCom

	// ---> ABM FolDiaCob	
	public FolDiaCobSearchPage getFolDiaCobSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public FolDiaCobSearchPage getFolDiaCobSearchPageResult(UserContext userContext, FolDiaCobSearchPage folDiaCobSearchPage) throws DemodaServiceException;
	public FolDiaCobSearchPage conciliarFolDiaCob(UserContext userContext, FolDiaCobSearchPage folDiaCobSearchPage) throws DemodaServiceException;
	public FolDiaCobSearchPage imprimirFolDiaCobPDF(UserContext userContext, FolDiaCobSearchPage folDiaCobSearchPage) throws DemodaServiceException;
	public FolDiaCobSearchPage generarPlanilla(UserContext userContext, FolDiaCobSearchPage folDiaCobSearchPage) throws DemodaServiceException;

	public FolDiaCobAdapter getFolDiaCobAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public FolDiaCobAdapter getFolDiaCobAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public FolDiaCobAdapter getFolDiaCobAdapterForCreate(UserContext userContext, CommonKey commonKey)throws DemodaServiceException;	
		
	public FolDiaCobVO createFolDiaCob(UserContext userContext, FolDiaCobVO folDiaCobVO) throws DemodaServiceException;
	public FolDiaCobVO updateFolDiaCob(UserContext userContext, FolDiaCobVO folDiaCobVO) throws DemodaServiceException;
	public FolDiaCobVO deleteFolDiaCob(UserContext userContext, FolDiaCobVO folDiaCobVO) throws DemodaServiceException;
	// <--- ABM FolDiaCob
	
	// ---> ABM TipoCob	
	public TipoCobSearchPage getTipoCobSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public TipoCobSearchPage getTipoCobSearchPageResult(UserContext userContext, TipoCobSearchPage tipoCobSearchPage) throws DemodaServiceException;
	public TipoCobAdapter getTipoCobAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public TipoCobAdapter getTipoCobAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public TipoCobAdapter getTipoCobAdapterForCreate(UserContext userContext)throws DemodaServiceException;	
		
	public TipoCobVO createTipoCob(UserContext userContext, TipoCobVO tipoCobVO) throws DemodaServiceException;
	public TipoCobVO updateTipoCob(UserContext userContext, TipoCobVO tipoCobVO) throws DemodaServiceException;
	public TipoCobVO deleteTipoCob(UserContext userContext, TipoCobVO tipoCobVO) throws DemodaServiceException;
	public TipoCobVO activarTipoCob(UserContext userContext, TipoCobVO tipoCobVO) throws DemodaServiceException;
	public TipoCobVO desactivarTipoCob(UserContext userContext, TipoCobVO tipoCobVO) throws DemodaServiceException;
	
	public TipoCobAdapter imprimirTipoCob(UserContext userContext, TipoCobAdapter tipoCobVO ) throws DemodaServiceException;
	// <--- ABM TipoCob
}
