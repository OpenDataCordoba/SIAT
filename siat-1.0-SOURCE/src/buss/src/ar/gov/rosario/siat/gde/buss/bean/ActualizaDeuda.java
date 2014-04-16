//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.def.buss.bean.IndiceAct;
import ar.gov.rosario.siat.pad.buss.bean.GeneralFacade;
import coop.tecso.demoda.iface.helper.DateUtil;

public class ActualizaDeuda {
	
	private static Logger log = Logger.getLogger(ActualizaDeuda.class);
	private static ActualizaDeuda INSTANCE = null;
	private BufferedWriter file = null;
	
	private static final int TRUNCATE_DECIMAL = 7;   //numero de decimales a truncar

	/**
	 * Constructor privado
	 */
	private ActualizaDeuda() {
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public synchronized static ActualizaDeuda getInstance() throws Exception {
		if (INSTANCE == null) {
			INSTANCE = new ActualizaDeuda();
			INSTANCE.initialize();
		}
		return INSTANCE;
	}

	/**
	 * Inicializa el la clase.
	 * El algoritmo de acutalizacion requiere de datos
	 * externos para calcular el valor de la deuda actualizada.
	 * <p>Este metodo, carga en memoria todos estos valores necesario
	 * para realizar el calculo.
	 * @throws Exception
	 */
	private void initialize() throws Exception {
	}
	
	/**
	 * Calcula la actualizacion del importe segun la fecha de vencimiento
	 * utilizando el dia de hoy como fecha reconfeccion
	 * @param importe valor a actualizar
	 * @param fechaVto fecha de vencimiento original de la deuda
	 * @return Objeto con los datos calculados de recago, importe, importeActualizado, y coeficiente
	 * @see actualizar(Double importe, Date fechaVto, Date fechaReconf)
	 */
	static public DeudaAct actualizar(Date fechaVto, double importe, boolean exentaActualizacion, boolean esActualizable) {
		try {
			if (exentaActualizacion || !esActualizable)return new DeudaAct(importe, importe,0D,0D);
			return ActualizaDeuda.getInstance().actualizarImporte(new Date(), fechaVto, importe);
		} catch (Exception e) {
			log.error("Error al acutalizar deuda:", e);
		}
		return new DeudaAct(-1,-1,-1,-1);
	}

	/**
	 * Calcula la acutualizacion del importe segun la fecha de vencimiento
	 * @param fechaReconf fecha de liminte para calcular la actualizacion.
	 *                    la actualizacion se calcula hasta esta fecha. 
	 * @param fechaVto fecha de vencimiento original de la deuda
	 * @param importe valor a actualizar
	 * @return Objeto con los datos calculados de recargo, importe, importeActualizado, y coeficiente
	 */
	static public DeudaAct actualizar(Date fechaReconf, Date fechaVto, double importe, boolean exentaActualizacion, boolean esActualizable) throws Exception {
		try {
			if (exentaActualizacion || !esActualizable)return new DeudaAct(importe, importe,0D,0D);
			return ActualizaDeuda.getInstance().actualizarImporte(fechaReconf, fechaVto, importe);
		} catch (Exception e) {
			log.error("Error al acutalizar deuda:", e);
		}
		return new DeudaAct(-1,-1,-1,-1);
	}

	/**
	 * Calcula la actualizacion y el importe original a partir del importe actualizado segun la fecha de vencimiento
	 * @param fechaReconf fecha de limite para calcular la actualizacion.
	 *                    la actualizacion se calcula hasta esta fecha. 
	 * @param fechaVto fecha de vencimiento original de la deuda
	 * @param importe actualizado
	 * @return valor de actualizacion e importe original
	 */
	static public DeudaAct calcularActualizacion(Date fechaReconf, Date fechaVto, double importe, double coefDesc) throws Exception {
		try {
			throw new Exception("Not Implemented!!");			
		} catch (Exception e) {
			log.error("Error al acutalizar deuda:", e);
		}
		return new DeudaAct(-1,-1,-1,-1);
	}

	/************************************************************************
	 * De aca para abajo, estan los metodos especificos de actualizacion
	 */

	
	/**
	 * Calcula la acutualizacion del importe segun la fecha de vencimiento
	 * @param fechaReconf fecha de liminte para calcular la actualizacion.
	 *                    la actualizacion se calcula hasta esta fecha. 
	 * @param fechaVto fecha de vencimiento original de la deuda
	 * @param importe valor a actualizar
	 * @return valor actualizado
	 */
	protected DeudaAct actualizarImporte(Date fechaReconf, Date fechaVto, double importe) throws Exception {
		double importeAct = 0.0;
		double recargo = 0.0;
		double CoefDiario = 0.0002;
		double coef = 0;
		
		int dias = 1;
		try {
			dias = DateUtil.getCantDias(fechaVto, fechaReconf);
			coef = dias * CoefDiario;
		} catch (Exception e) {}
				
	    importeAct = importe * (1 + coef);
	    recargo = importeAct - importe;
	    	    
	    log.debug("ACTUADEU:" + importeAct + "-" + coef);
	    return new DeudaAct(importe, importeAct, recargo, coef);
	}
}
