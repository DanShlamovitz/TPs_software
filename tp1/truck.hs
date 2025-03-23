module Truck ( Truck, newT, freeCellsT, loadT, unloadT, netT )
  where

import Palet
import Stack
import Route


data Truck = Tru [ Stack ] Route deriving (Eq, Show)

newT :: Int -> Int -> Route -> Truck  -- construye un camion según una cantidad de bahias, la altura de las mismas y una ruta
freeCellsT :: Truck -> Int            -- responde la celdas disponibles en el camion
loadT :: Truck -> Palet -> Truck      -- carga un palet en el camion
unloadT :: Truck -> String -> Truck   -- responde un camion al que se le han descargado los paletes que podían descargarse en la ciudad
netT :: Truck -> Int                  -- responde el peso neto en toneladas de los paletes en el camion

-- Funciones auxiliares:
getIndexBools :: [Bool] -> Int -> Int -- dada una lista de booleanos, indica el índice del primer True que aparece en la lista (utiliza un entero para realizar la recursión y poder iterar la lista)
removeAtIndex :: [ a ] -> [ a ] -> Int -> Int -> [ a ] -- dada una lista y un índice, elimina el elemento que se encuentra en el índice determinado de la lista (usa otra lista y un entero para la recursión)

newT n h route = Tru (replicate n (newS h)) route

freeCellsT (Tru stacks route) = sum (map freeCellsS stacks)

getIndexBools bools i | i == length bools = error "This boolean list has no elements with a value of True"
                      | bools !! i == True = i
                      | otherwise = getIndexBools bools (i+1)

removeAtIndex [] _ _ _ = []
removeAtIndex stacks newStacks idx i | i /= idx = removeAtIndex stacks (newStacks ++ [stacks !! i]) idx (i+1)
                                     | otherwise = removeAtIndex stacks newStacks idx (i+1)
                      
--necesito ver a donde va el palet que quiero agregar y a donde va el ultimo palet de la pila al que quiero agregar. 
-- loadT (Tru stacks route) palet = Tru (stackS (stacks !! getIndexBools (map (holdsS (stacks palet route)) 0) palet : (removeAtIndex stacks [] getIndexBools(map (holdsS (stacks palet route)) 0) 0) route

loadT (Tru stacks route) palet = Tru (removeAtIndex stacks [] (getIndexBools (map (\s -> holdsS s palet route) stacks) 0) 0 ++ [stackS (stacks !! getIndexBools (map (\s -> holdsS s palet route) stacks) 0) palet]) route


-- necesito ver qué stacks tienen palets cuyos
unloadT (Tru stacks route) city | elem city (getRoute route) == False = error "The provided city isn't a part of the provided truck's route"
                                | otherwise = Tru (map (\s -> popS s city) stacks) route

netT (Tru stacks route) = sum (map netS stacks)

