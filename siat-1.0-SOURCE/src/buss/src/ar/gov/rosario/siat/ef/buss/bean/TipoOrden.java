//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.model.TipoOrdenVO;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a TipoOrden
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_tipoOrden")
public class TipoOrden extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final Long ID_VERIFICACION = 1L;
	public static final Long ID_DETERM_OFICIO = 2L;
	public static final Long ID_FISCALIZACION = 3L;
	
	@Column(name = "desTipoOrden")
	private String desTipoOrden;
	
	// Constructores
	public TipoOrden(){
		super();
	}
	
	public TipoOrden(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TipoOrden getById(Long id) {
		return (TipoOrden) EfDAOFactory.getTipoOrdenDAO().getById(id);
	}
	
	public static TipoOrden getByIdNull(Long id) {
		return (TipoOrden) EfDAOFactory.getTipoOrdenDAO().getByIdNull(id);
	}
	
	public static List<TipoOrden> getList() {
		return (List<TipoOrden>) EfDAOFactory.getTipoOrdenDAO().getList();
	}
	
	public static List<TipoOrden> getListActivos() {			
		return (List<TipoOrden>) EfDAOFactory.getTipoOrdenDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDesTipoOrden() {
		return desTipoOrden;
	}

	public void setDesTipoOrden(String desTipoOrden) {
		this.desTipoOrden = desTipoOrden;
	}
	
	public TipoOrdenVO toVO4Print()throws Exception{
		TipoOrdenVO tipoOrdenVO = (TipoOrdenVO) this.toVO(0, false);
	
		return tipoOrdenVO;
	}
	
	// Validaciones 
}
