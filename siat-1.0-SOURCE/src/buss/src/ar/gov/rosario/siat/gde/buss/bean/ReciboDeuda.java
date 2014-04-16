//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.gov.rosario.siat.def.iface.model.AtrEmisionDefinition;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a ReciboDeuda
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_reciboDeuda")
public class ReciboDeuda extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idRecibo")
	private Recibo recibo;

	@Transient
	private Deuda deuda;
	
	@Column(name = "idDeuda")
	private Long idDeuda;

	@Column(name = "capitalOriginal")
	private Double capitalOriginal = 0D;

	@Column(name = "desCapitalOriginal")
	private Double desCapitalOriginal = 0D;

	@Column(name = "totCapital")
	private Double totCapital = 0D;

	@Column(name = "actualizacion")
	private Double actualizacion = 0D;

	@Column(name = "desActualizacion")
	private Double desActualizacion = 0D;

	@Column(name = "totActualizacion")
	private Double totActualizacion = 0D;

	@Column(name = "strConceptosDeuda")
	private String strConceptosDeuda;
	
	@Column(name="rectificativa")
	private Integer rectificativa;

	@Transient
	private Double totalReciboDeuda = 0D;
	
	@Transient
	private String periodoDeuda;
	
	@Transient
	private DesGen desGen;
	
	@Transient
	private DesEsp desEsp;	
	
	@Transient String conceptos;

	@Transient 	// Atributos al momento de la Emision
	private AtrEmisionDefinition atrEmisionDefinition;
	
	// <#Propiedades#>

	// Constructores
	public ReciboDeuda() {
		super();
		// Seteo de valores default
	}

	public ReciboDeuda(Long id) {
		super();
		setId(id);
	}

	// Getters y setters
	public Double getActualizacion() {
		return actualizacion;
	}

	public void setActualizacion(Double actualizacion) {
		this.actualizacion = actualizacion;
	}

	public Double getCapitalOriginal() {
		return capitalOriginal;
	}

	public void setCapitalOriginal(Double capitalOriginal) {
		this.capitalOriginal = capitalOriginal;
	}

	public Double getDesActualizacion() {
		return desActualizacion;
	}

	public void setDesActualizacion(Double desActualizacion) {
		this.desActualizacion = desActualizacion;
	}

	public Double getDesCapitalOriginal() {
		return desCapitalOriginal;
	}

	public void setDesCapitalOriginal(Double desCapitalOriginal) {
		this.desCapitalOriginal = desCapitalOriginal;
	}

	public Long getIdDeuda() {
		return idDeuda;
	}

	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}

	public Recibo getRecibo() {
		return recibo;
	}

	public void setRecibo(Recibo recibo) {
		this.recibo = recibo;
	}

	public String getStrConceptosDeuda() {
		return strConceptosDeuda;
	}

	public void setStrConceptosDeuda(String strConceptosDeuda) {
		this.strConceptosDeuda = strConceptosDeuda;
	}

	public Double getTotActualizacion() {
		return totActualizacion;
	}

	public void setTotActualizacion(Double totActualizacion) {
		this.totActualizacion = totActualizacion;
	}

	public Double getTotCapital() {
		return totCapital;
	}

	public void setTotCapital(Double totCapital) {
		this.totCapital = totCapital;
	}

	public DesEsp getDesEsp() {
		return desEsp;
	}

	public void setDesEsp(DesEsp desEsp) {
		this.desEsp = desEsp;
	}

	public DesGen getDesGen() {
		return desGen;
	}

	public void setDesGen(DesGen desGen) {
		this.desGen = desGen;
	}

	public Double getTotalReciboDeuda() {
		return totalReciboDeuda;
	}

	public void setTotalReciboDeuda(Double totalReciboDeuda) {
		this.totalReciboDeuda = totalReciboDeuda;
	}

	public String getPeriodoDeuda() {
		return periodoDeuda;
	}

	public void setPeriodoDeuda(String periodoDeuda) {
		this.periodoDeuda = periodoDeuda;
	}

	public String getConceptos() {
		return conceptos;
	}

	public void setConceptos(String conceptos) {
		this.conceptos = conceptos;
	}
	
	public Integer getRectificativa() {
		return rectificativa;
	}

	public void setRectificativa(Integer rectificativa) {
		this.rectificativa = rectificativa;
	}

	public AtrEmisionDefinition getAtrEmisionDefinition() {
		return atrEmisionDefinition;
	}

	public void setAtrEmisionDefinition(AtrEmisionDefinition atrEmisionDefinition) {
		this.atrEmisionDefinition = atrEmisionDefinition;
	}

	/**
	 * 
	 * Este metodo Devuelve la deuda que corresponda.
	 * 
	 * @author Tecso
	 * @return
	 */
	public Deuda getDeuda() {
		this.setDeuda(Deuda.getById( this.getIdDeuda() ));
		return this.deuda;
	}

	public void setDeuda(Deuda deuda) {
		this.deuda = deuda;
	}
	
	/**
	 * 
	 * @return Un DesGen ó DesEsp (Si uno es nulo, el otro no puede serlo, ambas propiedades son mutuamente excluyentes)
	 */
	public Object getDescuento(){
		if(desEsp==null)
			return desGen;
		return desEsp;
	}

	/**
	 * Devuelve la lista de RecCon para la deuda del reciboDeuda.
	 * Va por DAO, no está mapeado (por performance)
	 * @return
	 */
	public List<DeuAdmRecCon> getListDeuAdmRecCon(){
		return GdeDAOFactory.getReciboDeudaDAO().getListDeuAdmRecCon(idDeuda);
	}
	
	// Metodos de Clase
	public static ReciboDeuda getById(Long id) {
		return (ReciboDeuda) GdeDAOFactory.getReciboDeudaDAO().getById(id);
	}

	public static ReciboDeuda getByIdNull(Long id) {
		return (ReciboDeuda) GdeDAOFactory.getReciboDeudaDAO().getByIdNull(id);
	}

	public static ReciboDeuda getByReciboYDeuda(Recibo recibo, Deuda deuda) {
		return (ReciboDeuda) GdeDAOFactory.getReciboDeudaDAO().getByReciboYDeuda(recibo, deuda);
	}

	public static List<ReciboDeuda> getList() {
		return (ArrayList<ReciboDeuda>) GdeDAOFactory.getReciboDeudaDAO().getList();
	}

	public static List<ReciboDeuda> getListActivos() {
		return (ArrayList<ReciboDeuda>) GdeDAOFactory.getReciboDeudaDAO()
				.getListActiva();
	}

	public static List<ReciboDeuda> get(List<Procurador> listProcurador, Long idViaDeuda, Date fechaPagoDesde, Date fechaPagoHasta, boolean noLiquidados) throws Exception{
		return GdeDAOFactory.getReciboDeudaDAO().get(listProcurador, idViaDeuda, fechaPagoDesde, fechaPagoHasta, noLiquidados);
	}
	
	 public static List<ReciboDeuda> ordenarImporteCero(List<ReciboDeuda> listReciboDeuda){
	    	Comparator<ReciboDeuda> comparator = new Comparator<ReciboDeuda>(){
				public int compare(ReciboDeuda d1, ReciboDeuda d2) {
					// Se compara el anio
					if(d1.getTotalReciboDeuda()>0 && d2.getTotalReciboDeuda()==0D){
						return -1;
					}else if(d1.getTotalReciboDeuda()==0D && d2.getTotalReciboDeuda()>0D){
						return 1;
					}else{
						// Se compara el periodo
						if(d1.getDeuda().getAnio().longValue()>d2.getDeuda().getAnio().longValue()){
							return 1;
						}else if(d1.getDeuda().getAnio().longValue()<d2.getDeuda().getAnio().longValue()){
							return -1;
						}else{
							if(d1.getDeuda().getPeriodo().longValue()>d2.getDeuda().getPeriodo().longValue()){
								return 1;
							}else if(d1.getDeuda().getPeriodo().longValue()<d2.getDeuda().getPeriodo().longValue()){
								return -1;
							}
						}
					}
					//Si son iguales
					return 0;
				}    		
	    	};    	
	    	Collections.sort(listReciboDeuda, comparator);
	    	return listReciboDeuda;
	    }
	
