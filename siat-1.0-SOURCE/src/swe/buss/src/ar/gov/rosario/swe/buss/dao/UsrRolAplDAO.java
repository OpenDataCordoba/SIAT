//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.dao;

import org.apache.log4j.Logger;

import ar.gov.rosario.swe.buss.bean.UsrRolApl;



public class UsrRolAplDAO extends GenericDAO {

	private Logger log = Logger.getLogger(getClass());
	
	public UsrRolAplDAO() {
		super(UsrRolApl.class);
	}


}
