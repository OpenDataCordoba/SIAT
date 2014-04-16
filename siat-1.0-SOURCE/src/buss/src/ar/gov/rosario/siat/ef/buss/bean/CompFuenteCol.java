//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.model.AliComFueColVO;
import ar.gov.rosario.siat.ef.iface.model.CompFuenteColVO;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a CompFuenteCol
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_compFuenteCol")
public class CompFuenteCol extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@Transient
	private Log log = LogFactory.getLog(CompFuenteCol.class);

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCompFuente")
	private CompFuente compFuente;

	@Column(name = "colName")
	private String colName;

	@Column(name = "nroColumna")
	private Integer nroColumna;

	@Column(name = "orden")
	private Integer orden;

	@Column(name = "oculta")
	private Integer oculta;

	@Column(name = "sumaEnTotal")
	private Integer sumaEnTotal;

	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idCompFuenteCol")
	@OrderBy(clause = "anioDesde, periodoDesde ASC")
	private List<AliComFueCol> listAliComFueCol = new ArrayList<AliComFueCol>();
	
	// <#Propiedades#>

	// Constructores
	public CompFuenteCol() {
		super();
		// Seteo de valores default
	}

	public CompFuenteCol(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static CompFuenteCol getById(Long id) {
		return (CompFuenteCol) EfDAOFactory.getCompFuenteColDAO().getById(id);
	}

	public static CompFuenteCol getByIdNull(Long id) {
		return (CompFuenteCol) EfDAOFactory.getCompFuenteColDAO().getByIdNull(
				id);
	}

	public static List<CompFuenteCol> getList() {
		return (ArrayList<CompFuenteCol>) EfDAOFactory.getCompFuenteColDAO()
				.getList();
	}

	public static List<CompFuenteCol> getListActivos() {
		return (ArrayList<CompFuenteCol>) EfDAOFactory.getCompFuenteColDAO()
				.getListActiva();
	}
	
	public static List<CompFuenteCol>getListSumanEnTotalByCompFuente(CompFuente compFuente){
		return EfDAOFactory.getCompFuenteColDAO().getListByCompFuenteSuma(compFuente);
	}

	// Getters y setters
	public CompFuente getCompFuente() {
		return compFuente;
	}

	public void setCompFuente(CompFuente compFuente) {
		this.compFuente = compFuente;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public Integer getNroColumna() {
		return nroColumna;
	}

	public void setNroColumna(Integer nroColumna) {
		this.nroColumna = nroColumna;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public Integer getOculta() {
		return oculta;
	}

	public void setOculta(Integer oculta) {
		this.oculta = oculta;
	}

	public Integer getSumaEnTotal() {
		return sumaEnTotal;
	}

	public void setSumaEnTotal(Integer sumaEnTotal) {
		this.sumaEnTotal = sumaEnTotal;
	}

	public List<AliComFueCol> getListAliComFueCol() {
		return listAliComFueCol;
	}

	public void setListAliComFueCol(List<AliComFueCol> listAliComFueCol) {
		this.listAliComFueCol = listAliComFueCol;
	}

	// Validaciones
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}

		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}

		// Validaciones de Negocio

		return true;
	}

	public boolean validateDelete() {
		// limpiamos la lista de errores
		clearError();

		// <#ValidateDelete#>

		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones
		if (compFuente == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					EfError.COMPFUENTE_LABEL);
		}

		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el CompFuenteCol. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getCompFuenteColDAO().update(this);
	}

	/**
	 * Desactiva el CompFuenteCol. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getCompFuenteColDAO().update(this);
	}

	/**
	 * Valida la activacion del CompFuenteCol
	 * 
	 * @return boolean
	 */
	private boolean validateActivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	/**
	 * Valida la desactivacion del CompFuenteCol
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	
	/**
	 * Obtiene la la alicuota de la lista, para el periodo y anio pasado como parametro, excluyendo el registro con el id pasado como parametro.
	 * @return null si no encuentra ninguna alicuota para los datos.
	 */
	public AliComFueCol getAliComFueCol(Integer periodo,Integer anio, Long idAliComFueColExcluir,DetAju detAju) {
		Long periodoAnioEvaluar = anio*100L+periodo;
		
		for(AliComFueCol aliComFueCol: listAliComFueCol){
			if(detAju.equals(aliComFueCol.getDetAju())&& (idAliComFueColExcluir==null || !aliComFueCol.getId().equals(idAliComFueColExcluir))){
				Long periodoDesdeAliComFueCol = aliComFueCol.getAnioDesde()*100L+aliComFueCol.getPeriodoDesde();
				Long periodoHastaAliComFueCol = aliComFueCol.getAnioHasta()*100L+aliComFueCol.getPeriodoHasta();
				
				if(periodoAnioEvaluar>=periodoDesdeAliComFueCol && periodoAnioEvaluar<=periodoHastaAliComFueCol)
					return aliComFueCol;
			}
		}
		return null;
	}

	// <#MetodosBeanDetalle#>
	public AliComFueCol getAliComFueColVigente(Integer periodo, Integer anio){
		Long periodoEvaluar = anio*100L+periodo;
		for(AliComFueCol aliComFueCol: listAliComFueCol){
			Long periodoDesdeAliComFueCol = aliComFueCol.getAnioDesde()*100L+aliComFueCol.getPeriodoDesde();
			Long periodoHastaAliComFueCol = aliComFueCol.getAnioHasta()*100L+aliComFueCol.getPeriodoHasta();
			if(periodoDesdeAliComFueCol<=periodoEvaluar &&
					periodoHastaAliComFueCol>=periodoEvaluar){				
				return aliComFueCol;
			}
		}
		return null;
	}

	public CompFuenteColVO toVO4Print()throws Exception{
		CompFuenteColVO compFuenteColVO = (CompFuenteColVO) this.toVO(0, false);
	 	
		return compFuenteColVO;
	}
	
	public CompFuenteColVO toVOForView(DetAju detAju) throws Exception{
		CompFuenteColVO compFuenteColVO = (CompFuenteColVO) this.toVO(1, true);
		
		compFuenteColVO.setListAliComFueCol(new ArrayList<AliComFueColVO>());
		for(AliComFueCol aliComFueCol: listAliComFueCol){
			if(aliComFueCol.getDetAju().equals(detAju)){
				AliComFueColVO aliComFueColVO=(AliComFueColVO) aliComFueCol.toVO(1,false);
				aliComFueColVO.setEsOrdConCueEtur(aliComFueCol.getEsOrdConCueEtur());
				compFuenteColVO.getListAliComFueCol().add(aliComFueColVO);
			}
		}
	
		return compFuenteColVO;
	}
}
