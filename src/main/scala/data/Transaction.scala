package data

case class Transaction (accountId : String, withDraw : Double,
                        deposit : Double, balance : Double)
