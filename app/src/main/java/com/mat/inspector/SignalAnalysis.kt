package com.mat.inspector

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.transform.DftNormalization
import org.apache.commons.math3.transform.FastFourierTransformer
import org.apache.commons.math3.transform.TransformType
import kotlin.math.abs
import kotlin.math.sqrt

/*
    obliczanie RMS napięć i prądów, częstotliwości, mocy harmonicznych prądu obciążenia i prądu sieci,
     THD, asymetrii, mocy czynnej, biernej i pozornej, współczynnika mocy).
     Wizualizacja oraz logowanie wyników pracy algorytmów analizy danych.
 */

data class SigData(
    val amplitude: Double,
    val frequency: Double,
    val shift: Double = 0.0,
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

fun getSignalData(samplingFrequency: Double, fftResults: Array<Complex>): SigData {
    val freqBinSpace = samplingFrequency / fftResults.size
    val fftHalf = fftResults.dropLast(fftResults.size / 2 - 1)
    val amps = fftHalf.map {
        it.abs() * 2 / fftResults.size
    }
    val amplitude = amps.maxOrNull() ?: 0.0
    val index = amps.indexOf(amplitude)
    val freq = index * freqBinSpace
    val shift = cosShiftToSin(fftHalf[index].argument)
    return SigData(amplitude, freq, shift)
}

fun effectiveValue(vals: DoubleArray): Double {
    return sqrt(rms(vals))
}

fun cosShiftToSin(shift: Double): Double {
    return shift + 0.5 % 2
}

fun thd(samplingFrequency: Double, fftResults: Array<Complex>, harmonicsCnt: Int): Double {
    val freqBinSpace = samplingFrequency / fftResults.size
    val fftHalf = fftResults.dropLast(fftResults.size / 2 - 1)
    val amps = fftHalf.map {
        it.abs() * 2 / fftResults.size
    }
    val firstHarmonicFreq = 50.0
    val harmonicsAmps = mutableListOf<Double>()
    for (i in 2..harmonicsCnt) {
        val curHarmonicFreq = i * firstHarmonicFreq
        val curHarmonicIndex = closestValue(freqBinSpace, curHarmonicFreq)
        harmonicsAmps.add(amps[curHarmonicIndex])
    }
    val sumOfSquares = harmonicsAmps.fold(0.0) { _, value ->
        value * value
    }
    return sqrt(sumOfSquares) / amps[closestValue(freqBinSpace, firstHarmonicFreq)]
}

fun closestValue(bin: Double, value: Double): Int {
    val il: Int = (value / bin).toInt()
    return if (abs(value - bin*il) < abs(value - bin*(il+1))) il else il + 1
}
