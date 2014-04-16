//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

public enum TipoCancelacion implements IDemodaEmun  {

	TRANSFERENCIA(1, "Transferencia"),
	CONTADO(2, "Contado"),
    FALTA_DIST_FONDO(3, "Falta Distribucion Fondos"),
    FALTADEACTIVO(4, "Falta de Activo"),
    
    OpcionSelecionar(-1, StringUtil.SELECT_OPCION_SELECCIONAR),
    OpcionTodo(-2, StringUtil.SELECT_OPCION_TODOS),
    OpcionNula(null, "No Posee");

    private Integer id;
    private String  value;

    private TipoCancelacion() {
    }
    
    private TipoCancelacion(final Integer id, final String value ) {
    	this.id = id;
    	this.value = value;
    }
    
    /**
     * Obtiene el Tipo de Reporte a partir de su id
     * Soporta nulo y si se le pasa un id inexistente retorna la OpcionNula
     * @param  _id
     * @return TipoReporte
     */
    public static TipoCancelacion getById(Integer _id){
 	   TipoCancelacion[] tiposPago = TipoCancelacion.values();
 	   
 	   for (int i = 0; i < tiposPago.length; i++) {
 		   TipoCancelacion tipoPago = tiposPago[i]; 		   
  		   if (_id == null){
  			   if (tipoPago.id == null){
  				   return tipoPago;
  			   }  				   
  		   } else {
	  		   if (tipoPago.id.intValue() == _id.intValue()){
	  			   return tipoPago;
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
     * @return el numero del id valido del tipo de Reporte.
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
    * @return un String con el formato <tt>Tipo Reporte: [value, id]<tt>.
    */
   public String toString() {
       return "Tipo Reporte: [" + value + ", " + id + "]";
   }

   public static List<TipoCancelacion> getList(TipoCancelacion addEnu) {
	   
	   List<TipoCancelacion> listTipoPago = TipoCancelacion.getList();
       
	   listTipoPago.add(0, addEnu);
       
       return listTipoPago;       
   }
   
   public static List<TipoCancelacion> getList() {
	   List<TipoCancelacion> listTipoPago = new ArrayList<TipoCancelacion>();
	   
	   //Obtengo la lista de tiposPago
	   TipoCancelacion[] tiposPago = TipoCancelacion.values();
		
		// Agrego las enumeraciones cuyo id sea no nulo o mayor que cero
       for (int i = 0; i < tiposPago.length; i++) {
	       	if (tiposPago[i].getId() != null &&
	       			tiposPago[i].getId() >= 0)
	       		listTipoPago.add(tiposPago[i]);
	   }
       
       return listTipoPago;       
   }

   
   public static boolean getEsValido(Integer id){
	   return (TipoCancelacion.TRANSFERENCIA.id.equals(id)  ||
			   TipoCancelacion.CONTADO.id.equals(id)  ||
			   TipoCancelacion.FALTA_DIST_FONDO.id.equals(id) ||
			   TipoCancelacion.FALTADEACTIVO.id.equals(id));
   }
   
}
