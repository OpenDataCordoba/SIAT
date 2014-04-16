//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.cyq.buss.bean.TipoProceso;

public class TipoProcesoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TipoProcesoDAO.class);	
	
	public TipoProcesoDAO() {
		super(TipoProceso.class);
	}
	
	public List<TipoProceso> getList4Conversion(TipoProceso tipoProceso) throws Exception {

		Session session = SiatHibernateUtil.currentSession();
		
		List<TipoProceso> listTipoProceso = new ArrayList<TipoProceso>();
			
		if (tipoProceso != null && tipoProceso.getTipo() != null){

			String query= "FROM TipoProceso tipoProceso WHERE tipoProceso.tipo = ";
			
			if (tipoProceso.getTipo().equals("C")){
				query+= "'Q'";
			} else if (tipoProceso.getTipo().equals("Q")){
				query+= "'C'";
			}

			log.debug(query);
			
			listTipoProceso = session.createQuery(query).list();
		
		}
		
		return listTipoProceso;
	}
	
}
