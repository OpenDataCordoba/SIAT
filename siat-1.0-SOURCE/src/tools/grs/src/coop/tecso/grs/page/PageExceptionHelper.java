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
public class PageExceptionHelper {

	private static void checkRequiredField(String type,String field, String value) throws PageException {
		if (!"".equals(value) && value!=null){
			System.out.println("OK");
		}
		else
			throw new PageException("Error: Required Field");
	}

}