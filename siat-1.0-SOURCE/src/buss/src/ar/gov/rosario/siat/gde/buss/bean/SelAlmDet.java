//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a un Detalle de Seleccion Almacenada
 * 
 * @author tecso
 */

@Entity
@Table(name = "gde_selAlmDet")
public class SelAlmDet extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "selAlmDet";

	@ManyToOne(optional=false) 
    @JoinColumn(name="idSelAlm") 
	private SelAlm selAlm;
	
	// Corresponde al id de una deuda, contribuyente, cuenta, plan o recibo
	@Column(name = "idElemento")
	private Long idElemento;
	
	@ManyToOne(optional=true) 
    @JoinColumn(name="idTipoSelAlmDet") 
	private TipoSelAlm tipoSelAlmDet;   

	// Constructores
	public SelAlmDet(){
		super();
	}
	
	/**
	 * Constructor sobrecargado
	 *
	 * @param selAlm
	 * @param idElemento
	 * @param tipoSelAlm
	 */
	public SelAlmDet(SelAlm selAlm, Long idElemento, TipoSelAlm tipoSelAlm) {
		super();
		this.selAlm = selAlm;
		this.idElemento = idElemento;
		this.tipoSelAlmDet = tipoSelAlm;
	}
	
	/**
	 * Constructor optimizado para la carga masiva de SelAlmDet excluida
	 *
	 * @param selAlm
	 * @param idElemento
	 * @param tipoSelAlm
	 * @param usuarioUltMdf
	 * @param fechaUltMdf
	 */
	public SelAlmDet(SelAlm selAlm, Long idElemento, TipoSelAlm tipoSelAlm, String usuarioUltMdf, Date fechaUltMdf) {
		this(selAlm, idElemento, tipoSelAlm);
		this.setUsuarioUltMdf(usuarioUltMdf);
		this.setFechaUltMdf(fechaUltMdf);
	}

	
	// Getters y Setters
	public Long getIdElemento() {
		return idElemento;
	}
	public void setIdElemento(Long idElemento) {
		this.idElemento = idElemento;
	}
	public SelAlm getSelAlm() {
		return selAlm;
	}
	public void setSelAlm(SelAlm selAlm) {
		this.selAlm = selAlm;
	}
	public TipoSelAlm getTipoSelAlmDet() {
		return tipoSelAlmDet;
	}
	public void setTipoSelAlmDet(TipoSelAlm tipoSelAlmDet) {
		this.tipoSelAlmDet = tipoSelAlmDet;
	}

	// Metodos de Clase
	public static SelAlmDet getById(Long id) {
		return (SelAlmDet) GdeDAOFactory.getSelAlmDetDAO().getById(id);  
	}
	
	public static SelAlmDet getByIdNull(Long id) {
		return (SelAlmDet) GdeDAOFactory.getSelAlmDetDAO().getByIdNull(id);
	}
	
	public static List<SelAlmDet> getList() {
		return (ArrayList<SelAlmDet>) GdeDAOFactory.getSelAlmDetDAO().getList();
	}
	
	public static List<SelAlmDet> getListActivos() {			
		return (ArrayList<SelAlmDet>) GdeDAOFactory.getSelAlmDetDAO().getListActiva();
	}


	// Metodos de Instancia

	//Validaciones
	/**
	 * Valida la creacion
	 * @author 
	 */
	public boolean validateCreate() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}
		
		//Validaciones de Negocio
		
		return true;
	}

	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		
		return true;		
	}

	/**
	 * Valida la eliminacion
	 * @author 
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		
		return true;
	}
	
	// Metodos de negocio
	

	// 
	/**
	 * Obtiene la deuda Admin a partir del IdElemento
	 * @return DeudaAdmin 
	 */
	public DeudaAdmin obtenerDeudaAdmin(){
		return DeudaAdmin.getByIdNull(this.getIdElemento());
	}

	/**
	 * Obtiene la deuda Judicial a partir del idElemento
	 * @return DeudaJudicial
	 */
	public DeudaJudicial obtenerDeudaJudicial(){
		return DeudaJudicial.getByIdNull(this.getIdElemento());
	}

	/**
	 * Obtiene la Cuota de Convenio a partir del idElemento
	 * @return ConvenioCuota
	 */
	public ConvenioCuota obtenerConvenioCuota(){
		return ConvenioCuota.getByIdNull(this.getIdElemento());
	}


}
