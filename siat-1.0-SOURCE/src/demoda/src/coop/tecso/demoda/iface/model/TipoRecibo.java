//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

public enum TipoRecibo implements IDemodaEmun {

	SELECCIONAR(-1, StringUtil.SELECT_OPCION_SELECCIONAR),
    RECIBODEUDA(0, "Recibo Deuda"), 
    RECIBOCUOTA(1, "Recibo Cuota"),
    OpcionNula(null, "No Posee");
    
    private Integer id;
    private String value;

    private TipoRecibo(final Integer id, final String value) {
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
    	return "TipoRecibo: [" + id + ", " + value + "]";
	}
	
    public static TipoRecibo getById(Integer _id) {
       TipoRecibo[] tiposRecibo = TipoRecibo.values();
	   for (int i = 0; i < tiposRecibo.length; i++) {
		   TipoRecibo tipo = tiposRecibo[i];
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
    
    public static List<TipoRecibo> getList(){
    	
    	List<TipoRecibo> listTipoRecibo = new ArrayList<TipoRecibo>();
 	   
 	   //Obtengo la lista de TipoRecibo
 	   TipoRecibo[] tiposRecibo = TipoRecibo.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < tiposRecibo.length; i++) {
 	       	if (tiposRecibo[i].getId() != null &&
 	       		tiposRecibo[i].getId() >= 0)
 	       	listTipoRecibo.add(tiposRecibo[i]);
 	   }
        return listTipoRecibo;
    }
    
    public static boolean getEsValido(Integer id){
 	   return (TipoRecibo.RECIBODEUDA.id == id  || TipoRecibo.RECIBOCUOTA.id == id );
    }


}