/*
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
		if (StringUtil.isNullOrEmpty(getCodReciboDeuda())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					GdeError.RECIBODEUDA_CODRECIBODEUDA);
		}

		if (StringUtil.isNullOrEmpty(getDesReciboDeuda())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					GdeError.RECIBODEUDA_DESRECIBODEUDA);
		}

		if (hasError()) {
			return false;
		}

		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codReciboDeuda");
		if (!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO,
					GdeError.RECIBODEUDA_CODRECIBODEUDA);
		}

		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el ReciboDeuda. Previamente valida la activacion.
	 * 
	 */
/*	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getReciboDeudaDAO().update(this);
	}

	/**
	 * Desactiva el ReciboDeuda. Previamente valida la desactivacion.
	 * 
	 */
/*	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getReciboDeudaDAO().update(this);
	}
*/
	/**
	 * Valida la activacion del ReciboDeuda
	 * 
	 * @return boolean
	 */
/*	private boolean validateActivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	/**
	 * Valida la desactivacion del ReciboDeuda
	 * 
	 * @return boolean
	 */
/*	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}
*/
	// <#MetodosBeanDetalle#>
	/**
	 * pregunta si el recibo es Cero
	 * 
	 * @return boolean
	 */
	public boolean isValorCero() {
		Boolean rta=false;
       if ((this.getDeuda().getSaldo().doubleValue()==0D || deuda.getConvenio()!=null) && this.getDeuda().getRecurso().getEsAutoliquidable()!=null
    		   && this.getDeuda().getRecurso().getEsAutoliquidable()==1) rta=true;
		
		return rta;
	}


}
