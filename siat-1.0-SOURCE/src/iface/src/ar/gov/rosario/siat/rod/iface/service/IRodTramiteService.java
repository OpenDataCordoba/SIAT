//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.service;

import ar.gov.rosario.siat.pad.iface.model.DomicilioAdapter;
import ar.gov.rosario.siat.rod.iface.model.ModeloSearchPage;
import ar.gov.rosario.siat.rod.iface.model.PropietarioAdapter;
import ar.gov.rosario.siat.rod.iface.model.PropietarioVO;
import ar.gov.rosario.siat.rod.iface.model.TramiteRAAdapter;
import ar.gov.rosario.siat.rod.iface.model.TramiteRASearchPage;
import ar.gov.rosario.siat.rod.iface.model.TramiteRAVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.UserContext;

public interface IRodTramiteService {
	
	// ---> ABM TramiteRA
	public TramiteRASearchPage getTramiteRASearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public TramiteRASearchPage getTramiteRASearchPageResult(UserContext usercontext, TramiteRASearchPage tramiteRASearchPage) throws DemodaServiceException;

	public TramiteRAAdapter getTramiteRAAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public TramiteRAAdapter getTramiteRAAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public TramiteRAAdapter getTramiteRAAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public TramiteRAVO createTramiteRA(UserContext userContext, TramiteRAVO tramiteRAVO ) throws DemodaServiceException;
	public TramiteRAVO updateTramiteRA(UserContext userContext, TramiteRAVO tramiteRAVO ) throws DemodaServiceException;
	public TramiteRAVO deleteTramiteRA(UserContext userContext, TramiteRAVO tramiteRAVO ) throws DemodaServiceException;
	public PrintModel imprimirTramiteRA(UserContext userContext, TramiteRAAdapter tramteRAAdapterVO ) throws DemodaServiceException;
	
	public TramiteRAAdapter getTramiteRAAdapterParamPersonaActual(UserContext userContext, TramiteRAAdapter tramiteRAAdapter) throws DemodaServiceException;
	public TramiteRAAdapter getTramiteRAAdapterParamTipoFabricacion(UserContext userContext, TramiteRAAdapter tramiteRAAdapterVO ) throws DemodaServiceException;
	public TramiteRAAdapter getTramiteRAAdapterParamTipoTramite(UserContext userContext, TramiteRAAdapter tramiteRAAdapterVO ) throws DemodaServiceException;
	public DomicilioAdapter getTramiteRAAdapterParamLocalidad(UserContext userContext, DomicilioAdapter domicilioAdapterVO ) throws DemodaServiceException;
	public TramiteRAAdapter getTramiteRAAdapterParamTipoDocPropAnterior(UserContext userContext, TramiteRAAdapter tramiteRAAdapterVO ) throws DemodaServiceException;
	public TramiteRAAdapter getTramiteRAAdapterParamBTipoMotor(UserContext userContext, TramiteRAAdapter tramiteRAAdapterVO ) throws DemodaServiceException;
	public TramiteRAAdapter getTramiteRAAdapterParamETipoMotor(UserContext userContext, TramiteRAAdapter tramiteRAAdapterVO ) throws DemodaServiceException;
	public TramiteRAAdapter getTramiteRAAdapterParamTipoPago(UserContext userContext, TramiteRAAdapter tramiteRAAdapterVO ) throws DemodaServiceException;
	public TramiteRAAdapter getTramiteRAAdapterParamTipoUso(UserContext userContext, TramiteRAAdapter tramiteRAAdapterVO ) throws DemodaServiceException;
	public TramiteRAAdapter getTramiteRAAdapterParamPropAnterior(UserContext userContext, TramiteRAAdapter tramiteRAAdapterVO ) throws DemodaServiceException;
	public TramiteRAAdapter getTramiteRAAdapterParamTipoCarga(UserContext userContext, TramiteRAAdapter tramiteRAAdapterVO ) throws DemodaServiceException;
	public TramiteRAAdapter getTramiteRAAdapterParamCalle(UserContext userContext, TramiteRAAdapter tramiteRAAdapterVO ) throws DemodaServiceException;
	
	public TramiteRAVO cambiarEstadoTramiteRA(UserContext userContext,TramiteRAAdapter tramiteRAAdapter) throws DemodaServiceException;
	public TramiteRAAdapter getTramiteRAAdapterForCambiarEstado(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public TramiteRAVO marcarTitularPrincipal(UserContext userContext, TramiteRAVO tramiteRAVO) throws DemodaServiceException;
	// <--- ABM TramiteRA

	// Modelo
	public TramiteRAAdapter getTramiteRAAdapterParamModelo(UserContext userContext, TramiteRAAdapter tramiteRAAdapter) throws DemodaServiceException;
	public ModeloSearchPage getModeloSearchPageResult(UserContext userContext,ModeloSearchPage modeloSearchPage) throws DemodaServiceException;
	public ModeloSearchPage getModeloSearchPageInit(UserContext userContext,Integer codigo, String descrip) throws DemodaServiceException;
	// Fin Modelo

	// Propietario
	public PropietarioAdapter getPropietarioAdapterParamTipoDoc(UserContext userContext, PropietarioAdapter propietarioAdapterVO ) throws DemodaServiceException;
	public PropietarioAdapter getPropietarioAdapterParamEstadoCivil(UserContext userContext, PropietarioAdapter propietarioAdapterVO ) throws DemodaServiceException;
	public PropietarioAdapter getPropietarioAdapterParamTipoPropietario(UserContext userContext, PropietarioAdapter propietarioAdapterVO ) throws DemodaServiceException;
	
	public PropietarioAdapter getPropietarioAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PropietarioAdapter getPropietarioAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public PropietarioAdapter getPropietarioAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public PropietarioVO updatePropietario(UserContext userContext, PropietarioVO propietarioVO ) throws DemodaServiceException;
	public PropietarioVO deletePropietario(UserContext userContext, PropietarioVO propietarioVO ) throws DemodaServiceException;
	public PropietarioVO validateCreatePropietario(UserContext userContext, PropietarioVO propietarioVO ) throws DemodaServiceException;
	public PropietarioAdapter imprimirPropietario(UserContext userContext, PropietarioAdapter propietarioAdapterVO ) throws DemodaServiceException;
	// Fin Propietario
	
	// validate
	public String validatePropietario(UserContext userContext, TramiteRAAdapter tramiteRAAdapterVO) throws DemodaServiceException;
	public String validatePatente(UserContext userContext, TramiteRAAdapter tramiteRAAdapterVO) throws DemodaServiceException;
		
}