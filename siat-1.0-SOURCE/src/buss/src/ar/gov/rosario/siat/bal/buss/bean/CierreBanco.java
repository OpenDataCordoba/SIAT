//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;


import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Tipo Operacion de transacciones de Osiris 
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_cierreBanco")
public class CierreBanco extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idEnvioOsiris")
	private EnvioOsiris envioOsiris;
	
	@Column(name="banco")
	private Integer banco;
	
	@Column(name="cantTransaccion")
	private Long cantTransaccion;
	
	@Column(name="importeTotal")
	private Double importeTotal;
		
	@Column(name="nroCierreBanco")
	private Integer nroCierreBanco;
	
	@Column(name="fechaCierre")
	private Date fechaCierre;
	
	@Column(name = "conciliado")
	private Integer conciliado;
	
	@OneToMany(mappedBy="cierreBanco",fetch=FetchType.LAZY)
	@JoinColumn(name="idCierreBanco")
	private List<TranAfip> listTranAfip;

	@OneToMany(mappedBy="cierreBanco",fetch=FetchType.LAZY)
	@JoinColumn(name="idCierreBanco")
	@OrderBy("tipoNota")
	private List<NotaImpto> listNotaImpto;

	@OneToMany(mappedBy="cierreBanco",fetch=FetchType.LAZY)
	@JoinColumn(name="idCierreBanco")
	private List<NovedadEnvio> listNovedadEnvio;

	@OneToMany(mappedBy="cierreBanco",fetch=FetchType.LAZY)
	@JoinColumn(name="idCierreBanco")
	private List<CierreSucursal> listCierreSucursal;

	// Constructores
	public CierreBanco(){
		super();
	}
	
	
	//Metodos de clase
	public static CierreBanco getById(Long id) {
		return (CierreBanco) BalDAOFactory.getCierreBancoDAO().getById(id);  
	}
	
	public static CierreBanco getByIdNull(Long id) {
		return (CierreBanco) BalDAOFactory.getCierreBancoDAO().getByIdNull(id);
	}
	
	public static List<CierreBanco> getList() {
		return (List<CierreBanco>) BalDAOFactory.getCierreBancoDAO().getList();
	}
	
	public static List<CierreBanco> getListActivos() {			
		return (List<CierreBanco>) BalDAOFactory.getCierreBancoDAO().getListActiva();
	}
	

	//Getters y Setters
	public EnvioOsiris getEnvioOsiris() {
		return envioOsiris;
	}


	public void setEnvioOsiris(EnvioOsiris envioOsiris) {
		this.envioOsiris = envioOsiris;
	}


	public Integer getBanco() {
		return banco;
	}


	public void setBanco(Integer banco) {
		this.banco = banco;
	}


	public Long getCantTransaccion() {
		return cantTransaccion;
	}


	public void setCantTransaccion(Long cantTransaccion) {
		this.cantTransaccion = cantTransaccion;
	}


	public Double getImporteTotal() {
		return importeTotal;
	}


	public void setImporteTotal(Double importeTotal) {
		this.importeTotal = importeTotal;
	}


	
	public Integer getNroCierreBanco() {
		return nroCierreBanco;
	}


	public void setNroCierreBanco(Integer nroCierreBanco) {
		this.nroCierreBanco = nroCierreBanco;
	}


	public Date getFechaCierre() {
		return fechaCierre;
	}


	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public Integer getConciliado() {
		return conciliado;
	}

	public void setConciliado(Integer conciliado) {
		this.conciliado = conciliado;
	}

	public void setListTranAfip(List<TranAfip> listTranAfip) {
		this.listTranAfip = listTranAfip;
	}


	public List<TranAfip> getListTranAfip() {
		return listTranAfip;
	}

	public List<CierreSucursal> getListCierreSucursal() {
		return listCierreSucursal;
	}

	public void setListCierreSucursal(List<CierreSucursal> listCierreSucursal) {
		this.listCierreSucursal = listCierreSucursal;
	}

	public List<NotaImpto> getListNotaImpto() {
		return listNotaImpto;
	}

	public void setListNotaImpto(List<NotaImpto> listNotaImpto) {
		this.listNotaImpto = listNotaImpto;
	}

	public List<NovedadEnvio> getListNovedadEnvio() {
		return listNovedadEnvio;
	}

	public void setListNovedadEnvio(List<NovedadEnvio> listNovedadEnvio) {
		this.listNovedadEnvio = listNovedadEnvio;
	}
	
	public List<NovedadEnvio> getListNovedadForRechazoCheque() {
		//Las novedades de rechazo de cheques se determinar con tipoOperacion=5
		Long tipoOperacion = 5L;
		return (List<NovedadEnvio>) BalDAOFactory.getNovedadEnvioDAO().getListByCierreBancoAndTipoOperacion(this.getId(), tipoOperacion);
	}

	/**
	 * Lista de Notas de Obligacion asociadas al cierre banco que se esta por conciliar
	 * @return
	 */
	public List<EnvNotObl> getListEnvNotObl(){
		return BalDAOFactory.getEnvNovOblDAO().getListEnvNotOblForCierreBanco(this);
	}
	
	/**
	 * Obtiene la sumatoria de totalAcreditado para las EnvNovObl asociadas al cierreBanco
	 * @return
	 */
	public Double getTotalAcreditado(){
		return BalDAOFactory.getEnvNovOblDAO().getTotalAcreditado(this);
	}
	
	/**
	 * Obtiene la sumatoria de importePago para los DetallePago asociados al cierreBanco
	 * @return
	 */
	public Double getTotalImporteDetallePago(){
		return BalDAOFactory.getDetallePagoDAO().getMontoTotalDetalleForCierreBanco(this);
	}
	
	/**
	 * Obtiene la sumatoria de totalCredito para los EnvNotObl asociados al cierreBanco
	 * @return
	 */
	public Double getTotalRendido(){
		return BalDAOFactory.getEnvNovOblDAO().getTotalRendidoForCierreBanco(this);
	}
	
	/**
	 * Obtiene la sumatoria de totMontoIngresado para las tranAfip asociadas al banco y nroCierreBanco
	 * @return
	 */
	public Double getTotalImportePago(){
		return BalDAOFactory.getTranAfipDAO().getTotalImportePagoForCierreBanco(this);
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
		//clearError();

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
	// Metodos de Instancia
	
}
