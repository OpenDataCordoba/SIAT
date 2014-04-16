//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.base.iface.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class SiatParam {

	private static Logger log = Logger.getLogger(SiatParam.class);
	private static SiatParam INSTANCE = null;
	private Map<String, String> values = new HashMap<String, String>();
	private Context context;

	public static String TIMEOUT_ANONIMO = "TimeoutAnonimo";
	public static String TIMEOUT_AUTENTICADO = "TimeoutAutenticado";
	public static String FILE_SHARE_PATH = "FileSharePath";
	public static String GENERAL_DB_NAME = "GeneralDbName";
	
	public static String MAIL_SERVER ="MailServer";
	public static String OBTENERENVIO_ENVIOID = "ObtenerEnvio:envio_id";
	public static String PROCESARENVIO_ENVIOID = "ProcesarEnvio:envio_id";
	
	// Decimales a mostrar en la vista
	public static int DEC_IMPORTE_VIEW = 2;
	
	
	// Decimales utilizados en los calculos 
	public static int DEC_IMPORTE_CALC = 6;
	public static int DEC_PORCENTAJE_CALC = 6;
	public static int DEC_INDICE_CALC = 7;
	
	//  Decimales utilizados para guardar en la db
	public static int DEC_IMPORTE_DB = 2;
	public static int DEC_PORCENTAJE_DB = 6;
	public static int DEC_INDICE_DB = 7;
	
	public static String BASE_URL ="BaseURL";
	
	// Parametro para valor de porcentaje de deuda inicial a emitir en Habilitaciones
	public static String HAB_EMI_PORC ="HabEmiPorcParam";
	
	// Parametros con valores de inicio del Regimen Simplificado para Drei/Etur. El Web Service solo acepta altas al regimen desde el periodo indicado por estos parametros.
	public static String MES_INICIO_RS ="MesInicioRS";
	public static String ANIO_INICIO_RS ="AnioInicioRS";

	// Parametros con lista de Rubros no permitidos para la adhesion al regimen simplificado.
	public static String RUBROS_NO_PERMITIDOS_EN_RS ="RubrosNoPermitidosEnRS";
	
	// Codigos de Rubros de Habilitaciones para Comerio. Usados para determinar la distribucion de partidas de Drei.
	public static String RUBRO_BINGO_DREI ="RubroBingoDrei";
	public static String LISTA_RUBROS_DREI ="ListaRubrosDrei";
	
	// Lista de ids de Recursos sobre los cuales de restringirá la reconfección/reimpresión y declaración jurada.
	public static String LISTA_ID_RECURSO ="ListaIdRecurso";

	// Lista de ids de Recursos sobre los cuales no se solicita el codigo de gestion personal para acceder a la cuenta desde la web.
	public static String LISTA_ID_REC_SIN_COD_GES_PER ="ListaIdRecSinCodGesPer";

	// Parametros con valores de fecha desde la cual se debe limitar la Declaración Jurada por Siat y la reconfección/reimpresión para periodos fiscales DREI/ETUR
	public static String MES_LIMITACION_RG ="MesLimitacionRG";
	public static String ANIO_LIMITACION_RG ="AnioLimitacionRG";

	// Lista de ids de Clasificacion Deuda a incluir en el Proceso de PAS/Debito
	public static String LISTA_ID_CLADEU_FOR_PAS_DEBITO ="ListaIdClaDeuForPasDebito";

	public static final String VERIFICA_PAGO_OSIRIS = "VerificaPagoOsiris";

	private SiatParam() {
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public synchronized static SiatParam getInstance()  throws Exception {
		if (INSTANCE == null) {
			INSTANCE = new SiatParam();
		}
		return INSTANCE;
	}
	
	/**
	 * Busca key en los valores cargados en SiatParam
	 * Si no lo encuetra los busca en context.xml agregando prefijo: /siat/conf/
	 * Si no lo encuentra retorna null
	 * Si encuetra en alguno de los dos, retorna el valor que primero encuentra.
	 * @param key
	 * @return
	 */
	protected synchronized String getValue(String key) {
		String value = null;
		
		value = values.get(key);
		if (value != null)
			return value;
		
		// si es null, lo buscamos en los paramtros de arquitectura del context.xml
		try {
			String prefix = "java:comp/env/siat/conf/";			
			if (context == null) {
				context = new InitialContext();
			}
			value = (String) context.lookup(prefix + key);
			return value;
		} catch (Exception e) {}
		
		return null;
	}

	public synchronized void updateValues(Map<String, String> m) {		
		this.values.clear();
		this.values.putAll(m); 
	} 
	
	static public String getString(String key) throws Exception {
		String ret = SiatParam.getInstance().getValue(key);
		if (ret == null) throw new IllegalArgumentException("No se encontro el parametro: '" + key + "' en SiatParam y tampoco en context.xml");
		return ret;
	}

	static public String getString(String key, String def) {
		String ret = null;
		try { ret = getString(key); } catch (Exception e) {}
		return ret == null ? def : ret;
	}
	
	static public int getInteger(String key) throws Exception {
		String ret = SiatParam.getInstance().getValue(key);
		if (ret == null) throw new IllegalArgumentException("No se encontro el parametro: '" + key + "' en SiatParam y tampoco en context.xml");
		return Integer.parseInt(ret);
	}

	static public int getInteger(String key, int def) {
		String ret = null;
		try { ret = getString(key); } catch (Exception e) {}
		return ret == null ? def : Integer.parseInt(ret);
	}

	static public double getDouble(String key) throws Exception {
		String ret = SiatParam.getInstance().getValue(key);
		if (ret == null) throw new IllegalArgumentException("No se encontro el parametro: '" + key + "' en SiatParam y tampoco en context.xml");
		return Double.parseDouble(ret);
	}

	static public double getDouble(String key, double def) {
		String ret = null;
		try { ret = getString(key); } catch (Exception e) {}
		return ret == null ? def : Double.parseDouble(ret);
	}

	/**
	 * Obtiene el path formado por: fileSharePath + key 
	 * @param  key
	 * @return String
	 * @throws Exception
	 */
	static public String getPath(String key) throws Exception {
		String fileSharePath = SiatParam.getInstance().getValue(SiatParam.FILE_SHARE_PATH);
		if (fileSharePath == null) throw new IllegalArgumentException("No se encontro el parametro: '" + "fileSharePath" + "' en SiatParam");

		String ret = SiatParam.getInstance().getValue(key);
		if (ret == null) throw new IllegalArgumentException("No se encontro el parametro: '" + key + "' en SiatParam");
		
		return fileSharePath + "/" + ret;
	}

	/**
	 * Verifica si se trata de intranerSiat.
	 * @return
	 */
	private static Boolean isIntraSiat = null;
	public synchronized static boolean isIntranetSiat() {
		if (isIntraSiat != null) {
			return isIntraSiat;
		} else {			
			InputStream stream = new SiatParam().getClass().getClassLoader().getResourceAsStream("/intrasiat-key");
			isIntraSiat = (stream == null) ? false : true; 
		}
		return isIntraSiat;
	}

	/**
	 * Verifica si se trata de webSiat.
	 * @return
	 */
	private static Boolean isWebSiat = null;
	public synchronized static boolean isWebSiat() {
		if (isWebSiat != null) {
			return isWebSiat;
		} else { 
			InputStream stream = new SiatParam().getClass().getClassLoader().getResourceAsStream("/websiat-key");
			isWebSiat = (stream == null) ? false : true; 
		}
		return isWebSiat;
	}

	/**
	 * Lanza una exception si NO se trata de intranetSiat
	 * @throws Exception
	 */
	public static void onlyIntranetSiat() throws Exception {
		if (!isIntraSiat) throw new Exception("Funcion solo valida para Intranet Siat.");
	}

	/**
	 * Lanza una exception si NO se trata de webSiat
	 * @throws Exception
	 */
	public static void onlyWebSiat() throws Exception {
		if (!isIntraSiat) throw new Exception("Funcion solo valida para Web Siat.");
	}

	/** Path al directorio de archivos compartidos */
	public static String getFileSharePath() throws Exception {
		return getString(FILE_SHARE_PATH);
	}

	/** Nombre de la base de datos general */
	public static String getGeneralDbName() throws Exception {
		return getString(GENERAL_DB_NAME);
	}

	public static String version() {
		try {
			String version="";
			
			InputStream stream = new SiatParam().getClass().getClassLoader().getResourceAsStream("/version");
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			version = reader.readLine();
			stream.close();
			
			return version;
		} catch (Exception e) {
			return "?";
		}
	}
}
