//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

/**
 *  Enumeracion para los tipos de formularios de transacciones Osiris (AFIP)
 * 
 * @author tecso
 *
 */
public enum FormularioAfip implements IDemodaEmun {

	SELECCIONAR(-1, StringUtil.SELECT_OPCION_SELECCIONAR),
	TODOS(-2, StringUtil.SELECT_OPCION_TODOS),
    DREI_PRES_Y_PAGO_6050(6050, "Drei Presentación y Pago"), 
    DREI_SOLO_PRES_6062(6062, "Drei Sólo Presentación"),
    DREI_SOLO_PAGO_6052(6052, "Drei Sólo Pago"),
    ETUR_PRES_Y_PAGO_6054(6054, "Etur Presentación y Pago"),
    ETUR_SOLO_PRES_6055(6055, "Etur Sólo Presentación"),
    ETUR_SOLO_PAGO_6056(6056, "Etur Sólo Pago"),
    RS_6057(6057, "Régimen Simplificado"),
    DREI_PRES_Y_PAGO_WEB_6058(6058, "Drei Presentación y Pago WEB"),
    ETUR_PRES_Y_PAGO_WEB_6059(6059, "Etur Presentación y Pago WEB"),
    ACRETA_6060(6060, "ACRETA"),
    DREI_MULTAS_6053(6053, "Multas Drei"),
    ETUR_MULTAS_6061(6061, "Multas Etur"),
	
    /* A partir del periodo 1/2011 (con vencimiento 10/02/2011) sale una "nueva beta" del aplicativo 
     * de Osiris la cual reemplaza el formulario 6052 y 6056 por 6063 y 6064 respectivamente.
     */
    DREI_SOLO_PAGO_6063_BETA(6063, "Drei Sólo Pago - Beta"),
    ETUR_SOLO_PAGO_6064_BETA(6064, "Etur Sólo Pago - Beta");
    
    private Integer id;
    private String value;

    private FormularioAfip(final Integer id, final String value) {
        this.id = id;
        this.value = value;
    }
    
    public Integer getId() {
		return id;
	}

    public String getValue() {
		return value;
	}

    public String getIdView() {
		return id.toString();
	}
    
    public String toString() {
    	return "FormularioAfip: [" + id + ", " + value + "]";
	}
	
    public static FormularioAfip getById(Integer _id) {
       FormularioAfip[] formularioAfip = FormularioAfip.values();
	   for (int i = 0; i < formularioAfip.length; i++) {
		   FormularioAfip tipo = formularioAfip[i];
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
    
    public static List<FormularioAfip> getList(){
    	
    	List<FormularioAfip> listFormularioAfip = new ArrayList<FormularioAfip>();
 	   
 	   //Obtengo la lista de FormularioAfip
 	   FormularioAfip[] formularioAfip = FormularioAfip.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < formularioAfip.length; i++) {
 	       	if (formularioAfip[i].getId() != null &&
 	       		formularioAfip[i].getId() >= 0)
 	       	listFormularioAfip.add(formularioAfip[i]);
 	   }
        return listFormularioAfip;
    }
    
    /**
     * Obtiene la lista de FormulariosAfip que posean presentacion de Declaracion Jurada
     * 
     * @return
     */
    public static List<FormularioAfip> getListDJ(){
    	List<FormularioAfip> listFormularioAfip = new ArrayList<FormularioAfip>();
    	//Obtengo la lista de FormularioAfip
  	    FormularioAfip[] formularioAfip = FormularioAfip.values();
  	
    	// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < formularioAfip.length; i++) {
 	       	if (formularioAfip[i].getId() != null && 
 	       		(formularioAfip[i].getId() == 6050   || formularioAfip[i].getId() == 6062
 	       		|| formularioAfip[i].getId() == 6054 || formularioAfip[i].getId() == 6055 
 	       		|| formularioAfip[i].getId() == 6058 || formularioAfip[i].getId() == 6059))
 	       	listFormularioAfip.add(formularioAfip[i]);
 	   }
 	  
        return listFormularioAfip;
    }

    /**
     * Obtiene la lista de FormulariosAfip de solo Pago
     * 
     * @return
     */
    public static List<FormularioAfip> getListSoloPago(){
    	List<FormularioAfip> listFormularioAfip = new ArrayList<FormularioAfip>();
    	//Obtengo la lista de FormularioAfip
  	    FormularioAfip[] formularioAfip = FormularioAfip.values();
  	
    	// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < formularioAfip.length; i++) {
 	       	if (formularioAfip[i].getId() != null && (formularioAfip[i].getId() == 6052 
 	       		|| formularioAfip[i].getId() == 6056))
 	       	listFormularioAfip.add(formularioAfip[i]);
 	   }
 	  
        return listFormularioAfip;
    }

    public String getFullValue() {
    	if(id != null && id > 0)
    		return id.toString()+" - "+value;
    	else
    		return value;
	}
    
    public static boolean getEsRegimenSimplificado(int formulario){
    	return RS_6057.getId().intValue() == formulario? true:false;
    }
    
    public static boolean getEsMulta(int formulario){
    	if (DREI_MULTAS_6053.getId().intValue() == formulario || 
    		ETUR_MULTAS_6061.getId().intValue() == formulario)
    		return true;
		else 
			return false;
    }
    
    public static boolean getEsSoloPago(int formulario){
    	if (DREI_SOLO_PAGO_6052.getId().intValue() == formulario || 
   			ETUR_SOLO_PAGO_6056.getId().intValue() == formulario||
    		DREI_SOLO_PAGO_6063_BETA.getId().intValue() == formulario ||
    		ETUR_SOLO_PAGO_6064_BETA.getId().intValue() == formulario)
    		return true;
		else 
			return false;
    }
    
    public static boolean getEsDJyPago(int formulario){
    	if (DREI_PRES_Y_PAGO_6050.getId().intValue() == formulario || 
 			ETUR_PRES_Y_PAGO_6054.getId().intValue() ==  formulario)
    		return true;
		else 
			return false;
    }
    
    public static boolean getEsDJyPagoWeb(int formulario){
    	if (DREI_PRES_Y_PAGO_WEB_6058.getId().intValue() == formulario || 
    		ETUR_PRES_Y_PAGO_WEB_6059.getId().intValue() ==  formulario)
    		return true;
		else 
			return false;
    }
}
