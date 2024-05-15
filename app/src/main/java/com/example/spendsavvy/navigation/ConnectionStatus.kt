package com.example.spendsavvy.navigation

sealed class ConnectionStatus{
    object Available : ConnectionStatus()
    object UnAvailable : ConnectionStatus()
}
