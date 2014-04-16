//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.gde;

/**
 * Genera el Reporte de Convenios en forma desconectada.
 * 
 * @author Tecso Coop. Ltda.
 */
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.iface.model.AccionMasivaConvenioAdapter;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpWorker;
import coop.tecso.adpcore.engine.AdpParameter;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;

/**
 * Genera el Reporte de Convenios en forma desconectada.
 * 
 * @author Tecso Coop. Ltda.
 */
public class AccionConveniosWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(AccionConveniosWorker.class);
	
	

	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void execute(AdpRun adpRun) throws Exception {
		// Verfica numero paso y estado en adprun,
		// Llama a cada metodo segun el numero de paso
		Long pasoActual = adpRun.getPasoActual();
		if (pasoActual.equals(1L)){ 
			this.accionConvenios(adpRun);
		}else{
			// No existen otros pasos para el Proceso.
		}
	}

	public void reset(AdpRun adpRun) throws Exception {
	}

	public boolean validate(AdpRun adpRun) throws Exception {
		return false;
	}

	/**
	 *  Generar Reporte de Convenios (PDF).
	 * 
	 * @param adpRun
	 * @throws Exception
	 */
	public void accionConvenios(AdpRun adpRun) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			

			
			
			AdpParameter paramIdsConvenios = adpRun.getAdpParameter(AccionMasivaConvenioAdapter.ACCION_CONVENIOS_IDS);
			AdpParameter paramAccion = adpRun.getAdpParameter(AccionMasivaConvenioAdapter.ACCION_CONVENIO_ACCION);
			
			String accion = paramAccion.getValue();
			String idConvenios=paramIdsConvenios.getValue();
			boolean huboErrores = false;
			
			adpRun.logDebug("Lista de id de Convenios: "+idConvenios);
			List<Long> listIdConvenio =ListUtil.getListIdFromStringWithCommas(idConvenios); 
			
			int cantConvenios=listIdConvenio.size();
			
			int count=1;
			for (Long idConvenio : listIdConvenio){
				Convenio convenio= Convenio.getByIdNull(idConvenio);
				if (convenio!=null){
					tx = session.beginTransaction();
					if (accion.equals(Convenio.ACCION_APLICA_PAGO_CUENTA)){
						convenio.aplicarPagosACuenta(0L);
					}
					if (accion.equals(Convenio.ACCION_SALDO_POR_CADUCIDAD)){
						convenio.realizarSaldoPorCaducidad("", new CasoVO(), false);
					}
					if (accion.equals(Convenio.ACCION_REHABILITAR)){
						convenio.vueltaAtrasSalPorCad("", new CasoVO());
					}
					if (convenio.hasError()){
						adpRun.logDebug(accion + " Falla para convenio nro="+convenio.getNroConvenio()+ ", id="+ idConvenio+ " error "+ convenio.errorString());
						huboErrores=true;
						tx.rollback();
					}else{
						adpRun.logDebug(accion + " OK para convenio nro="+convenio.getNroConvenio()+ ", id="+ idConvenio);
						tx.commit();
					}
				}else{
					adpRun.logDebug("Convenio con id = "+ idConvenio + " No Encontrado");
				}
				if(count%5==0){
					int porcProc= count * 100 / cantConvenios;
					log.debug("INGRESO A FLUSH, PORCENTAJE: "+porcProc + " CONTADOR: "+count);
					AdpRun.changeRunMessage(porcProc + " % convenios procesados",0);
					session.flush();
				}
				count++;
			}
			String errores="";
			if (huboErrores)
				errores="Hubo Errores en convenios, revisar log";
			else
				errores="Procesado sin errores";
			adpRun.changeStateFinOk(errores);
			
			
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
}
