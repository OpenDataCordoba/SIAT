//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.afi.buss.bean.AfiFormulariosDJManager;
import ar.gov.rosario.siat.afi.buss.bean.EstForDecJur;
import ar.gov.rosario.siat.afi.buss.bean.ForDecJur;
import ar.gov.rosario.siat.afi.buss.dao.AfiDAOFactory;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.DecJur;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.DemodaStringMsg;

/**
 * Manejador del m&oacute;dulo Bal y submodulo Envios de Osiris
 * 
 * @author tecso
 *
 */
public class BalEnvioOsirisManager {


	private static final BalEnvioOsirisManager INSTANCE = new BalEnvioOsirisManager();
	
	private Logger log = Logger.getLogger(BalEnvioOsirisManager.class); 
	
	/**
	 * Constructor privado
	 */
	private BalEnvioOsirisManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static BalEnvioOsirisManager getInstance() {
		return INSTANCE;
	}
	
	
	// ---> ABM EnvioOsiris	
	public EnvioOsiris createEnvioOsiris(EnvioOsiris envioOsiris) throws Exception {

		// Validaciones de negocio
		if (!envioOsiris.validateCreate()) {
			return envioOsiris;
		}

		BalDAOFactory.getEnvioOsirisDAO().update(envioOsiris);

		return envioOsiris;
	}
	
	public EnvioOsiris updateEnvioOsiris(EnvioOsiris envioOsiris) throws Exception {
		
		// Validaciones de negocio
		if (!envioOsiris.validateUpdate()) {
			return envioOsiris;
		}
		
		BalDAOFactory.getEnvioOsirisDAO().update(envioOsiris);
		
	    return envioOsiris;
	}
	
	public EnvioOsiris deleteEnvioOsiris(EnvioOsiris envioOsiris) throws Exception {

		
		// Validaciones de negocio
		if (!envioOsiris.validateDelete()) {
			return envioOsiris;
		}
		
		BalDAOFactory.getEnvioOsirisDAO().delete(envioOsiris);
		
		return envioOsiris;
	}
	// <--- ABM EnvioOsiris

