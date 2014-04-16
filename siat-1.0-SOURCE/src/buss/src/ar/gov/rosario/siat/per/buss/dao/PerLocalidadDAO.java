//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.per.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.pad.iface.model.LocalidadSearchPage;
import ar.gov.rosario.siat.per.buss.bean.PerLocalidad;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class PerLocalidadDAO extends GenericDAO {

	public PerLocalidadDAO() {
		super(PerLocalidad.class);
	}	

	/**
	 * Obtiene una Localidad por su codigo
	 */	
	public PerLocalidad getByCodPostal(Long codPostal) throws Exception {
		if (codPostal == null)
			return null;
		
		PerLocalidad localidad;
		String queryString = "from PerLocalidad t where t.codPostal = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codPostal.toString());
		//TODO:actualmente hay codigos repetidos, por eso se ultiliza la segunda sentencia
		localidad = (PerLocalidad) query.setMaxResults(1).list().get(0);

		return localidad; 
	}
	
	
	/**
	 * Obtiene un Localidad por su nombre exacto
	 */	
	public PerLocalidad getByExactName(String nomLocalidad) throws Exception {
		PerLocalidad localidad;
		String queryString = "from PerLocalidad t where t.descripcion = :localidad";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("localidad", nomLocalidad);
		localidad = (PerLocalidad) query.uniqueResult();	

		return localidad; 
	}
	
	public List<PerLocalidad> getBySearchPage(LocalidadSearchPage localidadSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		
		String queryString = "from PerLocalidad t ";
	    boolean flagAnd = false;

		//Si id = -1 se listan todos
		if (!ModelUtil.isNullOrEmpty(localidadSearchPage.getLocalidad().getProvincia())) {
			  queryString += flagAnd ? " and " : " where ";
		      queryString += " t.provincia.id = "+ localidadSearchPage.getLocalidad().getProvincia().getId();
		      flagAnd = true;
		}
		
		// Armamos filtros del HQL
		if (localidadSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}		
		
		// filtro por codigo postal
 		if (localidadSearchPage.getLocalidad().getCodPostal() != null) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codPostal)) like '%" + 
				StringUtil.escaparUpper(localidadSearchPage.getLocalidad().getCodPostal().toString()) + "%'";
			flagAnd = true;
		}
 		
 		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(localidadSearchPage.getLocalidad().getDescripcionPostal())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) like '%" + 
				StringUtil.escaparUpper(localidadSearchPage.getLocalidad().getDescripcionPostal()) + "%'";
			flagAnd = true;
		} 			
		 		
		List<PerLocalidad> listLocalidad = (ArrayList<PerLocalidad>) executeCountedSearch(queryString, localidadSearchPage);
		
		return listLocalidad;
	}

}
