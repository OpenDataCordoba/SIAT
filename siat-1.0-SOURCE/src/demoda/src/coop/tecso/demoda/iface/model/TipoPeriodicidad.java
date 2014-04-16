//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

public enum TipoPeriodicidad implements IDemodaEmun {

	SELECCIONAR(-1, StringUtil.SELECT_OPCION_SELECCIONAR),
    MENSUAL(0, "Mensual"), 
    ANUAL(1, "Anual");
    
    private Integer id;
    private String value;

    private TipoPeriodicidad(final Integer id, final String value) {
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
    	return "TipoPeriodicidad: [" + id + ", " + value + "]";
	}
	
    public static TipoPeriodicidad getById(Integer _id) {
    	TipoPeriodicidad[] tiposPeriodicidad = TipoPeriodicidad.values();
	   for (int i = 0; i < tiposPeriodicidad.length; i++) {
		   TipoPeriodicidad tipo = tiposPeriodicidad[i];
  		   if (_id == null){
  			   if (tipo.id == null){
  				   return tipo;
  			   }  				   
  		   } else {
			   if (tipo.id.intValue() == _id.intValue()){
				   return tipo;
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
    
    public static List<TipoPeriodicidad> getList(){
    	
    	List<TipoPeriodicidad> listTipoPeridiocidad = new ArrayList<TipoPeriodicidad>();
 	   
 	   //Obtengo la lista de TipoPersona
    	TipoPeriodicidad[] tiposPeriodicidad = TipoPeriodicidad.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < tiposPeriodicidad.length; i++) {
 	       	if (tiposPeriodicidad[i].getId() != null &&
 	       		tiposPeriodicidad[i].getId() >= 0)
 	       	listTipoPeridiocidad.add(tiposPeriodicidad[i]);
 	   }
        return listTipoPeridiocidad;
    }
    
    public static boolean getEsValido(Integer id){
 	   return (TipoPeriodicidad.ANUAL.id == id  || TipoPeriodicidad.MENSUAL.id == id );
    }

}
