//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.def.buss.bean.SituacionInmueble;

public class SituacionInmuebleDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(SituacionInmuebleDAO.class);	
	
	public SituacionInmuebleDAO() {
		super(SituacionInmueble.class);
	}
	
}
