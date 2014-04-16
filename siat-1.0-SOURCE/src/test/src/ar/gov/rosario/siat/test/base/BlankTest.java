//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.test.base;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import coop.tecso.demoda.iface.model.SiNo;

import ar.gov.rosario.siat.test.TestUtil;

public class BlankTest {
	
	@BeforeClass
	public static void oneTimeBefore() {
		TestUtil.initSiat();
	}	

	@AfterClass
	public static void oneTimeAfter() {
	}

	@Before
	public void before() {
	}

	@After
	public void after() {
	}

	@Test
	public void testInitSiat() {
		assertTrue(TestUtil.initSiat());
	}

	@Test
	public void testEquals() {
		System.out.println(SiNo.SI.equals(1));
		System.out.println(SiNo.SI.getId().equals(1));
		
		assertTrue(SiNo.SI.equals(1));
	}

}
