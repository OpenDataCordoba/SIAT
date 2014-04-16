//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

/**
 *  Enumeracion para los Propietarios. Datos de Declaracion Jurada por Osiris (AFIP)
 * 
 * @author tecso
 *
 */
public enum PropietarioAfip implements IDemodaEmun {

	SELECCIONAR(-1, StringUtil.SELECT_OPCION_SELECCIONAR),
    EMPRESA_FISCAL(1, "Empresa Fiscal"), 
    EMPRESA_CASA_CENTRAL(2, "Empresa Casa Central"),
    FIRMANTE(3, "Firmante"),
    LOCAL(4, "Local"),
    SOCIO(5, "Socio");
	
    private Integer id;
    private String value;

    private PropietarioAfip(final Integer id, final String value) {
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
    	return "PropietarioAfip: [" + id + ", " + value + "]";
	}
	
    public static PropietarioAfip getById(Integer _id) {
       PropietarioAfip[] propietarioAfip = PropietarioAfip.values();
	   for (int i = 0; i < propietarioAfip.length; i++) {
		   PropietarioAfip tipo = propietarioAfip[i];
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
    
    public static List<PropietarioAfip> getList(){
    	
    	List<PropietarioAfip> listPropietarioAfip = new ArrayList<PropietarioAfip>();
 	   
 	   //Obtengo la lista de PropietarioAfip
 	   PropietarioAfip[] propietarioAfip = PropietarioAfip.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < propietarioAfip.length; i++) {
 	       	if (propietarioAfip[i].getId() != null &&
 	       		propietarioAfip[i].getId() >= 0)
 	       	listPropietarioAfip.add(propietarioAfip[i]);
 	   }
        return listPropietarioAfip;
    }
    
  	

}
