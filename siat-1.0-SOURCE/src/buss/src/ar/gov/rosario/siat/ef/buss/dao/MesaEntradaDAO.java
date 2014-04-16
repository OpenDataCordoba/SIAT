//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.MesaEntrada;

public class MesaEntradaDAO extends GenericDAO {

	//	private Log log = LogFactory.getLog(MesaEntradaDAO.class);
	
	public MesaEntradaDAO() {
		super(MesaEntrada.class);
	}
	
	/**
	 * Obtiene un MesaEntrada por su codigo
	 */
	public MesaEntrada getByCodigo(String codigo) throws Exception {
		MesaEntrada mesaEntrada;
		String queryString = "from MesaEntrada t where t.codMesaEntrada = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		mesaEntrada = (MesaEntrada) query.uniqueResult();	

		return mesaEntrada; 
	}
	
}
