//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.io.Serializable;

import coop.tecso.demoda.iface.error.DemodaError;

/**
 * Representa la instancia de un Mensage de error con su numero, clave y parametros si los tuviese.
 * Dificilmente usted tenga que instaciar esta clase.
 * <p>Para manejar los errores utilizar los metodos 
 * del Common (addRecovreableError y addNonRecoverableError, etc...)
*/
public class DemodaStringMsg implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private long number;
	private String key;
	private Object[] params = null;
	private String rawkey;

	public DemodaStringMsg() {
		this.number = 0;
		this.key = "";
		this.params = new Object[0];
		this.rawkey = "";
	}
	
	public DemodaStringMsg(String errorConst) {
		this.number = 0;
		this.key = DemodaError.errorKey(errorConst);
		this.params = new Object[0];
		this.rawkey = errorConst;
	}

	public DemodaStringMsg(String errorConst, Object[] params) {
		this.number = 0;
		this.key = DemodaError.errorKey(errorConst);
		this.params = params;
		this.rawkey = errorConst;
	}

	public DemodaStringMsg(long number, String key) {
		this.number = number;
		this.key = key;
		this.params = new Object[0];
		this.rawkey = key;
	}

	public DemodaStringMsg(long number, String key, Object[] params) {
		this.number = number;
		this.key = key;
		this.params = params;
		this.rawkey = key;
	}
	
	public boolean isValue() {
		boolean isValue = false;
		
		if (this.key.startsWith("&")) {
			isValue = true;
		}
		
		return isValue;
	}
	
	public boolean isKey() {
		return !this.isValue();
	}

	public long number() { return number; }
	public String key() { return key; }
	public Object[] params() { return params; }
	public String rawkey() { return rawkey; }

}

