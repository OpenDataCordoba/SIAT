//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.service;

import ar.gov.rosario.siat.cas.iface.model.SolPendReport;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.UserContext;


public interface ICasReporteService {
	
	// ---> Reporte de Solicitudes Pendientes
	public SolPendReport getSolPendReportInit(UserContext userContext) throws DemodaServiceException;
	public SolPendReport getSolPendReportResult(UserContext userContext, SolPendReport solPendReportVO) throws DemodaServiceException;
	public SolPendReport getSolPendReportParamArea(UserContext userContext, SolPendReport solPendReportVO)throws DemodaServiceException;
	// <--- Reporte de Solicitudes Pendientes
	
}
