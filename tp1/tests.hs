module Test where

-- import Truck
import Palet
import Route
import Stack
import Distribution.Parsec (parsecLeadingCommaList)
import Control.Exception
import System.IO.Unsafe
import Foreign (free)

-- Función de prueba
testF :: Show a => a -> Bool
testF action = unsafePerformIO $ do
    result <- tryJust isException (evaluate action)
    return $ case result of
        Left _ -> True
        Right _ -> False
  where
    isException :: SomeException -> Maybe ()
    isException _ = Just ()

-- Variables
ba = "Buenos Aires"
mdq = "Mar del Plata"
bhi = "Bahia Blanca"
tuc = "Tucuman"
ush = "Ushuaia"
sla = "Salta"
rga = "Rio Gallegos"

-- Palets
p1 = newP ba 5
testPalet = [
    testF (newP "" 5),
    testF (newP ba (-1)),
    testF (newP ba 3),
    testF (destinationP p1),
    testF (netP p1)
    ]

-- Pruebas de Route
ruta1 = newR [tuc, sla, ba, mdq, bhi, rga, ush]
testRoute = [
    testF (newR []),
    testF (newR [ba, mdq]),
    testF (inRouteR ruta1 ba == True),
    testF (inRouteR ruta1 "Cordoba" == False),
    testF (inOrderR ruta1 ba bhi == True), -- fijarse el caso en donde si estan en oden devuelve false no tira error
    testF (inOrderR ruta1 mdq ba == True)
    ]

-- Pruebas de stack



