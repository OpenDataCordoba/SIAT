//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.List;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.gde.buss.bean.SelAlm;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmPlanes;
import ar.gov.rosario.siat.gde.iface.model.RescateAdapter;

public class SelAlmDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(SelAlmDAO.class);	
	
	public SelAlmDAO() {
		super(SelAlm.class);
	}
	
	public List<Long> getListIdConvenioSelAlmByRescate (RescateAdapter rescateAdapter,SelAlmPlanes selAlmPlanes)throws Exception{
		
		String queryString ="SELECT s.idElemento FROM SelAlmDet s WHERE s.selAlm.id = "+ selAlmPlanes.getId();
		queryString += " ORDER BY s.idElemento";
		
		List<Long> listIdConvenio = (List<Long>)executeCountedQuery(queryString, rescateAdapter);
		
		return listIdConvenio;
	}
}
