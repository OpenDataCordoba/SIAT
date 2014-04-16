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
public interface PageTag {

	// Commands
	public abstract String tag();
	public abstract String content();
	public abstract String end();

	public abstract String all();
}
