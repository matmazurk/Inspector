package com.mat.inspector

import org.junit.Test

class SignalAnalysisTest {

    @Test
    fun `test get signal data`() {
        val fs = 32.0
        var samples = doubleArrayOf(0.0,8.4096,14.245,15.761,12.591,5.8383,-2.3206,-9.368,-13.262,-13.06,-9.1503,-3.0291,3.2677,7.8684,9.6524,8.5259,5.3199,1.379,-1.9777,-3.9151,-4.2944,-3.5841,-2.5378,-1.7937,-1.5754,-1.6216,-1.3662,-0.28326,1.7666,4.3526,6.5548,7.2983,5.8171,2.059,-3.1492,-8.2726,-11.545,-11.597,-8.0038,-1.5424,5.9629,12.184,15.047,13.451,7.6882,-0.59353,-8.8855,-14.62,-16.004,-12.611,-5.516,3.0598,10.479,14.542,14.173,9.7188,2.7668,-4.4307,-9.7192,-11.722,-10.21,-6.068,-0.87918,3.6727,6.3755,6.8257,5.4338,3.1308,0.92031,-0.53269,-1.1125,-1.1933,-1.3566,-2.0257,-3.1904,-4.3532,-4.7307,-3.6316,-0.84878,3.1146,7.1047,9.671,9.6009,6.4359,0.76002,-5.8749,-11.395,-13.884,-12.269,-6.7543,1.156,9.0905,14.559,15.772,12.252,5.0179,-3.7254,-11.295,-15.397,-14.855,-9.9696,-2.3807,5.504,11.309,13.443,11.561,6.5811,0.28894,-5.3123,-8.6713,-9.1403,-7.0711,-3.5573,0.054209,2.676,3.8254,3.686,2.8909,2.1459,1.8655,1.9855,2.0269,1.381,-0.32033,-2.9191,-5.6758,-7.4913,-7.3355,-4.7188,-0.0074098,5.5666,10.241,12.306,10.739,5.6468,-1.6578,-9.0077,-14.059,-15.072,-11.532,-4.3715,4.2847,11.783,15.797,15.088,9.9045,1.8943,-6.4449,-12.583,-14.766,-12.544,-6.8519,0.36545,6.8409,10.731,11.169,8.4507,3.8091,-1.0967,-4.7879,-6.4351,-6.0373,-4.2741,-2.1232,-0.4167,0.47808,0.73114,0.86472,1.4089,2.5632,4.0244,5.0701,4.8715,2.9105,-0.6839,-5.0383,-8.7499,-10.358,-8.9121,-4.4091,2.0716,8.6292,13.129,13.929,10.483,3.6087,-4.7079,-11.917,-15.728,-14.871,-9.5358,-1.3346,7.2141,13.498,15.649,13.133,6.8818,-1.0542,-8.205,-12.489,-12.854,-9.5361,-3.8852,2.1701,6.8014,8.8624,8.1779,5.4667,1.9604,-1.1114,-2.9631,-3.4604,-3.0369,-2.382,-2.0544,-2.1972,-2.4788,-2.2847,-1.0704,1.2859,4.2988,6.9627,8.1006,6.849,3.0876,-2.3741,-7.9571,-11.794,-12.378,-9.1432,-2.7647,4.9699,11.686,15.19,14.214,8.885,0.73159,-7.7768,-14.018,-16.067,-13.319,-6.6808,1.7453,9.355,13.89,14.145,10.301,3.7919,-3.2344,-8.6514,-11.033,-10.046,-6.437,-1.6664,2.67,5.3936)
        var sigData = getSignalData(samples, fs)
        assert(inMargin(2.5, sigData.frequency))
        assert(inMargin(9.12, sigData.amplitude!!))

        samples = doubleArrayOf(0.0,5.4057,-1.0551,9.5504,6.0681,5.1231,13.101,3.7811,9.3204,8.5736,0.65345,8.2228,-0.92886,-1.6491,1.6147,-9.3175,-2.7992,-6.895,-11.574,-3.2979,-11.881,-6.8692,-3.3562,-9.698,1.2779,-2.4179,-1.1966,7.662,0.14077,8.4975,8.8514,3.8189,13.194,5.0562,6.6029,9.7477,-0.62809,6.0903,0.24251,-4.7037,1.4826,-9.3204,-5.5094,-5.1681,-12.963,-3.7248,-9.7555,-8.619,-1.1595,-8.7568,0.65985,0.87512,-1.9492,8.9062,2.2287,6.8943,11.227,3.2808,12.145,6.817,3.8978,10.046,-0.94006,3.1788,1.4618,-7.0763,0.37668,-8.3912,-8.3501,-3.8384,-13.241,-4.9675,-7.1122,-9.9114,0.20327,-6.755,-0.48385,3.9867,-1.9089,9.0589,4.9288,5.1912,12.78,3.6504,10.155,8.6332,1.6572,9.2619,-0.39077,-0.098595,2.2799,-8.4657,-1.6467,-6.8674,-10.841,-3.2468,-12.368,-6.7381,-4.4221,-10.36,0.60269,-3.9291,-1.7232,6.468,-0.897,8.2552,7.8183,3.8396,13.242,4.857,7.5942,10.041,0.21777,7.3975,0.72288,-3.2574,2.3325,-8.7668,-4.3283,-5.1927,-12.553,-3.5578,-10.519,-8.6159,-2.1446,-9.7364,0.12286,-0.67775,-2.6054,7.9976,1.0552,6.8147,10.416,3.1959,12.547,6.6326,4.9274,10.64,-0.26715,4.6662,1.9797,-5.8391,1.4183,-8.0903,-7.2575,-3.8225,-13.197,-4.7251,-8.0474,-10.135,-0.63345,-8.0155,-0.95843,2.5181,-2.7517,8.4456,3.7099,5.1726,12.283,3.4475,10.844,8.5671,2.6201,10.178,0.14269,1.4512,2.9243,-7.5037,-0.45628,-6.7366,-9.955,-3.1285,-12.682,-6.5008,-5.4119,-10.883,-0.065196,-5.3876,-2.23,5.1921,-1.9387,7.8974,6.6699,3.7875,13.107,4.5721,8.4702,10.194,1.0422,8.6068,1.1894,-1.7716,3.1647,-8.0963,-3.0757,-5.1313,-11.97,-3.3196,-11.131,-8.4866,-3.0821,-10.586,-0.40467,-2.2191,-3.2353,6.9858,-0.14789,6.6336,9.4583,3.0448,12.773,6.3428,5.8741,11.089,0.39299,6.0907,2.4729,-4.5291,2.4562,-7.6773,-6.0572,-3.7349,-12.972,-4.3983,-8.8614,-10.217,-1.4426,-9.1693,-1.4146,1.0205,-3.57,7.7206,2.4279,5.0692,11.615,3.1748,11.379,8.3745,3.5289,10.959,0.66192,2.9787,3.5369,-6.446,0.75521,-6.5065,-8.928,-2.9452,-12.82,-6.1591,-6.3124,-11.258,-0.71493,-6.7732,-2.7073,3.8526)
        sigData = getSignalData(samples, fs)
        assert(inMargin(1.25, sigData.frequency))
        assert(inMargin(8.3, sigData.amplitude!!))
    }

    private fun inMargin(expected: Double, actual: Double): Boolean {
        return expected * 1.05 > actual && actual > expected * 0.95
    }
}