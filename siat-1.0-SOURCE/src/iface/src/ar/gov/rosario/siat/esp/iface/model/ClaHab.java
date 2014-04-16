//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.model.IDemodaEmun;

public enum ClaHab implements IDemodaEmun {

    PERMANENTE(1, "Permanente"), 
    OCASIONAL(0, "Ocasional"),
    OpcionNula(null, "No Posee");

    private Integer id;
    private String value;

    private ClaHab(final Integer id, final String value) {
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
    	return "ClaHab: [" + id + ", " + value + "]";
	}
	
    public static ClaHab getById(Integer _id) {
	   ClaHab[] estados = ClaHab.values();
	   for (int i = 0; i < estados.length; i++) {
		   ClaHab est = estados[i];
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
    
    public static List<ClaHab> getList(){
    	
    	List<ClaHab> listClaHab = new ArrayList<ClaHab>();
 	   
 	   //Obtengo la lista de ClaHab
 	   ClaHab[] estados = ClaHab.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < estados.length; i++) {
 	       	if (estados[i].getId() != null &&
 	       		estados[i].getId() >= 0)
 	       	listClaHab.add(estados[i]);
 	   }
        return listClaHab;
    }
    
    public static boolean getEsValido(Integer id){
 	   return (ClaHab.PERMANENTE.id == id  || ClaHab.OCASIONAL.id == id);
    }

    public boolean getEsPermanente(){
 	   return this.id == PERMANENTE.getId();
    }
    
    public boolean getEsOcasional(){
  	   return this.id == OCASIONAL.getId();
    }
}
