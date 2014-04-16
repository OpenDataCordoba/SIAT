//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.bal.buss.bean.DisPar;
import ar.gov.rosario.siat.bal.buss.bean.DisParDet;
import ar.gov.rosario.siat.bal.buss.bean.DisParRec;
import ar.gov.rosario.siat.bal.buss.bean.TipoImporte;
import ar.gov.rosario.siat.def.buss.bean.DomAtrVal;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.ContenedorVO;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.TablaVO;

/**
 * Helper para realizar el reporte de Emision con distribucion por partida estimada.
 *
 */
public class DistribucionReportHelper {

	private Log log = LogFactory.getLog(DistribucionReportHelper.class);

	private Recurso recurso;
	private Date fechaDesde;
	private Date fechaHasta;
		
	private Map<String, Double> mapConcepto = new LinkedHashMap<String, Double>();
	private Map<String, Double> mapPartida = new LinkedHashMap<String, Double>();
	
	public DistribucionReportHelper(Recurso recurso, Date fechaDesde, Date fechaHasta) {
		super();
		this.recurso = recurso;
		this.fechaDesde = fechaDesde;
		this.fechaHasta = fechaHasta;
	}
	
	// Getters y setters
	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
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
	
	public Map<String, Double> getMapPartida() {
		return mapPartida;
	}

	public void setMapPartida(Map<String, Double> mapPartida) {
		this.mapPartida = mapPartida;
	}
	
	public Map<String, Double> getMapConcepto() {
		return mapConcepto;
	}

	public void setMapConcepto(Map<String, Double> mapConcepto) {
		this.mapConcepto = mapConcepto;
	}

	/**
	 *  Genera la tablas con detalle de totales por partidas. (distribucion estimada). Genere una tabla 
	 *  por valor de atributo de asentamiento posible.
	 *  
	 * @return listTablaVO
	 */
	public List<ContenedorVO> generarTablasPartida(){
		//List<TablaVO> listTablaResult = new ArrayList<TablaVO>();
		List<ContenedorVO> listBloqueResult = new ArrayList<ContenedorVO>();
		// . buscar valores posibles para el atributo de asentamiento del recurso (si tienen)
		List<String> listValorAtributo = new ArrayList<String>();
		if(recurso.getAtributoAse() != null){
			for(DomAtrVal domAtrVal: recurso.getAtributoAse().getDomAtr().getListDomAtrVal()){
				listValorAtributo.add(domAtrVal.getStrValor());
			}
		}else{
			listValorAtributo.add(null);
		}
		//	. por cada valor:
		for(String valorAtributo: listValorAtributo){
			mapConcepto = new LinkedHashMap<String, Double>();
			mapPartida = new LinkedHashMap<String, Double>();
			String desValorAtributo = recurso.getAtributoAse().getDomAtr().getDesValorByCodigo(valorAtributo);
			// Bloque para Atributo
			ContenedorVO bloque = new ContenedorVO("Bloque");
			FilaVO filaCabeceraBloque = new FilaVO();	
			filaCabeceraBloque.add(new CeldaVO("Distribución de importes totales de deudas para Cuentas de "+desValorAtributo+". Supone pagos en via administrativa antes del vencimiento", "Aclaracion", "Aclaración"));
			TablaVO tablaTitBlo = new TablaVO("tablaTitulo");
			tablaTitBlo.add(filaCabeceraBloque);
			tablaTitBlo.setTitulo(desValorAtributo);
			bloque.setTablaCabecera(tablaTitBlo);
			
			// 	. buscar distribuidor de partidas para el recurso, via administrativa y valor atributo
			DisPar disPar = null;
			List<DisParRec> listDisParRec =  DisParRec.getListByRecursoViaDeudaFechaAtrVal(recurso, ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN), new Date(), valorAtributo);
			if (listDisParRec.size()== 1  ) {
				// toma el DisParRec
				disPar = listDisParRec.get(0).getDisPar();
			}
			if(disPar == null){
				// TODO ver como disparar error
				continue;
			}
		   	//. calcular total de importe por concepto para deuda en todas las tablas (join de tablas deuda con tablas concepto)			
			//. cargar valores en mapa con clave: 'idConcepto'
			agregarAMapaConcepto(GdeDAOFactory.getDeudaDAO().getConEmiForReport(recurso.getId(),
					"gde_deudaAdmin", "gde_deuAdmRecCon",fechaDesde, fechaHasta, valorAtributo));
			agregarAMapaConcepto(GdeDAOFactory.getDeudaDAO().getConEmiForReport(recurso.getId(),
					"gde_deudaJudicial", "gde_deuJudRecCon",fechaDesde, fechaHasta, valorAtributo));
			agregarAMapaConcepto(GdeDAOFactory.getDeudaDAO().getConEmiForReport(recurso.getId(),
					"gde_deudaCancelada", "gde_deuCanRecCon",fechaDesde, fechaHasta, valorAtributo));
			agregarAMapaConcepto(GdeDAOFactory.getDeudaDAO().getConEmiForReport(recurso.getId(),
					"gde_deudaAnulada", "gde_deuAnuRecCon",fechaDesde, fechaHasta, valorAtributo));
		
			//  . por cada detalle del distribuidor para tipo Importe capital:
			for(DisParDet disParDet: disPar.getListDisParDet()){
				if(disParDet.getRecCon() != null)
					log.debug("iterando disParDet: tipoImporte:"+disParDet.getTipoImporte().getDesTipoImporte()
							+" idRecCon: "+disParDet.getRecCon().getId());
				if(disParDet.getTipoImporte().getId().longValue() == TipoImporte.ID_CAPITAL){
					//	. buscar en el mapa el total de importe para idConcepto correspondiente al detalle
					Double totalConcepto = mapConcepto.get(disParDet.getRecCon().getId().toString());
					log.debug("aplicar porcentaje:"+disParDet.getPorcentaje()
							+" totalConcepto: "+totalConcepto+" partida:"+disParDet.getPartida().getCodDesPartida());

					//  . aplicar porcentaje de distribucion y cargar en un mapa con clave: 'codPartida' el valor distribuido
					if(totalConcepto != null && totalConcepto != 0D){
						Double totalPartida = 0D;
						if(mapPartida.containsKey(disParDet.getPartida().getCodDesPartida())){
							totalPartida = mapPartida.get(disParDet.getPartida().getCodDesPartida());
						}
						
						totalPartida += totalConcepto * disParDet.getPorcentaje();;
						mapPartida.put(disParDet.getPartida().getCodDesPartida(), totalPartida);					
					}
				}
			}
			
			//  . recorrer la lista de claves del mapa, y por cada una crear una fila en una tabla con una columna para 
			//    el codigo de partida y otra para el valor . (ir acumulando el total en una variable)
			//  . cargar tabla en contenedor
			FilaVO filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO("Partida"));
			filaCabecera.add(new CeldaVO("Total"));
			String sufijoTabla = "";
			if(valorAtributo != null)
				sufijoTabla = valorAtributo;
			TablaVO tablacontenido = new TablaVO("Contenido"+sufijoTabla);
			tablacontenido.setFilaCabecera(filaCabecera);

