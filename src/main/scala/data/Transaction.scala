package data

import com.fasterxml.jackson.annotation.JsonProperty

case class Transaction ( @JsonProperty("accountId") val accountId : String,
                        @JsonProperty val withDraw : Double,
                        @JsonProperty val deposit : Double,
                        @JsonProperty val balance : Double)
