//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.IDemodaEmun;

public enum LetraCuit implements IDemodaEmun {

	 /** */
    C(0,'C',"Creado" ), 
    F(1,'F', "Ficticio"),
    R(2,'R', "Real"),
    I(3,'I', "Todos"),
    OpcionSeleccionar(-1,'S', StringUtil.SELECT_OPCION_SELECCIONAR),
    OpcionTodo(-2,'T', StringUtil.SELECT_OPCION_TODOS),
    OpcionNula(null,null, "No Posee");

       private Integer id;	
	   private Character codigo;
	   private String value;
	    
	    /**
	     * Constructor de la enumeracion de Letra Cuit
	     * con id codigo y descripcion.
	     *
	     * @param value descripcion de la Letra Cuit.
	     */
	    private LetraCuit(final Integer id, final Character codigo,  final String value) {
	    	this.id = id;
	    	this.codigo = codigo;
	    	this.value = value;
	    }
	    
	    public Integer getId(){
		 	   return id;
		}
	    public Character getCodigo(){
	 	   return codigo;
	    }
	    public String getValue(){	   
	 	   return value;
	    }
	    
	    /**
	     * Obtiene la Letra Cuit a partir de su id
	     * Soporta nulo y si se le pasa un id inexistente retorna la OpcionNula
	     * @param  _id de la LetraCuit a buscar
	     * @return LetraCuit
	     */
	    public static LetraCuit getById(Integer _id){
	    	
	    	LetraCuit[] letrasCuit = LetraCuit.values();
	    	for (int i = 0; i < letrasCuit.length; i++) {
		 		  LetraCuit letraCuit = letrasCuit[i]; 		   
		  		   if (_id == null){
		  			   if (letraCuit.id == null){
		  				   return letraCuit;
		  			   }  				   
		  		   } else if (_id.equals(letraCuit.id)){
			  			   return letraCuit;
			  		   }
		 	}
	 	   return OpcionNula;
	    }
	    
	    /**
	     * Obtiene la Letra Cuit a partir de su codigo
	     * Si se le pasa un character nulo devuelve la Letra Cuit OpcionNula
	     * @param _codigo Character
	     * @return LetraCuit
	     */
	    public static LetraCuit getByCodigo(Character _codigo){

	    	LetraCuit[] letrasCuit = LetraCuit.values();

	    	for (int i = 0; i < letrasCuit.length; i++) {
	    		LetraCuit letraCuit = letrasCuit[i]; 		   
	    		if (_codigo == null){
	    			if (letraCuit.codigo == null){
	    				return letraCuit;
	    			}  				   
	    		} else if (_codigo.equals(letraCuit.codigo)){
	    			return letraCuit;
	    		}
	    	} 	   
	    	return OpcionNula;
	    }

	    /**
	    *
	    * @return un String con el formato id, codigo, valor
	    */
	   public String toString() {
	       return "Letra Cuit: [" + id + ", " + codigo + ", " + value   + "]";
	   }

	   public static List<LetraCuit> getList(LetraCuit addEnu) {
		   List<LetraCuit> listLetraCuit = new ArrayList<LetraCuit>();
		   
		   listLetraCuit.add(addEnu);
		   listLetraCuit.addAll(getList());

		   return listLetraCuit;       
	   }
	   
	   public static List<LetraCuit> getList() {
		   List<LetraCuit> listLetraCuit = new ArrayList<LetraCuit>();
		   
		   listLetraCuit.add(LetraCuit.I);
		   listLetraCuit.add(LetraCuit.C);
		   listLetraCuit.add(LetraCuit.F);
		   listLetraCuit.add(LetraCuit.R);

		   return listLetraCuit;       
	   }

	   
	    /**
	     * Devuelve un Id valido. 
	     *
	     * @return el numero del SiNo.
	     */
	    public Integer getBussId(){
	 	    if (id < 0)
	 	    	return null;
	 	    else
	 	    	return id;
	    }
	   
}
