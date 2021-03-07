/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.R

val customFont = FontFamily(
    Font(R.font.ibm_plexmono_regular),
    Font(R.font.ibm_plexmono_bold, FontWeight.Bold),
    Font(R.font.ibm_plexmono_italic, style = FontStyle.Italic),
    Font(R.font.ibm_plexmono_medium, FontWeight.Medium),
    Font(R.font.ibm_plexmono_semibold, FontWeight.SemiBold),
    Font(R.font.ibm_plexmono_light, FontWeight.Light)
)

val typography = Typography(
    defaultFontFamily = customFont
)
