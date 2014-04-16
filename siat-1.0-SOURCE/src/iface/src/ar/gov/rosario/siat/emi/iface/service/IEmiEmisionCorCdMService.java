//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.service;

import ar.gov.rosario.siat.emi.iface.model.AuxDeudaSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import ar.gov.rosario.siat.emi.iface.model.ProcesoEmisionCorCdMAdapter;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IEmiEmisionCorCdMService extends IEmiEmisionServiceOld {
	
	// ---> ABM Proceso Emision Corregida de CdM
	public ProcesoEmisionCorCdMAdapter getProcesoEmisionCorCdMAdapterInit(UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException;
	public EmisionVO activar(UserContext userContext, EmisionVO emisionVO) throws DemodaServiceException;
	public EmisionVO reprogramar(UserContext userContext, EmisionVO emisionVO) throws DemodaServiceException;
	public EmisionVO cancelar(UserContext userContext, EmisionVO emisionVO) throws DemodaServiceException;
	public EmisionVO reiniciar(UserContext userContext, EmisionVO emisionVO) throws DemodaServiceException;
	// <--- ABM Proceso Emision Corregida de CdM
	
	// ---> Consulta AuxDeu por Cuenta
 	public AuxDeudaSearchPage getAuxDeudaSearchPageInit(UserContext userContext, Long idEmision) throws DemodaServiceException;
 	public AuxDeudaSearchPage getAuxDeudaSearchPageResult(UserContext userContext, 
 			AuxDeudaSearchPage auxDeudaSearchPage) throws DemodaServiceException;
	// <--- Consulta AuxDeu por Cuenta

}