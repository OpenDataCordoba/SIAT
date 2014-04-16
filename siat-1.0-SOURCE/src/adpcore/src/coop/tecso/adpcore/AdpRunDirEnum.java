//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore;

/**
 * Nombre de los subdirectorios de Corrida
 * @author Tecso Coop. Ltda.
 *
 */
public enum AdpRunDirEnum {
	BASE("adpInputDirBase", ""), /**directorio base (directorio input del proceso)*/
	RECIBIDO("adpInputDirRecibido", "recibido"),
	PROCESANDO("adpInputDirProcesando", "procesando"),
	PROCESADO_OK("adpInputDirProcesadoOk", "procesado_ok"),
	PROCESADO_ERROR("adpInputDirProcesadoError", "procesado_error"),
	ENTRADA("adpInputDirEntrada", "entrada"),
	SALIDA("adpOutputDirSalida", "salida"),
	LOGS("adpOutPutDirLogs", "logs");

	private String codAtributo;
	private String dirName;
	
	AdpRunDirEnum(String codAtr, String dirName) {
		this.codAtributo = codAtr;
		this.dirName = dirName;
	}
	
	public String codAtributo() { return this.codAtributo; }
	public String dirName() { return this.dirName; }
}
