package com.example.kalkulator

import org.junit.Test
import org.junit.Assert.*

class CalculatorTest {

    @Test
    fun testKolejnoscDzialan() {
        assertEquals("6", calculateResult("2+2*2"))
    }

    @Test
    fun testNawiasy() {
        assertEquals("8", calculateResult("(2+2)*2"))
    }

    @Test
    fun testDzieleniePrzezZero() {
        assertEquals("Błąd", calculateResult("1/0"))
    }

    @Test
    fun testWyrazenieNiedokanczone() {
        assertEquals("Błąd", calculateResult("2+"))
    }

    @Test
    fun testNawiasNiezamkniety() {
        assertEquals("9", calculateResult("((2+7)"))
    }

    @Test
    fun testPustePole() {
        assertEquals("0", calculateResult(""))
    }

    @Test
    fun testDziedzinaFunkcji() {
        assertEquals("Błąd", calculateResult("√(-1)"))
    }

    @Test
    fun testDuzeLiczby() {
        val result = calculateResult("1234567890*1234567890")
        assertNotEquals("Błąd", result)
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun testSeparatorDziesietnyPrzecinek() {
        assertEquals("3,5", calculateResult("2,5+1"))
    }
}