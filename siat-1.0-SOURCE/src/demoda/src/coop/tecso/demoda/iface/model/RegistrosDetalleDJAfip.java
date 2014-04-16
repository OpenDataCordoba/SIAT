//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

/**
 *  Enumeracion para los titulos de registros que forman el detalle de declaraciones juradas por Osiris (AFIP)
 * 
 * @author tecso
 *
 */
public enum RegistrosDetalleDJAfip implements IDemodaEmun {

	SELECCIONAR(-1, StringUtil.SELECT_OPCION_SELECCIONAR),
    REG_1(1, "Cabecera Encriptada"), 
    REG_2(2, "Datos Generales de la Empresa"),
    REG_3(3, "Datos de Convenio"),
    REG_4(4, "Socios"),
    REG_5(5, "Firmantes"),
    REG_6(6, "Locales"),
    REG_7(7, "Habilitaciones de los Locales"),
    REG_8(8, "Actividades de los Locales"),
    REG_9(9, "Exenciones de las Actividades"),
    REG_10(10, "Otros Pagos"),
    REG_11(11, "Declaracion de Actividades por Local"),
    REG_12(12, "Totales por Local de las Actividades Declaradas"),
    REG_13(13, "Retenciones y Percepciones"),
    REG_14(14, "Ajuste Base Imponible por cambio de Coeficiente por local"),
    REG_15(15, "Liquidacion de DJ Mensual DREI"),
    REG_16(16, "Declaracion de Actividades ETUR por Local"),
    REG_17(17, "Totales por Local de las Actividades Declaradas ETUR"),
    REG_18(18, "Liquidacion de DJ Mensual ETUR"),
    REG_19(20, "Datos de Domicilios"),
    REG_20(96, "Datos de pago por Cuenta"),
    REG_96(98, "Totales de Derecho y Accesorios de la DJ"),
	REG_DESCONOCIDO(0, "Desconocido");
    
    private Integer id;
    private String value;

    private RegistrosDetalleDJAfip(final Integer id, final String value) {
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
    	return "RegistrosDetalleDJAfip: [" + id + ", " + value + "]";
	}
	
    public static RegistrosDetalleDJAfip getById(Integer _id) {
       RegistrosDetalleDJAfip[] registrosDetalleDJAfip = RegistrosDetalleDJAfip.values();
	   for (int i = 0; i < registrosDetalleDJAfip.length; i++) {
		   RegistrosDetalleDJAfip tipo = registrosDetalleDJAfip[i];
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
    
    public static List<RegistrosDetalleDJAfip> getList(){
    	
    	List<RegistrosDetalleDJAfip> listRegistrosDetalleDJAfip = new ArrayList<RegistrosDetalleDJAfip>();
 	   
 	   //Obtengo la lista de RegistrosDetalleDJAfip
 	   RegistrosDetalleDJAfip[] registrosDetalleDJAfip = RegistrosDetalleDJAfip.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < registrosDetalleDJAfip.length; i++) {
 	       	if (registrosDetalleDJAfip[i].getId() != null &&
 	       		registrosDetalleDJAfip[i].getId() >= 0)
 	       	listRegistrosDetalleDJAfip.add(registrosDetalleDJAfip[i]);
 	   }
        return listRegistrosDetalleDJAfip;
    }
    
  	

}
