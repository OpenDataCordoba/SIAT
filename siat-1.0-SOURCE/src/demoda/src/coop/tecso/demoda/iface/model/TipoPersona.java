//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

public enum TipoPersona implements IDemodaEmun {

	SELECCIONAR(-1, StringUtil.SELECT_OPCION_SELECCIONAR),
    FISICA(1, "F\u00EDsica"), 
    JURIDICA(2, "Jur\u00EDdica");
    
    private Integer id;
    private String value;

    private TipoPersona(final Integer id, final String value) {
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
    	return "TipoPersona: [" + id + ", " + value + "]";
	}
	
    public static TipoPersona getById(Integer _id) {
       TipoPersona[] tiposPersona = TipoPersona.values();
	   for (int i = 0; i < tiposPersona.length; i++) {
		   TipoPersona tipo = tiposPersona[i];
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
    
    public static List<TipoPersona> getList(){
    	
    	List<TipoPersona> listTipoPersona = new ArrayList<TipoPersona>();
 	   
 	   //Obtengo la lista de TipoPersona
 	   TipoPersona[] tiposPersona = TipoPersona.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < tiposPersona.length; i++) {
 	       	if (tiposPersona[i].getId() != null &&
 	       		tiposPersona[i].getId() >= 0)
 	       	listTipoPersona.add(tiposPersona[i]);
 	   }
        return listTipoPersona;
    }
    
    public static boolean getEsValido(Integer id){
 	   return (TipoPersona.FISICA.id == id  || TipoPersona.JURIDICA.id == id );
    }

    public boolean getEsFisica(){
  	   return this.id.intValue() == FISICA.getId().intValue();
     }	

}
