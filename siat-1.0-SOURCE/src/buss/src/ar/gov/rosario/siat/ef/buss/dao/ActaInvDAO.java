//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.ef.buss.bean.ActaInv;

public class ActaInvDAO extends GenericDAO {

//	private Log log = LogFactory.getLog(ActaInvDAO.class);
	
	private static String SEQUENCE_NRO_ACTA ="ef_acta_sq";
	
	public ActaInvDAO() {
		super(ActaInv.class);
	}


	public Long getNextNroActa() {
		return super.getNextVal(SEQUENCE_NRO_ACTA);
	}
	

}
