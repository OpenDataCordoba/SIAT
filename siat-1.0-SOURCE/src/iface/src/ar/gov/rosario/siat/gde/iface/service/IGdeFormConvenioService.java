//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.service;

import ar.gov.rosario.siat.gde.iface.model.LiqConvenioCuentaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqFormConvenioAdapter;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.UserContext;

public interface IGdeFormConvenioService {

	
	public LiqFormConvenioAdapter getLiqFormConvenioInit(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception ;
	
	public LiqFormConvenioAdapter getPlanes(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception ;
	
	public LiqFormConvenioAdapter getPlanesEsp (UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception;
	
	public LiqFormConvenioAdapter getAlternativaCuotas(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception ;
		
	public LiqFormConvenioAdapter getSimulacionCuotas(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception ;
	
	public LiqFormConvenioAdapter getFormalizarPlanInit(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception ;
	
	public LiqFormConvenioAdapter formalizarPlan(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception;
	
	public LiqFormConvenioAdapter paramPersona(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO, Long selectedId) throws Exception;
	
	public PrintModel getPrintForm(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception;
	
	public PrintModel getPrintRecibos(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception;
	
	public PrintModel getPrintAltCuotas(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception;
	
	public PrintModel getPrintFormAuto(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception;
		
	public LiqFormConvenioAdapter getConvenioFormalizado(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception;

	public void crearTransaccionDummy(UserContext userContext, LiqConvenioCuentaAdapter liqConvenioCuentaAdapterVO) throws Exception;
		
}
