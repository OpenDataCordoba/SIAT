//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

/**
 *  Enumeracion para los Tipos de Documentos. Datos de Declaracion Jurada por Osiris (AFIP)
 * 
 * @author tecso
 *
 */
public enum TipoDocumentoAfip implements IDemodaEmun {

	SELECCIONAR(-1, StringUtil.SELECT_OPCION_SELECCIONAR),
    DNI(1, "DNI"), 
    CEDULA(2, "Cédula de Identidad"),
    PASAPORTE(3, "Pasaporte"),
    LC_LE(4, "L.C./L.E.");
	
    private Integer id;
    private String value;

    private TipoDocumentoAfip(final Integer id, final String value) {
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
    	return "TipoDocumentoAfip: [" + id + ", " + value + "]";
	}
	
    public static TipoDocumentoAfip getById(Integer _id) {
       TipoDocumentoAfip[] tipoDocumentoAfip = TipoDocumentoAfip.values();
	   for (int i = 0; i < tipoDocumentoAfip.length; i++) {
		   TipoDocumentoAfip tipo = tipoDocumentoAfip[i];
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
    
    public static List<TipoDocumentoAfip> getList(){
    	
    	List<TipoDocumentoAfip> listTipoDocumentoAfip = new ArrayList<TipoDocumentoAfip>();
 	   
 	   //Obtengo la lista de TipoDocumentoAfip
 	   TipoDocumentoAfip[] tipoDocumentoAfip = TipoDocumentoAfip.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < tipoDocumentoAfip.length; i++) {
 	       	if (tipoDocumentoAfip[i].getId() != null &&
 	       		tipoDocumentoAfip[i].getId() >= 0)
 	       	listTipoDocumentoAfip.add(tipoDocumentoAfip[i]);
 	   }
        return listTipoDocumentoAfip;
    }
    
  	

}
