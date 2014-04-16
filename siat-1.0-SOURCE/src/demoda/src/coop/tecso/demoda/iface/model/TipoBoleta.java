//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

public enum TipoBoleta implements IDemodaEmun {

	SELECCIONAR( 0, StringUtil.SELECT_OPCION_SELECCIONAR),
    TIPODEUDA(1,"Deuda"), 
    TIPORECIBO(2, "Recibo"),
    TIPOCUOTA(3, "Cuota"),
    TIPORECIBOCUOTA(4, "Recibo Cuota"),
    TIPOSELLADO(5, "Sellado"),
    NULO(null," ");
   
    
    private Integer id;
    private String value;
    

    private TipoBoleta(final Integer id, final String value) {
    	 this.id = id;
         this.value = value;
    }
    
 
    public Integer getId() {
		return id;
	}

    public String getValue() {
		return value;
	}

    public String getCodValue() {
		return id + "-" + value;
	}

    public String toString() {
    	return "TipoEstadoCueExe: [" + id + ", " + value + "]";
	}
    
    public static TipoBoleta getById(Integer _id) {
    	TipoBoleta[] tipoBoletas = TipoBoleta.values();
	   for (int i = 0; i < tipoBoletas.length; i++) {
		   TipoBoleta tipoBoleta = tipoBoletas[i];
  		   if (_id == null){
  			   if (tipoBoleta.id == null){
  				   return tipoBoleta;
  			   }  				   
  		   } else {
			   if (tipoBoleta.id.intValue() == _id.intValue()){
				   return tipoBoleta;
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
    
    public static List<TipoBoleta> getList(){
    	
    	List<TipoBoleta> listTipoBoleta = new ArrayList<TipoBoleta>();
 	   
 	   //Obtengo la lista de tipoBoleta
    	TipoBoleta[] tipoBoleta = TipoBoleta.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < tipoBoleta.length; i++) {
 	       	if (tipoBoleta[i].getId() != null &&
 	       		tipoBoleta[i].getId() >= 0)
 	       	listTipoBoleta.add(tipoBoleta[i]);
 	   }
        return listTipoBoleta;
    }
    
    public static List<TipoBoleta> getListEstadosValidos(){
    	
    	List<TipoBoleta> listTipoBoleta = new ArrayList<TipoBoleta>();
 	   
 	   //Obtengo la lista de TipoBoleta
    	TipoBoleta[] tipoBoleta = TipoBoleta.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < 4; i++) {
 	       	if (tipoBoleta[i].getId() != null &&
 	       		tipoBoleta[i].getId() >= 0)
 	       	listTipoBoleta.add(tipoBoleta[i]);
 	   }
        return listTipoBoleta;
    }
    
    public static boolean getEsValido(Integer id){
 	   return (TipoBoleta.TIPODEUDA.id == id  || TipoBoleta.TIPOCUOTA.id == id ||TipoBoleta.TIPORECIBO.id == id ||TipoBoleta.TIPORECIBOCUOTA.id == id  );
    }

	

}
