import java.util.*

const val MINUTES_IN_MILISECONDS = 60000

data class ParkingSpace(var vehicle: Vehicle,val parking: Parking){
    val checkInTime : Calendar = Calendar.getInstance()
    val parkedTime: Long
        get() = (Calendar.getInstance().timeInMillis - checkInTime.timeInMillis) / MINUTES_IN_MILISECONDS

    fun checkOutVehicle(plate : String, onSuccess : (Int) -> Unit, onError: ()->Unit){

        if(parking.vehicles.any { it.plate == plate }){

            val vehicleFound = parking.vehicles.first { it.plate == plate }
            val fee = calculateFee(vehicleFound.type,parkedTime.toInt(),vehicleFound.discountCard?.let { true } ?: false)
            parking.vehicles.remove(vehicleFound)
            with(parking) {
                total = total.copy(first = total.first + 1, second = total.second + fee) //not elegant at all IMO
            }

            onSuccess(fee)

        } else onError

    }

    fun calculateFee(type: VehicleType, parkedTime : Int, hasDiscountCard : Boolean): Int{
        var cost = type.fee

        if(parkedTime > 120){
            cost += 5* (parkedTime-120 ) / 15
        }

        if(hasDiscountCard) cost = (cost * 0.85).toInt()

        return cost
    }

}


data class Parking(val  vehicles: MutableSet<Vehicle>){
    val maxSize = 20
    var total : Pair<Int,Int> = Pair(0,0)

    fun addVehicle(vehicle: Vehicle) : Boolean{
        if(vehicles.size < maxSize){
            vehicles.add(vehicle)
            return true
        }
        return false
    }

    fun listVehicles(){
        vehicles.forEach{
            println(it.plate)
        }
    }

    fun getEarnings(){
        println("${total.first} vehicles have checked out and have earnings of \$${total.second}")
    }
}

data class Vehicle(
    val plate: String,
    val type: VehicleType,
    val checkInTime: Calendar,
    var discountCard : String? = null){


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

//1) Vehicles is defined as set because it is a data structure that contains elements
//   of the same type, none of which is the same.
//   In this case, there cannot be two vehicles in the parking lot that are the same

enum class VehicleType(val fee: Int ){
    CAR(20), MOTORBIKE(15), MINIBUS(25), BUS(30);
}

fun main(){
    val car = Vehicle("AA111AA", VehicleType.CAR, Calendar.getInstance(), "DISCOUNT_CARD_001")
    val motorBike = Vehicle("B222BBB", VehicleType.MOTORBIKE, Calendar.getInstance())
    val minibus = Vehicle("CC333CCC", VehicleType.MINIBUS, Calendar.getInstance())
    val bus = Vehicle("DD444DD", VehicleType.BUS, Calendar.getInstance(), "DISCOUNT_CARD_002")
    val parking = Parking(mutableSetOf(car, motorBike, minibus, bus))

    /*val car2 = Vehicle("AA111AA", VehicleType.CAR, Calendar.getInstance(), "DISCOUNT_CARD_001")
    val isCar2Inserted = parking.vehicles.add(car2)*/


    val vehicleList = arrayOf(
        Vehicle("BB33344", VehicleType.CAR, Calendar.getInstance(), "DISCOUNT_CARD_001"),
        Vehicle("3342352", VehicleType.MINIBUS, Calendar.getInstance(), "DISCOUNT_CARD_001"),
        Vehicle("6577878", VehicleType.BUS, Calendar.getInstance(), "DISCOUNT_CARD_001"),
        Vehicle("9877677", VehicleType.MOTORBIKE, Calendar.getInstance(), "DISCOUNT_CARD_001"),
        Vehicle("DFFGG54", VehicleType.CAR, Calendar.getInstance(), "DISCOUNT_CARD_001"),
        Vehicle("JYGFRTY", VehicleType.MINIBUS, Calendar.getInstance()),
        Vehicle("FGTTY56", VehicleType.BUS, Calendar.getInstance(), "DISCOUNT_CARD_001"),
        Vehicle("HYTT564", VehicleType.MOTORBIKE, Calendar.getInstance(), "DISCOUNT_CARD_001"),
        Vehicle("FTTYGT5", VehicleType.CAR, Calendar.getInstance()),
        Vehicle("UTTRHFR", VehicleType.MINIBUS, Calendar.getInstance()),
        Vehicle("3332456", VehicleType.BUS, Calendar.getInstance(), "DISCOUNT_CARD_001"),
        Vehicle("DFFRT45", VehicleType.MOTORBIKE, Calendar.getInstance(), "DISCOUNT_CARD_001"),
        Vehicle("DFGR345", VehicleType.CAR, Calendar.getInstance(), "DISCOUNT_CARD_001"),
        Vehicle("DFGFG45", VehicleType.MINIBUS, Calendar.getInstance()),
        Vehicle("DFGG434", VehicleType.BUS, Calendar.getInstance()),
        Vehicle("DFGRE45", VehicleType.MOTORBIKE, Calendar.getInstance(), "DISCOUNT_CARD_001"),
        Vehicle("HR324AS", VehicleType.MOTORBIKE, Calendar.getInstance())
    )

    vehicleList.forEach{
        if(parking.addVehicle(it)) println("Welcome to AlkeParking!")
        else println("Sorry, the check-in failed")
    }

    val parkingSpace = ParkingSpace(car,parking)

    parking.listVehicles()

    parkingSpace.checkOutVehicle("B222BBB",{println("Your fee is \$$it. Come back soon.")},{println("Sorry, the check-out failed")})
    parkingSpace.checkOutVehicle("DFGG434",{println("Your fee is \$$it. Come back soon.")},{println("Sorry, the check-out failed")})
    parkingSpace.checkOutVehicle("DFGFG45",{println("Your fee is \$$it. Come back soon.")},{println("Sorry, the check-out failed")})

    parking.listVehicles()
    parking.getEarnings()

}
