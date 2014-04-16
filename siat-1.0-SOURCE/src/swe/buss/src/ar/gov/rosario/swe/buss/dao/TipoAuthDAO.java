//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.classic.Session;

import ar.gov.rosario.swe.buss.bean.TipoAuth;


public class TipoAuthDAO extends GenericDAO {		
	
	public TipoAuthDAO() {
		super(TipoAuth.class);
	}	

	
	public List<TipoAuth> getListTipoAuth() throws Exception {
        Session session = SweHibernateUtil.currentSession();

	    String consulta = "SELECT descripcion " +
	    				  "FROM TipoAuth";

	
	    TipoAuth tipoAuth = new TipoAuth();
	    long myID = 40;
	    
	    tipoAuth.setId(myID);
	    tipoAuth.setDescripcion("Test");
	    
	        
	    List<TipoAuth> listTipoAuth = new ArrayList<TipoAuth>(); 
	    
	    listTipoAuth.add(tipoAuth);
	    
		/*List<TipoAuth> listTipoAuth = (ArrayList<TipoAuth>) session.createQuery(consulta)
		  .setEntity("tipoAuth",tipoAuth)
		  //.setInteger("estadoActivo",Estado.ACTIVO.getId())
		  .list();*/
    
	    return listTipoAuth;
        
    }
    
	
	
}