	/**
	 * Procesa TranAfip generando para cada una el Formulario DJ AFIP (forDecJur), 
	 * Formularios DDJJ SIAT (decJur) y determinando Deuda (DeudaAdmin).
	 * 
	 * @param tranAfip
	 * @throws Exception 
	 */
	public Boolean procesarTranAfip(TranAfip tranAfip, EnvioOsiris envioOsiris) throws Exception{
		
		//Mapa de contadores del Envio
		//ForDecJur Generadas Correctamente
		int forDecJurGen = tranAfip.getEnvioOsiris().getCountMap().get(1); 
		//ForDecJur Pendientes de Procesar
		int forDecJurPen = tranAfip.getEnvioOsiris().getCountMap().get(2); 
		
		// Intento creo Formulario DDJJ AFIP (ForDecJur)
		ForDecJur forDecJur = tranAfip.generarForDecJur();

		/* Si no puedo generar el ForDecJur retorno false para
		 * marcar la TranAfip como procesada con error.
		 */
		if (forDecJur.hasError()) {
			forDecJur.passErrorMessages(tranAfip);
			forDecJur.clearError();
			return false;
		}
		
		//Guardo el ForDecJur creado
		AfiFormulariosDJManager.getInstance().createForDecJur(forDecJur);
		
		SiatHibernateUtil.currentSession().getTransaction().commit();
		SiatHibernateUtil.currentSession().beginTransaction();
		
		//Incremento la cant de DDJJ Afip Generadas
		forDecJurGen++;
		tranAfip.getEnvioOsiris().getCountMap().put(1, forDecJurGen);
		
		
		List<DecJur> listDecJur = null;
		try {
			// Para el ForDecJur intento generar DDJJ SIAT (uno por Cuenta)
			SiatHibernateUtil.currentSession().refresh(forDecJur);//agregado para que levante las cuentas ordenadas
			listDecJur = forDecJur.generarDecJur();
		} catch (Exception e) {
			// Si ocurre una excepcion, cargo un error al ForDecJur
			String logErr="ForDecJur de id: "+forDecJur.getId()+". Ocurrió un error inesperado o no manejado.";
				   logErr+= "\nExcepcion: "+StringUtil.stackTrace(e);
				   
			forDecJur.addRecoverableValueError(logErr);
		}
		
		if (forDecJur.hasError() || listDecJur == null) {
			SiatHibernateUtil.currentSession().getTransaction().rollback();
			SiatHibernateUtil.closeSession();
			
			SiatHibernateUtil.currentSession().refresh(envioOsiris);
			SiatHibernateUtil.currentSession().refresh(tranAfip);
			SiatHibernateUtil.currentSession().refresh(forDecJur);
			SiatHibernateUtil.currentSession().beginTransaction();
			
			//Logueo los errores detectados en el forDecJur 
			String logErr = "\nErrores detectados al intentar generar DDJJ SIAT: ";
			for (DemodaStringMsg error : forDecJur.getListError()) 
				logErr += "\n - "+error.key().substring(1);
			forDecJur.setObservaciones(logErr);
			
			//Incremento la cant de DDJJ Afip Pendientes a Procesar
			tranAfip.getEnvioOsiris().getCountMap().put(2, forDecJurPen + 1);
			tranAfip.getEnvioOsiris().getCountMap().put(1, forDecJurGen);
			
			AfiFormulariosDJManager.getInstance().updateForDecJur(forDecJur);
			
			SiatHibernateUtil.currentSession().getTransaction().commit();
			SiatHibernateUtil.currentSession().beginTransaction();
			
			/* si no puedo generar la DecJur retorno true para marcar la TranAfip
			 * como PROCESADO_OK. El ForDecJur queda con estado SIN_PROCESAR
			 * para poder corregirlo. 
			 */
			return true;
		}
		
		// Determinacion de Deuda por Declaracion Jurada
		List<LiqDeudaVO> listDeuda = new ArrayList<LiqDeudaVO>();
		
		// Para cada DDJJ SIAT
		for (DecJur decJur: listDecJur){
			try {
				// Intento Procesarla
				decJur.procesarDDJJ(listDeuda,null, true); 
			} catch (Exception e) {
				// Si ocurre una excepcion, cargo un error a la DecJur
				String logErr="ForDecJur de id: "+forDecJur.getId()+". Ocurrió un error inesperado o no manejado.";
					   logErr+= "\nExcepcion: "+StringUtil.stackTrace(e);
					   
				decJur.clearError();
				decJur.addRecoverableValueError(logErr);
			}
			
			if (decJur.hasError()) {
				SiatHibernateUtil.currentSession().getTransaction().rollback();
				SiatHibernateUtil.closeSession();
				
				SiatHibernateUtil.currentSession().refresh(envioOsiris);
				SiatHibernateUtil.currentSession().refresh(tranAfip);
				SiatHibernateUtil.currentSession().refresh(forDecJur);
				SiatHibernateUtil.currentSession().beginTransaction();
				
				//Logueo los errores detectados en el forDecJur 
				String logErr = "\nErrores detectados al intentar procesar DDJJ SIAT: ";
				for (DemodaStringMsg error : decJur.getListError()) 
					logErr += "\n - "+error.key().substring(1);
				forDecJur.setObservaciones(logErr);
				
				//Incremento la cant de DDJJ Afip Pendientes a Procesar
				tranAfip.getEnvioOsiris().getCountMap().put(2, forDecJurPen + 1);
				tranAfip.getEnvioOsiris().getCountMap().put(1, forDecJurGen);
				
				AfiFormulariosDJManager.getInstance().updateForDecJur(forDecJur);
				
				SiatHibernateUtil.currentSession().getTransaction().commit();
				SiatHibernateUtil.currentSession().beginTransaction();
				
				/* si no puedo procesar la DecJur retorno true para marcar la TranAfip
				 * como PROCESADO_OK. El ForDecJur queda con estado SIN_PROCESAR para
				 * poder corregirlo. 
				 */
				return true;
			}
		}				

		//Seteo estado Formulario DDJJ AFIP a Procesada
		forDecJur.setEstForDecJur(EstForDecJur.getById(EstForDecJur.ID_PROCESADA));
		AfiDAOFactory.getForDecJurDAO().update(forDecJur);

		return true;
	}
	
	/**
	 *  Procesa las novedades de rechazo de Cheques asociadas al banco y cierrebanco incluidos en el Envio.
	 *  Este metodo se ejecuta durante el obtenerEnvio
	 */
	public void aplicarNovedadRechazoCheque(EnvioOsiris envioOsiris){
		String funcName = "aplicarNovedadRechazoCheque()";
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//Lista de CierreBanco asociados al envio en proceso
		log.debug("lista de cierreBanco: "+envioOsiris.getListCierreBanco().size());
		for(CierreBanco cierreBanco: envioOsiris.getListCierreBanco()){
			
			//Lista de Novedades de cheques rechazados
			List<NovedadEnvio> listNovedad = cierreBanco.getListNovedadForRechazoCheque();
			
			for (NovedadEnvio novedadEnvio : listNovedad) {
				//Actualizo el estado del DetallePago a anulado asi la conciliacion no suma estos montos
				BalDAOFactory.getDetallePagoDAO().updateEstDetPago(novedadEnvio.getId(), 
																   novedadEnvio.getIdTransaccionAfip(), 
																   EstDetPago.ID_ANULADO);
			}
		}

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
	}
	
}
