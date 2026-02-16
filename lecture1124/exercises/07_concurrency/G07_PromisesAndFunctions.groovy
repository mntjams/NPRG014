import groovyx.gpars.dataflow.Dataflow
import static groovyx.gpars.dataflow.Dataflow.*
import groovyx.gpars.dataflow.DataflowVariable
import groovyx.gpars.dataflow.DataflowQueue
import groovyx.gpars.dataflow.DataflowBroadcast
import groovyx.gpars.group.NonDaemonPGroup
import groovyx.gpars.group.PGroup

def checkEngine(elTest) {
    println "Checking the engine"
    if (elTest.get()) {
        sleep 3000
        return true
    }
    else {
        println "No electricity in engine, check ends"
        return false
    }
}

def checkTyres() {
    println "Preparing the tyres"
    sleep 4000
    return true
}

def checkElectricity() {
    println "Checking electricity"
    sleep 3000
    return System.currentTimeMillis() % 3 == 0 ? true : false
}

def checkRadar(elTest) {
    println "Turning radar on"
    sleep 1000
    if (elTest.get()) {
        println "Electricity ok, continue with the radar"
        sleep 500
        return System.currentTimeMillis() % 2 == 0 ? true : false
    } else {
        println "Electricity failed, do not continue with radar"    
        return false
    }
}

def electricityCheck = task { checkElectricity() }
def engineCheck = task { checkEngine(electricityCheck) }
def tyrePressure = task { checkTyres() }
def radarOn = task { checkRadar(electricityCheck) }

engineCheck.then {println "Engine ok"}
tyrePressure.then {println "Tyres ok"}
radarOn.then {println "Radar ${it ? 'ok' : 'failed'}"}

boolean ready = [engineCheck, tyrePressure, radarOn].every {it.val}
if(ready) {
    println 'Taking off'
} else {
    println 'Staying on the ground today'
}
// TASK Currently, checkElectricity() is local to the checkRadar() function. Modify the code is a way
// that allows both checkRadar() and checkEngine() to first have the electricity checked and only after
// a successful result of electricity check the radar or engine can be turned on, respectively.
// Make sure that electricity is checked only once.