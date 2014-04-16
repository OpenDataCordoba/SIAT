//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.iface.model.SalPorCadMasivoAdapter;
import ar.gov.rosario.siat.gde.iface.model.SaldoPorCaducidadVO;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class GdeProcesosMasivosBeanHelper {
	
	private static Logger log = Logger.getLogger(GdeProcesosMasivosBeanHelper.class);

	Plan plan = null;
	Recurso recurso = null;
	
	
	/**
	 * Constructor que recibe y setea una cuenta.
	 * 
	 * @param cuenta
	 * @throws DemodaServiceException
	 */
	public GdeProcesosMasivosBeanHelper(Plan plan) throws DemodaServiceException {					
		this.plan = plan;	

	}


	public SalPorCadMasivoAdapter createSalPorCad (SalPorCadMasivoAdapter salPorCadMasivo)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug(funcName+" :enter");
		SalPorCad salPorCad = new SalPorCad();
		SaldoPorCaducidadVO saldoVO = salPorCadMasivo.getSaldoPorCaducidad();
		String description = "Saldo por Caducidad Masivo en "+plan.getDesPlan();
		AdpRun run=AdpRun.newRun(Proceso.PROCESO_SALPORCAD_MASIVO, description);
		
		run.create();
		Corrida corrida = Corrida.getByIdNull(run.getId());

		try{
			
			salPorCad.setCorrida(corrida);
			salPorCad.setCuotaSuperiorA(saldoVO.getCuotaSuperiorA());
			salPorCad.setFecForDes(saldoVO.getFechaFormDesdeSaldo());
			salPorCad.setFecForHas(saldoVO.getFechaFormHastaSaldo());
			salPorCad.setFechaSalCad(saldoVO.getFechaSaldo());

			salPorCad.setObservacion(saldoVO.getObservacion());
			salPorCad.setPlan(this.plan);

			
			// 1) Registro uso de expediente
			AccionExp accionExp = AccionExp.getById(AccionExp.ID_SALDO_POR_CAD_MASIVO); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(saldoVO, salPorCad, 
        			accionExp, null, salPorCad.infoString() );
        	// Si no pasa la validacion, pasa error al BO y retorna. 
        	if (saldoVO.hasError()){
        		saldoVO.passErrorMessages(salPorCadMasivo);
				return salPorCadMasivo;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	salPorCad.setIdCaso(saldoVO.getIdCaso());
			
			//guardo en la base de datos
			salPorCad.update();

			if (salPorCad.hasError()){
				salPorCad.passErrorMessages(salPorCadMasivo);
				return salPorCadMasivo;
			}

			salPorCadMasivo.getSaldoPorCaducidad().setCorrida(corrida.toVOWithDesEstado());
			salPorCadMasivo.getSaldoPorCaducidad().setPlan(this.plan.toVOForView());
			salPorCadMasivo.getSaldoPorCaducidad().setId(salPorCad.getId());
			run.putParameter(SalPorCad.ID_SALPORCAD, salPorCad.getId().toString());
			
			return salPorCadMasivo;
		
		} catch (Exception exception){
			salPorCadMasivo.addNonRecoverableError(exception.getMessage());
			return salPorCadMasivo;
		}
	}
	

	
}
	