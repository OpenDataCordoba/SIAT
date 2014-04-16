//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.service;

import ar.gov.rosario.siat.bal.iface.model.ArchivoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.AuxCaja7Adapter;
import ar.gov.rosario.siat.bal.iface.model.AuxCaja7SearchPage;
import ar.gov.rosario.siat.bal.iface.model.AuxCaja7VO;
import ar.gov.rosario.siat.bal.iface.model.BalanceAdapter;
import ar.gov.rosario.siat.bal.iface.model.BalanceSearchPage;
import ar.gov.rosario.siat.bal.iface.model.BalanceVO;
import ar.gov.rosario.siat.bal.iface.model.Caja69Adapter;
import ar.gov.rosario.siat.bal.iface.model.Caja69VO;
import ar.gov.rosario.siat.bal.iface.model.Caja7Adapter;
import ar.gov.rosario.siat.bal.iface.model.Caja7VO;
import ar.gov.rosario.siat.bal.iface.model.CompensacionSearchPage;
import ar.gov.rosario.siat.bal.iface.model.ControlConciliacionSearchPage;
import ar.gov.rosario.siat.bal.iface.model.CorridaProcesoBalanceAdapter;
import ar.gov.rosario.siat.bal.iface.model.ProcesoBalanceAdapter;
import ar.gov.rosario.siat.bal.iface.model.ReingresoAdapter;
import ar.gov.rosario.siat.bal.iface.model.TranBalAdapter;
import ar.gov.rosario.siat.bal.iface.model.TranBalSearchPage;
import ar.gov.rosario.siat.bal.iface.model.TranBalVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IBalBalanceService {
	
	// ---> ABM Caja7
	public Caja7Adapter getCaja7AdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public Caja7Adapter getCaja7AdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public Caja7Adapter getCaja7AdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public Caja7VO createCaja7(UserContext userContext, Caja7VO caja7VO ) throws DemodaServiceException;
	public Caja7VO updateCaja7(UserContext userContext, Caja7VO caja7VO ) throws DemodaServiceException;
	public Caja7VO deleteCaja7(UserContext userContext, Caja7VO caja7VO ) throws DemodaServiceException;
	public Caja7Adapter imprimirCaja7(UserContext userContext, Caja7Adapter caja7AdapterVO ) throws DemodaServiceException;
	// <--- ABM Caja7
	
	// ---> ABM Balance
	public BalanceSearchPage getBalanceSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public BalanceSearchPage getBalanceSearchPageResult(UserContext userContext, BalanceSearchPage balanceSearchPage) throws DemodaServiceException;
	
	public BalanceAdapter getBalanceAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public BalanceAdapter getBalanceAdapterForCreate(UserContext userContext) throws DemodaServiceException;	
	public BalanceAdapter getBalanceAdapterParamEjercicio(UserContext userContext, BalanceAdapter balanceAdapter) throws DemodaServiceException;
	
	public BalanceVO createBalance(UserContext userContext, BalanceVO balanceVO) throws DemodaServiceException;
	public BalanceVO updateBalance(UserContext userContext, BalanceVO balanceVO) throws DemodaServiceException;
	public BalanceVO deleteBalance(UserContext userContext, BalanceVO balanceVO) throws DemodaServiceException;
	// <--- ABM Balance

	// ---> ADM ProcesoBalance
	public ProcesoBalanceAdapter getProcesoBalanceAdapterInit(UserContext userContext, CommonKey commonKey, ProcesoBalanceAdapter procesoBalanceAdapter) throws DemodaServiceException;
	public BalanceVO activar(UserContext userContext, BalanceVO balanceVO) throws DemodaServiceException;	
	public BalanceVO cancelar(UserContext userContext, BalanceVO balanceVO) throws DemodaServiceException;
	public BalanceVO reiniciar(UserContext userContext, BalanceVO balanceVO) throws DemodaServiceException;
	public BalanceVO reprogramar(UserContext userContext, BalanceVO balanceVO) throws DemodaServiceException;
	public BalanceVO retrocederPaso(UserContext userContext, BalanceVO balanceVO) throws DemodaServiceException;
	public CorridaProcesoBalanceAdapter getCorridaProcesoBalanceAdapterForView(UserContext userContext, CommonKey procesoBalanceKey) throws DemodaServiceException;
	
	public ProcesoBalanceAdapter incluirFolio(UserContext userContext, ProcesoBalanceAdapter procesoBalanceAdapter) throws DemodaServiceException; 
	public ProcesoBalanceAdapter excluirFolio(UserContext userContext, ProcesoBalanceAdapter procesoBalanceAdapter) throws DemodaServiceException; 
	public ArchivoSearchPage incluirArchivo(UserContext userContext, ArchivoSearchPage archivoSearchPage) throws DemodaServiceException;
	public ProcesoBalanceAdapter excluirArchivo(UserContext userContext, ProcesoBalanceAdapter procesoBalanceAdapter) throws DemodaServiceException;
	public ProcesoBalanceAdapter incluirCaja7(UserContext userContext, ProcesoBalanceAdapter procesoBalanceAdapter) throws DemodaServiceException; 
	public ProcesoBalanceAdapter excluirCaja7(UserContext userContext, ProcesoBalanceAdapter procesoBalanceAdapter) throws DemodaServiceException;
	public CompensacionSearchPage incluirCompensacion(UserContext userContext, CompensacionSearchPage compensacionSearchPage) throws DemodaServiceException;
	public ProcesoBalanceAdapter excluirCompensacion(UserContext userContext, ProcesoBalanceAdapter procesoBalanceAdapter) throws DemodaServiceException;

	public ReingresoAdapter getReingresoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ReingresoAdapter getReingresoAdapterForIncluir(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ReingresoAdapter getReingresoAdapterForExcluir(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;

	public ReingresoAdapter incluirReingreso(UserContext userContext, ReingresoAdapter reingresoAdapter) throws DemodaServiceException;
	public ReingresoAdapter excluirReingreso(UserContext userContext, ReingresoAdapter reingresoAdapter) throws DemodaServiceException;
	
	public ReingresoAdapter getReingresoAdapterParamActualizar(UserContext userContext, ReingresoAdapter reingresoAdapter) throws DemodaServiceException;
	// <--- ADM ProcesoBalance

	// ---> ABM Caja69
	public Caja69Adapter getCaja69AdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public Caja69Adapter getCaja69AdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public Caja69Adapter getCaja69AdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public Caja69VO createCaja69(UserContext userContext, Caja69VO caja69VO ) throws DemodaServiceException;
	public Caja69VO updateCaja69(UserContext userContext, Caja69VO caja69VO ) throws DemodaServiceException;
	public Caja69VO deleteCaja69(UserContext userContext, Caja69VO caja69VO ) throws DemodaServiceException;
	public Caja69Adapter imprimirCaja69(UserContext userContext, Caja69Adapter caja69AdapterVO ) throws DemodaServiceException;
	// <--- ABM Caja69
	
	// ---> ABM TranBal
	public TranBalSearchPage getTranBalSearchPageInit(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public TranBalSearchPage getTranBalSearchPageResult(UserContext userContext, TranBalSearchPage tranBalSearchPage) throws DemodaServiceException;

	public TranBalAdapter getTranBalAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public TranBalAdapter getTranBalAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public TranBalAdapter getTranBalAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public TranBalVO createTranBal(UserContext userContext, TranBalVO tranBalVO ) throws DemodaServiceException;
	public TranBalVO updateTranBal(UserContext userContext, TranBalVO tranBalVO ) throws DemodaServiceException;
	public TranBalVO deleteTranBal(UserContext userContext, TranBalVO tranBalVO ) throws DemodaServiceException;
	public TranBalAdapter imprimirTranBal(UserContext userContext, TranBalAdapter tranBalAdapterVO ) throws DemodaServiceException;
	// <--- ABM TranBal
	
	// ---> ABM AuxCaja7
	public AuxCaja7SearchPage getAuxCaja7SearchPageInit(UserContext userContext, AuxCaja7SearchPage auxCaja7SPFiltro) throws DemodaServiceException;
	public AuxCaja7SearchPage getAuxCaja7SearchPageResult(UserContext userContext, AuxCaja7SearchPage auxCaja7SearchPage) throws DemodaServiceException;
	public AuxCaja7SearchPage incluirAuxCaja7(UserContext userContext, AuxCaja7SearchPage auxCaja7SearchPage) throws DemodaServiceException;

	public AuxCaja7Adapter getAuxCaja7AdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public AuxCaja7Adapter getAuxCaja7AdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public AuxCaja7Adapter getAuxCaja7AdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public AuxCaja7VO createAuxCaja7(UserContext userContext, AuxCaja7VO auxCaja7VO ) throws DemodaServiceException;
	public AuxCaja7VO updateAuxCaja7(UserContext userContext, AuxCaja7VO auxCaja7VO ) throws DemodaServiceException;
	public AuxCaja7VO deleteAuxCaja7(UserContext userContext, AuxCaja7VO auxCaja7VO ) throws DemodaServiceException;
	public AuxCaja7Adapter imprimirAuxCaja7(UserContext userContext, AuxCaja7Adapter auxCaja7AdapterVO ) throws DemodaServiceException;
	public AuxCaja7SearchPage imprimirAuxCaja7(UserContext userContext, AuxCaja7SearchPage auxCaja7SearchPage) throws  DemodaServiceException;
	public AuxCaja7VO activarAuxCaja7(UserContext userContext, AuxCaja7VO auxCaja7VO) throws DemodaServiceException;
	// <--- ABM AuxCaja7
	
	// ---> Control de Conciliacion
	public ControlConciliacionSearchPage getControlConciliacionSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public ControlConciliacionSearchPage getControlConciliacionSearchPageResult(UserContext userContext, ControlConciliacionSearchPage controlConciliacionSearchPage) throws DemodaServiceException;
	// <--- Control de Conciliacion
}
