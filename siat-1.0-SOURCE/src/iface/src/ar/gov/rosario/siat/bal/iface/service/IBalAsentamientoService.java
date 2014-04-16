//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.service;

import ar.gov.rosario.siat.bal.iface.model.AsentamientoAdapter;
import ar.gov.rosario.siat.bal.iface.model.AsentamientoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.AsentamientoVO;
import ar.gov.rosario.siat.bal.iface.model.CorridaProcesoAsentamientoAdapter;
import ar.gov.rosario.siat.bal.iface.model.ProcesoAsentamientoAdapter;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IBalAsentamientoService {

	// ---> ABM Asentamiento
	public AsentamientoSearchPage getAsentamientoSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public AsentamientoSearchPage getAsentamientoSearchPageResult(UserContext userContext, AsentamientoSearchPage asentamientoSearchPage) throws DemodaServiceException;
	
	public AsentamientoAdapter getAsentamientoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public AsentamientoAdapter getAsentamientoAdapterForCreate(UserContext userContext) throws DemodaServiceException;	
	public AsentamientoAdapter getAsentamientoAdapterParamFechaBalance(UserContext userContext, AsentamientoAdapter asentamientoAdapter) throws DemodaServiceException;
	
	public AsentamientoVO createAsentamiento(UserContext userContext, AsentamientoVO asentamientoVO) throws DemodaServiceException;
	public AsentamientoVO updateAsentamiento(UserContext userContext, AsentamientoVO asentamientoVO) throws DemodaServiceException;
	public AsentamientoVO deleteAsentamiento(UserContext userContext, AsentamientoVO asentamientoVO) throws DemodaServiceException;
	// <--- ABM Asentamiento

	// ---> ADM ProcesoAsentamiento
	public ProcesoAsentamientoAdapter getProcesoAsentamientoAdapterInit(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public AsentamientoVO activar(UserContext userContext, AsentamientoVO asentamientoVO) throws DemodaServiceException;	
	public AsentamientoVO cancelar(UserContext userContext, AsentamientoVO asentamientoVO) throws DemodaServiceException;
	public AsentamientoVO reiniciar(UserContext userContext, AsentamientoVO asentamientoVO) throws DemodaServiceException;
	public AsentamientoVO reprogramar(UserContext userContext, AsentamientoVO asentamientoVO) throws DemodaServiceException;
	public CorridaProcesoAsentamientoAdapter getCorridaProcesoAsentamientoAdapterForView(UserContext userContext, CommonKey procesoAsentamientoKey) throws DemodaServiceException;
	public AsentamientoVO forzar(UserContext userContext, AsentamientoVO asentamientoVO) throws DemodaServiceException;
	// <--- ADM ProcesoAsentamiento


}
