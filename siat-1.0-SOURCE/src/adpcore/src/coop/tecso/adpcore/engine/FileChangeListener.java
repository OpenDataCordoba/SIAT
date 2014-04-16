//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore.engine;

import java.io.File;

/**
 * 
 * @author Tecso Coop. Ltda.
 */
public interface FileChangeListener {
	public void fileModified(File f);
	public void fileRemoved(File f);
} 

