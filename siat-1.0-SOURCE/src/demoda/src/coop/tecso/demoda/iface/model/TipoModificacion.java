//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

public enum TipoModificacion implements IDemodaEmun {

	SELECCIONAR(-1, StringUtil.SELECT_OPCION_SELECCIONAR),
    CUIT(1, "Cuit"), 
    DENOMINACION(2, "Denominaci\u00F3n");
    
    private Integer id;
    private String value;

    private TipoModificacion(final Integer id, final String value) {
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
    	return "TipoModificacion: [" + id + ", " + value + "]";
	}
	
    public static TipoModificacion getById(Integer _id) {
       TipoModificacion[] tiposModificacion = TipoModificacion.values();
	   for (int i = 0; i < tiposModificacion.length; i++) {
		   TipoModificacion tipo = tiposModificacion[i];
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
    
    public static List<TipoModificacion> getList(){
    	
    	List<TipoModificacion> listTipoModificacion = new ArrayList<TipoModificacion>();
 	   
 	   //Obtengo la lista de TipoModificacion
 	   TipoModificacion[] tiposModificacion = TipoModificacion.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < tiposModificacion.length; i++) {
 	       	if (tiposModificacion[i].getId() != null &&
 	       		tiposModificacion[i].getId() >= 0)
 	       	listTipoModificacion.add(tiposModificacion[i]);
 	   }
        return listTipoModificacion;
    }
    
    public static boolean getEsValido(Integer id){
 	   return (TipoModificacion.CUIT.id == id  || TipoModificacion.DENOMINACION.id == id );
    }

    public boolean getEsCuit(){
 	   return this.id.intValue() == CUIT.getId().intValue();
    }
}
