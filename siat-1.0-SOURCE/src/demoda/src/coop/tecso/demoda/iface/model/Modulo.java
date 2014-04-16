//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

public enum Modulo implements IDemodaEmun {

	SELECCIONAR(-1, "-1", StringUtil.SELECT_OPCION_SELECCIONAR),
    gde(1, "gde", "Gestion Deuda"),
    bal(2, "bal", "Balance"),
    cas(3, "cas", "Casos y Solicitudes"),
    cyq(4, "cyq", "Concursos y Quiebras"),
    def(5, "def", "Definicion"),
    emi(6, "emi", "Emision"),
    exe(7, "exe", "Exenciones"),
    frm(8, "frm", "Formularios"),
    pad(9, "pad", "Padron"),
    pro(10, "pro", "Procesos"),
    rec(11, "rec", "Recurso"),
    seg(12, "seg", "Seguridad"),
	fra(13, "fra", "Frase");
    
    
	private Integer id;
    private String cod;
    private String value;

    private Modulo(final Integer id, final String cod, final String value) {
        this.id = id;
    	this.cod= cod;
        this.value = value;
    }
    
    public Integer getId() {
		return id;
	}
    
    public String getCod() {
		return cod;
	}
    
    public String getValue() {
		return value;
	}

    public String toString() {
    	return "Modulo: [" + id + ", " + cod + ", " + value + "]";
	}
	
    public static Modulo getById(Integer _id) {
    	Modulo[] modulos = Modulo.values();
	   for (int i = 0; i < modulos.length; i++) {
		   Modulo modulo = modulos[i];
  		   if (_id == null){
  			   if (modulo.id == null){
  				   return modulo;
  			   }  				   
  		   } else {
			   if (modulo.id.intValue() == _id.intValue()){
				   return modulo;
			   }
  		   }
		}
		   return null;
	}
    
    public static Modulo getByCod(String cod) {
    	Modulo[] modulos = Modulo.values();
	   for (int i = 0; i < modulos.length; i++) {
		   Modulo modulo = modulos[i];
  		   if (cod == null){
  			   if (modulo.cod == null){
  				   return modulo;
  			   }  				   
  		   } else {
			   if (modulo.cod.trim().equals(cod)){
				   return modulo;
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
    
    public static List<Modulo> getList(){
    	
    	List<Modulo> listModulo = new ArrayList<Modulo>();
 	   
 	   //Obtengo la lista de TipoRecibo
    	Modulo[] modulos = Modulo.values();
 		
 	   // Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
       for (int i = 0; i < modulos.length; i++) {
 	       	if (modulos[i].getId() != null &&
 	       		modulos[i].getId() >= 0)
 	       	listModulo.add(modulos[i]);
 	   }
       
        return listModulo;
    }
    
    public static boolean getEsValido(Integer id){
 	   return (id >= 1  && id <= 12);
    }


}
