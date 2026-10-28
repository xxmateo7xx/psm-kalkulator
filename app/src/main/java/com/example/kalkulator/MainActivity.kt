package com.example.kalkulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White
            ) {
                KalkulatorApp()
            }
        }
    }
}

data class CalcButton(
    val label: String,
    val textToInsert: String = label
)

@Composable
fun KalkulatorApp() {
    var displayExpression by rememberSaveable { mutableStateOf("") }

    val basicButtons = listOf(
        listOf(CalcButton("AC"), CalcButton("C"), CalcButton("("), CalcButton(")")),
        listOf(CalcButton("7"), CalcButton("8"), CalcButton("9"), CalcButton("/")),
        listOf(CalcButton("4"), CalcButton("5"), CalcButton("6"), CalcButton("*")),
        listOf(CalcButton("1"), CalcButton("2"), CalcButton("3"), CalcButton("-")),
        listOf(CalcButton("0"), CalcButton(","), CalcButton("="), CalcButton("+"))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // 1. POLE DZIAŁANIA (jasnoszare tło)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
                .background(Color(0xFFEEEEEE), RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = if (displayExpression.isEmpty()) "0" else displayExpression,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.End,
                maxLines = 1,
                modifier = Modifier.horizontalScroll(rememberScrollState())
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2. SIATKA PRZYCISKÓW (Szare przyciski)
        Box(modifier = Modifier.weight(0.7f)) {
            ButtonGrid(
                grid = basicButtons,
                onClick = { btn ->
                    when (btn.label) {
                        "AC" -> displayExpression = ""
                        "C" -> if (displayExpression.isNotEmpty()) displayExpression = displayExpression.dropLast(1)
                        "=" -> { /* Obliczenia będą w Lab 5 */ }
                        else -> displayExpression += btn.textToInsert
                    }
                }
            )
        }
    }
}

@Composable
fun ButtonGrid(grid: List<List<CalcButton>>, onClick: (CalcButton) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        for (row in grid) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                for (btn in row) {
                    Button(
                        onClick = { onClick(btn) },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFDDDDDD),
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = btn.label,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}