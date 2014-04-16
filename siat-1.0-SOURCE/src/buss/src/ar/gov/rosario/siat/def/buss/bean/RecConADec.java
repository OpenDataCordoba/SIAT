//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.model.RecConADecVO;
import ar.gov.rosario.siat.def.iface.model.RecTipUniVO;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean correspondiente a RecConADec
 * Indica los valores de un concepto a declarar de un recurso autoliquidable
 * 
 * @author tecso
 */

@Entity
@Table(name = "def_recConADec")
public class RecConADec extends BaseBO {

	private static final long serialVersionUID = 1L;

	// Se necesitan para sincronismo con AFIP
	public static final Long ID_CIBER1 = 900L;
	public static final Long ID_CIBER2 = 901L;
	public static final Long ID_CIBER3 = 902L;
	public static final Long ID_BAILABLE1 = 894L;
	public static final Long ID_BAILABLE2 = 895L;
	public static final Long ID_BAILABLE3 = 896L;
	public static final Long ID_BAILABLE4 = 897L;
	public static final Long ID_BAILABLE5 = 898L;
	public static final Long ID_BAILABLE6 = 899L;
	
	@ManyToOne()  
    @JoinColumn(name="idRecurso")
	private Recurso recurso;
	
	@ManyToOne()
	@JoinColumn(name="idTipRecConADec")
	private TipRecConADec tipRecConADec;
	
	@ManyToOne()
	@JoinColumn(name="idRecTipUni")
	RecTipUni recTipUni;
	
    @Column(name="codConcepto")
	private String codConcepto;
	
	@Column(name = "desConcepto")
	private String desConcepto;
	
	@Column(name="observacion")
	private String observacion;
	
	@Column(name="fechaDesde")
	private Date fechaDesde;
	
	@Column(name="fechaHasta")
	private Date fechaHasta;
	
	@Column(name="codigoAfip")
	private String codigoAfip;
	
	@OneToMany(mappedBy="recConADec", fetch=FetchType.LAZY)
	@JoinColumn(name="idrecConADec")
	private List<ValUnRecConADe> listValUnRecConADe;
	
	
	
	// Constructores
	public RecConADec(){
		super();
	}
	// Getters y Setters
	public Recurso getRecurso(){
		return recurso;
	}
	public void setRecurso(Recurso recurso){
		this.recurso = recurso;
	}
	
