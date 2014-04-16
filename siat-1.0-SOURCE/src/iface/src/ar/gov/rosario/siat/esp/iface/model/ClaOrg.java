//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.model.IDemodaEmun;

public enum ClaOrg implements IDemodaEmun {

    HABITUAL(1, "Habitual"), 
    ESPORADICO(0, "Esporádico"),
    OpcionNula(null, "No Posee");
    
    private Integer id;
    private String value;

    private ClaOrg(final Integer id, final String value) {
        this.id = id;
        this.value = value;
    }
    
    public Integer getId() {
		return id;
	}

    public String getValue() {
		return value;
	}

    public String toString() {
    	return "ClaOrg: [" + id + ", " + value + "]";
	}
	
    public static ClaOrg getById(Integer _id) {
	   ClaOrg[] estados = ClaOrg.values();
	   for (int i = 0; i < estados.length; i++) {
		   ClaOrg est = estados[i];
  		   if (_id == null){
  			   if (est.id == null){
  				   return est;
  			   }  				   
  		   } else {
			   if (est.id.intValue() == _id.intValue()){
				   return est;
			   }
  		   }
		}
		   return null;
	}
    
    /**
     * Devuelve un Id valido para persistir, en un create o update.
     *
     * @return el numero del SiNo.
     */
    public Integer getBussId(){
 	    if (id == null || id < 0)
 	    	return null;
 	    else
 	    	return id;
    }
    
    public static List<ClaOrg> getList(){
    	
    	List<ClaOrg> listClaOrg = new ArrayList<ClaOrg>();
 	   
 	   //Obtengo la lista de ClaOrg
 	   ClaOrg[] estados = ClaOrg.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < estados.length; i++) {
 	       	if (estados[i].getId() != null &&
 	       		estados[i].getId() >= 0)
 	       	listClaOrg.add(estados[i]);
 	   }
        return listClaOrg;
    }
    
    public static boolean getEsValido(Integer id){
 	   return (ClaOrg.HABITUAL.id == id  || ClaOrg.ESPORADICO.id == id);
    }

    public boolean getEsHabitual(){
 	   return this.id == HABITUAL.getId();
    }
    
    public boolean getEsEsporadico(){
  	   return this.id == ESPORADICO.getId();
    }
}
