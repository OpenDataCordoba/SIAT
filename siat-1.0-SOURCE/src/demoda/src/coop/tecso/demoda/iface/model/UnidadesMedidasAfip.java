//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

/**
 *  Enumeracion para los tipos de unidades de medidas utilizadas en las Declaraciones de Actividades por Local. Datos de Declaracion Jurada por Osiris (Afip).
 * 
 * @author tecso
 *
 */
public enum UnidadesMedidasAfip implements IDemodaEmun {

	SELECCIONAR(-1, StringUtil.SELECT_OPCION_SELECCIONAR),
	DESCONOCIDA(0, "Desconocida"), 
	M2(1, "m2"), 
    M2_UTILES(2, "m2 utiles"),
    UNIDADES(3, "Unidades"),
    M2_COCHERA(4, "m2 cochera"),
    M2_PLAYA(5, "m2 playa");
    
    private Integer id;
    private String value;

    private UnidadesMedidasAfip(final Integer id, final String value) {
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
    	return "UnidadesMedidasAfip: [" + id + ", " + value + "]";
	}
	
    public static UnidadesMedidasAfip getById(Integer _id) {
       UnidadesMedidasAfip[] unidadesMedidasAfip = UnidadesMedidasAfip.values();
	   for (int i = 0; i < unidadesMedidasAfip.length; i++) {
		   UnidadesMedidasAfip tipo = unidadesMedidasAfip[i];
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
    
    public static List<UnidadesMedidasAfip> getList(){
    	
    	List<UnidadesMedidasAfip> listUnidadesMedidasAfip = new ArrayList<UnidadesMedidasAfip>();
 	   
 	   //Obtengo la lista de UnidadesMedidasAfip
 	   UnidadesMedidasAfip[] unidadesMedidasAfip = UnidadesMedidasAfip.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < unidadesMedidasAfip.length; i++) {
 	       	if (unidadesMedidasAfip[i].getId() != null &&
 	       		unidadesMedidasAfip[i].getId() >= 0)
 	       	listUnidadesMedidasAfip.add(unidadesMedidasAfip[i]);
 	   }
        return listUnidadesMedidasAfip;
    }
    
  	

}
