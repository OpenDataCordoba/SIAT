//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.service;

import ar.gov.rosario.siat.gde.iface.model.CierreComercioAdapter;
import ar.gov.rosario.siat.gde.iface.model.CierreComercioVO;
import ar.gov.rosario.siat.gde.iface.model.DesgloseAjusteAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqDecJurAdapter;
import ar.gov.rosario.siat.gde.iface.model.MultaAdapter;
import ar.gov.rosario.siat.gde.iface.model.MultaSearchPage;
import ar.gov.rosario.siat.gde.iface.model.MultaVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.UserContext;

public interface IGdeGDeudaAutoService {
	public DesgloseAjusteAdapter getDesgloseAjusteInit(UserContext userContext, DesgloseAjusteAdapter desgloseAjusteAdapterVO) throws Exception ;
	
	// ---> ABM Multa
	public MultaSearchPage getMultaSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public MultaSearchPage getMultaSearchPageResult(UserContext userContext, MultaSearchPage multaSearchPage) throws DemodaServiceException;
	
	public MultaAdapter getMultaAdapterForCreate(UserContext userContext,MultaAdapter multaAdapter) throws DemodaServiceException;
	public MultaAdapter getMultaAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public MultaAdapter getMultaAdapterForView(UserContext userContext,	CommonKey commonKey) throws DemodaServiceException;
	public MultaAdapter getMultaAdapterParamTipoMulta(UserContext userContext, MultaAdapter multaAdapterVO) throws DemodaServiceException;
	
	public MultaVO createMulta(UserContext userContext, MultaVO multaVO) throws DemodaServiceException;
	public MultaVO deleteMulta(UserContext userContext, MultaVO multaVO) throws DemodaServiceException;
	public MultaVO activarMulta(UserContext userContext, MultaVO multaVO) throws DemodaServiceException;
	public MultaVO desactivarMulta(UserContext userContext, MultaVO multaVO) throws DemodaServiceException;
	public MultaVO updateMulta(UserContext userContext, MultaVO multaVO) throws DemodaServiceException;
	
	public PrintModel imprimirMulta(UserContext userContext, MultaAdapter multaAdapterVO ) throws DemodaServiceException;
	// <--- ABM Multa
		
	// ---> Descuento Multa
	public MultaAdapter getMultaAdapterParamDescuentoMulta(UserContext userContext, MultaAdapter multaAdapter) throws DemodaServiceException;
	// <--- Descuento Multa
	
	// ---> Detalle Multa
	public MultaAdapter getMultaAdapterParamDetalleMulta(UserContext userContext, MultaAdapter multaAdapter) throws DemodaServiceException;
	// <--- Detalle Multa
	
	// ---> Cierre Comercio
	public CierreComercioAdapter getCierreComercioAdapterInit(UserContext userContext,CierreComercioAdapter cierreComercioAdapter) throws DemodaServiceException;
	public CierreComercioAdapter createCierreComercio(UserContext userContext,CierreComercioAdapter cierreComercioAdapter) throws DemodaServiceException;
	public CierreComercioAdapter updateCierreComercio(UserContext userContext, CierreComercioAdapter cierreComercioAdapter)	throws DemodaServiceException;
	public CierreComercioAdapter getCierreComercioAdapterParamMotivoCierre(UserContext userContext,CierreComercioAdapter cierreComercioAdapter) throws DemodaServiceException;
	public CierreComercioAdapter inicioCierreComercio(UserContext userContext,CierreComercioAdapter cierreComercioAdapter) throws DemodaServiceException;
	// <--- Cierre Comercio
	
	public DesgloseAjusteAdapter getDesglose(DesgloseAjusteAdapter desgloseAjusteAdapter) throws Exception;
	
	public MultaAdapter createMultaHistorico(UserContext userContext,MultaAdapter multaAdapter) throws DemodaServiceException;

	// ---> CierreComercio	
	public CierreComercioAdapter getCierreComercioAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public CierreComercioAdapter getCierreComercioAdapterForUpdate(UserContext userContext, CommonKey commonKeyPlan) throws DemodaServiceException;
	public CierreComercioVO updateCierreComercio(UserContext userContext, CierreComercioVO cierreComercioVO) throws DemodaServiceException;
	// <--- CierreComercio
	
	// ---> Declaracion Jurada Masiva
	public LiqDecJurAdapter getLiqDecJurAdapterInit(UserContext userContext, LiqDecJurAdapter liqDecJurAdapterVO) throws DemodaServiceException;
	public LiqDecJurAdapter getLiqDecJurAdapterDetalle(UserContext userContext, LiqDecJurAdapter liqDecJurAdapterVO) throws DemodaServiceException;
	public LiqDecJurAdapter getLiqDecJurAdapterGeneral(UserContext userContext, LiqDecJurAdapter liqDecJurAdapterVO) throws DemodaServiceException;
	public LiqDecJurAdapter getLiqDecJurAdapterSimular(UserContext userContext, LiqDecJurAdapter liqDecJurAdapterVO) throws DemodaServiceException;
	public LiqDecJurAdapter getLiqDecJurAdapterSimularAFecha(UserContext userContext, LiqDecJurAdapter liqDecJurAdapterVO) throws DemodaServiceException;
	public LiqDecJurAdapter getLiqDecJurAdapterGenerar(UserContext userContext, LiqDecJurAdapter liqDecJurAdapterVO) throws DemodaServiceException;
	public PrintModel imprimirSimulacion(UserContext userContext, LiqDecJurAdapter liqDecJurAdapterVO) throws DemodaServiceException;
	// <--- Declaracion Jurada Masiva
}
