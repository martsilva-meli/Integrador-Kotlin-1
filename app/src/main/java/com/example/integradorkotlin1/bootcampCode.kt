import java.util.*

data class ParkingSpace(var vehicle: Vehicle){
    var CheckInTime : Calendar = Calendar.getInstance()
}

//A Set is used in order to avoid having duplicated Vehicles
data class Parking(val  vehicles: MutableSet<Vehicle>)

data class Vehicle(val plate: String, val type: VehicleType){

    val fee : Int = when(type){
        VehicleType.CAR -> 20
        VehicleType.MOTORBIKE -> 15
        VehicleType.MINIBUS -> 25
        VehicleType.BUS -> 30
    }

    var discountCard : String? = null
    //Function states that two Vehicles are equal if their plates are equal
    override fun equals(other: Any?) : Boolean {
        if (other is Vehicle){
            return this.plate == other.plate
        }

        return super.equals(other)
    }
    //Function states that the hashCode (Used internally in search functions
    // in sets and arrays) is the hashCode of the plate
    override fun hashCode() : Int = this.plate.hashCode()
}

enum class VehicleType{
    CAR,MOTORBIKE,MINIBUS,BUS
}