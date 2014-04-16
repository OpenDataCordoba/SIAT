//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.base.view.util;

import coop.tecso.demoda.iface.model.DemodaUserSession;

public class UserSession extends DemodaUserSession {
	private static final long serialVersionUID = -1597116720963043531L;
	
	private String idMenuSession="1";
	
	private String idRecurso;

	//private boolean canAccessSolicitudes = false;
	private boolean canAccessSolEmitidasMenu = false;
	private boolean canAccessSolPendMenu = false;
	
	public String getIdMenuSession() {
		return idMenuSession;
	}

	public void setIdMenuSession(String idMenuSession) {
		this.idMenuSession = idMenuSession;
	}


	public boolean getCanAccessSolEmitidasMenu() {
		return canAccessSolEmitidasMenu;
	}

	public void setCanAccessSolEmitidasMenu(boolean canAccessSolEmitidasMenu) {
		this.canAccessSolEmitidasMenu = canAccessSolEmitidasMenu;
	}

	public boolean getCanAccessSolPendMenu() {
		return canAccessSolPendMenu;
	}

	public void setCanAccessSolPendMenu(boolean canAccessSolPendMenu) {
		this.canAccessSolPendMenu = canAccessSolPendMenu;
	}

	/**
	 * @return the idRecurso
	 */
	public String getIdRecurso() {
		return idRecurso;
	}

	/**
	 * @param idRecurso the idRecurso to set
	 */
	public void setIdRecurso(String idRecurso) {
		this.idRecurso = idRecurso;
	}

	
	
}