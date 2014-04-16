//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.service;

import ar.gov.rosario.siat.bal.iface.model.ClaComReport;
import ar.gov.rosario.siat.bal.iface.model.ClasificadorAdapter;
import ar.gov.rosario.siat.bal.iface.model.ClasificadorReport;
import ar.gov.rosario.siat.bal.iface.model.ClasificadorSearchPage;
import ar.gov.rosario.siat.bal.iface.model.ClasificadorVO;
import ar.gov.rosario.siat.bal.iface.model.ConsultaNodoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.NodoAdapter;
import ar.gov.rosario.siat.bal.iface.model.NodoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.NodoVO;
import ar.gov.rosario.siat.bal.iface.model.RelClaAdapter;
import ar.gov.rosario.siat.bal.iface.model.RelClaVO;
import ar.gov.rosario.siat.bal.iface.model.RelPartidaAdapter;
import ar.gov.rosario.siat.bal.iface.model.RelPartidaVO;
import ar.gov.rosario.siat.bal.iface.model.RentasReport;
import ar.gov.rosario.siat.bal.iface.model.TotalParReport;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IBalClasificacionService {

	// ---> ABM Nodo
	public NodoSearchPage getNodoSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public NodoSearchPage getNodoSearchPageResult(UserContext userContext, NodoSearchPage nodoSearchPage) throws DemodaServiceException;
	public NodoSearchPage getNodoSearchPageForTree(UserContext userContext,	NodoSearchPage nodoSearchPage, Long idSelectedNodo) throws DemodaServiceException;
	public NodoSearchPage getNodoSearchPageForVolver(UserContext userContext, NodoSearchPage nodoSearchPage, Long idSelectedNodo, boolean expandirNodo) throws DemodaServiceException;		

	public NodoAdapter getNodoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public NodoAdapter getNodoAdapterForCreate(UserContext userContext, CommonKey commonKey,String idClaStr) throws DemodaServiceException;	
	public NodoAdapter getNodoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public NodoVO createNodo(UserContext userContext, NodoVO nodoVO) throws DemodaServiceException;
	public NodoVO updateNodo(UserContext userContext, NodoVO nodoVO) throws DemodaServiceException;
	public NodoVO deleteNodo(UserContext userContext, NodoVO nodoVO) throws DemodaServiceException;
	
	public NodoSearchPage imprimirArbolDeClasificacion(UserContext userContext,	NodoSearchPage nodoSearchPage) throws DemodaServiceException;
	// <--- ABM Nodo
	
	// ---> ABM Clasificador
	public ClasificadorSearchPage getClasificadorSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public ClasificadorSearchPage getClasificadorSearchPageResult(UserContext userContext, ClasificadorSearchPage clasificadorSearchPage) throws DemodaServiceException;
	
	public ClasificadorAdapter getClasificadorAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ClasificadorAdapter getClasificadorAdapterForCreate(UserContext userContext) throws DemodaServiceException;	
	public ClasificadorAdapter getClasificadorAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public ClasificadorVO createClasificador(UserContext userContext, ClasificadorVO clasificadorVO) throws DemodaServiceException;
	public ClasificadorVO updateClasificador(UserContext userContext, ClasificadorVO clasificadorVO) throws DemodaServiceException;
	public ClasificadorVO deleteClasificador(UserContext userContext, ClasificadorVO clasificadorVO) throws DemodaServiceException;
	
	public ClasificadorAdapter imprimirClasificador(UserContext userContext, ClasificadorAdapter clasificadorVO ) throws DemodaServiceException;
	// <--- ABM Clasificador
	
	// ---> ABM RelCla	
	public RelClaAdapter getRelClaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public RelClaAdapter getRelClaAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public RelClaAdapter getRelClaAdapterParamClasificador(UserContext userContext, RelClaAdapter relClaAdapter) throws DemodaServiceException;
	
	public RelClaVO createRelCla(UserContext userContext, RelClaVO relClaVO) throws DemodaServiceException;
	public RelClaVO updateRelCla(UserContext userContext, RelClaVO relClaVO) throws DemodaServiceException;
	public RelClaVO deleteRelCla(UserContext userContext, RelClaVO relClaVO) throws DemodaServiceException;
	// <--- ABM RelCla

	//	 ---> ABM RelPartida	
	public RelPartidaAdapter getRelPartidaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public RelPartidaAdapter getRelPartidaAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	
	public RelPartidaVO createRelPartida(UserContext userContext, RelPartidaVO relPartidaVO) throws DemodaServiceException;
	public RelPartidaVO updateRelPartida(UserContext userContext, RelPartidaVO relPartidaVO) throws DemodaServiceException;
	public RelPartidaVO deleteRelPartida(UserContext userContext, RelPartidaVO relPartidaVO) throws DemodaServiceException;
	// <--- ABM RelPartida
	
	// ---> Consultar Reporte de Clasificador
	public ClasificadorReport getClasificadorReportInit(UserContext userContext) throws DemodaServiceException;
	public ClasificadorReport getClasificadorReportResult(UserContext userContext, ClasificadorReport clasificadorReport) throws DemodaServiceException;
	public ClasificadorReport getClasificadorReportParamEjercicio(UserContext userContext, ClasificadorReport clasificadorReport) throws DemodaServiceException;
	public ClasificadorReport getClasificadorReportParamClasificador(UserContext userContext, ClasificadorReport clasificadorReport) throws DemodaServiceException;
	public ClasificadorReport getClasificadorReportParamNivel(UserContext userContext, ClasificadorReport clasificadorReport) throws DemodaServiceException; 
	// <--- Consultar Reporte de Clasificador
	
	// ---> Consultar Reporte de Rentas
	public RentasReport getRentasReportInit(UserContext userContext) throws DemodaServiceException;
	public RentasReport getRentasReportResult(UserContext userContext, RentasReport rentasReport) throws DemodaServiceException;
	// <--- Consultar Reporte de Rentas

	// ---> Consulta de Total por Nodo
	public ConsultaNodoSearchPage getConsultaNodoSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public ConsultaNodoSearchPage getConsultaNodoSearchPageResult(UserContext userContext, ConsultaNodoSearchPage consultaNodoSearchPage) throws DemodaServiceException;
	public ConsultaNodoSearchPage getConsultaNodoSearchPageParamClasificador(UserContext userContext, ConsultaNodoSearchPage consultaNodoSearchPage) throws DemodaServiceException;
	public ConsultaNodoSearchPage getConsultaNodoSearchPageParamNivel(UserContext userContext, ConsultaNodoSearchPage consultaNodoSearchPage) throws DemodaServiceException; 
	public ConsultaNodoSearchPage getConsultaNodoSearchPageValidarNodo(UserContext userContext, ConsultaNodoSearchPage consultaNodoSearchPage) throws DemodaServiceException;
	public ConsultaNodoSearchPage getConsultaNodoSearchPageParamRango(UserContext userContext, ConsultaNodoSearchPage consultaNodoSearchPage) throws DemodaServiceException;
	public ConsultaNodoSearchPage getConsultaNodoSearchPageParamEspecial(UserContext userContext, ConsultaNodoSearchPage consultaNodoSearchPage) throws DemodaServiceException;
	// <--- Consulta de Total por Nodo
	
	// ---> Consultar Reporte de Clasificador Comparativo
	public ClaComReport getClaComReportInit(UserContext userContext) throws DemodaServiceException;
	public ClaComReport getClaComReportResult(UserContext userContext, ClaComReport claComReport) throws DemodaServiceException;
	public ClaComReport getClaComReportParamClasificador(UserContext userContext, ClaComReport claComReport) throws DemodaServiceException;
	// <--- Consultar Reporte de Clasificador Comparativo
	
	// ---> Reporte de Total por Partida
	public TotalParReport getTotalParReportInit(UserContext userContext) throws DemodaServiceException;
	public TotalParReport getTotalParReportResult(UserContext userContext, TotalParReport totalParReport) throws DemodaServiceException;
	public TotalParReport getTotalParReportParamRango(UserContext userContext, TotalParReport totalParReport) throws DemodaServiceException;
	// <--- Reporte de Total por Partida
	
}
