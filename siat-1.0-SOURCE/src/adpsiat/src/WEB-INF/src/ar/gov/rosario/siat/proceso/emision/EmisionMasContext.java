//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.emision;

import java.util.List;

import ar.gov.rosario.siat.exe.buss.bean.CueExeCache;
import coop.tecso.adpcore.AdpProcessorContext;
import coop.tecso.demoda.iface.helper.SpooledWriter;


/**
 * Contexto de ejecucion para los threads
 * procesadores de la Emision Masiva de 
 * Deuda
 * 
 * @author Tecso Coop. Lmtda.
 */
public class EmisionMasContext extends AdpProcessorContext {

	private CueExeCache cueExeCache;

	private SpooledWriter spool;
	
	private List<String> listAtributos;
	
	// Constructor
	public EmisionMasContext() {
	}

	public CueExeCache getCueExeCache() {
		return cueExeCache;
	}

	public void setCueExeCache(CueExeCache cueExeCache) {
		this.cueExeCache = cueExeCache;
	}

	public SpooledWriter getSpool() {
		return spool;
	}

	public void setSpool(SpooledWriter spool) {
		this.spool = spool;
	}

	public List<String> getListAtributos() {
		return listAtributos;
	}

	public void setListAtributos(List<String> listAtributos) {
		this.listAtributos = listAtributos;
	}
}
