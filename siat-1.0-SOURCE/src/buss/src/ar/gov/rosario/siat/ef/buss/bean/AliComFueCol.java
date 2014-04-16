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
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.bean.RecConADec;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a AliComFueCol
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_aliComFueCol")
public class AliComFueCol extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCompFuenteCol")
	private CompFuenteCol compFuenteCol;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idDetAju")
	private DetAju detAju;
	
	@Column(name = "valorAlicuota")
	private Double valorAlicuota;
	
	@Column(name = "periodoDesde")
	private Integer periodoDesde;

	@Column(name = "anioDesde")
	private Integer anioDesde;

	@Column(name = "periodoHasta")
	private Integer periodoHasta;

	@Column(name = "anioHasta")
	private Integer anioHasta;
	
	@Column(name="cantidad")
	private Double cantidad;
	
	@Column(name="valorUnitario")
	private Double valorUnitario;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idRecConADec")
	private RecConADec tipoUnidad;
	
	@Column(name="radio")
	private Integer radio;

	// <#Propiedades#>

	// Constructores
	public AliComFueCol() {
		super();
		// Seteo de valores default
	}

	public AliComFueCol(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static AliComFueCol getById(Long id) {
		return (AliComFueCol) EfDAOFactory.getAliComFueColDAO().getById(id);
	}

	public static AliComFueCol getByIdNull(Long id) {
		return (AliComFueCol) EfDAOFactory.getAliComFueColDAO().getByIdNull(id);
	}

	public static List<AliComFueCol> getList() {
		return (ArrayList<AliComFueCol>) EfDAOFactory.getAliComFueColDAO()
				.getList();
	}

	public static List<AliComFueCol> getListActivos() {
		return (ArrayList<AliComFueCol>) EfDAOFactory.getAliComFueColDAO()
				.getListActiva();
	}

	// Getters y setters
	public DetAju getDetAju() {
		return detAju;
	}

	public void setDetAju(DetAju detAju) {
		this.detAju = detAju;
	}
	
	public CompFuenteCol getCompFuenteCol() {
		return compFuenteCol;
	}

	public void setCompFuenteCol(CompFuenteCol compFuenteCol) {
		this.compFuenteCol = compFuenteCol;
	}

	public Integer getPeriodoDesde() {
		return periodoDesde;
	}

	public void setPeriodoDesde(Integer periodoDesde) {
		this.periodoDesde = periodoDesde;
	}

	public Integer getAnioDesde() {
		return anioDesde;
	}

	public void setAnioDesde(Integer anioDesde) {
		this.anioDesde = anioDesde;
	}

	public Integer getPeriodoHasta() {
		return periodoHasta;
	}

	public void setPeriodoHasta(Integer periodoHasta) {
		this.periodoHasta = periodoHasta;
	}

	public Integer getAnioHasta() {
		return anioHasta;
	}

	public void setAnioHasta(Integer anioHasta) {
		this.anioHasta = anioHasta;
	}

	public Double getValorAlicuota() {
		return valorAlicuota;
	}

	public void setValorAlicuota(Double valor) {
		this.valorAlicuota = valor;
	}

	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public Double getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(Double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public RecConADec getTipoUnidad() {
		return tipoUnidad;
	}

	public void setTipoUnidad(RecConADec tipoUnidad) {
		this.tipoUnidad = tipoUnidad;
	}

	public Integer getRadio() {
		return radio;
	}

	public void setRadio(Integer radio) {
		this.radio = radio;
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
		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el AliComFueCol. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getAliComFueColDAO().update(this);
	}

	/**
	 * Desactiva el AliComFueCol. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getAliComFueColDAO().update(this);
	}

	/**
	 * Valida la activacion del AliComFueCol
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
	 * Valida la desactivacion del AliComFueCol
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	// <#MetodosBeanDetalle#>
	
	// metodos para reporte
	public String getValorAlicuotaView4Report(){
		if(valorAlicuota!=null){
			double valorRet = valorAlicuota*100;
			int cantDecimales;
			
			if(valorRet>1){
				// es %
				cantDecimales = 2;
			}else{
				// es "por mil"
				cantDecimales = 3;
			}
			
			return StringUtil.redondearDecimales(valorRet, 1, cantDecimales)+" %";
		}else{
			return "";
		}
	}
	
	public boolean getEsOrdConCueEtur (){
		if(this.getDetAju()!=null && Recurso.getETur().equals(this.getDetAju().getOrdConCue().getCuenta().getRecurso()))
			return true;
		else
			return false;
	}
}
