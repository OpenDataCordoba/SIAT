//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.gde;

import ar.gov.rosario.siat.pad.buss.bean.ExentaAreaCache;
import coop.tecso.adpcore.AdpProcessorContext;
import coop.tecso.demoda.iface.helper.SpooledWriter;


/**
 * Contexto de ejecucion para los threads
 * procesadores del Proceso de Prescripcion
 * de Deuda
 */
public class PreDeuProContext extends AdpProcessorContext {

	private Long idProPreDeu;

	private SpooledWriter spool;

	// Archivos de salida del Paso 1
	private String	 pathPresFile;

	private String pathNoPresFile;
	
	private ExentaAreaCache exentaAreaCache;

	//Constructor
	public PreDeuProContext() {
	}
	
	// Getters y Setters
	public Long getIdProPreDeu() {
		return idProPreDeu;
	}

	public void setIdProPreDeu(Long idProPreDeu) {
		this.idProPreDeu = idProPreDeu;
	}

	public SpooledWriter getSpool() {
		return spool;
	}

	public void setSpool(SpooledWriter spool) {
		this.spool = spool;
	}

	public String getPathPresFile() {
		return pathPresFile;
	}

	public void setPathPresFile(String pathPresFile) {
		this.pathPresFile = pathPresFile;
	}

	public String getPathNoPresFile() {
		return pathNoPresFile;
	}

	public void setPathNoPresFile(String pathNoPresFile) {
		this.pathNoPresFile = pathNoPresFile;
	}

	public ExentaAreaCache getExentaAreaCache() {
		return exentaAreaCache;
	}

	public void setExentaAreaCache(ExentaAreaCache exentaAreaCache) {
		this.exentaAreaCache = exentaAreaCache;
	}
	
}
