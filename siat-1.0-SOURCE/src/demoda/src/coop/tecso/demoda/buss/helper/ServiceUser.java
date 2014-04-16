//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>


package coop.tecso.demoda.buss.helper;


public class ServiceUser {

	private static ThreadLocal usr = new ThreadLocal();
	
	public void setUsr(String usuario) {
		usr.set(usuario);
	}
	
	public String getUsr() {
		return (String)usr.get();
	}
	
	}
