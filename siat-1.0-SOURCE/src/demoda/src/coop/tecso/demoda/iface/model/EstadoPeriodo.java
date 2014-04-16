//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

public enum EstadoPeriodo implements IDemodaEmun {

    
	//NODECLARADOS(1,"No declarados"),
	//DECLARADOS(2,"Declarados"),
	IMPAGOS(3,"Determinados Impagos"),
	PAGOS(4,"Determinados Pagos"),
	NODETERMINADOS(5,"No Determinados"),
	ADEUDADOS(6,"No Determinados + Determinados Impagos"),
	
	OpcionTodo(-1, StringUtil.SELECT_OPCION_TODOS),
	OpcionSelecionar(-2, StringUtil.SELECT_OPCION_SELECCIONAR);
	   
    
    private Integer id;
    private String value;

    private EstadoPeriodo(final Integer id, final String value) {
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
    	return "EstadoPeriodo: [" + id + ", " + value + "]";
	}
	
    public static EstadoPeriodo getById(Integer _id) {
       EstadoPeriodo[] estados = EstadoPeriodo.values();
	   for (int i = 0; i < estados.length; i++) {
		   EstadoPeriodo est = estados[i];
  		   if (_id == null){
  			   if (est.id == null){
  				   return est;
  			   }  				   
  		   } else {
			   if (est.id.intValue() == _id.intValue()){
				   return est;
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
    
    public static List<EstadoPeriodo> getList(EstadoPeriodo estadoPeriodo){
    	
    	List<EstadoPeriodo> listEstadoPeriodo = new ArrayList<EstadoPeriodo>();
 	   
 	   //Obtengo la lista de EstadoPeriodo
 	   EstadoPeriodo[] estados = EstadoPeriodo.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < estados.length; i++) {
 	       	if (estados[i].getId() != null &&
 	       		estados[i].getId() >= 0)
 	       	listEstadoPeriodo.add(estados[i]);
 	   	}
        
        listEstadoPeriodo.add(0, estadoPeriodo);
        
        return listEstadoPeriodo;
    }
    
    public static boolean getEsValido(Integer id){
 	   return (EstadoPeriodo.NODETERMINADOS.id== id || //EstadoPeriodo.NODECLARADOS.id == id  || EstadoPeriodo.DECLARADOS.id == id || 
 			   EstadoPeriodo.IMPAGOS.id == id || EstadoPeriodo.PAGOS.id == id );
    }
    
    public boolean getEsValido(){
  	   return this.getId().intValue() > 0;
    }
    
    /*
    public boolean getEsJoinDeclaracion(){
    	return this.getId().intValue() == NODECLARADOS.id || this.getId().intValue() == DECLARADOS.id;
    }
    
    public boolean getEsConDeclaracion(){
  	   return this.getId().intValue() == DECLARADOS.id;
    }
    
    public boolean getEsSinDeclaracion(){
   	   return this.getId().intValue() == NODECLARADOS.id;
    }
    */
    
    public boolean getEsPago(){
    	   return this.getId().intValue() == PAGOS.id;
    }

    public boolean getEsInpago(){
 	   return this.getId().intValue() == IMPAGOS.id;
    }
    
    public boolean getEsNoDeterminado(){
    	return this.getId().intValue()==NODETERMINADOS.id;
    }
    
    public boolean getEsAdeudado(){
    	return this.getId().intValue()==ADEUDADOS.id;
    }
}
