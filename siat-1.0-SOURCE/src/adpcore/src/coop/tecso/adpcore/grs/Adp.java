//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore.grs;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.NativeObject;

import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.engine.AdpDao;
import coop.tecso.grs.Grs;
import coop.tecso.grs.GrsEngine;
import coop.tecso.grs.sql.Sql;

/**
 * The Adp Grs bind class
 *
 * @author Andrei
 */
public class Adp implements Serializable{

	private static final long serialVersionUID = 7465229469573837285L;
	
	// Metodos que necesitan que exista al menos una corrida en ADP.

	/**
	 * Changes current run message
	 * @param m String
	 * @throws Exception
	 */
	public static void message(String format, Object...args ) throws Exception{
		AdpRun.changeRunMessage(String.format(format, args), 0);
	}

	/**
	 * Log format in the current run.
	 * @param format String
	 * @param args Object...
	 * @throws Exception
	 */
	public static void log(String format, Object ...args) throws Exception{
		System.out.printf(format + "\n", args);
		if (AdpRun.currentRun() != null) {
			AdpRun.logRun(String.format(format, args));
		}
	}

	/**
	 * Returns the parameters of the current run. 
	 * @return Map<String, String>
	 * @throws Exception
	 */
	public static Map<String, Object> parameters() throws Exception {
		return parameters(null);
	}

	/**
	 * Returns the parameters of the id run. 
	 * @return Map<String, String>
	 * @throws Exception
	 */
	public static Map<String, Object> parameters(Long idrun) throws Exception {
		AdpRun run;
		if (idrun == null) {
			run = AdpRun.currentRun();
		} else {
			run = AdpRun.getRun(idrun);
		}
		if (run == null)
			return null;

		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, String> m = run.getParameters();
		for(String key: m.keySet()){
			String value = m.get(key);
			ret.put(key, Grs.paramToObject(value));
		}
		
		return ret;
	}

	/**
	 * Save JS nativeObject as a parameter of the current run. Uses GrsEngine.jsToJvm() to perform
	 * the transformation between NativeObject & Map if necessary.
	 * @param o
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static void saveParameters(NativeObject o, Long idrun) throws Exception {
		saveParameters((Map<String, Object>) GrsEngine.jsToJvm(o), idrun);
	}

	@SuppressWarnings("unchecked")
	public static void saveParameters(NativeObject o) throws Exception {
		saveParameters((Map<String, Object>) GrsEngine.jsToJvm(o), null);
	}

	/**
	 * Save parameters Map as parameters of the runid.
	 * If runif is null, use the curren run
	 * @param params
	 * @throws Exception
	 */
	public static void saveParameters(Map<String,Object> params, Long idrun) throws Exception {
		AdpRun run;
		if (idrun == null) {
			run = AdpRun.currentRun();
		} else {
			run = AdpRun.getRun(idrun);
		}

		Object value;
		for(String key:params.keySet()){
			value = params.get(key);
			String s = Grs.paramToString(value);
			run.putParameter(key, s);
		}
	}
	
	/**
	 * Si puede (no tiene errores en los buffers), cambia el estado de una tarea 
	 * con todo lo que ello implica. Ademas, si tiene warnings, hace un dump del 
	 * buffer de warnings e informa al usuario.
	 * En caso de no poder, termina en el estado FIN_ERROR.
	 *  
	 * @param state:   int estado al cual pasar la corrida
	 * @param message: mensaje de observacion de la corrida.
	 */
	public static void changeState(int stateId, String message) throws Exception {
		AdpRun.currentRun().changeState(AdpRunState.getById(stateId), message);
	}
	
	/**
	 * Cambia el estado de una tarea con todo lo que ello implica.
	 * @param state int estado al cual pasar la corrida
	 * @param message mensaje de observacion de la corrida. si es null, no lo modifica.
	 * @param changeStep true si se desea que al cambiar el estado tambien se incremente el nuemero de paso
	 */
	public static void changeState(String stateId, String message, Boolean changeStep) throws Exception {
		AdpRun.currentRun().changeState(AdpRunState.getById(Long.valueOf(stateId)), message, changeStep);
	}
	
