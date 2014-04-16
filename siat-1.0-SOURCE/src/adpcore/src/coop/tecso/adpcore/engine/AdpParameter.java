//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore.engine;

/**
 * Parametro de corrida de ADP
 * @author Tecso Coop. Ltda.
 *
 */
public class AdpParameter {

	/* Parametros standar de ADP. El nombre debe comenzar "adp" */
	public static final String INPUTFILEPATH = "adpInputFilePath";
	
	private Long idAtributo = 0L;
	//private String codParVal = "";
	private Long idProcesoParVal = 0L;
	private Long idProcesoAtrVal = 0L;
	private String key = "";
	private String description = "";
	private String value = "";
	private boolean isTemporal = false;
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the idAtributo
	 */
	public Long getIdAtributo() {
		return idAtributo;
	}
	/**
	 * @param idAtributo the idAtributo to set
	 */
	public void setIdAtributo(Long idAtributo) {
		this.idAtributo = idAtributo;
	}
	/**
	 * @return the idProcesoAtrVal
	 */
	public Long getIdProcesoAtrVal() {
		return idProcesoAtrVal;
	}
	/**
	 * @param idProcesoAtrVal the idProcesoAtrVal to set
	 */
	public void setIdProcesoAtrVal(Long idProcesoAtrVal) {
		this.idProcesoAtrVal = idProcesoAtrVal;
	}
	/**
	 * @return the idProcesoParVal
	 */
	public Long getIdProcesoParVal() {
		return idProcesoParVal;
	}
	/**
	 * @param idProcesoParVal the idProcesoParVal to set
	 */
	public void setIdProcesoParVal(Long idProcesoParVal) {
		this.idProcesoParVal = idProcesoParVal;
	}
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	public static boolean isAdp(String key) {
		return key.startsWith("adp");
	}
	//public String getCodParVal() {
	//	return codParVal;
	//}
	//public void setCodParVal(String codParVal) {
	//	this.codParVal = codParVal;
	//}
	public boolean isTemporal() {
		return isTemporal;
	}
	public void setTemporal(boolean isTemporal) {
		this.isTemporal = isTemporal;
	}
}
