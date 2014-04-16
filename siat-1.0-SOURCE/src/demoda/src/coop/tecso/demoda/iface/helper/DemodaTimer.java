//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.helper;

public class DemodaTimer {
	private long timeStart = 0L;
	
	public DemodaTimer() {
		timeStart = System.currentTimeMillis();
	}

	public void reset() {
		timeStart = System.currentTimeMillis();
	}
	
	public long stop() {
		long time = System.currentTimeMillis() - timeStart; 
		reset();
		return time;
	}

	public String stop(String msg) {
		return "DemodaTimer(ms):" + msg + ":" + stop();
	}

}
