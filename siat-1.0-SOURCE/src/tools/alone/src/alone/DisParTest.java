//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package alone; 

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.DisPar;
import ar.gov.rosario.siat.bal.buss.bean.DisParDet;
import ar.gov.rosario.siat.bal.buss.bean.DisParPla;
import ar.gov.rosario.siat.bal.buss.bean.DisParRec;
import ar.gov.rosario.siat.bal.buss.bean.Partida;
import ar.gov.rosario.siat.bal.buss.bean.TipoImporte;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.SerBanRec;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.model.Estado;

public class DisParTest {

	
	public static void main(String[] args) throws Exception {
		System.out.printf("DisParTest\n\n");
		DisParTest main = new DisParTest();
		
		Alone.init(false, false);
		
		main.insertaDisParPLANESGRE();
	}

	 /**
	  *	Inserta los Distribuidores de Partida para Planes Gravamenes Especiales para Testeo. 
	  * @return True si se carg&oacute; correctamente, false si fall&oacute;.
	  */
	public boolean insertaDisParPLANESGRE(){
	   
	   Session session = null;
	   Transaction tx = null; 
	   try{
			session = SiatHibernateUtil.currentSession();
			session.setFlushMode(FlushMode.COMMIT);
			tx = session.beginTransaction();
			ServicioBanco serBanGre = ServicioBanco.getByCodigo("85");
			List<Recurso> listRecurso = new ArrayList<Recurso>();
			System.out.println("armando lista de Recurso: ");
			for(SerBanRec serBanRec :serBanGre.getListSerBanRec()){
				listRecurso.add(serBanRec.getRecurso());
				System.out.print(serBanRec.getRecurso().getCodRecurso()+",");
			}
			Date fechaDesde = DateUtil.getDate("01012009",DateUtil.ddMMYYYY_MASK);
	       	ViaDeuda viaDeuda = ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN);
	       	TipoImporte interesFinanciero = TipoImporte.getById(TipoImporte.ID_INTERES_FINANCIERO);
	       	TipoImporte pagoACuenta = TipoImporte.getById(TipoImporte.ID_PAGO_A_CUENTA);
	       	TipoImporte redondeo = TipoImporte.getById(TipoImporte.ID_REDONDEO);
	       	TipoImporte saldoAReing = TipoImporte.getById(TipoImporte.ID_SALDO_A_REINGRESAR);
	       	
	       	Partida partida = Partida.getByCod("1102");
	       	Partida pagoACtaPar = Partida.getByCod("1103");
	       	Partida saldoPar = Partida.getByCod("4109");
	       	Partida redondeoPar = Partida.getByCod("1104");
	       	
			for(Recurso recurso: listRecurso){
				System.out.println("CodRecurso: "+recurso.getCodRecurso()+" DesRecurso: "+recurso.getDesRecurso());

				boolean saltar = false;
				for(Plan plan: Plan.getListByIdRecurso(recurso.getId())){
					if(!ListUtil.isNullOrEmpty(DisParPla.getListByPlanFecha(plan, new Date()))){
						saltar = true;
						break;
					}
				}
				if(saltar){
					System.out.println("#### YA SE COMPLETO EN CORRIDA ANTERIOR ####");
					continue;
				}
				
				System.out.println("buscando: Distribuidor "+recurso.getDesRecurso());
				DisPar disPar = null;
				if(!ListUtil.isNullOrEmpty(DisParRec.getListByRecursoViaDeudaFecha(recurso, viaDeuda, new Date())))
					disPar = DisParRec.getListByRecursoViaDeudaFecha(recurso, viaDeuda, new Date()).get(0).getDisPar();
				else { 
					System.out.println("#### NO SE ENCONTRO EL DISTRIBUIDOR ####");
					continue;
				}
				
				System.out.println("agregando detalles para tipoImporte Interes Financiero");
				for(RecCon recCon: recurso.getListRecCon()){
					DisParDet disParDet = new DisParDet();
					disParDet.setDisPar(disPar);
					disParDet.setRecCon(recCon);
					disParDet.setPorcentaje(1D);
					disParDet.setTipoImporte(interesFinanciero);
					disParDet.setFechaDesde(fechaDesde);
					disParDet.setEstado(Estado.ACTIVO.getId());
					disParDet.setPartida(partida);
					
					disPar.createDisParDet(disParDet);
				}
				
				System.out.println("agregando detalles para tipoImporte Saldos A Reintegrar");
				DisParDet disParDet = new DisParDet();
				disParDet.setDisPar(disPar);
				disParDet.setRecCon(null);
				disParDet.setPorcentaje(1D);
				disParDet.setTipoImporte(saldoAReing);
				disParDet.setFechaDesde(fechaDesde);
				disParDet.setEstado(Estado.ACTIVO.getId());
				disParDet.setPartida(saldoPar);
				
				disPar.createDisParDet(disParDet);
		
				System.out.println("agregando detalles para tipoImporte Pago A Cuenta");
				disParDet = new DisParDet();
				disParDet.setDisPar(disPar);
				disParDet.setRecCon(null);
				disParDet.setPorcentaje(1D);
				disParDet.setTipoImporte(pagoACuenta);
				disParDet.setFechaDesde(fechaDesde);
				disParDet.setEstado(Estado.ACTIVO.getId());
				disParDet.setPartida(pagoACtaPar);
				
				disPar.createDisParDet(disParDet);
				
				System.out.println("agregando detalles para tipoImporte Redondeo");
				disParDet = new DisParDet();
				disParDet.setDisPar(disPar);
				disParDet.setRecCon(null);
				disParDet.setPorcentaje(1D);
				disParDet.setTipoImporte(redondeo);
				disParDet.setFechaDesde(fechaDesde);
				disParDet.setEstado(Estado.ACTIVO.getId());
				disParDet.setPartida(redondeoPar);
				
				disPar.createDisParDet(disParDet);
		
				System.out.println("asociando con planes relacionados al recurso");
				for(Plan plan: Plan.getListByIdRecurso(recurso.getId())){
					DisParPla disParPla = new DisParPla();
					disParPla.setDisPar(disPar);
					disParPla.setFechaDesde(fechaDesde);
					disParPla.setPlan(plan);
					disParPla.setEstado(Estado.ACTIVO.getId());
					disPar.createDisParPla(disParPla);
				}
		
			}   
	        tx.commit();
			
			
	   }catch ( Exception e ) {
		   System.out.println("Migrania Error en: "+ e);
			return false;
	   } finally {
			SiatHibernateUtil.closeSession();
	   }
		return true;
	}
	
	
}
