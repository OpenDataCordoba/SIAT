//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.model.IDemodaEmun;

public enum PermiteWeb implements IDemodaEmun  {

	NO_PERMITE_WEB(0, "No Permite Web", "N"),   //No permite login por web
    PERMITE_WEB(1, "Permite Web", "P");         //Permite web solo si cada uno de los roles del usuario tiene PERMITE_WEB
    
    private Integer id;
    private String value;
    /** Una sola letra de la abreviatura. */
    private String abreviatura;

    private PermiteWeb() {

    }
    
    /**
     * Constructor de la enumeracion de SiNO
     * con abreviatura y descripcion.
     *
     * @param abreviatura del SiNo.
     * @param descripcion del SiNo.
     */
    private PermiteWeb(final Integer id,  final String value , final String abreviatura) {
    	this.id = id;
    	this.value = value;
        this.abreviatura = abreviatura;
    }
    
    /**
     * Obtiene el SiNo a partir de su id
     * Soporta nulo y si se le pasa un id inexistente retorna la OpcionNula
     * @param  abrev del SiNo a buscar
     * @return SiNo
     */
    public static PermiteWeb getById(Integer _id){
 	   PermiteWeb[] siNos = PermiteWeb.values();
 	   
 	   for (int i = 0; i < siNos.length; i++) {
 		   PermiteWeb sino = siNos[i]; 		   
  		   if (_id == null){
  			   if (sino.id == null){
  				   return sino;
  			   }  				   
  		   } else {
	  		   if (sino.id.intValue() == _id.intValue()){
	  			   return sino;
	  		   }
  		   }
 	   } 	   
 	   return null;
    }
    
    /**
     * Devuelve 1 (Si) o 0 (No).
     *
     * @return el numero del SiNo.
     */
    public Integer getId(){
 	   return id;
    }
    
    /**
     * Devuelve Si o No.
     *
     * @return la descripcion del SiNo.
     */
    public String getValue(){	   
 	   return value;
    }

    /**
     * Devuelve S o N.
     *
     * @return la abreviatura del sexo.
     */
    public String getAbreviatura() {
        return abreviatura;
    }

    /**
     * Devuelve un Id valido para persistir, en un create o update.
     *
     * @return el numero del SiNo.
     */
    public Integer getBussId(){
 	    if (id == null || id < 0)
 	    	return null;
 	    else
 	    	return id;
    }
    
   /**
    *
    * @return un String con el formato <tt>SiNo: [S, Si]<tt>.
    */
   public String toString() {
       return "PermiteWeb: [" + abreviatura + ", " + value + ", " + id + "]";
   }

   /**
    * Obtiene el SiNo a partir de su abreviatura
    * @param  abrev del SiNo a buscar
    * @return SiNo
    */
   public static PermiteWeb getSiNoByAbreviatura(String abrev){
	   PermiteWeb[] siNos = PermiteWeb.values();
	   for (int i = 0; i < siNos.length; i++) {
		   PermiteWeb sino = siNos[i];
		   if (sino.abreviatura.equalsIgnoreCase(abrev.trim())){
			   return sino;
		   }
	   }
	   return null;	   

   }

   public static List<PermiteWeb> getList(PermiteWeb addEnu) {
	   List<PermiteWeb> listRet = new ArrayList<PermiteWeb>();
	   
	   //Obtengo la lista de SiNo
	   PermiteWeb[] list = PermiteWeb.values();
		
		// Agrego las enumeraciones cuyo id sea no nulo o mayor que cero
       for (int i = 0; i < list.length; i++) {
	       	if (list[i].getId() != null &&
	       			list[i].getId() >= 0)
	       		listRet.add(list[i]);
	   }
       
       if (addEnu != null){
    	   listRet.add(0, addEnu);
       }
       
       return listRet;       
   }   
}
