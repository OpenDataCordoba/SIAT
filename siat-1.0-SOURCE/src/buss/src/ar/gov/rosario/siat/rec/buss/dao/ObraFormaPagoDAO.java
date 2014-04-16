//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.rec.buss.bean.Obra;
import ar.gov.rosario.siat.rec.buss.bean.ObraFormaPago;

public class ObraFormaPagoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ObraFormaPagoDAO.class);	
	
	public ObraFormaPagoDAO() {
		super(ObraFormaPago.class);
	}
	
	/** Recupera todas las obraFormaPago, filtradas
	 *  por una determinada obra, ordenas en forma
	 *  descendente por cantidad de cuotas, sin tener
	 *  en cuenta las que tengan alguna exencion
	 * 
	 * @param obra
	 * @return
	 */
	public List<ObraFormaPago> getListByObraDes(Obra obra) {
		Session session = SiatHibernateUtil.currentSession();
    	String sQuery = "FROM ObraFormaPago OFP WHERE OFP.obra = :obra AND OFP.exencion IS NULL " +
    					"ORDER BY OFP.cantCuotas DESC";
    	
    	Query query = session.createQuery(sQuery).setEntity("obra", obra);
    	return (ArrayList<ObraFormaPago>) query.list();		
	}
	
	
	
	
}
