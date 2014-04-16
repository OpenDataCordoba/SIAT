//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.pad.buss.bean.TipoDomicilio;

public class TipoDomicilioDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TipoDomicilioDAO.class);	
	
	public TipoDomicilioDAO() {
		super(TipoDomicilio.class);
	}

}
