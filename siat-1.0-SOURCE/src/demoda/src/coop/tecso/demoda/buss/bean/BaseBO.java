//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.buss.bean;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


/**
 * Esta es de la que debieran heredar todos los BO
 * 
 * @author ggiro
 * 
 */
@MappedSuperclass
public class BaseBO extends BaseBean {

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    public BaseBO() { 
    	setUsuarioUltMdf("siat");
        setFechaUltMdf(new Date());
        setEstado(new Integer(1));
    }

    public BaseBO(Long id) {
    	this();
    	this.id = id;
    }

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	
}
