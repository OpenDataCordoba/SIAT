//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

/**
 *  Enumeracion para el Tratamiento de Actividades en Declaracion Jurada por Osiris (AFIP)
 * 
 * @author tecso
 *
 */
public enum TratamientoActividadAfip implements IDemodaEmun {

	SELECCIONAR(-1, StringUtil.SELECT_OPCION_SELECCIONAR),
    NO_EXENTA(1, "No Exenta"), 
    EXENTA(2, "Exenta"),
    DOBLE_TRATAMIENTO(3, "Doble Tratamiento");
    
	
    private Integer id;
    private String value;

    private TratamientoActividadAfip(final Integer id, final String value) {
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
    	return "TratamientoActividadAfip: [" + id + ", " + value + "]";
	}
	
    public static TratamientoActividadAfip getById(Integer _id) {
       TratamientoActividadAfip[] tratamientoActividadAfip = TratamientoActividadAfip.values();
	   for (int i = 0; i < tratamientoActividadAfip.length; i++) {
		   TratamientoActividadAfip tipo = tratamientoActividadAfip[i];
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
    
    public static List<TratamientoActividadAfip> getList(){
    	
    	List<TratamientoActividadAfip> listTratamientoActividadAfip = new ArrayList<TratamientoActividadAfip>();
 	   
 	   //Obtengo la lista de TratamientoActividadAfip
 	   TratamientoActividadAfip[] tratamientoActividadAfip = TratamientoActividadAfip.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < tratamientoActividadAfip.length; i++) {
 	       	if (tratamientoActividadAfip[i].getId() != null &&
 	       		tratamientoActividadAfip[i].getId() >= 0)
 	       	listTratamientoActividadAfip.add(tratamientoActividadAfip[i]);
 	   }
        return listTratamientoActividadAfip;
    }
    
  	

}
