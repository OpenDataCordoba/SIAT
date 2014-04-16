//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.pad.buss.bean.CriRepCat;
import coop.tecso.demoda.iface.model.Estado;

public class CriRepCatDAO extends GenericDAO {

	public CriRepCatDAO(){
		super(CriRepCat.class);
	}


	/**
	 * Obtiene la lista de Todos los criterios activos aplicando los filtros: recurso, fecha vigencia
	 * @param  recurso
	 * @param  fecha
	 * @return List<CriRepCat>
	 */
	public List<CriRepCat> getListActivosByRecursoFecha(Long idRecurso, Date fecha){

		String queryString = "SELECT cri "; 
		queryString += "FROM Repartidor rep, CriRepCat cri WHERE ";
		queryString += "rep.id = cri.repartidor.id AND ";
		queryString += "rep.recurso.id = :recurso AND ";
		queryString += "rep.estado = :estActivo AND  ";
		queryString += "cri.estado = :estActivo AND ";
		queryString += "cri.fechaDesde <= :fecha AND ";
		queryString += "(cri.fechaHasta IS NULL OR cri.fechaHasta > :fecha )";

		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).
			setLong("recurso", idRecurso).
			setDate("fecha", fecha).
			setInteger("estActivo", Estado.ACTIVO.getId());
		
		return (List<CriRepCat>) query.list();	
	}

}
