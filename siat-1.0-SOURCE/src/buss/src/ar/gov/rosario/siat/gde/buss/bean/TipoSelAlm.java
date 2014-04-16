//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente al Tipo de Seleccion Almacenada
 * Tambien representa los Tipos de Detalle de Seleccion Almacenada
 * 
 * @author tecso
 */

@Entity
@Table(name = "gde_tipoSelAlm")
public class TipoSelAlm extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoSelAlm";
	
	public static final Long TIPO_SEL_ALM_DEUDA  = 1l;
	public static final Long TIPO_SEL_ALM_PLANES = 2l;
	public static final Long TIPO_SEL_ALM_RECIBO = 3l;
	public static final Long TIPO_SEL_ALM_CUENTA = 4l;
	public static final Long TIPO_SEL_ALM_CONTRIBUYENTE = 5l;
	public static final Long TIPO_SEL_ALM_CUENTATITULAR = 6L;
	
	// Tipos de Detalle de Seleccion Almacenada
	public static final Long TIPO_SEL_ALM_DET_DEUDA_ADM = 10L;
	public static final Long TIPO_SEL_ALM_DET_DEUDA_JUD = 11L;
	public static final Long TIPO_SEL_ALM_DET_CONV_CUOT_ADM = 12L;
	public static final Long TIPO_SEL_ALM_DET_CONV_CUOT_JUD = 13L;
	
	private static final Long MIN_ID_TIPO_SEL_ALM_DET = TIPO_SEL_ALM_DET_DEUDA_ADM;
	private static final Long MAX_ID_TIPO_SEL_ALM_DET = TIPO_SEL_ALM_DET_CONV_CUOT_JUD;

	@Column(name = "codTipoSelAlm")
	private String codTipoSelAlm;   // CHAR(10) NOT NULL

	@Column(name = "desTipoSelAlm")
	private String desTipoSelAlm;   // VARCHAR(255) NOT NULL 
	
	// Constructores
	public TipoSelAlm(){
		super();
	}
	
	// Getters y Setters
	public String getCodTipoSelAlm() {
		return codTipoSelAlm;
	}
	public void setCodTipoSelAlm(String codTipoSelAlm) {
		this.codTipoSelAlm = codTipoSelAlm;
	}
	public String getDesTipoSelAlm() {
		return desTipoSelAlm;
	}
	public void setDesTipoSelAlm(String desTipoSelAlm) {
		this.desTipoSelAlm = desTipoSelAlm;
	}
	
	// Metodos de Clase
	public static TipoSelAlm getById(Long id) {
		return (TipoSelAlm) GdeDAOFactory.getTipoSelAlmDAO().getById(id);  
	}
	
	public static TipoSelAlm getByIdNull(Long id) {
		return (TipoSelAlm) GdeDAOFactory.getTipoSelAlmDAO().getByIdNull(id);
	}
	
	public static List<TipoSelAlm> getList() {
		return (ArrayList<TipoSelAlm>) GdeDAOFactory.getTipoSelAlmDAO().getList();
	}
	
	public static List<TipoSelAlm> getListActivos() {			
		return (ArrayList<TipoSelAlm>) GdeDAOFactory.getTipoSelAlmDAO().getListActiva();
	}

	/**
	 * Obtiene la lista de Tipos para los Detalles de Seleccion Almacenada
	 * No obtiene los tipos para la Seleccion Almacenada
	 * @return List<TipoSelAlm>
	 */
	public static List<TipoSelAlm> getListForSelAlmDet() {
		
		return (ArrayList<TipoSelAlm>) GdeDAOFactory.getTipoSelAlmDAO().
					getListByRangoIds(MIN_ID_TIPO_SEL_ALM_DET, MAX_ID_TIPO_SEL_ALM_DET);
	}
	
	/**
	 * Obtiene el Tipo de Detalle de Seleccion Almacenada para la Deuda Administrativa
	 * @return TipoSelAlm
	 */
	public static TipoSelAlm getTipoSelAlmDetDeudaAdm(){
		return TipoSelAlm.getById(TIPO_SEL_ALM_DET_DEUDA_ADM);
	}
	
	public static TipoSelAlm getTipoSelAlmDetDeudaJud(){
		return TipoSelAlm.getById(TIPO_SEL_ALM_DET_DEUDA_JUD);
	}
	public static TipoSelAlm getTipoSelAlmDetConvCuotAdm(){
		return TipoSelAlm.getById(TIPO_SEL_ALM_DET_CONV_CUOT_ADM);
	}
	
	public static TipoSelAlm getTipoSelAlmDetConvCuotJud(){
		return TipoSelAlm.getById(TIPO_SEL_ALM_DET_CONV_CUOT_JUD);
	}


	// Metodos de Instancia

	// Metodos de negocio
	
	public boolean getEsTipoSelAlmDetDeudaAdm(){
		return TIPO_SEL_ALM_DET_DEUDA_ADM.equals(this.getId());
	}
	public boolean getEsTipoSelAlmDetDeudaJud(){
		return TIPO_SEL_ALM_DET_DEUDA_JUD.equals(this.getId());
	}
	public boolean getEsTipoSelAlmDetConvCuotAdm(){
		return TIPO_SEL_ALM_DET_CONV_CUOT_ADM.equals(this.getId());
	}
	public boolean getEsTipoSelAlmDetConvCuotJud(){
		return TIPO_SEL_ALM_DET_CONV_CUOT_JUD.equals(this.getId());
	}
	public boolean getEsTipoSelAlmDetDeuda(){
		return this.getEsTipoSelAlmDetDeudaAdm() || this.getEsTipoSelAlmDetDeudaJud();
	}
	public boolean getEsTipoSelAlmDetConvCuot(){
		return this.getEsTipoSelAlmDetConvCuotAdm() || this.getEsTipoSelAlmDetConvCuotJud();
	}
	

	

}
