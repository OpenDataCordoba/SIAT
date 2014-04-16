//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

/**
 *  Enumeracion para los tipos de pagos. Otros Pagos en detalle de Declaracion Jurada por Osiris (AFIP)
 * 
 * @author tecso
 *
 */
public enum TipoPagoAfip implements IDemodaEmun {

	SELECCIONAR(-1, StringUtil.SELECT_OPCION_SELECCIONAR),
    CAMBIO_DE_COEFICIENTE(1, "Cambio de Coeficiente"),
    POR_RESOLUCION(2, "Por resolución"),
    PAGO_EN_DEFECTO(3, "Pago en defecto"),
    RESTO_PERIODO_ANTERIOR(4, "Resto período anterior");
    
    private Integer id;
    private String value;

    private TipoPagoAfip(final Integer id, final String value) {
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
    	return "TipoPagoAfip: [" + id + ", " + value + "]";
	}
	
    public static TipoPagoAfip getById(Integer _id) {
       TipoPagoAfip[] tipoPagoAfip = TipoPagoAfip.values();
	   for (int i = 0; i < tipoPagoAfip.length; i++) {
		   TipoPagoAfip tipo = tipoPagoAfip[i];
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
     * @return el numero del TipoPago.
     */
    public Integer getBussId(){
 	    if (id < 0)
 	    	return null;
 	    else
 	    	return id;
    }
    
    public static List<TipoPagoAfip> getList(){
    	
    	List<TipoPagoAfip> listTipoPagoAfip = new ArrayList<TipoPagoAfip>();
 	   
 	   //Obtengo la lista de TipoPagoAfip
 	   TipoPagoAfip[] tipoPagoAfip = TipoPagoAfip.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < tipoPagoAfip.length; i++) {
 	       	if (tipoPagoAfip[i].getId() != null &&
 	       		tipoPagoAfip[i].getId() >= 0)
 	       	listTipoPagoAfip.add(tipoPagoAfip[i]);
 	   }
        return listTipoPagoAfip;
    }
    
  	

}
