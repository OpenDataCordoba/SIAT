//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.grs.page;

/**
 * 
 * @author Andrei
 *
 */
class PageException extends Exception {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -4209573219468049730L;
	String error;

	public PageException() {
		super();
		error = "";
	}

	public PageException(String err) {
		super(err); 
		error = err;
	}
	
    /**
     */
    public PageException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     */
    public PageException(Throwable cause) {
        super(cause);
    }
    
	public String getError() {
		return error;
	}
}
