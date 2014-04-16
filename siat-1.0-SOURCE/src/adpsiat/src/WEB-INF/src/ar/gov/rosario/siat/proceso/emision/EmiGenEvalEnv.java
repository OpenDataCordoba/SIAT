//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.emision;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.exe.buss.bean.CueExeCache;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;

/**
 * Entorno de Evaluacion para los procesos de  
 * Emision Generica  
 * */
public class EmiGenEvalEnv {
	
	private static Logger log = Logger.getLogger(EmiGenEvalEnv.class);
	
	// Cache de Cuenta-CueExe
	private CueExeCache cueExeCache; 

	public boolean initialize(CueExeCache cueExeCache, Map<String, String> atrEmi) {
		try {
			
			//ScriptEngineManager mgr = new ScriptEngineManager();
			//ScriptEngine engine		= mgr.getEngineByName("JavaScript");

			InputStream codeStream  = new FileInputStream("codePath"); 
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	public void eval (Cuenta cuenta) {
		// Obtenemos los atributos con getCuentaDefinitionValue
		// y armamos un mapa
		// Clonar los atrEmi
		// Cargar jsCuenta
		// Cargar jsExencion
		// Cargar jsMatriz
		// TODO : Ver vencimientos
	}
	

	
	
}