//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.IDemodaEmun;

public enum AccionTraspasoDevolucion implements IDemodaEmun  {

    TRASPASO(1, "Traspaso de deuda entre procuradores"), 
    DEVOLUCION(0, "Devolución de Deuda"),
    OpcionSelecionar(-1, StringUtil.SELECT_OPCION_SELECCIONAR),
    OpcionTodo(-2, StringUtil.SELECT_OPCION_TODOS),
    OpcionNula(null, "No Posee");

    private Integer id;
    private String  value;

    private AccionTraspasoDevolucion() {
    }
    
    private AccionTraspasoDevolucion(final Integer id, final String value ) {
    	this.id = id;
    	this.value = value;
    }
    
    /**
     * Obtiene la Accion a partir de su id
     * Soporta nulo y si se le pasa un id inexistente retorna la OpcionNula
     * @param  _id
     * @return AccionTraspasoDevolucion
     */
    public static AccionTraspasoDevolucion getById(Integer _id){
 	   AccionTraspasoDevolucion[] acciones = AccionTraspasoDevolucion.values();
 	   
 	   for (int i = 0; i < acciones.length; i++) {
 		   AccionTraspasoDevolucion accion = acciones[i]; 		   
  		   if (_id == null){
  			   if (accion.id == null){
  				   return accion;
  			   }  				   
  		   } else {
	  		   if (accion.id.intValue() == _id.intValue()){
	  			   return accion;
	  		   }
  		   }
 	   } 	   
 	   return OpcionNula;
    }
    
    public Integer getId(){
 	   return id;
    }
    
    public String getValue(){	   
 	   return value;
    }

    /**
     * Devuelve un Id valido.
     *
     * @return el numero del id valido de la Accion.
     * Si el id es nulo o menor a cero devuelve null
     */
    public Integer getBussId(){
 	    if (id == null || id < 0)
 	    	return null;
 	    else
 	    	return id;
    }
    
   /**
    *
    * @return un String con el formato <tt>Accion: [value, id]<tt>.
    */
   public String toString() {
       return "Accion: [" + value + ", " + id + "]";
   }

   public static List<AccionTraspasoDevolucion> getList(AccionTraspasoDevolucion addEnu) {
	   List<AccionTraspasoDevolucion> listSiNo = new ArrayList<AccionTraspasoDevolucion>();
	   
	   //Obtengo la lista de Acciones
	   AccionTraspasoDevolucion[] acciones = AccionTraspasoDevolucion.values();
		
		// Agrego las enumeraciones cuyo id sea no nulo o mayor que cero
       for (int i = 0; i < acciones.length; i++) {
	       	if (acciones[i].getId() != null &&
	       			acciones[i].getId() >= 0)
	       		listSiNo.add(acciones[i]);
	   }
       
       listSiNo.add(0, addEnu);
       
       return listSiNo;       
   }
   
   public boolean getEsTraspaso(){
	   return this.id.intValue() == TRASPASO.getId().intValue();
   }
   
   public boolean getEsDevolucion(){
	   return this.id.intValue() == DEVOLUCION.getId().intValue();
   }
   
   public static boolean getEsValido(Integer id){
	   return (AccionTraspasoDevolucion.TRASPASO.id.intValue() == id.intValue()  || 
			   AccionTraspasoDevolucion.DEVOLUCION.id.intValue() == id.intValue() );
   }
   
}
