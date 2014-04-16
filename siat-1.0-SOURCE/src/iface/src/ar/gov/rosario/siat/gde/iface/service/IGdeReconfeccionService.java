//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.service;

import java.util.List;

import ar.gov.rosario.siat.gde.iface.model.LiqConvenioCuotaSaldoAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqCuentaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqFormConvenioAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqReciboVO;
import ar.gov.rosario.siat.gde.iface.model.LiqReconfeccionAdapter;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.UserContext;

public interface IGdeReconfeccionService {

	public LiqReconfeccionAdapter getLiqReconfeccionAdapter(UserContext userContext, Long idCuenta, String[] listaIdDeudaEstado, 
			boolean esReconfEspecial, LiqCuentaVO liqCuentaFiltro) throws Exception ;
	public LiqReconfeccionAdapter reconfeccionar(UserContext userContext, LiqReconfeccionAdapter reconfeccionAdapter) throws Exception ;
	public PrintModel getImprimirRecibos(UserContext userContext, List<LiqReciboVO> listReciboVO) throws Exception;
	public LiqReconfeccionAdapter getReconfeccionCuota (UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO)throws Exception;
	public PrintModel getImprimirRecibosCuotas(UserContext userContext, List<LiqReciboVO> listReciboVO) throws Exception;
	public LiqReconfeccionAdapter getCuotaSaldo (UserContext userContext, LiqConvenioCuotaSaldoAdapter liqConvenioCuotaSaldoAdapter)throws Exception;
	
	public LiqReconfeccionAdapter getLiqReconfeccionAdapterForVolantePagoIntRS(UserContext userContext, Long idCuenta, String[] listaIdDeudaEstado, LiqCuentaVO liqCuentaFiltro) throws Exception ;
	public LiqReconfeccionAdapter generarVolantePagoIntRS(UserContext userContext, LiqReconfeccionAdapter liqReconfeccionAdapter) throws Exception;
}
