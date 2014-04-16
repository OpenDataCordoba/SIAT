//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.service;

import ar.gov.rosario.siat.bal.iface.model.AseDelAdapter;
import ar.gov.rosario.siat.bal.iface.model.AseDelSearchPage;
import ar.gov.rosario.siat.bal.iface.model.AseDelVO;
import ar.gov.rosario.siat.bal.iface.model.CorridaProcesoAseDelAdapter;
import ar.gov.rosario.siat.bal.iface.model.ProcesoAseDelAdapter;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IBalDelegadorService {
	
	// ---> ABM AseDel
	public AseDelSearchPage getAseDelSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public AseDelSearchPage getAseDelSearchPageResult(UserContext userContext, AseDelSearchPage aseDelSearchPage) throws DemodaServiceException;
	
	public AseDelAdapter getAseDelAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public AseDelAdapter getAseDelAdapterForCreate(UserContext userContext) throws DemodaServiceException;	
	public AseDelAdapter getAseDelAdapterParamFechaBalance(UserContext userContext, AseDelAdapter aseDelAdapter) throws DemodaServiceException;
	
	public AseDelVO createAseDel(UserContext userContext, AseDelVO aseDelVO) throws DemodaServiceException;
	public AseDelVO updateAseDel(UserContext userContext, AseDelVO aseDelVO) throws DemodaServiceException;
	public AseDelVO deleteAseDel(UserContext userContext, AseDelVO aseDelVO) throws DemodaServiceException;
	// <--- ABM AseDel

	// ---> ADM ProcesoAseDel
	public ProcesoAseDelAdapter getProcesoAseDelAdapterInit(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public AseDelVO activar(UserContext userContext, AseDelVO aseDelVO) throws DemodaServiceException;	
	public AseDelVO cancelar(UserContext userContext, AseDelVO aseDelVO) throws DemodaServiceException;
	public AseDelVO reiniciar(UserContext userContext, AseDelVO aseDelVO) throws DemodaServiceException;
	public AseDelVO reprogramar(UserContext userContext, AseDelVO aseDelVO) throws DemodaServiceException;
	public CorridaProcesoAseDelAdapter getCorridaProcesoAseDelAdapterForView(UserContext userContext, CommonKey procesoAseDelKey) throws DemodaServiceException;
	// <--- ADM ProcesoAseDel

}
