//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.NovedadEnvio;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;

public class NovedadEnvioDAO extends GenericDAO {

	private Log log = LogFactory.getLog(NovedadEnvioDAO.class);
	
	public NovedadEnvioDAO() {
		super(NovedadEnvio.class);
	}

	/**
	 * Retorna lista de NovedadEnvio para un CierreBanco y tipo de operacion particular.
	 * 
	 * @param idCierreBanco
	 * @param tipoOperacion
	 * @return
	 */
	public List<NovedadEnvio> getListByCierreBancoAndTipoOperacion(Long idCierreBanco,Long tipoOperacion){ 
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM NovedadEnvio n WHERE n.cierreBanco.id="+idCierreBanco;
			   queryString+= " AND n.tipoOperacion="+tipoOperacion;
		
		Query query = session.createQuery(queryString);
		
		return query.list();
	}
	
}
