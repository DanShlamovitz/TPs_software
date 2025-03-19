module Stack ( Stack, newS, freeCellsS, stackS, netS, holdsS, popS )
  where

import Palet
import Route

data Stack = Sta [ Palet ] Int deriving (Eq, Show)

newS :: Int -> Stack                      -- construye una Pila con la capacidad indicada 
freeCellsS :: Stack -> Int                -- responde la cantidad de celdas disponibles en la pila
stackS :: Stack -> Palet -> Stack         -- apila el palet indicado en la pila
netS :: Stack -> Int                      -- responde el peso neto de los paletes en la pila
holdsS :: Stack -> Palet -> Route -> Bool -- indica si la pila puede aceptar el palet considerando las ciudades en la ruta
popS :: Stack -> String -> Stack          -- quita del tope los paletes con destino en la ciudad indicada


newS n = Sta [] n

freeCellsS (Sta palets n) = n - length palets

netS (Sta palets n) = sum (map netP palets)

-- stackS (Sta palets n) (Pal dest weight) | freeCellsS (Sta palets n) == 0 = error "The stack is already full"
--                                         | netS (Sta palets n) + weight > 10 = error "The stack can't weigh more than 10 tons"
--                                         | holdsS (Sta palets n) (Pal dest weight) _ == False = error "The provided palet has a destination which occurs later on the route than the destination of the palet at the top of the stack"
--                                         | otherwise = Sta (Pal dest weight : palets) n


-- stackS (Sta palets n) newPalet | freeCellsS (Sta palets n) == 0 = error "\nThe stack is already full"
--                                | netS (Sta palets n) + netP(newPalet) > 10 = error "\nThe stack can't weigh more than 10 tons"
--                                | holdsS (Sta palets n) newPalet (newR(["a"]) == False = error "\nThe provided palet has a destination which occurs later on the route than the destination of the palet at the top of the stack"
--                                | otherwise = Sta (newPalet : palets) n

stackS (Sta palets n) newPalet | freeCellsS (Sta palets n) == 0 = error "The stack is already full"
                               | netS (Sta palets n) + netP(newPalet) > 10 = error "The stack can't weigh more than 10 tons"
                               | otherwise = Sta (palets ++ [newPalet]) n


-- holdsS (Stack palets n) (Pal dest weight) (Rou route) = map (inOrderR dest) (map destinationP palets)

-- holdsS (Sta palets n) (Pal dest weight) (Rou route) = inOrderR dest destinationP (last palets)

holdsS (Sta palets n) newPalet route = inOrderR route (destinationP newPalet) (destinationP (last palets))


-- popS (Sta palets n) city | city /= destinationP (last palets) = error "The provided city isn't the destination of the palet at the top of the stack"
--                          | otherwise = Sta (init palets) n

popS (Sta palets n) city | city /= destinationP (last palets) = Sta palets n
                         | otherwise = popS(Sta (init palets) n) city

