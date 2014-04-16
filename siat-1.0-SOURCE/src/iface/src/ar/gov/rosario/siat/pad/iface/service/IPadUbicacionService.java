//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.service;

import ar.gov.rosario.siat.pad.iface.model.CalleSearchPage;
import ar.gov.rosario.siat.pad.iface.model.CalleVO;
import ar.gov.rosario.siat.pad.iface.model.DomicilioAdapter;
import ar.gov.rosario.siat.pad.iface.model.DomicilioSearchPage;
import ar.gov.rosario.siat.pad.iface.model.DomicilioVO;
import ar.gov.rosario.siat.pad.iface.model.LocalidadSearchPage;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IPadUbicacionService {
	
	// ---> Calle
	public CalleSearchPage getCalleSearchPageInit(UserContext usercontext, CalleVO calleVO) throws DemodaServiceException;
	public CalleSearchPage getCalleSearchPageResult(UserContext usercontext, CalleSearchPage calleSearchPage) throws DemodaServiceException;
	// <--- Calle
	
	
	// ---> Domicilio
	public DomicilioVO validarDomicilio(UserContext usercontext, DomicilioVO domicilioVO ) throws DemodaServiceException;
	
	public DomicilioSearchPage getDomicilioSearchPageInit(UserContext usercontext, DomicilioVO domicilioVO) throws DemodaServiceException;
	public DomicilioSearchPage getDomicilioSearchPageResult(UserContext usercontext, DomicilioSearchPage domicilioSearchPage) throws DemodaServiceException;	
	
	public DomicilioAdapter getDomicilioAdapterForValidate(UserContext usercontext) throws DemodaServiceException;
	public DomicilioAdapter getDomicilioAdapterParamLocalidad(UserContext usercontext, DomicilioAdapter domicilioAdapterVO) throws DemodaServiceException;
	public DomicilioAdapter getDomicilioAdapterParamCalle(UserContext usercontext, DomicilioAdapter domicilioAdapterVO) throws DemodaServiceException;
	// <--- Domicilio
	
	// ---> Localidad
	public LocalidadSearchPage getLocalidadSearchPageInit(UserContext userContext, CommonKey commonKeyProvincia ) throws DemodaServiceException;
	public LocalidadSearchPage getLocalidadSearchPageResult(UserContext usercontext, LocalidadSearchPage localidadSearchPage) throws DemodaServiceException;
	
	// <--- Localidad
}