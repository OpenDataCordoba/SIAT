//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.service;

import org.apache.log4j.Logger;

/**
 *  - Service locator
 * @author tecso
 */
public class BalServiceLocator {

	static Logger log = Logger.getLogger(BalServiceLocator.class);

	// Implementaciones de servicio	
	private static String MODULO = "ar.gov.rosario.siat.bal.buss.service.";
	private static String DEFINICION_SERVICE_IMPL = MODULO + "BalDefinicionServiceHbmImpl";
	private static String RECLAMO_SERVICE_IMPL = MODULO + "BalReclamoServiceHbmImpl";
	private static String DISTRIBUCION_SERVICE_IMPL = MODULO + "BalDistribucionServiceHbmImpl";
	private static String ASENTAMIENTO_SERVICE_IMPL = MODULO + "BalAsentamientoServiceHbmImpl";
	private static String DELEGADOR_SERVICE_IMPL = MODULO + "BalDelegadorServiceHbmImpl";
	private static String SALDOAFAVOR_SERVICE_IMPL = MODULO + "BalSaldoAFavorServiceHbmImpl";
	private static String CLASIFICACION_SERVICE_IMPL = MODULO + "BalClasificacionServiceHbmImpl";
	private static String OTROINGRESOTESORERIA_SERVICE_IMPL = MODULO + "BalOtroIngresoTesoreriaServiceHbmImpl";
	private static String AJUSTE_SERVICE_IMPL = MODULO + "BalBalanceServiceHbmImpl";
	private static String INDET_SERVICE_IMPL = MODULO + "BalIndetServiceHbmImpl";
	private static String ARCHIVOSBANCO_SERVICE_IMPL = MODULO + "BalArchivosBancoServiceHbmImpl";
	private static String FOLIOTESORERIA_SERVICE_IMPL = MODULO + "BalFolioTesoreriaServiceHbmImpl";
	private static String COMPENSACION_SERVICE_IMPL = MODULO + "BalCompensacionServiceHbmImpl";
	private static String ENVIOOSIRIS_SERVICE_IMPL = MODULO + "BalEnvioOsirisServiceHbmImpl";
	private static String CONCILIACIONOSIRIS_SERVICE_IMPL = MODULO + "BalConciliacionOsirisServiceHbmImpl";

	// Instancia
	public static final BalServiceLocator INSTANCE = new BalServiceLocator();


	private IBalDefinicionService   	definicionServiceHbmImpl;
	private IBalReclamoService 			reclamoServiceHbmImpl;
	private IBalDistribucionService 	distribucionServiceHbmImpl;
	private IBalAsentamientoService		asentamientoServiceHbmImpl;
	private IBalDelegadorService		delegadorServiceHbmImpl;
	private IBalSaldoAFavorService		saldoAFavorServiceHbmImpl;
	private IBalClasificacionService	clasificacionServiceHbmImpl;
	private IBalOtroIngresoTesoreriaService otroIngresoTesoreriaServiceHbmImpl;
    private IBalBalanceService	balanceServiceHbmImpl;
    private IBalIndetService indetServiceHbmImpl = null;
    private IBalArchivosBancoService	archivosBancoServiceHbmImpl;
    private IBalFolioTesoreriaService	folioTesoreriaServiceHbmImpl;
    private IBalCompensacionService	compensacionServiceHbmImpl;
    private IBalEnvioOsirisService envioOsirisServiceHbmlImpl;
    private IBalConciliacionOsirisService conciliacionOsirisServiceHbmlImpl;
    
