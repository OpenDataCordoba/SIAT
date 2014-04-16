//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;

public enum TipoReporte implements IDemodaEmun  {

	RESUMIDO(0, "Resumido"), 
    DETALLADO(1, "Detallado"),
    TOTALIZADO(2, "Totalizado"),
    
    OpcionSelecionar(-1, StringUtil.SELECT_OPCION_SELECCIONAR),
    OpcionTodo(-2, StringUtil.SELECT_OPCION_TODOS),
    OpcionNula(null, "No Posee");

    private Integer id;
    private String  value;

    private TipoReporte() {
    }
    
    private TipoReporte(final Integer id, final String value ) {
    	this.id = id;
    	this.value = value;
    }
    
    /**
     * Obtiene el Tipo de Reporte a partir de su id
     * Soporta nulo y si se le pasa un id inexistente retorna la OpcionNula
     * @param  _id
     * @return TipoReporte
     */
    public static TipoReporte getById(Integer _id){
 	   TipoReporte[] tiposReporte = TipoReporte.values();
 	   
 	   for (int i = 0; i < tiposReporte.length; i++) {
 		   TipoReporte tipoReporte = tiposReporte[i]; 		   
  		   if (_id == null){
  			   if (tipoReporte.id == null){
  				   return tipoReporte;
  			   }  				   
  		   } else {
	  		   if (tipoReporte.id.intValue() == _id.intValue()){
	  			   return tipoReporte;
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

   public static List<TipoReporte> getList(TipoReporte addEnu) {
	   
	   List<TipoReporte> listTipoReporte = TipoReporte.getList();
       
	   listTipoReporte.add(0, addEnu);
       
       return listTipoReporte;       
   }
   
   public static List<TipoReporte> getList() {
	   List<TipoReporte> listTipoReporte = new ArrayList<TipoReporte>();
	   
	   //Obtengo la lista de tiposReporte
	   TipoReporte[] tiposReporte = TipoReporte.values();
		
		// Agrego las enumeraciones cuyo id sea no nulo o mayor que cero
       for (int i = 0; i < tiposReporte.length; i++) {
	       	if (tiposReporte[i].getId() != null &&
	       			tiposReporte[i].getId() >= 0)
	       		listTipoReporte.add(tiposReporte[i]);
	   }
       
       return listTipoReporte;       
   }

   
   public boolean getEsResumido(){
	   return RESUMIDO.getId().equals(this.id);
   }
   
   public boolean getEsDetallado(){
	   return DETALLADO.getId().equals(this.id);
   }

   public boolean getEsTotalizado(){
	   return TOTALIZADO.getId().equals(this.id);
   }

   
   public static boolean getEsValido(Integer id){
	   return (TipoReporte.RESUMIDO.id.equals(id)  || 
			   TipoReporte.DETALLADO.id.equals(id) ||
			   TipoReporte.TOTALIZADO.id.equals(id));
   }
   
}
