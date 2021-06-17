package com.mat.inspector

import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class RemoteRepository {

    fun writeUnsignedParameter(ip: InetAddress, port: Int, offset: Int, value: Int): Boolean {
        return true
    }

    fun writeDoubleParameter(ip: InetAddress, port: Int, offset: Int, value: Double): Boolean {
        return true
    }

    fun readUnsignedParameter(ip: InetAddress, port: Int, offset: Int): Int {
        var value: Int
        Socket(ip, port).use {
            value = Connector.TcpReadUnsigned(it, offset)
        }
        return value
    }

    fun readDoubleParameter(ip: InetAddress, port: Int, offset: Int): Double {
        var value: Double
        Socket(ip, port).use {
            value = Connector.TcpReadDouble(it, offset)
        }
        return value
    }
}