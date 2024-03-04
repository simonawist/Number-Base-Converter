package converter

import java.math.BigInteger
import java.math.BigDecimal
import java.math.RoundingMode

fun main() {
    val digits = "0123456789abcdefghijklmnopqrstuvwxyz"
    var sourceBase = 0
    var targetBase = 0
    var number = ""
    
    while (true) {
        println("Enter two numbers in format: {source base} {target base} (To quit type /exit)")
        val option = readln()
        
        if (option == "/exit") {
            break
        } else {
            val source = option.split(" ")
            sourceBase = source[0].toInt()
            targetBase = source[1].toInt()
        }

        while (true) {
            println("Enter number in base $sourceBase to convert to base $targetBase (To go back type /back)")
            number = readln()
            
            if (number == "/back") {
                break
            } 
            
            // split the number into integer part and decimal part
            val numberParts = number.split(".")
            val integerPart = numberParts[0]
            val decimalPart = if (numberParts.size > 1) numberParts[1] else ""

            // convert the integer part from source base to decimal
            var integerPartInDecimal = BigInteger.valueOf(0)
            for (i in integerPart.indices) {
                integerPartInDecimal = integerPartInDecimal.add(BigInteger.valueOf(digits.indexOf(integerPart[i].lowercase()).toLong())
                    .multiply(BigInteger.valueOf(sourceBase.toLong()).pow(integerPart.length - 1 - i)))
            }

            // convert the decimal part from source base to decimal
            var decimalPartInDecimal = BigDecimal.valueOf(0.0)
            for (i in decimalPart.indices) {
                decimalPartInDecimal = decimalPartInDecimal.add(BigDecimal.valueOf(digits.indexOf(decimalPart[i].lowercase()).toDouble())
                    .divide(BigDecimal.valueOf(sourceBase.toDouble()).pow(i + 1), 50, RoundingMode.HALF_UP))
            }

            // convert the integer part and decimal part from decimal to target base
            var integerPartInTargetBase = ""
            var n = integerPartInDecimal
            while (n > BigInteger.ZERO) {
                integerPartInTargetBase = digits[n.mod(BigInteger.valueOf(targetBase.toLong())).toInt()] + integerPartInTargetBase
                n /= targetBase.toBigInteger()
            }

            // convert the decimal part from decimal to target base
            var decimalPartInTargetBase = ""
            var m = decimalPartInDecimal
            for (j in 0 until 5) {
                m *= targetBase.toBigDecimal()
                val digit = m.toBigInteger()
                decimalPartInTargetBase += digits[digit.toInt()]
                m -= digit.toBigDecimal()
            }
            
            // combine both parts and get the final result
            var finalResult = ""
            if (decimalPartInTargetBase == "" || decimalPart == "") {
                finalResult = integerPartInTargetBase
                println("Conversion result: $finalResult")
            } else {
                finalResult = integerPartInTargetBase + "." + decimalPartInTargetBase
                println("Conversion result: $finalResult")
            }
        }
    }
}
