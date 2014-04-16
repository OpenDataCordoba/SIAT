//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.CuentaRel;
import coop.tecso.demoda.iface.helper.DateUtil;

public class CuentaRelDAO extends GenericDAO {
	
	public CuentaRelDAO() {
		super(CuentaRel.class);
	}

	 /**
	  * Obtiene la Relacion de Cuenta Asociada al la Cuenta Origen y Destino indicadas.
	  * @param cuentaOrigen , cuentaDestino
	  * @return	CuentaRel. Si no la encuentra null
	  * @throws Exception
	  */
	 public CuentaRel getByOrigenYDestino(Cuenta cuentaOrigen, Cuenta cuentaDestino) throws Exception {
		 	
		 	CuentaRel cuentaRel;
			
		    String queryString = "from CuentaRel t where t.cuentaOrigen.id = " + cuentaOrigen.getId() + 
		    					 " and t.cuentaDestino.id = " + cuentaDestino.getId();
			
			Session session = SiatHibernateUtil.currentSession();

			Query query = session.createQuery(queryString);
			cuentaRel = (CuentaRel) query.uniqueResult();	

			return cuentaRel; 
	 }
	 
	 public List<CuentaRel> ListCuentaRelByCuenta(Cuenta cuenta) throws Exception {
		 	
		    String queryString = "from CuentaRel t where (t.cuentaOrigen.id = " + cuenta.getId() + 
		    					 " or t.cuentaDestino.id = " + cuenta.getId() + ")";
		    queryString += " and (t.fechaHasta IS NULL OR t.fechaHasta  >= TO_DATE('" 
		    					+ DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y'))";
		    queryString += " order by t.cuentaOrigen.id";
			
			Session session = SiatHibernateUtil.currentSession();

			Query query = session.createQuery(queryString);
			return  (ArrayList<CuentaRel>) query.list();
	}
	 
	 public List<CuentaRel> ListCuentaRelByCuentaOrigen(Cuenta cuenta) throws Exception {
		 	
		    String queryString = "from CuentaRel t where t.cuentaOrigen.id = " + cuenta.getId();
		  		   queryString += " order by t.fechaDesde";
			
			Session session = SiatHibernateUtil.currentSession();

			Query query = session.createQuery(queryString);
			return  (ArrayList<CuentaRel>) query.list();
	}
}
