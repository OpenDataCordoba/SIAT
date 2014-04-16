//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.service;

import ar.gov.rosario.siat.bal.iface.model.OtrIngTesAdapter;
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesParAdapter;
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesParVO;
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesSearchPage;
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.UserContext;

public interface IBalOtroIngresoTesoreriaService {

	// ---> ABM OtrIngTes
	public OtrIngTesSearchPage getOtrIngTesSearchPageInit(UserContext userContext, OtrIngTesSearchPage otrIngTesSPFiltro) throws DemodaServiceException;
	public OtrIngTesSearchPage getOtrIngTesSearchPageResult(UserContext userContext, OtrIngTesSearchPage otrIngTesSearchPage) throws DemodaServiceException;
	
	public OtrIngTesAdapter getOtrIngTesAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public OtrIngTesAdapter getOtrIngTesAdapterForCreate(UserContext userContext) throws DemodaServiceException;	
	public OtrIngTesAdapter getOtrIngTesAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public OtrIngTesAdapter getOtrIngTesAdapterForAdm(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public OtrIngTesVO createOtrIngTes(UserContext userContext, OtrIngTesVO otrIngTesVO) throws DemodaServiceException;
	public OtrIngTesVO updateOtrIngTes(UserContext userContext, OtrIngTesVO otrIngTesVO) throws DemodaServiceException;
	public OtrIngTesVO deleteOtrIngTes(UserContext userContext, OtrIngTesVO otrIngTesVO) throws DemodaServiceException;
	public OtrIngTesVO distribuirOtrIngTes(UserContext userContext, OtrIngTesVO otrIngTesVO) throws DemodaServiceException;
	public OtrIngTesVO imputarOtrIngTes(UserContext userContext, OtrIngTesVO otrIngTesVO) throws DemodaServiceException;
	public PrintModel generarRecibo(UserContext userContext, OtrIngTesVO otrIngTesVO) throws DemodaServiceException;
	
	public OtrIngTesAdapter paramRecurso(UserContext userContext, OtrIngTesAdapter otrIngTesAdapter) throws DemodaServiceException;
	public OtrIngTesAdapter paramArea(UserContext userContext, OtrIngTesAdapter otrIngTesAdapter) throws DemodaServiceException;
	// <--- ABM OtrIngTes
	
	// ---> ABM OtrIngTesPar	
	public OtrIngTesParAdapter getOtrIngTesParAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public OtrIngTesParAdapter getOtrIngTesParAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public OtrIngTesParAdapter getOtrIngTesParAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	
	public OtrIngTesParVO createOtrIngTesPar(UserContext userContext, OtrIngTesParVO otrIngTesParVO) throws DemodaServiceException;
	public OtrIngTesParVO updateOtrIngTesPar(UserContext userContext, OtrIngTesParVO otrIngTesParVO) throws DemodaServiceException;
	public OtrIngTesParVO deleteOtrIngTesPar(UserContext userContext, OtrIngTesParVO otrIngTesParVO) throws DemodaServiceException;
	// <--- ABM OtrIngTesPar

}
