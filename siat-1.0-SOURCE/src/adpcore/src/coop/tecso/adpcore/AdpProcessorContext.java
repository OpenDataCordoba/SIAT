//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore;

/**
 * Contexto de ejecucion para los threads
 * procesadores de los workers multi-threaded
 */
public abstract class AdpProcessorContext {

	private AdpRun adpRun;

	// Getters y Setters
	public AdpRun getAdpRun() {
		return adpRun;
	}

	public void setAdpRun(AdpRun adpRun) {
		this.adpRun = adpRun;
	}
}
