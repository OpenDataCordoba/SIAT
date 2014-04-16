//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.service;

import ar.gov.rosario.siat.bal.iface.model.DisParAdapter;
import ar.gov.rosario.siat.bal.iface.model.DisParDetAdapter;
import ar.gov.rosario.siat.bal.iface.model.DisParDetVO;
import ar.gov.rosario.siat.bal.iface.model.DisParPlaAdapter;
import ar.gov.rosario.siat.bal.iface.model.DisParPlaSearchPage;
import ar.gov.rosario.siat.bal.iface.model.DisParPlaVO;
import ar.gov.rosario.siat.bal.iface.model.DisParRecAdapter;
import ar.gov.rosario.siat.bal.iface.model.DisParRecSearchPage;
import ar.gov.rosario.siat.bal.iface.model.DisParRecVO;
import ar.gov.rosario.siat.bal.iface.model.DisParSearchPage;
import ar.gov.rosario.siat.bal.iface.model.DisParVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IBalDistribucionService {

	// ---> ABM DisPar
	public DisParSearchPage getDisParSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public DisParSearchPage getDisParSearchPageResult(UserContext userContext, DisParSearchPage disParSearchPage) throws DemodaServiceException;
	
	public DisParAdapter getDisParAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public DisParAdapter getDisParAdapterForCreate(UserContext userContext) throws DemodaServiceException;	
	public DisParAdapter getDisParAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public DisParAdapter paramTipoImporte(UserContext userContext, DisParAdapter disParAdapter) throws DemodaServiceException;
	public DisParAdapter paramRecCon(UserContext userContext, DisParAdapter disParAdapter) throws DemodaServiceException;
	
	public DisParVO createDisPar(UserContext userContext, DisParVO disParVO) throws DemodaServiceException;
	public DisParVO updateDisPar(UserContext userContext, DisParVO disParVO) throws DemodaServiceException;
	public DisParVO deleteDisPar(UserContext userContext, DisParVO disParVO) throws DemodaServiceException;
	public DisParVO activarDisPar(UserContext userContext, DisParVO disParVO) throws DemodaServiceException;
	public DisParVO desactivarDisPar(UserContext userContext, DisParVO disParVO) throws DemodaServiceException;
	// <--- ABM DisPar
	
	// ---> ABM DisParDet
	public DisParDetAdapter getDisParDetAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public DisParDetAdapter getDisParDetAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public DisParDetAdapter getDisParDetAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public DisParDetAdapter getDisParDetAdapterParamTipoImporte(UserContext userContext, DisParDetAdapter disParDetAdapter) throws DemodaServiceException;
	
	public DisParDetVO createDisParDet(UserContext userContext, DisParDetVO disParDetVO) throws DemodaServiceException;
	public DisParDetVO updateDisParDet(UserContext userContext, DisParDetVO disParDetVO) throws DemodaServiceException;
	public DisParDetVO deleteDisParDet(UserContext userContext, DisParDetVO disParDetVO) throws DemodaServiceException;
	// <--- ABM DisParDet
	
	// ---> ABM DisParRec
	public DisParRecSearchPage getDisParRecSearchPageInit(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public DisParRecSearchPage getDisParRecSearchPageResult(UserContext userContext, DisParRecSearchPage disParRecSearchPage) throws DemodaServiceException;
	
	public DisParRecAdapter getDisParRecAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public DisParRecAdapter getDisParRecAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	
	public DisParRecVO createDisParRec(UserContext userContext, DisParRecVO disParRecVO) throws DemodaServiceException;
	public DisParRecVO updateDisParRec(UserContext userContext, DisParRecVO disParRecVO) throws DemodaServiceException;
	public DisParRecVO deleteDisParRec(UserContext userContext, DisParRecVO disParRecVO) throws DemodaServiceException;
	// <--- ABM DisParRec

	// ---> ABM DisParPla
	public DisParPlaSearchPage getDisParPlaSearchPageInit(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public DisParPlaSearchPage getDisParPlaSearchPageResult(UserContext userContext, DisParPlaSearchPage disParPlaSearchPage) throws DemodaServiceException;
	
	public DisParPlaAdapter getDisParPlaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public DisParPlaAdapter getDisParPlaAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	
	public DisParPlaVO createDisParPla(UserContext userContext, DisParPlaVO disParPlaVO) throws DemodaServiceException;
	public DisParPlaVO updateDisParPla(UserContext userContext, DisParPlaVO disParPlaVO) throws DemodaServiceException;
	public DisParPlaVO deleteDisParPla(UserContext userContext, DisParPlaVO disParPlaVO) throws DemodaServiceException;
	// <--- ABM DisParPla

}