	// Constructor de instancia
	public BalServiceLocator() {
		try {

			this.definicionServiceHbmImpl = (IBalDefinicionService)   Class.forName(DEFINICION_SERVICE_IMPL).newInstance();			
			this.reclamoServiceHbmImpl = (IBalReclamoService) Class.forName(RECLAMO_SERVICE_IMPL).newInstance();
			this.distribucionServiceHbmImpl = (IBalDistribucionService) Class.forName(DISTRIBUCION_SERVICE_IMPL).newInstance();
			this.asentamientoServiceHbmImpl = (IBalAsentamientoService) Class.forName(ASENTAMIENTO_SERVICE_IMPL).newInstance();
			this.delegadorServiceHbmImpl = (IBalDelegadorService) Class.forName(DELEGADOR_SERVICE_IMPL).newInstance();
			this.saldoAFavorServiceHbmImpl = (IBalSaldoAFavorService) Class.forName(SALDOAFAVOR_SERVICE_IMPL).newInstance();
			this.clasificacionServiceHbmImpl = (IBalClasificacionService) Class.forName(CLASIFICACION_SERVICE_IMPL).newInstance();
			this.otroIngresoTesoreriaServiceHbmImpl = (IBalOtroIngresoTesoreriaService) Class.forName(OTROINGRESOTESORERIA_SERVICE_IMPL).newInstance();
		    this.balanceServiceHbmImpl = (IBalBalanceService) Class.forName(AJUSTE_SERVICE_IMPL).newInstance();
		    this.indetServiceHbmImpl = (IBalIndetService) Class.forName(INDET_SERVICE_IMPL).newInstance();
			this.archivosBancoServiceHbmImpl = (IBalArchivosBancoService) Class.forName(ARCHIVOSBANCO_SERVICE_IMPL).newInstance();
			this.folioTesoreriaServiceHbmImpl = (IBalFolioTesoreriaService) Class.forName(FOLIOTESORERIA_SERVICE_IMPL).newInstance();
			this.compensacionServiceHbmImpl = (IBalCompensacionService) Class.forName(COMPENSACION_SERVICE_IMPL).newInstance();
			this.envioOsirisServiceHbmlImpl = (IBalEnvioOsirisService) Class.forName(ENVIOOSIRIS_SERVICE_IMPL).newInstance();
			this.conciliacionOsirisServiceHbmlImpl = (IBalConciliacionOsirisService) Class.forName(CONCILIACIONOSIRIS_SERVICE_IMPL).newInstance();
		} catch (Exception e) {
			log.error("No se pudo crear la clase" + e);
		}
	}

	public static IBalDefinicionService getDefinicionService(){
		return INSTANCE.definicionServiceHbmImpl;		
	}
	
	public static IBalReclamoService getReclamoService(){
		return INSTANCE.reclamoServiceHbmImpl;
	}
	
	public static IBalDistribucionService getDistribucionService(){
		return INSTANCE.distribucionServiceHbmImpl;
	}
	
	public static IBalAsentamientoService getAsentamientoService(){
		return INSTANCE.asentamientoServiceHbmImpl;
	}
	
	public static IBalDelegadorService getDelegadorService(){
		return INSTANCE.delegadorServiceHbmImpl;
	}
	
	public static IBalSaldoAFavorService getSaldoAFavorService(){
		return INSTANCE.saldoAFavorServiceHbmImpl;
	}
	

	public static IBalClasificacionService getClasificacionService(){
		return INSTANCE.clasificacionServiceHbmImpl;
	}
	
	public static IBalBalanceService getBalanceService(){
		return INSTANCE.balanceServiceHbmImpl;
	}
	
	public static IBalOtroIngresoTesoreriaService getOtroIngresoTesoreriaService(){
		return INSTANCE.otroIngresoTesoreriaServiceHbmImpl;
	}

	public static IBalIndetService getIndetService() {
		return INSTANCE.indetServiceHbmImpl;
	}

	public static IBalArchivosBancoService getArchivosBancoService(){
		return INSTANCE.archivosBancoServiceHbmImpl;
	}
	
	public static IBalFolioTesoreriaService getFolioTesoreriaService(){
		return INSTANCE.folioTesoreriaServiceHbmImpl;
	}
	
	public static IBalCompensacionService getCompensacionService(){
		return INSTANCE.compensacionServiceHbmImpl;
	}
	
	public static IBalEnvioOsirisService getEnvioOsirisService(){
		return INSTANCE.envioOsirisServiceHbmlImpl;
	}
	
	public static IBalConciliacionOsirisService getConciliacionOsirisService(){
		return INSTANCE.conciliacionOsirisServiceHbmlImpl;
	}

}


