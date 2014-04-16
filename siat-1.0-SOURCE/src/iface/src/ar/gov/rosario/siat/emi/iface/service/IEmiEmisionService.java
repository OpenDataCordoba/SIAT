//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.service;

import ar.gov.rosario.siat.emi.iface.model.AuxDeudaAdapter;
import ar.gov.rosario.siat.emi.iface.model.AuxDeudaSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionExtAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionExtSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionExternaAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionExternaSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionMasAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionMasSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionPuntualAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionPuntualPreviewAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionPuntualSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionTRPAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import ar.gov.rosario.siat.emi.iface.model.PlanoDetalleAdapter;
import ar.gov.rosario.siat.emi.iface.model.PlanoDetalleVO;
import ar.gov.rosario.siat.emi.iface.model.ProcesoEmisionExternaAdapter;
import ar.gov.rosario.siat.emi.iface.model.ProcesoEmisionMasAdapter;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IEmiEmisionService {
	
	// ---> ABM Emision Masiva 
	public EmisionMasSearchPage getEmisionMasSearchPageInit(UserContext usercontext) 
		throws DemodaServiceException;
	public EmisionMasSearchPage getEmisionMasSearchPageResult(UserContext usercontext, 
			EmisionMasSearchPage emisionMasSearchPage) throws DemodaServiceException;

	public EmisionMasAdapter getEmisionMasAdapterForView(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public EmisionMasAdapter getEmisionMasAdapterForCreate(UserContext userContext) 
		throws DemodaServiceException;
	public EmisionMasAdapter getEmisionMasAdapterForUpdate(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public EmisionMasAdapter getEmisionMasAdapterParamRecurso(UserContext userContext, EmisionMasAdapter emisionMasAdapterVO) 
		throws DemodaServiceException;

	public EmisionVO createEmisionMas(UserContext userContext, EmisionMasAdapter emisionMasAdapter) 
		throws DemodaServiceException;
	public EmisionVO updateEmisionMas(UserContext userContext, EmisionMasAdapter emisionMasAdapter) 
		throws DemodaServiceException;
	public EmisionVO deleteEmisionMas(UserContext userContext, EmisionMasAdapter emisionMasAdapter) 
		throws DemodaServiceException;

	public ProcesoEmisionMasAdapter getProcesoEmisionMasAdapterInit(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public EmisionVO reiniciar(UserContext userContext, EmisionVO emision) throws DemodaServiceException;
	// <--- ABM Emision Masiva
	
	// ---> Consulta AuxDeuda
 	public AuxDeudaSearchPage getAuxDeudaSearchPageInit(UserContext userContext, Long idEmision) 
 		throws DemodaServiceException;
 	public AuxDeudaSearchPage getAuxDeudaSearchPageResult(UserContext userContext, 
 			AuxDeudaSearchPage auxDeudaSearchPage) throws DemodaServiceException;

	public AuxDeudaAdapter getAuxDeudaAdapterForView(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
 	// <--- Consulta AuxDeuda
	
	// ---> ABM Emision Extraordinaria
	public EmisionExtSearchPage getEmisionExtSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public EmisionExtSearchPage getEmisionExtSearchPageResult(UserContext userContext,
			EmisionExtSearchPage emisionExtSearchPage) throws DemodaServiceException;

	public EmisionExtAdapter getEmisionExtAdapterForView(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public EmisionExtAdapter getEmisionExtAdapterForCreate(UserContext userContext) 
		throws DemodaServiceException;
	public EmisionExtAdapter getEmisionExtAdapterParamRecurso(UserContext userContext, 
			EmisionExtAdapter emisionExtAdapter) throws DemodaServiceException;
	public EmisionExtAdapter getEmisionExtAdapterParamCuenta(UserContext userContext, 
			EmisionExtAdapter emisionExtAdapter) throws DemodaServiceException;

	public EmisionVO createEmisionExt(UserContext userContext, EmisionExtAdapter emisionExtAdapter) 
		throws DemodaServiceException;
	// <--- ABM Emision Extraordinaria

	// ---> ABM Emision Puntual 
	public EmisionPuntualSearchPage getEmisionPuntualSearchPageInit(UserContext usercontext) 
		throws DemodaServiceException;
	public EmisionPuntualSearchPage getEmisionPuntualSearchPageParamCuenta(UserContext userContext, 
			EmisionPuntualSearchPage emisionPuntualSearchPage) throws DemodaServiceException;
	public EmisionPuntualSearchPage getEmisionPuntualSearchPageResult(UserContext usercontext, 
			EmisionPuntualSearchPage emisionPuntualSearchPage) 
		throws DemodaServiceException;
	
	public EmisionPuntualAdapter getEmisionPuntualAdapterForView(UserContext userContext, CommonKey commonKey)
		throws DemodaServiceException;
	public EmisionPuntualPreviewAdapter getEmisionPuntualPreviewAdapter(UserContext userContext, EmisionPuntualPreviewAdapter emisionPuntualPreviewAdapterPasado)
		throws DemodaServiceException;
	public EmisionPuntualAdapter getEmisionPuntualAdapterForCreate(UserContext userContext, CuentaVO cuentVO) 
		throws DemodaServiceException;
	public EmisionPuntualAdapter getEmisionPuntualAdapterParamRecurso(UserContext userContext, 
			EmisionPuntualAdapter emisionPuntualAdapter) throws DemodaServiceException;
	public EmisionPuntualAdapter getEmisionPuntualAdapterParamCuenta(UserContext userContext, 
			EmisionPuntualAdapter emisionPuntualAdapter) throws DemodaServiceException;
	
	public EmisionPuntualAdapter createDeudaAdminFromAuxDeuda(UserContext userContext, 
			EmisionPuntualAdapter emisionPuntualAdapter) throws DemodaServiceException;
	public EmisionVO createEmisionPuntual(UserContext userContext, EmisionPuntualAdapter emisionPuntualAdapter) 
		throws DemodaServiceException;

	public EmisionTRPAdapter getEmisionTRPAdapterForCreate(UserContext userContext, CuentaVO cuentVO) 
		throws DemodaServiceException;
	public EmisionTRPAdapter getEmisionTRPAdapterParamCuenta(UserContext userContext, EmisionTRPAdapter emisionTRPAdapter) 
		throws DemodaServiceException;
	public PlanoDetalleAdapter getPlanoDetalleAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public PlanoDetalleVO createPlanoDetalle(UserContext userContext, PlanoDetalleVO planoDetalleVO) 
		throws DemodaServiceException;;
	
	public EmisionVO createEmisionTRP(UserContext userContext, EmisionTRPAdapter emisionTRPAdapter) 
		throws DemodaServiceException;
	// <--- ABM Emision Puntual
		
	// ---> Emision Externa	
	public EmisionExternaSearchPage getEmisionExternaSearchPageInit(UserContext usercontext)throws DemodaServiceException;
	public EmisionExternaSearchPage getEmisionExternaSearchPageResult(UserContext usercontext, EmisionExternaSearchPage emisionExternaSearchPage)throws DemodaServiceException;
	public EmisionExternaAdapter getEmisionExternaAdapterForView(UserContext userContext, CommonKey commonKey)throws DemodaServiceException;
	public EmisionVO deleteEmisionExterna(UserContext userContext, EmisionVO emisionVO)throws DemodaServiceException;
	public EmisionExternaAdapter emisionExternaUploadFile(UserContext userContext, EmisionExternaAdapter emisionExternaAdapter)throws DemodaServiceException;
	public EmisionExternaAdapter emisionExternaValidarFile(UserContext userContext, EmisionExternaAdapter emisionExternaAdapter)throws DemodaServiceException;
	public EmisionExternaAdapter createEmisionExterna(UserContext userContext, EmisionExternaAdapter emisionExternaAdapter)throws DemodaServiceException;
	public ProcesoEmisionExternaAdapter getProcesoEmisionExternaAdapterInit(UserContext userContext, CommonKey commonKey)throws DemodaServiceException;
	// <--- Emision Externa
	
}
