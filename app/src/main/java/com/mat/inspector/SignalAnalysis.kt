package com.mat.inspector

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.transform.DftNormalization
import org.apache.commons.math3.transform.FastFourierTransformer
import org.apache.commons.math3.transform.TransformType
import kotlin.math.sqrt

/*
    obliczanie RMS napięć i prądów, częstotliwości, mocy harmonicznych prądu obciążenia i prądu sieci,
     THD, asymetrii, mocy czynnej, biernej i pozornej, współczynnika mocy).
     Wizualizacja oraz logowanie wyników pracy algorytmów analizy danych.
 */

data class SigData(
    val amplitude: Double?,
    val frequency: Double,
)

fun rms(points: DoubleArray): Double {
    val squares = points.map {
        it * it
    }
    return squares.sum() / squares.size
}

fun fft(vals: DoubleArray): Array<Complex> {
    val fft = FastFourierTransformer(DftNormalization.STANDARD)
    return fft.transform(vals, TransformType.FORWARD)
}

fun getSignalData(samples: DoubleArray, samplingFrequency: Double): SigData {
    val freqBinSpace = samplingFrequency / samples.size
    val fft = fft(samples)
    val fftHalf = fft.dropLast(fft.size / 2 - 1)
    val amps = fftHalf.map {
        it.abs() * 2 / samples.size
    }
    val amplitude = amps.maxOrNull()
    val index = amps.indexOf(amplitude)
    val freq = index * freqBinSpace
    return SigData(amplitude, freq)
}

fun effectiveValue(vals: DoubleArray): Double {
    return sqrt(rms(vals))
}

