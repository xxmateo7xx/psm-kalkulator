package com.example.kalkulator

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val CustomLightScheme = lightColorScheme(
    primary = Color(0xFF4F46E5),
    secondary = Color(0xFFF59E0B),
    tertiary = Color(0xFF6B7280),
    surface = Color(0xFF38BDF8),
    background = Color(0xFFF1F5F9),
    onSurface = Color(0xFF0F172A)
)

private val CustomDarkScheme = darkColorScheme(
    primary = Color(0xFF6366F1),
    secondary = Color(0xFFF59E0B),
    tertiary = Color(0xFF374151),
    surface = Color(0xFF0284C7),
    background = Color(0xFF0F172A),
    onSurface = Color(0xFFF8FAFC)
)

enum class ButtonType { NUMBER, OPERATOR, SCIENTIFIC, ACTION }

data class CalcButton(
    val label: String,
    val textToInsert: String = label,
    val type: ButtonType = ButtonType.NUMBER
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDark = isSystemInDarkTheme()
            val colors = if (isDark) CustomDarkScheme else CustomLightScheme

            MaterialTheme(colorScheme = colors) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KalkulatorApp()
                }
            }
        }
    }
}

@Composable
fun KalkulatorApp() {
    var displayExpression by rememberSaveable { mutableStateOf("") }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isDark = isSystemInDarkTheme()

    val basicButtons = listOf(
        listOf(CalcButton("AC", type = ButtonType.ACTION), CalcButton("C", type = ButtonType.ACTION), CalcButton("(", type = ButtonType.OPERATOR), CalcButton(")", type = ButtonType.OPERATOR)),
        listOf(CalcButton("7"), CalcButton("8"), CalcButton("9"), CalcButton("/", type = ButtonType.OPERATOR)),
        listOf(CalcButton("4"), CalcButton("5"), CalcButton("6"), CalcButton("*", type = ButtonType.OPERATOR)),
        listOf(CalcButton("1"), CalcButton("2"), CalcButton("3"), CalcButton("-", type = ButtonType.OPERATOR)),
        listOf(CalcButton("0"), CalcButton(","), CalcButton("=", type = ButtonType.OPERATOR), CalcButton("+", type = ButtonType.OPERATOR))
    )

    val scientificButtons = listOf(
        listOf(CalcButton("sin", "sin(", ButtonType.SCIENTIFIC), CalcButton("cos", "cos(", ButtonType.SCIENTIFIC)),
        listOf(CalcButton("tan", "tan(", ButtonType.SCIENTIFIC), CalcButton("ln", "ln(", ButtonType.SCIENTIFIC)),
        listOf(CalcButton("log", "log(", ButtonType.SCIENTIFIC), CalcButton("√", "√(", ButtonType.SCIENTIFIC)),
        listOf(CalcButton("x²", "^2", ButtonType.SCIENTIFIC), CalcButton("xⁿ", "^", ButtonType.SCIENTIFIC)),
        listOf(CalcButton("π", "π", ButtonType.SCIENTIFIC), CalcButton("e", "e", ButtonType.SCIENTIFIC))
    )

    val onButtonClick: (CalcButton) -> Unit = { btn ->
        when (btn.label) {
            "AC" -> displayExpression = ""
            "C" -> if (displayExpression.isNotEmpty()) displayExpression = displayExpression.dropLast(1)
            "=" -> { }
            else -> displayExpression += btn.textToInsert
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(if (isLandscape) 0.25f else 0.3f)
                .background(if (isDark) Color(0xFF1E293B) else Color.White, RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = if (displayExpression.isEmpty()) "0" else displayExpression,
                fontSize = if (isLandscape) 26.sp else 36.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Color.White else Color.Black,
                textAlign = TextAlign.End,
                maxLines = 1,
                modifier = Modifier.horizontalScroll(rememberScrollState())
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.weight(if (isLandscape) 0.75f else 0.7f)) {
            if (isLandscape) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(modifier = Modifier.weight(0.4f)) {
                        ButtonGrid(grid = scientificButtons, onClick = onButtonClick)
                    }
                    Box(modifier = Modifier.weight(0.6f)) {
                        ButtonGrid(grid = basicButtons, onClick = onButtonClick)
                    }
                }
            } else {
                ButtonGrid(grid = basicButtons, onClick = onButtonClick)
            }
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
                    val containerColor = when (btn.type) {
                        ButtonType.NUMBER -> MaterialTheme.colorScheme.surface
                        ButtonType.OPERATOR -> MaterialTheme.colorScheme.secondary
                        ButtonType.SCIENTIFIC -> MaterialTheme.colorScheme.tertiary
                        ButtonType.ACTION -> MaterialTheme.colorScheme.primary
                    }

                    Button(
                        onClick = { onClick(btn) },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = containerColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = btn.label,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}