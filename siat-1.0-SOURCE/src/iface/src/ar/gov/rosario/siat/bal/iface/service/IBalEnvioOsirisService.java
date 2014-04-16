//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.service;

import ar.gov.rosario.siat.bal.iface.model.CierreBancoAdapter;
import ar.gov.rosario.siat.bal.iface.model.ConciliacionAdapter;
import ar.gov.rosario.siat.bal.iface.model.DetalleDJAdapter;
import ar.gov.rosario.siat.bal.iface.model.DetalleDJVO;
import ar.gov.rosario.siat.bal.iface.model.DetallePagoAdapter;
import ar.gov.rosario.siat.bal.iface.model.DetallePagoVO;
import ar.gov.rosario.siat.bal.iface.model.EnvioOsirisAdapter;
import ar.gov.rosario.siat.bal.iface.model.EnvioOsirisSearchPage;
import ar.gov.rosario.siat.bal.iface.model.EnvioOsirisVO;
import ar.gov.rosario.siat.bal.iface.model.TranAfipAdapter;
import ar.gov.rosario.siat.bal.iface.model.TranAfipVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IBalEnvioOsirisService {
	
	// ---> ABM EnvioOsiris
	public EnvioOsirisSearchPage getEnvioSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public EnvioOsirisSearchPage getEnvioSearchPageResult (EnvioOsirisSearchPage envioSearchPage, UserContext userContext)throws DemodaServiceException;
		
	public EnvioOsirisAdapter getEnvioOsirisAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public EnvioOsirisAdapter getEnvioOsirisAdapterForObtener(UserContext userContext) throws DemodaServiceException;
	public EnvioOsirisAdapter getEnvioOsirisAdapterForProcesar(UserContext userContext) throws DemodaServiceException;
	
	public EnvioOsirisAdapter obtenerEnviosOsiris(UserContext userContext, EnvioOsirisAdapter envioOsirisAdapter) throws DemodaServiceException;
	public EnvioOsirisAdapter procesarEnviosOsiris(UserContext userContext, EnvioOsirisAdapter envioOsirisAdapter) throws DemodaServiceException;
	public EnvioOsirisVO generarTransaccion(UserContext userContext, EnvioOsirisVO envioOsirisVO) throws DemodaServiceException;
	public EnvioOsirisVO generarDecJur(UserContext userContext, EnvioOsirisVO envioOsirisVO) throws DemodaServiceException;
	public EnvioOsirisVO cambiarEstado(UserContext userContext, EnvioOsirisVO envioOsirisVO) throws DemodaServiceException;
	public EnvioOsirisAdapter imprimirEnvioOsiris(UserContext userContext, EnvioOsirisAdapter envioOsirisAdapterVO ) throws DemodaServiceException;
	// <--- ABM EnvioOsiris
	
	// ---> ABM Cierre Banco
	public CierreBancoAdapter getCierreBancoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	// ---> ABM Cierre Banco
	
	// ---> ABM Tran Afip
	public TranAfipAdapter getTranAfipAdapterForView(UserContext userContext,CommonKey commonKey) throws DemodaServiceException;
	public TranAfipVO generarDecJurForTranAfip(UserContext userContext, TranAfipVO tranAfipVO) throws DemodaServiceException;
	public TranAfipVO eliminarTranAfip(UserContext userContext, TranAfipVO tranAfipVO) throws DemodaServiceException;

	// <--- ABM Tran Afip
	
	// ---> ABM Detalle DJ
	public DetalleDJAdapter getDetalleDJAdapterForView(UserContext userContext,	CommonKey commonKey) throws DemodaServiceException;
	public DetalleDJVO eliminarDetalleDJ(UserContext userContext, DetalleDJVO detalleDJ) throws DemodaServiceException;
	// <--- ABM Detalle DJ
	
	// ---> ABM Detalle Pago
	public DetallePagoAdapter getDetallePagoAdapterForView(UserContext userContext,	CommonKey commonKey) throws DemodaServiceException;
	public DetallePagoVO eliminarDetallePago(UserContext userContext, DetallePagoVO detallePago) throws DemodaServiceException;
	// <--- ABM Detalle Pago
	
	// ---> ABM Conciliacion
	public ConciliacionAdapter getConciliacionAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ConciliacionAdapter imprimirConciliacion(UserContext userContext, ConciliacionAdapter conciliacionAdapterVO ) throws DemodaServiceException ;
	// ---> ABM Conciliacion


}
