//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

/**
 *  Enumeracion para aclaracion del caracter en que una persona presenta Declaracion Jurada por Osiris (AFIP)
 * 
 * @author tecso
 *
 */
public enum EnCaracterDeAfip implements IDemodaEmun {

	SELECCIONAR(-1, StringUtil.SELECT_OPCION_SELECCIONAR),
    ADMINISTRADOR(1, "Administrador"), 
    APODERADO(2, "Apoderado"),
    DIRECTOR(3, "Director"),
    GERENTE(4, "Gerente"),
    PRESIDENTE(5, "Presidente"),
    SOCIO(6, "Socio"),
    SOCIO_GERENTE(7, "Socio - Gerente"),
    TITULAR(8, "Titular");
    
    private Integer id;
    private String value;

    private EnCaracterDeAfip(final Integer id, final String value) {
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
    	return "EnCaracterDeAfip: [" + id + ", " + value + "]";
	}
	
    public static EnCaracterDeAfip getById(Integer _id) {
       EnCaracterDeAfip[] enCaracterDeAfip = EnCaracterDeAfip.values();
	   for (int i = 0; i < enCaracterDeAfip.length; i++) {
		   EnCaracterDeAfip tipo = enCaracterDeAfip[i];
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
    
    public static List<EnCaracterDeAfip> getList(){
    	
    	List<EnCaracterDeAfip> listEnCaracterDeAfip = new ArrayList<EnCaracterDeAfip>();
 	   
 	   //Obtengo la lista de EnCaracterDeAfip
 	   EnCaracterDeAfip[] enCaracterDeAfip = EnCaracterDeAfip.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < enCaracterDeAfip.length; i++) {
 	       	if (enCaracterDeAfip[i].getId() != null &&
 	       		enCaracterDeAfip[i].getId() >= 0)
 	       	listEnCaracterDeAfip.add(enCaracterDeAfip[i]);
 	   }
        return listEnCaracterDeAfip;
    }
    
  	

}
