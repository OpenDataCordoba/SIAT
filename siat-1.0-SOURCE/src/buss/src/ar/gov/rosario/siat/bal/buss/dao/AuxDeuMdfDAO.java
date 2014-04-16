//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.bal.buss.bean.AuxDeuMdf;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.model.Estado;

public class AuxDeuMdfDAO extends GenericDAO {

	public AuxDeuMdfDAO(){
		super(AuxDeuMdf.class);
	}
	
	/**
	 * Elimina los registros de AuxDeuMdf que corresponden al Asentamiento
	 * 
	 * @param asentamiento
	 * @return int
	 */
	public int deleteAllByAsentamiento (Asentamiento asentamiento){

		String queryString = "delete from AuxDeuMdf t ";
			   queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
	
	/**
	 * Obtiene el registro de Deuda Auxiliar para determinado Asentamiento y Deuda.
	 * 
	 * @param idDeuda
	 * @param idAsentamiento
	 * @param idTransaccion
	 * @return
	 */
	public AuxDeuMdf getByIdDeudaYAse(Long idDeuda, Long idAsentamiento, Long idTransaccion) {
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from AuxDeuMdf t ";
	
	    // Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
		queryString += " and t.asentamiento.id = " + idAsentamiento;
		queryString += " and t.transaccion.id = " + idTransaccion;
		queryString += " and t.idDeuda = " + idDeuda;
		
		Query query = session.createQuery(queryString);
		
		List<AuxDeuMdf> listAuxDeuMdf = (ArrayList<AuxDeuMdf>) query.list();
		if(!ListUtil.isNullOrEmpty(listAuxDeuMdf)){
			return listAuxDeuMdf.get(0);
		}else{
			return null;
		}
	}
	
	
	/**
	 * Obtiene el registro de Deuda Auxiliar para una deuda determinada.
	 * 
	 * @param idDeuda
	 * @return
	 */
	public AuxDeuMdf getByIdDeuda(Long idDeuda) {
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from AuxDeuMdf t ";
	
	    // Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
		queryString += " and t.idDeuda = " + idDeuda;
		
		Query query = session.createQuery(queryString);
		
		List<AuxDeuMdf> listAuxDeuMdf = (ArrayList<AuxDeuMdf>) query.list();
		if(!ListUtil.isNullOrEmpty(listAuxDeuMdf)){
			return listAuxDeuMdf.get(0);
		}else{
			return null;
		}
	}
}