			Double total = 0D; 
			for(String key: mapPartida.keySet()){
				Double totalPartida = mapPartida.get(key);
				log.debug("key:"+key
						+" get(key): "+totalPartida);
				if(totalPartida == null || totalPartida == 0D)
					continue;
				// Genera la fila del reporte
				FilaVO filaContenido = new FilaVO();
				// Partida 
				filaContenido.add(new CeldaVO(key));
				// Total Partida			
				filaContenido.add(new CeldaVO("$"+StringUtil.redondearDecimales(totalPartida, 1, 2)));
				
				tablacontenido.add(filaContenido);
				// Acumulo los total
				total += totalPartida;
			}
			
			// Se agrega la fila con los totales generales
			FilaVO filaPie = new FilaVO();
			filaPie.add(new CeldaVO("TOTAL"));
			filaPie.add(new CeldaVO("$"+StringUtil.redondearDecimales(total, 1, 2)));
						
			tablacontenido.addFilaPie(filaPie);
			bloque.add(tablacontenido);
			listBloqueResult.add(bloque);
			//listTablaResult.add(tablacontenido);

		}
		
		return listBloqueResult;//listTablaResult;
	}
	
	/**
	 * Agrega los importes titales de conceptos al mapa, teniendo en cuenta si ya existen o no, 
	 * actualizando los valores de los totales 
	 * @param listConceptosImporte
	 */
	private void agregarAMapaConcepto(List<Object[]> listConceptosImporte){
		log.debug("agregarConcepto - enter");
		for(Object[] obj: listConceptosImporte){
			Integer idConceptoBD = (Integer) obj[0];
			Long idConcepto = idConceptoBD.longValue();
			Double total = ((BigDecimal) obj[1]).doubleValue();
			log.debug("idConcepto:"+idConcepto+"  total:"+total);
			Double totales = 0D;
			if(mapConcepto.containsKey(idConcepto.toString())){
				totales = mapConcepto.get(idConcepto.toString());
			}
			
			totales += total;
			mapConcepto.put(idConcepto.toString(), totales);
		}
	}
	
}
