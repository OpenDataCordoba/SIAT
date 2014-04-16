//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface;

public class DemodaServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public DemodaServiceException() {
		super();
	}

	public DemodaServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public DemodaServiceException(String message) {
		super(message);
	}

	public DemodaServiceException(Throwable cause) {
		super(cause);
	}
}
