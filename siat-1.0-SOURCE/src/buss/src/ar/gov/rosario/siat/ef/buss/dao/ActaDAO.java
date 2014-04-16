//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.Acta;
import ar.gov.rosario.siat.ef.buss.bean.OrdenControl;

public class ActaDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(ActaDAO.class);

	public static String NAME_SEQUENCE = "ef_actaProcedimiento_sq";
	
	public ActaDAO() {
		super(Acta.class);
	}
	
	public Long getNextVal(){
		return super.getNextVal(NAME_SEQUENCE);
	}
	
	 public Acta getByOrdenControlTipoActa(Long idOrdenControl, Long tipoActa)  {
		    
		 Acta acta;
		 
		 String queryString = "from Acta t where t.ordenControl.id = " + idOrdenControl + 
	    					 " and t.tipoActa.id = " + tipoActa;
	    					 
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		
		query.setMaxResults(1);
		acta = (Acta) query.uniqueResult();	
		

		return acta; 
	} 
	 
	public Long getUltNroActa (OrdenControl ordenControl){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "SELECT MAX(numeroActa) FROM Acta a WHERE a.ordenControl.id = "+ ordenControl.getId();
		
		Query query = session.createQuery(queryString);
		
		Long ultNro=(Long)query.uniqueResult();
		if (ultNro==null)
			ultNro=0L;
		
		return ultNro;
		
	}

}