	public TipRecConADec getTipRecConADec() {
		return tipRecConADec;
	}
	public void setTipRecConADec(TipRecConADec tipRecConADec) {
		this.tipRecConADec = tipRecConADec;
	}
	public String getCodConcepto() {
		return codConcepto;
	}
	public void setCodConcepto(String codConcepto) {
		this.codConcepto = codConcepto;
	}
	public String getDesConcepto() {
		return desConcepto;
	}
	public void setDesConcepto(String desConcepto) {
		this.desConcepto = desConcepto;
	}
	
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}	
	
	public List<ValUnRecConADe> getListValUnRecConADe() {
		return listValUnRecConADe;
	}
	public void setListValUnRecConADe(List<ValUnRecConADe> listValUnRecConADe) {
		this.listValUnRecConADe = listValUnRecConADe;
	}
	
	public RecTipUni getRecTipUni() {
		return recTipUni;
	}
	public void setRecTipUni(RecTipUni recTipUni) {
		this.recTipUni = recTipUni;
	}
	
	public String getCodigoAfip() {
		return codigoAfip;
	}
	public void setCodigoAfip(String codigoAfip) {
		this.codigoAfip = codigoAfip;
	}
	
	// Metodos de Clase
	public static RecConADec getById(Long id) {
		return (RecConADec) DefDAOFactory.getRecConADecDAO().getById(id);  
	}
	
	public static RecConADec getByIdNull(Long id) {
		return (RecConADec) DefDAOFactory.getRecConADecDAO().getByIdNull(id);
	}
	
	public static List<RecConADec> getList() {
		return (List<RecConADec>) DefDAOFactory.getRecConADecDAO().getList();
	}
	
	public static List<RecConADec> getListActivos() {			
		return (List<RecConADec>) DefDAOFactory.getRecConADecDAO().getListActiva();
	}
	
	public static List<RecConADec> getListByIdRecurso(Long id){
		return (List<RecConADec>) DefDAOFactory.getRecConADecDAO().getListByIdRecurso(id);
	}
	
	public static RecConADec getByCodConceptoRecurso(Long idRecurso, String codConcepto) throws Exception{
		return (RecConADec)DefDAOFactory.getRecConADecDAO().getByCodConceptoRecurso(idRecurso, codConcepto);
	}

	
	public static List<RecConADec> getByListCodConceptoRecurso(Long idRecurso, String strConceptos) throws Exception {
		return (List<RecConADec>) DefDAOFactory.getRecConADecDAO().getByListCodConceptoRecurso(idRecurso, strConceptos);
		
	}
	
	public static List<RecConADec> getListVigenteByRecurso(Long idRecurso, Date fecha, Long idTipo) throws Exception{
		return (List<RecConADec>)DefDAOFactory.getRecConADecDAO().getListVigenteByIdRecurso(idRecurso, fecha, idTipo);
	}

	/** 
	 *  Devuelve el registro de Tipo de Unidad segun el codigo de sincronismo afip indicado.
	 * 
	 * @param codigoAfip
	 * @return
	 */
	public static RecConADec getByCodigoAfip(String codigoAfip) {
		return (RecConADec) DefDAOFactory.getRecConADecDAO().getByCodigoAfip(codigoAfip);  
	}
	
	// Metodos de Instancia
	public ValUnRecConADe getValUnRecConADeVigente(Date fecha){
		return DefDAOFactory.getValUnRecConADeDAO().getListVigenteByFechaYRecConADe(fecha, this);
	}
	
	public ValUnRecConADe getVigenteByFechaYValRef(Date fecha, Double valRef){
		return (ValUnRecConADe) DefDAOFactory.getValUnRecConADeDAO().getVigenteByFechaYRecConADeYValRef(fecha, this, valRef);
	}

	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
		
		if (hasError()) {
			return false;
		}

				
		return !hasError();
	}
	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
	
		if (hasError()) {
			return false;
		}

			
		return !hasError();
	}

	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos
		if (getRecurso()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCONADEC_RECURSO);
		}
		
		if (getTipRecConADec()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCONADEC_TIPRECCONADEC);
		}
		
		if (getCodConcepto()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCONADEC_CODCONCEPTO);
		}
		
		if (getDesConcepto()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCONADEC_DESCONCEPTO);
		}
		
		if (getFechaDesde()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCONADEC_FECHADESDE);
		}
	
		if (hasError()) {
			return false;
		}
		// Validaciones de Unicidad
		if (!StringUtil.isNullOrEmpty(this.codConcepto)){
			if(DefDAOFactory.getRecConADecDAO().getEsCodDuplicado(this.recurso.getId(), this.codConcepto)) 
				addRecoverableError(BaseError.MSG_CAMPO_UNICO, DefError.RECCONADEC_CODCONCEPTO);
		}
		// Otras Validaciones
		if(!DateUtil.isDateBefore(this.fechaDesde, this.fechaHasta)){
			addRecoverableError(BaseError.MSG_VALORMAYORQUE, DefError.RECCONADEC_FECHADESDE, DefError.RECCONADEC_FECHAHASTA);
		}
		// Valida que la Fecha Desde no sea menor que la fecha Alta del Recurso
		if(!DateUtil.isDateBeforeOrEqual(this.getRecurso().getFechaAlta(), this.fechaDesde)){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, DefError.RECCONADEC_FECHADESDE, DefError.RECCONADEC_FECHAHASTA);
		}
		
		return !hasError();
	}

	
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO
		if (GdeDAOFactory.getDecJurDetDAO().getRecConADecHasReference(this)){
			addRecoverableError(DefError.RECCONADEC_TIENE_REFERENCIA);
		}
		
		if (getListValUnRecConADe()!=null && getListValUnRecConADe().size()>0){
			addRecoverableError(DefError.RECCONADEC_EXISTE_LISTA_VAL);
		}

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		
		return true;
	}

	// Metodos de negocio

	
	/**
	 * toVO liviano para Declaracion Jurada Masiva
	 * 
	 */
	public RecConADecVO toVO4Map() throws Exception {
		
		RecConADecVO recConADecVO = (RecConADecVO) this.toVO(0, false); 
		
		recConADecVO.setListValUnRecConADe(ListUtilBean.toVO(this.getListValUnRecConADe(), 0));
		recConADecVO.setRecTipUni((RecTipUniVO) this.getRecTipUni().toVO(0, false));
		
		return recConADecVO;
	}
	
}
