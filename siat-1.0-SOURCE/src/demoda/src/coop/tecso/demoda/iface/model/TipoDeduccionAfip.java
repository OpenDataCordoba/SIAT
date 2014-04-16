//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

/**
 *  Enumeracion para los Tipos de Deducción en datos de Declaracion Jurada por Osiris (AFIP)
 * 
 * @author tecso
 *
 */
public enum TipoDeduccionAfip implements IDemodaEmun {

	SELECCIONAR(-1, StringUtil.SELECT_OPCION_SELECCIONAR),
    PERCEPCION(1, "Percepción"), 
    RETENCION(2, "Retención");
	
    private Integer id;
    private String value;

    private TipoDeduccionAfip(final Integer id, final String value) {
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
    	return "TipoDeduccionAfip: [" + id + ", " + value + "]";
	}
	
    public static TipoDeduccionAfip getById(Integer _id) {
       TipoDeduccionAfip[] tipoDeduccionAfip = TipoDeduccionAfip.values();
	   for (int i = 0; i < tipoDeduccionAfip.length; i++) {
		   TipoDeduccionAfip tipo = tipoDeduccionAfip[i];
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
    
    public static List<TipoDeduccionAfip> getList(){
    	
    	List<TipoDeduccionAfip> listTipoDeduccionAfip = new ArrayList<TipoDeduccionAfip>();
 	   
 	   //Obtengo la lista de TipoDeduccionAfip
 	   TipoDeduccionAfip[] tipoDeduccionAfip = TipoDeduccionAfip.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < tipoDeduccionAfip.length; i++) {
 	       	if (tipoDeduccionAfip[i].getId() != null &&
 	       		tipoDeduccionAfip[i].getId() >= 0)
 	       	listTipoDeduccionAfip.add(tipoDeduccionAfip[i]);
 	   }
        return listTipoDeduccionAfip;
    }
    
  	

}
