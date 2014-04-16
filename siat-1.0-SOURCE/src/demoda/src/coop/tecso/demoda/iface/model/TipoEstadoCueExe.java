//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

public enum TipoEstadoCueExe implements IDemodaEmun {

	SELECCIONAR( 0, "0", StringUtil.SELECT_OPCION_SELECCIONAR),
    TIPOACCION(1, "A", "Accion"), 
    TIPOSOLICITUD(2, "S", "Solicitud"),
    TIPOESTADO(3, "E", "Estado"),
    NULO(null,null," ");
   
    
    private Integer id;
    private String value;
    private String cod;

    private TipoEstadoCueExe(final Integer id, final String cod, final String value) {
    	 this.id = id;
     	this.cod= cod;
         this.value = value;
    }
    
    public String getCod() {
		return cod;
	}

    public Integer getId() {
		return id;
	}

    public String getValue() {
		return value;
	}

    public String toString() {
    	return "TipoEstadoCueExe: [" + id + ", " + cod + ", " + value + "]";
	}
    
    public static TipoEstadoCueExe getById(Integer _id) {
    	TipoEstadoCueExe[] tipoEstadoCueExes = TipoEstadoCueExe.values();
	   for (int i = 0; i < tipoEstadoCueExes.length; i++) {
		   TipoEstadoCueExe tipoEstadoCueExe = tipoEstadoCueExes[i];
  		   if (_id == null){
  			   if (tipoEstadoCueExe.id == null){
  				   return tipoEstadoCueExe;
  			   }  				   
  		   } else {
			   if (tipoEstadoCueExe.id.intValue() == _id.intValue()){
				   return tipoEstadoCueExe;
			   }
  		   }
		}
		   return null;
	}
    
    public static TipoEstadoCueExe getByCod(String cod) {
    	TipoEstadoCueExe[] tipoEstadoCueExes = TipoEstadoCueExe.values();
	   for (int i = 0; i < tipoEstadoCueExes.length; i++) {
		   TipoEstadoCueExe tipoEstadoCueExe = tipoEstadoCueExes[i];
  		   if (cod == null){
  			   if (tipoEstadoCueExe.cod == null){
  				   return tipoEstadoCueExe;
  			   }  				   
  		   } else {
			   if (tipoEstadoCueExe.cod.trim().equals(cod)){
				   return tipoEstadoCueExe;
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
    
    public static List<TipoEstadoCueExe> getList(){
    	
    	List<TipoEstadoCueExe> listTipoEstadoCueExe = new ArrayList<TipoEstadoCueExe>();
 	   
 	   //Obtengo la lista de tipoEstadoCueExe
 	   TipoEstadoCueExe[] tipoEstadoCueExe = TipoEstadoCueExe.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < tipoEstadoCueExe.length; i++) {
 	       	if (tipoEstadoCueExe[i].getId() != null &&
 	       		tipoEstadoCueExe[i].getId() >= 0)
 	       	listTipoEstadoCueExe.add(tipoEstadoCueExe[i]);
 	   }
        return listTipoEstadoCueExe;
    }
    
    public static List<TipoEstadoCueExe> getListEstadosValidos(){
    	
    	List<TipoEstadoCueExe> listTipoEstadoCueExe = new ArrayList<TipoEstadoCueExe>();
 	   
 	   //Obtengo la lista de tipoEstadoCueExe
 	   TipoEstadoCueExe[] tipoEstadoCueExe = TipoEstadoCueExe.values();
 		
 		// Agrego las enumeraciones cuyo id sea no nulo o mayor o igual que cero
        for (int i = 0; i < 3; i++) {
 	       	if (tipoEstadoCueExe[i].getId() != null &&
 	       		tipoEstadoCueExe[i].getId() >= 0)
 	       	listTipoEstadoCueExe.add(tipoEstadoCueExe[i]);
 	   }
        return listTipoEstadoCueExe;
    }
    
    public static boolean getEsValido(Integer id){
 	   return (TipoEstadoCueExe.TIPOACCION.id == id  || TipoEstadoCueExe.TIPOSOLICITUD.id == id );
    }

	

}
