//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.service;

import ar.gov.rosario.siat.gde.iface.model.ProPreDeuAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProPreDeuDetSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ProPreDeuSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ProPreDeuVO;
import ar.gov.rosario.siat.gde.iface.model.ProcesoPrescripcionDeudaAdapter;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IGdeAdmDeuConService {
	
	// ---> ABM ProPreDeu
	public ProPreDeuSearchPage getProPreDeuSearchPageInit(UserContext usercontext) 
		throws DemodaServiceException;
	public ProPreDeuSearchPage getProPreDeuSearchPageResult(UserContext usercontext, 
			ProPreDeuSearchPage proPreDeuSearchPage) throws DemodaServiceException;

	public ProPreDeuAdapter getProPreDeuAdapterForView(UserContext userContext, 
			CommonKey commonKey) throws DemodaServiceException;
	public ProPreDeuAdapter getProPreDeuAdapterForCreate(UserContext userContext) 
			throws DemodaServiceException;
	public ProPreDeuAdapter getProPreDeuAdapterForUpdate(UserContext userContext, 
			CommonKey commonKey) throws DemodaServiceException;
	
	public ProPreDeuVO createProPreDeu(UserContext userContext, ProPreDeuVO proPreDeuVO ) 
		throws DemodaServiceException;
	public ProPreDeuVO updateProPreDeu(UserContext userContext, ProPreDeuVO proPreDeuVO ) 
		throws DemodaServiceException;
	public ProPreDeuVO deleteProPreDeu(UserContext userContext, ProPreDeuVO proPreDeuVO ) 
		throws DemodaServiceException;
	public ProPreDeuAdapter imprimirProPreDeu(UserContext userContext, ProPreDeuAdapter 
			proPreDeuAdapterVO ) throws DemodaServiceException;
	
	public ProcesoPrescripcionDeudaAdapter getProcesoPrescripcionDeudaAdapterInit
		(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ProPreDeuVO activar(UserContext userContext, ProPreDeuVO proPreDeuVO) 
		throws DemodaServiceException;
	public ProPreDeuVO cancelar(UserContext userContext, ProPreDeuVO proPreDeuVO) 
		throws DemodaServiceException;
	public ProPreDeuVO reiniciar(UserContext userContext, ProPreDeuVO proPreDeuVO) 
		throws DemodaServiceException;
	// <--- ABM ProPreDeu
	
	// ---> ABM ProPreDeuDet
	public ProPreDeuDetSearchPage getProPreDeuDetSearchPageInit(UserContext usercontext, Long idProPreDeu) 
		throws DemodaServiceException;
	public ProPreDeuDetSearchPage getProPreDeuDetSearchPageResult(UserContext usercontext, 
			ProPreDeuDetSearchPage proPreDeuDetSearchPage) throws DemodaServiceException;
	// <--- ABM ProPreDeuDet
}