	public static List<Map<String, Object>> files(Long idrun) throws Exception {
		final String SqlFiles = "select * from pro_filecorrida where idcorrida = #i order by paso, orden, nombre";
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		
		if (idrun == null) {
			AdpRun run = AdpRun.currentRun();
			if (run == null)
				return ret;
			idrun = run.getId();
		}
		
		Grs grs = new Grs();
		try {
			Sql sql = grs.sql(AdpDao.DATASOURCE_CONTEXT);
			ret = sql.list(SqlFiles, idrun);
		} finally {
			grs.close();
		}
		return ret;
	}
	
	/**
	 * Schedulean un reporte para el usuario actual
	 * @param processName
	 * @param message
	 * @param params
	 * @throws Exception
	 */
	public static void scheduleReport(String name, String message, Map<String, Object> params) throws Exception {
		AdpRun run = AdpRun.newRun(name, message);
		run.create();		
		Adp.saveParameters(params, run.getId());

		run.execute(new Date());
		
		// Elimino las corridas y reportes anteriores
		/*
		String username = DemodaUtil.currentUserContext().getUserName();
		List<Long> listIdRun = run.getListOldRunId(username);
		if(listIdRun != null) {
			for(Long id: listIdRun) {
				AdpRun.deleteFiles(id);
			}				
			run.cleanOld(username);
		}
		*/
	}
	
	/**
	 * Construye un nombre de archivo unico para la corrida actual.
	 * El formato es: [prefix]-fecha-NombreProceso-idcorrida.[suffix]
	 */
	public static String makeFileName(String prefix, String suffix) throws Exception {
		String date = String.format("%1$tY-%1$tm-%1$td", new Date());
		return String.format("%s-%s-%s-%s.%s",prefix, date, Adp.processName(), Adp.runId(), suffix);
	}
	
	/**
	 * Construye un nombre de archivo absoluto unico para la corrida actual.
	 * El formato es: segun makeFileName()
	 * Si no esta corriendo en una instancia de Adp, retorna la direccion absoluta de filename.
	 * Si esta en una corrida de Adp, el filename es relativo al directorio del proceso.
	 * Si filename es un path absoluto, retorna el mismo path absoluto. 
	 */
	public static String makeFilePath(String prefix, String suffix) throws Exception {
		String filename = makeFileName(prefix, suffix); 
		String filepath = filename;
		
		if (AdpRun.currentRun() == null) {
			return new File(filename).getAbsolutePath();
		}
		
		File ftmp = new File(filename);
		if (ftmp.isAbsolute()) {
			filepath = ftmp.getAbsolutePath();
		} else {
			filepath = new File(AdpRun.currentRun().getProcessDir(AdpRunDirEnum.BASE), filename).getAbsolutePath();
		}
		
		return filepath;
	}

	/**
	 * Agrega una entrada de archivo en adp y retorna el nombre abosoluto del arhivo.
	 * @param filepath file path absoluto del archivo a agregar
	 * @param title titulo
	 * @param description
	 * @param registros cantida de lineas, tamaño, etc 
	 * @return Una direccion absoluta de nombre de archivo
	 * @throws Exception
	 */
	public static void addFile(String filepath, String title, String description, Long registros) throws Exception {
		Map<String, Object> file = new HashMap<String, Object>();
		AdpRun run = AdpRun.currentRun();
		if (run == null) {
			return;
		}
		
		file.put("idcorrida", run.getId());
		file.put("paso", 1);
		file.put("fileName", filepath);
		file.put("nombre", title);
		file.put("observacion", description);
		file.put("ctdregistros", registros);
		file.put("orden", 1);
		file.put("usuario", run.getUsuario());
		file.put("fechaultmdf", new Date());
		file.put("estado", 1);

		Grs grs = new Grs();
		try {
			grs.sql(AdpDao.DATASOURCE_CONTEXT).save("pro_filecorrida", file);
		} finally {
			grs.close();
		}
	}

	/**
	 * Retorna el nombre del proceso actual o "" si
	 * no se esta ejecutando en Adp 
	 * @return
	 * @throws Exception
	 */
	public static String processName() throws Exception {
		if (AdpRun.currentRun() == null) {
			return "";
		}
		return AdpRun.currentRun().getProcess().getCodProceso();
	}
	
	/**
	 * Retorna el id de corrida actual o 0 si
	 * no se esta ejecutando en Adp 
	 * @return
	 * @throws Exception
	 */
	public static Long runId() throws Exception {
		if (AdpRun.currentRun() == null) {
			return 0L;
		}
		return AdpRun.currentRun().getId();
	}	
}
