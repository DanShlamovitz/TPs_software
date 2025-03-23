module Test where

import Truck
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
p2 = newP mdq 4
p3 = newP bhi 3
p4 = newP tuc 1
p5 = newP ush 2
pError = newP ba 1000

testPalet = [
    testF (newP "" 5),
    testF (newP ba (-1)),
    not (testF (newP ba 3)),
    not(testF (destinationP p1)),
    not (testF (netP p1))
    ]

-- Pruebas de Route
ruta1 = newR [tuc, sla, ba, mdq, bhi, rga, ush]
testRoute = [
    testF (newR []),
    not (testF (newR [ba, mdq])),

    inRouteR ruta1 "Cordoba" == False,
    not (inRouteR ruta1 "Cordoba" == True),

    inRouteR ruta1 ba == True,
    not(inRouteR ruta1 ba == False),

    inOrderR ruta1 ba mdq == True,
    not(inOrderR ruta1 ba mdq == False),

    not(inOrderR ruta1 mdq ba == True),
    inOrderR ruta1 mdq ba == False
    ]

-- Stack
-- ruta1 = newR [tuc, sla, ba, mdq, bhi, rga, ush]
-- p1 = newP ba 5
-- p2 = newP mdq 4
-- p3 = newP bhi 3
-- p4 = newP tuc 1
-- p5 = newP ush 2
-- pError = newP ba 1000

s1 = newS 3
s2 = stackS s1 p1
s3 = stackS s2 p2


testStack = [
    testF (newS 0),
    testF (newS (-1)),
    not (testF (newS 3)),
    
    freeCellsS s1 == 3,
    not (freeCellsS s1 == 0),

    netS s1 == 0,
    netS s2 == 5,
    netS s3 == 9,
    
    testF(stackS s1 pError),
    not (testF(stackS s1 p1)),

    holdsS s3 p5 ruta1 == False,
    holdsS s3 p4 ruta1 == True,

    popS s3 ba == s3,
    popS s3 mdq == s2
    ]

    
-- Truck

-- ruta1 = newR [tuc, sla, ba, mdq, bhi, rga, ush]
-- p1 = newP ba 5
-- p2 = newP mdq 4
-- p3 = newP bhi 3
-- p4 = newP tuc 1
-- p5 = newP ush 2
-- pError = newP ba 1000

-- s1 = newS 3
-- s2 = stackS s1 p1
-- s3 = stackS s2 p2

t1 = newT 3 3 ruta1
t2 = loadT t1 p1
t3 = loadT t2 p2

testTruck = [
    testF (newT 0 0 ruta1),
    testF (newT (-1) 0 ruta1),
    not (testF (newT 3 3 ruta1)),

    netT t1 == 5
    -- netT t2 == 9
    

    -- testF (newT (-1) 0 ruta1),
    -- not (testF (newT 3 3 ruta1)),

    -- freeCellsT t1 == 9,
    -- not (freeCellsT t1 == 0),

    -- netT t1 == 0,
    -- netT t2 == 5,
    -- netT t3 == 9,

    -- testF(loadT t1 pError),
    -- not (testF(loadT t1 p1)),

    -- unloadT t3 "Cordoba" == t3,
    -- unloadT t3 ba == t2,
    ]