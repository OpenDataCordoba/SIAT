//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

/**
 *  Enumeracion para los tipos de Impuestos para transacciones Osiris (AFIP)
 * 
 * @author tecso
 *
 */
public enum ImpuestoAfip implements IDemodaEmun {

	SELECCIONAR(-1, StringUtil.SELECT_OPCION_SELECCIONAR),
    DREI(6050, "Drei"), 
    DREI_MULTAS(6052, "Multas Drei"),
    ETUR(6053, "Etur"),
    ETUR_INTERESES(6054, "Intereses Etur"),
    ETUR_MULTAS(6055, "Multas Etur"),
    RS_DREI(6056, "Drei RS"),
    RS_ETUR(6057, "Etur RS"),
    ACRETA(6058, "ACRETA"),
    DREI_INTERESES(6059, "Intereses Drei"),
    DREI_INT_MULTAS(6060, "Multas Intereses Drei"),
    ETUR_INT_MULTAS(6061, "Multas Intereses Etur");
    
    private Integer id;
    private String value;

    private ImpuestoAfip(final Integer id, final String value) {
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
    	return "ImpuestoAfip: [" + id + ", " + value + "]";
	}
	
    public static ImpuestoAfip getById(Integer _id) {
       ImpuestoAfip[] impuestoAfip = ImpuestoAfip.values();
	   for (int i = 0; i < impuestoAfip.length; i++) {
		   ImpuestoAfip tipo = impuestoAfip[i];
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
    
    public static List<ImpuestoAfip> getList(){
    	
    	List<ImpuestoAfip> listImpuestoAfip = new ArrayList<ImpuestoAfip>();
 	   
 	   //Obtengo la lista de ImpuestoAfip
 	   ImpuestoAfip[] impuestoAfip = ImpuestoAfip.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < impuestoAfip.length; i++) {
 	       	if (impuestoAfip[i].getId() != null &&
 	       		impuestoAfip[i].getId() >= 0)
 	       	listImpuestoAfip.add(impuestoAfip[i]);
 	   }
        return listImpuestoAfip;
    }
    
    public String getFullValue() {
    	if(id != null && id > 0)
    		return id.toString()+" - "+value;
    	else
    		return value;
	}

}
