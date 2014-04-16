//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.frm.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.IDemodaEmun;

/**
 * Representa el formato de salida de los reportes de los formularios.
 * @author Tecso Coop. Ltda.
 *
 */
public enum FormatoSalida implements IDemodaEmun  {
	
	PDF(1,"PDF","Formato de Salida Pdf"),
	TXT(2,"TXT","Formato de Salida Txt"),
	OpcionSelecionar(-1, StringUtil.SELECT_OPCION_SELECCIONAR, StringUtil.SELECT_OPCION_SELECCIONAR),
    OpcionTodo(-2, StringUtil.SELECT_OPCION_TODOS, StringUtil.SELECT_OPCION_TODOS),
    OpcionNula(null, "No Posee", "No Posee");
	
    private Integer id;
    private String value;
    private String descripcion;
    
    private FormatoSalida(){
    }
    
    private FormatoSalida(Integer id, String value, String descripcion){
    	this.id = id;
    	this.value = value;
    	this.descripcion = descripcion;
    }

	public String getDescripcion() {
		return descripcion;
	}
	public Integer getId() {
		return id;
	}
	public String getValue() {
		return value;
	}
	public Integer getBussId() {
		if (id == null || id < 0)
			return null;
		else
			return id;
	}

	 public static List<FormatoSalida> getList() {
		   
		 List<FormatoSalida> listFS = new ArrayList<FormatoSalida>();
		   
		   //Obtengo la lista de SiNo
		 FormatoSalida[] formatoSalidas = FormatoSalida.values();
			
		 // Agrego las enumeraciones cuyo id sea no nulo o mayor que cero
	       for (int i = 0; i < formatoSalidas.length; i++) {
		       	if (formatoSalidas[i].getId() != null &&
		       			formatoSalidas[i].getId() >= 0)
		       		listFS.add(formatoSalidas[i]);
		   }
	       
	       return listFS;       
	   }
	 
	 public static List<FormatoSalida> getList(FormatoSalida formatoSalida) {

		 List<FormatoSalida> listFS = FormatoSalida.getList();

		 listFS.add(0, formatoSalida);

		 return listFS;       
	 }

    public static FormatoSalida getById(Integer _id){
    	
    	FormatoSalida[] formatoSalidas = FormatoSalida.values();
	  	   
	  	   for (int i = 0; i < formatoSalidas.length; i++) {
	  		 FormatoSalida fs = formatoSalidas[i]; 		   
	   		   if (_id == null){
	   			   if (fs.id == null){
	   				   return fs;
	   			   }  				   
	   		   } else {
	 	  		   if (fs.id.intValue() == _id.intValue()){
	 	  			   return fs;
	 	  		   }
	   		   }
	  	   } 	   
	  	   return OpcionNula;
	     }

    public boolean getEsPDF(){
    	return PDF.getId().equals(this.id);
    }
    
    public boolean getEsTXT(){
    	return TXT.getId().equals(this.id);
    }
    
    public static boolean getEsValido(Integer id){
 	   	return (id != null)  && (FormatoSalida.TXT.id.intValue() == id.intValue()  
 			|| FormatoSalida.PDF.id.intValue() == id.intValue());
    }
}
