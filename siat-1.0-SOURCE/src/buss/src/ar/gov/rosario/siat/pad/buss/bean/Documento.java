//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Esta clase representa a un documento.
 * 
 * @author tecso
 */
@Embeddable
public class Documento extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "nrodoc") //tiene que ser un nombre que no este en otra columna que componga con persona.
    private Long numero;    // difiere en el nombre por compatibilidad con siatmr

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idTipoDocumento") 
	private TipoDocumento tipoDocumento;

    // Constructores
    public Documento() {
    }
    
	/**
     * 
     * @param documentoTipo
     * @param numero
     */
    public Documento(TipoDocumento tipo, Long numero) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de documento no puede ser nulo");
        }
        if (numero == null) {
            throw new IllegalArgumentException("El numero no puede ser nulo");
        }
        this.tipoDocumento = tipo;
        this.numero = numero;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }
    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
    public Long getNumero() {
        return numero;
    }
    
    
    
    public void setNumero(Long numero) {
        this.numero = numero;
    }
    
    /** Devuelve true si el documento no tiene tipo
     *  ni id.
     * 
     * @return
     */
    public boolean isEmpty () {
    	
    	boolean isEmpty = false;
    	
    	if (getTipoDocumento() == null || getTipoDocumento().getId() == null 
            || getTipoDocumento().getId() == 0) {
    		
    		isEmpty = true;
    		
    	}
    	
    	return isEmpty;
    	
    }
    
    public boolean isEqual (Long idTipoDocumento, Long numero) {
    	
    	boolean isEqual = false;
    	
    	if ( idTipoDocumento.equals(this.getTipoDocumento().getId()) &&
    		numero.equals(this.getNumero()) ) {
    		
    		isEqual = true;
    		
    	}
    	
    	return isEqual;
    	
    }   
    
    public String getTipoyNumeroView(){
    	TipoDocumento td = this.getTipoDocumento();
    	String tipoYNro = "";
    	tipoYNro = StringUtil.concat((td==null)?"":td.getAbreviatura(), " - "); 
       	tipoYNro += this.getNumero().toString(); 
    	
    	return tipoYNro;
    }

}
