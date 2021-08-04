package com.mat.inspector

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.transform.DftNormalization
import org.apache.commons.math3.transform.FastFourierTransformer
import org.apache.commons.math3.transform.TransformType

fun rms(points: List<Double>): Double {
    val squares = points.map {
        it * it
    }
    return squares.sum() / squares.size
}

fun fft(vals: DoubleArray): Array<Complex> {
    val fft = FastFourierTransformer(DftNormalization.STANDARD)
    return fft.transform(vals, TransformType.FORWARD)

}