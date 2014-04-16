//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

/**
 *  Enumeracion para los tipos de acance etur en datos de Declaracion Jurada por Osiris (AFIP)
 * 
 * @author tecso
 *
 */
public enum AlcanceEturAfip implements IDemodaEmun {

	DESCONOCIDO(-1, "Desconocido"),
    NO_ALCANZADA(0, "No Alcanzada"), 
    BARES_REST_PIZZ(1, "Bares, restaurantes y Pizzerías"),
    AGEVIATUR_CASASCAMBIO_ENTFINAN(2, "Agencias de Viajes y Turismo, Casas de Cambio y Entidades Financieras"),
    CAS_CAB_CAF_ESP_NIG_CLU_BIN_BARNOC(3, "Casinos, Cabarets, Café, Espectáculos, Night Clubs, Bingos y Bares Nocturnos"),
    CONF_BAI(4, "Confiterías Bailables"),
    HOT_HOS(3, "Hoteles y Hospedajes");
	
    private Integer id;
    private String value;

    private AlcanceEturAfip(final Integer id, final String value) {
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
    	return "AlcanceEturAfip: [" + id + ", " + value + "]";
	}
	
    public static AlcanceEturAfip getById(Integer _id) {
       AlcanceEturAfip[] alcanceEturAfip = AlcanceEturAfip.values();
	   for (int i = 0; i < alcanceEturAfip.length; i++) {
		   AlcanceEturAfip tipo = alcanceEturAfip[i];
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
    
    public static List<AlcanceEturAfip> getList(){
    	
    	List<AlcanceEturAfip> listAlcanceEturAfip = new ArrayList<AlcanceEturAfip>();
 	   
 	   //Obtengo la lista de AlcanceEturAfip
 	   AlcanceEturAfip[] alcanceEturAfip = AlcanceEturAfip.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < alcanceEturAfip.length; i++) {
 	       	if (alcanceEturAfip[i].getId() != null &&
 	       		alcanceEturAfip[i].getId() >= 0)
 	       	listAlcanceEturAfip.add(alcanceEturAfip[i]);
 	   }
        return listAlcanceEturAfip;
    }
    
  	

}
