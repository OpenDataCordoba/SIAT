//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.service;

import ar.gov.rosario.siat.gde.iface.model.SelAlmAgregarParametrosSearchPage;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IGdeSelAlmService {


	// DEUDA INCLUIDA
	// agregacion de la deuda incluida 
	public SelAlmAgregarParametrosSearchPage getSelAlmDeudaAgregarSearchPageInit(UserContext userContext, CommonKey recursoKey, CommonKey corridaKey, CommonKey selAlmKey, CommonKey tipoProcMasKey, CommonKey viaDeudaKey) throws DemodaServiceException;
	
	public SelAlmAgregarParametrosSearchPage getSelAlmDeudaAgregarSearchPageParam(UserContext userContext, SelAlmAgregarParametrosSearchPage selAlmAgregarParametrosSearchPage) throws DemodaServiceException;
	
	// carga los parametros usando el proceso
	public SelAlmAgregarParametrosSearchPage cargarParametrosSelAlmDeuda(UserContext userContext, SelAlmAgregarParametrosSearchPage selAlmAgregarParametrosSearchPage) throws DemodaServiceException;

	public SelAlmAgregarParametrosSearchPage paramPersona(UserContext userContext, SelAlmAgregarParametrosSearchPage selAlmAgregarParametrosSearchPageVO) throws DemodaServiceException;

}	
