module Route ( Route, newR, inOrderR, inRouteR, getRoute )
   where

data Route = Rou [ String ] deriving (Eq, Show)

newR :: [ String ] -> Route -- construye una ruta según una lista de ciudades
inOrderR :: Route -> String -> String -> Bool -- indica si la primer ciudad consultada está antes que la segunda ciudad en la ruta
inRouteR :: Route -> String -> Bool -- indica si la ciudad consultada está en la ruta
getRoute :: Route -> [ String ] -- devuelve la lista de ciudades de la ruta

getRoute (Rou cities) = cities

newR cities 
   | null(cities) == True = error "The city list has to contain at least one city"
   | otherwise = Rou cities

inRouteR (Rou []) _ = False
inRouteR (Rou (x:xs)) c1   
   | x == c1   = True
   | otherwise = inRouteR (Rou xs) c1

inOrderR (Rou []) _ _ = error "The route has to contain at least two cities"
inOrderR (Rou route) c1 c2 
   | inRouteR (Rou route) c1 == False = error ("The city " ++ c1 ++ " isn't a part of the provided route")
   | inRouteR (Rou route) c2 == False = error ("The city " ++ c2 ++ " isn't a part of the provided route")
   | route !! 0 == c1 = True
   | route !! 0 == c2 = False
   | otherwise = inOrderR (Rou (drop 1 route)) c1 c2