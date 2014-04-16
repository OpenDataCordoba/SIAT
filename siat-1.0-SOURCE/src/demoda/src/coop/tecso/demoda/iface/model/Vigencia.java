//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

public enum Vigencia implements IDemodaEmun {

    VIGENTE(1, "Vigente"), 
    NOVIGENTE(0, "No vigente"),
    CREADO(-1,"Creado"),
    OpcionNula(null, "No Posee");
    
    private Integer id;
    private String value;

    private Vigencia(final Integer id, final String value) {
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
    	return "Estado: [" + id + ", " + value + "]";
	}
	
    public static Vigencia getById(Integer _id) {
	   Vigencia[] estados = Vigencia.values();
	   for (int i = 0; i < estados.length; i++) {
		   Vigencia est = estados[i];
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
 	    if (id < 0)
 	    	return null;
 	    else
 	    	return id;
    }
    
    public static List<Vigencia> getList(){
    	
    	List<Vigencia> listEstado = new ArrayList<Vigencia>();
 	   
 	   //Obtengo la lista de Estado
 	   Vigencia[] estados = Vigencia.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < estados.length; i++) {
 	       	if (estados[i].getId() != null &&
 	       		estados[i].getId() >= 0)
 	       	listEstado.add(estados[i]);
 	   }
        return listEstado;
    }
    
    public static boolean getEsValido(Integer id){
 	   return (Vigencia.VIGENTE.id == id  || Vigencia.NOVIGENTE.id == id || Vigencia.CREADO.id == id );
    }

    public boolean getEsVigente(){
 	   return this.id == VIGENTE.getId();
    }
}
