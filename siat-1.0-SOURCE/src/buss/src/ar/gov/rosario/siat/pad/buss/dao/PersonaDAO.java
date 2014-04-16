//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.iface.model.PersonaSearchPage;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.model.Sexo;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class PersonaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PersonaDAO.class);
	
	public PersonaDAO() {
		super(Persona.class);
	}
	
	/**
	 * Obtiene un Persona por su codigo
	 */
//	public Persona getByCodigo(String codigo) throws Exception {
//		Persona Persona;
//		String queryString = "from Persona t where t.codPersona = :codigo";
//		Session session = SatHibernateUtil.currentSession();
//
//		Query query = session.createQuery(queryString).setString("codigo", codigo);
//		Persona = (Persona) query.uniqueResult();	
//
//		return Persona; 
//	}
	

	
	public List<Persona> getBySearchPage(PersonaSearchPage personaSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Persona t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del PersonaSearchPage: " + personaSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (personaSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		//Determino si una persona Fisica o Juridica para armar el filtro
		if (personaSearchPage.getPersona().getTipoPersona().equals(PersonaVO.FISICA)) {
			// filtro Apellido
			if (!StringUtil.isNullOrEmpty(personaSearchPage.getPersona().getApellido())){
				queryString += flagAnd ? " and " : " where ";
				queryString += " UPPER(TRIM(t.apellido)) like '%" + 
					StringUtil.escaparUpper(personaSearchPage.getPersona().getApellido()) + "%'";
				flagAnd = true;
			}
			// filtro Nombre
			if (!StringUtil.isNullOrEmpty(personaSearchPage.getPersona().getNombres())){
				queryString += flagAnd ? " and " : " where ";
				queryString += " UPPER(TRIM(t.nombres)) like '%" + 
					StringUtil.escaparUpper(personaSearchPage.getPersona().getNombres()) + "%'";
				flagAnd = true;
			}		
			// filtro Tipo Documento
			if (!ModelUtil.isNullOrEmpty(personaSearchPage.getPersona().getDocumento().getTipoDocumento())) {
				queryString += flagAnd ? " and " : " where ";
				queryString += " t.documento.tipoDocumento.id = " +  personaSearchPage.getPersona().getDocumento().getTipoDocumento().getId();
				flagAnd = true;
			}
			// filtro Documento
			if (personaSearchPage.getPersona().getDocumento().getNumero() != null) {
				queryString += flagAnd ? " and " : " where ";
				queryString += " t.documento.numero = " + 
					personaSearchPage.getPersona().getDocumento().getNumero();
				flagAnd = true;
			}
			// filtro Sexo
			if (!(personaSearchPage.getPersona().getSexo().equals(Sexo.OpcionTodo))){
				queryString += flagAnd ? " and " : " where ";
				queryString += " t.sexo = " + 	personaSearchPage.getPersona().getSexo().getId() ;
				flagAnd = true;
			} 
		} else if (personaSearchPage.getPersona().getTipoPersona().equals(PersonaVO.JURIDICA)) {
			// filtro Razon Social
			if (!StringUtil.isNullOrEmpty(personaSearchPage.getPersona().getRazonSocial())){
				queryString += flagAnd ? " and " : " where ";
				queryString += " UPPER(TRIM(t.razonSocial)) like '%" + 
					StringUtil.escaparUpper(personaSearchPage.getPersona().getRazonSocial()) + "%'";
				flagAnd = true;
			}	
		}
		
		// filtro Cuit
		if (!StringUtil.isNullOrEmpty(personaSearchPage.getPersona().getCuit())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.cuit)) like '%" + 
				StringUtil.escaparUpper(personaSearchPage.getPersona().getCuit()) + "%'";
			flagAnd = true;
		}		
		
 		/*
 		// Order By
		queryString += " order by t.codPersona ";
		*/
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);    
	

		List<Persona> listPersona = (ArrayList<Persona>) executeCountedSearch(queryString, personaSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listPersona;
	}
	
	/**
	 * Realiza la busqueda de personas fisicas y juridicas por el cuit pasado como parametro
	 * @param cuit
	 * @return
	 */
	public List<Persona> getListPersonaByCuit(String cuit){			
		
		String queryString = "from Persona t where t.cuit = :cuit";
		Session session = SiatHibernateUtil.currentSession();

		return session.createQuery(queryString).setString("cuit", cuit).list();		
	}

	
    

}
